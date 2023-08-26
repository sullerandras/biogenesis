package biogenesis.gui.stats;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class that holds the information needed to draw one line graph on a GraphPanel.
 */
public class GraphInfo {
  private List<Double> values;
  private int capacity;
  private int height;
  private Color color;
  private double max;
  private double min;
  private String name;

  /**
   * Creates a new GraphInfo object.
   * @param maxValue Default maximum value. Can be changed later with setMaxAndPoints.
   * @param minValue minimum value.
   * @param capacity The capacity of the graph, i.e. it will always show exactly this number of points.
   *                 If this is 0 then the size of <pre>values</pre> will be used instead.
   * @param height Height of the graph in pixels.
   * @param color Color of the lines to draw.
   * @param name Name of the graph, used for the legend.
   */
  public GraphInfo(double maxValue, double minValue, int capacity, int height, Color color,
      String name) {
    this.values = new ArrayList<>();
    max = maxValue;
    min = minValue;
    this.capacity = capacity;
    this.height = height;
    this.color = color;
    this.name = name;
  }

  /**
   * Draws the graph on the given Graphics object. There might be other graphs drawn to the same Graphics object.
   */
  public void draw(Graphics g, int hoveredIndex) {
    if (values.size() == 0) {
      return;
    }

    g.setColor(color);

    int capacity = getActualCapacity();
    int x = 0;
    double prevY = 0;
    for (Iterator<Double> it = values.iterator(); it.hasNext() && x < capacity; x++) {
      double y = height - (it.next().doubleValue() - min) * height / (max - min);
      if (x > 0) {
        g.drawLine(x - 1, (int) prevY, x, (int) y);
      }
      prevY = y;
    }

    if (hoveredIndex >= 0 && hoveredIndex < capacity) {
      g.setColor(Color.WHITE);
      g.drawLine(hoveredIndex, 0, hoveredIndex, height);
    }
  }

  /**
   * Sets the maximum value and the values to draw. Need to call {@link GraphInfo#draw(Graphics)} to see the changes.
   * @param max The new maximum value.
   * @param values The new values to draw.
   */
  public void setMaxAndPoints(double max, List<Double> values) {
    this.max = max;
    if (values != null) {
      this.values = values;
    }
  }

  public String getName() {
    return name;
  }

  public Color getColor() {
    return color;
  }

  public double getPointAt(int index) {
    if (index < 0 || index >= values.size()) {
      return 0;
    }
    return values.get(index);
  }

  public int getPointsSize() {
    return values.size();
  }

  public int getActualCapacity() {
    return capacity > 0 ? capacity : values.size();
  }
}