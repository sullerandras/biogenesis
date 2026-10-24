package biogenesis;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Headless world: a seeded genesis or a loaded save, then frames. Prints, for
 * every frame, the population and a hash of the save file text (the world and
 * the statics, see Dump), or with "full N" that text for frame N; "save N
 * file" writes it. rust/core/src/bin/world_check.rs does the
 * same with the translated code; the outputs must be identical.
 *
 * The GUI is stubbed as in benchmark/Benchmark.java (and as J2R's `stub`
 * types behave): no selected organism, repaints do nothing.
 *
 * Usage: WorldCheck new seed width height organisms frames [options]
 *        WorldCheck load file frames [options]
 * options: save N file, full N, every K (hash every K-th frame only; 0: the last)
 * (with --add-opens java.base/java.util=ALL-UNNAMED, for Random and HashSet internals)
 */
public class WorldCheck {
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

	static final java.nio.file.Path STATICS = java.nio.file.Paths.get("rust/core/src/generated/statics.txt");

	public static void main(String[] args) throws java.io.IOException {
		Dump.stubs = new Class<?>[] { VisibleWorldInterface.class, VisibleWorld.class, MainWindowInterface.class, InfoToolbar.class,
				BioFile.class, InCorridor.class, OutCorridor.class, Corridor.class, Dimension.class, java.awt.Graphics.class };
		World world;
		int i;
		if (args[0].equals("new")) {
			long seed = Long.parseLong(args[1]);
			Utils.WORLD_WIDTH = Integer.parseInt(args[2]);
			Utils.WORLD_HEIGHT = Integer.parseInt(args[3]);
			Utils.INITIAL_ORGANISMS = Integer.parseInt(args[4]);
			Utils.random = new Random(seed);
			world = new World(VISIBLE_WORLD);
			world.genesis();
			i = 5;
		} else if (args[0].equals("load")) {
			String text = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(args[1])), StandardCharsets.UTF_8);
			java.util.Map<Class<?>, Object> stubs = new java.util.LinkedHashMap<>();
			stubs.put(VISIBLE_WORLD.getClass(), VISIBLE_WORLD);
			stubs.put(MAIN_WINDOW.getClass(), MAIN_WINDOW);
			stubs.put(INFO.getClass(), INFO);
			world = Load.load(text, stubs);
			i = 2;
		} else {
			throw new IllegalArgumentException("usage: WorldCheck new|load ...");
		}
		int frames = Integer.parseInt(args[i++]);
		int saveAt = -1, full = -1, every = 1;
		String saveFile = null;
		while (i < args.length) {
			if (args[i].equals("save")) {
				saveAt = Integer.parseInt(args[i + 1]);
				saveFile = args[i + 2];
				i += 3;
			} else if (args[i].equals("every")) {
				every = Integer.parseInt(args[i + 1]);
				i += 2;
			} else if (args[i].equals("full")) {
				full = Integer.parseInt(args[i + 1]);
				i += 2;
			} else {
				throw new IllegalArgumentException(args[i]);
			}
		}

		StringBuilder out = new StringBuilder();
		for (int f = 0; f <= frames; f++) {
			if (f > 0) world.time();
			boolean hashed = every == 0 ? f == frames : f % every == 0;
			if (!(hashed || f == saveAt || f == full)) continue;
			String save = save(world);
			if (f == saveAt) java.nio.file.Files.write(java.nio.file.Paths.get(saveFile), save.getBytes(StandardCharsets.UTF_8));
			if (f == full) {
				System.out.print(save);
				return;
			}
			if (!hashed) continue;
			out.append("frame ").append(f).append(" population ").append(world.getPopulation())
					.append(" organisms ").append(world._organisms.size())
					.append(" hash ").append(Long.toHexString(fnv(save))).append('\n');
		}
		System.out.print(out);
	}

	/** The save file text: as rust/core/src/save.rs writes it. */
	static String save(World world) throws java.io.IOException {
		Dump d = new Dump();
		d.raw(Load.HEADER);
		d.field("world", world, World.class);
		d.statics(STATICS);
		return d.out.toString();
	}

	/** FNV-1a, 64 bits, of the UTF-8 bytes. */
	static long fnv(String s) {
		long h = 0xcbf29ce484222325L;
		for (byte b : s.getBytes(StandardCharsets.UTF_8)) {
			h ^= b & 0xff;
			h *= 0x100000001b3L;
		}
		return h;
	}
}
