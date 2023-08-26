package biogenesis.gui.stats;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;

/**
 * Class that holds the information needed to draw one line graph on a GraphPanel.
 */
public class GraphInfo {
  private List<Double> points;
  private int width;
  private int height;
  private Color color;
  private double max;
  private double min;
  private String name;

  /**
   * Creates a new GraphInfo object.
   * @param points Default points to draw. Can be changed later with setMaxAndPoints.
   * @param maxValue Default maximum value. Can be changed later with setMaxAndPoints.
   * @param minValue minimum value.
   * @param width Width of the graph in pixels.
   * @param height Height of the graph in pixels.
   * @param color Color of the lines to draw.
   * @param name Name of the graph, used for the legend.
   */
  public GraphInfo(List<Double> points, double maxValue, double minValue, int width, int height, Color color,
      String name) {
    this.points = points;
    max = maxValue;
    min = minValue;
    this.width = width;
    this.height = height;
    this.color = color;
    this.name = name;
  }

  /**
   * Draws the graph on the given Graphics object. There might be other graphs drawn to the same Graphics object.
   */
  public void draw(Graphics g) {
    g.setColor(color);

    int x = 0;
    double prevY = 0;
    for (Iterator<Double> it = points.iterator(); it.hasNext() && x < width; x++) {
      double y = height - (it.next().doubleValue() - min) * height / (max - min);
      if (x > 0) {
        g.drawLine(x - 1, (int) prevY, x, (int) y);
      }
      prevY = y;
    }
  }

  /**
   * Sets the maximum value and the points to draw. Need to call @link{GraphInfo#draw} to see the changes.
   * @param max The new maximum value.
   * @param points The new points to draw.
   */
  public void setMaxAndPoints(double max, List<Double> points) {
    this.max = max;
    this.points = points;
  }

  public String getName() {
    return name;
  }

  public Color getColor() {
    return color;
  }
}