package biogenesis.clade_analyzer.gui;

import java.awt.Frame;
import java.util.List;

import biogenesis.WindowManager;
import biogenesis.clade_analyzer.CladeSummary;
import biogenesis.clade_analyzer.DB;

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
          List<CladeSummary> ancestors = db.getCladeAncestors(cladeId);
          java.awt.EventQueue.invokeLater(() -> cladeListPanel.setCladeList(ancestors, db, maxTime));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  private void initComponents(DB db) {
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
    cladeListPanel = new CladeListPanel();
    mainPanel.add(cladeListPanel,
        new java.awt.GridBagConstraints(0, 1, 1, 1, 1, 1, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    invalidate();
  }
}
