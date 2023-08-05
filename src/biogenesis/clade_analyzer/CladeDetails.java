package biogenesis.clade_analyzer;

import biogenesis.GeneticCode;

public class CladeDetails {
  private final String cladeId;
  private final GeneticCode geneticCode;

  // from "clades" table

  private final int firstSeenTime;
  private final int lastSeenTime;
  private final int maxPopulation;

  // from "clade_summaries" table

  private final int time;
  private final int population;

  public CladeDetails(String cladeId, int firstSeenTime, int lastSeenTime, GeneticCode geneticCode, int maxPopulation) {
    this(cladeId, firstSeenTime, lastSeenTime, geneticCode, maxPopulation, 0, 0);
  }

  public CladeDetails(String cladeId, int time, GeneticCode geneticCode, int population) {
    this(cladeId, 0, 0, geneticCode, 0, time, population);
  }

  public CladeDetails(String cladeId, int firstSeenTime, int lastSeenTime, GeneticCode geneticCode, int maxPopulation,
      int time, int population) {
    this.cladeId = cladeId;
    this.firstSeenTime = firstSeenTime;
    this.lastSeenTime = lastSeenTime;
    this.geneticCode = geneticCode;
    this.maxPopulation = maxPopulation;
    this.time = time;
    this.population = population;
  }

  public String getCladeId() {
    return cladeId;
  }

  public int getFirstSeenTime() {
    return firstSeenTime;
  }

  public int getLastSeenTime() {
    return lastSeenTime;
  }

  public GeneticCode getGeneticCode() {
    return geneticCode;
  }

  public int getMaxPopulation() {
    return maxPopulation;
  }

  public int getTime() {
    return time;
  }

  public int getPopulation() {
    return population;
  }

  public String toString() {
    return "CladeSummary [cladeId=" + cladeId +
        ", firstSeenTime=" + firstSeenTime +
        ", lastSeenTime=" + lastSeenTime
        + ", geneticCode=" + geneticCode +
        ", maxPopulation=" + maxPopulation +
        ", time=" + time +
        ", population=" + population + "]";
  }
}
