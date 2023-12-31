package biogenesis.clade_analyzer.db.models;

import java.sql.SQLException;
import java.util.List;

import biogenesis.clade_analyzer.CladeDetails;
import biogenesis.clade_analyzer.TimeAndPopulation;
import biogenesis.clade_analyzer.db.DB;

public class DBCladePopulation extends Base {
  public DBCladePopulation(DB db) {
    super(db);
  }

  public void deleteRecordsForSummaryFile(int summaryFileId) throws SQLException {
    executeUpdate("DELETE FROM clade_populations WHERE SUMMARY_FILE_ID = " + summaryFileId);
  }

  public int insertAndReturnId(int cladeId, int summaryFileId, int geneticCodeId, int population) throws SQLException {
    executeUpdate("INSERT INTO clade_populations " +
        " (CLADE_ID, SUMMARY_FILE_ID, GENETIC_CODE_ID, POPULATION) " +
        "VALUES (" + cladeId + ", " + summaryFileId + ", " + geneticCodeId + ", " + population + ")");

    return executeQueryInteger("select max(CLADE_POPULATION_ID) from clade_populations",
        rs -> rs.getInt(1));
  }

  public List<TimeAndPopulation> getPopulationHistorySync(String cladeId) throws SQLException {
    return executeQueryListOfTimeAndPopulation(
        "select TIME, POPULATION" +
            " from clade_populations cp" +
            " join summary_files sf using (SUMMARY_FILE_ID)" +
            " where CLADE_ID = " + new DBClade(db).getCladeId(cladeId) + " order by TIME",
        rs -> readTimeAndPopulations(rs));
  }

  public List<CladeDetails> getMostPopulousCladesSync(int time, int limit) throws SQLException {
    return executeQueryListOfCladeDetails(
        "select c.CLADEID, c.FIRST_SEEN_TIME, c.LAST_SEEN_TIME, gc.GENETIC_CODE, c.MAX_POPULATION, sf.TIME, cp.POPULATION "
            +
            " from summary_files sf" +
            " join clade_populations cp using (SUMMARY_FILE_ID)" +
            " join clades c using (CLADE_ID)" +
            " join genetic_codes gc on (gc.GENETIC_CODE_ID = c.GENETIC_CODE_ID)" +
            " where sf.TIME = " + time +
            " order by cp.POPULATION desc" +
            (limit >= 0 ? " limit " + limit : ""),
        rs -> readCladeDetailsWithTimeAndPopulation(rs));
  }
}
