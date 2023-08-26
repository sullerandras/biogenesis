package biogenesis.gui.stats;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.AffineTransform;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import biogenesis.Utils;

/**
 * Panel that shows a graph with zero or more lines, and shows a legend.
 */
public class GraphPanel extends JPanel {
	private static final long serialVersionUID = Utils.FILE_VERSION;

	private final NumberFormat nf;

	private List<GraphInfo> graphList = new ArrayList<GraphInfo>();
	private int width;
	private int height;
	private JPanel centralPanel;
	private JPanel legendPanel;
	private JLabel fromLabel;
	private JLabel toLabel;

	/**
	 * Creates a new instance of GraphPanel.
	 * @param w Width of the graph in pixels.
	 * @param h Height of the graph in pixels.
	 */
	public GraphPanel(int w, int h, NumberFormat nf) {
		this.nf = nf;
		setBackground(Color.BLACK);
		width = w;
		height = h;
		setLayout(new BorderLayout());
		centralPanel = new JPanel();
		centralPanel.setPreferredSize(new Dimension(width, height));
		centralPanel.setBackground(Color.BLACK);
		centralPanel.setOpaque(false);
		add(centralPanel, BorderLayout.CENTER);

		legendPanel = new JPanel();
		legendPanel.setBackground(Color.BLACK);
		add(legendPanel, BorderLayout.EAST);

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(1, 2));
		southPanel.setPreferredSize(new Dimension(width, 20));
		fromLabel = new JLabel(nf.format(0), SwingConstants.LEFT);
		toLabel = new JLabel(nf.format(100), SwingConstants.RIGHT);
		southPanel.add(fromLabel);
		southPanel.add(toLabel);
		add(southPanel, BorderLayout.SOUTH);
	}

	/**
	 * Add a new line to this graph.
	 * @param info List of initial values to draw.
	 * @param max Maximum value of the graph. Used to scale the values across all the graphs.
	 * @param min Minimum value of the graph. Used to scale the values across all the graphs.
	 * @param color Color of the line to draw.
	 * @param name Name of the graph, used for the legend.
	 * @return The GraphInfo object that holds the information of the new graph.
	 */
	public GraphInfo addGraph(List<Double> info, double max, double min, Color color, String name) {
		GraphInfo g = new GraphInfo(info, max, min, width, height, color, name);
		graphList.add(g);
		updateLegend();
		return g;
	}

	/**
	 * Sets the minimum time to show in the legend.
	 */
	public void setMinTime(long minTime) {
		fromLabel.setText(nf.format(minTime));
	}

	/**
	 * Sets the maximum time to show in the legend.
	 */
	public void setMaxTime(long maxTime) {
		toLabel.setText(nf.format(maxTime));
	}

	private void updateLegend() {
		legendPanel.removeAll();
		legendPanel.setLayout(new GridLayout(graphList.size(), 1));
		for (GraphInfo graph : graphList) {
			JLabel label = new JLabel(graph.getName());
			label.setForeground(graph.getColor());
			legendPanel.add(label);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform saveAT = g2.getTransform();
		g2.scale(centralPanel.getWidth() / (double) width, centralPanel.getHeight() / (double) height);
		for (GraphInfo graph : graphList) {
			graph.draw(g);
		}
		g2.setTransform(saveAT);
	}
}