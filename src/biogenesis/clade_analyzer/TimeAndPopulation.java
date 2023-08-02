package biogenesis.clade_analyzer;

public class TimeAndPopulation {
  private final int time;
  private final int population;

  public TimeAndPopulation(int time, int population) {
    this.time = time;
    this.population = population;
  }

  public int getTime() {
    return time;
  }

  public int getPopulation() {
    return population;
  }
}
