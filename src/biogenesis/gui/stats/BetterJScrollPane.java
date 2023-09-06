package biogenesis.gui.stats;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 * JScrollPane with better defaults:
 * <ul>
 * <li>Unit increment of 10 pixels for both scrollbars.</li>
 * <li>Scroll mode set to BACKINGSTORE_SCROLL_MODE.</li>
 * </ul>
 */
public class BetterJScrollPane extends JScrollPane {
  /**
   * Creates a new BetterJScrollPane with unit increment set to 10 and scroll mode set to BACKINGSTORE_SCROLL_MODE.
   */
  public BetterJScrollPane() {
    this(null);
  }

  /**
   * Creates a new BetterJScrollPane with unit increment set to 10 and scroll mode set to BACKINGSTORE_SCROLL_MODE.
   * @param view The component to display in the scrollpane's viewport.
   */
  public BetterJScrollPane(java.awt.Component view) {
    super(view);

    setDefaults();
  }

  private void setDefaults() {
    getHorizontalScrollBar().setUnitIncrement(10);
    getVerticalScrollBar().setUnitIncrement(10);
    getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
  }
}
