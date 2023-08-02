package biogenesis.clade_analyzer.gui;

import java.util.List;

import javax.swing.JPanel;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import biogenesis.clade_analyzer.TimeAndPopulation;

public class CladePopulationOverTime extends JPanel {
  public CladePopulationOverTime(List<TimeAndPopulation> timeAndPopulationList) {
    initComponents(timeAndPopulationList);
  }

  private void initComponents(List<TimeAndPopulation> timeAndPopulationList) {
    setLayout(new java.awt.GridLayout(1, 1));
    setMinimumSize(new java.awt.Dimension(200, 200));
    setPreferredSize(new java.awt.Dimension(200, 200));

    XYChart chart = new XYChartBuilder().width(200).height(200).title("Population over time").xAxisTitle("Time")
        .yAxisTitle("Population").build();
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setXAxisDecimalPattern("0");
    chart.getStyler().setYAxisDecimalPattern("0");
    chart.getStyler().setCursorEnabled(true);

    List<Integer> xData = timeAndPopulationList.stream().map(TimeAndPopulation::getTime).toList();
    List<Integer> yData = timeAndPopulationList.stream().map(TimeAndPopulation::getPopulation).toList();
    XYSeries series = chart.addSeries("PopulationOverTime", xData, yData);
    series.setMarker(SeriesMarkers.NONE);

    add(new XChartPanel<XYChart>(chart));
  }
}
