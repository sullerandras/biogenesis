package j2rtest;

/** Prints Samples.run for some seeds; rust/core/src/bin/samples_check.rs does the same. */
public class SamplesCheck {
	public static void main(String[] args) {
		for (int seed = 0; seed < 40; seed++) System.out.println(Samples.run(seed));
	}
}
