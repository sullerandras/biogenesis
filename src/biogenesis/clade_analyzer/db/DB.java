package biogenesis.clade_analyzer.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import biogenesis.BioFile;
import biogenesis.clade_analyzer.CladeDetails;
import biogenesis.clade_analyzer.TimeAndPopulation;
import biogenesis.clade_analyzer.db.models.DBClade;
import biogenesis.clade_analyzer.db.models.DBCladePopulation;
import biogenesis.clade_analyzer.db.models.DBGeneticCode;
import biogenesis.clade_analyzer.db.models.DBOrganism;
import biogenesis.clade_analyzer.db.models.DBSummaryFile;

/**
 * Simple abstraction for the clade database.
 */
public class DB {
  public static final String SCHEMA_MIGRATIONS_TABLE = "schema_migrations";

  private final BioFile dbFile;
  private final Connection connection;
  private Statement statement;

  public DB(BioFile dbFile) throws ClassNotFoundException, SQLException {
    this.dbFile = dbFile;
    connection = open(dbFile.getSqliteFile());
  }

  public BioFile getDbFile() {
    return dbFile;
  }

  private Connection open(File dbFile) throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    return DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
  }

  public void close() throws SQLException {
    if (statement != null) {
      statement.close();
    }
    connection.close();
  }

  public void createTables() throws SQLException, IOException {
    executeUpdate("CREATE TABLE IF NOT EXISTS " + SCHEMA_MIGRATIONS_TABLE + " (" +
        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "FILENAME TEXT UNIQUE " +
        ")");
    executeMigration("1.sql");
    executeMigration("2.sql");
  }

  public boolean isSummaryFileDone(File summaryFile) throws SQLException {
    return new DBSummaryFile(this).isSummaryFileDone(summaryFile);
  }

  public int upsertSummaryFileToPending(File summaryFile, int time) throws SQLException {
    return new DBSummaryFile(this).upsertSummaryFileToPending(summaryFile, time);
  }

  public int getSummaryFileId(File summaryFile) throws SQLException {
    return new DBSummaryFile(this).getSummaryFileId(summaryFile);
  }

  public void deleteRecordsForSummaryFile(int summaryFileId) throws SQLException {
    new DBCladePopulation(this).deleteRecordsForSummaryFile(summaryFileId);
  }

  public void markSummaryFileDone(int summaryFileId) throws SQLException {
    new DBSummaryFile(this).markSummaryFileDone(summaryFileId);
  }

  /**
   * Inserts a clade summary into the database and returns the cladePopulationId.
   * We can use the returned cladePopulationId to insert the organisms for this clade.
   */
  public int insertCladeSummary(int summaryFileId, String cladeId, int time, String geneticCode, int population)
      throws SQLException {
    final int geneticCodeId = new DBGeneticCode(this).insertAndReturnId(geneticCode);
    final DBClade dbClade = new DBClade(this);
    final int intCladeId = dbClade.insertAndReturnId(cladeId, geneticCodeId, time, time, population);
    final int cladePopulationId = new DBCladePopulation(this).insertAndReturnId(intCladeId, summaryFileId,
        geneticCodeId, population);
    dbClade.updateLastSeenTimeAndGeneticCodeId(intCladeId, time, geneticCodeId);
    dbClade.updateMaxPopulation(intCladeId, population);

    return cladePopulationId;
  }

  public Promise<List<CladeDetails>> getLongestSurvivors(int limit) {
    Promise<List<CladeDetails>> promise = new Promise<>();

    new Job<List<CladeDetails>>(promise) {
      @Override
      public List<CladeDetails> run() throws SQLException {
        return getLongestSurvivorsSync(limit);
      }
    };

    return promise;
  }

  public List<CladeDetails> getLongestSurvivorsSync(int limit) throws SQLException {
    return new DBClade(this).getLongestSurvivorsSync(limit);
  }

  public Promise<List<TimeAndPopulation>> getPopulationHistory(String cladeIdStr) {
    Promise<List<TimeAndPopulation>> promise = new Promise<>();

    new Job<List<TimeAndPopulation>>(promise) {
      @Override
      public List<TimeAndPopulation> run() throws SQLException {
        return getPopulationHistorySync(cladeIdStr);
      }
    };

    return promise;
  }

  public List<TimeAndPopulation> getPopulationHistorySync(String cladeId) throws SQLException {
    return new DBCladePopulation(this).getPopulationHistorySync(cladeId);
  }

  public Promise<List<CladeDetails>> getMostPopulousClades(int time, int limit) {
    Promise<List<CladeDetails>> promise = new Promise<>();

    new Job<List<CladeDetails>>(promise) {
      @Override
      public List<CladeDetails> run() throws SQLException {
        return getMostPopulousCladesSync(time, limit);
      }
    };

    return promise;
  }

  public List<CladeDetails> getMostPopulousCladesSync(int time, int limit) throws SQLException {
    return new DBCladePopulation(this).getMostPopulousCladesSync(time, limit);
  }

  public Promise<List<Integer>> getListOfTimes() {
    Promise<List<Integer>> promise = new Promise<>();

    new Job<List<Integer>>(promise) {
      @Override
      public List<Integer> run() throws SQLException {
        return getListOfTimesSync();
      }
    };

    return promise;
  }

  public List<Integer> getListOfTimesSync() throws SQLException {
    return new DBSummaryFile(this).getListOfTimesSync();
  }

  public Promise<List<CladeDetails>> getOrganismsAtTime(int time) {
    Promise<List<CladeDetails>> promise = new Promise<>();

    new Job<List<CladeDetails>>(promise) {
      @Override
      public List<CladeDetails> run() throws SQLException {
        return getOrganismsAtTimeSync(time);
      }
    };

    return promise;
  }

  public List<CladeDetails> getOrganismsAtTimeSync(int time) throws SQLException {
    return new DBOrganism(this).getOrganismsAtTimeSync(time);
  }

  public List<CladeDetails> getCladeAncestors(String cladeIdStr) throws SQLException {
    return new DBClade(this).getCladeAncestors(cladeIdStr);
  }

  public int getMaxTime() throws SQLException {
    return new DBSummaryFile(this).getMaxTime();
  }

  public int getCladeCount() throws SQLException {
    return new DBClade(this).getCladeCount();
  }

  public int getCladeCountAtTime(int time) throws SQLException {
    return new DBClade(this).getCladeCountAtTime(time);
  }

  public void insertOrganism(int cladePopulationId, int x, int y) throws SQLException {
    new DBOrganism(this).insertOrganism(cladePopulationId, x, y);
  }

  public void executeUpdate(String sql) throws SQLException {
    // System.out.println(sql);
    synchronized (this) {
      getStatement().executeUpdate(sql);
    }
  }

  public String executeQueryString(String sql, ResultSetProcessor<String> resultSetProcessor) throws SQLException {
    // System.out.println(sql);
    synchronized (this) {
      return resultSetProcessor.processResultSet(getStatement().executeQuery(sql));
    }
  }

  public Integer executeQueryInteger(String sql, ResultSetProcessor<Integer> resultSetProcessor) throws SQLException {
    // System.out.println(sql);
    synchronized (this) {
      return resultSetProcessor.processResultSet(getStatement().executeQuery(sql));
    }
  }

  public List<CladeDetails> executeQueryListOfCladeDetails(String sql,
      ResultSetProcessor<List<CladeDetails>> resultSetProcessor) throws SQLException {
    // System.out.println(sql);
    synchronized (this) {
      return resultSetProcessor.processResultSet(getStatement().executeQuery(sql));
    }
  }

  public List<TimeAndPopulation> executeQueryListOfTimeAndPopulation(String sql,
      ResultSetProcessor<List<TimeAndPopulation>> resultSetProcessor) throws SQLException {
    // System.out.println(sql);
    synchronized (this) {
      return resultSetProcessor.processResultSet(getStatement().executeQuery(sql));
    }
  }

  public List<Integer> executeQueryListOfIntegers(String sql, ResultSetProcessor<List<Integer>> resultSetProcessor)
      throws SQLException {
    // System.out.println(sql);
    synchronized (this) {
      return resultSetProcessor.processResultSet(getStatement().executeQuery(sql));
    }
  }

  private Statement getStatement() throws SQLException {
    if (statement == null) {
      statement = connection.createStatement();
    }
    return statement;
  }

  private boolean isMigrationExecuted(String filename) throws SQLException {
    String f = executeQueryString("SELECT * FROM " + SCHEMA_MIGRATIONS_TABLE + " WHERE FILENAME = '" + filename + "'",
        rs -> rs.getString("FILENAME"));
    return f != null;
  }

  private void executeMigration(String filename) throws IOException, SQLException {
    if (isMigrationExecuted(filename)) {
      return;
    }

    executeUpdate(readMigrationFile(filename));
    executeUpdate("INSERT INTO " + SCHEMA_MIGRATIONS_TABLE + " (FILENAME) VALUES ('" + filename + "')");
  }

  private String readMigrationFile(String filename) throws IOException {
    return new String(getClass().getResourceAsStream("migrations/" + filename).readAllBytes());
  }

  public void startTransaction() throws SQLException {
    connection.setAutoCommit(false);
  }

  public void commitTransaction() throws SQLException {
    connection.commit();
  }

  public void rollbackTransaction() throws SQLException {
    connection.rollback();
  }
}
