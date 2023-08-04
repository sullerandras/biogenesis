package biogenesis.clade_analyzer.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import biogenesis.Clade;
import biogenesis.GeneticCode;
import biogenesis.clade_analyzer.CladeSummary;
import biogenesis.clade_analyzer.DB;
import biogenesis.clade_analyzer.TimeAndPopulation;

public class CladePanel extends javax.swing.JPanel {
  private CladeSummary cladeSummary;
  private CladePopulationOverTime populationOverTimeChart;
  private List<ActionListener> actionListeners = new ArrayList<ActionListener>();

  public CladePanel(CladeSummary cladeSummary, DB db, int maxTime) {
    this.cladeSummary = cladeSummary;
    initComponents();

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

  public void initComponents() {
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

    JTextArea textAreaClade = new JTextArea(2, 20);
    textAreaClade.setText(cladeSummary.getCladeId());
    textAreaClade.setWrapStyleWord(true);
    textAreaClade.setLineWrap(true);
    textAreaClade.setOpaque(false);
    textAreaClade.setEditable(false);
    textAreaClade.setFocusable(false);
    textAreaClade.setBackground(UIManager.getColor("Label.background"));
    textAreaClade.setFont(UIManager.getFont("Label.font"));
    textAreaClade.setBorder(UIManager.getBorder("Label.border"));
    infoPanel.add(textAreaClade,
        new java.awt.GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    infoPanel.add(new javax.swing.JLabel("First seen: " + cladeSummary.getFirstSeenTime()),
        new java.awt.GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    infoPanel.add(new javax.swing.JLabel("Last seen: " + cladeSummary.getLastSeenTime()),
        new java.awt.GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    infoPanel.add(new javax.swing.JLabel("Max population: " + cladeSummary.getMaxPopulation()),
        new java.awt.GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    infoPanel.add(
        new javax.swing.JLabel("Survived time: " + (cladeSummary.getLastSeenTime() - cladeSummary.getFirstSeenTime())),
        new java.awt.GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));
    add(infoPanel, new java.awt.GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, java.awt.GridBagConstraints.NORTHWEST,
        java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    // Population over time chart
    populationOverTimeChart = new CladePopulationOverTime();
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
    textAreaClade.addMouseListener(mouseAdapter);
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
