package biogenesis.gui.stats;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
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
	private int hoveredIndex = -1;

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
		centralPanel.setMinimumSize(centralPanel.getPreferredSize());
		centralPanel.setBackground(Color.BLACK);
		centralPanel.setOpaque(false);
		add(centralPanel, BorderLayout.CENTER);

		legendPanel = new JPanel();
		legendPanel.setBackground(Color.BLACK);
		add(legendPanel, BorderLayout.EAST);

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(1, 2));
		southPanel.setPreferredSize(new Dimension(width, 20));
		southPanel.setMinimumSize(southPanel.getPreferredSize());
		fromLabel = new JLabel(nf.format(0), SwingConstants.LEFT);
		toLabel = new JLabel(nf.format(100), SwingConstants.RIGHT);
		southPanel.add(fromLabel);
		southPanel.add(toLabel);
		add(southPanel, BorderLayout.SOUTH);

		MouseAdapter mouseAdapter = new MouseAdapter() {
			private Popup popup = null;
			private final GraphTooltipPanel tooltip = new GraphTooltipPanel(nf);
			private final Object monitor = new Object();

			@Override
			public void mouseMoved(MouseEvent e) {
				if (graphList.isEmpty()) {
					return;
				}

				final int elementCount = graphList.get(0).getPointsSize();
				final int index = (int) Math.round(e.getX() * elementCount / (double) centralPanel.getWidth());
				if (index < 0 || index >= elementCount) {
					return;
				}
				hoveredIndex = index;

				for (GraphInfo graph : graphList) {
					tooltip.addValue(graph.getName(), graph.getPointAt(index));
				}

				// Show popup on the screen and ensure it's always visible
				final Point p = e.getLocationOnScreen();
				int x = p.x + 20;
				int y = p.y + 5;
				if (x + 20 + tooltip.getWidth() > getToolkit().getScreenSize().width) {
					x = p.x - 20 - tooltip.getWidth();
				}
				if (y + 20 + tooltip.getHeight() > getToolkit().getScreenSize().height) {
					y = p.y - 20 - tooltip.getHeight();
				}
				synchronized (monitor) {
					final Popup oldPopup = popup;
					popup = PopupFactory.getSharedInstance().getPopup(centralPanel, tooltip, x, y);
					popup.show();
					if (oldPopup != null) {
						oldPopup.hide();
					}
				}

				GraphPanel.this.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				hoveredIndex = -1;
				if (popup != null) {
					synchronized (monitor) {
						popup.hide();
						popup = null;
					}
				}
			}
		};

		centralPanel.addMouseMotionListener(mouseAdapter);
		centralPanel.addMouseListener(mouseAdapter);
	}

	/**
	 * Add a new line to this graph.
	 */
	public void addGraph(GraphInfo graphInfo) {
		graphList.add(graphInfo);
		updateLegend();
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
		for (GraphInfo graph : graphList) {
			AffineTransform saveAT = g2.getTransform();
			graph.draw(g, hoveredIndex, centralPanel.getWidth(), centralPanel.getHeight());
			g2.setTransform(saveAT);
		}
	}
}