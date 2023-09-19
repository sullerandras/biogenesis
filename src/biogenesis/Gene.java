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

import java.awt.Color;
import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * This class implements a single organism's gene. A gene is a colored segment.
 * This segment is part of the organism's body and will be drawn several times
 * depending on the symmetry of the organism. Genes are always segments starting
 * at (0,0). The position in the organism's body depends on their gene neighbors
 * and the organism's symmetry and mirroring.
 */
public class Gene implements Cloneable, Serializable {
	/**
	 * The version number of this class
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;
	@Expose
	private double _length = 0;
	@Expose
	private double _theta = 0;
	/**
	 * Segment's color
	 */
	@Expose
	private Color _color;
	/**
	 * Where a segment branches from another segment (-1 is no branch).
	 */
	@Expose
	private int _branch = 0;
	/**
	 * Segment's reactions
	 */
	private int _redreaction = 0;
	private int _greenreaction = 0;
	private int _bluereaction = 0;
	private int _plaguereaction = 0;
	private int _scourgereaction = 0;
	private int _whitereaction = 0;
	private int _grayreaction = 0;
	private int _silverreaction = 0;
	private int _defaultreaction = 0;
	private int _consumerreaction = 0;
	private int _plantreaction = 0;
	private int _magentareaction = 0;
	private int _pinkreaction = 0;
	private int _coralreaction = 0;
	private int _orangereaction = 0;
	private int _barkreaction = 0;
	private int _violetreaction = 0;
	private int _virusreaction = 0;
	private int _maroonreaction = 0;
	private int _olivereaction = 0;
	private int _mintreaction = 0;
	private int _creamreaction = 0;
	private int _spikereaction = 0;
	private int _fallowreaction = 0;
	private int _lightbluereaction = 0;
	private int _ochrereaction = 0;
	private int _skyreaction = 0;
	private int _lilacreaction = 0;
	private int _firereaction = 0;
	private int _lightbrownreaction = 0;
	private int _greenbrownreaction = 0;
	private int _brownreaction = 0;
	private int _icereaction = 0;
	private int _brokenreaction = 0;
	private int _sickreaction = 0;
	private int _friendreaction = 0;

	/**
	 * Void constructor. Creates the gene but leave it uninitialized.
	 */
	public Gene() {
	}

	/**
	 * Creates a gene with the specified final point and color.
	 * 
	 * @param x
	 *            x coordinate of the final point
	 * @param y
	 *            t coordinate of the final point
	 * @param color
	 *            segment's color
	 * @param reaction
	 *            segment's reaction
	 */
	public Gene(double length, double theta, Color color, int branch, int redreaction, int greenreaction, int bluereaction, int plaguereaction, int scourgereaction
			, int whitereaction, int grayreaction, int silverreaction, int defaultreaction, int consumerreaction, int plantreaction, int magentareaction, int pinkreaction
			, int coralreaction, int orangereaction, int barkreaction, int violetreaction, int virusreaction, int maroonreaction, int olivereaction, int mintreaction
			, int creamreaction, int spikereaction, int fallowreaction, int lightbluereaction, int ochrereaction, int skyreaction, int lilacreaction, int firereaction
			, int lightbrownreaction, int greenbrownreaction, int brownreaction, int icereaction, int brokenreaction, int sickreaction, int friendreaction) {
		_length = length;
		_theta = theta;
		_color = color;
		_branch = branch;
		_redreaction = redreaction;
		_greenreaction = greenreaction;
		_bluereaction = bluereaction;
		_plaguereaction = plaguereaction;
		_scourgereaction = scourgereaction;
		_whitereaction = whitereaction;
		_grayreaction = grayreaction;
		_silverreaction = silverreaction;
		_defaultreaction = defaultreaction;
		_consumerreaction = consumerreaction;
		_plantreaction = plantreaction;
		_magentareaction = magentareaction;
		_pinkreaction = pinkreaction;
		_coralreaction = coralreaction;
		_orangereaction = orangereaction;
		_barkreaction = barkreaction;
		_violetreaction = violetreaction;
		_virusreaction = virusreaction;
		_maroonreaction = maroonreaction;
		_olivereaction = olivereaction;
		_mintreaction = mintreaction;
		_creamreaction = creamreaction;
		_spikereaction = spikereaction;
		_fallowreaction = fallowreaction;
		_lightbluereaction = lightbluereaction;
		_ochrereaction = ochrereaction;
		_skyreaction = skyreaction;
		_lilacreaction = lilacreaction;
		_firereaction = firereaction;
		_lightbrownreaction = lightbrownreaction;
		_greenbrownreaction = greenbrownreaction;
		_brownreaction = brownreaction;
		_icereaction = icereaction;
		_brokenreaction = brokenreaction;
		_sickreaction = sickreaction;
		_friendreaction = friendreaction;
	}

