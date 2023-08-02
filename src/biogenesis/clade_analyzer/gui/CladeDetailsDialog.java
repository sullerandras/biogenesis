package biogenesis.clade_analyzer.gui;

import java.awt.Frame;
import java.util.List;

import biogenesis.WindowManager;
import biogenesis.clade_analyzer.CladeSummary;
import biogenesis.clade_analyzer.TimeAndPopulation;

/**
 * Shows details about the selected clade:
 * - Ancestor clades
 * - Chart about the population of the clade over time
 */
public class CladeDetailsDialog extends javax.swing.JDialog {
  public CladeDetailsDialog(Frame parent, List<CladeSummary> ancestors, List<TimeAndPopulation> populationOverTime) {
    super(parent, false);

    WindowManager.registerWindow(this, 800, 600, 0, 0);

    initComponents(ancestors, populationOverTime);
  }

  private void initComponents(List<CladeSummary> ancestors, List<TimeAndPopulation> populationOverTime) {
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Clade Ancestors");
    setMinimumSize(new java.awt.Dimension(800, 600));
    getContentPane().setLayout(new java.awt.GridBagLayout());

    // main panel
    javax.swing.JPanel mainPanel = new javax.swing.JPanel();
    mainPanel.setLayout(new java.awt.GridBagLayout());
    getContentPane().add(mainPanel,
        new java.awt.GridBagConstraints(0, 0, 1, 1, 1, 1, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    // Population over time chart
    CladePopulationOverTime populationOverTimeChart = new CladePopulationOverTime(populationOverTime);
    mainPanel.add(populationOverTimeChart,
        new java.awt.GridBagConstraints(0, 0, 1, 1, 1, 0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    // ancestor clades
    CladeListPanel cladeListPanel = new CladeListPanel();
    mainPanel.add(cladeListPanel,
        new java.awt.GridBagConstraints(0, 1, 1, 1, 1, 1, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    cladeListPanel.setCladeList(ancestors);

    invalidate();
  }
}
