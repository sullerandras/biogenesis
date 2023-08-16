package biogenesis.clade_analyzer.db.models;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import biogenesis.clade_analyzer.db.DB;

public class DBSummaryFile extends Base {
  public DBSummaryFile(DB db) {
    super(db);
  }

  public boolean isSummaryFileDone(File summaryFile) throws SQLException {
    String state = executeQuery(
        "SELECT state " +
            " FROM summary_files " +
            " WHERE FILENAME = '" + relativePath(summaryFile) + "'")
        .getString("state");
    return state != null && state.equals("done");
  }

  public int upsertSummaryFileToPending(File summaryFile, int time) throws SQLException {
    executeUpdate("INSERT OR REPLACE INTO summary_files (FILENAME, TIME, STATE) " +
        "VALUES ('" + relativePath(summaryFile) + "', " + time + ", 'pending')");

    return getSummaryFileId(summaryFile);
  }

  public int getSummaryFileId(File summaryFile) throws SQLException {
    ResultSet rs = executeQuery("select SUMMARY_FILE_ID from summary_files where FILENAME = '" +
        relativePath(summaryFile) + "'");
    return rs.getInt(1);
  }

  public void markSummaryFileDone(int summaryFileId) throws SQLException {
    executeUpdate("UPDATE summary_files SET state = '" + "done" + "'" +
        " WHERE SUMMARY_FILE_ID = " + summaryFileId);
  }

  public int getMaxTime() throws SQLException {
    ResultSet rs = executeQuery("select max(TIME) from summary_files");
    return rs.getInt(1);
  }

  private String relativePath(File file) {
    return new File(".").getAbsoluteFile().getParentFile().toPath().relativize(file.getAbsoluteFile().toPath())
        .toString();
  }
}
