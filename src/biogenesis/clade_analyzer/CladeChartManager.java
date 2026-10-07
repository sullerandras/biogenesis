package biogenesis.clade_analyzer;

import java.util.HashSet;
import java.util.Set;

import biogenesis.clade_analyzer.gui.CladePopulationOverTime;

/**
 * This is used to keep track of all visible charts and synchronize their Y axis if needed.
 */
public class CladeChartManager {
  private boolean synchronizeYAxis = false;
  private Set<CladePopulationOverTime> charts = new HashSet<>();
  private int lastMaximumY = -1; // initial value is different from any possible value

  public CladeChartManager() {
  }

  public boolean isSynchronizeYAxis() {
    return synchronizeYAxis;
  }

  public void setSynchronizeYAxis(boolean synchronizeYAxis) {
    synchronized (this) {
      lastMaximumY = -1;
      this.synchronizeYAxis = synchronizeYAxis;
      synchronizeYAxis();
    }
  }

  public void addChart(CladePopulationOverTime chart) {
    synchronized (this) {
      lastMaximumY = -1;
      charts.add(chart);
    }
  }

  public void removeChart(CladePopulationOverTime chart) {
    synchronized (this) {
      lastMaximumY = -1;
      charts.remove(chart);
    }
  }

  public void synchronizeYAxis() {
    synchronized (this) {
      final int maximumY;
      if (isSynchronizeYAxis()) {
        maximumY = charts.stream().map(chart -> chart.getMaximumY()).max(Integer::compare).orElse(0);
      } else {
        maximumY = 0;
      }

      java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
          synchronized (CladeChartManager.this) {
            if (lastMaximumY == maximumY) {
              return;
            }
            charts.forEach(chart -> chart.updateWithMaximumY(maximumY));
            lastMaximumY = maximumY;
          }
        }
      });
    }
  }
}
