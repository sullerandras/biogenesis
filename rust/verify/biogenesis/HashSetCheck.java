package biogenesis;

import java.util.HashSet;
import java.util.Random;

/**
 * Random adds and removes on a java.util.HashSet, printing its iteration
 * order. Many keys share their low hash bits, so bins grow into trees and are
 * split again on resize. rust/core/src/bin/hashset_check.rs does the same with
 * rt::JHashSet; the outputs must be identical.
 *
 * Usage: HashSetCheck seed ops
 */
public class HashSetCheck {
	static final class Key {
		final int id;
		Key(int id) { this.id = id; }
		@Override public int hashCode() { return id % 3 == 0 ? id << 10 : id; }
		@Override public boolean equals(Object o) { return o instanceof Key && ((Key) o).id == id; }
	}

	public static void main(String[] args) {
		Random r = new Random(Long.parseLong(args[0]));
		int ops = Integer.parseInt(args[1]);
		HashSet<Key> set = new HashSet<>();
		StringBuilder out = new StringBuilder();
		for (int i = 1; i <= ops; i++) {
			int pool = 1 + (i / 1000) % 4 * 700;
			int op = r.nextInt(1000);
			int id = r.nextInt(pool);
			boolean b;
			if (op == 0) { set.clear(); b = true; }
			else if (op < 600) b = set.add(new Key(id));
			else if (op < 950) b = set.remove(new Key(id));
			else b = set.contains(new Key(id));
			out.append(b ? '+' : '-');
			if (i % 500 == 0) {
				out.append("\nsize ").append(set.size()).append(':');
				for (Key k : set) out.append(' ').append(k.id);
				out.append('\n');
			}
		}
		System.out.print(out);
	}
}
