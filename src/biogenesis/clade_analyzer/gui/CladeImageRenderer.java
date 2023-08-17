package biogenesis.clade_analyzer.gui;

import java.awt.Color;

import javax.swing.JPanel;

import biogenesis.Clade;
import biogenesis.GeneticCode;

public class CladeImageRenderer extends JPanel {
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