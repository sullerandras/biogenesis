package biogenesis;

import java.util.Random;

/**
 * Prints StrictMath results for random arguments, as raw bits.
 * rust/core/src/bin/math_check.rs does the same with the StrictMath/FdLibm
 * code J2R translated from the JDK; the outputs must be identical.
 *
 * Usage: MathCheck seed count
 */
public class MathCheck {
	static double arg(Random r, int kind) {
		switch (kind) {
			case 0: return (r.nextDouble() - 0.5) * 20;
			case 1: return (r.nextDouble() - 0.5) * 2000;
			case 2: return Double.longBitsToDouble(r.nextLong()); // anything, NaN and infinities included
			case 3: return r.nextInt(2000) - 1000 + r.nextDouble();
			default: return r.nextDouble() * 2 - 1;
		}
	}

	public static void main(String[] args) {
		Random r = new Random(Long.parseLong(args[0]));
		int n = Integer.parseInt(args[1]);
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < n; i++) {
			double x = arg(r, i % 5), y = arg(r, (i / 5) % 5);
			double[] v = {
				StrictMath.sin(x), StrictMath.cos(x), StrictMath.tan(x), StrictMath.asin(x), StrictMath.acos(x),
				StrictMath.atan(x), StrictMath.atan2(x, y), StrictMath.exp(x), StrictMath.log(x), StrictMath.log10(x),
				StrictMath.log1p(x), StrictMath.expm1(x), StrictMath.pow(x, y), StrictMath.cbrt(x), StrictMath.hypot(x, y),
				StrictMath.sinh(x), StrictMath.cosh(x), StrictMath.tanh(x),
			};
			out.append(Long.toHexString(Double.doubleToRawLongBits(x))).append(' ').append(Long.toHexString(Double.doubleToRawLongBits(y)));
			for (double d : v) out.append(' ').append(Long.toHexString(Double.doubleToRawLongBits(d)));
			out.append('\n');
		}
		System.out.print(out);
	}
}
