import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Translates a subset of Java (the Biogenesis simulation classes) to Rust.
 *
 * Usage: J2R <java src root>[:<more roots>] <config> <out dir> [jars for type resolution...]
 *
 * The config lists the classes to translate:
 *   class biogenesis.Gene                       instance fields, constructors, methods
 *   skip biogenesis.GeneticCode.draw(java.awt.Graphics, int, int)
 *   source java.lang.StrictMath                 parse this file too, for its statics
 *   static java.lang.StrictMath.sin(double)     translate this static method even if unused
 *   stub biogenesis.VisibleWorldInterface       a GUI type: values are rt::Stub, calls do nothing
 *   stub biogenesis.Utils.addRepaintWorldChangeListener(biogenesis.RepaintWorldChangeListener)
 *                                               calls to this method do nothing
 *   native biogenesis.World.getDistinctCladeIDCount(int)
 *                                               body is hand-written in core/src/native/
 * Static fields and methods of any class are translated only when the
 * translated code uses them (transitively), so Utils comes in on demand.
 *
 * The output keeps Java's semantics exactly (see rust/README.md):
 * objects are rt::JRef (shared, nullable), int arithmetic wraps, evaluation
 * order is Java's. Constructs it doesn't know are reported with their line and
 * replaced by unimplemented!(), so the report is the list of what's missing.
 */
public class J2R {

	// ---------------------------------------------------------------- types

	enum K { INT, LONG, DOUBLE, FLOAT, BOOL, CHAR, BYTE, SHORT, VOID, STRING, COLOR, RANDOM, CLASS, ARRAY, LIST, NULL, OBJECT, GEOM,
		COLL, HSET, ITER, FN, STUB }

	/** A Java type, as far as the translation cares. */
	static final class JT {
		final K k;
		final String cls;  // CLASS, FN, STUB: qualified name; GEOM: the rt struct (see GEO)
		final JT elem;     // ARRAY, LIST, COLL, HSET, ITER
		JT(K k, String cls, JT elem) { this.k = k; this.cls = cls; this.elem = elem; }
		static JT of(K k) { return new JT(k, null, null); }
		boolean prim() { return k.ordinal() <= K.SHORT.ordinal(); }
		boolean numeric() { return prim() && k != K.BOOL; }
		boolean integral() { return k == K.INT || k == K.LONG || k == K.CHAR || k == K.BYTE || k == K.SHORT; }
		/** Copied by value in Rust (no clone needed). */
		boolean copy() { return prim() || k == K.COLOR || k == K.STUB; }
		boolean ref() { return !prim() && k != K.VOID; }
		@Override public boolean equals(Object o) {
			if (!(o instanceof JT)) return false;
			JT t = (JT) o;
			return k == t.k && java.util.Objects.equals(cls, t.cls) && java.util.Objects.equals(elem, t.elem);
		}
		@Override public int hashCode() { return k.hashCode(); }
		@Override public String toString() { return k + (cls != null ? "(" + cls + ")" : "") + (elem != null ? "[" + elem + "]" : ""); }
	}

	static final JT INT = JT.of(K.INT), LONG = JT.of(K.LONG), DOUBLE = JT.of(K.DOUBLE), FLOAT = JT.of(K.FLOAT),
			BOOL = JT.of(K.BOOL), CHAR = JT.of(K.CHAR), VOID = JT.of(K.VOID), STRING = JT.of(K.STRING),
			COLOR = JT.of(K.COLOR), NULLT = JT.of(K.NULL), OBJECT = JT.of(K.OBJECT);

	/** Unsupported construct: reported, translation continues. */
	static final class Unsupported extends RuntimeException {
		Unsupported(String m) { super(m); }
	}

	JT jt(ResolvedType t) {
		if (t.isPrimitive()) {
			switch (t.asPrimitive().describe()) {
				case "int": return INT;
				case "long": return LONG;
				case "double": return DOUBLE;
				case "float": return FLOAT;
				case "boolean": return BOOL;
				case "char": return CHAR;
				case "byte": return JT.of(K.BYTE);
				case "short": return JT.of(K.SHORT);
			}
		}
		if (t.isVoid()) return VOID;
		if (t.isNull()) return NULLT;
		if (t.isArray()) return new JT(K.ARRAY, null, jt(t.asArrayType().getComponentType()));
		if (t.isReferenceType()) {
			String q = t.asReferenceType().getQualifiedName();
			switch (q) {
				case "java.lang.String": return STRING;
				case "java.awt.Color": return COLOR;
				case "java.util.Random": return JT.of(K.RANDOM);
				case "java.lang.Object": return OBJECT;
				case "java.util.List":
				case "java.util.ArrayList": return new JT(K.LIST, null, typeArg(t, q));
				case "java.util.Collection":
				case "java.util.Set": return new JT(K.COLL, null, typeArg(t, q));
				case "java.util.HashSet": return new JT(K.HSET, null, typeArg(t, q));
				case "java.util.Iterator": return new JT(K.ITER, null, typeArg(t, q));
			}
			if (stubTypes.contains(q)) return new JT(K.STUB, q, null);
			if (classes.containsKey(q)) return new JT(K.CLASS, q, null);
			if (sam(q) != null) return new JT(K.FN, q, null);
			if (GEO_TYPES.containsKey(q)) return new JT(K.GEOM, GEO_TYPES.get(q), null);
			throw new Unsupported("type " + q);
		}
		throw new Unsupported("type " + t.describe());
	}

	/** The element type of List<E> etc. Boxed elements are their primitive (never null here). */
	JT typeArg(ResolvedType t, String q) {
		List<ResolvedType> tp = t.asReferenceType().typeParametersValues();
		if (tp.size() != 1) throw new Unsupported("raw " + q);
		ResolvedType a = tp.get(0);
		if (a.isReferenceType()) {
			switch (a.asReferenceType().getQualifiedName()) {
				case "java.lang.Double": return DOUBLE;
				case "java.lang.Integer": return INT;
				case "java.lang.Long": return LONG;
				case "java.lang.Boolean": return BOOL;
				default: break;
			}
		}
		return jt(a);
	}

	/** The single abstract method of a functional interface from the sources, or null. */
	MethodDeclaration sam(String q) {
		ClassOrInterfaceDeclaration c = allTypes.get(q);
		if (c == null || !c.isInterface()) return null;
		MethodDeclaration found = null;
		for (MethodDeclaration m : c.getMethods()) {
			if (m.isStatic() || m.isDefault()) continue;
			if (found != null) return null;
			found = m;
		}
		return found;
	}

	/** dyn Fn(params) -> ret of a functional interface. */
	String fnType(String q) {
		MethodDeclaration m = sam(q);
		StringBuilder s = new StringBuilder("dyn Fn(");
		for (int i = 0; i < m.getParameters().size(); i++) {
			if (i > 0) s.append(", ");
			s.append(rust(jt(m.getParameter(i).getType().resolve())));
		}
		s.append(")");
		JT r = jt(m.getType().resolve());
		if (r.k != K.VOID) s.append(" -> ").append(rust(r));
		return s.toString();
	}

	String rust(JT t) {
		switch (t.k) {
			case INT: return "i32";
			case LONG: return "i64";
			case DOUBLE: return "f64";
			case FLOAT: return "f32";
			case BOOL: return "bool";
			case CHAR: return "u16";
			case BYTE: return "i8";
			case SHORT: return "i16";
			case VOID: return "()";
			case STRING: return "JStr";
			case COLOR: return "Color";
			case RANDOM: return "JRef<Random>";
			case CLASS: return "JRef<" + simple(t.cls) + ">";
			case GEOM: {
				Geo g = GEO.get(t.cls);
				return g.trait != null ? "JRef<dyn " + g.trait + ">" : "JRef<" + t.cls + ">";
			}
			case ARRAY: return "JArray<" + rust(t.elem) + ">";
			case LIST: return "JList<" + rust(t.elem) + ">";
			case COLL: return "JCollection<" + rust(t.elem) + ">";
			case HSET: return "JRef<JHashSet<" + rust(t.elem) + ">>";
			case ITER: return "JIter<" + rust(t.elem) + ">";
			case FN: return "JFn<" + fnType(t.cls) + ">";
			case STUB: return "Stub";
			default: throw new Unsupported("rust type for " + t);
		}
	}

	String defaultValue(JT t) {
		switch (t.k) {
			case INT: case LONG: case CHAR: case BYTE: case SHORT: return "0";
			case DOUBLE: return "0f64";
			case FLOAT: return "0f32";
			case BOOL: return "false";
			case STRING: return "JStr::NULL";
			case COLOR: return "Color::NULL";
			case STUB: return "Stub::NULL";
			case FN: return "JFn::null()";
			default: return "JRef::null()";
		}
	}

	// ---------------------------------------------------------------- java.awt geometry

	/**
	 * The JDK geometry classes are hand-written structs in rt/geom.rs. The ones
	 * the simulation extends (Rectangle, Line2D.Double) are used through a trait,
	 * so a variable of that type is a JRef<dyn RectangleT> and can hold a
	 * subclass; a translated subclass keeps its base part in `__super`.
	 */
	static final class Geo {
		final String trait, acc, upcast; // null for the ones nobody extends
		final List<String> fields;       // instance fields, sorted (dump order)
		Geo(String trait, String acc, String upcast, String... fields) {
			this.trait = trait; this.acc = acc; this.upcast = upcast; this.fields = java.util.Arrays.asList(fields);
		}
	}

	static final Map<String, Geo> GEO = new HashMap<>();
	/** Java types of variables -> rt struct. */
	static final Map<String, String> GEO_TYPES = new HashMap<>();
	/** Classes declaring the fields and methods -> rt struct. */
	static final Map<String, String> GEO_OWNERS = new HashMap<>();
	static {
		GEO.put("Rectangle", new Geo("RectangleT", "rect", "as_rectangle", "height", "width", "x", "y"));
		GEO.put("Line2D", new Geo("Line2DT", "line", "as_line2d", "x1", "x2", "y1", "y2"));
		GEO.put("Point", new Geo(null, null, null, "x", "y"));
		GEO.put("Point2D", new Geo(null, null, null, "x", "y"));
		GEO_TYPES.put("java.awt.Rectangle", "Rectangle");
		GEO_TYPES.put("java.awt.geom.Line2D", "Line2D"); // only ever a Line2D.Double here
		GEO_TYPES.put("java.awt.geom.Line2D.Double", "Line2D");
		GEO_TYPES.put("java.awt.Point", "Point");
		GEO_TYPES.put("java.awt.geom.Point2D.Double", "Point2D");
		GEO_OWNERS.putAll(GEO_TYPES);
		GEO_OWNERS.put("java.awt.geom.Rectangle2D", "Rectangle");
		GEO_OWNERS.put("java.awt.geom.RectangularShape", "Rectangle");
		GEO_OWNERS.put("java.awt.geom.Point2D", "Point2D");
	}

	/** A JDK geometry method: rt method name, whether it changes the object, return type. */
	static final class GeoMethod {
		final String rust; final boolean mut; final JT ret;
		GeoMethod(String rust, boolean mut, JT ret) { this.rust = rust; this.mut = mut; this.ret = ret; }
	}

	static final Map<String, GeoMethod> GEO_METHODS = new HashMap<>();
	static {
		GEO_METHODS.put("setBounds(int,int,int,int)", new GeoMethod("setBounds", true, VOID));
		GEO_METHODS.put("setBounds(java.awt.Rectangle)", new GeoMethod("setBounds_rect", true, VOID));
		GEO_METHODS.put("intersects(java.awt.Rectangle)", new GeoMethod("intersects", false, BOOL));
		GEO_METHODS.put("contains(int,int)", new GeoMethod("contains", false, BOOL));
		GEO_METHODS.put("intersectsLine(java.awt.geom.Line2D)", new GeoMethod("intersectsLine", false, BOOL));
		GEO_METHODS.put("intersectsLine(double,double,double,double)", new GeoMethod("intersectsLine_xy", false, BOOL));
		GEO_METHODS.put("isEmpty()", new GeoMethod("isEmpty", false, BOOL));
		GEO_METHODS.put("getX()", new GeoMethod("getX", false, DOUBLE));
		GEO_METHODS.put("getY()", new GeoMethod("getY", false, DOUBLE));
		GEO_METHODS.put("getWidth()", new GeoMethod("getWidth", false, DOUBLE));
		GEO_METHODS.put("getHeight()", new GeoMethod("getHeight", false, DOUBLE));
		GEO_METHODS.put("getMinX()", new GeoMethod("getMinX", false, DOUBLE));
		GEO_METHODS.put("getMinY()", new GeoMethod("getMinY", false, DOUBLE));
		GEO_METHODS.put("getMaxX()", new GeoMethod("getMaxX", false, DOUBLE));
		GEO_METHODS.put("getMaxY()", new GeoMethod("getMaxY", false, DOUBLE));
		GEO_METHODS.put("setLine(double,double,double,double)", new GeoMethod("setLine", true, VOID));
		GEO_METHODS.put("getX1()", new GeoMethod("getX1", false, DOUBLE));
		GEO_METHODS.put("getY1()", new GeoMethod("getY1", false, DOUBLE));
		GEO_METHODS.put("getX2()", new GeoMethod("getX2", false, DOUBLE));
		GEO_METHODS.put("getY2()", new GeoMethod("getY2", false, DOUBLE));
		GEO_METHODS.put("distanceSq(java.awt.geom.Point2D)", new GeoMethod("distanceSq", false, DOUBLE));
		GEO_METHODS.put("getP1()", new GeoMethod("getP1", false, new JT(K.GEOM, "Point2D", null)));
		GEO_METHODS.put("getP2()", new GeoMethod("getP2", false, new JT(K.GEOM, "Point2D", null)));
	}

	/** Translated classes extending a geometry class -> its rt struct. */
	final Map<String, String> superGeom = new HashMap<>();

	/** Path from an object of type t (the closure's `o`) to the geometry struct inside it. */
	String geoPath(JT t, boolean write) {
		if (t.k == K.CLASS) {
			if (!superGeom.containsKey(t.cls)) throw new Unsupported("geometry member of " + t.cls);
			return "o.__super";
		}
		if (t.k == K.GEOM) {
			Geo g = GEO.get(t.cls);
			return g.trait == null ? "o" : "o." + g.acc + (write ? "_mut()" : "()");
		}
		throw new Unsupported("geometry member of " + t);
	}

	/** A copy of the geometry struct of x (arguments of JDK methods). */
	String geoValue(Ex x) {
		String p = geoPath(x.t, false);
		return recvRef(x) + ".get(|o| " + (p.endsWith(")") || p.equals("o") ? "*" + p : p) + ")";
	}

	// ---------------------------------------------------------------- setup

	final Map<String, ClassOrInterfaceDeclaration> classes = new LinkedHashMap<>(); // translated classes
	final Map<String, ClassOrInterfaceDeclaration> allTypes = new HashMap<>();      // every parsed class
	final Map<String, String> fileOf = new HashMap<>();
	final Set<String> skip = new LinkedHashSet<>();
	final Set<String> stubTypes = new LinkedHashSet<>();
	final Set<String> stubMethods = new LinkedHashSet<>();
	final Set<String> natives = new LinkedHashSet<>();
	final List<String> problems = new ArrayList<>();

