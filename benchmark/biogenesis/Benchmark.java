package biogenesis;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Headless benchmark: loads a saved world, runs it for a number of frames and
 * prints the time per frame and a checksum of the final world state.
 *
 * Single-threaded runs are deterministic, so the checksum must not change when
 * an optimization is meant to keep the simulation identical. Multi-threaded runs
 * are not deterministic (the order organisms are processed in varies).
 *
 * Usage: make benchmark WORLD=runs/1.bgw.gz FRAMES=500 THREADS=1
 */
public class Benchmark {
  /** InfoToolbar stand-in. Instantiated without running any constructor (see INFO). */
  static class NullInfoToolbar extends InfoToolbar {
    private static final long serialVersionUID = 1L;
    NullInfoToolbar() { super(null, null); }
    @Override public void changeNChildren() {}
    @Override public void changeNInfected() {}
    @Override public void changeNKills() {}
    @Override public void recalculate() {}
  }

  static final InfoToolbar INFO;
  static {
    try {
      // InfoToolbar's constructor needs a real MainWindow; skip constructors altogether.
      INFO = (InfoToolbar) sun.reflect.ReflectionFactory.getReflectionFactory()
          .newConstructorForSerialization(NullInfoToolbar.class, Object.class.getDeclaredConstructor())
          .newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static final MainWindowInterface MAIN_WINDOW = new MainWindowInterface() {
    @Override public Frame getFrame() { return null; }
    @Override public World getWorld() { return null; }
    @Override public VisibleWorld getVisibleWorld() { return null; }
    @Override public InfoToolbar getInfoPanel() { return INFO; }
    @Override public BioFile getBioFile() { return null; }
  };

  static final VisibleWorldInterface VISIBLE_WORLD = new VisibleWorldInterface() {
    @Override public MainWindowInterface getMainWindow() { return MAIN_WINDOW; }
    @Override public void repaint() {}
    @Override public void repaint(Rectangle r) {}
    @Override public void setPreferredSize(Dimension d) {}
    @Override public Organism getSelectedOrganism() { return null; }
    @Override public void setSelectedOrganism(Organism o) {}
    @Override public void showDeadToolbar() {}
  };

  public static void main(String[] args) throws Exception {
    if (args.length < 3) {
      System.err.println("usage: Benchmark <world.bgw[.gz]> <frames> <threads> [seed]");
      System.exit(1);
    }
    final String worldFile = args[0];
    final int frames = Integer.parseInt(args[1]);

    Utils.readPreferences();
    Utils.THREAD_COUNT = Integer.parseInt(args[2]);
    Utils.random.setSeed(args.length > 3 ? Long.parseLong(args[3]) : 0);

    World world;
    InputStream in = new FileInputStream(worldFile);
    if (worldFile.endsWith(".gz")) {
      in = new GZIPInputStream(in);
    }
    try (ObjectInputStream objectIn = new ObjectInputStream(in)) {
      world = (World) objectIn.readObject();
    }
    world.init(VISIBLE_WORLD);
    world.worldStatistics.saveGameLoaded(MAIN_WINDOW);
    System.out.println("loaded " + worldFile + ", population " + world.getPopulation()
        + ", threads " + Utils.THREAD_COUNT);

    final long start = System.nanoTime();
    long lap = start;
    for (int i = 1; i <= frames; i++) {
      world.time();
      if (i % 100 == 0 || i == frames) {
        long now = System.nanoTime();
        int lapFrames = i % 100 == 0 ? 100 : i % 100;
        System.out.printf("frame %d, population %d, %.2f ms/frame%n", i, world.getPopulation(),
            (now - lap) / 1e6 / lapFrames);
        lap = now;
      }
    }
    long totalMs = (System.nanoTime() - start) / 1_000_000;
    System.out.printf("total %d ms, %.2f ms/frame, population %d, checksum %s%n", totalMs,
        (double) totalMs / frames, world.getPopulation(), Long.toHexString(checksum(world)));
    System.exit(0); // the parallel executor's worker threads are not daemons
  }

  /** Hash of organism and atmosphere state; diverges quickly if the simulation changes. */
  static long checksum(World world) {
    List<Organism> organisms;
    synchronized (world._organisms) {
      organisms = new ArrayList<>(world._organisms);
    }
    organisms.sort((a, b) -> Integer.compare(a._ID, b._ID));
    long h = 17;
    for (Organism o : organisms) {
      h = h * 31 + o._ID;
      h = h * 31 + Double.doubleToLongBits(o._dCenterX);
      h = h * 31 + Double.doubleToLongBits(o._dCenterY);
      h = h * 31 + Double.doubleToLongBits(o._theta);
      h = h * 31 + Double.doubleToLongBits(o._energy);
      h = h * 31 + o._growthRatio;
    }
    h = h * 31 + Double.doubleToLongBits(world.getO2());
    h = h * 31 + Double.doubleToLongBits(world.getCO2());
    return h;
  }
}
