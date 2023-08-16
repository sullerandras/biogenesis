package biogenesis.clade_analyzer.db.models;

import java.sql.SQLException;

import biogenesis.clade_analyzer.db.DB;

public class DBOrganism extends Base {
  public DBOrganism(DB db) {
    super(db);
  }

  public void insertOrganism(int cladePopulationId, int x, int y) throws SQLException {
    executeUpdate(
        "INSERT INTO organisms (clade_population_id, x, y) VALUES (" + cladePopulationId + ", " + x + ", " + y + ")");
  }
}
