package biogenesis.clade_analyzer;

import biogenesis.GeneticCode;

public class CladeSummary {
  private final String cladeId;
  private final int firstSeenTime;
  private final int lastSeenTime;
  private final GeneticCode geneticCode;
  private final int maxPopulation;

  public CladeSummary(String cladeId, int firstSeenTime, int lastSeenTime, GeneticCode geneticCode, int maxPopulation) {
    this.cladeId = cladeId;
    this.firstSeenTime = firstSeenTime;
    this.lastSeenTime = lastSeenTime;
    this.geneticCode = geneticCode;
    this.maxPopulation = maxPopulation;
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

  public String toString() {
    return "CladeSummary [cladeId=" + cladeId + ", firstSeenTime=" + firstSeenTime + ", lastSeenTime=" + lastSeenTime
        + ", geneticCode=" + geneticCode + ", maxPopulation=" + maxPopulation + "]";
  }
}
