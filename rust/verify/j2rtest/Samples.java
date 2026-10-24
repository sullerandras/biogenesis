package j2rtest;

/**
 * Translator tests: Java constructs J2R must translate exactly, run by
 * SamplesCheck (Java) and samples_check (the translated Rust). Each method
 * returns a string; the outputs must be identical. Add a case here when J2R
 * learns a construct.
 */
public class Samples {
	public static String run(int seed) {
		String s = "";
		for (int i = -3; i < 12; i++) s += fallThrough(i + seed) + ",";
		s += "|" + strings(seed);
		s += "|" + switchInLoop(seed);
		s += "|" + arithmetic(seed);
		s += "|" + collections(seed);
		return s;
	}

	/** HashSet iteration order, Iterator.remove, lambdas capturing locals and this, boxed lists. */
	static String collections(int seed) {
		Bag b = new Bag();
		String s = "";
		for (int i = 0; i < 40; i++) b.items.add(new Item((i * 37 + seed) % 50, i * 0.5));
		s += b.items.size();
		final int limit = 20 + seed % 5;
		Item f = b.find(it -> it.id > limit && it.w < 15);
		s += f == null ? "none" : "f" + f.id;
		Item h = b.heavy();
		s += h == null ? "none" : "h" + h.id;
		for (java.util.Iterator<Item> it = b.items.iterator(); it.hasNext();) {
			Item x = it.next();
			if (x.id % 3 == 0) it.remove();
		}
		for (Item x : b.items) {
			s += "," + x.id;
			b.weights.add(Double.valueOf(x.w));
		}
		b.items.remove(new Item(seed % 50, 0));
		s += "|" + b.items.size() + (b.items.contains(new Item(1, 0)) ? "y" : "n");
		b.weights.remove(0);
		s += "|" + b.weights.size() + (b.weights.get(0) > 3 ? "big" : "small");
		Item[] a = b.items.toArray(new Item[0]);
		s += "|" + a.length + (a.length > 0 ? "," + a[0].id : "");
		return s;
	}

	/** Fall-through, grouped labels, a default in the middle. */
	static int fallThrough(int x) {
		int r = 0;
		switch (x % 7) {
			case 0:
				r += 1;
			case 1:
			case 2:
				r += 10;
				if (x > 20) break;
			default:
				r += 100;
			case 4:
				r += 1000;
				break;
			case 5:
				r = -1;
		}
		return r;
	}

	/** break and continue from a switch inside a loop. */
	static String switchInLoop(int seed) {
		String s = "";
		outer:
		for (int i = 0; i < 10; i++) {
			switch ((i + seed) % 4) {
				case 0:
					s += "a";
					continue;
				case 1:
					s += "b";
				case 2:
					s += "c";
					if (i > 7) break outer;
					break;
				default:
					s += "d";
			}
			s += i;
		}
		return s;
	}

	static String strings(int seed) {
		char c = (char) ('A' + seed % 26);
		String s = "x" + c + 1 + 2;
		s = 1 + 2 + s;
		String n = null;
		s += n;
		s += 'q';
		s += 7L * seed;
		s += true;
		s += Integer.toHexString(-seed) + Integer.toString(seed * 1000003);
		s += "°";
		int count = 0;
		for (char d : s.toCharArray()) if (d == 'x' || d == '°') count++;
		return s.replace('x', 'y') + count;
	}

	static String arithmetic(int seed) {
		int a = Integer.MAX_VALUE - seed;
		a += 7;
		int b = -7 / 2 + -7 % 2 + (a >> 3) + (a >>> 3) + (-1 >>> 28);
		long l = (long) a * a + (1L << 40);
		double d = 1e308 * 10;
		return a + " " + b + " " + l + " " + (int) d + " " + (long) -d + " " + (Integer.MIN_VALUE / -1);
	}
}
