package biogenesis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CladeStats {
  private final CladeStore cladeStore = new CladeStore();

  public CladeStats(Collection<Organism> organisms) {
    synchronized (organisms) {
      Map<CladeId, GeneticCode> cladeIdToGeneticCode = new HashMap<>();
      for (Organism o : organisms) {
        CladeId cladeId = new CladeId(o.getGeneticCode().getcladeID());
        if (o.isAlive() && !cladeIdToGeneticCode.containsKey(cladeId)) {
          cladeIdToGeneticCode.put(cladeId, o.getGeneticCode());
        }
      }

      List<Entry<String, Long>> sortedCladeIds = organisms
          .stream()
          .filter(o -> o.isAlive())
          .collect(Collectors.groupingBy(o -> o.getGeneticCode().getcladeID(), Collectors.counting()))
          .entrySet()
          .stream()
          .filter(e -> e.getValue() >= 10) // only show clades with at least 10 organisms
          .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
          .collect(Collectors.toList());

      for (int i = 0; i < sortedCladeIds.size(); i++) {
        Entry<String, Long> e = sortedCladeIds.get(i);

        CladeId cladeId = new CladeId(e.getKey());
        Clade clade = new Clade(cladeId, e.getValue().intValue(), cladeIdToGeneticCode.get(cladeId));
        cladeStore.add(clade);
      }

      cladeStore.print();
    }
  }

  /**
   * Returns how big image is needed to renger the clade tree.
   */
  public Rectangle getBounds() {
    return new Rectangle(0, 0, 10 * Clade.CLADE_SIZE, Clade.CLADE_SIZE * cladeStore.getCladeCount());
  }

  /**
   * Draws the clade tree to the given graphics object.
   */
  public void draw(Graphics2D g) {
    if (cladeStore.getCladeCount() == 0) {
      return;
    }

    Rectangle bounds = getBounds();
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, bounds.width, bounds.height);
    cladeStore.draw(g, bounds.width);
  }
}
