package biogenesis;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Prints the results of the java.awt geometry methods that rt/geom.rs ports,
 * for random arguments. rust/core/src/bin/geom_check.rs does the same with
 * rt/geom.rs; the outputs must be identical.
 *
 * Usage: GeomCheck seed count
 */
public class GeomCheck {
	static Random r;

	/** Small grid values make touching, collinear and degenerate cases common. */
	static int coord() {
		switch (r.nextInt(4)) {
			case 0: return r.nextInt(21) - 10;
			case 1: return r.nextInt(2001) - 1000;
			case 2: return Integer.MAX_VALUE - r.nextInt(20); // overflow handling
			default: return r.nextInt();
		}
	}

	static double dcoord() {
		return r.nextInt(3) == 0 ? r.nextInt(21) - 10 : (r.nextDouble() - 0.5) * 40;
	}

	public static void main(String[] args) {
		r = new Random(Long.parseLong(args[0]));
		int n = Integer.parseInt(args[1]);
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < n; i++) {
			Rectangle a = new Rectangle(coord(), coord(), r.nextInt(25) - 2, r.nextInt(25) - 2);
			Rectangle b = new Rectangle();
			if (r.nextBoolean()) b.setBounds(a.x + r.nextInt(30) - 15, a.y + r.nextInt(30) - 15, r.nextInt(25) - 2, r.nextInt(25) - 2);
			else b.setBounds(coord(), coord(), coord(), coord());
			Rectangle small = new Rectangle(r.nextInt(21) - 10, r.nextInt(21) - 10, r.nextInt(12) - 1, r.nextInt(12) - 1);
			Line2D.Double l = new Line2D.Double();
			l.setLine(dcoord(), dcoord(), dcoord(), dcoord());
			Line2D.Double m = new Line2D.Double(dcoord(), dcoord(), dcoord(), dcoord());
			int px = coord(), py = coord();
			out.append(a.intersects(b) ? 1 : 0).append(b.intersects(a) ? 1 : 0)
				.append(a.contains(px, py) ? 1 : 0).append(a.contains(a.x + r.nextInt(30) - 5, a.y + r.nextInt(30) - 5) ? 1 : 0)
				.append(small.intersectsLine(l) ? 1 : 0).append(l.intersectsLine(m) ? 1 : 0).append(small.isEmpty() ? 1 : 0)
				.append(' ').append(Line2D.relativeCCW(l.x1, l.y1, l.x2, l.y2, m.x1, m.y1))
				.append(' ').append(Long.toHexString(Double.doubleToRawLongBits(l.getP1().distanceSq(m.getP2()))))
				.append('\n');
		}
		System.out.print(out);
	}
}
