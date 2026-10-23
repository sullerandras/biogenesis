package biogenesis;

import java.util.Random;

/**
 * Creates random genetic codes and mutates them for generations, printing
 * every code (see Dump). rust/core/src/bin/genetic_code_check.rs does the
 * same with the translated code; the outputs must be identical.
 *
 * Usage: GeneticCodeCheck seed lineages generations [hot]
 * "hot" raises the mutation rates so rare branches run often.
 */
public class GeneticCodeCheck {
	public static void main(String[] args) {
		long seed = Long.parseLong(args[0]);
		int lineages = Integer.parseInt(args[1]);
		int generations = Integer.parseInt(args[2]);
		boolean hot = args.length > 3 && args[3].equals("hot");
		Utils.random = new Random(seed);
		if (hot) {
			Utils.META_MUTATION_RATE = 4000;
			Utils.XY_MUTATION_RATE = 4000;
			Utils.MIN_MUTATION_RATE = 300;
			Utils.MAX_MUTATION_RATE = 6000;
			Utils.MIN_CLONE_RATE = 10;
			Utils.MAX_CLONE_RATE = 90;
			Utils.INITIAL_COMPLEXITY = 6;
		}
		Dump d = new Dump();
		for (int l = 0; l < lineages; l++) {
			GeneticCode code = new GeneticCode();
			d.raw("lineage " + l + "\n");
			d.value(code, GeneticCode.class);
			d.raw("\n");
			for (int g = 0; g < generations; g++) {
				GeneticCode child = new GeneticCode(code);
				if (g % 7 == 3) child = (GeneticCode) child.clone();
				d.value(child, GeneticCode.class);
				d.raw("\n");
				code = child;
			}
		}
		d.raw("random " + Utils.random.nextLong() + "\n");
		System.out.print(d.out);
	}
}
