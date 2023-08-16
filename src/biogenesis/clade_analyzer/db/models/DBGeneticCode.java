package biogenesis.clade_analyzer.db.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import biogenesis.clade_analyzer.db.DB;

public class DBGeneticCode extends Base {
  public DBGeneticCode(DB db) {
    super(db);
  }

  public int insertAndReturnId(String geneticCode) throws SQLException {
    executeUpdate("INSERT OR IGNORE INTO genetic_codes (GENETIC_CODE) VALUES ('" + geneticCode + "')");
    return getGeneticCodeId(geneticCode);
  }

  public int getGeneticCodeId(String geneticCode) throws SQLException {
    ResultSet rs = executeQuery("select GENETIC_CODE_ID from genetic_codes where GENETIC_CODE = '" + geneticCode + "'");
    return rs.getInt(1);
  }
}
