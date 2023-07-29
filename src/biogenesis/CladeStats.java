package biogenesis;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CladeStats {
  public static void printStats(List<Organism> organisms) {
    System.out.println("===================================================");
    System.out.println("Clade stats:");
    List<Entry<String, Long>> sortedCladeIds = organisms
        .stream()
        .filter(o -> o.isAlive())
        .collect(Collectors.groupingBy(o -> o.getGeneticCode().getcladeID(), Collectors.counting()))
        .entrySet()
        .stream()
        .filter(e -> e.getValue() >= 10) // only show clades with at least 10 organisms
        .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
        .collect(Collectors.toList());

    CladeStore cladeStore = new CladeStore();

    for (int i = 0; i < sortedCladeIds.size(); i++) {
      Entry<String, Long> e = sortedCladeIds.get(i);

      Clade clade = new Clade(new CladeId(e.getKey()), e.getValue().intValue());
      cladeStore.add(clade);
    }

    cladeStore.print();
  }
}
