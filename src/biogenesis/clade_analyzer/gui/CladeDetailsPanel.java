package biogenesis.clade_analyzer.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;

import biogenesis.Clade;
import biogenesis.GeneticCode;
import biogenesis.clade_analyzer.CladeChartManager;
import biogenesis.clade_analyzer.CladeDetails;
import biogenesis.clade_analyzer.CladeNameGenerator;
import biogenesis.clade_analyzer.DB;
import biogenesis.clade_analyzer.TimeAndPopulation;

public class CladeDetailsPanel extends javax.swing.JPanel {
  private CladeDetails cladeSummary;
  private CladePopulationOverTime populationOverTimeChart;
  private List<ActionListener> actionListeners = new ArrayList<ActionListener>();

  public CladeDetailsPanel(Window owner, CladeChartManager cladeChartManager, CladeDetails cladeSummary, DB db, int maxTime) {
    this.cladeSummary = cladeSummary;

    initComponents(owner, cladeChartManager);

    if (db != null) {
      new Thread() {
        @Override
        public void run() {
          try {
            List<TimeAndPopulation> populationOverTime = db.getPopulationHistory(cladeSummary.getCladeId());
            java.awt.EventQueue.invokeLater(() -> populationOverTimeChart.setData(populationOverTime, maxTime));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }.start();
    }
  }

  public void initComponents(Window owner, CladeChartManager cladeChartManager) {
    if (!java.awt.EventQueue.isDispatchThread()) {
      throw new RuntimeException("Not in dispatch thread");
    }

    setMinimumSize(new Dimension(500, 200));
    setPreferredSize(new Dimension(500, 200));
    setBorder(BorderFactory.createLoweredBevelBorder());
    setLayout(new java.awt.GridBagLayout());

    // clade image
    CladeImageRenderer imagePanel = new CladeImageRenderer(cladeSummary.getGeneticCode());
    imagePanel.setMinimumSize(new Dimension(Clade.NET_CLADE_SIZE, Clade.NET_CLADE_SIZE));
    imagePanel.setPreferredSize(new Dimension(Clade.NET_CLADE_SIZE, Clade.NET_CLADE_SIZE));
    add(imagePanel, new java.awt.GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, java.awt.GridBagConstraints.NORTHWEST,
        java.awt.GridBagConstraints.NONE, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    // clade information
    JPanel infoPanel = new JPanel();
    infoPanel.setMinimumSize(new Dimension(200, Clade.NET_CLADE_SIZE));
    infoPanel.setPreferredSize(new Dimension(200, Clade.NET_CLADE_SIZE));
    infoPanel.setLayout(new java.awt.GridBagLayout());

    MultilineLabel labelClade = new MultilineLabel(cladeSummary.getCladeId());
    infoPanel.add(labelClade,
        new java.awt.GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    JPanel detailsPanel = new JPanel();
    detailsPanel.setBackground(null);
    detailsPanel.setLayout(new java.awt.GridBagLayout());
    MultilineLabel labelName = new MultilineLabel(
        "Name: " + CladeNameGenerator.generateName(cladeSummary.getGeneticCode()));
    detailsPanel.add(labelName,
        new java.awt.GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    detailsPanel.add(new javax.swing.JLabel("First seen: " + cladeSummary.getFirstSeenTime()),
        new java.awt.GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    detailsPanel.add(new javax.swing.JLabel("Last seen: " + cladeSummary.getLastSeenTime()),
        new java.awt.GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    detailsPanel.add(new javax.swing.JLabel("Max population: " + cladeSummary.getMaxPopulation()),
        new java.awt.GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    detailsPanel.add(
        new javax.swing.JLabel("Survived time: " + (cladeSummary.getLastSeenTime() - cladeSummary.getFirstSeenTime())),
        new java.awt.GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    if (cladeSummary.getTime() > 0 && cladeSummary.getPopulation() > 0) {
      detailsPanel.add(new javax.swing.JLabel("Population: " + cladeSummary.getPopulation()),
          new java.awt.GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, java.awt.GridBagConstraints.NORTHWEST,
              java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));
      detailsPanel.add(new javax.swing.JLabel("Time: " + cladeSummary.getTime()),
          new java.awt.GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, java.awt.GridBagConstraints.NORTHWEST,
              java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    }

    infoPanel.add(detailsPanel,
        new java.awt.GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    add(infoPanel, new java.awt.GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, java.awt.GridBagConstraints.NORTHWEST,
        java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    // Population over time chart
    populationOverTimeChart = new CladePopulationOverTime(owner, cladeChartManager);
    add(populationOverTimeChart,
        new java.awt.GridBagConstraints(2, 0, 1, 1, 1.0, 0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    java.awt.event.MouseAdapter mouseAdapter = new java.awt.event.MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        openDetails();
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        infoPanel.setBackground(java.awt.Color.LIGHT_GRAY);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        infoPanel.setBackground(UIManager.getColor("Label.background"));
      }
    };
    addMouseListener(mouseAdapter);
    labelClade.addMouseListener(mouseAdapter);
    labelName.addMouseListener(mouseAdapter);
    invalidate();
  }

  public void addActionListener(ActionListener actionListener) {
    actionListeners.add(actionListener);
  }

  private void openDetails() {
    // System.out.println("===> mouse clicked " + cladeSummary);
    actionListeners
        .forEach(actionListener -> actionListener.actionPerformed(new ActionEvent(cladeSummary, 0, "click")));
  }

  class CladeImageRenderer extends JPanel {
    private GeneticCode geneticCode;

    public CladeImageRenderer(GeneticCode geneticCode) {
      this.geneticCode = geneticCode;
    }

    @Override
    public void paint(java.awt.Graphics g) {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, Clade.NET_CLADE_SIZE, Clade.NET_CLADE_SIZE);
      geneticCode.draw(g, Clade.NET_CLADE_SIZE, Clade.NET_CLADE_SIZE);
    }
  }
}
