package biogenesis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;

public class Clade {
  private final CladeId id;
  private final int population;
  private final CladeList children = new CladeList();
  private final GeneticCode exampleGeneticCode;

  public Clade(CladeId id, int population, GeneticCode exampleGeneticCode) {
    this.id = id;
    this.population = population;
    this.exampleGeneticCode = exampleGeneticCode;
  }

  public CladeId parentId() {
    return id.parentId();
  }

  public CladeId getId() {
    return id;
  }

  public int getPopulation() {
    return population;
  }

  public void addChild(Clade child) {
    children.add(child);
  }

  public List<Clade> getChildren() {
    return children;
  }

  public GeneticCode getExampleGeneticCode() {
    return exampleGeneticCode;
  }

  public String toString() {
    return id.toString();
  }

  public void print(int indentation) {
    System.out.println("  ".repeat(indentation) + id + ": " + population);
    for (Clade clade : children) {
      clade.print(indentation + 1);
    }
  }

  /**
   * Draw this clade and its children onto the given graphics context.
   * @param g
   * @param x
   * @param y
   * @param width
   * @param height
   * @return the total height that was used to draw this clade and its children
   */
  public int draw(Graphics2D g, int x, int y, int width, int height) {
    final int originalY = y;

    g.setColor(Color.WHITE);
    g.drawRect(x, y, width, height);
    g.setColor(Color.BLACK);
    g.fillRect(x + 1, y + 1, width - 2, height - 2);

    AffineTransform orginalAffineTransform = g.getTransform();
    g.translate(x + 1, y + 1);
    exampleGeneticCode.draw(g, width - 2, height - 2);
    g.setTransform(orginalAffineTransform);

    g.setColor(Color.WHITE);
    g.drawString(id.toString(), x + width + 5, y + 15);
    g.drawString(Integer.toString(population), x + width + 5, y + 30);
    if (children.size() == 0) {
      return height;
    }

    y += height;
    int prevY = y;

    for (int i = 0; i < children.size(); i++) {
      int childHeight = children.get(i).draw(g, x + width, y, width, height);

      g.setColor(Color.WHITE);
      g.drawLine(x + width / 2, prevY, x + width / 2, y + height / 2);
      g.drawLine(x + width / 2, y + height / 2, x + width, y + height / 2);

      prevY = y;
      y += childHeight;
    }

    return y - originalY;
  }
}
