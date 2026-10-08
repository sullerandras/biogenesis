import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
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
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Build-time source transform that adds the collision mirror (perf/mirror/README.md)
 * to the build copy of the sources. src/ is never touched.
 *
 * usage: MirrorTransform <build-src dir> <perf/mirror dir> [library jars...]
 *
 * It parses every source file with type resolution, finds every write of a
 * mirrored Organism field, and inserts a mirrorSync*() call after it. Edits are
 * inserted as text on the same source line, so line numbers in the build copy
 * match src/. Anything it can't handle safely stops the build with a message.
 */
public class MirrorTransform {
	static final String ORGANISM = "biogenesis.Organism";
	static final Set<String> RECTANGLE_TYPES = new HashSet<>(Arrays.asList(
			"java.awt.Rectangle", "java.awt.geom.Rectangle2D", "java.awt.geom.RectangularShape"));
	/** Public methods of Rectangle and its supertypes that change the bounds. */
	static final Set<String> RECTANGLE_MUTATORS = new HashSet<>(Arrays.asList(
			"setBounds", "setRect", "reshape", "setLocation", "move", "translate", "setSize", "resize",
			"add", "grow", "setFrame", "setFrameFromDiagonal", "setFrameFromCenter"));

	enum Group { BOUNDS, CENTER, SEGMENTS }

	/** Mirrored fields: declared in Organism, or inherited from Rectangle. */
	static final Map<String, Group> ORGANISM_FIELDS = new LinkedHashMap<>();
	static final Map<String, Group> RECTANGLE_FIELDS = new LinkedHashMap<>();
	static final Set<String> SEGMENT_ARRAYS = new HashSet<>(Arrays.asList("x1", "y1", "x2", "y2"));
	static {
		ORGANISM_FIELDS.put("_centerX", Group.CENTER);
		ORGANISM_FIELDS.put("_centerY", Group.CENTER);
		for (String a : SEGMENT_ARRAYS) {
			ORGANISM_FIELDS.put(a, Group.SEGMENTS);
		}
		ORGANISM_FIELDS.put("_segments", Group.SEGMENTS);
		for (String f : new String[] {"x", "y", "width", "height"}) {
			RECTANGLE_FIELDS.put(f, Group.BOUNDS);
		}
	}

	/** Methods that (may) read the mirror. Callers are found by name, transitively. */
	static final Set<String> MIRROR_READERS = new HashSet<>(Arrays.asList("checkHit", "findFirstHit"));

	static Path srcRoot;
	static Path mirrorDir;
	static final Map<Path, CompilationUnit> units = new TreeMap<>();
	static final Map<Path, String> texts = new LinkedHashMap<>();
	static final Map<Path, List<Edit>> edits = new LinkedHashMap<>();
	static final List<String> errors = new ArrayList<>();
	static Set<String> tainted;
	static final Map<Group, Integer> syncCount = new TreeMap<>();
	static final List<String> report = new ArrayList<>();

	static final class Edit {
		final int offset;
		final int order; // at the same offset: lower first
		final String text;

		Edit(int offset, int order, String text) {
			this.offset = offset;
			this.order = order;
			this.text = text;
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println("usage: MirrorTransform <build-src dir> <perf/mirror dir> [library jars...]");
			System.exit(2);
		}
		long start = System.currentTimeMillis();
		srcRoot = Paths.get(args[0]);
		mirrorDir = Paths.get(args[1]);
		CombinedTypeSolver types = new CombinedTypeSolver(new ReflectionTypeSolver(true), new JavaParserTypeSolver(srcRoot));
		for (int i = 2; i < args.length; i++) {
			types.add(new JarTypeSolver(Paths.get(args[i])));
		}
		ParserConfiguration config = new ParserConfiguration()
				.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
				.setSymbolResolver(new JavaSymbolSolver(types))
				.setTabSize(1);
		JavaParser parser = new JavaParser(config);
		try (Stream<Path> files = Files.walk(srcRoot.resolve("biogenesis"))) {
			for (Path p : files.filter(f -> f.toString().endsWith(".java")).collect(Collectors.toList())) {
				String text = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
				ParseResult<CompilationUnit> r = parser.parse(text);
				if (!r.isSuccessful() || !r.getResult().isPresent()) {
					fail(p, null, "parse error: " + r.getProblems());
					continue;
				}
				units.put(p, r.getResult().get());
				texts.put(p, text);
			}
		}
		stopOnErrors();

		tainted = taintedMethodNames();
		checkNoOrganismSubclasses();
		for (Map.Entry<Path, CompilationUnit> e : units.entrySet()) {
			findWrites(e.getKey(), e.getValue());
		}
		Path organism = file("Organism.java");
		Path world = file("World.java");
		Path buckets = file("OrganismBuckets.java");
		emitSyncs();
		checkContact(organism);
		transformWorld(world);
		transformBuckets(buckets);
		appendMembers(organism, "Organism", "Organism.members");
		stopOnErrors();

		for (Map.Entry<Path, List<Edit>> e : edits.entrySet()) {
			Files.write(e.getKey(), apply(texts.get(e.getKey()), e.getValue()).getBytes(StandardCharsets.UTF_8));
		}
		Files.copy(mirrorDir.resolve("CollisionMirror.java"), srcRoot.resolve("biogenesis/CollisionMirror.java"),
				StandardCopyOption.REPLACE_EXISTING);
		Files.write(srcRoot.resolve("mirror-report.txt"), report, StandardCharsets.UTF_8);
		System.out.println("MirrorTransform: " + syncCount + " sync calls in " + edits.size() + " files, "
				+ (System.currentTimeMillis() - start) + " ms (details: " + srcRoot.resolve("mirror-report.txt") + ")");
	}

