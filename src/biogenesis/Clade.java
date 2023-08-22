package biogenesis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;

public class Clade {
  /**
   * The size of the border around the clade.
   */
  public static final int BORDER_SIZE = 1;
  /**
   * The size of the padding around the clade.
   */
  public static final int PADDING_SIZE = 5;
  /**
   * The size of the clade.
   */
  public static final int CLADE_SIZE = 100 + 2 * (BORDER_SIZE + PADDING_SIZE);
  /**
   * The size of the clade without border and padding.
   */
  public static final int NET_CLADE_SIZE = CLADE_SIZE - 2 * (BORDER_SIZE + PADDING_SIZE);

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
	for (int i = 0; i < indentation; i++) {
	     System.out.print("  ");
	}
	System.out.println(id + ": " + population);
    for (Clade clade : children) {
      clade.print(indentation + 1);
    }
  }

  /**
   * Draw this clade and its children onto the given graphics context.
   * @param g
   * @param x
   * @param y
   * @return the total height that was used to draw this clade and its children
   */
  public int draw(Graphics2D g, int x, int y) {
    final int originalY = y;

    g.setColor(Color.WHITE);
    g.drawRect(x + PADDING_SIZE, y + PADDING_SIZE, CLADE_SIZE - 2 * PADDING_SIZE, CLADE_SIZE - 2 * PADDING_SIZE);

    AffineTransform orginalAffineTransform = g.getTransform();
    g.translate(x + BORDER_SIZE + PADDING_SIZE, y + BORDER_SIZE + PADDING_SIZE);
    exampleGeneticCode.draw(g, NET_CLADE_SIZE, NET_CLADE_SIZE);
    g.setTransform(orginalAffineTransform);

    g.setColor(Color.WHITE);
    g.drawString(id.toString(), x + CLADE_SIZE + 5, y + 15);
    g.drawString(Integer.toString(population), x + CLADE_SIZE + 5, y + 30);
    if (children.size() == 0) {
      return CLADE_SIZE;
    }

    y += CLADE_SIZE;
    int prevY = y;

    for (int i = 0; i < children.size(); i++) {
      int childHeight = children.get(i).draw(g, x + CLADE_SIZE, y);

      g.setColor(Color.WHITE);
      g.drawLine(x + CLADE_SIZE / 2, prevY - PADDING_SIZE, x + CLADE_SIZE / 2, y + CLADE_SIZE / 2);
      g.drawLine(x + CLADE_SIZE / 2, y + CLADE_SIZE / 2, x + CLADE_SIZE + PADDING_SIZE, y + CLADE_SIZE / 2);

      prevY = y;
      y += childHeight;
    }

    return y - originalY;
  }
}
