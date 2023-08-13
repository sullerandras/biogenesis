package biogenesis.clade_analyzer.gui;

import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import biogenesis.clade_analyzer.CladeChartManager;
import biogenesis.clade_analyzer.CladeDetails;
import biogenesis.clade_analyzer.DB;

public class CladeListPanel extends javax.swing.JPanel {
  private final List<ActionListener> actionListeners = new ArrayList<ActionListener>();
  private final Window owner;
  private final CladeChartManager cladeChartManager;

  public CladeListPanel(Window owner, CladeChartManager cladeChartManager) {
    this.owner = owner;
    this.cladeChartManager = cladeChartManager;

    setCladeList(new java.util.ArrayList<CladeDetails>(), null, 0);
  }

  public CladeChartManager getCladeChartManager() {
    return cladeChartManager;
  }

  public void setCladeList(java.util.List<CladeDetails> cladeList, DB db, int maxTime) {
    removeAll();
    initComponents(cladeList, db, maxTime);
  }

  private void initComponents(java.util.List<CladeDetails> cladeList, DB db, int maxTime) {
    if (!java.awt.EventQueue.isDispatchThread()) {
      throw new RuntimeException("Not in dispatch thread");
    }

    setLayout(new java.awt.GridBagLayout());

    // toolbar
    javax.swing.JToolBar toolbar = new javax.swing.JToolBar();
    toolbar.setFloatable(false);
    toolbar.setRollover(true);

    JCheckBox synchronizeYAxisCheckBox = new JCheckBox("Synchronize Y axis");
    synchronizeYAxisCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cladeChartManager.setSynchronizeYAxis(synchronizeYAxisCheckBox.isSelected());
      }
    });
    toolbar.add(synchronizeYAxisCheckBox);
    add(toolbar,
        new java.awt.GridBagConstraints(0, 0, 1, 1, 1, 0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    // list of clades
    JPanel panel = new JPanel();
    JScrollPane jScrollPane = new JScrollPane(panel);
    jScrollPane.getHorizontalScrollBar().setUnitIncrement(20);
    jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
    add(jScrollPane,
        new java.awt.GridBagConstraints(0, 1, 1, 1, 1, 1, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    panel.setLayout(new java.awt.GridLayout(cladeList.size(), 1));

    for (CladeDetails cladeSummary : cladeList) {
      // System.out.println(
      //     cladeSummary.getCladeId() + " " + cladeSummary.getFirstSeenTime() + " " + cladeSummary.getLastSeenTime()
      //         + " " + cladeSummary.getGeneticCode() + " " + cladeSummary.getMaxPopulation());
      CladeDetailsPanel cladePanel = new CladeDetailsPanel(owner, cladeChartManager, cladeSummary, db, maxTime);
      panel.add(cladePanel);
      cladePanel.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          actionListeners.forEach(l -> l.actionPerformed(evt));
        }
      });
    }

    revalidate();

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jScrollPane.getHorizontalScrollBar().setValue(0);
        jScrollPane.getVerticalScrollBar().setValue(0);
      }
    });
  }

  public void addActionListener(ActionListener actionListener) {
    actionListeners.add(actionListener);
  }
}
