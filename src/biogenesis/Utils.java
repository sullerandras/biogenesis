/* Copyright (C) 2006-2010  Joan Queralt Molina
 * Color Mod (C) 2012-2022  MarcoDBAA
 * Backup function by Tyler Coleman
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 * This class contains all global program parameters and a few useful methods for
 * reading or writing them from the user preferences.
 */
public final class Utils {
	/**
	 * This value indicates the version of the program.
	 * There are two digits for version, two for subversion and two for revision, so
	 * a value of 400 or 000400 means version 0, subversion 4 and revision 0.
	 *
	 * All serializable classes use this value as their serialVersionUID.
	 */
	static final int FILE_VERSION = 700;
	static final int VERSION = 900; //two digits for version, subversion and revision

	// Default values for parameters
	final static int DEF_WINDOW_X = 0;
	final static int DEF_WINDOW_Y = 0;
	final static int DEF_WINDOW_WIDTH = 640;
	final static int DEF_WINDOW_HEIGHT = 480;
	final static int DEF_WINDOW_STATE = JFrame.NORMAL;
	/**
	 * This is the default value of organisms that are created when a new world begins.
	 */
	final static int DEF_INITIAL_ORGANISMS = 12500;
	/**
	 * This is the default complexity of random organisms.
	 */
	final static int DEF_INITIAL_COMPLEXITY = 2;
	/**
	 * This is the default complexity a clade can have.
	 */
	final static int DEF_CLADE_COMPLEXITY = -1;
	/**
	 * This is the default amount of O2 that exists in a newly created world.
	 */
	final static double DEF_INITIAL_O2 = 0;
	/**
	 * This is the default amount of CO2 that exists in a newly created world.
	 */
	final static double DEF_INITIAL_CO2 = 500000;
	/**
	 * This is the default amount of CH4 that exists in a newly created world.
	 */
	final static double DEF_INITIAL_CH4 = 0;
	/**
	 * This is the initial size of the organisms vector.
	 */
	final static int DEF_ORGANISMS_VECTOR_SIZE = 50;
	/**
	 * This is the default world's width.
	 */
	final static int DEF_WORLD_WIDTH = 7000;
	/**
	 * This is the default world's height.
	 */
	final static int DEF_WORLD_HEIGHT = 7000;
	/**
	 * This is the default maximum age that an organism can achieve,
	 * without counting the number of segments.
	 */
	final static int DEF_MAX_AGE = 6;
	/**
	 * This is the default age divisor,
	 * which adds the number of segments to the maximum age, divided by this value.
	 */
	final static int DEF_AGE_DIVISOR = 4;
	/**
	 * This is the default CO2 to CH4 divisor,
	 * which turns CO2 into CH4, divided by this value.
	 */
	final static int DEF_CO2_TO_CH4_DIVISOR = 999;
	/**
	 * This is the default CH4 to CO2 divisor,
	 * which turns CH4 into CO2, divided by this value.
	 */
	final static int DEF_CH4_TO_CO2_DIVISOR = 1000;
	/**
	 * This is the default rubbing coefficient that is applied to movements. This value is
	 * multiplied by the speed at every frame.
	 */
	final static double DEF_RUBBING = 0.98d;
	/**
	 * This is the default meta mutation probability,
	 * that the individual mutation rate of an organism changes.
	 */
	final static int DEF_META_MUTATION_RATE = 1000;
	/**
	 * This is the default maximal mutation rate of an organism.
	 */
	final static int DEF_MAX_MUTATION_RATE = 100;
	/**
	 * This is the default minimal mutation rate of an organism.
	 */
	final static int DEF_MIN_MUTATION_RATE = 0;
	/**
	 * This is the default maximal clone rate of an organism.
	 */
	final static int DEF_MAX_CLONE_RATE = 33;
	/**
	 * This is the default minimal clone rate of an organism.
	 */
	final static int DEF_MIN_CLONE_RATE = 0;
	/**
	 * This default value is used to calculate the energy cost that an organism must
	 * pay to maintain a segment. It spends the length of the segment divided by this
	 * number units of energy.
	 */
	final static int DEF_SEGMENT_COST_DIVISOR = 5000;
	/**
	 * This default value is multiplied by the length of red segments to calculate
	 * the amount of energy that is stolen from an organism when it is touched.
	 */
	final static double DEF_ORGANIC_OBTAINED_ENERGY = 2d;
	/**
	 * This default value divides the length of green segments to calculate the
	 * amount of solar energy that the segment can get in a frame.
	 */
	final static int DEF_GREEN_OBTAINED_ENERGY_DIVISOR = 1000;
	/**
	 * This is the default value that determines how much energy will be released
	 * to the atmosphere when an organism touches another organism with a consuming segment.
	 * The energy that the target lost is multiplied by this value and the result
	 * is added to the atmospheric CO2. The rest is added to the attacker's energy.
	 */
	final static double DEF_ORGANIC_SUBS_PRODUCED = 0.1d;
	/**
	 * This is the default value that determines how much energy will be released
	 * to the atmosphere when an organism touches another organism with a cream segment.
	 * The energy that the target lost is multiplied by this value and the result
	 * is added to the atmospheric CO2. The rest is added to the attacker's energy.
	 */
	final static double DEF_CREAM_ORGANIC_SUBS_PRODUCED = 0.0125d;
	/**
	 * This is the default healing efficiency.
	 * Healing is more efficient, if this value is lower.
	 */
	final static int DEF_HEALING = 40000;
	/**
	 * This is the default immune system efficiency.
	 * Immune system is more efficient, if this value is lower.
	 */
	final static int DEF_IMMUNE_SYSTEM = 40000;
	/**
	 * This is the default lavender shield efficiency.
	 * Lavender shield is more efficient, if this value is higher.
	 */
	final static int DEF_LAVENDER_SHIELD = 10000;
	/**
	 * This is the default indigo efficiency divisor.
	 * Indigo is more efficient, if this value is lower.
	 */
	final static double DEF_INDIGO_DIVISOR = 10.0;
	/**
	 * This is the default gold efficiency divisor.
	 * Gold is more efficient, if this value is lower.
	 */
	final static double DEF_GOLD_DIVISOR = 2;
	/**
	 * This is the default energy that is consumed when an organism dodged an attack.
	 */
	final static double DEF_DODGE_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the delay when a dark jade segment regenerates.
	 */
	final static int DEF_DARKJADE_DELAY = 4;
	/**
	 * This is the default energy that is consumed when a red segment is used.
	 */
	final static double DEF_RED_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a green segment is used.
	 */
	final static double DEF_GREEN_ENERGY_CONSUMPTION = 1.1d;
	/**
	 * This is the default energy that is consumed when a blue segment is used.
	 */
	final static double DEF_BLUE_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a cyan segment is used.
	 */
	final static double DEF_CYAN_ENERGY_CONSUMPTION = 12d;
	/**
	 * This is the default energy that is consumed when a white segment is used.
	 */
	final static double DEF_WHITE_ENERGY_CONSUMPTION = 0.1d;
	/**
	 * This is the default energy that is consumed when a white non-plant segment is used.
	 */
	final static double DEF_VIRUS_ENERGY_CONSUMPTION = 0.01d;
	/**
	 * This is the default energy that is consumed when a gray segment is used.
	 */
	final static double DEF_GRAY_ENERGY_CONSUMPTION = 0.1d;
	/**
	 * This is the default energy that is consumed when a dark gray segment (for enhanced forest) is used.
	 */
	final static double DEF_DARKGRAY_ENERGY_CONSUMPTION = 10d;
	/**
	 * This is the default energy that is consumed when a silver segment is used.
	 */
	final static double DEF_SILVER_ENERGY_CONSUMPTION = 0.1d;
	/**
	 * This is the default energy that is consumed when a yellow segment is used.
	 */
	final static double DEF_YELLOW_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a magenta segment is used.
	 */
	final static double DEF_MAGENTA_ENERGY_CONSUMPTION = 0.01d;
	/**
	 * This is the default energy that is consumed when a pink segment is used.
	 */
	final static double DEF_PINK_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a coral segment is used.
	 */
	final static double DEF_CORAL_ENERGY_CONSUMPTION = 0.01d;
	/**
	 * This is the default energy that is consumed when a orange segment is used.
	 */
	final static double DEF_ORANGE_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a forest segment is used.
	 */
	final static double DEF_FOREST_ENERGY_CONSUMPTION = 1d;
	/**
	 * This is the default energy that is consumed when a crowded forest segment is used.
	 */
	final static double DEF_CROWDEDFOREST_ENERGY_CONSUMPTION = 1d;
	/**
	 * This is the default energy that is consumed when an organism touches a symbiont (with rose segments).
	 */
	final static double DEF_SYMBIONT_ENERGY_CONSUMPTION = 0.58d;
	/**
	 * This is the default energy that is consumed when a spring segment is used.
	 */
	final static double DEF_SPRING_ENERGY_CONSUMPTION = 1d;
	/**
	 * This is the default energy that is consumed when a leaf segment is used.
	 */
	final static double DEF_LEAF_ENERGY_CONSUMPTION = 1.15d;
	/**
	 * This is the default energy that is consumed when a modified leaf segment is used.
	 */
	final static double DEF_MODLEAF_ENERGY_CONSUMPTION = 0.99d;
	/**
	 * This is the default energy that is consumed when a lime segment is used.
	 */
	final static double DEF_LIME_ENERGY_CONSUMPTION = 1.25d;
	/**
	 * This is the default energy that is consumed when a crowded lime segment is used.
	 */
	final static double DEF_CROWDEDLIME_ENERGY_CONSUMPTION = 0.5d;
	/**
	 * This is the default energy that is consumed when a summer segment is used.
	 */
	final static double DEF_SUMMER_ENERGY_CONSUMPTION = 1.65d;
	/**
	 * This is the default energy that is consumed when a consuming spike segment is used.
	 */
	final static double DEF_MOSQUITO_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a bark segment is used.
	 */
	final static double DEF_BARK_ENERGY_CONSUMPTION = 0.7d;
	/**
	 * This is the default energy that is consumed when a jade segment is used.
	 */
	final static double DEF_JADE_ENERGY_CONSUMPTION = 1d;
	/**
	 * This is the default energy that is consumed when a grass segment is used.
	 */
	final static double DEF_GRASS_ENERGY_CONSUMPTION = 1d;
	/**
	 * This is the default energy that is consumed when a purple segment is used.
	 */
	final static double DEF_PURPLE_ENERGY_CONSUMPTION = 4d;
	/**
	 * This is the default energy that is consumed when a c4 segment is used.
	 */
	final static double DEF_C4_ENERGY_CONSUMPTION = 0.54d;
	/**
	 * This is the default energy that is consumed when a violet segment is used.
	 */
	final static double DEF_VIOLET_ENERGY_CONSUMPTION = 0.1d;
	/**
	 * This is the default energy that is consumed when a teal segment is used.
	 */
	final static double DEF_TEAL_ENERGY_CONSUMPTION = 11.88d;
	/**
	 * This is the default energy that is consumed when an eye segment is used.
	 */
	final static double DEF_EYE_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a maroon segment is used.
	 */
	final static double DEF_MAROON_ENERGY_CONSUMPTION = 2d;
	/**
	 * This is the default energy that is consumed when a olive segment is used.
	 */
	final static double DEF_OLIVE_ENERGY_CONSUMPTION = 0.1d;
	/**
	 * This is the default energy that is consumed when a mint segment is used.
	 */
	final static double DEF_MINT_ENERGY_CONSUMPTION = 0.07d;
	/**
	 * This is the default energy that is consumed when a cream segment is used.
	 */
	final static double DEF_CREAM_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a rose segment is used.
	 */
	final static double DEF_ROSE_ENERGY_CONSUMPTION = 0.005d;
	/**
	 * This is the default energy that is consumed when a dark segment is used.
	 */
	final static double DEF_DARK_ENERGY_CONSUMPTION = 0.001d;
	/**
	 * This is the default energy that is consumed when an ochre segment is used.
	 */
	final static double DEF_OCHRE_ENERGY_CONSUMPTION = 0.4d;
	/**
	 * This is the default energy that is consumed when a sky segment is used.
	 */
	final static double DEF_SKY_ENERGY_CONSUMPTION = 0.05d;
	/**
	 * This is the default energy that is consumed when a lilac segment is used.
	 */
	final static double DEF_LILAC_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a fire segment is used.
	 */
	final static double DEF_FIRE_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a consuming silver segment is used.
	 */
	final static double DEF_EXPERIENCE_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a blond segment is used.
	 */
	final static double DEF_BLOND_ENERGY_CONSUMPTION = 0.037d;
	/**
	 * This is the default energy that is consumed when a flower segment is used.
	 */
	final static double DEF_FLOWER_ENERGY_CONSUMPTION = 0.01d;
	/**
	 * This is the default energy that is consumed when a auburn segment is used.
	 */
	final static double DEF_AUBURN_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when an indigo segment is used.
	 */
	final static double DEF_INDIGO_ENERGY_CONSUMPTION = 1d;
	/**
	 * This is the default energy that is consumed when a lavender segment is used.
	 */
	final static double DEF_LAVENDER_ENERGY_CONSUMPTION = 1d;
	/**
	 * This is the default energy that is consumed when a fallow segment is used.
	 */
	final static double DEF_FALLOW_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a spore segment is used.
	 */
	final static double DEF_SPORE_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a plague segment is used.
	 */
	final static double DEF_PLAGUE_ENERGY_CONSUMPTION = 10d;
	/**
	 * This is the default energy that is consumed when a non-plant plague segment is used.
	 */
	final static double DEF_SCOURGE_ENERGY_CONSUMPTION = 0.1d;
	/**
	 * This is the default energy that is consumed when a spike segment is used.
	 */
	final static double DEF_SPIKE_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default probability for a new segment to be red.
	 */
	final static int DEF_RED_PROB = 1;
	/**
	 * This is the default probability for a new segment to be green.
	 */
	final static int DEF_GREEN_PROB = 3;
	/**
	 * This is the default probability for a new segment to be blue.
	 */
	final static int DEF_BLUE_PROB = 2;
	/**
	 * This is the default probability for a new segment to be cyan.
	 */
	final static int DEF_CYAN_PROB = 8;
	/**
	 * This is the default probability for a new segment to be white.
	 */
	final static int DEF_WHITE_PROB = 4;
	/**
	 * This is the default probability for a new segment to be gray.
	 */
	final static int DEF_GRAY_PROB = 1;
	/**
	 * This is the default probability for a new segment to be dark gray.
	 */
	final static int DEF_DARKGRAY_PROB = 5;
	/**
	 * This is the default probability for a new segment to be silver.
	 */
	final static int DEF_SILVER_PROB = 1;
	/**
	 * This is the default probability for a new segment to be yellow.
	 */
	final static int DEF_YELLOW_PROB = 2;
	/**
	 * This is the default probability for a new segment to be magenta.
	 */
	final static int DEF_MAGENTA_PROB = 5;
	/**
	 * This is the default probability for a new segment to be pink.
	 */
	final static int DEF_PINK_PROB = 2;
	/**
	 * This is the default probability for a new segment to be coral.
	 */
	final static int DEF_CORAL_PROB = 1;
	/**
	 * This is the default probability for a new segment to be orange.
	 */
	final static int DEF_ORANGE_PROB = 1;
	/**
	 * This is the default probability for a new segment to be forest.
	 */
	final static int DEF_FOREST_PROB = 3;
	/**
	 * This is the default probability for a new segment to be spring.
	 */
	final static int DEF_SPRING_PROB = 3;
	/**
	 * This is the default probability for a new segment to be leaf.
	 */
	final static int DEF_LEAF_PROB = 3;
	/**
	 * This is the default probability for a new segment to be lime.
	 */
	final static int DEF_LIME_PROB = 3;
	/**
	 * This is the default probability for a new segment to be summer.
	 */
	final static int DEF_SUMMER_PROB = 3;
	/**
	 * This is the default probability for a new segment to be bark.
	 */
	final static int DEF_BARK_PROB = 3;
	/**
	 * This is the default probability for a new segment to be jade.
	 */
	final static int DEF_JADE_PROB = 3;
	/**
	 * This is the default probability for a new segment to be grass.
	 */
	final static int DEF_GRASS_PROB = 3;
	/**
	 * This is the default probability for a new segment to be purple.
	 */
	final static int DEF_PURPLE_PROB = 3;
	/**
	 * This is the default probability for a new segment to be c4.
	 */
	final static int DEF_C4_PROB = 3;
	/**
	 * This is the default probability for a new segment to be violet.
	 */
	final static int DEF_VIOLET_PROB = 1;
	/**
	 * This is the default probability for a new segment to be teal.
	 */
	final static int DEF_TEAL_PROB = 8;
	/**
	 * This is the default probability for a new segment to be eye.
	 */
	final static int DEF_EYE_PROB = 4;
	/**
	 * This is the default probability for a new segment to be maroon.
	 */
	final static int DEF_MAROON_PROB = 2;
	/**
	 * This is the default probability for a new segment to be olive.
	 */
	final static int DEF_OLIVE_PROB = 1;
	/**
	 * This is the default probability for a new segment to be mint.
	 */
	final static int DEF_MINT_PROB = 2;
	/**
	 * This is the default probability for a new segment to be cream.
	 */
	final static int DEF_CREAM_PROB = 1;
	/**
	 * This is the default probability for a new segment to be rose.
	 */
	final static int DEF_ROSE_PROB = 2;
	/**
	 * This is the default probability for a new segment to be dark.
	 */
	final static int DEF_DARK_PROB = 1;
	/**
	 * This is the default probability for a new segment to be ochre.
	 */
	final static int DEF_OCHRE_PROB = 1;
	/**
	 * This is the default probability for a new segment to be sky.
	 */
	final static int DEF_SKY_PROB = 1;
	/**
	 * This is the default probability for a new segment to be lilac.
	 */
	final static int DEF_LILAC_PROB = 1;
	/**
	 * This is the default probability for a new segment to be fire.
	 */
	final static int DEF_FIRE_PROB = 1;
	/**
	 * This is the default probability for a new segment to be gold.
	 */
	final static int DEF_GOLD_PROB = 2;
	/**
	 * This is the default probability for a new segment to be blond.
	 */
	final static int DEF_BLOND_PROB = 2;
	/**
	 * This is the default probability for a new segment to be flower.
	 */
	final static int DEF_FLOWER_PROB = 2;
	/**
	 * This is the default probability for a new segment to be auburn.
	 */
	final static int DEF_AUBURN_PROB = 2;
	/**
	 * This is the default probability for a new segment to be indigo.
	 */
	final static int DEF_INDIGO_PROB = 2;
	/**
	 * This is the default probability for a new segment to be lavender.
	 */
	final static int DEF_LAVENDER_PROB = 2;
	/**
	 * This is the default probability for a new segment to be fallow.
	 */
	final static int DEF_FALLOW_PROB = 1;
	/**
	 * This is the default probability for a new segment to be spore.
	 */
	final static int DEF_SPORE_PROB = 2;
	/**
	 * This is the default probability for a new segment to be plague.
	 */
	final static int DEF_PLAGUE_PROB = 1;
	/**
	 * This is the default probability for a new segment to be spike.
	 */
	final static int DEF_SPIKE_PROB = 1;
	/**
	 * This default value divides the amount of CO2 in the atmosphere to
	 * establish how many CO2 an organism can drain in a frame.
	 */
	final static int DEF_DRAIN_SUBS_DIVISOR = 5000;
	/**
	 * This is the default value for the initial energy that an organism has
	 * if it isn't a child of another organism.
	 */
	final static int DEF_INITIAL_ENERGY = 40;
	/**
	 * This is the default value for the reaction speed factor.
	 */
	final static double DEF_REACTION_VEL = 1d;
	/**
	 * This is the default value for the spore-1 speed factor.
	 */
	final static double DEF_SPORE1_VEL = 12d;
	/**
	 * This is the default value for the spore-5 speed factor.
	 */
	final static double DEF_SPORE5_VEL = 1.33d;
	/**
	 * This is the default value for the spore-10-12 speed factor.
	 */
	final static double DEF_SPORE10_VEL = 12d;
	/**
	 * This is the default value for the maximum speed that an organism can
	 * achieve.
	 */
	final static double DEF_MAX_VEL = 5d;
	/**
	 * This is the default value for the maximum rotational speed that an organism
	 * can achieve.
	 */
	final static double DEF_MAX_ROT = Math.PI / 16d;
	/**
	 * This is the default elasticity coefficient. This value is used to establish
	 * the energy that a movement keeps after a collision.
	 */
	final static double DEF_ELASTICITY = 0.8d;
	/**
	 * This is the default number of milliseconds that pass between frames.
	 */
	final static int DEF_DELAY = 20;
	final static boolean DEF_repaintWorld = true;
	final static String DEF_repaintWorldStrategy = RepaintWorldStrategy.ALWAYS.toString();
	final static int DEF_STATUS_BAR_REFRESH_FPS = 4;
	final static int DEF_STATISTICS_REFRESH_FPS = 2;
	/**
	 * This is the default value for having or not having automatic backups.
	 */
	final static boolean DEF_AUTO_BACKUP = false;
	/**
	 * This is the default value for compressing or not the backups.
	 */
	final static boolean DEF_COMPRESS_BACKUPS = false;
	/**
	 * This is the default value for saving or not saving world stats as a CSV file when saving
	 * automatic backups.
	 */
	final static boolean DEF_AUTO_BACKUP_CSV = false;
	/**
	 * This is the default value for saving or not saving the world as a PNG image when saving
	 * automatic backups.
	 */
	final static boolean DEF_AUTO_BACKUP_WORLD_PNG = false;
	/**
	 * This is the default value for saving or not saving the statistics dialog as a PNG image
	 * when saving automatic backups.
	 * Note that the statistics dialog has to be open to be able to automatically save it as PNG.
	 * If the statistics dialog is not open when the automatic backup is performed, then no image
	 * will be saved.
	 */
	final static boolean DEF_AUTO_BACKUP_STATISTICS_PNG = false;
	/**
	 * This is the default number of game time units that pass between backups.
	 */
	final static int DEF_BACKUP_DELAY = 100;
	/**
	 * This is the default port where the net server will listen for connections.
	 */
	final static int DEF_LOCAL_PORT = 8888;
	/**
	 * This is the default name your organisms will use in the network.
	 */
	final static String DEF_USER_NAME = "ID"; //$NON-NLS-1$
	/**
	 * This is the default value for the maximum number of network connections allowed.
	 */
	final static int DEF_MAX_CONNECTIONS = 1;
	/**
	 * This is the default value for accepting or not new connections from other hosts.
	 */
	final static boolean DEF_ACCEPT_CONNECTIONS = false;
	/**
	 * This is the default value for using or not a meta-server to find other instances
	 * of biogenesis running. At the moment it is not used.
	 */
	final static boolean DEF_CONNECT_TO_SERVER = false;
	/**
	 * This is the default IP that the meta-server will have. At the moment it is not used.
	 */
	final static String DEF_SERVER_ADDRESS = ""; //$NON-NLS-1$
	/**
	 * This is the default port where the meta-server will listen. At the moment it is not used.
	 */
	final static int DEF_SERVER_PORT = 0;
	/**
	 * This is the default hardware acceleration applied when drawing
	 */
	final static int DEF_HARDWARE_ACCELERATION = 0; //0 none, 1 try opengl, 2 opengl