	// ------------------------------------------------------------------------
	// Finding writes of mirrored fields

	static void findWrites(Path path, CompilationUnit cu) {
		for (AssignExpr a : cu.findAll(AssignExpr.class)) {
			checkWrite(path, a.getTarget(), a);
		}
		for (UnaryExpr u : cu.findAll(UnaryExpr.class)) {
			if (u.getOperator().isPrefix() && u.getOperator() != UnaryExpr.Operator.PREFIX_INCREMENT
					&& u.getOperator() != UnaryExpr.Operator.PREFIX_DECREMENT) {
				continue;
			}
			checkWrite(path, u.getExpression(), u);
		}
		for (MethodCallExpr call : cu.findAll(MethodCallExpr.class)) {
			if (RECTANGLE_MUTATORS.contains(call.getNameAsString())) {
				checkRectangleCall(path, call);
			}
		}
		// The segment arrays can also change through an alias (System.arraycopy(..., x1, ...),
		// int[] a = x1; a[0] = ...): treat any use other than x1[i] / x1.length as a write.
		for (Expression e : namesAndFieldAccesses(cu)) {
			String name = identifier(e);
			if (!SEGMENT_ARRAYS.contains(name) || isElementOrLength(e) || isAssignTarget(e)) {
				continue;
			}
			MirroredField f = mirroredField(path, e);
			if (f != null) {
				addSync(path, e, f.group, f.receiver, "alias of " + name);
			}
		}
	}

	static List<Expression> namesAndFieldAccesses(CompilationUnit cu) {
		List<Expression> l = new ArrayList<>(cu.findAll(NameExpr.class));
		l.addAll(cu.findAll(FieldAccessExpr.class));
		return l;
	}

	static String identifier(Expression e) {
		if (e instanceof NameExpr) {
			return ((NameExpr) e).getNameAsString();
		}
		if (e instanceof FieldAccessExpr) {
			return ((FieldAccessExpr) e).getNameAsString();
		}
		return null;
	}

	static boolean isElementOrLength(Expression e) {
		Node p = e.getParentNode().orElse(null);
		if (p instanceof ArrayAccessExpr && ((ArrayAccessExpr) p).getName() == e) {
			return true;
		}
		return p instanceof FieldAccessExpr && ((FieldAccessExpr) p).getScope() == e
				&& ((FieldAccessExpr) p).getNameAsString().equals("length");
	}

	static boolean isAssignTarget(Expression e) {
		Node p = e.getParentNode().orElse(null);
		return p instanceof AssignExpr && ((AssignExpr) p).getTarget() == e;
	}

	static final class MirroredField {
		final Group group;
		final String receiver; // null: this

		MirroredField(Group group, String receiver) {
			this.group = group;
			this.receiver = receiver;
		}
	}

	static void checkWrite(Path path, Expression target, Node write) {
		Expression e = target;
		while (e instanceof ArrayAccessExpr) {
			e = ((ArrayAccessExpr) e).getName();
		}
		while (e instanceof EnclosedExpr) {
			e = ((EnclosedExpr) e).getInner();
		}
		String name = identifier(e);
		if (name == null || !(ORGANISM_FIELDS.containsKey(name) || RECTANGLE_FIELDS.containsKey(name))) {
			return;
		}
		MirroredField f = mirroredField(path, e);
		if (f != null) {
			addSync(path, write, f.group, f.receiver, "write of " + name);
		}
	}

	/** If e names a mirrored field of an Organism: its group and receiver. */
	static MirroredField mirroredField(Path path, Expression e) {
		String name = identifier(e);
		ResolvedValueDeclaration d;
		try {
			d = e instanceof NameExpr ? ((NameExpr) e).resolve() : ((FieldAccessExpr) e).resolve();
		} catch (RuntimeException ex) {
			fail(path, e, "can't resolve '" + e + "': " + ex);
			return null;
		}
		if (!d.isField()) {
			return null;
		}
		String declaring = d.asField().declaringType().getQualifiedName();
		Group group;
		if (declaring.equals(ORGANISM) && ORGANISM_FIELDS.containsKey(name)) {
			group = ORGANISM_FIELDS.get(name);
		} else if (RECTANGLE_TYPES.contains(declaring) && RECTANGLE_FIELDS.containsKey(name)) {
			group = RECTANGLE_FIELDS.get(name);
		} else {
			return null;
		}
		if (e instanceof NameExpr) {
			// implicit this: the innermost class must be Organism itself
			String type = innermostType(e);
			if (type.equals(ORGANISM)) {
				return new MirroredField(group, null);
			}
			if (declaring.equals(ORGANISM) || isOrganismInnerType(e)) {
				fail(path, e, "write of Organism field " + name + " from a nested class (" + type + ") is not supported");
			}
			return null;
		}
		Expression scope = ((FieldAccessExpr) e).getScope();
		if (!isOrganism(path, scope)) {
			return null;
		}
		if (scope instanceof ThisExpr || scope instanceof SuperExpr) {
			if (!innermostType(e).equals(ORGANISM)) {
				fail(path, e, "write of Organism field " + name + " through this/super of a nested class");
			}
			return new MirroredField(group, null);
		}
		if (scope instanceof NameExpr) {
			return new MirroredField(group, ((NameExpr) scope).getNameAsString());
		}
		fail(path, e, "write of Organism field " + name + " through '" + scope
				+ "': only this, a variable or a parameter is supported as the receiver");
		return null;
	}

