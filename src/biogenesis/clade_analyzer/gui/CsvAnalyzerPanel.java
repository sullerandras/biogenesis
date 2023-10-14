package biogenesis.clade_analyzer.gui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import biogenesis.clade_analyzer.db.DB;
import biogenesis.csv.CSV;
import biogenesis.csv.CsvContent;
import biogenesis.gui.stats.BetterJScrollPane;

/**
 * Panel that shows charts for the CSV file.
 * Each column in the CSV file will be shown as a chart.
 * It's quite slow to render the charts because there are so many data points and so many charts.
 */
public class CsvAnalyzerPanel extends javax.swing.JPanel {
  private final MainFrame mainFrame;
  private javax.swing.JPanel chartsPanel;

  /**
   * Creates a new CsvAnalyzerPanel that will show charts the DB's CSV file.
   * The panel will be empty if there is no CSV file.
   * @param mainFrame
   */
  public CsvAnalyzerPanel(MainFrame mainFrame) {
    this.mainFrame = mainFrame;
    initComponents();
  }

  /**
   * Sets the DB to use to get the CSV file.
   * It is called when `db` changes, i.e. the user opened a different db, or the db has new rows inserted.
   * @param db
   */
  public void setDB(DB db) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        refreshCharts(db);
      }
    });
  }

  private void initComponents() {
    if (!java.awt.EventQueue.isDispatchThread()) {
      throw new RuntimeException("Not in dispatch thread");
    }

    setLayout(new java.awt.BorderLayout());

    javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
    jLabel1.setText("CSV Analyzer");
    add(jLabel1, java.awt.BorderLayout.NORTH);

    chartsPanel = new javax.swing.JPanel();
    add(new BetterJScrollPane(chartsPanel), java.awt.BorderLayout.CENTER);
  }

  private void refreshCharts(DB db) {
    if (!java.awt.EventQueue.isDispatchThread()) {
      throw new RuntimeException("Not in dispatch thread");
    }

    chartsPanel.removeAll();

    File csvFile = db.getDbFile().getCsvFile();
    if (csvFile == null || !csvFile.isFile() || !csvFile.exists()) {
      return;
    }

    try {
      CsvContent csvContent = new CSV(csvFile).read();
      chartsPanel.setLayout(new java.awt.GridLayout(csvContent.getColumnNames().size() - 1, 1));

      List<Integer> valueLabels = csvContent.getColumnAsIntegers("time");
      // create a chart for each column in the csv
      for (String columnName : csvContent.getColumnNames()) {
        if (columnName.equals("time")) {
          continue;
        }

        chartsPanel.add(new GenericChart(mainFrame, columnName, "Time", columnName,
            csvContent.getColumnAsDoubles(columnName), valueLabels));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
