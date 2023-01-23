package biogenesis.csv;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Row represents one row of data in the CSV file.
 */
public class Row {
  // We use LinkedHashMap to ensure the column order.
  private Map<String, String> values = new LinkedHashMap<>();

  public void add(String columnName, long value) {
    values.put(columnName, Long.toString(value, 10));
  }

  public void add(String columnName, String value) {
    values.put(columnName, value);
  }

  public void add(String columnName, double value, int decimalPlaces) {
    values.put(columnName, String.format("%." + decimalPlaces + "f", value));
  }

  public int size() {
    return values.size();
  }

  public List<String> getColumnNames() {
    return values.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());
  }

  public String getValue(String columnName, String defaultValue) {
    return values.getOrDefault(columnName, defaultValue);
  }
}
