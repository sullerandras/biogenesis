package biogenesis.clade_analyzer.gui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import biogenesis.clade_analyzer.db.DB;
import biogenesis.clade_analyzer.gui.downsamplers.Downsampler;
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
  private DB db;
  private javax.swing.JPanel chartsPanel;
  private javax.swing.JComboBox<Integer> maxPointsComboBox;
  private javax.swing.JComboBox<Downsampler> downsamplerComboBox;

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
    this.db = db;
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        refreshCharts(
            db,
            (Downsampler) downsamplerComboBox.getSelectedItem(),
            (Integer) maxPointsComboBox.getSelectedItem());
      }
    });
  }

  private void initComponents() {
    if (!java.awt.EventQueue.isDispatchThread()) {
      throw new RuntimeException("Not in dispatch thread");
    }

    setLayout(new java.awt.BorderLayout());

    javax.swing.JPanel toolbarPanel = new javax.swing.JPanel();
    toolbarPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

    // downsampler algorithm combo box
    downsamplerComboBox = new javax.swing.JComboBox<>();
    for (Downsampler downsampler : Downsampler.downsamplers) {
      downsamplerComboBox.addItem(downsampler);
    }
    downsamplerComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        setDB(db);
      }
    });
    toolbarPanel.add(new javax.swing.JLabel("Downsampler:"));
    toolbarPanel.add(downsamplerComboBox);

    // max points combo box
    maxPointsComboBox = new javax.swing.JComboBox<>();
    maxPointsComboBox.addItem(100);
    maxPointsComboBox.addItem(500);
    maxPointsComboBox.addItem(1000);
    maxPointsComboBox.addItem(5000);
    maxPointsComboBox.setSelectedIndex(1);
    maxPointsComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        setDB(db);
      }
    });
    toolbarPanel.add(new javax.swing.JLabel("Max points:"));
    toolbarPanel.add(maxPointsComboBox);

    add(toolbarPanel, java.awt.BorderLayout.NORTH);

    chartsPanel = new javax.swing.JPanel();
    add(new BetterJScrollPane(chartsPanel), java.awt.BorderLayout.CENTER);
  }

  private void refreshCharts(DB db, Downsampler downsampler, int maxPoints) {
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

        List<Double> values = csvContent.getColumnAsDoubles(columnName);
        List<Double> downsampledValues = new java.util.ArrayList<>();
        List<Integer> downsampledValueLabels = new java.util.ArrayList<>();
        downsampler.downsample(values, valueLabels, maxPoints, downsampledValues, downsampledValueLabels);
        chartsPanel.add(new GenericChart(mainFrame, columnName, "Time", columnName,
            downsampledValues, downsampledValueLabels));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
