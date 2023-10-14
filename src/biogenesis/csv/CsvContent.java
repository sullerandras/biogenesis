package biogenesis.csv;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CsvContent is a representation of the content of a csv file.
 * Not optimized for performance.
 */
public class CsvContent {
  // Using linked hash map to preserve order
  private final Map<String, List<String>> data = new LinkedHashMap<>();

  /**
   * Adds a value to the csv content. The value will be added to the column with the given name.
   * There are no safety checks to ensure that the value is of the correct type of that all columns have the same
   * number of values.
   */
  public void addValue(String columnName, String value) {
    data.putIfAbsent(columnName, new ArrayList<>());
    data.get(columnName).add(value);
  }

  /**
   * Returns the names of the columns in the csv file.
   */
  public List<String> getColumnNames() {
    return new ArrayList<>(data.keySet());
  }

  /**
   * Returns the values of the given column as doubles.
   */
  public List<Double> getColumnAsDoubles(String columnName) {
    return data.get(columnName).stream().mapToDouble(Double::parseDouble).boxed().collect(Collectors.toList());
  }

  /**
   * Returns the values of the given column as integers.
   */
  public List<Integer> getColumnAsIntegers(String columnName) {
    return data.get(columnName).stream().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
  }
}
