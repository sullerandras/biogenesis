package biogenesis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

public class CladeStats {
  public static void printStats(List<Organism> organisms) {
    System.out.println("===================================================");
    System.out.println("Clade stats:");
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

    CladeStore cladeStore = new CladeStore();

    for (int i = 0; i < sortedCladeIds.size(); i++) {
      Entry<String, Long> e = sortedCladeIds.get(i);

      CladeId cladeId = new CladeId(e.getKey());
      Clade clade = new Clade(cladeId, e.getValue().intValue(), cladeIdToGeneticCode.get(cladeId));
      cladeStore.add(clade);
    }

    cladeStore.print();

    if (cladeStore.getCladeCount() == 0) {
      return;
    }

    File f = new File("cladestats.png"); //$NON-NLS-1$
    final BufferedImage img = new BufferedImage(1000, 100 * cladeStore.getCladeCount(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (java.awt.Graphics2D) img.getGraphics();
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, img.getWidth(), img.getHeight());
    cladeStore.draw(g);
    try {
      ImageIO.write(img, "PNG", f); //$NON-NLS-1$
    } catch (FileNotFoundException ex) {
      System.err.println(ex.getMessage());
    } catch (IOException ex) {
      System.err.println(ex.getMessage());
    }
  }
}
