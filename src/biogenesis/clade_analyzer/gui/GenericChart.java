package biogenesis.clade_analyzer.gui;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Generic chart that can be used to display any data, as long as the data
 * is a list of doubles and the labels are a list of integers.
 */
public class GenericChart extends JPanel {
  private final String chartTitle;
  private final String xAxisTitle;
  private final String yAxisTitle;
  private double maximumY = 0;

  /**
   * Creates a new GenericChart. You will need to call setValues() to set the values and labels.
   * @param owner The owner of the chart. Can be used for optimizations to destroy the chart when the owner is destroyed.
   * @param chartTitle The title of the chart, displayed above the chart.
   * @param xAxisTitle The title of the x (horizontal) axis.
   * @param yAxisTitle The title of the y (vertical) axis.
   */
  public GenericChart(Window owner, String chartTitle, String xAxisTitle, String yAxisTitle) {
    this(owner, chartTitle, xAxisTitle, yAxisTitle, new ArrayList<>(), new ArrayList<>());
  }

  /**
   * Creates a new GenericChart.
   * @param owner The owner of the chart. Can be used for optimizations to destroy the chart when the owner is destroyed.
   * @param chartTitle The title of the chart, displayed above the chart.
   * @param xAxisTitle The title of the x (horizontal) axis.
   * @param yAxisTitle The title of the y (vertical) axis.
   * @param values The values to display in the chart.
   * @param valueLabels The labels for the values. The labels are displayed in the x axis.
   */
  public GenericChart(Window owner, String chartTitle, String xAxisTitle, String yAxisTitle, List<Double> values,
      List<Integer> valueLabels) {
    this.chartTitle = chartTitle;
    this.xAxisTitle = xAxisTitle;
    this.yAxisTitle = yAxisTitle;
    setValues(values, valueLabels);
  }

  /**
   * Sets the values to display in the chart.
   * @param values The values to display in the chart.
   * @param valueLabels The labels for the values. The labels are displayed in the x axis.
   */
  public void setValues(List<Double> values, List<Integer> valueLabels) {
    this.maximumY = values.stream().mapToDouble(Double::doubleValue).max().orElse(0);

    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        initComponents(values, valueLabels, maximumY);
      }
    });
  }

  private void initComponents(List<Double> values, List<Integer> valueLabels, double maximumY) {
    if (!java.awt.EventQueue.isDispatchThread()) {
      throw new RuntimeException("Not in dispatch thread");
    }

    removeAll();
    setLayout(new java.awt.GridLayout(1, 1));
    setMinimumSize(new java.awt.Dimension(200, 200));
    setPreferredSize(new java.awt.Dimension(200, 200));

    XYChart chart = new XYChartBuilder().width(200).height(200).title(chartTitle).xAxisTitle(xAxisTitle)
        .yAxisTitle(yAxisTitle).build();
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setXAxisDecimalPattern("###,###,###");
    chart.getStyler().setYAxisDecimalPattern("###,###,###,###.#");
    chart.getStyler().setCursorEnabled(true);
    chart.getStyler().setXAxisTitleVisible(false);

    List<Integer> xData = new ArrayList<>(valueLabels);
    List<Double> yData = new ArrayList<>(values);

    XYSeries series = chart.addSeries(yAxisTitle, xData, yData);
    series.setMarker(SeriesMarkers.NONE);

    add(new XChartPanel<XYChart>(chart));

    revalidate();
  }
}
