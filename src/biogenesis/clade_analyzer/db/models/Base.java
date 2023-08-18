package biogenesis.clade_analyzer.db.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biogenesis.clade_analyzer.CladeDetails;
import biogenesis.clade_analyzer.CladeParser;
import biogenesis.clade_analyzer.Logger;
import biogenesis.clade_analyzer.TimeAndPopulation;
import biogenesis.clade_analyzer.db.DB;
import biogenesis.clade_analyzer.db.JobManager;
import biogenesis.clade_analyzer.db.ResultSetProcessor;

public class Base {
  private static final boolean debug = System.getenv().getOrDefault("BIOSIM_CLADE_ANALYZER_DEBUG", "false")
      .equals("true");

  protected final DB db;
  protected static final Map<String, Long> executionTimes = new HashMap<>();
  protected static final Map<String, Long> executionCounts = new HashMap<>();

  static {
    if (debug) {
      new Thread() {
        @Override
        public void run() {
          while (true) {
            try {
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              Logger.printStackTrace(e);
            }
            synchronized (executionTimes) {
              if (executionTimes.size() > 0) {
                Logger.println("SQL execution times:");
                executionTimes.entrySet().stream().map(entry -> {
                  return String.format("total %.9f sec, %9d calls, %.9f sec/call - %s\n",
                      entry.getValue() / 1_000_000_000.0,
                      executionCounts.get(entry.getKey()),
                      entry.getValue() / 1_000_000_000.0 / executionCounts.get(entry.getKey()),
                      entry.getKey());
                }).sorted().forEach(Logger::print);
                executionTimes.clear();
                executionCounts.clear();
              }
            }
          }
        }
      }.start();
    }
  }

  public Base(DB db) {
    this.db = db;
  }

  protected void addTime(String sql, long time) {
    if (debug) {
      synchronized (executionTimes) {
        executionTimes.put(sql, executionTimes.getOrDefault(sql, 0L) + time);
        executionCounts.put(sql, executionCounts.getOrDefault(sql, 0L) + 1);
      }
    }
  }

  protected void executeUpdate(String sql) throws SQLException {
    long start = System.nanoTime();
    db.executeUpdate(sql);
    addTime(sql.substring(0, 30), System.nanoTime() - start);
  }

  protected String executeQueryString(String sql, ResultSetProcessor<String> resultSetProcessor) throws SQLException {
    long start = System.nanoTime();
    String x = db.executeQueryString(sql, resultSetProcessor);
    addTime(sql.substring(0, 30), System.nanoTime() - start);
    return x;
  }

  protected Integer executeQueryInteger(String sql, ResultSetProcessor<Integer> resultSetProcessor)
      throws SQLException {
    long start = System.nanoTime();
    Integer x = db.executeQueryInteger(sql, resultSetProcessor);
    addTime(sql.substring(0, 30), System.nanoTime() - start);
    return x;
  }

  protected List<CladeDetails> executeQueryListOfCladeDetails(String sql,
      ResultSetProcessor<List<CladeDetails>> resultSetProcessor) throws SQLException {
    long start = System.nanoTime();
    List<CladeDetails> x = db.executeQueryListOfCladeDetails(sql, resultSetProcessor);
    addTime(sql.substring(0, 30), System.nanoTime() - start);
    return x;
  }

  protected List<TimeAndPopulation> executeQueryListOfTimeAndPopulation(String sql,
      ResultSetProcessor<List<TimeAndPopulation>> resultSetProcessor) throws SQLException {
    long start = System.nanoTime();
    List<TimeAndPopulation> x = db.executeQueryListOfTimeAndPopulation(sql, resultSetProcessor);
    addTime(sql.substring(0, 30), System.nanoTime() - start);
    return x;
  }

  protected List<Integer> executeQueryListOfIntegers(String sql,
      ResultSetProcessor<List<Integer>> resultSetProcessor) throws SQLException {
    long start = System.nanoTime();
    List<Integer> x = db.executeQueryListOfIntegers(sql, resultSetProcessor);
    addTime(sql.substring(0, 30), System.nanoTime() - start);
    return x;
  }

  protected List<TimeAndPopulation> readTimeAndPopulations(ResultSet rs) throws SQLException {
    List<TimeAndPopulation> list = new ArrayList<>();

    while (rs.next()) {
      list.add(new TimeAndPopulation(rs.getInt("TIME"), rs.getInt("POPULATION")));
    }

    return list;
  }

  protected List<Integer> readListOfIntegers(ResultSet rs) throws SQLException {
    List<Integer> times = new java.util.ArrayList<Integer>();
    while (rs.next()) {
      times.add(rs.getInt(1));
    }
    return times;
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

  protected List<CladeDetails> readCladeDetailsWithTimeAndPopulation(ResultSet rs) throws SQLException {
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

  protected List<CladeDetails> readCladeDetailsWithTimeAndPopulationAndCoordinates(ResultSet rs) throws SQLException {
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
            rs.getInt("POPULATION"),
            rs.getInt("X"),
            rs.getInt("Y")));
      }
    } finally {
      rs.close();
    }

    return list;
  }
}
