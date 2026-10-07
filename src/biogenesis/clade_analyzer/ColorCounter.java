package biogenesis.clade_analyzer;

import java.util.LinkedHashMap;
import java.util.Map;

import biogenesis.GeneColor;

public class ColorCounter {
  private Map<GeneColor, Integer> colorCount = new LinkedHashMap<>();

  public ColorCounter() {
    ////////////// primary segments //////////////
    // consumer
    colorCount.put(GeneColor.RED, 0);
    colorCount.put(GeneColor.FIRE, 0);
    colorCount.put(GeneColor.ORANGE, 0);
    colorCount.put(GeneColor.MAROON, 0);
    colorCount.put(GeneColor.CRIMSON, 0);
    colorCount.put(GeneColor.PINK, 0);
    colorCount.put(GeneColor.CREAM, 0);
    // virus
    colorCount.put(GeneColor.WHITE, 0);
    colorCount.put(GeneColor.CORAL, 0);
    // silver
    colorCount.put(GeneColor.SILVER, 0);
    // plant
    colorCount.put(GeneColor.GREEN, 0);
    colorCount.put(GeneColor.FOREST, 0);
    colorCount.put(GeneColor.IVY, 0);
    colorCount.put(GeneColor.SPRING, 0);
    colorCount.put(GeneColor.SUMMER, 0);
    colorCount.put(GeneColor.LIME, 0);
    colorCount.put(GeneColor.LEAF, 0);
    colorCount.put(GeneColor.C4, 0);
    colorCount.put(GeneColor.JADE, 0);
    colorCount.put(GeneColor.GRASS, 0);
    colorCount.put(GeneColor.BARK, 0);
    colorCount.put(GeneColor.PURPLE, 0);
    colorCount.put(GeneColor.PLANKTON, 0);

    ////////////// secondary segments //////////////
    colorCount.put(GeneColor.SPIKE, 0);
    colorCount.put(GeneColor.LILAC, 0);
    colorCount.put(GeneColor.GRAY, 0);
    colorCount.put(GeneColor.VIOLET, 0);
    colorCount.put(GeneColor.OLIVE, 0);
    colorCount.put(GeneColor.SKY, 0);
    colorCount.put(GeneColor.BLUE, 0);
    colorCount.put(GeneColor.OCHRE, 0);
    colorCount.put(GeneColor.FALLOW, 0);
    colorCount.put(GeneColor.PLAGUE, 0);
    colorCount.put(GeneColor.DARK, 0);

    ////////////// tertiary segments //////////////
    colorCount.put(GeneColor.DARKGRAY, 0);
    colorCount.put(GeneColor.SPORE, 0);
    colorCount.put(GeneColor.ROSE, 0);
    colorCount.put(GeneColor.MAGENTA, 0);
    colorCount.put(GeneColor.MINT, 0);
    colorCount.put(GeneColor.LAVENDER, 0);
    colorCount.put(GeneColor.YELLOW, 0);
    colorCount.put(GeneColor.AUBURN, 0);
    colorCount.put(GeneColor.INDIGO, 0);
    colorCount.put(GeneColor.BLOND, 0);
    colorCount.put(GeneColor.FLOWER, 0);
    colorCount.put(GeneColor.GOLD, 0);
    
    //////////////quaternary segments //////////////
    colorCount.put(GeneColor.CYAN, 0);
    colorCount.put(GeneColor.TEAL, 0);
    colorCount.put(GeneColor.DRIFT, 0);
    colorCount.put(GeneColor.SPIN, 0);
    colorCount.put(GeneColor.EYE, 0);
  }

  public void add(GeneColor color) {
    if (colorCount.containsKey(color)) {
      colorCount.put(color, colorCount.get(color) + 1);
    } else {
      colorCount.put(color, 1);
    }
  }

  public int size() {
    return (int) colorCount.entrySet().stream().filter(x -> x.getValue() > 0).count();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<GeneColor, Integer> entry : colorCount.entrySet()) {
      if (entry.getValue() == 0) {
        continue;
      }

      if (sb.length() > 0) {
        sb.append(".");
      }
      if (entry.getValue() > 1) {
        sb.append(entry.getValue());
      }
      sb.append(entry.getKey());
    }
    return sb.toString();
  }
}
