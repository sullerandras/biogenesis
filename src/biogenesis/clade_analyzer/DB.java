package biogenesis.clade_analyzer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biogenesis.CladeId;

/**
 * Simple abstraction for the clade database.
 */
public class DB {
  public static final String SUMMARY_FILES_TABLE = "summary_files";
  public static final String SUMMARY_FILES_STATE_COLUMN = "state";
  public static final String SUMMARY_FILES_STATES_PENDING = "pending";
  public static final String SUMMARY_FILES_STATES_DONE = "done";

  public static final String CLADE_SUMMARIES_TABLE = "clade_summaries";
  public static final String CLADES_TABLE = "clades";

  private final File dbFile;
  private final Connection connection;

  public DB(File dbFile) throws ClassNotFoundException, SQLException {
    this.dbFile = dbFile;
    connection = open(dbFile);
  }

  public File getDbFile() {
    return dbFile;
  }

  private Connection open(File dbFile) throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    return DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
  }

  public void close() throws SQLException {
    connection.close();
  }

  public void dropTables() throws SQLException {
    executeUpdate("DROP TABLE IF EXISTS " + SUMMARY_FILES_TABLE);
    executeUpdate("DROP TABLE IF EXISTS " + CLADE_SUMMARIES_TABLE);
    executeUpdate("DROP TABLE IF EXISTS " + CLADES_TABLE);
  }

  public void createTables() throws SQLException {
    executeUpdate("CREATE TABLE IF NOT EXISTS " + SUMMARY_FILES_TABLE + " " +
        "(ID INTEGER PRIMARY KEY ASC," +
        " FILENAME       TEXT    UNIQUE NOT NULL, " +
        " STATE          TEXT    NOT NULL " +
        ")");
    executeUpdate("CREATE TABLE IF NOT EXISTS " + CLADE_SUMMARIES_TABLE + " " +
        "(ID INTEGER PRIMARY KEY ASC," +
        " SUMMARY_FILE_ID INTEGER    NOT NULL, " +
        " CLADEID        TEXT        NOT NULL, " +
        " TIME           INTEGER     NOT NULL, " +
        " GENETIC_CODE   TEXT        NOT NULL, " + // this is the most common genetic code for this clade in this time
        " POPULATION     INTEGER     NOT NULL " +
        ")");
    executeUpdate("CREATE TABLE IF NOT EXISTS " + CLADES_TABLE + " " +
        "(ID INTEGER PRIMARY KEY ASC," +
        " CLADEID          TEXT     UNIQUE NOT NULL, " +
        " FIRST_SEEN_TIME  INTEGER  NOT NULL, " +
        " LAST_SEEN_TIME   INTEGER  NOT NULL, " +
        " GENETIC_CODE     TEXT     NOT NULL, " + // this is the genetic code from the clade_summaries table at the last seen time
        " MAX_POPULATION   INTEGER  NOT NULL " +
        ")");
  }

  public boolean isSummaryFileDone(File summaryFile) throws SQLException {
    String state = connection.createStatement().executeQuery(
        "SELECT " + SUMMARY_FILES_STATE_COLUMN +
            " FROM " + SUMMARY_FILES_TABLE +
            " WHERE FILENAME = '" + relativePath(summaryFile) + "'")
        .getString(SUMMARY_FILES_STATE_COLUMN);
    return state != null && state.equals(SUMMARY_FILES_STATES_DONE);
  }

  public void upsertSummaryFileToPending(File summaryFile) throws SQLException {
    executeUpdate("INSERT OR REPLACE INTO " + SUMMARY_FILES_TABLE + " (FILENAME, STATE) " +
        "VALUES ('" + relativePath(summaryFile) + "', '" +
        SUMMARY_FILES_STATES_PENDING + "')");
  }

  public void markSummaryFileDone(File summaryFile) throws SQLException {
    executeUpdate("UPDATE " + SUMMARY_FILES_TABLE + " SET " + SUMMARY_FILES_STATE_COLUMN + " = '" +
        SUMMARY_FILES_STATES_DONE + "' WHERE FILENAME = '" +
        relativePath(summaryFile) + "'");
  }

  public void insertCladeSummary(File summaryFile, String cladeId, int time, String geneticCode, int population)
      throws SQLException {
    executeUpdate("INSERT INTO " + CLADE_SUMMARIES_TABLE
        + " (SUMMARY_FILE_ID, CLADEID, TIME, GENETIC_CODE, POPULATION) " +
        "VALUES (" +
        "(SELECT ID FROM " + SUMMARY_FILES_TABLE + " WHERE FILENAME = '" +
        relativePath(summaryFile) + "'), '" + cladeId + "', " + time + ", '" + geneticCode + "', " + population + ")");
    executeUpdate("INSERT OR IGNORE INTO " + CLADES_TABLE
        + " (CLADEID, FIRST_SEEN_TIME, LAST_SEEN_TIME, GENETIC_CODE, MAX_POPULATION) " +
        "VALUES ('" + cladeId + "', " + time + ", " + time + ", '" + geneticCode + "', " + population + ")");
    executeUpdate("UPDATE " + CLADES_TABLE + " SET " +
        "LAST_SEEN_TIME = " + time + ", " +
        "GENETIC_CODE = '" + geneticCode + "' " +
        "WHERE CLADEID = '" + cladeId + "'");
    executeUpdate("UPDATE " + CLADES_TABLE + " SET " +
        "MAX_POPULATION = " + population + " " +
        "WHERE CLADEID = '" + cladeId + "' AND MAX_POPULATION < " + population);
  }

  public void deleteRecordsForSummaryFile(File summaryFile) throws SQLException {
    executeUpdate("DELETE FROM " + CLADE_SUMMARIES_TABLE + " WHERE SUMMARY_FILE_ID = " +
        "(SELECT ID FROM " + SUMMARY_FILES_TABLE + " WHERE FILENAME = '" +
        relativePath(summaryFile) + "')");
  }

  public List<CladeSummary> getCladeSummaries() throws SQLException {
    ResultSet rs = executeQuery("select * from clades order by LAST_SEEN_TIME - FIRST_SEEN_TIME desc limit 10");

    return readCladeSummaries(rs);
  }

  public List<CladeSummary> getCladeAncestors(String cladeIdStr) throws SQLException {
    CladeId cladeId = new CladeId(cladeIdStr);

    List<CladeSummary> ancestors = new ArrayList<>();

    while (cladeId != null) {
      ResultSet rs = executeQuery("select * from clades where CLADEID = '" + cladeId.getId() + "'");

      ancestors.addAll(readCladeSummaries(rs));

      cladeId = cladeId.parentId();
    }

    return ancestors;
  }

  private List<CladeSummary> readCladeSummaries(ResultSet rs) throws SQLException {
    List<CladeSummary> list = new ArrayList<>();

    try {
      while (rs.next()) {
        list.add(CladeSummaryParser.parse(
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

  private void executeUpdate(String sql) throws SQLException {
    // System.out.println(sql);
    connection.createStatement().executeUpdate(sql);
  }

  private ResultSet executeQuery(String sql) throws SQLException {
    // System.out.println(sql);
    return connection.createStatement().executeQuery(sql);
  }

  private String relativePath(File file) {
    return new File(".").toPath().relativize(file.toPath()).toString();
  }
}