	final static double DEF_DECAY_ENERGY = 0.1d;
	// Effective parameters values
	static int WINDOW_X = DEF_WINDOW_X;
	static int WINDOW_Y = DEF_WINDOW_Y;
	static int WINDOW_WIDTH = DEF_WINDOW_WIDTH;
	static int WINDOW_HEIGHT = DEF_WINDOW_HEIGHT;
	static int WINDOW_STATE = DEF_WINDOW_STATE;
	/**
	 * This is the effective value of organisms that are created when a new world begins.
	 */
	static int INITIAL_ORGANISMS = DEF_INITIAL_ORGANISMS;
	/**
	 * This is the effective complexity of random organisms.
	 */
	static int INITIAL_COMPLEXITY = DEF_INITIAL_COMPLEXITY;
	/**
	 * This is the effective complexity a clade can have.
	 */
	static int CLADE_COMPLEXITY = DEF_CLADE_COMPLEXITY;
	/**
	 * This is the effective amount of O2 that exists in a newly created world.
	 */
	static double INITIAL_O2 = DEF_INITIAL_O2;
	/**
	 * This is the effective amount of CO2 that exists in a newly created world.
	 */
	static double INITIAL_CO2 = DEF_INITIAL_CO2;
	/**
	 * This is the effective amount of CH4 that exists in a newly created world.
	 */
	static double INITIAL_CH4 = DEF_INITIAL_CH4;
	/**
	 * This is the effective size of the organisms vector.
	 */
	static int ORGANISMS_VECTOR_SIZE = DEF_ORGANISMS_VECTOR_SIZE;
	/**
	 * This is the effective world's width for new worlds.
	 */
	static int WORLD_WIDTH = DEF_WORLD_WIDTH;
	/**
	 * This is the effective world's height for new worlds.
	 */
	static int WORLD_HEIGHT = DEF_WORLD_HEIGHT;
	/**
	 * This is the maximum age that an organism can achieve,
	 * without counting the number of segments.
	 */
	static int MAX_AGE = DEF_MAX_AGE;
	/**
	 * This is the age divisor,
	 * which adds the number of segments to the maximum age, divided by this value.
	 */
	static int AGE_DIVISOR = DEF_AGE_DIVISOR;
	/**
	 * This is the CO2 to CH4 divisor,
	 * which turns CO2 into CH4, divided by this value.
	 */
	static int CO2_TO_CH4_DIVISOR = DEF_CO2_TO_CH4_DIVISOR;
	/**
	 * This is the CH4 to CO2 divisor,
	 * which turns CH4 into CO2, divided by this value.
	 */
	static int CH4_TO_CO2_DIVISOR = DEF_CH4_TO_CO2_DIVISOR;
	/**
	 * This is the rubbing coefficient that is applied to movements. This value is
	 * multiplied by the speed at every frame.
	 */
	static double RUBBING = DEF_RUBBING;
	/**
	 * This is the meta mutation probability,
	 * that the individual mutation rate of an organism changes.
	 */
	static int META_MUTATION_RATE = DEF_META_MUTATION_RATE;
	/**
	 * This is the maximal mutation rate of an organism.
	 */
	static int MAX_MUTATION_RATE = DEF_MAX_MUTATION_RATE;
	/**
	 * This is the minimal mutation rate of an organism.
	 */
	static int MIN_MUTATION_RATE = DEF_MIN_MUTATION_RATE;
	/**
	 * This is the maximal clone rate of an organism.
	 */
	static int MAX_CLONE_RATE = DEF_MAX_CLONE_RATE;
	/**
	 * This is the minimal clone rate of an organism.
	 */
	static int MIN_CLONE_RATE = DEF_MIN_CLONE_RATE;
	/**
	 * This value is used to calculate the energy cost that an organism must
	 * pay to maintain a segment. It spends the length of the segment divided by this
	 * number units of energy.
	 */
	static int SEGMENT_COST_DIVISOR = DEF_SEGMENT_COST_DIVISOR;
	/**
	 * This value is multiplied by the length of red segments to calculate
	 * the amount of energy that is stolen from an organism when it is touched.
	 */
	static double ORGANIC_OBTAINED_ENERGY = DEF_ORGANIC_OBTAINED_ENERGY;
	/**
	 * This value divides the length of green segments to calculate the
	 * amount of solar energy that the segment can get in a frame.
	 */
	static int GREEN_OBTAINED_ENERGY_DIVISOR = DEF_GREEN_OBTAINED_ENERGY_DIVISOR;
	/**
	 * This is the effective value that determines how much energy will be released
	 * to the atmosphere when an organism touches another organism with a consuming segment.
	 * The energy that the target lost is multiplied by this value and the result
	 * is added to the atmospheric CO2. The rest is added to the attacker's energy.
	 */
	static double ORGANIC_SUBS_PRODUCED = DEF_ORGANIC_SUBS_PRODUCED;
	/**
	 * This is the effective value that determines how much energy will be released
	 * to the atmosphere when an organism touches another organism with a cream segment.
	 * The energy that the target lost is multiplied by this value and the result
	 * is added to the atmospheric CO2. The rest is added to the attacker's energy.
	 */
	static double CREAM_ORGANIC_SUBS_PRODUCED = DEF_CREAM_ORGANIC_SUBS_PRODUCED;
	/**
	 * This is the effective healing efficiency.
	 * Healing is more efficient, if this value is lower.
	 */
	static int HEALING = DEF_HEALING;
	/**
	 * This is the effective immune system efficiency.
	 * Immune system is more efficient, if this value is lower.
	 */
	static int IMMUNE_SYSTEM = DEF_IMMUNE_SYSTEM;
	/**
	 * This is the effective lavender shield efficiency.
	 * Lavender shield is more efficient, if this value is higher.
	 */
	static int LAVENDER_SHIELD = DEF_LAVENDER_SHIELD;
	/**
	 * This is the effective indigo efficiency divisor.
	 * Indigo is more efficient, if this value is lower.
	 */
	static double INDIGO_DIVISOR = DEF_INDIGO_DIVISOR;
	/**
	 * This is the effective gold efficiency divisor.
	 * Gold is more efficient, if this value is lower.
	 */
	static double GOLD_DIVISOR = DEF_GOLD_DIVISOR;
	/**
	 * This is the energy that is consumed when an organism dodged an attack.
	 */
	static double DODGE_ENERGY_CONSUMPTION = DEF_DODGE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a dark jade segment regenerates.
	 */
	static int DARKJADE_DELAY = DEF_DARKJADE_DELAY;
	/**
	 * This is the energy that is consumed when a red segment is used.
	 */
	static double RED_ENERGY_CONSUMPTION = DEF_RED_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a green segment is used.
	 */
	static double GREEN_ENERGY_CONSUMPTION = DEF_GREEN_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a blue segment is used.
	 */
	static double BLUE_ENERGY_CONSUMPTION = DEF_BLUE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a cyan segment is used.
	 */
	static double CYAN_ENERGY_CONSUMPTION = DEF_CYAN_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a white segment is used.
	 */
	static double WHITE_ENERGY_CONSUMPTION = DEF_WHITE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a non-plant white segment is used.
	 */
	static double VIRUS_ENERGY_CONSUMPTION = DEF_VIRUS_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a gray segment is used.
	 */
	static double GRAY_ENERGY_CONSUMPTION = DEF_GRAY_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a dark gray segment is used.
	 */
	static double DARKGRAY_ENERGY_CONSUMPTION = DEF_DARKGRAY_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a silver segment is used.
	 */
	static double SILVER_ENERGY_CONSUMPTION = DEF_SILVER_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a yellow segment is used.
	 */
	static double YELLOW_ENERGY_CONSUMPTION = DEF_YELLOW_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a magenta segment is used.
	 */
	static double MAGENTA_ENERGY_CONSUMPTION = DEF_MAGENTA_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a pink segment is used.
	 */
	static double PINK_ENERGY_CONSUMPTION = DEF_PINK_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a coral segment is used.
	 */
	static double CORAL_ENERGY_CONSUMPTION = DEF_CORAL_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a orange segment is used.
	 */
	static double ORANGE_ENERGY_CONSUMPTION = DEF_ORANGE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a forest segment is used.
	 */
	static double FOREST_ENERGY_CONSUMPTION = DEF_FOREST_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a crowded forest segment is used.
	 */
	static double CROWDEDFOREST_ENERGY_CONSUMPTION = DEF_CROWDEDFOREST_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when an organism touches a symbiont (with rose segments).
	 */
	static double SYMBIONT_ENERGY_CONSUMPTION = DEF_SYMBIONT_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a spring segment is used.
	 */
	static double SPRING_ENERGY_CONSUMPTION = DEF_SPRING_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a leaf segment is used.
	 */
	static double LEAF_ENERGY_CONSUMPTION = DEF_LEAF_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a modified leaf segment is used.
	 */
	static double MODLEAF_ENERGY_CONSUMPTION = DEF_MODLEAF_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a lime segment is used.
	 */
	static double LIME_ENERGY_CONSUMPTION = DEF_LIME_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a crowded lime segment is used.
	 */
	static double CROWDEDLIME_ENERGY_CONSUMPTION = DEF_CROWDEDLIME_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a summer segment is used.
	 */
	static double SUMMER_ENERGY_CONSUMPTION = DEF_SUMMER_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a consuming spike segment is used.
	 */
	static double MOSQUITO_ENERGY_CONSUMPTION = DEF_MOSQUITO_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a bark segment is used.
	 */
	static double BARK_ENERGY_CONSUMPTION = DEF_BARK_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a jade segment is used.
	 */
	static double JADE_ENERGY_CONSUMPTION = DEF_JADE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a grass segment is used.
	 */
	static double GRASS_ENERGY_CONSUMPTION = DEF_GRASS_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a purple segment is used.
	 */
	static double PURPLE_ENERGY_CONSUMPTION = DEF_PURPLE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a c4 segment is used.
	 */
	static double C4_ENERGY_CONSUMPTION = DEF_C4_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a violet segment is used.
	 */
	static double VIOLET_ENERGY_CONSUMPTION = DEF_VIOLET_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a teal segment is used.
	 */
	static double TEAL_ENERGY_CONSUMPTION = DEF_TEAL_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when an eye segment is used.
	 */
	static double EYE_ENERGY_CONSUMPTION = DEF_EYE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a maroon segment is used.
	 */
	static double MAROON_ENERGY_CONSUMPTION = DEF_MAROON_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a olive segment is used.
	 */
	static double OLIVE_ENERGY_CONSUMPTION = DEF_OLIVE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a mint segment is used.
	 */
	static double MINT_ENERGY_CONSUMPTION = DEF_MINT_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a cream segment is used.
	 */
	static double CREAM_ENERGY_CONSUMPTION = DEF_CREAM_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a rose segment is used.
	 */
	static double ROSE_ENERGY_CONSUMPTION = DEF_ROSE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a dark segment is used.
	 */
	static double DARK_ENERGY_CONSUMPTION = DEF_DARK_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when an ochre segment is used.
	 */
	static double OCHRE_ENERGY_CONSUMPTION = DEF_OCHRE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a sky segment is used.
	 */
	static double SKY_ENERGY_CONSUMPTION = DEF_SKY_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a lilac segment is used.
	 */
	static double LILAC_ENERGY_CONSUMPTION = DEF_LILAC_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a fire segment is used.
	 */
	static double FIRE_ENERGY_CONSUMPTION = DEF_FIRE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a consuming silver segment is used.
	 */
	static double EXPERIENCE_ENERGY_CONSUMPTION = DEF_EXPERIENCE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a blond segment is used.
	 */
	static double BLOND_ENERGY_CONSUMPTION = DEF_BLOND_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a flower segment is used.
	 */
	static double FLOWER_ENERGY_CONSUMPTION = DEF_FLOWER_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a auburn segment is used.
	 */
	static double AUBURN_ENERGY_CONSUMPTION = DEF_AUBURN_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when an indigo segment is used.
	 */
	static double INDIGO_ENERGY_CONSUMPTION = DEF_INDIGO_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a lavender segment is used.
	 */
	static double LAVENDER_ENERGY_CONSUMPTION = DEF_LAVENDER_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a fallow segment is used.
	 */
	static double FALLOW_ENERGY_CONSUMPTION = DEF_FALLOW_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a spore segment is used.
	 */
	static double SPORE_ENERGY_CONSUMPTION = DEF_SPORE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a plague segment is used.
	 */
	static double PLAGUE_ENERGY_CONSUMPTION = DEF_PLAGUE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a non-plant plague segment is used.
	 */
	static double SCOURGE_ENERGY_CONSUMPTION = DEF_SCOURGE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a spike segment is used.
	 */
	static double SPIKE_ENERGY_CONSUMPTION = DEF_SPIKE_ENERGY_CONSUMPTION;
	/**
	 * This is the probability for a new segment to be red.
	 */
	static int RED_PROB = DEF_RED_PROB;
	/**
	 * This is the probability for a new segment to be green.
	 */
	static int GREEN_PROB = DEF_GREEN_PROB;
	/**
	 * This is the probability for a new segment to be blue.
	 */
	static int BLUE_PROB = DEF_BLUE_PROB;
	/**
	 * This is the probability for a new segment to be cyan.
	 */
	static int CYAN_PROB = DEF_CYAN_PROB;
	/**
	 * This is the probability for a new segment to be white.
	 */
	static int WHITE_PROB = DEF_WHITE_PROB;
	/**
	 * This is the probability for a new segment to be gray.
	 */
	static int GRAY_PROB = DEF_GRAY_PROB;
	/**
	 * This is the probability for a new segment to be dark gray.
	 */
	static int DARKGRAY_PROB = DEF_DARKGRAY_PROB;
	/**
	 * This is the probability for a new segment to be silver.
	 */
	static int SILVER_PROB = DEF_SILVER_PROB;
	/**
	 * This is the probability for a new segment to be yellow.
	 */
	static int YELLOW_PROB = DEF_YELLOW_PROB;
	/**
	 * This is the probability for a new segment to be magenta.
	 */
	static int MAGENTA_PROB = DEF_MAGENTA_PROB;
	/**
	 * This is the probability for a new segment to be pink.
	 */
	static int PINK_PROB = DEF_PINK_PROB;
	/**
	 * This is the probability for a new segment to be coral.
	 */
	static int CORAL_PROB = DEF_CORAL_PROB;
	/**
	 * This is the probability for a new segment to be orange.
	 */
	static int ORANGE_PROB = DEF_ORANGE_PROB;
	/**
	 * This is the probability for a new segment to be forest.
	 */
	static int FOREST_PROB = DEF_FOREST_PROB;
	/**
	 * This is the probability for a new segment to be spring.
	 */
	static int SPRING_PROB = DEF_SPRING_PROB;
	/**
	 * This is the probability for a new segment to be leaf.
	 */
	static int LEAF_PROB = DEF_LEAF_PROB;
	/**
	 * This is the probability for a new segment to be lime.
	 */
	static int LIME_PROB = DEF_LIME_PROB;
	/**
	 * This is the probability for a new segment to be summer.
	 */
	static int SUMMER_PROB = DEF_SUMMER_PROB;
	/**
	 * This is the probability for a new segment to be bark.
	 */
	static int BARK_PROB = DEF_BARK_PROB;
	/**
	 * This is the probability for a new segment to be jade.
	 */
	static int JADE_PROB = DEF_JADE_PROB;
	/**
	 * This is the probability for a new segment to be grass.
	 */
	static int GRASS_PROB = DEF_GRASS_PROB;
	/**
	 * This is the probability for a new segment to be purple.
	 */
	static int PURPLE_PROB = DEF_PURPLE_PROB;
	/**
	 * This is the probability for a new segment to be c4.
	 */
	static int C4_PROB = DEF_C4_PROB;
	/**
	 * This is the probability for a new segment to be violet.
	 */
	static int VIOLET_PROB = DEF_VIOLET_PROB;
	/**
	 * This is the probability for a new segment to be teal.
	 */
	static int TEAL_PROB = DEF_TEAL_PROB;
	/**
	 * This is the probability for a new segment to be eye.
	 */
	static int EYE_PROB = DEF_EYE_PROB;
	/**
	 * This is the probability for a new segment to be maroon.
	 */
	static int MAROON_PROB = DEF_MAROON_PROB;
	/**
	 * This is the probability for a new segment to be olive.
	 */
	static int OLIVE_PROB = DEF_OLIVE_PROB;
	/**
	 * This is the probability for a new segment to be mint.
	 */
	static int MINT_PROB = DEF_MINT_PROB;
	/**
	 * This is the probability for a new segment to be cream.
	 */
	static int CREAM_PROB = DEF_CREAM_PROB;
	/**
	 * This is the probability for a new segment to be rose.
	 */
	static int ROSE_PROB = DEF_ROSE_PROB;
	/**
	 * This is the probability for a new segment to be dark.
	 */
	static int DARK_PROB = DEF_DARK_PROB;
	/**
	 * This is the probability for a new segment to be ochre.
	 */
	static int OCHRE_PROB = DEF_OCHRE_PROB;
	/**
	 * This is the probability for a new segment to be sky.
	 */
	static int SKY_PROB = DEF_SKY_PROB;
	/**
	 * This is the probability for a new segment to be lilac.
	 */
	static int LILAC_PROB = DEF_LILAC_PROB;
	/**
	 * This is the probability for a new segment to be fire.
	 */
	static int FIRE_PROB = DEF_FIRE_PROB;
	/**
	 * This is the probability for a new segment to be gold.
	 */
	static int GOLD_PROB = DEF_GOLD_PROB;
	/**
	 * This is the probability for a new segment to be blond.
	 */
	static int BLOND_PROB = DEF_BLOND_PROB;
	/**
	 * This is the probability for a new segment to be flower.
	 */
	static int FLOWER_PROB = DEF_FLOWER_PROB;
	/**
	 * This is the probability for a new segment to be auburn.
	 */
	static int AUBURN_PROB = DEF_AUBURN_PROB;
	/**
	 * This is the probability for a new segment to be indigo.
	 */
	static int INDIGO_PROB = DEF_INDIGO_PROB;
	/**
	 * This is the probability for a new segment to be lavender.
	 */
	static int LAVENDER_PROB = DEF_LAVENDER_PROB;
	/**
	 * This is the probability for a new segment to be fallow.
	 */
	static int FALLOW_PROB = DEF_FALLOW_PROB;
	/**
	 * This is the probability for a new segment to be spore.
	 */
	static int SPORE_PROB = DEF_SPORE_PROB;
	/**
	 * This is the probability for a new segment to be plague.
	 */
	static int PLAGUE_PROB = DEF_PLAGUE_PROB;
	/**
	 * This is the probability for a new segment to be spike.
	 */
	static int SPIKE_PROB = DEF_SPIKE_PROB;
	/**
	 * This value divides the amount of CO2 in the atmosphere to
	 * establish how many CO2 an organism can drain in a frame.
	 */
	static int DRAIN_SUBS_DIVISOR = DEF_DRAIN_SUBS_DIVISOR;
	/**
	 * This is the value for the initial energy that an organism has
	 * if it isn't a child of another organism.
	 */
	static int INITIAL_ENERGY = DEF_INITIAL_ENERGY;
	/**
	 * This is the value for the reaction speed factor.
	 */
	static double REACTION_VEL = DEF_REACTION_VEL;
	/**
	 * This is the value for the spore-1 speed factor.
	 */
	static double SPORE1_VEL = DEF_SPORE1_VEL;
	/**
	 * This is the value for the spore-5 speed factor.
	 */
	static double SPORE5_VEL = DEF_SPORE5_VEL;
	/**
	 * This is the value for the spore-10-12 speed factor.
	 */
	static double SPORE10_VEL = DEF_SPORE10_VEL;
	/**
	 * This is the value for the maximum speed that an organism can
	 * achieve.
	 */
	static double MAX_VEL = DEF_MAX_VEL;
	/**
	 * This is the value for the maximum rotational speed that an organism
	 * can achieve.
	 */
	static double MAX_ROT = DEF_MAX_ROT;
	/**
	 * This is the elasticity coefficient. This value is used to establish
	 * the energy that a movement keeps after a collision.
	 */
	static double ELASTICITY = DEF_ELASTICITY;
	/**
	 * This is the number of milliseconds that pass between frames.
	 */
	static int DELAY = DEF_DELAY;
	/**
	 * This stores the value of the `repaint world` checkbox.
	 */
	private static boolean repaintWorld = DEF_repaintWorld;
	/**
	 * If false then the main window is not in focus, so we don't need to
	 * repaint the world.
	 */
	private static boolean mainWindowInFocus = true;
	/**
	 * If true then the main window or any of the dialog windows are in focus.
	 * If false then none of the app windows are in focus, i.e. the app is in
	 * the background, so we can omit updating the status bar and the statistics
	 * window to make simulation faster.
	 */
	private static boolean appInFocus = true;
	/**
	 * The currently active strategy for repainting the world.
	 */
	private static RepaintWorldStrategy repaintWorldStrategy = RepaintWorldStrategy.ALWAYS;
	/**
	 * List of listeners that needs to be notified when the return value
	 * of the `repaintWorld` method changes.
	 * Can be used for example to force repaint the world when we need to
	 * resume repainting the world.
	 */
	static List<RepaintWorldChangeListener> repaintWorldChangeListeners = new ArrayList<>();
	/**
	 * How many times per second we want to update the status bar, e.g. to
	 * update the O2 level and the population number.
	 */
	static int STATUS_BAR_REFRESH_FPS = DEF_STATUS_BAR_REFRESH_FPS;
	/**
	 * How many times per second we want to update the statistics window, if it's open.
	 */
	static int STATISTICS_REFRESH_FPS = DEF_STATISTICS_REFRESH_FPS;
	/**
	 * This is the value for having or not having automatic backups.
	 */
	static boolean AUTO_BACKUP = DEF_AUTO_BACKUP;
	/**
	 * If true the backups will be compressed using gzip. Otherwise it's uncompressed serialized object.
	 */
	static boolean COMPRESS_BACKUPS = DEF_COMPRESS_BACKUPS;
	/**
	 * This is the value for saving or not saving world stats as a CSV file when saving
	 * automatic backups.
	 */
	static boolean AUTO_BACKUP_CSV = DEF_AUTO_BACKUP_CSV;
	/**
	 * This is the value for saving or not saving the world as a PNG image when saving
	 * automatic backups.
	 */
	static boolean AUTO_BACKUP_WORLD_PNG = DEF_AUTO_BACKUP_WORLD_PNG;
	/**
	 * This is the value for saving or not saving the statistics dialog as a PNG image
	 * when saving automatic backups.
	 * Note that the statistics dialog has to be open to be able to automatically save it as PNG.
	 * If the statistics dialog is not open when the automatic backup is performed, then no image
	 * will be saved.
	 */
	static boolean AUTO_BACKUP_STATISTICS_PNG = DEF_AUTO_BACKUP_STATISTICS_PNG;
	/**
	 * This is the number of game time units that pass between backups.
	 */
	static int BACKUP_DELAY = DEF_BACKUP_DELAY;
	/**
	 * This is the port where the net server will listen for connections.
	 */
	static int LOCAL_PORT = DEF_LOCAL_PORT;
	/**
	 * This is the name your organisms will use in the network.
	 */
	static String USER_NAME = DEF_USER_NAME;
	/**
	 * This is the value for the maximum number of network connections allowed.
	 */
	static int MAX_CONNECTIONS = DEF_MAX_CONNECTIONS;
	/**
	 * This is the value for accepting or not new connections from other hosts.
	 */
	static boolean ACCEPT_CONNECTIONS = DEF_ACCEPT_CONNECTIONS;
	/**
	 * This is the value for using or not a meta-server to find other instances
	 * of biogenesis running. At the moment it is not used.
	 */
	static boolean CONNECT_TO_SERVER = DEF_CONNECT_TO_SERVER;
	/**
	 * This is the IP that the meta-server will have. At the moment it is not used.
	 */
	static String SERVER_ADDRESS = DEF_SERVER_ADDRESS;
	/**
	 * This is the hardware acceleration applied when drawing.
	 * 0 none, 1 try opengl next time, 2 trying opengl, 3 opengl
	 * 4 try opengl without fbobject next time, 5 trying opengl without fbobject,
	 * 6 opengl without fbobject
	 */
	static int HARDWARE_ACCELERATION = DEF_HARDWARE_ACCELERATION;
	/**
	 * This is the port where the meta-server will listen. At the moment it is not used.
	 */
	static int SERVER_PORT = DEF_SERVER_PORT;

