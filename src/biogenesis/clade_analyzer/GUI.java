package biogenesis.clade_analyzer;

import biogenesis.clade_analyzer.gui.MainFrame;

public class GUI {
  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new MainFrame().setVisible(true);
      }
    });
  }
}
