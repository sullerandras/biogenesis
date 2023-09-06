package biogenesis.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 * Panel that shows a multiline label. Internally it uses a JTextArea to
 * render the text.
 */
public class MultilineLabel extends JPanel {
  private static final long serialVersionUID = 1L;
  private final JTextArea renderer;
  private int lastHeight;

  public MultilineLabel(String text) {
    renderer = new JTextArea(text);

    renderer.setWrapStyleWord(true);
    renderer.setLineWrap(true);
    renderer.setOpaque(false);
    renderer.setEditable(false);
    renderer.setFocusable(false);
    renderer.setBackground(UIManager.getColor("Label.background"));
    renderer.setFont(UIManager.getFont("Label.font"));
    renderer.setBorder(UIManager.getBorder("Label.border"));
    setPreferredHeight();

    addComponentListener(new java.awt.event.ComponentAdapter() {
      @Override
      public void componentResized(java.awt.event.ComponentEvent evt) {
        setPreferredHeight();
      }
    });
  }

  /**
   * Sets the text to be rendered.
   */
  public void setText(String text) {
    renderer.setText(text);
    setPreferredHeight();
  }

  /**
   * Sets the minimum height and preferred height of the panel to the height of the text.
   * This is also called when the panel is resized.
   * We need to change the minimum and preferred height so the layout automatically
   * adjusts when the text becomes too long for one line.
   * Both the minimum width and the preferred width are set to 1, so the label can
   * become narrower if needed.
   */
  private void setPreferredHeight() {
    renderer.setSize(MultilineLabel.this.getWidth(), 1000);
    final int preferredHeight = renderer.getPreferredSize().height;
    if (lastHeight != preferredHeight) {
      setMinimumSize(new Dimension(1, preferredHeight));
      setPreferredSize(new Dimension(1, preferredHeight));
      invalidate();
    }
    lastHeight = preferredHeight;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    renderer.setSize(getWidth(), getHeight());
    renderer.paint(g);
  }
}
