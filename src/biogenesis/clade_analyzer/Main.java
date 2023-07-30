package biogenesis.clade_analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * Reads json files of the backups and saves the summary in a sqlite database.
 * It keeps a record of which backups have been analyzed and skips the ones that are already processed.
 */
public class Main {
  public static void main(String args[]) {
    if (args.length != 1) {
      System.err.println("Usage: java biogenesis.clade_analyzer.Main <backup_dir>");
      System.exit(1);
    }

    DB db = null;

    try {
      String backupDir = args[0];

      db = new DB(new File(backupDir, "clades.sqlite"));
      // db.dropTables();
      db.createTables();

      File[] files = new File(backupDir).listFiles();
      Arrays.sort(files);
      for (File file : files) {
        if (file.getName().endsWith(".json")) {
          System.out.println("Analyzing " + file.getName());
          analyzeJsonFile(file, db);
        }
      }
    } catch (Exception e) {
      System.err.println("Error: " + e);
      e.printStackTrace();
    } finally {
      if (db != null) {
        try {
          db.close();
        } catch (SQLException e) {
          System.err.println("Error closing database: " + e);
          e.printStackTrace();
        }
      }
    }
  }

  private static void analyzeJsonFile(File file, DB db)
      throws JsonIOException, JsonSyntaxException, FileNotFoundException, SQLException {

    if (db.isSummaryFileDone(file)) {
      System.out.println("Skipping " + file.getName() + " because it is already done");
      return;
    }

    db.upsertSummaryFileToPending(file);
    db.deleteRecordsForSummaryFile(file);

    Map<String, List<String>> cladeIdToGeneticCodes = new HashMap<>();

    JsonObject jsonObject = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
    int time = jsonObject.get("worldStatistics").getAsJsonObject().get("time").getAsInt();
    for (JsonElement organism : jsonObject.get("_organisms").getAsJsonArray()) {
      if (organism.getAsJsonObject().get("alive").getAsBoolean() == false) {
        continue;
      }

      String cladeId = organism.getAsJsonObject().get("_geneticCode").getAsJsonObject().get("_cladeID").getAsString();
      String geneticCode = organism.getAsJsonObject().get("_geneticCode").getAsJsonObject().toString();

      cladeIdToGeneticCodes.putIfAbsent(cladeId, new ArrayList<>());
      cladeIdToGeneticCodes.get(cladeId).add(geneticCode);
      // db.insertCladeSummary(cladeId, time, geneticCode);
      // System.out.println("====> cladeId: "+cladeId+", time: "+time+", geneticCode: "+geneticCode);
    }

    for (String cladeId : cladeIdToGeneticCodes.keySet()) {
      Entry<String, Long> max = cladeIdToGeneticCodes.get(cladeId)
          .stream()
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
          .entrySet()
          .stream()
          .max(Map.Entry.comparingByValue())
          .get();

      String geneticCode = max.getKey();
      // Long count = max.getValue();
      // System.out.println(
      //     "====> cladeId: " + cladeId + ", time: " + time + ", geneticCode: " + geneticCode + ", count: " + count);
      db.insertCladeSummary(file, cladeId, time, geneticCode, cladeIdToGeneticCodes.get(cladeId).size());
    }

    db.markSummaryFileDone(file);
  }
}
