package biogenesis;

import java.awt.Color;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Canonical text dump of an object graph; the same format as the Rust side's
 * rt::dump (rust/core/src/rt/dump.rs). Instance fields sorted by name, doubles
 * as raw bits, objects seen before as @id.
 */
public class Dump {
	/**
	 * Caches that the perf patches add (`make` builds: perf/perf_patch.py's
	 * symmetric() cache, perf/mirror's collision mirror), not simulation state.
	 * Skipped, so a patched build's world can be compared with the unpatched
	 * code's (and the Rust translation's).
	 */
	static final Set<String> PATCH_FIELDS = new HashSet<>(Arrays.asList("Organism._symCache0", "Organism._symCache1",
		"Organism._mirror", "Organism._mirrorSlot", "World.collisionMirror",
		"OrganismBuckets.mirror", "OrganismBuckets.slotLists", "OrganismBuckets.slotCounts"));

	final StringBuilder out = new StringBuilder();
	private final Map<Object, Integer> ids = new IdentityHashMap<>();
	private int depth;
	/** Types the Rust side has as rt::Stub (J2R's `stub` config): written as "stub". */
	public static Class<?>[] stubs = {};

	public void raw(String s) {
		out.append(s);
	}

	private void indent() {
		for (int i = 0; i < depth; i++) out.append(' ');
	}

	public void value(Object v, Class<?> declared) {
		if (declared == int.class || declared == short.class || declared == byte.class) out.append(((Number) v).intValue());
		else if (declared == long.class) out.append((long) (Long) v).append('L');
		else if (declared == char.class) out.append((int) (Character) v);
		else if (declared == boolean.class) out.append((boolean) (Boolean) v);
		else if (declared == double.class) out.append(String.format("d:%016x", Double.doubleToRawLongBits((Double) v)));
		else if (declared == float.class) out.append(String.format("f:%08x", Float.floatToRawIntBits((Float) v)));
		else if (v == null) out.append("null");
		else if (v instanceof Color) out.append(String.format("c:%08x", ((Color) v).getRGB()));
		else if (v instanceof String) out.append("s").append(((String) v).length()).append(':').append(v);
		// boxes (collection elements) are their value on the Rust side
		else if (v instanceof Double) value(v, double.class);
		else if (v instanceof Integer) value(v, int.class);
		else if (v instanceof Long) value(v, long.class);
		else if (v instanceof Boolean) value(v, boolean.class);
		else if (isStub(v)) out.append("stub");
		else reference(v);
	}

	public void field(String name, Object v, Class<?> declared) {
		indent();
		out.append(name).append('=');
		value(v, declared);
		out.append('\n');
	}

	/** A private field's value (reflection). */
	static Object get(Object o, Class<?> c, String name) {
		try {
			Field f = c.getDeclaredField(name);
			f.setAccessible(true);
			return f.get(o);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}

	/** The collection inside Collections.synchronizedSet(...) etc. */
	static Object unwrapSynchronized(Object v) {
		if (v.getClass().getName().startsWith("java.util.Collections$Synchronized")) {
			try {
				return get(v, Class.forName("java.util.Collections$SynchronizedCollection"), "c");
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e);
			}
		}
		return v;
	}

	/** The simulation's statics a save holds, listed by J2R in rust/core/src/generated/statics.txt. */
	public void statics(java.nio.file.Path list) throws java.io.IOException {
		for (String name : java.nio.file.Files.readAllLines(list)) {
			if (name.isEmpty()) continue;
			Field f = staticField(name);
			try {
				field(name, f.get(null), f.getType());
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	static Field staticField(String name) {
		int dot = name.lastIndexOf('.');
		try {
			Field f = Class.forName(name.substring(0, dot)).getDeclaredField(name.substring(dot + 1));
			f.setAccessible(true);
			return f;
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(name, e);
		}
	}

	private static boolean isStub(Object v) {
		for (Class<?> c : stubs) if (c.isInstance(v)) return true;
		return false;
	}

	private void reference(Object v) {
		Integer seen = ids.get(v);
		if (seen != null) {
			out.append('@').append(seen);
			return;
		}
		int id = ids.size();
		ids.put(v, id);
		if (v.getClass().isArray()) {
			int n = Array.getLength(v);
			out.append("[#").append(id).append(' ').append(n).append('\n');
			depth++;
			for (int i = 0; i < n; i++) {
				indent();
				value(Array.get(v, i), v.getClass().getComponentType());
				out.append('\n');
			}
			depth--;
			indent();
			out.append(']');
			return;
		}
		if (v instanceof java.util.Random) {
			// java.util.Random's own fields (needs --add-opens java.base/java.util=ALL-UNNAMED)
			out.append("Random#").append(id).append("{\n");
			depth++;
			field("haveNextNextGaussian", get(v, java.util.Random.class, "haveNextNextGaussian"), boolean.class);
			field("nextNextGaussian", get(v, java.util.Random.class, "nextNextGaussian"), double.class);
			field("seed", ((java.util.concurrent.atomic.AtomicLong) get(v, java.util.Random.class, "seed")).get(), long.class);
			depth--;
			indent();
			out.append('}');
			return;
		}
		if (v instanceof Collection) {
			Collection<?> l = (Collection<?>) v;
			out.append("[#").append(id).append(' ').append(l.size());
			// A HashSet's capacity decides its iteration order: saves keep it.
			Object inner = unwrapSynchronized(v);
			if (inner instanceof java.util.HashSet && !(inner instanceof java.util.LinkedHashSet)) {
				Object[] table = (Object[]) get(get(inner, java.util.HashSet.class, "map"), java.util.HashMap.class, "table");
				out.append(" HashSet(").append(table == null ? 0 : table.length).append(')');
			}
			out.append('\n');
			depth++;
			for (Object x : l) {
				indent();
				value(x, Object.class);
				out.append('\n');
			}
			depth--;
			indent();
			out.append(']');
			return;
		}
		if (!v.getClass().getName().startsWith("biogenesis.") && !v.getClass().getName().startsWith("java.awt.")) throw new IllegalArgumentException("can't dump " + v.getClass());
		out.append(v.getClass().getSimpleName()).append('#').append(id).append("{\n");
		depth++;
		List<Field> fields = new ArrayList<>();
		for (Class<?> c = v.getClass(); c != null && c != Object.class; c = c.getSuperclass())
			for (Field f : c.getDeclaredFields()) if (!Modifier.isStatic(f.getModifiers()) && !PATCH_FIELDS.contains(c.getSimpleName() + "." + f.getName())) fields.add(f);
		fields.sort(Comparator.comparing(Field::getName));
		for (Field f : fields) {
			f.setAccessible(true);
			indent();
			out.append(f.getName()).append('=');
			try {
				value(f.get(v), f.getType());
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
			out.append('\n');
		}
		depth--;
		indent();
		out.append('}');
	}
}
