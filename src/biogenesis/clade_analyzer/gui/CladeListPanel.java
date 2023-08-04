package biogenesis.clade_analyzer.gui;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import biogenesis.clade_analyzer.CladeSummary;
import biogenesis.clade_analyzer.DB;

public class CladeListPanel extends javax.swing.JPanel {
  private List<ActionListener> actionListeners = new ArrayList<ActionListener>();

  public CladeListPanel() {
    setCladeList(new java.util.ArrayList<CladeSummary>(), null, 0);
  }

  public void setCladeList(java.util.List<CladeSummary> cladeList, DB db, int maxTime) {
    removeAll();
    initComponents(cladeList, db, maxTime);
  }

  private void initComponents(java.util.List<CladeSummary> cladeList, DB db, int maxTime) {
    setLayout(new java.awt.GridLayout(1, 1));

    JPanel panel = new JPanel();
    JScrollPane jScrollPane = new JScrollPane(panel);
    jScrollPane.getHorizontalScrollBar().setUnitIncrement(20);
    jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
    add(jScrollPane);
    panel.setLayout(new java.awt.GridLayout(cladeList.size(), 1));

    for (CladeSummary cladeSummary : cladeList) {
      // System.out.println(
      //     cladeSummary.getCladeId() + " " + cladeSummary.getFirstSeenTime() + " " + cladeSummary.getLastSeenTime()
      //         + " " + cladeSummary.getGeneticCode() + " " + cladeSummary.getMaxPopulation());
      CladePanel cladePanel = new CladePanel(cladeSummary, db, maxTime);
      panel.add(cladePanel);
      cladePanel.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          for (ActionListener actionListener : actionListeners) {
            actionListener.actionPerformed(evt);
          }
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
