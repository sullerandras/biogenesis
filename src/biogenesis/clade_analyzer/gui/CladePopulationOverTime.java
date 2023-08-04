package biogenesis.clade_analyzer.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import biogenesis.clade_analyzer.TimeAndPopulation;

public class CladePopulationOverTime extends JPanel {
  public CladePopulationOverTime() {
    setData(new ArrayList<>(), 0);
  }

  public void setData(List<TimeAndPopulation> timeAndPopulationList, int maxTime) {
    removeAll();
    initComponents(timeAndPopulationList, maxTime);
  }

  private void initComponents(List<TimeAndPopulation> timeAndPopulationList, int maxTime) {
    setLayout(new java.awt.GridLayout(1, 1));
    setMinimumSize(new java.awt.Dimension(200, 200));
    setPreferredSize(new java.awt.Dimension(200, 200));

    XYChart chart = new XYChartBuilder().width(200).height(200).title("Population over time").xAxisTitle("Time")
        .yAxisTitle("Population").build();
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setXAxisDecimalPattern("0");
    chart.getStyler().setYAxisDecimalPattern("0");
    chart.getStyler().setCursorEnabled(true);
    chart.getStyler().setXAxisTitleVisible(false);

    List<Integer> xData = timeAndPopulationList.stream().map(TimeAndPopulation::getTime).toList();
    List<Integer> yData = timeAndPopulationList.stream().map(TimeAndPopulation::getPopulation).toList();

    xData = new ArrayList<>(xData);
    xData.add(0, 0);
    xData.add(maxTime);
    yData = new ArrayList<>(yData);
    yData.add(0, null);
    yData.add(null);

    XYSeries series = chart.addSeries("Population", xData, yData);
    series.setMarker(SeriesMarkers.NONE);

    add(new XChartPanel<XYChart>(chart));

    revalidate();
  }
}
