package biogenesis.clade_analyzer;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import biogenesis.Gene;
import biogenesis.GeneticCode;

public class CladeParser {
  public static CladeDetails parse(String cladeId, int firstSeenTime, int lastSeenTime, String geneticCode,
      int maxPopulation) {
    return new CladeDetails(cladeId, firstSeenTime, lastSeenTime, parseGeneticCode(geneticCode), maxPopulation);
  }

  public static CladeDetails parse(String cladeId, int firstSeenTime, int lastSeenTime, String geneticCode,
      int maxPopulation, int time, int population) {
    return new CladeDetails(cladeId, firstSeenTime, lastSeenTime, parseGeneticCode(geneticCode), maxPopulation, time,
        population);
  }

  public static CladeDetails parse(String cladeId, int firstSeenTime, int lastSeenTime, String geneticCode,
      int maxPopulation, int time, int population, int x, int y) {
    return new CladeDetails(cladeId, firstSeenTime, lastSeenTime, parseGeneticCode(geneticCode), maxPopulation, time,
        population, x, y);
  }

  private static GeneticCode parseGeneticCode(String str) {
    JsonObject o = JsonParser.parseString(str).getAsJsonObject();
    List<Gene> genes = o.get("_genes")
        .getAsJsonArray()
        .asList()
        .stream()
        .map(gene -> parseGene(gene.getAsJsonObject()))
        .collect(Collectors.toList());
    int symmetry = o.get("_symmetry").getAsInt();
    int mirror = o.get("_mirror").getAsInt();

    return new GeneticCode(genes, symmetry, mirror, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false);
  }

  private static Gene parseGene(JsonObject gene) {
    double length = gene.get("_length").getAsDouble();
    double theta = gene.get("_theta").getAsDouble();
    Color color = parseColor(gene.get("_color").getAsJsonObject());
    int branch = gene.get("_branch").getAsInt();

    return new Gene(length, theta, color, branch, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
  }

  private static Color parseColor(JsonObject color) {
    return new Color(
        color.get("r").getAsInt(),
        color.get("g").getAsInt(),
        color.get("b").getAsInt(),
        color.get("a").getAsInt());
  }
}