	public void randomizeColor() {
		int max_prob = Utils.RED_PROB + Utils.GREEN_PROB + Utils.BLUE_PROB + Utils.CYAN_PROB + Utils.WHITE_PROB + Utils.GRAY_PROB + Utils.YELLOW_PROB + Utils.MAGENTA_PROB
				+ Utils.PINK_PROB + Utils.CORAL_PROB + Utils.ORANGE_PROB + Utils.FOREST_PROB + Utils.SPRING_PROB + Utils.LEAF_PROB + Utils.SUMMER_PROB + Utils.LIME_PROB
				+ Utils.BARK_PROB + Utils.VIOLET_PROB + Utils.TEAL_PROB + Utils.SPIN_PROB + Utils.EYE_PROB + Utils.MAROON_PROB + Utils.OLIVE_PROB + Utils.MINT_PROB
				+ Utils.CREAM_PROB + Utils.ROSE_PROB + Utils.DARK_PROB + Utils.OCHRE_PROB + Utils.SKY_PROB + Utils.LILAC_PROB + Utils.SILVER_PROB + Utils.FIRE_PROB
				+ Utils.DARKGRAY_PROB + Utils.GOLD_PROB + Utils.BLOND_PROB + Utils.FLOWER_PROB + Utils.AUBURN_PROB + Utils.PLAGUE_PROB + Utils.SPIKE_PROB + Utils.INDIGO_PROB
				+ Utils.LAVENDER_PROB + Utils.FALLOW_PROB + Utils.SPORE_PROB + Utils.JADE_PROB + Utils.C4_PROB + Utils.GRASS_PROB + Utils.PURPLE_PROB + Utils.PLANKTON_PROB;
		int prob = Utils.random.nextInt(max_prob);
		int ac_prob = Utils.RED_PROB;
		if (prob < ac_prob) {
			_color = Color.RED;
			return;
		}
		ac_prob += Utils.GREEN_PROB;
		if (prob < ac_prob) {
			_color = Color.GREEN;
			return;
		}
		ac_prob += Utils.BLUE_PROB;
		if (prob < ac_prob) {
			_color = Color.BLUE;
			return;
		}
		ac_prob += Utils.CYAN_PROB;
		if (prob < ac_prob) {
			_color = Color.CYAN;
			return;
		}
		ac_prob += Utils.WHITE_PROB;
		if (prob < ac_prob) {
			_color = Color.WHITE;
			return;
		}
		ac_prob += Utils.GRAY_PROB;
		if (prob < ac_prob) {
			_color = Color.GRAY;
			return;
		}
		ac_prob += Utils.MAGENTA_PROB;
		if (prob < ac_prob) {
			_color = Color.MAGENTA;
			return;
		}
		ac_prob += Utils.PINK_PROB;
		if (prob < ac_prob) {
			_color = Color.PINK;
			return;
		}
		ac_prob += Utils.CORAL_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorCORAL;
			return;
		}
		ac_prob += Utils.ORANGE_PROB;
		if (prob < ac_prob) {
			_color = Color.ORANGE;
			return;
		}
		ac_prob += Utils.FOREST_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorFOREST;
			return;
		}
		ac_prob += Utils.SPRING_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorSPRING;
			return;
		}
		ac_prob += Utils.LEAF_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorLEAF;
			return;
		}
		ac_prob += Utils.SUMMER_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorSUMMER;
			return;
		}
		ac_prob += Utils.LIME_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorLIME;
			return;
		}
		ac_prob += Utils.BARK_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorBARK;
			return;
		}
		ac_prob += Utils.VIOLET_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorVIOLET;
			return;
		}
		ac_prob += Utils.TEAL_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorTEAL;
			return;
		}
		ac_prob += Utils.SPIN_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorSPIN;
			return;
		}
		ac_prob += Utils.EYE_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorEYE;
			return;
		}
		ac_prob += Utils.MAROON_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorMAROON;
			return;
		}
		ac_prob += Utils.OLIVE_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorOLIVE;
			return;
		}
		ac_prob += Utils.MINT_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorMINT;
			return;
		}
		ac_prob += Utils.CREAM_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorCREAM;
			return;
		}
		ac_prob += Utils.ROSE_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorROSE;
			return;
		}
		ac_prob += Utils.DARK_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorDARK;
			return;
		}
		ac_prob += Utils.OCHRE_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorOCHRE;
			return;
		}
		ac_prob += Utils.SKY_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorSKY;
			return;
		}
		ac_prob += Utils.LILAC_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorLILAC;
			return;
		}
		ac_prob += Utils.SILVER_PROB;
		if (prob < ac_prob) {
			_color = Color.LIGHT_GRAY;
			return;
		}
		ac_prob += Utils.FIRE_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorFIRE;
			return;
		}
		ac_prob += Utils.DARKGRAY_PROB;
		if (prob < ac_prob) {
			_color = Color.DARK_GRAY;
			return;
		}
		ac_prob += Utils.GOLD_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorGOLD;
			return;
		}
		ac_prob += Utils.BLOND_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorBLOND;
			return;
		}
		ac_prob += Utils.FLOWER_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorFLOWER;
			return;
		}
		ac_prob += Utils.AUBURN_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorAUBURN;
			return;
		}
		ac_prob += Utils.PLAGUE_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorPLAGUE;
			return;
		}
		ac_prob += Utils.SPIKE_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorSPIKE;
			return;
		}
		ac_prob += Utils.INDIGO_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorINDIGO;
			return;
		}
		ac_prob += Utils.LAVENDER_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorLAVENDER;
			return;
		}
		ac_prob += Utils.FALLOW_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorFALLOW;
			return;
		}
		ac_prob += Utils.SPORE_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorSPORE;
			return;
		}
		ac_prob += Utils.JADE_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorJADE;
			return;
		}
		ac_prob += Utils.C4_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorC4;
			return;
		}
		ac_prob += Utils.GRASS_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorGRASS;
			return;
		}
		ac_prob += Utils.PURPLE_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorPURPLE;
			return;
		}
		ac_prob += Utils.PLANKTON_PROB;
		if (prob < ac_prob) {
			_color = Utils.ColorPLANKTON;
			return;
		}
		_color = Color.YELLOW;
	}
	
	public void randomizeredReaction() {
		_redreaction = Utils.random.nextInt(6);
	}
	
	public void randomizegreenReaction() {
		_greenreaction = Utils.random.nextInt(6);
	}
	
	public void randomizeblueReaction() {
		_bluereaction = Utils.random.nextInt(6);
	}
	
	public void randomizeplagueReaction() {
		_plaguereaction = Utils.random.nextInt(6);
	}
	
	public void randomizescourgeReaction() {
		_scourgereaction = Utils.random.nextInt(6);
	}
	
	public void randomizewhiteReaction() {
		_whitereaction = Utils.random.nextInt(6);
	}
	
	public void randomizegrayReaction() {
		_grayreaction = Utils.random.nextInt(6);
	}
	
	public void randomizesilverReaction() {
		_silverreaction = Utils.random.nextInt(6);
	}
	
	public void randomizedefaultReaction() {
		_defaultreaction = Utils.random.nextInt(6);
	}
	
	public void randomizeconsumerReaction() {
		_consumerreaction = Utils.random.nextInt(6);
	}
	
	public void randomizeplantReaction() {
		_plantreaction = Utils.random.nextInt(6);
	}
	
	public void randomizemagentaReaction() {
		_magentareaction = Utils.random.nextInt(6);
	}
	
	public void randomizepinkReaction() {
		_pinkreaction = Utils.random.nextInt(6);
	}
	
	public void randomizecoralReaction() {
		_coralreaction = Utils.random.nextInt(6);
	}
	
	public void randomizeorangeReaction() {
		_orangereaction = Utils.random.nextInt(6);
	}
	
	public void randomizebarkReaction() {
		_barkreaction = Utils.random.nextInt(6);
	}
	
	public void randomizevioletReaction() {
		_violetreaction = Utils.random.nextInt(6);
	}
	
	public void randomizevirusReaction() {
		_virusreaction = Utils.random.nextInt(6);
	}
	
	public void randomizemaroonReaction() {
		_maroonreaction = Utils.random.nextInt(6);
	}
	
	public void randomizeoliveReaction() {
		_olivereaction = Utils.random.nextInt(6);
	}
	
	public void randomizemintReaction() {
		_mintreaction = Utils.random.nextInt(6);
	}
	
	public void randomizecreamReaction() {
		_creamreaction = Utils.random.nextInt(6);
	}
	
	public void randomizespikeReaction() {
		_spikereaction = Utils.random.nextInt(6);
	}
	
	public void randomizefallowReaction() {
		_fallowreaction = Utils.random.nextInt(6);
	}
	
	public void randomizelightblueReaction() {
		_lightbluereaction = Utils.random.nextInt(6);
	}
	
	public void randomizeochreReaction() {
		_ochrereaction = Utils.random.nextInt(6);
	}
	
	public void randomizeskyReaction() {
		_skyreaction = Utils.random.nextInt(6);
	}
	
	public void randomizelilacReaction() {
		_lilacreaction = Utils.random.nextInt(6);
	}
	
	public void randomizefireReaction() {
		_firereaction = Utils.random.nextInt(6);
	}
	
	public void randomizelightbrownReaction() {
		_lightbrownreaction = Utils.random.nextInt(6);
	}
	
	public void randomizegreenbrownReaction() {
		_greenbrownreaction = Utils.random.nextInt(6);
	}
	
	public void randomizebrownReaction() {
		_brownreaction = Utils.random.nextInt(6);
	}
	
	public void randomizeiceReaction() {
		_icereaction = Utils.random.nextInt(6);
	}
	
	public void randomizebrokenReaction() {
		_brokenreaction = Utils.random.nextInt(6);
	}
	
	public void randomizesickReaction() {
		_sickreaction = Utils.random.nextInt(6);
	}
	
	public void randomizefriendReaction() {
		_friendreaction = Utils.random.nextInt(6);
	}

	public void randomizeLength() {
		_length = 2.0 + Utils.random.nextDouble() * 16.0;
	}

	public void randomizeTheta() {
		_theta = Utils.random.nextDouble() * 2.0 * Math.PI;
	}

	/**
	 * Randomize the component of this gene: final point and color. Coordinates
	 * are given a random number between -13 and -2 or 2 and 13. Color is given
	 * a random color. The probability of each color is taken from user
	 * preferences.
	 */
	public void randomize() {
		randomizeLength();
		randomizeTheta();
		randomizeColor();
		randomizeredReaction();
		randomizegreenReaction();
		randomizeblueReaction();
		randomizeplagueReaction();
		randomizescourgeReaction();
		randomizewhiteReaction();
		randomizegrayReaction();
		randomizesilverReaction();
		randomizedefaultReaction();
		randomizeconsumerReaction();
		randomizeplantReaction();
		randomizemagentaReaction();
		randomizepinkReaction();
		randomizecoralReaction();
		randomizeorangeReaction();
		randomizebarkReaction();
		randomizevioletReaction();
		randomizevirusReaction();
		randomizemaroonReaction();
		randomizeoliveReaction();
		randomizemintReaction();
		randomizecreamReaction();
		randomizespikeReaction();
		randomizefallowReaction();
		randomizelightblueReaction();
		randomizeochreReaction();
		randomizeskyReaction();
		randomizelilacReaction();
		randomizefireReaction();
		randomizelightbrownReaction();
		randomizegreenbrownReaction();
		randomizebrownReaction();
		randomizeiceReaction();
		randomizebrokenReaction();
		randomizesickReaction();
		randomizefriendReaction();
	}

	/**
	 * Return an exact copy of this gene.
	 */
	@Override
	public Object clone() {
		Gene newGen = null;
		try {
			newGen = (Gene) super.clone();
		} catch (CloneNotSupportedException e) {// We should never reach this
		}
		return newGen;
	}

	public double getLength() {
		return _length;
	}

	public double getTheta() {
		return _theta;
	}

	/**
	 * Returns the segment's color.
	 * 
	 * @return the segment's color
	 */
	public Color getColor() {
		return _color;
	}
	
	/**
	 * Returns the segment's branching point.
	 * 
	 * @return the segment's branching point
	 */
	public int getBranch() {
		return _branch;
	}
	
	/**
	 * Returns the segment's reactions.
	 * 
	 * @return the segment's reactions
	 */
	public int getredReaction() {
		return _redreaction;
	}
	
	public int getgreenReaction() {
		return _greenreaction;
	}
	
	public int getblueReaction() {
		return _bluereaction;
	}
	
	public int getplagueReaction() {
		return _plaguereaction;
	}
	
	public int getscourgeReaction() {
		return _scourgereaction;
	}
	
	public int getwhiteReaction() {
		return _whitereaction;
	}
	
	public int getgrayReaction() {
		return _grayreaction;
	}
	
	public int getsilverReaction() {
		return _silverreaction;
	}
	
	public int getdefaultReaction() {
		return _defaultreaction;
	}
	
	public int getconsumerReaction() {
		return _consumerreaction;
	}
	
	public int getplantReaction() {
		return _plantreaction;
	}
	
	public int getmagentaReaction() {
		return _magentareaction;
	}
	
	public int getpinkReaction() {
		return _pinkreaction;
	}
	
	public int getcoralReaction() {
		return _coralreaction;
	}
	
	public int getorangeReaction() {
		return _orangereaction;
	}
	
	public int getbarkReaction() {
		return _barkreaction;
	}
	
	public int getvioletReaction() {
		return _violetreaction;
	}
	
	public int getvirusReaction() {
		return _virusreaction;
	}
	
	public int getmaroonReaction() {
		return _maroonreaction;
	}
	
	public int getoliveReaction() {
		return _olivereaction;
	}
	
	public int getmintReaction() {
		return _mintreaction;
	}
	
	public int getcreamReaction() {
		return _creamreaction;
	}
	
	public int getspikeReaction() {
		return _spikereaction;
	}
	
	public int getfallowReaction() {
		return _fallowreaction;
	}
	
	public int getlightblueReaction() {
		return _lightbluereaction;
	}
	
	public int getochreReaction() {
		return _ochrereaction;
	}
	
	public int getskyReaction() {
		return _skyreaction;
	}
	
	public int getlilacReaction() {
		return _lilacreaction;
	}
	
	public int getfireReaction() {
		return _firereaction;
	}
	
	public int getlightbrownReaction() {
		return _lightbrownreaction;
	}
	
	public int getgreenbrownReaction() {
		return _greenbrownreaction;
	}
	
	public int getbrownReaction() {
		return _brownreaction;
	}
	
	public int geticeReaction() {
		return _icereaction;
	}
	
	public int getbrokenReaction() {
		return _brokenreaction;
	}
	
	public int getsickReaction() {
		return _sickreaction;
	}
    
	public int getfriendReaction() {
		return _friendreaction;
	}

	/**
	 * Assign a color to the segment.
	 * 
	 * @param color
	 *            The color to assign
	 */
	public void setColor(Color color) {
		_color = color;
	}
	
	/**
	 * Assign a branching point to the segment.
	 * 
	 * @param branch
	 *            The branching point to assign
	 */
	public void setBranch(int branch) {
		_branch = branch;
	}
	
	/**
	 * Assign a reaction to the segment.
	 * 
	 * @param reaction
	 *            The reaction to assign
	 */
	public void setredReaction(int redreaction) {
		_redreaction = redreaction;
	}
	
	public void setgreenReaction(int greenreaction) {
		_greenreaction = greenreaction;
	}
	
	public void setblueReaction(int bluereaction) {
		_bluereaction = bluereaction;
	}
	
	public void setplagueReaction(int plaguereaction) {
		_plaguereaction = plaguereaction;
	}
	
	public void setscourgeReaction(int scourgereaction) {
		_scourgereaction = scourgereaction;
	}
	
	public void setwhiteReaction(int whitereaction) {
		_whitereaction = whitereaction;
	}
	
	public void setgrayReaction(int grayreaction) {
		_grayreaction = grayreaction;
	}
	
	public void setsilverReaction(int silverreaction) {
		_silverreaction = silverreaction;
	}
	
	public void setdefaultReaction(int defaultreaction) {
		_defaultreaction = defaultreaction;
	}
	
	public void setconsumerReaction(int consumerreaction) {
		_consumerreaction = consumerreaction;
	}
	
	public void setplantReaction(int plantreaction) {
		_plantreaction = plantreaction;
	}
	
	public void setmagentaReaction(int magentareaction) {
		_magentareaction = magentareaction;
	}
	
	public void setpinkReaction(int pinkreaction) {
		_pinkreaction = pinkreaction;
	}
	
	public void setcoralReaction(int coralreaction) {
		_coralreaction = coralreaction;
	}
	
	public void setorangeReaction(int orangereaction) {
		_orangereaction = orangereaction;
	}
	
	public void setbarkReaction(int barkreaction) {
		_barkreaction = barkreaction;
	}
	
	public void setvioletReaction(int violetreaction) {
		_violetreaction = violetreaction;
	}
	
	public void setvirusReaction(int virusreaction) {
		_virusreaction = virusreaction;
	}
	
	public void setmaroonReaction(int maroonreaction) {
		_maroonreaction = maroonreaction;
	}
	
	public void setoliveReaction(int olivereaction) {
		_olivereaction = olivereaction;
	}
	
	public void setmintReaction(int mintreaction) {
		_mintreaction = mintreaction;
	}
	
	public void setcreamReaction(int creamreaction) {
		_creamreaction = creamreaction;
	}
	
	public void setspikeReaction(int spikereaction) {
		_spikereaction = spikereaction;
	}
	
	public void setfallowReaction(int fallowreaction) {
		_fallowreaction = fallowreaction;
	}
	
	public void setlightblueReaction(int lightbluereaction) {
		_lightbluereaction = lightbluereaction;
	}
	
	public void setochreReaction(int ochrereaction) {
		_ochrereaction = ochrereaction;
	}
	
	public void setskyReaction(int skyreaction) {
		_skyreaction = skyreaction;
	}
	
	public void setlilacReaction(int lilacreaction) {
		_lilacreaction = lilacreaction;
	}
	
	public void setfireReaction(int firereaction) {
		_firereaction = firereaction;
	}
	
	public void setlightbrownReaction(int lightbrownreaction) {
		_lightbrownreaction = lightbrownreaction;
	}
	
	public void setgreenbrownReaction(int greenbrownreaction) {
		_greenbrownreaction = greenbrownreaction;
	}
	
	public void setbrownReaction(int brownreaction) {
		_brownreaction = brownreaction;
	}
	
	public void seticeReaction(int icereaction) {
		_icereaction = icereaction;
	}
	
	public void setbrokenReaction(int brokenreaction) {
		_brokenreaction = brokenreaction;
	}
	
	public void setsickReaction(int sickreaction) {
		_sickreaction = sickreaction;
	}
	
	public void setfriendReaction(int friendreaction) {
		_friendreaction = friendreaction;
	}

	public void setLength(double length) {
		_length = length;
	}

	public void setTheta(double theta) {
		_theta = theta;
	}
}
