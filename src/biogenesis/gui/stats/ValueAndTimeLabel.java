package biogenesis.gui.stats;

import java.text.NumberFormat;

import javax.swing.JLabel;

/**
 * Label that shows a value and a time. Makes it easier to update the value and time since it stores the labels.
 */
public class ValueAndTimeLabel extends JLabel {
  private final String valueLabel;
  private final String timeLabel;
  private final NumberFormat nf;

  /**
   * Creates a new instance of ValueAndTimeLabel. Both valueLabel and timeLabel should end with a space,
   * but timeLabel should not start with space.
   * @param valueLabel The label of the value.
   * @param timeLabel The label of the time. If this is null, the time is not shown.
   * @param nf The number format to use.
   */
  public ValueAndTimeLabel(String valueLabel, String timeLabel, NumberFormat nf) {
    this.valueLabel = valueLabel;
    this.timeLabel = timeLabel;
    this.nf = nf;

    update(0, 0);
  }

  /**
   * Updates the value of this object and sets time to 0. This method should only be used when the time is not shown.
   * @param value The value to show.
   */
  public void update(double value) {
    update(value, 0);
  }

  /**
   * Updates the value and time in the text of this object.
   * The actual text looks like this:
   * <pre>
   * valueLabel + value + " " + timeLabel + time
   * </pre>
   *
   * If timeLabel is null, then only <pre>valueLabel + value</pre> is shown.
   *
   * @param value The value to show.
   * @param time The time to show.
   */
  public void update(double value, double time) {
    if (timeLabel != null) {
      setText(valueLabel + nf.format(value) + " " + timeLabel + nf.format(time));
    } else {
      setText(valueLabel + nf.format(value));
    }
  }
}
