package biogenesis.clade_analyzer.db.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biogenesis.CladeId;
import biogenesis.clade_analyzer.CladeDetails;
import biogenesis.clade_analyzer.db.DB;

public class DBClade extends Base {
  public DBClade(DB db) {
    super(db);
  }

  public int insertAndReturnId(String cladeId, int geneticCodeId, int firstSeenTime, int lastSeenTime, int population)
      throws SQLException {
    executeUpdate("INSERT OR IGNORE INTO clades" +
        " (CLADEID, GENETIC_CODE_ID, FIRST_SEEN_TIME, LAST_SEEN_TIME, MAX_POPULATION) " +
        "VALUES ('" + cladeId + "', " + geneticCodeId + ", " + firstSeenTime + ", " + lastSeenTime + ", " + population
        + ")");

    return getCladeId(cladeId);
  }

  public void updateLastSeenTimeAndGeneticCodeId(int cladeId, int lastSeenTime, int geneticCodeId) throws SQLException {
    executeUpdate("UPDATE clades SET " +
        "LAST_SEEN_TIME = " + lastSeenTime + ", " +
        "GENETIC_CODE_ID = '" + geneticCodeId + "' " +
        "WHERE CLADE_ID = " + cladeId);
  }

  public void updateMaxPopulation(int cladeId, int maxPopulation) throws SQLException {
    executeUpdate("UPDATE clades SET " +
        "MAX_POPULATION = " + maxPopulation + " " +
        "WHERE CLADE_ID = " + cladeId + " AND MAX_POPULATION < " + maxPopulation);
  }

  public int getCladeId(String cladeId) throws SQLException {
    ResultSet rs = executeQuery("select CLADE_ID from clades where CLADEID = '" + cladeId + "'");
    return rs.getInt(1);
  }

  public List<CladeDetails> getLongestSurvivorsSync(int limit) throws SQLException {
    ResultSet rs = executeQuery(
        "select c.CLADEID, c.FIRST_SEEN_TIME, c.LAST_SEEN_TIME, gc.GENETIC_CODE, c.MAX_POPULATION"+
        " from clades c"+
        " join genetic_codes gc using (GENETIC_CODE_ID)"+
        " order by LAST_SEEN_TIME - FIRST_SEEN_TIME desc"
            + (limit >= 0 ? " limit " + limit : ""));

    return readCladeSummaries(rs);
  }

  public List<CladeDetails> getCladeAncestors(String cladeIdStr) throws SQLException {
    CladeId cladeId = new CladeId(cladeIdStr);

    List<CladeDetails> ancestors = new ArrayList<>();

    while (cladeId != null) {
      ResultSet rs = executeQuery(
        "select c.CLADEID, c.FIRST_SEEN_TIME, c.LAST_SEEN_TIME, gc.GENETIC_CODE, c.MAX_POPULATION"+
        " from clades c"+
        " join genetic_codes gc using (GENETIC_CODE_ID)"+
        " where CLADEID = '" + cladeId.getId() + "'");

      ancestors.addAll(readCladeSummaries(rs));

      cladeId = cladeId.parentId();
    }

    return ancestors;
  }
}
