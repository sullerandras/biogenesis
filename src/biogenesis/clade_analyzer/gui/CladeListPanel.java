package biogenesis.clade_analyzer.gui;

import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import biogenesis.clade_analyzer.CladeChartManager;
import biogenesis.clade_analyzer.CladeDetails;
import biogenesis.clade_analyzer.DB;

public class CladeListPanel extends javax.swing.JPanel {
  private final List<ActionListener> clickCladeListeners = new ArrayList<ActionListener>();
  private final List<ActionListener> limitChangeListeners = new ArrayList<ActionListener>();
  private final Window owner;
  private final CladeChartManager cladeChartManager;

  private JComboBox<Limit> limitsComboBox;
  private JScrollPane jScrollPane;
  private JPanel cladeListPanel;

  public CladeListPanel(Window owner, CladeChartManager cladeChartManager) {
    this.owner = owner;
    this.cladeChartManager = cladeChartManager;

    initComponents(new java.util.ArrayList<CladeDetails>(), null, 0);
  }

  public CladeChartManager getCladeChartManager() {
    return cladeChartManager;
  }

  public void setCladeList(java.util.List<CladeDetails> cladeList, DB db, int maxTime) {
    refreshCladeListPanel(cladeList, db, maxTime);
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

    toolbar.addSeparator();

    toolbar.add(new javax.swing.JLabel("Limit: "));
    limitsComboBox = new JComboBox<Limit>(Limit.values());
    limitsComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        // disable synchronization when changing limit since the charts will not be syncronized anymore
        synchronizeYAxisCheckBox.setSelected(false);

        limitChangeListeners.forEach(l -> l.actionPerformed(evt));
      }
    });
    toolbar.add(limitsComboBox);

    add(toolbar,
        new java.awt.GridBagConstraints(0, 0, 1, 1, 1, 0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    // list of clades
    cladeListPanel = new JPanel();
    jScrollPane = new JScrollPane(cladeListPanel);
    jScrollPane.getHorizontalScrollBar().setUnitIncrement(20);
    jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
    jScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
    add(jScrollPane,
        new java.awt.GridBagConstraints(0, 1, 1, 1, 1, 1, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    cladeListPanel.setLayout(new java.awt.GridLayout(cladeList.size(), 1));

    refreshCladeListPanel(cladeList, db, maxTime);
  }

  private void refreshCladeListPanel(java.util.List<CladeDetails> cladeList, DB db, int maxTime) {
    if (!java.awt.EventQueue.isDispatchThread()) {
      throw new RuntimeException("Not in dispatch thread");
    }

    if (cladeListPanel == null) {
      return;
    }

    cladeListPanel.removeAll();

    for (CladeDetails cladeSummary : cladeList) {
      CladeDetailsPanel cladePanel = new CladeDetailsPanel(owner, cladeChartManager, cladeSummary, db, maxTime);
      cladeListPanel.add(cladePanel);
      cladePanel.addClickCladeListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          clickCladeListeners.forEach(l -> l.actionPerformed(evt));
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

  public void addClickCladeListener(ActionListener l) {
    clickCladeListeners.add(l);
  }

  public void addLimitChangeListener(ActionListener l) {
    limitChangeListeners.add(l);
  }

  public int getLimit() {
    return ((Limit) limitsComboBox.getSelectedItem()).getLimit();
  }
}
