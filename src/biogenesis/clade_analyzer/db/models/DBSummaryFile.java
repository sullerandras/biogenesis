package biogenesis.clade_analyzer.db.models;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import biogenesis.clade_analyzer.db.DB;

public class DBSummaryFile extends Base {
  public DBSummaryFile(DB db) {
    super(db);
  }

  public boolean isSummaryFileDone(File summaryFile) throws SQLException {
    String state = executeQueryString(
        "SELECT state " +
            " FROM summary_files " +
            " WHERE FILENAME = '" + relativePath(summaryFile) + "'",
        rs -> rs.getString("state"));
    return state != null && state.equals("done");
  }

  public int upsertSummaryFileToPending(File summaryFile, int time) throws SQLException {
    executeUpdate("INSERT OR REPLACE INTO summary_files (FILENAME, TIME, STATE) " +
        "VALUES ('" + relativePath(summaryFile) + "', " + time + ", 'pending')");

    return getSummaryFileId(summaryFile);
  }

  public int getSummaryFileId(File summaryFile) throws SQLException {
    return executeQueryInteger(
        "select SUMMARY_FILE_ID from summary_files where FILENAME = '" +
            relativePath(summaryFile) + "'",
        rs -> rs.getInt(1));
  }

  public void markSummaryFileDone(int summaryFileId) throws SQLException {
    executeUpdate("UPDATE summary_files SET state = '" + "done" + "'" +
        " WHERE SUMMARY_FILE_ID = " + summaryFileId);
  }

  public int getMaxTime() throws SQLException {
    return executeQueryInteger("select max(TIME) from summary_files", rs -> rs.getInt(1));
  }

  public List<Integer> getListOfTimesSync() throws SQLException {
    return executeQueryListOfIntegers("select TIME from summary_files order by TIME",
        rs -> readListOfIntegers(rs));
  }

  public static String relativePath(File file) {
    try {
      return new File(".").getAbsoluteFile().getParentFile().toPath().relativize(file.getAbsoluteFile().toPath())
          .toString();
    } catch (IllegalArgumentException e) {
      return file.getAbsolutePath();
    }
  }
}