	static boolean isOrganism(Path path, Expression scope) {
		if ((scope instanceof ThisExpr && !((ThisExpr) scope).getTypeName().isPresent()) || scope instanceof SuperExpr) {
			return innermostType(scope).equals(ORGANISM);
		}
		try {
			ResolvedType t = scope.calculateResolvedType();
			return t.isReferenceType() && t.asReferenceType().getQualifiedName().equals(ORGANISM);
		} catch (RuntimeException ex) {
			fail(path, scope, "can't resolve the type of '" + scope + "': " + ex);
			return false;
		}
	}

	static void checkRectangleCall(Path path, MethodCallExpr call) {
		Optional<Expression> scope = call.getScope();
		String receiver;
		if (!scope.isPresent()) {
			if (!innermostType(call).equals(ORGANISM)) {
				return;
			}
			receiver = null;
		} else {
			if (!isOrganism(path, scope.get())) {
				return;
			}
			Expression s = scope.get();
			if (s instanceof ThisExpr || s instanceof SuperExpr) {
				receiver = null;
			} else if (s instanceof NameExpr) {
				receiver = ((NameExpr) s).getNameAsString();
			} else {
				fail(path, call, "Rectangle method " + call.getNameAsString() + " on '" + s
						+ "': only this, a variable or a parameter is supported as the receiver");
				return;
			}
		}
		ResolvedMethodDeclaration m;
		try {
			m = call.resolve();
		} catch (RuntimeException ex) {
			fail(path, call, "can't resolve '" + call + "': " + ex);
			return;
		}
		if (RECTANGLE_TYPES.contains(m.declaringType().getQualifiedName())) {
			addSync(path, call, Group.BOUNDS, receiver, "Rectangle." + call.getNameAsString());
		}
	}

	static String innermostType(Node n) {
		for (Node p = n.getParentNode().orElse(null); p != null; p = p.getParentNode().orElse(null)) {
			if (p instanceof ObjectCreationExpr && ((ObjectCreationExpr) p).getAnonymousClassBody().isPresent()) {
				return "<anonymous>";
			}
			if (p instanceof TypeDeclaration) {
				return ((TypeDeclaration<?>) p).getFullyQualifiedName().orElse("?");
			}
		}
		return "?";
	}

	static boolean isOrganismInnerType(Node n) {
		for (Node p = n.getParentNode().orElse(null); p != null; p = p.getParentNode().orElse(null)) {
			if (p instanceof TypeDeclaration && ((TypeDeclaration<?>) p).getFullyQualifiedName().orElse("").equals(ORGANISM)) {
				return true;
			}
		}
		return false;
	}

