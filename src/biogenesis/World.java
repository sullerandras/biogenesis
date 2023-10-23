/* Copyright (C) 2006-2010  Joan Queralt Molina
 * Color Mod (C) 2012-2022  MarcoDBAA
 * Performance enhancing R-trees implemented by Richard David Williams
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package biogenesis;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import com.google.gson.annotations.Expose;

import biogenesis.parallel_executor.ParallelExecutor;
/**
 * This class contains all the information needed to run a world:
 * the organisms, the substances and the biological corridors. It
 * also contains a reference to the visible part of the world,
 * {@link VisibleWorld}, and its statistics {@link WorldStatistics}.
 * There are methods to do all needed operations to the world: manage
 * organisms and substances.
 */
public class World implements Serializable{
	/**
	 * Version number of the class
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;
	/**
	 * World width
	 */
	@Expose
	protected int _width;
	/**
	 * World height
	 */
	@Expose
	protected int _height;
	/**
	 * A list of the organisms in the world, even dead ones.
	 * Note that this must be a synchronized list so it is mandatory to
	 * manually synchronize when iterating over it.
	 */
	@Expose
	protected Collection<Organism> _organisms;
	/**
	 * A list of all input biological corridors from where organisms
	 * of other hosts will arrive.
	 * Note that this must be a synchronized list so it is mandatory to
	 * manually synchronize when iterating over it.
	 */
	transient protected List<InCorridor> inCorridors;
	/**
	 * A list of all output biological corridors to send organisms
	 * to other hosts.
	 * Note that this must be a synchronized list so it is mandatory to
	 * manually synchronize when iterating over it.
	 */
	transient protected List<OutCorridor> outCorridors;
	/**
	 * Number of living organisms in the world
	 */
	protected volatile int _population = 0;
	private static final Object _population_monitor = new Object();
	/**
	 * The next identification number that will be assigned to an organism
	 * in this world
	 */
	protected int NEXT_ID;
	private static final Object NEXT_ID_monitor = new Object();
	/**
	 * The next identification part of the string that will be assigned to an organisms clade
	 * in this world
	 */
	protected int NEXT_CLADE_PART;
	private static final Object NEXT_CLADE_PART_monitor = new Object();
	/**
	 * A reference to the visible world part of this world used basically
	 * to indicate which parts of the world should be repainted due to
	 * events in the world.
	 */
	transient protected VisibleWorldInterface _visibleWorld;
	/**
	 * Frame counter. 256 frames are a time unit. This value is used to count
	 * time and to trigger some window updating at regular intervals.
	 */
	private int nFrames;
	/**
	 * Resource manager keeps track of all resources in the world.
	 */
	@Expose
	private ResourceManager resourceManager;
	/**
	 * Do we need to check for corridors?
	 */
	protected boolean _corridorexists;
	/**
	 * Did we backup already?
	 */
	protected boolean _isbackuped;
	/**
	 * Did we autosave already?
	 */
	protected boolean _issaved;
	/**
	 * Reference to the object that keeps track of all world statistics.
	 */
	@Expose
	protected WorldStatistics worldStatistics;
	/**
	 * Called by the JRE when an instance of this class is read from a file
	 *
	 * @param in  The stream from where the object comes from
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream in)
    throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		inCorridors = Collections.synchronizedList(new ArrayList<InCorridor>());
		outCorridors = Collections.synchronizedList(new ArrayList<OutCorridor>());
	}
	/**
	 * Finds an organism that has the given coordinates inside its bounding box and
	 * returns a reference to it. If more than on organism satisfies this condition,
	 * if possible, an alive organism is returned. If non organism satisfies this
	 * condition, this method returns null.
	 *
	 * @param x  X coordinate
	 * @param y  Y coordinate
	 * @return  An organism with the point (x,y) inside its bounding box, or null
	 * if such organism doesn't exist.
	 */
	public Organism findOrganismFromPosition(int x, int y) {
		Organism b;
		Organism deadOrganism = null;
		synchronized(_organisms) {
			for (Iterator<Organism> it = _organisms.iterator(); it.hasNext(); ) {
				b = it.next();
				if (b.contains(x,y)) {
					if (b.isAlive())
						return b;
					deadOrganism = b;
				}
			}
		}
		return deadOrganism;
	}
	/**
	 * Returns the world's width.
	 *
	 * @return  The world's width.
	 */
	public int getWidth() {
		return _width;
	}
	/**
	 * Returns the world's height.
	 *
	 * @return  The world's height.
	 */
	public int getHeight() {
		return _height;
	}
	/**
	 * Returns the next available organism identification number.
	 *
	 * @return  A unique number used to identify an organism.
	 */
	public int getNewId() {
		synchronized (NEXT_ID_monitor) {
			return NEXT_ID++;
		}
	}
	/**
	 * Returns the next available clade part identification number.
	 *
	 * @return  A unique number used to identify part of the clade string
	 */
	public int getNewCladePart() {
		synchronized (NEXT_CLADE_PART_monitor) {
			return NEXT_CLADE_PART++;
		}
	}
	/**
	 * Returns the actual time.
	 *
	 * @return  The actual time.
	 */
	public long getTime() {
		return worldStatistics.getTime();
	}
	/**
	 * Returns the actual frame.
	 *
	 * @return  The actual frame.
	 */
	public long getFrame() {
		return nFrames;
	}
	/**
	 * Returns the number of corpses that still have energy and drawn in the
	 * world.
	 *
	 * @return  The number of corpses in the world.
	 */
	public int getNCorpses() {
		return _organisms.size() - _population;
	}
	/**
	 * Returns the number of alive organisms that populate the world.
	 *
	 * @return  The number of alive organisms in the world.
	 */
	public int getPopulation() {
		return _population;
	}
	/**
	 * Returns the number of distinct cladeIDs in the current population.
	 */
	public int getDistinctCladeIDCount() {
		return getDistinctCladeIDCount(1);
	}
	/**
	 * Returns the number of distinct cladeIDs in the current population.
	 * @param minPopulation only count clades with at least minPopulation organisms
	 */
	public int getDistinctCladeIDCount(int minPopulation) {
		synchronized (_organisms) {
		return (int) _organisms
				.stream()
				.filter(o -> o.isAlive())
				.collect(Collectors.groupingBy(o -> o.getGeneticCode().getcladeID(), Collectors.counting()))
				.entrySet()
				.stream()
				.filter(e -> e.getValue() >= minPopulation) // only show clades with at least minPopulation organisms
				.count();
		}
	}
	/**
	 * Increase the population counter by one.
	 *
	 * This method should be called every time a new organism is
	 * created. Normally, it is called by addOrganism, but in some
	 * cases it may be used directly.
	 */
	public void increasePopulation() {
		synchronized (_population_monitor) {
			_population++;
			worldStatistics.eventPopulationIncrease(_population);
		}
	}
	/**
	 * Decrease the population counter by one.
	 *
	 * This method should be called every time an organism dies.
	 * Normally, it is called by Organism.die or Organism.breath,
	 * but in some cases it may be used directly.
	 */
	public void decreasePopulation() {
		synchronized (_population_monitor) {
			_population--;
			worldStatistics.eventPopulationDecrease(_population);
		}
	}
	/**
	 * Returns the amount of O2 that exist in the atmosphere.
	 *
	 * @return  The amount of O2.
	 */
	public double getO2() {
		return resourceManager.getTotalO2();
	}
	/**
	 * Returns the amount of CO2 that exist in the atmosphere.
	 *
	 * @return  The amount of CO2.
	 */
	public double getCO2() {
		return resourceManager.getTotalCO2();
	}
	/**
	 * Returns the amount of CH4 that exist in the atmosphere.
	 *
	 * @return  The amount of CH4.
	 */
	public double getCH4() {
		return resourceManager.getTotalCH4();
	}
	/**
	 * Returns the amount of CO that exist in the atmosphere.
	 *
	 * @return  The amount of CO.
	 */
	public double getCO1() {
		return resourceManager.getTotalCO1();
	}
	/**
	 * Returns the amount of detritus that exist in the atmosphere.
	 *
	 * @return  The amount of detritus.
	 */
	public double getDetritus() {
		return resourceManager.getTotalDetritus();
	}
	/**
	 * Add CO2 to the atmosphere.
	 *
	 * @param q  The amount of CO2 to add.
	 */
	public void addCO2(double q) {
		resourceManager.addCO2(q);
	}
	/**
	 * Add CH4 to the atmosphere.
	 *
	 * @param q  The amount of CH4 to add.
	 */
	public void addCH4(double q) {
		resourceManager.addCH4(q);
	}
	/**
	 * Add CO to the atmosphere.
	 *
	 * @param q  The amount of CO to add.
	 */
	public void addCO1(double q) {
		resourceManager.addCO1(q);
	}
	/**
	 * Add detritus to the atmosphere.
	 *
	 * @param q  The amount of detritus to add.
	 */
	public void addDetritus(double q) {
		resourceManager.addDetritus(q);
	}
	/**
	 * Substract CO2 from the atmosphere.
	 *
	 * @param q  The amount of CO2 to substract.
	 */
	public void decreaseCO2(double q) {
		resourceManager.removeCO2(q);
	}
	/**
	 * Substract CH4 from the atmosphere.
	 *
	 * @param q  The amount of CH4 to substract.
	 */
	public void decreaseCH4(double q) {
		resourceManager.removeCH4(q);
	}
	/**
	 * Substract CO from the atmosphere.
	 *
	 * @param q  The amount of CO to substract.
	 */
	public void decreaseCO1(double q) {
		resourceManager.removeCO1(q);
	}
	/**
	 * Substract detritus from the atmosphere.
	 *
	 * @param q  The amount of detritus to substract.
	 */
	public void decreaseDetritus(double q) {
		resourceManager.removeDetritus(q);
	}
	/**
	 * Takes the given amount of CO2 from the region around the given coordinates and converts it to O2.
	 * If the regi=ion doesn't have enough CO2, only the available amount is
	 * converted.
	 *
	 * @param q  The amount of CO2 to convert.
	 * @return  The amount of CO2 converted. This is always <code>q</code>
	 * unless there weren't enough CO2 in the atmosphere.
	 */
	public double convertCO2ToO2(double q, int x, int y) {
		return resourceManager.convertCO2ToO2(q, x, y);
	}
	/**
	 * Consume O2 from the region around the given coordinates to realize the respiration process
	 * needed to consume accumulated chemical energy. Frees the same
	 * amount of CO2 to the atmosphere than O2 consumed.
	 *
	 * @param q  The amount of O2 required.
	 * @return  The amount of O2 obtained. This is always <code>q</code>
	 */
	public double respiration(double q, int x, int y) {
		return resourceManager.convertO2ToCO2(q, x, y);
	}
	/**
	 * Decaying organisms and pink, while consuming another organism, release
	 * carbon as methane into the region around the given coordinates.
	 *
	 * @param q  The amount of O2 required.
	 * @return  The amount of O2 obtained. This is always <code>q</code>
	 */
	public double decomposition(double q, int x, int y) {
		return resourceManager.convertO2ToCH4(q, x, y);
	}
	/**
	 * Organisms using their abilities, release
	 * carbon as CO into the region around the given coordinates.
	 *
	 * @param q  The amount of O2 required.
	 * @return  The amount of O2 obtained. This is always <code>q</code>
	 */
	public double energyuse(double q, int x, int y) {
		return resourceManager.convertO2ToCO1(q, x, y);
	}
	/**
	 * Feeding organisms except pink (but also produced in some other cases) release
	 * carbon as detritus into the region around the given coordinates.
	 *
	 * @param q  The amount of O2 required.
	 * @return  The amount of O2 obtained. This is always <code>q</code>
	 */
	public double detritusproduction(double q, int x, int y) {
		return resourceManager.convertO2ToDetritus(q, x, y);
	}
	/**
	 * Consume CO2 from the region around the given coordinates to realize the photosynthesis process
	 * needed to obtain chemical energy from the Sun. Frees the same amount
	 * of O2 to the atmosphere than CO2 consumed.
	 *
	 * The CO2 obtained is calculated as follows: the total length of the
	 * organism's green segments is divided by a fixed parameter that indicates
	 * green segment effectiveness. Then, the result is multiplied by the total
	 * CO2 in the atmosphere and divided by another parameter that indicates
	 * the concentration of CO2 needed to absorb it. The result is the total
	 * amount of CO2 that the organism can get. This value can't be greater than
	 * the total amount of CO2 in the atmosphere, nor the effectiveness of the
	 * initial length.
	 *
	 * @param q  The total length of the organism's green segments.
	 * @return  The amount of CO2 obtained.
	 */
	public double photosynthesis(double q, int x, int y) {
		return resourceManager.convertCO2ToO2(q, x, y);
	}
	/**
	 * Consume CH4 from the region around the given coordinates to realize the methanotrophic process
	 * needed to obtain chemical energy. Frees the same amount
	 * of O2 to the atmosphere than CH4 consumed.
	 *
	 * The CH4 obtained is calculated as follows: the total length of the
	 * organism's purple segments is divided by a fixed parameter that indicates
	 * purple segment effectiveness. Then, the result is multiplied by the total
	 * CH4 in the atmosphere and divided by another parameter that indicates
	 * the concentration of CH4 needed to absorb it. The result is the total
	 * amount of CH4 that the organism can get. This value can't be greater than
	 * the total amount of CH4 in the atmosphere, nor the effectiveness of the
	 * initial length.
	 *
	 * @param q  The total length of the organism's purple segments.
	 * @return  The amount of CH4 obtained.
	 */
	public double methanotrophy(double q, int x, int y) {
		return resourceManager.convertCH4ToO2(q, x, y);
	}
	/**
	 * Consume CO from the region around the given coordinates for C4 only plants to realize the photosynthesis process
	 * needed to obtain chemical energy from the Sun. Frees the same amount
	 * of O2 to the atmosphere than CO consumed.
	 *
	 * The CO obtained is calculated as follows: the total length of the
	 * organism's C4 segments is divided by a fixed parameter that indicates
	 * C4 segment effectiveness. Then, the result is multiplied by the total
	 * CO in the atmosphere and divided by another parameter that indicates
	 * the concentration of CO needed to absorb it. The result is the total
	 * amount of CO that the organism can get. This value can't be greater than
	 * the total amount of CO in the atmosphere, nor the effectiveness of the
	 * initial length.
	 *
	 * @param q  The total length of the organism's C4 segments.
	 * @return  The amount of CO obtained.
	 */
	public double C4photosynthesis(double q, int x, int y) {
		return resourceManager.convertCO1ToO2(q, x, y);
	}
	/**
	 * Consume detritus from the region around the given coordinates to realize the filter feeder
	 * process needed to obtain chemical energy. Frees the same amount
	 * of O2 to the atmosphere than detritus consumed.
	 *
	 * The detritus obtained is calculated as follows: the total length of the
	 * organism's plankton segments is divided by a fixed parameter that indicates
	 * plankton segment effectiveness. Then, the result is multiplied by the total
	 * detritus in the atmosphere and divided by another parameter that indicates
	 * the concentration of detritus needed to absorb it. The result is the total
	 * amount of detritus that the organism can get. This value can't be greater than
	 * the total amount of detritus in the atmosphere, nor the effectiveness of the
	 * initial length.
	 *
	 * @param q  The total length of the organism's plankton segments.
	 * @return  The amount of detritus obtained.
	 */
	public double filterfeeding(double q, int x, int y) {
		return resourceManager.convertDetritusToO2(q, x, y);
	}
	/**
	 * Constructor of the World class. All internal structures are initialized and
	 * the world's size is obtained from parameters.
	 *
	 * @param visibleWorld  A reference to the visual representation of this world.
	 */
	public World(VisibleWorldInterface visibleWorld) {
		_visibleWorld = visibleWorld;
		_width = Utils.WORLD_WIDTH;
		_height = Utils.WORLD_HEIGHT;
		_organisms = Collections.synchronizedSet(new HashSet<>());
		inCorridors = Collections.synchronizedList(new ArrayList<InCorridor>());
		outCorridors = Collections.synchronizedList(new ArrayList<OutCorridor>());
		worldStatistics = new WorldStatistics(_visibleWorld.getMainWindow());

		Utils.addRepaintWorldChangeListener(new RepaintWorldChangeListener() {
			@Override
			public void drawWorldChanged(boolean value) {
				SwingUtilities.invokeLater(() -> _visibleWorld.repaint());
			}
		});
	}
	/**
	 * When a world object is read from a file, it must be linked with its visualization.
	 * That is what this method does.
	 *
	 * @param visibleWorld  A reference to the visual representation of this world.
	 */
	public void init(VisibleWorldInterface visibleWorld) {
		_visibleWorld = visibleWorld;
		_visibleWorld.setPreferredSize(new Dimension(getWidth(), getHeight()));
	}
	/**
	 * Populate the word with a new set of organisms.
	 * This is used to destroy a world and create a new one.
	 */
	public void genesis() {
		// Reset atributs
		nFrames = 0;
		resourceManager = new ResourceManager(
				Utils.INITIAL_O2,
				Utils.INITIAL_CO2,
				Utils.INITIAL_CH4,
				Utils.INITIAL_DETRITUS,
				Utils.INITIAL_CO1,
				Utils.WORLD_WIDTH,
				Utils.WORLD_HEIGHT);
		NEXT_ID = 0;
		NEXT_CLADE_PART = 0;
		_population = 0;
		_visibleWorld.setSelectedOrganism(null);
		_organisms.clear();
		// Initialize size
		_width = Utils.WORLD_WIDTH;
		_height = Utils.WORLD_HEIGHT;
		_visibleWorld.setPreferredSize(new Dimension(Utils.WORLD_WIDTH, Utils.WORLD_HEIGHT));
		// Create statistics
		worldStatistics = new WorldStatistics(_visibleWorld.getMainWindow());
		// Create organisms
		for (int i=0; i<Utils.INITIAL_ORGANISMS; i++) {
			Organism b = new Organism(this);
			// Only add the new organism if it can be placed in the world
			if (b.randomCreate())
				addOrganism(b,null);
		}
	}
	/**
	 * Remove all corpses from the world and return their organic matter to
	 * the atmosphere in the form of CO2.
	 */
	public synchronized void disperseAll() {
		Organism b;
		synchronized (_organisms) {
			for (Iterator<Organism> it = _organisms.iterator(); it.hasNext();) {
				b = it.next();
				if (!b.isAlive())
					b.useEnergy(b.getEnergy());
			}
		}
	}
	/**
	 * Kill all organisms in the world.
	 */
	public synchronized void killAll() {
		Organism org;
		synchronized (_organisms) {
			for (Iterator<Organism> it = _organisms.iterator(); it.hasNext();) {
				org = it.next();
				if (org.isAlive())
					org.die(null);
			}
		}
	}
	/**
	 * Draws all visible components of the world to a graphic context.
	 * This includes organisms and corridors. Called from {@link biogenesis.VisibleWorld.paintComponents}.
	 *
	 * @param g  The graphic context to draw to.
	 */
	public void draw(Graphics g, boolean fullRedraw) {
		Organism b;
		if (_corridorexists) {
			Corridor c;
			synchronized (inCorridors) {
				for (Iterator<InCorridor> it = inCorridors.iterator(); it.hasNext();) {
					c = it.next();
					c.draw(g);
				}
			}
			synchronized (outCorridors) {
				for (Iterator<OutCorridor> it = outCorridors.iterator(); it.hasNext();) {
					c = it.next();
					c.draw(g);
				}
			}
		}
		resourceManager.draw(g);
		synchronized (_organisms) {
			if (organismBuckets != null && !fullRedraw) {
				final JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, (VisibleWorld) _visibleWorld);
				final Rectangle view = viewPort.getViewRect();
				final int bucketSize = organismBuckets.getBucketSize();

				final int minx = Math.max(0, (int) (view.x / (double) bucketSize));
				final int miny = Math.max(0, (int) (view.y / (double) bucketSize));
				final int maxx = Math.min(organismBuckets.getMaxWidth(), (int) ((view.x + view.width) / (double) bucketSize));
				final int maxy = Math.min(organismBuckets.getMaxHeight(), (int) ((view.y + view.height) / (double) bucketSize));

				for (int y = miny; y <= maxy; y++) {
					for (int x = minx; x <= maxx; x++) {
						Collection<Organism> bucket = organismBuckets.getBucket(x, y);
						for (Organism o : bucket) {
							o.draw(g);
						}
					}
				}
			} else {
				for (Iterator<Organism> it = _organisms.iterator(); it.hasNext();) {
					b = it.next();
					b.draw(g);
				}
			}
		}
	}

	/**
	 * Executes a frame. This method iterates through all objects in the world
	 * and make them to execute a movement. Here is the place where all action
	 * occurs: organism movement, interaction, birth and death.
	 *
	 * Additionally, every 20 frames the {@link InfoWindow} is updated, if showed,
	 * and every 256 frames the time counter is increased by 1.
	 */
	public synchronized void time() {
		organismBuckets = new OrganismBuckets(_width, _height, 70);
		if (_corridorexists) {
			InCorridor c;
			synchronized (inCorridors) {
				for (Iterator<InCorridor> it = inCorridors.iterator(); it.hasNext();) {
					c = it.next();
					c.frame();
				}
			}
		}
		synchronized (_organisms) {
			for (Organism o: _organisms) {
				organismBuckets.insert(o);
			}
		}
		ParallelExecutor.progressAllOrganisms(_organisms, organismBuckets, _visibleWorld);

		// Reactions turning CO2 and CH4 into each other, detritus into CO, and CO into CO2
		resourceManager.reactions();
		// Diffuse resources between regions
		resourceManager.diffusion();

		if (nFrames++ % 20 == 0)
			_visibleWorld.getMainWindow().getInfoPanel().recalculate();
		if (nFrames % 256 == 0) {
			nFrames = 0;
			worldStatistics.eventTime(_population, getDistinctCladeIDCount(1), getDistinctCladeIDCount(10), getDistinctCladeIDCount(100),
				getO2(), getCO2(), getCO1(), getCH4(), getDetritus(), _organisms);
			_isbackuped = false;
			_issaved = false;
		}
	}

	/**
	 * Add a pair of biological corridors to the world.
	 * This method is called by {@link biogenesis.Connection.setState} when
	 * a new connection is stablished in order to activate the pair
	 * of corridors associated with the new connection.
	 *
	 * @param in  The corridor where organisms will arrive from another world.
	 * @param out  The corridor where organisms will leave this world.
	 */
	public void addCorridors(InCorridor in, OutCorridor out) {
		inCorridors.add(in);
		outCorridors.add(out);
		_corridorexists = true;
	}
	/**
	 * Remove a pair of biological corridors from the world.
	 * This method is called by {@link biogenesis.Connection.setState} when
	 * a connection is closed in order to remove the pair of corridors
	 * associated with the closing connection.
	 *
	 * @param in  The corridor where organisms were arriving from the other world.
	 * @param out  The corridor where organisms were leaving from this world.
	 */
	public void removeCorridors(InCorridor in, OutCorridor out) {
		inCorridors.remove(in);
		outCorridors.remove(out);
		in.width++;
		in.height++;
		out.width++;
		out.height++;
		_visibleWorld.repaint(in);
		_visibleWorld.repaint(out);
	}
	/**
	 * Checks if an organism enters an output corridor. It is considered
	 * that the organism has entered a corridor if its center is inside
	 * the corridor.
	 *
	 * @param org  The organism that is being checked.
	 * @return  The corridor that the organism is in, or null if it is not
	 * inside any corridor.
	 */
	public OutCorridor checkHitCorridor(Organism org) {
		OutCorridor c;
		synchronized (outCorridors) {
			for (Iterator<OutCorridor> it = outCorridors.iterator(); it.hasNext();) {
				c = it.next();
				if (c.contains(org._centerX, org._centerY))
					return c;
			}
		}
		return null;
	}
	/**
	 * Checks if a randomly generated or pasted organism overlaps with another organism.
	 * This is done by checking if the bounding rectangles
	 * of both organisms overlaps.
	 *
	 * @param b1  The organism that is being checked.
	 * @return  The organism which bounding rectangle is touching the bounding
	 * rectangle of {@code b1} or null if there is no such organism.
	 */
	public Organism genesisCheckHit(Organism b1) {
		Organism b;
		synchronized (_organisms) {
			for (Iterator<Organism> it = _organisms.iterator(); it.hasNext(); ) {
				b = it.next();
				if (b1 != b) {
					if (b1.intersects(b)) {
						return b1;
					}
				}
			}
		}
		return null;
	}

	public transient OrganismBuckets organismBuckets = new OrganismBuckets(_width, _height, 70);

	/**
	 * Checks if an organism has a high probability of being in touch with
	 * another organism. This is done by checking if the bounding rectangles
	 * of both organisms overlaps.
	 *
	 * @param b1  The organism that is being checked.
	 * @return  The organism which bounding rectangle is touching the bounding
	 * rectangle of {@code b1} or null if there is no such organism.
	 */
	public Organism fastCheckHit(Organism b1) {
		return organismBuckets.findFirst(b1, org -> {
			if (b1 != org) {
				if (b1.intersects(org)) {
					return true;
				}
			}

			return false;
		});
	}
	/**
	 * Used for transformations.
	 * Checks if an organism has a high probability of being in touch with
	 * another organism. This is done by checking if the bounding rectangles
	 * of both organisms overlaps.
	 *
	 * @param b1  The organism that is being checked.
	 * @return  The organism which bounding rectangle is touching the bounding
	 * rectangle of {@code b1} or null if there is no such organism.
	 */

	public Organism transformCheckHit(Organism b1) {
		return organismBuckets.findFirst(b1, org -> {
			if ((b1 != org) && (b1._ID != org._parentID) && (b1._parentID != org._ID)) {
				if (b1.intersects(org)) {
					return true;
				}
			}
			return false;
		});
	}
	/**
	 * Checks if an organism hits another organism.
	 *
	 * @param org1  The organism to check.
	 * @return  The organism that is touching {@code org1} or null if not such
	 * organism exists.
	 */
	public Organism checkHit(Organism org1) {
		return organismBuckets.findFirst(org1, collidingOrganism -> {
			if (collidingOrganism != org1) {
				// First check if the bounding boxes intersect
				if (org1.intersects(collidingOrganism)) {
					if (collidingOrganism.getEnergy() >= Utils.tol && org1.getEnergy() >= Utils.tol) {
						// Check if they are touching
						if (org1.contact(collidingOrganism)) {
							return true;
						}
					}
				}
			}

			return false;
		});
	}
	/**
	 * Adds an organism to the world. Once added, the new organism will move at every
	 * frame and interact with other organisms in the world.
	 *
	 * Updates world statistics, population and the {@link biogenesis.InfoWindow}, if necessary.
	 *
	 * @param child  The organism that needs to be added.
	 * @param parent  The parent of the added organism, or null if there is no parent.
	 */
	public void addOrganism(Organism child, Organism parent) {
		_organisms.add(child);
		if (parent == _visibleWorld.getSelectedOrganism())
			_visibleWorld.getMainWindow().getInfoPanel().changeNChildren();
		if (parent != null) {
			worldStatistics.eventOrganismBorn(child, parent);
		}
		worldStatistics.eventOrganismCreated();
		increasePopulation();
	}
	/**
	 * Informs the world of a defunction event. This will update statistics.
	 *
	 * @param dyingOrganism  The organism that has just died.
	 * @param killingOrganism  The organism that has killed the other organism, if any.
	 */
	public void organismHasDied(Organism dyingOrganism, Organism killingOrganism) {
		worldStatistics.eventOrganismDie(dyingOrganism, killingOrganism);
		if (killingOrganism == _visibleWorld.getSelectedOrganism())
			_visibleWorld.getMainWindow().getInfoPanel().changeNKills();
		if (dyingOrganism == _visibleWorld.getSelectedOrganism())
			_visibleWorld.showDeadToolbar();
	}
	/**
	 * Informs the world of an infection event. This will update statistics.
	 *
	 * @param infectedOrganism  The organism that has just been infected.
	 * @param infectingOrganism  The organism that has infected the other organism.
	 */
	public void organismHasBeenInfected(Organism infectedOrganism, Organism infectingOrganism) {
		worldStatistics.eventOrganismInfects(infectedOrganism, infectingOrganism);
		if (infectingOrganism == _visibleWorld.getSelectedOrganism())
			_visibleWorld.getMainWindow().getInfoPanel().changeNInfected();
	}
}