	static double DECAY_ENERGY = DEF_DECAY_ENERGY;
	/**
	 * Tolerance. Smaller numbers are considered equal to 0.
	 */
	static final double tol = 0.0000001;
	/**
	 * Indicates the eight possible directions. The row is the direction we want, from 0 to 7, first
	 * column is the x coordinate and second column the y coordinate.
	 */
	static final int side[][] = {{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
	/**
	 * These are the old scale factor applied to segments depending on the growth rate of the
	 * organism. Segment length is divided by scale[i], where i is the growth rate.
	 * scale[0] indicates that the organism is fully developed and scale[15] that it has just
	 * been born.
	static final double oldscale[] = {1.00, 1.12, 1.25, 1.40, 1.57, 1.76, 1.97, 2.21,
        2.47, 2.77, 3.11, 3.48, 3.90, 4.36, 4.89, 5.47};
    */
	/**
	 * These are the scale factor applied to segments depending on the growth rate of the
	 * organism. Segment length is multiplicated by scale[i], where i is the growth rate.
	 * scale[0] indicates that the organism is fully developed and scale[15] that it has just
	 * been born.
	 */
	static final double scale[] = {1.00, 0.90, 0.80, 0.71, 0.63, 0.56, 0.50, 0.45,
		0.40, 0.36, 0.32, 0.28, 0.25, 0.22, 0.20, 0.18};
	/**
	 * These are the scale factor applied to spring segments depending on the growth rate of the
	 * organism. Spring segment length *100 is multiplicated by scale[i], where i is the growth rate.
	 * scale[0] indicates that the organism is fully developed and scale[15] that it has just
	 * been born.
	 */
	static final double springscale[] = {0.01, 0.009, 0.008, 0.0071, 0.0063, 0.0056, 0.005, 0.0045,
			0.004, 0.0036, 0.0032, 0.0028, 0.0025, 0.0022, 0.002, 0.0018};
	/**
	 * Precalculated dark green color
	 */
	static final Color ColorDARK_GREEN = Color.GREEN.darker();
	/**
	 * Precalculated dark red color
	 */
	static final Color ColorDARK_RED = Color.RED.darker();
	/**
	 * Precalculated dark cyan color
	 */
	static final Color ColorDARK_CYAN = Color.CYAN.darker();
	/**
	 * Precalculated dark blue color
	 */
	static final Color ColorDARK_BLUE = Color.BLUE.darker();
	/**
	 * Precalculated dark magenta color
	 */
	static final Color ColorDARK_MAGENTA = Color.MAGENTA.darker();
	/**
	 * Precalculated dark pink color
	 */
	static final Color ColorDARK_PINK = Color.PINK.darker();
	/**
	 * Precalculated dark orange color
	 */
	static final Color ColorDARK_ORANGE = Color.ORANGE.darker();
	/**
	 * Precalculated dark white color
	 */
	static final Color ColorDARK_WHITE = Color.WHITE.darker();
	/**
	 * Precalculated dark gray color
	 */
	static final Color ColorDARK_GRAY = Color.GRAY.darker();
	/**
	 * Precalculated dark yellow color
	 */
	static final Color ColorDARK_YELLOW = Color.YELLOW.darker();
	/**
	 * Precalculated dark white color
	 */
	static final Color ColorDARKWHITE = new Color(227,227,227);
	/**
	 * Precalculated brown color
	 */
	static final Color ColorBROWN = new Color(150,75,0);
	/**
	 * Precalculated light brown color
	 */
	static final Color ColorLIGHTBROWN = new Color(128,112,64);
	/**
	 * Precalculated green brown color
	 */
	static final Color ColorGREENBROWN = new Color(128,132,64);
	/**
	 * Precalculated poisoned jade color
	 */
	static final Color ColorDARKGREEN = new Color(54,84,54);
	/**
	 * Precalculated light blue color
	 */
	static final Color ColorLIGHT_BLUE = new Color(0,0,128);
	/**
	 * Precalculated violet color
	 */
	static final Color ColorVIOLET = new Color(128,0,128);
	/**
	 * Precalculated Forest color
	 */
	static final Color ColorFOREST = new Color(0,128,0);
	/**
	 * Precalculated bark color
	 */
	static final Color ColorBARK = new Color(96,128,64);
	/**
	 * Precalculated old bark color
	 */
	static final Color ColorOLDBARK = new Color(80,40,0);
	/**
	 * Precalculated old bark color
	 */
	static final Color ColorDEADBARK = new Color(128,96,64);
	/**
	 * Precalculated jade color
	 */
	static final Color ColorJADE = new Color(0,168,107);
	/**
	 * Precalculated spring color
	 */
	static final Color ColorSPRING = new Color(0,255,128);
	/**
	 * Precalculated leaf color
	 */
	static final Color ColorLEAF = new Color(92,184,0);
	/**
	 * Precalculated lime color
	 */
	static final Color ColorLIME = new Color(176,255,0);
	/**
	 * Precalculated summer color
	 */
	static final Color ColorSUMMER = new Color(128,255,64);
	/**
	 * Precalculated winter color
	 */
	static final Color ColorWINTER = new Color(54,84,0);
	/**
	 * Precalculated teal color
	 */
	static final Color ColorTEAL = new Color(0,128,128);
	/**
	 * Precalculated eye color
	 */
	static final Color ColorEYE = new Color(0,64,64);
	/**
	 * Precalculated vision color (used for non terminal eyes)
	 */
	static final Color ColorVISION = new Color(232,145,70);
	/**
	 * Precalculated mint color
	 */
	static final Color ColorMINT = new Color(160,224,160);
	/**
	 * Precalculated maroon color
	 */
	static final Color ColorMAROON = new Color(128,0,0);
	/**
	 * Precalculated olive color
	 */
	static final Color ColorOLIVE = new Color(176,176,0);
	/**
	 * Precalculated dark olive color
	 */
	static final Color ColorDARKOLIVE = new Color(88,88,0);
	/**
	 * Precalculated cream color
	 */
	static final Color ColorCREAM = new Color(208,192,140);
	/**
	 * Precalculated rose color
	 */
	static final Color ColorROSE = new Color(255,0,128);
	/**
	 * Precalculated coral color
	 */
	static final Color ColorCORAL = new Color(255,100,138);
	/**
	 * Precalculated ochre color
	 */
	static final Color ColorOCHRE = new Color(204,119,34);
	/**
	 * Precalculated sky color
	 */
	static final Color ColorSKY = new Color(128,192,255);
	/**
	 * Precalculated deep sky color
	 */
	static final Color ColorDEEPSKY = new Color(64,96,255);
	/**
	 * Precalculated ice color
	 */
	static final Color ColorICE = new Color(64,96,128);
	/**
	 * Precalculated lilac color
	 */
	static final Color ColorLILAC = new Color(192,128,255);
	/**
	 * Precalculated dark lilac color
	 */
	static final Color ColorDARKLILAC = new Color(96,64,96);
	/**
	 * Precalculated fire color
	 */
	static final Color ColorFIRE = new Color(255,100,0);
	/**
	 * Precalculated dark fire color
	 */
	static final Color ColorDARKFIRE = new Color(232,146,70);
	/**
	 * Precalculated grass color
	 */
	static final Color ColorGRASS = new Color(144,176,64);
	/**
	 * Precalculated purple color
	 */
	static final Color ColorPURPLE = new Color(168,0,84);
	/**
	 * Precalculated c4 color
	 */
	static final Color ColorC4 = new Color(96,192,96);
	/**
	 * Precalculated dark jade color
	 */
	static final Color ColorDARKJADE = new Color(0,84,54);
	/**
	 * Precalculated gold color
	 */
	static final Color ColorGOLD = new Color(212,175,55);
	/**
	 * Precalculated blond color
	 */
	static final Color ColorBLOND = new Color(255,255,128);
	/**
	 * Precalculated flower color
	 */
	static final Color ColorFLOWER = new Color(128,128,255);
	/**
	 * Precalculated auburn color
	 */
	static final Color ColorAUBURN = new Color(128,48,48);
	/**
	 * Precalculated indigo color
	 */
	static final Color ColorINDIGO = new Color(111,0,255);
	/**
	 * Precalculated lavender color
	 */
	static final Color ColorLAVENDER = new Color(128,96,176);
	/**
	 * Precalculated fallow color
	 */
	static final Color ColorFALLOW = new Color(150,113,23);
	/**
	 * Precalculated spore color
	 */
	static final Color ColorSPORE = new Color(0,80,160);
	/**
	 * Precalculated fruit color
	 */
	static final Color ColorFRUIT = new Color(255,128,192);
	/**
	 * Precalculated plague color
	 */
	static final Color ColorPLAGUE = new Color(255,192,255);
	/**
	 * Precalculated spike color
	 */
	static final Color ColorSPIKE = new Color(164,132,100);
	/**
	 * Precalculated spike point color
	 */
	static final Color ColorSPIKEPOINT = new Color(164,132,99);
	/**
	 * Precalculated broken defense color
	 */
	static final Color ColorBROKEN = new Color(96,128,96);
	/**
	 * Precalculated dark color
	 */
	static final Color ColorDARK = new Color(64,32,16);
	/**
	 * Used through all program to calculate random numbers
	 */
	public static Random random = new Random();
	/**
	 * Used to get a random -1 or 1 to create numbers with random sign.
	 *
	 * @return  a random -1 or 1
	 */
	public static final int randomSign() {
		return (random.nextInt(2)<<1)-1;
	}
	/**
	 * Calculates the minimum of three integers
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @return  The minimum of a, b, and c
	 */
	public static final int min(int a, int b, int c) {
		return Math.min(Math.min(a,b),c);
	}
	/**
	 * Calculates the minimum of three doubles
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @return  The minimum of a, b, and c
	 */
	public static final double min(double a, double b, double c) {
		return Math.min(Math.min(a,b),c);
	}
	/**
	 * Calculates the maximum of three integers
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @return  The maximum of a, b, and c
	 */
	public static final int max(int a, int b, int c) {
		return Math.max(Math.max(a,b),c);
	}
	/**
	 * Calculates the maximum of three doubles
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @return  The maximum of a, b, and c
	 */
	public static final double max(double a, double b, double c) {
		return Math.max(Math.max(a,b),c);
	}
	/**
	 * Return min if value<min, max if value>max and value otherwise.
	 *
	 * @param value
	 * @param min
	 * @param max
	 * @return  min if value<min, max if value>max and value otherwise
	 */
	public static final int between(int value, int min, int max) {
		return Math.max(Math.min(max, value), min);
	}
	/**
	 * Return min if value<min, max if value>max and value otherwise.
	 *
	 * @param value
	 * @param min
	 * @param max
	 * @return  min if value<min, max if value>max and value otherwise
	 */
	public static final double between(double value, double min, double max) {
		return Math.max(Math.min(max, value), min);
	}
	/**
	 * Check if the meta mutation rate changed or not, using a random number.
	 *
	 * @return  true if the meta mutation rate changed and false otherwise
	 */
	public static final boolean randomMutation() {
		if (random.nextInt(10000) < META_MUTATION_RATE)
			return true;
		return false;
	}
	/**
	 * Return the localized name of a color.
	 *
	 * @param c  A color
	 * @return  A String representing the name of the color
	 */
	public static final String colorToString(Color c) {
		if (c.equals(Color.GREEN)) return Messages.getString("T_GREEN"); //$NON-NLS-1$
		if (c.equals(Utils.ColorFOREST)) return Messages.getString("T_FOREST"); //$NON-NLS-1$
		if (c.equals(Utils.ColorSPRING)) return Messages.getString("T_SPRING"); //$NON-NLS-1$
		if (c.equals(Utils.ColorSUMMER)) return Messages.getString("T_SUMMER"); //$NON-NLS-1$
		if (c.equals(Utils.ColorLIME)) return Messages.getString("T_LIME"); //$NON-NLS-1$
		if (c.equals(Utils.ColorLEAF)) return Messages.getString("T_LEAF"); //$NON-NLS-1$
		if (c.equals(Utils.ColorC4)) return Messages.getString("T_C4"); //$NON-NLS-1$
		if (c.equals(Utils.ColorJADE)) return Messages.getString("T_JADE"); //$NON-NLS-1$
		if (c.equals(Utils.ColorGRASS)) return Messages.getString("T_GRASS"); //$NON-NLS-1$
		if (c.equals(Utils.ColorBARK)) return Messages.getString("T_BARK"); //$NON-NLS-1$
		if (c.equals(Utils.ColorPURPLE)) return Messages.getString("T_PURPLE"); //$NON-NLS-1$
		if (c.equals(Color.RED)) return Messages.getString("T_RED"); //$NON-NLS-1$
		if (c.equals(Utils.ColorFIRE)) return Messages.getString("T_FIRE"); //$NON-NLS-1$
		if (c.equals(Color.ORANGE)) return Messages.getString("T_ORANGE"); //$NON-NLS-1$
		if (c.equals(Utils.ColorMAROON)) return Messages.getString("T_MAROON"); //$NON-NLS-1$
		if (c.equals(Color.PINK)) return Messages.getString("T_PINK"); //$NON-NLS-1$
		if (c.equals(Utils.ColorCREAM)) return Messages.getString("T_CREAM"); //$NON-NLS-1$
		if (c.equals(Color.LIGHT_GRAY)) return Messages.getString("T_SILVER"); //$NON-NLS-1$
		if (c.equals(Utils.ColorSPIKE)) return Messages.getString("T_SPIKE"); //$NON-NLS-1$
		if (c.equals(Utils.ColorLILAC)) return Messages.getString("T_LILAC"); //$NON-NLS-1$
		if (c.equals(Color.GRAY)) return Messages.getString("T_GRAY"); //$NON-NLS-1$
		if (c.equals(Utils.ColorVIOLET)) return Messages.getString("T_VIOLET"); //$NON-NLS-1$
		if (c.equals(Utils.ColorOLIVE)) return Messages.getString("T_OLIVE"); //$NON-NLS-1$
		if (c.equals(Utils.ColorSKY)) return Messages.getString("T_SKY"); //$NON-NLS-1$
		if (c.equals(Color.BLUE)) return Messages.getString("T_BLUE"); //$NON-NLS-1$
		if (c.equals(Utils.ColorOCHRE)) return Messages.getString("T_OCHRE"); //$NON-NLS-1$
		if (c.equals(Utils.ColorFALLOW)) return Messages.getString("T_FALLOW"); //$NON-NLS-1$
		if (c.equals(Utils.ColorSPORE)) return Messages.getString("T_SPORE"); //$NON-NLS-1$
		if (c.equals(Color.WHITE)) return Messages.getString("T_WHITE"); //$NON-NLS-1$
		if (c.equals(Utils.ColorPLAGUE)) return Messages.getString("T_PLAGUE"); //$NON-NLS-1$
		if (c.equals(Utils.ColorCORAL)) return Messages.getString("T_CORAL"); //$NON-NLS-1$
		if (c.equals(Utils.ColorMINT)) return Messages.getString("T_MINT"); //$NON-NLS-1$
		if (c.equals(Utils.ColorLAVENDER)) return Messages.getString("T_LAVENDER"); //$NON-NLS-1$
		if (c.equals(Color.MAGENTA)) return Messages.getString("T_MAGENTA"); //$NON-NLS-1$
		if (c.equals(Utils.ColorROSE)) return Messages.getString("T_ROSE"); //$NON-NLS-1$
		if (c.equals(Color.CYAN)) return Messages.getString("T_CYAN"); //$NON-NLS-1$
		if (c.equals(Utils.ColorTEAL)) return Messages.getString("T_TEAL"); //$NON-NLS-1$
		if (c.equals(Color.YELLOW)) return Messages.getString("T_YELLOW"); //$NON-NLS-1$
		if (c.equals(Utils.ColorAUBURN)) return Messages.getString("T_AUBURN"); //$NON-NLS-1$
		if (c.equals(Utils.ColorINDIGO)) return Messages.getString("T_INDIGO"); //$NON-NLS-1$
		if (c.equals(Utils.ColorBLOND)) return Messages.getString("T_BLOND"); //$NON-NLS-1$
		if (c.equals(Utils.ColorFLOWER)) return Messages.getString("T_FLOWER"); //$NON-NLS-1$
		if (c.equals(Color.DARK_GRAY)) return Messages.getString("T_DARKGRAY"); //$NON-NLS-1$
		if (c.equals(Utils.ColorGOLD)) return Messages.getString("T_GOLD"); //$NON-NLS-1$
		if (c.equals(Utils.ColorDARK)) return Messages.getString("T_DARK"); //$NON-NLS-1$
		if (c.equals(Utils.ColorEYE)) return Messages.getString("T_EYE"); //$NON-NLS-1$
		return ""; //$NON-NLS-1$
	}
	/**
	 * Save user preferences to disk
	 */
	public static final void savePreferences() {
		try {
			Preferences prefs = Preferences.userNodeForPackage(Utils.class);
			prefs.putInt("VERSION",FILE_VERSION); //$NON-NLS-1$
			prefs.putInt("INITIAL_ORGANISMS",INITIAL_ORGANISMS); //$NON-NLS-1$
			prefs.putInt("INITIAL_COMPLEXITY",INITIAL_COMPLEXITY); //$NON-NLS-1$
			prefs.putInt("CLADE_COMPLEXITY",CLADE_COMPLEXITY); //$NON-NLS-1$
			prefs.putDouble("INITIAL_O2",INITIAL_O2); //$NON-NLS-1$
			prefs.putDouble("INITIAL_CO2",INITIAL_CO2); //$NON-NLS-1$
			prefs.putDouble("INITIAL_CH4",INITIAL_CH4); //$NON-NLS-1$
			prefs.putInt("ORGANISMS_VECTOR_SIZE",ORGANISMS_VECTOR_SIZE); //$NON-NLS-1$
			prefs.putInt("WORLD_WIDTH",WORLD_WIDTH); //$NON-NLS-1$
			prefs.putInt("WORLD_HEIGHT",WORLD_HEIGHT); //$NON-NLS-1$
			prefs.putInt("MAX_AGE",MAX_AGE); //$NON-NLS-1$
			prefs.putInt("AGE_DIVISOR",AGE_DIVISOR); //$NON-NLS-1$
			prefs.putInt("CO2_TO_CH4_DIVISOR",CO2_TO_CH4_DIVISOR); //$NON-NLS-1$
			prefs.putInt("CH4_TO_CO2_DIVISOR",CH4_TO_CO2_DIVISOR); //$NON-NLS-1$
			prefs.putDouble("RUBBING",RUBBING); //$NON-NLS-1$
			prefs.putInt("META_MUTATION_RATE",META_MUTATION_RATE); //$NON-NLS-1$
			prefs.putInt("MAX_MUTATION_RATE",MAX_MUTATION_RATE); //$NON-NLS-1$
			prefs.putInt("MIN_MUTATION_RATE",MIN_MUTATION_RATE); //$NON-NLS-1$
			prefs.putInt("MAX_CLONE_RATE",MAX_CLONE_RATE); //$NON-NLS-1$
			prefs.putInt("MIN_CLONE_RATE",MIN_CLONE_RATE); //$NON-NLS-1$
			prefs.putInt("SEGMENT_COST_DIVISOR",SEGMENT_COST_DIVISOR); //$NON-NLS-1$
			prefs.putDouble("ORGANIC_OBTAINED_ENERGY",ORGANIC_OBTAINED_ENERGY); //$NON-NLS-1$
			prefs.putInt("GREEN_OBTAINED_ENERGY_DIVISOR",GREEN_OBTAINED_ENERGY_DIVISOR); //$NON-NLS-1$
			prefs.putDouble("ORGANIC_SUBS_PRODUCED",ORGANIC_SUBS_PRODUCED); //$NON-NLS-1$
			prefs.putDouble("CREAM_ORGANIC_SUBS_PRODUCED",CREAM_ORGANIC_SUBS_PRODUCED); //$NON-NLS-1$
			prefs.putInt("HEALING",HEALING); //$NON-NLS-1$
			prefs.putInt("IMMUNE_SYSTEM",IMMUNE_SYSTEM); //$NON-NLS-1$
			prefs.putInt("LAVENDER_SHIELD",LAVENDER_SHIELD); //$NON-NLS-1$
			prefs.putDouble("INDIGO_DIVISOR",INDIGO_DIVISOR); //$NON-NLS-1$
			prefs.putDouble("GOLD_DIVISOR",GOLD_DIVISOR); //$NON-NLS-1$
			prefs.putDouble("DODGE_ENERGY_CONSUMPTION",DODGE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putInt("DARKJADE_DELAY",DARKJADE_DELAY); //$NON-NLS-1$
			prefs.putDouble("RED_ENERGY_CONSUMPTION",RED_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("GREEN_ENERGY_CONSUMPTION",GREEN_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("BLUE_ENERGY_CONSUMPTION",BLUE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("CYAN_ENERGY_CONSUMPTION",CYAN_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("WHITE_ENERGY_CONSUMPTION",WHITE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("VIRUS_ENERGY_CONSUMPTION",VIRUS_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("GRAY_ENERGY_CONSUMPTION",GRAY_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("DARKGRAY_ENERGY_CONSUMPTION",DARKGRAY_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("SILVER_ENERGY_CONSUMPTION",SILVER_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("YELLOW_ENERGY_CONSUMPTION",YELLOW_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("MAGENTA_ENERGY_CONSUMPTION",MAGENTA_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("PINK_ENERGY_CONSUMPTION",PINK_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("CORAL_ENERGY_CONSUMPTION",CORAL_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("ORANGE_ENERGY_CONSUMPTION",ORANGE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("FOREST_ENERGY_CONSUMPTION",FOREST_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("CROWDEDFOREST_ENERGY_CONSUMPTION",CROWDEDFOREST_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("SYMBIONT_ENERGY_CONSUMPTION",SYMBIONT_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("SPRING_ENERGY_CONSUMPTION",SPRING_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("LEAF_ENERGY_CONSUMPTION",LEAF_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("MODLEAF_ENERGY_CONSUMPTION",MODLEAF_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("LIME_ENERGY_CONSUMPTION",LIME_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("CROWDEDLIME_ENERGY_CONSUMPTION",CROWDEDLIME_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("SUMMER_ENERGY_CONSUMPTION",SUMMER_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("MOSQUITO_ENERGY_CONSUMPTION",MOSQUITO_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("BARK_ENERGY_CONSUMPTION",BARK_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("JADE_ENERGY_CONSUMPTION",JADE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("GRASS_ENERGY_CONSUMPTION",GRASS_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("PURPLE_ENERGY_CONSUMPTION",PURPLE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("C4_ENERGY_CONSUMPTION",C4_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("VIOLET_ENERGY_CONSUMPTION",VIOLET_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("TEAL_ENERGY_CONSUMPTION",TEAL_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("EYE_ENERGY_CONSUMPTION",EYE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("MAROON_ENERGY_CONSUMPTION",MAROON_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("OLIVE_ENERGY_CONSUMPTION",OLIVE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("MINT_ENERGY_CONSUMPTION",MINT_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("CREAM_ENERGY_CONSUMPTION",CREAM_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("ROSE_ENERGY_CONSUMPTION",ROSE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("DARK_ENERGY_CONSUMPTION",DARK_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("OCHRE_ENERGY_CONSUMPTION",OCHRE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("SKY_ENERGY_CONSUMPTION",SKY_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("LILAC_ENERGY_CONSUMPTION",LILAC_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("FIRE_ENERGY_CONSUMPTION",FIRE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("EXPERIENCE_ENERGY_CONSUMPTION",EXPERIENCE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("BLOND_ENERGY_CONSUMPTION",BLOND_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("FLOWER_ENERGY_CONSUMPTION",FLOWER_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("AUBURN_ENERGY_CONSUMPTION",AUBURN_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("PLAGUE_ENERGY_CONSUMPTION",PLAGUE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("SCOURGE_ENERGY_CONSUMPTION",SCOURGE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("SPIKE_ENERGY_CONSUMPTION",SPIKE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("INDIGO_ENERGY_CONSUMPTION",INDIGO_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("LAVENDER_ENERGY_CONSUMPTION",LAVENDER_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("FALLOW_ENERGY_CONSUMPTION",FALLOW_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("SPORE_ENERGY_CONSUMPTION",SPORE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putInt("RED_PROB",RED_PROB); //$NON-NLS-1$
			prefs.putInt("GREEN_PROB",GREEN_PROB); //$NON-NLS-1$
			prefs.putInt("BLUE_PROB",BLUE_PROB); //$NON-NLS-1$
			prefs.putInt("CYAN_PROB",CYAN_PROB); //$NON-NLS-1$
			prefs.putInt("WHITE_PROB",WHITE_PROB); //$NON-NLS-1$
			prefs.putInt("GRAY_PROB",GRAY_PROB); //$NON-NLS-1$
			prefs.putInt("DARKGRAY_PROB",DARKGRAY_PROB); //$NON-NLS-1$
			prefs.putInt("SILVER_PROB",SILVER_PROB); //$NON-NLS-1$
			prefs.putInt("YELLOW_PROB",YELLOW_PROB); //$NON-NLS-1$
			prefs.putInt("MAGENTA_PROB",MAGENTA_PROB); //$NON-NLS-1$
			prefs.putInt("PINK_PROB",PINK_PROB); //$NON-NLS-1$
			prefs.putInt("CORAL_PROB",CORAL_PROB); //$NON-NLS-1$
			prefs.putInt("ORANGE_PROB",ORANGE_PROB); //$NON-NLS-1$
			prefs.putInt("FOREST_PROB",FOREST_PROB); //$NON-NLS-1$
			prefs.putInt("SPRING_PROB",SPRING_PROB); //$NON-NLS-1$
			prefs.putInt("LEAF_PROB",LEAF_PROB); //$NON-NLS-1$
			prefs.putInt("LIME_PROB",LIME_PROB); //$NON-NLS-1$
			prefs.putInt("SUMMER_PROB",SUMMER_PROB); //$NON-NLS-1$
			prefs.putInt("BARK_PROB",BARK_PROB); //$NON-NLS-1$
			prefs.putInt("JADE_PROB",JADE_PROB); //$NON-NLS-1$
			prefs.putInt("GRASS_PROB",GRASS_PROB); //$NON-NLS-1$
			prefs.putInt("PURPLE_PROB",PURPLE_PROB); //$NON-NLS-1$
			prefs.putInt("C4_PROB",C4_PROB); //$NON-NLS-1$
			prefs.putInt("VIOLET_PROB",VIOLET_PROB); //$NON-NLS-1$
			prefs.putInt("TEAL_PROB",TEAL_PROB); //$NON-NLS-1$
			prefs.putInt("EYE_PROB",EYE_PROB); //$NON-NLS-1$
			prefs.putInt("MAROON_PROB",MAROON_PROB); //$NON-NLS-1$
			prefs.putInt("OLIVE_PROB",OLIVE_PROB); //$NON-NLS-1$
			prefs.putInt("MINT_PROB",MINT_PROB); //$NON-NLS-1$
			prefs.putInt("CREAM_PROB",CREAM_PROB); //$NON-NLS-1$
			prefs.putInt("ROSE_PROB",ROSE_PROB); //$NON-NLS-1$
			prefs.putInt("DARK_PROB",DARK_PROB); //$NON-NLS-1$
			prefs.putInt("OCHRE_PROB",OCHRE_PROB); //$NON-NLS-1$
			prefs.putInt("SKY_PROB",SKY_PROB); //$NON-NLS-1$
			prefs.putInt("LILAC_PROB",LILAC_PROB); //$NON-NLS-1$
			prefs.putInt("FIRE_PROB",FIRE_PROB); //$NON-NLS-1$
			prefs.putInt("GOLD_PROB",GOLD_PROB); //$NON-NLS-1$
			prefs.putInt("BLOND_PROB",BLOND_PROB); //$NON-NLS-1$
			prefs.putInt("FLOWER_PROB",FLOWER_PROB); //$NON-NLS-1$
			prefs.putInt("AUBURN_PROB",AUBURN_PROB); //$NON-NLS-1$
			prefs.putInt("PLAGUE_PROB",PLAGUE_PROB); //$NON-NLS-1$
			prefs.putInt("SPIKE_PROB",SPIKE_PROB); //$NON-NLS-1$
			prefs.putInt("INDIGO_PROB",INDIGO_PROB); //$NON-NLS-1$
			prefs.putInt("LAVENDER_PROB",LAVENDER_PROB); //$NON-NLS-1$
			prefs.putInt("FALLOW_PROB",FALLOW_PROB); //$NON-NLS-1$
			prefs.putInt("SPORE_PROB",SPORE_PROB); //$NON-NLS-1$
			prefs.putInt("DRAIN_SUBS_DIVISOR",DRAIN_SUBS_DIVISOR); //$NON-NLS-1$
			prefs.putInt("INITIAL_ENERGY",INITIAL_ENERGY); //$NON-NLS-1$
			prefs.putDouble("REACTION_VEL",REACTION_VEL); //$NON-NLS-1$
			prefs.putDouble("SPORE1_VEL",SPORE1_VEL); //$NON-NLS-1$
			prefs.putDouble("SPORE5_VEL",SPORE5_VEL); //$NON-NLS-1$
			prefs.putDouble("SPORE10_VEL",SPORE10_VEL); //$NON-NLS-1$
			prefs.putDouble("MAX_VEL",MAX_VEL); //$NON-NLS-1$
			prefs.putDouble("MAX_ROT",MAX_ROT); //$NON-NLS-1$
			prefs.putDouble("ELASTICITY",ELASTICITY); //$NON-NLS-1$
			prefs.putInt("DELAY",DELAY); //$NON-NLS-1$
			prefs.putBoolean("repaintWorld",repaintWorld); //$NON-NLS-1$
			prefs.put("repaintWorldStrategy",repaintWorldStrategy.toString()); //$NON-NLS-1$
			prefs.putInt("STATUS_BAR_REFRESH_FPS",STATUS_BAR_REFRESH_FPS); //$NON-NLS-1$
			prefs.putInt("STATISTICS_REFRESH_FPS",STATISTICS_REFRESH_FPS); //$NON-NLS-1$
			prefs.putBoolean("AUTO_BACKUP",AUTO_BACKUP);
			prefs.putBoolean("COMPRESS_BACKUPS",COMPRESS_BACKUPS);
			prefs.putBoolean("AUTO_BACKUP_CSV",AUTO_BACKUP_CSV);
			prefs.putBoolean("AUTO_BACKUP_WORLD_PNG",AUTO_BACKUP_WORLD_PNG);
			prefs.putBoolean("AUTO_BACKUP_STATISTICS_PNG",AUTO_BACKUP_STATISTICS_PNG);
			prefs.putInt("BACKUP_DELAY",BACKUP_DELAY);
			prefs.putInt("LOCAL_PORT",LOCAL_PORT); //$NON-NLS-1$
			prefs.put("USER_NAME", USER_NAME); //$NON-NLS-1$
			prefs.putBoolean("ACCEPT_CONNECTIONS",ACCEPT_CONNECTIONS); //$NON-NLS-1$
			prefs.putBoolean("CONNECT_TO_SERVER",CONNECT_TO_SERVER); //$NON-NLS-1$
			prefs.put("SERVER_ADDRESS",SERVER_ADDRESS); //$NON-NLS-1$
			prefs.putInt("SERVER_PORT",SERVER_PORT); //$NON-NLS-1$
			prefs.putInt("MAX_CONNECTIONS",MAX_CONNECTIONS); //$NON-NLS-1$
			prefs.putInt("HARDWARE_ACCELERATION", HARDWARE_ACCELERATION); //$NON-NLS-1$
			prefs.putDouble("DECAY_ENERGY", DECAY_ENERGY); //$NON-NLS-1$
			prefs.put("LOCALE",Messages.getLanguage()); //$NON-NLS-1$
		}
		catch (SecurityException ex) {
			// If we can't write, don't do it
		}
	}
	/**
	 * Read user preferences from disc
	 */
	public static final void readPreferences() {
		try {
			Preferences prefs = Preferences.userNodeForPackage(Utils.class);
			int previous_version = prefs.getInt("VERSION",0); //$NON-NLS-1$
			if (previous_version != FILE_VERSION)
				try {
					prefs.clear();
				} catch (BackingStoreException e) {
					//do nothing
				}
			WINDOW_X = prefs.getInt("WINDOW_X", DEF_WINDOW_X);
			WINDOW_Y = prefs.getInt("WINDOW_Y", DEF_WINDOW_Y);
			WINDOW_WIDTH = prefs.getInt("WINDOW_WIDTH", DEF_WINDOW_WIDTH);
			WINDOW_HEIGHT = prefs.getInt("WINDOW_HEIGHT", DEF_WINDOW_HEIGHT);
			WINDOW_STATE = prefs.getInt("WINDOW_STATE", DEF_WINDOW_STATE);
			INITIAL_ORGANISMS = prefs.getInt("INITIAL_ORGANISMS",DEF_INITIAL_ORGANISMS); //$NON-NLS-1$
			INITIAL_COMPLEXITY = prefs.getInt("INITIAL_COMPLEXITY",DEF_INITIAL_COMPLEXITY); //$NON-NLS-1$
			CLADE_COMPLEXITY = prefs.getInt("CLADE_COMPLEXITY",DEF_CLADE_COMPLEXITY); //$NON-NLS-1$
			INITIAL_O2 = prefs.getDouble("INITIAL_O2",DEF_INITIAL_O2); //$NON-NLS-1$
			INITIAL_CO2 = prefs.getDouble("INITIAL_CO2",DEF_INITIAL_CO2); //$NON-NLS-1$
			INITIAL_CH4 = prefs.getDouble("INITIAL_CH4",DEF_INITIAL_CH4); //$NON-NLS-1$
			ORGANISMS_VECTOR_SIZE = prefs.getInt("ORGANISMS_VECTOR_SIZE",DEF_ORGANISMS_VECTOR_SIZE); //$NON-NLS-1$
			WORLD_WIDTH = prefs.getInt("WORLD_WIDTH",DEF_WORLD_WIDTH); //$NON-NLS-1$
			WORLD_HEIGHT = prefs.getInt("WORLD_HEIGHT",DEF_WORLD_HEIGHT); //$NON-NLS-1$
			MAX_AGE = prefs.getInt("MAX_AGE",DEF_MAX_AGE); //$NON-NLS-1$
			AGE_DIVISOR = prefs.getInt("AGE_DIVISOR",DEF_AGE_DIVISOR); //$NON-NLS-1$
			CO2_TO_CH4_DIVISOR = prefs.getInt("CO2_TO_CH4_DIVISOR",DEF_CO2_TO_CH4_DIVISOR); //$NON-NLS-1$
			CH4_TO_CO2_DIVISOR = prefs.getInt("CH4_TO_CO2_DIVISOR",DEF_CH4_TO_CO2_DIVISOR); //$NON-NLS-1$
			RUBBING = prefs.getDouble("RUBBING",DEF_RUBBING); //$NON-NLS-1$
			META_MUTATION_RATE = prefs.getInt("META_MUTATION_RATE",DEF_META_MUTATION_RATE); //$NON-NLS-1$
			MAX_MUTATION_RATE = prefs.getInt("MAX_MUTATION_RATE",DEF_MAX_MUTATION_RATE); //$NON-NLS-1$
			MIN_MUTATION_RATE = prefs.getInt("MIN_MUTATION_RATE",DEF_MIN_MUTATION_RATE); //$NON-NLS-1$
			MAX_CLONE_RATE = prefs.getInt("MAX_CLONE_RATE",DEF_MAX_CLONE_RATE); //$NON-NLS-1$
			MIN_CLONE_RATE = prefs.getInt("MIN_CLONE_RATE",DEF_MIN_CLONE_RATE); //$NON-NLS-1$
			SEGMENT_COST_DIVISOR = prefs.getInt("SEGMENT_COST_DIVISOR",DEF_SEGMENT_COST_DIVISOR); //$NON-NLS-1$
			ORGANIC_OBTAINED_ENERGY = prefs.getDouble("ORGANIC_OBTAINED_ENERGY",DEF_ORGANIC_OBTAINED_ENERGY); //$NON-NLS-1$
			GREEN_OBTAINED_ENERGY_DIVISOR = prefs.getInt("GREEN_OBTAINED_ENERGY_DIVISOR",DEF_GREEN_OBTAINED_ENERGY_DIVISOR); //$NON-NLS-1$
			ORGANIC_SUBS_PRODUCED = prefs.getDouble("ORGANIC_SUBS_PRODUCED",DEF_ORGANIC_SUBS_PRODUCED); //$NON-NLS-1$
			CREAM_ORGANIC_SUBS_PRODUCED = prefs.getDouble("CREAM_ORGANIC_SUBS_PRODUCED",DEF_CREAM_ORGANIC_SUBS_PRODUCED); //$NON-NLS-1$
			HEALING = prefs.getInt("HEALING",DEF_HEALING); //$NON-NLS-1$
			IMMUNE_SYSTEM = prefs.getInt("IMMUNE_SYSTEM",DEF_IMMUNE_SYSTEM); //$NON-NLS-1$
			LAVENDER_SHIELD = prefs.getInt("LAVENDER_SHIELD",DEF_LAVENDER_SHIELD); //$NON-NLS-1$
			INDIGO_DIVISOR = prefs.getDouble("INDIGO_DIVISOR",DEF_INDIGO_DIVISOR); //$NON-NLS-1$
			GOLD_DIVISOR = prefs.getDouble("GOLD_DIVISOR",DEF_GOLD_DIVISOR); //$NON-NLS-1$
			DODGE_ENERGY_CONSUMPTION = prefs.getDouble("DODGE_ENERGY_CONSUMPTION",DEF_DODGE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			DARKJADE_DELAY = prefs.getInt("DARKJADE_DELAY",DEF_DARKJADE_DELAY); //$NON-NLS-1$
			RED_ENERGY_CONSUMPTION = prefs.getDouble("RED_ENERGY_CONSUMPTION",DEF_RED_ENERGY_CONSUMPTION); //$NON-NLS-1$
			GREEN_ENERGY_CONSUMPTION = prefs.getDouble("GREEN_ENERGY_CONSUMPTION",DEF_GREEN_ENERGY_CONSUMPTION); //$NON-NLS-1$
			BLUE_ENERGY_CONSUMPTION = prefs.getDouble("BLUE_ENERGY_CONSUMPTION",DEF_BLUE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			CYAN_ENERGY_CONSUMPTION = prefs.getDouble("CYAN_ENERGY_CONSUMPTION",DEF_CYAN_ENERGY_CONSUMPTION); //$NON-NLS-1$
			WHITE_ENERGY_CONSUMPTION = prefs.getDouble("WHITE_ENERGY_CONSUMPTION",DEF_WHITE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			VIRUS_ENERGY_CONSUMPTION = prefs.getDouble("VIRUS_ENERGY_CONSUMPTION",DEF_VIRUS_ENERGY_CONSUMPTION); //$NON-NLS-1$
			GRAY_ENERGY_CONSUMPTION = prefs.getDouble("GRAY_ENERGY_CONSUMPTION",DEF_GRAY_ENERGY_CONSUMPTION); //$NON-NLS-1$
			DARKGRAY_ENERGY_CONSUMPTION = prefs.getDouble("DARKGRAY_ENERGY_CONSUMPTION",DEF_DARKGRAY_ENERGY_CONSUMPTION); //$NON-NLS-1$
			SILVER_ENERGY_CONSUMPTION = prefs.getDouble("SILVER_ENERGY_CONSUMPTION",DEF_SILVER_ENERGY_CONSUMPTION); //$NON-NLS-1$
			YELLOW_ENERGY_CONSUMPTION = prefs.getDouble("YELLOW_ENERGY_CONSUMPTION",DEF_YELLOW_ENERGY_CONSUMPTION); //$NON-NLS-1$
			MAGENTA_ENERGY_CONSUMPTION = prefs.getDouble("MAGENTA_ENERGY_CONSUMPTION",DEF_MAGENTA_ENERGY_CONSUMPTION); //$NON-NLS-1$
			PINK_ENERGY_CONSUMPTION = prefs.getDouble("PINK_ENERGY_CONSUMPTION",DEF_PINK_ENERGY_CONSUMPTION); //$NON-NLS-1$
			CORAL_ENERGY_CONSUMPTION = prefs.getDouble("CORAL_ENERGY_CONSUMPTION",DEF_CORAL_ENERGY_CONSUMPTION); //$NON-NLS-1$
			ORANGE_ENERGY_CONSUMPTION = prefs.getDouble("ORANGE_ENERGY_CONSUMPTION",DEF_ORANGE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			FOREST_ENERGY_CONSUMPTION = prefs.getDouble("FOREST_ENERGY_CONSUMPTION",DEF_FOREST_ENERGY_CONSUMPTION); //$NON-NLS-1$
			CROWDEDFOREST_ENERGY_CONSUMPTION = prefs.getDouble("CROWDEDFOREST_ENERGY_CONSUMPTION",DEF_CROWDEDFOREST_ENERGY_CONSUMPTION); //$NON-NLS-1$
			SYMBIONT_ENERGY_CONSUMPTION = prefs.getDouble("SYMBIONT_ENERGY_CONSUMPTION",DEF_SYMBIONT_ENERGY_CONSUMPTION); //$NON-NLS-1$
			SPRING_ENERGY_CONSUMPTION = prefs.getDouble("SPRING_ENERGY_CONSUMPTION",DEF_SPRING_ENERGY_CONSUMPTION); //$NON-NLS-1$
			LEAF_ENERGY_CONSUMPTION = prefs.getDouble("LEAF_ENERGY_CONSUMPTION",DEF_LEAF_ENERGY_CONSUMPTION); //$NON-NLS-1$
			MODLEAF_ENERGY_CONSUMPTION = prefs.getDouble("MODLEAF_ENERGY_CONSUMPTION",DEF_MODLEAF_ENERGY_CONSUMPTION); //$NON-NLS-1$
			LIME_ENERGY_CONSUMPTION = prefs.getDouble("LIME_ENERGY_CONSUMPTION",DEF_LIME_ENERGY_CONSUMPTION); //$NON-NLS-1$
			CROWDEDLIME_ENERGY_CONSUMPTION = prefs.getDouble("CROWDEDLIME_ENERGY_CONSUMPTION",DEF_CROWDEDLIME_ENERGY_CONSUMPTION); //$NON-NLS-1$
			SUMMER_ENERGY_CONSUMPTION = prefs.getDouble("SUMMER_ENERGY_CONSUMPTION",DEF_SUMMER_ENERGY_CONSUMPTION); //$NON-NLS-1$
			MOSQUITO_ENERGY_CONSUMPTION = prefs.getDouble("MOSQUITO_ENERGY_CONSUMPTION",DEF_MOSQUITO_ENERGY_CONSUMPTION); //$NON-NLS-1$
			BARK_ENERGY_CONSUMPTION = prefs.getDouble("BARK_ENERGY_CONSUMPTION",DEF_BARK_ENERGY_CONSUMPTION); //$NON-NLS-1$
			JADE_ENERGY_CONSUMPTION = prefs.getDouble("JADE_ENERGY_CONSUMPTION",DEF_JADE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			GRASS_ENERGY_CONSUMPTION = prefs.getDouble("GRASS_ENERGY_CONSUMPTION",DEF_GRASS_ENERGY_CONSUMPTION); //$NON-NLS-1$
			PURPLE_ENERGY_CONSUMPTION = prefs.getDouble("PURPLE_ENERGY_CONSUMPTION",DEF_PURPLE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			C4_ENERGY_CONSUMPTION = prefs.getDouble("C4_ENERGY_CONSUMPTION",DEF_C4_ENERGY_CONSUMPTION); //$NON-NLS-1$
			VIOLET_ENERGY_CONSUMPTION = prefs.getDouble("VIOLET_ENERGY_CONSUMPTION",DEF_VIOLET_ENERGY_CONSUMPTION); //$NON-NLS-1$
			TEAL_ENERGY_CONSUMPTION = prefs.getDouble("TEAL_ENERGY_CONSUMPTION",DEF_TEAL_ENERGY_CONSUMPTION); //$NON-NLS-1$
			EYE_ENERGY_CONSUMPTION = prefs.getDouble("EYE_ENERGY_CONSUMPTION",DEF_EYE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			MAROON_ENERGY_CONSUMPTION = prefs.getDouble("MAROON_ENERGY_CONSUMPTION",DEF_MAROON_ENERGY_CONSUMPTION); //$NON-NLS-1$
			OLIVE_ENERGY_CONSUMPTION = prefs.getDouble("OLIVE_ENERGY_CONSUMPTION",DEF_OLIVE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			MINT_ENERGY_CONSUMPTION = prefs.getDouble("MINT_ENERGY_CONSUMPTION",DEF_MINT_ENERGY_CONSUMPTION); //$NON-NLS-1$
			CREAM_ENERGY_CONSUMPTION = prefs.getDouble("CREAM_ENERGY_CONSUMPTION",DEF_CREAM_ENERGY_CONSUMPTION); //$NON-NLS-1$
			ROSE_ENERGY_CONSUMPTION = prefs.getDouble("ROSE_ENERGY_CONSUMPTION",DEF_ROSE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			DARK_ENERGY_CONSUMPTION = prefs.getDouble("DARK_ENERGY_CONSUMPTION",DEF_DARK_ENERGY_CONSUMPTION); //$NON-NLS-1$
			OCHRE_ENERGY_CONSUMPTION = prefs.getDouble("OCHRE_ENERGY_CONSUMPTION",DEF_OCHRE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			SKY_ENERGY_CONSUMPTION = prefs.getDouble("SKY_ENERGY_CONSUMPTION",DEF_SKY_ENERGY_CONSUMPTION); //$NON-NLS-1$
			LILAC_ENERGY_CONSUMPTION = prefs.getDouble("LILAC_ENERGY_CONSUMPTION",DEF_LILAC_ENERGY_CONSUMPTION); //$NON-NLS-1$
			FIRE_ENERGY_CONSUMPTION = prefs.getDouble("FIRE_ENERGY_CONSUMPTION",DEF_FIRE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			EXPERIENCE_ENERGY_CONSUMPTION = prefs.getDouble("EXPERIENCE_ENERGY_CONSUMPTION",DEF_EXPERIENCE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			BLOND_ENERGY_CONSUMPTION = prefs.getDouble("BLOND_ENERGY_CONSUMPTION",DEF_BLOND_ENERGY_CONSUMPTION); //$NON-NLS-1$
			FLOWER_ENERGY_CONSUMPTION = prefs.getDouble("FLOWER_ENERGY_CONSUMPTION",DEF_FLOWER_ENERGY_CONSUMPTION); //$NON-NLS-1$
			AUBURN_ENERGY_CONSUMPTION = prefs.getDouble("AUBURN_ENERGY_CONSUMPTION",DEF_AUBURN_ENERGY_CONSUMPTION); //$NON-NLS-1$
			PLAGUE_ENERGY_CONSUMPTION = prefs.getDouble("PLAGUE_ENERGY_CONSUMPTION",DEF_PLAGUE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			SCOURGE_ENERGY_CONSUMPTION = prefs.getDouble("SCOURGE_ENERGY_CONSUMPTION",DEF_SCOURGE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			SPIKE_ENERGY_CONSUMPTION = prefs.getDouble("SPIKE_ENERGY_CONSUMPTION",DEF_SPIKE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			INDIGO_ENERGY_CONSUMPTION = prefs.getDouble("INDIGO_ENERGY_CONSUMPTION",DEF_INDIGO_ENERGY_CONSUMPTION); //$NON-NLS-1$
			LAVENDER_ENERGY_CONSUMPTION = prefs.getDouble("LAVENDER_ENERGY_CONSUMPTION",DEF_LAVENDER_ENERGY_CONSUMPTION); //$NON-NLS-1$
			FALLOW_ENERGY_CONSUMPTION = prefs.getDouble("FALLOW_ENERGY_CONSUMPTION",DEF_FALLOW_ENERGY_CONSUMPTION); //$NON-NLS-1$
			SPORE_ENERGY_CONSUMPTION = prefs.getDouble("SPORE_ENERGY_CONSUMPTION",DEF_SPORE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			RED_PROB = prefs.getInt("RED_PROB",DEF_RED_PROB); //$NON-NLS-1$
			GREEN_PROB = prefs.getInt("GREEN_PROB",DEF_GREEN_PROB); //$NON-NLS-1$
			BLUE_PROB = prefs.getInt("BLUE_PROB",DEF_BLUE_PROB); //$NON-NLS-1$
			CYAN_PROB = prefs.getInt("CYAN_PROB",DEF_CYAN_PROB); //$NON-NLS-1$
			WHITE_PROB = prefs.getInt("WHITE_PROB",DEF_WHITE_PROB); //$NON-NLS-1$
			GRAY_PROB = prefs.getInt("GRAY_PROB",DEF_GRAY_PROB); //$NON-NLS-1$
			DARKGRAY_PROB = prefs.getInt("DARKGRAY_PROB",DEF_DARKGRAY_PROB); //$NON-NLS-1$
			SILVER_PROB = prefs.getInt("SILVER_PROB",DEF_SILVER_PROB); //$NON-NLS-1$
			YELLOW_PROB = prefs.getInt("YELLOW_PROB",DEF_YELLOW_PROB); //$NON-NLS-1$
			MAGENTA_PROB = prefs.getInt("MAGENTA_PROB",DEF_MAGENTA_PROB); //$NON-NLS-1$
			PINK_PROB = prefs.getInt("PINK_PROB",DEF_PINK_PROB); //$NON-NLS-1$
			CORAL_PROB = prefs.getInt("CORAL_PROB",DEF_CORAL_PROB); //$NON-NLS-1$
			ORANGE_PROB = prefs.getInt("ORANGE_PROB",DEF_ORANGE_PROB); //$NON-NLS-1$
			FOREST_PROB = prefs.getInt("FOREST_PROB",DEF_FOREST_PROB); //$NON-NLS-1$
			SPRING_PROB = prefs.getInt("SPRING_PROB",DEF_SPRING_PROB); //$NON-NLS-1$
			LEAF_PROB = prefs.getInt("LEAF_PROB",DEF_LEAF_PROB); //$NON-NLS-1$
			LIME_PROB = prefs.getInt("LIME_PROB",DEF_LIME_PROB); //$NON-NLS-1$
			SUMMER_PROB = prefs.getInt("SUMMER_PROB",DEF_SUMMER_PROB); //$NON-NLS-1$
			BARK_PROB = prefs.getInt("BARK_PROB",DEF_BARK_PROB); //$NON-NLS-1$
			JADE_PROB = prefs.getInt("JADE_PROB",DEF_JADE_PROB); //$NON-NLS-1$
			GRASS_PROB = prefs.getInt("GRASS_PROB",DEF_GRASS_PROB); //$NON-NLS-1$
			PURPLE_PROB = prefs.getInt("PURPLE_PROB",DEF_PURPLE_PROB); //$NON-NLS-1$
			C4_PROB = prefs.getInt("C4_PROB",DEF_C4_PROB); //$NON-NLS-1$
			VIOLET_PROB = prefs.getInt("VIOLET_PROB",DEF_VIOLET_PROB); //$NON-NLS-1$
			TEAL_PROB = prefs.getInt("TEAL_PROB",DEF_TEAL_PROB); //$NON-NLS-1$
			EYE_PROB = prefs.getInt("EYE_PROB",DEF_EYE_PROB); //$NON-NLS-1$
			MAROON_PROB = prefs.getInt("MAROON_PROB",DEF_MAROON_PROB); //$NON-NLS-1$
			OLIVE_PROB = prefs.getInt("OLIVE_PROB",DEF_OLIVE_PROB); //$NON-NLS-1$
			MINT_PROB = prefs.getInt("MINT_PROB",DEF_MINT_PROB); //$NON-NLS-1$
			CREAM_PROB = prefs.getInt("CREAM_PROB",DEF_CREAM_PROB); //$NON-NLS-1$
			ROSE_PROB = prefs.getInt("ROSE_PROB",DEF_ROSE_PROB); //$NON-NLS-1$
			DARK_PROB = prefs.getInt("DARK_PROB",DEF_DARK_PROB); //$NON-NLS-1$
			OCHRE_PROB = prefs.getInt("OCHRE_PROB",DEF_OCHRE_PROB); //$NON-NLS-1$
			SKY_PROB = prefs.getInt("SKY_PROB",DEF_SKY_PROB); //$NON-NLS-1$
			LILAC_PROB = prefs.getInt("LILAC_PROB",DEF_LILAC_PROB); //$NON-NLS-1$
			FIRE_PROB = prefs.getInt("FIRE_PROB",DEF_FIRE_PROB); //$NON-NLS-1$
			GOLD_PROB = prefs.getInt("GOLD_PROB",DEF_GOLD_PROB); //$NON-NLS-1$
			BLOND_PROB = prefs.getInt("BLOND_PROB",DEF_BLOND_PROB); //$NON-NLS-1$
			FLOWER_PROB = prefs.getInt("FLOWER_PROB",DEF_FLOWER_PROB); //$NON-NLS-1$
			AUBURN_PROB = prefs.getInt("AUBURN_PROB",DEF_AUBURN_PROB); //$NON-NLS-1$
			PLAGUE_PROB = prefs.getInt("PLAGUE_PROB",DEF_PLAGUE_PROB); //$NON-NLS-1$
			SPIKE_PROB = prefs.getInt("SPIKE_PROB",DEF_SPIKE_PROB); //$NON-NLS-1$
			INDIGO_PROB = prefs.getInt("INDIGO_PROB",DEF_INDIGO_PROB); //$NON-NLS-1$
			LAVENDER_PROB = prefs.getInt("LAVENDER_PROB",DEF_LAVENDER_PROB); //$NON-NLS-1$
			FALLOW_PROB = prefs.getInt("FALLOW_PROB",DEF_FALLOW_PROB); //$NON-NLS-1$
			SPORE_PROB = prefs.getInt("SPORE_PROB",DEF_SPORE_PROB); //$NON-NLS-1$
			DRAIN_SUBS_DIVISOR = prefs.getInt("DRAIN_SUBS_DIVISOR",DEF_DRAIN_SUBS_DIVISOR); //$NON-NLS-1$
			INITIAL_ENERGY = prefs.getInt("INITIAL_ENERGY",DEF_INITIAL_ENERGY); //$NON-NLS-1$
			REACTION_VEL = prefs.getDouble("REACTION_VEL",DEF_REACTION_VEL); //$NON-NLS-1$
			SPORE1_VEL = prefs.getDouble("SPORE1_VEL",DEF_SPORE1_VEL); //$NON-NLS-1$
			SPORE5_VEL = prefs.getDouble("SPORE5_VEL",DEF_SPORE5_VEL); //$NON-NLS-1$
			SPORE10_VEL = prefs.getDouble("SPORE10_VEL",DEF_SPORE10_VEL); //$NON-NLS-1$
			MAX_VEL = prefs.getDouble("MAX_VEL",DEF_MAX_VEL); //$NON-NLS-1$
			MAX_ROT = prefs.getDouble("MAX_ROT",DEF_MAX_ROT); //$NON-NLS-1$
			ELASTICITY = prefs.getDouble("ELASTICITY",DEF_ELASTICITY); //$NON-NLS-1$
			DELAY = prefs.getInt("DELAY",DEF_DELAY); //$NON-NLS-1$
			repaintWorld = prefs.getBoolean("repaintWorld",DEF_repaintWorld); //$NON-NLS-1$
			repaintWorldStrategy = RepaintWorldStrategy.valueOf(prefs.get("repaintWorldStrategy", DEF_repaintWorldStrategy)); //$NON-NLS-1$
			STATUS_BAR_REFRESH_FPS = prefs.getInt("STATUS_BAR_REFRESH_FPS",DEF_STATUS_BAR_REFRESH_FPS); //$NON-NLS-1$
			STATISTICS_REFRESH_FPS = prefs.getInt("STATISTICS_REFRESH_FPS",DEF_STATISTICS_REFRESH_FPS); //$NON-NLS-1$
			AUTO_BACKUP = prefs.getBoolean("AUTO_BACKUP",DEF_AUTO_BACKUP);
			COMPRESS_BACKUPS = prefs.getBoolean("COMPRESS_BACKUPS",DEF_COMPRESS_BACKUPS);
			AUTO_BACKUP_CSV = prefs.getBoolean("AUTO_BACKUP_CSV",DEF_AUTO_BACKUP_CSV);
			AUTO_BACKUP_WORLD_PNG = prefs.getBoolean("AUTO_BACKUP_WORLD_PNG",DEF_AUTO_BACKUP_WORLD_PNG);
			AUTO_BACKUP_STATISTICS_PNG = prefs.getBoolean("AUTO_BACKUP_STATISTICS_PNG",DEF_AUTO_BACKUP_STATISTICS_PNG);
			BACKUP_DELAY = prefs.getInt("BACKUP_DELAY",DEF_BACKUP_DELAY);
			LOCAL_PORT = prefs.getInt("LOCAL_PORT",DEF_LOCAL_PORT); //$NON-NLS-1$
			USER_NAME = prefs.get("USER_NAME",DEF_USER_NAME);
			MAX_CONNECTIONS = prefs.getInt("MAX_CONNECTIONS",DEF_MAX_CONNECTIONS); //$NON-NLS-1$
			ACCEPT_CONNECTIONS = prefs.getBoolean("ACCEPT_CONNECTIONS",DEF_ACCEPT_CONNECTIONS); //$NON-NLS-1$
			CONNECT_TO_SERVER = prefs.getBoolean("CONNECT_TO_SERVER",DEF_CONNECT_TO_SERVER); //$NON-NLS-1$
			SERVER_ADDRESS = prefs.get("SERVER_ADDRESS",DEF_SERVER_ADDRESS); //$NON-NLS-1$
			SERVER_PORT = prefs.getInt("SERVER_PORT",DEF_SERVER_PORT); //$NON-NLS-1$
			DECAY_ENERGY = prefs.getDouble("DECAY_ENERGY", DEF_DECAY_ENERGY); //$NON-NLS-1$
			setHardwareAcceleration(prefs.getInt("HARDWARE_ACCELERATION", DEF_HARDWARE_ACCELERATION)); //$NON-NLS-1$
			if (HARDWARE_ACCELERATION == 1 || HARDWARE_ACCELERATION == 4) {
				prefs.putInt("HARDWARE_ACCELERATION", 0); //$NON-NLS-1$
				HARDWARE_ACCELERATION += 1;
			}
			Messages.setLocale(prefs.get("LOCALE",Messages.getLanguage())); //$NON-NLS-1$

		} catch (SecurityException ex) {
			Messages.setLocale(Messages.getLanguage());
		}
	}
	public static void quitProgram(MainWindow window) {
		try {
			Preferences prefs = Preferences.userNodeForPackage(Utils.class);
			if (HARDWARE_ACCELERATION == 2 || HARDWARE_ACCELERATION == 5) {
				String[] options = {Messages.getString("T_YES"), Messages.getString("T_NO")}; //$NON-NLS-1$ //$NON-NLS-2$
				int answer = JOptionPane.showOptionDialog(null, Messages.getString("T_DID_OPENGL_WORK_WELL"), Messages.getString("T_OPENGL_CONFIRMATION"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);  //$NON-NLS-1$//$NON-NLS-2$
				if (answer == JOptionPane.YES_OPTION)
					prefs.putInt("HARDWARE_ACCELERATION", HARDWARE_ACCELERATION+1); //$NON-NLS-1$
			}

			prefs.putInt("WINDOW_X", window.getX());
			prefs.putInt("WINDOW_Y", window.getY());
			prefs.putInt("WINDOW_WIDTH", window.getWidth());
			prefs.putInt("WINDOW_HEIGHT", window.getHeight());
			prefs.putInt("WINDOW_STATE", window.getExtendedState());
		} catch (SecurityException ex) {
			// do nothing
		}
		savePreferences();
	}

	public static void setHardwareAcceleration(int newValue) {
		if (System.getenv("SKIP_OPENGL") != null && System.getenv("SKIP_OPENGL").equals("true")) {
			return;
		}
		try {
			switch (newValue) {
			case 0:
			case 2:
			case 5:
				System.setProperty("sun.java2d.opengl", "false"); //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case 1:
			case 3:
				System.setProperty("sun.java2d.opengl", "True"); //$NON-NLS-1$ //$NON-NLS-2$
				System.setProperty("sun.java2d.noddraw", "true");  //$NON-NLS-1$//$NON-NLS-2$
				break;
			case 4:
			case 6:
				System.setProperty("sun.java2d.opengl", "True"); //$NON-NLS-1$ //$NON-NLS-2$
				System.setProperty("sun.java2d.noddraw", "true");  //$NON-NLS-1$//$NON-NLS-2$
				// Used to workaround problems with some drivers
				System.setProperty("sun.java2d.opengl.fbobject","false"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			HARDWARE_ACCELERATION = newValue;
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
	}

	/**
	 * Getter for the `repaintWorld` flag.
	 */
	public static boolean isRepaintWorld() {
		return repaintWorld;
	}

	/**
	 * Setter for the `repaintWorld` flag. It notifies the `repaintWorldChangeListeners` if
	 * the `b` is different than the old value of `repaintWorld`.
	 */
	public static void setRepaintWorld(boolean b) {
		if (repaintWorld != b) {
			repaintWorld = b;
			notifyRepaintWorldChangeListeners();
		}
	}

	/**
	 * Setter for the `mainWindowInFocus` flag. It notifies the `repaintWorldChangeListeners` if
	 * the `b` is different than the old value of `mainWindowInFocus`.
	 */
	public static void setMainWindowInFocus(boolean b) {
		if (mainWindowInFocus != b) {
			mainWindowInFocus = b;
			notifyRepaintWorldChangeListeners();
		}
	}

	/**
	 * Returns true if the world should be repainted for every change.
	 * It should not repaint the world if the window is not in focus
	 * (to reduce wasted work and to make the simulation faster).
	 */
	public static boolean repaintWorld() {
		if (!repaintWorld) {
			return false;
		}
		switch (repaintWorldStrategy) {
			case ALWAYS:
				return true;
			case ONLY_WHEN_MAIN_WINDOW_IS_IN_FOCUS:
				return mainWindowInFocus;
			case WHEN_ANY_APP_WINDOW_IS_IN_FOCUS:
				return appInFocus;
			default:
				return true;
		}
	}

	/**
	 * Adds `l` to the list of listeners to notify when the return value of
	 * the `repaintWorld` method changed.
	 * @param l The listener to notify about changes
	 */
	public static void addRepaintWorldChangeListener(RepaintWorldChangeListener l) {
		repaintWorldChangeListeners.add(l);
	}

	private static void notifyRepaintWorldChangeListeners() {
		boolean b = repaintWorld();
		for (RepaintWorldChangeListener drawWorldChangeListener : repaintWorldChangeListeners) {
			drawWorldChangeListener.drawWorldChanged(b);
		}
	}

	public static RepaintWorldStrategy getRepaintWorldStrategy() {
		return repaintWorldStrategy;
	}

	public static void setRepaintWorldStrategy(RepaintWorldStrategy repaintWorldStrategy) {
		Utils.repaintWorldStrategy = repaintWorldStrategy;
	}

	public static boolean isAppInFocus() {
		return appInFocus;
	}

	public static void setAppInFocus(boolean appInFocus) {
		Utils.appInFocus = appInFocus;
	}
}
