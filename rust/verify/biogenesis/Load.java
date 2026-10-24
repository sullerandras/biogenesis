package biogenesis;

import java.awt.Color;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Reads a save file (the Dump format, see rust/core/src/save.rs) back into
 * objects, by reflection: the Java side of rust/core/src/rt/load.rs. Objects
 * are made without running constructors and get every field from the file.
 * Values are read as the declared type of the field they go into.
 *
 * HashSets come back as Collections.synchronizedSet(new HashSet<>(capacity))
 * filled in iteration order, as World makes them; lists as ArrayLists.
 */
public class Load {
	public static final String HEADER = "biogenesis-save 1\n";

	private final String s;
	private int pos;
	private final Map<Integer, Object> ids = new HashMap<>();
	/** Instances for the stub types (the GUI), by type. */
	private final Map<Class<?>, Object> stubs;

	public Load(String s, Map<Class<?>, Object> stubs) {
		this.s = s;
		this.stubs = stubs;
	}

	/** Reads a whole save: sets the statics, returns the world. */
	public static World load(String text, Map<Class<?>, Object> stubs) {
		Load l = new Load(text, stubs);
		l.expect(HEADER);
		World world = null;
		while (l.pos < l.s.length()) {
			String name = l.name();
			if (name.equals("world")) {
				world = (World) l.value(World.class);
			} else {
				Field f = Dump.staticField(name);
				try {
					f.set(null, l.value(f.getGenericType()));
				} catch (IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
			}
			l.expect("\n");
		}
		if (world == null) throw l.fail("no world");
		return world;
	}

	private IllegalArgumentException fail(String what) {
		int line = 1;
		for (int i = 0; i < pos; i++) if (s.charAt(i) == '\n') line++;
		return new IllegalArgumentException("save file, line " + line + ": " + what + " at "
				+ s.substring(pos, Math.min(s.length(), pos + 40)));
	}

	private boolean eat(String p) {
		if (s.startsWith(p, pos)) {
			pos += p.length();
			return true;
		}
		return false;
	}

	private void expect(String p) {
		if (!eat(p)) throw fail("expected " + p);
	}

	private void spaces() {
		while (pos < s.length() && s.charAt(pos) == ' ') pos++;
	}

	private String name() {
		spaces();
		int start = pos;
		while (s.charAt(pos) != '=') pos++;
		return s.substring(start, pos++);
	}

	private String token(String chars) {
		int start = pos;
		while (pos < s.length() && chars.indexOf(s.charAt(pos)) >= 0) pos++;
		return s.substring(start, pos);
	}

	private long integer() {
		int start = pos;
		if (s.charAt(pos) == '-') pos++;
		token("0123456789");
		return Long.parseLong(s.substring(start, pos));
	}

	private long hex(String prefix) {
		expect(prefix);
		return Long.parseUnsignedLong(token("0123456789abcdef"), 16);
	}

	static Class<?> raw(Type t) {
		if (t instanceof Class) return (Class<?>) t;
		if (t instanceof ParameterizedType) return (Class<?>) ((ParameterizedType) t).getRawType();
		if (t instanceof GenericArrayType) return Array.newInstance(raw(((GenericArrayType) t).getGenericComponentType()), 0).getClass();
		throw new IllegalArgumentException("type " + t);
	}

	Object value(Type type) {
		Class<?> c = raw(type);
		if (c == int.class || c == Integer.class) return (int) integer();
		if (c == short.class) return (short) integer();
		if (c == byte.class) return (byte) integer();
		if (c == char.class) return (char) integer();
		if (c == long.class || c == Long.class) {
			long v = integer();
			expect("L");
			return v;
		}
		if (c == boolean.class || c == Boolean.class) {
			if (eat("true")) return true;
			expect("false");
			return false;
		}
		if (c == double.class || c == Double.class) return Double.longBitsToDouble(hex("d:"));
		if (c == float.class) return Float.intBitsToFloat((int) hex("f:"));
		if (eat("null")) return null;
		if (c == String.class) {
			expect("s");
			int n = (int) integer();
			expect(":");
			pos += n;
			return s.substring(pos - n, pos);
		}
		if (c == Color.class) return new Color((int) hex("c:"), true);
		if (eat("stub")) {
			for (Map.Entry<Class<?>, Object> e : stubs.entrySet()) if (c.isAssignableFrom(e.getKey())) return e.getValue();
			throw fail("no stub for " + c);
		}
		if (eat("@")) {
			Object o = ids.get((int) integer());
			if (o == null) throw fail("unknown @id");
			return o;
		}
		if (eat("[#")) return sequence(type, c);
		return object(c);
	}

	private Object sequence(Type type, Class<?> c) {
		int id = (int) integer();
		expect(" ");
		int n = (int) integer();
		int cap = -1;
		if (eat(" HashSet(")) {
			cap = (int) integer();
			expect(")");
		}
		expect("\n");
		Object result;
		if (c.isArray()) {
			Type elem = type instanceof GenericArrayType ? ((GenericArrayType) type).getGenericComponentType() : c.getComponentType();
			Object a = Array.newInstance(raw(elem), n);
			for (int i = 0; i < n; i++) {
				spaces();
				Array.set(a, i, value(elem));
				expect("\n");
			}
			result = a;
		} else {
			if (!(type instanceof ParameterizedType)) throw fail("raw collection type " + type);
			Type elem = ((ParameterizedType) type).getActualTypeArguments()[0];
			List<Object> items = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				spaces();
				items.add(value(elem));
				expect("\n");
			}
			if (cap >= 0) {
				// as World creates it, filled in iteration order
				HashSet<Object> set = cap == 0 ? new HashSet<>() : new HashSet<>(cap);
				set.addAll(items);
				result = Collections.synchronizedSet(set);
			} else {
				result = items;
			}
			if (!c.isInstance(result)) throw fail(result.getClass() + " where a " + c + " is expected");
		}
		spaces();
		expect("]");
		ids.put(id, result);
		return result;
	}

