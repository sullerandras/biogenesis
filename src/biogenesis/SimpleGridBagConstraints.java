package biogenesis;

/**
 * Creates GridBagConstraints with insets and padding set to 0.
 */
public class SimpleGridBagConstraints extends java.awt.GridBagConstraints {
  /**
   * Creates GridBagConstraints with insets and padding set to 0.
   */
  public SimpleGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty,
      int anchor, int fill) {
    super(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, new java.awt.Insets(0, 0, 0, 0), 0, 0);
  }

  /**
   * Creates GridBagConstraints with insets and padding set to 0, and gridwidth and gridheight set to 1.
   */
  public SimpleGridBagConstraints(int gridx, int gridy, double weightx, double weighty, int anchor, int fill) {
    this(gridx, gridy, 1, 1, weightx, weighty, anchor, fill);
  }

  /**
   * Creates a new SimpleGridBagConstraints with the same values as this one, except for gridx and gridy.
   * @param gridx The new gridx value.
   * @param gridy The new gridy value.
   * @return A new SimpleGridBagConstraints with the same values as this one, except for gridx and gridy.
   */
  public SimpleGridBagConstraints withGridXY(int gridx, int gridy) {
    return new SimpleGridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill);
  }

  /**
   * Creates a new SimpleGridBagConstraints with the same values as this one, except for fill.
   * @param fill The new fill value.
   * @return A new SimpleGridBagConstraints with the same values as this one, except for fill.
   */
  public SimpleGridBagConstraints withFill(int fill) {
    return new SimpleGridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill);
  }

  /**
   * Creates a new SimpleGridBagConstraints with the same values as this one, except for weightx and weighty.
   * @param weightx The new weightx value.
   * @param weighty The new weighty value.
   * @return A new SimpleGridBagConstraints with the same values as this one, except for weightx and weighty.
   */
  public SimpleGridBagConstraints withWeight(double weightx, double weighty) {
    return new SimpleGridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill);
  }
}