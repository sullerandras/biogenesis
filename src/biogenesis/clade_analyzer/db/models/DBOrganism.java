package biogenesis.clade_analyzer.db.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import biogenesis.clade_analyzer.CladeDetails;
import biogenesis.clade_analyzer.db.DB;

public class DBOrganism extends Base {
  public DBOrganism(DB db) {
    super(db);
  }

  public void insertOrganism(int cladePopulationId, int x, int y) throws SQLException {
    executeUpdate(
        "INSERT INTO organisms (clade_population_id, x, y) VALUES (" + cladePopulationId + ", " + x + ", " + y + ")");
  }

  public List<CladeDetails> getOrganismsAtTimeSync(int time) throws SQLException {
    ResultSet rs = executeQuery(
        "select c.CLADEID, c.FIRST_SEEN_TIME, c.LAST_SEEN_TIME, gc.GENETIC_CODE, c.MAX_POPULATION," +
            " sf.TIME, cp.POPULATION, o.X, o.Y " +
            " from summary_files sf" +
            " join clade_populations cp using (SUMMARY_FILE_ID)" +
            " join clades c using (CLADE_ID)" +
            " join genetic_codes gc on (gc.GENETIC_CODE_ID = c.GENETIC_CODE_ID)" +
            " join organisms o using (CLADE_POPULATION_ID) " +
            " where sf.TIME = " + time);

    return readCladeDetailsWithTimeAndPopulationAndCoordinates(rs);
  }
}
