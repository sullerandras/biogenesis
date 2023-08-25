package biogenesis;

/**
 * Creates GridBagConstraints with insets and padding set to 0.
 */
public class SimpleGridBagConstraints extends java.awt.GridBagConstraints {
  /**
   * Creates GridBagConstraints with insets and padding set to 0.
   */
  public SimpleGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill) {
    super(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, new java.awt.Insets(0, 0, 0, 0), 0, 0);
  }
}