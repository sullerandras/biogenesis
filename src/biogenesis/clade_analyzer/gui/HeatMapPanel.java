package biogenesis.clade_analyzer.gui;

import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import biogenesis.Clade;
import biogenesis.clade_analyzer.CladeDetails;
import biogenesis.clade_analyzer.CladeNameGenerator;
import biogenesis.clade_analyzer.db.DB;

public class HeatMapPanel extends JPanel {
  private DB db;
  private MapPanel mapPanel;
  private JSlider slider;
  private List<Integer> times;
  private JLabel selectedTimeLabel;

  public HeatMapPanel() {
    initComponents();
  }

  public void setDB(DB db) {
    this.db = db;
    db.getListOfTimes().then(times -> {
      java.awt.EventQueue.invokeLater(() -> {
        setListOfTimes(times);
      });
    }).onError(e -> {
      System.out.println("Error getting list of times: " + e);
      e.printStackTrace();
    });

    slider.addChangeListener(e -> {
      sliderChanged();
    });
  }

  private void sliderChanged() {
    if (times == null) {
      return;
    }

    final int index = slider.getValue();
    final int time = times.get(index);
    selectedTimeLabel.setText("Time: " + time);

    new Thread() {
      @Override
      public void run() {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        java.awt.EventQueue.invokeLater(() -> {
          if (slider.getValue() != index) {
            return;
          }
          sliderReallyChanged();
        });
      }
    }.start();
  }

  private void sliderReallyChanged() {
    final int index = slider.getValue();
    final int time = times.get(index);
    selectedTimeLabel.setText("Time: " + time);

    db.getOrganismsAtTime(time).then(clades -> {
      java.awt.EventQueue.invokeLater(() -> {
        mapPanel.setOrganisms(clades);
      });
    }).onError(e -> {
      System.out.println("Error getting clades: " + e);
      e.printStackTrace();
    });
  }

  private void setListOfTimes(List<Integer> times) {
    this.times = times;
    slider.setMinimum(0);
    slider.setMaximum(times.size() - 1);
  }

  private void initComponents() {
    setLayout(new GridBagLayout());

    mapPanel = new MapPanel();
    add(mapPanel,
        new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
            GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    JPanel controlsPanel = new JPanel();
    controlsPanel.add(new JLabel("Select time: "));
    slider = new JSlider();
    slider.setValue(0);
    controlsPanel.add(slider);
    selectedTimeLabel = new JLabel();
    controlsPanel.add(selectedTimeLabel);
    add(controlsPanel,
        new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    revalidate();
  }

  private class MapPanel extends JPanel {
    private List<CladeDetails> organisms;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;

    public MapPanel() {
    }

    public void setOrganisms(List<CladeDetails> organisms) {
      this.organisms = organisms;
      minX = Integer.MAX_VALUE;
      minY = Integer.MAX_VALUE;
      maxX = Integer.MIN_VALUE;
      maxY = Integer.MIN_VALUE;
      for (CladeDetails organism : organisms) {
        minX = Math.min(minX, organism.getX());
        minY = Math.min(minY, organism.getY());
        maxX = Math.max(maxX, organism.getX());
        maxY = Math.max(maxY, organism.getY());
      }
      repaint();
    }

    @Override
    public void paint(java.awt.Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      g.setColor(java.awt.Color.BLACK);
      g.fillRect(0, 0, getWidth(), getHeight());

      if (organisms == null) {
        return;
      }

      int orgSize = Clade.NET_CLADE_SIZE / 3;

      double scale = 1.0;
      if (maxX - minX > getWidth()) {
        scale = (double) getWidth() / (double) (maxX - minX + orgSize);
      }
      if (maxY - minY > getHeight()) {
        scale = Math.min(scale, (double) getHeight() / (double) (maxY - minY + orgSize));
      }
      g2.scale(scale, scale);

      AffineTransform origTransform = g2.getTransform();
      for (CladeDetails organism : organisms) {
        // g2.setTransform(origTransform);
        // g2.translate(organism.getX() - Clade.NET_CLADE_SIZE / 2, organism.getY() - Clade.NET_CLADE_SIZE / 2);
        String name = CladeNameGenerator.generateName(organism.getGeneticCode());
        switch (name.charAt(0)) {
          case 'V':
            g.setColor(java.awt.Color.WHITE);
            break;
          case 'C':
            g.setColor(java.awt.Color.RED);
            break;
          case 'P':
            g.setColor(java.awt.Color.GREEN);
            break;
          case 'O':
            g.setColor(java.awt.Color.BLUE);
            break;
          default:
            g.setColor(java.awt.Color.YELLOW);
            break;
        }
        g2.fillOval(organism.getX(), organism.getY(), orgSize, orgSize);
        // organism.getGeneticCode().draw(g, Clade.NET_CLADE_SIZE, Clade.NET_CLADE_SIZE);
      }
      g2.setTransform(origTransform);
    }
  }
}
