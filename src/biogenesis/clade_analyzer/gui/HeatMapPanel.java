package biogenesis.clade_analyzer.gui;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import biogenesis.Clade;
import biogenesis.clade_analyzer.CladeDetails;
import biogenesis.clade_analyzer.CladeName;
import biogenesis.clade_analyzer.CladeNameGenerator;
import biogenesis.clade_analyzer.db.DB;

public class HeatMapPanel extends JPanel {
  private final Frame owner;
  private DB db;
  private MapPanel mapPanel;
  private JSlider slider;
  private List<Integer> times;
  private JLabel selectedTimeLabel;

  private CladeDetailsDialog cladeDetailsDialog;

  public HeatMapPanel(Frame owner) {
    this.owner = owner;
    initComponents();

    new SliderChangedThread().start();
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

  private long lastSliderChangedMillis = 0;
  private int lastSliderIndex = 0;

  private class SliderChangedThread extends Thread {
    public SliderChangedThread() {
      super("SliderChangedThread");
      setDaemon(true);
    }

    @Override
    public void run() {
      while (true) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        synchronized (HeatMapPanel.this) {
          if (System.currentTimeMillis() - lastSliderChangedMillis < 100) {
            continue;
          }
        }
        java.awt.EventQueue.invokeLater(() -> {
          synchronized (HeatMapPanel.this) {
            if (slider.getValue() != lastSliderIndex) {
              return;
            }
          }

          sliderReallyChanged();
        });
      }
    }
  }

  private void sliderChanged() {
    synchronized (this) {
      lastSliderChangedMillis = System.currentTimeMillis();
      lastSliderIndex = slider.getValue();
    }
  }

  private void sliderReallyChanged() {
    if (times == null) {
      return;
    }

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

    private CladeDetails selectedOrganism;

    public MapPanel() {
      addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
          mapPanelMouseClicked(evt);
        }
      });
    }

    protected void mapPanelMouseClicked(MouseEvent evt) {
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

      int x = (int) (evt.getX() / scale);
      int y = (int) (evt.getY() / scale);

      for (CladeDetails organism : organisms) {
        if (organism.getX() <= x && x <= organism.getX() + orgSize && organism.getY() <= y
            && y <= organism.getY() + orgSize) {
          selectedOrganism = organism;
          repaint();
          if (cladeDetailsDialog != null) {
            cladeDetailsDialog.dispose();
          }
          cladeDetailsDialog = new CladeDetailsDialog(owner, db, organism.getCladeId(), times.get(slider.getValue()));
          cladeDetailsDialog.setVisible(true);
          break;
        }
      }
    }

    public void setOrganisms(List<CladeDetails> organisms) {
      this.organisms = organisms;
      selectedOrganism = null;
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
        CladeName name = CladeNameGenerator.generateName(organism.getGeneticCode());
        g.setColor(name.getColor());
        g2.fillOval(organism.getX(), organism.getY(), orgSize, orgSize);
        // organism.getGeneticCode().draw(g, Clade.NET_CLADE_SIZE, Clade.NET_CLADE_SIZE);
      }
      if (selectedOrganism != null) {
        g.setColor(java.awt.Color.WHITE);
        g2.drawOval(selectedOrganism.getX() - orgSize, selectedOrganism.getY() - orgSize, orgSize + orgSize * 2,
            orgSize + orgSize * 2);
      }
      g2.setTransform(origTransform);
    }
  }
}
