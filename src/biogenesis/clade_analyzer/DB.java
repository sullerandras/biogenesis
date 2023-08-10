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
  public static final String COLORS_TABLE = "colors";

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
    executeUpdate("DROP TABLE IF EXISTS " + COLORS_TABLE);
  }

  public void createTables() throws SQLException {
    executeUpdate("CREATE TABLE IF NOT EXISTS " + COLORS_TABLE + " " +
        "(ID INTEGER PRIMARY KEY ASC," +
        " COLOR            TEXT     UNIQUE NOT NULL, " +
        " NAME             TEXT     NOT NULL " +
        ")");
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
    executeUpdate("CREATE INDEX IF NOT EXISTS CLADES_SUMMARIES_TIME_INDEX ON " + CLADE_SUMMARIES_TABLE + " (TIME)");
    executeUpdate("CREATE INDEX IF NOT EXISTS CLADE_SUMMARIES_CLADEID_INDEX ON " +
        CLADE_SUMMARIES_TABLE + " (CLADEID)");


        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('green', '{\"r\":0,\"g\":255,\"b\":0,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('forest', '{\"r\":0,\"g\":128,\"b\":0,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('spring', '{\"r\":0,\"g\":255,\"b\":128,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('summer', '{\"r\":128,\"g\":255,\"b\":64,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('lime', '{\"r\":176,\"g\":255,\"b\":0,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('leaf', '{\"r\":92,\"g\":184,\"b\":0,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('c4', '{\"r\":96,\"g\":192,\"b\":96,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('jade', '{\"r\":0,\"g\":168,\"b\":107,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('grass', '{\"r\":144,\"g\":176,\"b\":64,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('bark', '{\"r\":96,\"g\":128,\"b\":64,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('purple', '{\"r\":168,\"g\":0,\"b\":84,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('red', '{\"r\":255,\"g\":0,\"b\":0,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('fire', '{\"r\":255,\"g\":100,\"b\":0,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('orange', '{\"r\":255,\"g\":200,\"b\":0,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('maroon', '{\"r\":128,\"g\":0,\"b\":0,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('pink', '{\"r\":255,\"g\":175,\"b\":175,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('cream', '{\"r\":208,\"g\":192,\"b\":140,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('silver', '{\"r\":192,\"g\":192,\"b\":192,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('spike', '{\"r\":164,\"g\":132,\"b\":100,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('lilac', '{\"r\":192,\"g\":128,\"b\":255,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('gray', '{\"r\":128,\"g\":128,\"b\":128,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('violet', '{\"r\":128,\"g\":0,\"b\":128,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('olive', '{\"r\":176,\"g\":176,\"b\":0,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('sky', '{\"r\":128,\"g\":192,\"b\":255,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('blue', '{\"r\":0,\"g\":0,\"b\":255,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('ochre', '{\"r\":204,\"g\":119,\"b\":34,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('fallow', '{\"r\":150,\"g\":113,\"b\":23,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('spore', '{\"r\":0,\"g\":80,\"b\":160,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('white', '{\"r\":255,\"g\":255,\"b\":255,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('plague', '{\"r\":255,\"g\":192,\"b\":255,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('coral', '{\"r\":255,\"g\":100,\"b\":138,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('mint', '{\"r\":160,\"g\":224,\"b\":160,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('lavender', '{\"r\":128,\"g\":96,\"b\":176,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('magenta', '{\"r\":255,\"g\":0,\"b\":255,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('rose', '{\"r\":255,\"g\":0,\"b\":128,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('cyan', '{\"r\":0,\"g\":255,\"b\":255,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('teal', '{\"r\":0,\"g\":128,\"b\":128,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('yellow', '{\"r\":255,\"g\":255,\"b\":0,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('auburn', '{\"r\":128,\"g\":48,\"b\":48,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('indigo', '{\"r\":111,\"g\":0,\"b\":255,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('blond', '{\"r\":255,\"g\":255,\"b\":128,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('flower', '{\"r\":128,\"g\":128,\"b\":255,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('darkgray', '{\"r\":64,\"g\":64,\"b\":64,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('gold', '{\"r\":212,\"g\":175,\"b\":55,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('dark', '{\"r\":64,\"g\":32,\"b\":16,\"a\":255}')");
        executeUpdate("INSERT OR IGNORE INTO "+COLORS_TABLE+"(name, color) VALUES ('eye', '{\"r\":0,\"g\":64,\"b\":64,\"a\":255}')");
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

  public List<CladeDetails> getLongestSurvivors() throws SQLException {
    ResultSet rs = executeQuery(
        "select * from " + CLADES_TABLE + " order by LAST_SEEN_TIME - FIRST_SEEN_TIME desc limit 10");

    return readCladeSummaries(rs);
  }

  public List<TimeAndPopulation> getPopulationHistory(String cladeIdStr) throws SQLException {
    ResultSet rs = executeQuery("select TIME, POPULATION from " + CLADE_SUMMARIES_TABLE + " where CLADEID = '"
        + cladeIdStr + "' order by TIME");

    List<TimeAndPopulation> list = new ArrayList<>();

    while (rs.next()) {
      list.add(new TimeAndPopulation(rs.getInt("TIME"), rs.getInt("POPULATION")));
    }

    return list;
  }

  public List<CladeDetails> getMostPopulousClades(int time) throws SQLException {
    ResultSet rs = executeQuery(
        "select c.*, cs.TIME, cs.POPULATION " +
            " from " + CLADE_SUMMARIES_TABLE + " cs" +
            " join " + CLADES_TABLE + " c using (CLADEID)" +
            " where cs.TIME = " + time +
            " order by cs.POPULATION desc limit 10");

    return readFullCladeDetails(rs);
  }

  public List<CladeDetails> getCladeAncestors(String cladeIdStr) throws SQLException {
    CladeId cladeId = new CladeId(cladeIdStr);

    List<CladeDetails> ancestors = new ArrayList<>();

    while (cladeId != null) {
      ResultSet rs = executeQuery("select * from clades where CLADEID = '" + cladeId.getId() + "'");

      ancestors.addAll(readCladeSummaries(rs));

      cladeId = cladeId.parentId();
    }

    return ancestors;
  }

  public int getMaxTime() throws SQLException {
    ResultSet rs = executeQuery("select max(TIME) from " + CLADE_SUMMARIES_TABLE);
    return rs.getInt(1);
  }

  private List<CladeDetails> readCladeSummaries(ResultSet rs) throws SQLException {
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

  private List<CladeDetails> readFullCladeDetails(ResultSet rs) throws SQLException {
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

  private void executeUpdate(String sql) throws SQLException {
    // System.out.println(sql);
    connection.createStatement().executeUpdate(sql);
  }

  private ResultSet executeQuery(String sql) throws SQLException {
    // System.out.println(sql);
    return connection.createStatement().executeQuery(sql);
  }

  private String relativePath(File file) {
    return new File(".").getAbsoluteFile().getParentFile().toPath().relativize(file.getAbsoluteFile().toPath())
        .toString();
  }
}
