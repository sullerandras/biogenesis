package biogenesis;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.util.Random;

public class Benchmark {
  class NullVisibleWorld implements VisibleWorldInterface {
    private MainWindowInterface mainWindowInterface;
    
    NullVisibleWorld(MainWindowInterface mainWindowInterface) {
      this.mainWindowInterface = mainWindowInterface;
    }
    @Override
    public MainWindowInterface getMainWindow() {
      return mainWindowInterface;
    }
    @Override
    public Organism getSelectedOrganism() {
      return null;
    }
    @Override
    public void repaint() {
    }
    @Override
    public void repaint(Rectangle r) {
    }
    @Override
    public void setPreferredSize(Dimension d) {
    }
    @Override
    public void setSelectedOrganism(Organism o) {
    }
    @Override
    public void showDeadToolbar() {
    }
  }

  class NullMainWindow implements MainWindowInterface {
    NullInfoToolbar nullInfoToolbar = new NullInfoToolbar();
    @Override
    public Frame getFrame() {
      return null;
    }
    @Override
    public InfoToolbarInterface getInfoPanel() {
      return nullInfoToolbar;
    }
    @Override
    public VisibleWorld getVisibleWorld() {
      return null;
    }
    @Override
    public World getWorld() {
      return null;
    }
  }

  class NullInfoToolbar implements InfoToolbarInterface {
    @Override
    public void changeNChildren() {
    }
    @Override
    public void changeNInfected() {
    }
    @Override
    public void changeNKills() {
    }
    @Override
    public void recalculate() {
    }
    @Override
    public void setSelectedOrganism(Organism o) {
    }
  }

  public void runBenchmark() {
    Utils.random = new Random(0);
    World world = new World(new NullVisibleWorld(new NullMainWindow()));
    world.genesis();
    long start = System.currentTimeMillis();
    final int maxFrames = 256 * 10;
    for (int i = 0; i < maxFrames; i++) {
      System.out.print("frame "+i+" / "+maxFrames+"\r");
      world.time();
    }
    long end = System.currentTimeMillis();
    System.out.println("Benchmark took "+(end - start)+" msec, population: "+world._population+", clades: "+world.getDistinctCladeIDCount());
  }

  public static void main(String[] args) {
    Benchmark b = new Benchmark();
    b.runBenchmark();
  }
}
