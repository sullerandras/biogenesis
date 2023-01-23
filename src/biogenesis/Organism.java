/* Copyright (C) 2006-2010  Joan Queralt Molina
 * Color Mod (C) 2012-2022  MarcoDBAA
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

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
/**
 * This class implements an organism.
 * The body of the organism is drawn inside the Rectangle from which it inherits.
 */
public class Organism extends Rectangle {
	/**
	 * The version of this class
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;
	/**
	 * A reference to the genetic code of this organism
	 */
	protected GeneticCode _geneticCode;
	/**
	 * If this organism has been infected by a white segment, here we have the
	 * genetic code that this organism will reproduce.
	 */
	protected GeneticCode _infectedGeneticCode = null;
	/**
	 * A reference to the saved genetic code of this organisms child, if the organism
	 * could not reproduce because there was no room.
	 */
	protected GeneticCode _savedGeneticCode = null;
	/**
	 * The symmetry used when constructing the organism. Possible values are
	 * 1 - 8.
	 */
	protected int _symmetry;
	/**
	 * Number of children that this organism will produce at once. This
	 * is the number of yellow segments in its genetic code with a
	 * maximum of 8 and a minimum of 1.
	 */
	protected int _nChildren;
	/**
	 * Number of viruses that this organism is forced to produce at once.
	 */
	protected int _nVirusChildren;
	/**
	 * Amount of indigo an organism has
	 */
	protected int _indigo;
	/**
	 * Amount of mint an organism has
	 */
	protected int _antiviral;
	/**
	 * Amount of lavender shielding an organism has
	 */
	protected int _lavender;
	/**
	 * Amount of lavender shielding an organism can create per frame
	 */
	protected int _createlavender;
	/**
	 * Amount of magenta an organism has
	 */
	protected int _healing;
	/**
	 * Amount of time an organism will spend as spore
	 */
	protected int _sporetime;
	/**
	 * Spore version
	 */
	protected int _sporeversion;
	/**
	 * Enhanced Cream version
	 */
	protected int _creamversion;
	/**
	 * Sky/Freezer version
	 */
	protected int _skyversion;
	/**
	 * Fallow version
	 */
	protected int _fallowversion;
	/**
	 * Fallow version 3 makes you reproduce dead children
	 */
	protected int _fallowinhibition;
	/**
	 * Plague version
	 */
	protected int _plagueversion;
	/**
	 * Black/Dark version
	 */
	protected int _blackversion;
	/**
	 * Summer inactivity version
	 */
	protected int _summerinactivity=-1;
	/**
	 * Total length of all yellow segments
	 */
	protected int _yellowCounter;
	/**
	 * Total length of all flower segments
	 */
	protected int _reproducelate;
	/**
	 * Total length of all golden segments
	 */
	protected int _gold;
	/**
	 * Segments maintenance costs
	 */
	protected double _maintenance;
	/**
	 * Segments methanotrophy
	 */
	protected double _methanotrophy;
	/**
	 * Segments photosynthesis
	 */
	protected double _photosynthesis;
	/**
	 * Active Forest colony photosynthesis
	 */
	protected double _colonyPhotosynthesis;
	/**
	 * Forest segments photosynthesis
	 */
	protected double _forestphoto;
	/**
	 * Leaf segments reduced photosynthesis
	 */
	protected double _leafphoto;
	/**
	 * Spin of spring segments
	 */
	protected int _spin;
	/**
	 * Indicates if this organism has jade segments and also the individual jade delay
	 */
	protected int _jadefactor;
	/**
	 * If rose segments of two organisms have the same length they are friends
	 */
	protected int _lengthfriend=-1;
	/**
	 * If rose segments of two organisms have the same theta they are friends
	 */
	protected int _thetafriend=-1;
	/**
	 * Indicates if siblings battle
	 */
	protected boolean _siblingbattle;
	/**
	 * Indicates if generations battle
	 */
	protected boolean _generationbattle;
	/**
	 * Indicates if organisms with rose segments are social towards other organisms with rose segments.
	 */
	protected boolean _social;
	/**
	 * Indicates if organisms with rose segments are peaceful towards other organisms with rose segments.
	 */
	protected boolean _peaceful;
	/**
	 * Indicates if this organism spins clockwise
	 */
	protected boolean _clockwise;
	/**
	 * Indicates if this organism is altruistic to related organisms
	 */
	protected boolean _familial;
	/**
	 * Indicates if this organism is an altruist
	 */
	protected boolean _altruist;
	/**
	 * Indicates if this organism has lower symmetry (1-3)
	 */
	protected int _lowersymmetric;
	/**
	 * Indicates if this organism is a plant
	 */
	protected boolean _isaplant;
	/**
	 * Indicates if this organism is a consumer
	 */
	protected boolean _isaconsumer;
	/**
	 * Indicates if this organism is a fungus (unmodified pink)
	 */
	protected boolean _isafungus;
	/**
	 * Indicates if this organism is a fungal consumer (modified pink)
	 */
	protected boolean _modifiespink;
	/**
	 * Indicates if this organism is modified lilac
	 */
	protected boolean _modifieslilac;
	/**
	 * Indicates if this organism is a killer
	 */
	protected boolean _isakiller;
	/**
	 * Indicates if this organism has gray segments
	 */
	protected boolean _isgray;
	/**
	 * Indicates if this organism has spike segments
	 */
	protected boolean _isspike;
	/**
	 * Indicates if this organism has movement segments
	 */
	protected boolean _canmove;
	/**
	 * Indicates if this organism has teal segments
	 */
	protected boolean _canreact;
	/**
	 * Indicates if this organism is frozen
	 */
	protected boolean _isfrozen;
	/**
	 * Indicates if all plant, sky and movement segments of this organism are frozen
	 */
	protected boolean _allfrozen;
	/**
	 * Indicates if this organism has destroyed segments.
	 */
	protected boolean _isinjured;
	/**
	 * Indicates if this organism has destroyed photosynthetic segments.
	 */
	protected boolean _isinjuredplant;
	/**
	 * Indicates if this organism has reproductive segments.
	 */
	protected boolean _isreproductive;
	/**
	 * Indicates if this organism has infectious segments.
	 */
	protected boolean _isinfectious;
	/**
	 * Indicates if this organism has coral segments.
	 */
	protected boolean _iscoral;
	/**
	 * Indicates if this organism has auburn segments.
	 */
	protected boolean _isauburn;
	/**
	 * Indicates if this organism has blond segments.
	 */
	protected boolean _isblond;
	/**
	 * Indicates if this organism has silver segments.
	 */
	protected boolean _issilver;
	/**
	 * Indicates if this organism has dark gray segments.
	 */
	protected boolean _isenhanced;
	/**
	 * Indicates if this organism has modified leaf segments
	 */
	protected boolean _modifiesleaf;
	/**
	 * Indicates if this organism has lime segments.
	 */
	protected boolean _islime;
	/**
	 * Indicates if this organism is a plant with only C4 segments.
	 */
	protected int _isonlyc4;
	/**
	 * Indicates if this organism has rose segments.
	 */
	protected boolean _transfersenergy;
	/**
	 * Indicates if this organism is near other organisms.
	 */
	protected boolean _crowded;
	/**
	 * Indicates if an organism can dodge an attack in general
	 */
	protected boolean _candodge;
	/**
	 * Indicates if an organism can dodge an attack this frame
	 */
	protected boolean _dodge;
	/**
	 * Indicates if an organism already dodged an attack
	 */
	protected boolean _hasdodged = true;
	/**
	 * Indicates if an organism has eyes.
	 */
	protected boolean _haseyes;
	/**
	 * Indicates if an organism has good vision;
	 */
	protected boolean _hasgoodvision;
	/**
	 * Indicates if an organism uses spike or dark
	 */
	protected int _segmentpretoucheffects;
	/**
	 * Indicates if an organism uses frame movement
	 */
	protected boolean _useframemovement;
	/**
	 * Indicates if an organism uses extra frame effects
	 */
	protected boolean _useextraeffects;
	/**
	 * Indicates if an organism uses pretouch effects
	 */
	protected boolean _usepretoucheffects;
	/**
	 * Indicates if an organism uses touchfriend effects
	 */
	protected int _usefriendeffects;
	/**
	 * Takes a note that the static segment effects needs to be updated
	 */
	protected int _updateEffects;
	/**
	 * Reference to the world where the organism lives.
	 */
	protected World _world;
	/**
	 * Reference to the visual part of the world where the organism lives.
	 */
	transient protected VisibleWorld _visibleWorld;
	/**
	 * Identification number of this organism's parent.
	 */
	protected int _parentID;
	/**
	 * Identification number of this organism.
	 */
	protected int _ID;
	/**
	 * String to check already used positions for a child
	 */
	protected String _nPosition;
	/**
	 * Number of children it has produced.
	 */
	protected int _nTotalChildren=0;
	/**
	 * Number of organism that has killed
	 */
	protected int _nTotalKills=0;
	/**
	 * Number of organism that has infected
	 */
	protected int _nTotalInfected=0;
	/**
	 * X coordinates of the starting point of each organism's segments.
	 */
	protected int[] _startPointX;
	/**
	 * Y coordinates of the starting point of each organism's segments.
	 */
	protected int[] _startPointY;
	/**
	 * X coordinates of the ending point of each organism's segments.
	 */
	protected int[] _endPointX;
	/**
	 * Y coordinates of the ending point of each organism's segments.
	 */
	protected int[] _endPointY;
	/**
	 * Precalculated modulus of each segment.
	 */
	protected double[] _m;
	/**
	 * Precalculated modulus of each segment, divided by symmetry, used for photosynthesis.
	 * Also used for touch effects and moving segments now, because of performance reasons.
	 */
	protected double[] _mphoto;
	/**
	 * X coordinate of this organim's center of gravity.
	 */
	protected int _centerX;
	/**
	 * Y coordinate of this organim's center of gravity.
	 */
	protected int _centerY;
	/**
	 * Like _centerX but with double precision to be able to make movements slower than a pixel.
	 */
	protected double _dCenterX;
	/**
	 * Like _centerY but with double precision to be able to make movements slower than a pixel.
	 */
	protected double _dCenterY;
	/**
	 * Effective segment colors, taken from the genetic code if alive or brown if dead.
	 */
	protected Color[] _segColor;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segredReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _seggreenReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segblueReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segplagueReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segscourgeReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segwhiteReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _seggrayReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segdefaultReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segconsumerReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segplantReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segmagentaReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segpinkReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segcoralReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segorangeReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segbarkReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segvioletReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segvirusReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segmaroonReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segoliveReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segmintReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segcreamReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segspikeReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segfallowReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _seglightblueReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segochreReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segskyReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _seglilacReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segsilverReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segfireReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _seglightbrownReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _seggreenbrownReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segbrownReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segiceReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segbrokenReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segsickReaction;
	/**
	 * Effective segment reactions, taken from the genetic code if alive or brown if dead.
	 */
	protected int[] _segfriendReaction;
	/**
	 * The total number of segments of the organism
	 */
	protected int _segments;
	/**
	 * Growth ratio of the organism. Used to calculate segments when the organism is not
	 * fully grown.
	 */
	protected int _growthRatio;
	/**
	 * Total mass of this organism. The mass is calculated as the sum of all segment lengths.
	 * Used to calculate the effect of collisions.
	 */
	protected double _mass = 0;
	/**
	 * Moment of inertia of this organism, used to calculate the effect of collisions.
	 */
	protected double _I = 0;
	/**
	 * Chemical energy stored by this organism
	 */
	protected double _energy;
	/**
	 * Organism size independent on its position in the world.
	 * Let p be a point in the organism. Then, p.x + x - _sizeRect.x is the x coordinate
	 * of p representation in the world.
	 */
	protected Rectangle _sizeRect = new Rectangle();
	/**
	 * Rotation angle that this organism has at a given moment.
	 */
	protected double _theta;
	/**
	 * Last frame angle, used to avoid calculating point rotations when the angle doesn't
	 * change between two consecutive frames.
	 */
	protected double _lastTheta = -1;
	/**
	 * Rotated segments of the last frame, to use when _theta == _lastTheta
	 */
	protected int x1[],y1[],x2[],y2[];
	/**
	 * Speed. Variation applied to organism coordinates at every frame.
	 */
	protected double dx=0d, dy=0d;
	/**
	 * Angular speed. Organism angle variation at every frame.
	 */
	protected double dtheta = 0d;
	/**
	 * Minimum required energy to reproduce this genetic code of an organism with blond segments.
	 * More genes mean, that more energy is needed.
	 */
	protected int _earlyReproduceEnergy;
	/**
	 * Minimum required energy to reproduce this genetic code.
	 * More genes mean, that more energy is needed.
	 */
	protected int _reproduceEnergy;
	/**
	 * Minimum required energy to reproduce this infected genetic code.
	 * More genes mean, that more energy is needed.
	 */
	protected int _infectedReproduceEnergy;
	/**
	 * Number of frames of life of this organism
	 */
	protected int _age=0;
	/**
	 * The maximum time that the organism can be alive.
	 */
	protected int _max_age;
	/**
	 * Color used to draw the organism when a collision occurs. We save the color that
	 * should be shown and the number of frames that it should be shown. If the number
	 * if frames is 0, each segment is shown in its color.
	 */
	protected Color _color;
	/**
	 * Number of frames in which the organism will be drawn in _color.
	 */
	protected int _framesColor = 0;
	/**
	 * Number of frame that need to pass between two reproductions, even they are not
	 * successfully.
	 */
	protected int _timeToReproduce = 0;
	/**
	 * Maximum number of frames that need to pass between two reproductions, directly
	 * after a reproduction attempt, even if it was not successful.
	 */
	protected int _timeToReproduceMax = 0;
	/**
	 * Indicates if the organism has grown at the last frame. If it has grown it is
	 * necessary to recalculate its segments.
	 */
	protected int hasGrown;
	/**
	 * Indicates if it has moved at the last frame. If it has moved it is necessary
	 * to repaint it.
	 */
	protected boolean hasMoved = true;
	/**
	 * The place that this organism occupies at the last frame. If the organism
	 * moves, this rectangle must be painted too.
	 */
	protected Rectangle lastFrame = new Rectangle();
	/**
	 * Indicates if the organism is alive.
	 */
	protected boolean alive = true;
	/**
	 * Indicates if the organism is active.
	 */
	protected boolean active = true;
	private static transient Vector2D v = new Vector2D();
	/**
	 * Returns true if this organism is alive, false otherwise.
	 *
	 * @return  true if this organism is alive, false otherwise.
	 */
	public boolean isAlive() {
		return alive;
	}
	/**
	 * Returns the amount of chemical energy stored by this organism.
	 *
	 * @return  The amount of chemical energy stored by this organism.
	 */
	public double getEnergy() {
		return _energy;
	}
	/**
	 * Returns the identification number of this organism.
	 *
	 * @return  The identification number of this organism.
	 */
	public int getID() {
		return _ID;
	}
	/**
	 * Returns the identification number of this organism's parent.
	 *
	 * @return  The identification number of this organism's parent.
	 */
	public int getParentID() {
		return _parentID;
	}
	/**
	 * Returns the energy needed to replicate this genetic code.
	 * This energy is equal to 40 plus 3 for each segment.
	 *
	 * @return  the energy needed to replicate this genetic code.
	 */
	public int getReproduceEnergy() {
		return _reproduceEnergy;
	}
	/**
	 * Returns the age of this organism.
	 *
	 * @return  The age of this organism, in number of frames.
	 */
	public int getAge() {
		return _age;
	}
	/**
	 * Returns the maximum age that the organism can be.
	 *
	 * @return  The maximum age that the organism can be.
	 */
	public int getMaxAge() {
		return _max_age;
	}
	/**
	 * Returns the number of children that this organism produced.
	 *
	 * @return  The number of children that this organism produced.
	 */
	public int getTotalChildren() {
		return _nTotalChildren;
	}
	/**
	 * Returns the number of organisms killed by this organism.
	 *
	 * @return  The number of organisms killed by this organism.
	 */
	public int getTotalKills() {
		return _nTotalKills;
	}
	/**
	 * Returns the number of organisms infected by this organism.
	 *
	 * @return  The number of organisms infected by this organism.
	 */
	public int getTotalInfected() {
		return _nTotalInfected;
	}
	/**
	 * Returns a reference to this organism's genetic code.
	 *
	 * @return  A reference to this organism's genetic code.
	 */
	public GeneticCode getGeneticCode() {
		return _geneticCode;
	}
	/**
	 * Returns the total mass of this organism.
	 *
	 * @return  The total mass of this organism calculated as the sum
	 * of all its segments length.
	 */
	public double getMass() {
		return _mass;
	}
	/**
	 * Basic constructor. Doesn't initialize it: use {@link randomCreate}
	 * or {@link inherit} to do this.
	 *
	 * @param world  A reference to the world where this organism is in.
	 */
	public Organism(World world) {
		_world = world;
		_visibleWorld = world._visibleWorld;
		_theta = Utils.random.nextDouble() * Math.PI * 2d;
	}
	/**
	 * Construct an organism with a given genetic code. Doesn't initialize it:
	 * use {@link pasteOrganism} to do it. Use {@link World.addOrganism} to add
	 * it to the world.
	 *
	 * @param world  A reference to the world where this organism is in.
	 * @param geneticCode  A reference to the genetic code of this organism.
	 */
	public Organism(World world, GeneticCode geneticCode) {
		_world = world;
		_visibleWorld = world._visibleWorld;
		_theta = Utils.random.nextDouble() * Math.PI * 2d;
		_geneticCode = geneticCode;
	}
	/**
	 * Creates all data structures of this organism. Must be used after the organism
	 * has a genetic code assigned.
	 */
	protected void create() {
		_symmetry = _geneticCode.getSymmetry();
		_segments = _geneticCode.getNGenes() * _symmetry;
		_segColor = new Color[_segments];
		_startPointX = new int[_segments];
		_startPointY = new int[_segments];
		_endPointX = new int[_segments];
		_endPointY = new int[_segments];
		_m = new double[_segments];
		_mphoto = new double[_segments];
		x1 = new int[_segments];
		y1 = new int[_segments];
		x2 = new int[_segments];
		y2 = new int[_segments];
		_segredReaction = new int[_segments];
		_seggreenReaction = new int[_segments];
		_segblueReaction = new int[_segments];
		_segplagueReaction = new int[_segments];
		_segscourgeReaction = new int[_segments];
		_segwhiteReaction = new int[_segments];
		_seggrayReaction = new int[_segments];
		_segsilverReaction = new int[_segments];
		_segdefaultReaction = new int[_segments];
		_segmagentaReaction = new int[_segments];
		_segpinkReaction = new int[_segments];
		_segcoralReaction = new int[_segments];
		_segorangeReaction = new int[_segments];
		_segbarkReaction = new int[_segments];
		_segvioletReaction = new int[_segments];
		_segvirusReaction = new int[_segments];
		_segmaroonReaction = new int[_segments];
		_segoliveReaction = new int[_segments];
		_segmintReaction = new int[_segments];
		_segcreamReaction = new int[_segments];
		_segspikeReaction = new int[_segments];
		_seglightblueReaction = new int[_segments];
		_segochreReaction = new int[_segments];
		_seglightbrownReaction = new int[_segments];
		_segbrownReaction = new int[_segments];
		_segsickReaction = new int[_segments];
		_segskyReaction = new int[_segments];
		_seglilacReaction = new int[_segments];
		_segiceReaction = new int[_segments];
		_segbrokenReaction = new int[_segments];
		_segfireReaction = new int[_segments];
		_segfriendReaction = new int[_segments];
		_seggreenbrownReaction = new int[_segments];
		_segfallowReaction = new int[_segments];
		_segconsumerReaction = new int[_segments];
		_segplantReaction = new int[_segments];
		for (int i = 0; i < _segments; i++) {
			_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
            if (_segColor[i].equals(Utils.ColorEYE)) {
            	_haseyes =true;
            	_mphoto[i] = -13;
			}
			_segredReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getredReaction();
			_seggreenReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getgreenReaction();
			_segblueReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getblueReaction();
			_segplagueReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getplagueReaction();
			_segscourgeReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getscourgeReaction();
			_segwhiteReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getwhiteReaction();
			_seggrayReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getgrayReaction();
			_segdefaultReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getdefaultReaction();
			_segmagentaReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getmagentaReaction();
			_segpinkReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getpinkReaction();
			_segcoralReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getcoralReaction();
			_segorangeReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getorangeReaction();
			_segbarkReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getbarkReaction();
			_segvioletReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getvioletReaction();
			_segvirusReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getvirusReaction();
			_segmaroonReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getmaroonReaction();
			_segoliveReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getoliveReaction();
			_segmintReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getmintReaction();
			_segcreamReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getcreamReaction();
			_segspikeReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getspikeReaction();
			_seglightblueReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getlightblueReaction();
			_segochreReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getochreReaction();
			_seglightbrownReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getlightbrownReaction();
			_segbrownReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getbrownReaction();
			_segsickReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getsickReaction();
			_segskyReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getskyReaction();
			_seglilacReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getlilacReaction();
			_segiceReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).geticeReaction();
			_segbrokenReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getbrokenReaction();
			_segsilverReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getsilverReaction();
			_segfireReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getfireReaction();
			_segfriendReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getfriendReaction();
			_seggreenbrownReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getgreenbrownReaction();
			_segfallowReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getfallowReaction();
			_segconsumerReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getconsumerReaction();
			_segplantReaction[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getplantReaction();
		}
	}
	/*
	 * Restores and removes jade, sky and spring spin
	 */
	final void segmentsCheckPlant() {
		int i;
		_spin = 0;
		if (_jadefactor > 0) {
			_jadefactor = 1;
		}
		for (i=_segments-1; i>=0; i--) {
			// Manteniment
			switch (getTypeColor(_segColor[i])) {
			case JADE:
				_jadefactor = 2;
				break;
			case DARKJADE:
				_jadefactor = 2;
				break;
			case SPRING:
				_spin += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				break;
			case DARKGREEN:
				if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorJADE)) {
					_jadefactor = 2;
				}
				if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorSPRING)) {
					_spin += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				}
				break;
			}
		}
		// Calculate jade delay used in restoration of the color
		if (_jadefactor > 1) {
			if (_symmetry != 1) {
				_jadefactor = Utils.DARKJADE_DELAY * _symmetry;
			} else {
				_jadefactor = Utils.DARKJADE_DELAY * 2;
			}
		}
	}
	/*
	 * Restores and removes segment dependent abilities
	 */
	final void segmentsRestoreEffects() {
		int i;
		_spin = 0;
		if (_jadefactor > 0) {
			_jadefactor = 1;
		}
		_healing = 0;
		_antiviral = 0;
		_createlavender = 0;
		_yellowCounter = 0;
		double fertility = 0;
		_indigo = 0;
		_earlyReproduceEnergy = 0;
		if (_reproducelate > 0) {
			_reproducelate = 0;
			_reproduceEnergy = (40 + 3 * _segments);
		}
		if (_gold > 0) {
			_gold = 0;
			_max_age = Utils.MAX_AGE + (_segments/Utils.AGE_DIVISOR);
		}
		_isreproductive =false;
		if (_skyversion > 0) {
			_skyversion = -1;
		}
		if (_isauburn) {
			if ((_isaconsumer) || (_isaplant) || (_isafungus)) {
				_isauburn =false;
			}
		}
		_isinjuredplant =false;
		for (i=_segments-1; i>=0; i--) {
			// Manteniment
			switch (getTypeColor(_segColor[i])) {
			case JADE:
				_jadefactor = 2;
				break;
			case DARKJADE:
				_jadefactor = 2;
				break;
			case SPRING:
				_spin += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				break;
			case DARKGREEN:
				if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorJADE)) {
					_jadefactor = 2;
				}
				if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorSPRING)) {
					_spin += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				}
				break;
			case TEAL:
				_canmove =true;
				_canreact =true;
				if (_geneticCode.getPassive() == false) {
					_useframemovement =true;
					_mphoto[i] = -22;
				} else {
					_mphoto[i] = -21;
				}
				break;
			case CYAN:
				_canmove =true;
				_useframemovement =true;
				_mphoto[i] = -23;
				break;
			case RED:
			case FIRE:
			case ORANGE:
			case CREAM:
			case SILVER:
			case OCHRE:
			case SPIKEPOINT:
			case GRAY:
				_mphoto[i] = -1;
				break;
			case SPIKE:
				_mphoto[i] = -0.5;
				break;
			case MAROON:
				_mphoto[i] = -1;
				break;
			case PINK:
				_mphoto[i] = -1;
				break;
			case WHITE:
			case PLAGUE:
			case FRUIT:
			case LILAC:
				_mphoto[i] = -4;
				break;
			case CORAL:
			case VIOLET:
			case OLIVE:
				_mphoto[i] = -4;
				break;
			case FALLOW:
				_mphoto[i] = -4;
				break;
			case DARKOLIVE:
			case DARKLILAC:
				_mphoto[i] = -20;
				break;
			case BLUE:
				_mphoto[i] = -0.1;
				break;
			case DARK:
				if (_geneticCode.getModifiesblack() <= 2) {
					_mphoto[i] = -8;
				} else {
					if (_geneticCode.getAdaptblack() == 24) {
						_mphoto[i] = -8;
					} else {
						_mphoto[i] = -9;
					}
				}
				break;
			case DARKGRAY:
				_mphoto[i] = -0.2;
				break;
			case SPORE:
				if (_geneticCode.getModifiesspore() <= 6) {
					_isreproductive =true;
					if (_geneticCode.getModifiesspore() <= 2) {
						_earlyReproduceEnergy += 20 * _geneticCode.getNGenes();
					}
				}
				_mphoto[i] = 0;
				break;
			case BLOND:
				_earlyReproduceEnergy += 30 + (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				_mphoto[i] = -0.5;
				break;
			case FLOWER:
				_reproducelate += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				_mphoto[i] = 0;
				break;
			case GOLD:
				_gold += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				_mphoto[i] = 0;
				break;
			case AUBURN:
				_isauburn =true;
				_isreproductive =true;
				_mphoto[i] = 0;
				break;
			case YELLOW:
				_yellowCounter++;
				if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
					fertility += _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				} else {
					fertility += (2/3) * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				}
				if ((_isinfectious) && (!_isaconsumer) && (!_isaplant) && (!_isafungus)) {
					_mphoto[i] = -4;
				} else {
					_mphoto[i] = -0.5;
				}
				break;
			case INDIGO:
				if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
					_indigo += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				} else {
					_indigo += (int)((20/3) * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				}
				_mphoto[i] = 0;
				break;
			case LAVENDER:
				_createlavender += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				if (_altruist) {
					_mphoto[i] = -4;
				} else {
					_mphoto[i] = -5;
				}
				break;
			case MINT:
				if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
					_antiviral += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				} else {
					_antiviral += (int)((20/3) * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				}
				_mphoto[i] = -4;
				break;
			case MAGENTA:
				if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
					_healing += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				} else {
					_healing += (int)((20/3) * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				}
				if (_altruist) {
					_mphoto[i] = -4;
				} else {
					_mphoto[i] = -0.5;
				}
				break;
			case ROSE:
				if (_altruist) {
					_mphoto[i] = -4;
				} else {
					_mphoto[i] = -5;
				}
				break;
			case SKY:
				if (_skyversion <= 0) {
					if (_geneticCode.getModifiessky()) {
						_skyversion = 2;
					} else {
						_skyversion = 1;
					}
				}
				_mphoto[i] = -4;
			case DEEPSKY:
				if (_skyversion <= 0) {
					if (_geneticCode.getModifiessky()) {
						_skyversion = 2;
					} else {
						_skyversion = 1;
					}
				}
				_mphoto[i] = -20;
				break;
			case VISION:
				_hasgoodvision =true;
				_mphoto[i] = 0;
				break;
			case LIGHTBROWN:
				_isinjured =true;
				break;
			case GREENBROWN:
				if (_mphoto[i] > 0) {
					_isinjuredplant =true;
				}
				_isinjured =true;
				break;
			case DARKFIRE:
				_isinjured =true;
				break;
			case LIGHT_BLUE:
				_isinjured =true;
				break;
			case BROKEN:
				_isinjured =true;
				break;
			case ICE:
				if (_mphoto[i] > 0) {
					_isinjuredplant =true;
				}
				_isfrozen =true;
				_isinjured =true;
				break;
			case DEADBARK:
				_isfrozen =true;
				break;
			default:
				if ((_mphoto[i] <= -20) && (_mphoto[i] >= -20.5)) {
					_mphoto[i] = 0;
				}
				break;
		    }
		}
		// Can this organism dodge?
		if (_canreact) {
			if ((!_isaconsumer) && (!_isafungus) && (!_isakiller) && (_skyversion == 0)
				&& (((!_isinfectious) && (!_iscoral) && (_plagueversion == 0)) || ((!_isaplant) && (_methanotrophy == 0) && (_geneticCode.getPassive())))) {
				_candodge =true;
			}
		}
		// Calculate jade delay used in restoration of the color
		if (_jadefactor > 1) {
			if (_symmetry != 1) {
				_jadefactor = Utils.DARKJADE_DELAY * _symmetry;
			} else {
				_jadefactor = Utils.DARKJADE_DELAY * 2;
			}
		}
		// Calculate the total shield production capacity of lavender segments
		if (_createlavender > 0) {
			_createlavender = _createlavender / _symmetry;
		}
		// Calculate extra children for yellow
		if (_yellowCounter > 0) {
			_isreproductive =true;
			if (_isonlyc4 == 2) {
				_yellowCounter = (_yellowCounter / _symmetry) + (int) (fertility / 17);
			} else {
				_yellowCounter = (_yellowCounter / _symmetry) + (int) (fertility / 23);
			}
		}
		// Calculate indigo
		if (_indigo > 0) {
			_isreproductive =true;
			_indigo = (int) (_indigo/Utils.INDIGO_DIVISOR);
		}
		// Calculate reproduction energy for blond segments
		if (_earlyReproduceEnergy > 0) {
			_earlyReproduceEnergy = (int) Math.round(_earlyReproduceEnergy/10);
			_isreproductive =true;
			if (_isaplant) {
			    _earlyReproduceEnergy = _reproduceEnergy - _earlyReproduceEnergy;
			    if ((_isonlyc4 == 2) && (_isblond) && (!_isenhanced)) {
			    	if (_earlyReproduceEnergy < 30) {
						_earlyReproduceEnergy = 30;
					}
			    } else {
			    	if (_earlyReproduceEnergy < 40) {
						_earlyReproduceEnergy = 40;
					}
			    }
			} else {
				if ((_isaconsumer) || (_isafungus)) {
					_earlyReproduceEnergy = _reproduceEnergy - _earlyReproduceEnergy;
					if (_earlyReproduceEnergy < 21) {
						_earlyReproduceEnergy = 21;
					}
				} else {
					if ((_transfersenergy) && (_isinfectious) && (_isblond)) {
						_earlyReproduceEnergy = _reproduceEnergy - _earlyReproduceEnergy;
						if (_earlyReproduceEnergy < 21) {
							_earlyReproduceEnergy = 21;
						}
					} else {
						if ((_sporeversion < 0) || (_sporeversion == 6)) {
							_earlyReproduceEnergy = _reproduceEnergy - _earlyReproduceEnergy;
							if (_earlyReproduceEnergy < 21) {
								_earlyReproduceEnergy = 21;
							}
						} else {
							_earlyReproduceEnergy = _reproduceEnergy - _earlyReproduceEnergy;
							if (_earlyReproduceEnergy < 14) {
								_earlyReproduceEnergy = 14;
							}
						}
					}
				}
		    }
		}
		// Calculate reproduction energy for flower segments
		if (_reproducelate > 0) {
			_reproducelate = (int) Math.round(_reproducelate/10);
			if (_isinfectious) {
				_reproduceEnergy = (40 + 3 * _segments) + (int)(_reproducelate*Utils.scale[_growthRatio-1]);
			} else {
				if ((_reproducelate - (3 * _symmetry)) > 0) {
					_reproduceEnergy = (40 + 3 * _segments) + _reproducelate - (3 * _symmetry);
				}
			}
		}
		// Calculate life expectancy with gold segments
		if (_gold > 0) {
			_max_age = Utils.MAX_AGE + (_segments/Utils.AGE_DIVISOR) + (int) Math.round((_gold/10)/Utils.GOLD_DIVISOR);
			_gold = 1;
		}
	}
	/*
	 * Creates segment dependent booleans
	 */
	final void segmentsCreateEffects() {
		int i;
		_spin = 0;
		_healing = 0;
		_antiviral = 0;
		_createlavender = 0;
		_yellowCounter = 0;
		double fertility = 0;
		_indigo = 0;
		_earlyReproduceEnergy = 0;
		if (_reproducelate > 0) {
			_reproducelate = 0;
		}
		_gold = 0;
		_haseyes =false;
		boolean enhancedconsumer =false;
		boolean ispink =false;
		int isprotective = 0;
		_updateEffects = 2;
		if (_geneticCode.getSiblingBattle()) {
			_siblingbattle =true;
		}
		if (_geneticCode.getGenerationBattle()) {
			_generationbattle =true;
		}
		if (_geneticCode.getPeaceful()) {
			_peaceful =true;
		}
		if (_geneticCode.getSocial()) {
			_social =true;
		}
		if (_geneticCode.getFamilial()) {
			_familial =true;
		}
		if (_geneticCode.getAltruist()) {
			_altruist =true;
		}
		double photofactor = 0;
		if (_symmetry == 8) {
			if (_geneticCode.getNGenes() != 1) {
				photofactor = 1961 + Math.round(6000 / ((double)_geneticCode.getNGenes() + 2)) + Math.round(6315 / (double)_symmetry);
			} else {
				photofactor = 1961 + 2200 + Math.round(6315 / (double)_symmetry);
			}
		} else {
			if (_geneticCode.getNGenes() != 1) {
				photofactor = 1961.329 + Math.round(6000 / ((double)_geneticCode.getNGenes() + 2)) + Math.round(6315 / (double)_symmetry);
			} else {
				photofactor = 1961.329 + 2200 + Math.round(6315 / (double)_symmetry);
			}
		}
		double photomultiplier = (photofactor * 0.0006) / Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
		for (i=_segments-1; i>=0; i--) {
			// Manteniment
			switch (getTypeColor(_segColor[i])) {
			case GREEN:
				_mphoto[i] = Utils.GREEN_ENERGY_CONSUMPTION * photomultiplier * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				_isaplant =true;
				break;
			case GRASS:
				_mphoto[i] = Utils.GRASS_ENERGY_CONSUMPTION * photomultiplier * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				_isaplant =true;
				break;
			case BARK:
				_mphoto[i] = Utils.BARK_ENERGY_CONSUMPTION * photomultiplier * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				_isaplant =true;
				break;
			case OLDBARK:
				_mphoto[i] = -0.5;
				break;
			case SUMMER:
			case WINTER:
				_mphoto[i] = Utils.SUMMER_ENERGY_CONSUMPTION * photomultiplier * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				_isaplant =true;
				if (_summerinactivity == -1) {
					if (_geneticCode.getActivity() == 0) {
						_summerinactivity = 0;
					} else {
						if (_geneticCode.getActivity() == 1) {
							_summerinactivity = 1;
						} else {
							_summerinactivity = 2;
						}
					}
				}
				break;
			case LEAF:
				if (_geneticCode.getModifiesleaf()) {
					_mphoto[i] = Utils.MODLEAF_ENERGY_CONSUMPTION * photomultiplier * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
					_modifiesleaf =true;
				} else {
					_mphoto[i] = Utils.LEAF_ENERGY_CONSUMPTION * photomultiplier * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				}
				_isaplant =true;
				break;
			case FOREST:
				_mphoto[i] = Utils.FOREST_ENERGY_CONSUMPTION * photomultiplier * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				_isaplant =true;
				_forestphoto = 1;
				if (_segmentpretoucheffects == 0) {
					_segmentpretoucheffects = 1;
				}
				_usepretoucheffects =true;
				break;
			case LIME:
				_mphoto[i] = Utils.LIME_ENERGY_CONSUMPTION * photomultiplier * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				_isaplant =true;
				_islime =true;
				break;
			case JADE:
			case DARKJADE:
				_mphoto[i] = Utils.JADE_ENERGY_CONSUMPTION * photomultiplier * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				_isaplant =true;
				_jadefactor = 2;
				break;
			case C4:
				_mphoto[i] = Utils.C4_ENERGY_CONSUMPTION * photomultiplier * (10 + _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				_isonlyc4 = 1;
				break;
			case SPRING:
				_mphoto[i] = Utils.SPRING_ENERGY_CONSUMPTION * photomultiplier * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				_spin += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				if (_geneticCode.getClockwise()) {
					_clockwise =true;
				}
				_isaplant =true;
				break;
			case DARKGREEN:
				if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorJADE)) {
					_jadefactor = 2;
				}
				if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorSPRING)) {
					_spin += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				}
				break;
			case PURPLE:
				if (_age == 0) {
					_mphoto[i] = Utils.PURPLE_ENERGY_CONSUMPTION * photomultiplier * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				}
				_methanotrophy = 1;
				break;
			case TEAL:
				_canmove =true;
				_canreact =true;
				if (_geneticCode.getPassive() == false) {
					_useframemovement =true;
					_mphoto[i] = -22;
				} else {
					_mphoto[i] = -21;
				}
				break;
			case CYAN:
				_canmove =true;
				_useframemovement =true;
				_mphoto[i] = -23;
				break;
			case RED:
			case FIRE:
			case ORANGE:
				_isaconsumer =true;
				_mphoto[i] = -1;
				break;
			case CREAM:
				if (_creamversion == 0) {
					if (_geneticCode.getModifiescream() == 1) {
						_creamversion = 1;
					} else {
						if (_geneticCode.getModifiescream() == 2) {
							_creamversion = 2;
						} else {
							if (_geneticCode.getModifiescream() == 3) {
								_creamversion = 3;
							}
						}
					}
				}
				_isaconsumer =true;
				_mphoto[i] = -1;
				break;
			case MAROON:
				_isaconsumer =true;
				_mphoto[i] = -1;
				break;
			case PINK:
				ispink =true;
				_isafungus =true;
				if (_geneticCode.getModifiespink()) {
					_modifiespink =true;
					_isaconsumer =true;
				}
				_mphoto[i] = -1;
				break;
			case AUBURN:
				_isauburn =true;
				_isreproductive =true;
				_mphoto[i] = 0;
				break;
			case YELLOW:
				_yellowCounter++;
				if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
					fertility += _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				} else {
					fertility += (2/3) * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength();
				}
				_mphoto[i] = -0.5;
				break;
			case INDIGO:
				if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
					_indigo += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				} else {
					_indigo += (int)((20/3) * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				}
				_mphoto[i] = 0;
				break;
			case SILVER:
				enhancedconsumer =true;
				_issilver =true;
				_mphoto[i] = -1;
				break;
			case WHITE:
				_isinfectious =true;
				_mphoto[i] = -4;
				break;
			case FRUIT:
				_mphoto[i] = -4;
				break;
			case PLAGUE:
				if (_plagueversion == 0) {
					if (!_geneticCode.getPlague()) {
						_plagueversion = 1;
					} else {
						_plagueversion = 2;
					}
				}
				_mphoto[i] = -4;
				break;
			case CORAL:
				_iscoral =true;
				_mphoto[i] = -4;
				break;
			case FALLOW:
				if (isprotective <= 2) {
					isprotective = 3;
				}
				if (_fallowversion == 0) {
					if (_geneticCode.getModifiesfallow() == 1) {
						_fallowversion = 1;
					} else {
						if (_geneticCode.getModifiesfallow() == 2) {
							_fallowversion = 2;
						} else {
							if (_geneticCode.getModifiesfallow() == 3) {
								_fallowversion = 3;
							} else {
								_fallowversion = 4;
							}
						}
					}
				}
				_mphoto[i] = -4;
				break;
			case DARKGRAY:
				_isenhanced =true;
				_mphoto[i] = -0.2;
				break;
			case EYE:
				_haseyes =true;
				_mphoto[i] = -13;
				break;
			case VISION:
				_hasgoodvision =true;
				_mphoto[i] = 0;
				break;
			case GOLD:
				_gold += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				_mphoto[i] = 0;
				break;
			case FLOWER:
				_reproducelate += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				_mphoto[i] = 0;
				break;
			case BLOND:
				_isblond =true;
				_earlyReproduceEnergy += 30 + (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				_mphoto[i] = -0.5;
				break;
			case SPORE:
				if (_geneticCode.getModifiesspore() <= 6) {
					_isreproductive =true;
					if (_geneticCode.getModifiesspore() <= 2) {
						_earlyReproduceEnergy += 20 * _geneticCode.getNGenes();
					}
				}
				if (_age == 0) {
					_sporetime += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				}
				_mphoto[i] = 0;
				break;
			case LAVENDER:
				_createlavender += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				if (_altruist) {
					_mphoto[i] = -4;
				} else {
					_mphoto[i] = -5;
				}
				_usefriendeffects = 2;
				break;
			case MINT:
				if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
					_antiviral += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				} else {
					_antiviral += (int)((20/3) * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				}
				_mphoto[i] = -4;
				_usefriendeffects = 2;
				break;
			case MAGENTA:
				if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
					_healing += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				} else {
					_healing += (int)((20/3) * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				}
				if (_altruist) {
					_mphoto[i] = -4;
				} else {
					_mphoto[i] = -0.5;
				}
				_usefriendeffects = 2;
				break;
			case ROSE:
				if (_transfersenergy == false) {
    				_transfersenergy =true;
    			    _lengthfriend = (int)(_geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
    			    _thetafriend = (int)(_geneticCode.getGene(i%_geneticCode.getNGenes()).getTheta());
    			}
				if (_altruist) {
					_mphoto[i] = -4;
				} else {
					_mphoto[i] = -5;
				}
				if (_usefriendeffects < 1) {
					_usefriendeffects = 1;
				}
				break;
			case VIOLET:
				if (isprotective == 0) {
					isprotective = 1;
				}
				_mphoto[i] = -4;
				break;
			case OCHRE:
				if (isprotective <= 1) {
					isprotective = 2;
				}
				_mphoto[i] = -1;
				break;
			case OLIVE:
				isprotective = 4;
				_mphoto[i] = -4;
				break;
			case DARKOLIVE:
				isprotective = 4;
				_mphoto[i] = -20;
				break;
			case BLUE:
				if (isprotective <= 2) {
					isprotective = 3;
				}
				_mphoto[i] = -0.1;
				break;
			case SKY:
				if (isprotective <= 2) {
					isprotective = 3;
				}
				if (_skyversion <= 0) {
					if (_geneticCode.getModifiessky()) {
						_skyversion = 2;
					} else {
						_skyversion = 1;
					}
				}
				_mphoto[i] = -4;
				break;
			case DEEPSKY:
				if (isprotective <= 2) {
					isprotective = 3;
				}
				if (_skyversion <= 0) {
					if (_geneticCode.getModifiessky()) {
						_skyversion = 2;
					} else {
						_skyversion = 1;
					}
				}
				_mphoto[i] = -20;
				break;
			case GRAY:
				_isgray =true;
				_isakiller =true;
				_mphoto[i] = -1;
				break;
			case LILAC:
				if (_geneticCode.getModifieslilac()) {
					_modifieslilac =true;
				}
				_isakiller =true;
				_mphoto[i] = -4;
				break;
			case DARKLILAC:
				_isakiller =true;
				_mphoto[i] = -20;
				break;
			case SPIKE:
				enhancedconsumer =true;
				_isspike =true;
				_isakiller =true;
				_usepretoucheffects =true;
				_mphoto[i] = -0.5;
				break;
			case SPIKEPOINT:
				enhancedconsumer =true;
				_isspike =true;
				_isakiller =true;
				_usepretoucheffects =true;
				_mphoto[i] = -1;
				break;
			case DARK:
				if (_geneticCode.getModifiesblack() <= 2) {
					if (_blackversion == 0) {
						if (_geneticCode.getModifiesblack() == 1) {
							_blackversion = -1;
						} else {
							_blackversion = -2;
						}
						_timeToReproduceMax -= 6;
					}
					if (_segmentpretoucheffects <= 1) {
						_segmentpretoucheffects = 2;
					}
					_usepretoucheffects =true;
					_mphoto[i] = -8;
				} else {
					if (_geneticCode.getAdaptblack() == 24) {
						_mphoto[i] = -8;
					} else {
						_mphoto[i] = -9;
					}
					if (_blackversion == 0) {
						_blackversion = _geneticCode.getAdaptblack();
						_timeToReproduceMax -= 6;
						if (_blackversion <= 2) {
							if (_blackversion == 1) {
								setColor(Color.GREEN);
							} else {
								setColor(Utils.ColorBARK);
							}
						} else if (_blackversion <= 8) {
							if (_blackversion == 3) {
								setColor(Color.RED);
							} else if (_blackversion == 4) {
								setColor(Utils.ColorFIRE);
							} else if (_blackversion == 5) {
								setColor(Color.ORANGE);
							} else if (_blackversion == 6) {
								setColor(Utils.ColorMAROON);
							} else if (_blackversion == 7) {
								setColor(Color.PINK);
							} else {
								setColor(Utils.ColorCREAM);
							}
						} else if (_blackversion <= 12) {
							if (_blackversion == 9) {
								setColor(Color.LIGHT_GRAY);
							} else if (_blackversion == 10) {
								setColor(Utils.ColorSPIKEPOINT);
							} else if (_blackversion == 11) {
								setColor(Utils.ColorLILAC);
							} else {
								setColor(Color.GRAY);
							}
						} else if (_blackversion <= 18) {
							if (_blackversion == 13) {
								setColor(Utils.ColorVIOLET);
							} else if (_blackversion == 14) {
								setColor(Utils.ColorOLIVE);
							} else if (_blackversion == 15) {
								setColor(Utils.ColorSKY);
							} else if (_blackversion == 16) {
								setColor(Color.BLUE);
							} else if (_blackversion == 17) {
								setColor(Utils.ColorOCHRE);
							} else {
								setColor(Utils.ColorFALLOW);
							}
						} else {
							if (_blackversion == 19) {
								setColor(Color.WHITE);
							} else if (_blackversion == 20) {
								setColor(Utils.ColorPLAGUE);
							} else if (_blackversion == 21) {
								setColor(Utils.ColorCORAL);
							} else if (_blackversion == 22) {
								setColor(Utils.ColorMINT);
							} else if (_blackversion == 23) {
								setColor(Color.MAGENTA);
							} else {
								setColor(Utils.ColorDARK);
							}
						}
					}
				}
				break;
		    }
		}
		if (enhancedconsumer) {
			if (_isenhanced) {
				_isaconsumer =true;
			}
			if (_issilver) {
				if (ispink) {
					_isaconsumer =true;
				}
				if (_iscoral) {
					_isinfectious =true;
				}
				if ((_isakiller) || ((_plagueversion > 0) && (!_isinfectious))) {
					_isafungus =true;
				}
			}
		}
		if ((_blackversion > 0) && (_geneticCode.getModifiesblack() == 4) && (_reproducelate == 0) && (_age == 0)) {
			int b;
			for (b=_segments-1; b>=0; b--) {
	            if (_segColor[b].equals(Utils.ColorDARK)) {
	            	_reproduceEnergy -= 3;
				}
			}
		}
		if (_isonlyc4 > 0) {
			if ((!_isaplant) && (_methanotrophy == 0) && (_blackversion >= 0)) {
				if ((!_isaconsumer) && (!_isafungus) && (!_isakiller) && (!_isinfectious) && (_plagueversion == 0) && (isprotective < 2)) {
					if ((!_iscoral) && (isprotective == 0)) {
						_isonlyc4 = 2;
						_candodge =true;
						if ((_indigo > 0) && (_jadefactor == 0)) {
							_jadefactor = -1;
						}
						int j;
						for (j=_segments-1; j>=0; j--) {
							switch (getTypeColor(_segColor[j])) {
							case C4:
								if ((_sporetime == 0) || (_geneticCode.getModifiesspore() <= 6)) {
									_mphoto[j] = Utils.C4_ENERGY_CONSUMPTION * photomultiplier * (11 + _geneticCode.getGene(j%_geneticCode.getNGenes()).getLength());
								}
								break;
							case TEAL:
							case CYAN:
								if ((_reproducelate == 0) && (_age == 0)) {
									_reproduceEnergy -= 1;
								}
								break;
							case LAVENDER:
								if ((_reproducelate == 0) && (_age == 0)) {
									_reproduceEnergy -= 2;
								}
								break;
							case SPORE:
								if ((_geneticCode.getModifiesspore() >= 3) && (_geneticCode.getModifiesspore() <= 6) && (_reproducelate == 0) && (_age == 0)) {
									_reproduceEnergy -= 3;
								}
								break;
							case BLOND:
							case GOLD:
							case DARK:
								break;
							default:
								if ((_reproducelate == 0) && (_age == 0)) {
									_reproduceEnergy -= 3;
								}
								break;
							}
						}
					} else {
						int j;
						for (j=_segments-1; j>=0; j--) {
							switch (getTypeColor(_segColor[j])) {
							case C4:
								if ((_sporetime == 0) || (_geneticCode.getModifiesspore() <= 6)) {
									_mphoto[j] = Utils.C4_ENERGY_CONSUMPTION * photomultiplier * (11 + _geneticCode.getGene(j%_geneticCode.getNGenes()).getLength());
								}
								break;
							}
						}
					}
				}
			} else {
				_isonlyc4 = 0;
			}
			_isaplant =true;
		}
        if ((_isaplant) && (_symmetry <= 7)) {
        	if (_symmetry >= 6) {
				_lowersymmetric = 700;
			} else {
				if (_symmetry >= 4) {
					_lowersymmetric = 700 + 200;
				} else {
					if ((_symmetry <= 2) && (!_isaconsumer) && (!_isafungus) && (!_isakiller) && (_blackversion >= 0) && (isprotective < 3)) {
						_lowersymmetric = 600 + (1200 / (_symmetry + 1));
					} else {
						_lowersymmetric = 700 + (1200 / (_symmetry + 1));
					}
				}
			}
		}
		// Can this organism dodge?
		if (_canreact) {
			if ((!_isaconsumer) && (!_isafungus) && (!_isakiller) && (_blackversion >= 0) && (_skyversion == 0) && (!_modifiesleaf)
				&& (((!_isinfectious) && (!_iscoral) && (_plagueversion == 0) && (isprotective < 4)) || ((!_isaplant) && (_methanotrophy == 0) && (_geneticCode.getPassive())))) {
				_candodge =true;
			} else {
				_candodge =false;
				_dodge =false;
			}
		}
		// Calculate jade delay used in restoration of the color
		if (_jadefactor > 1) {
			if (_symmetry != 1) {
				_jadefactor = Utils.DARKJADE_DELAY * _symmetry;
			} else {
				_jadefactor = Utils.DARKJADE_DELAY * 2;
			}
		}
		// Give non green methanotrophs the ability of jade during virus hatching, then make them count as plants and lower effectivity for mixed organisms
		if (_methanotrophy > 0) {
			if (_isenhanced) {
				_segmentpretoucheffects = 3;
				_usepretoucheffects =true;
			}
			if (!_isaplant) {
				if (_jadefactor == 0) {
					_jadefactor = -1;
				}
			} else {
				if (_age == 0) {
					int p;
					for (p=_segments-1; p>=0; p--) {
			            if (_segColor[p].equals(Utils.ColorPURPLE)) {
			            	_mphoto[p] = 0.55 * Utils.PURPLE_ENERGY_CONSUMPTION * photomultiplier * _geneticCode.getGene(p%_geneticCode.getNGenes()).getLength();
						}
					}
				}
			}
			_isaplant =true;
		}
		if ((_transfersenergy) && (!_isaplant)) {
			if ((!_isaconsumer) && (!_isafungus)) {
				if (_segmentpretoucheffects == 0) {
					_segmentpretoucheffects = 1;
				}
				_usepretoucheffects =true;
				if (_lengthfriend > 0) {
					_colonyPhotosynthesis = Utils.SYMBIONT_ENERGY_CONSUMPTION * (_lengthfriend + 9);
					_lengthfriend = -_lengthfriend;
				}
			} else {
				_colonyPhotosynthesis = 0;
			}
		}
		// Calculate the total shield production capacity of lavender segments
		if (_createlavender > 0) {
			_createlavender = _createlavender / _symmetry;
			if (_age == 0) {
				_lavender = Utils.LAVENDER_SHIELD;
			}
		}
		if (_sporetime > 0) {
			if (_age == 0) {
				if (_geneticCode.getModifiesspore() <= 6) {
	            	_sporeversion = _geneticCode.getModifiesspore();
	           		_sporetime = ((_sporetime / _symmetry) / 10) + 2;
	           		if (_createlavender > 0) {
	               		_lavender = 200;
	           		}
	           		if ((!_isaplant) && (!_isaconsumer) && (!_isafungus)) {
						if (_sporeversion <= 2) {
							_timeToReproduce = 20;
						}
					}
				} else {
					if (_geneticCode.getModifiesspore() <= 9) {
						_sporeversion = -2;
					} else {
						_sporeversion = -3;
					}
				}
			} else {
				if (_age == 1) {
					if ((!_isaplant) && (!_isaconsumer) && (!_isafungus)) {
						if (_sporeversion >= 5) {
							if (_sporeversion == 5) {
								_isafungus =true;
								_timeToReproduce = 20;
							} else {
								_timeToReproduce = _timeToReproduceMax;
							}
						}
					}
				}
			}
		}
		// Calculate extra children for yellow
		if (_yellowCounter > 0) {
			_isreproductive =true;
			if (_isonlyc4 == 2) {
				_yellowCounter = (_yellowCounter / _symmetry) + (int) (fertility / 17);
			} else {
				_yellowCounter = (_yellowCounter / _symmetry) + (int) (fertility / 23);
				if ((_isinfectious) && (!_isaconsumer) && (!_isaplant) && (!_isafungus)) {
					// Yellow of viruses uses touch effects
					int j;
					for (j=_segments-1; j>=0; j--) {
			            if (_segColor[j].equals(Color.YELLOW)) {
			            	_mphoto[j] = -4;
						}
					}
				}
			}
		}
		// Calculate indigo
		if (_indigo > 0) {
			_isreproductive =true;
			_indigo = (int) (_indigo/Utils.INDIGO_DIVISOR);
		}
		// Calculate reproduction energy for blond segments
		if (_earlyReproduceEnergy > 0) {
			_earlyReproduceEnergy = (int) Math.round(_earlyReproduceEnergy/10);
			_isreproductive =true;
			if (_isaplant) {
			    _earlyReproduceEnergy = _reproduceEnergy - _earlyReproduceEnergy;
			    if ((_isonlyc4 == 2) && (_isblond) && (!_isenhanced)) {
			    	if (_earlyReproduceEnergy < 30) {
						_earlyReproduceEnergy = 30;
					}
			    } else {
			    	if (_earlyReproduceEnergy < 40) {
						_earlyReproduceEnergy = 40;
					}
			    }
			} else {
				if ((_isaconsumer) || (_isafungus)) {
					_earlyReproduceEnergy = _reproduceEnergy - _earlyReproduceEnergy;
					if (_earlyReproduceEnergy < 21) {
						_earlyReproduceEnergy = 21;
					}
				} else {
					if ((_transfersenergy) && (_isinfectious) && (_isblond)) {
						_earlyReproduceEnergy = _reproduceEnergy - _earlyReproduceEnergy;
						if (_earlyReproduceEnergy < 21) {
							_earlyReproduceEnergy = 21;
						}
						if (_age == 0) {
							_timeToReproduce = _timeToReproduceMax;
						}
					} else {
						if ((_sporeversion < 0) || (_sporeversion == 6)) {
							_earlyReproduceEnergy = _reproduceEnergy - _earlyReproduceEnergy;
							if (_earlyReproduceEnergy < 21) {
								_earlyReproduceEnergy = 21;
							}
						} else {
							_earlyReproduceEnergy = _reproduceEnergy - _earlyReproduceEnergy;
							if (_earlyReproduceEnergy < 14) {
								_earlyReproduceEnergy = 14;
							}
						}
					}
					if ((_createlavender > 0) && (_isinfectious) && (_isblond) && (_age == 0)) {
						_lavender = 0;
					}
				}
		    }
		}
		// Calculate reproduction energy for flower segments
		if (_reproducelate > 0) {
			_reproducelate = (int) Math.round(_reproducelate/10);
			if (_isinfectious) {
				_reproduceEnergy = (40 + 3 * _segments) + (int)(_reproducelate*Utils.scale[_growthRatio-1]);
			} else {
				if ((_reproducelate - (3 * _symmetry)) > 0) {
					_reproduceEnergy = (40 + 3 * _segments) + _reproducelate - (3 * _symmetry);
				}
				if (_age == 0) {
					_timeToReproduceMax = 14;
				}
			}
		}
		// Calculate life expectancy with gold segments
		if (_gold > 0) {
			_max_age = Utils.MAX_AGE + (_segments/Utils.AGE_DIVISOR) + (int) Math.round((_gold/10)/Utils.GOLD_DIVISOR);
			_gold = 1;
		}
	}
	/**
	 * Initializes variables for a new random organism and finds a place
	 * to put it in the world.
	 *
	 * @return  true if it found a place for this organism or false otherwise.
	 */
	public boolean randomCreate() {
		// Generates a random genetic code
		_geneticCode = new GeneticCode();
		// it has no parent
		_parentID = -1;
		_geneticCode._generation = 1;
		_growthRatio = 16;
		// initial energy
		_energy = Math.min(Utils.INITIAL_ENERGY,_world._CO2);
		_world.decreaseCO2(_energy);
		_world.addO2(_energy);
		// initialize
		create();
		symmetric();
		// put it in the world
		return placeRandom();
	}
	/**
	 * Initializes variables for a new organism born from an existing
	 * organism. Generates a mutated genetic code based on the parent's one
	 * and finds a place in the world to put it.
	 *
	 * @param parent  The organism from which this organism is born.
	 * @return  true if it found a place for this organism or false otherwise.
	 */
	public boolean inherit(Organism parent, boolean first) {
		GeneticCode inheritGeneticCode;
		boolean ok = true;
		int sporeremember = 0;
		int inheritReproduceEnergy = 1;

		// Prepare the inherited genetic code
		if (parent._sporeversion > 0) {
			if (parent._infectedGeneticCode != null) {
				// inheritReproduceEnergy is used for virus delay at first here
				if (parent._isreproductive) {
					if (parent._nVirusChildren == 0) {
						inheritReproduceEnergy = 14 + (int)(parent._symmetry/2);
					} else {
						if (parent._nVirusChildren == 1) {
							if (parent._infectedGeneticCode.getPlague()) {
								inheritReproduceEnergy = 4 + (int)(parent._symmetry/2);
							} else {
								if (parent._infectedGeneticCode.getSymmetry() != 2) {
									inheritReproduceEnergy = (int)(120 / parent._infectedGeneticCode.getSymmetry()) - 11 + (int)(parent._symmetry/2);
								} else {
									inheritReproduceEnergy = 40 - 11 + (int)(parent._symmetry/2);
								}
							}
						}
					}
				}
				if ((first && Utils.random.nextInt(inheritReproduceEnergy) < 3) || (parent._nVirusChildren > 0 && !first) || (parent._sporetime < 0)) {
					if (parent._infectedReproduceEnergy > 0) {
						inheritGeneticCode = parent._infectedGeneticCode;
						inheritReproduceEnergy = parent._infectedReproduceEnergy;
						if (parent._nVirusChildren == 0) {
							sporeremember = 1;
						} else {
							sporeremember = 0;
						}
					} else {
						inheritReproduceEnergy = 2 + (int)(parent._symmetry/4);
						if ((first && Utils.random.nextInt(inheritReproduceEnergy) < 1) || (parent._nVirusChildren > 0 && !first) || (parent._sporetime < 0)) {
							inheritGeneticCode = parent._infectedGeneticCode;
							inheritReproduceEnergy = -parent._infectedReproduceEnergy;
							if (parent._nVirusChildren == 0) {
								sporeremember = 1;
							} else {
								sporeremember = 0;
							}
						} else {
							parent._nChildren = 1;
							parent._nVirusChildren = 0;
							inheritReproduceEnergy = 1;
							sporeremember = -1;
							parent.setColor(Utils.ColorSPORE);
							return false;
						}
					}
				} else {
					if (parent._nVirusChildren != 0) {
						parent._nChildren = 1;
						parent._nVirusChildren = 0;
						inheritReproduceEnergy = 1;
						sporeremember = -1;
						parent.setColor(Utils.ColorSPORE);
						return false;
					} else {
						inheritGeneticCode = parent._geneticCode;
						if (parent._earlyReproduceEnergy > 0) {
							inheritReproduceEnergy = parent._earlyReproduceEnergy;
						} else {
							inheritReproduceEnergy = parent._reproduceEnergy;
						}
						if (parent._sporeversion <= 2) {
							parent._infectedGeneticCode = null;
							parent._savedGeneticCode = null;
							sporeremember = -2;
						} else {
							sporeremember = -1;
						}
						if (parent._isauburn && first) {
							parent._nChildren -= 1;
						}
					}
				}
			} else {
				if (parent._sporetime > 0) {
					inheritGeneticCode = parent._geneticCode;
					if (parent._earlyReproduceEnergy > 0) {
						inheritReproduceEnergy = parent._earlyReproduceEnergy;
					} else {
						inheritReproduceEnergy = parent._reproduceEnergy;
					}
					if (parent._sporeversion <= 2) {
						sporeremember = -2;
					}
				} else {
					if (first) {
						parent._sporetime = -parent._sporetime;
					}
					parent._nChildren = 1;
					parent.setColor(Utils.ColorFLOWER);
					return false;
				}
			}
		} else {
			if (parent._infectedGeneticCode != null) {
				inheritGeneticCode = parent._infectedGeneticCode;
				inheritReproduceEnergy = parent._infectedReproduceEnergy;
			} else {
				inheritGeneticCode = parent._geneticCode;
				if (parent._earlyReproduceEnergy > 0) {
					inheritReproduceEnergy = parent._earlyReproduceEnergy;
				} else {
					inheritReproduceEnergy = parent._reproduceEnergy;
				}
			}
		}
		// Initial energy: minimum energy required to reproduce is divided
		// between all children and the parent.
		if ((parent._sporeversion > 0) && (inheritGeneticCode == parent._geneticCode)) {
			if ((parent._sporeversion <= 3) || (parent._isblond)) {
				_energy = Math.min(parent._sporetime, parent._energy);
			} else {
				_energy = Math.min(parent._reproduceEnergy * 0.025 * parent._sporetime, parent._energy);
			}
		} else if ((parent._geneticCode.getSelfish()) && (inheritGeneticCode == parent._geneticCode)) {
		        _energy = Math.min(((inheritReproduceEnergy / 2) / (double)parent._nChildren), parent._energy);
		} else if ((parent._indigo > 0) && (inheritGeneticCode == parent._infectedGeneticCode)) {
			if ((parent._jadefactor != 0) && (!parent._isaconsumer) && (!parent._isafungus) && (inheritReproduceEnergy > parent._reproduceEnergy)) {
			    _energy = Math.min(((parent._reproduceEnergy / (((parent._indigo*Utils.scale[parent._growthRatio-1])/10) + 1))
					/ (double)(parent._nChildren + 1)), parent._energy);
			} else {
			    _energy = Math.min(((inheritReproduceEnergy / (((parent._indigo*Utils.scale[parent._growthRatio-1])/10) + 1))
					/ (double)(parent._nChildren + 1)), parent._energy);
			}
			if ((inheritGeneticCode.getSymmetry() != 3) || (inheritGeneticCode.getMirror() == 0)) {
				if (_energy < 3) {
					parent._infectedGeneticCode = null;
					parent._savedGeneticCode = null;
					parent._nChildren = 1;
					parent._nVirusChildren = 0;
					if (parent._timeToReproduce > 20) {
						parent._timeToReproduce = 20;
					}
					return false;
				}
			} else {
				if (_energy < 4) {
					parent._infectedGeneticCode = null;
					parent._savedGeneticCode = null;
					parent._nChildren = 1;
					parent._nVirusChildren = 0;
					if (parent._timeToReproduce > 20) {
						parent._timeToReproduce = 20;
					}
					return false;
				}
			}
		} else {
			if ((parent._jadefactor != 0) && (!parent._isaconsumer) && (!parent._isafungus) && (inheritReproduceEnergy > parent._reproduceEnergy)) {
			    _energy = Math.min((parent._reproduceEnergy / (double)(parent._nChildren + 1)), parent._energy);
			} else {
		        _energy = Math.min((inheritReproduceEnergy / (double)(parent._nChildren + 1)), parent._energy);
			}
		}
		// Create the inherited genetic code
		if (sporeremember == -2) {
			_geneticCode = parent._geneticCode;
		} else if ((parent._savedGeneticCode != null) && (sporeremember >= 0)) {
			_geneticCode = parent._savedGeneticCode;
		} else {
			_geneticCode = new GeneticCode(inheritGeneticCode);
		}
		// Take a reference to the parent
		_parentID = parent._ID;
		_growthRatio = 16;
		// Initialize
		create();
		symmetric();
		// Put it in the world, near its parent
		if ((parent._nChildren <= 1) && (parent._nVirusChildren <= 1)) {
			ok = placeNear(parent);
		} else {
			if (first) {
				parent._nPosition = "01234567";
				ok = placeNearFirst(parent);
			} else {
				ok = placeNearYellow(parent);
			}
		}
		if (ok) {
			if (parent._fallowinhibition > 0) {
				// Organism affected by fallow version 3 will have a dead child
				if (inheritGeneticCode == parent._geneticCode) {
					if (parent._timeToReproduce == parent._timeToReproduceMax) {
						parent._timeToReproduce--;
						if ((!parent._isaconsumer) && (!parent._isafungus)) {
							if (parent._fallowinhibition > 1) {
								parent._fallowinhibition = 1;
							} else {
								parent._fallowinhibition--;
							}
						} else {
							parent._fallowinhibition--;
						}
					}
					if (parent._fallowinhibition > 0) {
						if ((parent._reproducelate > 0) && (!parent._isinfectious)) {
							if (Utils.random.nextBoolean()) {
								parent._energy += _energy/1.5;
								_energy -= _energy/1.5;
								inheritReproduceEnergy = 0;
							}
						} else {
							inheritReproduceEnergy = 0;
						}
					}
				}
			}
			if ((parent._reproducelate > 0) && (!parent._isinfectious)) {
				parent._timeToReproduce = 0;
				if ((!parent._geneticCode.getSelfish()) && (inheritGeneticCode == parent._geneticCode) && (inheritReproduceEnergy > 4)) {
					double Energy = 0;
					Energy = Utils.between((parent._energy) / ((256 * (parent._max_age+1)) / (double)(parent._age+1)), 0, parent._energy);
					parent._energy -= Energy;
					_energy += Energy;
				}
			}
			if ((parent._sporeversion < 0) && (parent._nChildren <= 1)) {
				parent._timeToReproduce = 0;
			}
			if (sporeremember == 1) {
				parent._infectedGeneticCode = null;
				parent._savedGeneticCode = null;
			}
			// Maximum age that an organism can reach
			_max_age = Utils.MAX_AGE + (_segments/Utils.AGE_DIVISOR);
			// Calculates the energy required to reproduce this genetic code.
            _reproduceEnergy = (40 + 3 * _segments);
			// Maximum time that needs to elapse after a reproduction attempt
            _timeToReproduceMax = 8;
            int additionalcentersegments = 0;
    		for (int j = 0; j < _geneticCode.getNGenes(); j++) {
    			if ((_geneticCode.getGene(j).getBranch() != 0) || (j==0)) {
    				_timeToReproduceMax += 6;
    			} else {
    				additionalcentersegments += _symmetry;
    				if ((_symmetry + additionalcentersegments) > 8) {
    					_timeToReproduceMax += 9;
    				} else {
    					_timeToReproduceMax += 6;
    				}
    			}
    		}
            if (inheritReproduceEnergy <= 4) {
            	// Virus created by this noninfectious plague organism, or child of an organism affected by modified fallow will die at birth
            	_age = ((_max_age + 1) * 512);
            	for (int i=0; i<_segments; i++) {
        			if (_segColor[i].equals(Utils.ColorEYE)) {
        				_segColor[i] = Utils.ColorEYE;
        			} else {
        				_segColor[i] = Utils.ColorBROWN;
        			}
        		}
            	if (inheritReproduceEnergy > 0) {
            		parent._timeToReproduce = 0;
            	}
    		} else {
    			// Initialize effects
    			segmentsCreateEffects();
    			// Create spores
    			if (_sporetime > 0) {
    			    if (_sporeversion == 1) {
                    	if (parent._sporeversion == 1) {
                    		if ((useEnergy(Utils.SPORE_ENERGY_CONSUMPTION)) || (useEnergy(_energy))) {
                    			for (int i=0; i<_segments; i++) {
                          			 _segColor[i] = Utils.ColorFRUIT;
                          			 _mphoto[i] = -4;
                          		}
                    		}
                       		_usepretoucheffects = false;
                       		_usefriendeffects = 0;
                       		_canreact = false;
                       		active = false;
                       		hasMoved = true;
                       		_isenhanced = false;
                       		if ((!_isaplant) && (!_isaconsumer) && (!_isafungus)) {
                       			_isenhanced = true;
                       		}
                       		_isaplant = false;
                       		_isaconsumer = false;
                       		_isafungus = false;
                       		if (_haseyes) {
                       			_haseyes = false;
                       			symmetric();
                       		}
                    	}
    			    } else if (_sporeversion == 2) {
                    	if (parent._sporeversion == 2) {
                    		if ((useEnergy(Utils.SPORE_ENERGY_CONSUMPTION)) || (useEnergy(_energy))) {
                    			for (int i=0; i<_segments; i++) {
                          			 _segColor[i] = Utils.ColorFRUIT;
                          			 _mphoto[i] = 0;
                          		}
                    		}
                       		_usepretoucheffects = false;
                       		_usefriendeffects = 0;
                       		_canreact = false;
                       		active = false;
                       		hasMoved = true;
                       		_isenhanced = false;
                       		if ((_isaplant) && (!_isafungus) && (!_isaconsumer)) {
                       			_isenhanced = true;
                       			_max_age = _sporetime;
                       			if (_max_age > 20) {
    	                   			_max_age = 20;
    	                   		}
                       		} else {
                       			_max_age = _sporetime - 1;
                       			if (_max_age > 19) {
    	                   			_max_age = 19;
    	                   		}
                       		}
                       		_isaplant = false;
                       		_isaconsumer = false;
                       		_isafungus = false;
                       		if (_haseyes) {
                       			_haseyes = false;
                       			symmetric();
                       		}
                    	}
    				} else if (_sporeversion == 3) {
    					if (parent._sporeversion == 3) {
    						if (_isaplant) {
    							if ((_isaconsumer) || (_isafungus)) {
    								if (parent._geneticCode.getDisperseChildren()) {
    									dx = 2 * dx;
	        							dy = 2 * dy;
    	    						}
    								_isenhanced = false;
    								for (int i=0; i<_segments; i++) {
    	                    			 _segColor[i] = Utils.ColorBARK;
    	                    			 _mphoto[i] = 0;
    	                    		}
    							} else {
    								if (parent._geneticCode.getDisperseChildren()) {
    	    							if (!_canmove) {
    	    								dx = 3 * dx;
    	        							dy = 3 * dy;
    	    							} else {
    	    								dx = 2 * dx;
    	        							dy = 2 * dy;
    	    							}
    	    						}
    								_isenhanced = true;
    								for (int i=0; i<_segments; i++) {
    	                    			 _segColor[i] = Utils.ColorBARK;
    	                    			 _mphoto[i] = 0;
    	                    		}
    							}
    						} else {
    							if (parent._geneticCode.getDisperseChildren()) {
	    							if (!_canmove) {
	    								dx = 3 * dx;
	        							dy = 3 * dy;
	    							} else {
	    								dx = 2 * dx;
	        							dy = 2 * dy;
	    							}
	    						}
    							if ((_isaconsumer) || (_isafungus)) {
    								_isenhanced = false;
    								for (int i=0; i<_segments; i++) {
    									 _segColor[i] = Utils.ColorVISION;
    									 _mphoto[i] = 0;
    	                    		}
    							} else {
    								_isenhanced = true;
    								for (int i=0; i<_segments; i++) {
    	                    			 _segColor[i] = Utils.ColorBARK;
    	                    			 _mphoto[i] = 0;
    	                    		}
    							}
    						}
    						_timeToReproduce = 105 - _sporetime - Math.round(_symmetry/4);
                       		_usepretoucheffects = false;
                       		_usefriendeffects = 0;
                       		_canreact = false;
                       		active = false;
                       		hasMoved = true;
                       		_isaplant = false;
                       		_isaconsumer = false;
                       		_isafungus = false;
                       		if (_haseyes) {
                       			_haseyes = false;
                       			symmetric();
                       		}
                    	}
    				} else if (_sporeversion == 4) {
    					if (parent._sporeversion == 4) {
                       		for (int i=0; i<_segments; i++) {
                     			 _segColor[i] = Utils.ColorBROWN;
                     			 _mphoto[i] = 0;
                     		}
                       		if ((_isaconsumer) || (_isafungus)) {
                       			_timeToReproduce = 116 - _sporetime - Math.round(_symmetry/4) + Utils.random.nextInt(2);
                       			_isenhanced = false;
                       		} else {
                       			_timeToReproduce = 105 - _sporetime - Math.round(_symmetry/4);
                       			_isenhanced = true;
                       		}
                       		_usepretoucheffects = false;
                       		_usefriendeffects = 0;
                       		_canreact = false;
                       		active = false;
                       		hasMoved = true;
                       		_isaplant = false;
                       		_isaconsumer = false;
                       		_isafungus = false;
                       		if (_haseyes) {
                       			_haseyes = false;
                       			symmetric();
                       		}
                    	}
    				} else if (_sporeversion == 5) {
    					if (parent._sporeversion == 5) {
                       		for (int i=0; i<_segments; i++) {
                     			 _segColor[i] = Utils.ColorCREAM;
                     			 _mphoto[i] = -1;
                     		}
                       		if (_creamversion == 0) {
            					if (_geneticCode.getModifiescream() == 1) {
            						_creamversion = 1;
            					} else {
            						if (_geneticCode.getModifiescream() == 2) {
            							_creamversion = 2;
            						} else {
            							if (_geneticCode.getModifiescream() == 3) {
            								_creamversion = 3;
            							}
            						}
            					}
            				}
                       		_usepretoucheffects = false;
                       		_usefriendeffects = 0;
                       		_canreact = false;
                       		active = false;
                       		hasMoved = true;
                       		_isaplant = false;
                       		_isaconsumer = true;
                       		_isafungus = false;
                       		_isenhanced = false;
                       		if (_haseyes) {
                       			_haseyes = false;
                       			symmetric();
                       		}
                    	}
    				} else if (_sporeversion == 6) {
    					if (parent._sporeversion == 6) {
    						boolean largeenough = false;
    						_usepretoucheffects = false;
							if (_isaplant) {
								if ((_isaconsumer) || (_isafungus)) {
									if (_methanotrophy > 0) {
			                   			for (int i=0; i<_segments; i++) {
			                   				_segColor[i] = Utils.ColorPURPLE;
			                   				_mphoto[i] = 0;
			                   				if (_m[i]>=1) {
			                   					largeenough = true;
			                   				}
			                    		}
			                   			if (largeenough) {
			                   				_methanotrophy = (60 * _mass)/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
										}
									} else {
										if (_canmove) {
											if (_jadefactor > 0) {
												_jadefactor = 1;
											}
											_islime = false;
				                   			for (int i=0; i<_segments; i++) {
				                  			    _segColor[i] = Utils.ColorC4;
				                  			    _mphoto[i] = 0;
				                  			    if (_m[i]>=1) {
				                   					largeenough = true;
				                   				}
				                  		    }
				                   			if (largeenough) {
				                   				_photosynthesis = (7.5 * _mass)/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
											}
										} else {
											if (_jadefactor > 0) {
												_jadefactor = 1;
											}
											_islime = false;
				                   			for (int i=0; i<_segments; i++) {
				                  			    _segColor[i] = Utils.ColorLEAF;
				                  			    _mphoto[i] = 0;
				                  			    if (_m[i]>=1) {
				                   					largeenough = true;
				                   				}
				                  		    }
				                   			if (largeenough) {
				                   				_photosynthesis = (8 * _mass)/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
											}
										}
									}
								} else {
									if (_methanotrophy > 0) {
			                   			for (int i=0; i<_segments; i++) {
			                   				_segColor[i] = Utils.ColorPURPLE;
			                   				_mphoto[i] = 0;
			                   				if (_m[i]>=1) {
			                   					largeenough = true;
			                   				}
			                    		}
			                   			if (largeenough) {
			                   				_methanotrophy = (120 * _mass)/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
										}
									} else {
										if (_blackversion < 0) {
											_islime = false;
				                   			for (int i=0; i<_segments; i++) {
				                   				_segColor[i] = Utils.ColorWINTER;
				                   				_mphoto[i] = 0;
				                   				if (_m[i]>=1) {
				                   					largeenough = true;
				                   				}
				                    		}
				                   			if (largeenough) {
				                   			    _photosynthesis = ((10 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
											}
										} else {
											if ((_isakiller) || (_fallowversion > 0)) {
												_islime = false;
					                   			for (int i=0; i<_segments; i++) {
					                   				_segColor[i] = Utils.ColorSUMMER;
					                   				_mphoto[i] = 0;
					                   				if (_m[i]>=1) {
					                   					largeenough = true;
					                   				}
					                    		}
					                   			if (largeenough) {
					                   			    _photosynthesis = ((14 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
												}
											} else {
												if (_islime) {
													for (int i=0; i<_segments; i++) {
						                   				_segColor[i] = Utils.ColorLIME;
						                   				_mphoto[i] = 0;
						                   				if (_m[i]>=1) {
						                   					largeenough = true;
						                   				}
						                    		}
						                   			if (largeenough) {
						                   			    _photosynthesis = ((14 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
						                   			    _leafphoto = ((19 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
													}
												} else {
													if (_forestphoto > 0) {
							                   			for (int i=0; i<_segments; i++) {
							                   				_segColor[i] = Utils.ColorFOREST;
							                   				_mphoto[i] = 1;
							                   				if (_m[i]>=1) {
							                   					largeenough = true;
							                   				}
							                    		}
							                   			if (largeenough) {
							                   			    _photosynthesis = ((17.5 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
							                   			    _forestphoto = ((18 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
							                   			    _usepretoucheffects = true;
														}
													} else {
														if (_isenhanced) {
								                   			for (int i=0; i<_segments; i++) {
								                   				_segColor[i] = Utils.ColorGRASS;
								                   				_mphoto[i] = 0;
								                   				if (_m[i]>=1) {
								                   					largeenough = true;
								                   				}
								                    		}
								                   			if (largeenough) {
								                   			    _photosynthesis = ((18 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
															}
														} else {
															if ((_jadefactor > 1) && (!_isblond)) {
									                   			for (int i=0; i<_segments; i++) {
									                   				_segColor[i] = Utils.ColorJADE;
									                   				_mphoto[i] = 0;
									                   				if (_m[i]>=1) {
									                   					largeenough = true;
									                   				}
									                    		}
									                   			if (largeenough) {
									                   			    _photosynthesis = ((18 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
																}
															} else {
									                   			for (int i=0; i<_segments; i++) {
									                   				_segColor[i] = Color.GREEN;
									                   				_mphoto[i] = 0;
									                   				if (_m[i]>=1) {
									                   					largeenough = true;
									                   				}
									                    		}
									                   			if (largeenough) {
									                   			    _photosynthesis = ((18 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
																}
															}
														}
													}
												}
											}
										}
									}
								}
							} else {
								if ((_isaconsumer) || (_isafungus)) {
									_isaplant = true;
		                   			for (int i=0; i<_segments; i++) {
		                   				_segColor[i] = Utils.ColorSPRING;
		                   				_mphoto[i] = 0;
		                   				if (_m[i]>=1) {
		                   					largeenough = true;
		                   				}
		                  		    }
		                   			if (largeenough) {
		                   				_photosynthesis = (7 * _mass)/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
									}
								} else {
									_isaplant = true;
									_gold = -1;
									for (int i=0; i<_segments; i++) {
										_segColor[i] = Utils.ColorPURPLE;
		                   				_mphoto[i] = 0;
		                   				if (_m[i]>=1) {
		                   					largeenough = true;
		                   				}
		                    		}
									if (largeenough) {
										_methanotrophy = (22 * _mass)/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
									}
								}
							}
							if ((_symmetry == 3) && (_geneticCode.getMirror() != 0)) {
								_photosynthesis = (2d/3d) * _photosynthesis;
								_methanotrophy = (2d/3d) * _methanotrophy;
							}
							if (_gold != 0) {
								_max_age = Utils.MAX_AGE + (_segments/Utils.AGE_DIVISOR);
							}
							if ((_isblond) && (_jadefactor > 0)) {
								_jadefactor = 1;
							}
	                   		_usefriendeffects = 0;
	                   		_canreact = false;
	                   		_candodge = false;
	                   		_modifiesleaf =false;
	                   		active = false;
	                   		hasMoved = true;
	                   		_isreproductive = false;
	                   		if (_haseyes) {
	                   			_haseyes = false;
	                   			symmetric();
	                   		}
                    	}
    				} else if (_geneticCode.getModifiesspore() <= 9) {
    					if (_geneticCode.getAdaptspore() <= 10) {
    						blueSpore();
    					} else if (_geneticCode.getAdaptspore() <= 20) {
    						if (Utils.random.nextBoolean()) {
    							blueSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 30) {
    						if (Utils.random.nextInt(4) == 0) {
    							blueSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 40) {
    						if (Utils.random.nextInt(8) == 0) {
    							blueSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 50) {
    						if (Utils.random.nextInt(16) == 0) {
    							blueSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 60) {
    						if (Utils.random.nextInt(32) == 0) {
    							blueSpore();
    						}
    					}
    				} else if (_geneticCode.getModifiesspore() <= 12) {
    					if (_geneticCode.getAdaptspore() <= 10) {
    						cyanSpore();
    					} else if (_geneticCode.getAdaptspore() <= 20) {
    						if (Utils.random.nextBoolean()) {
    							cyanSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 30) {
    						if (Utils.random.nextInt(4) == 0) {
    							cyanSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 40) {
    						if (Utils.random.nextInt(8) == 0) {
    							cyanSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 50) {
    						if (Utils.random.nextInt(16) == 0) {
    							cyanSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 60) {
    						if (Utils.random.nextInt(32) == 0) {
    							cyanSpore();
    						}
    					}
    				}
    			}
    		}
			// Create subclades
            if (_geneticCode.getUpdateClade()) {
				_geneticCode._updateClade =false;
				int subcladecounter = 0;
				if (Utils.CLADE_COMPLEXITY >= 0) {
					for(char c : _geneticCode._cladeID.toCharArray()) {
				        if( c == '|' || c == '+' || c == '-' || c == '<' || c == '>') {
				            ++subcladecounter;
				        }
				    }
				} else {
					subcladecounter = -2;
				}
			    if (subcladecounter < Utils.CLADE_COMPLEXITY) {
			    	if (_geneticCode.getNGenes() > inheritGeneticCode.getNGenes()) {
			    		_geneticCode._cladeID += "+" + Integer.toHexString(_world.getNewCladePart());
			    	} else if (_geneticCode.getNGenes() < inheritGeneticCode.getNGenes()) {
			    		_geneticCode._cladeID += "-" + Integer.toHexString(_world.getNewCladePart());
			    	} else if (_symmetry > inheritGeneticCode.getSymmetry()) {
			    		_geneticCode._cladeID += ">" + Integer.toHexString(_world.getNewCladePart());
			    	} else if (_symmetry < inheritGeneticCode.getSymmetry()) {
			    		_geneticCode._cladeID += "<" + Integer.toHexString(_world.getNewCladePart());
			    	} else {
			    		_geneticCode._cladeID += "|" + Integer.toHexString(_world.getNewCladePart());
			    	}
			    }
			}
		} else {
			if ((parent._savedGeneticCode == null) && (sporeremember >= 0)) {
				parent._savedGeneticCode = _geneticCode;
			}
		}

		return ok;
	}
	/**
	 * Initializes variables for a new organism born from an existing
	 * transformed organism. Generates a mutated genetic code based on the transformers's one
	 * and finds a place in the world to put it.
	 *
	 * @param victim  The organism from which this organism is transformed.
	 * @return  true if it found a place for this organism or false otherwise.
	 */
	public boolean inheritTransformation(Organism victim, boolean first) {
		GeneticCode inheritGeneticCode;
		boolean ok = true;

		// Create the transformed genetic code
		inheritGeneticCode = victim._infectedGeneticCode;
		_geneticCode = new GeneticCode(inheritGeneticCode);
		// Take a reference to the victim
		_parentID = victim._ID;
		_growthRatio = 16;
		// Initial energy is total energy of the victim
		_energy = victim._energy;
		if (first) {
			// Initialize
			create();
			symmetric();
			// Put it in the world, near its parent
			ok = transform(victim);
			if (ok) {
				// Maximum age that an organism can reach
				_max_age = Utils.MAX_AGE + (_segments/Utils.AGE_DIVISOR);
				// Calculates the energy required to reproduce this genetic code.
                _reproduceEnergy = (40 + 3 * _segments);
				// Maximum time that needs to elapse after a reproduction attempt
                _timeToReproduceMax = 8;
                int additionalcentersegments = 0;
        		for (int j = 0; j < _geneticCode.getNGenes(); j++) {
        			if ((_geneticCode.getGene(j).getBranch() != 0) || (j==0)) {
        				_timeToReproduceMax += 6;
        			} else {
        				additionalcentersegments += _symmetry;
        				if ((_symmetry + additionalcentersegments) > 8) {
        					_timeToReproduceMax += 9;
        				} else {
        					_timeToReproduceMax += 6;
        				}
        			}
        		}
				// Initialize effects
				segmentsCreateEffects();
				// Create spores
				if (_sporetime > 0) {
				    if (_sporeversion == 1) {
				    	    if ((useEnergy(Utils.SPORE_ENERGY_CONSUMPTION)) || (useEnergy(_energy))) {
	                			for (int i=0; i<_segments; i++) {
	                      			 _segColor[i] = Utils.ColorFRUIT;
	                      			 _mphoto[i] = -4;
	                      		}
	                		}
	                   		_usepretoucheffects = false;
	                   		_usefriendeffects = 0;
	                   		_canreact = false;
	                   		active = false;
	                   		hasMoved = true;
	                   		_isenhanced = false;
	                   		if ((!_isaplant) && (!_isaconsumer) && (!_isafungus)) {
	                   			_isenhanced = true;
	                   		}
	                   		_isaplant = false;
	                   		_isaconsumer = false;
	                   		_isafungus = false;
	                   		if (_haseyes) {
	                   			_haseyes = false;
	                   			symmetric();
	                   		}
				    } else if (_sporeversion == 2) {
	                		if ((useEnergy(Utils.SPORE_ENERGY_CONSUMPTION)) || (useEnergy(_energy))) {
	                			for (int i=0; i<_segments; i++) {
	                      			 _segColor[i] = Utils.ColorFRUIT;
	                      			 _mphoto[i] = 0;
	                      		}
	                		}
	                   		_usepretoucheffects = false;
	                   		_usefriendeffects = 0;
	                   		_canreact = false;
	                   		active = false;
	                   		hasMoved = true;
	                   		_isenhanced = false;
	                   		if ((_isaplant) && (!_isafungus) && (!_isaconsumer)) {
                       			_isenhanced = true;
                       			_max_age = _sporetime;
                       			if (_max_age > 20) {
    	                   			_max_age = 20;
    	                   		}
                       		} else {
                       			_max_age = _sporetime - 1;
                       			if (_max_age > 19) {
    	                   			_max_age = 19;
    	                   		}
                       		}
	                   		_isaplant = false;
	                   		_isaconsumer = false;
	                   		_isafungus = false;
	                   		if (_haseyes) {
	                   			_haseyes = false;
	                   			symmetric();
	                   		}
					} else if (_sporeversion == 3) {
							if (_isaplant) {
								if ((_isaconsumer) || (_isafungus)) {
									_isenhanced = false;
									for (int i=0; i<_segments; i++) {
		                    			 _segColor[i] = Utils.ColorBARK;
		                    			 _mphoto[i] = 0;
		                    		}
								} else {
									_isenhanced = true;
									for (int i=0; i<_segments; i++) {
		                    			 _segColor[i] = Utils.ColorBARK;
		                    			 _mphoto[i] = 0;
		                    		}
								}
							} else {
								if ((_isaconsumer) || (_isafungus)) {
									_isenhanced = false;
									for (int i=0; i<_segments; i++) {
										 _segColor[i] = Utils.ColorVISION;
										 _mphoto[i] = 0;
		                    		}
								} else {
									_isenhanced = true;
									for (int i=0; i<_segments; i++) {
		                    			 _segColor[i] = Utils.ColorBARK;
		                    			 _mphoto[i] = 0;
		                    		}
								}
							}
							_timeToReproduce = 105 - _sporetime - Math.round(_symmetry/4);
	                   		_usepretoucheffects = false;
	                   		_usefriendeffects = 0;
	                   		_canreact = false;
	                   		active = false;
	                   		hasMoved = true;
	                   		_isaplant = false;
	                   		_isaconsumer = false;
	                   		_isafungus = false;
	                   		if (_haseyes) {
	                   			_haseyes = false;
	                   			symmetric();
	                   		}
					} else if (_sporeversion == 4) {
	                   		for (int i=0; i<_segments; i++) {
	                 			 _segColor[i] = Utils.ColorBROWN;
	                 			 _mphoto[i] = 0;
	                 		}
	                   		if ((_isaconsumer) || (_isafungus)) {
	                   			_timeToReproduce = 116 - _sporetime - Math.round(_symmetry/4) + Utils.random.nextInt(2);
                       			_isenhanced = false;
                       		} else {
                       			_timeToReproduce = 105 - _sporetime - Math.round(_symmetry/4);
                       			_isenhanced = true;
                       		}
	                   		_usepretoucheffects = false;
	                   		_usefriendeffects = 0;
	                   		_canreact = false;
	                   		active = false;
	                   		hasMoved = true;
	                   		_isaplant = false;
	                   		_isaconsumer = false;
	                   		_isafungus = false;
	                   		if (_haseyes) {
	                   			_haseyes = false;
	                   			symmetric();
	                   		}
					} else if (_sporeversion == 5) {
	                   		for (int i=0; i<_segments; i++) {
	                 			 _segColor[i] = Utils.ColorCREAM;
	                 			 _mphoto[i] = -1;
	                 		}
	                   		if (_creamversion == 0) {
	        					if (_geneticCode.getModifiescream() == 1) {
	        						_creamversion = 1;
	        					} else {
	        						if (_geneticCode.getModifiescream() == 2) {
	        							_creamversion = 2;
	        						} else {
	        							if (_geneticCode.getModifiescream() == 3) {
	        								_creamversion = 3;
	        							}
	        						}
	        					}
	        				}
	                   		_usepretoucheffects = false;
	                   		_usefriendeffects = 0;
	                   		_canreact = false;
	                   		active = false;
	                   		hasMoved = true;
	                   		_isaplant = false;
	                   		_isaconsumer = true;
	                   		_isafungus = false;
	                   		_isenhanced = false;
	                   		if (_haseyes) {
	                   			_haseyes = false;
	                   			symmetric();
	                   		}
					} else if (_sporeversion == 6) {
						    boolean largeenough = false;
						    _usepretoucheffects = false;
							if (_isaplant) {
								if ((_isaconsumer) || (_isafungus)) {
									if (_methanotrophy > 0) {
			                   			for (int i=0; i<_segments; i++) {
			                   				_segColor[i] = Utils.ColorPURPLE;
			                   				_mphoto[i] = 0;
			                   				if (_m[i]>=1) {
			                   					largeenough = true;
			                   				}
			                    		}
			                   			if (largeenough) {
			                   				_methanotrophy = (60 * _mass)/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
										}
									} else {
										if (_canmove) {
											if (_jadefactor > 0) {
												_jadefactor = 1;
											}
											_islime = false;
				                   			for (int i=0; i<_segments; i++) {
				                  			    _segColor[i] = Utils.ColorC4;
				                  			    _mphoto[i] = 0;
				                  			    if (_m[i]>=1) {
				                   					largeenough = true;
				                   				}
				                  		    }
				                   			if (largeenough) {
				                   				_photosynthesis = (7.5 * _mass)/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
											}
										} else {
											if (_jadefactor > 0) {
												_jadefactor = 1;
											}
											_islime = false;
				                   			for (int i=0; i<_segments; i++) {
				                  			    _segColor[i] = Utils.ColorLEAF;
				                  			    _mphoto[i] = 0;
				                  			    if (_m[i]>=1) {
				                   					largeenough = true;
				                   				}
				                  		    }
				                   			if (largeenough) {
				                   				_photosynthesis = (8 * _mass)/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
											}
										}
									}
								} else {
									if (_methanotrophy > 0) {
			                   			for (int i=0; i<_segments; i++) {
			                   				_segColor[i] = Utils.ColorPURPLE;
			                   				_mphoto[i] = 0;
			                   				if (_m[i]>=1) {
			                   					largeenough = true;
			                   				}
			                    		}
			                   			if (largeenough) {
			                   				_methanotrophy = (120 * _mass)/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
										}
									} else {
										if (_blackversion < 0) {
											_islime = false;
				                   			for (int i=0; i<_segments; i++) {
				                   				_segColor[i] = Utils.ColorWINTER;
				                   				_mphoto[i] = 0;
				                   				if (_m[i]>=1) {
				                   					largeenough = true;
				                   				}
				                    		}
				                   			if (largeenough) {
				                   			    _photosynthesis = ((10 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
											}
										} else {
											if ((_isakiller) || (_fallowversion > 0)) {
												_islime = false;
					                   			for (int i=0; i<_segments; i++) {
					                   				_segColor[i] = Utils.ColorSUMMER;
					                   				_mphoto[i] = 0;
					                   				if (_m[i]>=1) {
					                   					largeenough = true;
					                   				}
					                    		}
					                   			if (largeenough) {
					                   			    _photosynthesis = ((14 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
												}
											} else {
												if (_islime) {
													for (int i=0; i<_segments; i++) {
						                   				_segColor[i] = Utils.ColorLIME;
						                   				_mphoto[i] = 0;
						                   				if (_m[i]>=1) {
						                   					largeenough = true;
						                   				}
						                    		}
						                   			if (largeenough) {
						                   			    _photosynthesis = ((14 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
						                   			    _leafphoto = ((19 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
													}
												} else {
													if (_forestphoto > 0) {
							                   			for (int i=0; i<_segments; i++) {
							                   				_segColor[i] = Utils.ColorFOREST;
							                   				_mphoto[i] = 1;
							                   				if (_m[i]>=1) {
							                   					largeenough = true;
							                   				}
							                    		}
							                   			if (largeenough) {
							                   			    _photosynthesis = ((17.5 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
							                   			    _forestphoto = ((18 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
							                   			    _usepretoucheffects = true;
														}
													} else {
														if (_isenhanced) {
								                   			for (int i=0; i<_segments; i++) {
								                   				_segColor[i] = Utils.ColorGRASS;
								                   				_mphoto[i] = 0;
								                   				if (_m[i]>=1) {
								                   					largeenough = true;
								                   				}
								                    		}
								                   			if (largeenough) {
								                   			    _photosynthesis = ((18 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
															}
														} else {
															if ((_jadefactor > 1) && (!_isblond)) {
									                   			for (int i=0; i<_segments; i++) {
									                   				_segColor[i] = Utils.ColorJADE;
									                   				_mphoto[i] = 0;
									                   				if (_m[i]>=1) {
									                   					largeenough = true;
									                   				}
									                    		}
									                   			if (largeenough) {
									                   			    _photosynthesis = ((18 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
																}
															} else {
									                   			for (int i=0; i<_segments; i++) {
									                   				_segColor[i] = Color.GREEN;
									                   				_mphoto[i] = 0;
									                   				if (_m[i]>=1) {
									                   					largeenough = true;
									                   				}
									                    		}
									                   			if (largeenough) {
									                   			    _photosynthesis = ((18 * _mass) + (19.5 * (double)_symmetry))/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
																}
															}
														}
													}
												}
											}
										}
									}
								}
							} else {
								if ((_isaconsumer) || (_isafungus)) {
									_isaplant = true;
		                   			for (int i=0; i<_segments; i++) {
		                   				_segColor[i] = Utils.ColorSPRING;
		                   				_mphoto[i] = 0;
		                   				if (_m[i]>=1) {
		                   					largeenough = true;
		                   				}
		                  		    }
		                   			if (largeenough) {
		                   				_photosynthesis = (7 * _mass)/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
									}
								} else {
									_isaplant = true;
									_gold = -1;
									for (int i=0; i<_segments; i++) {
										_segColor[i] = Utils.ColorPURPLE;
		                   				_mphoto[i] = 0;
		                   				if (_m[i]>=1) {
		                   					largeenough = true;
		                   				}
		                    		}
									if (largeenough) {
										_methanotrophy = (22 * _mass)/Utils.GREEN_OBTAINED_ENERGY_DIVISOR;
									}
								}
							}
							if ((_symmetry == 3) && (_geneticCode.getMirror() != 0)) {
								_photosynthesis = (2d/3d) * _photosynthesis;
								_methanotrophy = (2d/3d) * _methanotrophy;
							}
							if (_gold != 0) {
								_max_age = Utils.MAX_AGE + (_segments/Utils.AGE_DIVISOR);
							}
							if ((_isblond) && (_jadefactor > 0)) {
								_jadefactor = 1;
							}
	                   		_usefriendeffects = 0;
	                   		_canreact = false;
	                   		_candodge = false;
	                   		_modifiesleaf =false;
	                   		active = false;
	                   		hasMoved = true;
	                   		_isreproductive = false;
	                   		if (_haseyes) {
	                   			_haseyes = false;
	                   			symmetric();
	                   		}
					} else if (_geneticCode.getModifiesspore() <= 9) {
    					if (_geneticCode.getAdaptspore() <= 10) {
    						blueSpore();
    					} else if (_geneticCode.getAdaptspore() <= 20) {
    						if (Utils.random.nextBoolean()) {
    							blueSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 30) {
    						if (Utils.random.nextInt(4) == 0) {
    							blueSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 40) {
    						if (Utils.random.nextInt(8) == 0) {
    							blueSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 50) {
    						if (Utils.random.nextInt(16) == 0) {
    							blueSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 60) {
    						if (Utils.random.nextInt(32) == 0) {
    							blueSpore();
    						}
    					}
    				} else if (_geneticCode.getModifiesspore() <= 12) {
    					if (_geneticCode.getAdaptspore() <= 10) {
    						cyanSpore();
    					} else if (_geneticCode.getAdaptspore() <= 20) {
    						if (Utils.random.nextBoolean()) {
    							cyanSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 30) {
    						if (Utils.random.nextInt(4) == 0) {
    							cyanSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 40) {
    						if (Utils.random.nextInt(8) == 0) {
    							cyanSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 50) {
    						if (Utils.random.nextInt(16) == 0) {
    							cyanSpore();
    						}
    					} else if (_geneticCode.getAdaptspore() <= 60) {
    						if (Utils.random.nextInt(32) == 0) {
    							cyanSpore();
    						}
    					}
    				}
				}
				// Create subclades
				if (_geneticCode.getUpdateClade()) {
					_geneticCode._updateClade =false;
					int subcladecounter = 0;
					if (Utils.CLADE_COMPLEXITY >= 0) {
						for(char c : _geneticCode._cladeID.toCharArray()) {
					        if( c == '|' || c == '+' || c == '-' || c == '<' || c == '>') {
					            ++subcladecounter;
					        }
					    }
					} else {
						subcladecounter = -2;
					}
				    if (subcladecounter < Utils.CLADE_COMPLEXITY) {
				    	if (_geneticCode.getNGenes() > inheritGeneticCode.getNGenes()) {
				    		_geneticCode._cladeID += "+" + Integer.toHexString(_world.getNewCladePart());
				    	} else if (_geneticCode.getNGenes() < inheritGeneticCode.getNGenes()) {
				    		_geneticCode._cladeID += "-" + Integer.toHexString(_world.getNewCladePart());
				    	} else if (_symmetry > inheritGeneticCode.getSymmetry()) {
				    		_geneticCode._cladeID += ">" + Integer.toHexString(_world.getNewCladePart());
				    	} else if (_symmetry < inheritGeneticCode.getSymmetry()) {
				    		_geneticCode._cladeID += "<" + Integer.toHexString(_world.getNewCladePart());
				    	} else {
				    		_geneticCode._cladeID += "|" + Integer.toHexString(_world.getNewCladePart());
				    	}
				    }
				}
			}
		} else
			ok = false;

		return ok;
	}
	/**
	 * Places the organism at the specified position in the world and initializes its
	 * variables. The organism must has an assigned genetic code.
	 *
	 * @param posx  The x coordinate of the position in the world we want to put this organism.
	 * @param posy  The y coordinate of the position in the world we want to put this organism.
	 * @return  true if there were enough space to put the organism, false otherwise.
	 */
	public boolean pasteOrganism(int posx, int posy) {
		boolean isacheater =false;
		_parentID = -1;
		if (_geneticCode.getGeneration() == 0) {
			_geneticCode._generation = 1;
		}
		_growthRatio = 16;
		create();
		symmetric();
		for (int i = 0; i < _geneticCode.getNGenes(); i++) {
			if (_geneticCode.getGene(i).getBranch() > i) {
				isacheater =true;
			}
		}
		if (isacheater == false) {
		_dCenterX = _centerX = posx;
		_dCenterY = _centerY = posy;
		calculateBounds();
		// Check that the position is inside the world
		if (isInsideWorld()) {
			// Check that the organism will not overlap other organisms
			if (_world.genesisCheckHit(this) == null) {
				// Generem identificador
				_ID = _world.getNewId();
				if (_geneticCode.getcladeID() == null) {
					if (Utils.ACCEPT_CONNECTIONS) {
						_geneticCode._cladeID = Utils.USER_NAME + ":" + Integer.toString(_ID);
					} else {
						_geneticCode._cladeID = Integer.toString(_ID);
					}
				}
				_energy = Math.min(Utils.INITIAL_ENERGY,_world._CO2);
				_world.decreaseCO2(_energy);
				_world.addO2(_energy);
				// Maximum age that an organism can reach
				_max_age = Utils.MAX_AGE + (_segments/Utils.AGE_DIVISOR);
				// Calculates the energy required to reproduce this genetic code.
                _reproduceEnergy = (40 + 3 * _segments);
				// Maximum time that needs to elapse after a reproduction attempt
                _timeToReproduceMax = 8;
                int additionalcentersegments = 0;
        		for (int j = 0; j < _geneticCode.getNGenes(); j++) {
        			if ((_geneticCode.getGene(j).getBranch() != 0) || (j==0)) {
        				_timeToReproduceMax += 6;
        			} else {
        				additionalcentersegments += _symmetry;
        				if ((_symmetry + additionalcentersegments) > 8) {
        					_timeToReproduceMax += 9;
        				} else {
        					_timeToReproduceMax += 6;
        				}
        			}
        		}
				// Initialize effects
				segmentsCreateEffects();
				return true;
			}
		}}
		// It can't be placed
		return false;
	}
	/**
	 * Translates the genetic code of this organism to its segments representation in the world.
	 * Also, calculates some useful information like segments length, inertia, etc.
	 * This method must be called when an organism is firstly displayed on the world and every
	 * time it changes its size.
	 * inherit, randomCreate and pasteOrganism are the standard ways to add an organism to a world
	 * and they already call this method.
	 */
	public void symmetric() {
		int i,j,segment=0;
		int symmetry = _symmetry;
		int mirror = _geneticCode.getMirror();
		int sequence = _segments / symmetry;
		int left=0, right=0, top=0, bottom=0;
		int centerX, centerY;
		double cx, cy;
		if (_updateEffects == 0) {
			_updateEffects = 1;
		}
		_mass = 0;

		if (_haseyes) {
			for (i=0; i<symmetry; i++) {
				for (j=0; j<sequence; j++,segment++) {
					// Here, we take the vector that forms the segment, scale it depending on
					// the relative size of the organism and rotate it depending on the
					// symmetry and mirroring.
					// _mphoto value of -13 is the eye segment
					if (_mphoto[j] == -13) {
						if (_hasgoodvision) {
							v.setModulus(2 * (_geneticCode.getGene(j).getLength()*Utils.scale[_growthRatio-1]));
						} else {
							v.setModulus(1.5 * (_geneticCode.getGene(j).getLength()*Utils.scale[_growthRatio-1]));
						}
						if ((_isaconsumer) || (_isafungus) || (_isinfectious)) {
							_m[segment] = 0.026 * v.getModulus();
						} else {
							_m[segment] = 0.025 * v.getModulus();
						}
					} else {
						v.setModulus(_geneticCode.getGene(j).getLength()*Utils.scale[_growthRatio-1]);
						_m[segment] = v.getModulus();
					}
					_mass += _m[segment];
					if (j==0) {
						_startPointX[segment] = 0;
						_startPointY[segment] = 0;
						if (mirror == 0 || i%2==0)
							v.setTheta(_geneticCode.getGene(j).getTheta()+i*2*Math.PI/symmetry);
						else {
							v.setTheta(_geneticCode.getGene(j).getTheta()+(i-1)*2*Math.PI/symmetry);
							v.invertX();
						}
					} else {
						if (_geneticCode.getGene(j).getBranch() == -1) {
							_startPointX[segment] = _endPointX[segment - 1];
							_startPointY[segment] = _endPointY[segment - 1];
							if (_mphoto[segment - 1] == -13) {
								_segColor[segment - 1] = Utils.ColorVISION;
								_mphoto[segment - 1] = 0;
							}
							if (mirror == 0 || i%2==0)
								v.addDegree(_geneticCode.getGene(j).getTheta());
							else
								v.addDegree(-_geneticCode.getGene(j).getTheta());
						} else {
						if (_geneticCode.getGene(j).getBranch() == 0) {
							_startPointX[segment] = 0;
						    _startPointY[segment] = 0;
						    if (mirror == 0 || i%2==0)
								v.setTheta(_geneticCode.getGene(j).getTheta()+i*2*Math.PI/symmetry);
							else {
								v.setTheta(_geneticCode.getGene(j).getTheta()+(i-1)*2*Math.PI/symmetry);
								v.invertX();
							}
						} else {
							_startPointX[segment] = _endPointX[(i * sequence) + _geneticCode.getGene(j).getBranch() - 1];
						    _startPointY[segment] = _endPointY[(i * sequence) + _geneticCode.getGene(j).getBranch() - 1];
						    if (_mphoto[(i * sequence) + _geneticCode.getGene(j).getBranch() - 1] == -13) {
								_segColor[(i * sequence) + _geneticCode.getGene(j).getBranch() - 1] = Utils.ColorVISION;
								_mphoto[(i * sequence) + _geneticCode.getGene(j).getBranch() - 1] = 0;
							}
						    if (mirror == 0 || i%2==0)
								v.addDegree(_geneticCode.getGene(j).getTheta());
							else
								v.addDegree(-_geneticCode.getGene(j).getTheta());
						    }
						}
					}
					// Apply the vector to the starting point to get the ending point.
					_endPointX[segment] = (int) Math.round(v.getX() + _startPointX[segment]);
					_endPointY[segment] = (int) Math.round(v.getY() + _startPointY[segment]);
				    // Calculate the bounding rectangle of this organism
				    left = Math.min(left, _endPointX[segment]);
				    right = Math.max(right, _endPointX[segment]);
				    top = Math.min(top, _endPointY[segment]);
				    bottom = Math.max(bottom, _endPointY[segment]);
				}
			}
		} else {
			for (i=0; i<symmetry; i++) {
				for (j=0; j<sequence; j++,segment++) {
					// Here, we take the vector that forms the segment, scale it depending on
					// the relative size of the organism and rotate it depending on the
					// symmetry and mirroring.
					v.setModulus(_geneticCode.getGene(j).getLength()*Utils.scale[_growthRatio-1]);
					_m[segment] = v.getModulus();
					_mass += _m[segment];
					if (j==0) {
						_startPointX[segment] = 0;
						_startPointY[segment] = 0;
						if (mirror == 0 || i%2==0)
							v.setTheta(_geneticCode.getGene(j).getTheta()+i*2*Math.PI/symmetry);
						else {
							v.setTheta(_geneticCode.getGene(j).getTheta()+(i-1)*2*Math.PI/symmetry);
							v.invertX();
						}
					} else {
						if (_geneticCode.getGene(j).getBranch() == -1) {
							_startPointX[segment] = _endPointX[segment - 1];
							_startPointY[segment] = _endPointY[segment - 1];
							if (mirror == 0 || i%2==0)
								v.addDegree(_geneticCode.getGene(j).getTheta());
							else
								v.addDegree(-_geneticCode.getGene(j).getTheta());
						} else {
						if (_geneticCode.getGene(j).getBranch() == 0) {
							_startPointX[segment] = 0;
						    _startPointY[segment] = 0;
						    if (mirror == 0 || i%2==0)
								v.setTheta(_geneticCode.getGene(j).getTheta()+i*2*Math.PI/symmetry);
							else {
								v.setTheta(_geneticCode.getGene(j).getTheta()+(i-1)*2*Math.PI/symmetry);
								v.invertX();
							}
						} else {
							_startPointX[segment] = _endPointX[(i * sequence) + _geneticCode.getGene(j).getBranch() - 1];
						    _startPointY[segment] = _endPointY[(i * sequence) + _geneticCode.getGene(j).getBranch() - 1];
						    if (mirror == 0 || i%2==0)
								v.addDegree(_geneticCode.getGene(j).getTheta());
							else
								v.addDegree(-_geneticCode.getGene(j).getTheta());
						    }
						}
					}
					// Apply the vector to the starting point to get the ending point.
					_endPointX[segment] = (int) Math.round(v.getX() + _startPointX[segment]);
					_endPointY[segment] = (int) Math.round(v.getY() + _startPointY[segment]);
				    // Calculate the bounding rectangle of this organism
				    left = Math.min(left, _endPointX[segment]);
				    right = Math.max(right, _endPointX[segment]);
				    top = Math.min(top, _endPointY[segment]);
				    bottom = Math.max(bottom, _endPointY[segment]);
				}
			}
		}
		_sizeRect.setBounds(left, top, right-left+1, bottom-top+1);
		// image center
		centerX = (left+right)>>1;
		centerY = (top+bottom)>>1;
		_mass = 1000 * _mass;
	    _mass = 0.001 * Math.round(_mass);
		_I = 0;
		for (i=0; i<_segments; i++) {
			// express points relative to the image center
			_startPointX[i]-=centerX;
			_startPointY[i]-=centerY;
			_endPointX[i]-=centerX;
			_endPointY[i]-=centerY;
			// calculate inertia moment
			// the mass center of a segment is its middle point
			cx = (_startPointX[i] + _endPointX[i]) / 2d;
			cy = (_startPointY[i] + _endPointY[i]) / 2d;
			// add the effect of this segment, following the parallel axis theorem
			_I += Math.pow(_m[i],3)/12d +
				_m[i] * cx*cx + cy*cy;// mass * length^2 (center is at 0,0)
		}
	}
	/**
	 * Given a vector, calculates the resulting vector after a rotation, a scalation and possibly
	 * after mirroring it.
	 * The rotation degree and the mirroring is found using the Utils.degree array, where parameter
	 * mirror is the row and step is the column. The step represents the repetition of this vector
	 * following the organism symmetry.
	 * The scalation is calculated using the Utils.scale coefficients, using the organism's
	 * _growthRatio to find the appropriate value.
	 *
	 * @param p  The end point of the vector. The starting point is (0,0).
	 * @param step  The repetition of the vectors pattern  we are calculating.
	 * @param mirror  If mirroring is applied to this organism 1, otherwise 0.
	 * @return  The translated vector.
	 */
/*	private Vector2D translate(Point p, int step, int mirror) {
		if (p.x == 0 && p.y == 0)
			return new Vector2D();

		double px = p.x;
		double py = p.y;

		px *= Utils.scale[_growthRatio - 1];
		py *= Utils.scale[_growthRatio - 1];

		Vector2D v = new Vector2D(px,py);
		v.addDegree(Utils.degree[mirror][step]);

		if (Utils.invertX[mirror][step] != 0)
			v.invertX();
		if (Utils.invertY[mirror][step] != 0)
			v.invertY();

		return v;
	}*/
	/**
	 * Tries to find a spare place in the world for this organism and place it.
	 * It also generates an identification number for the organism if it can be placed
	 * somewhere.
	 *
	 * @return  true if a suitable place has been found, false if not.
	 */
	private boolean placeRandom() {
		/* We try to place the organism in 12 different positions. If all of them
		 * are occupied, we return false.
		 */
		for (int i=12; i>0; i--) {
			/* Get a random point for the top left corner of the organism
			 * making sure it is inside the world.
			 */
			Point origin = new Point(
				Utils.random.nextInt(_world._width-_sizeRect.width),
				Utils.random.nextInt(_world._height-_sizeRect.height));
			setBounds(origin.x,origin.y,_sizeRect.width,_sizeRect.height);
			_dCenterX = _centerX = origin.x + (_sizeRect.width>>1);
			_dCenterY = _centerY = origin.y + (_sizeRect.height>>1);
			// Check that the position is not occupied.
			if (_world.genesisCheckHit(this) == null) {
				// Generate an identification
				_ID = _world.getNewId();
				if (Utils.ACCEPT_CONNECTIONS) {
					_geneticCode._cladeID = Utils.USER_NAME + ":" + Integer.toString(_ID);
				} else {
					_geneticCode._cladeID = Integer.toString(_ID);
				}
				// Maximum age that an organism can reach
				_max_age = Utils.MAX_AGE + (_segments/Utils.AGE_DIVISOR);
				// Calculates the energy required to reproduce this genetic code.
                _reproduceEnergy = (40 + 3 * _segments);
				// Maximum time that needs to elapse after a reproduction attempt
                _timeToReproduceMax = 8;
                int additionalcentersegments = 0;
        		for (int j = 0; j < _geneticCode.getNGenes(); j++) {
        			if ((_geneticCode.getGene(j).getBranch() != 0) || (j==0)) {
        				_timeToReproduceMax += 6;
        			} else {
        				additionalcentersegments += _symmetry;
        				if ((_symmetry + additionalcentersegments) > 8) {
        					_timeToReproduceMax += 9;
        				} else {
        					_timeToReproduceMax += 6;
        				}
        			}
        		}
				// Initialize effects
				segmentsCreateEffects();
				return true;
			}
		}
		// If we get here, we haven't find a place for this organism.
		return false;
	}
	/**
	 * Version for the second to last child of an organism.
	 * Tries to find a spare place near its parent for this organism and place it.
	 * It also generates an identification number for the organism if it can be placed
	 * somewhere and substracts its energy from its parent's energy.
	 *
	 * @return  true if a suitable place has been found, false if not.
	 */
	private boolean placeNearYellow(Organism parent) {
		int nPos = Utils.random.nextInt(8);
		char cPos = (char)(nPos + '0');
		// Try to put it in any possible position, starting from a randomly chosen one.
		for (int nSide = 0; nSide < 8; nSide++) {
			// Calculate candidate position
			_dCenterX = parent._dCenterX + (parent.width / 2 + width / 2 + 1) * Utils.side[nPos][0];
			_dCenterY = parent._dCenterY + (parent.height / 2 + height / 2 + 1) * Utils.side[nPos][1];
			_centerX = (int) _dCenterX;
			_centerY = (int) _dCenterY;
			calculateBounds();
			// Check this position is inside the world.
			if (isInsideWorld()) {
				// Check that it doesn't overlap with other organisms.
				if (_world.fastCheckHit(this) == null) {
					for(char c : parent._nPosition.toCharArray()) {
				        if (c == cPos) {
				        	if (parent._geneticCode.getDisperseChildren()) {
								dx = Utils.side[nPos][0];
								dy = Utils.side[nPos][1];
							} else {
								dx = parent.dx;
								dy = parent.dy;
							}
							// Generate an identification
							_ID = _world.getNewId();
							// Substract the energy from the parent
							parent._energy -= _energy;
							parent._nPosition = parent._nPosition.replace(cPos, 'X');
							return true;
				        }
					}
				}
			}
			nPos = (nPos + 1) % 8;
			cPos = (char)(nPos + '0');
		}
		// It can't be placed.
		return false;
	}
	/**
	 * Version for the first of more children of an organism.
	 * Tries to find a spare place near its parent for this organism and place it.
	 * It also generates an identification number for the organism if it can be placed
	 * somewhere and substracts its energy from its parent's energy.
	 *
	 * @return  true if a suitable place has been found, false if not.
	 */
	private boolean placeNearFirst(Organism parent) {
		int nPos = Utils.random.nextInt(8);
		// Try to put it in any possible position, starting from a randomly chosen one.
		for (int nSide = 0; nSide < 8; nSide++) {
			// Calculate candidate position
			_dCenterX = parent._dCenterX + (parent.width / 2 + width / 2 + 1) * Utils.side[nPos][0];
			_dCenterY = parent._dCenterY + (parent.height / 2 + height / 2 + 1) * Utils.side[nPos][1];
			_centerX = (int) _dCenterX;
			_centerY = (int) _dCenterY;
			calculateBounds();
			// Check this position is inside the world.
			if (isInsideWorld()) {
				// Check that it doesn't overlap with other organisms.
				if (_world.fastCheckHit(this) == null) {
					if (parent._geneticCode.getDisperseChildren()) {
						dx = Utils.side[nPos][0];
						dy = Utils.side[nPos][1];
					} else {
						dx = parent.dx;
						dy = parent.dy;
					}
					// Generate an identification
					_ID = _world.getNewId();
					// Substract the energy from the parent
					parent._energy -= _energy;
					char cPos = (char)(nPos + '0');
					parent._nPosition = parent._nPosition.replace(cPos, 'X');
					return true;
				}
			}
			nPos = (nPos + 1) % 8;
		}
		// It can't be placed.
		return false;
	}
	/**
	 * Version for the only child of an organism.
	 * Tries to find a spare place near its parent for this organism and place it.
	 * It also generates an identification number for the organism if it can be placed
	 * somewhere and substracts its energy from its parent's energy.
	 *
	 * @return  true if a suitable place has been found, false if not.
	 */
	private boolean placeNear(Organism parent) {
		int nPos = Utils.random.nextInt(8);
		// Try to put it in any possible position, starting from a randomly chosen one.
		for (int nSide = 0; nSide < 8; nSide++) {
			// Calculate candidate position
			_dCenterX = parent._dCenterX + (parent.width / 2 + width / 2 + 1) * Utils.side[nPos][0];
			_dCenterY = parent._dCenterY + (parent.height / 2 + height / 2 + 1) * Utils.side[nPos][1];
			_centerX = (int) _dCenterX;
			_centerY = (int) _dCenterY;
			calculateBounds();
			// Check this position is inside the world.
			if (isInsideWorld()) {
				// Check that it doesn't overlap with other organisms.
				if (_world.fastCheckHit(this) == null) {
					if (parent._geneticCode.getDisperseChildren()) {
						dx = Utils.side[nPos][0];
						dy = Utils.side[nPos][1];
					} else {
						dx = parent.dx;
						dy = parent.dy;
					}
					// Generate an identification
					_ID = _world.getNewId();
					// Substract the energy from the parent
					parent._energy -= _energy;
					return true;
				}
			}
			nPos = (nPos + 1) % 8;
		}
		// It can't be placed.
		return false;
	}
	/**
	 * Transforms an organism after infected by coral.
	 * It also generates an identification number for the organism
	 * and substracts its energy from its parent's energy.
	 *
	 * @return  true if a suitable place has been found, false if not.
	 */
	private boolean transform(Organism parent) {
		_dCenterX = parent._dCenterX;
		_dCenterY = parent._dCenterY;
		_centerX = (int) _dCenterX;
		_centerY = (int) _dCenterY;
		calculateBounds();
		// Check this position is inside the world and check that it doesn't overlap with other organisms.
		if ((isInsideWorld()) && (_world.transformCheckHit(this) == null)) {
			dx = parent.dx;
			dy = parent.dy;
			// Generate an identification
			_ID = _world.getNewId();
			// Substract the energy from the parent
			parent._energy -= _energy;
			return true;
		} else {
			int nPos = Utils.random.nextInt(8);
			// Try to put it in any possible position, starting from a randomly chosen one.
			for (int nSide = 0; nSide < 8; nSide++) {
				// Calculate candidate position
				_dCenterX = parent._dCenterX + (parent.width / 2 + width / 2 + 1) * Utils.side[nPos][0];
				_dCenterY = parent._dCenterY + (parent.height / 2 + height / 2 + 1) * Utils.side[nPos][1];
				_centerX = (int) _dCenterX;
				_centerY = (int) _dCenterY;
				calculateBounds();
				// Check this position is inside the world.
				if (isInsideWorld()) {
					// Check that it doesn't overlap with other organisms.
					if (_world.fastCheckHit(this) == null) {
						dx = parent.dx;
						dy = parent.dy;
						// Generate an identification
						_ID = _world.getNewId();
						// Substract the energy from the parent
						parent._energy -= _energy;
						return true;
					}
				}
				nPos = (nPos + 1) % 8;
			}
		}
		// It can't be placed.
		return false;
	}
	/**
	 * Draws this organism to a graphics context.
	 * The organism is drawn at its position in the world.
	 *
	 * @param g  The graphics context to draw to.
	 */
	public void draw(Graphics g) {
		int i;
		if (_framesColor != 0) {
			if (_haseyes) {
				// Draw all the organism in the same color except the eyes
				for (i=0; i<_segments; i++) {
					if (_mphoto[i] == -13) {
						g.setColor(_segColor[i]);
					} else {
						g.setColor(_color);
					}
					g.drawLine(
						x1[i] + _centerX,
						y1[i] + _centerY,
						x2[i] + _centerX,
						y2[i] + _centerY);
				}
			} else {
				// Draw all the organism in the same color
				g.setColor(_color);
				for (i=0; i<_segments; i++)
					g.drawLine(
						x1[i] + _centerX,
						y1[i] + _centerY,
						x2[i] + _centerX,
						y2[i] + _centerY);
			}
		} else {
			if ((alive) || (_haseyes)) {
				for (i=0; i<_segments; i++) {
					g.setColor(_segColor[i]);
					g.drawLine(
							x1[i] + _centerX,
							y1[i] + _centerY,
							x2[i] + _centerX,
							y2[i] + _centerY);
				}
			} else {
				g.setColor(Utils.ColorBROWN);
				for (i=0; i<_segments; i++) {
					g.drawLine(
							x1[i] + _centerX,
							y1[i] + _centerY,
							x2[i] + _centerX,
							y2[i] + _centerY);
				}
			}
		}
	}
	/**
	 * Calculates the position of all organism points in the world, depending on
	 * its rotation. It also calculates the bounding rectangle of the organism.
	 * This method must be called from outside this class only when doing
	 * manual drawing.
	 *
	 * @param force  To avoid calculations, segments position are only calculated
	 * if the organism's rotation has changed in the last frame. If it is necessary
	 * to calculate them even when the rotation hasn't changed, assign true to this
	 * parameter.
	 */
	public void calculateBounds() {
		double left=java.lang.Double.MAX_VALUE, right=java.lang.Double.MIN_VALUE,
		top=java.lang.Double.MAX_VALUE, bottom=java.lang.Double.MIN_VALUE;

		final double sin = Math.sin(_theta);
		final double cos = Math.cos(_theta);
		for (int i=0; i < _segments; i++) {
			x1[i] = (int) (_startPointX[i] * cos - _startPointY[i] * sin);
			y1[i] = (int) (_startPointX[i] * sin + _startPointY[i] * cos);
			x2[i] = (int) (_endPointX[i] * cos - _endPointY[i] * sin);
			y2[i] = (int) (_endPointX[i] * sin + _endPointY[i] * cos);

			// Finds the rectangle that comprises the organism
			left   = Utils.min(left,   x1[i] + _dCenterX, x2[i] + _dCenterX);
			right  = Utils.max(right,  x1[i] + _dCenterX, x2[i] + _dCenterX);
			top    = Utils.min(top,    y1[i] + _dCenterY, y2[i] + _dCenterY);
			bottom = Utils.max(bottom, y1[i] + _dCenterY, y2[i] + _dCenterY);
		}
		setBounds((int)left, (int)top, (int)(right-left+1)+1, (int)(bottom-top+1)+1);
		_lastTheta = _theta;
	}
	/**
	 * If its the time for this organism to grow, calculates its new segments and speed.
	 * An active organism can grow once every 8 frames until it gets its maximum size.
	 */
	private void grow() {
		if ((_growthRatio > 1) && (_energy >= _mass/10)) {
			if (((active) || (_growthRatio == 16)) && ((_age & 0x07) == 0x07)) {
				_growthRatio--;
				double m = _mass;
				double I = _I;
				symmetric();
				// Cynetic energy is constant. If mass changes, speed must also change.
				m = Math.sqrt(m/_mass);
				dx *= m;
				dy *= m;
				dtheta *= Math.sqrt(I/_I);
				hasGrown = 1;
				if (_sporeversion < -1) {
					_sporeversion = -1;
				}
			} else {
				hasGrown = 0;
			}
		} else if (_energy < _mass/12) {
			if (_growthRatio < 15) {
				_growthRatio++;
				double m = _mass;
				double I = _I;
				symmetric();
				// Cynetic energy is constant. If mass changes, speed must also change.
				m = Math.sqrt(m/_mass);
				dx *= m;
				dy *= m;
				dtheta *= Math.sqrt(I/_I);
				hasGrown = -1;
			} else {
				hasGrown = 0;
				if ((_sporeversion == -1) && (active) && (((!_isinfectious) && (!_isblond) && (_framesColor <= 9)) || (_framesColor <= 0))) {
					_isfrozen =false;
					_isinjured =false;
					_allfrozen =false;
					if (_geneticCode.getModifiesspore() >= 10) {
						cyanSpore();
						setColor(Color.CYAN);
					} else {
						blueSpore();
						setColor(Color.BLUE);
					}
					if (_infectedGeneticCode != null) {
		    			_infectedGeneticCode = null;
		    			_savedGeneticCode = null;
		    		}
				}
			}
		} else {
			hasGrown = 0;
		}
	}

	/**
	 * Makes this organism reproduce. It tries to create at least one
	 * child and at maximum 8 (depending on the number of yellow segments
	 * of the organism) and put them in the world.
	 */
	public void reproduce() {
		_timeToReproduce = _timeToReproduceMax;
		// Calculate number of children
		if (_issilver) {
			if ((_isaconsumer) || (_isafungus) || (_isinfectious)) {
				       if (_nTotalChildren <    1) {
				    _nChildren = 1;
				} else if (_nTotalChildren <    9) {
					_nChildren = 2;
				} else if (_nTotalChildren <   36) {
					_nChildren = 3;
				} else if (_nTotalChildren <  100) {
					_nChildren = 4;
				} else if (_nTotalChildren <  225) {
					_nChildren = 5;
				} else if (_nTotalChildren <  441) {
				    _nChildren = 6;
				} else if (_nTotalChildren <  784) {
					_nChildren = 7;
				} else if (_nTotalChildren >= 784) {
					_nChildren = 8;
				}
			} else {
				       if (_nTotalChildren <   2) {
				    _nChildren = 2;
				} else if (_nTotalChildren <   5) {
					_nChildren = 3;
				} else if (_nTotalChildren <   9) {
					_nChildren = 4;
				} else if (_nTotalChildren <  14) {
					_nChildren = 5;
				} else if (_nTotalChildren <  20) {
					_nChildren = 6;
				} else if (_nTotalChildren <  27) {
				    _nChildren = 7;
				} else if (_nTotalChildren >= 27) {
					_nChildren = 8;
				}
			}
	    } else {
	    	_nChildren = 1;
	    }
		if ((_yellowCounter > 0) && (useEnergy(Utils.YELLOW_ENERGY_CONSUMPTION))) {
			_nChildren += _yellowCounter;
		}
		if (_isauburn && _infectedGeneticCode != null && (useEnergy(Utils.AUBURN_ENERGY_CONSUMPTION))) {
			_nChildren += 1;
		}
		Organism newOrg;

        if (_sporeversion > 0) {
        	for (int i=0; i < Utils.between(_nChildren,1,8); i++) {
				newOrg = new Organism(_world);
				if (newOrg.inherit(this, i==0)) {
					// It can be created
					_nTotalChildren++;
					_world.addOrganism(newOrg,this);
					_savedGeneticCode = null;
				} else {
					break;
				}
			}
		} else {
			for (int i=0; i < Utils.between(_nChildren,1,8); i++) {
				newOrg = new Organism(_world);
				if (newOrg.inherit(this, i==0)) {
					// It can be created
					_nTotalChildren++;
					_world.addOrganism(newOrg,this);
					_infectedGeneticCode = null;
					_savedGeneticCode = null;
				} else {
					break;
				}
			}
		}
	}
	/**
	 * Makes this organism with blond segments reproduce early. It tries
	 * to create at least one child and at maximum 8 (depending on the
	 * number of yellow segments of the organism) and put them in the world.
	 */
	public void reproduceEarly() {
		_timeToReproduce = _timeToReproduceMax;
		// Calculate number of children
		if (_issilver) {
			if ((_isaconsumer) || (_isafungus) || (_isinfectious)) {
				       if (_nTotalChildren <    1) {
				    _nChildren = 1;
				} else if (_nTotalChildren <    9) {
					_nChildren = 2;
				} else if (_nTotalChildren <   36) {
					_nChildren = 3;
				} else if (_nTotalChildren <  100) {
					_nChildren = 4;
				} else if (_nTotalChildren <  225) {
					_nChildren = 5;
				} else if (_nTotalChildren <  441) {
				    _nChildren = 6;
				} else if (_nTotalChildren <  784) {
					_nChildren = 7;
				} else if (_nTotalChildren >= 784) {
					_nChildren = 8;
				}
			} else {
				       if (_nTotalChildren <   2) {
				    _nChildren = 2;
				} else if (_nTotalChildren <   5) {
					_nChildren = 3;
				} else if (_nTotalChildren <   9) {
					_nChildren = 4;
				} else if (_nTotalChildren <  14) {
					_nChildren = 5;
				} else if (_nTotalChildren <  20) {
					_nChildren = 6;
				} else if (_nTotalChildren <  27) {
				    _nChildren = 7;
				} else if (_nTotalChildren >= 27) {
					_nChildren = 8;
				}
			}
	    } else {
	    	_nChildren = 1;
	    }
		if ((_yellowCounter > 0) && (useEnergy(Utils.YELLOW_ENERGY_CONSUMPTION))) {
			_nChildren += _yellowCounter;
		}
		Organism newOrg;
		boolean useblondcosts =true;

		for (int i=0; i < Utils.between(_nChildren,1,8); i++) {
			newOrg = new Organism(_world);
			if (newOrg.inherit(this, i==0)) {
				// It can be created
				_nTotalChildren++;
				_world.addOrganism(newOrg,this);
				if (_isblond) {
				    if (useblondcosts) {
					    useblondcosts =false;
						useEnergy(Math.min(_energy, Utils.BLOND_ENERGY_CONSUMPTION));
					}
				}
				_savedGeneticCode = null;
			} else {
				break;
			}
		}
	}
	/**
	 * Makes this plant organism with blond segments reproduce early. It tries
	 * to create at least one child and at maximum 8 (depending on the
	 * number of yellow segments of the organism) and put them in the world.
	 */
	public void reproduceEarlyPlant() {
		_timeToReproduce = _timeToReproduceMax;
		// Calculate number of children
		if (_issilver) {
			if ((_isaconsumer) || (_isafungus) || (_isinfectious)) {
				       if (_nTotalChildren <    1) {
				    _nChildren = 1;
				} else if (_nTotalChildren <    9) {
					_nChildren = 2;
				} else if (_nTotalChildren <   36) {
					_nChildren = 3;
				} else if (_nTotalChildren <  100) {
					_nChildren = 4;
				} else if (_nTotalChildren <  225) {
					_nChildren = 5;
				} else if (_nTotalChildren <  441) {
				    _nChildren = 6;
				} else if (_nTotalChildren <  784) {
					_nChildren = 7;
				} else if (_nTotalChildren >= 784) {
					_nChildren = 8;
				}
			} else {
				       if (_nTotalChildren <   2) {
				    _nChildren = 2;
				} else if (_nTotalChildren <   5) {
					_nChildren = 3;
				} else if (_nTotalChildren <   9) {
					_nChildren = 4;
				} else if (_nTotalChildren <  14) {
					_nChildren = 5;
				} else if (_nTotalChildren <  20) {
					_nChildren = 6;
				} else if (_nTotalChildren <  27) {
				    _nChildren = 7;
				} else if (_nTotalChildren >= 27) {
					_nChildren = 8;
				}
			}
	    } else {
	    	_nChildren = 1;
	    }
		if ((_yellowCounter > 0) && (useEnergy(Utils.YELLOW_ENERGY_CONSUMPTION))) {
			_nChildren += _yellowCounter;
		}
		Organism newOrg;
		boolean useblondcosts =true;

		for (int i=0; i < Utils.between(_nChildren,1,8); i++) {
			newOrg = new Organism(_world);
			if (newOrg.inherit(this, i==0)) {
				// It can be created
				_nTotalChildren++;
				_world.addOrganism(newOrg,this);
				if (_isblond) {
				    if (useblondcosts) {
					    useblondcosts =false;
					    if ((_isonlyc4 == 2) || (_reproducelate > 0)) {
					    	useEnergy(Math.min(_energy, Utils.BLOND_ENERGY_CONSUMPTION));
					    } else {
					    	useEnergy(Math.min(_energy, Utils.BLOND_ENERGY_CONSUMPTION * (_reproduceEnergy - _earlyReproduceEnergy)));
					    }
					}
				}
				_savedGeneticCode = null;
			} else {
				break;
			}
		}
	}
	/**
	 * Makes this virus with blond segments reproduce. It tries
	 * to create one child and put it in the world.
	 */
	public void reproduceEarlyVirus() {
		_timeToReproduce = _timeToReproduceMax;
	    _nChildren = 1;
		Organism newOrg;

		newOrg = new Organism(_world);
		if (newOrg.inherit(this, true)) {
			// It can be created
			_nTotalChildren++;
			_world.addOrganism(newOrg,this);
			if (_isblond) {
				useEnergy(Math.min(_energy, Utils.BLOND_ENERGY_CONSUMPTION));
			}
			if ((_sporeversion <= 0) || (_sporeversion == 3) || (_sporeversion == 4)) {
				_timeToReproduce = 0;
				_savedGeneticCode = newOrg._geneticCode;
			} else {
				if (_sporeversion <= 2) {
					_timeToReproduce = 20;
					_savedGeneticCode = newOrg._geneticCode;
				} else {
					_savedGeneticCode = null;
				}
			}
		}
	}
	/**
	 * Makes this organism with blue or cyan spores reproduce at death.
	 * It tries to create one child as a pore and put it in the world.
	 */
	public void reproduceSpore() {
		_timeToReproduce = _timeToReproduceMax;
	    _nChildren = 1;
		Organism newOrg;

		newOrg = new Organism(_world);
		if (_infectedGeneticCode != null) {
			_infectedGeneticCode = null;
			_savedGeneticCode = null;
		}
		if (newOrg.inherit(this, true)) {
			// It can be created
			_nTotalChildren++;
			_world.addOrganism(newOrg,this);
			_savedGeneticCode = null;
			if ((newOrg._sporeversion < 0) && (newOrg.active)) {
				if (newOrg._geneticCode.getModifiesspore() >= 10) {
					newOrg.cyanSpore();
				} else {
					newOrg.blueSpore();
				}
			}
		}
	}
	/**
	 * Makes this organism create many virus and put them in the world.
	 * Used for viruses with yellow segments.
	 */
	public void reproduceYellow() {
		if (_timeToReproduce < _timeToReproduceMax) {
			_timeToReproduce = _timeToReproduceMax;
		}
		boolean victimized =false;
		// Calculate number of children
		if (_issilver) {
			if ((_isaconsumer) || (_isafungus) || (_isinfectious)) {
				       if (_nTotalChildren <    1) {
				    _nChildren = 1;
				} else if (_nTotalChildren <    9) {
					_nChildren = 2;
				} else if (_nTotalChildren <   36) {
					_nChildren = 3;
				} else if (_nTotalChildren <  100) {
					_nChildren = 4;
				} else if (_nTotalChildren <  225) {
					_nChildren = 5;
				} else if (_nTotalChildren <  441) {
				    _nChildren = 6;
				} else if (_nTotalChildren <  784) {
					_nChildren = 7;
				} else if (_nTotalChildren >= 784) {
					_nChildren = 8;
				}
			} else {
				       if (_nTotalChildren <   2) {
				    _nChildren = 2;
				} else if (_nTotalChildren <   5) {
					_nChildren = 3;
				} else if (_nTotalChildren <   9) {
					_nChildren = 4;
				} else if (_nTotalChildren <  14) {
					_nChildren = 5;
				} else if (_nTotalChildren <  20) {
					_nChildren = 6;
				} else if (_nTotalChildren <  27) {
				    _nChildren = 7;
				} else if (_nTotalChildren >= 27) {
					_nChildren = 8;
				}
			}
	    } else {
	    	_nChildren = 1;
	    }
		if ((_yellowCounter > 0) && (useEnergy(Utils.YELLOW_ENERGY_CONSUMPTION))) {
			_nChildren += _yellowCounter;
		}
		if (_isauburn && (useEnergy(Utils.AUBURN_ENERGY_CONSUMPTION))) {
			_nChildren += 1;
		}
		Organism newOrg;

		for (int i=0; i < Utils.between(_nVirusChildren,1,8); i++) {
			if (_infectedGeneticCode != null) {
				newOrg = new Organism(_world);
				if (newOrg.inherit(this, i==0)) {
					// It can be created
					_nTotalChildren++;
					_world.addOrganism(newOrg,this);
					_savedGeneticCode = null;
					victimized =true;
				} else {
					break;
				}
			}
		}
		if (victimized == false) {
			_nVirusChildren = 0;
		}
	}
	/**
	 * Makes this organism create a virus and put it in the world.
	 */
	public void reproduceVirus() {
		_nVirusChildren = 1;
		boolean victimized =false;
		// Calculate number of children
		if (_issilver) {
			if ((_isaconsumer) || (_isafungus) || (_isinfectious)) {
				       if (_nTotalChildren <    1) {
				    _nChildren = 1;
				} else if (_nTotalChildren <    9) {
					_nChildren = 2;
				} else if (_nTotalChildren <   36) {
					_nChildren = 3;
				} else if (_nTotalChildren <  100) {
					_nChildren = 4;
				} else if (_nTotalChildren <  225) {
					_nChildren = 5;
				} else if (_nTotalChildren <  441) {
				    _nChildren = 6;
				} else if (_nTotalChildren <  784) {
					_nChildren = 7;
				} else if (_nTotalChildren >= 784) {
					_nChildren = 8;
				}
			} else {
				       if (_nTotalChildren <   2) {
				    _nChildren = 2;
				} else if (_nTotalChildren <   5) {
					_nChildren = 3;
				} else if (_nTotalChildren <   9) {
					_nChildren = 4;
				} else if (_nTotalChildren <  14) {
					_nChildren = 5;
				} else if (_nTotalChildren <  20) {
					_nChildren = 6;
				} else if (_nTotalChildren <  27) {
				    _nChildren = 7;
				} else if (_nTotalChildren >= 27) {
					_nChildren = 8;
				}
			}
	    } else {
	    	_nChildren = 1;
	    }
		if ((_yellowCounter > 0) && (useEnergy(Utils.YELLOW_ENERGY_CONSUMPTION))) {
			_nChildren += _yellowCounter*Utils.scale[_growthRatio-1];
		}
		if (_isauburn && (useEnergy(Utils.AUBURN_ENERGY_CONSUMPTION))) {
			_nChildren += 1;
		}
		Organism newOrg;

		newOrg = new Organism(_world);
		if (newOrg.inherit(this, true)) {
			// It can be created
			_nTotalChildren++;
			_world.addOrganism(newOrg,this);
			_infectedGeneticCode = null;
			_savedGeneticCode = null;
			if (_timeToReproduce < _timeToReproduceMax) {
				_timeToReproduce = _timeToReproduceMax;
			}
			victimized =true;
		}
		if (victimized == false) {
			_nVirusChildren = 0;
		}
	}
	/**
	 * Makes this organism create a foreign virus and put it in the world.
	 */
	public void reproduceforeignVirus() {
		boolean victimized =false;
		// Calculate number of children
		if (_issilver) {
			if ((_isaconsumer) || (_isafungus) || (_isinfectious)) {
				       if (_nTotalChildren <    1) {
				    _nChildren = 1;
				} else if (_nTotalChildren <    9) {
					_nChildren = 2;
				} else if (_nTotalChildren <   36) {
					_nChildren = 3;
				} else if (_nTotalChildren <  100) {
					_nChildren = 4;
				} else if (_nTotalChildren <  225) {
					_nChildren = 5;
				} else if (_nTotalChildren <  441) {
				    _nChildren = 6;
				} else if (_nTotalChildren <  784) {
					_nChildren = 7;
				} else if (_nTotalChildren >= 784) {
					_nChildren = 8;
				}
			} else {
				       if (_nTotalChildren <   2) {
				    _nChildren = 2;
				} else if (_nTotalChildren <   5) {
					_nChildren = 3;
				} else if (_nTotalChildren <   9) {
					_nChildren = 4;
				} else if (_nTotalChildren <  14) {
					_nChildren = 5;
				} else if (_nTotalChildren <  20) {
					_nChildren = 6;
				} else if (_nTotalChildren <  27) {
				    _nChildren = 7;
				} else if (_nTotalChildren >= 27) {
					_nChildren = 8;
				}
			}
	    } else {
	    	_nChildren = 1;
	    }
		if (_nVirusChildren != -1) {
			_nVirusChildren = -1;
			if (_isauburn && (useEnergy(Utils.AUBURN_ENERGY_CONSUMPTION))) {
				_nChildren += 1;
			}
		}
		Organism newOrg;

		newOrg = new Organism(_world);
		if (newOrg.inherit(this, true)) {
			// It can be created
			_nTotalChildren++;
			_world.addOrganism(newOrg,this);
			_infectedGeneticCode = null;
			_savedGeneticCode = null;
			if (_timeToReproduce < _timeToReproduceMax) {
				_timeToReproduce = _timeToReproduceMax;
			}
			victimized =true;
		}
		if (victimized == false) {
			_nVirusChildren = 0;
		}
	}
	/**
	 * Makes this organism transform into its killer
	 */
	public void transform() {
		Organism newOrg;

		newOrg = new Organism(_world);
		if (newOrg.inheritTransformation(this, true)) {
			// It can be created
			_world.addOrganism(newOrg,this);
			_infectedGeneticCode = null;
			_savedGeneticCode = null;
		}
	}
	/**
	 * Executes the organism's movement for this frame.
	 * This includes segments upkeep and activation,
	 * movement, growth, collision detection, reproduction,
	 * respiration and death.
	 */
	public boolean move() {
		lastFrame.setBounds(this);
		// Apply rubbing effects
		rubbingFramesEffects();
		// Check if it can grow or shrink
		grow();
		// Ticker for the time the organism is shown in color
		if (_framesColor != 0) {
			if (_framesColor > 0) {
				_framesColor--;
			} else {
				_framesColor++;
			}
		}
		// Movement and collisions
		if (dx!=0 || dy!=0 || dtheta!=0 || hasGrown!=0 || (_haseyes && _canmove)) {
			boolean collision = false;
			// Movement
			double dxbak=dx, dybak=dy, dthetabak=dtheta;
			offset(dx,dy,dtheta);
			calculateBounds();
			// Check it is inside the world
			collision = !isInsideWorld();
			// Collision detection with biological corridors
			if (_world._corridorexists == true) {
				if (alive) {
					OutCorridor c = _world.checkHitCorridor(this);
					if (c != null && c.canSendOrganism()) {
						if (c.sendOrganism(this))
							return false;
					}
				}
			}
			// Collision detection with other organisms.
			if (_world.checkHit(this) != null)
				collision = true;
			// If there is a collision, undo movement.
			if (collision) {
				if (hasMoved == true) {
					hasMoved = false;
				}
				offset(-dxbak,-dybak,-dthetabak);
				if (hasGrown!=0) {
					_growthRatio+=hasGrown;
					symmetric();
					if (_updateEffects == 1) {
						_updateEffects = 0;
					}
				}
				calculateBounds();
			} else {
				if (hasMoved == false) {
					hasMoved = true;
				}
			}
		} else {
			if (hasMoved == true) {
				hasMoved = false;
			}
		}
		if (active) {
			// Apply segment effects for this frame.
			if (_usefriendeffects == 2) {
				// Check if the organism can and has to heal itself later
				if ((_healing > 0) && (_isinjured)) {
					_useextraeffects = true;
				}
				// Try to remove a virus, or restore organism to have live births
				if (_antiviral > 0) {
					if (_infectedGeneticCode != null) {
						if (Utils.random.nextInt(Utils.IMMUNE_SYSTEM) <= _antiviral) {
							if (useEnergy(Utils.MINT_ENERGY_CONSUMPTION)) {
					    		_infectedGeneticCode = null;
					    		_savedGeneticCode = null;
								setColor(Utils.ColorMINT);
							}
						}
					}
					if ((_fallowinhibition > 0) && (_timeToReproduce <= _timeToReproduceMax)) {
						if (Utils.random.nextInt(Utils.IMMUNE_SYSTEM) <= _antiviral) {
							if (useEnergy(Utils.MINT_ENERGY_CONSUMPTION)) {
								_fallowinhibition = 0;
								setColor(Utils.ColorMINT);
							}
						}
					}
				}
			    if (_createlavender > 0) {
			    	// Lavender shield production
			    	if (_lavender < Utils.LAVENDER_SHIELD) {
						_lavender += _createlavender;
						if (_lavender >= Utils.LAVENDER_SHIELD) {
							_lavender = Utils.LAVENDER_SHIELD;
						}
					}
			    	// Fallow induced reproduction delay is reduced faster
			    	if (_timeToReproduce > _timeToReproduceMax) {
			    		_timeToReproduce -= (_createlavender/20);
						if (_timeToReproduce < _timeToReproduceMax) {
							_timeToReproduce = _timeToReproduceMax;
						}
					}
			    }
			}
			if (_isaplant) {
				if (_islime) {
				    if (_world.fastCheckHit(this) != null) {
				    	if (_crowded == false) {
				    		_crowded =true;
				    		_updateEffects = 2;
				    	}
				    } else {
				    	if (_crowded == true) {
				    		_crowded =false;
				    		_updateEffects = 2;
				    	}
				    }
				}
				if (_summerinactivity > -1) {
					if (_world.getFrame() == 0) {
						_updateEffects = 2;
						_useextraeffects = true;
					}
				}
				if (_updateEffects > 0) {
					if (_isonlyc4 > 0) {
						if (_useextraeffects) {
							_useextraeffects = false;
						}
						c4UpdateEffects();
					} else {
						if (_useextraeffects) {
							_useextraeffects = false;
							plantsExtraUpdateEffects();
						} else {
							if (_isinjuredplant) {
								plantsExtraUpdateEffects();
							} else {
								plantsUpdateEffects();
							}
						}
					}
					_updateEffects = 0;
					if ((_reproducelate > 0) && (_isinfectious)) {
						// Calculate reproduction energy for flower segments of viruses
						_reproduceEnergy = (40 + 3 * _segments) + (int)(_reproducelate*Utils.scale[_growthRatio-1]);
					}
				} else {
					if (_useextraeffects) {
						_useextraeffects = false;
						if (_jadefactor > 0) {
							jadeExtraEffects();
						} else {
							if ((_healing > 0) && (_isinjured)) {
								segmentsHealingEffects();
							} else {
								if (_useframemovement) {
									segmentsExtraEffects();
								} else {
									segmentsExtraPlantsEffects();
								}
							}
							if ((_leafphoto > 0) && (_framesColor > 0)) {
								leafExtraEffects();
							} else {
								// Get chemotrophic energy from methane
							    if (_methanotrophy > 0) {
								    if ((_age & 0x07) == 0x00) {
									    _energy += _world.methanotrophy(_methanotrophy);
								    }
								    if (_photosynthesis > 0) {
								    	//Get sun's energy
										if (_colonyPhotosynthesis > 0) {
											if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
												if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
													_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
												}
											} else {
												_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
											}
											_colonyPhotosynthesis = 0;
										} else {
											if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
												if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
													_energy += _world.photosynthesis(_photosynthesis);
												}
											} else {
												_energy += _world.photosynthesis(_photosynthesis);
											}
										}
								    }
							    } else {
							    	//Get sun's energy
							    	if (_colonyPhotosynthesis > 0) {
										if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
											if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
												_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
											}
										} else {
											_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
										}
										_colonyPhotosynthesis = 0;
									} else {
										if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
											if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
												_energy += _world.photosynthesis(_photosynthesis);
											}
										} else {
											_energy += _world.photosynthesis(_photosynthesis);
										}
									}
							    }
							}
						}
					} else {
						if (_useframemovement) {
							segmentsFrameEffects();
						}
						if ((_leafphoto > 0) && (_framesColor > 0)) {
							leafExtraEffects();
						} else {
						    // Get chemotrophic energy from methane
						    if (_methanotrophy > 0) {
							    if ((_age & 0x07) == 0x00) {
								    _energy += _world.methanotrophy(_methanotrophy);
							    }
							    if (_photosynthesis > 0) {
							    	//Get sun's energy
							    	if (_colonyPhotosynthesis > 0) {
										if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
											if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
												_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
											}
										} else {
											_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
										}
										_colonyPhotosynthesis = 0;
									} else {
										if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
											if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
												_energy += _world.photosynthesis(_photosynthesis);
											}
										} else {
											_energy += _world.photosynthesis(_photosynthesis);
										}
									}
							    }
						    } else {
						    	//Get sun's energy
						    	if (_colonyPhotosynthesis > 0) {
									if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
										if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
											_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
										}
									} else {
										_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
									}
									_colonyPhotosynthesis = 0;
								} else {
									if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
										if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
											_energy += _world.photosynthesis(_photosynthesis);
										}
									} else {
										_energy += _world.photosynthesis(_photosynthesis);
									}
								}
						    }
						}
					}
				}
				if (_spin > 0) {
					if (_clockwise) {
						dtheta=Utils.between(dtheta+(_spin*Utils.springscale[_growthRatio-1])*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
					} else {
					    dtheta=Utils.between(dtheta-(_spin*Utils.springscale[_growthRatio-1])*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
					}
				}
			} else {
				if (_updateEffects > 0) {
					if (_useextraeffects) {
						_useextraeffects = false;
					}
					segmentsUpdateEffects();
					_updateEffects = 0;
					if ((_reproducelate > 0) && (_isinfectious)) {
						// Calculate reproduction energy for flower segments of viruses
						_reproduceEnergy = (40 + 3 * _segments) + (int)(_reproducelate*Utils.scale[_growthRatio-1]);
					}
				} else {
					if (_useextraeffects) {
						_useextraeffects = false;
						if ((_healing > 0) && (_isinjured)) {
							segmentsHealingEffects();
						} else {
							segmentsExtraEffects();
						}
					} else {
						if (_useframemovement) {
							segmentsFrameEffects();
						}
					}
				}
			}
			// Reset dodging
			if (_candodge == true) {
				if (_hasdodged == true) {
					_dodge =false;
					_hasdodged =false;
				} else if (_dodge == false) {
				    _dodge =true;
				}
			}
			// Substract one to the time needed to reproduce
			if (_timeToReproduce > 0) {
				_timeToReproduce--;
			} else {
				// Calculate reproduction energy for blond segments and check if it can reproduce
				if (_earlyReproduceEnergy > 0) {
					if (_energy > _earlyReproduceEnergy) {
					    if (_infectedGeneticCode == null) {
							if (_isaplant) {
							    if (_growthRatio==1) {
									reproduceEarlyPlant();
								}
							} else {
								if (_isaconsumer) {
								    if (_growthRatio==1) {
										reproduceEarly();
									}
								} else {
									if (_isafungus) {
										if (_sporeversion < 0) {
											if (_growthRatio==1) {
												reproduceEarly();
											}
										} else {
											if (_growthRatio<16) {
												reproduceEarly();
											}
										}
									} else {
										if (_transfersenergy) {
											if (_growthRatio<16) {
												reproduceEarly();
											}
										} else {
											if (_growthRatio<16) {
												reproduceEarlyVirus();
											}
										}
									}
								}
						    }
						} else {
							// Check if it can reproduce: it needs enough energy and to be adult
							if (_energy > _reproduceEnergy && _growthRatio==1) {
								if ((_antiviral == 0) || (_lavender == 0)) {
									reproduce();
								} else {
									if (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/16)) {
										if (_lavender > 0) {
											lavenderinhibition();
										}
										if (_lavender <= 0) {
											reproduce();
										}
									} else {
										reproduce();
									}
								}
							}
					    }
					}
				} else {
				// Check if it can reproduce: it needs enough energy and to be adult
					if (_energy > _reproduceEnergy && _growthRatio==1) {
						if ((_antiviral == 0) || (_lavender == 0) || (_infectedGeneticCode == null)) {
							reproduce();
						} else {
							if (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/16)) {
								if (_lavender > 0) {
									lavenderinhibition();
								}
								if (_lavender <= 0) {
									reproduce();
								}
							} else {
								reproduce();
							}
						}
					}
				}
			}
			// Maintenance of active organisms
			breath();
		} else {
			// Maintenance of dead organisms and spores
			decay();
		}
		// Check that the organism has energy after this frame
		return _energy > Utils.tol;
	}
	/**
	 * Makes the organism decay an amount of energy using the
	 * decomposition process (also used for pink feeding).
	 *
	 * @param q  The quantity of energy to decay.
	 * @return  true if the organism has enough energy and there is
	 * enough oxygen in the atmosphere, false otherwise.
	 */
	public boolean useDecayEnergy(double q) {
		if (_energy < q) {
			return false;
		}
		double decomposition = _world.decomposition(q);
		_energy -= decomposition;
		if (decomposition < q)
			return false;
		return true;
	}
	/**
	 * Makes the organism spend an amount of energy using the
	 * respiration process.
	 *
	 * @param q  The quantity of energy to spend.
	 * @return  true if the organism has enough energy and there is
	 * enough oxygen in the atmosphere, false otherwise.
	 */
	public boolean useEnergy(double q) {
		if (_energy < q) {
			return false;
		}
		double respiration = _world.respiration(q);
		_energy -= respiration;
		if (respiration < q)
			return false;
		return true;
	}
	/**
	 * Realize the respiration process to maintain its structure.
	 * Aging is applied here too.
	 */
	public void breath() {
		_age++;
		// Respiration process
		boolean canBreath = useEnergy(Math.min(_maintenance, _energy));
		if ((_age >> 8) > _max_age || !canBreath) {
			// Dormant and moving spore organisms may reproduce before dying
			if ((_sporeversion == -1) && (_energy > (_reproduceEnergy/2)) && (_growthRatio==1)) {
				if ((_age >> 8) > _max_age) {
					_reproduceEnergy = (int)_energy + (16 + 3 * _symmetry);
					reproduceSpore();
					_reproduceEnergy = (40 + 3 * _segments);
				}
			}
			// It's dead, but still may have energy
			die(null);
		} else {
			// Check that it don't exceed the maximum chemical energy
			if (_energy > _reproduceEnergy) {
				if (_energy > 2*_reproduceEnergy) {
					useEnergy((int)_energy - 2*_reproduceEnergy);
					if ((_reproducelate > 0) && (!_isinfectious) && (_timeToReproduce > 0)) {
						_timeToReproduce = 0;
					}
				} else {
					if ((_reproducelate == 0) || (_isinfectious)) {
						useEnergy(((int)_energy - _reproduceEnergy)*0.005);
					} else {
						if ((_timeToReproduce > 0) && (_energy > 2*_reproduceEnergy-_reproducelate)) {
							_timeToReproduce = 0;
						}
					}
				}
			} else {
				if (_energy <= Utils.tol) {
					active = false;
					alive = false;
					_world.decreasePopulation();
					_world.organismHasDied(this, null);
				}
			}
		}
	}
	/**
	 * Realize the decaying process of dead organisms
	 * Spore respiration, movement and hatching is applied here too.
	 */
	public void decay() {
		// Spore respiration, movement and hatching
		if (alive) {
			if (_sporeversion == 1) {
				_age++;
				// Respiration process
				boolean canBreath = useEnergy(Math.min(0.003, _energy));
				if ((_age >> 8) >= _max_age || !canBreath) {
					// It's dead, but still may have energy
					die(null);
				} else {
					if (_energy <= Utils.tol) {
						alive = false;
						_world.decreasePopulation();
						_world.organismHasDied(this, null);
					} else {
						// mobile spore movement
						if (!_isfrozen) {
							int i;
							for (i=_segments-1; i>=0; i--) {
								// Movement
								if (Utils.random.nextInt(100)<8) {
									dx=Utils.between(dx+12d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
									dy=Utils.between(dy+12d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
									dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
								}
							}
						}
					}
				}
			} else if (_sporeversion == 2) {
				_age++;
				if ((_age >> 8) > _max_age) {
					// It's dead, but still may have energy
					die(null);
				} else {
					if (_energy <= Utils.tol) {
						alive = false;
						_world.decreasePopulation();
						_world.organismHasDied(this, null);
					}
				}
			} else if (_sporeversion == 5) {
				_age++;
				// Respiration process
				boolean canBreath = useEnergy(Math.min(0.0026, _energy));
				if ((_age >> 8) >= _max_age || !canBreath) {
					// It's dead, but still may have energy
					die(null);
				} else {
					if (_energy <= Utils.tol) {
						alive = false;
						_world.decreasePopulation();
						_world.organismHasDied(this, null);
					} else {
						// mobile spore movement
						if (!_isfrozen) {
							int i;
							for (i=_segments-1; i>=0; i--) {
								// Movement
								if (Utils.random.nextInt(100)<8) {
									dx=Utils.between(dx+1.33d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
									dy=Utils.between(dy+1.33d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
									dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
								}
							}
						}
						// Spore hatching
						if (_energy > 0.5*_reproduceEnergy) {
							if (useEnergy(Utils.SPORE_ENERGY_CONSUMPTION)) {
								for (int j = 0; j < _segments; j++) {
									_segColor[j] = getGeneticCode().getGene(j%getGeneticCode().getNGenes()).getColor();
								    _mphoto[j] = 0;
								}
								_framesColor = 0;
								_isaconsumer = false;
								_isfrozen =false;
								_isinjured =false;
								_allfrozen =false;
								active = true;
								hasMoved = true;
								_age = 1;
								segmentsCreateEffects();
								if (_haseyes) {
	                       			symmetric();
	                       		}
							}
						}
					}
				}
			} else if (_sporeversion == 6) {
				if ((!_isinjured) || useDecayEnergy(Math.min(0.001, _energy))) {
					_age++;
					if ((_age >> 8) > _max_age) {
						// It's dead, but still may have energy
						die(null);
					} else {
						if (_energy <= Utils.tol) {
							alive = false;
							_world.decreasePopulation();
							_world.organismHasDied(this, null);
						} else {
							// Spore hatching
							if (((_isblond) && (_gold >= 0) && (_energy > 0.5*_earlyReproduceEnergy)) || (_energy > 0.5*_reproduceEnergy)) {
								if (((_isblond) || (_gold > 0) || (_sporetime < 0)) && (_infectedGeneticCode != null)) {
									reproduceVirus();
									_nVirusChildren = 0;
									if (_energy <= Utils.tol) {
										alive = false;
										_world.decreasePopulation();
										_world.organismHasDied(this, null);
									}
								} else {
									if (_timeToReproduce <= _timeToReproduceMax) {
										if (useEnergy(Utils.SPORE_ENERGY_CONSUMPTION)) {
											for (int j = 0; j < _segments; j++) {
												_segColor[j] = getGeneticCode().getGene(j%getGeneticCode().getNGenes()).getColor();
											    _mphoto[j] = 0;
											}
											_photosynthesis = 0;
											_methanotrophy = 0;
											_leafphoto = 0;
											_forestphoto = 0;
											_framesColor = 0;
											_isaplant = false;
											if (_jadefactor > 0) {
												_jadefactor = 1;
											}
											_isfrozen =false;
											_isinjured =false;
											_allfrozen =false;
											active = true;
											hasMoved = true;
											_gold = 0;
											_age = 1;
											segmentsCreateEffects();
											if (_haseyes) {
				                       			symmetric();
				                       		}
										}
									} else {
										_timeToReproduce--;
										setColor(Utils.ColorFLOWER);
									}
								}
							} else {
								// Photosynthesis
								if (!_isinjured) {
									if (_methanotrophy > 0) {
										if ((_age & 0x07) == 0x00) {
										    _energy += _world.methanotrophy(_methanotrophy);
									    }
									} else {
										if (_islime) {
										    if (_world.fastCheckHit(this) != null) {
										    	_energy += _world.photosynthesis(_photosynthesis);
										    } else {
										    	// leafphoto is used for lime photosynthesis, if alone, here
										    	_energy += _world.photosynthesis(_leafphoto);
										    }
										} else {
											if (_colonyPhotosynthesis > 0) {
												_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
												_colonyPhotosynthesis = 0;
											} else {
												_energy += _world.photosynthesis(_photosynthesis);
											}
										}
									}
								}
							}
						}
					}
				}
			} else if (_sporeversion >= 3) {
				if ((!_isinjured) || useDecayEnergy(Math.min(0.001, _energy))) {
					if (_energy <= Utils.tol) {
						alive = false;
						_world.decreasePopulation();
						_world.organismHasDied(this, null);
					} else {
						// Spore hatching
						if (_timeToReproduce == 0) {
							if (useEnergy(Utils.SPORE_ENERGY_CONSUMPTION)) {
								for (int j = 0; j < _segments; j++)
									_segColor[j] = getGeneticCode().getGene(j%getGeneticCode().getNGenes()).getColor();
								_framesColor = 0;
								_isenhanced =false;
								_isfrozen =false;
								_isinjured =false;
								_allfrozen =false;
								active = true;
								hasMoved = true;
								_age = 1;
								segmentsCreateEffects();
								if (_haseyes) {
	                       			symmetric();
	                       		}
							}
						} else {
							if (!_isinjured) {
								// Color for egg spore
								if ((_sporeversion == 4) && (_framesColor <= 1)) {
									setColor(Utils.ColorSPORE);
								}
								_timeToReproduce--;
							}
						}
					}
				}
			} else if (_sporeversion == -3) {
				if ((!_isinjured) || useDecayEnergy(Math.min(0.001, _energy))) {
					// Respiration process
					boolean canBreath = useEnergy(Math.min(0.001, _energy));
					if (!canBreath) {
						// It's dead, but still may have energy
						die(null);
					} else {
						if (_energy <= Utils.tol) {
							alive = false;
							_world.decreasePopulation();
							_world.organismHasDied(this, null);
						} else {
							// mobile spore movement
							if (!_isfrozen) {
								// Color for moving spore
								if (_framesColor <= 1) {
									setColor(Color.CYAN);
								}
								int i;
								for (i=_segments-1; i>=0; i--) {
									// Movement
									if (Utils.random.nextInt(100)<8) {
										dx=Utils.between(dx+12d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
										dy=Utils.between(dy+12d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
										dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
									}
								}
							}
							// Spore hatching
							if (_timeToReproduce == 0) {
								if ((!_isinjured) && ((_world.fastCheckHit(this) == null) || (_gold == 0))) {
									hatch();
								} else {
									if (_gold > 0) {
										_gold--;
									}
								}
							} else {
								_timeToReproduce--;
							}
						}
					}
				}
			} else {
				if ((!_isinjured) || useDecayEnergy(Math.min(0.001, _energy))) {
					if (_energy <= Utils.tol) {
						alive = false;
						_world.decreasePopulation();
						_world.organismHasDied(this, null);
					} else {
						// Spore hatching
						if (_timeToReproduce == 0) {
							if ((!_isinjured) && ((_world.fastCheckHit(this) == null) || (_gold == 0))) {
								hatch();
							} else {
								if (_gold > 0) {
									_gold--;
								}
							}
						} else {
							_timeToReproduce--;
						}
					}
				}
			}
		} else {
			// The corpse slowly decays
			useDecayEnergy(Math.min(_energy, Utils.DECAY_ENERGY));
		}
	}
	/**
	 * Create blue stationary spore
	 */
	public void blueSpore() {
		if (_geneticCode.getModifiesspore() == 7) {
			for (int i=0; i<_segments; i++) {
	  			 _segColor[i] = Color.BLUE;
	  			 _mphoto[i] = 0;
	  		}
		} else {
			for (int i=0; i<_segments; i++) {
	  			 _segColor[i] = Color.BLUE;
	  			 _mphoto[i] = -4;
	  		}
		}
		_sporeversion = -2;
		_timeToReproduce = _sporetime / _symmetry;
		_timeToReproduce = (_timeToReproduce * _timeToReproduce * _timeToReproduce) / 400;
		// _gold is used for the possible hatch delay here
		_gold = 20;
  		_usepretoucheffects = false;
  		_usefriendeffects = 0;
   		_canreact = false;
  		active = false;
  		hasMoved = true;
  		_isaplant = false;
  		_isaconsumer = false;
  		if (_isafungus) {
  			_isafungus = false;
  			_spin = -1;
  		}
  		_isenhanced = false;
  		if (_haseyes) {
  			_haseyes = false;
  			symmetric();
  		}
	}
	/**
	 * Create moving spore (shown in cyan color later)
	 */
	public void cyanSpore() {
		if (_geneticCode.getModifiesspore() == 10) {
			for (int i=0; i<_segments; i++) {
	  			 _segColor[i] = Color.BLUE;
	  			 _mphoto[i] = 0;
	  		}
		} else {
			for (int i=0; i<_segments; i++) {
	  			 _segColor[i] = Color.BLUE;
	  			 _mphoto[i] = -4;
	  		}
		}
		_sporeversion = -3;
		_timeToReproduce = _sporetime / _symmetry;
		_timeToReproduce = (_timeToReproduce * _timeToReproduce) / 40;
		// _gold is used for the possible hatch delay here
		_gold = 20;
  		_usepretoucheffects = false;
  		_usefriendeffects = 0;
   		_canreact = false;
  		active = false;
  		hasMoved = true;
  		_isaplant = false;
  		_isaconsumer = false;
  		if (_isafungus) {
  			_isafungus = false;
  			_spin = -1;
  		}
  		_isenhanced = false;
  		if (_haseyes) {
  			_haseyes = false;
  			symmetric();
  		}
	}
	/**
	 * Hatching for blue and cyan spores
	 */
	public void hatch() {
		if (useEnergy(Utils.SPORE_ENERGY_CONSUMPTION)) {
			for (int j = 0; j < _segments; j++) {
				_segColor[j] = getGeneticCode().getGene(j%getGeneticCode().getNGenes()).getColor();
				_mphoto[j] = 0;
			}
			_framesColor = 0;
			if (_geneticCode.getModifiesspore() <= 9) {
				_sporeversion = -2;
			} else {
				_sporeversion = -3;
			}
			_timeToReproduce = 0;
			_gold = 0;
			_isaplant = false;
			_isfrozen =false;
			_isinjured =false;
			_allfrozen =false;
			active = true;
			hasMoved = true;
			_age++;
			segmentsCreateEffects();
			if (_haseyes) {
       			symmetric();
       		}
		} else {
			useEnergy(Math.min(0.01, _energy));
			if (_energy <= Utils.tol) {
				alive = false;
				_world.decreasePopulation();
				_world.organismHasDied(this, null);
			}
		}
	}
	/**
	 * Applies hatching produced by a spore touching a segment
	 *
	 * @param org  The organism which is touching.
	 * @param seg  Index of this organism's segment.
	 * @param oseg  Index of the other organism's segment.
	 */
	private final void touchHatch(Organism org, int seg, int oseg) {
		switch (getTypeColor(org._segColor[oseg])) {
		case RED:
			if (_geneticCode.getAdaptspore() % 10 == 1) {
				hatch();
			}
			break;
		case FIRE:
			if (_geneticCode.getAdaptspore() % 10 == 2) {
				hatch();
			}
			break;
		case ORANGE:
		case MAROON:
			if (_geneticCode.getAdaptspore() % 10 == 3) {
				hatch();
			}
			break;
		case PINK:
			if (_geneticCode.getAdaptspore() % 10 == 5) {
			    hatch();
		    }
			break;
		case BLUE:
		case LIGHT_BLUE:
		case OCHRE:
		case FALLOW:
		case BARK:
		case OLDBARK:
		case DEADBARK:
		case ICE:
			if ((org._isaplant) && (!org._isaconsumer) && (!org._isafungus)) {
				if (_geneticCode.getAdaptspore() % 10 == 9) {
				    hatch();
			    }
			}
			break;
		case OLIVE:
		case DARKOLIVE:
		case SKY:
		case DEEPSKY:
		case VIOLET:
			if ((_healing > 0) && (org._isaplant) && (!org._isaconsumer) && (!org._isafungus)) {
				if (_geneticCode.getAdaptspore() % 10 == 9) {
				    hatch();
			    }
			}
			break;
		case SPIKE:
		case SPIKEPOINT:
		case GRAY:
			if ((org._isaplant) && (!org._isaconsumer) && (!org._isafungus)) {
				if (_geneticCode.getAdaptspore() % 10 == 8) {
				    hatch();
			    }
			}
			break;
		case LILAC:
		case DARKLILAC:
			if ((!org._modifieslilac) && (org._isaplant) && (!org._isaconsumer) && (!org._isafungus)) {
				if (_geneticCode.getAdaptspore() % 10 == 8) {
				    hatch();
			    }
			}
			break;
		case SILVER:
			if (org._isaconsumer) {
				if (_geneticCode.getAdaptspore() % 10 == 2) {
					hatch();
				}} else if (org._isafungus) {
					if (_geneticCode.getAdaptspore() % 10 == 5) {
					    hatch();
				}} else if (org._isaplant) {
					if (org._isinfectious) {
						if (_geneticCode.getAdaptspore() % 10 == 6) {
						    hatch();
					}} else {
						if (_geneticCode.getAdaptspore() % 10 == 7) {
						    hatch();
					}
			    }} else if (org.active) {
			    	if (_geneticCode.getAdaptspore() % 10 == 0) {
					    hatch();
				}
			}
			break;
		case CORAL:
			if (org._isaconsumer) {
				if (_geneticCode.getAdaptspore() % 10 == 4) {
					hatch();
				}} else if (org._isafungus) {
					if (_geneticCode.getAdaptspore() % 10 == 5) {
					    hatch();
				}} else if (org._isaplant) {
					if (org._isinfectious) {
						if (_geneticCode.getAdaptspore() % 10 == 6) {
						    hatch();
					}} else {
						if (_geneticCode.getAdaptspore() % 10 == 7) {
						    hatch();
					}
			    }} else if (org.active) {
			    	if ((!_isinfectious) && (!_iscoral) && (_geneticCode.getAdaptspore() % 10 == 0)) {
					    hatch();
				}
			}
			break;
		case FRUIT:
			if (org._geneticCode.getModifiesspore() == 1) {
				if (_geneticCode.getAdaptspore() % 10 == 0) {
    			    hatch();
    		    }
			} else {
				if (_geneticCode.getAdaptspore() % 10 == 6) {
				    hatch();
				}
			}
            break;
		case CREAM:
		case BROWN:
			// "spin -1" is used for "isafungus" here
            if (_spin == -1) {
				hatch();
			}
			break;
		default:
			if (org._isaconsumer) {
				if (_geneticCode.getAdaptspore() % 10 == 4) {
					hatch();
				}} else if (org._isafungus) {
					if (_geneticCode.getAdaptspore() % 10 == 5) {
					    hatch();
				}} else if (org._isaplant) {
					if (org._isinfectious) {
						if (_geneticCode.getAdaptspore() % 10 == 6) {
						    hatch();
					}} else {
						if (_geneticCode.getAdaptspore() % 10 == 7) {
						    hatch();
					}
			    }} else if (org.active) {
			    	if (_geneticCode.getAdaptspore() % 10 == 0) {
					    hatch();
				}
			}
		}
	}
	/**
	 * Applies mimicry by a black/dark segment against other organisms reactions
	 *
	 * @param seg  Index of this organism's segment.
	 */
	private final void mimic(int seg) {
		if (_blackversion <= 2) {
			if (_blackversion == 1) {
				_segColor[seg] = Color.GREEN;
			} else {
				_segColor[seg] = Utils.ColorBARK;
			}
		} else if (_blackversion <= 8) {
			if (_blackversion == 3) {
				_segColor[seg] = Color.RED;
			} else if (_blackversion == 4) {
				_segColor[seg] = Utils.ColorFIRE;
			} else if (_blackversion == 5) {
				_segColor[seg] = Color.ORANGE;
			} else if (_blackversion == 6) {
				_segColor[seg] = Utils.ColorMAROON;
			} else if (_blackversion == 7) {
				_segColor[seg] = Color.PINK;
			} else {
				_segColor[seg] = Utils.ColorCREAM;
			}
		} else if (_blackversion <= 12) {
			if (_blackversion == 9) {
				_segColor[seg] = Color.LIGHT_GRAY;
			} else if (_blackversion == 10) {
				_segColor[seg] = Utils.ColorSPIKEPOINT;
			} else if (_blackversion == 11) {
				_segColor[seg] = Utils.ColorLILAC;
			} else {
				_segColor[seg] = Color.GRAY;
			}
		} else if (_blackversion <= 18) {
			if (_blackversion == 13) {
				_segColor[seg] = Utils.ColorVIOLET;
			} else if (_blackversion == 14) {
				_segColor[seg] = Utils.ColorOLIVE;
			} else if (_blackversion == 15) {
				_segColor[seg] = Utils.ColorSKY;
			} else if (_blackversion == 16) {
				_segColor[seg] = Color.BLUE;
			} else if (_blackversion == 17) {
				_segColor[seg] = Utils.ColorOCHRE;
			} else {
				_segColor[seg] = Utils.ColorFALLOW;
			}
		} else {
			if (_blackversion == 19) {
				_segColor[seg] = Color.WHITE;
			} else if (_blackversion == 20) {
				_segColor[seg] = Utils.ColorPLAGUE;
			} else if (_blackversion == 21) {
				_segColor[seg] = Utils.ColorCORAL;
			} else if (_blackversion == 22) {
				_segColor[seg] = Utils.ColorMINT;
			} else if (_blackversion == 23) {
				_segColor[seg] = Color.MAGENTA;
			} else {
				_segColor[seg] = Utils.ColorDARK;
			}
		}
	}
	/**
	 * Applies mimicry by a black/dark segment, showing the mimicked color
	 *
	 * @param seg  Index of this organism's segment.
	 */
	private final void mimicColor() {
		if (_blackversion <= 2) {
			if (_blackversion == 1) {
				setColor(Color.GREEN);
			} else {
				setColor(Utils.ColorBARK);
			}
		} else if (_blackversion <= 8) {
			if (_blackversion == 3) {
				setColor(Color.RED);
			} else if (_blackversion == 4) {
				setColor(Utils.ColorFIRE);
			} else if (_blackversion == 5) {
				setColor(Color.ORANGE);
			} else if (_blackversion == 6) {
				setColor(Utils.ColorMAROON);
			} else if (_blackversion == 7) {
				setColor(Color.PINK);
			} else {
				setColor(Utils.ColorCREAM);
			}
		} else if (_blackversion <= 12) {
			if (_blackversion == 9) {
				setColor(Color.LIGHT_GRAY);
			} else if (_blackversion == 10) {
				setColor(Utils.ColorSPIKEPOINT);
			} else if (_blackversion == 11) {
				setColor(Utils.ColorLILAC);
			} else {
				setColor(Color.GRAY);
			}
		} else if (_blackversion <= 18) {
			if (_blackversion == 13) {
				setColor(Utils.ColorVIOLET);
			} else if (_blackversion == 14) {
				setColor(Utils.ColorOLIVE);
			} else if (_blackversion == 15) {
				setColor(Utils.ColorSKY);
			} else if (_blackversion == 16) {
				setColor(Color.BLUE);
			} else if (_blackversion == 17) {
				setColor(Utils.ColorOCHRE);
			} else {
				setColor(Utils.ColorFALLOW);
			}
		} else {
			if (_blackversion == 19) {
				setColor(Color.WHITE);
			} else if (_blackversion == 20) {
				setColor(Utils.ColorPLAGUE);
			} else if (_blackversion == 21) {
				setColor(Utils.ColorCORAL);
			} else if (_blackversion == 22) {
				setColor(Utils.ColorMINT);
			} else if (_blackversion == 23) {
				setColor(Color.MAGENTA);
			} else {
				setColor(Utils.ColorDARK);
			}
		}
	}
	/**
	 * Kills the organism. Sets its segments to brown and tells the world
	 * about the event.
	 *
	 * @param killingOrganism  The organism that has killed this organism,
	 * or null if it has died of natural causes.
	 */
	public void die(Organism killingOrganism) {
		active = false;
		alive = false;
	    _usepretoucheffects = false;
	    _usefriendeffects = 0;
	    _photosynthesis = 0;
	    _methanotrophy = 0;
	    _sporetime = 0;
	    _sporeversion = 0;
	    _fallowinhibition = 0;
	    _useframemovement = false;
	    _canmove = false;
	    _canreact = false;
		hasMoved = true;
		_infectedGeneticCode = null;
		_savedGeneticCode = null;
		for (int i=0; i<_segments; i++) {
			if (_segColor[i].equals(Utils.ColorEYE)) {
				_segColor[i] = Utils.ColorEYE;
			} else {
				_segColor[i] = Utils.ColorBROWN;
				_mphoto[i] = 0;
			}
		}
		_world.decreasePopulation();
		if (killingOrganism != null)
			killingOrganism._nTotalKills++;
		_world.organismHasDied(this, killingOrganism);
	}
	/**
	 * Magenta segments are getting weakened by a gray segment
	 *
	 * @param grayOrganism  The organism that weakened
	 * all magenta segments of this organism
	 */
	public void survive(Organism grayOrganism) {
		_healing = 0;
		for (int i=0; i<_segments; i++) {
			if (_segColor[i].equals(Color.MAGENTA)) {
                _segColor[i] = Utils.ColorDARKFIRE;
                _mphoto[i] = -20;
                _useextraeffects = true;
				_isinjured =true;
			}
		}
		grayOrganism.setColor(Color.GRAY);
	}
	/**
	 * Reproductive segments are getting destroyed by a fallow segment
	 *
	 * @param fallowOrganism  The organism that destroyed
	 * the reproductive segments of this organism
	 */
	public void inhibited(Organism fallowOrganism) {
		for (int i=0; i<_segments; i++) {
			switch (getTypeColor(_segColor[i])) {
			case SPORE:
				if ((_sporeversion > 0) && (fallowOrganism.useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
					if (_isaplant) {
					    _segColor[i] = Utils.ColorBROKEN;
					} else {
						_segColor[i] = Utils.ColorLIGHTBROWN;
					}
					_mphoto[i] = -20;
					fallowOrganism.setColor(Utils.ColorFALLOW);
					_updateEffects = 2;
				}
				break;
			case AUBURN:
			case INDIGO:
				if (fallowOrganism.useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
					if (_isaplant) {
					    _segColor[i] = Utils.ColorBROKEN;
					} else {
						_segColor[i] = Utils.ColorLIGHTBROWN;
					}
					_mphoto[i] = -20;
					fallowOrganism.setColor(Utils.ColorFALLOW);
					_updateEffects = 2;
				}
				break;
			case YELLOW:
			case BLOND:
				if (fallowOrganism.useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
					if (_isaplant) {
					    _segColor[i] = Utils.ColorBROKEN;
					} else {
						_segColor[i] = Utils.ColorLIGHTBROWN;
					}
					_mphoto[i] = -20;
					fallowOrganism.setColor(Utils.ColorFALLOW);
				}
				break;
			}
	    }
		segmentsRestoreEffects();
	}
	/**
	 * Green and moving segments are getting destroyed by a modified sky segment
	 *
	 * @param freezingOrganism  The organism that destroyed
	 * the green and moving segments of this organism
	 */
	public void frozen(Organism freezingOrganism) {
		boolean freeze =false;
		_allfrozen =true;
		_canmove =false;
		_canreact =false;
		_useframemovement =false;
		if (_skyversion > 0) {
			_skyversion = -1;
		}
		if (_jadefactor > 0) {
			_jadefactor = 1;
		}
		_spin = 0;
		for (int i=0; i<_segments; i++) {
			switch (getTypeColor(_segColor[i])) {
			case SKY:
			case DEEPSKY:
				if (freezingOrganism.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
					_segColor[i] = Utils.ColorLIGHT_BLUE;
					_mphoto[i] = -20;
					freeze =true;
					_isinjured =true;
					if ((!_isaplant) && (!_geneticCode.getModifiessky())) {
						_updateEffects = 2;
					}
				} else {
					if (_skyversion <= 0) {
						if (_geneticCode.getModifiessky()) {
							_skyversion = 2;
						} else {
							_skyversion = 1;
						}
					}
					_allfrozen =false;
				}
				break;
			case TEAL:
				if (freezingOrganism.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
					_segColor[i] = Utils.ColorICE;
					_mphoto[i] = -20;
					if (_geneticCode.getPassive()) {
						_updateEffects = 2;
					}
					freeze =true;
					_isinjured =true;
					_isfrozen =true;
				} else {
					_allfrozen =false;
					_canmove =true;
					_canreact =true;
					if (!_geneticCode.getPassive()) {
						_useframemovement =true;
					}
				}
				break;
			case CYAN:
				if (freezingOrganism.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
					_segColor[i] = Utils.ColorICE;
					_mphoto[i] = -20;
					freeze =true;
					_isinjured =true;
					_isfrozen =true;
				} else {
					_allfrozen =false;
					_canmove =true;
					_useframemovement =true;
				}
				break;
			case GREENBROWN:
				if (freezingOrganism.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
					_segColor[i] = Utils.ColorICE;
					freeze =true;
					_isinjured =true;
					_isfrozen =true;
				} else {
					_allfrozen =false;
				}
				break;
			case WINTER:
				if (freezingOrganism.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
					_segColor[i] = Utils.ColorICE;
					freeze =true;
					_isinjuredplant =true;
					_isinjured =true;
					_isfrozen =true;
				} else {
					_allfrozen =false;
				}
				break;
			case JADE:
			case DARKJADE:
				if (freezingOrganism.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
					_segColor[i] = Utils.ColorICE;
					freeze =true;
					_isinjuredplant =true;
					_isinjured =true;
					_isfrozen =true;
					_updateEffects = 2;
				} else {
					_allfrozen =false;
					_jadefactor = 2;
				}
				break;
			case SPRING:
				if (freezingOrganism.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
					_segColor[i] = Utils.ColorICE;
					freeze =true;
					_isinjuredplant =true;
					_isinjured =true;
					_isfrozen =true;
					_updateEffects = 2;
				} else {
					_allfrozen =false;
					_spin += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				}
				break;
			case DARKGREEN:
				if (freezingOrganism.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
					_segColor[i] = Utils.ColorICE;
					freeze =true;
					_isinjuredplant =true;
					_isinjured =true;
					_isfrozen =true;
					_updateEffects = 2;
				} else {
					_allfrozen =false;
					if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorJADE)) {
						_jadefactor = 2;
					}
					if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorSPRING)) {
						_spin += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
					}
				}
				break;
			case SUMMER:
			case LEAF:
			case LIME:
			case GREEN:
			case FOREST:
			case GRASS:
			case C4:
			case PURPLE:
				if (freezingOrganism.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
					_segColor[i] = Utils.ColorICE;
					freeze =true;
					_isinjuredplant =true;
					_isinjured =true;
					_isfrozen =true;
					_updateEffects = 2;
				} else {
					_allfrozen =false;
				}
				break;
			case OLDBARK:
			case BARK:
				if (freezingOrganism.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
					_segColor[i] = Utils.ColorDEADBARK;
					_mphoto[i] = -0.5;
					freeze =true;
					_isfrozen =true;
					_updateEffects = 2;
				} else {
					_allfrozen =false;
				}
				break;
			}
	    }
		if (freeze) {
			freezingOrganism.setColor(Utils.ColorSKY);
		}
		if ((!_canreact) && (_isonlyc4 != 2)) {
			_candodge =false;
			_dodge =false;
		}
		// Calculate jade delay used in restoration of the color
		if (_jadefactor > 1) {
			if (_symmetry != 1) {
				_jadefactor = Utils.DARKJADE_DELAY * _symmetry;
			} else {
				_jadefactor = Utils.DARKJADE_DELAY * 2;
			}
		}
	}
	/**
	 * Moving segments are getting destroyed by an unmodified sky segment
	 *
	 * @param freezingOrganism  The organism that destroyed
	 * the green and moving segments of this organism
	 */
	public void paralyzed(Organism freezingOrganism) {
		boolean freeze =false;
		_canmove =false;
		_canreact =false;
		_useframemovement =false;
		for (int i=0; i<_segments; i++) {
			switch (getTypeColor(_segColor[i])) {
			case TEAL:
				if (freezingOrganism.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
					_segColor[i] = Utils.ColorICE;
					_mphoto[i] = -20;
					if (_geneticCode.getPassive()) {
						_updateEffects = 2;
					}
					freeze =true;
					_isinjured =true;
					_isfrozen =true;
				} else {
					_canmove =true;
					_canreact =true;
					if (!_geneticCode.getPassive()) {
						_useframemovement =true;
					}
				}
				break;
			case CYAN:
				if (freezingOrganism.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
					_segColor[i] = Utils.ColorICE;
					_mphoto[i] = -20;
					freeze =true;
					_isinjured =true;
					_isfrozen =true;
				} else {
					_canmove =true;
					_useframemovement =true;
				}
				break;
			}
	    }
		if (freeze) {
			freezingOrganism.setColor(Utils.ColorSKY);
		}
		if ((!_canreact) && (_isonlyc4 != 2)) {
			_candodge =false;
			_dodge =false;
		}
	}
	/**
	 * Infectious, coral and fallow segments are getting destroyed by a mint segment
	 *
	 * @param mintOrganism  The organism that destroyed
	 * the infectious, coral and fallow segments of this organism
	 */
	public void neutralized(Organism mintOrganism) {
		boolean neutralize = false;
		for (int i=0; i<_segments; i++) {
			switch (getTypeColor(_segColor[i])) {
			case WHITE:
			case PLAGUE:
				if (_isaplant) {
					if (mintOrganism.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
					    _segColor[i] = Utils.ColorBROKEN;
					    _mphoto[i] = -20;
					    neutralize = true;
					}
				} else {
					if ((_isaconsumer) || (_iscoral)) {
						if (mintOrganism.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
						    _segColor[i] = Utils.ColorLIGHTBROWN;
						    _mphoto[i] = -20;
						    neutralize = true;
						}
					} else {
						if ((_antiviral == 0) || (mintOrganism._isaplant) || (mintOrganism._isaconsumer) || (mintOrganism._isafungus) || (mintOrganism._iscoral)) {
							if ((_healing > 0) && (_plagueversion == 0)) {
								if (mintOrganism.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
								    _segColor[i] = Utils.ColorDARKFIRE;
								    _mphoto[i] = -20;
								    _useextraeffects = true;
								    neutralize = true;
								}
						    } else {
						    	if (mintOrganism.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
								    _segColor[i] = Utils.ColorLIGHTBROWN;
								    _mphoto[i] = -20;
								    neutralize = true;
						    	}
						    }
					    }
					}
				}
				break;
			case CORAL:
				if (_isaplant) {
					if (mintOrganism.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
					    _segColor[i] = Utils.ColorBROKEN;
					    _mphoto[i] = -20;
					    neutralize = true;
					}
				} else {
					if (mintOrganism.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
					    _segColor[i] = Utils.ColorLIGHTBROWN;
					    _mphoto[i] = -20;
					    neutralize = true;
					    if ((_gold > 0) && (!_isaconsumer) && (!_isafungus)) {
					    	_updateEffects = 2;
						}
					}
				}
				break;
			case FALLOW:
				if (_isaplant) {
					if (mintOrganism.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
					    _segColor[i] = Utils.ColorBROKEN;
					    _mphoto[i] = -20;
					    neutralize = true;
					    _updateEffects = 2;
					}
				} else {
					if ((_isaconsumer) || (_iscoral)) {
						if (mintOrganism.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
						    _segColor[i] = Utils.ColorLIGHTBROWN;
						    _mphoto[i] = -20;
						    neutralize = true;
						    _updateEffects = 2;
						}
					} else {
						if ((_antiviral == 0) || (mintOrganism._isaplant) || (mintOrganism._isaconsumer) || (mintOrganism._isafungus) || (mintOrganism._iscoral)) {
							if (mintOrganism.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
							    _segColor[i] = Utils.ColorLIGHTBROWN;
							    _mphoto[i] = -20;
							    neutralize = true;
							    _updateEffects = 2;
					    	}
					    }
					}
				}
				break;
			}
		}
		if (neutralize) {
			mintOrganism.setColor(Utils.ColorMINT);
			_isinjured =true;
		}
	}
	/**
	 * Infects this organism with a genetic code.
	 * Tells the world about this event.
	 * Not currently used.
	 *
	 * @param infectingCode  The genetic code that infects this organism.
	 */
	public void infectedBy(GeneticCode infectingCode) {
		_infectedGeneticCode = infectingCode;
		_savedGeneticCode = null;
		_world.organismHasBeenInfected(this, null);
	}
	/**
	 * Infects this organism with the genetic code of another organism.
	 * Tells the world about this event.
	 *
	 * @param infectingOrganism  The organism that is infecting this one.
	 */
	public void infectedBy(Organism infectingOrganism) {
		infectingOrganism._nTotalInfected++;
		_infectedGeneticCode = infectingOrganism._geneticCode;
		_savedGeneticCode = null;
		_world.organismHasBeenInfected(this, infectingOrganism);
		_infectedReproduceEnergy = infectingOrganism._reproduceEnergy;
	}
	/**
	 * Infects this organism with the genetic code of a fruit.
	 * Tells the world about this event.
	 *
	 * @param infectingOrganism  The organism that is infecting this one.
	 */
	public void infectedByFruit(Organism infectingOrganism) {
		infectingOrganism._nTotalInfected++;
		_infectedGeneticCode = infectingOrganism._geneticCode;
		_savedGeneticCode = null;
		_world.organismHasBeenInfected(this, infectingOrganism);
		if (_reproducelate == 0) {
			_infectedReproduceEnergy = (int) (_reproduceEnergy + (_reproduceEnergy * 0.048 * infectingOrganism._sporetime));
		} else {
			_infectedReproduceEnergy = (int) ((40 + 3 * _segments) + ((40 + 3 * _segments) * 0.048 * infectingOrganism._sporetime));
		}
	}
	/**
	 * Infects this organism with the genetic code of a fruit (weak).
	 * Tells the world about this event.
	 *
	 * @param infectingOrganism  The organism that is infecting this one.
	 */
	public void infectedByFruitWeak(Organism infectingOrganism) {
		infectingOrganism._nTotalInfected++;
		_infectedGeneticCode = infectingOrganism._geneticCode;
		_savedGeneticCode = null;
		_world.organismHasBeenInfected(this, infectingOrganism);
		if (_reproducelate == 0) {
			_infectedReproduceEnergy = (int) (_reproduceEnergy * 0.048 * infectingOrganism._sporetime);
		} else {
			_infectedReproduceEnergy = (int) ((40 + 3 * _segments) * 0.048 * infectingOrganism._sporetime);
		}
	}
	/**
	 * Calculates the resulting speeds after a collision between two organisms, following
	 * physical rules.
	 *
	 * @param org  The other organism in the collision.
	 * @param p  Intersection point between the organisms.
	 * @param l  Line that has collided. Of the two lines, this is the one that collided
	 * on the center, not on the vertex.
	 * @param thisOrganism  true if l is a line of this organism, false if l is a line of org.
	 */
	private final void touchMove(Organism org, Point2D.Double p, Line2D l, boolean thisOrganism) {
		// Distance vector between centers of mass and p
		double rapx = p.x - _dCenterX;
		double rapy = p.y - _dCenterY;
		double rbpx = p.x - org._dCenterX;
		double rbpy = p.y - org._dCenterY;
		// Speeds of point p in the body A and B, before collision.
		double vap1x = dx - dtheta * rapy + hasGrown*rapx/10d;
		double vap1y = dy + dtheta * rapx + hasGrown*rapy/10d;
		double vbp1x = org.dx - org.dtheta * rbpy;
		double vbp1y = org.dy + org.dtheta * rbpx;
		// Relative speeds between the two collision points.
		double vab1x = vap1x - vbp1x;
		double vab1y = vap1y - vbp1y;
		// Normal vector to the impact line
		//First: perpendicular vector to the line
		double nx = l.getY1() - l.getY2();
		double ny = l.getX2() - l.getX1();
		//Second: normalize, modulus 1
		double modn = Math.sqrt(nx * nx + ny * ny);
		if (modn == 0) {
			modn = 0.000000000000001;
		}
		nx /= modn;
		ny /= modn;
		/*Third: of the two possible normal vectors we need the one that points to the
		 * outside; we choose the one that its final point is the nearest to the center
		 * of the other line.
		 */
		if (thisOrganism) {
			if ((p.x+nx-org._dCenterX)*(p.x+nx-org._dCenterX)+(p.y+ny-org._dCenterY)*(p.y+ny-org._dCenterY) <
				(p.x-nx-org._dCenterX)*(p.x-nx-org._dCenterX)+(p.y-ny-org._dCenterY)*(p.y-ny-org._dCenterY)) {
				nx = -nx;
				ny = -ny;
			}
		} else {
			if ((p.x+nx-_dCenterX)*(p.x+nx-_dCenterX)+(p.y+ny-_dCenterY)*(p.y+ny-_dCenterY) >
				(p.x-nx-_dCenterX)*(p.x-nx-_dCenterX)+(p.y-ny-_dCenterY)*(p.y-ny-_dCenterY)) {
				nx = -nx;
				ny = -ny;
			}
		}
		// This is the j in the parallel axis theorem
		double j = (-(1+Utils.ELASTICITY) * (vab1x * nx + vab1y * ny)) /
			(1/_mass + 1/org._mass + Math.pow(rapx * ny - rapy * nx, 2) / _I +
					Math.pow(rbpx * ny - rbpy * nx, 2) / org._I);
		// Final speed
		dx = Utils.between(dx + j*nx/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
		dy = Utils.between(dy + j*ny/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
		org.dx = Utils.between(org.dx - j*nx/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
		org.dy = Utils.between(org.dy - j*ny/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
		dtheta = Utils.between(dtheta + j * (rapx * ny - rapy * nx) / _I, -Utils.MAX_ROT, Utils.MAX_ROT);
		org.dtheta = Utils.between(org.dtheta - j * (rbpx * ny - rbpy * ny) / org._I, -Utils.MAX_ROT, Utils.MAX_ROT);
	}
	/**
	 * Checks if the organism is inside the world. If it is not, calculates its
	 * speed after the collision with the world border.
	 * This calculation should be updated to follow the parallel axis theorem, just
	 * like the collision between two organisms.
	 *
	 * @return  true if the organism is inside the world, false otherwise.
	 */
	private final boolean isInsideWorld() {
		// Check it is inside the world
		if (x<=0 || y<=0 || x+width>=_world._width || y+height>=_world._height) {
			if (height <= 1 && width <= 1 && alive && x == 0 && y == 0)
				die(null);
			// Adjust direction
			if (x <= 0 || x + width >= _world._width)
				dx = -dx;
			if (y <= 0 || y + height >= _world._height)
				dy = -dy;
			dtheta = 0;
			return false;
		}
		return true;
	}
	/**
	 * Moves the organism and rotates it.
	 *
	 * @param offsetx  displacement on the x axis.
	 * @param offsety  displacement on the y axis.
	 * @param offsettheta  rotation degree.
	 */
	private final void offset(double offsetx, double offsety, double offsettheta) {
		_dCenterX += offsetx; _dCenterY += offsety; _theta += offsettheta;
		_centerX = (int)_dCenterX; _centerY = (int)_dCenterY;
	}
	/**
	 * Stop reaction
	 */
	private final void stopmoving() {
		dx *= Utils.RUBBING/5;
		if (Math.abs(dx) < Utils.tol) dx = 0;
		dy *= Utils.RUBBING/5;
		if (Math.abs(dy) < Utils.tol) dy = 0;
		dtheta *= Utils.RUBBING/5;
		if (Math.abs(dtheta) < Utils.tol) dtheta = 0;
	}
	/**
	 * Stop reaction for non moving organisms with eyes
	 */
	private final void standstill() {
		dx *= Utils.RUBBING/2.5;
		if (Math.abs(dx) < Utils.tol) dx = 0;
		dy *= Utils.RUBBING/2.5;
		if (Math.abs(dy) < Utils.tol) dy = 0;
		dtheta *= Utils.RUBBING/2.5;
		if (Math.abs(dtheta) < Utils.tol) dtheta = 0;
	}
	/**
	 * Stand still for Ochre
	 */
	private final void standochre() {
		dx *= Utils.RUBBING/1.5;
		if (Math.abs(dx) < Utils.tol) dx = 0;
		dy *= Utils.RUBBING/1.5;
		if (Math.abs(dy) < Utils.tol) dy = 0;
		dtheta *= Utils.RUBBING/1.5;
		if (Math.abs(dtheta) < Utils.tol) dtheta = 0;
	}
	/**
	 * Infectious segments are destroyed
	 */
	private final void virusneutralized() {
		for (int x = 0; x < _segments; x++) {
			switch (getTypeColor(_segColor[x])) {
			case WHITE:
			case PLAGUE:
				if (_isaplant) {
				    _segColor[x] = Utils.ColorBROKEN;
				} else {
					_segColor[x] = Utils.ColorLIGHTBROWN;
				}
				_mphoto[x] = -20;
				_isinjured =true;
			case CORAL:
				if (_isaplant) {
				    _segColor[x] = Utils.ColorBROKEN;
				} else {
					_segColor[x] = Utils.ColorLIGHTBROWN;
					if ((_gold > 0) && (!_isaconsumer) && (!_isafungus)) {
				    	_updateEffects = 2;
					}
				}
				_mphoto[x] = -20;
				_isinjured =true;
			}
		}
	}
	/**
	 * Lavender shield is used
	 */
	private final void lavenderinhibition() {
		_lavender -= 185;
		if (_lavender <= 0) {
			_lavender = 0;
		} else {
			if (_framesColor < 10) {
				setColor(Utils.ColorLIGHT_BLUE);
			}
		}
	}
	/**
	 * Lavender shield is used
	 */
	private final void lavendershield() {
		_lavender -= 185;
		if (_lavender <= 0) {
			_lavender = 0;
		} else {
			setColor(Utils.ColorLIGHT_BLUE);
		}
	}
	/**
	 * Weak lavender shield is used
	 */
	private final void weaklavendershield() {
		_lavender -= 2590;
		if (_lavender <= 0) {
			_lavender = 0;
		} else {
			setColor(Utils.ColorLIGHT_BLUE);
		}
	}
	/**
	 * Finds if two organism are touching and if so applies the effects of the
	 * collision.
	 *
	 * @param org  The organism to check for collisions.
	 * @return  true if the two organisms are touching, false otherwise.
	 */
	public final boolean contact(Organism org) {
		int i,j;
		ExLine2DDouble line = new ExLine2DDouble();
		ExLine2DDouble bline = new ExLine2DDouble();
		// Check collisions for all segments
		if (!_haseyes) {
			for (i = _segments-1; i >= 0; i--) {
				// Consider only segments with modulus greater than 1
				if (_m[i]>=1) {
					line.setLine(x1[i]+_centerX, y1[i]+_centerY, x2[i]+_centerX, y2[i]+_centerY);
					// First check if the line intersects the bounding box of the other organism
					if (org.intersectsLine(line)) {
						// Do the same for the other organism's segments.
						for (j = org._segments-1; j >= 0; j--) {
							if (org._m[j]>=1) {
								bline.setLine(org.x1[j] + org._centerX, org.y1[j] + org._centerY,
										org.x2[j] + org._centerX, org.y2[j] + org._centerY);
								if (intersectsLine(bline) && line.intersectsLine(bline)) {
									// Intersection point
									Point2D.Double intersec= line.getIntersection(bline);
									/* touchMove needs to know which is the line that collides from the middle (not
									 * from a vertex). Try to guess it by finding the vertex nearest to the
									 * intersection point.
									 */
									double dl1, dl2, dbl1, dbl2;
									dl1 = intersec.distanceSq(line.getP1());
									dl2 = intersec.distanceSq(line.getP2());
									dbl1 = intersec.distanceSq(bline.getP1());
									dbl2 = intersec.distanceSq(bline.getP2());
									// Use this to send the best choice to touchMove
									if (Math.min(dl1, dl2) < Math.min(dbl1, dbl2))
										touchMove(org,intersec,bline,false);
									else
										touchMove(org,intersec,line,true);
									// Find only one collision to speed up.
									// If we found two intersecting segments, apply effects
									if (_usepretoucheffects) {
										if ((_isspike) && ((_segColor[i].equals(Utils.ColorSPIKEPOINT)) || (_segColor[i].equals(Utils.ColorSPIKE)))) {
											if ((org._isspike) && ((org._segColor[j].equals(Utils.ColorSPIKEPOINT)) || (org._segColor[j].equals(Utils.ColorSPIKE)))) {
												if (Math.min(dl1, dl2) < Math.min(dbl1, dbl2)) {
													if (_mphoto[i] != -1) {
														_segColor[i] = Utils.ColorSPIKEPOINT;
														_mphoto[i] = -1;
													}
												} else {
													if (_mphoto[i] != -0.5) {
														_segColor[i] = Utils.ColorSPIKE;
														_mphoto[i] = -0.5;
													}
												}
											} else {
												if (Math.min(dl1, dl2) <= Math.min(dbl1, dbl2)) {
													if (_mphoto[i] != -1) {
														_segColor[i] = Utils.ColorSPIKEPOINT;
														_mphoto[i] = -1;
													}
												} else {
													if (_mphoto[i] != -0.5) {
														_segColor[i] = Utils.ColorSPIKE;
														_mphoto[i] = -0.5;
													}
												}
											}
										}
										if (_segmentpretoucheffects > 0) {
											pretouchEffects(org,i,j);
										}
									}
									if (org._usepretoucheffects) {
										if ((org._isspike) && ((org._segColor[j].equals(Utils.ColorSPIKEPOINT)) || (org._segColor[j].equals(Utils.ColorSPIKE)))) {
											if ((_isspike) && ((_segColor[i].equals(Utils.ColorSPIKEPOINT)) || (_segColor[i].equals(Utils.ColorSPIKE)))) {
												if (Math.min(dbl1, dbl2) < Math.min(dl1, dl2)) {
													if (org._mphoto[j] != -1) {
														org._segColor[j] = Utils.ColorSPIKEPOINT;
														org._mphoto[j] = -1;
													}
												} else {
													if (org._mphoto[j] != -0.5) {
														org._segColor[j] = Utils.ColorSPIKE;
														org._mphoto[j] = -0.5;
													}
												}
											} else {
												if (Math.min(dbl1, dbl2) <= Math.min(dl1, dl2)) {
													if (org._mphoto[j] != -1) {
														org._segColor[j] = Utils.ColorSPIKEPOINT;
														org._mphoto[j] = -1;
													}
												} else {
													if (org._mphoto[j] != -0.5) {
														org._segColor[j] = Utils.ColorSPIKE;
														org._mphoto[j] = -0.5;
													}
												}
											}
										}
										if (org._segmentpretoucheffects > 0) {
											org.pretouchEffects(this,j,i);
										}
									}
									if ((((_parentID == org._ID || _ID == org._parentID) && !_generationbattle && !org._generationbattle) ||
									    (_parentID == org._parentID && !_siblingbattle && !org._siblingbattle && _parentID != -1 ) ||
									    (_transfersenergy && org._transfersenergy && ((_peaceful && org._peaceful) ||
									    (_lengthfriend == org._lengthfriend && _thetafriend == org._thetafriend && _social && org._social))))
										 && alive && org.alive) {
										if (_canreact) {
											touchfriendReaction(org,i,j);
										}
										if (_usefriendeffects > 0) {
											touchfriendEffects(org,i,j);
										}
										if (org._canreact) {
											org.touchfriendReaction(this,j,i);
										}
										if (org._usefriendeffects > 0) {
											org.touchfriendEffects(this,j,i);
										}
									} else {
										if (_canreact) {
											if ((org._mphoto[j] == -9) && (!_hasgoodvision)) {
												org.mimic(j);
												touchReaction(org,i,j);
												org._segColor[j] = Utils.ColorDARK;
											} else {
												touchReaction(org,i,j);
											}
										}
										if (_mphoto[i] == -1) {
											touchEffects1(org,i,j);
										} else if (_mphoto[i] == -4) {
											touchEffects2(org,i,j);
										}
										if (org._canreact) {
											if ((_mphoto[i] == -9) && (!org._hasgoodvision)) {
												mimic(i);
												org.touchReaction(this,j,i);
												_segColor[i] = Utils.ColorDARK;
											} else {
												org.touchReaction(this,j,i);
											}
										}
										if (org._mphoto[j] == -1) {
											org.touchEffects1(this,j,i);
										} else if (org._mphoto[j] == -4) {
											org.touchEffects2(this,j,i);
										}
									}
									return true;
								}
							}
						}
					}
				}
			}
		} else for (i = _segments-1; i >= 0; i--) {
			// Consider only segments with modulus greater than 1
			if (_m[i]>=1) {
				line.setLine(x1[i]+_centerX, y1[i]+_centerY, x2[i]+_centerX, y2[i]+_centerY);
				// First check if the line intersects the bounding box of the other organism
				if (org.intersectsLine(line)) {
					// Do the same for the other organism's segments.
					for (j = org._segments-1; j >= 0; j--) {
						if (org._m[j]>=1) {
							bline.setLine(org.x1[j] + org._centerX, org.y1[j] + org._centerY,
									org.x2[j] + org._centerX, org.y2[j] + org._centerY);
							if (intersectsLine(bline) && line.intersectsLine(bline)) {
								// Intersection point
								Point2D.Double intersec= line.getIntersection(bline);
								/* touchMove needs to know which is the line that collides from the middle (not
								 * from a vertex). Try to guess it by finding the vertex nearest to the
								 * intersection point.
								 */
								double dl1, dl2, dbl1, dbl2;
								dl1 = intersec.distanceSq(line.getP1());
								dl2 = intersec.distanceSq(line.getP2());
								dbl1 = intersec.distanceSq(bline.getP1());
								dbl2 = intersec.distanceSq(bline.getP2());
								// Use this to send the best choice to touchMove
								if (Math.min(dl1, dl2) < Math.min(dbl1, dbl2))
									touchMove(org,intersec,bline,false);
								else
									touchMove(org,intersec,line,true);
								// Find only one collision to speed up.
								// If we found two intersecting segments, apply effects
								if (_usepretoucheffects) {
									if ((_isspike) && ((_segColor[i].equals(Utils.ColorSPIKEPOINT)) || (_segColor[i].equals(Utils.ColorSPIKE)))) {
										if ((org._isspike) && ((org._segColor[j].equals(Utils.ColorSPIKEPOINT)) || (org._segColor[j].equals(Utils.ColorSPIKE)))) {
											if (Math.min(dl1, dl2) < Math.min(dbl1, dbl2)) {
												if (_mphoto[i] != -1) {
													_segColor[i] = Utils.ColorSPIKEPOINT;
													_mphoto[i] = -1;
												}
											} else {
												if (_mphoto[i] != -0.5) {
													_segColor[i] = Utils.ColorSPIKE;
													_mphoto[i] = -0.5;
												}
											}
										} else {
											if (Math.min(dl1, dl2) <= Math.min(dbl1, dbl2)) {
												if (_mphoto[i] != -1) {
													_segColor[i] = Utils.ColorSPIKEPOINT;
													_mphoto[i] = -1;
												}
											} else {
												if (_mphoto[i] != -0.5) {
													_segColor[i] = Utils.ColorSPIKE;
													_mphoto[i] = -0.5;
												}
											}
										}
									}
									if (_segmentpretoucheffects > 0) {
										pretouchEffects(org,i,j);
									}
								}
								if (org._usepretoucheffects) {
									if ((org._isspike) && ((org._segColor[j].equals(Utils.ColorSPIKEPOINT)) || (org._segColor[j].equals(Utils.ColorSPIKE)))) {
										if ((_isspike) && ((_segColor[i].equals(Utils.ColorSPIKEPOINT)) || (_segColor[i].equals(Utils.ColorSPIKE)))) {
											if (Math.min(dbl1, dbl2) < Math.min(dl1, dl2)) {
												if (org._mphoto[j] != -1) {
													org._segColor[j] = Utils.ColorSPIKEPOINT;
													org._mphoto[j] = -1;
												}
											} else {
												if (org._mphoto[j] != -0.5) {
													org._segColor[j] = Utils.ColorSPIKE;
													org._mphoto[j] = -0.5;
												}
											}
										} else {
											if (Math.min(dbl1, dbl2) <= Math.min(dl1, dl2)) {
												if (org._mphoto[j] != -1) {
													org._segColor[j] = Utils.ColorSPIKEPOINT;
													org._mphoto[j] = -1;
												}
											} else {
												if (org._mphoto[j] != -0.5) {
													org._segColor[j] = Utils.ColorSPIKE;
													org._mphoto[j] = -0.5;
												}
											}
										}
									}
									if (org._segmentpretoucheffects > 0) {
										org.pretouchEffects(this,j,i);
									}
								}
								if ((((_parentID == org._ID || _ID == org._parentID) && !_generationbattle && !org._generationbattle) ||
									(_parentID == org._parentID && !_siblingbattle && !org._siblingbattle && _parentID != -1 ) ||
								    (_transfersenergy && org._transfersenergy && ((_peaceful && org._peaceful) ||
								    (_lengthfriend == org._lengthfriend && _thetafriend == org._thetafriend && _social && org._social))))
									 && alive && org.alive) {
									if (_canreact) {
										touchfriendReaction(org,i,j);
									}
									if (_usefriendeffects > 0) {
										touchfriendEffects(org,i,j);
									}
									if (org._canreact) {
										org.touchfriendReaction(this,j,i);
									}
									if (org._usefriendeffects > 0) {
										org.touchfriendEffects(this,j,i);
									}
								} else {
									if (_canreact) {
										if ((org._mphoto[j] == -9) && (!_hasgoodvision)) {
											org.mimic(j);
											touchReaction(org,i,j);
											org._segColor[j] = Utils.ColorDARK;
										} else {
											touchReaction(org,i,j);
										}
									}
									if (_mphoto[i] == -1) {
										touchEffects1(org,i,j);
									} else if (_mphoto[i] == -4) {
										touchEffects2(org,i,j);
									}
									if (org._canreact) {
										if ((_mphoto[i] == -9) && (!org._hasgoodvision)) {
											mimic(i);
											org.touchReaction(this,j,i);
											_segColor[i] = Utils.ColorDARK;
										} else {
											org.touchReaction(this,j,i);
										}
									}
									if (org._mphoto[j] == -1) {
										org.touchEffects1(this,j,i);
									} else if (org._mphoto[j] == -4) {
										org.touchEffects2(this,j,i);
									}
								}
								return true;
							}
						}
					}
				}
			} else if (_mphoto[i] == -13) {
				if (_canmove) {
					line.setLine(x1[i]+_centerX, y1[i]+_centerY, x2[i]+_centerX, y2[i]+_centerY);
					// First check if the line intersects the bounding box of the other organism
					if (org.intersectsLine(line)) {
						// Do the same for the other organism's segments.
						for (j = 0; j < org._segments; j++) {
							if (org._m[j]>=1) {
								bline.setLine(org.x1[j] + org._centerX, org.y1[j] + org._centerY,
										org.x2[j] + org._centerX, org.y2[j] + org._centerY);
								if (intersectsLine(bline) && line.intersectsLine(bline)) {
									// If we found two intersecting segments, apply effects
									if ((((_parentID == org._ID || _ID == org._parentID) && !_generationbattle && !org._generationbattle) ||
										(_parentID == org._parentID && !_siblingbattle && !org._siblingbattle && _parentID != -1 ) ||
									    (_transfersenergy && org._transfersenergy && ((_peaceful && org._peaceful) ||
									    (_lengthfriend == org._lengthfriend && _thetafriend == org._thetafriend && _social && org._social))))
										 && alive && org.alive) {
										touchfriendReaction(org,i,j);
									} else {
										if ((org._mphoto[j] == -9) && (!_hasgoodvision)) {
											org.mimic(j);
											touchReaction(org,i,j);
											org._segColor[j] = Utils.ColorDARK;
										} else {
											touchReaction(org,i,j);
										}
									}
								}
							}
						}
					}
				} else if (alive) {
					line.setLine(x1[i]+_centerX, y1[i]+_centerY, x2[i]+_centerX, y2[i]+_centerY);
					// First check if the line intersects the bounding box of the other organism
					if (org.intersectsLine(line)) {
						// Do the same for the other organism's segments.
						for (j = 0; j < org._segments; j++) {
							if (org._m[j]>=1) {
								bline.setLine(org.x1[j] + org._centerX, org.y1[j] + org._centerY,
										org.x2[j] + org._centerX, org.y2[j] + org._centerY);
								if (intersectsLine(bline) && line.intersectsLine(bline)) {
									// If we found two intersecting segments, apply effects
									if ((((_parentID == org._ID || _ID == org._parentID) && !_generationbattle && !org._generationbattle) ||
										(_parentID == org._parentID && !_siblingbattle && !org._siblingbattle && _parentID != -1 ) ||
									    (_transfersenergy && org._transfersenergy && ((_peaceful && org._peaceful) ||
									    (_lengthfriend == org._lengthfriend && _thetafriend == org._thetafriend && _social && org._social))))
										 && alive && org.alive) {
										touchfriendBrake(org,i,j);
									} else {
										if ((org._mphoto[j] == -9) && (!_hasgoodvision)) {
											org.mimic(j);
											touchBrake(org,i,j);
											org._segColor[j] = Utils.ColorDARK;
										} else {
											touchBrake(org,i,j);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	/**
	 * Applies forest photosynthesis, reactions and some other effects produced by two touching segments.
	 *
	 * @param org  The organism which is touching.
	 * @param seg  Index of this organism's segment.
	 * @param oseg  Index of the other organism's segment.
	 */
	private final void pretouchEffects(Organism org, int seg, int oseg) {
		if (_segmentpretoucheffects >= 2) {
			switch (getTypeColor(_segColor[seg])) {
			case PURPLE:
				// Purple segment: Get energy from CH4 in the atmosphere, boosted for enhanced organisms if touching brown
				switch (getTypeColor(org._segColor[oseg])) {
				case BROWN:
					if ((_isenhanced) && (!_isgray) && (!org.alive)) {
						if ((org._age >> 8) > org._max_age) {
							_energy += _world.methanotrophy(1.25 * _methanotrophy);
						} else {
							_energy += _world.methanotrophy(0.75 * _methanotrophy);
						}
					}
					break;
				default:
					break;
				}
				break;
			case DARKGRAY:
				// Darkgray segment: Get energy from CH4 in the atmosphere, boosted for purple organisms if touching brown
				switch (getTypeColor(org._segColor[oseg])) {
				case BROWN:
					if ((_methanotrophy > 0) && (!org.alive)) {
						if ((org._age >> 8) > org._max_age) {
							_energy += _world.methanotrophy(1.25 * _methanotrophy);
						} else {
							_energy += _world.methanotrophy(0.75 * _methanotrophy);
						}
					}
					break;
				default:
					break;
				}
				break;
			case DARK:
				if (_blackversion < 0) {
					// Dark segment: Mimic other segments
					switch (getTypeColor(org._segColor[oseg])) {
					case DARK:
					case GREEN:
					case GRASS:
					case FOREST:
					case SPRING:
					case SUMMER:
		            case WINTER:
					case LIME:
					case LEAF:
					case C4:
					case JADE:
					case DARKJADE:
					case DARKGREEN:
					case PURPLE:
					case SPORE:
					case FRUIT:
					case VISION:
					case DARKFIRE:
					case ICE:
					case LIGHT_BLUE:
					case LIGHTBROWN:
					case GREENBROWN:
					case BROKEN:
					case DEADBARK:
					case BROWN:
						break;
					default:
						if (_blackversion == -2) {
							for (int x = 0; x < _segments; x++) {
								switch (getTypeColor(_segColor[x])) {
								case DARK:
									if (org._segColor[oseg].equals(Utils.ColorBARK)) {
										if (_isaplant) {
											_segColor[x] = Utils.ColorBARK;
										} else {
											_segColor[x] = Utils.ColorOLDBARK;
										}
									} else {
										if (org._segColor[oseg].equals(Utils.ColorOLDBARK)) {
											if (_isaplant) {
												_segColor[x] = Utils.ColorBARK;
											} else {
												_segColor[x] = Utils.ColorOLDBARK;
											}
										} else {
											_segColor[x] = org._segColor[oseg];
										}
									}
								    _segredReaction[x] = org._segredReaction[oseg];
								    _seggreenReaction[x] = org._seggreenReaction[oseg];
								    _segblueReaction[x] = org._segblueReaction[oseg];
								    _segplagueReaction[x] = org._segplagueReaction[oseg];
								    _segscourgeReaction[x] = org._segscourgeReaction[oseg];
								    _segwhiteReaction[x] = org._segwhiteReaction[oseg];
								    _seggrayReaction[x] = org._seggrayReaction[oseg];
								    _segsilverReaction[x] = org._segsilverReaction[oseg];
								    _segdefaultReaction[x] = org._segdefaultReaction[oseg];
								    _segconsumerReaction[x] = org._segconsumerReaction[oseg];
								    _segplantReaction[x] = org._segplantReaction[oseg];
								    _segmagentaReaction[x] = org._segmagentaReaction[oseg];
								    _segpinkReaction[x] = org._segpinkReaction[oseg];
								    _segcoralReaction[x] = org._segcoralReaction[oseg];
								    _segorangeReaction[x] = org._segorangeReaction[oseg];
								    _segbarkReaction[x] = org._segbarkReaction[oseg];
								    _segvioletReaction[x] = org._segvioletReaction[oseg];
								    _segvirusReaction[x] = org._segvirusReaction[oseg];
								    _segmaroonReaction[x] = org._segmaroonReaction[oseg];
								    _segoliveReaction[x] = org._segoliveReaction[oseg];
								    _segmintReaction[x] = org._segmintReaction[oseg];
								    _segcreamReaction[x] = org._segcreamReaction[oseg];
								    _segspikeReaction[x] = org._segspikeReaction[oseg];
								    _segfallowReaction[x] = org._segfallowReaction[oseg];
								    _seglightblueReaction[x] = org._seglightblueReaction[oseg];
								    _segochreReaction[x] = org._segochreReaction[oseg];
								    _segskyReaction[x] = org._segskyReaction[oseg];
								    _seglilacReaction[x] = org._seglilacReaction[oseg];
								    _segfireReaction[x] = org._segfireReaction[oseg];
								    _seglightbrownReaction[x] = org._seglightbrownReaction[oseg];
								    _seggreenbrownReaction[x] = org._seggreenbrownReaction[oseg];
								    _segbrownReaction[x] = org._segbrownReaction[oseg];
								    _segiceReaction[x] = org._segiceReaction[oseg];
								    _segbrokenReaction[x] = org._segbrokenReaction[oseg];
								    _segsickReaction[x] = org._segsickReaction[oseg];
								    _segfriendReaction[x] = org._segfriendReaction[oseg];
								}
							}
						} else {
							if (org._segColor[oseg].equals(Utils.ColorBARK)) {
								if (_isaplant) {
									_segColor[seg] = Utils.ColorBARK;
								} else {
									_segColor[seg] = Utils.ColorOLDBARK;
								}
							} else {
								if (org._segColor[oseg].equals(Utils.ColorOLDBARK)) {
									if (_isaplant) {
										_segColor[seg] = Utils.ColorBARK;
									} else {
										_segColor[seg] = Utils.ColorOLDBARK;
									}
								} else {
									_segColor[seg] = org._segColor[oseg];
								}
							}
						    _segredReaction[seg] = org._segredReaction[oseg];
						    _seggreenReaction[seg] = org._seggreenReaction[oseg];
						    _segblueReaction[seg] = org._segblueReaction[oseg];
						    _segplagueReaction[seg] = org._segplagueReaction[oseg];
						    _segscourgeReaction[seg] = org._segscourgeReaction[oseg];
						    _segwhiteReaction[seg] = org._segwhiteReaction[oseg];
						    _seggrayReaction[seg] = org._seggrayReaction[oseg];
						    _segsilverReaction[seg] = org._segsilverReaction[oseg];
						    _segdefaultReaction[seg] = org._segdefaultReaction[oseg];
						    _segconsumerReaction[seg] = org._segconsumerReaction[oseg];
						    _segplantReaction[seg] = org._segplantReaction[oseg];
						    _segmagentaReaction[seg] = org._segmagentaReaction[oseg];
						    _segpinkReaction[seg] = org._segpinkReaction[oseg];
						    _segcoralReaction[seg] = org._segcoralReaction[oseg];
						    _segorangeReaction[seg] = org._segorangeReaction[oseg];
						    _segbarkReaction[seg] = org._segbarkReaction[oseg];
						    _segvioletReaction[seg] = org._segvioletReaction[oseg];
						    _segvirusReaction[seg] = org._segvirusReaction[oseg];
						    _segmaroonReaction[seg] = org._segmaroonReaction[oseg];
						    _segoliveReaction[seg] = org._segoliveReaction[oseg];
						    _segmintReaction[seg] = org._segmintReaction[oseg];
						    _segcreamReaction[seg] = org._segcreamReaction[oseg];
						    _segspikeReaction[seg] = org._segspikeReaction[oseg];
						    _segfallowReaction[seg] = org._segfallowReaction[oseg];
						    _seglightblueReaction[seg] = org._seglightblueReaction[oseg];
						    _segochreReaction[seg] = org._segochreReaction[oseg];
						    _segskyReaction[seg] = org._segskyReaction[oseg];
						    _seglilacReaction[seg] = org._seglilacReaction[oseg];
						    _segfireReaction[seg] = org._segfireReaction[oseg];
						    _seglightbrownReaction[seg] = org._seglightbrownReaction[oseg];
						    _seggreenbrownReaction[seg] = org._seggreenbrownReaction[oseg];
						    _segbrownReaction[seg] = org._segbrownReaction[oseg];
						    _segiceReaction[seg] = org._segiceReaction[oseg];
						    _segbrokenReaction[seg] = org._segbrokenReaction[oseg];
						    _segsickReaction[seg] = org._segsickReaction[oseg];
						    _segfriendReaction[seg] = org._segfriendReaction[oseg];
						}
						segmentsCreateEffects();
					}
					break;
				}
			}
		}
		if (_forestphoto > 0) {
		    if (org._photosynthesis > 0) {
				// Enhance photosynthesis in a colony, _mphoto value of -0.2 is darkgray
		    	if ((_mphoto[seg] <= 0) && (_mphoto[seg] != -0.2)) {
		    		if (org._mphoto[oseg] <= 0) {
						if (_colonyPhotosynthesis == 0) {
							_colonyPhotosynthesis += 0.325 * _forestphoto;
						} else {
							_colonyPhotosynthesis += 0.27 * _forestphoto;
						}
			    	} else {
			    		switch (getTypeColor(org._segColor[oseg])) {
						case FOREST:
							if (_colonyPhotosynthesis == 0) {
								_colonyPhotosynthesis += 0.525 * _forestphoto;
							} else {
								_colonyPhotosynthesis += 0.47 * _forestphoto;
							}
							break;
						case BARK:
						case OLDBARK:
						case PURPLE:
						case GREENBROWN:
						case DEADBARK:
						case ICE:
							if (_colonyPhotosynthesis == 0) {
								_colonyPhotosynthesis += 0.325 * _forestphoto;
							} else {
								_colonyPhotosynthesis += 0.27 * _forestphoto;
							}
							break;
						case DARKGRAY:
							break;
						default:
							if (_colonyPhotosynthesis == 0) {
								_colonyPhotosynthesis += 0.425 * _forestphoto;
							} else {
								_colonyPhotosynthesis += 0.37 * _forestphoto;
							}
							break;
						}
			    	}
		    	} else {
		    		switch (getTypeColor(_segColor[seg])) {
					case FOREST:
						if (org._mphoto[oseg] <= 0) {
							if (_colonyPhotosynthesis == 0) {
								_colonyPhotosynthesis += 0.525 * _forestphoto;
							} else {
								_colonyPhotosynthesis += 0.47 * _forestphoto;
							}
				    	} else {
				    		switch (getTypeColor(org._segColor[oseg])) {
							case FOREST:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 1.025 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.97 * _forestphoto;
								}
								break;
							case BARK:
							case OLDBARK:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.625 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.57 * _forestphoto;
								}
								break;
							case PURPLE:
							case GREENBROWN:
							case DEADBARK:
							case ICE:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.525 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.47 * _forestphoto;
								}
								break;
							case DARKGRAY:
								break;
							default:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.925 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.87 * _forestphoto;
								}
								break;
							}
				    	}
						break;
					case BARK:
					case OLDBARK:
						if (org._mphoto[oseg] <= 0) {
							if (_colonyPhotosynthesis == 0) {
								_colonyPhotosynthesis += 0.325 * _forestphoto;
							} else {
								_colonyPhotosynthesis += 0.27 * _forestphoto;
							}
				    	} else {
				    		switch (getTypeColor(org._segColor[oseg])) {
							case FOREST:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.625 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.57 * _forestphoto;
								}
								break;
							case BARK:
							case OLDBARK:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.425 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.37 * _forestphoto;
								}
								break;
							case PURPLE:
							case GREENBROWN:
							case DEADBARK:
							case ICE:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.325 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.27 * _forestphoto;
								}
								break;
							case DARKGRAY:
								break;
							default:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.525 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.47 * _forestphoto;
								}
								break;
							}
				    	}
						break;
					case PURPLE:
					case GREENBROWN:
					case DEADBARK:
					case ICE:
						if (org._mphoto[oseg] <= 0) {
							if (_colonyPhotosynthesis == 0) {
								_colonyPhotosynthesis += 0.325 * _forestphoto;
							} else {
								_colonyPhotosynthesis += 0.27 * _forestphoto;
							}
				    	} else {
				    		switch (getTypeColor(org._segColor[oseg])) {
							case FOREST:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.525 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.47 * _forestphoto;
								}
								break;
							case BARK:
							case OLDBARK:
							case PURPLE:
							case GREENBROWN:
							case DEADBARK:
							case ICE:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.325 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.27 * _forestphoto;
								}
								break;
							case DARKGRAY:
								break;
							default:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.425 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.37 * _forestphoto;
								}
								break;
							}
				    	}
						break;
					case DARKGRAY:
						if (!org._isenhanced) {
						    if ((!_isaconsumer) && (!_isakiller)) {
						    	_colonyPhotosynthesis += Utils.DARKGRAY_ENERGY_CONSUMPTION * _forestphoto;
							} else {
								_colonyPhotosynthesis += 0.5 * Utils.DARKGRAY_ENERGY_CONSUMPTION * _forestphoto;
							}
						}
						break;
					default:
						if (org._mphoto[oseg] <= 0) {
							if (_colonyPhotosynthesis == 0) {
								_colonyPhotosynthesis += 0.425 * _forestphoto;
							} else {
								_colonyPhotosynthesis += 0.37 * _forestphoto;
							}
				    	} else {
				    		switch (getTypeColor(org._segColor[oseg])) {
							case FOREST:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.925 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.87 * _forestphoto;
								}
								break;
							case BARK:
							case OLDBARK:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.525 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.47 * _forestphoto;
								}
								break;
							case PURPLE:
							case GREENBROWN:
							case DEADBARK:
							case ICE:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.425 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.37 * _forestphoto;
								}
								break;
							case DARKGRAY:
								break;
							default:
								if (_colonyPhotosynthesis == 0) {
									_colonyPhotosynthesis += 0.825 * _forestphoto;
								} else {
									_colonyPhotosynthesis += 0.77 * _forestphoto;
								}
								break;
							}
				    	}
						break;
					}
		    	}
			}
		} else if ((_framesColor <= 0) && (_colonyPhotosynthesis > 0) && (!_isaplant) && (org._isaplant) && (org.active)) {
			// photosynthesis is enhanced if an organism touches symbiotic rose organisms, if these are not a plant or consumer
			if (org._photosynthesis > 0) {
				double symbiontphotosynthesis = 0;
				if (org._transfersenergy) {
					if (org._forestphoto > 0) {
						symbiontphotosynthesis = _colonyPhotosynthesis * ((1.5 * org._photosynthesis) + (0.375 * org._forestphoto));
					} else {
						symbiontphotosynthesis = _colonyPhotosynthesis * (1.5 * org._photosynthesis);
					}
				} else {
	                if (org._forestphoto > 0) {
	                	symbiontphotosynthesis = _colonyPhotosynthesis * (org._photosynthesis + (0.25 * org._forestphoto));
					} else {
						symbiontphotosynthesis = _colonyPhotosynthesis * org._photosynthesis;
					}
				}
				org._energy += _world.photosynthesis(symbiontphotosynthesis);
				setColor(Utils.ColorROSE);
			}
			if (org._methanotrophy > 0) {
				double symbiontmethanotrophy = 0;
				if (org._transfersenergy) {
					symbiontmethanotrophy = _colonyPhotosynthesis * (0.1875 * org._methanotrophy);
				} else {
					symbiontmethanotrophy = _colonyPhotosynthesis * (0.125 * org._methanotrophy);
				}
				org._energy += _world.methanotrophy(symbiontmethanotrophy);
				setColor(Utils.ColorROSE);
			}
		}
	}
	/**
	 * Applies the friendly effects produced by two touching segments.
	 *
	 * @param org  The organism which is touching.
	 * @param seg  Index of this organism's segment.
	 * @param oseg  Index of the other organism's segment.
	 */
	private final void touchfriendEffects(Organism org, int seg, int oseg) {
		if ((_transfersenergy) && (_growthRatio==1) && (_energy > (org._energy+2))) {
			if (_altruist && org._altruist) {
				// Transfers energy
				double takenEnergy1 = Utils.between(2 * Utils.ORGANIC_OBTAINED_ENERGY, 0, (_energy - (org._energy+2)));
				// The other organism will be shown in cyan
				org.setColor(Color.CYAN);
				// This organism will be shown in rose
				setColor(Utils.ColorROSE);
				org._energy += takenEnergy1;
				_energy -= takenEnergy1;
				if ((!org.active) && ((org._sporeversion == 3) || (org._sporeversion == 4)) && (org._energy > 0.5*org._reproduceEnergy)) {
					org._timeToReproduce = 0;
				}
			} else {
				if ((_familial) && ((_lengthfriend == org._lengthfriend) || (_thetafriend == org._thetafriend) ||
					(_parentID == org._ID) || (_ID == org._parentID) || (_parentID == org._parentID && _parentID != -1 ))) {
					// Transfers energy
					double takenEnergy2 = Utils.between(2 * Utils.ORGANIC_OBTAINED_ENERGY, 0, (_energy - (org._energy+2)));
					// The other organism will be shown in cyan
					org.setColor(Color.CYAN);
					// This organism will be shown in rose
					setColor(Utils.ColorROSE);
					org._energy += takenEnergy2;
					_energy -= takenEnergy2;
					if ((!org.active) && ((org._sporeversion == 3) || (org._sporeversion == 4)) && (org._energy > 0.5*org._reproduceEnergy)) {
						org._timeToReproduce = 0;
					}
				} else {
					if ((_peaceful) && (_isaplant) && (!org._altruist) && (org._transfersenergy) && (org.active) &&
						(!org._isaplant) && (!org._isaconsumer) && (!org._isafungus)) {
						// Transfers energy
						double takenEnergy3 = Utils.between(0.5 * Utils.ORGANIC_OBTAINED_ENERGY, 0, (_energy - (org._energy+2)));
						// The other organism will be shown in cyan
						org.setColor(Color.CYAN);
						// This organism will be shown in rose
						setColor(Utils.ColorROSE);
						org._energy += takenEnergy3;
						_energy -= takenEnergy3;
					}
				}
			}
		}
		if (_usefriendeffects == 2) {
		// Magenta segment: Heal all sick segments
		if ((org._isinjured) && ((_segColor[seg].equals(Color.MAGENTA)) || ((_transfersenergy) && (_healing > 0)))) {
			switch (getTypeColor(org._segColor[oseg])) {
			case BROWN:
				break;
			case MAGENTA:
				boolean heal1 =false;
				for (int j = 0; j < org._segments; j++) {
				if ((org._segColor[j].equals(Utils.ColorLIGHTBROWN)) || (org._segColor[j].equals(Utils.ColorGREENBROWN)) || (org._segColor[j].equals(Utils.ColorBROKEN))
					|| (org._segColor[j].equals(Utils.ColorLIGHT_BLUE)) || (org._segColor[j].equals(Utils.ColorICE)) || (org._segColor[j].equals(Utils.ColorDARKFIRE))) {
					    if ((org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION/2))) {
							org._segColor[j] = org._geneticCode.getGene(j%org._geneticCode.getNGenes()).getColor();
							setColor(Color.MAGENTA);
							heal1 =true;
					    }
					}
			    }
				if (heal1) {
					org._isfrozen =false;
					org._isinjured =false;
					org._allfrozen =false;
					org._updateEffects = 2;
					org.segmentsRestoreEffects();
				}
				break;
			default:
				if (_altruist && org._altruist) {
					if (org.active) {
					boolean heal2 =false;
			        for (int j = 0; j < org._segments; j++) {
			        if ((org._segColor[j].equals(Utils.ColorLIGHTBROWN)) || (org._segColor[j].equals(Utils.ColorGREENBROWN)) || (org._segColor[j].equals(Utils.ColorBROKEN))
						|| (org._segColor[j].equals(Utils.ColorLIGHT_BLUE)) || (org._segColor[j].equals(Utils.ColorICE)) || (org._segColor[j].equals(Utils.ColorDARKFIRE))) {
			        	if ((org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION/2))) {
							    org._segColor[j] = org._geneticCode.getGene(j%org._geneticCode.getNGenes()).getColor();
							    setColor(Color.MAGENTA);
							    heal2 =true;
					        }
					    }
					}
			        if (heal2) {
			        	org._isfrozen =false;
						org._isinjured =false;
						org._allfrozen =false;
			        	org._updateEffects = 2;
						org.segmentsRestoreEffects();
					}
				    } else if (org._geneticCode.getModifiesspore() >= 7) {
						for (int a = 0; a < org._segments; a++) {
							org._segColor[a] = Color.BLUE;
						}
						org._isinjured =false;
						org._isfrozen =false;
						org._allfrozen =false;
				    }
				} else if ((_familial) && ((_lengthfriend == org._lengthfriend) || (_thetafriend == org._thetafriend) ||
					(_parentID == org._ID) || (_ID == org._parentID) || (_parentID == org._parentID && _parentID != -1 ))) {
					if (org.active) {
					boolean heal3 =false;
					for (int j = 0; j < org._segments; j++) {
					if ((org._segColor[j].equals(Utils.ColorLIGHTBROWN)) || (org._segColor[j].equals(Utils.ColorGREENBROWN)) || (org._segColor[j].equals(Utils.ColorBROKEN))
						|| (org._segColor[j].equals(Utils.ColorLIGHT_BLUE)) || (org._segColor[j].equals(Utils.ColorICE)) || (org._segColor[j].equals(Utils.ColorDARKFIRE))) {
						if ((org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION/2))) {
								org._segColor[j] = org._geneticCode.getGene(j%org._geneticCode.getNGenes()).getColor();
								setColor(Color.MAGENTA);
								heal3 =true;
							}
						}
					}
					if (heal3) {
						org._isfrozen =false;
						org._isinjured =false;
						org._allfrozen =false;
						org._updateEffects = 2;
						org.segmentsRestoreEffects();
					}
					} else if (org._geneticCode.getModifiesspore() >= 7) {
						for (int a = 0; a < org._segments; a++) {
							org._segColor[a] = Color.BLUE;
						}
						org._isinjured =false;
						org._isfrozen =false;
						org._allfrozen =false;
				    }
				}
			}
		}
		// Mint segment: Remove an infection
		if ((_segColor[seg].equals(Utils.ColorMINT)) || ((_transfersenergy) && (_antiviral > 0))) {
			switch (getTypeColor(org._segColor[oseg])) {
			case BROWN:
				break;
			case MINT:
				if (org._infectedGeneticCode != null) {
					if ((org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2))) {
						org._infectedGeneticCode = null;
						org._savedGeneticCode = null;
						org.setColor(Color.CYAN);
						setColor(Utils.ColorMINT);
					}
				}
				if ((org._fallowinhibition > 0) && (_timeToReproduce <= _timeToReproduceMax)) {
					if ((org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2))) {
						org._fallowinhibition = 0;
						org.setColor(Color.CYAN);
						setColor(Utils.ColorMINT);
					}
				}
				break;
			default:
				if (_altruist && org._altruist) {
					if ((org._infectedGeneticCode != _geneticCode) && (org._infectedGeneticCode != null)) {
						if ((org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2))) {
							org._infectedGeneticCode = null;
							org._savedGeneticCode = null;
							org.setColor(Color.CYAN);
							setColor(Utils.ColorMINT);
						}
					}
					if ((org._fallowinhibition > 0) && (_timeToReproduce <= _timeToReproduceMax)) {
						if ((org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2))) {
							org._fallowinhibition = 0;
							org.setColor(Color.CYAN);
							setColor(Utils.ColorMINT);
						}
					}
				} else if ((_familial) && ((_lengthfriend == org._lengthfriend) || (_thetafriend == org._thetafriend) ||
					(_parentID == org._ID) || (_ID == org._parentID) || (_parentID == org._parentID && _parentID != -1 ))) {
					if ((org._infectedGeneticCode != _geneticCode) && (org._infectedGeneticCode != null)) {
						if ((org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2))) {
							org._infectedGeneticCode = null;
							org._savedGeneticCode = null;
							org.setColor(Color.CYAN);
							setColor(Utils.ColorMINT);
						}
					}
					if ((org._fallowinhibition > 0) && (_timeToReproduce <= _timeToReproduceMax)) {
						if ((org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2))) {
							org._fallowinhibition = 0;
							org.setColor(Color.CYAN);
							setColor(Utils.ColorMINT);
						}
					}
				}
			}
		}
		// Lavender segment: Immunity vs Plague, Coral and Fallow, it can immunize other organisms temporarily.
		if ((_segColor[seg].equals(Utils.ColorLAVENDER)) || ((_transfersenergy) && (_createlavender > 0))) {
			switch (getTypeColor(org._segColor[oseg])) {
			case BROWN:
				break;
			case LAVENDER:
				if (_lavender >= Utils.LAVENDER_SHIELD) {
					if (org._lavender < Utils.LAVENDER_SHIELD) {
						org._lavender += _createlavender;
						if (org._lavender >= Utils.LAVENDER_SHIELD) {
							org._lavender = Utils.LAVENDER_SHIELD;
						}
						org.setColor(Color.CYAN);
						setColor(Utils.ColorLAVENDER);
					}
				}
				if (_timeToReproduce <= _timeToReproduceMax) {
					if (org._timeToReproduce > org._timeToReproduceMax) {
			    		org._timeToReproduce -= (_createlavender/20);
						if (org._timeToReproduce < org._timeToReproduceMax) {
							org._timeToReproduce = org._timeToReproduceMax;
						}
						org.setColor(Color.CYAN);
						setColor(Utils.ColorLAVENDER);
					}
				}
				break;
			default:
				if (_altruist && org._altruist) {
					if (_lavender >= Utils.LAVENDER_SHIELD) {
						if (org._lavender < Utils.LAVENDER_SHIELD) {
							org._lavender += _createlavender;
							if (org._lavender >= Utils.LAVENDER_SHIELD) {
								org._lavender = Utils.LAVENDER_SHIELD;
							}
							org.setColor(Color.CYAN);
							setColor(Utils.ColorLAVENDER);
						}
					}
					if (_timeToReproduce <= _timeToReproduceMax) {
						if (org._timeToReproduce > org._timeToReproduceMax) {
				    		org._timeToReproduce -= (_createlavender/20);
							if (org._timeToReproduce < org._timeToReproduceMax) {
								org._timeToReproduce = org._timeToReproduceMax;
							}
							org.setColor(Color.CYAN);
							setColor(Utils.ColorLAVENDER);
						}
					}
				} else if ((_familial) && ((_lengthfriend == org._lengthfriend) || (_thetafriend == org._thetafriend) ||
					(_parentID == org._ID) || (_ID == org._parentID) || (_parentID == org._parentID && _parentID != -1 ))) {
					if (_lavender >= Utils.LAVENDER_SHIELD) {
						if (org._lavender < Utils.LAVENDER_SHIELD) {
							org._lavender += _createlavender;
							if (org._lavender >= Utils.LAVENDER_SHIELD) {
								org._lavender = Utils.LAVENDER_SHIELD;
							}
							org.setColor(Color.CYAN);
							setColor(Utils.ColorLAVENDER);
						}
					}
					if (_timeToReproduce <= _timeToReproduceMax) {
						if (org._timeToReproduce > org._timeToReproduceMax) {
				    		org._timeToReproduce -= (_createlavender/20);
							if (org._timeToReproduce < org._timeToReproduceMax) {
								org._timeToReproduce = org._timeToReproduceMax;
							}
							org.setColor(Color.CYAN);
							setColor(Utils.ColorLAVENDER);
						}
					}
				}
			}
		}
		}
	}
	/**
	 * Applies the friendly reaction effects produced by two touching segments.
	 *
	 * @param org  The organism which is touching.
	 * @param seg  Index of this organism's segment.
	 * @param oseg  Index of the other organism's segment.
	 */
	private final void touchfriendReaction(Organism org, int seg, int oseg) {
		int i;
		for (i=_segments-1; i>=0; i--) {
			if ((_mphoto[i] == -22) || (_mphoto[i] == -21) || ((_mphoto[seg] == -13) && (_mphoto[i] == -23))) {
				// Teal segment: React on other organisms, _mphoto values above of -21 (passive) and -22 (active) are TEAL, -23 is CYAN and -13 is the EYE
				switch (getTypeColor(_segColor[seg])) {
				default:
	                  switch (getTypeColor(org._segColor[oseg])) {
	                  case EYE:
						  break;
	                  default:
						    if (_segfriendReaction[seg] == 1) {
						    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
						    	stopmoving();
						    }} else
						    if (_segfriendReaction[seg] == 2) {
						    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(3 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(3 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
						    }} else
			                if (_segfriendReaction[seg] == 3) {
			    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
			    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(3 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
			    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(3 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
			    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segfriendReaction[seg] == 4) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(3 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(3 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segfriendReaction[seg] == 5) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
								dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(3 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
								dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(3 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
								dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
			                }}
					}
					break;
				}
				break;
			}
		}
	}
	/**
	 * Applies the reaction effects produced by two touching segments.
	 *
	 * @param org  The organism which is touching.
	 * @param seg  Index of this organism's segment.
	 * @param oseg  Index of the other organism's segment.
	 */
	private final void touchReaction(Organism org, int seg, int oseg) {
		int i;
		for (i=_segments-1; i>=0; i--) {
			if ((_mphoto[i] == -22) || (_mphoto[i] == -21) || ((_mphoto[seg] == -13) && (_mphoto[i] == -23))) {
				// Teal segment: React on other organisms, _mphoto values above of -21 (passive) and -22 (active) are TEAL, -23 is CYAN and -13 is the EYE
				switch (getTypeColor(_segColor[seg])) {
				case SPIKE:
				case DARKOLIVE:
				case BROKEN:
				case DARKFIRE:
				case LIGHTBROWN:
				case GREENBROWN:
				case LIGHT_BLUE:
				case DEADBARK:
				case ICE:
					  switch (getTypeColor(org._segColor[oseg])) {
					  case EYE:
						  break;
					  default:
						    if (_segsickReaction[seg] == 1) {
						    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
						    	stopmoving();
						    }} else
						    if (_segsickReaction[seg] == 2) {
						    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segsickReaction[seg] == 3) {
		    			    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
		    				    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    				    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		    	            }} else
		                    if (_segsickReaction[seg] == 4) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segsickReaction[seg] == 5) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
								dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
								dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
								dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
			                }}
					  }
					  break;
				default:
					if (org._mphoto[oseg] == -1) {
	                  switch (getTypeColor(org._segColor[oseg])) {
	                  case ORANGE:
	                	    if (_segorangeReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segorangeReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segorangeReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segorangeReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segorangeReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  case FIRE:
	              	        if (_segfireReaction[seg] == 1) {
	              	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	              	        	stopmoving();
						    }} else
	                	    if (_segfireReaction[seg] == 2) {
	                	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                	    }} else
	    	                if (_segfireReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segfireReaction[seg] == 4) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segfireReaction[seg] == 5) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
		  						dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		  						dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		  						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
			                }}
	              	        break;
	                  case RED:
	                	    if (_segredReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                  	    if (_segredReaction[seg] == 2) {
	                  	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  						    dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  						    dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  						    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segredReaction[seg] == 3) {
		    	    	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
		    	    		    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    	    			dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    	    			dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		    	    	    }} else
		                    if (_segredReaction[seg] == 4) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  						    dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  						    dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  						    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segredReaction[seg] == 5) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
		  						dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		  						dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		  						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
			                }}
	                	    break;
	                  case PINK:
	                	    if (_segpinkReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segpinkReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segpinkReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segpinkReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segpinkReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  case MAROON:
	                	    if (_segmaroonReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segmaroonReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segmaroonReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segmaroonReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segmaroonReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  case CREAM:
	                	    if (_segcreamReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segcreamReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segcreamReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segcreamReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segcreamReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  case OCHRE:
	                	    if (_segochreReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segochreReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segochreReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segochreReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segochreReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  case SILVER:
	                	  if (((org._nTotalKills > 0) && ((org._isaconsumer) || (org._isafungus))) || (org._isenhanced)) {
	                	    if (_segsilverReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segsilverReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segsilverReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segsilverReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segsilverReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	  } else if (org._isaplant) {
	                      	if (_segplantReaction[seg] == 1) {
	                        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                            stopmoving();
	            			}} else
	                        if (_segplantReaction[seg] == 2) {
	                        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	                			dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	                			dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	                			dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                        }} else
	          	            if (_segplantReaction[seg] == 3) {
	          	    	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	          	    	    	dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          	    	    	dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          	    	    	dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	              	        }} else
	              	        if (_segplantReaction[seg] == 4) {
	              	        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	                			dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	                			dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	                			dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	              	        }} else
	              	        if (_segplantReaction[seg] == 5) {
	              	        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	              	    		dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	              	    		dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	              	    		dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	              	  	    }}
	                	  } else if ((org._isaconsumer) || (org._isafungus)) {
	                        if (_segconsumerReaction[seg] == 1) {
	                        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                        	stopmoving();
	        				}} else
	                        if (_segconsumerReaction[seg] == 2) {
	                        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	            				dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            				dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                        }} else
	          	            if (_segconsumerReaction[seg] == 3) {
	          	    	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	          	    	    	dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          	    	    	dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          	    	    	dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	          	            }} else
	          	            if (_segconsumerReaction[seg] == 4) {
	          	            if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	            				dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            				dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	          	            }} else
	          	            if (_segconsumerReaction[seg] == 5) {
	          	            if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	          	    			dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          	    			dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          	    			dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	          	  	        }}
	                      } else {
	                        if (_segdefaultReaction[seg] == 1) {
	                        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                          	stopmoving();
	            		    }} else
	                        if (_segdefaultReaction[seg] == 2) {
	                        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	                			dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	                			dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	                			dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                        }} else
	          	            if (_segdefaultReaction[seg] == 3) {
	          	    	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	          	    	    	dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          	    	    	dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          	    	    	dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	              	        }} else
	              	        if (_segdefaultReaction[seg] == 4) {
	              	        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	                			dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	                			dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	                			dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	              	        }} else
	              	        if (_segdefaultReaction[seg] == 5) {
	              	        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	              	    		dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	              	    		dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	              	    		dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	              	  	    }}
	                      }
	                      break;
	                  case SPIKEPOINT:
	                	    if (_segspikeReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segspikeReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segspikeReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segspikeReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segspikeReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  case GRAY:
	                	    if (_seggrayReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_seggrayReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_seggrayReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_seggrayReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_seggrayReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                     }
	                     break;
	                } else {
	                  switch (getTypeColor(org._segColor[oseg])) {
	                  case EYE:
						    break;
	                  case WHITE:
	                  case PLAGUE:
	                	  if (org._isaplant) {
	                		if (org._plagueversion > 0) {
	                		if (_segplagueReaction[seg] == 1) {
	                        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                          	stopmoving();
	            			}} else
	                        if (_segplagueReaction[seg] == 2) {
	                        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	                			dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	                			dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	                			dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                        }} else
	    	                if (_segplagueReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	              	        }} else
	              	        if (_segplagueReaction[seg] == 4) {
	              	        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	                			dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	                			dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	                			dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	              	        }} else
	              	        if (_segplagueReaction[seg] == 5) {
	              	        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	              	    	    dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	              	    		dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	              	    		dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	              	  	    }}
	                		} else {
	                	    if (_segwhiteReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segwhiteReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segwhiteReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segwhiteReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segwhiteReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                		}
	                	  } else if ((org._plagueversion > 0) || (org._isaconsumer) || (org._isafungus) || (org._isauburn)) {
	                  		if (_segscourgeReaction[seg] == 1) {
	                  		if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                  			stopmoving();
	    					}} else
	                        if (_segscourgeReaction[seg] == 2) {
	                        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	        					dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        					dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                        }} else
	    	                if (_segscourgeReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	      	                }} else
	      	                if (_segscourgeReaction[seg] == 4) {
	      	                if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	        					dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        					dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	      	                }} else
	      	                if (_segscourgeReaction[seg] == 5) {
	      	                if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	      	    			    dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      	    				dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      	    				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	      	  	            }}
	                	  } else {
	                		if (_segvirusReaction[seg] == 1) {
	                		if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                			stopmoving();
	  					    }} else
	                      	if (_segvirusReaction[seg] == 2) {
	                      	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	      						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                      	}} else
	    	                if (_segvirusReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	    	                }} else
	    	                if (_segvirusReaction[seg] == 4) {
	    	                if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	      						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	    	                }} else
	    	                if (_segvirusReaction[seg] == 5) {
	    	                if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    			    dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    				dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	    	  	            }}
	                	  }
	                	  break;
	                  case BARK:
	                  case OLDBARK:
	              	        if (_segbarkReaction[seg] == 1) {
	              	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	              	        	stopmoving();
						    }} else
	                  	    if (_segbarkReaction[seg] == 2) {
	                  	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  						    dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  						    dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  						    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                  	    }} else
	  	                    if (_segbarkReaction[seg] == 3) {
	  	    	    	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    	    	    	dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    	    	    	dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    	    	    	dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segbarkReaction[seg] == 4) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  						    dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  						    dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  						    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segbarkReaction[seg] == 5) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
		    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		  	                }}
	              	        break;
	                  case GREEN:
	                  case FOREST:
	                  case SPRING:
	                  case SUMMER:
	                  case WINTER:
	                  case LIME:
	                  case LEAF:
	                  case C4:
	                  case JADE:
	                  case DARKJADE:
	                  case DARKGREEN:
	                  case GRASS:
	                  case PURPLE:
	            	        if (_seggreenReaction[seg] == 1) {
	            	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	            	        	stopmoving();
						    }} else
	                        if (_seggreenReaction[seg] == 2) {
	                        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
						        dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
						        dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
						        dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_seggreenReaction[seg] == 3) {
		    	    	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
		    	    	    	dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    	    	    	dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    	    	    	dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		    	    	    }} else
		                    if (_seggreenReaction[seg] == 4) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
						        dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
						        dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
						        dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_seggreenReaction[seg] == 5) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
		    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		  	                }}
	            	        break;
	                  case BLUE:
	                	    if (_segblueReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segblueReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segblueReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segblueReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segblueReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  case OLIVE:
	                	    if (_segoliveReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	              	        if (_segoliveReaction[seg] == 2) {
	              	        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	              	        }} else
	  	                    if (_segoliveReaction[seg] == 3) {
	  	    	    	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    	    	    	dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    	    	    	dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    	    	    	dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segoliveReaction[seg] == 4) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segoliveReaction[seg] == 5) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
								dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
								dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
								dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
			                }}
	                	    break;
	                  case SKY:
	                  case DEEPSKY:
	                	    if (_segskyReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                	    if (_segskyReaction[seg] == 2) {
	                	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                	    }} else
	    	                if (_segskyReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segskyReaction[seg] == 4) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segskyReaction[seg] == 5) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
								dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
								dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
								dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
			                }}
	                	    break;
	                  case FRUIT:
	                	  if (org._sporeversion == 2) {
	                		if (_segwhiteReaction[seg] == 1) {
	    	                if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	    	                  	stopmoving();
	    	  				}} else
	    	                if (_segwhiteReaction[seg] == 2) {
	    	                if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	      				dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	      				dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	      				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	    	                }} else
	    	      	        if (_segwhiteReaction[seg] == 3) {
	    	      	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	      	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	      	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	      	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	    	    	        }} else
	    	    	        if (_segwhiteReaction[seg] == 4) {
	    	    	        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	      				dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	      				dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	      				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	    	    	        }} else
	    	    	        if (_segwhiteReaction[seg] == 5) {
	    	    	        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    		dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    		dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    		dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	    	    	  	    }}
	                	  } else {
	                		if (_segvirusReaction[seg] == 1) {
			                if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
			                	stopmoving();
			  				}} else
			                if (_segvirusReaction[seg] == 2) {
			                if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
			      				dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
			      				dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
			      				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
			                }} else
			    	        if (_segvirusReaction[seg] == 3) {
			    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
			    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
			    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
			    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
			    	        }} else
			    	        if (_segvirusReaction[seg] == 4) {
			    	        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
			      				dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
			      				dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
			      				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
			    	        }} else
			    	        if (_segvirusReaction[seg] == 5) {
			    	        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
			    	    		dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
			    	    		dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
			    	    		dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
			    	  	    }}
			              }
			              break;
	                  case CORAL:
	              	        if (_segcoralReaction[seg] == 1) {
	              	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	              	        	stopmoving();
						    }} else
	                  	    if (_segcoralReaction[seg] == 2) {
	                  	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  						    dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  						    dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  						    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                  	    }} else
	  	                    if (_segcoralReaction[seg] == 3) {
	  	    	    	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    	    	    	dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    	    	    	dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    	    	    	dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segcoralReaction[seg] == 4) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  						    dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  						    dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  						    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segcoralReaction[seg] == 5) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
		    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		  	                }}
	              	        break;
	                  case FALLOW:
	                	    if (_segfallowReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segfallowReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segfallowReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segfallowReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segfallowReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  case VIOLET:
	                	    if (_segvioletReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segvioletReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segvioletReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segvioletReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segvioletReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  case SPIKE:
	                	  if (_mphoto[seg] == -13) {
	                		if (_segspikeReaction[seg] == 1) {
	                      	if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                      	    stopmoving();
	      					}} else
	                        if (_segspikeReaction[seg] == 2) {
	                        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	          				    dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          					dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                        }} else
	    	                if (_segspikeReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	        	            }} else
	        	            if (_segspikeReaction[seg] == 4) {
	        	            if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	          					dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          					dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	        	            }} else
	        	            if (_segspikeReaction[seg] == 5) {
	        	            if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	        	    			dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        	    			dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        	    			dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	        	  	        }}
	                      } else if (org._isaplant) {
	                  	    if (_seggreenReaction[seg] == 1) {
	                  	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                  	    	stopmoving();
	  					    }} else
	                      	if (_seggreenReaction[seg] == 2) {
	                      	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	      						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                      	}} else
	    	                if (_seggreenReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	    	                }} else
	    	                if (_seggreenReaction[seg] == 4) {
	    	                if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	      						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	    	                }} else
	    	                if (_seggreenReaction[seg] == 5) {
	    	                if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    				dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    				dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	    	  	            }}
	                      } else if (org._isenhanced) {
	                  		if (_segconsumerReaction[seg] == 1) {
	                    	if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                    		stopmoving();
	      					}} else
	                        if (_segconsumerReaction[seg] == 2) {
	                        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	          					dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          					dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                        }} else
	    	                if (_segconsumerReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	        	            }} else
	        	            if (_segconsumerReaction[seg] == 4) {
	        	            if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	          					dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          					dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	        	            }} else
	        	            if (_segconsumerReaction[seg] == 5) {
	        	            if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	        	    			dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        	    		    dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        	    		    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	        	  	        }}
	                      } else {
	                      	if (_segblueReaction[seg] == 1) {
	                        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                        	stopmoving();
	          				}} else
	                        if (_segblueReaction[seg] == 2) {
	                        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	              				dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	              				dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	              				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                        }} else
	        	            if (_segblueReaction[seg] == 3) {
	        	    	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	        	    	    	dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        	    	    	dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        	    	    	dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	            	        }} else
	            	        if (_segblueReaction[seg] == 4) {
	            	        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	              				dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	              				dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	              				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	            	        }} else
	            	        if (_segblueReaction[seg] == 5) {
	            	        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	            	    	    dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            	    		dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            	    		dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	            	  	    }}
	                      }
	                	  break;
	                  case LILAC:
	                  case DARKLILAC:
	                	    if (_seglilacReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                	    if (_seglilacReaction[seg] == 2) {
	                	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                	    }} else
	    	                if (_seglilacReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_seglilacReaction[seg] == 4) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_seglilacReaction[seg] == 5) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
								dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
								dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
								dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
			                }}
	                	    break;
	                  case MINT:
	                  case LAVENDER:
	                	    if (_segmintReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segmintReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segmintReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segmintReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segmintReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  case MAGENTA:
	                  case ROSE:
	                	    if (_segmagentaReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segmagentaReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segmagentaReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segmagentaReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segmagentaReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  case LIGHTBROWN:
	                  case DARKFIRE:
	            	        if (_seglightbrownReaction[seg] == 1) {
	            	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	            	        	stopmoving();
						    }} else
	                	    if (_seglightbrownReaction[seg] == 2) {
	                	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                	    }} else
	    	                if (_seglightbrownReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_seglightbrownReaction[seg] == 4) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_seglightbrownReaction[seg] == 5) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
		    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		  	                }}
	            	        break;
	                  case GREENBROWN:
	            	        if (_seggreenbrownReaction[seg] == 1) {
	            	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	            	        	stopmoving();
						    }} else
	                	    if (_seggreenbrownReaction[seg] == 2) {
	                	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                	    }} else
	    	                if (_seggreenbrownReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_seggreenbrownReaction[seg] == 4) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_seggreenbrownReaction[seg] == 5) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
		    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		  	                }}
	            	        break;
	                  case DARKOLIVE:
	                  case LIGHT_BLUE:
	                	    if (_seglightblueReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_seglightblueReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_seglightblueReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_seglightblueReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_seglightblueReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  case ICE:
	                  case DEADBARK:
	            	        if (_segiceReaction[seg] == 1) {
	            	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	            	        	stopmoving();
						    }} else
	                	    if (_segiceReaction[seg] == 2) {
	                	    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                	    }} else
	    	                if (_segiceReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segiceReaction[seg] == 4) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
							    dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		                    }} else
		                    if (_segiceReaction[seg] == 5) {
		                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
		    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
		    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
		  	                }}
	            	        break;
	                  case BROKEN:
	                	    if (_segbrokenReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segbrokenReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segbrokenReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segbrokenReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segbrokenReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  case BROWN:
	                	    if (_segbrownReaction[seg] == 1) {
	                	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	    	stopmoving();
						    }} else
	                    	if (_segbrownReaction[seg] == 2) {
	                    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                    	}} else
	    	                if (_segbrownReaction[seg] == 3) {
	    	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segbrownReaction[seg] == 4) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	    						dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	    						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	                    }} else
	  	                    if (_segbrownReaction[seg] == 5) {
	  	                    if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	  	    					dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	  	    					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	  	  	                }}
	                	    break;
	                  default:
	                	  if ((org._isaconsumer) || (org._isafungus)) {
	                  		if (_segconsumerReaction[seg] == 1) {
	                        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                        	stopmoving();
	        				}} else
	                        if (_segconsumerReaction[seg] == 2) {
	                        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	            				dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            			    dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                        }} else
	      	                if (_segconsumerReaction[seg] == 3) {
	      	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	      	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	          	            }} else
	          	            if (_segconsumerReaction[seg] == 4) {
	          	            if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	            				dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            				dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	          	            }} else
	          	            if (_segconsumerReaction[seg] == 5) {
	          	            if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	          	    			dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          	    			dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          	    			dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	          	  	        }}
	                      } else if (org._isaplant) {
	                    	if (_segplantReaction[seg] == 1) {
	                    	if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                    	    stopmoving();
	    					}} else
	                        if (_segplantReaction[seg] == 2) {
	                        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	        					dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        					dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                        }} else
	      	                if (_segplantReaction[seg] == 3) {
	      	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	      	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	      	                }} else
	      	                if (_segplantReaction[seg] == 4) {
	      	                if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	        					dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        					dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	        					dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	      	                }} else
	      	                if (_segplantReaction[seg] == 5) {
	      	                if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	      	    				dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      	    				dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      	    				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	      	  	            }}
	                      } else {
	                    	if (_segdefaultReaction[seg] == 1) {
	                      	if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                      		stopmoving();
	        				}} else
	                        if (_segdefaultReaction[seg] == 2) {
	                        if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	            				dx=Utils.between(((x1[seg]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            				dy=Utils.between(((y1[seg]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	                        }} else
	      	                if (_segdefaultReaction[seg] == 3) {
	      	    	    	if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	      	    	    	    dx=Utils.between(((x1[0]-x2[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      	    	    	    dy=Utils.between(((y1[0]-y2[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	      	    	    	    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	          	            }} else
	          	            if (_segdefaultReaction[seg] == 4) {
	          	            if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	            				dx=Utils.between(((x2[seg]-x1[seg])*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            				dy=Utils.between(((y2[seg]-y1[seg])*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	            				dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	          	            }} else
	          	            if (_segdefaultReaction[seg] == 5) {
	          	            if (((!_candodge && Utils.random.nextBoolean()) || (_candodge && Utils.random.nextInt(10)>2)) && (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION))) {
	          	    			dx=Utils.between(((org._centerX-_centerX)*(_m[i]*_m[i])+12d*(x2[i]-x1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          	    		    dy=Utils.between(((org._centerY-_centerY)*(_m[i]*_m[i])+12d*(y2[i]-y1[i]))/(1.5 * _mass), -Utils.MAX_VEL, Utils.MAX_VEL);
	          	    		    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
	          	  	        }}
	                      }
	                  	  break;
	                  }
	                  break;
	                }
	            }
				break;
			}
		}
	}
	/**
	 * Applies the friendly brake effects produced by an eye segment of a non moving organism touching another segment
	 *
	 * @param org  The organism which is touching.
	 * @param seg  Index of this organism's segment.
	 * @param oseg  Index of the other organism's segment.
	 */
	private final void touchfriendBrake(Organism org, int seg, int oseg) {
		switch (getTypeColor(_segColor[seg])) {
		default:
              switch (getTypeColor(org._segColor[oseg])) {
              case EYE:
				  break;
              default:
				    if (_segfriendReaction[seg] == 1) {
				    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
				    	standstill();
				    }}
    	            break;
			  }
			  break;
		}
	}
	/**
	 * Applies the brake effects produced by an eye segment of a non moving organism touching another segment
	 *
	 * @param org  The organism which is touching.
	 * @param seg  Index of this organism's segment.
	 * @param oseg  Index of the other organism's segment.
	 */
	private final void touchBrake(Organism org, int seg, int oseg) {
		switch (getTypeColor(_segColor[seg])) {
		default:
			if (org._mphoto[oseg] == -1) {
              switch (getTypeColor(org._segColor[oseg])) {
              case ORANGE:
            	    if (_segorangeReaction[seg] == 1) {
                    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
                	    standstill();
      				}}
      	            break;
              case FIRE:
        	        if (_segfireReaction[seg] == 1) {
        	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
        	        	standstill();
				    }}
                    break;
              case RED:
            	    if (_segredReaction[seg] == 1) {
            	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
            	    	standstill();
				    }}
    	            break;
              case PINK:
          	        if (_segpinkReaction[seg] == 1) {
          	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
          	    	    standstill();
				    }}
	                break;
              case MAROON:
          	        if (_segmaroonReaction[seg] == 1) {
          	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
          	    	    standstill();
				    }}
	                break;
              case CREAM:
          	        if (_segcreamReaction[seg] == 1) {
          	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
          	    	    standstill();
				    }}
	                break;
              case OCHRE:
        	        if (_segochreReaction[seg] == 1) {
        	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
        	    	    standstill();
				    }}
	                break;
              case SILVER:
            	  if (((org._nTotalKills > 0) && ((org._isaconsumer) || (org._isafungus))) || (org._isenhanced)) {
            	    if (_segsilverReaction[seg] == 1) {
            	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
            	    	standstill();
				    }}
	    	        break;
            	  } else if (org._isaplant) {
                  	if (_segplantReaction[seg] == 1) {
                    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
                    	standstill();
        			}}
          	        break;
            	  } else if ((org._isaconsumer) || (org._isafungus)) {
                    if (_segconsumerReaction[seg] == 1) {
                    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
                    	standstill();
    				}}
      	    	    break;
                  } else {
                    if (_segdefaultReaction[seg] == 1) {
                    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
                    	standstill();
        		    }}
                  }
                  break;
              case SPIKEPOINT:
        	        if (_segspikeReaction[seg] == 1) {
        	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
        	    	    standstill();
				    }}
	                break;
              case GRAY:
          	        if (_seggrayReaction[seg] == 1) {
          	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
          	    	    standstill();
				    }}
	     	        break;
              }
              break;
			} else {
			  switch (getTypeColor(org._segColor[oseg])) {
              case EYE:
 				    break;
              case WHITE:
              case PLAGUE:
            	  if (org._isaplant) {
            		if (org._plagueversion > 0) {
            		if (_segplagueReaction[seg] == 1) {
                    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
                    	standstill();
        			}}
          	    	break;
            		} else {
            	    if (_segwhiteReaction[seg] == 1) {
            	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
            	    	standstill();
  				    }}
  	    	        break;
            		}
            	  } else if ((org._plagueversion > 0) || (org._isaconsumer) || (org._isafungus) || (org._isauburn)) {
              		if (_segscourgeReaction[seg] == 1) {
              		if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
              			standstill();
  					}}
  	    	        break;
            	  } else {
            		if (_segvirusReaction[seg] == 1) {
            		if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
            			standstill();
  					}}
            	  }
            	  break;
              case BARK:
              case OLDBARK:
          	        if (_segbarkReaction[seg] == 1) {
          	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
          	        	standstill();
				    }}
                    break;
              case GREEN:
              case FOREST:
              case SPRING:
              case SUMMER:
              case WINTER:
              case LIME:
              case LEAF:
              case C4:
              case JADE:
              case DARKJADE:
              case DARKGREEN:
              case GRASS:
              case PURPLE:
        	        if (_seggreenReaction[seg] == 1) {
        	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
        	        	standstill();
				    }}
    	            break;
              case BLUE:
            	    if (_segblueReaction[seg] == 1) {
            	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
            	    	standstill();
				    }}
	    	        break;
              case OLIVE:
          	        if (_segoliveReaction[seg] == 1) {
          	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
          	    	    standstill();
				    }}
                  break;
              case SKY:
              case DEEPSKY:
            	    if (_segskyReaction[seg] == 1) {
            	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
            	    	standstill();
				    }}
                    break;
              case FRUIT:
            	  if (org._sporeversion == 2) {
            		if (_segwhiteReaction[seg] == 1) {
	                if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	standstill();
	  				}}
	    	    	break;
            	  } else {
            		if (_segvirusReaction[seg] == 1) {
	                if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
	                	standstill();
	  				}}
	              }
	              break;
              case CORAL:
        	        if (_segcoralReaction[seg] == 1) {
        	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
        	        	standstill();
				    }}
                  break;
              case FALLOW:
          	        if (_segfallowReaction[seg] == 1) {
          	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
          	    	    standstill();
				    }}
	                break;
              case VIOLET:
          	        if (_segvioletReaction[seg] == 1) {
          	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
          	    	    standstill();
				    }}
	                break;
              case SPIKE:
            	  if (_mphoto[seg] == -13) {
            		if (_segspikeReaction[seg] == 1) {
                  	if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
                  		standstill();
  					}}
    	            break;
                  } else if (org._isaplant) {
              	    if (_seggreenReaction[seg] == 1) {
              	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
              	    	standstill();
					}}
	    	        break;
                  } else if (org._isenhanced) {
              		if (_segconsumerReaction[seg] == 1) {
                	if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
                		standstill();
  					}}
    	    	    break;
                  } else {
                  	if (_segblueReaction[seg] == 1) {
                    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
                    	standstill();
      				}}
                  }
            	  break;
              case LILAC:
              case DARKLILAC:
            	    if (_seglilacReaction[seg] == 1) {
            	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
            	    	standstill();
				    }}
                    break;
              case MINT:
              case LAVENDER:
            	    if (_segmintReaction[seg] == 1) {
            	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
            	    	standstill();
				    }}
	                break;
              case MAGENTA:
              case ROSE:
            	    if (_segmagentaReaction[seg] == 1) {
            	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
            	    	standstill();
				    }}
	     	        break;
              case LIGHTBROWN:
              case DARKFIRE:
        	        if (_seglightbrownReaction[seg] == 1) {
        	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
        	        	standstill();
				    }}
                    break;
              case GREENBROWN:
        	        if (_seggreenbrownReaction[seg] == 1) {
        	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
        	        	standstill();
				    }}
                    break;
              case DARKOLIVE:
              case LIGHT_BLUE:
            	    if (_seglightblueReaction[seg] == 1) {
            	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
            	    	standstill();
				    }}
	                break;
              case ICE:
              case DEADBARK:
        	        if (_segiceReaction[seg] == 1) {
        	        if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
        	        	standstill();
				    }}
                    break;
              case BROKEN:
            	    if (_segbrokenReaction[seg] == 1) {
            	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
            	    	standstill();
				    }}
	                break;
              case BROWN:
            	    if (_segbrownReaction[seg] == 1) {
            	    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
            	    	standstill();
				    }}
	                break;
              default:
            	  if ((org._isaconsumer) || (org._isafungus)) {
              		if (_segconsumerReaction[seg] == 1) {
                    if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
                    	standstill();
    				}}
      	            break;
                  } else if (org._isaplant) {
                	if (_segplantReaction[seg] == 1) {
                	if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
                		standstill();
					}}
  	    	        break;
                  } else {
                	if (_segdefaultReaction[seg] == 1) {
                  	if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
                  		standstill();
    				}}
                  }
              	  break;
              }
              break;
			}
        }
	}
	/**
	 * Applies the hostile effects produced by two touching segments.
	 * Version for segments, that can drain energy
	 *
	 * @param org  The organism which is touching.
	 * @param seg  Index of this organism's segment.
	 * @param oseg  Index of the other organism's segment.
	 */
	private final void touchEffects1(Organism org, int seg, int oseg) {
		switch (getTypeColor(_segColor[seg])) {
		case ORANGE:
		    // Orange segment: try to get energy from the other organism
			double takenEnergyOrange = 0;
			if (org._hasdodged == false) {
			    org._hasdodged =true;
		    }
			switch (getTypeColor(org._segColor[oseg])) {
			case WHITE:
				if ((org._isaplant) || (org._isaconsumer) || (org._isafungus) || (org._plagueversion > 0) || (org._isauburn)) {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
				    }
			    }
				break;
			case ORANGE:
				if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
					if (_isenhanced) {
						if (_symmetry == org._symmetry) {
							takenEnergyOrange = Utils.between((1.2 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						} else {
							takenEnergyOrange = Utils.between((0.99 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						}
					} else {
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					}
					// The other organism will be shown in orange
					org.setColor(Color.ORANGE);
					// This organism will be shown in orange
					setColor(Color.ORANGE);
				}
				break;
			case FIRE:
				if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
					if (_isenhanced) {
						if (_symmetry == org._symmetry) {
							takenEnergyOrange = Utils.between((1.02 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						} else {
							takenEnergyOrange = Utils.between((0.99 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						}
					} else {
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					}
					// The other organism will be shown in fire
					org.setColor(Utils.ColorFIRE);
					// This organism will be shown in orange
					setColor(Color.ORANGE);
				}
				break;
			case SILVER:
            	if (((org._nTotalKills > 0) && ((org._isaconsumer) || (org._isafungus))) || (org._isenhanced)) {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in gold
						org.setColor(Utils.ColorGOLD);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
					}
				} else {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
					}
				}
				break;
			case VIOLET:
				if ((org._isaplant) || (org._isaconsumer) || (org._isafungus) || (org._plagueversion > 0) || (org._isauburn)) {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
				    }
			    } else {
			    	if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((0.5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
				    }
			    }
				break;
			case GRASS:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Color.ORANGE);
				} else {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((0.5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
				    }
				}
				break;
			case LEAF:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Color.ORANGE);
				} else {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						if (org._modifiesleaf) {
							if (org._framesColor > 0) {
								// Get energy depending on segment length
								takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in yellow
								org.setColor(Color.YELLOW);
							} else {
								// Get energy depending on segment length
								takenEnergyOrange = Utils.between((0.02 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark olive
								org.setColorforLeaf(Utils.ColorDARKOLIVE);
							}
						} else {
							// Get energy depending on segment length
							takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
						}
						// This organism will be shown in orange
						setColor(Color.ORANGE);
				    }
				}
				break;
			case GREEN:
			case FOREST:
			case SPRING:
			case SUMMER:
            case WINTER:
			case LIME:
			case C4:
			case JADE:
			case DARKJADE:
			case DARKGREEN:
			case PURPLE:
			case TEAL:
			case CYAN:
			case AUBURN:
			case INDIGO:
			case BLOND:
			case FLOWER:
			case DARKGRAY:
			case GOLD:
			case SPORE:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Color.ORANGE);
				} else {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
				    	// Get energy depending on segment length
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
				    }
				}
				break;
			case YELLOW:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Color.ORANGE);
				} else {
					if ((org._isaconsumer) || (org._isafungus) || (org._isaplant) || (org._transfersenergy)) {
						if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in orange
							setColor(Color.ORANGE);
					    }
					} else {
						if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyOrange = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in orange
							setColor(Color.ORANGE);
					    }
					}
				}
				break;
			case CORAL:
				if (org._isaconsumer) {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
					}
				} else {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((0.5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
				    }
				}
				break;
			case PLAGUE:
				if (org._isinfectious) {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
					}
				} else {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
				    }
				}
				break;
			case SPIKE:
				if ((org._isaplant) || (org._isenhanced)) {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
					}
				}
				break;
			case FALLOW:
				if ((!org._isaplant) && (!org._isinfectious)) {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((0.01 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in broken
						org.setColor(Utils.ColorBROKEN);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
				    }
				} else {
					if ((org._isinfectious) && (org._fallowversion == 4)) {
						if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in orange
							setColor(Color.ORANGE);
						}
					} else {
						if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyOrange = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in orange
							setColor(Color.ORANGE);
					    }
					}
				}
				break;
			case BLUE:
				// If the other segment is blue, it acts as a shield
				if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
					if (org._isenhanced) {
					    useEnergy(Utils.between((0.5 * Math.sqrt(org._m[oseg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, _energy));
						setColor(Utils.ColorDARKLILAC);
						if (_energy < Utils.tol) {
							die(org);
						}
					} else {
					    setColor(Color.ORANGE);
					}
					org.setColor(Color.BLUE);
				} else {
					// Doesn't have energy to use the shield
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
					}
				}
				break;
			case LIGHT_BLUE:
				if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
					setColor(Color.ORANGE);
					org.setColor(Utils.ColorLIGHT_BLUE);
				} else {
					// Doesn't have energy to use the shield
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
					}
				}
				break;
			case SKY:
				if ((org._skyversion == 2) || (org._isaplant) || (!_isfrozen)) {
					if (org.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
						org._segColor[oseg] = Utils.ColorDEEPSKY;
						org._mphoto[oseg] = -20;
						org._useextraeffects = true;
						setColor(Color.ORANGE);
						org.setColor(Utils.ColorDEEPSKY);
					} else {
						// Doesn't have energy to use the shield
						if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in orange
							setColor(Color.ORANGE);
						}
					}
				}
				break;
			case DEEPSKY:
				if ((org._skyversion == 2) || (org._isaplant) || (!_isfrozen)) {
					setColor(Color.ORANGE);
					org.setColor(Utils.ColorDEEPSKY);
				}
				break;
			case LAVENDER:
				if (_altruist) {
	            break;
				} else {
					if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Color.ORANGE);
					} else {
						if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
							if ((org._isaplant) && (!org._isaconsumer) && (!org._isafungus) && (!org._isakiller)) {
								// Get energy depending on segment length
								takenEnergyOrange = Utils.between((0.5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in green brown
								org.setColor(Utils.ColorGREENBROWN);
							} else {
								// Get energy depending on segment length
								takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in yellow
								org.setColor(Color.YELLOW);
							}
							// This organism will be shown in orange
							setColor(Color.ORANGE);
					    }
					}
				}
				break;
			case MINT:
			case MAGENTA:
			case ROSE:
				if (_altruist) {
                break;
				} else {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Color.ORANGE);
					} else {
						if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
							if ((org._isaplant) && (!org._isaconsumer) && (!org._isafungus) && (!org._isakiller)) {
								// Get energy depending on segment length
								takenEnergyOrange = Utils.between((0.5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in green brown
								org.setColor(Utils.ColorGREENBROWN);
							} else {
								// Get energy depending on segment length
								takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in yellow
								org.setColor(Color.YELLOW);
							}
							// This organism will be shown in orange
							setColor(Color.ORANGE);
					    }
					}
				}
				break;
			case FRUIT:
				if (org._sporeversion == 2) {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						if (org._isenhanced) {
							// Get energy depending on segment length
							takenEnergyOrange = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
						} else {
							// Get energy depending on segment length
							takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
						}
						if (_infectedGeneticCode != org._geneticCode) {
							// Infectious fruit
							infectedByFruit(org);
							setColor(Utils.ColorBLOND);
						} else {
							// This organism will be shown in orange
							setColor(Color.ORANGE);
						}
				    }
			    }
				break;
			case ICE:
			case DEADBARK:
				if (_skyversion > 0) {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
				    }
			    }
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				} else {
					if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in orange
						setColor(Color.ORANGE);
					}
				}
				break;
			case BARK:
		    	org._segColor[oseg] = Utils.ColorOLDBARK;
		    	if (org.active) {
					org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
					org._mphoto[oseg] = -0.5;
				}
		    	break;
			case OLDBARK:
			case RED:
			case MAROON:
			case CREAM:
			case OCHRE:
			case OLIVE:
			case DARKOLIVE:
			case SPIKEPOINT:
			case BROWN:
				break;
			default:
				if (useEnergy(Utils.ORANGE_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
					takenEnergyOrange = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in yellow
					org.setColor(Color.YELLOW);
					// This organism will be shown in orange
					setColor(Color.ORANGE);
				}
			}
			// energy interchange
			if (takenEnergyOrange > 0) {
				org._energy -= takenEnergyOrange;
				_energy += takenEnergyOrange;
				takenEnergyOrange = takenEnergyOrange * Utils.ORGANIC_SUBS_PRODUCED;
				useEnergy(takenEnergyOrange);
			}
			break;
		case FIRE:
			// Fire segment: omnivore between red and orange
			double takenEnergyFire = 0;
			if (org._hasdodged == false) {
			    org._hasdodged =true;
		    }
			switch (getTypeColor(org._segColor[oseg])) {
			case WHITE:
				if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in fire
						setColor(Utils.ColorFIRE);
					}
				} else {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				}
				break;
			case FIRE:
            	if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
            		takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in fire
					org.setColor(Utils.ColorFIRE);
					// This organism will be shown in fire
					setColor(Utils.ColorFIRE);
				}
            	break;
            case ORANGE:
            	if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
            		takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in orange
					org.setColor(Color.ORANGE);
					// This organism will be shown in fire
					setColor(Utils.ColorFIRE);
				}
				break;
			case RED:
            	if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
            		takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in red
					org.setColor(Color.RED);
					// This organism will be shown in fire
					setColor(Utils.ColorFIRE);
				}
				break;
            case SILVER:
            	if (((org._nTotalKills > 0) && ((org._isaconsumer) || (org._isafungus))) || (org._isenhanced)) {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in gold
						org.setColor(Utils.ColorGOLD);
						// This organism will be shown in fire
						setColor(Utils.ColorFIRE);
					}
				} else
					if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in fire
						setColor(Utils.ColorFIRE);
					}
				} else
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
				}
				break;
            case PINK:
            	if ((org._isgray) && (org._modifiespink)) {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in fire
						setColor(Utils.ColorFIRE);
					}
				} else {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				}
            	break;
			case MAROON:
				if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
					takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in yellow
					org.setColor(Color.YELLOW);
					// This organism will be shown in dark fire
					setColor(Utils.ColorDARKFIRE);
				}
				break;
			case CREAM:
				if ((org._isenhanced) && (org._creamversion == 2)) {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				} else {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				}
				break;
			case GRASS:
				if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((0.5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in fire
						setColor(Utils.ColorFIRE);
					}
				} else {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorDARKFIRE);
					} else {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((0.125 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
				}
				break;
			case LEAF:
				if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						if (org._modifiesleaf) {
							if (org._framesColor > 0) {
								// Get energy depending on segment length
								takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in yellow
								org.setColor(Color.YELLOW);
							} else {
								// Get energy depending on segment length
								takenEnergyFire = Utils.between((0.02 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark olive
								org.setColorforLeaf(Utils.ColorDARKOLIVE);
							}
						} else {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
						}
						// This organism will be shown in fire
						setColor(Utils.ColorFIRE);
					}
				} else {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorDARKFIRE);
					} else {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							if (org._modifiesleaf) {
								if (org._framesColor > 0) {
									// Get energy depending on segment length
									takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in yellow
									org.setColor(Color.YELLOW);
								} else {
									// Get energy depending on segment length
									takenEnergyFire = Utils.between((0.01 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in dark olive
									org.setColorforLeaf(Utils.ColorDARKOLIVE);
								}
							} else {
								// Get energy depending on segment length
								takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in yellow
								org.setColor(Color.YELLOW);
							}
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
				}
				break;
			case GREEN:
			case FOREST:
			case SPRING:
			case SUMMER:
            case WINTER:
			case LIME:
			case C4:
			case JADE:
			case DARKJADE:
			case DARKGREEN:
			case PURPLE:
			case TEAL:
			case CYAN:
			case AUBURN:
			case INDIGO:
			case BLOND:
			case FLOWER:
			case DARKGRAY:
			case GOLD:
			case SPORE:
				if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in fire
						setColor(Utils.ColorFIRE);
					}
				} else {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorDARKFIRE);
					} else {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
				}
				break;
			case YELLOW:
				if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in fire
						setColor(Utils.ColorFIRE);
					}
				} else {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorDARKFIRE);
					} else {
						if ((org._isafungus) || (org._isaplant) || (org._transfersenergy)) {
							if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in yellow
								org.setColor(Color.YELLOW);
								// This organism will be shown in dark fire
								setColor(Utils.ColorDARKFIRE);
							}
						} else {
							if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergyFire = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in green brown
								org.setColor(Utils.ColorGREENBROWN);
								// This organism will be shown in dark fire
								setColor(Utils.ColorDARKFIRE);
							}
						}
					}
				}
				break;
			case SPIKE:
            	if ((org._isaplant) || (org._isenhanced)) {
            		if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
    					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
    						// Get energy depending on segment length
    						takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
    						// The other organism will be shown in yellow
    						org.setColor(Color.YELLOW);
    						// This organism will be shown in fire
    						setColor(Utils.ColorFIRE);
    					}
    				} else {
    					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
    						// Get energy depending on segment length
    						takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
    						// The other organism will be shown in yellow
    						org.setColor(Color.YELLOW);
    						// This organism will be shown in dark fire
    						setColor(Utils.ColorDARKFIRE);
    					}
    				}
            	}
            	break;
			case FALLOW:
				if ((!org._isaplant) && (!org._isinfectious)) {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((0.01 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in broken
						org.setColor(Utils.ColorBROKEN);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				} else {
					if ((org._isinfectious) && (org._fallowversion == 4)) {
						if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
							if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in yellow
								org.setColor(Color.YELLOW);
								// This organism will be shown in fire
								setColor(Utils.ColorFIRE);
							}
						} else {
							if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in yellow
								org.setColor(Color.YELLOW);
								// This organism will be shown in dark fire
								setColor(Utils.ColorDARKFIRE);
							}
						}
					} else {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
				}
				break;
			case PLAGUE:
				if (org._isinfectious) {
					if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in fire
							setColor(Utils.ColorFIRE);
						}
					} else {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
				} else {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				}
				break;
			case BLUE:
				// If the other segment is blue, it acts as a shield
				if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
					if (org._isenhanced) {
					    useEnergy(Utils.between((0.5 * Math.sqrt(org._m[oseg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, _energy));
						setColor(Utils.ColorDARKLILAC);
						if (_energy < Utils.tol) {
							die(org);
						}
					} else {
						setColor(Utils.ColorDARKFIRE);
					}
					org.setColor(Color.BLUE);
				} else {
					// Doesn't have energy to use the shield
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				}
				break;
			case LIGHT_BLUE:
				if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
					setColor(Utils.ColorDARKFIRE);
					org.setColor(Utils.ColorLIGHT_BLUE);
				} else {
					// Doesn't have energy to use the shield
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				}
			    break;
			case SKY:
				if ((!_isenhanced) || (org._skyversion == 2)) {
					if (org.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
						org._segColor[oseg] = Utils.ColorDEEPSKY;
						org._mphoto[oseg] = -20;
						org._useextraeffects = true;
						setColor(Utils.ColorDARKFIRE);
						org.setColor(Utils.ColorDEEPSKY);
					} else {
						// Doesn't have energy to use the shield
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
				}
				break;
			case DEEPSKY:
				if ((!_isenhanced) || (org._skyversion == 2)) {
					setColor(Utils.ColorDARKFIRE);
					org.setColor(Utils.ColorDEEPSKY);
				}
				break;
			case VIOLET:
				if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in fire
						setColor(Utils.ColorFIRE);
					}
				} else {
					if (org._isafungus) {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					} else {
						if (!_isenhanced) {
							if ((org._isaplant) || (org._plagueversion > 0) || (org._isauburn)) {
								if ((!org._isenhanced) || (org._isinfectious)) {
									if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
										// Get energy depending on segment length
										takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
										// The other organism will be shown in yellow
										org.setColor(Color.YELLOW);
										// This organism will be shown in dark fire
										setColor(Utils.ColorDARKFIRE);
									}
								}
							}
						}
					}
				}
				break;
			case FRUIT:
				if (org._sporeversion == 2) {
				    if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
				    	takenEnergyFire = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						if (_infectedGeneticCode != org._geneticCode) {
							// Infectious fruit
							infectedByFruit(org);
							setColor(Utils.ColorBLOND);
						} else {
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
				} else {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				}
				break;
			case ICE:
			case DEADBARK:
				if (_skyversion > 0) {
					if ((org._isaconsumer) || (org._isaplant)) {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in fire
							setColor(Utils.ColorFIRE);
						}
					} else {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
				}
				break;
			case BARK:
		    	org._segColor[oseg] = Utils.ColorOLDBARK;
		    	if (org.active) {
					org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
					org._mphoto[oseg] = -0.5;
				}
		    	break;
			case OLDBARK:
			case OCHRE:
			case OLIVE:
			case DARKOLIVE:
			case SPIKEPOINT:
				break;
			case LIGHTBROWN:
				if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
					takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in yellow
					org.setColor(Color.YELLOW);
					// This organism will be shown in fire
					setColor(Utils.ColorFIRE);
				}
				break;
			case BROWN:
				if (_isenhanced) {
					if (!org.alive) {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in fire
							setColor(Utils.ColorFIRE);
						}
					} else {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
							if (!org._isenhanced) {
								org._isinjured =true;
							}
						}
					}
				}
				break;
			case GREENBROWN:
				if (_isenhanced) {
					if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in fire
							setColor(Utils.ColorFIRE);
						}
					} else {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
			    }
				break;
			case LAVENDER:
				if (_altruist) {
	            break;
				} else {
					if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in fire
							setColor(Utils.ColorFIRE);
						}
					} else {
						if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorDARKFIRE);
						} else {
							if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
								if ((org._isaplant) && (!org._isafungus) && (!org._isakiller)) {
									// Get energy depending on segment length
									takenEnergyFire = Utils.between((0.125 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in green brown
									org.setColor(Utils.ColorGREENBROWN);
								} else {
									// Get energy depending on segment length
									takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in yellow
									org.setColor(Color.YELLOW);
								}
								// This organism will be shown in dark fire
								setColor(Utils.ColorDARKFIRE);
							}
						}
					}
				}
				break;
			case MINT:
			case MAGENTA:
			case ROSE:
				if (_altruist) {
                break;
				} else {
					if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in fire
							setColor(Utils.ColorFIRE);
						}
					} else {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorDARKFIRE);
						} else {
							if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
								if ((org._isaplant) && (!org._isafungus) && (!org._isakiller)) {
									// Get energy depending on segment length
									takenEnergyFire = Utils.between((0.125 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in green brown
									org.setColor(Utils.ColorGREENBROWN);
								} else {
									// Get energy depending on segment length
									takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in yellow
									org.setColor(Color.YELLOW);
								}
								// This organism will be shown in dark fire
								setColor(Utils.ColorDARKFIRE);
							}
						}
					}
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				} else {
					if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in fire
							setColor(Utils.ColorFIRE);
						}
					} else {
						if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
				}
				break;
			default:
				if ((org._isaconsumer) || ((_skyversion > 0) && (org._isfrozen) && (org._isaplant))) {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in fire
						setColor(Utils.ColorFIRE);
					}
				} else {
					if (useEnergy(Utils.FIRE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyFire = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				}
		    }
			// energy interchange
			if (takenEnergyFire > 0) {
				org._energy -= takenEnergyFire;
				_energy += takenEnergyFire;
				takenEnergyFire = takenEnergyFire * Utils.ORGANIC_SUBS_PRODUCED;
				useEnergy(takenEnergyFire);
			}
			break;
		case RED:
			// Red segment: try to get energy from other consumers
			double takenEnergyRed = 0;
			switch (getTypeColor(org._segColor[oseg])) {
			case RED:
				if (useEnergy(Utils.RED_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
					takenEnergyRed = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in red
					org.setColor(Color.RED);
					// This organism will be shown in red
					setColor(Color.RED);
				}
				break;
			case FIRE:
				if (useEnergy(Utils.RED_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
					takenEnergyRed = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in fire
					org.setColor(Utils.ColorFIRE);
					// This organism will be shown in red
					setColor(Color.RED);
				}
				break;
			case ORANGE:
			case MAROON:
			case PINK:
				if (useEnergy(Utils.RED_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
					takenEnergyRed = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in yellow
					org.setColor(Color.YELLOW);
					// This organism will be shown in red
					setColor(Color.RED);
				}
				break;
			case SILVER:
				if (((org._nTotalKills > 0) && ((org._isaconsumer) || (org._isafungus))) || (org._isenhanced)) {
					if (useEnergy(Utils.RED_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyRed = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in gold
						org.setColor(Utils.ColorGOLD);
						// This organism will be shown in red
						setColor(Color.RED);
					}
				}
				break;
			case SPIKE:
				if ((org._isenhanced) && (useEnergy(Utils.RED_ENERGY_CONSUMPTION))) {
					// Get energy depending on segment length
					takenEnergyRed = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in yellow
					org.setColor(Color.YELLOW);
					// This organism will be shown in red
					setColor(Color.RED);
				}
				break;
			case SPIKEPOINT:
				if ((_isenhanced) && (org._isenhanced) && (useEnergy(Utils.RED_ENERGY_CONSUMPTION))) {
					// Get energy depending on segment length
					takenEnergyRed = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in yellow
					org.setColor(Color.YELLOW);
					// This organism will be shown in red
					setColor(Color.RED);
				}
				break;
			case GRAY:
				if ((_isenhanced) && ((org._isaconsumer) || (org._isafungus))) {
					if (org._isenhanced) {
						if (useEnergy(Utils.RED_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyRed = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in red
							setColor(Color.RED);
						}
					} else {
						if (useEnergy(Utils.RED_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyRed = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
				}
				break;
			case CREAM:
				if (_isenhanced) {
					if ((org._isenhanced) && (org._creamversion == 2)) {
						if (useEnergy(Utils.RED_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyRed = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					} else {
						if (useEnergy(Utils.RED_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyRed = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
				}
				break;
			case LILAC:
			case DARKLILAC:
				if ((_isenhanced) && ((org._isaconsumer) || (org._isafungus)) && (useEnergy(Utils.RED_ENERGY_CONSUMPTION))) {
					// Get energy depending on segment length
					takenEnergyRed = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in yellow
					org.setColor(Color.YELLOW);
					// This organism will be shown in dark fire
					setColor(Utils.ColorDARKFIRE);
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				}
				break;
			case BARK:
		    	org._segColor[oseg] = Utils.ColorOLDBARK;
		    	if (org.active) {
					org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
					org._mphoto[oseg] = -0.5;
				}
		    	break;
			default:
				break;
			}
			// energy interchange
			if (takenEnergyRed > 0) {
				org._energy -= takenEnergyRed;
				_energy += takenEnergyRed;
				takenEnergyRed = takenEnergyRed * Utils.ORGANIC_SUBS_PRODUCED;
				useEnergy(takenEnergyRed);
			}
			break;
		case PINK:
			// Pink segment: try to get energy from corpses, weak organisms, viruses and parasites
			double takenEnergyPink = 0;
			if (org._hasdodged == false) {
			    org._hasdodged =true;
		    }
			switch (getTypeColor(org._segColor[oseg])) {
			case PINK:
				if (_modifiespink) {
					if (org._modifiespink) {
						if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in pink
						    org.setColor(Color.PINK);
							// This organism will be shown in pink
							setColor(Color.PINK);
						}
					} else {
						if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark fire
							org.setColor(Utils.ColorDARKFIRE);
							// This organism will be shown in pink
							setColor(Color.PINK);
						}
					}
				} else {
					if (org._modifiespink) {
						if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyPink = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in pink
						    org.setColor(Color.PINK);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					} else {
						if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyPink = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark fire
							org.setColor(Utils.ColorDARKFIRE);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
				}
				break;
			case WHITE:
			case LIGHTBROWN:
			case ICE:
				if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
					takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in yellow
					org.setColor(Color.YELLOW);
					// This organism will be shown in pink
					setColor(Color.PINK);
				}
				break;
			case FRUIT:
				if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
					takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in yellow
					org.setColor(Color.YELLOW);
					if ((org._sporeversion == 2) && (_infectedGeneticCode != org._geneticCode)) {
						// Infectious fruit
						if ((_modifiespink) || (_isblond)) {
							infectedByFruit(org);
							setColor(Utils.ColorBLOND);
						} else if (_indigo == 0) {
							infectedByFruitWeak(org);
							setColor(Utils.ColorBLOND);
						} else {
							// This organism will be shown in pink
							setColor(Color.PINK);
						}
					} else {
						// This organism will be shown in pink
						setColor(Color.PINK);
					}
				}
				break;
			case CREAM:
				if ((!_modifiespink) && (org._creamversion == 2) && (org.active)) {
					if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyPink = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				} else {
					if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in pink
						setColor(Color.PINK);
					}
				}
				break;
			case GREENBROWN:
			case LIGHT_BLUE:
			case DARKOLIVE:
			case DEEPSKY:
				if ((_modifiespink) || ((org._isblond) && (org._isaplant)) || (org._isfrozen) || (!org.active)) {
					if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in pink
						setColor(Color.PINK);
					}
				} else {
					if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyPink = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				}
				break;
			case DEADBARK:
				if ((_modifiespink) || (org._isblond) || ((org._isfrozen) && (_skyversion > 0)) || (!org.active)) {
					if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in pink
						setColor(Color.PINK);
					}
				} else {
					if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyPink = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				}
				break;
			case BROKEN:
				if ((_modifiespink) || (org._mphoto[oseg] != -20.1) || (org._isblond) || (org._isfrozen)) {
					// also feed on everything except destroyed Violet, Gray, Lilac and Spike
					if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in pink
						setColor(Color.PINK);
					}
				} else {
					if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyPink = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in dark fire
						setColor(Utils.ColorDARKFIRE);
					}
				}
				break;
			case DARKFIRE:
				if ((_modifiespink) || ((!org._isaplant) && (org._mphoto[oseg] != -20.4)) || (org._mphoto[oseg] == -20.5)) {
					// also feed on all non-plants except vulnerable Fire, but feed on all vulnerable Cream
					if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in pink
						setColor(Color.PINK);
					}
				} else {
					if (org._mphoto[oseg] == -20.4) {
						// feed on vulnerable Fire
						if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyPink = Utils.between((0.125 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					} else {
						if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyPink = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in dark fire
							setColor(Utils.ColorDARKFIRE);
						}
					}
				}
				break;
			case CORAL:
				if ((!_modifiespink) || (org._isaconsumer)) {
					if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
					    // Get energy depending on segment length
						takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					    // The other organism will be shown in yellow
					    org.setColor(Color.YELLOW);
					    // This organism will be shown in pink
					    setColor(Color.PINK);
				    }
				} else {
					if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyPink = Utils.between((0.5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in pink
					    setColor(Color.PINK);
				    }
				}
				break;
			case BROWN:
				if ((!_isgray) || (_modifiespink)) {
					if (org.alive) {
						if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in pink
							setColor(Color.PINK);
							if (!org._isenhanced) {
								org._isinjured =true;
							}
						}
				    } else {
				    	if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
				    		if ((_fallowversion > 0) && ((org._age >> 8) >= org._max_age * 2)) {
								// Get the total energy of the still born child
								takenEnergyPink = org._energy;
							} else {
								// Get energy depending on segment length
								takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							}
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in pink
						    setColor(Color.PINK);
					    }
				    }
				}
				break;
			case PLAGUE:
				if (org._isinfectious) {
					if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in pink
						setColor(Color.PINK);
					}
				} else {
					if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyPink = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in pink
					    setColor(Color.PINK);
				    }
				}
				break;
			case OLDBARK:
				if (_isenhanced) {
					if ((org._isenhanced) && (org.active)) {
						if ((_modifiespink) || (org._isblond)) {
							if (org._framesColor > 0) {
								if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
									// Get energy depending on segment length
									takenEnergyPink = Utils.between((0.01 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in broken
									org.setColor(Utils.ColorBROKEN);
									// This organism will be shown in pink
									setColor(Color.PINK);
								}
							} else {
								// The other organism will be shown in old bark
								org.setColortwoFrames(Utils.ColorOLDBARK);
							}
						} else {
							if (org._framesColor > 0) {
								if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
									// Get energy depending on segment length
									takenEnergyPink = Utils.between((0.0025 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in broken
									org.setColor(Utils.ColorBROKEN);
									// This organism will be shown in dark fire
									setColor(Utils.ColorDARKFIRE);
								}
							} else {
								// The other organism will be shown in old bark
								org.setColortwoFrames(Utils.ColorOLDBARK);
							}
						}
					} else {
						if ((_modifiespink) || (org._isblond)) {
							if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergyPink = Utils.between((Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in yellow
								org.setColor(Color.YELLOW);
								// This organism will be shown in pink
								setColor(Color.PINK);
							}
						} else {
							if (useEnergy(Utils.PINK_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergyPink = Utils.between((0.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in yellow
								org.setColor(Color.YELLOW);
								// This organism will be shown in dark fire
								setColor(Utils.ColorDARKFIRE);
							}
						}
					}
				}
				break;
			case BARK:
		    	org._segColor[oseg] = Utils.ColorOLDBARK;
		    	if (org.active) {
					org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
					org._mphoto[oseg] = -0.5;
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				}
				break;
			default:
				break;
			}
			// energy interchange
			if (takenEnergyPink > 0) {
				org._energy -= takenEnergyPink;
				_energy += takenEnergyPink;
				takenEnergyPink = takenEnergyPink * Utils.ORGANIC_SUBS_PRODUCED;
				useDecayEnergy(takenEnergyPink);
			}
			break;
		case MAROON:
			// Maroon segment: try to get energy from plants and feed on ochre, sky, darkolive and cracked light blue segments
			double takenEnergyMaroon = 0;
			if (org._hasdodged == false) {
			    org._hasdodged =true;
		    }
			switch (getTypeColor(org._segColor[oseg])) {
			case WHITE:
			case FALLOW:
			case OCHRE:
			case DEEPSKY:
			case DARKOLIVE:
			case SPIKE:
			case ICE:
				if (org._isaplant) {
					// Get energy depending on segment length
					takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
				    // The other organism will be shown in yellow
				    org.setColor(Color.YELLOW);
				    // This organism will be shown in maroon
				    setColor(Utils.ColorMAROON);
				}
				break;
			case GRASS:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Utils.ColorMAROON);
				} else {
					// Get energy depending on segment length
					takenEnergyMaroon = Utils.between((0.5 * _m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
					// The other organism will be shown in green brown
					org.setColor(Utils.ColorGREENBROWN);
					// This organism will be shown in maroon
					setColor(Utils.ColorMAROON);
				}
				break;
			case LEAF:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Utils.ColorMAROON);
				} else {
					if (org._modifiesleaf) {
						if (org._framesColor != 0) {
							// Get energy depending on segment length
							takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						} else {
							// Get energy depending on segment length
							takenEnergyMaroon = Utils.between((0.08 * _m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
							// The other organism will be shown in dark olive
							org.setColorforLeaf(Utils.ColorDARKOLIVE);
						}
					} else {
						// Get energy depending on segment length
						takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
					    // The other organism will be shown in yellow
					    org.setColor(Color.YELLOW);
					}
					// This organism will be shown in maroon
					setColor(Utils.ColorMAROON);
				}
				break;
			case GREEN:
			case FOREST:
			case SPRING:
			case SUMMER:
            case WINTER:
			case LIME:
			case C4:
			case JADE:
			case DARKJADE:
			case DARKGREEN:
			case PURPLE:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Utils.ColorMAROON);
				} else {
					// Get energy depending on segment length
					takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
				    // The other organism will be shown in yellow
				    org.setColor(Color.YELLOW);
				    // This organism will be shown in maroon
				    setColor(Utils.ColorMAROON);
				}
				break;
			case BLUE:
				if (org._isaplant) {
				    if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
				    	if ((org._isenhanced) && (!_isenhanced)) {
						    useEnergy(Utils.between((0.25 * Math.sqrt(org._m[oseg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, _energy));
							setColor(Utils.ColorDARKLILAC);
							if (_energy < Utils.tol) {
								die(org);
							}
						} else {
							setColor(Utils.ColorMAROON);
						}
					    org.setColor(Color.BLUE);
				    } else {
						// Doesn't have energy to use the shield
				    	// Get energy depending on segment length
						takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
					    // The other organism will be shown in yellow
					    org.setColor(Color.YELLOW);
					    // This organism will be shown in maroon
					    setColor(Utils.ColorMAROON);
					}
				}
				break;
			case PLAGUE:
				if (org._isaplant) {
					if (org._isinfectious) {
						// Get energy depending on segment length
						takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
					    // The other organism will be shown in yellow
					    org.setColor(Color.YELLOW);
					    // This organism will be shown in maroon
					    setColor(Utils.ColorMAROON);
					} else {
						// Get energy depending on segment length
						takenEnergyMaroon = Utils.between((0.1 * _m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in maroon
						setColor(Utils.ColorMAROON);
					}
				}
				break;
			case FRUIT:
				if (org._sporeversion == 2) {
					// Get energy depending on segment length
					takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
					// The other organism will be shown in yellow
					org.setColor(Color.YELLOW);
					if (_infectedGeneticCode != org._geneticCode) {
						// Infectious fruit
						infectedByFruit(org);
						setColor(Utils.ColorBLOND);
					} else {
						// This organism will be shown in maroon
					    setColor(Utils.ColorMAROON);
					}

				}
				break;
			case BROKEN:
			case LIGHT_BLUE:
			case DEADBARK:
				if ((org._isaplant) || (!org.active)) {
					// Get energy depending on segment length
					takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
				    // The other organism will be shown in yellow
				    org.setColor(Color.YELLOW);
				    // This organism will be shown in maroon
				    setColor(Utils.ColorMAROON);
				}
				break;
			case OLDBARK:
				if (org.active) {
					if (org._isenhanced) {
						if (org._framesColor > 0) {
							// Get energy depending on segment length
							takenEnergyMaroon = Utils.between((0.01 * _m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
							// The other organism will be shown in broken
							org.setColor(Utils.ColorBROKEN);
							// This organism will be shown in maroon
						    setColor(Utils.ColorMAROON);
						} else {
							// The other organism will be shown in old bark
							org.setColortwoFrames(Utils.ColorOLDBARK);
						}
					} else {
						// Get energy depending on segment length
						takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
					    // The other organism will be shown in yellow
					    org.setColor(Color.YELLOW);
					    // This organism will be shown in maroon
					    setColor(Utils.ColorMAROON);
					}
				} else {
					if (org._isenhanced) {
						// Get energy depending on segment length
						takenEnergyMaroon = Utils.between((0.5 * _m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in maroon
						setColor(Utils.ColorMAROON);
					} else {
						// Get energy depending on segment length
						takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
					    // The other organism will be shown in yellow
					    org.setColor(Color.YELLOW);
					    // This organism will be shown in maroon
					    setColor(Utils.ColorMAROON);
					}
				}
				break;
			case BARK:
		    	org._segColor[oseg] = Utils.ColorOLDBARK;
		    	if (org.active) {
					org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
					org._mphoto[oseg] = -0.5;
				} else {
					if (!org._isenhanced) {
						// Get energy depending on segment length
						takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
					    // The other organism will be shown in yellow
					    org.setColor(Color.YELLOW);
					    // This organism will be shown in maroon
					    setColor(Utils.ColorMAROON);
					}
				}
		    	break;
			case CYAN:
			case TEAL:
			case YELLOW:
			case AUBURN:
			case INDIGO:
			case BLOND:
			case FLOWER:
			case DARKGRAY:
			case GOLD:
			case SPORE:
				if ((_isenhanced) && (org._isaplant)) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorMAROON);
					} else {
						// Get energy depending on segment length
						takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in auburn
						setColor(Utils.ColorAUBURN);
					}
				}
				break;
			case SILVER:
				if (((org._nTotalKills > 0) && ((org._isaconsumer) || (org._isafungus))) || (org._isenhanced)) {
	                break;
				} else {
					if ((_isenhanced) && (org._isaplant)) {
						// Get energy depending on segment length
						takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in auburn
						setColor(Utils.ColorAUBURN);
					}
				}
				break;
			case SPIKEPOINT:
				if ((_isenhanced) && (!org._isenhanced) && (org._isaplant)) {
					// Get energy depending on segment length
					takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
					// The other organism will be shown in yellow
					org.setColor(Color.YELLOW);
					// This organism will be shown in auburn
					setColor(Utils.ColorAUBURN);
				}
				break;
			case LAVENDER:
				if (_altruist) {
			    break;
				} else {
					if ((_isenhanced) && (org._isaplant)) {
						if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorMAROON);
						} else {
							if ((!org._isaconsumer) && (!org._isafungus) && (!org._isakiller)) {
								// Get energy depending on segment length
								takenEnergyMaroon = Utils.between((0.5 * _m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
								// The other organism will be shown in green brown
								org.setColor(Utils.ColorGREENBROWN);
							} else {
								// Get energy depending on segment length
								takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
								// The other organism will be shown in yellow
								org.setColor(Color.YELLOW);
							}
							// This organism will be shown in auburn
							setColor(Utils.ColorAUBURN);
						}
					}
				}
				break;
			case MINT:
			case MAGENTA:
			case ROSE:
				if (_altruist) {
				break;
				} else {
					if ((_isenhanced) && (org._isaplant)) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorMAROON);
						} else {
							if ((!org._isaconsumer) && (!org._isafungus) && (!org._isakiller)) {
								// Get energy depending on segment length
								takenEnergyMaroon = Utils.between((0.5 * _m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
								// The other organism will be shown in green brown
								org.setColor(Utils.ColorGREENBROWN);
							} else {
								// Get energy depending on segment length
								takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
								// The other organism will be shown in yellow
								org.setColor(Color.YELLOW);
							}
							// This organism will be shown in auburn
							setColor(Utils.ColorAUBURN);
						}
					}
				}
				break;
			case SKY:
				if ((_isenhanced) && (org._isaplant)) {
					if (org.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
						org._segColor[oseg] = Utils.ColorDEEPSKY;
						org._mphoto[oseg] = -20;
						org._useextraeffects = true;
						setColor(Utils.ColorAUBURN);
						org.setColor(Utils.ColorDEEPSKY);
					} else {
						// Get energy depending on segment length
						takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in auburn
						setColor(Utils.ColorAUBURN);
					}
				}
				break;
			case GREENBROWN:
			case VISION:
				if ((_isenhanced) && (org._isaplant)) {
					// Get energy depending on segment length
					takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
					// The other organism will be shown in yellow
					org.setColor(Color.YELLOW);
					// This organism will be shown in auburn
					setColor(Utils.ColorAUBURN);
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				} else {
					if ((_isenhanced) && (org._isaplant)) {
						// Get energy depending on segment length
						takenEnergyMaroon = Utils.between((_m[seg]) / Utils.MAROON_ENERGY_CONSUMPTION, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);
						// This organism will be shown in auburn
						setColor(Utils.ColorAUBURN);
					}
				}
				break;
			default:
				break;
			}
			// energy interchange
			if (takenEnergyMaroon > 0) {
				org._energy -= takenEnergyMaroon;
				_energy += takenEnergyMaroon;
				takenEnergyMaroon = takenEnergyMaroon * Utils.ORGANIC_SUBS_PRODUCED;
				useEnergy(takenEnergyMaroon);
			}
			break;
		case CREAM:
			// Cream segment: Parasitize on other organisms
			double takenEnergyCream = 0;
			if (org._hasdodged == false) {
				org._hasdodged =true;
			}
			switch (getTypeColor(org._segColor[oseg])) {
		    case WHITE:
				if ((org._isaplant) || (_creamversion == 2) || (org._isaconsumer) || (org._isafungus) || (org._plagueversion > 0) || (org._isauburn)) {
					if (org._lavender > 0) {
						org.lavendershield();
						setColor(Utils.ColorCREAM);
					}
					if (org._lavender <= 0) {
						if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in cream
							setColor(Utils.ColorCREAM);
						}
					}
				}
			    break;
		    case LEAF:
		    	if ((org._dodge) && ((org._canreact) || (!_hasgoodvision)) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Utils.ColorCREAM);
				} else {
					if (org._lavender > 0) {
						org.lavendershield();
						setColor(Utils.ColorCREAM);
					}
					if (org._lavender <= 0) {
						if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
							if (org._modifiesleaf) {
								if (((_isenhanced) || (!active)) && (_creamversion == 3)) {
									// Get energy depending on segment length
									takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in green brown
									org.setColor(Utils.ColorGREENBROWN);
									// This organism will be shown in dark gray
								    setColor(Color.DARK_GRAY);
								} else {
									if (org._framesColor > 0) {
										// Get energy depending on segment length
										takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
										// The other organism will be shown in green brown
										org.setColor(Utils.ColorGREENBROWN);
									} else {
										// Get energy depending on segment length
										takenEnergyCream = Utils.between((0.01 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
										// The other organism will be shown in dark olive
										org.setColorforLeaf(Utils.ColorDARKOLIVE);
									}
									// This organism will be shown in cream
									setColor(Utils.ColorCREAM);
								}
							} else {
								// Get energy depending on segment length
								takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in green brown
								org.setColor(Utils.ColorGREENBROWN);
								// This organism will be shown in cream
								setColor(Utils.ColorCREAM);
							}
						}
					}
				}
				break;
		    case GREEN:
		    case GRASS:
			case FOREST:
			case SPRING:
			case SUMMER:
            case WINTER:
			case LIME:
			case C4:
			case JADE:
			case DARKJADE:
			case DARKGREEN:
			case PURPLE:
			case TEAL:
			case CYAN:
			case YELLOW:
			case AUBURN:
			case INDIGO:
			case BLOND:
			case FLOWER:
			case DARKGRAY:
			case GOLD:
			case SPORE:
				if ((org._dodge) && ((org._canreact) || (!_hasgoodvision)) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Utils.ColorCREAM);
				} else {
					if (org._lavender > 0) {
						org.lavendershield();
						setColor(Utils.ColorCREAM);
					}
					if (org._lavender <= 0) {
						if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in cream
							setColor(Utils.ColorCREAM);
						}
					}
				}
				break;
			case SPIKE:
		    	if (((org._isaplant) && (org._createlavender == 0)) || (org._isenhanced)) {
		    		if (org._lavender > 0) {
						org.lavendershield();
						setColor(Utils.ColorCREAM);
					}
					if (org._lavender <= 0) {
						if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in cream
							setColor(Utils.ColorCREAM);
						}
					}
		    	}
		    	break;
		    case BLUE:
		    	if (((!_isenhanced) || (_creamversion != 3) || (!org._isaplant) || (org._createlavender > 0))
		    			&& (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION))) {
					setColor(Utils.ColorCREAM);
					org.setColor(Color.BLUE);
				} else {
					if (org._lavender > 0) {
						org.lavendershield();
						if ((_isenhanced) && (_creamversion == 3)) {
						    // This organism will be shown in dark gray
						    setColor(Color.DARK_GRAY);
						} else {
							// This organism will be shown in cream
							setColor(Utils.ColorCREAM);
						}
					}
					if (org._lavender <= 0) {
						if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							if ((_isenhanced) && (_creamversion == 3)) {
							    // This organism will be shown in dark gray
							    setColor(Color.DARK_GRAY);
							} else {
								// This organism will be shown in cream
								setColor(Utils.ColorCREAM);
							}
						}
					}
				}
			    break;
		    case SKY:
		    	if ((active) || (org._skyversion == 1)) {
		    		if (org.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
						org._segColor[oseg] = Utils.ColorDEEPSKY;
						org._mphoto[oseg] = -20;
						org._useextraeffects = true;
						setColor(Utils.ColorCREAM);
						org.setColor(Utils.ColorDEEPSKY);
					} else {
						// Doesn't have energy to use the shield
						if (org._lavender > 0) {
							org.lavendershield();
							setColor(Utils.ColorCREAM);
						}
						if (org._lavender <= 0) {
							if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in green brown
								org.setColor(Utils.ColorGREENBROWN);
								// This organism will be shown in cream
								setColor(Utils.ColorCREAM);
							}
						}
					}
		    	}
		    	break;
		    case DEEPSKY:
		    	if (((_isenhanced) || (!active)) && (_creamversion == 3)) {
		    		if (org._lavender > 0) {
		    			org.lavendershield();
						setColor(Color.DARK_GRAY);
					}
					if (org._lavender <= 0) {
						if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in dark gray
						    setColor(Color.DARK_GRAY);
						}
					}
				} else {
					if ((!_isfrozen) && (org._createlavender == 0)) {
						if (org._lavender > 0) {
							org.lavendershield();
							setColor(Utils.ColorCREAM);
						}
						if (org._lavender <= 0) {
							if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in green brown
								org.setColor(Utils.ColorGREENBROWN);
								// This organism will be shown in cream
								setColor(Utils.ColorCREAM);
							}
						}
					} else {
						setColor(Utils.ColorCREAM);
						org.setColor(Utils.ColorDEEPSKY);
					}
				}
			    break;
		    case OCHRE:
		    	if (org._isaplant) {
		    		if (org._lavender > 0) {
						org.lavendershield();
						setColor(Utils.ColorCREAM);
					}
					if (org._lavender <= 0) {
						if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in cream
							setColor(Utils.ColorCREAM);
						}
					}
		    	} else {
		    		if ((_isenhanced) && (_creamversion == 3)) {
			    		if (org._lavender > 0) {
			    			org.lavendershield();
							setColor(Color.DARK_GRAY);
						}
						if (org._lavender <= 0) {
							if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in green brown
								org.setColor(Utils.ColorGREENBROWN);
								// This organism will be shown in dark gray
							    setColor(Color.DARK_GRAY);
							}
						}
					}
		    	}
		    	break;
		    case FALLOW:
		    	if (((_isenhanced) || (!active)) && (_creamversion == 3)) {
		    		if (org._lavender > 0) {
		    			org.lavendershield();
						setColor(Color.DARK_GRAY);
					}
					if (org._lavender <= 0) {
						if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in dark gray
						    setColor(Color.DARK_GRAY);
						}
					}
				}
			    break;
		    case OLDBARK:
		    	if (((_isenhanced) || (!active)) && (_creamversion == 3)) {
		    		if (org._lavender > 0) {
		    			org.lavendershield();
						setColor(Color.DARK_GRAY);
					}
					if (org._lavender <= 0) {
						if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in dark gray
						    setColor(Color.DARK_GRAY);
						}
					}
				}
			    break;
		    case BARK:
		    	org._segColor[oseg] = Utils.ColorOLDBARK;
		    	if (org.active) {
					org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
					org._mphoto[oseg] = -0.5;
				}
		    	break;
		    case SILVER:
		    	if (((org._nTotalKills > 0) && ((org._isaconsumer) || (org._isafungus))) || (org._isenhanced)) {
		    		break;
				} else {
					if (org._lavender > 0) {
						org.lavendershield();
						setColor(Utils.ColorCREAM);
					}
					if (org._lavender <= 0) {
						if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in cream
							setColor(Utils.ColorCREAM);
						}
					}
				}
				break;
		    case ORANGE:
		    case FIRE:
			case RED:
			case MAROON:
			case PINK:
				if ((_isenhanced) && (_creamversion == 1)) {
					if (org._lavender > 0) {
						org.lavendershield();
						setColor(Color.DARK_GRAY);
					}
					if (org._lavender <= 0) {
						if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in dark gray
						    setColor(Color.DARK_GRAY);
						}
					}
				}
			    break;
			case FRUIT:
				if ((org._sporeversion == 2) || (_creamversion == 2)) {
					if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in cream
						setColor(Utils.ColorCREAM);
					}
				}
			    break;
		    case MAGENTA:
		    case ROSE:
				if (_altruist) {
                break;
				} else {
					if ((org._dodge) && ((org._canreact) || (!_hasgoodvision)) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorCREAM);
					} else {
						if (org._lavender > 0) {
							org.lavendershield();
							setColor(Utils.ColorCREAM);
						}
						if (org._lavender <= 0) {
							if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in green brown
								org.setColor(Utils.ColorGREENBROWN);
								// This organism will be shown in cream
								setColor(Utils.ColorCREAM);
							}
						}
					}
				}
				break;
			case CREAM:
				if ((_creamversion == 2) && (active)) {
					if ((org._creamversion == 2) && (org.active)) {
						if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in cream
							org.setColor(Utils.ColorCREAM);
							// This organism will be shown in cream
							setColor(Utils.ColorCREAM);
						}
					} else {
						if (!org.active) {
							if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in green brown
								org.setColor(Utils.ColorGREENBROWN);
								// This organism will be shown in cream
								setColor(Utils.ColorCREAM);
							}
						} else {
							if (_isenhanced) {
					    		if (org._lavender > 0) {
					    			org.lavendershield();
									setColor(Color.DARK_GRAY);
								}
								if (org._lavender <= 0) {
									if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
										// Get energy depending on segment length
										takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
										// The other organism will be shown in green brown
										org.setColor(Utils.ColorGREENBROWN);
										// This organism will be shown in dark gray
									    setColor(Color.DARK_GRAY);
									}
								}
							} else {
								if (org._lavender > 0) {
									org.lavendershield();
									setColor(Utils.ColorCREAM);
								}
								if (org._lavender <= 0) {
									if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
										// Get energy depending on segment length
										takenEnergyCream = Utils.between((0.01 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
										// The other organism will be shown in broken
										org.setColor(Utils.ColorBROKEN);
										// This organism will be shown in cream
										setColor(Utils.ColorCREAM);
									}
								}
							}
						}
					}
				}
				break;
			case LAVENDER:
				if (org._isenhanced) {
					useEnergy(Utils.between((1/Utils.LAVENDER_ENERGY_CONSUMPTION), 0, _energy));
					if (org._lavender < Utils.LAVENDER_SHIELD) {
						org._lavender += 1000;
						if (org._lavender >= Utils.LAVENDER_SHIELD) {
							org._lavender = Utils.LAVENDER_SHIELD;
						}
					}
				} else {
					useEnergy(Utils.between((0.2/Utils.LAVENDER_ENERGY_CONSUMPTION), 0, _energy));
					if (org._lavender < Utils.LAVENDER_SHIELD) {
						org._lavender += 200;
						if (org._lavender >= Utils.LAVENDER_SHIELD) {
							org._lavender = Utils.LAVENDER_SHIELD;
						}
					}
				}
				// The other organism will be shown in lavender
				org.setColor(Utils.ColorLAVENDER);
				// This organism will be shown in deadbark
				setColor(Utils.ColorDEADBARK);
				if (_energy < Utils.tol) {
					die(org);
				}
				break;
			case MINT:
				if (_healing > 0) {
					if (org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
					    _segColor[seg] = Utils.ColorDARKFIRE;
					    _mphoto[seg] = -20.5;
					    org.setColor(Utils.ColorMINT);
					    _useextraeffects = true;
					    _isinjured =true;
					}
			    } else {
			    	if (_isaplant) {
						if (org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
						    _segColor[seg] = Utils.ColorBROKEN;
						    _mphoto[seg] = -20;
						    org.setColor(Utils.ColorMINT);
						    _isinjured =true;
						}
					} else {
				    	if (org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
						    _segColor[seg] = Utils.ColorLIGHTBROWN;
						    _mphoto[seg] = -20;
						    org.setColor(Utils.ColorMINT);
						    _isinjured =true;
				    	}
				    }
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				} else {
					if (org._lavender > 0) {
						org.lavendershield();
						setColor(Utils.ColorCREAM);
					}
					if (org._lavender <= 0) {
						if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in cream
							setColor(Utils.ColorCREAM);
						}
					}
				}
				break;
			case SPIKEPOINT:
			case CORAL:
				break;
			case BROWN:
				if ((!active) && (_creamversion == 1)) {
					if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in cream
						setColor(Utils.ColorCREAM);
						if ((org.alive) && (!org._isenhanced)) {
							org._isinjured =true;
					    }
					}
				}
				break;
			default:
				if (org._lavender > 0) {
					org.lavendershield();
					setColor(Utils.ColorCREAM);
				}
				if (org._lavender <= 0) {
					if (useEnergy(Utils.CREAM_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergyCream = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in green brown
						org.setColor(Utils.ColorGREENBROWN);
						// This organism will be shown in cream
						setColor(Utils.ColorCREAM);
					}
				}
			}
			// energy interchange
		    if (takenEnergyCream > 0) {
		    	org._energy -= takenEnergyCream;
				_energy += takenEnergyCream;
				if ((_creamversion == 2) && (active)) {
					takenEnergyCream = takenEnergyCream * Utils.ORGANIC_SUBS_PRODUCED;
				} else {
					takenEnergyCream = takenEnergyCream * Utils.CREAM_ORGANIC_SUBS_PRODUCED;
				}
				useEnergy(takenEnergyCream);
		    }
			break;
		case SPIKEPOINT:
			// Spike segment: Hurts organisms, if it hits with its end point, enhanced SPIKE can feed on other organisms
			double takenEnergySpike = 0;
			if (org._hasdodged == false) {
				org._hasdodged =true;
			}
			switch (getTypeColor(org._segColor[oseg])) {
			case SPIKEPOINT:
			case OLDBARK:
			case CREAM:
				break;
			case SPIKE:
		    	if ((org._isaplant) || (org._isenhanced)) {
		    		if (_isenhanced) {
						if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length
							takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					} else {
						if ((_isaplant) || (org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					}
		    	}
		    	break;
			case BROWN:
		    	if ((!_isenhanced) && (org.alive)) {
		    		if (_isaplant) {
						if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
						    org.setColor(Utils.ColorDARKLILAC);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
							if (!org._isenhanced) {
						    	org._isinjured =true;
						    }
						}
					}
				}
		    	break;
			case BLUE:
				if (_isenhanced) {
					if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
						if (org._isenhanced) {
						    useEnergy(Utils.between((0.5 * Math.sqrt(org._m[oseg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, _energy));
							setColor(Utils.ColorDARKLILAC);
							if (_energy < Utils.tol) {
								die(org);
							}
						} else {
							setColor(Utils.ColorSPIKE);
						}
						org.setColor(Color.BLUE);
					} else {
						// Doesn't have energy to use the shield
						if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length
							takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					}
				} else {
					if (_isaplant) {
						if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
							if (org._isenhanced) {
							    useEnergy(Utils.between((0.5 * Math.sqrt(org._m[oseg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, _energy));
								setColor(Utils.ColorDARKLILAC);
								if (_energy < Utils.tol) {
									die(org);
								}
							} else {
								setColor(Utils.ColorSPIKE);
							}
							org.setColor(Color.BLUE);
						} else {
							// Doesn't have energy to use the shield
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					}
				}
				break;
			case SKY:
				if (_isenhanced) {
					if (org.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
						org._segColor[oseg] = Utils.ColorDEEPSKY;
						org._mphoto[oseg] = -20;
						org._useextraeffects = true;
						setColor(Utils.ColorSPIKE);
						org.setColor(Utils.ColorDEEPSKY);
					} else {
						// Doesn't have energy to use the shield
						if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length
							takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					}
				} else {
					if ((_isaplant) || (org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
						if (org.useEnergy(16 * Utils.SKY_ENERGY_CONSUMPTION)) {
							org._segColor[oseg] = Utils.ColorDEEPSKY;
							org._mphoto[oseg] = -20;
							org._useextraeffects = true;
							setColor(Utils.ColorSPIKE);
							org.setColor(Utils.ColorDEEPSKY);
						} else {
							// Doesn't have energy to use the shield
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					}
				}
				break;
			case DEEPSKY:
				if (_isenhanced) {
					setColor(Utils.ColorSPIKE);
			        org.setColor(Utils.ColorDEEPSKY);
				} else {
					if ((_isaplant) || (org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
						if (org.useEnergy(16 * Utils.SKY_ENERGY_CONSUMPTION)) {
							setColor(Utils.ColorSPIKE);
					        org.setColor(Utils.ColorDEEPSKY);
						} else {
							// Doesn't have energy to use the shield
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					}
				}
				break;
			case OCHRE:
				if (org._isaplant) {
					if (_isenhanced) {
						if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length
							takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					} else {
						if ((_isaplant) || (org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					}
				}
				break;
			case CORAL:
		    	if ((!_isaplant) && (!_isaconsumer) && (!_isafungus)) {
		    		if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in dark lilac
					    org.setColor(Utils.ColorDARKLILAC);
					    // This organism will be shown in spike
						setColor(Utils.ColorSPIKE);
					}
		    	}
		    	break;
			case RED:
		    	if (org._isenhanced) {
				break;
				} else {
					if (_isenhanced) {
						if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length
							takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					} else {
						if ((_isaplant) || (org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					}
				}
		    	break;
			case LAVENDER:
				if (_altruist) {
	            break;
				} else {
					if (_isenhanced) {
						if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorSPIKE);
						} else {
							if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
								if ((org._isaplant) && (!org._isaconsumer) && (!org._isafungus) && (!org._isakiller)) {
									// Get energy depending on segment length
									takenEnergySpike = Utils.between((0.4 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								    // The other organism will be shown in green brown
								    org.setColor(Utils.ColorGREENBROWN);
								} else {
									// Get energy depending on segment length
									takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								    // The other organism will be shown in yellow
								    org.setColor(Color.YELLOW);
								}
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					} else {
						if (_isaplant) {
							if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
								org.setColor(Utils.ColorTEAL);
								setColor(Utils.ColorSPIKE);
							} else {
								if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
									// Get energy depending on segment length
									takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in dark lilac
								    org.setColor(Utils.ColorDARKLILAC);
								    // This organism will be shown in spike
									setColor(Utils.ColorSPIKE);
								}
							}
						} else {
							if ((org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
								if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
									// Get energy depending on segment length
									takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in dark lilac
								    org.setColor(Utils.ColorDARKLILAC);
								    // This organism will be shown in spike
									setColor(Utils.ColorSPIKE);
								}
							}
						}
					}
				}
				break;
			case MINT:
		    case MAGENTA:
		    case ROSE:
		    	if (_altruist) {
                break;
				} else {
					if (_isenhanced) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorSPIKE);
						} else {
							if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
								if ((org._isaplant) && (!org._isaconsumer) && (!org._isafungus) && (!org._isakiller)) {
									// Get energy depending on segment length
									takenEnergySpike = Utils.between((0.4 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								    // The other organism will be shown in green brown
								    org.setColor(Utils.ColorGREENBROWN);
								} else {
									// Get energy depending on segment length
									takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								    // The other organism will be shown in yellow
								    org.setColor(Color.YELLOW);
								}
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					} else {
						if (_isaplant) {
							if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
								org.setColor(Utils.ColorTEAL);
								setColor(Utils.ColorSPIKE);
							} else {
								if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
									// Get energy depending on segment length
									takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in dark lilac
								    org.setColor(Utils.ColorDARKLILAC);
								    // This organism will be shown in spike
									setColor(Utils.ColorSPIKE);
								}
							}
						} else {
							if ((org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
								if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
									// Get energy depending on segment length
									takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in dark lilac
								    org.setColor(Utils.ColorDARKLILAC);
								    // This organism will be shown in spike
									setColor(Utils.ColorSPIKE);
								}
							}
						}
					}
				}
				break;
			case WHITE:
		    	if (_isenhanced) {
		    		if ((org._isaplant) || (org._isaconsumer) || (org._isafungus) || (org._plagueversion > 0) || (org._isauburn)) {
		    			if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length
		    				takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					}
				} else {
					if ((_isaplant) || (org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
						if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
						    org.setColor(Utils.ColorDARKLILAC);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					}
				}
		    	break;
			case GRASS:
				if (_isenhanced) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorSPIKE);
					} else {
						if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length
							takenEnergySpike = Utils.between((0.4 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in green brown
						    org.setColor(Utils.ColorGREENBROWN);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					}
				} else {
					if (_isaplant) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorSPIKE);
						} else {
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((2.5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					} else {
						if ((org._isaconsumer) || (org._isafungus)) {
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((2.5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					}
				}
				break;
			case LEAF:
				if (_isenhanced) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorSPIKE);
					} else {
						if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
							if ((org._modifiesleaf) && (!org._isfrozen)) {
								if (org._framesColor > 0) {
									// Get energy depending on segment length
									takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								    // The other organism will be shown in yellow
								    org.setColor(Color.YELLOW);
								} else {
									// Get energy depending on segment length
									takenEnergySpike = Utils.between((0.016 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in dark olive
									org.setColorforLeaf(Utils.ColorDARKOLIVE);
								}
							} else {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							    // The other organism will be shown in yellow
							    org.setColor(Color.YELLOW);
							}
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					}
				} else {
					if (_isaplant) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorSPIKE);
						} else {
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								if ((!org._modifiesleaf) || (org._framesColor > 0)) {
									// Get energy depending on segment length
									takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in dark lilac
								    org.setColor(Utils.ColorDARKLILAC);
								    // This organism will be shown in spike
									setColor(Utils.ColorSPIKE);
								} else {
									// The other organism will be shown in light blue
									org.setColortwoFrames(Utils.ColorLIGHT_BLUE);
									// This organism will be shown in spike
									setColor(Utils.ColorSPIKE);
								}
							}
						}
					} else {
						if ((org._isaconsumer) || (org._isafungus)) {
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								if ((!org._modifiesleaf) || (org._framesColor > 0)) {
									// Get energy depending on segment length
									takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in dark lilac
								    org.setColor(Utils.ColorDARKLILAC);
								    // This organism will be shown in spike
									setColor(Utils.ColorSPIKE);
								} else {
									// The other organism will be shown in light blue
									org.setColortwoFrames(Utils.ColorLIGHT_BLUE);
									// This organism will be shown in spike
									setColor(Utils.ColorSPIKE);
								}
							}
						}
					}
				}
				break;
			case GREEN:
			case FOREST:
			case SPRING:
			case SUMMER:
            case WINTER:
			case LIME:
			case C4:
			case JADE:
			case DARKJADE:
			case DARKGREEN:
			case PURPLE:
			case TEAL:
			case CYAN:
			case AUBURN:
			case INDIGO:
			case BLOND:
			case FLOWER:
			case DARKGRAY:
			case GOLD:
			case SPORE:
				if (_isenhanced) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorSPIKE);
					} else {
						if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length
							takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					}
				} else {
					if (_isaplant) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorSPIKE);
						} else {
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					} else {
						if ((org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					}
				}
				break;
			case YELLOW:
				if (_isenhanced) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorSPIKE);
					} else {
						if ((org._isaconsumer) || (org._isafungus) || (org._isaplant) || (org._transfersenergy)) {
							if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
							    // Get energy depending on segment length
								takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							    // The other organism will be shown in yellow
							    org.setColor(Color.YELLOW);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						} else {
							if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							    // The other organism will be shown in green brown
							    org.setColor(Utils.ColorGREENBROWN);
								// This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
						    }
						}
					}
				} else {
					if (_isaplant) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorSPIKE);
						} else {
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					} else {
						if ((org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					}
				}
				break;
		    case FRUIT:
		    	if (_isenhanced) {
		    		if (org._sporeversion == 2) {
		    		    if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
		    		    	// Get energy depending on segment length
		    		    	takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    if ((_infectedGeneticCode != org._geneticCode) && (_antiviral == 0)) {
								// Infectious fruit
								infectedByFruit(org);
								setColor(Utils.ColorBLOND);
							} else {
								// This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					}
				} else {
					if (_isaplant) {
						if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
						    org.setColor(Utils.ColorDARKLILAC);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					}
				}
		    	break;
		    case MAROON:
				if (_isenhanced) {
					if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
					    // Get energy depending on segment length
						takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					    // The other organism will be shown in yellow
					    org.setColor(Color.YELLOW);
					    // This organism will be shown in spike
						setColor(Utils.ColorSPIKE);
					}
				} else {
					if (_isaplant) {
						if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
						    org.setColor(Utils.ColorDARKLILAC);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					} else {
						if ((org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					}
				}
				break;
		    case SILVER:
		    	if (_isenhanced) {
					if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
					    // Get energy depending on segment length
						takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					    if ((_nTotalKills >= org._nTotalKills) || ((!org._isaconsumer) && (!org._isafungus))) {
					    	// The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
					    } else {
					    	// The other organism will be shown in gold
						    org.setColor(Utils.ColorGOLD);
					    }
					    // This organism will be shown in spike
						setColor(Utils.ColorSPIKE);
					}
				} else {
					if ((_isaplant) || (org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
						if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
						    org.setColor(Utils.ColorDARKLILAC);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					}
				}
		    	break;
		    case PLAGUE:
		    	if (_isenhanced) {
		    		if (org._isinfectious) {
		    			if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length
							takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
		    		} else {
		    			if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
		    				// Get energy depending on segment length
		    				takenEnergySpike = Utils.between((0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
		    		}
				} else {
					if ((_isaplant) || (org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
						if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
						    org.setColor(Utils.ColorDARKLILAC);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					}
				}
		    	break;
		    case FALLOW:
		    	if (!_isenhanced) {
		    		if ((_isaplant) || (org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
						if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
						    org.setColor(Utils.ColorDARKLILAC);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					}
				}
		    	break;
		    case DARK:
		    	if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				} else {
					if (_isenhanced) {
						if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length
							takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					} else {
						if ((_isaplant) || (org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
							if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
								// Get energy depending on segment length
								takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
							    org.setColor(Utils.ColorDARKLILAC);
							    // This organism will be shown in spike
								setColor(Utils.ColorSPIKE);
							}
						}
					}
				}
		    	break;
		    case BARK:
		    	org._segColor[oseg] = Utils.ColorOLDBARK;
		    	if (org.active) {
					org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
					org._mphoto[oseg] = -0.5;
				}
				break;
		    case OLIVE:
		    	break;
			default:
				if (_isenhanced) {
					if (useEnergy(Utils.MOSQUITO_ENERGY_CONSUMPTION)) {
					    // Get energy depending on segment length
						takenEnergySpike = Utils.between((0.8 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					    // The other organism will be shown in yellow
					    org.setColor(Color.YELLOW);
					    // This organism will be shown in spike
						setColor(Utils.ColorSPIKE);
					}
				} else {
					if ((_isaplant) || (org._isaconsumer) || (org._isafungus) || ((!org._isaplant) && (_isaconsumer))) {
						if (useEnergy(Utils.SPIKE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
							takenEnergySpike = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
						    org.setColor(Utils.ColorDARKLILAC);
						    // This organism will be shown in spike
							setColor(Utils.ColorSPIKE);
						}
					}
				}
			}
			// energy interchange
			if (takenEnergySpike > 0) {
				if (_isenhanced) {
					org._energy -= takenEnergySpike;
					_energy += takenEnergySpike;
					takenEnergySpike = takenEnergySpike * Utils.ORGANIC_SUBS_PRODUCED;
					useEnergy(takenEnergySpike);
				} else {
				    org.useEnergy(takenEnergySpike);
				}
			}
			break;
		case OCHRE:
			// Ochre segment: Push other organisms away
			if (_hasdodged == false) {
				_hasdodged =true;
			}
			switch (getTypeColor(org._segColor[oseg])) {
			case OCHRE:
				if (Utils.random.nextBoolean()) {
					if ((_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION)) || (!_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION/1000))) {
						if (!_useframemovement) {
							standochre();
						}
						org.dx=Utils.between((org._centerX-_centerX)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						org.dy=Utils.between((org._centerY-_centerY)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						if (_isenhanced) {
							if (org._lavender > 0) {
								org.lavendershield();
							}
							if (org._lavender <= 0) {
								if (org._isenhanced) {
									org.useEnergy(Utils.between((Math.abs(org.dx) + Math.abs(org.dy)) * (Utils.ORGANIC_OBTAINED_ENERGY/250), 0, org._energy));
								} else {
									org.useEnergy(Utils.between(2 * (Math.abs(org.dx) + Math.abs(org.dy)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy));
								}
							    org.setColor(Utils.ColorDARKLILAC);
							}
						}
						setColor(Utils.ColorOCHRE);
					}
				}
				break;
			case BLUE:
				if (Utils.random.nextBoolean()) {
					if ((_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION)) || (!_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION/1000))) {
						if (!_useframemovement) {
							standochre();
						}
						org.dx=Utils.between((org._centerX-_centerX)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						org.dy=Utils.between((org._centerY-_centerY)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						if (_isenhanced) {
							if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
								org.setColor(Color.BLUE);
							} else {
								if (org._lavender > 0) {
									org.lavendershield();
								}
								if (org._lavender <= 0) {
									org.useEnergy(Utils.between(2 * (Math.abs(org.dx) + Math.abs(org.dy)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy));
								    org.setColor(Utils.ColorDARKLILAC);
								}
							}
						}
						setColor(Utils.ColorOCHRE);
					}
				}
				break;
			case RED:
				if (Utils.random.nextBoolean()) {
					if ((_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION)) || (!_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION/1000))) {
						if (!_useframemovement) {
							standochre();
						}
						org.dx=Utils.between((org._centerX-_centerX)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						org.dy=Utils.between((org._centerY-_centerY)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						if ((_isenhanced) && (org._createlavender == 0)) {
							if (org._lavender > 0) {
								org.lavendershield();
							}
							if (org._lavender <= 0) {
								org.useEnergy(Utils.between(2 * (Math.abs(org.dx) + Math.abs(org.dy)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy));
							    org.setColor(Utils.ColorDARKLILAC);
							}
						}
						setColor(Utils.ColorOCHRE);
					}
				}
				break;
			case MAROON:
				if (Utils.random.nextBoolean()) {
					if ((_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION)) || (!_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION/1000))) {
						if (!_useframemovement) {
							standochre();
						}
						org.dx=Utils.between((org._centerX-_centerX)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						org.dy=Utils.between((org._centerY-_centerY)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						if (_isenhanced) {
							if (org._lavender > 0) {
								org.lavendershield();
							}
							if (org._lavender <= 0) {
								if (org._isenhanced) {
									org.useEnergy(Utils.between(0.5 * (Math.abs(org.dx) + Math.abs(org.dy)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy));
								} else {
									org.useEnergy(Utils.between((Math.abs(org.dx) + Math.abs(org.dy)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy));
								}
							    org.setColor(Utils.ColorDARKLILAC);
							}
						}
						setColor(Utils.ColorOCHRE);
					}
				}
				break;
			case CREAM:
			case CORAL:
				if (Utils.random.nextBoolean()) {
					if ((_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION)) || (!_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION/1000))) {
						if (!_useframemovement) {
							standochre();
						}
						org.dx=Utils.between((org._centerX-_centerX)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						org.dy=Utils.between((org._centerY-_centerY)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						setColor(Utils.ColorOCHRE);
					}
				}
				break;
			case SKY:
			case DEEPSKY:
				if (Utils.random.nextBoolean()) {
					if ((_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION)) || (!_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION/1000))) {
						if (!_useframemovement) {
							standochre();
						}
						if ((org._skyversion == 1) && (_isaplant)) {
							setColor(Utils.ColorOCHRE);
							org.setColor(Utils.ColorDEEPSKY);
						} else {
							org.dx=Utils.between((org._centerX-_centerX)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
							org.dy=Utils.between((org._centerY-_centerY)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
							if (_isenhanced) {
								if (org._lavender > 0) {
									org.lavendershield();
								}
								if (org._lavender <= 0) {
									org.useEnergy(Utils.between(2 * (Math.abs(org.dx) + Math.abs(org.dy)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy));
								    org.setColor(Utils.ColorDARKLILAC);
								}
							}
							setColor(Utils.ColorOCHRE);
						}
					}
				}
				break;
			case BROWN:
				if ((org.alive) && (Utils.random.nextBoolean())) {
					if ((_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION)) || (!_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION/1000))) {
						if (!_useframemovement) {
							standochre();
						}
						org.dx=Utils.between((org._centerX-_centerX)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						org.dy=Utils.between((org._centerY-_centerY)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						if (_isenhanced) {
							if (org._lavender > 0) {
								org.lavendershield();
							}
							if (org._lavender <= 0) {
								org.useEnergy(Utils.between(2 * (Math.abs(org.dx) + Math.abs(org.dy)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy));
							    org.setColor(Utils.ColorDARKLILAC);
							    if (!org._isenhanced) {
							    	org._isinjured =true;
							    }
							}
						}
						setColor(Utils.ColorOCHRE);
					}
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
					if (Utils.random.nextBoolean()) {
						if ((_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION)) || (!_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION/1000))) {
							if (!_useframemovement) {
								standochre();
							}
							org.dx=Utils.between((org._centerX-_centerX)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
							org.dy=Utils.between((org._centerY-_centerY)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
							if (_isenhanced) {
								if (org._lavender > 0) {
									org.lavendershield();
								}
								if (org._lavender <= 0) {
									org.useEnergy(Utils.between(2 * (Math.abs(org.dx) + Math.abs(org.dy)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy));
								    org.setColor(Utils.ColorDARKLILAC);
								}
							}
							setColor(Utils.ColorOCHRE);
						}
					}
				}
				break;
			case OLIVE:
				break;
			default:
				if (Utils.random.nextBoolean()) {
					if ((_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION)) || (!_isaplant && useEnergy(Utils.OCHRE_ENERGY_CONSUMPTION/1000))) {
						if (!_useframemovement) {
							standochre();
						}
						org.dx=Utils.between((org._centerX-_centerX)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						org.dy=Utils.between((org._centerY-_centerY)*0.2*_m[seg]*_m[seg]/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						if (_isenhanced) {
							if (org._lavender > 0) {
								org.lavendershield();
							}
							if (org._lavender <= 0) {
								org.useEnergy(Utils.between(2 * (Math.abs(org.dx) + Math.abs(org.dy)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy));
							    org.setColor(Utils.ColorDARKLILAC);
							}
						}
						setColor(Utils.ColorOCHRE);
					}
				}
			}
			break;
		case SILVER:
			// Silver segment: infects all other organism, if it has more infections , duels with other absorbing segments
			double takenEnergy = 0;
			switch (getTypeColor(org._segColor[oseg])) {
			case SILVER:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if ((org._isaplant) || (org._isaconsumer) || ((org._isafungus) && (org._nTotalKills > 0))) {
						if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
					        org.infectedBy(this);
					        org.setColor(Utils.ColorBLOND);
					        setColor(Color.LIGHT_GRAY);
						}
			    	}
				}
				if ((org._indigo > 0) && (_nTotalInfected > 0) && (!org._isaplant) && (!org._isaconsumer) && (!org._isafungus) && (!_hasgoodvision)) {
					if ((_indigo == 0) || (_isaplant) || (_isaconsumer) || (_isafungus)) {
						if (useEnergy((Utils.SILVER_ENERGY_CONSUMPTION + 0.01)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
			    			org.setColor(Utils.ColorINDIGO);
			    			setColor(Color.LIGHT_GRAY);
			    		}
			    	}
				}
				if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
					if (((org._nTotalKills > 0) && ((org._isaconsumer) || (org._isafungus))) || (org._isenhanced)) {
						if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length and relation between kills of both organisms
						    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in gold
						    org.setColor(Utils.ColorGOLD);
						    // This organism will be shown in gold
						    setColor(Utils.ColorGOLD);
						}
					} else {
						if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length and relation between kills of both organisms
						    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in gold
						    setColor(Utils.ColorGOLD);
						}
				    }
				}
				break;
			case WHITE:
			case PLAGUE:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if ((org._isaplant) || (org._isaconsumer)) {
						if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
					        org.infectedBy(this);
					        org.setColor(Utils.ColorBLOND);
					        setColor(Color.LIGHT_GRAY);
						}
			    	}
				}
				if ((org._indigo > 0) && (_nTotalInfected > 0) && (!org._isaplant) && (!org._isaconsumer) && (!org._isafungus) && (!_hasgoodvision)) {
					if ((_indigo == 0) || (_isaplant) || (_isaconsumer) || (_isafungus)) {
						if (useEnergy((Utils.SILVER_ENERGY_CONSUMPTION + 0.01)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
			    			org.setColor(Utils.ColorINDIGO);
			    			setColor(Color.LIGHT_GRAY);
			    		}
			    	}
				}
				if ((org._isaplant) || (org._isaconsumer) || (org._isafungus) || (org._plagueversion > 0) || (org._isauburn)) {
					if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
						if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length and relation between kills of both organisms
						    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in gold
						    setColor(Utils.ColorGOLD);
						}
					}
				}
				break;
			case FRUIT:
				if (org._sporeversion == 2) {
					if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
						if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length and relation between kills of both organisms
						    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(0.1 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in green brown
						    org.setColor(Utils.ColorGREENBROWN);
						    if (_infectedGeneticCode != org._geneticCode) {
								// Infectious fruit
								infectedByFruit(org);
								setColor(Utils.ColorBLOND);
							} else {
								// This organism will be shown in gold
						        setColor(Utils.ColorGOLD);
							}
						}
					}
				}
				break;
			case INDIGO:
				if ((_nTotalInfected > 0) && (!_hasgoodvision)) {
					if ((_indigo == 0) || (_isaplant) || (_isaconsumer) || (_isafungus)) {
						if (useEnergy((Utils.SILVER_ENERGY_CONSUMPTION + 0.01)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
			    			org.setColor(Utils.ColorINDIGO);
			    			setColor(Color.LIGHT_GRAY);
			    		}
			    	}
				}
				if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
					    setColor(Utils.ColorGOLD);
				    } else {
					    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length and relation between kills of both organisms
							takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in gold
							setColor(Utils.ColorGOLD);
						}
					}
				}
				break;
			case GRASS:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org._hasdodged =true;
						org.setColor(Utils.ColorTEAL);
						setColor(Color.LIGHT_GRAY);
					} else {
						if (org._jadefactor > 1) {
				    		org._segColor[oseg] = Utils.ColorDARKGREEN;
				    		setColor(Color.LIGHT_GRAY);
							org._hasdodged =true;
							org._useextraeffects =true;
				    	} else {
				    		if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
						        org.setColor(Utils.ColorBLOND);
						        setColor(Color.LIGHT_GRAY);
							}
				    	}
					}
				}
				if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
					    setColor(Utils.ColorGOLD);
					} else {
					    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length and relation between kills of both organisms
							takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])/2) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in green brown
							org.setColor(Utils.ColorGREENBROWN);
							// This organism will be shown in gold
							setColor(Utils.ColorGOLD);
						}
					}
				}
				break;
			case LEAF:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org._hasdodged =true;
						org.setColor(Utils.ColorTEAL);
						setColor(Color.LIGHT_GRAY);
					} else {
						if (org._jadefactor > 1) {
				    		org._segColor[oseg] = Utils.ColorDARKGREEN;
				    		setColor(Color.LIGHT_GRAY);
							org._hasdodged =true;
							org._useextraeffects =true;
				    	} else {
				    		if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
						        org.setColor(Utils.ColorBLOND);
						        setColor(Color.LIGHT_GRAY);
							}
				    	}
					}
				}
				if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
					    setColor(Utils.ColorGOLD);
					} else {
					    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
					    	if (org._modifiesleaf) {
								if (org._framesColor > 0) {
									// Get energy depending on segment length and relation between kills of both organisms
									takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in yellow
									org.setColor(Color.YELLOW);
								} else {
									// Get energy depending on segment length and relation between kills of both organisms
									takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])/50) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in dark olive
									org.setColorforLeaf(Utils.ColorDARKOLIVE);
								}
							} else {
								// Get energy depending on segment length and relation between kills of both organisms
								takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in yellow
								org.setColor(Color.YELLOW);
							}
							// This organism will be shown in gold
							setColor(Utils.ColorGOLD);
						}
					}
				}
				break;
			case GREEN:
			case FOREST:
			case SPRING:
			case SUMMER:
            case WINTER:
			case LIME:
			case C4:
			case JADE:
			case DARKJADE:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						if (org._isonlyc4 != 2) {
							org._hasdodged =true;
						} else {
							if (((!org._isauburn) || (_hasgoodvision)) && (((!_isaplant) && (org._indigo == 0)) || (org._framesColor > 8))) {
								org._hasdodged =true;
							}
						}
						org.setColor(Utils.ColorTEAL);
						setColor(Color.LIGHT_GRAY);
					} else {
						if (org._jadefactor > 1) {
							if (org._segColor[oseg].equals(Utils.ColorJADE)) {
				    			org._segColor[oseg] = Utils.ColorDARKJADE;
				    		} else {
				    			org._segColor[oseg] = Utils.ColorDARKGREEN;
				    		}
				    		setColor(Color.LIGHT_GRAY);
							org._hasdodged =true;
							org._useextraeffects =true;
				    	} else {
				    		if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
						        org.setColor(Utils.ColorBLOND);
						        setColor(Color.LIGHT_GRAY);
							}
				    	}
					}
				}
				if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
					    setColor(Utils.ColorGOLD);
					} else {
					    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length and relation between kills of both organisms
							takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in gold
							setColor(Utils.ColorGOLD);
						}
					}
				}
				break;
			case DARKGREEN:
			case PURPLE:
			case TEAL:
			case CYAN:
			case YELLOW:
			case AUBURN:
			case BLOND:
			case FLOWER:
			case DARKGRAY:
			case GOLD:
			case SPORE:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if ((org._isaplant) || (org._isaconsumer)) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							if (org._isonlyc4 != 2) {
								org._hasdodged =true;
							} else {
								if (((!org._isauburn) || (_hasgoodvision)) && (((!_isaplant) && (org._indigo == 0)) || (org._framesColor > 8))) {
									org._hasdodged =true;
								}
							}
							org.setColor(Utils.ColorTEAL);
							setColor(Color.LIGHT_GRAY);
						} else {
							if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
						        org.setColor(Utils.ColorBLOND);
						        setColor(Color.LIGHT_GRAY);
							}
						}
					}
				}
				if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
					    setColor(Utils.ColorGOLD);
					} else {
					    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length and relation between kills of both organisms
							takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in yellow
							org.setColor(Color.YELLOW);
							// This organism will be shown in gold
							setColor(Utils.ColorGOLD);
						}
					}
				}
				break;
			case RED:
			    if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
			    	if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
				        org.infectedBy(this);
				        org.setColor(Utils.ColorBLOND);
				        setColor(Color.LIGHT_GRAY);
					}
				}
			    if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
				    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
					    // Get energy depending on segment length and relation between kills of both organisms
					    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					    // The other organism will be shown in red
					    org.setColor(Color.RED);
					    // This organism will be shown in gold
					    setColor(Utils.ColorGOLD);
				    }
				}
				break;
			case FIRE:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
				        org.infectedBy(this);
				        org.setColor(Utils.ColorBLOND);
				        setColor(Color.LIGHT_GRAY);
					}
				}
				if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
				    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
					    // Get energy depending on segment length and relation between kills of both organisms
					    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					    // The other organism will be shown in fire
					    org.setColor(Utils.ColorFIRE);
					    // This organism will be shown in gold
					    setColor(Utils.ColorGOLD);
				    }
				}
				break;
			case ORANGE:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
				        org.infectedBy(this);
				        org.setColor(Utils.ColorBLOND);
				        setColor(Color.LIGHT_GRAY);
					}
				}
				if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
				    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
					    // Get energy depending on segment length and relation between kills of both organisms
					    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					    // The other organism will be shown in orange
					    org.setColor(Color.ORANGE);
					    // This organism will be shown in gold
					    setColor(Utils.ColorGOLD);
				    }
				}
				break;
			case PINK:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if (org._modifiespink) {
						if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
					        org.infectedBy(this);
					        org.setColor(Utils.ColorBLOND);
					        setColor(Color.LIGHT_GRAY);
						}
			    	}
				}
				if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
					if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
					    // Get energy depending on segment length and relation between kills of both organisms
					    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					    // The other organism will be shown in yellow
					    org.setColor(Color.YELLOW);
					    // This organism will be shown in gold
					    setColor(Utils.ColorGOLD);
					}
				}
				break;
			case SPIKE:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if ((org._isaplant) || (org._isaconsumer)) {
						if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
					        org.infectedBy(this);
					        org.setColor(Utils.ColorBLOND);
					        setColor(Color.LIGHT_GRAY);
						}
					}
				}
				if ((org._isaplant) || (org._isenhanced) || (org._nTotalKills < _nTotalKills)) {
					if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
					    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length and relation between kills of both organisms
						    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in gold
						    setColor(Utils.ColorGOLD);
					    }
					}
				}
				break;
			case SPIKEPOINT:
				if (org._nTotalKills < _nTotalKills) {
					if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
					    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length and relation between kills of both organisms
						    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    if (org._isenhanced) {
						    	// The other organism will be shown in spike
							    org.setColor(Utils.ColorSPIKE);
						    } else {
						    	// The other organism will be shown in yellow
							    org.setColor(Color.YELLOW);
						    }
						    // This organism will be shown in gold
						    setColor(Utils.ColorGOLD);
					    }
					}
				}
				break;
			case LAVENDER:
				if (_altruist) {
				break;
				} else {
					if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
						if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
						    setColor(Utils.ColorGOLD);
						} else {
						    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
						    	if ((org._isaplant) && (!org._isaconsumer) && (!org._isafungus) && (!org._isakiller)) {
						    		// Get energy depending on segment length and relation between kills of both organisms
									takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])/2) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in green brown
									org.setColor(Utils.ColorGREENBROWN);
								} else {
									// Get energy depending on segment length and relation between kills of both organisms
								    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								    // The other organism will be shown in yellow
								    org.setColor(Color.YELLOW);
								}
								// This organism will be shown in gold
								setColor(Utils.ColorGOLD);
							}
						}
					}
				}
				break;
			case MINT:
				if (_altruist) {
				break;
				} else {
					if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
						    setColor(Utils.ColorGOLD);
						} else {
						    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
						    	if ((org._isaplant) && (!org._isaconsumer) && (!org._isafungus) && (!org._isakiller)) {
						    		// Get energy depending on segment length and relation between kills of both organisms
									takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])/2) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in green brown
									org.setColor(Utils.ColorGREENBROWN);
								} else {
									// Get energy depending on segment length and relation between kills of both organisms
								    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								    // The other organism will be shown in yellow
								    org.setColor(Color.YELLOW);
								}
								// This organism will be shown in gold
								setColor(Utils.ColorGOLD);
							}
						}
					}
				}
				break;
			case MAGENTA:
			case ROSE:
				if (_altruist) {
                break;
				} else {
					if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
						if ((org._isaplant) || (org._isaconsumer)) {
							if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
								if (org._isonlyc4 != 2) {
									org._hasdodged =true;
								} else {
									if (((!org._isauburn) || (_hasgoodvision)) && (((!_isaplant) && (org._indigo == 0)) || (org._framesColor > 8))) {
										org._hasdodged =true;
									}
								}
								org.setColor(Utils.ColorTEAL);
								setColor(Color.LIGHT_GRAY);
							} else {
								if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorBLOND);
							        setColor(Color.LIGHT_GRAY);
								}
							}
						}
					}
					if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
						    setColor(Utils.ColorGOLD);
						} else {
						    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
						    	if ((org._isaplant) && (!org._isaconsumer) && (!org._isafungus) && (!org._isakiller)) {
						    		// Get energy depending on segment length and relation between kills of both organisms
									takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])/2) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in green brown
									org.setColor(Utils.ColorGREENBROWN);
								} else {
									// Get energy depending on segment length and relation between kills of both organisms
								    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								    // The other organism will be shown in yellow
								    org.setColor(Color.YELLOW);
								}
								// This organism will be shown in gold
								setColor(Utils.ColorGOLD);
							}
						}
					}
				}
				break;
			case BLUE:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if ((org._isaplant) || (org._isaconsumer)) {
						if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
					        org.infectedBy(this);
					        org.setColor(Utils.ColorBLOND);
					        setColor(Color.LIGHT_GRAY);
						}
					}
				}
				if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
					if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
						if (org._isenhanced) {
						    useEnergy(Utils.between((0.5 * Math.sqrt(org._m[oseg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, _energy));
							setColor(Utils.ColorDARKLILAC);
							if (_energy < Utils.tol) {
								die(org);
							}
						} else {
							setColor(Utils.ColorGOLD);
						}
						org.setColor(Color.BLUE);
					} else {
						if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length and relation between kills of both organisms
						    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in gold
						    setColor(Utils.ColorGOLD);
					    }
					}
				}
				break;
			case CREAM:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
			    	if (org._isaplant) {
			    		if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
					        org.infectedBy(this);
					        org.setColor(Utils.ColorBLOND);
					        setColor(Color.LIGHT_GRAY);
						}
					}
				}
				break;
			case MAROON:
			case CORAL:
			case OCHRE:
			case SKY:
			case OLIVE:
			case OLDBARK:
			case FALLOW:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if ((org._isaplant) || (org._isaconsumer)) {
						if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
					        org.infectedBy(this);
					        org.setColor(Utils.ColorBLOND);
					        setColor(Color.LIGHT_GRAY);
						}
					}
				}
				break;
			case BARK:
				org._segColor[oseg] = Utils.ColorOLDBARK;
				if (org.active) {
					org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
					org._mphoto[oseg] = -0.5;
				}
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if ((!_isaplant) && (!_isaconsumer) && (!_isafungus)) {
						if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
						    org.infectedBy(this);
						    org.setColor(Utils.ColorBLOND);
						    setColor(Color.LIGHT_GRAY);
					    }
					}
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				} else {
					if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
						if ((org._isaplant) || (org._isaconsumer)) {
							if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
						        org.setColor(Utils.ColorBLOND);
						        setColor(Color.LIGHT_GRAY);
							}
						}
					}
					if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
					    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
						    // Get energy depending on segment length and relation between kills of both organisms
						    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						    // The other organism will be shown in yellow
						    org.setColor(Color.YELLOW);
						    // This organism will be shown in gold
						    setColor(Utils.ColorGOLD);
					    }
					}
				}
				break;
			case BROWN:
				break;
			default:
				if ((org._infectedGeneticCode != _geneticCode) && (org._nTotalInfected < _nTotalInfected)) {
					if ((org._isaplant) || (org._isaconsumer)) {
						if (useEnergy(Utils.SILVER_ENERGY_CONSUMPTION)) {
					        org.infectedBy(this);
					        org.setColor(Utils.ColorBLOND);
					        setColor(Color.LIGHT_GRAY);
						}
					}
				}
				if (((_nTotalKills > 0) && ((_isaconsumer) || ((_isafungus) && (_isaconsumer = true)))) || (_isenhanced)) {
				    if (useEnergy(Utils.EXPERIENCE_ENERGY_CONSUMPTION)) {
					    // Get energy depending on segment length and relation between kills of both organisms
					    takenEnergy = Utils.between(((_nTotalKills+12)/(org._nTotalKills+12))*(Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					    // The other organism will be shown in yellow
					    org.setColor(Color.YELLOW);
					    // This organism will be shown in gold
					    setColor(Utils.ColorGOLD);
				    }
				}
			}
			if (org._hasdodged == false) {
				if (((_nTotalKills > 0) && (_isaconsumer)) || (_isenhanced)) {
					org._hasdodged =true;
				}
			}
			// energy interchange
			if (takenEnergy > 0) {
				org._energy -= takenEnergy;
				_energy += takenEnergy;
				takenEnergy = takenEnergy * Utils.ORGANIC_SUBS_PRODUCED;
				useEnergy(takenEnergy);
			}
			break;
		case GRAY:
			// Gray segment: Kill an organism
			if (org._hasdodged == false) {
				if ((org._isonlyc4 != 2) || (org._healing == 0) || (_hasgoodvision)) {
					org._hasdodged =true;
				}
		    }
			switch (getTypeColor(org._segColor[oseg])) {
			case BROWN:
			case SPIKEPOINT:
				break;
			case FALLOW:
				if ((_isenhanced) && (_segmentpretoucheffects < 3) && ((org._isaplant) || (org._isinfectious))) {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus)))) {
						if (org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
						    org.setColor(Color.MAGENTA);
						    setColor(Color.GRAY);
						} else {
							if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
								org.survive(this);
							}
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				}
				break;
			case PINK:
			case OLDBARK:
				if ((_isenhanced) && (_segmentpretoucheffects < 3) && (!_isaconsumer) && (!_isafungus)) {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus))) && (org.active)) {
						if (org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
						    org.setColor(Color.MAGENTA);
						    setColor(Color.GRAY);
						} else {
							if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
								org.survive(this);
							}
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				}
				break;
			case CORAL:
				if (((!_isaplant) || ((_isenhanced) && (_segmentpretoucheffects < 3))) && (!_isaconsumer) && (!_isafungus)) {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus)))) {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.survive(this);
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
	    		}
				break;
			case MAROON:
				if ((org._healing > 0) && (org._framesColor <= 0) && ((_isaconsumer) || (_isafungus)) && (org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION))) {
				    org.setColor(Color.MAGENTA);
				    setColor(Color.GRAY);
				} else {
					if ((org._healing > 0) && (!org._isgray)) {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.survive(this);
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				}
				break;
			case LEAF:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Color.GRAY);
				} else {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus))) && (org.active)) {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							if ((!org._modifiesleaf) || (org._framesColor > 0)) {
								org.survive(this);
							} else {
								// The other organism will be shown in light blue
								org.setColortwoFrames(Utils.ColorLIGHT_BLUE);
								setColor(Color.GRAY);
							}
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							if ((!org._modifiesleaf) || (org._framesColor > 0)) {
								org.die(this);
								setColor(Color.GRAY);
							} else {
								// The other organism will be shown in light blue
								org.setColortwoFrames(Utils.ColorLIGHT_BLUE);
								setColor(Color.GRAY);
							}
						}
					}
				}
				break;
			case GREEN:
			case GRASS:
			case FOREST:
			case SPRING:
			case SUMMER:
            case WINTER:
			case LIME:
			case C4:
			case DARKGREEN:
			case PURPLE:
			case TEAL:
			case CYAN:
			case YELLOW:
			case AUBURN:
			case INDIGO:
			case BLOND:
			case FLOWER:
			case DARKGRAY:
			case GOLD:
			case SPORE:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Color.GRAY);
				} else {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus))) && (org.active)) {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.survive(this);
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				}
				break;
			case JADE:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Color.GRAY);
				} else {
					if ((org._healing > 0) && ((!_isenhanced) || (_segmentpretoucheffects == 3)) && (org.active)) {
						org._segColor[oseg] = Utils.ColorDARKJADE;
				    	setColor(Color.GRAY);
				    	org._useextraeffects =true;
					} else {
						if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus))) && (org.active)) {
							if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
								org.survive(this);
							}
						} else {
							if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
								org.die(this);
								setColor(Color.GRAY);
							}
						}
					}
				}
				break;
			case DARKJADE:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Color.GRAY);
				} else {
					if ((org._healing > 0) && ((!_isenhanced) || (_segmentpretoucheffects == 3)) && (org.active)) {
						org._segColor[oseg] = Utils.ColorDARKGREEN;
				    	setColor(Color.GRAY);
				    	org._useextraeffects =true;
					} else {
						if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus))) && (org.active)) {
							if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
								org.survive(this);
							}
						} else {
							if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
								org.die(this);
								setColor(Color.GRAY);
							}
						}
					}
				}
				break;
			case WHITE:
				if ((org._isaplant) || (org._isaconsumer) || (org._isafungus) || (org._plagueversion > 0) || ((_isenhanced) && (_segmentpretoucheffects < 3))) {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus)))) {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.survive(this);
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				} else {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus)))) {
						if (org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
						    org.setColor(Color.MAGENTA);
						    setColor(Color.GRAY);
						} else {
							if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
								org.survive(this);
							}
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				}
				break;
			case BLUE:
				if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
					if (org._isenhanced) {
					    useEnergy(Utils.between((0.5 * Math.sqrt(org._m[oseg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, _energy));
						setColor(Utils.ColorDARKLILAC);
						if (_energy < Utils.tol) {
							die(org);
						}
					} else {
						setColor(Color.GRAY);
					}
					org.setColor(Color.BLUE);
				} else {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus))) && (org.active)) {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.survive(this);
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				}
				break;
			case SKY:
				if ((org._skyversion == 2) || (org._isaplant) || (!_isfrozen)) {
					if (org.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
						org._segColor[oseg] = Utils.ColorDEEPSKY;
						org._mphoto[oseg] = -20;
						org._useextraeffects = true;
						org.setColor(Utils.ColorDEEPSKY);
						setColor(Color.GRAY);
					} else {
						if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus)))) {
							if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
								org.survive(this);
							}
						} else {
							if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
								org.die(this);
								setColor(Color.GRAY);
							}
						}
					}
				}
				break;
			case DEEPSKY:
				if ((org._skyversion == 2) || (org._isaplant) || (!_isfrozen)) {
					if ((_isaconsumer) || (_isafungus)) {
						org.setColor(Utils.ColorDEEPSKY);
						setColor(Color.GRAY);
					} else {
						if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus)))) {
							if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
								org.survive(this);
							}
						} else {
							if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
								org.die(this);
								setColor(Color.GRAY);
							}
						}
					}
				}
				break;
			case OCHRE:
				if (org._isaplant) {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus))) && (org.active)) {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.survive(this);
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				}
				break;
			case LAVENDER:
				if (_altruist) {
		        break;
				} else {
					if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Color.GRAY);
					} else {
						if ((org._segColor[oseg].equals(Color.MAGENTA)) && (!org._isgray) && (org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION))) {
						    org.setColor(Color.MAGENTA);
						    setColor(Color.GRAY);
						} else {
							if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus)))) {
								if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
									org.survive(this);
								}
							} else {
								if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
									org.die(this);
									setColor(Color.GRAY);
								}
							}
						}
					}
				}
				break;
			case MINT:
			case ROSE:
			case MAGENTA:
				if (_altruist) {
	            break;
				} else {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Color.GRAY);
					} else {
						if ((org._segColor[oseg].equals(Color.MAGENTA)) && (!org._isgray) && (org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION))) {
						    org.setColor(Color.MAGENTA);
						    setColor(Color.GRAY);
						} else {
							if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus)))) {
								if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
									org.survive(this);
								}
							} else {
								if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
									org.die(this);
									setColor(Color.GRAY);
								}
							}
						}
					}
				}
				break;
			case RED:
				if (org._isenhanced) {
				break;
				} else {
					if ((org._healing > 0) && (!org._isgray)) {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.survive(this);
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				}
				break;
			case SILVER:
				if ((_nTotalKills >= org._nTotalKills) || ((!org._isaconsumer) && (!org._isafungus))) {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus)))) {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.survive(this);
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				}
				break;
			case SPIKE:
				if ((org._isaplant) || (org._isenhanced)) {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus)))) {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.survive(this);
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				}
				break;
			case OLIVE:
				if (org.useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
					if (_isaplant) {
					    _segColor[seg] = Utils.ColorBROKEN;
					} else {
						_segColor[seg] = Utils.ColorLIGHTBROWN;
					}
					_mphoto[seg] = -20.1;
					_isinjured =true;
					if ((org._healing == 0) && (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION))) {
						org._segColor[oseg] = Utils.ColorDARKOLIVE;
						org._mphoto[oseg] = -20;
						org._useextraeffects = true;
						org.setColor(Utils.ColorOLIVE);
						setColor(Color.GRAY);
					} else {
						org.setColor(Utils.ColorOLIVE);
					}
				} else {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus)))) {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.survive(this);
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				}
				break;
			case DARKOLIVE:
				if (org.useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
					if (_isaplant) {
					    _segColor[seg] = Utils.ColorBROKEN;
					} else {
						_segColor[seg] = Utils.ColorLIGHTBROWN;
					}
					_mphoto[seg] = -20.1;
					_isinjured =true;
					if ((org._healing == 0) && (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION))) {
						org._segColor[oseg] = Utils.ColorLIGHT_BLUE;
						org._mphoto[oseg] = -20;
						org.setColor(Utils.ColorOLIVE);
						setColor(Color.GRAY);
						if (!org._isaplant) {
							org._updateEffects = 2;
						}
						org._isinjured =true;
					} else {
						org.setColor(Utils.ColorOLIVE);
					}
				} else {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus)))) {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.survive(this);
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				}
				break;
			case VIOLET:
				if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus)))) {
					if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
						org.survive(this);
						org._segColor[oseg] = Utils.ColorDARKFIRE;
						org._useextraeffects = true;
						org._isinjured =true;
					}
				} else {
					if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
						org.die(this);
						setColor(Color.GRAY);
					}
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				} else {
					if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus))) && (org.active)) {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.survive(this);
						}
					} else {
						if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
							org.die(this);
							setColor(Color.GRAY);
						}
					}
				}
				break;
			case BARK:
				org._segColor[oseg] = Utils.ColorOLDBARK;
				if (org.active) {
					org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
					org._mphoto[oseg] = -0.5;
				}
				break;
			default:
				if ((org._healing > 0) && ((!org._isgray) || ((!org._isaconsumer) && (!org._isafungus))) && (org.active)) {
					if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
						org.survive(this);
					}
				} else {
					if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
						org.die(this);
						setColor(Color.GRAY);
					}
				}
			}
			break;
		}
		// Check if the other organism has died
		if (org.alive && org._energy < Utils.tol) {
			org.die(this);
		}
	}
	/**
	 * Applies the hostile effects produced by two touching segments.
	 * Version for segments, that cannot drain energy
	 *
	 * @param org  The organism which is touching.
	 * @param seg  Index of this organism's segment.
	 * @param oseg  Index of the other organism's segment.
	 */
	private final void touchEffects2(Organism org, int seg, int oseg) {
		switch (getTypeColor(_segColor[seg])) {
		case WHITE:
			// White segment: try to infect the other organism
			switch (getTypeColor(org._segColor[oseg])) {
			case WHITE:
			case GREENBROWN:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._isaplant) || (org._isaconsumer)) {
						if (_isaplant) {
							if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
						        org.setColor(Utils.ColorBLOND);
						        setColor(Color.WHITE);
					    	}
						} else {
					    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
					            org.setColor(Utils.ColorLIGHTBROWN);
					            setColor(Color.WHITE);
					        }
						}
					} else {
						if ((org._indigo > 0) && (!org._isafungus) && (!_hasgoodvision)) {
							if (_isaplant) {
								if (useEnergy((Utils.WHITE_ENERGY_CONSUMPTION + 0.01)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
						    	    org.setColor(Utils.ColorINDIGO);
						    	    setColor(Color.WHITE);
								}
						    } else {
						    	if ((_indigo == 0) || (_isaconsumer) || (_isafungus)) {
						    		if (useEnergy((Utils.VIRUS_ENERGY_CONSUMPTION + 0.1)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
						    			org.setColor(Utils.ColorINDIGO);
							    	    setColor(Color.WHITE);
						    		}
						    	}
						    }
						}
					}
				}
				break;
			case SILVER:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._isaplant) || (org._isaconsumer) || ((org._isafungus) && (org._nTotalKills > 0))) {
						if ((org._isenhanced) || (_nTotalInfected >= org._nTotalInfected)) {
							if (_isaplant) {
								if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorBLOND);
							        setColor(Color.WHITE);
						    	}
						    } else {
						    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
						            org.setColor(Utils.ColorLIGHTBROWN);
						            setColor(Color.WHITE);
						        }
							}
						}
					} else {
						if ((org._indigo > 0) && (!org._isafungus) && (!_hasgoodvision)) {
							if (_isaplant) {
								if (useEnergy((Utils.WHITE_ENERGY_CONSUMPTION + 0.01)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
						    	    org.setColor(Utils.ColorINDIGO);
						    	    setColor(Color.WHITE);
								}
						    } else {
						    	if ((_indigo == 0) || (_isaconsumer) || (_isafungus)) {
						    		if (useEnergy((Utils.VIRUS_ENERGY_CONSUMPTION + 0.1)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
						    			org.setColor(Utils.ColorINDIGO);
							    	    setColor(Color.WHITE);
						    		}
						    	}
						    }
						}
					}
				}
				break;
			case PLAGUE:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._isaplant) || (org._isaconsumer)) {
						if (_isaplant) {
							if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
						        org.setColor(Utils.ColorBLOND);
						        setColor(Color.WHITE);
						        if ((!org._isinfectious) && (!org._isaconsumer) && (!org._isafungus)) {
									org._infectedReproduceEnergy = 4;
								}
					    	}
						} else {
					    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
					            org.setColor(Utils.ColorLIGHTBROWN);
					            setColor(Color.WHITE);
					            if ((!org._isinfectious) && (!org._isaconsumer) && (!org._isafungus)) {
									org._infectedReproduceEnergy = 4;
								}
					        }
						}
					} else {
						if ((org._indigo > 0) && (!org._isafungus) && (!_hasgoodvision)) {
							if (_isaplant) {
								if (useEnergy((Utils.WHITE_ENERGY_CONSUMPTION + 0.01)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
						    	    org.setColor(Utils.ColorINDIGO);
						    	    setColor(Color.WHITE);
								}
						    } else {
						    	if ((_indigo == 0) || (_isaconsumer) || (_isafungus)) {
						    		if (useEnergy((Utils.VIRUS_ENERGY_CONSUMPTION + 0.1)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
						    			org.setColor(Utils.ColorINDIGO);
							    	    setColor(Color.WHITE);
						    		}
						    	}
						    }
						}
					}
				}
				break;
			case INDIGO:
				if (!_hasgoodvision) {
					if (_isaplant) {
						if (useEnergy((Utils.WHITE_ENERGY_CONSUMPTION + 0.01)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
				    	    org.setColor(Utils.ColorINDIGO);
				    	    setColor(Color.WHITE);
						}
				    } else {
				    	if ((_indigo == 0) || (_isaconsumer) || (_isafungus)) {
				    		if (useEnergy((Utils.VIRUS_ENERGY_CONSUMPTION + 0.1)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
				    			org.setColor(Utils.ColorINDIGO);
					    	    setColor(Color.WHITE);
				    		}
				    	}
				    }
				}
				if ((org._antiviral > 0) && (_plagueversion > 0)) {
	    			virusneutralized();
				}
				break;
			case GREEN:
			case GRASS:
			case FOREST:
			case SPRING:
			case SUMMER:
            case WINTER:
			case LIME:
			case LEAF:
			case C4:
			case JADE:
			case DARKJADE:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						if (org._isonlyc4 != 2) {
							org._hasdodged =true;
						} else {
							if (((!org._isauburn) || (_hasgoodvision)) && (((!_isaplant) && (org._indigo == 0)) || (org._framesColor > 8))) {
								org._hasdodged =true;
							}
						}
						org.setColor(Utils.ColorTEAL);
				        setColor(Color.WHITE);
					} else {
						if (org._jadefactor > 1) {
				    		if (org._segColor[oseg].equals(Utils.ColorJADE)) {
				    			org._segColor[oseg] = Utils.ColorDARKJADE;
				    		} else {
				    			org._segColor[oseg] = Utils.ColorDARKGREEN;
				    		}
				    		setColor(Color.WHITE);
							org._hasdodged =true;
							org._useextraeffects =true;
							if ((org._antiviral > 0) && (_plagueversion > 0)) {
				    			virusneutralized();
							}
				    	} else {
				    		if (_isaplant) {
								if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorBLOND);
							        setColor(Color.WHITE);
						    	}
						    } else {
						    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorLIGHTBROWN);
						            setColor(Color.WHITE);
						        }
							}
				    	}
					}
				}
				break;
			case DARKGREEN:
			case PURPLE:
			case TEAL:
			case CYAN:
			case YELLOW:
			case AUBURN:
			case BLOND:
			case FLOWER:
			case DARKGRAY:
			case GOLD:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._isaplant) || (org._isaconsumer)) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							if (org._isonlyc4 != 2) {
								org._hasdodged =true;
							} else {
								if (((!org._isauburn) || (_hasgoodvision)) && (((!_isaplant) && (org._indigo == 0)) || (org._framesColor > 8))) {
									org._hasdodged =true;
								}
							}
							org.setColor(Utils.ColorTEAL);
					        setColor(Color.WHITE);
						} else {
							if (_isaplant) {
								if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorBLOND);
							        setColor(Color.WHITE);
						    	}
						    } else {
						    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorLIGHTBROWN);
						            setColor(Color.WHITE);
						        }
							}
						}
					}
				}
				break;
			case SPORE:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._isaplant) || (org._isaconsumer)) {
					    if ((org._sporeversion >= 3) && (org._isaplant)) {
							if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
								if (org._isonlyc4 != 2) {
									org._hasdodged =true;
								} else {
									if (((!org._isauburn) || (_hasgoodvision)) && (((!_isaplant) && (org._indigo == 0)) || (org._framesColor > 8))) {
										org._hasdodged =true;
									}
								}
								org.setColor(Utils.ColorTEAL);
						        setColor(Color.WHITE);
							} else {
								if (_isaplant) {
									if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
								        org.infectedBy(this);
								        org.setColor(Utils.ColorBLOND);
								        setColor(Color.WHITE);
								        if ((!org._isaconsumer) && (!org._isafungus)) {
											org._infectedReproduceEnergy = -org._infectedReproduceEnergy;
										}
							    	}
							    } else {
							    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
								        org.infectedBy(this);
							            org.setColor(Utils.ColorLIGHTBROWN);
							            setColor(Color.WHITE);
							            if ((!org._isaconsumer) && (!org._isafungus)) {
											org._infectedReproduceEnergy = -org._infectedReproduceEnergy;
										}
							        }
								}
							}
						} else {
							setColor(Color.WHITE);
							org.setColor(Utils.ColorSPORE);
						}
					}
				}
				break;
			case BLUE:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._isaplant) || (org._isaconsumer)) {
				        if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
				        	if (org._isenhanced) {
							    useEnergy(Utils.between((0.5 * Math.sqrt(org._m[oseg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, _energy));
								setColor(Utils.ColorDARKLILAC);
								if (_energy < Utils.tol) {
									die(org);
								}
							} else {
								setColor(Color.WHITE);
							}
					        org.setColor(Color.BLUE);
					        org._hasdodged =true;
				        } else {
				        	if (_isaplant) {
				        		if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorBLOND);
							        setColor(Color.WHITE);
						    	}
						    } else {
						    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
						            org.setColor(Utils.ColorLIGHTBROWN);
						            setColor(Color.WHITE);
						        }
							}
						}
					}
				}
				break;
			case LIGHT_BLUE:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._isaplant) || (org._isaconsumer)) {
						if ((!_isenhanced) && (_isaplant) && (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION))) {
						    setColor(Color.WHITE);
						    org.setColor(Utils.ColorLIGHT_BLUE);
						    org._hasdodged =true;
					    } else {
					    	if (_isaplant) {
					    		if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorBLOND);
							        setColor(Color.WHITE);
						    	}
						    } else {
								if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
								    org.infectedBy(this);
							        org.setColor(Utils.ColorLIGHTBROWN);
							        setColor(Color.WHITE);
							    }
							}
						}
					}
				}
				break;
			case SKY:
				if ((((org._skyversion == 2) || (_isaplant) || (_isaconsumer) || (_isafungus)) && ((!_isfrozen) || (_healing > 0)))
						|| ((_isenhanced) && (org._isaplant) && (!_isakiller) && (org._antiviral == 0))) {
					if ((org._isaplant) || ((org._isaconsumer) && (org._skyversion == 2))) {
						if (org.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
							org._segColor[oseg] = Utils.ColorDEEPSKY;
							org._mphoto[oseg] = -20;
							org._useextraeffects = true;
							setColor(Color.WHITE);
							org.setColor(Utils.ColorDEEPSKY);
							org._hasdodged =true;
				        } else {
				        	if (org._infectedGeneticCode != _geneticCode) {
				        		if (_isaplant) {
				        			if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
								        org.infectedBy(this);
								        org.setColor(Utils.ColorBLOND);
								        setColor(Color.WHITE);
							    	}
							    } else {
							    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
								        org.infectedBy(this);
							            org.setColor(Utils.ColorLIGHTBROWN);
							            setColor(Color.WHITE);
							        }
								}
							}
						}
					}
				}
				break;
			case DEEPSKY:
				if ((((org._skyversion == 2) || (_isaplant) || (_isaconsumer) || (_isafungus)) && ((!_isfrozen) || (_healing > 0)))
						|| ((_isenhanced) && (org._isaplant) && (!_isakiller) && (org._antiviral == 0))) {
					if ((org._isaplant) || ((org._isaconsumer) && (org._skyversion == 2))) {
						if ((_isenhanced) && (org._isaplant) && (!_isakiller) && (org._antiviral == 0)) {
							if (org._infectedGeneticCode != _geneticCode) {
								if (_isaplant) {
									if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
								        org.infectedBy(this);
								        org.setColor(Utils.ColorBLOND);
								        setColor(Color.WHITE);
							    	}
							    } else {
							    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
								        org.infectedBy(this);
							            org.setColor(Utils.ColorLIGHTBROWN);
							            setColor(Color.WHITE);
							        }
								}
							}
						} else {
							setColor(Color.WHITE);
							org.setColor(Utils.ColorDEEPSKY);
							org._hasdodged =true;
						}
					}
				}
				break;
			case OLIVE:
			case DARKOLIVE:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._isaplant) || (org._isaconsumer)) {
					    if (_isaplant) {
					    	if ((_isenhanced) && (org._isaplant) && (!_isakiller) && (org._antiviral == 0)) {
					    		if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorBLOND);
							        setColor(Color.WHITE);
						    	}
					    	}
					    } else {
					    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
					            org.setColor(Utils.ColorLIGHTBROWN);
					            setColor(Color.WHITE);
					        }
						}
					}
				}
				break;
			case VIOLET:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._isaplant) || (org._isaconsumer)) {
					    if (_isaplant) {
					    	if ((org._isaconsumer) || ((_isenhanced) && (!_isakiller) && (org._antiviral == 0))) {
					    		if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorBLOND);
							        setColor(Color.WHITE);
						    	}
					    	}
					    } else {
					    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
					            org.setColor(Utils.ColorLIGHTBROWN);
					            setColor(Color.WHITE);
					        }
						}
					}
				}
				break;
			case ICE:
			case DEADBARK:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._isaplant) || (org._isaconsumer)) {
					    if (_isaplant) {
					    	if ((org._isaconsumer) || ((_isenhanced) && (!_isakiller))) {
					    		if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorBLOND);
							        setColor(Color.WHITE);
						    	}
					    	}
					    } else {
					    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
					            org.setColor(Utils.ColorLIGHTBROWN);
					            setColor(Color.WHITE);
					        }
						}
					}
				}
				break;
			case LAVENDER:
				if ((org._isaplant) || (org._isaconsumer)) {
					if ((_plagueversion > 0) || (_fallowversion > 0) || (org._isenhanced)) {
						if (org._isenhanced) {
							useEnergy(Utils.between((1/Utils.LAVENDER_ENERGY_CONSUMPTION), 0, _energy));
							if (org._lavender < Utils.LAVENDER_SHIELD) {
								org._lavender += 1000;
								if (org._lavender >= Utils.LAVENDER_SHIELD) {
									org._lavender = Utils.LAVENDER_SHIELD;
								}
							}
						} else {
							useEnergy(Utils.between((0.2/Utils.LAVENDER_ENERGY_CONSUMPTION), 0, _energy));
							if (org._lavender < Utils.LAVENDER_SHIELD) {
								org._lavender += 200;
								if (org._lavender >= Utils.LAVENDER_SHIELD) {
									org._lavender = Utils.LAVENDER_SHIELD;
								}
							}
						}
					} else {
						useEnergy(Utils.between((0.02/Utils.LAVENDER_ENERGY_CONSUMPTION), 0, _energy));
						if (org._lavender < Utils.LAVENDER_SHIELD) {
							org._lavender += 20;
							if (org._lavender >= Utils.LAVENDER_SHIELD) {
								org._lavender = Utils.LAVENDER_SHIELD;
							}
						}
					}
					if (org._infectedGeneticCode != null) {
		    			org._infectedGeneticCode = null;
		    			org._savedGeneticCode = null;
		    		}
					// The other organism will be shown in lavender
					org.setColor(Utils.ColorLAVENDER);
					// This organism will be shown in deadbark
					setColor(Utils.ColorDEADBARK);
					org._hasdodged =true;
					if (_energy < Utils.tol) {
						die(org);
					}
				}
				break;
			case MAGENTA:
			case ROSE:
				if ((!_altruist) && (org._infectedGeneticCode != _geneticCode)) {
					if ((org._isaplant) || (org._isaconsumer)) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							if (org._isonlyc4 != 2) {
								org._hasdodged =true;
							} else {
								if (((!org._isauburn) || (_hasgoodvision)) && (((!_isaplant) && (org._indigo == 0)) || (org._framesColor > 8))) {
									org._hasdodged =true;
								}
							}
							org.setColor(Utils.ColorTEAL);
					        setColor(Color.WHITE);
						} else {
							if (_isaplant) {
								if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorBLOND);
							        setColor(Color.WHITE);
						    	}
						    } else {
						    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
						            org.setColor(Utils.ColorLIGHTBROWN);
						            setColor(Color.WHITE);
						        }
							}
						}
					}
				}
				break;
			case PINK:
				if ((org._modifiespink) || ((org._timeToReproduce > org._timeToReproduceMax) && ((org._reproducelate == 0) || (org._isinfectious)))) {
				    if (org._infectedGeneticCode != _geneticCode) {
					    if (_isaplant) {
					    	if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
						        org.setColor(Utils.ColorBLOND);
						        setColor(Color.WHITE);
					    	}
					    } else {
					    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
					            org.setColor(Utils.ColorLIGHTBROWN);
					            setColor(Color.WHITE);
					        }
						}
					}
				} else {
					if ((_isauburn) && (!_isaplant) && (!_isaconsumer) && (!_isafungus)) {
						if (org._infectedGeneticCode != _geneticCode) {
							if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
					            org.setColor(Utils.ColorLIGHTBROWN);
					            setColor(Color.WHITE);
					        }
						}
					}
				}
				break;
			case CREAM:
				if (org._isaplant) {
				    if (org._infectedGeneticCode != _geneticCode) {
					    if (_isaplant) {
					    	if ((_isenhanced) && (!_isakiller) && (org._indigo == 0)) {
					    		if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorBLOND);
							        setColor(Color.WHITE);
						    	}
						    }
					    } else {
					    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
					            org.setColor(Utils.ColorLIGHTBROWN);
					            setColor(Color.WHITE);
					        }
						}
					}
				} else {
					if (_hasgoodvision) {
						if ((!_isaplant) && (!_isaconsumer) && (!_isafungus)) {
							if (org._infectedGeneticCode != _geneticCode) {
								if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
						            org.setColor(Utils.ColorLIGHTBROWN);
						            setColor(Color.WHITE);
						        }
							}
						}
					}
				}
				break;
			case SPIKE:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._isaplant) || (org._isenhanced)) {
					    if (_isaplant) {
					    	if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
						        org.setColor(Utils.ColorBLOND);
						        setColor(Color.WHITE);
					    	}
					    } else {
					    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
					            org.setColor(Utils.ColorLIGHTBROWN);
					            setColor(Color.WHITE);
					        }
						}
					}
				}
				break;
			case SPIKEPOINT:
				if ((_isenhanced) && (!_isakiller)) {
					if (((org._isaplant) && (org._antiviral == 0)) || (org._isenhanced)) {
					    if (org._infectedGeneticCode != _geneticCode) {
						    if (_isaplant) {
						    	if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorBLOND);
							        setColor(Color.WHITE);
						    	}
						    } else {
						    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
						            org.setColor(Utils.ColorLIGHTBROWN);
						            setColor(Color.WHITE);
						        }
							}
						}
					}
				}
				break;
			case CORAL:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._isaplant) || (org._isaconsumer)) {
					    if (_isaplant) {
					    	if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
						        org.setColor(Utils.ColorBLOND);
						        setColor(Color.WHITE);
					    	}
					    } else {
					    	if ((_isaconsumer) || (!_isblond)) {
					    		if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
						            org.setColor(Utils.ColorLIGHTBROWN);
						            setColor(Color.WHITE);
						        }
					    	}
						}
					}
				}
				break;
			case FALLOW:
				if ((_isenhanced) && (org._isaplant) && (!_isakiller) && (org._antiviral == 0)) {
					if ((!_isaplant) && (!_isaconsumer) && (!_isafungus)) {
					    if (org._infectedGeneticCode != _geneticCode) {
						    if ((org._isaplant) || (org._isaconsumer)) {
								if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
								    org.infectedBy(this);
							        org.setColor(Utils.ColorLIGHTBROWN);
							        setColor(Color.WHITE);
								}
							}
						}
					}
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				} else {
					if (org._infectedGeneticCode != _geneticCode) {
						if ((org._isaplant) || (org._isaconsumer)) {
						    if (_isaplant) {
						    	if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
							        org.setColor(Utils.ColorBLOND);
							        setColor(Color.WHITE);
						    	}
						    } else {
						    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
							        org.infectedBy(this);
						            org.setColor(Utils.ColorLIGHTBROWN);
						            setColor(Color.WHITE);
						        }
							}
						}
					}
				}
				break;
			case BARK:
				org._segColor[oseg] = Utils.ColorOLDBARK;
				if (org.active) {
					org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
					org._mphoto[oseg] = -0.5;
				}
				break;
			case OLDBARK:
			case OCHRE:
			case MINT:
			case BROWN:
				break;
			default:
				if (org._infectedGeneticCode != _geneticCode) {
					if ((org._isaplant) || (org._isaconsumer)) {
					    if (_isaplant) {
					    	if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
						        org.setColor(Utils.ColorBLOND);
						        setColor(Color.WHITE);
					    	}
					    } else {
					    	if (useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION)) {
						        org.infectedBy(this);
					            org.setColor(Utils.ColorLIGHTBROWN);
					            setColor(Color.WHITE);
					        }
						}
					}
				}
			}
			break;
		case FALLOW:
			// Fallow segment: Inhibit and destroy other organisms reproduction system but let them reproduce if they are infected by this organism.
			if (org._hasdodged == false) {
				if ((org._isonlyc4 != 2) || (org._reproducelate == 0) || (_hasgoodvision)) {
					org._hasdodged =true;
				}
		    }
			switch (getTypeColor(org._segColor[oseg])) {
			case FALLOW:
				if ((org._isakiller) || (org._isblond)) {
					if ((org._fallowversion != _fallowversion) || (org._isinfectious) || ((!_isaconsumer) && (!_isafungus)) || (_isenhanced)) {
				    	if ((org._isaplant) || (org._isaconsumer) || ((org._isafungus) && (((_fallowversion == 2) && (org._antiviral == 0)) || (!_isaplant)))) {
							if (org._lavender > 0) {
								if ((_isenhanced) && (!_isinfectious)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorFALLOW);
							}
							if (org._lavender <= 0) {
								if (org._infectedGeneticCode != _geneticCode) {
									if (_fallowversion <= 2) {
										if ((_fallowversion == 2) || (org._sporeversion > 0)) {
											if (org._timeToReproduce < org._timeToReproduceMax + (int)(10 * _m[seg])) {
												if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
													setColor(Utils.ColorFALLOW);
													org.setColor(Utils.ColorFLOWER);
													org._timeToReproduce = org._timeToReproduceMax + (int)(10 * _m[seg]);
												}
											}
										}
									} else {
										if ((org._reproducelate == 0) || (org._isinfectious)) {
											if (_fallowversion == 4) {
												if (org._timeToReproduce < org._timeToReproduceMax + (int)(6 * _m[seg])) {
													if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
														setColor(Utils.ColorFALLOW);
														org.setColor(Utils.ColorFLOWER);
														org._timeToReproduce = org._timeToReproduceMax + (int)(6 * _m[seg]);
													}
												}
											} else {
												if (_isinfectious) {
													if (org._timeToReproduce < org._timeToReproduceMax) {
														if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
															setColor(Utils.ColorFALLOW);
															org.setColor(Utils.ColorFLOWER);
															org._timeToReproduce = org._timeToReproduceMax;
														}
													}
												} else {
													if (org._timeToReproduce < org._timeToReproduceMax + (int)(_m[seg])) {
														if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
															setColor(Utils.ColorFALLOW);
															org.setColor(Utils.ColorFLOWER);
															org._timeToReproduce = org._timeToReproduceMax + (int)(_m[seg]);
														}
													}
												}
											}
										}
									}
								} else {
									if ((org._timeToReproduce > 4) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
										setColor(Utils.ColorFALLOW);
										org.setColor(Utils.ColorBLOND);
										org._timeToReproduce = 4;
									}
								}
								if (_fallowversion == 3) {
									if (_isinfectious) {
										if ((org._fallowinhibition < 2) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
											org._fallowinhibition = 2;
											setColor(Utils.ColorFALLOW);
											org.setColor(Utils.ColorFLOWER);
										}
									} else {
										if ((org._fallowinhibition < (int)((_m[seg]/2)+1)) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
											org._fallowinhibition = (int)((_m[seg]/2)+1);
											setColor(Utils.ColorFALLOW);
											org.setColor(Utils.ColorFLOWER);
										}
									}
								} else {
									if (_fallowversion == 4) {
										if ((org._isreproductive) && ((org._isaconsumer) || (org._isaplant)) && (org._antiviral == 0)) {
											org.inhibited(this);
										}
									} else {
										if (_fallowversion == 1) {
											if ((org._sporetime > 0) && (org._sporeversion > 0) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
												org._sporetime = -org._sporetime;
												setColor(Utils.ColorFALLOW);
												org.setColor(Utils.ColorFLOWER);
											}
										}
									}
								}
							}
						}
					}
				}
				break;
			case WHITE:
			case PURPLE:
			case TEAL:
			case CYAN:
			case YELLOW:
			case AUBURN:
			case INDIGO:
			case BLOND:
			case SPORE:
			case GOLD:
				if ((org._fallowversion != _fallowversion) || (org._isinfectious) || ((!_isaconsumer) && (!_isafungus)) || (_isenhanced)) {
					if ((org._isaplant) || (org._isaconsumer) || ((org._isafungus) && (((_fallowversion == 2) && (org._antiviral == 0)) || (!_isaplant)))) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorFALLOW);
						} else {
							if (org._lavender > 0) {
								if ((_isenhanced) && (!_isinfectious)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorFALLOW);
							}
							if (org._lavender <= 0) {
								if (org._infectedGeneticCode != _geneticCode) {
									if (_fallowversion <= 2) {
										if ((_fallowversion == 2) || (org._sporeversion > 0)) {
											if (org._timeToReproduce < org._timeToReproduceMax + (int)(10 * _m[seg])) {
												if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
													setColor(Utils.ColorFALLOW);
													org.setColor(Utils.ColorFLOWER);
													org._timeToReproduce = org._timeToReproduceMax + (int)(10 * _m[seg]);
												}
											}
										}
									} else {
										if ((org._reproducelate == 0) || (org._isinfectious)) {
											if (_fallowversion == 4) {
												if (org._timeToReproduce < org._timeToReproduceMax + (int)(6 * _m[seg])) {
													if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
														setColor(Utils.ColorFALLOW);
														org.setColor(Utils.ColorFLOWER);
														org._timeToReproduce = org._timeToReproduceMax + (int)(6 * _m[seg]);
													}
												}
											} else {
												if (_isinfectious) {
													if (org._timeToReproduce < org._timeToReproduceMax) {
														if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
															setColor(Utils.ColorFALLOW);
															org.setColor(Utils.ColorFLOWER);
															org._timeToReproduce = org._timeToReproduceMax;
														}
													}
												} else {
													if (org._timeToReproduce < org._timeToReproduceMax + (int)(_m[seg])) {
														if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
															setColor(Utils.ColorFALLOW);
															org.setColor(Utils.ColorFLOWER);
															org._timeToReproduce = org._timeToReproduceMax + (int)(_m[seg]);
														}
													}
												}
											}
										}
									}
								} else {
									if ((org._timeToReproduce > 4) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
										setColor(Utils.ColorFALLOW);
										org.setColor(Utils.ColorBLOND);
										org._timeToReproduce = 4;
									}
								}
								if (_fallowversion == 3) {
									if (_isinfectious) {
										if ((org._fallowinhibition < 2) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
											org._fallowinhibition = 2;
											setColor(Utils.ColorFALLOW);
											org.setColor(Utils.ColorFLOWER);
										}
									} else {
										if ((org._fallowinhibition < (int)((_m[seg]/2)+1)) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
											org._fallowinhibition = (int)((_m[seg]/2)+1);
											setColor(Utils.ColorFALLOW);
											org.setColor(Utils.ColorFLOWER);
										}
									}
								} else {
									if (_fallowversion == 4) {
										if ((org._isreproductive) && ((org._isaconsumer) || (org._isaplant))) {
											if (org._antiviral == 0) {
												org.inhibited(this);
											} else {
												switch (getTypeColor(org._segColor[oseg])) {
												case SPORE:
													if ((org._sporeversion > 0) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
														if (org._isaplant) {
														    org._segColor[oseg] = Utils.ColorBROKEN;
														} else {
															org._segColor[oseg] = Utils.ColorLIGHTBROWN;
														}
														org._mphoto[oseg] = -20;
														setColor(Utils.ColorFALLOW);
														org.segmentsRestoreEffects();
														org._updateEffects = 2;
													}
													break;
												case AUBURN:
												case INDIGO:
													if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
														if (org._isaplant) {
														    org._segColor[oseg] = Utils.ColorBROKEN;
														} else {
															org._segColor[oseg] = Utils.ColorLIGHTBROWN;
														}
														org._mphoto[oseg] = -20;
														setColor(Utils.ColorFALLOW);
														org.segmentsRestoreEffects();
														org._updateEffects = 2;
													}
													break;
												case YELLOW:
												case BLOND:
													if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
														if (org._isaplant) {
														    org._segColor[oseg] = Utils.ColorBROKEN;
														} else {
															org._segColor[oseg] = Utils.ColorLIGHTBROWN;
														}
														org._mphoto[oseg] = -20;
														setColor(Utils.ColorFALLOW);
														org.segmentsRestoreEffects();
													}
													break;
												}
											}
										}
									} else {
										if (_fallowversion == 1) {
											if ((org._sporetime > 0) && (org._sporeversion > 0) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
												org._sporetime = -org._sporetime;
												setColor(Utils.ColorFALLOW);
												org.setColor(Utils.ColorFLOWER);
											}
										}
									}
								}
							}
						}
					}
				}
				break;
			case JADE:
			case GREEN:
			case GRASS:
			case FOREST:
			case SPRING:
			case SUMMER:
            case WINTER:
			case LIME:
			case LEAF:
			case C4:
			case DARKJADE:
			case DARKGREEN:
				if ((org._jadefactor <= 1) || ((_fallowversion == 1) && (org._sporeversion > 0)) || (!org.active)) {
				    if ((org._fallowversion != _fallowversion) || (org._isinfectious) || ((!_isaconsumer) && (!_isafungus)) || (_isenhanced)) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorFALLOW);
						} else {
							if (org._lavender > 0) {
								if ((_isenhanced) && (!_isinfectious)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorFALLOW);
							}
							if (org._lavender <= 0) {
								if (org._infectedGeneticCode != _geneticCode) {
									if (_fallowversion <= 2) {
										if ((_fallowversion == 2) || (org._sporeversion > 0)) {
											if (org._timeToReproduce < org._timeToReproduceMax + (int)(10 * _m[seg])) {
												if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
													setColor(Utils.ColorFALLOW);
													org.setColor(Utils.ColorFLOWER);
													org._timeToReproduce = org._timeToReproduceMax + (int)(10 * _m[seg]);
												}
											}
										}
									} else {
										if ((org._reproducelate == 0) || (org._isinfectious)) {
											if (_fallowversion == 4) {
												if (org._timeToReproduce < org._timeToReproduceMax + (int)(6 * _m[seg])) {
													if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
														setColor(Utils.ColorFALLOW);
														org.setColor(Utils.ColorFLOWER);
														org._timeToReproduce = org._timeToReproduceMax + (int)(6 * _m[seg]);
													}
												}
											} else {
												if (_isinfectious) {
													if (org._timeToReproduce < org._timeToReproduceMax) {
														if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
															setColor(Utils.ColorFALLOW);
															org.setColor(Utils.ColorFLOWER);
															org._timeToReproduce = org._timeToReproduceMax;
														}
													}
												} else {
													if (org._timeToReproduce < org._timeToReproduceMax + (int)(_m[seg])) {
														if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
															setColor(Utils.ColorFALLOW);
															org.setColor(Utils.ColorFLOWER);
															org._timeToReproduce = org._timeToReproduceMax + (int)(_m[seg]);
														}
													}
												}
											}
										}
									}
								} else {
									if ((org._timeToReproduce > 4) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
										setColor(Utils.ColorFALLOW);
										org.setColor(Utils.ColorBLOND);
										org._timeToReproduce = 4;
									}
								}
								if (_fallowversion == 3) {
									if (_isinfectious) {
										if ((org._fallowinhibition < 2) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
											org._fallowinhibition = 2;
											setColor(Utils.ColorFALLOW);
											org.setColor(Utils.ColorFLOWER);
										}
									} else {
										if ((org._fallowinhibition < (int)((_m[seg]/2)+1)) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
											org._fallowinhibition = (int)((_m[seg]/2)+1);
											setColor(Utils.ColorFALLOW);
											org.setColor(Utils.ColorFLOWER);
										}
									}
								} else {
									if (_fallowversion == 4) {
										if ((org._isreproductive) && (org._antiviral == 0)) {
											org.inhibited(this);
										}
									} else {
										if (_fallowversion == 1) {
											if ((org._sporetime > 0) && (org._sporeversion > 0) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
												org._sporetime = -org._sporetime;
												setColor(Utils.ColorFALLOW);
												org.setColor(Utils.ColorFLOWER);
											}
										}
									}
								}
							}
						}
					}
				}
				break;
			case ROSE:
			case MAGENTA:
				if (_altruist) {
	                break;
				} else {
					if ((org._fallowversion != _fallowversion) || (org._isinfectious) || ((!_isaconsumer) && (!_isafungus)) || (_isenhanced)) {
						if ((org._isaplant) || (org._isaconsumer) || ((org._isafungus) && (((_fallowversion == 2) && (org._antiviral == 0)) || (!_isaplant)))) {
							if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
								org.setColor(Utils.ColorTEAL);
								setColor(Utils.ColorFALLOW);
							} else {
								if (org._lavender > 0) {
									if ((_isenhanced) && (!_isinfectious)) {
										org.weaklavendershield();
									} else {
										org.lavendershield();
									}
									setColor(Utils.ColorFALLOW);
								}
								if (org._lavender <= 0) {
									if ((org._segColor[oseg].equals(Color.MAGENTA)) && (org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION))) {
									    org.setColor(Color.MAGENTA);
									    setColor(Utils.ColorFALLOW);
									} else {
										if (org._infectedGeneticCode != _geneticCode) {
											if (_fallowversion <= 2) {
												if ((_fallowversion == 2) || (org._sporeversion > 0)) {
													if (org._timeToReproduce < org._timeToReproduceMax + (int)(10 * _m[seg])) {
														if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
															setColor(Utils.ColorFALLOW);
															org.setColor(Utils.ColorFLOWER);
															org._timeToReproduce = org._timeToReproduceMax + (int)(10 * _m[seg]);
														}
													}
												}
											} else {
												if ((org._reproducelate == 0) || (org._isinfectious)) {
													if (_fallowversion == 4) {
														if (org._timeToReproduce < org._timeToReproduceMax + (int)(6 * _m[seg])) {
															if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
																setColor(Utils.ColorFALLOW);
																org.setColor(Utils.ColorFLOWER);
																org._timeToReproduce = org._timeToReproduceMax + (int)(6 * _m[seg]);
															}
														}
													} else {
														if (_isinfectious) {
															if (org._timeToReproduce < org._timeToReproduceMax) {
																if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
																	setColor(Utils.ColorFALLOW);
																	org.setColor(Utils.ColorFLOWER);
																	org._timeToReproduce = org._timeToReproduceMax;
																}
															}
														} else {
															if (org._timeToReproduce < org._timeToReproduceMax + (int)(_m[seg])) {
																if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
																	setColor(Utils.ColorFALLOW);
																	org.setColor(Utils.ColorFLOWER);
																	org._timeToReproduce = org._timeToReproduceMax + (int)(_m[seg]);
																}
															}
														}
													}
												}
											}
										} else {
											if ((org._timeToReproduce > 4) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
												setColor(Utils.ColorFALLOW);
												org.setColor(Utils.ColorBLOND);
												org._timeToReproduce = 4;
											}
										}
										if (_fallowversion == 3) {
											if (_isinfectious) {
												if ((org._fallowinhibition < 2) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
													org._fallowinhibition = 2;
													setColor(Utils.ColorFALLOW);
													org.setColor(Utils.ColorFLOWER);
												}
											} else {
												if ((org._fallowinhibition < (int)((_m[seg]/2)+1)) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
													org._fallowinhibition = (int)((_m[seg]/2)+1);
													setColor(Utils.ColorFALLOW);
													org.setColor(Utils.ColorFLOWER);
												}
											}
										} else {
											if (_fallowversion == 4) {
												if ((org._isreproductive) && ((org._isaconsumer) || (org._isaplant)) && (org._antiviral == 0)) {
													org.inhibited(this);
												}
											} else {
												if (_fallowversion == 1) {
													if ((org._sporetime > 0) && (org._sporeversion > 0) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
														org._sporetime = -org._sporetime;
														setColor(Utils.ColorFALLOW);
														org.setColor(Utils.ColorFLOWER);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				break;
			case BLUE:
				if ((org._fallowversion != _fallowversion) || (org._isinfectious) || ((!_isaconsumer) && (!_isafungus)) || (_isenhanced)) {
					if ((org._isaplant) || (org._isaconsumer) || ((org._isafungus) && (((_fallowversion == 2) && (org._antiviral == 0)) || (!_isaplant)))) {
						if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
	    					setColor(Utils.ColorFALLOW);
	    					org.setColor(Color.BLUE);
	    				} else {
	    					if (org._lavender > 0) {
	    						if ((_isenhanced) && (!_isinfectious)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorFALLOW);
							}
							if (org._lavender <= 0) {
								if (org._infectedGeneticCode != _geneticCode) {
									if (_fallowversion <= 2) {
										if ((_fallowversion == 2) || (org._sporeversion > 0)) {
											if (org._timeToReproduce < org._timeToReproduceMax + (int)(10 * _m[seg])) {
												if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
													setColor(Utils.ColorFALLOW);
													org.setColor(Utils.ColorFLOWER);
													org._timeToReproduce = org._timeToReproduceMax + (int)(10 * _m[seg]);
												}
											}
										}
									} else {
										if ((org._reproducelate == 0) || (org._isinfectious)) {
											if (_fallowversion == 4) {
												if (org._timeToReproduce < org._timeToReproduceMax + (int)(6 * _m[seg])) {
													if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
														setColor(Utils.ColorFALLOW);
														org.setColor(Utils.ColorFLOWER);
														org._timeToReproduce = org._timeToReproduceMax + (int)(6 * _m[seg]);
													}
												}
											} else {
												if (_isinfectious) {
													if (org._timeToReproduce < org._timeToReproduceMax) {
														if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
															setColor(Utils.ColorFALLOW);
															org.setColor(Utils.ColorFLOWER);
															org._timeToReproduce = org._timeToReproduceMax;
														}
													}
												} else {
													if (org._timeToReproduce < org._timeToReproduceMax + (int)(_m[seg])) {
														if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
															setColor(Utils.ColorFALLOW);
															org.setColor(Utils.ColorFLOWER);
															org._timeToReproduce = org._timeToReproduceMax + (int)(_m[seg]);
														}
													}
												}
											}
										}
									}
								} else {
									if ((org._timeToReproduce > 4) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
										setColor(Utils.ColorFALLOW);
										org.setColor(Utils.ColorBLOND);
										org._timeToReproduce = 4;
									}
								}
								if (_fallowversion == 3) {
									if (_isinfectious) {
										if ((org._fallowinhibition < 2) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
											org._fallowinhibition = 2;
											setColor(Utils.ColorFALLOW);
											org.setColor(Utils.ColorFLOWER);
										}
									} else {
										if ((org._fallowinhibition < (int)((_m[seg]/2)+1)) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
											org._fallowinhibition = (int)((_m[seg]/2)+1);
											setColor(Utils.ColorFALLOW);
											org.setColor(Utils.ColorFLOWER);
										}
									}
								} else {
									if (_fallowversion == 4) {
										if ((org._isreproductive) && ((org._isaconsumer) || (org._isaplant)) && (org._antiviral == 0)) {
											org.inhibited(this);
										}
									} else {
										if (_fallowversion == 1) {
											if ((org._sporetime > 0) && (org._sporeversion > 0) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
												org._sporetime = -org._sporetime;
												setColor(Utils.ColorFALLOW);
												org.setColor(Utils.ColorFLOWER);
											}
										}
									}
								}
							}
	    				}
					}
				}
				break;
			case RED:
			case SPIKEPOINT:
				if ((org._fallowversion != _fallowversion) || (org._isinfectious) || ((!_isaconsumer) && (!_isafungus)) || (_isenhanced)) {
					if ((((_isenhanced) && (org._createlavender == 0)) || (!_isaplant) || (_fallowversion == 1)) && (org._isaconsumer)) {
						if (org._lavender > 0) {
							if ((_isenhanced) && (!_isinfectious)) {
								org.weaklavendershield();
							} else {
								org.lavendershield();
							}
							setColor(Utils.ColorFALLOW);
						}
						if (org._lavender <= 0) {
							if (org._infectedGeneticCode != _geneticCode) {
								if (_fallowversion <= 2) {
									if ((_fallowversion == 2) || (org._sporeversion > 0)) {
										if (org._timeToReproduce < org._timeToReproduceMax + (int)(10 * _m[seg])) {
											if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
												setColor(Utils.ColorFALLOW);
												org.setColor(Utils.ColorFLOWER);
												org._timeToReproduce = org._timeToReproduceMax + (int)(10 * _m[seg]);
											}
										}
									}
								} else {
									if ((org._reproducelate == 0) || (org._isinfectious)) {
										if (_fallowversion == 4) {
											if (org._timeToReproduce < org._timeToReproduceMax + (int)(6 * _m[seg])) {
												if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
													setColor(Utils.ColorFALLOW);
													org.setColor(Utils.ColorFLOWER);
													org._timeToReproduce = org._timeToReproduceMax + (int)(6 * _m[seg]);
												}
											}
										} else {
											if (_isinfectious) {
												if (org._timeToReproduce < org._timeToReproduceMax) {
													if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
														setColor(Utils.ColorFALLOW);
														org.setColor(Utils.ColorFLOWER);
														org._timeToReproduce = org._timeToReproduceMax;
													}
												}
											} else {
												if (org._timeToReproduce < org._timeToReproduceMax + (int)(_m[seg])) {
													if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
														setColor(Utils.ColorFALLOW);
														org.setColor(Utils.ColorFLOWER);
														org._timeToReproduce = org._timeToReproduceMax + (int)(_m[seg]);
													}
												}
											}
										}
									}
								}
							} else {
								if ((org._timeToReproduce > 4) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
									setColor(Utils.ColorFALLOW);
									org.setColor(Utils.ColorBLOND);
									org._timeToReproduce = 4;
								}
							}
							if (_fallowversion == 3) {
								if (_isinfectious) {
									if ((org._fallowinhibition < 2) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
										org._fallowinhibition = 2;
										setColor(Utils.ColorFALLOW);
										org.setColor(Utils.ColorFLOWER);
									}
								} else {
									if ((org._fallowinhibition < (int)((_m[seg]/2)+1)) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
										org._fallowinhibition = (int)((_m[seg]/2)+1);
										setColor(Utils.ColorFALLOW);
										org.setColor(Utils.ColorFLOWER);
									}
								}
							} else {
								if (_fallowversion == 4) {
									if ((org._isreproductive) && (org._antiviral == 0)) {
										org.inhibited(this);
									}
								} else {
									if (_fallowversion == 1) {
										if ((org._sporetime > 0) && (org._sporeversion > 0) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
											org._sporetime = -org._sporetime;
											setColor(Utils.ColorFALLOW);
											org.setColor(Utils.ColorFLOWER);
										}
									}
								}
							}
						}
					}
				}
			    break;
			case LAVENDER:
				if ((org._isaplant) || (org._isaconsumer) || ((org._isafungus) && (((_fallowversion == 2) && (org._antiviral == 0)) || (!_isaplant)))) {
					if (org._isenhanced) {
						useEnergy(Utils.between((1/Utils.LAVENDER_ENERGY_CONSUMPTION), 0, _energy));
						if (org._lavender < Utils.LAVENDER_SHIELD) {
							org._lavender += 1000;
							if (org._lavender >= Utils.LAVENDER_SHIELD) {
								org._lavender = Utils.LAVENDER_SHIELD;
							}
						}
					} else {
						useEnergy(Utils.between((0.2/Utils.LAVENDER_ENERGY_CONSUMPTION), 0, _energy));
						if (org._lavender < Utils.LAVENDER_SHIELD) {
							org._lavender += 200;
							if (org._lavender >= Utils.LAVENDER_SHIELD) {
								org._lavender = Utils.LAVENDER_SHIELD;
							}
						}
					}
					// The other organism will be shown in lavender
					org.setColor(Utils.ColorLAVENDER);
					// This organism will be shown in deadbark
					setColor(Utils.ColorDEADBARK);
					if (_energy < Utils.tol) {
						die(org);
					}
				}
				break;
			case MINT:
				if (_isaplant) {
					if (org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
					    _segColor[seg] = Utils.ColorBROKEN;
					    _mphoto[seg] = -20;
					    org.setColor(Utils.ColorMINT);
					    _isinjured =true;
					    _updateEffects = 2;
					}
				} else {
					if ((_isaconsumer) || (_iscoral)) {
						if (org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
						    _segColor[seg] = Utils.ColorLIGHTBROWN;
						    _mphoto[seg] = -20;
						    org.setColor(Utils.ColorMINT);
						    _isinjured =true;
						    _updateEffects = 2;
						}
					} else {
						if ((_antiviral == 0) || (org._isaplant) || (org._isaconsumer) || (org._isafungus) || (org._iscoral)) {
							if (org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
							    _segColor[seg] = Utils.ColorLIGHTBROWN;
							    _mphoto[seg] = -20;
							    org.setColor(Utils.ColorMINT);
							    _isinjured =true;
							    _updateEffects = 2;
					    	}
					    }
					}
				}
				break;
			case CREAM:
			case OLIVE:
			case FLOWER:
			case DARKGRAY:
			case CORAL:
			case FRUIT:
				break;
			case BROWN:
			case VISION:
				if ((org.alive) && (!org.active)) {
					if ((_fallowversion != 2) || (!_isinfectious)) {
						if (org._lavender > 0) {
							if ((_isenhanced) && (!_isinfectious)) {
								org.weaklavendershield();
							} else {
								org.lavendershield();
							}
							setColor(Utils.ColorFALLOW);
						}
						if ((org._lavender <= 0) || ((!_isaplant) && (!_isinfectious)) || (_fallowversion == 1)) {
							if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
								for (int y = 0; y < org._segments; y++) {
									switch (getTypeColor(org._segColor[y])) {
									case BROWN:
									case VISION:
										org._segColor[y] = Utils.ColorLIGHTBROWN;
										break;
									}
								}
								setColor(Utils.ColorFALLOW);
								org._framesColor = 0;
								org._isinjured =true;
								org._isfrozen =true;
								org._allfrozen =true;
							}
						}
					}
				}
				break;
			case BARK:
				org._segColor[oseg] = Utils.ColorOLDBARK;
				if (org.active) {
					org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
					org._mphoto[oseg] = -0.5;
				} else {
					if (((_fallowversion != 2) || (!_isinfectious)) && ((!org._isenhanced) || (_fallowversion == 1))) {
						if (org._lavender > 0) {
							if ((_isenhanced) && (!_isinfectious)) {
								org.weaklavendershield();
							} else {
								org.lavendershield();
							}
							setColor(Utils.ColorFALLOW);
						}
						if ((org._lavender <= 0) || ((!_isaplant) && (!_isinfectious)) || (_fallowversion == 1)) {
							if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
								for (int y = 0; y < org._segments; y++) {
									switch (getTypeColor(org._segColor[y])) {
									case BARK:
									case OLDBARK:
										org._segColor[y] = Utils.ColorBROKEN;
										break;
									}
								}
								setColor(Utils.ColorFALLOW);
								org._framesColor = 0;
								org._isinjured =true;
								org._isfrozen =true;
								org._allfrozen =true;
							}
						}
					}
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
					// break is here on purpose ;)
					break;
				}
			default:
				if ((!org.active) && (org._segColor[oseg].equals(Utils.ColorOLDBARK))) {
					if (((_fallowversion != 2) || (!_isinfectious)) && ((!org._isenhanced) || (_fallowversion == 1))) {
						if (org._lavender > 0) {
							if ((_isenhanced) && (!_isinfectious)) {
								org.weaklavendershield();
							} else {
								org.lavendershield();
							}
							setColor(Utils.ColorFALLOW);
						}
						if ((org._lavender <= 0) || ((!_isaplant) && (!_isinfectious)) || (_fallowversion == 1)) {
							if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
								for (int y = 0; y < org._segments; y++) {
									switch (getTypeColor(org._segColor[y])) {
									case BARK:
									case OLDBARK:
										org._segColor[y] = Utils.ColorBROKEN;
										break;
									}
								}
								setColor(Utils.ColorFALLOW);
								org._framesColor = 0;
								org._isinjured =true;
								org._isfrozen =true;
								org._allfrozen =true;
							}
						}
					}
				} else {
					if ((org._fallowversion != _fallowversion) || (org._isinfectious) || ((!_isaconsumer) && (!_isafungus)) || (_isenhanced)) {
						if ((org._isaplant) || (org._isaconsumer) || ((org._isafungus) && (((_fallowversion == 2) && (org._antiviral == 0)) || (!_isaplant)))) {
							if (org._lavender > 0) {
								if ((_isenhanced) && (!_isinfectious)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorFALLOW);
							}
							if (org._lavender <= 0) {
								if (org._infectedGeneticCode != _geneticCode) {
									if (_fallowversion <= 2) {
										if ((_fallowversion == 2) || (org._sporeversion > 0)) {
											if (org._timeToReproduce < org._timeToReproduceMax + (int)(10 * _m[seg])) {
												if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
													setColor(Utils.ColorFALLOW);
													org.setColor(Utils.ColorFLOWER);
													org._timeToReproduce = org._timeToReproduceMax + (int)(10 * _m[seg]);
												}
											}
										}
									} else {
										if ((org._reproducelate == 0) || (org._isinfectious)) {
											if (_fallowversion == 4) {
												if (org._timeToReproduce < org._timeToReproduceMax + (int)(6 * _m[seg])) {
													if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
														setColor(Utils.ColorFALLOW);
														org.setColor(Utils.ColorFLOWER);
														org._timeToReproduce = org._timeToReproduceMax + (int)(6 * _m[seg]);
													}
												}
											} else {
												if (_isinfectious) {
													if (org._timeToReproduce < org._timeToReproduceMax) {
														if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
															setColor(Utils.ColorFALLOW);
															org.setColor(Utils.ColorFLOWER);
															org._timeToReproduce = org._timeToReproduceMax;
														}
													}
												} else {
													if (org._timeToReproduce < org._timeToReproduceMax + (int)(_m[seg])) {
														if (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION)) {
															setColor(Utils.ColorFALLOW);
															org.setColor(Utils.ColorFLOWER);
															org._timeToReproduce = org._timeToReproduceMax + (int)(_m[seg]);
														}
													}
												}
											}
										}
									}
								} else {
									if ((org._timeToReproduce > 4) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
										setColor(Utils.ColorFALLOW);
										org.setColor(Utils.ColorBLOND);
										org._timeToReproduce = 4;
									}
								}
								if (_fallowversion == 3) {
									if (_isinfectious) {
										if ((org._fallowinhibition < 2) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
											org._fallowinhibition = 2;
											setColor(Utils.ColorFALLOW);
											org.setColor(Utils.ColorFLOWER);
										}
									} else {
										if ((org._fallowinhibition < (int)((_m[seg]/2)+1)) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
											org._fallowinhibition = (int)((_m[seg]/2)+1);
											setColor(Utils.ColorFALLOW);
											org.setColor(Utils.ColorFLOWER);
										}
									}
								} else {
									if (_fallowversion == 4) {
										if ((org._isreproductive) && ((org._isaconsumer) || (org._isaplant)) && (org._antiviral == 0)) {
											org.inhibited(this);
										}
									} else {
										if (_fallowversion == 1) {
											if ((org._sporetime > 0) && (org._sporeversion > 0) && (useEnergy(Utils.FALLOW_ENERGY_CONSUMPTION))) {
												org._sporetime = -org._sporetime;
												setColor(Utils.ColorFALLOW);
												org.setColor(Utils.ColorFLOWER);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			break;
		case CORAL:
			// Transform viruses and particles into children
			switch (getTypeColor(org._segColor[oseg])) {
			case CORAL:
				if ((!org._isaplant) && (!org._isaconsumer) && ((!org._isafungus) || ((_isenhanced) && (!org._issilver)))) {
					if ((org._indigo > 0) && (!org._isafungus) && ((_indigo == 0) || (_isaplant) || (_isaconsumer) || (_isafungus))) {
						if (!_hasgoodvision) {
							if (useEnergy((Utils.CORAL_ENERGY_CONSUMPTION + 0.1)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
					    	    org.setColor(Utils.ColorINDIGO);
					    	    setColor(Utils.ColorCORAL);
							}
						}
					}
				}
				break;
			case WHITE:
			case SILVER:
			case PLAGUE:
				if ((!org._isaplant) && (!org._isaconsumer) && ((!org._isafungus) || ((_isenhanced) && (!org._issilver)))) {
					if ((!org._transfersenergy) || (org._isinfectious) || (org._iscoral) || (org._isafungus)) {
						if ((org._indigo > 0) && (!org._isafungus) && (!org._isblond)) {
							if (!_hasgoodvision) {
								if (useEnergy((Utils.CORAL_ENERGY_CONSUMPTION + 0.01)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
						    	    org.setColor(Utils.ColorINDIGO);
						    	    setColor(Utils.ColorCORAL);
								}
							}
							if (org._antiviral > 0) {
								virusneutralized();
							}
						} else {
							if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
								org.setColor(Utils.ColorTEAL);
								setColor(Utils.ColorCORAL);
								org._hasdodged =true;
							} else {
								if (org._antiviral > 0) {
									virusneutralized();
								}
								if (org._lavender > 0) {
									if ((_isauburn) && (!_isaplant) && (!_isaconsumer) && (!_isafungus) && (!org._isafungus)) {
										org.weaklavendershield();
									} else {
										org.lavendershield();
									}
									setColor(Utils.ColorCORAL);
									org._hasdodged =true;
								}
								if (org._lavender <= 0) {
									if (useEnergy(Utils.CORAL_ENERGY_CONSUMPTION)) {
										setColor(Utils.ColorCORAL);
										org.infectedBy(this);
										if (org._energy >= 1) {
											org.transform();
										}
										org.die(this);
									}
								}
							}
						}
					}
				}
				break;
			case INDIGO:
				if ((!org._isaplant) && (!org._isaconsumer) && ((!org._isafungus) || ((_isenhanced) && (!org._issilver)))) {
					if (!org._isinfectious) {
						if (!_hasgoodvision) {
							if (useEnergy((Utils.CORAL_ENERGY_CONSUMPTION + 0.1)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
					    	    org.setColor(Utils.ColorINDIGO);
					    	    setColor(Utils.ColorCORAL);
							}
						}
						if (org._antiviral > 0) {
							virusneutralized();
						}
					} else {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorCORAL);
							org._hasdodged =true;
						} else {
							if (org._antiviral > 0) {
								virusneutralized();
							}
							if (org._lavender > 0) {
								if ((_isauburn) && (!_isaplant) && (!_isaconsumer) && (!_isafungus) && (!org._isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorCORAL);
								org._hasdodged =true;
							}
							if (org._lavender <= 0) {
								if (useEnergy(Utils.CORAL_ENERGY_CONSUMPTION)) {
									setColor(Utils.ColorCORAL);
									org.infectedBy(this);
									if (org._energy >= 1) {
										org.transform();
									}
									org.die(this);
								}
							}
						}
					}
				}
				break;
			case TEAL:
			case CYAN:
			case AUBURN:
			case BLOND:
			case FLOWER:
				if ((!org._isaplant) && (!org._isaconsumer) && ((!org._isafungus) || ((_isenhanced) && (!org._issilver)))) {
					if ((!org._transfersenergy) || (org._isinfectious) || (org._iscoral) || (org._isafungus)) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorCORAL);
							org._hasdodged =true;
						} else {
							if (org._antiviral > 0) {
								virusneutralized();
							}
							if (org._lavender > 0) {
								if ((_isauburn) && (!_isaplant) && (!_isaconsumer) && (!_isafungus) && (!org._isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorCORAL);
								org._hasdodged =true;
							}
							if (org._lavender <= 0) {
								if (useEnergy(Utils.CORAL_ENERGY_CONSUMPTION)) {
									setColor(Utils.ColorCORAL);
									org.infectedBy(this);
									if (org._energy >= 1) {
										org.transform();
									}
									org.die(this);
								}
							}
						}
					}
				}
				break;
			case YELLOW:
			case DARKGRAY:
			case GOLD:
			case VISION:
			case VIOLET:
				if ((!org._isaplant) && (!org._isaconsumer) && ((!org._isafungus) || ((_isenhanced) && (!org._issilver))) && (org.active)) {
					if ((!org._transfersenergy) || (org._isinfectious) || (org._iscoral) || (org._isafungus)) {
						if (org._antiviral > 0) {
							virusneutralized();
						}
						if (org._isafungus) {
							if (org._lavender > 0) {
								if ((_isauburn) && (!_isaplant) && (!_isaconsumer) && (!_isafungus) && (!org._isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorCORAL);
							}
							if (org._lavender <= 0) {
								if (useEnergy(Utils.CORAL_ENERGY_CONSUMPTION)) {
									setColor(Utils.ColorCORAL);
									org.infectedBy(this);
									if (org._energy >= 1) {
										org.transform();
									}
									org.die(this);
								}
							}
						}
					}
				}
				break;
			case SPORE:
				if ((!org._isaplant) && (!org._isaconsumer) && ((!org._isafungus) || ((_isenhanced) && (!org._issilver)))) {
					if ((!org._transfersenergy) || (org._isinfectious) || (org._iscoral) || (org._isafungus)) {
						if (org._antiviral > 0) {
							virusneutralized();
						}
						if (org._sporeversion == 6) {
							if (org._lavender > 0) {
								if ((_isauburn) && (!_isaplant) && (!_isaconsumer) && (!_isafungus) && (!org._isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorCORAL);
							}
							if (org._lavender <= 0) {
								if (useEnergy(Utils.CORAL_ENERGY_CONSUMPTION)) {
									setColor(Utils.ColorCORAL);
									org.infectedBy(this);
									if (org._energy >= 1) {
										org.transform();
									}
									org.die(this);
								}
							}
						}
					}
				}
				break;
			case LIGHTBROWN:
			case GREENBROWN:
			case DARKFIRE:
			case BROKEN:
			case ICE:
				if ((!org._isaplant) && (!org._isaconsumer) && ((!org._isafungus) || ((_isenhanced) && (!org._issilver)))) {
					if ((!org._transfersenergy) || (org._isinfectious) || (org._iscoral) || (org._isafungus)) {
						if (useEnergy(Utils.CORAL_ENERGY_CONSUMPTION)) {
							setColor(Utils.ColorCORAL);
							org.infectedBy(this);
							if (org._energy >= 1) {
								org.transform();
							}
							org.die(this);
						}
					}
				}
				break;
			case MAGENTA:
			case ROSE:
				if (_altruist) {
	                break;
				} else {
					if ((!org._isaplant) && (!org._isaconsumer) && ((!org._isafungus) || ((_isenhanced) && (!org._issilver)))) {
						if ((!org._transfersenergy) || (org._isinfectious) || (org._iscoral) || (org._isafungus)) {
							if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
								org.setColor(Utils.ColorTEAL);
								setColor(Utils.ColorCORAL);
								org._hasdodged =true;
							} else {
								if (org._antiviral > 0) {
									virusneutralized();
								}
								if (org._lavender > 0) {
									if ((_isauburn) && (!_isaplant) && (!_isaconsumer) && (!_isafungus) && (!org._isafungus)) {
										org.weaklavendershield();
									} else {
										org.lavendershield();
									}
									setColor(Utils.ColorCORAL);
									org._hasdodged =true;
								}
								if (org._lavender <= 0) {
									if (useEnergy(Utils.CORAL_ENERGY_CONSUMPTION)) {
										setColor(Utils.ColorCORAL);
										org.infectedBy(this);
										if (org._energy >= 1) {
											org.transform();
										}
										org.die(this);
									}
								}
							}
						}
					}
				}
				break;
			case LAVENDER:
				if ((!org._isaplant) && (!org._isaconsumer) && ((!org._isafungus) || ((_isenhanced) && (!org._issilver)))) {
					if ((!org._transfersenergy) || (org._isinfectious) || (org._iscoral) || (org._isafungus)) {
						if (org._antiviral > 0) {
							virusneutralized();
						}
						if (org._isenhanced) {
							useEnergy(Utils.between((1/Utils.LAVENDER_ENERGY_CONSUMPTION), 0, _energy));
							if (org._lavender < Utils.LAVENDER_SHIELD) {
								org._lavender += 1000;
								if (org._lavender >= Utils.LAVENDER_SHIELD) {
									org._lavender = Utils.LAVENDER_SHIELD;
								}
							}
						} else {
							useEnergy(Utils.between((0.2/Utils.LAVENDER_ENERGY_CONSUMPTION), 0, _energy));
							if (org._lavender < Utils.LAVENDER_SHIELD) {
								org._lavender += 200;
								if (org._lavender >= Utils.LAVENDER_SHIELD) {
									org._lavender = Utils.LAVENDER_SHIELD;
								}
							}
						}
						// The other organism will be shown in lavender
						org.setColor(Utils.ColorLAVENDER);
						// This organism will be shown in deadbark
						setColor(Utils.ColorDEADBARK);
						if (_energy < Utils.tol) {
							die(org);
						}
					}
				}
				break;
			case BLUE:
			case SKY:
			case DEEPSKY:
			case OCHRE:
			case OLIVE:
			case DARKOLIVE:
			case GRAY:
			case LILAC:
			case DARKLILAC:
			case SPIKE:
			case SPIKEPOINT:
			case FALLOW:
			case MINT:
				if ((!org._isaplant) && (!org._isaconsumer) && ((!org._isafungus) || ((_isenhanced) && (!org._issilver))) && (org.active)) {
					if ((!org._transfersenergy) || (org._isinfectious) || (org._iscoral) || (org._isafungus)) {
						if (org._antiviral > 0) {
							virusneutralized();
						}
					}
				}
				break;
			case FRUIT:
				if (org._sporeversion == 1) {
					if ((org._framesColor > 0) || (_isaplant) || ((_isauburn) && (!_isaconsumer) && (!_isafungus))) {
						if (useEnergy(Utils.CORAL_ENERGY_CONSUMPTION)) {
							setColor(Utils.ColorCORAL);
							org.infectedBy(this);
							if (org._energy >= 1) {
								org.transform();
							}
							org.die(this);
						}
					} else {
						org.setColor(Utils.ColorBROKEN);
					}
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				} else {
					if ((!org._isaplant) && (!org._isaconsumer) && ((!org._isafungus) || ((_isenhanced) && (!org._issilver)))) {
						if ((!org._transfersenergy) || (org._isinfectious) || (org._iscoral) || (org._isafungus)) {
							if (org._antiviral > 0) {
								virusneutralized();
							}
							if (org._lavender > 0) {
								if ((_isauburn) && (!_isaplant) && (!_isaconsumer) && (!_isafungus) && (!org._isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorCORAL);
							}
							if (org._lavender <= 0) {
								if (useEnergy(Utils.CORAL_ENERGY_CONSUMPTION)) {
									setColor(Utils.ColorCORAL);
									org.infectedBy(this);
									if (org._energy >= 1) {
										org.transform();
									}
									org.die(this);
								}
							}
						}
					}
				}
				break;
			case BARK:
			case OLDBARK:
			case DEADBARK:
			case BROWN:
				break;
			default:
				if ((!org._isaplant) && (!org._isaconsumer) && ((!org._isafungus) || ((_isenhanced) && (!org._issilver)))) {
					if ((!org._transfersenergy) || (org._isinfectious) || (org._iscoral) || (org._isafungus)) {
						if (org._antiviral > 0) {
							virusneutralized();
						}
						if (org._lavender > 0) {
							if ((_isauburn) && (!_isaplant) && (!_isaconsumer) && (!_isafungus) && (!org._isafungus)) {
								org.weaklavendershield();
							} else {
								org.lavendershield();
							}
							setColor(Utils.ColorCORAL);
						}
						if (org._lavender <= 0) {
							if (useEnergy(Utils.CORAL_ENERGY_CONSUMPTION)) {
								setColor(Utils.ColorCORAL);
								org.infectedBy(this);
								if (org._energy >= 1) {
									org.transform();
								}
								org.die(this);
							}
						}
					}
				}
			}
			break;
		case FRUIT:
			// Fruit segment: Infect the other organism, if the other organism tries to eat it, or infect and integrate with plants
			double takenEnergyFruit = 0;
			switch (getTypeColor(org._segColor[oseg])) {
			case GREEN:
			case GRASS:
			case FOREST:
			case SPRING:
			case SUMMER:
            case WINTER:
			case LIME:
			case LEAF:
			case C4:
			case JADE:
			case DARKJADE:
				if ((org._infectedGeneticCode != _geneticCode) && (_sporeversion == 1)) {
					if ((org._infectedGeneticCode != null) || (org._sporeversion != 1)) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							if ((org._isonlyc4 != 2) || (org._framesColor > 8)) {
								org._hasdodged =true;
							}
							org.setColor(Utils.ColorTEAL);
						} else {
							if ((org._antiviral > 0) && (org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10))) {
								for (int a = 0; a < _segments; a++) {
									switch (getTypeColor(_segColor[a])) {
									case FRUIT:
									case DARKFIRE:
										_segColor[a] = Utils.ColorBROKEN;
										_mphoto[a] = -20;
										break;
									}
								}
								org.setColor(Utils.ColorMINT);
								_framesColor = 0;
								_isinjured =true;
								_isfrozen =true;
								_allfrozen =true;
							} else {
								if (org._jadefactor > 1) {
						    		if (org._segColor[oseg].equals(Utils.ColorJADE)) {
						    			org._segColor[oseg] = Utils.ColorDARKJADE;
						    		} else {
						    			org._segColor[oseg] = Utils.ColorDARKGREEN;
						    		}
									org._hasdodged =true;
									org._useextraeffects =true;
						    	} else {
						    		org.infectedByFruit(this);
						    		takenEnergyFruit = Utils.between(_energy - 0.0000002, 0, _energy);
								    org._energy += takenEnergyFruit;
								    _energy -= takenEnergyFruit;
							        org.setColor(Utils.ColorLIGHTBROWN);
						            org._timeToReproduce = 0;
						        }
							}
						}
					}
				}
				break;
			case DARKGREEN:
			case PURPLE:
			case TEAL:
			case CYAN:
			case YELLOW:
			case AUBURN:
			case BLOND:
			case FLOWER:
			case DARKGRAY:
			case GOLD:
				if ((org._infectedGeneticCode != _geneticCode) && (_sporeversion == 1)) {
					if (org._isaplant) {
						if ((org._infectedGeneticCode != null) || (org._sporeversion != 1)) {
							if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
								if ((org._isonlyc4 != 2) || (org._framesColor > 8)) {
									org._hasdodged =true;
								}
								org.setColor(Utils.ColorTEAL);
							} else {
								org.infectedByFruit(this);
								takenEnergyFruit = Utils.between(_energy - 0.0000002, 0, _energy);
							    org._energy += takenEnergyFruit;
							    _energy -= takenEnergyFruit;
						        org.setColor(Utils.ColorLIGHTBROWN);
					            org._timeToReproduce = 0;
							}
						}
					}
				}
				break;
			case SPORE:
				if ((org._infectedGeneticCode != _geneticCode) && (_sporeversion == 1)) {
					if (org._isaplant) {
						if (((org._infectedGeneticCode != null) || (org._sporeversion != 1)) && (org._sporeversion > 0)) {
							if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
								if ((org._isonlyc4 != 2) || (org._framesColor > 8)) {
									org._hasdodged =true;
								}
								org.setColor(Utils.ColorTEAL);
							} else {
								org.infectedByFruit(this);
								takenEnergyFruit = Utils.between(_energy - 0.0000002, 0, _energy);
							    org._energy += takenEnergyFruit;
							    _energy -= takenEnergyFruit;
					            org.setColor(Utils.ColorLIGHTBROWN);
					            org._timeToReproduce = 0;
					            if ((!org._isaconsumer) && (!org._isafungus) && (org._sporeversion >= 3)) {
									org._infectedReproduceEnergy = -org._infectedReproduceEnergy;
								}
							}
						} else {
							if (org._sporeversion != 1) {
								org.setColor(Utils.ColorSPORE);
							}
						}
					}
				}
				break;
			case INDIGO:
				if ((org._isaplant) && (_sporeversion == 1)) {
					takenEnergyFruit = Utils.between(_energy - 0.0000002, 0, _energy);
				    org._energy += takenEnergyFruit;
				    _energy -= takenEnergyFruit;
	    			org.setColor(Utils.ColorINDIGO);
	    			org._timeToReproduce = 0;
				}
				break;
			case MAGENTA:
			case ROSE:
				if ((org._infectedGeneticCode != _geneticCode) && (_sporeversion == 1) && (!_altruist)) {
					if (org._isaplant) {
						if ((org._infectedGeneticCode != null) || (org._sporeversion != 1)) {
							if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
								if ((org._isonlyc4 != 2) || (org._framesColor > 8)) {
									org._hasdodged =true;
								}
								org.setColor(Utils.ColorTEAL);
							} else {
								org.infectedByFruit(this);
								takenEnergyFruit = Utils.between(_energy - 0.0000002, 0, _energy);
							    org._energy += takenEnergyFruit;
							    _energy -= takenEnergyFruit;
					            org.setColor(Utils.ColorLIGHTBROWN);
					            org._timeToReproduce = 0;
							}
						}
					}
				}
				break;
			case SILVER:
				if (org._infectedGeneticCode != _geneticCode) {
					if (((org._nTotalKills > 0) && ((org._isaconsumer) || (org._isafungus))) || (org._isenhanced)) {
					    break;
					} else {
						if (_sporeversion == 1) {
							if (org._isaplant) {
								if ((org._infectedGeneticCode != null) || (org._sporeversion != 1)) {
									org.infectedByFruit(this);
									takenEnergyFruit = Utils.between(_energy - 0.0000002, 0, _energy);
								    org._energy += takenEnergyFruit;
								    _energy -= takenEnergyFruit;
						            org.setColor(Utils.ColorLIGHTBROWN);
						            org._timeToReproduce = 0;
								}
							}
						}
					}
				}
				break;
			case SKY:
				if ((org._skyversion == 2) && (org._infectedGeneticCode != _geneticCode) && (_sporeversion == 1)) {
					if (org._isaplant) {
						if ((org._infectedGeneticCode != null) || (org._sporeversion != 1)) {
							if (org.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
								org._segColor[oseg] = Utils.ColorDEEPSKY;
								org._mphoto[oseg] = -20;
								org._useextraeffects = true;
								org.setColor(Utils.ColorDEEPSKY);
					        } else {
					        	// isenhanced is used for real viruses here
					        	if (((_isenhanced) && (!_isblond)) || (org._isblond)) {
									org.infectedByFruit(this);
								} else {
									org.infectedByFruitWeak(this);
								}
					        	takenEnergyFruit = Utils.between(_energy - 0.0000002, 0, _energy);
							    org._energy += takenEnergyFruit;
							    _energy -= takenEnergyFruit;
					            org.setColor(Utils.ColorLIGHTBROWN);
					            org._timeToReproduce = 0;
					        }
						}
					}
				}
				break;
			case SPIKE:
			case DEEPSKY:
			case OLIVE:
			case DARKOLIVE:
			case BARK:
			case OLDBARK:
			case OCHRE:
			case FALLOW:
				if ((org._infectedGeneticCode != _geneticCode) && (_sporeversion == 1)) {
					if (org._isaplant) {
						if ((org._infectedGeneticCode != null) || (org._sporeversion != 1)) {
							// isenhanced is used for real viruses here
							if (((_isenhanced) && (!_isblond)) || (org._isblond)) {
								org.infectedByFruit(this);
							} else {
								org.infectedByFruitWeak(this);
							}
							takenEnergyFruit = Utils.between(_energy - 0.0000002, 0, _energy);
						    org._energy += takenEnergyFruit;
						    _energy -= takenEnergyFruit;
				            org.setColor(Utils.ColorLIGHTBROWN);
				            org._timeToReproduce = 0;
						}
					}
				}
				break;
			case PLAGUE:
				if ((org._infectedGeneticCode != _geneticCode) && (_sporeversion == 1)) {
					if (org._isaplant) {
						if ((org._infectedGeneticCode != null) || (org._sporeversion != 1)) {
							if ((!org._isinfectious) && (!org._isaconsumer) && (!org._isafungus)) {
								org.infectedByFruitWeak(this);
								org._infectedReproduceEnergy = 4;
							} else {
								org.infectedByFruit(this);
							}
							takenEnergyFruit = Utils.between(_energy - 0.0000002, 0, _energy);
						    org._energy += takenEnergyFruit;
						    _energy -= takenEnergyFruit;
				            org.setColor(Utils.ColorLIGHTBROWN);
				            org._timeToReproduce = 0;
						}
					}
				}
				break;
			case LAVENDER:
				if ((org._infectedGeneticCode != _geneticCode) && (_sporeversion == 1)) {
					if (org._isaplant) {
						if ((org._infectedGeneticCode != null) || (org._sporeversion != 1)) {
							if (org._isenhanced) {
								useEnergy(Utils.between((1/Utils.LAVENDER_ENERGY_CONSUMPTION), 0, _energy));
								if (org._lavender < Utils.LAVENDER_SHIELD) {
									org._lavender += 1000;
									if (org._lavender >= Utils.LAVENDER_SHIELD) {
										org._lavender = Utils.LAVENDER_SHIELD;
									}
								}
							} else {
								useEnergy(Utils.between((0.2/Utils.LAVENDER_ENERGY_CONSUMPTION), 0, _energy));
								if (org._lavender < Utils.LAVENDER_SHIELD) {
									org._lavender += 200;
									if (org._lavender >= Utils.LAVENDER_SHIELD) {
										org._lavender = Utils.LAVENDER_SHIELD;
									}
								}
							}
							// The other organism will be shown in lavender
							org.setColor(Utils.ColorLAVENDER);
							// This organism will be shown in deadbark
							setColor(Utils.ColorDEADBARK);
							org._hasdodged =true;
							if (_energy < Utils.tol) {
								die(org);
							}
						}
					}
				}
				break;
			case RED:
			case ORANGE:
			case FIRE:
			case MAROON:
			case PINK:
			case CREAM:
			case SPIKEPOINT:
			case BLUE:
			case VIOLET:
			case CORAL:
			case MINT:
			case BROWN:
				break;
			default:
				if ((org._infectedGeneticCode != _geneticCode) && (_sporeversion == 1)) {
					if (org._isaplant) {
						if ((org._infectedGeneticCode != null) || (org._sporeversion != 1)) {
							org.infectedByFruit(this);
							takenEnergyFruit = Utils.between(_energy - 0.0000002, 0, _energy);
						    org._energy += takenEnergyFruit;
						    _energy -= takenEnergyFruit;
				            org.setColor(Utils.ColorLIGHTBROWN);
				            org._timeToReproduce = 0;
						}
					}
				}
			}
			break;
		case PLAGUE:
			// Force reproduction of infected victims
			switch (getTypeColor(org._segColor[oseg])) {
			case WHITE:
			case PLAGUE:
				if (((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) && (org._isinfectious)) {
					if ((((_plagueversion == 2) || (!_isinfectious)) && (org._isaconsumer)) || ((_plagueversion == 1) && (org._isaplant))) {
						if (org._lavender > 0) {
							if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
								org.weaklavendershield();
							} else {
								org.lavendershield();
							}
							setColor(Utils.ColorPLAGUE);
						}
						if (org._lavender <= 0) {
							if (_isinfectious) {
								if (_isaplant) {
									if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
								        org.reproduceVirus();
								        setColor(Utils.ColorPLAGUE);
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
							            org.reproduceVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							} else {
								if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
									if ((!_isaconsumer) && (!_isafungus)) {
										org._nVirusChildren = -1;
									}
						            org.reproduceforeignVirus();
						            setColor(Utils.ColorPLAGUE);
					            }
							}
						}
					}
				}
				if ((org._indigo > 0) && (!org._isaplant) && (!org._isaconsumer) && (!org._isafungus) && (!_hasgoodvision)) {
					if ((_isaplant) && (_isinfectious)) {
						if (useEnergy((Utils.PLAGUE_ENERGY_CONSUMPTION + 0.01)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
				    	    org.setColor(Utils.ColorINDIGO);
				    	    setColor(Utils.ColorPLAGUE);
						}
				    } else {
				    	if ((_indigo == 0) || (_isaplant) || (_isaconsumer) || (_isafungus)) {
				    		if (useEnergy((Utils.SCOURGE_ENERGY_CONSUMPTION + 0.1)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
				    			org.setColor(Utils.ColorINDIGO);
				    			setColor(Utils.ColorPLAGUE);
				    		}
				    	}
				    }
				}
				break;
			case SILVER:
				if ((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
					if ((org._isenhanced) || (_nTotalInfected >= org._nTotalInfected)) {
						if ((((_plagueversion == 2) || (!_isinfectious)) && (org._isaconsumer)) || ((_plagueversion == 1) && (org._isaplant))) {
							if (org._lavender > 0) {
								if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorPLAGUE);
							}
							if (org._lavender <= 0) {
								if (_isinfectious) {
									if (_isaplant) {
										if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
									        org.reproduceVirus();
									        setColor(Utils.ColorPLAGUE);
										}
									} else {
										if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
								            org.reproduceVirus();
								            setColor(Utils.ColorPLAGUE);
							            }
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
										if ((!_isaconsumer) && (!_isafungus)) {
											org._nVirusChildren = -1;
										}
							            org.reproduceforeignVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							}
						}
					}
				}
				if ((org._indigo > 0) && (!org._isaplant) && (!org._isaconsumer) && (!org._isafungus) && (!_hasgoodvision)) {
					if ((_isaplant) && (_isinfectious)) {
						if (useEnergy((Utils.PLAGUE_ENERGY_CONSUMPTION + 0.01)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
				    	    org.setColor(Utils.ColorINDIGO);
				    	    setColor(Utils.ColorPLAGUE);
						}
				    } else {
				    	if ((_indigo == 0) || (_isaplant) || (_isaconsumer) || (_isafungus)) {
				    		if (useEnergy((Utils.SCOURGE_ENERGY_CONSUMPTION + 0.1)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
				    			org.setColor(Utils.ColorINDIGO);
				    			setColor(Utils.ColorPLAGUE);
				    		}
				    	}
				    }
				}
				break;
			case INDIGO:
				if ((((_plagueversion == 2) || (!_isinfectious)) && (org._isaconsumer)) || ((_plagueversion == 1) && (org._isaplant))
						|| ((!org._isaplant) && (!org._isaconsumer) && (!org._isafungus))) {
					if (!_hasgoodvision) {
						if ((_isaplant) && (_isinfectious)) {
							if (useEnergy((Utils.PLAGUE_ENERGY_CONSUMPTION + 0.01)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
					    	    org.setColor(Utils.ColorINDIGO);
					    	    setColor(Utils.ColorPLAGUE);
							}
					    } else {
					    	if ((_indigo == 0) || (_isaplant) || (_isaconsumer) || (_isafungus)) {
					    		if (useEnergy((Utils.SCOURGE_ENERGY_CONSUMPTION + 0.1)/Utils.INDIGO_ENERGY_CONSUMPTION)) {
					    			org.setColor(Utils.ColorINDIGO);
					    			setColor(Utils.ColorPLAGUE);
					    		}
					    	}
					    }
					}
					if ((org._antiviral > 0) && (_isinfectious)) {
		    			virusneutralized();
					}
				}
				break;
			case LEAF:
				if ((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
					if (_plagueversion == 1) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorPLAGUE);
							org._hasdodged =true;
						} else {
							if ((!org._modifiesleaf) || (org._framesColor > 0)) {
								if (org._lavender > 0) {
									if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
										org.weaklavendershield();
									} else {
										org.lavendershield();
									}
									setColor(Utils.ColorPLAGUE);
								}
								if (org._lavender <= 0) {
									if (org._jadefactor > 1) {
										if (org._segColor[oseg].equals(Utils.ColorJADE)) {
							    			org._segColor[oseg] = Utils.ColorDARKJADE;
							    		} else {
							    			org._segColor[oseg] = Utils.ColorDARKGREEN;
							    		}
							    		setColor(Utils.ColorPLAGUE);
										org._hasdodged =true;
										org._useextraeffects =true;
										if ((org._antiviral > 0) && (_isinfectious)) {
							    			virusneutralized();
										}
							    	} else {
							    		if (_isinfectious) {
											if (_isaplant) {
												if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
											        org.reproduceVirus();
											        setColor(Utils.ColorPLAGUE);
												}
											} else {
												if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
										            org.reproduceVirus();
										            setColor(Utils.ColorPLAGUE);
									            }
											}
										} else {
											if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
												if ((!_isaconsumer) && (!_isafungus)) {
													org._nVirusChildren = -1;
												}
									            org.reproduceforeignVirus();
									            setColor(Utils.ColorPLAGUE);
								            }
										}
							    	}
								}
							} else {
								// The other organism will be shown in light blue
								org.setColortwoFrames(Utils.ColorLIGHT_BLUE);
								setColor(Utils.ColorPLAGUE);
							}
						}
					}
				}
				break;
			case GREEN:
			case GRASS:
			case FOREST:
			case SPRING:
			case SUMMER:
            case WINTER:
			case LIME:
			case C4:
			case JADE:
			case DARKJADE:
				if ((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
					if (_plagueversion == 1) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorPLAGUE);
							org._hasdodged =true;
						} else {
							if (org._lavender > 0) {
								if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorPLAGUE);
							}
							if (org._lavender <= 0) {
								if (org._jadefactor > 1) {
									if (org._segColor[oseg].equals(Utils.ColorJADE)) {
						    			org._segColor[oseg] = Utils.ColorDARKJADE;
						    		} else {
						    			org._segColor[oseg] = Utils.ColorDARKGREEN;
						    		}
						    		setColor(Utils.ColorPLAGUE);
									org._hasdodged =true;
									org._useextraeffects =true;
									if ((org._antiviral > 0) && (_isinfectious)) {
						    			virusneutralized();
									}
						    	} else {
						    		if (_isinfectious) {
										if (_isaplant) {
											if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
										        org.reproduceVirus();
										        setColor(Utils.ColorPLAGUE);
											}
										} else {
											if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
									            org.reproduceVirus();
									            setColor(Utils.ColorPLAGUE);
								            }
										}
									} else {
										if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
											if ((!_isaconsumer) && (!_isafungus)) {
												org._nVirusChildren = -1;
											}
								            org.reproduceforeignVirus();
								            setColor(Utils.ColorPLAGUE);
							            }
									}
						    	}
							}
						}
					}
				}
				break;
			case DARKGREEN:
			case PURPLE:
				if ((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
					if (_plagueversion == 1) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorPLAGUE);
							org._hasdodged =true;
						} else {
							if (org._lavender > 0) {
								if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorPLAGUE);
							}
							if (org._lavender <= 0) {
								if (_isinfectious) {
									if (_isaplant) {
										if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
									        org.reproduceVirus();
									        setColor(Utils.ColorPLAGUE);
										}
									} else {
										if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
								            org.reproduceVirus();
								            setColor(Utils.ColorPLAGUE);
							            }
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
										if ((!_isaconsumer) && (!_isafungus)) {
											org._nVirusChildren = -1;
										}
							            org.reproduceforeignVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							}
						}
					}
				}
				break;
			case BLUE:
				if ((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
					if ((_plagueversion == 1) && (org._isaplant)) {
				        if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
				        	if (org._isenhanced) {
							    useEnergy(Utils.between((0.5 * Math.sqrt(org._m[oseg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, _energy));
								setColor(Utils.ColorDARKLILAC);
								if (_energy < Utils.tol) {
									die(org);
								}
							} else {
								setColor(Utils.ColorPLAGUE);
							}
					        org.setColor(Color.BLUE);
				        } else {
				        	if (org._lavender > 0) {
				        		if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorPLAGUE);
							}
							if (org._lavender <= 0) {
								if (_isinfectious) {
									if (_isaplant) {
										if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
									        org.reproduceVirus();
									        setColor(Utils.ColorPLAGUE);
										}
									} else {
										if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
								            org.reproduceVirus();
								            setColor(Utils.ColorPLAGUE);
							            }
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
										if ((!_isaconsumer) && (!_isafungus)) {
											org._nVirusChildren = -1;
										}
							            org.reproduceforeignVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							}
				        }
					}
				}
				break;
			case OCHRE:
			case BARK:
			case OLDBARK:
				if ((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
					if ((_plagueversion == 1) && (org._isaplant)) {
						if (org._lavender > 0) {
							if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
								org.weaklavendershield();
							} else {
								org.lavendershield();
							}
							setColor(Utils.ColorPLAGUE);
						}
						if (org._lavender <= 0) {
							if (_isinfectious) {
								if (_isaplant) {
									if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
								        org.reproduceVirus();
								        setColor(Utils.ColorPLAGUE);
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
							            org.reproduceVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							} else {
								if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
									if ((!_isaconsumer) && (!_isafungus)) {
										org._nVirusChildren = -1;
									}
						            org.reproduceforeignVirus();
						            setColor(Utils.ColorPLAGUE);
					            }
							}
						}
					}
				}
				break;
			case SKY:
			case DEEPSKY:
			case FALLOW:
				if (((org._infectedGeneticCode == _geneticCode) && (_isenhanced)) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
					if ((_plagueversion == 1) && (org._isaplant) && (org._createlavender == 0)) {
						if (org._lavender > 0) {
							if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
								org.weaklavendershield();
							} else {
								org.lavendershield();
							}
							setColor(Utils.ColorPLAGUE);
						}
						if (org._lavender <= 0) {
							if (_isinfectious) {
								if (_isaplant) {
									if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
								        org.reproduceVirus();
								        setColor(Utils.ColorPLAGUE);
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
							            org.reproduceVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							} else {
								if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
									if ((!_isaconsumer) && (!_isafungus)) {
										org._nVirusChildren = -1;
									}
						            org.reproduceforeignVirus();
						            setColor(Utils.ColorPLAGUE);
					            }
							}
						}
					}
				}
				break;
			case MAROON:
			case ORANGE:
			case FIRE:
				if ((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
					if ((_plagueversion == 2) || (!_isinfectious)) {
						if (org._lavender > 0) {
							if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
								org.weaklavendershield();
							} else {
								org.lavendershield();
							}
							setColor(Utils.ColorPLAGUE);
						}
						if (org._lavender <= 0) {
							if (_isinfectious) {
								if (_isaplant) {
									if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
								        org.reproduceVirus();
								        setColor(Utils.ColorPLAGUE);
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
							            org.reproduceVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							} else {
								if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
									if ((!_isaconsumer) && (!_isafungus)) {
										org._nVirusChildren = -1;
									}
						            org.reproduceforeignVirus();
						            setColor(Utils.ColorPLAGUE);
					            }
							}
						}
					}
				}
				break;
			case RED:
			case CREAM:
				if (((org._infectedGeneticCode == _geneticCode) && (_isenhanced)) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
					if (((_plagueversion == 2) || (!_isinfectious)) && (org._createlavender == 0)) {
						if (org._lavender > 0) {
							if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
								org.weaklavendershield();
							} else {
								org.lavendershield();
							}
							setColor(Utils.ColorPLAGUE);
						}
						if (org._lavender <= 0) {
							if (_isinfectious) {
								if (_isaplant) {
									if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
								        org.reproduceVirus();
								        setColor(Utils.ColorPLAGUE);
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
							            org.reproduceVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							} else {
								if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
									if ((!_isaconsumer) && (!_isafungus)) {
										org._nVirusChildren = -1;
									}
						            org.reproduceforeignVirus();
						            setColor(Utils.ColorPLAGUE);
					            }
							}
						}
					}
				}
				break;
			case PINK:
				if (((org._infectedGeneticCode == _geneticCode) && ((org._modifiespink) || ((_isauburn) && (!_isaplant) && (!_isaconsumer) && (!_isafungus))))
						|| ((!_isinfectious) && (org._infectedGeneticCode != null))) {
					if ((_plagueversion == 2) || (!_isinfectious)) {
						if (org._lavender > 0) {
							if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
								org.weaklavendershield();
							} else {
								org.lavendershield();
							}
							setColor(Utils.ColorPLAGUE);
						}
						if (org._lavender <= 0) {
							if (_isinfectious) {
								if (_isaplant) {
									if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
								        org.reproduceVirus();
								        setColor(Utils.ColorPLAGUE);
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
							            org.reproduceVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							} else {
								if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
									if ((!_isaconsumer) && (!_isafungus)) {
										org._nVirusChildren = -1;
									}
						            org.reproduceforeignVirus();
						            setColor(Utils.ColorPLAGUE);
					            }
							}
						}
					}
				}
				break;
			case SPIKE:
				if ((org._isaplant) || (org._isenhanced)) {
					if ((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
						if ((((_plagueversion == 2) || (!_isinfectious)) && (org._isaconsumer)) || ((_plagueversion == 1) && (org._isaplant))) {
							if (org._lavender > 0) {
								if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorPLAGUE);
							}
							if (org._lavender <= 0) {
								if (_isinfectious) {
									if (_isaplant) {
										if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
									        org.reproduceVirus();
									        setColor(Utils.ColorPLAGUE);
										}
									} else {
										if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
								            org.reproduceVirus();
								            setColor(Utils.ColorPLAGUE);
							            }
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
										if ((!_isaconsumer) && (!_isafungus)) {
											org._nVirusChildren = -1;
										}
							            org.reproduceforeignVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							}
						}
					}
				}
				break;
			case LAVENDER:
				if ((org._isaconsumer) || (org._isaplant)) {
					if (org._isenhanced) {
						useEnergy(Utils.between((1/Utils.LAVENDER_ENERGY_CONSUMPTION), 0, _energy));
						if (org._lavender < Utils.LAVENDER_SHIELD) {
							org._lavender += 1000;
							if (org._lavender >= Utils.LAVENDER_SHIELD) {
								org._lavender = Utils.LAVENDER_SHIELD;
							}
						}
					} else {
						useEnergy(Utils.between((0.2/Utils.LAVENDER_ENERGY_CONSUMPTION), 0, _energy));
						if (org._lavender < Utils.LAVENDER_SHIELD) {
							org._lavender += 200;
							if (org._lavender >= Utils.LAVENDER_SHIELD) {
								org._lavender = Utils.LAVENDER_SHIELD;
							}
						}
					}
					if (org._infectedGeneticCode == _geneticCode) {
						org._infectedGeneticCode = null;
						org._savedGeneticCode = null;
					}
					// The other organism will be shown in lavender
					org.setColor(Utils.ColorLAVENDER);
					// This organism will be shown in deadbark
					setColor(Utils.ColorDEADBARK);
					org._hasdodged =true;
					if (_energy < Utils.tol) {
						die(org);
					}
				}
				break;
			case SPORE:
				if ((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
					if ((((_plagueversion == 2) || (!_isinfectious)) && (org._isaconsumer)) || ((_plagueversion == 1) && (org._isaplant))) {
						if ((org._sporeversion >= 1) && (org._isaconsumer)) {
							if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
								org.setColor(Utils.ColorTEAL);
								setColor(Utils.ColorPLAGUE);
								org._hasdodged =true;
							} else {
								if (org._lavender > 0) {
									if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
										org.weaklavendershield();
									} else {
										org.lavendershield();
									}
									setColor(Utils.ColorPLAGUE);
								}
								if (org._lavender <= 0) {
									if (_isinfectious) {
										if (_isaplant) {
											if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
										        org.reproduceVirus();
										        setColor(Utils.ColorPLAGUE);
											}
										} else {
											if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
									            org.reproduceVirus();
									            setColor(Utils.ColorPLAGUE);
								            }
										}
									} else {
										if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
											if ((!_isaconsumer) && (!_isafungus)) {
												org._nVirusChildren = -1;
											}
								            org.reproduceforeignVirus();
								            setColor(Utils.ColorPLAGUE);
							            }
									}
								}
							}
						} else {
							setColor(Utils.ColorPLAGUE);
							org.setColor(Utils.ColorSPORE);
						}
					}
				}
				break;
			case CYAN:
			case TEAL:
			case YELLOW:
			case AUBURN:
			case BLOND:
			case FLOWER:
			case DARKGRAY:
			case GOLD:
				if ((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
					if ((((_plagueversion == 2) || (!_isinfectious)) && (org._isaconsumer)) || ((_plagueversion == 1) && (org._isaplant))) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorPLAGUE);
							org._hasdodged =true;
						} else {
							if (org._lavender > 0) {
								if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorPLAGUE);
							}
							if (org._lavender <= 0) {
								if (_isinfectious) {
									if (_isaplant) {
										if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
									        org.reproduceVirus();
									        setColor(Utils.ColorPLAGUE);
										}
									} else {
										if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
								            org.reproduceVirus();
								            setColor(Utils.ColorPLAGUE);
							            }
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
										if ((!_isaconsumer) && (!_isafungus)) {
											org._nVirusChildren = -1;
										}
							            org.reproduceforeignVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							}
						}
					}
				}
				break;
			case MAGENTA:
			case ROSE:
				if (((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) && (!_altruist)) {
					if ((((_plagueversion == 2) || (!_isinfectious)) && (org._isaconsumer)) || ((_plagueversion == 1) && (org._isaplant))) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorPLAGUE);
							org._hasdodged =true;
						} else {
							if (org._lavender > 0) {
								if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorPLAGUE);
							}
							if (org._lavender <= 0) {
								if (_isinfectious) {
									if (_isaplant) {
										if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
									        org.reproduceVirus();
									        setColor(Utils.ColorPLAGUE);
										}
									} else {
										if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
								            org.reproduceVirus();
								            setColor(Utils.ColorPLAGUE);
							            }
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
										if ((!_isaconsumer) && (!_isafungus)) {
											org._nVirusChildren = -1;
										}
							            org.reproduceforeignVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							}
						}
					}
				}
				break;
			case SPIKEPOINT:
				if (org._isenhanced) {
					if ((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
						if ((((_plagueversion == 2) || (!_isinfectious)) && (org._isaconsumer)) || ((_plagueversion == 1) && (org._isaplant))) {
							if (org._lavender > 0) {
								if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorPLAGUE);
							}
							if (org._lavender <= 0) {
								if (_isinfectious) {
									if (_isaplant) {
										if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
									        org.reproduceVirus();
									        setColor(Utils.ColorPLAGUE);
										}
									} else {
										if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
								            org.reproduceVirus();
								            setColor(Utils.ColorPLAGUE);
							            }
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
										if ((!_isaconsumer) && (!_isafungus)) {
											org._nVirusChildren = -1;
										}
							            org.reproduceforeignVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							}
						}
					}
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				} else {
					if ((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
						if ((((_plagueversion == 2) || (!_isinfectious)) && (org._isaconsumer)) || ((_plagueversion == 1) && (org._isaplant))) {
							if (org._lavender > 0) {
								if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
									org.weaklavendershield();
								} else {
									org.lavendershield();
								}
								setColor(Utils.ColorPLAGUE);
							}
							if (org._lavender <= 0) {
								if (_isinfectious) {
									if (_isaplant) {
										if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
									        org.reproduceVirus();
									        setColor(Utils.ColorPLAGUE);
										}
									} else {
										if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
								            org.reproduceVirus();
								            setColor(Utils.ColorPLAGUE);
							            }
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
										if ((!_isaconsumer) && (!_isafungus)) {
											org._nVirusChildren = -1;
										}
							            org.reproduceforeignVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							}
						}
					}
				}
				break;
			case MINT:
			case CORAL:
			case OLIVE:
			case BROWN:
				break;
			default:
				if ((org._infectedGeneticCode == _geneticCode) || ((!_isinfectious) && (org._infectedGeneticCode != null))) {
					if ((((_plagueversion == 2) || (!_isinfectious)) && (org._isaconsumer)) || ((_plagueversion == 1) && (org._isaplant))) {
						if (org._lavender > 0) {
							if ((_isenhanced) && (!_isinfectious) && (!_isaconsumer) && (!_isafungus)) {
								org.weaklavendershield();
							} else {
								org.lavendershield();
							}
							setColor(Utils.ColorPLAGUE);
						}
						if (org._lavender <= 0) {
							if (_isinfectious) {
								if (_isaplant) {
									if (_energy > Utils.PLAGUE_ENERGY_CONSUMPTION) {
								        org.reproduceVirus();
								        setColor(Utils.ColorPLAGUE);
									}
								} else {
									if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
							            org.reproduceVirus();
							            setColor(Utils.ColorPLAGUE);
						            }
								}
							} else {
								if (_energy > Utils.SCOURGE_ENERGY_CONSUMPTION) {
									if ((!_isaconsumer) && (!_isafungus)) {
										org._nVirusChildren = -1;
									}
						            org.reproduceforeignVirus();
						            setColor(Utils.ColorPLAGUE);
					            }
							}
						}
					}
				}
			}
			if (org._nVirusChildren != 0) {
				if (_isinfectious) {
					if (_isaplant) {
						if (_plagueversion == 2) {
							useEnergy(Utils.PLAGUE_ENERGY_CONSUMPTION);
						} else {
							useEnergy(0.5 * Utils.PLAGUE_ENERGY_CONSUMPTION);
						}
			            org.setColor(Utils.ColorDARKLILAC);
					} else {
			            useEnergy(Utils.SCOURGE_ENERGY_CONSUMPTION);
			            org.setColor(Utils.ColorDARKLILAC);
					}
					if (org._antiviral > 0) {
						virusneutralized();
					}
				} else {
		            useEnergy(Utils.SCOURGE_ENERGY_CONSUMPTION);
		            org.setColor(Utils.ColorDARKLILAC);
				}
				org._nVirusChildren = 0;
			}
			break;
		case VIOLET:
			// Violet segment: Poison another segment and make it useless
			if (org._hasdodged == false) {
				if ((org._isonlyc4 != 2) || ((org._framesColor > 8) && ((org._healing == 0) || (_hasgoodvision)))) {
					org._hasdodged =true;
				}
		    }
			switch (getTypeColor(org._segColor[oseg])) {
			case LIGHTBROWN:
			case GREENBROWN:
			case OLDBARK:
			case OCHRE:
			case SPIKEPOINT:
			case VISION:
			case ICE:
			case DEADBARK:
			case BROWN:
				break;
			case DARKFIRE:
				if ((org._mphoto[oseg] == -20) && (org.active) && (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION))) {
					if (org._isaplant) {
					    org._segColor[oseg] = Utils.ColorGREENBROWN;
					} else {
						org._segColor[oseg] = Utils.ColorLIGHTBROWN;
					}
					setColor(Utils.ColorVIOLET);
					org._isinjured =true;
				}
				break;
			case VIOLET:
			case WHITE:
				if ((!_isaconsumer) && (!_isafungus) && ((_isaplant) || (_iscoral) || (org._iscoral))) {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						org._isinjured =true;
					}
				} else {
					if ((org._isaplant) || (org._isaconsumer) || (org._isafungus) || (org._plagueversion > 0)) {
					    if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
					    	if (org._isaplant) {
							    org._segColor[oseg] = Utils.ColorGREENBROWN;
							} else {
								org._segColor[oseg] = Utils.ColorLIGHTBROWN;
							}
					    	org._mphoto[oseg] = -20;
						    setColor(Utils.ColorVIOLET);
						    org._isinjured =true;
					    }
					}
				}
				break;
			case BLUE:
				if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
					if (org._isenhanced) {
					    useEnergy(Utils.between((0.5 * Math.sqrt(org._m[oseg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, _energy));
						setColor(Utils.ColorDARKLILAC);
						if (_energy < Utils.tol) {
							die(org);
						}
					} else {
						setColor(Utils.ColorVIOLET);
					}
					org.setColor(Color.BLUE);
				} else {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						if (!org._isaplant) {
							org._updateEffects = 2;
						}
						org._isinjured =true;
					}
				}
				break;
			case SKY:
				if ((org._skyversion == 2) || (org._isaplant) || (!_isfrozen)) {
					if (org.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
						org._segColor[oseg] = Utils.ColorDEEPSKY;
						org._mphoto[oseg] = -20;
						org._useextraeffects = true;
						org.setColor(Utils.ColorDEEPSKY);
						setColor(Utils.ColorVIOLET);
					} else {
						if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
							if (org._isaplant) {
							    org._segColor[oseg] = Utils.ColorGREENBROWN;
							} else {
								org._segColor[oseg] = Utils.ColorLIGHTBROWN;
							}
							org._mphoto[oseg] = -20;
							setColor(Utils.ColorVIOLET);
							org.segmentsRestoreEffects();
							if ((!org._isaplant) && (!org._geneticCode.getModifiessky())) {
								org._updateEffects = 2;
							}
						}
					}
				}
				break;
			case DEEPSKY:
				if ((org._skyversion == 2) || (org._isaplant) || (!_isfrozen)) {
					org.setColor(Utils.ColorDEEPSKY);
					setColor(Utils.ColorVIOLET);
				}
				break;
			case FALLOW:
				if ((!_isaconsumer) && (!_isafungus) && (!_isinfectious)) {
				    if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
				    	if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
				    	org._mphoto[oseg] = -20;
					    setColor(Utils.ColorVIOLET);
					    org._updateEffects = 2;
					    org._isinjured =true;
				    }
				}
				break;
			case FIRE:
				if (((!_isaconsumer) && (!_isafungus)) || (org._isenhanced)) {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						org._isinjured =true;
					}
				} else {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						org._segColor[oseg] = Utils.ColorDARKFIRE;
						org._mphoto[oseg] = -20.4;
						org._useextraeffects = true;
						setColor(Utils.ColorVIOLET);
						org._isinjured =true;
					}
				}
				break;
			case MAROON:
				if ((org._healing > 0) && (org._framesColor <= 0) && ((_isaconsumer) || (_isafungus)) && (org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION))) {
				    org.setColor(Color.MAGENTA);
				    setColor(Utils.ColorVIOLET);
				} else {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						org._updateEffects = 2;
						org._isinjured =true;
					}
				}
				break;
			case PINK:
				if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
					if (org._isaplant) {
					    org._segColor[oseg] = Utils.ColorGREENBROWN;
					} else {
						org._segColor[oseg] = Utils.ColorLIGHTBROWN;
					}
					org._mphoto[oseg] = -20;
					setColor(Utils.ColorVIOLET);
					org._updateEffects = 2;
					org._isinjured =true;
				}
				break;
			case CREAM:
				if ((!_isaconsumer) && (!_isafungus) && (!_isinfectious)) {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
					    setColor(Utils.ColorVIOLET);
					    org._isinjured =true;
				    }
				} else {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						org._segColor[oseg] = Utils.ColorDARKFIRE;
						org._mphoto[oseg] = -20.5;
						org._useextraeffects = true;
						setColor(Utils.ColorVIOLET);
						org._isinjured =true;
					}
				}
				break;
			case SILVER:
				if (((!_isaconsumer) && (!_isafungus)) || (_nTotalKills >= org._nTotalKills) || ((!org._isaconsumer) && (!org._isafungus))) {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
					    setColor(Utils.ColorVIOLET);
					    org._isinjured =true;
					}
				}
				break;
			case SPIKE:
				if (((!_isaconsumer) && (!_isafungus) && (org._isaplant)) || (org._isenhanced)) {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						org._isinjured =true;
					}
				}
				break;
			case LILAC:
			case DARKLILAC:
				if ((!_isaconsumer) && (!_isafungus)) {
				    if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
				    	if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
				    	org._mphoto[oseg] = -20;
					    setColor(Utils.ColorVIOLET);
					    org._isinjured =true;
				    }
				}
				break;
			case LEAF:
				if ((_isaconsumer) || (_isaplant) || (_isafungus)) {
					if (org.active) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorVIOLET);
						} else {
							if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
								if ((!org._modifiesleaf) || (org._framesColor > 0)) {
									org._segColor[oseg] = Utils.ColorGREENBROWN;
									org._updateEffects = 2;
									setColor(Utils.ColorVIOLET);
									org._isinjuredplant =true;
									org._isinjured =true;
								} else {
									// The other organism will be shown in light blue
									org.setColortwoFrames(Utils.ColorLIGHT_BLUE);
									setColor(Utils.ColorVIOLET);
								}
							}
						}
					} else {
						if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
							for (int a = 0; a < org._segments; a++) {
								switch (getTypeColor(org._segColor[a])) {
								case LEAF:
								case DARKGREEN:
									org._segColor[a] = Utils.ColorGREENBROWN;
									break;
								}
							}
							setColor(Utils.ColorVIOLET);
							org._framesColor = 0;
							org._isinjured =true;
							org._isfrozen =true;
							org._allfrozen =true;
						}
					}
				}
				break;
			case GREEN:
			case GRASS:
			case FOREST:
			case SUMMER:
			case LIME:
			case C4:
			case PURPLE:
				if ((_isaconsumer) || (_isaplant) || (_isafungus)) {
					if (org.active) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorVIOLET);
						} else {
							if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
								org._segColor[oseg] = Utils.ColorGREENBROWN;
								org._updateEffects = 2;
								setColor(Utils.ColorVIOLET);
								org._isinjuredplant =true;
								org._isinjured =true;
							}
						}
					} else {
						if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
							for (int a = 0; a < org._segments; a++) {
								switch (getTypeColor(org._segColor[a])) {
								case GREEN:
								case GRASS:
								case FOREST:
								case SUMMER:
								case LIME:
								case C4:
								case PURPLE:
								case DARKGREEN:
									org._segColor[a] = Utils.ColorGREENBROWN;
									break;
								}
							}
							setColor(Utils.ColorVIOLET);
							org._framesColor = 0;
							org._isinjured =true;
							org._isfrozen =true;
							org._allfrozen =true;
						}
					}
				}
				break;
			case SPRING:
			case DARKGREEN:
				if ((_isaconsumer) || (_isaplant) || (_isafungus)) {
					if (org.active) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorVIOLET);
						} else {
							if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
								org._segColor[oseg] = Utils.ColorGREENBROWN;
								org._updateEffects = 2;
								setColor(Utils.ColorVIOLET);
								org._isinjuredplant =true;
								org._isinjured =true;
								org.segmentsCheckPlant();
							}
						}
					} else {
						if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
							for (int a = 0; a < org._segments; a++) {
								switch (getTypeColor(org._segColor[a])) {
								case GREEN:
								case FOREST:
								case SPRING:
								case SUMMER:
								case WINTER:
								case LIME:
								case LEAF:
								case C4:
								case GRASS:
								case JADE:
								case DARKJADE:
								case DARKGREEN:
									org._segColor[a] = Utils.ColorGREENBROWN;
									break;
								}
							}
							setColor(Utils.ColorVIOLET);
							org._framesColor = 0;
							org._isinjured =true;
							org._isfrozen =true;
							org._allfrozen =true;
						}
					}
				}
				break;
			case WINTER:
				if ((_isaconsumer) || (_isaplant) || (_isafungus)) {
					if (org.active) {
						if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
							org.setColor(Utils.ColorTEAL);
							setColor(Utils.ColorVIOLET);
						} else {
							if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
								org._segColor[oseg] = Utils.ColorGREENBROWN;
								setColor(Utils.ColorVIOLET);
								org._isinjuredplant =true;
								org._isinjured =true;
							}
						}
					} else {
						if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
							for (int a = 0; a < org._segments; a++) {
								switch (getTypeColor(org._segColor[a])) {
								case WINTER:
								case DARKGREEN:
									org._segColor[a] = Utils.ColorGREENBROWN;
									break;
								}
							}
							setColor(Utils.ColorVIOLET);
							org._framesColor = 0;
							org._isinjured =true;
							org._isfrozen =true;
							org._allfrozen =true;
						}
					}
				}
				break;
			case JADE:
				if ((_isaconsumer) || (_isaplant) || (_isafungus)) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorVIOLET);
					} else {
						org._segColor[oseg] = Utils.ColorDARKJADE;
					    setColor(Utils.ColorVIOLET);
						org._useextraeffects =true;
					}
				}
				break;
			case DARKJADE:
				if ((_isaconsumer) || (_isaplant) || (_isafungus)) {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorVIOLET);
					} else {
						org._segColor[oseg] = Utils.ColorDARKGREEN;
					    setColor(Utils.ColorVIOLET);
					    org._useextraeffects =true;
					}
				}
				break;
			case DARKGRAY:
			case CYAN:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Utils.ColorVIOLET);
				} else {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						org._isinjured =true;
					}
				}
				break;
			case TEAL:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Utils.ColorVIOLET);
				} else {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						if (org._geneticCode.getPassive()) {
							org._updateEffects = 2;
						}
						org._isinjured =true;
					}
				}
				break;
			case AUBURN:
			case INDIGO:
			case FLOWER:
			case GOLD:
			case SPORE:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Utils.ColorVIOLET);
				} else {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						org.segmentsRestoreEffects();
						org._updateEffects = 2;
					}
				}
				break;
			case YELLOW:
			case BLOND:
				if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Utils.ColorVIOLET);
				} else {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						org.segmentsRestoreEffects();
					}
				}
				break;
			case ROSE:
				if (_altruist) {
                break;
				} else {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorVIOLET);
					} else {
						if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
							if (org._isaplant) {
							    org._segColor[oseg] = Utils.ColorGREENBROWN;
							} else {
								org._segColor[oseg] = Utils.ColorLIGHTBROWN;
							}
							org._mphoto[oseg] = -20;
							setColor(Utils.ColorVIOLET);
							org._updateEffects = 2;
							org._isinjured =true;
						}
					}
				}
				break;
			case MAGENTA:
				if (_altruist) {
				break;
				} else {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorVIOLET);
					} else {
						if ((org._isenhanced) && (!org._isaconsumer) && (!org._isafungus) && (org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION))) {
						    org.setColor(Color.MAGENTA);
						    setColor(Utils.ColorVIOLET);
						} else {
							if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
								if (org._isaconsumer) {
									if (org._isaplant) {
									    org._segColor[oseg] = Utils.ColorGREENBROWN;
									} else {
										org._segColor[oseg] = Utils.ColorLIGHTBROWN;
									}
								} else {
									org._segColor[oseg] = Utils.ColorDARKFIRE;
									org._useextraeffects = true;
								}
								org._mphoto[oseg] = -20;
								setColor(Utils.ColorVIOLET);
								org.segmentsRestoreEffects();
							}
						}
					}
				}
				break;
			case MINT:
				if (_altruist) {
				break;
				} else {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorVIOLET);
					} else {
						if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
							if (org._isaplant) {
							    org._segColor[oseg] = Utils.ColorGREENBROWN;
							} else {
								org._segColor[oseg] = Utils.ColorLIGHTBROWN;
							}
							org._mphoto[oseg] = -20;
							setColor(Utils.ColorVIOLET);
							org.segmentsRestoreEffects();
						}
					}
				}
				break;
			case LAVENDER:
				if (_altruist) {
				break;
				} else {
					if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorVIOLET);
					} else {
						if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
							if (org._isaplant) {
							    org._segColor[oseg] = Utils.ColorGREENBROWN;
							} else {
								org._segColor[oseg] = Utils.ColorLIGHTBROWN;
							}
							org._mphoto[oseg] = -20;
							setColor(Utils.ColorVIOLET);
							org.segmentsRestoreEffects();
							org._updateEffects = 2;
						}
					}
				}
				break;
			case OLIVE:
				if (org.useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
					if (_isaplant) {
					    _segColor[seg] = Utils.ColorBROKEN;
					} else {
						_segColor[seg] = Utils.ColorLIGHTBROWN;
					}
					_mphoto[seg] = -20.1;
					_isinjured =true;
					if ((org._healing == 0) && (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION))) {
						org._segColor[oseg] = Utils.ColorDARKOLIVE;
						org._mphoto[oseg] = -20;
						org._useextraeffects = true;
						org.setColor(Utils.ColorOLIVE);
						setColor(Utils.ColorVIOLET);
					} else {
						org.setColor(Utils.ColorOLIVE);
					}
				} else {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						if (!org._isaplant) {
							org._updateEffects = 2;
						}
						org._isinjured =true;
					}
				}
				break;
			case DARKOLIVE:
				if (org.useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
					if (_isaplant) {
					    _segColor[seg] = Utils.ColorBROKEN;
					} else {
						_segColor[seg] = Utils.ColorLIGHTBROWN;
					}
					_mphoto[seg] = -20.1;
					_isinjured =true;
					if ((org._healing == 0) && (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION))) {
						org._segColor[oseg] = Utils.ColorLIGHT_BLUE;
						org._mphoto[oseg] = -20;
						org.setColor(Utils.ColorOLIVE);
						setColor(Utils.ColorVIOLET);
						if (!org._isaplant) {
							org._updateEffects = 2;
						}
						org._isinjured =true;
					} else {
						org.setColor(Utils.ColorOLIVE);
					}
				} else {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						if (!org._isaplant) {
							org._updateEffects = 2;
						}
						org._isinjured =true;
					}
				}
				break;
			case PLAGUE:
				if ((org._isinfectious) || ((!_isaconsumer) && (!_isafungus))) {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						org._isinjured =true;
					}
				}
				break;
			case FRUIT:
				if ((!_isaconsumer) && (!_isafungus)) {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						org._isinjured =true;
					}
				}
				break;
			case CORAL:
				if ((!_isaplant) && (!_isaconsumer) && (!_isafungus)) {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						if ((org._gold > 0) && (!org._isaplant) && (!org._isaconsumer) && (!org._isafungus)) {
							org._updateEffects = 2;
						}
						org._isinjured =true;
					}
				} else {
					if ((org._healing > 0) && (org._framesColor <= 0) && ((_isaconsumer) || (_isafungus)) && (org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION))) {
					    org.setColor(Color.MAGENTA);
					    setColor(Utils.ColorVIOLET);
					} else {
						if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
							if (org._isaplant) {
							    org._segColor[oseg] = Utils.ColorGREENBROWN;
							} else {
								org._segColor[oseg] = Utils.ColorLIGHTBROWN;
							}
							org._mphoto[oseg] = -20;
							setColor(Utils.ColorVIOLET);
							if ((org._gold > 0) && (!org._isaplant) && (!org._isaconsumer) && (!org._isafungus)) {
								org._updateEffects = 2;
							}
							org._isinjured =true;
						}
					}
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				} else {
					if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorGREENBROWN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorVIOLET);
						org._isinjured =true;
					}
				}
				break;
			case BARK:
				org._segColor[oseg] = Utils.ColorOLDBARK;
				if (org.active) {
					org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
					org._mphoto[oseg] = -0.5;
				}
				break;
			default:
				if (useEnergy(Utils.VIOLET_ENERGY_CONSUMPTION)) {
					if (org._isaplant) {
					    org._segColor[oseg] = Utils.ColorGREENBROWN;
					} else {
						org._segColor[oseg] = Utils.ColorLIGHTBROWN;
					}
					if (org._mphoto[oseg] <= 0) {
						org._mphoto[oseg] = -20;
					}
					setColor(Utils.ColorVIOLET);
					org._isinjured =true;
				}
			}
			break;
		case SKY:
			// Sky segment: Freeze another organism by disabling its photosynthetic and movement segments, protects itself with a frost shield
			if (org._hasdodged == false) {
				if ((_skyversion == 1) || ((!org._canreact) && (org._framesColor > 0))) {
					if ((org._isonlyc4 != 2) || (org._healing == 0) || (_hasgoodvision)) {
						org._dodge =false;
					}
				}
				if ((org._isonlyc4 != 2) || (org._healing == 0) || (_hasgoodvision)) {
					org._hasdodged =true;
				}
			}
			switch (getTypeColor(org._segColor[oseg])) {
			case SKY:
				if (_skyversion == 1) {
					if (org._canmove) {
						if (org._skyversion == 2) {
							org.paralyzed(this);
						}
					}
				} else {
					if (org._skyversion == 2) {
						if (useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
		                	if (org.useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
		                		_segColor[seg] = Utils.ColorDEEPSKY;
		                		_mphoto[seg] = -20;
		    					_useextraeffects = true;
		    					org._segColor[oseg] = Utils.ColorDEEPSKY;
		    					org._mphoto[oseg] = -20;
		    					org._useextraeffects = true;
		    					setColor(Utils.ColorDEEPSKY);
		    				    org.setColor(Utils.ColorDEEPSKY);
		                	} else {
		                		if (!org._allfrozen) {
									org.frozen(this);
								}
		                	}
		                }
					} else {
						if ((_isaplant) && (!_isenhanced) && (org.useEnergy(16 * Utils.SKY_ENERGY_CONSUMPTION))) {
	    					org._segColor[oseg] = Utils.ColorDEEPSKY;
	    					org._mphoto[oseg] = -20;
	    					org._useextraeffects = true;
	    					setColor(Utils.ColorSKY);
	    					org.setColor(Utils.ColorDEEPSKY);
	    				} else {
	    					if (!org._allfrozen) {
								org.frozen(this);
							}
	    				}
	            	}
				}
			    break;
			case DEEPSKY:
				if (_skyversion == 1) {
					if (org._canmove) {
						if (org._skyversion == 2) {
							org.paralyzed(this);
						}
					}
				} else {
					if (org._skyversion == 2) {
						if (!org._allfrozen) {
							org.frozen(this);
						}
					} else {
						if ((_isaplant) && (!_isenhanced) && (org.useEnergy(16 * Utils.SKY_ENERGY_CONSUMPTION))) {
	    					setColor(Utils.ColorSKY);
	    					org.setColor(Utils.ColorDEEPSKY);
	    				} else {
	    					if (!org._allfrozen) {
								org.frozen(this);
							}
	    				}
					}
				}
				break;
			case LEAF:
				if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Utils.ColorSKY);
				} else {
					if (_skyversion == 1) {
						if (org._canmove) {
							if ((org._skyversion != 1) || (_isaplant) || (org._isaplant) || (_isaconsumer) || (org._isaconsumer) || (_isafungus) || (org._isafungus)) {
								org.paralyzed(this);
							}
						}
					} else {
						if (!org._allfrozen) {
							if ((!org._modifiesleaf) || (org._framesColor > 0)) {
								org.frozen(this);
							} else {
								// The other organism will be shown in light blue
								org.setColortwoFrames(Utils.ColorLIGHT_BLUE);
								setColor(Utils.ColorSKY);
							}
						}
					}
				}
				break;
			case GREEN:
			case GRASS:
			case FOREST:
			case SPRING:
			case SUMMER:
            case WINTER:
			case LIME:
			case C4:
			case DARKGREEN:
			case PURPLE:
			case TEAL:
			case CYAN:
			case YELLOW:
			case AUBURN:
			case INDIGO:
			case BLOND:
			case FLOWER:
			case DARKGRAY:
			case GOLD:
			case SPORE:
				if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Utils.ColorSKY);
				} else {
					if (_skyversion == 1) {
						if (org._canmove) {
							if ((org._skyversion != 1) || (_isaplant) || (org._isaplant) || (_isaconsumer) || (org._isaconsumer) || (_isafungus) || (org._isafungus)) {
								org.paralyzed(this);
							}
						}
					} else {
						if (!org._allfrozen) {
							org.frozen(this);
						}
					}
				}
				break;
			case BLUE:
				if (org.active) {
					if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
						if (org._isenhanced) {
						    useEnergy(Utils.between((0.5 * Math.sqrt(org._m[oseg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, _energy));
							setColor(Utils.ColorDARKLILAC);
							if (_energy < Utils.tol) {
								die(org);
							}
						} else {
							setColor(Utils.ColorSKY);
						}
						org.setColor(Color.BLUE);
					} else {
						if (_skyversion == 1) {
							if (org._canmove) {
								if ((org._skyversion != 1) || (_isaplant) || (org._isaplant) || (_isaconsumer) || (org._isaconsumer) || (_isafungus) || (org._isafungus)) {
									if ((org._healing == 0) || (!_isaplant) || (_isenhanced)) {
										org.paralyzed(this);
									}
								}
							}
						} else {
							if (!org._allfrozen) {
								org.frozen(this);
							}
						}
					}
				} else {
					if (org._geneticCode.getModifiesspore() >= 10) {
						if ((_skyversion == 2) || (!_isaplant)) {
							if (useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
								for (int a = 0; a < org._segments; a++) {
									switch (getTypeColor(org._segColor[a])) {
									case BLUE:
										org._segColor[a] = Utils.ColorICE;
										org._mphoto[a] = -20;
										break;
									}
								}
								setColor(Utils.ColorSKY);
								org._framesColor = 0;
								org._isinjured =true;
								org._isfrozen =true;
								org._allfrozen =true;
							}
						}
					}
				}
			    break;
			case JADE:
				if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Utils.ColorSKY);
				} else {
					if (_skyversion == 1) {
						if (org._canmove) {
							org.paralyzed(this);
						}
					} else {
						if (!_isenhanced) {
							org._segColor[oseg] = Utils.ColorDARKJADE;
							setColor(Utils.ColorSKY);
							org._useextraeffects =true;
						} else {
							org.frozen(this);
						}
					}
				}
				break;
			case DARKJADE:
				if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					org.setColor(Utils.ColorTEAL);
					setColor(Utils.ColorSKY);
				} else {
					if (_skyversion == 1) {
						if (org._canmove) {
							org.paralyzed(this);
						}
					} else {
						if (!_isenhanced) {
							if ((org._healing == 0) || (!org.active)) {
								org._segColor[oseg] = Utils.ColorDARKGREEN;
								setColor(Utils.ColorSKY);
								org._useextraeffects =true;
							}
						} else {
							org.frozen(this);
						}
					}
				}
				break;
			case MINT:
			case LAVENDER:
			case ROSE:
			case MAGENTA:
				if (_altruist) {
	            break;
				} else {
					if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
					    org.setColor(Utils.ColorTEAL);
					    setColor(Utils.ColorSKY);
				} else {
					if (_skyversion == 1) {
						if (org._canmove) {
							if ((org._skyversion != 1) || (_isaplant) || (org._isaplant) || (_isaconsumer) || (org._isaconsumer) || (_isafungus) || (org._isafungus)) {
								if (org._healing == 0) {
									org.paralyzed(this);
								} else {
									if ((!_isaplant) || (_isenhanced)) {
										if ((org._segColor[oseg].equals(Color.MAGENTA)) && (org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION))) {
										    org.setColor(Color.MAGENTA);
										    setColor(Utils.ColorSKY);
										} else {
											org.paralyzed(this);
										}
									}
								}
							}
						}
					} else {
						if (!org._allfrozen) {
							if ((org._segColor[oseg].equals(Color.MAGENTA)) && (org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION))) {
							    org.setColor(Color.MAGENTA);
							    setColor(Utils.ColorSKY);
							} else {
								org.frozen(this);
							}
						}
					}
				}}
				break;
			case MAROON:
				if (_skyversion == 1) {
					if (org._canmove) {
						if (org._healing == 0) {
							org.paralyzed(this);
						} else {
							if ((!_isaplant) || (_isenhanced)) {
								if ((!_isenhanced) && (org._framesColor <= 0) && ((_isaconsumer) || (_isafungus)) && (org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION))) {
								    org.setColor(Color.MAGENTA);
								    setColor(Utils.ColorSKY);
								} else {
									org.paralyzed(this);
								}
							}
						}
					}
				} else {
					if (!org._allfrozen) {
						org.frozen(this);
					}
				}
				break;
			case OLDBARK:
			case VIOLET:
			case GRAY:
			case LIGHTBROWN:
			case GREENBROWN:
			case BROKEN:
			case ICE:
			case DEADBARK:
				if (_skyversion == 1) {
					if (org._canmove) {
						if ((org._skyversion != 1) || (_isaplant) || (org._isaplant) || (_isaconsumer) || (org._isaconsumer) || (_isafungus) || (org._isafungus)) {
							org.paralyzed(this);
						}
					}
				} else {
					if (!org._allfrozen) {
						org.frozen(this);
					}
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				} else {
					if (_skyversion == 1) {
						if (org._canmove) {
							if ((org._skyversion != 1) || (_isaplant) || (org._isaplant) || (_isaconsumer) || (org._isaconsumer) || (_isafungus) || (org._isafungus)) {
								org.paralyzed(this);
							}
						}
					} else {
						if (!org._allfrozen) {
							org.frozen(this);
						}
					}
				}
				break;
			case CORAL:
				if (_skyversion == 1) {
					if (org._canmove) {
						if ((org._skyversion != 1) || (_isaplant) || (org._isaplant) || (_isaconsumer) || (org._isaconsumer) || (_isafungus) || (org._isafungus)) {
							if ((org._healing == 0) || (!_isaplant) || (_isenhanced)) {
								org.paralyzed(this);
							}
						}
					}
				}
				break;
			case CREAM:
			case FRUIT:
			case DARKFIRE:
				if (org.active) {
					if (_skyversion == 1) {
						if (org._canmove) {
							if ((org._skyversion != 1) || (_isaplant) || (org._isaplant) || (_isaconsumer) || (org._isaconsumer) || (_isafungus) || (org._isafungus)) {
								if ((org._healing == 0) || (!_isaplant) || (_isenhanced) || (org._segColor[oseg].equals(Utils.ColorDARKFIRE))) {
									org.paralyzed(this);
								}
							}
						}
					} else {
						if (!org._allfrozen) {
							org.frozen(this);
						}
					}
				} else {
					if (!org._isfrozen) {
						if (org._sporeversion == 1) {
							if (useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
								for (int a = 0; a < org._segments; a++) {
									switch (getTypeColor(org._segColor[a])) {
									case FRUIT:
									case DARKFIRE:
										org._segColor[a] = Utils.ColorICE;
										org._mphoto[a] = -20;
										break;
									}
								}
								setColor(Utils.ColorSKY);
								org._framesColor = 0;
								org._isinjured =true;
								org._isfrozen =true;
								org._allfrozen =true;
							}
						} else {
							if ((_skyversion == 1) && (org._sporeversion == 5)) {
								if (useEnergy(Utils.SKY_ENERGY_CONSUMPTION)) {
									setColor(Utils.ColorSKY);
									org._isfrozen =true;
								}
							}
						}
					}
				}
				break;
			case BARK:
				if (_skyversion == 1) {
					if (org._canmove) {
						org.paralyzed(this);
					}
					org._segColor[oseg] = Utils.ColorOLDBARK;
					if (org.active) {
						org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
						org._mphoto[oseg] = -0.5;
					}
				} else {
					if (!org._allfrozen) {
						org.frozen(this);
					}
				}
				break;
			case FALLOW:
				if (_skyversion == 1) {
					if ((org._canmove) && ((org._isaplant) || (org._isinfectious))) {
						org.paralyzed(this);
					}
				}
				break;
			case SPIKE:
				if ((org._isaplant) || (org._isenhanced)) {
					if (_skyversion == 1) {
						if (org._canmove) {
							if ((org._skyversion != 1) || (_isaplant) || (org._isaplant) || (_isaconsumer) || (org._isaconsumer) || (_isafungus) || (org._isafungus)) {
								org.paralyzed(this);
							}
						}
					} else {
						if (!org._allfrozen) {
							org.frozen(this);
						}
					}
				}
				break;
			case OLIVE:
			case SPIKEPOINT:
			case BROWN:
				break;
			default:
				if (_skyversion == 1) {
					if (org._canmove) {
						if ((org._skyversion != 1) || (_isaplant) || (org._isaplant) || (_isaconsumer) || (org._isaconsumer) || (_isafungus) || (org._isafungus)) {
							if ((org._healing == 0) || (!_isaplant) || (_isenhanced)) {
								org.paralyzed(this);
							}
						}
					}
				} else {
					if (!org._allfrozen) {
						org.frozen(this);
					}
				}
		    }
		    break;
		case OLIVE:
			// Olive segment: Crack defense
			switch (getTypeColor(org._segColor[oseg])) {
			case OLIVE:
				if (org._isaplant) {
					if ((!_isaplant) || ((_isenhanced) && (org._isinfectious) && (!_isinfectious))) {
						if (useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
							org._segColor[oseg] = Utils.ColorLIGHT_BLUE;
							org._mphoto[oseg] = -20;
							setColor(Utils.ColorOLIVE);
							org._isinjured =true;
						}
					} else {
						if ((org._isinfectious) || (!_isinfectious)) {
							if (org.useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
						    	org._segColor[oseg] = Utils.ColorDARKOLIVE;
						    	org._mphoto[oseg] = -20;
						    	org._useextraeffects = true;
						    } else {
						    	if (useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
									org._segColor[oseg] = Utils.ColorLIGHT_BLUE;
									org._mphoto[oseg] = -20;
									setColor(Utils.ColorOLIVE);
									org._isinjured =true;
								}
						    }
						}
				    }
				}
				break;
			case DARKOLIVE:
				if (org._isaplant) {
					if ((!_isaplant) || ((_isenhanced) && (org._isinfectious) && (!_isinfectious))) {
						if (useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
							org._segColor[oseg] = Utils.ColorLIGHT_BLUE;
							org._mphoto[oseg] = -20;
							setColor(Utils.ColorOLIVE);
							org._isinjured =true;
						}
					}
				}
				break;
			case BLUE:
				if (org.active) {
					if ((!_isenhanced) && ((!org._isaplant) || ((_isinfectious) && (_isaplant))) && (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION))) {
						setColor(Utils.ColorOLIVE);
						org.setColor(Color.BLUE);
					} else {
						if (useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
							org._segColor[oseg] = Utils.ColorLIGHT_BLUE;
							org._mphoto[oseg] = -20;
							setColor(Utils.ColorOLIVE);
							if (!org._isaplant) {
								org._updateEffects = 2;
							}
							org._isinjured =true;
						}
					}
				} else {
					if (org._geneticCode.getModifiesspore() <= 9) {
						if (((!_isaplant) && (!_isinfectious)) || (_iscoral) || (_isenhanced)) {
							if (useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
								for (int a = 0; a < org._segments; a++) {
									switch (getTypeColor(org._segColor[a])) {
									case BLUE:
										org._segColor[a] = Utils.ColorLIGHT_BLUE;
										org._mphoto[a] = -20;
										break;
									}
								}
								setColor(Utils.ColorOLIVE);
								org._framesColor = 0;
								org._isinjured =true;
								org._isfrozen =true;
								org._allfrozen =true;
							}
						}
					}
				}
				break;
			case OCHRE:
				if ((_isenhanced) || (org._isaplant)) {
					if (useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
						org._segColor[oseg] = Utils.ColorLIGHT_BLUE;
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorOLIVE);
						org._isinjured =true;
					}
				}
				break;
			case SKY:
			case DEEPSKY:
				if (useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
					org._segColor[oseg] = Utils.ColorLIGHT_BLUE;
					org._mphoto[oseg] = -20;
					setColor(Utils.ColorOLIVE);
					org.segmentsRestoreEffects();
					if ((!org._isaplant) && (!org._geneticCode.getModifiessky())) {
						org._updateEffects = 2;
					}
				}
				break;
			case LILAC:
			case DARKLILAC:
			case SPIKE:
			case SPIKEPOINT:
				if (useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
					if (org._isaplant) {
					    org._segColor[oseg] = Utils.ColorBROKEN;
					} else {
						org._segColor[oseg] = Utils.ColorLIGHTBROWN;
					}
					org._mphoto[oseg] = -20.1;
					setColor(Utils.ColorOLIVE);
					org._isinjured =true;
				}
				break;
			case FALLOW:
				if (((!_isaconsumer) && (!_isafungus) && ((!_isinfectious) || (!_isaplant))) || (_isenhanced)) {
					if (useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
						if (org._isaplant) {
						    org._segColor[oseg] = Utils.ColorBROKEN;
						} else {
							org._segColor[oseg] = Utils.ColorLIGHTBROWN;
						}
						org._mphoto[oseg] = -20;
						setColor(Utils.ColorOLIVE);
						org._updateEffects = 2;
						org._isinjured =true;
					}
				}
				break;
			case BARK:
				if ((((!_isaplant) && (!_isaconsumer)) || (org._isaconsumer) || (_isenhanced) || (org._isenhanced)) && (org.active)) {
					if (useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
						org._segColor[oseg] = Utils.ColorDEADBARK;
						org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
						org._mphoto[oseg] = -0.5;
						setColor(Utils.ColorOLIVE);
						org._isfrozen =true;
					}
				} else {
					org._segColor[oseg] = Utils.ColorOLDBARK;
					if (org.active) {
						org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
						org._mphoto[oseg] = -0.5;
					}
				}
				break;
			case OLDBARK:
				if ((((!_isaplant) && (!_isaconsumer)) || (org._isaconsumer) || (_isenhanced) || (org._isenhanced)) && (org.active)) {
					if (useEnergy(Utils.OLIVE_ENERGY_CONSUMPTION)) {
						org._segColor[oseg] = Utils.ColorDEADBARK;
						org._mphoto[oseg] = -0.5;
						setColor(Utils.ColorOLIVE);
						org._isfrozen =true;
					}
				}
				break;
			case DARK:
				if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				}
				break;
			default:
				break;
			}
			break;
		case LILAC:
			// Lilac segment: Weaken organisms
			double takenEnergyLilac = 0;
			if (org._hasdodged == false) {
				org._hasdodged =true;
		    }
			switch (getTypeColor(org._segColor[oseg])) {
			case LILAC:
				if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
					takenEnergyLilac = Utils.between((7.25 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in dark lilac
					org.setColor(Utils.ColorDARKLILAC);
					// This organism will be shown in lilac
					setColor(Utils.ColorLILAC);
					// Organism has to recharge
					if (!_isenhanced) {
						_segColor[seg] = Utils.ColorDARKLILAC;
						_mphoto[seg] = -20;
						_useextraeffects = true;
					}
					if (!org._isenhanced) {
						org._segColor[oseg] = Utils.ColorDARKLILAC;
						org._mphoto[oseg] = -20;
						org._useextraeffects = true;
					}
				}
				break;
			case WHITE:
			case PLAGUE:
				if ((_modifieslilac) || (org._isaconsumer) || (org._isafungus) || (org._plagueversion > 0) || (org._fallowversion > 0)
			    	|| ((org._iscoral) && (!_isaplant) && (!_isaconsumer))) {
					if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
		    			takenEnergyLilac = Utils.between((10 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in dark lilac
						org.setColor(Utils.ColorDARKLILAC);
						// This organism will be shown in lilac
						setColor(Utils.ColorLILAC);
						// Organism has to recharge
						if (!_isenhanced) {
							_segColor[seg] = Utils.ColorDARKLILAC;
							_mphoto[seg] = -20;
							_useextraeffects = true;
						}
					}
		    	}
		    	break;
		    case OLDBARK:
		    case DEADBARK:
		    case ICE:
		    case CORAL:
		    case FRUIT:
		    	if ((_modifieslilac) || (org._isaconsumer) || (org._isafungus) || (org._plagueversion > 0) || (org._fallowversion > 0)
		    		|| ((org._iscoral) && (!_isaplant) && (!_isaconsumer))) {
		    		if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
		    			if (org._lavender > 0) {
							// Get energy depending on segment length reduced by the amount of lavender
							takenEnergyLilac = Utils.between(((10 * Math.sqrt(_m[seg])) - (org._lavender/50)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							if (takenEnergyLilac > 0) {
								// The other organism will be shown in dark lilac
								org.setColor(Utils.ColorDARKLILAC);
							} else {
								// The other organism will be shown in light blue
								org.setColor(Utils.ColorLIGHT_BLUE);
							}
							org._lavender = 0;
						} else {
							// Get energy depending on segment length
							takenEnergyLilac = Utils.between((10 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
							org.setColor(Utils.ColorDARKLILAC);
						}
						// This organism will be shown in lilac
						setColor(Utils.ColorLILAC);
						// Organism has to recharge
						if (!_isenhanced) {
							_segColor[seg] = Utils.ColorDARKLILAC;
							_mphoto[seg] = -20;
							_useextraeffects = true;
						}
					}
		    	}
		    	break;
		    case DARK:
		    	if ((org._framesColor <= 0) && (org._blackversion > 0)) {
					// The other organism will be shown in the color it mimicks
					org.mimicColor();
				} else {
					if ((_modifieslilac) || (org._isaconsumer) || (org._isafungus) || (org._plagueversion > 0) || (org._fallowversion > 0)
						|| ((org._iscoral) && (!_isaplant) && (!_isaconsumer))) {
			    		if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
			    			if (org._lavender > 0) {
								// Get energy depending on segment length reduced by the amount of lavender
								takenEnergyLilac = Utils.between(((10 * Math.sqrt(_m[seg])) - (org._lavender/50)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								if (takenEnergyLilac > 0) {
									// The other organism will be shown in dark lilac
									org.setColor(Utils.ColorDARKLILAC);
								} else {
									// The other organism will be shown in light blue
									org.setColor(Utils.ColorLIGHT_BLUE);
								}
								org._lavender = 0;
							} else {
								// Get energy depending on segment length
								takenEnergyLilac = Utils.between((10 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
								org.setColor(Utils.ColorDARKLILAC);
							}
							// This organism will be shown in lilac
							setColor(Utils.ColorLILAC);
							// Organism has to recharge
							if (!_isenhanced) {
								_segColor[seg] = Utils.ColorDARKLILAC;
								_mphoto[seg] = -20;
								_useextraeffects = true;
							}
						}
			    	}
				}
		    	break;
		    case GRASS:
		    	if ((_modifieslilac) || (org._isaconsumer) || (org._isafungus) || (org._plagueversion > 0) || (org._fallowversion > 0)
		    		|| ((org._iscoral) && (!_isaplant) && (!_isaconsumer))) {
		    		if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorLILAC);
					} else {
						if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
							if (org._lavender > 0) {
								// Get energy depending on segment length reduced by the amount of lavender
								takenEnergyLilac = Utils.between(((5 * Math.sqrt(_m[seg])) - (org._lavender/50)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								if (takenEnergyLilac > 0) {
									// The other organism will be shown in dark lilac
									org.setColor(Utils.ColorDARKLILAC);
								} else {
									// The other organism will be shown in light blue
									org.setColor(Utils.ColorLIGHT_BLUE);
								}
								org._lavender = 0;
							} else {
								// Get energy depending on segment length
								takenEnergyLilac = Utils.between((5 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
								org.setColor(Utils.ColorDARKLILAC);
							}
							// This organism will be shown in lilac
							setColor(Utils.ColorLILAC);
							// Organism has to recharge
							if (!_isenhanced) {
								_segColor[seg] = Utils.ColorDARKLILAC;
								_mphoto[seg] = -20;
								_useextraeffects = true;
							}
						}
					}
		    	}
		    	break;
			case LEAF:
				if ((_modifieslilac) || (org._isaconsumer) || (org._isafungus) || (org._plagueversion > 0) || (org._fallowversion > 0)
					|| ((org._iscoral) && (!_isaplant) && (!_isaconsumer))) {
		    		if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorLILAC);
					} else {
						if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
							if ((!org._modifiesleaf) || (org._framesColor > 0)) {
								if (org._lavender > 0) {
									// Get energy depending on segment length reduced by the amount of lavender
									takenEnergyLilac = Utils.between(((10 * Math.sqrt(_m[seg])) - (org._lavender/50)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									if (takenEnergyLilac > 0) {
										// The other organism will be shown in dark lilac
										org.setColor(Utils.ColorDARKLILAC);
									} else {
										// The other organism will be shown in light blue
										org.setColor(Utils.ColorLIGHT_BLUE);
									}
									org._lavender = 0;
								} else {
									// Get energy depending on segment length
									takenEnergyLilac = Utils.between((10 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in dark lilac
									org.setColor(Utils.ColorDARKLILAC);
								}
								// This organism will be shown in lilac
								setColor(Utils.ColorLILAC);
								// Organism has to recharge
								if (!_isenhanced) {
									_segColor[seg] = Utils.ColorDARKLILAC;
									_mphoto[seg] = -20;
									_useextraeffects = true;
								}
							} else {
								// The other organism will be shown in light blue
								org.setColortwoFrames(Utils.ColorLIGHT_BLUE);
								// This organism will be shown in lilac
								setColor(Utils.ColorLILAC);
							}
						}
					}
		    	}
		    	break;
			case GREEN:
			case FOREST:
			case SPRING:
			case SUMMER:
            case WINTER:
			case LIME:
			case C4:
			case JADE:
			case DARKJADE:
			case DARKGREEN:
			case PURPLE:
			case TEAL:
			case CYAN:
			case YELLOW:
			case AUBURN:
			case INDIGO:
			case BLOND:
			case FLOWER:
			case DARKGRAY:
			case GOLD:
			case SPORE:
				if ((_modifieslilac) || (org._isaconsumer) || (org._isafungus) || (org._plagueversion > 0) || (org._fallowversion > 0)
					|| ((org._iscoral) && (!_isaplant) && (!_isaconsumer))) {
		    		if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorLILAC);
					} else {
						if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
							if (org._lavender > 0) {
								// Get energy depending on segment length reduced by the amount of lavender
								takenEnergyLilac = Utils.between(((10 * Math.sqrt(_m[seg])) - (org._lavender/50)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								if (takenEnergyLilac > 0) {
									// The other organism will be shown in dark lilac
									org.setColor(Utils.ColorDARKLILAC);
								} else {
									// The other organism will be shown in light blue
									org.setColor(Utils.ColorLIGHT_BLUE);
								}
								org._lavender = 0;
							} else {
								// Get energy depending on segment length
								takenEnergyLilac = Utils.between((10 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
								org.setColor(Utils.ColorDARKLILAC);
							}
							// This organism will be shown in lilac
							setColor(Utils.ColorLILAC);
							// Organism has to recharge
							if (!_isenhanced) {
								_segColor[seg] = Utils.ColorDARKLILAC;
								_mphoto[seg] = -20;
								_useextraeffects = true;
							}
						}
					}
		    	}
		    	break;
		    case BLUE:
				if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
					if (org._isenhanced) {
					    useEnergy(Utils.between((0.5 * Math.sqrt(org._m[oseg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, _energy));
						setColor(Utils.ColorDARKLILAC);
						if (_energy < Utils.tol) {
							die(org);
						}
					} else {
						setColor(Utils.ColorLILAC);
					}
					org.setColor(Color.BLUE);
				} else {
					// Doesn't have energy to use the shield
					if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
						if (org._lavender > 0) {
							// Get energy depending on segment length reduced by the amount of lavender
							takenEnergyLilac = Utils.between(((10 * Math.sqrt(_m[seg])) - (org._lavender/50)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							if (takenEnergyLilac > 0) {
								// The other organism will be shown in dark lilac
								org.setColor(Utils.ColorDARKLILAC);
							} else {
								// The other organism will be shown in light blue
								org.setColor(Utils.ColorLIGHT_BLUE);
							}
							org._lavender = 0;
						} else {
							// Get energy depending on segment length
							takenEnergyLilac = Utils.between((10 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
							org.setColor(Utils.ColorDARKLILAC);
						}
						// This organism will be shown in lilac
						setColor(Utils.ColorLILAC);
						// Organism has to recharge
						if (!_isenhanced) {
							_segColor[seg] = Utils.ColorDARKLILAC;
							_mphoto[seg] = -20;
							_useextraeffects = true;
						}
					}
				}
				break;
		    case RED:
		    	if (org._isenhanced) {
				break;
				} else {
					if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
						if (org._lavender > 0) {
							// Get energy depending on segment length reduced by the amount of lavender
							takenEnergyLilac = Utils.between(((10 * Math.sqrt(_m[seg])) - (org._lavender/50)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							if (takenEnergyLilac > 0) {
								// The other organism will be shown in dark lilac
								org.setColor(Utils.ColorDARKLILAC);
							} else {
								// The other organism will be shown in light blue
								org.setColor(Utils.ColorLIGHT_BLUE);
							}
							org._lavender = 0;
						} else {
							// Get energy depending on segment length
							takenEnergyLilac = Utils.between((10 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
							org.setColor(Utils.ColorDARKLILAC);
						}
						// This organism will be shown in lilac
						setColor(Utils.ColorLILAC);
						// Organism has to recharge
						if (!_isenhanced) {
							_segColor[seg] = Utils.ColorDARKLILAC;
							_mphoto[seg] = -20;
							_useextraeffects = true;
						}
					}
				}
				break;
		    case LAVENDER:
		    	if (_altruist) {
	            break;
		    	} else {
					if ((org._dodge) && (org._framesColor <= 0) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorLILAC);
					} else {
						if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
							if (org._isenhanced) {
								// The other organism will be shown in lavender
								org.setColor(Utils.ColorLAVENDER);
							} else {
								if (org._lavender > 0) {
									// Get energy depending on segment length reduced by the amount of lavender
									takenEnergyLilac = Utils.between(((10 * Math.sqrt(_m[seg])) - (org._lavender/50)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									if (takenEnergyLilac > 0) {
										// The other organism will be shown in dark lilac
										org.setColor(Utils.ColorDARKLILAC);
									} else {
										// The other organism will be shown in light blue
										org.setColor(Utils.ColorLIGHT_BLUE);
									}
									org._lavender = 0;
								} else {
									// Get energy depending on segment length
									takenEnergyLilac = Utils.between((10 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
									// The other organism will be shown in dark lilac
									org.setColor(Utils.ColorDARKLILAC);
								}
							}
							// This organism will be shown in lilac
							setColor(Utils.ColorLILAC);
							// Organism has to recharge
							if (!_isenhanced) {
								_segColor[seg] = Utils.ColorDARKLILAC;
								_mphoto[seg] = -20;
								_useextraeffects = true;
							}
						}
					}
				}
				break;
		    case MINT:
		    case MAGENTA:
		    case ROSE:
				if (_altruist) {
                break;
				} else {
					if ((org._dodge) && (org.useEnergy(Utils.DODGE_ENERGY_CONSUMPTION))) {
						org.setColor(Utils.ColorTEAL);
						setColor(Utils.ColorLILAC);
					} else {
						if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
							if (org._lavender > 0) {
								// Get energy depending on segment length reduced by the amount of lavender
								takenEnergyLilac = Utils.between(((10 * Math.sqrt(_m[seg])) - (org._lavender/50)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								if (takenEnergyLilac > 0) {
									// The other organism will be shown in dark lilac
									org.setColor(Utils.ColorDARKLILAC);
								} else {
									// The other organism will be shown in light blue
									org.setColor(Utils.ColorLIGHT_BLUE);
								}
								org._lavender = 0;
							} else {
								// Get energy depending on segment length
								takenEnergyLilac = Utils.between((10 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
								// The other organism will be shown in dark lilac
								org.setColor(Utils.ColorDARKLILAC);
							}
							// This organism will be shown in lilac
							setColor(Utils.ColorLILAC);
							// Organism has to recharge
							if (!_isenhanced) {
								_segColor[seg] = Utils.ColorDARKLILAC;
								_mphoto[seg] = -20;
								_useextraeffects = true;
							}
						}
					}
				}
				break;
		    case SPIKE:
		    	if ((org._isaplant) || (org._isenhanced)) {
		    		if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
		    			if (org._lavender > 0) {
							// Get energy depending on segment length reduced by the amount of lavender
							takenEnergyLilac = Utils.between(((10 * Math.sqrt(_m[seg])) - (org._lavender/50)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							if (takenEnergyLilac > 0) {
								// The other organism will be shown in dark lilac
								org.setColor(Utils.ColorDARKLILAC);
							} else {
								// The other organism will be shown in light blue
								org.setColor(Utils.ColorLIGHT_BLUE);
							}
							org._lavender = 0;
						} else {
							// Get energy depending on segment length
							takenEnergyLilac = Utils.between((10 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
							org.setColor(Utils.ColorDARKLILAC);
						}
						// This organism will be shown in lilac
						setColor(Utils.ColorLILAC);
						// Organism has to recharge
						if (!_isenhanced) {
							_segColor[seg] = Utils.ColorDARKLILAC;
							_mphoto[seg] = -20;
							_useextraeffects = true;
						}
					}
		    	}
		    	break;
		    case SPIKEPOINT:
		    	if (!_modifieslilac) {
		    		if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
		    			if (org._lavender > 0) {
							// Get energy depending on segment length reduced by the amount of lavender
							takenEnergyLilac = Utils.between(((10 * Math.sqrt(_m[seg])) - (org._lavender/50)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							if (takenEnergyLilac > 0) {
								// The other organism will be shown in dark lilac
								org.setColor(Utils.ColorDARKLILAC);
							} else {
								// The other organism will be shown in light blue
								org.setColor(Utils.ColorLIGHT_BLUE);
							}
							org._lavender = 0;
						} else {
							// Get energy depending on segment length
							takenEnergyLilac = Utils.between((10 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
							org.setColor(Utils.ColorDARKLILAC);
						}
						// This organism will be shown in lilac
						setColor(Utils.ColorLILAC);
						// Organism has to recharge
						if (!_isenhanced) {
							_segColor[seg] = Utils.ColorDARKLILAC;
							_mphoto[seg] = -20;
							_useextraeffects = true;
						}
					}
		    	}
		    	break;
		    case OLIVE:
		    	break;
		    case BROWN:
		    	if (org.alive) {
		    		if ((_modifieslilac) || (!org._isenhanced)) {
			    		if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
							// Get energy depending on segment length
				    		takenEnergyLilac = Utils.between((10 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
							// The other organism will be shown in dark lilac
							org.setColor(Utils.ColorDARKLILAC);
							// This organism will be shown in lilac
							setColor(Utils.ColorLILAC);
							// Organism has to recharge
							if (!_isenhanced) {
								_segColor[seg] = Utils.ColorDARKLILAC;
								_mphoto[seg] = -20;
								_useextraeffects = true;
							}
							if (!org._isenhanced) {
						    	org._isinjured =true;
						    }
						}
			    	}
		    	}
		    	break;
		    case BARK:
				org._segColor[oseg] = Utils.ColorOLDBARK;
				if (org.active) {
					org._photosynthesis -= org._mphoto[oseg]*Utils.scale[org._growthRatio-1];
					org._mphoto[oseg] = -0.5;
				}
				break;
		    default:
		    	if (useEnergy(Utils.LILAC_ENERGY_CONSUMPTION)) {
		    		if (org._lavender > 0) {
						// Get energy depending on segment length reduced by the amount of lavender
						takenEnergyLilac = Utils.between(((10 * Math.sqrt(_m[seg])) - (org._lavender/50)) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						if (takenEnergyLilac > 0) {
							// The other organism will be shown in dark lilac
							org.setColor(Utils.ColorDARKLILAC);
						} else {
							// The other organism will be shown in light blue
							org.setColor(Utils.ColorLIGHT_BLUE);
						}
						org._lavender = 0;
					} else {
						// Get energy depending on segment length
						takenEnergyLilac = Utils.between((10 * Math.sqrt(_m[seg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in dark lilac
						org.setColor(Utils.ColorDARKLILAC);
					}
					// This organism will be shown in lilac
					setColor(Utils.ColorLILAC);
					// Organism has to recharge
					if (!_isenhanced) {
						_segColor[seg] = Utils.ColorDARKLILAC;
						_mphoto[seg] = -20;
						_useextraeffects = true;
					}
				}
			}
			// energy interchange
			if (takenEnergyLilac > 0) {
				org.useEnergy(takenEnergyLilac);
			}
			break;
		// Spore hatching
		case BLUE:
			if (!active) {
				touchHatch(org,seg,oseg);
			}
			break;
		// Yellow segment: Normally used to have more children, viruses try to let other organisms reproduce many viruses at once.
		case YELLOW:
			switch (getTypeColor(org._segColor[oseg])) {
			case BROWN:
				break;
			case BLUE:
				if ((_isinfectious) && (!_isaconsumer) && (!_isaplant) && (!_isafungus) && ((!_transfersenergy) || (org._indigo == 0))) {
					if ((org._isaplant) || (org._isaconsumer) || (org._isafungus)) {
						if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
				        	if (org._isenhanced) {
							    useEnergy(Utils.between((0.5 * Math.sqrt(org._m[oseg])) * Utils.ORGANIC_OBTAINED_ENERGY, 0, _energy));
								setColor(Utils.ColorDARKLILAC);
								if (_energy < Utils.tol) {
									die(org);
								}
							} else {
								setColor(Utils.ColorFALLOW);
							}
					        org.setColor(Color.BLUE);
				        } else {
				        	if (org._timeToReproduce < org._timeToReproduceMax) {
								org._timeToReproduce = org._timeToReproduceMax;
								if (_timeToReproduce == 0) {
									setColor(Utils.ColorFALLOW);
									org.setColor(Utils.ColorFLOWER);
								}
							}
							if (org._infectedGeneticCode == _geneticCode) {
								if ((org._energy > org._reproduceEnergy) || ((!org.active) && (org._energy > 0.5*org._reproduceEnergy))) {
									if (_energy > Utils.VIRUS_ENERGY_CONSUMPTION) {
										setColor(Utils.ColorPLAGUE);
										org._nVirusChildren = 1 + _yellowCounter;
										org.reproduceYellow();
										if (org._nVirusChildren > 0) {
											if (org._antiviral > 0) {
								    			org._infectedGeneticCode = null;
								    			org._savedGeneticCode = null;
								    		}
											org.setColor(Utils.ColorDARKLILAC);
								            useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION);
								            org._nVirusChildren = 0;
								            if (_timeToReproduce < 10) {
								            	_timeToReproduce = 10;
								            }
										}
									}
								}
							}
				        }
					}
				}
				break;
			case MINT:
				if ((_isinfectious) && (!_isaconsumer) && (!_isaplant) && (!_isafungus)) {
					if (org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
					    _segColor[seg] = Utils.ColorLIGHTBROWN;
					    _mphoto[seg] = -20;
					    org.setColor(Utils.ColorMINT);
					    segmentsRestoreEffects();
			    	}
				}
				break;
			default:
				if ((_isinfectious) && (!_isaconsumer) && (!_isaplant) && (!_isafungus) && ((!_transfersenergy) || (org._indigo == 0))) {
					if ((org._isaplant) || (org._isaconsumer) || (org._isafungus)) {
						if (org._timeToReproduce < org._timeToReproduceMax) {
							org._timeToReproduce = org._timeToReproduceMax;
							if (_timeToReproduce == 0) {
								setColor(Utils.ColorFALLOW);
								org.setColor(Utils.ColorFLOWER);
							}
						}
						if (org._infectedGeneticCode == _geneticCode) {
							if ((org._energy > org._reproduceEnergy) || ((!org.active) && (org._energy > 0.5*org._reproduceEnergy))) {
								if (_energy > Utils.VIRUS_ENERGY_CONSUMPTION) {
									setColor(Utils.ColorPLAGUE);
									org._nVirusChildren = 1 + _yellowCounter;
									org.reproduceYellow();
									if (org._nVirusChildren > 0) {
										if (org._antiviral > 0) {
							    			org._infectedGeneticCode = null;
							    			org._savedGeneticCode = null;
							    		}
										org.setColor(Utils.ColorDARKLILAC);
							            useEnergy(Utils.VIRUS_ENERGY_CONSUMPTION);
							            org._nVirusChildren = 0;
							            if (_timeToReproduce < 10) {
							            	_timeToReproduce = 10;
							            }
									}
								}
							}
						}
					}
				}
			}
			break;
		case ROSE:
			// Rose segment: Transfers energy
			switch (getTypeColor(org._segColor[oseg])) {
			case BROWN:
				break;
			default:
				if (_altruist && org._altruist) {
					if ((_growthRatio==1) && (org.active)) {
						if ((_energy > (org._energy+2)) && (_energy > (_reproduceEnergy/2)) && (org._energy < (org._reproduceEnergy/2))) {
							// Transfers energy
						    double takenEnergyRose = Utils.between(0.5 * Utils.ORGANIC_OBTAINED_ENERGY, 0, (_energy - (org._energy+2)));
						    // The other organism will be shown in cyan
						    org.setColor(Color.CYAN);
						    // This organism will be shown in rose
						    setColor(Utils.ColorROSE);
						    org._energy += takenEnergyRose;
						    _energy -= takenEnergyRose;
						}
					}
				}
			}
			break;
		case MINT:
			// Mint segment: Remove an infection, corrupt all white and cream segments.
			switch (getTypeColor(org._segColor[oseg])) {
			case BROWN:
				if (org._isinfectious) {
					if (_infectedGeneticCode == org._geneticCode) {
						if (useEnergy(Utils.MINT_ENERGY_CONSUMPTION)) {
							setColor(Utils.ColorMINT);
							_infectedGeneticCode = null;
							_savedGeneticCode = null;
						}
					}
				}
				break;
			case BLUE:
				if (((org._isinfectious) || (org._iscoral) || (org._plagueversion > 0)
					|| ((org._fallowversion > 0) && ((org._isaplant) || (org._fallowversion == 4) || (_isenhanced)))) && (org.active)) {
					if (((!org._isaplant) || (((_isaconsumer) || (_isafungus)) && (!_isenhanced))) && (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION))) {
						setColor(Utils.ColorMINT);
						org.setColor(Color.BLUE);
					} else {
						org.neutralized(this);
						if (_infectedGeneticCode == org._geneticCode) {
							if (useEnergy(Utils.MINT_ENERGY_CONSUMPTION)) {
								setColor(Utils.ColorMINT);
								_infectedGeneticCode = null;
								_savedGeneticCode = null;
							}
						}
					}
				}
				if (_altruist && org._altruist) {
					if ((org._infectedGeneticCode != _geneticCode) && (org._infectedGeneticCode != null)) {
						if ((org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2))) {
							org._infectedGeneticCode = null;
							org._savedGeneticCode = null;
							org.setColor(Color.CYAN);
							setColor(Utils.ColorMINT);
						}
					}
					if ((org._fallowinhibition > 0) && (_timeToReproduce <= _timeToReproduceMax)) {
						if ((org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2))) {
							org._fallowinhibition = 0;
							org.setColor(Color.CYAN);
							setColor(Utils.ColorMINT);
						}
					}
				}
				break;
			case MINT:
				if (org._isinfectious) {
					if (_infectedGeneticCode == org._geneticCode) {
						if (useEnergy(Utils.MINT_ENERGY_CONSUMPTION)) {
							setColor(Utils.ColorMINT);
							_infectedGeneticCode = null;
							_savedGeneticCode = null;
						}
					}
				}
				if (_altruist && org._altruist) {
					if ((org._infectedGeneticCode != _geneticCode) && (org._infectedGeneticCode != null)) {
						if ((org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2))) {
							org._infectedGeneticCode = null;
							org._savedGeneticCode = null;
							org.setColor(Color.CYAN);
							setColor(Utils.ColorMINT);
						}
					}
					if ((org._fallowinhibition > 0) && (_timeToReproduce <= _timeToReproduceMax)) {
						if ((org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2))) {
							org._fallowinhibition = 0;
							org.setColor(Color.CYAN);
							setColor(Utils.ColorMINT);
						}
					}
				}
				break;
			default:
				if (org.active) {
					if ((org._isinfectious) || (org._iscoral) || (org._plagueversion > 0)
						|| ((org._fallowversion > 0) && ((org._isaplant) || (org._fallowversion == 4) || (_isenhanced)))) {
						org.neutralized(this);
						if (_infectedGeneticCode == org._geneticCode) {
							if (useEnergy(Utils.MINT_ENERGY_CONSUMPTION)) {
								setColor(Utils.ColorMINT);
								_infectedGeneticCode = null;
								_savedGeneticCode = null;
							}
						}
					}
				} else {
					if ((org._sporeversion == 1) || (org._sporeversion == 2)) {
						if (!org._allfrozen) {
							if (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
								for (int a = 0; a < org._segments; a++) {
									switch (getTypeColor(org._segColor[a])) {
									case FRUIT:
									case DARKFIRE:
										org._segColor[a] = Utils.ColorBROKEN;
										org._mphoto[a] = -20;
										break;
									}
								}
								setColor(Utils.ColorMINT);
								org._framesColor = 0;
								org._isinjured =true;
								org._isfrozen =true;
								org._allfrozen =true;
					    	}
						}
						if (_infectedGeneticCode == org._geneticCode) {
							if (useEnergy(Utils.MINT_ENERGY_CONSUMPTION)) {
								setColor(Utils.ColorMINT);
								_infectedGeneticCode = null;
								_savedGeneticCode = null;
							}
						}
					} else {
						if (org._sporeversion == 5) {
							if (!org._allfrozen) {
								if (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/10)) {
									for (int a = 0; a < org._segments; a++) {
										switch (getTypeColor(org._segColor[a])) {
										case CREAM:
										case DARKFIRE:
											org._segColor[a] = Utils.ColorLIGHTBROWN;
											org._mphoto[a] = -20;
											break;
										}
									}
									setColor(Utils.ColorMINT);
									org._framesColor = 0;
									org._isinjured =true;
									org._isfrozen =true;
									org._allfrozen =true;
									org._isaconsumer =false;
						    	}
							}
						}
					}
				}
				if (_altruist && org._altruist) {
					if ((org._infectedGeneticCode != _geneticCode) && (org._infectedGeneticCode != null)) {
						if ((org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2))) {
							org._infectedGeneticCode = null;
							org._savedGeneticCode = null;
							org.setColor(Color.CYAN);
							setColor(Utils.ColorMINT);
						}
					}
					if ((org._fallowinhibition > 0) && (_timeToReproduce <= _timeToReproduceMax)) {
						if ((org.useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MINT_ENERGY_CONSUMPTION/2))) {
							org._fallowinhibition = 0;
							org.setColor(Color.CYAN);
							setColor(Utils.ColorMINT);
						}
					}
				}
			}
			break;
		case LAVENDER:
			// Lavender segment: Immunity vs cream, plague, coral and fallow, it can immunize other organisms temporarily.
			switch (getTypeColor(org._segColor[oseg])) {
			case WHITE:
			case PLAGUE:
			case CORAL:
			case CREAM:
			case FALLOW:
			case FRUIT:
			case BROWN:
				break;
			default:
				if (_altruist && org._altruist) {
					if (_lavender >= Utils.LAVENDER_SHIELD) {
						if (org._lavender < Utils.LAVENDER_SHIELD) {
							org._lavender += _createlavender;
							if (org._lavender >= Utils.LAVENDER_SHIELD) {
								org._lavender = Utils.LAVENDER_SHIELD;
							}
							org.setColor(Color.CYAN);
							setColor(Utils.ColorLAVENDER);
						}
					}
					if (_timeToReproduce <= _timeToReproduceMax) {
						if (org._timeToReproduce > org._timeToReproduceMax) {
				    		org._timeToReproduce -= (_createlavender/20);
							if (org._timeToReproduce < org._timeToReproduceMax) {
								org._timeToReproduce = org._timeToReproduceMax;
							}
							org.setColor(Color.CYAN);
							setColor(Utils.ColorLAVENDER);
						}
					}
				}
			}
			break;
		case MAGENTA:
			// Magenta segment: Heal all sick segments
			switch (getTypeColor(org._segColor[oseg])) {
			case BROWN:
				break;
			default:
				if (org._isinjured && org.active && _altruist && org._altruist) {
					boolean heal =false;
			        for (int j = 0; j < org._segments; j++) {
					if ((org._segColor[j].equals(Utils.ColorLIGHTBROWN)) || (org._segColor[j].equals(Utils.ColorGREENBROWN)) || (org._segColor[j].equals(Utils.ColorBROKEN))
						|| (org._segColor[j].equals(Utils.ColorLIGHT_BLUE)) || (org._segColor[j].equals(Utils.ColorICE)) || (org._segColor[j].equals(Utils.ColorDARKFIRE))) {
						if ((org.useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION/2)) && (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION/2))) {
						        org._segColor[j] = org._geneticCode.getGene(j%org._geneticCode.getNGenes()).getColor();
						        setColor(Color.MAGENTA);
						        heal =true;
				            }
				        }
					}
			        if (heal) {
			        	org._isfrozen =false;
						org._isinjured =false;
						org._allfrozen =false;
			        	org._updateEffects = 2;
						org.segmentsRestoreEffects();
					}
				}
			}
			break;
		}
		// Check if the other organism has died
		if (org.alive && org._energy < Utils.tol) {
			org.die(this);
		}
	}

	/*
	 * Perd velocitat pel fregament.
	 */
	private final void rubbingFramesEffects() {
		dx *= Utils.RUBBING;
		if (Math.abs(dx) < Utils.tol) dx = 0;
		dy *= Utils.RUBBING;
		if (Math.abs(dy) < Utils.tol) dy = 0;
		dtheta *= Utils.RUBBING;
		if (Math.abs(dtheta) < Utils.tol) dtheta = 0;
	}
	/*
	 * Apply segment effects for this frame, used for organisms with cyan and teal segments
	 */
	private final void segmentsFrameEffects() {
			int i;
			for (i=_segments-1; i>=0; i--) {
				// Movement
				if (_mphoto[i] <= -22) {
					if (Utils.random.nextInt(100)<8) {
						if (_mphoto[i] == -22) {
							if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
								dx=Utils.between(dx+11.88d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
							    dy=Utils.between(dy+11.88d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
							    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
							}
						} else {
							if (useEnergy(Utils.CYAN_ENERGY_CONSUMPTION)) {
								dx=Utils.between(dx+12d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
								dy=Utils.between(dy+12d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
								dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
							}
						}
					}
				}
			}
	    }

	/*
	 * Apply segment effects for this frame for plants, only used for photosynthesis
	 */
	private final void leafExtraEffects() {
		    // Get chemotrophic energy from methane
		    if (_methanotrophy > 0) {
			    if ((_age & 0x07) == 0x00) {
				    _energy += _world.methanotrophy(_methanotrophy);
			    }
		    }
		    //Get sun's energy
		    if ((_framesColor == 1) && (!_color.equals(Utils.ColorDARKGREEN))) {
		    	switch (getTypeColor(_color)) {
		    	case RED:
		    	case FIRE:
		    	case DARKFIRE:
		    	case ORANGE:
		    	case MAROON:
		    	case AUBURN:
		    	case GOLD:
		    	case PINK:
		    	case CREAM:
		    	case DARKGRAY:
		    		break;
		    	case SPIKE:
		    		if (!_isenhanced) {
		    			setColorDarkgreen(Utils.ColorDARKGREEN);
		    		}
		    		break;
		    	default:
		    		setColorDarkgreen(Utils.ColorDARKGREEN);
		    		break;
		    	}
			}
		    if (_colonyPhotosynthesis > 0) {
				if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
					if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
						_energy += _world.photosynthesis(_photosynthesis - _leafphoto + _colonyPhotosynthesis);
					}
				} else {
					_energy += _world.photosynthesis(_photosynthesis - _leafphoto + _colonyPhotosynthesis);
				}
				_colonyPhotosynthesis = 0;
			} else {
				if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
					if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
						_energy += _world.photosynthesis(_photosynthesis - _leafphoto);
					}
				} else {
					_energy += _world.photosynthesis(_photosynthesis - _leafphoto);
				}
			}
	    }

	/*
	 * Apply segment effects for this frame for jade plants, used for organisms with additional effects
	 */
	private final void jadeExtraEffects() {
		    double specialphoto = 0;
			int i;
			for (i=_segments-1; i>=0; i--) {
				if (_mphoto[i] > 0) {
					// Manteniment
					switch (getTypeColor(_segColor[i])) {
					// Restore Jade
					case DARKJADE:
						if (Utils.random.nextInt(_jadefactor)<8) {
							_segColor[i] = Utils.ColorJADE;
						} else {
							_useextraeffects = true;
						}
						break;
					case DARKGREEN:
						if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorSUMMER)) {
							if ((_world.getTime() % 3) != _summerinactivity) {
								specialphoto += _mphoto[i];
								if (Utils.random.nextInt(_jadefactor)<8) {
									_segColor[i] = Utils.ColorSUMMER;
								} else {
									_useextraeffects = true;
								}
							} else {
								if (Utils.random.nextInt(_jadefactor)<8) {
									_segColor[i] = Utils.ColorWINTER;
								} else {
									_useextraeffects = true;
								}
							}
						} else {
							if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorLEAF)) {
								if (_framesColor <= 0) {
									specialphoto += _mphoto[i];
								}
							} else {
								if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorLIME)) {
									if (_crowded == true) {
										specialphoto += Utils.CROWDEDLIME_ENERGY_CONSUMPTION * _mphoto[i];
									} else {
										specialphoto += _mphoto[i];
									}
								} else {
									specialphoto += _mphoto[i];
								}
							}
							if (Utils.random.nextInt(_jadefactor)<8) {
								_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
							} else {
								_useextraeffects = true;
							}
						}
						break;
					// Healing
					case GREENBROWN:
					case ICE:
						if (_healing > 0) {
							if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
								if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
									_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
									_isfrozen =false;
									_isinjured =false;
									_allfrozen =false;
									_updateEffects = 2;
									segmentsRestoreEffects();
								}
							}
						}
						break;
					}
				} else {
					if (_mphoto[i] <= -20) {
						if (_mphoto[i] <= -21) {
							// Movement
							if (Utils.random.nextInt(100)<8) {
								if (_mphoto[i] == -22) {
									if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
										dx=Utils.between(dx+11.88d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
									    dy=Utils.between(dy+11.88d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
									    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
									}
								} else if (_mphoto[i] == -23) {
									if (useEnergy(Utils.CYAN_ENERGY_CONSUMPTION)) {
										dx=Utils.between(dx+12d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
										dy=Utils.between(dy+12d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
										dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
									}
								}
							}
						} else {
							switch (getTypeColor(_segColor[i])) {
							// Healing
							case GREENBROWN:
							case BROKEN:
							case LIGHT_BLUE:
							case ICE:
								if (_healing > 0) {
									if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
										if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
											_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
											_isfrozen =false;
											_isinjured =false;
											_allfrozen =false;
											_updateEffects = 2;
											segmentsRestoreEffects();
										}
									}
								}
								break;
							case DARKFIRE:
								if (_healing > 0) {
									if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
										if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
											_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
											darkfireRestoreEffects();
										}
									}
								}
								if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
									if (Utils.random.nextInt(100)<_symmetry) {
										_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
										darkfireRestoreEffects();
									} else {
										_useextraeffects = true;
									}
								} else {
									if (Utils.random.nextInt(100)<2) {
										_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
										darkfireRestoreEffects();
									} else {
										_useextraeffects = true;
									}
								}
								break;
							// Restore abilities
							case DARKOLIVE:
								if (Utils.random.nextInt(100)<8) {
									_segColor[i] = Utils.ColorOLIVE;
									_mphoto[i] = -4;
								} else {
									_useextraeffects = true;
								}
								break;
							case DARKLILAC:
								if (Utils.random.nextInt(100)<8) {
									_segColor[i] = Utils.ColorLILAC;
									_mphoto[i] = -4;
								} else {
									_useextraeffects = true;
								}
								break;
							case DEEPSKY:
								if (Utils.random.nextInt(100)<8) {
									_segColor[i] = Utils.ColorSKY;
									_mphoto[i] = -4;
								} else {
									_useextraeffects = true;
								}
								break;
							}
						}
					}
				}
			}
			// Get chemotrophic energy from methane
			if (_methanotrophy > 0) {
				if ((_age & 0x07) == 0x00) {
					_energy += _world.methanotrophy(_methanotrophy);
				}
			}
			//Get sun's energy
			if ((_leafphoto > 0) && (_framesColor > 0)) {
				if ((_framesColor == 1) && (!_color.equals(Utils.ColorDARKGREEN))) {
			    	switch (getTypeColor(_color)) {
			    	case RED:
			    	case FIRE:
			    	case DARKFIRE:
			    	case ORANGE:
			    	case MAROON:
			    	case AUBURN:
			    	case GOLD:
			    	case PINK:
			    	case CREAM:
			    	case DARKGRAY:
			    		break;
			    	case SPIKE:
			    		if (!_isenhanced) {
			    			setColorDarkgreen(Utils.ColorDARKGREEN);
			    		}
			    		break;
			    	default:
			    		setColorDarkgreen(Utils.ColorDARKGREEN);
			    		break;
			    	}
				}
				if (specialphoto > 0) {
					if (_colonyPhotosynthesis > 0) {
						if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
							if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
								_energy += _world.photosynthesis(_photosynthesis - _leafphoto - (specialphoto*Utils.scale[_growthRatio-1]) + _colonyPhotosynthesis);
							}
						} else {
							_energy += _world.photosynthesis(_photosynthesis - _leafphoto - (specialphoto*Utils.scale[_growthRatio-1]) + _colonyPhotosynthesis);
						}
						_colonyPhotosynthesis = 0;
					} else {
						if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
							if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
								_energy += _world.photosynthesis(_photosynthesis - _leafphoto - (specialphoto*Utils.scale[_growthRatio-1]));
							}
						} else {
							_energy += _world.photosynthesis(_photosynthesis - _leafphoto - (specialphoto*Utils.scale[_growthRatio-1]));
						}
					}
				} else {
					if (_colonyPhotosynthesis > 0) {
						if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
							if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
								_energy += _world.photosynthesis(_photosynthesis - _leafphoto + _colonyPhotosynthesis);
							}
						} else {
							_energy += _world.photosynthesis(_photosynthesis - _leafphoto + _colonyPhotosynthesis);
						}
						_colonyPhotosynthesis = 0;
					} else {
						if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
							if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
								_energy += _world.photosynthesis(_photosynthesis - _leafphoto);
							}
						} else {
							_energy += _world.photosynthesis(_photosynthesis - _leafphoto);
						}
					}
				}
			} else {
				if (specialphoto > 0) {
					if (_colonyPhotosynthesis > 0) {
						if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
							if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
								_energy += _world.photosynthesis(_photosynthesis - (specialphoto*Utils.scale[_growthRatio-1]) + _colonyPhotosynthesis);
							}
						} else {
							_energy += _world.photosynthesis(_photosynthesis - (specialphoto*Utils.scale[_growthRatio-1]) + _colonyPhotosynthesis);
						}
						_colonyPhotosynthesis = 0;
					} else {
						if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
							if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
								_energy += _world.photosynthesis(_photosynthesis - (specialphoto*Utils.scale[_growthRatio-1]));
							}
						} else {
							_energy += _world.photosynthesis(_photosynthesis - (specialphoto*Utils.scale[_growthRatio-1]));
						}
					}
				} else {
					if (_colonyPhotosynthesis > 0) {
						if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
							if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
								_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
							}
						} else {
							_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
						}
						_colonyPhotosynthesis = 0;
					} else {
						if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
							if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
								_energy += _world.photosynthesis(_photosynthesis);
							}
						} else {
							_energy += _world.photosynthesis(_photosynthesis);
						}
					}
				}
			}
	    }

	/*
	 * Apply segment effects for this frame, used for organisms with additional effects
	 */
	private final void segmentsHealingEffects() {
			int i;
			for (i=_segments-1; i>=0; i--) {
				if (_mphoto[i] > 0) {
					switch (getTypeColor(_segColor[i])) {
					// Healing
					case GREENBROWN:
					case ICE:
						if (_healing > 0) {
							if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
								if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
									_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
									_isfrozen =false;
									_isinjured =false;
									_allfrozen =false;
									_updateEffects = 2;
									segmentsRestoreEffects();
								}
							}
						}
						break;
					}
				} else {
					if (_mphoto[i] <= -20) {
						if (_mphoto[i] <= -21) {
							// Movement
							if (Utils.random.nextInt(100)<8) {
								if (_mphoto[i] == -22) {
									if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
										dx=Utils.between(dx+11.88d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
									    dy=Utils.between(dy+11.88d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
									    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
									}
								} else if (_mphoto[i] == -23) {
									if (useEnergy(Utils.CYAN_ENERGY_CONSUMPTION)) {
										dx=Utils.between(dx+12d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
										dy=Utils.between(dy+12d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
										dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
									}
								}
							}
						} else {
							switch (getTypeColor(_segColor[i])) {
							// Healing
							case GREENBROWN:
							case LIGHTBROWN:
							case BROKEN:
							case LIGHT_BLUE:
							case ICE:
								if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
									if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
										_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
										_isfrozen =false;
										_isinjured =false;
										_allfrozen =false;
										_updateEffects = 2;
										segmentsRestoreEffects();
									}
								}
								break;
							case DARKFIRE:
								if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
									if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
										_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
										darkfireRestoreEffects();
									}
								}
								if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
									if (Utils.random.nextInt(100)<_symmetry) {
										_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
										darkfireRestoreEffects();
									} else {
										_useextraeffects = true;
									}
								} else {
									if (Utils.random.nextInt(100)<2) {
										_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
										darkfireRestoreEffects();
									} else {
										_useextraeffects = true;
									}
								}
								break;
							// Restore abilities
							case DARKOLIVE:
								if (Utils.random.nextInt(100)<8) {
									_segColor[i] = Utils.ColorOLIVE;
									_mphoto[i] = -4;
								} else {
									_useextraeffects = true;
								}
								break;
							case DARKLILAC:
								if (Utils.random.nextInt(100)<8) {
									_segColor[i] = Utils.ColorLILAC;
									_mphoto[i] = -4;
								} else {
									_useextraeffects = true;
								}
								break;
							case DEEPSKY:
								if (Utils.random.nextInt(100)<8) {
									_segColor[i] = Utils.ColorSKY;
									_mphoto[i] = -4;
								} else {
									_useextraeffects = true;
								}
								break;
							}
						}
					}
				}
			}
	    }

	/*
	 * Apply segment effects for this frame, used for Olive, Sky, Lilac and Darkfire of non-plants and plants that move
	 */
	private final void segmentsExtraEffects() {
			int i;
			for (i=_segments-1; i>=0; i--) {
				if (_mphoto[i] <= -20) {
					if (_mphoto[i] <= -21) {
						// Movement
						if (Utils.random.nextInt(100)<8) {
							if (_mphoto[i] == -22) {
								if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
									dx=Utils.between(dx+11.88d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
								    dy=Utils.between(dy+11.88d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
								    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
								}
							} else if (_mphoto[i] == -23) {
								if (useEnergy(Utils.CYAN_ENERGY_CONSUMPTION)) {
									dx=Utils.between(dx+12d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
									dy=Utils.between(dy+12d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
									dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
								}
							}
						}
					} else {
						switch (getTypeColor(_segColor[i])) {
						// Healing
						case DARKFIRE:
							if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
								if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
									_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
									darkfireRestoreEffects();
								}
							}
							if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
								if (Utils.random.nextInt(100)<_symmetry) {
									_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
									darkfireRestoreEffects();
								} else {
									_useextraeffects = true;
								}
							} else {
								if (Utils.random.nextInt(100)<2) {
									_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
									darkfireRestoreEffects();
								} else {
									_useextraeffects = true;
								}
							}
							break;
						// Restore abilities
						case DARKOLIVE:
							if (Utils.random.nextInt(100)<8) {
								_segColor[i] = Utils.ColorOLIVE;
								_mphoto[i] = -4;
							} else {
								_useextraeffects = true;
							}
							break;
						case DARKLILAC:
							if (Utils.random.nextInt(100)<8) {
								_segColor[i] = Utils.ColorLILAC;
								_mphoto[i] = -4;
							} else {
								_useextraeffects = true;
							}
							break;
						case DEEPSKY:
							if (Utils.random.nextInt(100)<8) {
								_segColor[i] = Utils.ColorSKY;
								_mphoto[i] = -4;
							} else {
								_useextraeffects = true;
							}
							break;
						}
					}
				}
			}
	    }

	/*
	 * Apply segment effects for this frame, used for Olive, Sky, Lilac and Darkfire of plants, that do not move
	 */
	private final void segmentsExtraPlantsEffects() {
			int i;
			for (i=_segments-1; i>=0; i--) {
				if ((_mphoto[i] <= -20) && (_mphoto[i] >= -20.5)) {
					// Manteniment
					switch (getTypeColor(_segColor[i])) {
					// Healing
					case DARKFIRE:
						if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
							if (Utils.random.nextInt(100)<_symmetry) {
								_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
								darkfireRestoreEffects();
							} else {
								_useextraeffects = true;
							}
						} else {
							if (Utils.random.nextInt(100)<2) {
								_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
								darkfireRestoreEffects();
							} else {
								_useextraeffects = true;
							}
						}
						break;
					// Restore abilities
					case DARKOLIVE:
						if (Utils.random.nextInt(100)<8) {
							_segColor[i] = Utils.ColorOLIVE;
							_mphoto[i] = -4;
						} else {
							_useextraeffects = true;
						}
						break;
					case DARKLILAC:
						if (Utils.random.nextInt(100)<8) {
							_segColor[i] = Utils.ColorLILAC;
							_mphoto[i] = -4;
						} else {
							_useextraeffects = true;
						}
						break;
					case DEEPSKY:
						if (Utils.random.nextInt(100)<8) {
							_segColor[i] = Utils.ColorSKY;
							_mphoto[i] = -4;
						} else {
							_useextraeffects = true;
						}
						break;
					}
				}
			}
	    }

	/*
	 * Similar to plantsFrameEffects,
	 * but used when the plant grows, shrinks or some segments change color, not used if colors need to be restored or if a photosynthetic segment is injured
	 */
	private final void plantsUpdateEffects() {
			int i;
			double addmaintenance = _mass;
			double addphoto = 0;
			_forestphoto = 0;
			_leafphoto = 0;
			_methanotrophy = 0;
			for (i=_segments-1; i>=0; i--) {
				if (_mphoto[i] > 0) {
					// Manteniment
					switch (getTypeColor(_segColor[i])) {
					// Photosynthesis
					case FOREST:
						addphoto += _mphoto[i];
						_forestphoto += _mphoto[i];
						break;
					case LIME:
						if (_crowded == true) {
							addphoto += Utils.CROWDEDLIME_ENERGY_CONSUMPTION * _mphoto[i];
						} else {
							addphoto += _mphoto[i];
						}
						break;
					case LEAF:
						addphoto += _mphoto[i];
						_leafphoto += _mphoto[i];
						break;
					case PURPLE:
						_methanotrophy += _mphoto[i];
						addmaintenance -= 0.99 * _m[i];
						break;
					case WINTER:
						break;
					default:
						addphoto += _mphoto[i];
						break;
					}
				} else {
					if (_mphoto[i] > -1) {
						if (_mphoto[i] == 0) {
							// Manteniment
							switch (getTypeColor(_segColor[i])) {
							// Organisms with indigo segments reduce the energy the new born virus receives
							case INDIGO:
								addmaintenance -= 0.8 * _m[i];
								break;
							// Organism will create spores
							case SPORE:
								addmaintenance -= 0.99 * _m[i];
								break;
							// Auburn always has one real child if infected
							case AUBURN:
								addmaintenance -= 0.99 * _m[i];
								break;
							// Organisms with flower segments reproduce later
							case FLOWER:
								addmaintenance -= _m[i] - (Utils.FLOWER_ENERGY_CONSUMPTION * _m[i]);
								break;
							// Organisms with gold segments live longer
							case GOLD:
								addmaintenance -= 0.8715 * _m[i];
								break;
							// Better vision and true sight
							case VISION:
								if (_haseyes) {
									addmaintenance -= _m[i];
								} else {
									addmaintenance -= 0.95 * _m[i];
								}
								break;
							}
						}
					} else {
						if (_mphoto[i] == -1) {
							// Manteniment
							switch (getTypeColor(_segColor[i])) {
							// is a consumer
							case MAROON:
								addmaintenance -= 0.8 * _m[i];
								break;
							// is a fungus
							case PINK:
								addmaintenance -= 0.8 * _m[i];
								break;
							}
						} else {
							if (_mphoto[i] <= -21) {
								// Movement
								if (_mphoto[i] == -21) {
									addmaintenance -= 0.95 * _m[i];
								} else {
									if (Utils.random.nextInt(100)<8) {
										if (_mphoto[i] == -22) {
											if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
												dx=Utils.between(dx+11.88d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
											    dy=Utils.between(dy+11.88d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
											    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
											}
										} else if (_mphoto[i] == -23) {
											if (useEnergy(Utils.CYAN_ENERGY_CONSUMPTION)) {
												dx=Utils.between(dx+12d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
												dy=Utils.between(dy+12d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
												dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
											}
										}
									}
								}
							} else {
								if (_mphoto[i] <= -8) {
									switch (getTypeColor(_segColor[i])) {
									// Organisms with eye segments can react to other organisms without colliding with them
									case EYE:
										addmaintenance += Utils.EYE_ENERGY_CONSUMPTION * _m[i];
										break;
									// Organisms with dark segments mimic other segments
									case DARK:
										addmaintenance -= _m[i] - (Utils.DARK_ENERGY_CONSUMPTION * _m[i]);
										break;
									}
								} else {
									// Manteniment
									switch (getTypeColor(_segColor[i])) {
									// Stop at white
									case WHITE:
										break;
									// Organisms with fallow segments inhibit the reproduction of other organisms
									case FALLOW:
										if (_fallowversion <= 3) {
											addmaintenance -= 0.9 * _m[i];
										} else {
											if (!_isinfectious) {
												addmaintenance -= 0.8 * _m[i];
											}
										}
										break;
									// Lavender immunity against cream, plague, coral and fallow
									case LAVENDER:
										addmaintenance -= 0.7 * _m[i];
										break;
									// Organisms with rose segments transfer energy to friendly organisms
									case ROSE:
										if (!_isakiller) {
											addmaintenance -= _m[i] - (Utils.ROSE_ENERGY_CONSUMPTION * _m[i]);
										} else {
											addmaintenance -= (0.75 * _m[i]) - (Utils.ROSE_ENERGY_CONSUMPTION * _m[i]);
										}
										break;
									}
								}
							}
						}
					}
				}
			}
			// Effective maintenance used for breathing
			_maintenance = (addmaintenance / Utils.SEGMENT_COST_DIVISOR);
			// Get chemotrophic energy from methane
			if (_methanotrophy > 0) {
				_methanotrophy = (_methanotrophy*Utils.scale[_growthRatio-1]);
				if ((_age & 0x07) == 0x00) {
					_energy += _world.methanotrophy(_methanotrophy);
				}
			}
			//Get sun's energy
			_photosynthesis = (addphoto*Utils.scale[_growthRatio-1]);
			if (_photosynthesis > 0) {
				// Effective colony photosynthesis
				if (_forestphoto > 0) {
					_forestphoto = Utils.CROWDEDFOREST_ENERGY_CONSUMPTION * (_forestphoto*Utils.scale[_growthRatio-1]);
				}
				if (_leafphoto > 0) {
					_leafphoto = (_leafphoto*Utils.scale[_growthRatio-1]);
					if (_framesColor > 0) {
						if ((_framesColor == 1) && (!_color.equals(Utils.ColorDARKGREEN))) {
					    	switch (getTypeColor(_color)) {
					    	case RED:
					    	case FIRE:
					    	case DARKFIRE:
					    	case ORANGE:
					    	case MAROON:
					    	case AUBURN:
					    	case GOLD:
					    	case PINK:
					    	case CREAM:
					    	case DARKGRAY:
					    		break;
					    	case SPIKE:
					    		if (!_isenhanced) {
					    			setColorDarkgreen(Utils.ColorDARKGREEN);
					    		}
					    		break;
					    	default:
					    		setColorDarkgreen(Utils.ColorDARKGREEN);
					    		break;
					    	}
						}
						if (_colonyPhotosynthesis > 0) {
							if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
								if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
									_energy += _world.photosynthesis(_photosynthesis - _leafphoto + _colonyPhotosynthesis);
								}
							} else {
								_energy += _world.photosynthesis(_photosynthesis - _leafphoto + _colonyPhotosynthesis);
							}
							_colonyPhotosynthesis = 0;
						} else {
							if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
								if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
									_energy += _world.photosynthesis(_photosynthesis - _leafphoto);
								}
							} else {
								_energy += _world.photosynthesis(_photosynthesis - _leafphoto);
							}
						}
					} else {
						if (_colonyPhotosynthesis > 0) {
							if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
								if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
									_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
								}
							} else {
								_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
							}
							_colonyPhotosynthesis = 0;
						} else {
							if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
								if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
									_energy += _world.photosynthesis(_photosynthesis);
								}
							} else {
								_energy += _world.photosynthesis(_photosynthesis);
							}
						}
					}
				} else {
					if (_colonyPhotosynthesis > 0) {
						if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
							if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
								_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
							}
						} else {
							_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
						}
						_colonyPhotosynthesis = 0;
					} else {
						if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
							if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
								_energy += _world.photosynthesis(_photosynthesis);
							}
						} else {
							_energy += _world.photosynthesis(_photosynthesis);
						}
					}
				}
			}
		}

	/*
	 * Similar to plantsFrameEffects,
	 * but used when the plant grows, shrinks or some segments change color
	 */
	private final void plantsExtraUpdateEffects() {
			int i;
			double addmaintenance = _mass;
			double addphoto = 0;
			double specialphoto = 0;
			_forestphoto = 0;
			_leafphoto = 0;
			_methanotrophy = 0;
			for (i=_segments-1; i>=0; i--) {
				if (_mphoto[i] > 0) {
					// Manteniment
					switch (getTypeColor(_segColor[i])) {
					// Photosynthesis
					case FOREST:
						addphoto += _mphoto[i];
						_forestphoto += _mphoto[i];
						break;
					case JADE:
						addphoto += _mphoto[i];
						break;
					case SUMMER:
						if ((_world.getTime() % 3) != _summerinactivity) {
							addphoto += _mphoto[i];
						} else {
							_segColor[i] = Utils.ColorWINTER;
						}
						break;
					case LIME:
						if (_crowded == true) {
							addphoto += Utils.CROWDEDLIME_ENERGY_CONSUMPTION * _mphoto[i];
						} else {
							addphoto += _mphoto[i];
						}
						break;
					case LEAF:
						addphoto += _mphoto[i];
						_leafphoto += _mphoto[i];
						break;
					case SPRING:
						addphoto += _mphoto[i];
						break;
					case GRASS:
						addphoto += _mphoto[i];
						break;
					case GREEN:
						addphoto += _mphoto[i];
						break;
					case C4:
						addphoto += _mphoto[i];
						break;
					case BARK:
						addphoto += _mphoto[i];
						break;
					case PURPLE:
						_methanotrophy += _mphoto[i];
						addmaintenance -= 0.99 * _m[i];
						break;
					case WINTER:
						if ((_world.getTime() % 3) != _summerinactivity) {
							_segColor[i] = Utils.ColorSUMMER;
							addphoto += _mphoto[i];
						}
						break;
					// Restore Jade
					case DARKJADE:
						addphoto += _mphoto[i];
						if (Utils.random.nextInt(_jadefactor)<8) {
							_segColor[i] = Utils.ColorJADE;
						} else {
							_useextraeffects = true;
						}
						break;
					case DARKGREEN:
						if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorSUMMER)) {
							if ((_world.getTime() % 3) != _summerinactivity) {
								addphoto += _mphoto[i];
								specialphoto += _mphoto[i];
								if (Utils.random.nextInt(_jadefactor)<8) {
									_segColor[i] = Utils.ColorSUMMER;
								} else {
									_useextraeffects = true;
								}
							} else {
								if (Utils.random.nextInt(_jadefactor)<8) {
									_segColor[i] = Utils.ColorWINTER;
								} else {
									_useextraeffects = true;
								}
							}
						} else {
							if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorFOREST)) {
								addphoto += _mphoto[i];
								_forestphoto += _mphoto[i];
								specialphoto += _mphoto[i];
							} else {
								if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorLEAF)) {
									addphoto += _mphoto[i];
									_leafphoto += _mphoto[i];
									if (_framesColor <= 0) {
										specialphoto += _mphoto[i];
									}
								} else {
									if (_geneticCode.getGene(i%_geneticCode.getNGenes()).getColor().equals(Utils.ColorLIME)) {
										if (_crowded == true) {
											addphoto += Utils.CROWDEDLIME_ENERGY_CONSUMPTION * _mphoto[i];
											specialphoto += Utils.CROWDEDLIME_ENERGY_CONSUMPTION * _mphoto[i];
										} else {
											addphoto += _mphoto[i];
											specialphoto += _mphoto[i];
										}
									} else {
										addphoto += _mphoto[i];
										specialphoto += _mphoto[i];
									}
								}
							}
							if (Utils.random.nextInt(_jadefactor)<8) {
								_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
							} else {
								_useextraeffects = true;
							}
						}
						break;
					// Healing
					case GREENBROWN:
					case ICE:
						if (_healing > 0) {
							if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
								if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
									_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
									_isfrozen =false;
									_isinjured =false;
									_allfrozen =false;
									_updateEffects = 2;
									segmentsRestoreEffects();
								}
							}
						}
						break;
					}
				} else {
					if (_mphoto[i] > -1) {
						if (_mphoto[i] == 0) {
							// Manteniment
							switch (getTypeColor(_segColor[i])) {
							// Organisms with indigo segments reduce the energy the new born virus receives
							case INDIGO:
								addmaintenance -= 0.8 * _m[i];
								break;
							// Organism will create spores
							case SPORE:
								addmaintenance -= 0.99 * _m[i];
								break;
							// Auburn always has one real child if infected
							case AUBURN:
								addmaintenance -= 0.99 * _m[i];
								break;
							// Organisms with flower segments reproduce later
							case FLOWER:
								addmaintenance -= _m[i] - (Utils.FLOWER_ENERGY_CONSUMPTION * _m[i]);
								break;
							// Organisms with gold segments live longer
							case GOLD:
								addmaintenance -= 0.8715 * _m[i];
								break;
							// Better vision and true sight
							case VISION:
								if (_haseyes) {
									addmaintenance -= _m[i];
								} else {
									addmaintenance -= 0.95 * _m[i];
								}
								break;
							}
						}
					} else {
						if (_mphoto[i] == -1) {
							// Manteniment
							switch (getTypeColor(_segColor[i])) {
							// is a consumer
							case MAROON:
								addmaintenance -= 0.8 * _m[i];
								break;
							// is a fungus
							case PINK:
								addmaintenance -= 0.8 * _m[i];
								break;
							}
						} else {
							if (_mphoto[i] <= -21) {
								// Movement
								if (_mphoto[i] == -21) {
									addmaintenance -= 0.95 * _m[i];
								} else {
									if (Utils.random.nextInt(100)<8) {
										if (_mphoto[i] == -22) {
											if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
												dx=Utils.between(dx+11.88d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
											    dy=Utils.between(dy+11.88d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
											    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
											}
										} else if (_mphoto[i] == -23) {
											if (useEnergy(Utils.CYAN_ENERGY_CONSUMPTION)) {
												dx=Utils.between(dx+12d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
												dy=Utils.between(dy+12d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
												dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
											}
										}
									}
								}
							} else {
								if (_mphoto[i] <= -8) {
									switch (getTypeColor(_segColor[i])) {
									// Organisms with eye segments can react to other organisms without colliding with them
									case EYE:
										addmaintenance += Utils.EYE_ENERGY_CONSUMPTION * _m[i];
										break;
									// Organisms with dark segments mimic other segments
									case DARK:
										addmaintenance -= _m[i] - (Utils.DARK_ENERGY_CONSUMPTION * _m[i]);
										break;
									// Healing
									case GREENBROWN:
									case BROKEN:
									case LIGHT_BLUE:
									case ICE:
										if (_healing > 0) {
											if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
												if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
													_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
													_isfrozen =false;
													_isinjured =false;
													_allfrozen =false;
													_updateEffects = 2;
													segmentsRestoreEffects();
												}
											}
										}
										break;
									case DARKFIRE:
										if (_healing > 0) {
											if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
												if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
													_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
													darkfireRestoreEffects();
												}
											}
										}
										if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
											if (Utils.random.nextInt(100)<_symmetry) {
												_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
												darkfireRestoreEffects();
											} else {
												_useextraeffects = true;
											}
										} else {
											if (Utils.random.nextInt(100)<2) {
												_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
												darkfireRestoreEffects();
											} else {
												_useextraeffects = true;
											}
										}
										break;
									// Restore abilities
									case DARKOLIVE:
										if (Utils.random.nextInt(100)<8) {
											_segColor[i] = Utils.ColorOLIVE;
											_mphoto[i] = -4;
										} else {
											_useextraeffects = true;
										}
										break;
									case DARKLILAC:
										if (Utils.random.nextInt(100)<8) {
											_segColor[i] = Utils.ColorLILAC;
											_mphoto[i] = -4;
										} else {
											_useextraeffects = true;
										}
										break;
									case DEEPSKY:
										if (Utils.random.nextInt(100)<8) {
											_segColor[i] = Utils.ColorSKY;
											_mphoto[i] = -4;
										} else {
											_useextraeffects = true;
										}
										break;
									}
								} else {
									// Manteniment
									switch (getTypeColor(_segColor[i])) {
									// Stop at white
									case WHITE:
										break;
									// Organisms with fallow segments inhibit the reproduction of other organisms
									case FALLOW:
										if (_fallowversion <= 3) {
											addmaintenance -= 0.9 * _m[i];
										} else {
											if (!_isinfectious) {
												addmaintenance -= 0.8 * _m[i];
											}
										}
										break;
									// Lavender immunity against cream, plague, coral and fallow
									case LAVENDER:
										addmaintenance -= 0.7 * _m[i];
										break;
									// Organisms with rose segments transfer energy to friendly organisms
									case ROSE:
										if (!_isakiller) {
											addmaintenance -= _m[i] - (Utils.ROSE_ENERGY_CONSUMPTION * _m[i]);
										} else {
											addmaintenance -= (0.75 * _m[i]) - (Utils.ROSE_ENERGY_CONSUMPTION * _m[i]);
										}
										break;
									}
								}
							}
						}
					}
				}
			}
			// Effective maintenance used for breathing
			_maintenance = (addmaintenance / Utils.SEGMENT_COST_DIVISOR);
			// Get chemotrophic energy from methane
			if (_methanotrophy > 0) {
				_methanotrophy = (_methanotrophy*Utils.scale[_growthRatio-1]);
				if ((_age & 0x07) == 0x00) {
					_energy += _world.methanotrophy(_methanotrophy);
				}
			}
			//Get sun's energy
			_photosynthesis = (addphoto*Utils.scale[_growthRatio-1]);
			if (_photosynthesis > 0) {
				// Effective colony photosynthesis
				if (_forestphoto > 0) {
					_forestphoto = Utils.CROWDEDFOREST_ENERGY_CONSUMPTION * (_forestphoto*Utils.scale[_growthRatio-1]);
				}
				if (_leafphoto > 0) {
					_leafphoto = (_leafphoto*Utils.scale[_growthRatio-1]);
					if (_framesColor > 0) {
						if ((_framesColor == 1) && (!_color.equals(Utils.ColorDARKGREEN))) {
					    	switch (getTypeColor(_color)) {
					    	case RED:
					    	case FIRE:
					    	case DARKFIRE:
					    	case ORANGE:
					    	case MAROON:
					    	case AUBURN:
					    	case GOLD:
					    	case PINK:
					    	case CREAM:
					    	case DARKGRAY:
					    		break;
					    	case SPIKE:
					    		if (!_isenhanced) {
					    			setColorDarkgreen(Utils.ColorDARKGREEN);
					    		}
					    		break;
					    	default:
					    		setColorDarkgreen(Utils.ColorDARKGREEN);
					    		break;
					    	}
						}
						if (specialphoto > 0) {
							if (_colonyPhotosynthesis > 0) {
								if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
									if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
										_energy += _world.photosynthesis(_photosynthesis - _leafphoto - (specialphoto*Utils.scale[_growthRatio-1]) + _colonyPhotosynthesis);
									}
								} else {
									_energy += _world.photosynthesis(_photosynthesis - _leafphoto - (specialphoto*Utils.scale[_growthRatio-1]) + _colonyPhotosynthesis);
								}
								_colonyPhotosynthesis = 0;
							} else {
								if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
									if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
										_energy += _world.photosynthesis(_photosynthesis - _leafphoto - (specialphoto*Utils.scale[_growthRatio-1]));
									}
								} else {
									_energy += _world.photosynthesis(_photosynthesis - _leafphoto - (specialphoto*Utils.scale[_growthRatio-1]));
								}
							}
						} else {
							if (_colonyPhotosynthesis > 0) {
								if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
									if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
										_energy += _world.photosynthesis(_photosynthesis - _leafphoto + _colonyPhotosynthesis);
									}
								} else {
									_energy += _world.photosynthesis(_photosynthesis - _leafphoto + _colonyPhotosynthesis);
								}
								_colonyPhotosynthesis = 0;
							} else {
								if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
									if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
										_energy += _world.photosynthesis(_photosynthesis - _leafphoto);
									}
								} else {
									_energy += _world.photosynthesis(_photosynthesis - _leafphoto);
								}
							}
						}
					} else {
						if (specialphoto > 0) {
							if (_colonyPhotosynthesis > 0) {
								if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
									if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
										_energy += _world.photosynthesis(_photosynthesis - (specialphoto*Utils.scale[_growthRatio-1]) + _colonyPhotosynthesis);
									}
								} else {
									_energy += _world.photosynthesis(_photosynthesis - (specialphoto*Utils.scale[_growthRatio-1]) + _colonyPhotosynthesis);
								}
								_colonyPhotosynthesis = 0;
							} else {
								if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
									if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
										_energy += _world.photosynthesis(_photosynthesis - (specialphoto*Utils.scale[_growthRatio-1]));
									}
								} else {
									_energy += _world.photosynthesis(_photosynthesis - (specialphoto*Utils.scale[_growthRatio-1]));
								}
							}
						} else {
							if (_colonyPhotosynthesis > 0) {
								if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
									if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
										_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
									}
								} else {
									_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
								}
								_colonyPhotosynthesis = 0;
							} else {
								if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
									if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
										_energy += _world.photosynthesis(_photosynthesis);
									}
								} else {
									_energy += _world.photosynthesis(_photosynthesis);
								}
							}
						}
					}
				} else {
					if (specialphoto > 0) {
						if (_colonyPhotosynthesis > 0) {
							if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
								if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
									_energy += _world.photosynthesis(_photosynthesis - (specialphoto*Utils.scale[_growthRatio-1]) + _colonyPhotosynthesis);
								}
							} else {
								_energy += _world.photosynthesis(_photosynthesis - (specialphoto*Utils.scale[_growthRatio-1]) + _colonyPhotosynthesis);
							}
							_colonyPhotosynthesis = 0;
						} else {
							if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
								if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
									_energy += _world.photosynthesis(_photosynthesis - (specialphoto*Utils.scale[_growthRatio-1]));
								}
							} else {
								_energy += _world.photosynthesis(_photosynthesis - (specialphoto*Utils.scale[_growthRatio-1]));
							}
						}
					} else {
						if (_colonyPhotosynthesis > 0) {
							if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
								if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
									_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
								}
							} else {
								_energy += _world.photosynthesis(_photosynthesis + _colonyPhotosynthesis);
							}
							_colonyPhotosynthesis = 0;
						} else {
							if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
								if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
									_energy += _world.photosynthesis(_photosynthesis);
								}
							} else {
								_energy += _world.photosynthesis(_photosynthesis);
							}
						}
					}
				}
			}
		}

	/*
	 * Similar to plantsFrameEffects,
	 * but used when a C4 plant grows, shrinks or some segments change color
	 */
	private final void c4UpdateEffects() {
			int i;
			double addmaintenance = _mass;
			double addphoto = 0;
			for (i=_segments-1; i>=0; i--) {
				if (_mphoto[i] > 0) {
					// Manteniment
					switch (getTypeColor(_segColor[i])) {
					// Photosynthesis
					case C4:
						addphoto += _mphoto[i];
						break;
					// Healing
					case GREENBROWN:
					case ICE:
						if (_healing > 0) {
							if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
								if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
									_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
									_isfrozen =false;
									_isinjured =false;
									_allfrozen =false;
									_updateEffects = 2;
									segmentsRestoreEffects();
								}
							}
						}
						break;
					}
				} else {
					if (_mphoto[i] > -1) {
						if (_mphoto[i] >= -0.2) {
							// Manteniment
							switch (getTypeColor(_segColor[i])) {
							// Organisms with indigo segments reduce the energy the new born virus receives
							case INDIGO:
								if (_isonlyc4 == 2) {
									addmaintenance -= 0.95 * _m[i];
								} else {
									addmaintenance -= 0.8 * _m[i];
								}
								break;
							// Organism will create spores
							case SPORE:
								if (_isonlyc4 == 2) {
									addmaintenance -= _m[i];
								} else {
									addmaintenance -= 0.99 * _m[i];
								}
								break;
							// Auburn always has one real child if infected
							case AUBURN:
								if (_isonlyc4 == 2) {
									addmaintenance -= _m[i];
								} else {
									addmaintenance -= 0.99 * _m[i];
								}
								break;
							// Small organisms with only C4 can neutralize all effects if enhanced
							case DARKGRAY:
								if ((_isonlyc4 == 2) && (!_isblond)) {
									addmaintenance -= _m[i];
								}
								break;
							// Organisms with flower segments reproduce later
							case FLOWER:
								if (_isonlyc4 == 2) {
									addmaintenance -= _m[i];
								} else {
									addmaintenance -= _m[i] - (Utils.FLOWER_ENERGY_CONSUMPTION * _m[i]);
								}
								break;
							// Organisms with gold segments live longer
							case GOLD:
								if (_isonlyc4 == 2) {
									addmaintenance -= 0.95 * _m[i];
								} else {
									addmaintenance -= 0.8715 * _m[i];
								}
								break;
							// Better vision and true sight
							case VISION:
								if ((_isonlyc4 == 2) || (_haseyes)) {
									addmaintenance -= _m[i];
								} else {
									addmaintenance -= 0.95 * _m[i];
								}
								break;
							}
						}
					} else {
						if (_mphoto[i] == -1) {
							// Manteniment
							switch (getTypeColor(_segColor[i])) {
							// is a consumer
							case MAROON:
								addmaintenance -= 0.8 * _m[i];
								break;
							// is a fungus
							case PINK:
								addmaintenance -= 0.8 * _m[i];
								break;
							}
						} else {
							if (_mphoto[i] <= -21) {
								// Movement
								if (_mphoto[i] == -21) {
									addmaintenance -= 0.95 * _m[i];
								} else {
									if (Utils.random.nextInt(100)<8) {
										if (_mphoto[i] == -22) {
											if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
												dx=Utils.between(dx+11.88d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
											    dy=Utils.between(dy+11.88d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
											    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
											}
										} else if (_mphoto[i] == -23) {
											if (useEnergy(Utils.CYAN_ENERGY_CONSUMPTION)) {
												dx=Utils.between(dx+12d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
												dy=Utils.between(dy+12d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
												dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
											}
										}
									}
								}
							} else {
								if (_mphoto[i] <= -8) {
									switch (getTypeColor(_segColor[i])) {
									// Organisms with eye segments can react to other organisms without colliding with them
									case EYE:
										addmaintenance += Utils.EYE_ENERGY_CONSUMPTION * _m[i];
										break;
									// Organisms with dark segments mimic other segments
									case DARK:
										addmaintenance -= _m[i] - (Utils.DARK_ENERGY_CONSUMPTION * _m[i]);
										break;
									// Healing
									case GREENBROWN:
									case BROKEN:
									case LIGHT_BLUE:
									case ICE:
										if (_healing > 0) {
											if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
												if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
													_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
													_isfrozen =false;
													_isinjured =false;
													_allfrozen =false;
													_updateEffects = 2;
													segmentsRestoreEffects();
												}
											}
										}
										break;
									case DARKFIRE:
										if (_healing > 0) {
											if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
												if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
													_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
													darkfireRestoreEffects();
												}
											}
										}
										if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
											if (Utils.random.nextInt(100)<_symmetry) {
												_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
												darkfireRestoreEffects();
											} else {
												_useextraeffects = true;
											}
										} else {
											if (Utils.random.nextInt(100)<2) {
												_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
												darkfireRestoreEffects();
											} else {
												_useextraeffects = true;
											}
										}
										break;
									// Restore abilities
									case DARKOLIVE:
										if (Utils.random.nextInt(100)<8) {
											_segColor[i] = Utils.ColorOLIVE;
											_mphoto[i] = -4;
										} else {
											_useextraeffects = true;
										}
										break;
									case DARKLILAC:
										if (Utils.random.nextInt(100)<8) {
											_segColor[i] = Utils.ColorLILAC;
											_mphoto[i] = -4;
										} else {
											_useextraeffects = true;
										}
										break;
									case DEEPSKY:
										if (Utils.random.nextInt(100)<8) {
											_segColor[i] = Utils.ColorSKY;
											_mphoto[i] = -4;
										} else {
											_useextraeffects = true;
										}
										break;
									}
								} else {
									// Manteniment
									switch (getTypeColor(_segColor[i])) {
									// Stop at white
									case WHITE:
										break;
									// Organisms with fallow segments inhibit the reproduction of other organisms
									case FALLOW:
										if (_fallowversion <= 3) {
											addmaintenance -= 0.9 * _m[i];
										} else {
											if (!_isinfectious) {
												addmaintenance -= 0.8 * _m[i];
											}
										}
										break;
									// Lavender immunity against cream, plague, coral and fallow
									case LAVENDER:
										addmaintenance -= 0.7 * _m[i];
										break;
									// Organisms with rose segments transfer energy to friendly organisms
									case ROSE:
										if (!_isakiller) {
											addmaintenance -= _m[i] - (Utils.ROSE_ENERGY_CONSUMPTION * _m[i]);
										} else {
											addmaintenance -= (0.75 * _m[i]) - (Utils.ROSE_ENERGY_CONSUMPTION * _m[i]);
										}
										break;
									}
								}
							}
						}
					}
				}
			}
			// Effective maintenance used for breathing
			_maintenance = (addmaintenance / Utils.SEGMENT_COST_DIVISOR);
			//Get sun's energy
			if ((_isenhanced) && (_isonlyc4 == 2)) {
				_photosynthesis = (addphoto);
			} else {
				_photosynthesis = (addphoto*Utils.scale[_growthRatio-1]);
			}
			if (_photosynthesis > 0) {
				if ((_lowersymmetric > 0) && (_world._CO2 < _lowersymmetric)) {
					if (Utils.random.nextInt(_lowersymmetric) < _world._CO2) {
						_energy += _world.photosynthesis(_photosynthesis);
					}
				} else {
					_energy += _world.photosynthesis(_photosynthesis);
				}
			}
		}

	/*
	 * Similar to segmentsFrameEffects,
	 * but used when the organism grows, shrinks or some segments change color
	 */
	private final void segmentsUpdateEffects() {
			int i;
			double addmaintenance = _mass;
			for (i=_segments-1; i>=0; i--) {
				if (_mphoto[i] > -1) {
					if (_mphoto[i] >= -0.1) {
						// Manteniment
						switch (getTypeColor(_segColor[i])) {
						// Organisms with indigo segments reduce the energy the new born virus receives
						case INDIGO:
							addmaintenance -= 0.8 * _m[i];
							break;
						// Organism will create spores
						case SPORE:
							addmaintenance -= 0.99 * _m[i];
							break;
						// Auburn always has one real child if infected
						case AUBURN:
							addmaintenance -= 0.99 * _m[i];
							break;
						// Organisms with flower segments reproduce later
						case FLOWER:
							addmaintenance -= _m[i] - (Utils.FLOWER_ENERGY_CONSUMPTION * _m[i]);
							break;
						// Organisms with blue segments protect with a shield
						case BLUE:
							addmaintenance -= 0.9 * _m[i];
							break;
						// Organisms with gold segments live longer
						case GOLD:
							if (_isaconsumer) {
								addmaintenance -= 0.9 * _m[i];
							} else {
								addmaintenance -= 0.95 * _m[i];
							}
							break;
						// Better vision and true sight
						case VISION:
							if (_haseyes) {
								addmaintenance -= _m[i];
							} else {
								addmaintenance -= 0.95 * _m[i];
							}
							break;
						}
					}
				} else {
					if (_mphoto[i] == -1) {
						// Manteniment
						switch (getTypeColor(_segColor[i])) {
						// is a consumer
						case MAROON:
							addmaintenance -= 0.8 * _m[i];
							break;
						// is a fungus
						case PINK:
							addmaintenance -= 0.8 * _m[i];
							break;
						}
					} else {
						if (_mphoto[i] <= -21) {
							// Movement
							if (_mphoto[i] == -21) {
								addmaintenance -= 0.95 * _m[i];
							} else {
								if (Utils.random.nextInt(100)<8) {
									if (_mphoto[i] == -22) {
										if (useEnergy(Utils.TEAL_ENERGY_CONSUMPTION)) {
											dx=Utils.between(dx+11.88d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
										    dy=Utils.between(dy+11.88d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
										    dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
										}
									} else if (_mphoto[i] == -23) {
										if (useEnergy(Utils.CYAN_ENERGY_CONSUMPTION)) {
											dx=Utils.between(dx+12d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
											dy=Utils.between(dy+12d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
											dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
										}
									}
								}
							}
						} else {
							if (_mphoto[i] <= -8) {
								// Manteniment
								switch (getTypeColor(_segColor[i])) {
								// Organisms with eye segments can react to other organisms without colliding with them
								case EYE:
									addmaintenance += Utils.EYE_ENERGY_CONSUMPTION * _m[i];
									break;
								// Organisms with dark segments mimic other segments
								case DARK:
									addmaintenance -= _m[i] - (Utils.DARK_ENERGY_CONSUMPTION * _m[i]);
									break;
								// Healing
								case LIGHTBROWN:
								case LIGHT_BLUE:
								case ICE:
									if (_healing > 0) {
										if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
											if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
												_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
												_isfrozen =false;
												_isinjured =false;
												_allfrozen =false;
												_updateEffects = 2;
												segmentsRestoreEffects();
											}
										}
									}
									break;
								case DARKFIRE:
									if (_healing > 0) {
										if (Utils.random.nextInt(Utils.HEALING) <= _healing) {
											if (useEnergy(Utils.MAGENTA_ENERGY_CONSUMPTION)) {
												_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
												darkfireRestoreEffects();
											}
										}
									}
									if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
										if (Utils.random.nextInt(100)<_symmetry) {
											_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
											darkfireRestoreEffects();
										} else {
											_useextraeffects = true;
										}
									} else {
										if (Utils.random.nextInt(100)<2) {
											_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
											darkfireRestoreEffects();
										} else {
											_useextraeffects = true;
										}
									}
									break;
								// Restore abilities
								case DARKOLIVE:
									addmaintenance -= 0.55 * _m[i];
									if (Utils.random.nextInt(100)<8) {
										_segColor[i] = Utils.ColorOLIVE;
										_mphoto[i] = -4;
									} else {
										_useextraeffects = true;
									}
									break;
								case DARKLILAC:
									if (Utils.random.nextInt(100)<8) {
										_segColor[i] = Utils.ColorLILAC;
										_mphoto[i] = -4;
									} else {
										_useextraeffects = true;
									}
									break;
								case DEEPSKY:
									if (_skyversion == 1) {
										addmaintenance -= 0.9 * _m[i];
									}
									if (Utils.random.nextInt(100)<8) {
										_segColor[i] = Utils.ColorSKY;
										_mphoto[i] = -4;
									} else {
										_useextraeffects = true;
									}
									break;
								}
							} else {
								// Manteniment
								switch (getTypeColor(_segColor[i])) {
								// Stop at white
								case WHITE:
									break;
								// Organisms with violet poison other segments and make them useless
								case VIOLET:
									if ((!_isaconsumer) && (!_isafungus)) {
										addmaintenance -= 0.95 * _m[i];
									}
									break;
								// Organisms with olive destroy defense and killer segments
								case OLIVE:
									addmaintenance -= 0.55 * _m[i];
									break;
								// Organisms with sky segments freeze movement and photosynthesis, if modified
								case SKY:
									if (_skyversion == 1) {
										addmaintenance -= 0.9 * _m[i];
									}
									break;
								// Organisms with coral segments transform viruses into themselves
								case CORAL:
									if ((_gold > 0) && (!_isaconsumer) && (!_isafungus)) {
										addmaintenance -= 0.95 * _m[i];
									}
									break;
								// Organisms with fallow segments inhibit the reproduction of other organisms
								case FALLOW:
									if (_isaconsumer) {
										addmaintenance -= 0.95 * _m[i];
									} else {
										addmaintenance -= 0.99 * _m[i];
									}
									break;
								// Lavender immunity against cream, plague, coral and fallow
								case LAVENDER:
									if (_isaconsumer) {
										addmaintenance -= 0.7 * _m[i];
									} else {
										addmaintenance -= 0.95 * _m[i];
									}
									break;
								// Organisms with rose segments transfer energy to friendly organisms
								case ROSE:
									if (!_isakiller) {
										addmaintenance -= _m[i] - (Utils.ROSE_ENERGY_CONSUMPTION * _m[i]);
									} else {
										addmaintenance -= (0.75 * _m[i]) - (Utils.ROSE_ENERGY_CONSUMPTION * _m[i]);
									}
									break;
								}
							}
						}
					}
				}
			}
			// Effective maintenance used for breathing
			_maintenance = (addmaintenance / Utils.SEGMENT_COST_DIVISOR);
		}

	/*
	 * Checks if the organism is still injured
	 */
	private final void darkfireRestoreEffects() {
		int i;
		_healing = 0;
		_isinjured =false;
		for (i=_segments-1; i>=0; i--) {
			// Manteniment
			switch (getTypeColor(_segColor[i])) {
			case FIRE:
			case CREAM:
				_mphoto[i] = -1;
				break;
			case WHITE:
			case PLAGUE:
			case FRUIT:
				_mphoto[i] = -4;
				break;
			case VIOLET:
				_mphoto[i] = -4;
				break;
			case MAGENTA:
				if ((_symmetry != 3) || (_geneticCode.getMirror() == 0)) {
					_healing += (int)(10 * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				} else {
					_healing += (int)((20/3) * _geneticCode.getGene(i%_geneticCode.getNGenes()).getLength());
				}
				if (_altruist) {
					_mphoto[i] = -4;
				} else {
					_mphoto[i] = -0.5;
				}
				break;
			case LIGHTBROWN:
				_isinjured =true;
				break;
			case GREENBROWN:
				_isinjured =true;
				break;
			case DARKFIRE:
				_isinjured =true;
				break;
			case LIGHT_BLUE:
				_isinjured =true;
				break;
			case BROKEN:
				_isinjured =true;
				break;
			case ICE:
				_isinjured =true;
				break;
			}
		}
	}

	private static final int NOCOLOR=-1;
	private static final int GREEN=0;
	private static final int FOREST=1;
	private static final int SPRING=2;
	private static final int SUMMER=3;
	private static final int LIME=4;
	private static final int LEAF=5;
	private static final int C4=6;
	private static final int JADE=7;
	private static final int GRASS=8;
	private static final int BARK=9;
	private static final int PURPLE=10;
	private static final int RED=11;
	private static final int FIRE=12;
	private static final int ORANGE=13;
	private static final int MAROON=14;
	private static final int PINK=15;
	private static final int CREAM=16;
	private static final int SILVER=17;
	private static final int SPIKE=18;
	private static final int LILAC=19;
	private static final int GRAY=20;
	private static final int VIOLET=21;
	private static final int OLIVE=22;
	private static final int SKY=23;
	private static final int BLUE=24;
	private static final int OCHRE=25;
	private static final int FALLOW=26;
	private static final int SPORE=27;
	private static final int WHITE=28;
	private static final int PLAGUE=29;
	private static final int CORAL=30;
	private static final int MINT=31;
	private static final int LAVENDER=32;
	private static final int MAGENTA=33;
	private static final int ROSE=34;
	private static final int CYAN=35;
	private static final int TEAL=36;
	private static final int YELLOW=37;
	private static final int AUBURN=38;
	private static final int INDIGO=39;
	private static final int BLOND=40;
	private static final int FLOWER=41;
	private static final int DARKGRAY=42;
	private static final int GOLD=43;
	private static final int DARK=44;
	private static final int EYE=45;
	private static final int WINTER=46;
	private static final int OLDBARK=47;
	private static final int DARKJADE=48;
	private static final int DARKGREEN=49;
	private static final int DARKFIRE=50;
	private static final int DARKLILAC=51;
	private static final int DEEPSKY=52;
	private static final int DARKOLIVE=53;
	private static final int SPIKEPOINT=54;
	private static final int FRUIT=55;
	private static final int VISION=56;
	private static final int ICE=57;
	private static final int LIGHT_BLUE=58;
	private static final int LIGHTBROWN=59;
	private static final int GREENBROWN=60;
	private static final int BROKEN=61;
	private static final int DEADBARK=62;
	private static final int BROWN=63;
	private static final int getTypeColor(Color c) {
		if (c.equals(Color.GREEN))
			return GREEN;
		if (c.equals(Utils.ColorFOREST))
			return FOREST;
		if (c.equals(Utils.ColorSPRING))
			return SPRING;
		if (c.equals(Utils.ColorSUMMER))
			return SUMMER;
		if (c.equals(Utils.ColorLIME))
			return LIME;
		if (c.equals(Utils.ColorLEAF))
			return LEAF;
		if (c.equals(Utils.ColorC4))
			return C4;
		if (c.equals(Utils.ColorJADE))
			return JADE;
		if (c.equals(Utils.ColorGRASS))
			return GRASS;
		if (c.equals(Utils.ColorBARK))
			return BARK;
		if (c.equals(Utils.ColorPURPLE))
			return PURPLE;
		if (c.equals(Color.RED))
			return RED;
		if (c.equals(Utils.ColorFIRE))
			return FIRE;
		if (c.equals(Color.ORANGE))
			return ORANGE;
		if (c.equals(Utils.ColorMAROON))
			return MAROON;
		if (c.equals(Color.PINK))
			return PINK;
		if (c.equals(Utils.ColorCREAM))
			return CREAM;
		if (c.equals(Color.LIGHT_GRAY))
			return SILVER;
		if (c.equals(Utils.ColorSPIKE))
			return SPIKE;
		if (c.equals(Utils.ColorLILAC))
			return LILAC;
		if (c.equals(Color.GRAY))
			return GRAY;
		if (c.equals(Utils.ColorVIOLET))
			return VIOLET;
		if (c.equals(Utils.ColorOLIVE))
			return OLIVE;
		if (c.equals(Utils.ColorSKY))
			return SKY;
		if (c.equals(Color.BLUE))
			return BLUE;
		if (c.equals(Utils.ColorOCHRE))
			return OCHRE;
		if (c.equals(Utils.ColorFALLOW))
			return FALLOW;
		if (c.equals(Utils.ColorSPORE))
			return SPORE;
		if (c.equals(Color.WHITE))
			return WHITE;
		if (c.equals(Utils.ColorPLAGUE))
			return PLAGUE;
		if (c.equals(Utils.ColorCORAL))
			return CORAL;
		if (c.equals(Utils.ColorMINT))
			return MINT;
		if (c.equals(Utils.ColorLAVENDER))
			return LAVENDER;
		if (c.equals(Color.MAGENTA))
			return MAGENTA;
		if (c.equals(Utils.ColorROSE))
			return ROSE;
		if (c.equals(Color.CYAN))
			return CYAN;
		if (c.equals(Utils.ColorTEAL))
			return TEAL;
		if (c.equals(Color.YELLOW))
			return YELLOW;
		if (c.equals(Utils.ColorAUBURN))
			return AUBURN;
		if (c.equals(Utils.ColorINDIGO))
			return INDIGO;
		if (c.equals(Utils.ColorBLOND))
			return BLOND;
		if (c.equals(Utils.ColorFLOWER))
			return FLOWER;
		if (c.equals(Color.DARK_GRAY))
			return DARKGRAY;
		if (c.equals(Utils.ColorGOLD))
			return GOLD;
		if (c.equals(Utils.ColorDARK))
			return DARK;
		if (c.equals(Utils.ColorEYE))
			return EYE;
		if (c.equals(Utils.ColorWINTER))
			return WINTER;
		if (c.equals(Utils.ColorOLDBARK))
			return OLDBARK;
		if (c.equals(Utils.ColorDARKJADE))
			return DARKJADE;
		if (c.equals(Utils.ColorDARKGREEN))
			return DARKGREEN;
		if (c.equals(Utils.ColorDARKFIRE))
			return DARKFIRE;
		if (c.equals(Utils.ColorDARKLILAC))
			return DARKLILAC;
		if (c.equals(Utils.ColorDEEPSKY))
			return DEEPSKY;
		if (c.equals(Utils.ColorDARKOLIVE))
			return DARKOLIVE;
		if (c.equals(Utils.ColorSPIKEPOINT))
			return SPIKEPOINT;
		if (c.equals(Utils.ColorFRUIT))
			return FRUIT;
		if (c.equals(Utils.ColorVISION))
			return VISION;
		if (c.equals(Utils.ColorICE))
			return ICE;
		if (c.equals(Utils.ColorLIGHT_BLUE))
			return LIGHT_BLUE;
		if (c.equals(Utils.ColorLIGHTBROWN))
			return LIGHTBROWN;
		if (c.equals(Utils.ColorGREENBROWN))
			return GREENBROWN;
		if (c.equals(Utils.ColorBROKEN))
			return BROKEN;
		if (c.equals(Utils.ColorDEADBARK))
			return DEADBARK;
		if (c.equals(Utils.ColorBROWN))
			return BROWN;
		return NOCOLOR;
	}

	private final void setColor(Color c) {
		_color = c;
		_framesColor = 10;
	}

	private final void setColortwoFrames(Color c) {
		_color = c;
		_framesColor = 2;
	}

	private final void setColorforLeaf(Color c) {
		_color = c;
		_framesColor = -10;
	}

	private final void setColorDarkgreen(Color c) {
		_color = c;
		_framesColor = 4;
	}

	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setBackground(Color.BLACK);
		g.clearRect(0,0,width,height);
		for (int i=_segments-1; i>=0; i--) {
				g.setColor(_segColor[i]);
				g.drawLine(x1[i] -x + _centerX, y1[i] - y + _centerY, x2[i] - x + _centerX, y2[i] - y+_centerY);
		}
		return image;
	}
}
