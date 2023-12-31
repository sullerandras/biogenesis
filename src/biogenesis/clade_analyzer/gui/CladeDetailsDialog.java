package biogenesis.clade_analyzer.gui;

import java.awt.Frame;
import java.util.List;

import biogenesis.WindowManager;
import biogenesis.clade_analyzer.CladeChartManager;
import biogenesis.clade_analyzer.CladeDetails;
import biogenesis.clade_analyzer.db.DB;

/**
 * Shows details about the selected clade:
 * - Ancestor clades
 * - Chart about the population of the clade over time
 */
public class CladeDetailsDialog extends javax.swing.JDialog {
  private CladeListPanel cladeListPanel;

  public CladeDetailsDialog(Frame parent, DB db, String cladeId, int maxTime) {
    super(parent, false);

    WindowManager.registerWindow(this, 800, 600, 0, 0);

    initComponents(db);

    new Thread() {
      @Override
      public void run() {
        try {
          List<CladeDetails> ancestors = db.getCladeAncestors(cladeId);
          int cladesCount = ancestors.size();
          java.awt.EventQueue.invokeLater(() -> cladeListPanel.setCladeList(ancestors, db, maxTime, cladesCount));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  private void initComponents(DB db) {
    if (!java.awt.EventQueue.isDispatchThread()) {
      throw new RuntimeException("Not in dispatch thread");
    }

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

    // ancestor clades
    cladeListPanel = new CladeListPanel(this, new CladeChartManager(), false);
    mainPanel.add(cladeListPanel,
        new java.awt.GridBagConstraints(0, 1, 1, 1, 1, 1, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    invalidate();
  }
}
