package biogenesis.clade_analyzer.gui;

import javax.swing.JTextArea;
import javax.swing.UIManager;

public class MultilineLabel extends JTextArea {
  private static final long serialVersionUID = 1L;

  public MultilineLabel(String text) {
    super(text);
    setWrapStyleWord(true);
    setLineWrap(true);
    setOpaque(false);
    setEditable(false);
    setFocusable(false);
    setBackground(UIManager.getColor("Label.background"));
    setFont(UIManager.getFont("Label.font"));
    setBorder(UIManager.getBorder("Label.border"));
  }

  public void setText(String text) {
    super.setText(text);
    setRows(getLineCount());
  }
}
