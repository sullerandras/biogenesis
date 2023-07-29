package biogenesis;

import java.util.List;

public class Clade {
  private final CladeId id;
  private final int population;
  private final CladeList children = new CladeList();

  public Clade(CladeId id, int population) {
    this.id = id;
    this.population = population;
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

  public String toString() {
    return id.toString();
  }

  public void print(int indentation) {
    System.out.println("  ".repeat(indentation) + id + ": " + population);
    for (Clade clade : children) {
      clade.print(indentation + 1);
    }
  }
}
