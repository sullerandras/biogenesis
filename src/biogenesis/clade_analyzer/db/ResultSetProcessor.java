package biogenesis.clade_analyzer.db;

public interface ResultSetProcessor<T> {
  public T processResultSet(java.sql.ResultSet rs) throws java.sql.SQLException;
}
