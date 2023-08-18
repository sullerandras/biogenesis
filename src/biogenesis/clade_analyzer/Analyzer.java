package biogenesis.clade_analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.ProgressMonitor;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import biogenesis.BioFile;
import biogenesis.clade_analyzer.db.DB;

/**
 * Reads json files of the backups and saves the summary in a sqlite database.
 * It keeps a record of which backups have been analyzed and skips the ones that are already processed.
 */
public class Analyzer {
  public static void analyze(BioFile bioFile, DB db, ProgressMonitor progressMonitor)
      throws JsonIOException, JsonSyntaxException, SQLException, IOException {
    db.createTables();
    String jsonFilePrefix = bioFile.getWorldName() + "@";// we only process json files that start with this prefix

    File[] files = bioFile.getFile().getParentFile().listFiles();
    Arrays.sort(files);
    List<File> filesToProcess = new ArrayList<>();
    for (File file : files) {
      if (file.getName().startsWith(jsonFilePrefix) && file.getName().endsWith(".json")) {
        filesToProcess.add(file);
      }
    }

    for (int i = 0; i < filesToProcess.size(); i++) {
      File file = filesToProcess.get(i);
      if (progressMonitor != null) {
        if (progressMonitor.isCanceled()) {
          return;
        }

        final int index = i;
        java.awt.EventQueue.invokeLater(() -> {
          progressMonitor.setProgress(index * 100 / filesToProcess.size());
          progressMonitor.setNote("Analyzing " + file.getName());
        });
      }

      analyzeJsonFile(file, db);
    }
  }

  private static void analyzeJsonFile(File file, DB db)
      throws JsonIOException, JsonSyntaxException, FileNotFoundException, SQLException {

    if (db.isSummaryFileDone(file)) {
      // System.out.println("Skipping " + file.getName() + " because it is already done");
      return;
    }
    System.out.println("Analyzing " + file.getName());

    db.startTransaction(); // using transactions to speed up the inserts
    try {
      JsonObject jsonObject = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
      int time = jsonObject.get("worldStatistics").getAsJsonObject().get("time").getAsInt();

      int summaryFileId = db.upsertSummaryFileToPending(file, time);
      db.deleteRecordsForSummaryFile(summaryFileId);

      Map<String, List<GeneticCodeAndCoordinates>> cladeIdToGeneticCodes = new HashMap<>();

      for (JsonElement organism : jsonObject.get("_organisms").getAsJsonArray()) {
        if (organism.getAsJsonObject().get("alive").getAsBoolean() == false) {
          continue;
        }

        String cladeId = organism.getAsJsonObject().get("_geneticCode").getAsJsonObject().get("_cladeID").getAsString();
        String geneticCode = organism.getAsJsonObject().get("_geneticCode").getAsJsonObject().toString();
        int x = organism.getAsJsonObject().get("_centerX").getAsInt();
        int y = organism.getAsJsonObject().get("_centerY").getAsInt();

        cladeIdToGeneticCodes.putIfAbsent(cladeId, new ArrayList<>());
        cladeIdToGeneticCodes.get(cladeId).add(new GeneticCodeAndCoordinates(geneticCode, x, y));
        // db.insertCladeSummary(cladeId, time, geneticCode);
        // System.out.println("====> cladeId: "+cladeId+", time: "+time+", geneticCode: "+geneticCode);
      }

      for (String cladeId : cladeIdToGeneticCodes.keySet()) {
        Entry<String, Long> max = cladeIdToGeneticCodes.get(cladeId)
            .stream()
            .map(o -> o.geneticCode)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .get();

        String geneticCode = max.getKey();
        // Long count = max.getValue();
        // System.out.println(
        //     "====> cladeId: " + cladeId + ", time: " + time + ", geneticCode: " + geneticCode + ", count: " + count);
        final int cladePopulationId = db.insertCladeSummary(summaryFileId, cladeId, time, geneticCode,
            cladeIdToGeneticCodes.get(cladeId).size());

        for (GeneticCodeAndCoordinates o : cladeIdToGeneticCodes.get(cladeId)) {
          db.insertOrganism(cladePopulationId, o.x, o.y);
        }
      }

      db.markSummaryFileDone(summaryFileId);
      db.commitTransaction();
    } catch (FileNotFoundException | SQLException | RuntimeException e) {
      db.rollbackTransaction();
      throw e;
    }
  }
}