	private Object object(Class<?> declared) {
		int start = pos;
		while (s.charAt(pos) != '#') pos++;
		String name = s.substring(start, pos++);
		int id = (int) integer();
		expect("{\n");
		Class<?> c = classFor(name, declared);
		Object o;
		if (c == java.util.Random.class) {
			o = new java.util.Random(0);
		} else {
			try {
				// no constructor runs: every field comes from the file
				o = sun.reflect.ReflectionFactory.getReflectionFactory()
						.newConstructorForSerialization(c, Object.class.getDeclaredConstructor()).newInstance();
			} catch (ReflectiveOperationException e) {
				throw new IllegalStateException(e);
			}
		}
		ids.put(id, o);
		Map<String, Field> fields = new HashMap<>();
		for (Class<?> k = c; k != null && k != Object.class; k = k.getSuperclass())
			for (Field f : k.getDeclaredFields()) if (!Modifier.isStatic(f.getModifiers())) fields.putIfAbsent(f.getName(), f);
		while (true) {
			spaces();
			if (eat("}")) return o;
			String n = name();
			Field f = fields.get(n);
			if (f == null) throw fail(c.getName() + " has no field " + n);
			f.setAccessible(true);
			try {
				if (c == java.util.Random.class && n.equals("seed")) {
					((java.util.concurrent.atomic.AtomicLong) f.get(o)).set((Long) value(long.class));
				} else {
					f.set(o, value(f.getGenericType()));
				}
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
			expect("\n");
		}
	}

	/** The class a dump names by its simple name (Line2D.Double is "Double"). */
	private Class<?> classFor(String name, Class<?> declared) {
		if (declared.getSimpleName().equals(name)) return declared;
		for (Class<?> n : declared.getDeclaredClasses()) if (n.getSimpleName().equals(name) && declared.isAssignableFrom(n)) return n;
		if (name.equals("Random")) return java.util.Random.class;
		for (String p : new String[] { "biogenesis.", "java.awt." }) {
			try {
				Class<?> c = Class.forName(p + name);
				if (declared.isAssignableFrom(c)) return c;
			} catch (ClassNotFoundException e) {
				// next
			}
		}
		throw fail("no class " + name + " for a " + declared.getName());
	}
}
