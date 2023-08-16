package biogenesis.clade_analyzer.db.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biogenesis.clade_analyzer.CladeDetails;
import biogenesis.clade_analyzer.CladeParser;
import biogenesis.clade_analyzer.db.DB;

public class Base {
  protected final DB db;
  protected static final Map<String, Long> executionTimes = new HashMap<>();
  protected static final Map<String, Long> executionCounts = new HashMap<>();

  static {
    new Thread() {
      @Override
      public void run() {
        while (true) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          synchronized (executionTimes) {
            if (executionTimes.size() > 0) {
              System.out.println("SQL execution times:");
              executionTimes.entrySet().stream().map(entry -> {
                return String.format("total %.9f sec, %9d calls, %.9f sec/call - %s\n",
                    entry.getValue() / 1_000_000_000.0,
                    executionCounts.get(entry.getKey()),
                    entry.getValue() / 1_000_000_000.0 / executionCounts.get(entry.getKey()),
                    entry.getKey());
              }).sorted().forEach(System.out::print);
              executionTimes.clear();
              executionCounts.clear();
            }
          }
        }
      }
    }.start();
  }

  public Base(DB db) {
    this.db = db;
  }

  protected void addTime(String sql, long time) {
    synchronized (executionTimes) {
      executionTimes.put(sql, executionTimes.getOrDefault(sql, 0L) + time);
      executionCounts.put(sql, executionCounts.getOrDefault(sql, 0L) + 1);
    }
  }

  protected void executeUpdate(String sql) throws SQLException {
    long start = System.nanoTime();
    db.executeUpdate(sql);
    addTime(sql.substring(0, 30), System.nanoTime() - start);
  }

  protected ResultSet executeQuery(String sql) throws SQLException {
    long start = System.currentTimeMillis();
    ResultSet x = db.executeQuery(sql);
    addTime(sql.substring(0, 30), System.currentTimeMillis() - start);
    return x;
  }

  protected List<CladeDetails> readCladeSummaries(ResultSet rs) throws SQLException {
    List<CladeDetails> list = new ArrayList<>();

    try {
      while (rs.next()) {
        list.add(CladeParser.parse(
            rs.getString("CLADEID"),
            rs.getInt("FIRST_SEEN_TIME"),
            rs.getInt("LAST_SEEN_TIME"),
            rs.getString("GENETIC_CODE"),
            rs.getInt("MAX_POPULATION")));
      }
    } finally {
      rs.close();
    }

    return list;
  }

  protected List<CladeDetails> readFullCladeDetails(ResultSet rs) throws SQLException {
    List<CladeDetails> list = new ArrayList<>();

    try {
      while (rs.next()) {
        list.add(CladeParser.parse(
            rs.getString("CLADEID"),
            rs.getInt("FIRST_SEEN_TIME"),
            rs.getInt("LAST_SEEN_TIME"),
            rs.getString("GENETIC_CODE"),
            rs.getInt("MAX_POPULATION"),
            rs.getInt("TIME"),
            rs.getInt("POPULATION")));
      }
    } finally {
      rs.close();
    }

    return list;
  }
}
