package biogenesis.clade_analyzer.gui;

public enum Limit {
  TEN(10, "10"),
  HUNDRED(100, "100"),
  THOUSAND(1000, "1000"),
  ALL(-1, "All");

  private final int limit;
  private final String label;

  private Limit(int limit, String label) {
    this.limit = limit;
    this.label = label;
  }

  public int getLimit() {
    return limit;
  }

  public String toString() {
    return label;
  }
}