	/** Static members (MethodDeclaration or VariableDeclarator) to emit, in the order they were needed. */
	final Set<Node> neededStatics = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>()); // Node.equals is structural
	final Deque<Node> staticWork = new ArrayDeque<>();

	/** Rust names of methods and constructors: "cls.signature" -> name. */
	final Map<String, String> callableNames = new HashMap<>();
	final Set<String> nativeUsed = new LinkedHashSet<>();
	/** Non-final statics of the simulation (a save holds them): Java name -> "getter|setter". */
	final Map<String, String> savedStatics = new TreeMap<>();

	public static void main(String[] args) throws IOException {
		if (args.length < 3) {
			System.err.println("usage: J2R <java src root>[:<more roots>] <config> <out dir> [jars...]");
			System.exit(2);
		}
		J2R t = new J2R();
		List<Path> roots = new ArrayList<>();
		for (String r : args[0].split(":")) roots.add(Paths.get(r));
		int rc = t.run(roots, Paths.get(args[1]), Paths.get(args[2]), java.util.Arrays.copyOfRange(args, 3, args.length));
		System.exit(rc);
	}

	int run(List<Path> roots, Path configFile, Path outDir, String[] jars) throws IOException {
		CombinedTypeSolver types = new CombinedTypeSolver(new ReflectionTypeSolver(true));
		for (Path r : roots) types.add(new JavaParserTypeSolver(r));
		for (String j : jars) types.add(new JarTypeSolver(Paths.get(j)));
		ParserConfiguration pc = new ParserConfiguration()
				.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
				.setSymbolResolver(new JavaSymbolSolver(types));
		JavaParser parser = new JavaParser(pc);

		List<String> wanted = new ArrayList<>();
		List<String> sources = new ArrayList<>();
		List<String> forced = new ArrayList<>();
		for (String line : Files.readAllLines(configFile)) {
			line = line.replaceAll("#.*", "").trim();
			if (line.isEmpty()) continue;
			String[] w = line.split("\\s+", 2);
			if (w[0].equals("class")) wanted.add(w[1]);
			else if (w[0].equals("skip")) skip.add(w[1].replace(" ", ""));
			else if (w[0].equals("source")) sources.add(w[1]);
			else if (w[0].equals("static")) forced.add(w[1].replace(" ", ""));
			else if (w[0].equals("stub")) (w[1].contains("(") ? stubMethods : stubTypes).add(w[1].replace(" ", ""));
			else if (w[0].equals("native")) natives.add(w[1].replace(" ", ""));
			else throw new IllegalArgumentException("config: " + line);
		}

		// Parse every file of the package(s) the wanted classes are in, so
		// statics of other classes (Utils) can be found.
		Set<Path> files = new LinkedHashSet<>();
		for (String q : wanted) {
			Path dir = find(roots, q.substring(0, q.lastIndexOf('.')).replace('.', '/'));
			try (java.util.stream.Stream<Path> s = Files.list(dir)) {
				s.filter(p -> p.toString().endsWith(".java")).sorted().forEach(files::add);
			}
		}
		for (String q : sources) files.add(find(roots, q.replace('.', '/') + ".java"));
		for (Path f : files) {
			ParseResult<CompilationUnit> r = parser.parse(new String(Files.readAllBytes(f), StandardCharsets.UTF_8));
			if (!r.isSuccessful()) throw new IllegalStateException(f + ": " + r.getProblems());
			CompilationUnit cu = r.getResult().get();
			for (ClassOrInterfaceDeclaration c : cu.findAll(ClassOrInterfaceDeclaration.class)) {
				c.getFullyQualifiedName().ifPresent(q -> {
					allTypes.put(q, c);
					fileOf.put(q, f.getFileName().toString());
					// Nested classes are named after the whole chain: FdLibm.Sin -> fd_libm_sin
					String chain = c.getNameAsString();
					for (Node n = c.getParentNode().orElse(null); n instanceof ClassOrInterfaceDeclaration; n = n.getParentNode().orElse(null))
						chain = ((ClassOrInterfaceDeclaration) n).getNameAsString() + chain;
					moduleOf.put(q, snake(chain));
				});
			}
		}
		for (String q : wanted) {
			ClassOrInterfaceDeclaration c = allTypes.get(q);
			if (c == null) throw new IllegalArgumentException("class not found: " + q);
			classes.put(q, c);
		}
		for (ClassOrInterfaceDeclaration c : classes.values()) nameCallables(c);
		for (Map.Entry<String, ClassOrInterfaceDeclaration> e : classes.entrySet()) {
			if (e.getValue().getExtendedTypes().size() != 1) continue;
			String base = e.getValue().getExtendedTypes().get(0).resolve().asReferenceType().getQualifiedName();
			if (GEO_TYPES.containsKey(base) && GEO.get(GEO_TYPES.get(base)).trait != null) superGeom.put(e.getKey(), GEO_TYPES.get(base));
		}
		for (String r : forced) {
			int dot = r.lastIndexOf('.', r.indexOf('('));
			String owner = r.substring(0, dot);
			if (!allTypes.containsKey(owner)) throw new IllegalArgumentException("config: no source for " + owner);
			nameCallables(allTypes.get(owner));
			needStatic(method(owner, r.substring(dot + 1)));
		}

		Files.createDirectories(outDir);
		Map<String, StringBuilder> out = new TreeMap<>();
		for (Map.Entry<String, ClassOrInterfaceDeclaration> e : classes.entrySet()) {
			out.put(e.getKey(), new StringBuilder(translateClass(e.getValue())));
		}
		// Statics, on demand (translating one may need more).
		Map<String, StringBuilder> staticOut = new LinkedHashMap<>();
		while (!staticWork.isEmpty()) {
			Node d = staticWork.poll();
			String owner = ownerOf(d);
			String code = d instanceof VariableDeclarator ? translateStaticVar(owner, (VariableDeclarator) d)
					: translateStaticMethod(owner, (MethodDeclaration) d);
			staticOut.computeIfAbsent(owner, o -> new StringBuilder()).append(code);
		}
		for (Map.Entry<String, StringBuilder> e : staticOut.entrySet()) {
			out.computeIfAbsent(e.getKey(), k -> new StringBuilder()).append("\n// ---- statics\n").append(e.getValue());
		}

		StringBuilder mod = new StringBuilder(HEADER_MOD);
		for (String q : out.keySet()) {
			String m = moduleName(q);
			mod.append("pub mod ").append(m).append(";\n");
		}
		mod.append("\n");
		for (String q : out.keySet()) {
			if (classes.containsKey(q)) mod.append("pub use ").append(moduleName(q)).append("::").append(simple(q)).append(";\n");
		}
		// The statics a save holds, by Java name (rust/verify/biogenesis reads statics.txt).
		mod.append("\nuse crate::rt::*;\n\n/// The non-final statics of the simulation's classes, sorted by Java name.\n");
		mod.append("pub fn dump_statics(d: &mut Dumper) {\n");
		if (savedStatics.isEmpty()) mod.append("    let _ = d;\n");
		for (Map.Entry<String, String> e : savedStatics.entrySet())
			mod.append("    d.field(\"").append(e.getKey()).append("\", &").append(e.getValue().split("\\|")[0]).append("());\n");
		mod.append("}\n\n/// Sets a static from a save. False if there's no such static.\n");
		mod.append("pub fn load_static(l: &mut Loader, name: &str) -> bool {\n");
		if (savedStatics.isEmpty()) mod.append("    let _ = (l, name);\n    false\n}\n");
		else mod.append("    match name {\n");
		for (Map.Entry<String, String> e : savedStatics.entrySet())
			mod.append("        \"").append(e.getKey()).append("\" => ").append(e.getValue().split("\\|")[1]).append("(JLoad::load(l)),\n");
		if (!savedStatics.isEmpty()) mod.append("        _ => return false,\n    }\n    true\n}\n");
		write(outDir.resolve("mod.rs"), mod.toString());
		write(outDir.resolve("statics.txt"), String.join("\n", savedStatics.keySet()) + "\n");
		for (Map.Entry<String, StringBuilder> e : out.entrySet()) {
			String src = "// Generated by rust/transpiler/J2R.java from " + fileOf.get(e.getKey()) + ". Do not edit.\n\n"
					+ "#![allow(non_snake_case, non_upper_case_globals, unused_mut, unused_parens, unused_variables,\n"
					+ "    unused_assignments, unused_labels, unused_imports, unreachable_code, dead_code, while_true, clippy::all)]\n\n"
					+ "use crate::rt::*;\nuse super::*;\n\n" + e.getValue();
			write(outDir.resolve(moduleName(e.getKey()) + ".rs"), src);
		}

		for (String n : natives) if (!nativeUsed.contains(n)) problems.add("config: native " + n + " was not translated");
		StringBuilder report = new StringBuilder();
		for (String p : problems) report.append(p).append('\n');
		write(outDir.resolve("unsupported.txt"), report.toString());
		System.out.println("J2R: " + out.size() + " modules, " + problems.size() + " unsupported constructs"
				+ (problems.isEmpty() ? "" : " (see " + outDir.resolve("unsupported.txt") + ")"));
		for (int i = 0; i < Math.min(problems.size(), 20); i++) System.out.println("  " + problems.get(i));
		return problems.isEmpty() ? 0 : 1;
	}

	static final String HEADER_MOD = "// Generated by rust/transpiler/J2R.java. Do not edit.\n\n";

	static void write(Path p, String s) throws IOException {
		Files.write(p, s.getBytes(StandardCharsets.UTF_8));
	}

	static String simple(String q) { return q.substring(q.lastIndexOf('.') + 1); }

	static Path find(List<Path> roots, String rel) {
		for (Path r : roots) if (Files.exists(r.resolve(rel))) return r.resolve(rel);
		throw new IllegalArgumentException("not found in source roots: " + rel);
	}

	final Map<String, String> moduleOf = new HashMap<>();

	String moduleName(String q) {
		return moduleOf.getOrDefault(q, snake(simple(q)));
	}

	static String snake(String name) {
		return name.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
	}

	String ownerOf(Node n) {
		return n.findAncestor(ClassOrInterfaceDeclaration.class).flatMap(c -> c.getFullyQualifiedName()).get();
	}

	/** Rust names for overloaded methods: name, or name_arity when overloaded. */
	void nameCallables(ClassOrInterfaceDeclaration c) {
		String q = c.getFullyQualifiedName().get();
		Map<String, List<CallableDeclaration<?>>> byName = new LinkedHashMap<>();
		for (MethodDeclaration m : c.getMethods()) byName.computeIfAbsent(m.getNameAsString(), k -> new ArrayList<>()).add(m);
		for (ConstructorDeclaration k : c.getConstructors()) byName.computeIfAbsent("<init>", x -> new ArrayList<>()).add(k);
		for (Map.Entry<String, List<CallableDeclaration<?>>> e : byName.entrySet()) {
			Set<String> used = new LinkedHashSet<>();
			for (CallableDeclaration<?> d : e.getValue()) {
				String base = e.getKey().equals("<init>") ? "new" : rustIdent(e.getKey());
				int n = d.getParameters().size();
				String name = e.getValue().size() == 1 || (base.equals("new") && n == 0) ? base : base + n;
				if (!used.add(name)) name = name + "_" + used.size();
				callableNames.put(q + "." + sig(d), name);
			}
		}
	}

	/** Signature as JavaParser's resolver prints it: name(type, type). */
	String sig(CallableDeclaration<?> d) {
		if (d instanceof MethodDeclaration) return ((MethodDeclaration) d).resolve().getSignature().replace(" ", "");
		return ((ConstructorDeclaration) d).resolve().getSignature().replace(" ", "");
	}

	static final Set<String> RUST_KEYWORDS = new java.util.HashSet<>(java.util.Arrays.asList(
			"as", "async", "await", "box", "crate", "dyn", "enum", "extern", "fn", "impl", "in", "let", "loop", "match",
			"mod", "move", "mut", "pub", "ref", "self", "Self", "struct", "super", "trait", "type", "unsafe", "use",
			"where", "yield", "abstract", "become", "final", "macro", "override", "priv", "typeof", "unsized", "virtual",
			"gen", "try", "do"));
	/** Java names that would collide with methods every generated type has. */
	static final Set<String> RENAMED = new java.util.HashSet<>(java.util.Arrays.asList("clone", "default", "drop", "eq", "hash", "fmt"));

	static String rustIdent(String javaName) {
		if (RENAMED.contains(javaName)) return "j" + javaName;
		if (javaName.equals("self") || javaName.equals("super") || javaName.equals("crate") || javaName.equals("Self")) return javaName + "_";
		if (RUST_KEYWORDS.contains(javaName)) return "r#" + javaName;
		return javaName;
	}

	// ---------------------------------------------------------------- classes

	String curClass;          // qualified name of the class being translated
	JT curReturn;             // return type of the callable being translated
	boolean inStatic;         // no `this`

	String translateClass(ClassOrInterfaceDeclaration c) {
		String q = c.getFullyQualifiedName().get();
		curClass = q;
		String name = simple(q);
		StringBuilder out = new StringBuilder();
		String base = superGeom.get(q);
		if (c.getExtendedTypes().size() > 0 && base == null) problem(c, "class extends " + c.getExtendedTypes());

		List<VariableDeclarator> fields = new ArrayList<>();
		List<VariableDeclarator> inits = new ArrayList<>();
		for (FieldDeclaration f : c.getFields()) {
			if (f.isStatic()) continue;
			for (VariableDeclarator v : f.getVariables()) {
				fields.add(v);
				if (v.getInitializer().isPresent() && !isDefaultLiteral(v.getInitializer().get())) inits.add(v);
			}
		}
		out.append("/// Java: ").append(fileOf.get(q)).append(":").append(line(c)).append("\n");
		out.append("#[derive(Clone, Default)]\npub struct ").append(name).append(" {\n");
		if (base != null) out.append("    /// Java: extends ").append(c.getExtendedTypes().get(0)).append("\n    pub __super: ").append(base).append(",\n");
		List<VariableDeclarator> failed = new ArrayList<>();
		for (VariableDeclarator v : fields) {
			try {
				String ft = rust(jt(v.getType().resolve()));
				out.append("    pub ").append(rustIdent(v.getNameAsString())).append(": ").append(ft).append(",\n");
			} catch (RuntimeException e) {
				problem(v, e);
				failed.add(v);
			}
		}
		out.append("}\n\nimpl ").append(name).append(" {\n");

		// Field initializers, run by every constructor that doesn't call this(...).
		out.append("    fn __init(this: &JRef<Self>) {\n");
		inStatic = false;
		tmpCounter = 0;
		for (VariableDeclarator v : inits) {
			try {
				JT ft = jt(v.getType().resolve());
				Ex val = init(v.getInitializer().get(), ft, v);
				out.append("        this.setf(|o| &mut o.").append(rustIdent(v.getNameAsString())).append(", ").append(val.code).append(");\n");
			} catch (RuntimeException e) {
				problem(v, e);
			}
		}
		out.append("    }\n");

		boolean hasCtor = !c.getConstructors().isEmpty();
		if (!hasCtor) {
			out.append("\n    pub fn new() -> JRef<Self> {\n        let this = &JRef::new(Self::default());\n        Self::__init(this);\n        this.clone()\n    }\n");
		}
		for (ConstructorDeclaration k : c.getConstructors()) {
			if (skipped(q, k)) continue;
			out.append(translateCallable(k, q));
		}
		for (MethodDeclaration m : c.getMethods()) {
			if (m.isStatic()) continue; // emitted on demand with the statics
			if (m.isAbstract() || !m.getBody().isPresent()) { problem(m, "abstract method"); continue; }
			out.append(translateCallable(m, q));
		}
		out.append("}\n\n");

		// Collections use Java's hashCode/equals/==.
		boolean hash = false, eq = false;
		for (MethodDeclaration m : c.getMethods()) {
			if (m.isStatic()) continue;
			String sg = sig(m);
			if (sg.equals("hashCode()")) hash = true;
			if (sg.equals("equals(java.lang.Object)")) eq = true;
		}
		out.append("impl JObject for JRef<").append(name).append("> {\n");
		out.append("    fn hash_code(&self) -> i32 {\n        ").append(hash ? name + "::hashCode(self)" : "crate::rt::collections::identity_hash_code()").append("\n    }\n");
		out.append("    fn equals(&self, other: &Self) -> bool {\n        ").append(eq ? name + "::equals(self, other.clone())" : "JRef::ptr_eq(self, other)").append("\n    }\n");
		out.append("    fn same(&self, other: &Self) -> bool {\n        JRef::ptr_eq(self, other)\n    }\n");
		out.append("    fn is_null(&self) -> bool {\n        self.identity().is_none()\n    }\n}\n\n");

		if (base != null) {
			Geo g = GEO.get(base);
			out.append("impl ").append(g.trait).append(" for ").append(name).append(" {\n");
			out.append("    fn ").append(g.acc).append("(&self) -> &").append(base).append(" {\n        &self.__super\n    }\n");
			out.append("    fn ").append(g.acc).append("_mut(&mut self) -> &mut ").append(base).append(" {\n        &mut self.__super\n    }\n}\n\n");
		}

		// Dump, fields (inherited ones too) sorted by name (the Java harness sorts the same way).
		TreeMap<String, String> dumped = new TreeMap<>();
		for (VariableDeclarator v : fields) if (!failed.contains(v)) dumped.put(v.getNameAsString(), rustIdent(v.getNameAsString()));
		if (base != null) for (String f : GEO.get(base).fields) dumped.put(f, "__super." + f);
		out.append("impl JDumpTarget for ").append(name).append(" {\n    fn dump_target(&self, d: &mut Dumper, id: usize) {\n");
		out.append("        d.object(\"").append(name).append("\", id, |d| {\n");
		for (Map.Entry<String, String> f : dumped.entrySet()) {
			out.append("            d.field(\"").append(f.getKey()).append("\", &self.").append(f.getValue()).append(");\n");
		}
		out.append("        });\n    }\n}\n\n");

		// Load: the dump read back (save files).
		out.append("impl JLoad for JRef<").append(name).append("> {\n    fn load(l: &mut Loader) -> Self {\n");
		out.append("        l.object(\"").append(name).append("\", || JRef::new(").append(name).append("::default()), |l, this, n| match n {\n");
		for (Map.Entry<String, String> f : dumped.entrySet()) {
			String path = f.getValue();
			if (path.startsWith("__super.")) {
				String fn = base.equals("Rectangle") ? "load_rectangle_field" : "load_line2d_field";
				out.append("            \"").append(f.getKey()).append("\" => {\n                let mut g = this.get(|o| o.__super);\n                crate::rt::load::")
						.append(fn).append("(l, &mut g, n);\n                this.set(|o| o.__super = g)\n            }\n");
			} else {
				out.append("            \"").append(f.getKey()).append("\" => {\n                let v = JLoad::load(l);\n                this.set(|o| o.").append(path).append(" = v)\n            }\n");
			}
		}
		out.append("            _ => l.unknown_field(\"").append(name).append("\", n),\n        }, JRef::null())\n    }\n}\n");
		return out.toString();
	}

	boolean skipped(String q, CallableDeclaration<?> d) {
		return skip.contains(q + "." + sig(d));
	}

	static boolean isDefaultLiteral(Expression e) {
		if (e.isIntegerLiteralExpr()) return e.asIntegerLiteralExpr().asNumber().longValue() == 0;
		if (e.isDoubleLiteralExpr()) {
			double d = e.asDoubleLiteralExpr().asDouble();
			return d == 0 && 1 / d > 0;
		}
		if (e.isBooleanLiteralExpr()) return !e.asBooleanLiteralExpr().getValue();
		if (e.isNullLiteralExpr()) return true;
		return false;
	}

	static int line(Node n) {
		return n.getRange().map(r -> r.begin.line).orElse(0);
	}

	void problem(Node n, Object what) {
		String m = what instanceof Throwable ? (what instanceof Unsupported ? ((Throwable) what).getMessage() : what.toString()) : what.toString();
		String q = n.findAncestor(ClassOrInterfaceDeclaration.class).flatMap(c -> c.getFullyQualifiedName()).orElse("?");
		problems.add(fileOf.getOrDefault(q, q) + ":" + line(n) + ": " + m);
	}

	String translateCallable(CallableDeclaration<?> d, String q) {
		boolean ctor = d instanceof ConstructorDeclaration;
		boolean isStatic = d.isStatic();
		String name = callableNames.get(q + "." + sig(d));
		if (name == null) name = rustIdent(d.getNameAsString());
		StringBuilder out = new StringBuilder();
		Scope saved = scope;
		scope = new Scope(null);
		inStatic = isStatic;
		labelCounter = 0;
		tmpCounter = 0; // per method, so changes elsewhere don't renumber this one
		boolean skipped = skipped(q, d);
		String nativeName = natives.contains(q + "." + sig(d)) ? "crate::native::" + moduleName(q) + "::" + name : null;
		if (nativeName != null) nativeUsed.add(q + "." + sig(d));
		try {
			JT ret;
			if (ctor) ret = VOID;
			else {
				MethodDeclaration m = (MethodDeclaration) d;
				ResolvedType rt = m.getType().resolve();
				// clone() returns Object in Java; ours returns the class itself.
				if (m.getNameAsString().equals("clone") && rt.isReferenceType() && rt.asReferenceType().getQualifiedName().equals("java.lang.Object"))
					ret = new JT(K.CLASS, q, null);
				else ret = jt(rt);
			}
			curReturn = ret;
			StringBuilder params = new StringBuilder();
			StringBuilder args = new StringBuilder();
			if (!isStatic) params.append("this: &JRef<Self>");
			for (Parameter p : d.getParameters()) {
				// equals(Object) is only ever called with the same class here.
				JT pt = !ctor && !isStatic && sig(d).equals("equals(java.lang.Object)") ? new JT(K.CLASS, q, null) : jt(p.getType().resolve());
				String pn = rustIdent(p.getNameAsString());
				if (params.length() > 0) params.append(", ");
				if (viewParam(p)) {
					scope.declareView(p.getNameAsString(), pn, pt);
					params.append(pn).append(": &impl JArr<").append(rust(pt.elem)).append(">");
				} else {
					scope.declare(p.getNameAsString(), pn, pt);
					params.append("mut ").append(pn).append(": ").append(rust(pt));
				}
				if (args.length() > 0) args.append(", ");
				args.append(pn);
			}
			BlockStmt body = ctor ? ((ConstructorDeclaration) d).getBody() : ((MethodDeclaration) d).getBody().get();
			String ind = isStatic ? "" : "    ";
			out.append("\n").append(ind).append("/// Java: ").append(fileOf.get(q)).append(":").append(line(d)).append(" ").append(d.getDeclarationAsString(false, false, false)).append("\n");
			if (ctor) {
				// new...() allocates and runs the field initializers, __ctor...() runs the body.
				List<Statement> stmts = new ArrayList<>(body.getStatements());
				boolean delegates = !stmts.isEmpty() && stmts.get(0) instanceof ExplicitConstructorInvocationStmt
						&& ((ExplicitConstructorInvocationStmt) stmts.get(0)).isThis();
				if (!stmts.isEmpty() && stmts.get(0) instanceof ExplicitConstructorInvocationStmt) {
					ExplicitConstructorInvocationStmt s = (ExplicitConstructorInvocationStmt) stmts.get(0);
					if (!s.isThis() && !s.getArguments().isEmpty()) problem(s, "super(...) with arguments");
				}
				out.append(ind).append("pub fn ").append(name).append("(").append(params.toString().replace("this: &JRef<Self>, ", "").replace("this: &JRef<Self>", ""))
						.append(") -> JRef<Self> {\n");
				out.append(ind).append("    let this = &JRef::new(Self::default());\n");
				if (!delegates) out.append(ind).append("    Self::__init(this);\n");
				out.append(ind).append("    Self::__ctor_").append(name).append("(this").append(args.length() > 0 ? ", " + args : "").append(");\n");
				out.append(ind).append("    this.clone()\n").append(ind).append("}\n\n");
				out.append(ind).append("fn __ctor_").append(name).append("(").append(params).append(") {\n");
			} else {
				out.append(ind).append("pub fn ").append(name).append("(").append(params).append(")");
				if (ret.k != K.VOID) out.append(" -> ").append(rust(ret));
				out.append(" {\n");
			}
			StringBuilder b = new StringBuilder();
			if (skipped) {
				b.append(ind).append("    crate::rt::throw(\"J2R: skipped ").append(esc(q + "." + sig(d))).append("\")\n");
			} else if (nativeName != null) {
				b.append(ind).append("    ").append(nativeName).append("(").append(isStatic ? "" : args.length() > 0 ? "this, " : "this").append(args).append(")\n");
			} else {
				for (Statement s : body.getStatements()) stmt(s, b, ind + "    ");
			}
			out.append(b);
			out.append(ind).append("}\n");
		} catch (RuntimeException e) {
			// A skipped method whose signature can't be translated is left out.
			if (!skipped) problem(d, e);
			out.setLength(0);
			out.append(skipped ? "" : "    // " + d.getNameAsString() + ": not translated\n");
		} finally {
			scope = saved;
		}
		return out.toString();
	}

	// ---------------------------------------------------------------- statics

	/** A static method or (one variable of) a static field is used: emit it. */
	void needStatic(Node d) {
		if (neededStatics.add(d)) staticWork.add(d);
	}

	String translateStaticMethod(String owner, MethodDeclaration m) {
		curClass = owner;
		if (!callableNames.containsKey(owner + "." + sig(m))) nameCallables(allTypes.get(owner));
		return translateCallable(m, owner);
	}

	String translateStaticVar(String owner, VariableDeclarator v) {
		curClass = owner;
		inStatic = true;
		tmpCounter = 0;
		Scope saved = scope;
		scope = new Scope(null);
		try {
			FieldDeclaration f = (FieldDeclaration) v.getParentNode().get();
			JT t = jt(v.getType().resolve());
			String n = rustIdent(v.getNameAsString());
			String doc = "/// Java: " + fileOf.get(owner) + ":" + line(v) + "\n";
			Expression init = v.getInitializer().orElse(null);
			if (f.isFinal() && init != null && isConstExpr(init)) {
				Ex val = coerce(expr(init), t, v);
				return doc + "pub const " + n + ": " + rust(t) + " = " + val.code + ";\n";
			}
			if (!f.isFinal() && owner.startsWith("biogenesis.")) savedStatics.put(owner + "." + v.getNameAsString(), moduleName(owner) + "::" + n + "|" + moduleName(owner) + "::set_" + v.getNameAsString());
			String initCode = init == null ? defaultValue(t) : init(init, t, v).code;
			boolean constInit = init == null || isConstExpr(init);
			String cell = "__" + v.getNameAsString();
			StringBuilder out = new StringBuilder(doc.replace("/// ", "// "));
			out.append("pub static ").append(cell).append(": JStatic<").append(rust(t)).append("> = ")
					.append(constInit ? "JStatic::new(" + initCode + ")" : "JStatic::lazy(|| " + initCode + ")").append(";\n");
			out.append("#[inline] pub fn ").append(n).append("() -> ").append(rust(t)).append(" { ").append(cell).append(".get() }\n");
			out.append("pub fn set_").append(v.getNameAsString()).append("(v: ").append(rust(t)).append(") { ").append(cell).append(".set(v) }\n");
			return out.toString();
		} catch (RuntimeException e) {
			problem(v, e);
			return "// " + v.getNameAsString() + ": not translated\n";
		} finally {
			scope = saved;
		}
	}

	/** Can be a Rust const: literals, operators, casts, other constants, new Color(literals). */
	boolean isConstExpr(Expression e) {
		if (e.isLiteralExpr()) return !e.isStringLiteralExpr() && !e.isTextBlockLiteralExpr();
		if (e.isEnclosedExpr()) return isConstExpr(e.asEnclosedExpr().getInner());
		if (e.isUnaryExpr()) return isConstExpr(e.asUnaryExpr().getExpression());
		if (e.isBinaryExpr()) return isConstExpr(e.asBinaryExpr().getLeft()) && isConstExpr(e.asBinaryExpr().getRight());
		if (e.isCastExpr()) return isConstExpr(e.asCastExpr().getExpression());
		if (e.isConditionalExpr()) return isConstExpr(e.asConditionalExpr().getCondition()) && isConstExpr(e.asConditionalExpr().getThenExpr()) && isConstExpr(e.asConditionalExpr().getElseExpr());
		if (e.isObjectCreationExpr()) {
			ObjectCreationExpr o = e.asObjectCreationExpr();
			if (!o.getType().getNameAsString().equals("Color")) return false;
			for (Expression a : o.getArguments()) if (!isConstExpr(a)) return false;
			return true;
		}
		if (e.isNameExpr() || e.isFieldAccessExpr()) {
			try {
				ResolvedValueDeclaration r = e.isNameExpr() ? e.asNameExpr().resolve() : e.asFieldAccessExpr().resolve();
				if (!r.isField()) return false;
				ResolvedFieldDeclaration f = r.asField();
				if (!f.isStatic()) return false;
				String owner = f.declaringType().getQualifiedName();
				if (owner.equals("java.awt.Color")) return true;
				if (owner.equals("java.lang.Math")) return true;
				if (BOXES.contains(owner)) return true;
				VariableDeclarator v = staticVar(f);
				if (!((FieldDeclaration) v.getParentNode().get()).isFinal()) return false;
				return v.getInitializer().map(this::isConstExpr).orElse(false);
			} catch (RuntimeException ex) {
				return false;
			}
		}
		return false;
	}

	// ---------------------------------------------------------------- scopes

	static final class Var {
		final String rust; final JT t;
		boolean view; // an array that doesn't escape: a `&impl JArr` / `&JStackArray`, used without clone
		Var(String rust, JT t) { this.rust = rust; this.t = t; }
	}

	static final class Scope {
		final Scope parent;
		final Map<String, Var> vars = new HashMap<>();
		Scope(Scope p) { parent = p; }
		void declare(String java, String rust, JT t) { vars.put(java, new Var(rust, t)); }
		void declareView(String java, String rust, JT t) { Var v = new Var(rust, t); v.view = true; vars.put(java, v); }
		Var find(String n) {
			for (Scope s = this; s != null; s = s.parent) if (s.vars.containsKey(n)) return s.vars.get(n);
			return null;
		}
	}

	Scope scope = new Scope(null);

	// ---------------------------------------------------------------- statements

	/** A break/continue target. */
	static final class Target {
		final String javaLabel; final boolean loop;
		final String breakLabel; final String continueLabel; // rust labels
		boolean breakUsed, continueUsed;
		Target(String javaLabel, boolean loop, String b, String c) { this.javaLabel = javaLabel; this.loop = loop; breakLabel = b; continueLabel = c; }
	}

	final Deque<Target> targets = new ArrayDeque<>();
	int labelCounter;
	String pendingLabel; // Java label for the next loop

	void stmt(Statement s, StringBuilder out, String ind) {
		// A statement that fails halfway must not leave half its code behind.
		StringBuilder b = new StringBuilder();
		int savedTargets = targets.size();
		Scope savedScope = scope;
		try {
			stmt0(s, b, ind);
			out.append(b);
		} catch (Unsupported e) {
			while (targets.size() > savedTargets) targets.pop();
			scope = savedScope;
			pendingLabel = null;
			problem(s, e);
			out.append(ind).append("unimplemented!(\"J2R: ").append(esc(e.getMessage())).append(" (").append(fileOf.get(curClass)).append(":").append(line(s)).append(")\");\n");
		}
	}

	void block(Statement s, StringBuilder out, String ind) {
		Scope saved = scope;
		scope = new Scope(scope);
		if (s.isBlockStmt()) for (Statement x : s.asBlockStmt().getStatements()) stmt(x, out, ind);
		else stmt(s, out, ind);
		scope = saved;
	}

	void stmt0(Statement s, StringBuilder out, String ind) {
		if (s.isBlockStmt()) {
			out.append(ind).append("{\n");
			block(s, out, ind + "    ");
			out.append(ind).append("}\n");
		} else if (s.isEmptyStmt()) {
			// nothing
		} else if (s.isExpressionStmt()) {
			exprStmt(s.asExpressionStmt().getExpression(), out, ind);
		} else if (s.isIfStmt()) {
			IfStmt i = s.asIfStmt();
			out.append(ind).append("if ").append(cond(i.getCondition())).append(" {\n");
			block(i.getThenStmt(), out, ind + "    ");
			while (i.getElseStmt().isPresent() && i.getElseStmt().get().isIfStmt()) {
				i = i.getElseStmt().get().asIfStmt();
				out.append(ind).append("} else if ").append(cond(i.getCondition())).append(" {\n");
				block(i.getThenStmt(), out, ind + "    ");
			}
			if (i.getElseStmt().isPresent()) {
				out.append(ind).append("} else {\n");
				block(i.getElseStmt().get(), out, ind + "    ");
			}
			out.append(ind).append("}\n");
		} else if (s.isWhileStmt()) {
			WhileStmt w = s.asWhileStmt();
			Target t = pushLoop(false);
			StringBuilder body = new StringBuilder();
			block(w.getBody(), body, ind + "    ");
			targets.pop();
			out.append(ind).append(t.breakUsed || t.continueUsed ? t.breakLabel + ": " : "").append("while ").append(cond(w.getCondition())).append(" {\n").append(body).append(ind).append("}\n");
		} else if (s.isDoStmt()) {
			DoStmt w = s.asDoStmt();
			Target t = pushLoop(true);
			StringBuilder body = new StringBuilder();
			block(w.getBody(), body, ind + (t.continueLabel != null ? "        " : "    "));
			targets.pop();
			out.append(ind).append(t.breakLabel).append(": loop {\n");
			appendContinueBlock(t, body, out, ind + "    ");
			out.append(ind).append("    if !(").append(cond(w.getCondition())).append(") {\n").append(ind).append("        break ").append(t.breakLabel).append(";\n").append(ind).append("    }\n");
			out.append(ind).append("}\n");
		} else if (s.isForStmt()) {
			ForStmt f = s.asForStmt();
			Scope saved = scope;
			scope = new Scope(scope);
			out.append(ind).append("{\n");
			String in = ind + "    ";
			for (Expression e : f.getInitialization()) exprStmt(e, out, in);
			Target t = pushLoop(true);
			StringBuilder body = new StringBuilder();
			block(f.getBody(), body, in + "        ");
			targets.pop();
			String c = f.getCompare().isPresent() ? cond(f.getCompare().get()) : "true";
			out.append(in).append(t.breakUsed ? t.breakLabel + ": " : "").append("while ").append(c).append(" {\n");
			appendContinueBlock(t, body, out, in + "    ");
			for (Expression e : f.getUpdate()) exprStmt(e, out, in + "    ");
			out.append(in).append("}\n").append(ind).append("}\n");
			scope = saved;
		} else if (s.isForEachStmt()) {
			ForEachStmt f = s.asForEachStmt();
			Ex it = expr(f.getIterable());
			if (it.t.k == K.COLL) it = new Ex(recv(f.getIterable(), it) + ".snapshot_array()", new JT(K.ARRAY, null, it.t.elem), false);
			if (it.t.k == K.HSET) it = new Ex("(" + it.code + ").as_collection().snapshot_array()", new JT(K.ARRAY, null, it.t.elem), false);
			if (it.t.k != K.ARRAY && it.t.k != K.LIST) throw new Unsupported("for-each over " + it.t);
			int n = ++labelCounter;
			Scope saved = scope;
			scope = new Scope(scope);
			String var = rustIdent(f.getVariable().getVariables().get(0).getNameAsString());
			JT vt = jt(f.getVariable().getVariables().get(0).getType().resolve());
			scope.declare(f.getVariable().getVariables().get(0).getNameAsString(), var, vt);
			Target t = new Target(takeLabel(), true, "'l" + n, null);
			targets.push(t);
			StringBuilder body = new StringBuilder();
			block(f.getBody(), body, ind + "        ");
			targets.pop();
			out.append(ind).append("{\n");
			out.append(ind).append("    let __a").append(n).append(" = ").append(it.code).append(";\n");
			out.append(ind).append("    let mut __i").append(n).append(" = 0;\n");
			out.append(ind).append("    ").append(t.breakLabel).append(": while __i").append(n).append(" < __a").append(n).append(".len() {\n");
			out.append(ind).append("        let mut ").append(var).append(": ").append(rust(vt)).append(" = ")
					.append(coerce(new Ex("__a" + n + ".at(__i" + n + ")", it.t.elem, false), vt, f).code).append(";\n");
			out.append(ind).append("        __i").append(n).append(" += 1;\n");
			out.append(body);
			out.append(ind).append("    }\n").append(ind).append("}\n");
			scope = saved;
		} else if (s.isReturnStmt()) {
			ReturnStmt r = s.asReturnStmt();
			if (r.getExpression().isPresent()) out.append(ind).append("return ").append(exprTo(r.getExpression().get(), curReturn, r).code).append(";\n");
			else out.append(ind).append("return;\n");
		} else if (s.isBreakStmt()) {
			BreakStmt b = s.asBreakStmt();
			Target t = findTarget(b.getLabel().map(l -> l.asString()).orElse(null), false);
			t.breakUsed = true;
			out.append(ind).append("break ").append(t.breakLabel).append(";\n");
		} else if (s.isContinueStmt()) {
			ContinueStmt c = s.asContinueStmt();
			Target t = findTarget(c.getLabel().map(l -> l.asString()).orElse(null), true);
			if (t.continueLabel != null) {
				t.continueUsed = true;
				out.append(ind).append("break ").append(t.continueLabel).append(";\n");
			} else {
				t.breakUsed = true;
				out.append(ind).append("continue ").append(t.breakLabel).append(";\n");
			}
		} else if (s.isLabeledStmt()) {
			LabeledStmt l = s.asLabeledStmt();
			Statement inner = l.getStatement();
			if (inner.isForStmt() || inner.isWhileStmt() || inner.isDoStmt() || inner.isForEachStmt()) {
				pendingLabel = l.getLabel().asString();
				stmt0(inner, out, ind);
			} else {
				int n = ++labelCounter;
				Target t = new Target(l.getLabel().asString(), false, "'b" + n, null);
				targets.push(t);
				StringBuilder body = new StringBuilder();
				block(inner, body, ind + "    ");
				targets.pop();
				out.append(ind).append(t.breakLabel).append(": {\n").append(body).append(ind).append("}\n");
			}
		} else if (s.isSwitchStmt()) {
			switchStmt(s.asSwitchStmt(), out, ind);
		} else if (s.isThrowStmt()) {
			Expression e = s.asThrowStmt().getExpression();
			String what = e.isObjectCreationExpr() ? e.asObjectCreationExpr().getType().getNameAsString() : "exception";
			out.append(ind).append("crate::rt::throw(\"").append(esc(what)).append("\");\n");
		} else if (s.isTryStmt()) {
			TryStmt t = s.asTryStmt();
			if (t.getFinallyBlock().isPresent() || !t.getResources().isEmpty()) throw new Unsupported("try with finally/resources");
			for (CatchClause c : t.getCatchClauses()) {
				String ct = c.getParameter().getType().asString();
				if (!ct.equals("CloneNotSupportedException")) throw new Unsupported("catch " + ct);
			}
			// clone() can't fail here: the try body is all there is.
			stmt0(t.getTryBlock(), out, ind);
		} else if (s.isSynchronizedStmt()) {
			stmt0(s.asSynchronizedStmt().getBody(), out, ind);
		} else if (s.isAssertStmt()) {
			// Java runs without -ea: asserts are not evaluated.
		} else if (s.isExplicitConstructorInvocationStmt()) {
			ExplicitConstructorInvocationStmt e = s.asExplicitConstructorInvocationStmt();
			if (e.isThis()) {
				ResolvedConstructorDeclaration rc = e.resolve();
				String n = callableNames.get(curClass + "." + rc.getSignature().replace(" ", ""));
				out.append(ind).append("Self::__init(this);\n");
				out.append(ind).append("Self::__ctor_").append(n).append("(this").append(args(e.getArguments(), rc::getParam, rc.getNumberOfParams(), e, true)).append(");\n");
			}
			// super() of Object: nothing to do
		} else if (s.isLocalClassDeclarationStmt()) {
			throw new Unsupported("local class");
		} else {
			throw new Unsupported("statement " + s.getClass().getSimpleName());
		}
	}

	void appendContinueBlock(Target t, StringBuilder body, StringBuilder out, String ind) {
		if (t.continueUsed) {
			out.append(ind).append(t.continueLabel).append(": {\n").append(body).append(ind).append("}\n");
		} else {
			// body was indented for the block; drop one level
			out.append(body.toString().replaceAll("(?m)^    ", ""));
		}
	}

	String takeLabel() {
		String l = pendingLabel;
		pendingLabel = null;
		return l;
	}

	Target pushLoop(boolean needsContinueBlock) {
		int n = ++labelCounter;
		Target t = new Target(takeLabel(), true, "'l" + n, needsContinueBlock ? "'c" + n : null);
		targets.push(t);
		return t;
	}

	Target findTarget(String label, boolean isContinue) {
		for (Target t : targets) {
			if (label != null ? label.equals(t.javaLabel) : (t.loop || (!isContinue && t.continueLabel == null && t.breakLabel.startsWith("'s")))) {
				if (isContinue && !t.loop) continue;
				return t;
			}
		}
		throw new Unsupported((isContinue ? "continue" : "break") + " target");
	}

	void switchStmt(SwitchStmt sw, StringBuilder out, String ind) {
		Ex sel = expr(sw.getSelector());
		if (!sel.t.integral()) throw new Unsupported("switch on " + sel.t);
		int n = ++labelCounter;
		Target t = new Target(takeLabel(), false, "'s" + n, null);
		targets.push(t);
		// Group entries: labels without statements fall through to the next ones.
		List<List<Expression>> labels = new ArrayList<>();
		List<NodeList<Statement>> bodies = new ArrayList<>();
		List<Expression> pending = new ArrayList<>();
		boolean pendingDefault = false;
		int defaultIdx = -1;
		for (SwitchEntry e : sw.getEntries()) {
			if (e.getType() != SwitchEntry.Type.STATEMENT_GROUP) throw new Unsupported("switch arrow");
			if (e.getLabels().isEmpty()) pendingDefault = true;
			pending.addAll(e.getLabels());
			if (e.getStatements().isEmpty()) continue;
			if (pendingDefault) defaultIdx = labels.size();
			labels.add(pending);
			bodies.add(e.getStatements());
			pending = new ArrayList<>();
			pendingDefault = false;
		}
		boolean fallThrough = false;
		for (int i = 0; i < bodies.size() - 1; i++) {
			NodeList<Statement> b = bodies.get(i);
			Statement last = b.get(b.size() - 1);
			if (last.isBlockStmt() && !last.asBlockStmt().getStatements().isEmpty())
				last = last.asBlockStmt().getStatements().get(last.asBlockStmt().getStatements().size() - 1);
			if (!(last.isBreakStmt() || last.isReturnStmt() || last.isContinueStmt() || last.isThrowStmt())) fallThrough = true;
		}
		StringBuilder body = new StringBuilder();
		String in = ind + "    ";
		body.append(in).append("let __s").append(n).append(" = ").append(sel.code).append(";\n");
		if (fallThrough) {
			// __k = the group the selector jumps to; then every group from there
			// on runs in order (Java's fall-through) until a break.
			body.append(in).append("let __k").append(n).append(" = ");
			for (int i = 0; i < labels.size(); i++) {
				if (i == defaultIdx) continue;
				StringBuilder c = new StringBuilder();
				for (Expression l : labels.get(i)) {
					if (c.length() > 0) c.append(" || ");
					c.append("__s").append(n).append(" == ").append(coerce(expr(l), promote(sel.t), l).code);
				}
				body.append("if ").append(c).append(" { ").append(i).append(" } else ");
			}
			body.append("{ ").append(defaultIdx >= 0 ? defaultIdx : labels.size()).append(" };\n");
			for (int i = 0; i < labels.size(); i++) {
				body.append(in).append("if __k").append(n).append(" <= ").append(i).append(" {\n");
				Scope saved = scope;
				scope = new Scope(scope);
				for (Statement st : bodies.get(i)) stmt(st, body, in + "    ");
				scope = saved;
				body.append(in).append("}\n");
			}
			targets.pop();
			out.append(ind).append(t.breakLabel).append(": {\n").append(body).append(ind).append("}\n");
			return;
		}
		boolean first = true;
		for (int i = 0; i < labels.size(); i++) {
			if (i == defaultIdx) continue;
			StringBuilder c = new StringBuilder();
			for (Expression l : labels.get(i)) {
				if (c.length() > 0) c.append(" || ");
				c.append("__s").append(n).append(" == ").append(coerce(expr(l), promote(sel.t), l).code);
			}
			body.append(in).append(first ? "if " : "} else if ").append(c).append(" {\n");
			first = false;
			Scope saved = scope;
			scope = new Scope(scope);
			for (Statement st : bodies.get(i)) stmt(st, body, in + "    ");
			scope = saved;
		}
		if (defaultIdx >= 0) {
			body.append(in).append(first ? "{\n" : "} else {\n");
			Scope saved = scope;
			scope = new Scope(scope);
			for (Statement st : bodies.get(defaultIdx)) stmt(st, body, in + "    ");
			scope = saved;
			body.append(in).append("}\n");
		} else if (!first) body.append(in).append("}\n");
		targets.pop();
		out.append(ind).append(t.breakLabel).append(": {\n").append(body).append(ind).append("}\n");
	}

	String cond(Expression e) {
		return coerce(expr(e), BOOL, e).code;
	}

	void exprStmt(Expression e, StringBuilder out, String ind) {
		if (e.isVariableDeclarationExpr()) {
			for (VariableDeclarator v : e.asVariableDeclarationExpr().getVariables()) {
				JT t = jt(v.getType().resolve());
				String n = rustIdent(v.getNameAsString());
				String stack = stackArray(v, t);
				if (stack != null) {
					scope.declareView(v.getNameAsString(), n, t);
					out.append(ind).append("let ").append(n).append(" = &").append(stack).append(";\n");
					continue;
				}
				String init = v.getInitializer().isPresent() ? init(v.getInitializer().get(), t, v).code : defaultValue(t);
				scope.declare(v.getNameAsString(), n, t);
				out.append(ind).append("let mut ").append(n).append(": ").append(rust(t)).append(" = ").append(init).append(";\n");
			}
			return;
		}
		if (e.isAssignExpr()) {
			out.append(ind).append(assign(e.asAssignExpr(), false).code).append(";\n");
			return;
		}
		if (e.isUnaryExpr()) {
			UnaryExpr u = e.asUnaryExpr();
			UnaryExpr.Operator op = u.getOperator();
			if (op == UnaryExpr.Operator.PREFIX_INCREMENT || op == UnaryExpr.Operator.POSTFIX_INCREMENT
					|| op == UnaryExpr.Operator.PREFIX_DECREMENT || op == UnaryExpr.Operator.POSTFIX_DECREMENT) {
				out.append(ind).append(incDec(u, false).code).append(";\n");
				return;
			}
		}
		Ex x = expr(e);
		out.append(ind).append(x.code).append(";\n");
	}

	// ---------------------------------------------------------------- expressions

	/** A translated expression: Rust code, its Java type, and whether it is free of side effects and object reads. */
	static final class Ex {
		final String code; final JT t; final boolean pure;
		Long intLit; // value, when an int/long literal (possibly negated)
		String staticCell; // the JStatic, when a read of a static field
		boolean view;      // a non-escaping array variable (see Var.view)
		Ex(String code, JT t, boolean pure) { this.code = code; this.t = t; this.pure = pure; }
	}

	Ex expr(Expression e) {
		try {
			return expr0(e);
		} catch (Unsupported u) {
			throw u;
		} catch (RuntimeException r) {
			throw new Unsupported(e.getClass().getSimpleName() + " '" + abbreviate(e.toString()) + "': " + r);
		}
	}

	static String abbreviate(String s) {
		s = s.replaceAll("\\s+", " ");
		return s.length() > 80 ? s.substring(0, 77) + "..." : s;
	}

	static String esc(String s) {
		return s.replace("\\", "\\\\").replace("\"", "\\\"");
	}

	Ex expr0(Expression e) {
		if (e.isEnclosedExpr()) {
			Ex x = expr(e.asEnclosedExpr().getInner());
			Ex r = new Ex("(" + x.code + ")", x.t, x.pure);
			r.intLit = x.intLit;
			return r;
		}
		if (e.isIntegerLiteralExpr()) {
			Number v = e.asIntegerLiteralExpr().asNumber();
			Ex r = new Ex(v.intValue() < 0 ? "(" + v + "i32)" : v.toString(), INT, true);
			r.intLit = v.longValue();
			return r;
		}
		if (e.isLongLiteralExpr()) {
			Number v = e.asLongLiteralExpr().asNumber();
			Ex r = new Ex(v.longValue() < 0 ? "(" + v + "i64)" : v + "i64", LONG, true);
			r.intLit = v.longValue();
			return r;
		}
		if (e.isDoubleLiteralExpr()) {
			String s = e.asDoubleLiteralExpr().getValue().replace("_", "");
			boolean isFloat = s.endsWith("f") || s.endsWith("F");
			if (isFloat) return new Ex(floatLit(Float.parseFloat(s)), FLOAT, true);
			return new Ex(doubleLit(Double.parseDouble(s)), DOUBLE, true);
		}
		if (e.isBooleanLiteralExpr()) return new Ex(String.valueOf(e.asBooleanLiteralExpr().getValue()), BOOL, true);
		if (e.isCharLiteralExpr()) return new Ex((int) e.asCharLiteralExpr().asChar() + "u16", CHAR, true);
		if (e.isNullLiteralExpr()) return new Ex("JRef::null()", NULLT, true);
		if (e.isStringLiteralExpr()) return new Ex("JStr::from(\"" + esc(e.asStringLiteralExpr().asString()) + "\")", STRING, true);
		if (e.isThisExpr()) {
			if (inStatic) throw new Unsupported("this in static context");
			return new Ex("this.clone()", new JT(K.CLASS, curClass, null), true);
		}
		if (e.isNameExpr()) return name(e.asNameExpr());
		if (e.isFieldAccessExpr()) return fieldAccess(e.asFieldAccessExpr());
		if (e.isArrayAccessExpr()) {
			ArrayAccessExpr a = e.asArrayAccessExpr();
			Ex arr = expr(a.getName());
			Ex idx = coerce(expr(a.getIndex()), INT, a);
			if (arr.t.k != K.ARRAY) throw new Unsupported("index into " + arr.t);
			String[] fa = readOnly(a.getIndex()) ? fieldArray(arr) : null;
			if (fa != null) return new Ex(fa[0] + ".at(" + idx.code + ")" + fa[1], arr.t.elem, false);
			return new Ex(recv(a.getName(), arr) + ".at(" + idx.code + ")", arr.t.elem, false);
		}
		if (e.isMethodCallExpr()) return call(e.asMethodCallExpr());
		if (e.isObjectCreationExpr()) return create(e.asObjectCreationExpr(), null);
		if (e.isArrayCreationExpr()) return newArray(e.asArrayCreationExpr());
		if (e.isArrayInitializerExpr()) throw new Unsupported("array initializer without type");
		if (e.isCastExpr()) return cast(e.asCastExpr());
		if (e.isUnaryExpr()) return unary(e.asUnaryExpr());
		if (e.isBinaryExpr()) return binary(e.asBinaryExpr());
		if (e.isConditionalExpr()) {
			ConditionalExpr c = e.asConditionalExpr();
			JT t = jt(c.calculateResolvedType());
			Ex a = coerce(expr(c.getThenExpr()), t, c), b = coerce(expr(c.getElseExpr()), t, c);
			Ex k = coerce(expr(c.getCondition()), BOOL, c);
			return new Ex("(if " + k.code + " { " + a.code + " } else { " + b.code + " })", t, k.pure && a.pure && b.pure);
		}
		if (e.isAssignExpr()) return assign(e.asAssignExpr(), true);
		if (e.isInstanceOfExpr()) {
			InstanceOfExpr i = e.asInstanceOfExpr();
			if (i.getPattern().isPresent()) throw new Unsupported("instanceof pattern");
			Ex x = expr(i.getExpression());
			JT t = jt(i.getType().resolve());
			// Only the trivial case: the static type is already the class (equals(Object)).
			if (!x.t.equals(t)) throw new Unsupported("instanceof " + t + " on " + x.t);
			return new Ex("!" + recv(i.getExpression(), x) + ".is_null()", BOOL, false);
		}
		throw new Unsupported("expression " + e.getClass().getSimpleName() + " '" + abbreviate(e.toString()) + "'");
	}

	/**
	 * An expression converted to type `to`. The expected type is what gives
	 * lambdas their interface, `new HashSet<>()` its element type and raw
	 * `new ArrayList[n]` arrays theirs.
	 */
	Ex exprTo(Expression e, JT to, Node at) {
		if (e.isEnclosedExpr()) return exprTo(e.asEnclosedExpr().getInner(), to, at);
		if (e.isLambdaExpr()) return lambda(e.asLambdaExpr(), to);
		if (e.isArrayCreationExpr() && !e.asArrayCreationExpr().getInitializer().isPresent()) return coerce(newArray(e.asArrayCreationExpr(), to), to, at);
		if (e.isObjectCreationExpr() && e.asObjectCreationExpr().getType().isUsingDiamondOperator()) return coerce(create(e.asObjectCreationExpr(), to), to, at);
		if (e.isMethodCallExpr() && e.asMethodCallExpr().getArguments().size() == 1) {
			// Collections.synchronizedSet(x) etc.: x itself (no threads here)
			ResolvedMethodDeclaration rm = e.asMethodCallExpr().resolve();
			if (rm.declaringType().getQualifiedName().equals("java.util.Collections") && rm.getName().startsWith("synchronized"))
				return exprTo(e.asMethodCallExpr().getArgument(0), to, at);
		}
		return coerce(expr(e), to, at);
	}

	/**
	 * A lambda: a closure owning copies of the variables it uses (Java's
	 * captures are effectively final) and of `this`.
	 */
	Ex lambda(LambdaExpr l, JT to) {
		if (to.k != K.FN) throw new Unsupported("lambda as " + to);
		MethodDeclaration m = sam(to.cls);
		if (m.getParameters().size() != l.getParameters().size()) throw new Unsupported("lambda arity");
		Set<String> params = new java.util.HashSet<>();
		for (Parameter p : l.getParameters()) params.add(p.getNameAsString());
		Set<String> captured = new java.util.TreeSet<>();
		for (NameExpr n : l.getBody().findAll(NameExpr.class))
			if (!params.contains(n.getNameAsString()) && scope.find(n.getNameAsString()) != null) captured.add(n.getNameAsString());
		StringBuilder pre = new StringBuilder("{ ");
		if (!inStatic) pre.append("let __this = this.clone(); ");
		for (String c : captured) {
			Var v = scope.find(c);
			pre.append("let ").append(v.rust).append(" = ").append(v.t.copy() ? v.rust : v.rust + ".clone()").append("; ");
		}
		Scope saved = scope;
		JT savedReturn = curReturn;
		Deque<Target> savedTargets = new ArrayDeque<>(targets);
		targets.clear();
		scope = new Scope(scope);
		try {
			StringBuilder sigs = new StringBuilder();
			for (int i = 0; i < m.getParameters().size(); i++) {
				JT pt = jt(m.getParameter(i).getType().resolve());
				String pn = rustIdent(l.getParameter(i).getNameAsString());
				scope.declare(l.getParameter(i).getNameAsString(), pn, pt);
				if (i > 0) sigs.append(", ");
				sigs.append("mut ").append(pn).append(": ").append(rust(pt));
			}
			JT ret = jt(m.getType().resolve());
			curReturn = ret;
			StringBuilder body = new StringBuilder();
			String ind = "        ";
			if (!inStatic) body.append(ind).append("let this = &__this;\n");
			Statement b = l.getBody();
			if (b.isExpressionStmt() && ret.k != K.VOID) {
				body.append(ind).append(exprTo(b.asExpressionStmt().getExpression(), ret, l).code).append("\n");
			} else {
				block(b, body, ind);
			}
			String fn = fnType(to.cls);
			return new Ex(pre + "JFn::new(std::rc::Rc::new(move |" + sigs + "|" + (ret.k != K.VOID ? " -> " + rust(ret) : "") + " {\n" + body
					+ "    }) as std::rc::Rc<" + fn + ">) }", to, false);
		} finally {
			scope = saved;
			curReturn = savedReturn;
			targets.clear();
			targets.addAll(savedTargets);
		}
	}

	static String doubleLit(double d) {
		if (Double.isNaN(d) || Double.isInfinite(d)) return "f64::from_bits(0x" + Long.toHexString(Double.doubleToRawLongBits(d)) + ")";
		String s = Double.toString(d);
		return s + "f64";
	}

	static String floatLit(float f) {
		if (Float.isNaN(f) || Float.isInfinite(f)) return "f32::from_bits(0x" + Integer.toHexString(Float.floatToRawIntBits(f)) + ")";
		return Float.toString(f) + "f32";
	}

	/** Receiver of a method call or field access: a borrowed reference. */
	static final java.util.regex.Pattern FIELD_READ = java.util.regex.Pattern.compile("((?:r#)?[A-Za-z_]\\w*)\\.get\\(\\|o\\| o((?:\\.(?:r#)?\\w+)+)\\.clone\\(\\)\\)");

	/**
	 * For an array read from a field, `obj.get(|o| o.f.clone())` (or a static): the prefix
	 * and suffix that put an operation on the array inside the field access,
	 * `obj.get(|__o| __o.f` + `.at(i)` + `)`, so the reference isn't cloned
	 * (and dropped) for one element. Callers use it only when the index (and
	 * value) can't change the object, as the field is then read after them.
	 */
	String[] fieldArray(Ex arr) {
		if (arr.staticCell != null) return new String[] { arr.staticCell + ".with(|__o| __o", ")" };
		java.util.regex.Matcher m = FIELD_READ.matcher(arr.code);
		if (!m.matches()) return null;
		return new String[] { m.group(1) + ".get(|__o| __o" + m.group(2), ")" };
	}

	/** True when evaluating e has no side effects (no calls, assignments, ++/--, allocations). */
	static boolean readOnly(Expression e) {
		if (e.isEnclosedExpr()) return readOnly(e.asEnclosedExpr().getInner());
		if (e.isNameExpr() || e.isLiteralExpr() || e.isThisExpr()) return true;
		if (e.isFieldAccessExpr()) return readOnly(e.asFieldAccessExpr().getScope());
		if (e.isArrayAccessExpr()) return readOnly(e.asArrayAccessExpr().getName()) && readOnly(e.asArrayAccessExpr().getIndex());
		if (e.isBinaryExpr()) return readOnly(e.asBinaryExpr().getLeft()) && readOnly(e.asBinaryExpr().getRight());
		if (e.isCastExpr()) return readOnly(e.asCastExpr().getExpression());
		if (e.isConditionalExpr()) return readOnly(e.asConditionalExpr().getCondition()) && readOnly(e.asConditionalExpr().getThenExpr()) && readOnly(e.asConditionalExpr().getElseExpr());
		if (e.isUnaryExpr()) {
			UnaryExpr.Operator op = e.asUnaryExpr().getOperator();
			return op != UnaryExpr.Operator.PREFIX_INCREMENT && op != UnaryExpr.Operator.POSTFIX_INCREMENT
					&& op != UnaryExpr.Operator.PREFIX_DECREMENT && op != UnaryExpr.Operator.POSTFIX_DECREMENT
					&& readOnly(e.asUnaryExpr().getExpression());
		}
		return false;
	}

	String recv(Expression javaExpr, Ex x) {
		if (x.code.endsWith(".clone()") && isSimplePath(x.code.substring(0, x.code.length() - 8))) return x.code.substring(0, x.code.length() - 8);
		return "(" + x.code + ")";
	}

	static boolean isSimplePath(String s) {
		return s.matches("(r#)?[A-Za-z_][A-Za-z0-9_]*");
	}

	Ex name(NameExpr n) {
		Var v = scope.find(n.getNameAsString());
		if (v != null) {
			Ex x = new Ex(v.t.copy() || v.view ? v.rust : v.rust + ".clone()", v.t, true);
			x.view = v.view;
			return x;
		}
		ResolvedValueDeclaration r = n.resolve();
		if (r.isField()) return field(r.asField(), null, n);
		throw new Unsupported("name " + n);
	}

	Ex fieldAccess(FieldAccessExpr f) {
		if (f.getNameAsString().equals("length")) {
			try {
				ResolvedType st = f.getScope().calculateResolvedType();
				if (st.isArray()) {
					Ex a = expr(f.getScope());
					String[] fa = fieldArray(a);
					if (fa != null) return new Ex(fa[0] + ".len()" + fa[1], INT, false);
					return new Ex(recv(f.getScope(), a) + ".len()", INT, false);
				}
			} catch (RuntimeException ignored) {
				// scope is a type name
			}
		}
		ResolvedValueDeclaration r = f.resolve();
		if (!r.isField()) throw new Unsupported("field access " + f);
		ResolvedFieldDeclaration fd = r.asField();
		if (fd.isStatic()) return field(fd, null, f);
		return field(fd, f.getScope(), f);
	}

	/** A field read. scope == null means this (instance) or a static field. */
	Ex field(ResolvedFieldDeclaration fd, Expression scopeExpr, Node at) {
		String owner = fd.declaringType().getQualifiedName();
		JT t = jt(fd.getType());
		if (fd.isStatic() && stubTypes.contains(owner)) return new Ex("crate::rt::throw(\"J2R: stub field " + esc(simple(owner) + "." + fd.getName()) + "\")", t, false);
		if (fd.isStatic()) {
			if (owner.equals("java.awt.Color")) return new Ex("Color::" + fd.getName(), COLOR, true);
			if (BOXES.contains(owner)) return boxConstant(owner, fd.getName());
			if (owner.equals("java.lang.Math")) {
				if (fd.getName().equals("PI")) return new Ex("std::f64::consts::PI", DOUBLE, true);
				if (fd.getName().equals("E")) return new Ex("std::f64::consts::E", DOUBLE, true);
			}
			VariableDeclarator v = staticVar(fd);
			needStatic(v);
			FieldDeclaration decl = (FieldDeclaration) v.getParentNode().get();
			boolean isConst = decl.isFinal() && v.getInitializer().isPresent() && isConstExpr(v.getInitializer().get());
			String path = moduleName(owner) + "::" + rustIdent(v.getNameAsString());
			if (isConst) return new Ex(path, t, true);
			Ex x = new Ex(path + "()", t, false);
			x.staticCell = moduleName(owner) + "::__" + v.getNameAsString();
			return x;
		}
		String obj;
		JT objT;
		if (scopeExpr == null || scopeExpr.isThisExpr()) {
			if (inStatic) throw new Unsupported("instance field in static context");
			obj = "this";
			objT = new JT(K.CLASS, curClass, null);
		} else {
			Ex o = expr(scopeExpr);
			obj = recv(scopeExpr, o);
			objT = o.t;
		}
		String path = fieldPath(owner, objT, fd.getName(), false);
		return new Ex(obj + ".get(|o| " + path + (t.copy() ? "" : ".clone()") + ")", t, false);
	}

	/** o.f, or o.__super.f / o.rect().f for fields declared in a JDK geometry class. */
	String fieldPath(String owner, JT objT, String f, boolean write) {
		if (GEO_OWNERS.containsKey(owner)) return geoPath(objT, write) + "." + f;
		if (!classes.containsKey(owner)) throw new Unsupported("field of " + owner);
		return "o." + rustIdent(f);
	}

	static final Set<String> BOXES = new java.util.HashSet<>(java.util.Arrays.asList("java.lang.Double", "java.lang.Float",
			"java.lang.Integer", "java.lang.Long", "java.lang.Short", "java.lang.Byte", "java.lang.Character"));

	/** Double.MAX_VALUE etc.: the value, read from this JVM. */
	Ex boxConstant(String owner, String name) {
		Object v;
		try {
			v = Class.forName(owner).getField(name).get(null);
		} catch (ReflectiveOperationException e) {
			throw new Unsupported(owner + "." + name);
		}
		Ex r;
		if (v instanceof java.lang.Double) r = new Ex(doubleLit((java.lang.Double) v), DOUBLE, true);
		else if (v instanceof Float) r = new Ex(floatLit((Float) v), FLOAT, true);
		else if (v instanceof Character) r = new Ex((int) (Character) v + "u16", CHAR, true);
		else if (v instanceof Integer) { r = new Ex("(" + v + "i32)", INT, true); r.intLit = ((Integer) v).longValue(); }
		else if (v instanceof Long) { r = new Ex("(" + v + "i64)", LONG, true); r.intLit = (Long) v; }
		else if (v instanceof Short) r = new Ex("(" + v + "i16)", JT.of(K.SHORT), true);
		else if (v instanceof Byte) r = new Ex("(" + v + "i8)", JT.of(K.BYTE), true);
		else throw new Unsupported(owner + "." + name);
		return r;
	}

	/** The declaration in our own ASTs (the resolver's toAst() points into its private copies). */
	VariableDeclarator staticVar(ResolvedFieldDeclaration fd) {
		ClassOrInterfaceDeclaration c = allTypes.get(fd.declaringType().getQualifiedName());
		if (c == null) throw new Unsupported("static field of " + fd.declaringType().getQualifiedName());
		for (FieldDeclaration f : c.getFields())
			for (VariableDeclarator v : f.getVariables()) if (v.getNameAsString().equals(fd.getName())) return v;
		throw new Unsupported("static field " + fd.getName());
	}

	MethodDeclaration method(String owner, String sig) {
		ClassOrInterfaceDeclaration c = allTypes.get(owner);
		if (c != null) for (MethodDeclaration m : c.getMethods()) if (sig(m).equals(sig)) return m;
		throw new Unsupported("no source for " + owner + "." + sig);
	}

	JT promote(JT t) {
		if (t.k == K.CHAR || t.k == K.BYTE || t.k == K.SHORT) return INT;
		return t;
	}

	JT binaryPromote(JT a, JT b) {
		if (a.k == K.DOUBLE || b.k == K.DOUBLE) return DOUBLE;
		if (a.k == K.FLOAT || b.k == K.FLOAT) return FLOAT;
		if (a.k == K.LONG || b.k == K.LONG) return LONG;
		return INT;
	}

	/** Conversion in assignment / invocation / numeric-promotion contexts. */
	Ex coerce(Ex x, JT to, Node at) {
		if (x.t.equals(to)) return x;
		if (x.t.k == K.NULL && to.ref()) return new Ex(defaultValue(to), to, true);
		if (x.t.numeric() && to.numeric()) {
			if (x.intLit != null) {
				long v = x.intLit;
				switch (to.k) {
					case DOUBLE: return new Ex(v < 0 ? "(" + v + "f64)" : v + "f64", to, true);
					case FLOAT: return new Ex(v < 0 ? "(" + v + "f32)" : v + "f32", to, true);
					case LONG: { Ex r = new Ex(v < 0 ? "(" + v + "i64)" : v + "i64", to, true); r.intLit = v; return r; }
					default: break;
				}
			}
			return new Ex("(" + x.code + " as " + rust(to) + ")", to, x.pure);
		}
		if (to.k == K.OBJECT && x.t.ref()) return x;
		if (to.k == K.COLL && (x.t.k == K.LIST || x.t.k == K.HSET) && x.t.elem.equals(to.elem))
			return new Ex(paren(x.code) + ".as_collection()", to, x.pure);
		if (to.k == K.GEOM && x.t.k == K.CLASS && to.cls.equals(superGeom.get(x.t.cls)))
			return new Ex(paren(x.code) + "." + GEO.get(to.cls).upcast + "()", to, x.pure);
		throw new Unsupported("conversion " + x.t + " -> " + to);
	}

	Ex cast(CastExpr c) {
		JT to = jt(c.getType().resolve());
		Ex x = expr(c.getExpression());
		if (x.t.equals(to)) return x;
		if (x.t.numeric() && to.numeric()) {
			// char <- int etc. are all plain `as` in Rust, with Java's semantics
			// (saturating float->int, NaN -> 0, truncating int narrowing).
			if ((x.t.k == K.DOUBLE || x.t.k == K.FLOAT) && (to.k == K.BYTE || to.k == K.SHORT || to.k == K.CHAR))
				return new Ex("((" + x.code + " as i32) as " + rust(to) + ")", to, x.pure); // Java goes through int
			return new Ex("(" + x.code + " as " + rust(to) + ")", to, x.pure);
		}
		if (x.t.k == K.OBJECT && to.ref()) return new Ex(x.code, to, x.pure); // only clone() yields Object here
		throw new Unsupported("cast " + x.t + " -> " + to);
	}

	Ex unary(UnaryExpr u) {
		switch (u.getOperator()) {
			case LOGICAL_COMPLEMENT: {
				Ex x = coerce(expr(u.getExpression()), BOOL, u);
				return new Ex("!" + paren(x.code), BOOL, x.pure);
			}
			case MINUS: {
				Ex x = expr(u.getExpression());
				JT t = promote(x.t);
				x = coerce(x, t, u);
				Ex r = new Ex("-" + paren(x.code), t, x.pure);
				if (x.intLit != null) {
					// suffixed: in `(-5) as u32` Rust would infer the literal as u32
					boolean digits = x.code.matches("[0-9]+");
					r = new Ex("(-" + x.code + (digits && t.k == K.INT ? "i32" : "") + ")", t, true);
					r.intLit = -x.intLit;
				}
				return r;
			}
			case PLUS: {
				Ex x = expr(u.getExpression());
				return coerce(x, promote(x.t), u);
			}
			case BITWISE_COMPLEMENT: {
				Ex x = expr(u.getExpression());
				JT t = promote(x.t);
				x = coerce(x, t, u);
				return new Ex("!" + paren(x.code), t, x.pure);
			}
			default:
				return incDec(u, true);
		}
	}

	static String paren(String code) {
		return isSimplePath(code) || code.matches("[0-9.]+[a-z0-9]*") || (code.startsWith("(") && balanced(code)) ? code : "(" + code + ")";
	}

	/** True if code is one parenthesized group: "(...)". */
	static boolean balanced(String code) {
		int depth = 0;
		for (int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);
			if (c == '(') depth++;
			else if (c == ')') {
				depth--;
				if (depth == 0 && i != code.length() - 1) return false;
			}
		}
		return depth == 0;
	}

	Ex binary(BinaryExpr b) {
		BinaryExpr.Operator op = b.getOperator();
		if (op == BinaryExpr.Operator.PLUS && isString(b)) return concat(b);
		Ex l = expr(b.getLeft()), r = expr(b.getRight());
		boolean pure = l.pure && r.pure;
		switch (op) {
			case OR: case AND: {
				l = coerce(l, BOOL, b);
				r = coerce(r, BOOL, b);
				return new Ex("(" + l.code + (op == BinaryExpr.Operator.OR ? " || " : " && ") + r.code + ")", BOOL, pure);
			}
			case EQUALS: case NOT_EQUALS: {
				String o = op == BinaryExpr.Operator.EQUALS ? " == " : " != ";
				if (l.t.numeric() && r.t.numeric()) {
					JT t = binaryPromote(l.t, r.t);
					return new Ex("(" + coerce(l, t, b).code + o + coerce(r, t, b).code + ")", BOOL, pure);
				}
				if (l.t.k == K.BOOL && r.t.k == K.BOOL) return new Ex("(" + l.code + o + r.code + ")", BOOL, pure);
				// reference comparison
				String neg = op == BinaryExpr.Operator.EQUALS ? "" : "!";
				if (r.t.k == K.NULL && l.t.ref()) return new Ex("(" + neg + recv(b.getLeft(), l) + ".is_null())", BOOL, false);
				if (l.t.k == K.NULL && r.t.ref()) return new Ex("(" + neg + recv(b.getRight(), r) + ".is_null())", BOOL, false);
				if (l.t.k == K.CLASS || l.t.k == K.ARRAY || l.t.k == K.LIST || l.t.k == K.RANDOM || l.t.k == K.GEOM || l.t.k == K.COLL || l.t.k == K.HSET)
					return new Ex("(" + neg + "JRef::ptr_eq(&" + l.code + ", &" + r.code + "))", BOOL, false);
				throw new Unsupported("== on " + l.t + " (identity of value-typed objects)");
			}
			case LESS: case LESS_EQUALS: case GREATER: case GREATER_EQUALS: {
				JT t = binaryPromote(l.t, r.t);
				return new Ex("(" + coerce(l, t, b).code + " " + op.asString() + " " + coerce(r, t, b).code + ")", BOOL, pure);
			}
			case BINARY_AND: case BINARY_OR: case XOR: {
				if (l.t.k == K.BOOL && r.t.k == K.BOOL) return new Ex("(" + l.code + " " + op.asString() + " " + r.code + ")", BOOL, pure);
				JT t = binaryPromote(l.t, r.t);
				return new Ex("(" + coerce(l, t, b).code + " " + op.asString() + " " + coerce(r, t, b).code + ")", t, pure);
			}
			case LEFT_SHIFT: case SIGNED_RIGHT_SHIFT: case UNSIGNED_RIGHT_SHIFT: {
				JT t = promote(l.t);
				String lc = coerce(l, t, b).code, rc = coerce(r, promote(r.t), b).code;
				String n = "(" + rc + ") as u32";
				if (op == BinaryExpr.Operator.LEFT_SHIFT) return new Ex(rust(t) + "::wrapping_shl(" + lc + ", " + n + ")", t, pure);
				if (op == BinaryExpr.Operator.SIGNED_RIGHT_SHIFT) return new Ex(rust(t) + "::wrapping_shr(" + lc + ", " + n + ")", t, pure);
				String u = t.k == K.LONG ? "u64" : "u32";
				return new Ex("((" + lc + " as " + u + ").wrapping_shr(" + n + ") as " + rust(t) + ")", t, pure);
			}
			case PLUS: case MINUS: case MULTIPLY: case DIVIDE: case REMAINDER: {
				JT t = binaryPromote(l.t, r.t);
				String lc = coerce(l, t, b).code, rc = coerce(r, t, b).code;
				if (t.integral() && op == BinaryExpr.Operator.DIVIDE) return new Ex(rust(t) + "::wrapping_div(" + lc + ", " + rc + ")", t, pure);
				if (t.integral() && op == BinaryExpr.Operator.REMAINDER) return new Ex(rust(t) + "::wrapping_rem(" + lc + ", " + rc + ")", t, pure);
				// + - * wrap (overflow-checks are off); float ops are IEEE like Java's
				return new Ex("(" + lc + " " + op.asString() + " " + rc + ")", t, pure);
			}
			default:
				throw new Unsupported("operator " + op);
		}
	}

	static boolean isString(Expression e) {
		ResolvedType t = e.calculateResolvedType();
		return t.isReferenceType() && t.asReferenceType().getQualifiedName().equals("java.lang.String");
	}

	/** a + b + ... with a String among the first two: one JStr::cat of all parts, left to right. */
	Ex concat(BinaryExpr b) {
		List<Expression> parts = new ArrayList<>();
		Expression e = b;
		while (e.isBinaryExpr() && e.asBinaryExpr().getOperator() == BinaryExpr.Operator.PLUS && isString(e)) {
			parts.add(0, e.asBinaryExpr().getRight());
			e = e.asBinaryExpr().getLeft();
		}
		parts.add(0, e);
		List<Ex> xs = new ArrayList<>();
		for (Expression p : parts) xs.add(expr(p));
		return catCode(xs);
	}

	Ex catCode(List<Ex> parts) {
		StringBuilder s = new StringBuilder("JStr::cat(&[");
		for (int i = 0; i < parts.size(); i++) {
			Ex x = parts.get(i);
			if (!(x.t.integral() || x.t.k == K.BOOL || x.t.k == K.STRING)) throw new Unsupported("string conversion of " + x.t);
			s.append(i > 0 ? ", &" : "&").append(x.code);
		}
		return new Ex(s.append("])").toString(), STRING, false);
	}

	// ---------------------------------------------------------------- lvalues

	/**
	 * Something assignable. Rust evaluates a call's receiver and arguments left
	 * to right, like Java evaluates an assignment's target and value, so
	 * `obj.setf(|o| &mut o.f, value)` and `arr.put(i, value)` keep Java's order.
	 * setup binds temporaries when the target is read and written (x op= v).
	 */
	final class LValue {
		String setup = "";   // statements binding the target's parts (compound forms only)
		String read;         // expression reading the current value (after setup)
		String writePrefix;  // write(v) = writePrefix + v + writeSuffix (after setup)
		String writeSuffix;
		String directPrefix; // plain assignment without setup: directPrefix + v + writeSuffix
		JT t;
		String write(String v) { return writePrefix + v + writeSuffix; }
		String direct(String v) { return directPrefix + v + writeSuffix; }
	}

	int tmpCounter;

	LValue lvalue(Expression target) {
		LValue lv = new LValue();
		if (target.isEnclosedExpr()) return lvalue(target.asEnclosedExpr().getInner());
		if (target.isNameExpr()) {
			Var v = scope.find(target.asNameExpr().getNameAsString());
			if (v != null) {
				lv.t = v.t;
				lv.read = v.t.copy() ? v.rust : v.rust + ".clone()";
				lv.writePrefix = lv.directPrefix = v.rust + " = ";
				lv.writeSuffix = "";
				return lv;
			}
			ResolvedValueDeclaration r = target.asNameExpr().resolve();
			if (!r.isField()) throw new Unsupported("assign to " + target);
			return fieldLValue(r.asField(), null);
		}
		if (target.isFieldAccessExpr()) {
			ResolvedValueDeclaration r = target.asFieldAccessExpr().resolve();
			if (!r.isField()) throw new Unsupported("assign to " + target);
			return fieldLValue(r.asField(), r.asField().isStatic() ? null : target.asFieldAccessExpr().getScope());
		}
		if (target.isArrayAccessExpr()) {
			ArrayAccessExpr a = target.asArrayAccessExpr();
			Ex arr = expr(a.getName());
			if (arr.t.k != K.ARRAY) throw new Unsupported("index into " + arr.t);
			Ex idx = coerce(expr(a.getIndex()), INT, a);
			int n = ++tmpCounter;
			lv.t = arr.t.elem;
			lv.directPrefix = recv(a.getName(), arr) + ".put(" + idx.code + ", ";
			lv.setup = "let __a" + n + " = " + arr.code + "; let __i" + n + " = " + idx.code + "; ";
			lv.read = "__a" + n + ".at(__i" + n + ")";
			lv.writePrefix = "__a" + n + ".put(__i" + n + ", ";
			lv.writeSuffix = ")";
			return lv;
		}
		throw new Unsupported("assign to " + target);
	}

	LValue fieldLValue(ResolvedFieldDeclaration fd, Expression scopeExpr) {
		LValue lv = new LValue();
		lv.t = jt(fd.getType());
		String owner = fd.declaringType().getQualifiedName();
		if (fd.isStatic()) {
			VariableDeclarator v = staticVar(fd);
			needStatic(v);
			String m = moduleName(owner) + "::";
			lv.read = m + rustIdent(v.getNameAsString()) + "()";
			lv.writePrefix = lv.directPrefix = m + "set_" + v.getNameAsString() + "(";
			lv.writeSuffix = ")";
			return lv;
		}
		String obj;
		Ex o = null;
		JT objT;
		if (scopeExpr == null || scopeExpr.isThisExpr()) {
			if (inStatic) throw new Unsupported("instance field in static context");
			objT = new JT(K.CLASS, curClass, null);
		} else {
			o = expr(scopeExpr);
			objT = o.t;
		}
		String f = fieldPath(owner, objT, fd.getName(), false).substring(2);  // without "o."
		String fw = fieldPath(owner, objT, fd.getName(), true).substring(2);
		if (o == null) {
			obj = "this";
			lv.directPrefix = "this.setf(|o| &mut o." + fw + ", ";
		} else {
			String r = recv(scopeExpr, o);
			lv.directPrefix = r + ".setf(|o| &mut o." + fw + ", ";
			if (isSimplePath(r)) obj = r;
			else {
				int n = ++tmpCounter;
				lv.setup = "let __o" + n + " = " + o.code + "; ";
				obj = "__o" + n;
			}
		}
		lv.read = obj + ".get(|o| o." + f + (lv.t.copy() ? "" : ".clone()") + ")";
		lv.writePrefix = obj + ".setf(|o| &mut o." + fw + ", ";
		lv.writeSuffix = ")";
		return lv;
	}

	boolean isStubStaticField(Expression target) {
		if (!target.isFieldAccessExpr() && !target.isNameExpr()) return false;
		if (target.isNameExpr() && scope.find(target.asNameExpr().getNameAsString()) != null) return false;
		ResolvedValueDeclaration r = target.isNameExpr() ? target.asNameExpr().resolve() : target.asFieldAccessExpr().resolve();
		return r.isField() && r.asField().isStatic() && stubTypes.contains(r.asField().declaringType().getQualifiedName());
	}

	/** x = v, x op= v. asExpr: the assignment's value is used. */
	Ex assign(AssignExpr a, boolean asExpr) {
		AssignExpr.Operator op = a.getOperator();
		if (!asExpr && op == AssignExpr.Operator.ASSIGN && a.getValue().isAssignExpr()) return assignChain(a);
		if (!asExpr && op == AssignExpr.Operator.ASSIGN && isStubStaticField(a.getTarget())) {
			// VisibleWorld.zoomFactor = 1: the GUI's, dropped
			requireNoSideEffects(a.getValue());
			return new Ex("()", VOID, true);
		}
		if (!asExpr && op == AssignExpr.Operator.ASSIGN && a.getTarget().isArrayAccessExpr()
				&& readOnly(a.getTarget().asArrayAccessExpr().getIndex()) && readOnly(a.getValue())) {
			// x[i] = v with x a field: x.get(|__o| __o.x.put(i, v)), see fieldArray
			ArrayAccessExpr aa = a.getTarget().asArrayAccessExpr();
			Ex arr = expr(aa.getName());
			String[] fa = arr.t.k == K.ARRAY ? fieldArray(arr) : null;
			if (fa != null) {
				Ex idx = coerce(expr(aa.getIndex()), INT, aa);
				return new Ex(fa[0] + ".put(" + idx.code + ", " + exprTo(a.getValue(), arr.t.elem, a).code + ")" + fa[1], VOID, false);
			}
		}
		LValue lv = lvalue(a.getTarget());
		String value;
		if (op == AssignExpr.Operator.ASSIGN) {
			value = a.getValue().isAssignExpr() ? coerce(assign(a.getValue().asAssignExpr(), true), lv.t, a).code : exprTo(a.getValue(), lv.t, a).code;
			if (!asExpr) return new Ex(lv.direct(value), VOID, false);
		} else {
			// Java: read the target, evaluate the value, store (T)(old op value).
			BinaryExpr.Operator bop = op.toBinaryOperator().get();
			Ex rv = expr(a.getValue());
			if (lv.t.k == K.STRING && bop == BinaryExpr.Operator.PLUS) {
				value = catCode(java.util.Arrays.asList(new Ex(lv.read, STRING, false), rv)).code;
				if (!asExpr) return new Ex(lv.setup.isEmpty() ? lv.write(value) : "{ " + lv.setup + lv.write(value) + "; }", VOID, false);
				int n = ++tmpCounter;
				return new Ex("{ " + lv.setup + "let __v" + n + ": JStr = " + value + "; " + lv.write("__v" + n + ".clone()") + "; __v" + n + " }", STRING, false);
			}
			if (lv.t.k == K.STRING || rv.t.k == K.STRING) throw new Unsupported("string " + bop);
			boolean shift = bop == BinaryExpr.Operator.LEFT_SHIFT || bop == BinaryExpr.Operator.SIGNED_RIGHT_SHIFT || bop == BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT;
			JT t = shift ? promote(lv.t) : (lv.t.k == K.BOOL ? BOOL : binaryPromote(lv.t, rv.t));
			value = castTo(binaryCode(bop, new Ex(lv.read, lv.t, false), rv, t, a), lv.t).code;
			if (!asExpr) return new Ex(lv.setup.isEmpty() ? lv.write(value) : "{ " + lv.setup + lv.write(value) + "; }", VOID, false);
		}
		int n = ++tmpCounter;
		String tmp = "__v" + n;
		return new Ex("{ " + lv.setup + "let " + tmp + ": " + rust(lv.t) + " = " + value + "; " + lv.write(lv.t.copy() ? tmp : tmp + ".clone()") + "; " + tmp + " }", lv.t, false);
	}

	/** a = b = ... = v as a statement: evaluate v once, assign right to left. */
	Ex assignChain(AssignExpr a) {
		List<Expression> targets = new ArrayList<>();
		Expression e = a;
		while (e.isAssignExpr() && e.asAssignExpr().getOperator() == AssignExpr.Operator.ASSIGN) {
			targets.add(e.asAssignExpr().getTarget());
			e = e.asAssignExpr().getValue();
		}
		List<LValue> lvs = new ArrayList<>();
		StringBuilder s = new StringBuilder("{ ");
		for (Expression t : targets) {
			LValue lv = lvalue(t);
			s.append(lv.setup);
			lvs.add(lv);
		}
		Ex v = expr(e);
		for (int i = lvs.size() - 1; i >= 0; i--) {
			LValue lv = lvs.get(i);
			int n = ++tmpCounter;
			v = coerce(v, lv.t, a);
			s.append("let __v").append(n).append(" = ").append(v.code).append("; ").append(lv.write(lv.t.copy() ? "__v" + n : "__v" + n + ".clone()")).append("; ");
			v = new Ex("__v" + n, lv.t, true);
		}
		return new Ex(s.append("}").toString(), VOID, false);
	}

	/** Numeric binary operation on already translated operands, promoted to t. */
	Ex binaryCode(BinaryExpr.Operator op, Ex l, Ex r, JT t, Node at) {
		if (t.k == K.BOOL) {
			if (op == BinaryExpr.Operator.BINARY_AND || op == BinaryExpr.Operator.BINARY_OR || op == BinaryExpr.Operator.XOR)
				return new Ex("(" + l.code + " " + op.asString() + " " + r.code + ")", BOOL, false);
			throw new Unsupported("boolean " + op);
		}
		String lc = coerce(l, t, at).code;
		switch (op) {
			case LEFT_SHIFT: return new Ex(rust(t) + "::wrapping_shl(" + lc + ", (" + coerce(r, promote(r.t), at).code + ") as u32)", t, false);
			case SIGNED_RIGHT_SHIFT: return new Ex(rust(t) + "::wrapping_shr(" + lc + ", (" + coerce(r, promote(r.t), at).code + ") as u32)", t, false);
			case UNSIGNED_RIGHT_SHIFT: {
				String u = t.k == K.LONG ? "u64" : "u32";
				return new Ex("((" + lc + " as " + u + ").wrapping_shr((" + coerce(r, promote(r.t), at).code + ") as u32) as " + rust(t) + ")", t, false);
			}
			default: break;
		}
		String rc = coerce(r, t, at).code;
		if (t.integral() && op == BinaryExpr.Operator.DIVIDE) return new Ex(rust(t) + "::wrapping_div(" + lc + ", " + rc + ")", t, false);
		if (t.integral() && op == BinaryExpr.Operator.REMAINDER) return new Ex(rust(t) + "::wrapping_rem(" + lc + ", " + rc + ")", t, false);
		return new Ex("(" + lc + " " + op.asString() + " " + rc + ")", t, false);
	}

	/** The implicit narrowing cast of compound assignment. */
	Ex castTo(Ex x, JT to) {
		if (x.t.equals(to)) return x;
		if (x.t.numeric() && to.numeric()) return new Ex("(" + x.code + " as " + rust(to) + ")", to, x.pure);
		throw new Unsupported("compound assignment " + x.t + " -> " + to);
	}

	Ex incDec(UnaryExpr u, boolean asExpr) {
		LValue lv = lvalue(u.getExpression());
		if (!lv.t.numeric()) throw new Unsupported("++ on " + lv.t);
		boolean inc = u.getOperator() == UnaryExpr.Operator.PREFIX_INCREMENT || u.getOperator() == UnaryExpr.Operator.POSTFIX_INCREMENT;
		boolean prefix = u.getOperator() == UnaryExpr.Operator.PREFIX_INCREMENT || u.getOperator() == UnaryExpr.Operator.PREFIX_DECREMENT;
		String one = lv.t.k == K.DOUBLE ? "1f64" : lv.t.k == K.FLOAT ? "1f32" : "1";
		String sign = inc ? " + " : " - ";
		JT pt = promote(lv.t);
		if (!asExpr) {
			String v = castTo(new Ex(coerce(new Ex(lv.read, lv.t, false), pt, u).code + sign + one, pt, false), lv.t).code;
			return new Ex(lv.setup.isEmpty() ? lv.write(v) : "{ " + lv.setup + lv.write(v) + "; }", VOID, false);
		}
		int n = ++tmpCounter;
		String old = "__old" + n, nw = "__new" + n;
		String calc = castTo(new Ex("(" + coerce(new Ex(old, lv.t, true), pt, u).code + sign + one + ")", pt, false), lv.t).code;
		return new Ex("{ " + lv.setup + "let " + old + ": " + rust(lv.t) + " = " + lv.read + "; let " + nw + ": " + rust(lv.t) + " = " + calc + "; "
				+ lv.write(nw) + "; " + (prefix ? nw : old) + " }", lv.t, false);
	}

	// ---------------------------------------------------------------- calls

	interface ParamType { ResolvedType get(int i); }

	String args(List<Expression> as, java.util.function.IntFunction<com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration> param, int n, Node at, boolean leadingComma) {
		StringBuilder s = new StringBuilder();
		if (as.size() != n) throw new Unsupported("varargs call");
		for (int i = 0; i < as.size(); i++) {
			if (s.length() > 0 || leadingComma) s.append(", ");
			JT pt = jt(param.apply(i).getType());
			s.append(exprTo(as.get(i), pt, at).code);
		}
		return s.toString();
	}

	/** Arguments of a static method with non-escaping array parameters: those get a reference. */
	String viewArgs(List<Expression> as, MethodDeclaration md, Node at) {
		StringBuilder s = new StringBuilder();
		if (as.size() != md.getParameters().size()) throw new Unsupported("varargs call");
		for (int i = 0; i < as.size(); i++) {
			if (i > 0) s.append(", ");
			Parameter p = md.getParameter(i);
			Ex x = exprTo(as.get(i), jt(p.getType().resolve()), at);
			if (!viewParam(p) || x.view) s.append(x.code);
			else s.append("&").append(recv(as.get(i), x));
		}
		return s.toString();
	}

	// ---------------------------------------------------------------- arrays that don't escape

	/*
	 * HotSpot allocates a `new double[2]` that never leaves the method on the
	 * stack (escape analysis). FdLibm's sin/cos/pow do that on every call, so
	 * J2R does the same for the simple cases: a local primitive array
	 * created with a constant length, or a parameter of a static method,
	 * whose every use is an element access, `.length`, or an argument to such
	 * a parameter. Those parameters take `&impl JArr<T>`, the locals are
	 * `JStackArray`s.
	 */

	/** Parameters that don't escape, by identity (computed for all static methods at once). */
	java.util.Set<Parameter> viewParams;

	boolean viewParam(Parameter p) {
		if (viewParams == null) computeViewParams();
		return viewParams.contains(p);
	}

	static boolean primitiveArray(ResolvedType t) {
		return t.isArray() && t.asArrayType().getComponentType().isPrimitive();
	}

	void computeViewParams() {
		viewParams = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
		List<Parameter> candidates = new ArrayList<>();
		for (Map.Entry<String, ClassOrInterfaceDeclaration> e : allTypes.entrySet()) {
			for (MethodDeclaration m : e.getValue().getMethods()) {
				try {
					if (!m.isStatic() || !m.getBody().isPresent() || skipped(e.getKey(), m) || natives.contains(e.getKey() + "." + sig(m))) continue;
					for (Parameter p : m.getParameters()) if (primitiveArray(p.getType().resolve())) candidates.add(p);
				} catch (RuntimeException ignored) {
					// a type outside the parsed sources: not a candidate
				}
			}
		}
		viewParams.addAll(candidates);
		// Drop parameters that escape until none does (optimistic: calls among candidates are fine).
		for (boolean changed = true; changed; ) {
			changed = false;
			for (Parameter p : candidates) {
				if (!viewParams.contains(p)) continue;
				MethodDeclaration m = (MethodDeclaration) p.getParentNode().get();
				if (!staysLocal(p.getNameAsString(), m)) {
					viewParams.remove(p);
					changed = true;
				}
			}
		}
	}

	/** Every use of the array variable `name` in `c` is a[i], a.length, or a non-escaping parameter's argument. */
	boolean staysLocal(String name, CallableDeclaration<?> c) {
		int declared = 0;
		for (Parameter p : c.findAll(Parameter.class)) if (p.getNameAsString().equals(name)) declared++;
		for (VariableDeclarator v : c.findAll(VariableDeclarator.class)) if (v.getNameAsString().equals(name)) declared++;
		if (declared != 1) return false; // shadowed or reused: don't bother
		for (NameExpr n : c.findAll(NameExpr.class)) {
			if (!n.getNameAsString().equals(name)) continue;
			Node parent = n.getParentNode().orElse(null);
			if (parent instanceof ArrayAccessExpr && ((ArrayAccessExpr) parent).getName() == n) continue;
			if (parent instanceof FieldAccessExpr && ((FieldAccessExpr) parent).getNameAsString().equals("length")) continue;
			if (parent instanceof MethodCallExpr && passedToViewParam((MethodCallExpr) parent, n)) continue;
			return false;
		}
		return true;
	}

	boolean passedToViewParam(MethodCallExpr call, NameExpr arg) {
		int k = -1;
		for (int i = 0; i < call.getArguments().size(); i++) if (call.getArgument(i) == arg) k = i;
		if (k < 0) return false; // the receiver
		try {
			ResolvedMethodDeclaration rm = call.resolve();
			if (!rm.isStatic()) return false;
			String owner = rm.declaringType().getQualifiedName();
			if (!allTypes.containsKey(owner)) return false;
			MethodDeclaration md = method(owner, rm.getSignature().replace(" ", ""));
			return k < md.getParameters().size() && viewParams.contains(md.getParameter(k));
		} catch (RuntimeException e) {
			return false;
		}
	}

	/** The JStackArray for a local that can be one (see above), else null. */
	String stackArray(VariableDeclarator v, JT t) {
		if (t.k != K.ARRAY || !v.getInitializer().isPresent()) return null;
		try {
			if (!primitiveArray(v.getType().resolve()) || v.getType().getArrayLevel() != 1) return null;
		} catch (RuntimeException e) {
			return null;
		}
		Expression init = v.getInitializer().get();
		ArrayInitializerExpr values = null;
		String length = null;
		if (init.isArrayInitializerExpr()) values = init.asArrayInitializerExpr();
		else if (init.isArrayCreationExpr()) {
			ArrayCreationExpr a = init.asArrayCreationExpr();
			if (a.getLevels().size() != 1) return null;
			if (a.getInitializer().isPresent()) values = a.getInitializer().get();
			else if (a.getLevels().get(0).getDimension().map(Expression::isIntegerLiteralExpr).orElse(false))
				length = a.getLevels().get(0).getDimension().get().asIntegerLiteralExpr().getValue();
			else return null;
		} else return null;
		CallableDeclaration<?> c = v.findAncestor(CallableDeclaration.class).orElse(null);
		if (c == null) return null;
		if (viewParams == null) computeViewParams();
		if (!staysLocal(v.getNameAsString(), c)) return null;
		if (length != null) return "JStackArray::<" + rust(t.elem) + ", " + length + ">::new(" + defaultValue(t.elem) + ")";
		StringBuilder s = new StringBuilder("JStackArray::from([");
		boolean first = true;
		for (Expression e : values.getValues()) {
			if (!first) s.append(", ");
			first = false;
			s.append(coerce(expr(e), t.elem, e).code);
		}
		return s.append("])").toString();
	}

	Ex call(MethodCallExpr m) {
		ResolvedMethodDeclaration rm = m.resolve();
		String owner = rm.declaringType().getQualifiedName();
		String name = rm.getName();
		String sig = rm.getSignature().replace(" ", "");
		List<Expression> as = m.getArguments();

		// super.clone() in a translated class: shallow copy
		if (name.equals("clone") && as.isEmpty() && m.getScope().isPresent() && m.getScope().get().isSuperExpr()) {
			return new Ex("JRef::new(this.get(|o| o.clone()))", OBJECT, false);
		}
		if (isStubCall(m, rm)) return stubCall(m, rm);
		// A lambda's method
		if (m.getScope().isPresent() && sam(owner) != null && !rm.isStatic()) {
			Ex f = expr(m.getScope().get());
			if (f.t.k == K.FN) {
				String a = args(as, rm::getParam, rm.getNumberOfParams(), m, false);
				return new Ex("(" + recv(m.getScope().get(), f) + ".f())(" + a + ")", jt(rm.getReturnType()), false);
			}
		}
		switch (owner) {
			case "java.util.Random": return randomCall(m, rm, sig);
			case "java.lang.Math":
			case "java.lang.StrictMath": return mathCall(m, owner, name, sig);
			case "java.awt.Color": return colorCall(m, name, sig);
			case "java.lang.String": return stringCall(m, name, sig);
			case "java.lang.Integer": return integerCall(m, sig);
			case "java.lang.Double": return doubleCall(m, sig);
			case "java.lang.Long": return longCall(m, sig);
			case "java.util.List":
			case "java.util.ArrayList":
			case "java.util.Collection":
			case "java.util.Set":
			case "java.util.HashSet":
			case "java.util.AbstractCollection":
			case "java.util.AbstractList": return collectionCall(m, name, sig);
			case "java.util.Iterator": return iteratorCall(m, sig);
			default: if (GEO_OWNERS.containsKey(owner)) return geomCall(m, owner, sig);
				break;
		}
		if (!allTypes.containsKey(owner)) throw new Unsupported("call " + owner + "." + sig);
		JT ret;
		if (name.equals("clone") && as.isEmpty() && classes.containsKey(owner)) ret = new JT(K.CLASS, owner, null);
		else ret = jt(rm.getReturnType());
		if (rm.isStatic()) {
			MethodDeclaration md = method(owner, sig);
			if (!callableNames.containsKey(owner + "." + sig)) nameCallables(allTypes.get(owner));
			needStatic(md);
			String fn = moduleName(owner) + "::" + callableNames.get(owner + "." + sig);
			if (md.getParameters().stream().anyMatch(this::viewParam)) return new Ex(fn + "(" + viewArgs(as, md, m) + ")", ret, false);
			return new Ex(fn + "(" + args(as, rm::getParam, rm.getNumberOfParams(), m, false) + ")", ret, false);
		}
		if (!classes.containsKey(owner)) throw new Unsupported("instance method of " + owner);
		String fn = simple(owner) + "::" + callableNames.get(owner + "." + sig);
		String recv;
		if (!m.getScope().isPresent() || m.getScope().get().isThisExpr()) {
			if (inStatic) throw new Unsupported("instance call in static context");
			recv = "this";
		} else {
			Ex s = expr(m.getScope().get());
			recv = "&" + recv(m.getScope().get(), s);
		}
		return new Ex(fn + "(" + recv + args(as, rm::getParam, rm.getNumberOfParams(), m, true) + ")", ret, false);
	}

	String scopeRecv(MethodCallExpr m) {
		Expression s = m.getScope().orElseThrow(() -> new Unsupported("call without receiver"));
		return "&" + recv(s, expr(s));
	}

	/** A borrowed reference to x's object: `&name` or `&(expr)`. */
	String recvRef(Ex x) {
		if (x.code.equals("this.clone()")) return "this"; // already a reference
		if (x.code.endsWith(".clone()") && isSimplePath(x.code.substring(0, x.code.length() - 8))) return "&" + x.code.substring(0, x.code.length() - 8);
		return "&(" + x.code + ")";
	}

	/**
	 * A method of a JDK geometry class. Receiver and arguments are evaluated
	 * first (Java's order), geometry arguments as copies, then the rt method
	 * runs on the borrowed struct.
	 */
	Ex geomCall(MethodCallExpr m, String owner, String sig) {
		GeoMethod gm = GEO_METHODS.get(sig);
		if (gm == null) throw new Unsupported(simple(owner) + "." + sig);
		StringBuilder b = new StringBuilder("{ ");
		JT recvT;
		String r;
		if (!m.getScope().isPresent() || m.getScope().get().isThisExpr()) {
			if (inStatic) throw new Unsupported("instance call in static context");
			recvT = new JT(K.CLASS, curClass, null);
			r = "this";
		} else {
			Ex x = expr(m.getScope().get());
			recvT = x.t;
			r = recvRef(x);
		}
		// A subclass overriding the method would need dynamic dispatch.
		String key = recvT.k == K.GEOM ? recvT.cls : superGeom.get(recvT.cls);
		for (Map.Entry<String, String> e : superGeom.entrySet()) {
			if (!e.getValue().equals(key)) continue;
			for (MethodDeclaration md : classes.get(e.getKey()).getMethods())
				if (sig(md).equals(sig)) throw new Unsupported(simple(e.getKey()) + " overrides " + sig);
		}
		int n = ++tmpCounter;
		b.append("let __r").append(n).append(" = ").append(r).append("; ");
		List<String> args = new ArrayList<>();
		ResolvedMethodDeclaration rm = m.resolve();
		for (int i = 0; i < m.getArguments().size(); i++) {
			Ex a = expr(m.getArguments().get(i));
			boolean geo = a.t.k == K.GEOM || a.t.k == K.CLASS;
			String code = geo ? geoValue(a) : coerce(a, jt(rm.getParam(i).getType()), m).code;
			b.append("let __p").append(n).append('_').append(i).append(" = ").append(code).append("; ");
			args.add((geo ? "&" : "") + "__p" + n + "_" + i);
		}
		b.append("__r").append(n).append(gm.mut ? ".set" : ".get").append("(|o| ").append(geoPath(recvT, gm.mut)).append(".").append(gm.rust)
				.append("(").append(String.join(", ", args)).append(")) }");
		return new Ex(b.toString(), gm.ret, false);
	}

	Ex randomCall(MethodCallExpr m, ResolvedMethodDeclaration rm, String sig) {
		String r = scopeRecv(m);
		String a = args(m.getArguments(), rm::getParam, rm.getNumberOfParams(), m, true);
		switch (sig) {
			case "nextInt()": return new Ex("Random::nextInt(" + r + ")", INT, false);
			case "nextInt(int)": return new Ex("Random::nextInt_bound(" + r + a + ")", INT, false);
			case "nextLong()": return new Ex("Random::nextLong(" + r + ")", LONG, false);
			case "nextDouble()": return new Ex("Random::nextDouble(" + r + ")", DOUBLE, false);
			case "nextFloat()": return new Ex("Random::nextFloat(" + r + ")", FLOAT, false);
			case "nextBoolean()": return new Ex("Random::nextBoolean(" + r + ")", BOOL, false);
			case "nextGaussian()": return new Ex("Random::nextGaussian(" + r + ")", DOUBLE, false);
			case "setSeed(long)": return new Ex("Random::setSeed(" + r + a + ")", VOID, false);
			default: throw new Unsupported("Random." + sig);
		}
	}

	Ex mathCall(MethodCallExpr m, String owner, String name, String sig) {
		List<Expression> as = m.getArguments();
		Ex[] a = new Ex[as.size()];
		for (int i = 0; i < a.length; i++) a[i] = expr(as.get(i));
		boolean pure = true;
		for (Ex x : a) pure &= x.pure;
		switch (sig) {
			case "abs(int)": case "abs(long)": {
				JT t = sig.contains("long") ? LONG : INT;
				return new Ex(rust(t) + "::wrapping_abs(" + coerce(a[0], t, m).code + ")", t, pure);
			}
			case "abs(double)": case "abs(float)": {
				JT t = sig.contains("double") ? DOUBLE : FLOAT;
				return new Ex(paren(coerce(a[0], t, m).code) + ".abs()", t, pure);
			}
			case "max(int,int)": case "min(int,int)":
				return new Ex("std::cmp::" + name + "(" + coerce(a[0], INT, m).code + ", " + coerce(a[1], INT, m).code + ")", INT, pure);
			case "max(long,long)": case "min(long,long)":
				return new Ex("std::cmp::" + name + "(" + coerce(a[0], LONG, m).code + ", " + coerce(a[1], LONG, m).code + ")", LONG, pure);
			case "max(double,double)": case "min(double,double)":
				return new Ex("jmath::" + name + "_f64(" + coerce(a[0], DOUBLE, m).code + ", " + coerce(a[1], DOUBLE, m).code + ")", DOUBLE, pure);
			case "max(float,float)": case "min(float,float)":
				return new Ex("jmath::" + name + "_f32(" + coerce(a[0], FLOAT, m).code + ", " + coerce(a[1], FLOAT, m).code + ")", FLOAT, pure);
			case "sqrt(double)": return new Ex(paren(coerce(a[0], DOUBLE, m).code) + ".sqrt()", DOUBLE, pure);
			case "floor(double)": return new Ex(paren(coerce(a[0], DOUBLE, m).code) + ".floor()", DOUBLE, pure);
			case "ceil(double)": return new Ex(paren(coerce(a[0], DOUBLE, m).code) + ".ceil()", DOUBLE, pure);
			case "rint(double)": return new Ex(paren(coerce(a[0], DOUBLE, m).code) + ".round_ties_even()", DOUBLE, pure);
			case "round(double)": return new Ex("jmath::round_f64(" + coerce(a[0], DOUBLE, m).code + ")", LONG, pure);
			case "round(float)": return new Ex("jmath::round_f32(" + coerce(a[0], FLOAT, m).code + ")", INT, pure);
			case "signum(double)": return new Ex("jmath::signum_f64(" + coerce(a[0], DOUBLE, m).code + ")", DOUBLE, pure);
			case "powerOfTwoD(int)": return new Ex("jmath::power_of_two_d(" + coerce(a[0], INT, m).code + ")", DOUBLE, pure);
			case "scalb(double,int)": return new Ex("jmath::scalb(" + coerce(a[0], DOUBLE, m).code + ", " + coerce(a[1], INT, m).code + ")", DOUBLE, pure);
			default: break;
		}
		if (FDLIBM.contains(name) && sig.matches(name + "\\(double(,double)?\\)")) {
			// Math.sin etc. are StrictMath's (fdlibm) results here, translated from
			// the JDK's own source. HotSpot's Math.sin differs from it on some
			// platforms, so the Java side of the comparison uses StrictMath too.
			List<String> codes = new ArrayList<>();
			for (Ex x : a) codes.add(coerce(x, DOUBLE, m).code);
			return callStatic("java.lang.StrictMath", sig, codes, DOUBLE);
		}
		throw new Unsupported(simple(owner) + "." + sig);
	}

	static final Set<String> FDLIBM = new java.util.HashSet<>(java.util.Arrays.asList("sin", "cos", "tan", "asin", "acos", "atan",
			"atan2", "exp", "log", "log10", "log1p", "expm1", "pow", "cbrt", "hypot", "sinh", "cosh", "tanh"));

	/** A static method translated from source (pulled in on demand). */
	Ex callStatic(String owner, String sig, List<String> args, JT ret) {
		if (!allTypes.containsKey(owner)) throw new Unsupported(simple(owner) + "." + sig + " needs 'source " + owner + "' in the config");
		MethodDeclaration md = method(owner, sig);
		if (!callableNames.containsKey(owner + "." + sig)) nameCallables(allTypes.get(owner));
		needStatic(md);
		return new Ex(moduleName(owner) + "::" + callableNames.get(owner + "." + sig) + "(" + String.join(", ", args) + ")", ret, false);
	}

	/** Integer/Long.compareUnsigned: -1, 0 or 1, like the JDK's. */
	Ex compareUnsigned(MethodCallExpr m, JT t) {
		String u = t.k == K.LONG ? "u64" : "u32";
		String a = coerce(expr(m.getArguments().get(0)), t, m).code, b = coerce(expr(m.getArguments().get(1)), t, m).code;
		return new Ex("((" + a + " as " + u + ").cmp(&(" + b + " as " + u + ")) as i32)", INT, false);
	}

	Ex longCall(MethodCallExpr m, String sig) {
		switch (sig) {
			case "compareUnsigned(long,long)": return compareUnsigned(m, LONG);
			default: throw new Unsupported("Long." + sig);
		}
	}

	Ex doubleCall(MethodCallExpr m, String sig) {
		List<Expression> as = m.getArguments();
		switch (sig) {
			case "doubleToRawLongBits(double)": return new Ex("(" + paren(coerce(expr(as.get(0)), DOUBLE, m).code) + ".to_bits() as i64)", LONG, false);
			case "longBitsToDouble(long)": return new Ex("f64::from_bits(" + coerce(expr(as.get(0)), LONG, m).code + " as u64)", DOUBLE, false);
			case "isNaN(double)": return new Ex(paren(coerce(expr(as.get(0)), DOUBLE, m).code) + ".is_nan()", BOOL, false);
			case "isInfinite(double)": return new Ex(paren(coerce(expr(as.get(0)), DOUBLE, m).code) + ".is_infinite()", BOOL, false);
			case "isFinite(double)": return new Ex(paren(coerce(expr(as.get(0)), DOUBLE, m).code) + ".is_finite()", BOOL, false);
			case "valueOf(double)": return coerce(expr(as.get(0)), DOUBLE, m); // boxes are their value here
			case "doubleValue()": return coerce(expr(m.getScope().get()), DOUBLE, m);
			default: throw new Unsupported("Double." + sig);
		}
	}

	Ex colorCall(MethodCallExpr m, String name, String sig) {
		String r = recv(m.getScope().get(), expr(m.getScope().get()));
		switch (sig) {
			case "equals(java.lang.Object)": {
				Ex a = expr(m.getArguments().get(0));
				if (a.t.k != K.COLOR) throw new Unsupported("Color.equals(" + a.t + ")");
				return new Ex("Color::equals(" + r + ", " + a.code + ")", BOOL, false);
			}
			case "getRGB()": case "getRed()": case "getGreen()": case "getBlue()": case "getAlpha()":
				return new Ex(paren(r) + "." + name + "()", INT, false);
			default: throw new Unsupported("Color." + sig);
		}
	}

	Ex stringCall(MethodCallExpr m, String name, String sig) {
		String r = recv(m.getScope().get(), expr(m.getScope().get()));
		if (sig.equals("equals(java.lang.Object)")) {
			Ex a = expr(m.getArguments().get(0));
			if (a.t.k != K.STRING) throw new Unsupported("String.equals(" + a.t + ")");
			return new Ex("JStr::equals(&" + r + ", &" + a.code + ")", BOOL, false);
		}
		switch (sig) {
			case "toCharArray()": return new Ex(r + ".toCharArray()", new JT(K.ARRAY, null, CHAR), false);
			case "replace(char,char)":
				return new Ex(r + ".replace_char(" + coerce(expr(m.getArguments().get(0)), CHAR, m).code + ", "
						+ coerce(expr(m.getArguments().get(1)), CHAR, m).code + ")", STRING, false);
			default: throw new Unsupported("String." + sig);
		}
	}

	Ex integerCall(MethodCallExpr m, String sig) {
		switch (sig) {
			case "toHexString(int)": return new Ex("jstr::to_hex_string(" + coerce(expr(m.getArguments().get(0)), INT, m).code + ")", STRING, false);
			case "toString(int)": return new Ex("jstr::int_to_string(" + coerce(expr(m.getArguments().get(0)), INT, m).code + ")", STRING, false);
			case "compareUnsigned(int,int)": return compareUnsigned(m, INT);
			case "valueOf(int)": return coerce(expr(m.getArguments().get(0)), INT, m);
			case "intValue()": return coerce(expr(m.getScope().get()), INT, m);
			default: throw new Unsupported("Integer." + sig);
		}
	}

	/** A method of List, Collection, Set: by the receiver's type. */
	Ex collectionCall(MethodCallExpr m, String name, String sig) {
		Expression se = m.getScope().orElseThrow(() -> new Unsupported("collection call without receiver"));
		Ex s = expr(se);
		String r = recv(se, s);
		JT elem = s.t.elem;
		List<Expression> as = m.getArguments();
		if (s.t.k == K.HSET) {
			s = new Ex(paren(s.code) + ".as_collection()", new JT(K.COLL, null, elem), false);
			r = "(" + s.code + ")";
		}
		if (s.t.k == K.LIST) {
			switch (sig) {
				case "size()": return new Ex(r + ".len()", INT, false);
				case "isEmpty()": return new Ex("(" + r + ".len() == 0)", BOOL, false);
				case "get(int)": return new Ex(r + ".at(" + coerce(expr(as.get(0)), INT, m).code + ")", elem, false);
				case "add(E)": return new Ex(r + ".add(" + exprTo(as.get(0), elem, m).code + ")", BOOL, false);
				case "remove(int)": return new Ex(r + ".remove_at(" + coerce(expr(as.get(0)), INT, m).code + ")", elem, false);
				case "remove(java.lang.Object)": return new Ex(r + ".remove_obj(&" + coerce(expr(as.get(0)), elem, m).code + ")", BOOL, false);
				case "contains(java.lang.Object)": return new Ex(r + ".contains_obj(&" + coerce(expr(as.get(0)), elem, m).code + ")", BOOL, false);
				case "clear()": return new Ex(r + ".clear()", VOID, false);
				case "iterator()": return new Ex(paren(s.code) + ".as_collection().iterator()", new JT(K.ITER, null, elem), false);
				case "toArray(T[])": {
					Ex a = expr(as.get(0));
					return new Ex(r + ".to_array(&" + recv(as.get(0), a) + ", " + defaultValue(elem) + ")", a.t, false);
				}
				default: throw new Unsupported("List." + sig);
			}
		}
		if (s.t.k != K.COLL) throw new Unsupported("collection call on " + s.t);
		switch (sig) {
			case "size()": return new Ex(r + ".size()", INT, false);
			case "isEmpty()": return new Ex(r + ".isEmpty()", BOOL, false);
			case "add(E)": return new Ex(r + ".add(" + exprTo(as.get(0), elem, m).code + ")", BOOL, false);
			case "remove(java.lang.Object)": return new Ex(r + ".remove(&" + coerce(expr(as.get(0)), elem, m).code + ")", BOOL, false);
			case "contains(java.lang.Object)": return new Ex(r + ".contains(&" + coerce(expr(as.get(0)), elem, m).code + ")", BOOL, false);
			case "clear()": return new Ex(r + ".clear()", VOID, false);
			case "iterator()": return new Ex(r + ".iterator()", new JT(K.ITER, null, elem), false);
			case "toArray(T[])": {
				Ex a = expr(as.get(0));
				return new Ex(r + ".to_array(&" + recv(as.get(0), a) + ", " + defaultValue(elem) + ")", a.t, false);
			}
			default: throw new Unsupported("Collection." + sig);
		}
	}

	Ex iteratorCall(MethodCallExpr m, String sig) {
		Expression se = m.getScope().orElseThrow(() -> new Unsupported("iterator call without receiver"));
		Ex s = expr(se);
		String r = recv(se, s);
		switch (sig) {
			case "hasNext()": return new Ex(r + ".hasNext()", BOOL, false);
			case "next()": return new Ex(r + ".next()", s.t.elem, false);
			case "remove()": return new Ex(r + ".remove()", VOID, false);
			default: throw new Unsupported("Iterator." + sig);
		}
	}

	// ---------------------------------------------------------------- stubs

	boolean isStubCall(MethodCallExpr m, ResolvedMethodDeclaration rm) {
		String owner = rm.declaringType().getQualifiedName();
		if (stubTypes.contains(owner) || stubMethods.contains(owner + "." + rm.getSignature().replace(" ", ""))) return true;
		if (rm.isStatic() || !m.getScope().isPresent()) return false;
		try {
			ResolvedType t = m.getScope().get().calculateResolvedType();
			return t.isReferenceType() && stubTypes.contains(t.asReferenceType().getQualifiedName());
		} catch (RuntimeException e) {
			return false;
		}
	}

	/**
	 * A call on a stub: nothing runs. Its receiver and arguments are dropped,
	 * so they must have no side effects. Objects come back null, stubs as a
	 * (non-null) stub; a primitive result can't be made up, so it throws.
	 */
	Ex stubCall(MethodCallExpr m, ResolvedMethodDeclaration rm) {
		StringBuilder b = new StringBuilder();
		if (m.getScope().isPresent() && !rm.isStatic()) b.append(evalOrDrop(m.getScope().get()));
		for (Expression a : m.getArguments()) b.append(evalOrDrop(a));
		String what = simple(rm.declaringType().getQualifiedName()) + "." + rm.getSignature().replace(" ", "");
		ResolvedType rt = rm.getReturnType();
		JT t = rt.isVoid() ? VOID : jt(rt);
		String v;
		if (t.k == K.VOID) v = "()";
		else if (t.k == K.STUB) v = "Stub::NONNULL";
		else if (t.ref()) v = "<" + rust(t) + ">::default()";
		else v = "crate::rt::throw(\"J2R: stub " + esc(what) + "\")";
		return new Ex(b.length() == 0 ? v : "{ " + b + v + " }", t, b.length() == 0);
	}

	/**
	 * A receiver or argument of a stub call: evaluated (Java would), unless it
	 * can't be translated (a listener class), in which case it must have no
	 * side effects and is dropped.
	 */
	String evalOrDrop(Expression e) {
		if (!e.isLambdaExpr() && !(e.isObjectCreationExpr() && e.asObjectCreationExpr().getAnonymousClassBody().isPresent())) {
			try {
				Ex x = expr(e);
				return x.pure ? "" : "let _ = " + x.code + "; ";
			} catch (Unsupported u) {
				// fall through
			}
		}
		requireNoSideEffects(e);
		return "";
	}

	/** Expressions dropped by stub calls: no assignments, ++, or calls other than stub calls. */
	void requireNoSideEffects(Expression e) {
		if (e.isLambdaExpr() || e.isMethodReferenceExpr()) return;
		if (e.isObjectCreationExpr()) {
			ObjectCreationExpr o = e.asObjectCreationExpr();
			if (o.getAnonymousClassBody().isPresent()) return; // creating it runs nothing
			if (!stubTypes.contains(o.resolve().declaringType().getQualifiedName())) throw new Unsupported("side effect in a stub call: " + abbreviate(e.toString()));
		}
		if (e.isAssignExpr()) throw new Unsupported("side effect in a stub call: " + abbreviate(e.toString()));
		if (e.isUnaryExpr()) {
			UnaryExpr.Operator op = e.asUnaryExpr().getOperator();
			if (op == UnaryExpr.Operator.PREFIX_INCREMENT || op == UnaryExpr.Operator.POSTFIX_INCREMENT
					|| op == UnaryExpr.Operator.PREFIX_DECREMENT || op == UnaryExpr.Operator.POSTFIX_DECREMENT)
				throw new Unsupported("side effect in a stub call: " + abbreviate(e.toString()));
		}
		if (e.isMethodCallExpr() && !isStubCall(e.asMethodCallExpr(), e.asMethodCallExpr().resolve()))
			throw new Unsupported("call in a dropped stub argument: " + abbreviate(e.toString()));
		for (Node c : e.getChildNodes()) if (c instanceof Expression) requireNoSideEffects((Expression) c);
	}

	/** expected: the type the new object is assigned to (gives `new X<>()` its type arguments). */
	Ex create(ObjectCreationExpr o, JT expected) {
		if (o.getAnonymousClassBody().isPresent()) throw new Unsupported("anonymous class");
		ResolvedConstructorDeclaration rc = o.resolve();
		String owner = rc.declaringType().getQualifiedName();
		String sig = rc.getSignature().replace(" ", "");
		List<Expression> as = o.getArguments();
		if (stubTypes.contains(owner)) {
			StringBuilder b = new StringBuilder("{ ");
			for (Expression a : as) b.append(evalOrDrop(a));
			return new Ex(b + "Stub::NONNULL }", new JT(K.STUB, owner, null), false);
		}
		switch (owner) {
			case "java.util.HashSet": {
				JT t = o.getType().isUsingDiamondOperator() && expected != null && expected.elem != null
						? new JT(K.HSET, null, expected.elem) : jt(o.calculateResolvedType());
				if (as.isEmpty()) return new Ex("JRef::<JHashSet<" + rust(t.elem) + ">>::new_set()", t, false);
				throw new Unsupported("new HashSet" + sig);
			}
			case "java.util.Random":
				if (as.isEmpty()) return new Ex("Random::new()", JT.of(K.RANDOM), false);
				return new Ex("Random::new_seeded(" + coerce(expr(as.get(0)), LONG, o).code + ")", JT.of(K.RANDOM), false);
			case "java.awt.Color":
				if (sig.equals("Color(int,int,int)"))
					return new Ex("Color::rgb(" + args(as, rc::getParam, rc.getNumberOfParams(), o, false) + ")", COLOR, true);
				if (sig.equals("Color(int,int,int,int)"))
					return new Ex("Color::rgba(" + args(as, rc::getParam, rc.getNumberOfParams(), o, false) + ")", COLOR, true);
				throw new Unsupported("new Color" + sig);
			case "java.awt.Rectangle":
				if (as.isEmpty()) return new Ex("Rectangle::new_ref()", new JT(K.GEOM, "Rectangle", null), false);
				throw new Unsupported("new Rectangle" + sig);
			case "java.awt.geom.Line2D.Double":
				if (as.isEmpty()) return new Ex("Line2D::new_ref()", new JT(K.GEOM, "Line2D", null), false);
				throw new Unsupported("new Line2D.Double" + sig);
			case "java.awt.Point":
				if (sig.equals("Point(int,int)"))
					return new Ex("Point::new_ref(" + args(as, rc::getParam, rc.getNumberOfParams(), o, false) + ")", new JT(K.GEOM, "Point", null), false);
				throw new Unsupported("new Point" + sig);
			case "java.awt.geom.Point2D.Double":
				if (sig.equals("Double(double,double)"))
					return new Ex("Point2D::new_ref(" + args(as, rc::getParam, rc.getNumberOfParams(), o, false) + ")", new JT(K.GEOM, "Point2D", null), false);
				throw new Unsupported("new Point2D.Double" + sig);
			case "java.util.ArrayList": {
				JT t = o.getType().isUsingDiamondOperator() && expected != null && expected.elem != null
						? new JT(K.LIST, null, expected.elem) : jt(o.calculateResolvedType());
				if (as.isEmpty() || sig.equals("ArrayList(int)")) return new Ex("JList::from_vec(Vec::new())", t, false);
				throw new Unsupported("new ArrayList" + sig);
			}
			default: break;
		}
		if (!classes.containsKey(owner)) throw new Unsupported("new " + owner);
		String fn = simple(owner) + "::" + callableNames.getOrDefault(owner + "." + sig, "new");
		return new Ex(fn + "(" + args(as, rc::getParam, rc.getNumberOfParams(), o, false) + ")", new JT(K.CLASS, owner, null), false);
	}

	Ex newArray(ArrayCreationExpr a) {
		return newArray(a, null);
	}

	/** expected: the type the array is assigned to (for raw element types: new ArrayList[n]). */
	Ex newArray(ArrayCreationExpr a, JT expected) {
		JT t;
		try {
			t = jt(a.calculateResolvedType());
		} catch (Unsupported u) {
			if (expected == null || expected.k != K.ARRAY || !u.getMessage().startsWith("raw ")) throw u;
			t = expected;
		}
		if (a.getInitializer().isPresent()) return arrayInit(a.getInitializer().get(), t);
		List<Expression> dims = new ArrayList<>();
		for (com.github.javaparser.ast.ArrayCreationLevel l : a.getLevels()) {
			if (l.getDimension().isPresent()) dims.add(l.getDimension().get());
		}
		// Java evaluates all dimensions first, left to right.
		StringBuilder pre = new StringBuilder();
		List<String> names = new ArrayList<>();
		for (Expression d : dims) {
			int n = ++tmpCounter;
			pre.append("let __d").append(n).append(" = ").append(coerce(expr(d), INT, a).code).append("; ");
			names.add("__d" + n);
		}
		String code = build(t, names, 0);
		return new Ex(names.size() == 1 && dims.get(0).isLiteralExpr() ? code.replace(names.get(0), coerce(expr(dims.get(0)), INT, a).code)
				: "{ " + pre + code + " }", t, false);
	}

	String build(JT t, List<String> dims, int i) {
		if (i == dims.size() - 1) return "JArray::new_array(" + dims.get(i) + ", " + defaultValue(t.elem) + ")";
		return "JArray::from_vec((0.." + dims.get(i) + ").map(|_| " + build(t.elem, dims, i + 1) + ").collect())";
	}

	/** A declaration's initializer: may be a bare {...}. */
	Ex init(Expression e, JT t, Node at) {
		if (e.isArrayInitializerExpr()) {
			if (t.k != K.ARRAY) throw new Unsupported("array initializer for " + t);
			return arrayInit(e.asArrayInitializerExpr(), t);
		}
		return exprTo(e, t, at);
	}

	Ex arrayInit(ArrayInitializerExpr init, JT t) {
		StringBuilder s = new StringBuilder("JArray::from_vec(vec![");
		boolean first = true;
		for (Expression e : init.getValues()) {
			if (!first) s.append(", ");
			first = false;
			if (e.isArrayInitializerExpr()) s.append(arrayInit(e.asArrayInitializerExpr(), t.elem).code);
			else s.append(coerce(expr(e), t.elem, e).code);
		}
		return new Ex(s.append("])").toString(), t, false);
	}
}
