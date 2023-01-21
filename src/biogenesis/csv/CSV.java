package biogenesis.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CSV is an abstraction to handle CSV files. The only operation is to "append" to the csv file. If the .csv file
 * already exists then it will only print the columns that exist in the existing csv file to ensure that
 * the file remains readable. Because of this it is advisable to start a new world when we add more columns
 * to the biogenesis csv file.
 */
public class CSV {
  private final File file;
  private List<String> columnNames;

  public CSV(File file) {
    this.file = file;
  }

  public void append(Row row) {
    try {
      ensureColumnNames(row);

      FileWriter fw = null;
      try {
        fw = openFile();

        for (int i = 0; i < columnNames.size(); i++) {
          if (i > 0) {
            fw.write(",");
          }
          fw.write(row.getValue(columnNames.get(i), ""));
        }
        fw.write("\n");
      } finally {
        if (fw != null) {
          fw.close();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private FileWriter openFile() throws IOException {
    if (file.exists() && file.isFile()) {
      return new FileWriter(file, true);
    }

    FileWriter fw = new FileWriter(file);
    for (int i = 0; i < columnNames.size(); i++) {
      if (i > 0) {
        fw.write(',');
      }
      fw.write(columnNames.get(i));
    }
    fw.write('\n');

    return fw;
  }

  private void ensureColumnNames(Row row) throws IOException {
    if (columnNames != null) {
      return;
    }

    // if csv file exists then load the first line to get the column names
    if (file.exists() && file.isFile()) {
      BufferedReader br = new BufferedReader(new FileReader(file));
      try {
        String headerLine = br.readLine();
        columnNames = Arrays.asList(headerLine.split(","));
      } finally {
        br.close();
      }
    } else { // if file does not exists then take column names from `row`.
      columnNames = row.getColumnNames();
    }
  }
}