	static void checkNoOrganismSubclasses() {
		for (Map.Entry<Path, CompilationUnit> e : units.entrySet()) {
			for (ClassOrInterfaceDeclaration c : e.getValue().findAll(ClassOrInterfaceDeclaration.class)) {
				if (c.getExtendedTypes().stream().anyMatch(t -> t.getNameAsString().equals("Organism"))) {
					fail(e.getKey(), c, "class " + c.getNameAsString() + " extends Organism: subclasses are not supported");
				}
			}
			for (ObjectCreationExpr o : e.getValue().findAll(ObjectCreationExpr.class)) {
				if (o.getAnonymousClassBody().isPresent() && o.getType().getNameAsString().equals("Organism")) {
					fail(e.getKey(), o, "anonymous subclass of Organism is not supported");
				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// Placing the sync calls

	/**
	 * Inserts a sync of `group` after the write. For writes through `this`, the
	 * sync moves out of enclosing statements (loops, ifs, blocks) as long as they
	 * can't read the mirror (no call that may reach a mirror reader) and can't
	 * leave the method early (return, throw, break/continue to an outer
	 * statement). Writes through another receiver are synced right after the
	 * statement.
	 */
	static void addSync(Path path, Node write, Group group, String receiver, String what) {
		Statement s0 = write.findAncestor(Statement.class).orElse(null);
		Node owner = owner(write);
		if (s0 == null || !(owner instanceof CallableDeclaration || owner instanceof InitializerDeclaration)
				|| !isAncestor(owner, s0)) {
			fail(path, write, what + ": only writes inside method, constructor or initializer bodies are supported");
			return;
		}
		if (!(s0 instanceof ExpressionStmt)) {
			if (receiver != null || !isTaintFree(s0) || !isExitFree(s0)) {
				fail(path, write, what + " in a " + s0.getClass().getSimpleName()
						+ " header: not supported (move it into its own statement)");
				return;
			}
		}
		Statement at = s0;
		if (receiver == null) {
			for (Node p = at.getParentNode().orElse(null); p instanceof Statement && p != bodyOf(owner);
					p = p.getParentNode().orElse(null)) {
				Statement ps = (Statement) p;
				if (!isTaintFree(ps) || !isExitFree(ps)) {
					break;
				}
				at = ps;
			}
		}
		if (at.getParentNode().orElse(null) instanceof LabeledStmt) {
			fail(path, write, what + ": can't insert a sync after a labeled statement");
			return;
		}
		String call = (receiver == null ? "" : receiver + ".") + "mirrorSync" + capitalized(group) + "();";
		if (pending.computeIfAbsent(path, k -> new IdentityHashMap<>())
				.computeIfAbsent(at, k -> new LinkedHashSet<>()).add(call)) {
			syncCount.merge(group, 1, Integer::sum);
		}
		report.add(rel(path) + ":" + line(write) + ": " + what + " -> " + call + " after line " + line(at, true)
				+ (at == s0 ? "" : " (moved out to the enclosing " + at.getClass().getSimpleName() + ")"));
	}

	/** Sync calls to insert after each statement. */
	static final Map<Path, Map<Statement, LinkedHashSet<String>>> pending = new LinkedHashMap<>();

	static void emitSyncs() {
		for (Map.Entry<Path, Map<Statement, LinkedHashSet<String>>> f : pending.entrySet()) {
			Path path = f.getKey();
			for (Map.Entry<Statement, LinkedHashSet<String>> e : f.getValue().entrySet()) {
				Statement at = e.getKey();
				String calls = " " + String.join(" ", e.getValue());
				int end = endOffset(path, at);
				if (end < 0) {
					continue;
				}
				Node parent = at.getParentNode().orElse(null);
				if (parent instanceof BlockStmt || parent instanceof SwitchEntry) {
					addEdit(path, end, 1000 - depth(at), calls);
				} else {
					// single statement body (if (c) x = 1;): wrap it in a block
					addEdit(path, startOffset(path, at), depth(at), "{ ");
					addEdit(path, end, 1000 - depth(at), calls + " }");
				}
			}
		}
	}

	static final Set<String> seenEdits = new HashSet<>();

	static void addEdit(Path path, int offset, int order, String text) {
		if (!seenEdits.add(path + ":" + offset + ":" + text)) {
			return; // same sync at the same place
		}
		edits.computeIfAbsent(path, k -> new ArrayList<>()).add(new Edit(offset, order, text));
	}

	static String capitalized(Group g) {
		String s = g.name().toLowerCase();
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	/** The method, constructor, initializer or lambda the node is in. */
	static Node owner(Node n) {
		for (Node p = n.getParentNode().orElse(null); p != null; p = p.getParentNode().orElse(null)) {
			if (p instanceof CallableDeclaration || p instanceof InitializerDeclaration || p instanceof LambdaExpr
					|| p instanceof FieldDeclaration || p instanceof TypeDeclaration) {
				return p;
			}
		}
		return null;
	}

	static Node bodyOf(Node owner) {
		if (owner instanceof MethodDeclaration) {
			return ((MethodDeclaration) owner).getBody().orElse(null);
		}
		if (owner instanceof ConstructorDeclaration) {
			return ((ConstructorDeclaration) owner).getBody();
		}
		if (owner instanceof InitializerDeclaration) {
			return ((InitializerDeclaration) owner).getBody();
		}
		return null;
	}

	static boolean isAncestor(Node ancestor, Node n) {
		for (Node p = n; p != null; p = p.getParentNode().orElse(null)) {
			if (p == ancestor) {
				return true;
			}
		}
		return false;
	}

	static int depth(Node n) {
		int d = 0;
		for (Node p = n; p != null; p = p.getParentNode().orElse(null)) {
			d++;
		}
		return d;
	}

	/** No call in s may reach a mirror reader (by method name, transitively). */
	static boolean isTaintFree(Statement s) {
		for (MethodCallExpr c : s.findAll(MethodCallExpr.class)) {
			if (tainted.contains(c.getNameAsString())) {
				return false;
			}
		}
		for (MethodReferenceExpr r : s.findAll(MethodReferenceExpr.class)) {
			if (tainted.contains(r.getIdentifier())) {
				return false;
			}
		}
		for (ObjectCreationExpr o : s.findAll(ObjectCreationExpr.class)) {
			if (tainted.contains("<init>" + o.getType().getNameAsString()) || o.getAnonymousClassBody().isPresent()) {
				return false;
			}
		}
		return true;
	}

	/** Control can only leave s at its end (no return/throw, no break/continue to outside s). */
	static boolean isExitFree(Statement s) {
		for (Node n : s.findAll(Node.class)) {
			if (insideNestedCode(n, s)) {
				continue;
			}
			if (n instanceof ReturnStmt || n instanceof ThrowStmt) {
				return false;
			}
			if (n instanceof BreakStmt || n instanceof ContinueStmt) {
				Optional<String> label = n instanceof BreakStmt ? ((BreakStmt) n).getLabel().map(Object::toString)
						: ((ContinueStmt) n).getLabel().map(Object::toString);
				Node target = null;
				for (Node p = n.getParentNode().orElse(null); p != null; p = p.getParentNode().orElse(null)) {
					boolean loop = p instanceof ForStmt || p instanceof ForEachStmt || p instanceof WhileStmt || p instanceof DoStmt;
					if (label.isPresent() ? p instanceof LabeledStmt && ((LabeledStmt) p).getLabel().asString().equals(label.get())
							: loop || (n instanceof BreakStmt && p instanceof SwitchStmt)) {
						target = p;
						break;
					}
					if (p == s) {
						break;
					}
				}
				if (target == null || !isAncestor(s, target)) {
					return false;
				}
			}
		}
		return true;
	}

	/** n is inside a lambda or class body nested in s (its control flow is separate). */
	static boolean insideNestedCode(Node n, Node s) {
		for (Node p = n.getParentNode().orElse(null); p != null && p != s; p = p.getParentNode().orElse(null)) {
			if (p instanceof LambdaExpr || p instanceof TypeDeclaration
					|| (p instanceof ObjectCreationExpr && ((ObjectCreationExpr) p).getAnonymousClassBody().isPresent())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Names of the methods (and "<init>Class" constructors) that may call a mirror
	 * reader, directly or indirectly. By name only, so overloads and same-named
	 * methods of other classes count too (conservative).
	 */
	static Set<String> taintedMethodNames() {
		Map<String, Set<String>> calls = new LinkedHashMap<>();
		for (CompilationUnit cu : units.values()) {
			for (CallableDeclaration<?> c : cu.findAll(CallableDeclaration.class)) {
				String name = c instanceof ConstructorDeclaration ? "<init>" + c.getNameAsString() : c.getNameAsString();
				Set<String> called = calls.computeIfAbsent(name, k -> new HashSet<>());
				c.findAll(MethodCallExpr.class).forEach(m -> called.add(m.getNameAsString()));
				c.findAll(MethodReferenceExpr.class).forEach(m -> called.add(m.getIdentifier()));
				c.findAll(ObjectCreationExpr.class).forEach(o -> called.add("<init>" + o.getType().getNameAsString()));
			}
		}
		Set<String> t = new HashSet<>(MIRROR_READERS);
		boolean changed = true;
		while (changed) {
			changed = false;
			for (Map.Entry<String, Set<String>> e : calls.entrySet()) {
				if (!t.contains(e.getKey()) && !Collections.disjoint(e.getValue(), t)) {
					t.add(e.getKey());
					changed = true;
				}
			}
		}
		return t;
	}

	// ------------------------------------------------------------------------
	// Organism.contact(): it must have no side effects unless a segment of this
	// and a segment of org intersect (CollisionMirror.mayContact relies on it).

	static void checkContact(Path path) {
		MethodDeclaration contact = method(path, "Organism", "contact", 1);
		if (contact == null) {
			return;
		}
		String org = contact.getParameter(0).getNameAsString();
		if (!contact.getParameter(0).getType().asString().equals("Organism")) {
			fail(path, contact, "contact() parameter is not an Organism");
			return;
		}
		Set<String> lines = new HashSet<>();
		for (VariableDeclarator v : contact.findAll(VariableDeclarator.class)) {
			String t = v.getType().asString();
			if (t.equals("ExLine2DDouble") || t.equals("Line2D.Double") || t.equals("Line2D")) {
				lines.add(v.getNameAsString());
			}
		}
		BlockStmt body = contact.getBody().orElse(null);
		if (body == null) {
			return;
		}
		for (Statement s : body.findAll(Statement.class)) {
			if (s == body || hasVerifiedGuard(s, org, lines)) {
				continue;
			}
			// outside the guard: only control flow with pure conditions, local
			// declarations and line.setLine(...) are allowed
			if (s instanceof BlockStmt || s instanceof BreakStmt || s instanceof ContinueStmt) {
				continue;
			}
			if (s instanceof ReturnStmt && ((ReturnStmt) s).getExpression().map(e -> e.toString().equals("false")).orElse(false)) {
				continue;
			}
			if (s instanceof IfStmt) {
				checkPure(path, ((IfStmt) s).getCondition(), lines);
				continue;
			}
			if (s instanceof ForStmt) {
				ForStmt f = (ForStmt) s;
				f.getInitialization().forEach(e -> checkPure(path, e, lines));
				f.getCompare().ifPresent(e -> checkPure(path, e, lines));
				f.getUpdate().forEach(e -> checkPure(path, e, lines));
				continue;
			}
			if (s instanceof WhileStmt) {
				checkPure(path, ((WhileStmt) s).getCondition(), lines);
				continue;
			}
			if (s instanceof ExpressionStmt) {
				Expression e = ((ExpressionStmt) s).getExpression();
				if (e instanceof VariableDeclarationExpr) {
					for (VariableDeclarator v : ((VariableDeclarationExpr) e).getVariables()) {
						v.getInitializer().ifPresent(i -> {
							if (!i.toString().equals("new ExLine2DDouble()")) {
								checkPure(path, i, lines);
							}
						});
					}
					continue;
				}
				if (e instanceof MethodCallExpr && ((MethodCallExpr) e).getNameAsString().equals("setLine")
						&& ((MethodCallExpr) e).getScope().map(sc -> lines.contains(sc.toString())).orElse(false)) {
					((MethodCallExpr) e).getArguments().forEach(a -> checkPure(path, a, lines));
					continue;
				}
				checkPure(path, e, lines);
				continue;
			}
			fail(path, s, "contact(): statement outside a segment-intersection guard: " + oneLine(s));
		}
	}

	/** e has no side effects: no calls except intersectsLine/segBoxesOverlap, writes only to locals. */
	static void checkPure(Path path, Expression e, Set<String> lines) {
		for (MethodCallExpr c : e.findAll(MethodCallExpr.class)) {
			String n = c.getNameAsString();
			if (!(n.equals("intersectsLine") || n.equals("segBoxesOverlap"))) {
				fail(path, e, "contact(): call outside a segment-intersection guard: " + c);
			}
		}
		if (!e.findAll(ObjectCreationExpr.class).isEmpty()) {
			fail(path, e, "contact(): object creation outside a segment-intersection guard: " + e);
		}
		List<Expression> targets = new ArrayList<>();
		e.findAll(AssignExpr.class).forEach(a -> targets.add(a.getTarget()));
		e.findAll(UnaryExpr.class).stream()
				.filter(u -> u.getOperator().isPostfix() || u.getOperator() == UnaryExpr.Operator.PREFIX_INCREMENT
						|| u.getOperator() == UnaryExpr.Operator.PREFIX_DECREMENT)
				.forEach(u -> targets.add(u.getExpression()));
		for (Expression t : targets) {
			boolean local = false;
			if (t instanceof NameExpr) {
				try {
					ResolvedValueDeclaration d = ((NameExpr) t).resolve();
					local = !d.isField();
				} catch (RuntimeException ex) {
					local = false;
				}
			}
			if (!local) {
				fail(path, e, "contact(): write outside a segment-intersection guard: " + t);
			}
		}
	}

	/**
	 * s is inside the then-branch of `if (... intersectsLine(B) && L.intersectsLine(B) ...)`,
	 * where B was just set to a segment of org (B.setLine(org.x1[j] + org._centerX, ...)),
	 * and that if is inside `if (org.intersectsLine(L))` with L just set to a segment of
	 * this (L.setLine(x1[i]+_centerX, ...)), and L is not set again in between.
	 */
	static boolean hasVerifiedGuard(Statement s, String org, Set<String> lines) {
		for (Node n = s; n != null; n = n.getParentNode().orElse(null)) {
			Node p = n.getParentNode().orElse(null);
			if (!(p instanceof IfStmt) || ((IfStmt) p).getThenStmt() != n) {
				continue;
			}
			IfStmt g = (IfStmt) p;
			List<Expression> c = conjuncts(g.getCondition());
			for (Expression x : c) {
				if (!(x instanceof MethodCallExpr)) {
					continue;
				}
				MethodCallExpr lineTest = (MethodCallExpr) x;
				if (!lineTest.getNameAsString().equals("intersectsLine") || !lineTest.getScope().isPresent()
						|| lineTest.getArguments().size() != 1) {
					continue;
				}
				String l = lineTest.getScope().get().toString();
				String b = lineTest.getArguments().get(0).toString();
				if (!lines.contains(l) || !lines.contains(b)) {
					continue;
				}
				boolean thisBox = c.stream().anyMatch(y -> norm(y).equals("intersectsLine(" + b + ")")
						|| norm(y).equals("this.intersectsLine(" + b + ")"));
				if (!thisBox || !setBefore(g, b, segmentOf(org + ".", b, null))) {
					continue;
				}
				// the enclosing if (org.intersectsLine(l)), with l set to a segment of this just before
				for (Node m = g; m != null; m = m.getParentNode().orElse(null)) {
					Node q = m.getParentNode().orElse(null);
					if (q instanceof IfStmt && ((IfStmt) q).getThenStmt() == m
							&& conjuncts(((IfStmt) q).getCondition()).stream().anyMatch(y -> norm(y).equals(org + ".intersectsLine(" + l + ")"))
							&& setBefore((IfStmt) q, l, segmentOf("", l, null))
							&& ((IfStmt) q).getThenStmt().findAll(MethodCallExpr.class).stream()
									.noneMatch(k -> k.getNameAsString().equals("setLine") && k.getScope().map(sc -> sc.toString().equals(l)).orElse(false))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/** Normalized "L.setLine(P x1[I] + P _centerX, ...)" with I a placeholder. */
	static String segmentOf(String prefix, String line, String index) {
		String i = index == null ? "#" : index;
		return line + ".setLine(" + prefix + "x1[" + i + "]+" + prefix + "_centerX," + prefix + "y1[" + i + "]+" + prefix
				+ "_centerY," + prefix + "x2[" + i + "]+" + prefix + "_centerX," + prefix + "y2[" + i + "]+" + prefix + "_centerY);";
	}

	/** The statement right before `s` in its block is the expected setLine call (any index variable). */
	static boolean setBefore(Statement s, String line, String expected) {
		Node p = s.getParentNode().orElse(null);
		if (!(p instanceof BlockStmt)) {
			return false;
		}
		List<Statement> l = ((BlockStmt) p).getStatements();
		int k = l.indexOf(s);
		if (k <= 0) {
			return false;
		}
		String prev = norm(l.get(k - 1));
		java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\[(\\w+)\\]").matcher(prev);
		if (!m.find()) {
			return false;
		}
		return prev.equals(expected.replace("#", m.group(1)));
	}

	static List<Expression> conjuncts(Expression e) {
		while (e instanceof EnclosedExpr) {
			e = ((EnclosedExpr) e).getInner();
		}
		List<Expression> l = new ArrayList<>();
		if (e instanceof BinaryExpr && ((BinaryExpr) e).getOperator() == BinaryExpr.Operator.AND) {
			l.addAll(conjuncts(((BinaryExpr) e).getLeft()));
			l.addAll(conjuncts(((BinaryExpr) e).getRight()));
		} else {
			l.add(e);
		}
		return l;
	}

	// ------------------------------------------------------------------------
	// World and OrganismBuckets

	static void transformWorld(Path path) {
		MethodDeclaration checkHit = method(path, "World", "checkHit", 1);
		if (checkHit != null) {
			String param = checkHit.getParameter(0).getNameAsString();
			BlockStmt body = checkHit.getBody().get();
			expectBody(path, checkHit, "World.checkHit.txt");
			// replace the body, keeping the line count
			int start = startOffset(path, body), end = endOffset(path, body);
			String old = texts.get(path).substring(start, end);
			long newlines = old.chars().filter(ch -> ch == '\n').count();
			StringBuilder repl = new StringBuilder("{ return organismBuckets.findFirstHit(" + param + ");");
			for (long i = 0; i < newlines; i++) {
				repl.append('\n');
			}
			repl.append('}');
			replace(path, start, end, repl.toString());
		}
		// organismBuckets = new OrganismBuckets(...) in time(): attach the mirror right after
		MethodDeclaration time = method(path, "World", "time", 0);
		int found = 0;
		for (AssignExpr a : units.get(path).findAll(AssignExpr.class)) {
			if (a.getTarget().toString().equals("organismBuckets")) {
				if (time == null || !isAncestor(time, a) || !(a.getParentNode().get() instanceof ExpressionStmt)
						|| !(a.getValue() instanceof ObjectCreationExpr)) {
					fail(path, a, "unexpected assignment of organismBuckets (only time() may create the buckets)");
					continue;
				}
				found++;
				addEdit(path, endOffset(path, (Statement) a.getParentNode().get()), 0,
						" collisionMirror = CollisionMirror.prepare(collisionMirror, _organisms.size());"
								+ " organismBuckets.setMirror(collisionMirror);");
			}
		}
		if (found != 1) {
			fail(path, time, "expected exactly one 'organismBuckets = new OrganismBuckets(...)' in World.time(), found " + found);
		}
		for (MethodCallExpr c : units.get(path).findAll(MethodCallExpr.class)) {
			if (c.getNameAsString().equals("insert") && c.getScope().map(s -> s.toString().equals("organismBuckets")).orElse(false)
					&& (time == null || !isAncestor(time, c))) {
				fail(path, c, "organismBuckets.insert() outside World.time()");
			}
		}
		appendMembers(path, "World", "World.members");
	}

	static void transformBuckets(Path path) {
		MethodDeclaration findFirst = method(path, "OrganismBuckets", "findFirst", 2);
		if (findFirst != null) {
			expectBody(path, findFirst, "OrganismBuckets.findFirst.txt");
		}
		MethodDeclaration insert = method(path, "OrganismBuckets", "insert", 1);
		if (insert != null) {
			String o = insert.getParameter(0).getNameAsString();
			int adds = 0;
			for (MethodCallExpr c : insert.findAll(MethodCallExpr.class)) {
				if (!c.getNameAsString().equals("add")) {
					continue;
				}
				Expression scope = c.getScope().orElse(null);
				boolean ok = scope instanceof ArrayAccessExpr && ((ArrayAccessExpr) scope).getName() instanceof ArrayAccessExpr
						&& ((ArrayAccessExpr) ((ArrayAccessExpr) scope).getName()).getName().toString().equals("buckets")
						&& c.getArguments().size() == 1 && c.getArguments().get(0).toString().equals(o)
						&& c.getParentNode().get() instanceof ExpressionStmt
						&& c.getParentNode().get().getParentNode().get() instanceof BlockStmt;
				if (!ok) {
					fail(path, c, "OrganismBuckets.insert(): unexpected add(): " + c);
					continue;
				}
				Expression y = ((ArrayAccessExpr) ((ArrayAccessExpr) scope).getName()).getIndex();
				Expression x = ((ArrayAccessExpr) scope).getIndex();
				if (!(y instanceof NameExpr) || !(x instanceof NameExpr)) {
					fail(path, c, "OrganismBuckets.insert(): bucket indexes must be variables: " + c);
					continue;
				}
				adds++;
				addEdit(path, endOffset(path, (Statement) c.getParentNode().get()), 0,
						" mirrorAdd(" + y + ", " + x + ", " + o + ");");
			}
			if (adds == 0) {
				fail(path, insert, "OrganismBuckets.insert(): no buckets[y][x].add(o) found");
			}
		}
		appendMembers(path, "OrganismBuckets", "OrganismBuckets.members");
	}

	static void expectBody(Path path, MethodDeclaration m, String expectedFile) {
		String actual = norm(m.getBody().get());
		String expected;
		try {
			expected = new String(Files.readAllBytes(mirrorDir.resolve("expected").resolve(expectedFile)), StandardCharsets.UTF_8).trim();
		} catch (IOException e) {
			expected = "<missing " + expectedFile + ">";
		}
		if (!actual.equals(expected)) {
			fail(path, m, m.getNameAsString() + "() changed; the collision mirror copies its logic, so check"
					+ " perf/mirror/OrganismBuckets.members (findFirstHit) against it, then update"
					+ " perf/mirror/expected/" + expectedFile + " to:\n" + actual);
		}
	}

	/** Appends the members in perf/mirror/<file> to the class, checking for name clashes. */
	static void appendMembers(Path path, String className, String file) {
		String members;
		try {
			members = new String(Files.readAllBytes(mirrorDir.resolve(file)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			fail(path, null, "can't read " + file + ": " + e);
			return;
		}
		ClassOrInterfaceDeclaration c = units.get(path).getClassByName(className).orElse(null);
		if (c == null) {
			fail(path, null, "class " + className + " not found");
			return;
		}
		ParseResult<CompilationUnit> r = new JavaParser(new ParserConfiguration()
				.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)).parse("class X {\n" + members + "\n}");
		if (!r.isSuccessful()) {
			fail(path, null, file + ": " + r.getProblems());
			return;
		}
		Set<String> existing = new HashSet<>();
		for (BodyDeclaration<?> d : c.getMembers()) {
			if (d instanceof FieldDeclaration) {
				((FieldDeclaration) d).getVariables().forEach(v -> existing.add(v.getNameAsString()));
			} else if (d instanceof MethodDeclaration) {
				existing.add(((MethodDeclaration) d).getNameAsString());
			}
		}
		for (BodyDeclaration<?> d : r.getResult().get().getType(0).getMembers()) {
			List<String> names = new ArrayList<>();
			if (d instanceof FieldDeclaration) {
				((FieldDeclaration) d).getVariables().forEach(v -> names.add(v.getNameAsString()));
			} else if (d instanceof MethodDeclaration) {
				names.add(((MethodDeclaration) d).getNameAsString());
			}
			for (String n : names) {
				if (existing.contains(n)) {
					fail(path, c, file + ": " + className + " already has a member named " + n);
				}
			}
		}
		addEdit(path, endOffset(path, c) - 1, 2000, "\n" + members); // before the closing brace
	}

	// ------------------------------------------------------------------------
	// Helpers

	static Path file(String name) {
		return srcRoot.resolve("biogenesis").resolve(name);
	}

	static MethodDeclaration method(Path path, String className, String name, int params) {
		List<MethodDeclaration> l = units.get(path).getClassByName(className)
				.map(c -> c.getMethodsByName(name)).orElse(Collections.emptyList())
				.stream().filter(m -> m.getParameters().size() == params && m.getBody().isPresent())
				.collect(Collectors.toList());
		if (l.size() != 1) {
			fail(path, null, "expected one " + className + "." + name + "() with " + params + " parameter(s), found " + l.size());
			return null;
		}
		return l.get(0);
	}

	/** Source text without comments and whitespace. */
	static String norm(Node n) {
		Node c = n.clone();
		for (Comment k : c.getAllContainedComments()) {
			k.remove();
		}
		c.removeComment();
		return c.toString().replaceAll("\\s+", "");
	}

	static String oneLine(Node n) {
		String s = n.toString().replaceAll("\\s+", " ");
		return s.length() > 120 ? s.substring(0, 120) + "..." : s;
	}

	static final Map<Path, int[]> lineStarts = new LinkedHashMap<>();

	static int offset(Path path, Position p) {
		int[] starts = lineStarts.computeIfAbsent(path, k -> {
			String t = texts.get(k);
			List<Integer> l = new ArrayList<>();
			l.add(0);
			for (int i = 0; i < t.length(); i++) {
				if (t.charAt(i) == '\n') {
					l.add(i + 1);
				}
			}
			return l.stream().mapToInt(Integer::intValue).toArray();
		});
		return starts[p.line - 1] + p.column - 1;
	}

	static int startOffset(Path path, Node n) {
		return offset(path, n.getRange().get().begin);
	}

	/** Offset just after the node's last character. */
	static int endOffset(Path path, Node n) {
		int off = offset(path, n.getRange().get().end);
		char ch = texts.get(path).charAt(off);
		String s = n.toString().trim();
		if (ch != s.charAt(s.length() - 1)) {
			fail(path, n, "internal: source position mismatch (" + ch + ")");
			return -1;
		}
		return off + 1;
	}

	static void replace(Path path, int start, int end, String text) {
		edits.computeIfAbsent(path, k -> new ArrayList<>()).add(new Edit(start, -1, "\0" + (end - start) + "\0" + text));
	}

	static String apply(String text, List<Edit> list) {
		List<Edit> sorted = new ArrayList<>(list);
		// apply from the end; at the same offset, insert higher order first so lower order ends up first
		sorted.sort((a, b) -> a.offset != b.offset ? Integer.compare(b.offset, a.offset) : Integer.compare(b.order, a.order));
		StringBuilder sb = new StringBuilder(text);
		for (Edit e : sorted) {
			if (e.text.startsWith("\0")) {
				int k = e.text.indexOf('\0', 1);
				int len = Integer.parseInt(e.text.substring(1, k));
				sb.replace(e.offset, e.offset + len, e.text.substring(k + 1));
			} else {
				sb.insert(e.offset, e.text);
			}
		}
		return sb.toString();
	}

	static int line(Node n) {
		return n.getRange().map(r -> r.begin.line).orElse(0);
	}

	static int line(Node n, boolean end) {
		return n.getRange().map(r -> end ? r.end.line : r.begin.line).orElse(0);
	}

	static String rel(Path p) {
		return srcRoot.relativize(p).toString();
	}

	static void fail(Path path, Node n, String message) {
		errors.add((path == null ? "" : rel(path) + (n == null ? "" : ":" + line(n)) + ": ") + message);
	}

	static void stopOnErrors() {
		if (!errors.isEmpty()) {
			for (String e : new LinkedHashSet<>(errors)) {
				System.err.println("MirrorTransform: " + e);
			}
			System.exit(1);
		}
	}
}
