package biogenesis;

import java.awt.Color;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Canonical text dump of an object graph; the same format as the Rust side's
 * rt::dump (rust/core/src/rt/dump.rs). Instance fields sorted by name, doubles
 * as raw bits, objects seen before as @id.
 */
public class Dump {
	final StringBuilder out = new StringBuilder();
	private final Map<Object, Integer> ids = new IdentityHashMap<>();
	private int depth;

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
		else reference(v);
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
		if (v instanceof List) {
			List<?> l = (List<?>) v;
			out.append("[#").append(id).append(' ').append(l.size()).append('\n');
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
		if (!v.getClass().getName().startsWith("biogenesis.")) throw new IllegalArgumentException("can't dump " + v.getClass());
		out.append(v.getClass().getSimpleName()).append('#').append(id).append("{\n");
		depth++;
		List<Field> fields = new ArrayList<>();
		for (Class<?> c = v.getClass(); c != null && c != Object.class; c = c.getSuperclass())
			for (Field f : c.getDeclaredFields()) if (!Modifier.isStatic(f.getModifiers())) fields.add(f);
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
