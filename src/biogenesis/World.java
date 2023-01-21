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

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.index.strtree.STRtree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import java.util.Collections;
import java.io.*;
import java.awt.*;
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
	protected int _width;
	/**
	 * World height
	 */
	protected int _height;
	/**
	 * A list of the organisms in the world, even dead ones.
	 * Note that this must be a synchronized list so it is mandatory to
	 * manually synchronize when iterating over it.
	 */
	protected List<Organism> _organisms;
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
	protected int _population = 0;
	/**
	 * The next identification number that will be assigned to an organism
	 * in this world
	 */
	protected int NEXT_ID;
	/**
	 * The next identification part of the string that will be assigned to an organisms clade
	 * in this world
	 */
	protected int NEXT_CLADE_PART;
	/**
	 * A reference to the visible world part of this world used basically
	 * to indicate which parts of the world should be repainted due to
	 * events in the world.
	 */
	transient protected VisibleWorld _visibleWorld;
	/**
	 * Frame counter. 256 frames are a time unit. This value is used to count
	 * time and to trigger some window updating at regular intervals.
	 */
	private int nFrames;
	/**
	 * The amount of O2 in the atmosphere of this world.
	 */
	protected double _O2;
	/**
	 * The amount of CO2 in the atmosphere of this world.
	 */
	protected double _CO2;
	/**
	 * The amount of CH4 in the atmosphere of this world.
	 */
	protected double _CH4;
	/**
	 * Do we need to check for corridors?
	 */
	protected boolean _corridorexists;
	/**
	 * Reference to the object that keeps track of all world statistics.
	 */
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
	 * Returns a new StatisticsWindow refering to this world.
	 *
	 * @return  A newly created StatisticsWindow.
	 */
	public StatisticsWindow createStatisticsWindow() {
		return new StatisticsWindow(_visibleWorld.getMainWindow(), worldStatistics, _organisms);
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
		return NEXT_ID++;
	}
	/**
	 * Returns the next available clade part identification number.
	 *
	 * @return  A unique number used to identify part of the clade string
	 */
	public int getNewCladePart() {
		return NEXT_CLADE_PART++;
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
        return (int) _organisms
            .stream()
            .filter(o -> o.isAlive())
            .map(o -> o.getGeneticCode().getcladeID())
            .distinct()
            .count();
    }
	/**
	 * Increase the population counter by one.
	 *
	 * This method should be called every time a new organism is
	 * created. Normally, it is called by addOrganism, but in some
	 * cases it may be used directly.
	 */
	public void increasePopulation() {
		_population++;
		worldStatistics.eventPopulationIncrease(_population);
	}
	/**
	 * Decrease the population counter by one.
	 *
	 * This method should be called every time an organism dies.
	 * Normally, it is called by Organism.die or Organism.breath,
	 * but in some cases it may be used directly.
	 */
	public void decreasePopulation() {
		_population--;
		worldStatistics.eventPopulationDecrease(_population);
	}
	/**
	 * Returns the amount of O2 that exist in the atmosphere.
	 *
	 * @return  The amount of O2.
	 */
	public double getO2() {
		return _O2;
	}
	/**
	 * Returns the amount of CO2 that exist in the atmosphere.
	 *
	 * @return  The amount of CO2.
	 */
	public double getCO2() {
		return _CO2;
	}
	/**
	 * Returns the amount of CH4 that exist in the atmosphere.
	 *
	 * @return  The amount of CH4.
	 */
	public double getCH4() {
		return _CH4;
	}
	/**
	 * Add O2 to the atmosphere.
	 *
	 * @param q  The amount of O2 to add.
	 */
	public void addO2(double q) {
		_O2 += q;
	}
	/**
	 * Add CO2 to the atmosphere.
	 *
	 * @param q  The amount of CO2 to add.
	 */
	public void addCO2(double q) {
		_CO2 += q;
	}
	/**
	 * Add CH4 to the atmosphere.
	 *
	 * @param q  The amount of CH4 to add.
	 */
	public void addCH4(double q) {
		_CH4 += q;
	}
	/**
	 * Substracts O2 from the atmosphere.
	 *
	 * @param q  The amount of O2 to substract.
	 */
	public void decreaseO2(double q) {
		_O2 -= Math.min(q, _O2);
	}
	/**
	 * Substract CO2 from the atmosphere.
	 *
	 * @param q  The amount of CO2 to substract.
	 */
	public void decreaseCO2(double q) {
		_CO2 -= Math.min(q, _CO2);
	}
	/**
	 * Substract CH4 from the atmosphere.
	 *
	 * @param q  The amount of CH4 to substract.
	 */
	public void decreaseCH4(double q) {
		_CH4 -= Math.min(q, _CH4);
	}
	/**
	 * Consume O2 from the atmosphere to realize the respiration process
	 * needed to consume accumulated chemical energy. Frees the same
	 * amount of CO2 to the atmosphere than O2 consumed.
	 *
	 * @param q  The amount of O2 required.
	 * @return  The amount of O2 obtained. This is always <code>q</code>
	 * unless there weren't enough O2 in the atmosphere.
	 */
	public double respiration(double q) {
		double d = Math.min(q,_O2);
		_O2 -= d;
		_CO2 += d;
		return d;
	}
	/**
	 * Decaying organisms and pink, while consuming another organism, release
	 * carbon as methane into the atmosphere
	 *
	 * @param q  The amount of O2 required.
	 * @return  The amount of O2 obtained. This is always <code>q</code>
	 * unless there weren't enough O2 in the atmosphere.
	 */
	public double decomposition(double q) {
		double d = Math.min(q,_O2);
		_O2 -= d;
		_CH4 += d;
		return d;
	}
	/**
	 * Consume CO2 from the atmosphere to realize the photosynthesis process
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
	public double photosynthesis(double q) {
		q = Utils.min(q,q*_CO2/Utils.DRAIN_SUBS_DIVISOR,_CO2);
		_CO2 -= q;
		_O2 += q;
		return q;
	}
	/**
	 * Consume CH4 from the atmosphere to realize the methanotrophic process
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
	public double methanotrophy(double q) {
		q = Utils.min(q,q*_CH4/Utils.DRAIN_SUBS_DIVISOR,_CH4);
		_CH4 -= q;
		_O2 += q;
		return q;
	}
	/**
	 * Constructor of the World class. All internal structures are initialized and
	 * the world's size is obtained from parameters.
	 *
	 * @param visibleWorld  A reference to the visual representation of this world.
	 */
	public World(VisibleWorld visibleWorld) {
		_visibleWorld = visibleWorld;
		_width = Utils.WORLD_WIDTH;
		_height = Utils.WORLD_HEIGHT;
		_organisms = Collections.synchronizedList(new ArrayList<Organism>(Utils.ORGANISMS_VECTOR_SIZE));
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
	public void init(VisibleWorld visibleWorld) {
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
		_O2 = Utils.INITIAL_O2;
		_CO2 = Utils.INITIAL_CO2;
		_CH4 = Utils.INITIAL_CH4;
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
	public void disperseAll() {
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
	public void killAll() {
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
	public void draw(Graphics g) {
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
		synchronized (_organisms) {
			for (Iterator<Organism> it = _organisms.iterator(); it.hasNext();) {
				b = it.next();
				b.draw(g);
			}
		}
	}
	/**
	 * Determines the world's region that needs to be repainted in the associated
	 * {@link biogenesis.VisualWorld} and instructs it to do it.
	 *
	 * For optimization, only paints organisms that has moved in the last frame.
	 */
	public void setPaintingRegion() {
		if (!Utils.repaintWorld()) {
			return;
		}

		Organism b;
		if (_corridorexists) {
			Corridor c;
			synchronized (inCorridors) {
				for (Iterator<InCorridor> it = inCorridors.iterator(); it.hasNext();) {
					c = it.next();
					_visibleWorld.repaint(c);
				}
			}
			synchronized (outCorridors) {
				for (Iterator<OutCorridor> it = outCorridors.iterator(); it.hasNext();) {
					c = it.next();
					_visibleWorld.repaint(c);
					if (c.getTravellingOrganism() != null)
						_visibleWorld.repaint(c.getTravellingOrganism());
				}
			}
		}
		synchronized (_organisms) {
			for (Iterator<Organism> it = _organisms.iterator(); it.hasNext();) {
				b = it.next();
				if (b.hasMoved) {
					_visibleWorld.repaint(b.lastFrame);
					_visibleWorld.repaint(b);
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
	public void time() {
		colDetTree = new STRtree();
		int i;
		Organism b;
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
			/* We can't use an Iterator here because this list has to be changed
			 * inside Organism.move (when new organisms are born) and we need to
			 * remove organisms with no energy, so a ConcurrentModificationException
			 * will be thrown.
			 */
			for (Organism o: _organisms) {
			     colDetTree.insert(new Envelope(o.getX(), o.getMaxX(), o.getY(), o.getMaxY()), o);
		    }
			int l = _organisms.size();
			for (i=0; i<l; i++) {
				b = _organisms.get(i);
				if (!b.move()) {
					// Organism has no energy -> remove from the list
					if (Utils.repaintWorld()) {
						_visibleWorld.repaint(b);
					}
					_organisms.remove(i);
					if (_visibleWorld.getSelectedOrganism() == b)
						_visibleWorld.setSelectedOrganism(null);
					l--;
					i--;
				}
			}
		}
		// Reactions turning CO2 and CH4 into each other
		double x = Math.min(getCO2()/Utils.CO2_TO_CH4_DIVISOR,getCO2());
		_CO2 -= x;
		_CH4 += x;
		double y = Math.min(getCH4()/Utils.CH4_TO_CO2_DIVISOR,getCH4());
		_CH4 -= y;
		_CO2 += y;
		if (nFrames++ % 20 == 0)
			_visibleWorld.getMainWindow().getInfoPanel().recalculate();
		if (nFrames % 256 == 0) {
			nFrames = 0;
			worldStatistics.eventTime(_population, getDistinctCladeIDCount(), _O2, _CO2, _CH4, _organisms);
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
	/**
	 * Checks if an organism has a high probability of being in touch with
	 * another organism. This is done by checking if the bounding rectangles
	 * of both organisms overlaps.
	 *
	 * @param b1  The organism that is being checked.
	 * @return  The organism which bounding rectangle is touching the bounding
	 * rectangle of {@code b1} or null if there is no such organism.
	 */

	public STRtree colDetTree = new STRtree();

	public Organism fastCheckHit(Organism b1) {

		List<Organism> collidingOrgs = colDetTree.query(new Envelope(b1.getX(), b1.getMaxX(), b1.getY(), b1.getMaxY()));

		for (Organism org : collidingOrgs) {
			if (b1 != org) {
				if (b1.intersects(org)) {
					return b1;
				}
			}
		}
		return null;
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

		List<Organism> collidingOrgs = colDetTree.query(new Envelope(b1.getX(), b1.getMaxX(), b1.getY(), b1.getMaxY()));

		for (Organism org : collidingOrgs) {
			if ((b1 != org) && (b1._ID != org._parentID) && (b1._parentID != org._ID)) {
				if (b1.intersects(org)) {
					return b1;
				}
			}
		}
		return null;
	}
	/**
	 * Checks if an organism hits another organism.
	 *
	 * @param org1  The organism to check.
	 * @return  The organism that is touching {@code org1} or null if not such
	 * organism exists.
	 */
	public Organism checkHit(Organism org1) {
		List<Organism> collidingOrgs = colDetTree.query(new Envelope(org1.getX(), org1.getMaxX(), org1.getY(), org1.getMaxY()));

		for (Organism collidingOrganism : collidingOrgs) {
			if (collidingOrganism != org1) {
				// First check if the bounding boxes intersect
				if (org1.intersects(collidingOrganism)) {
					if (collidingOrganism.getEnergy() >= Utils.tol && org1.getEnergy() >= Utils.tol) {
						// Check if they are touching
						if (org1.contact(collidingOrganism)) {
							return org1;
						}
					}
				}
			}
		}
		return null;
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
