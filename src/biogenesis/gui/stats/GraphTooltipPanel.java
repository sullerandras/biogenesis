package biogenesis.gui.stats;

import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel that shows a tooltip with the values of the graph at the mouse position.
 */
public class GraphTooltipPanel extends JPanel {
  private final Map<String, Double> values = new LinkedHashMap<>();
  private final NumberFormat nf;
  private JLabel label;
  private String xAxisName;
  private long xAxisValue;

  /**
   * Creates a new instance of GraphTooltipPanel.
   * @param nf NumberFormat to use to format the values.
   */
  public GraphTooltipPanel(NumberFormat nf) {
    super();
    this.nf = nf;

    setBorder(javax.swing.BorderFactory.createRaisedBevelBorder());
    label = new JLabel();
    add(label);
  }

  /**
   * Sets the value of the X axis. This will be shown as the first line of the tooltip.
   * @param name Name of the X axis.
   * @param value Value of the X axis.
   */
  public void setXAxis(String name, long value) {
    xAxisName = name;
    xAxisValue = value;
  }

  /**
   * Adds a new value to the tooltip.
   * @param name Name of the value.
   * @param value Value to add.
   */
  public void addValue(String name, double value) {
    values.put(name, value);

    updateText();
  }

  /**
   * Adds a new value to the tooltip for the atmosphere graph.
   * @param name Name of the value.
   * @param value Value to add.
   */
  public void addAtmosphereValue(String name, double value) {
    values.put(name, (value * value * value * value));

    updateText();
  }

  private void updateText() {
    StringBuilder sb = new StringBuilder("<html><table>");
    if (xAxisName != null) {
      sb.append("<tr><th>").append(xAxisName).append("</th><td>").append(xAxisValue).append("</td></tr>");
    }

    for (Map.Entry<String, Double> entry : values.entrySet()) {
      sb.append("<tr><th>").append(entry.getKey()).append("</th><td>").append(nf.format(entry.getValue())).append("</td></tr>");
    }
    sb.append("</table></html>");
    label.setText(sb.toString());
  }
}
