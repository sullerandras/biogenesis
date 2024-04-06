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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ParamDialog extends JDialog {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	private JComboBox localeCombo = null;
	private JTextField widthText = null;
	private JTextField heightText = null;
	private JTextField delayText = null;
	private JTextField saveDelayText = null;
	private JTextField threadCountText = null;
	private JRadioButton repaintWorldStrategyRadio1 = null;
	private JRadioButton repaintWorldStrategyRadio2 = null;
	private JRadioButton repaintWorldStrategyRadio3 = null;
	private JCheckBox autoSaveCheck = null;
	private JCheckBox autoBackupsCheck = null;
	private JCheckBox compressBackupsCheck = null;
	private JCheckBox autoBackupsCSVCheck = null;
	private JCheckBox autoBackupsWorldPngCheck = null;
	private JCheckBox autoBackupsStatisticsPngCheck = null;
	private JCheckBox autoBackupsCladesPngCheck = null;
	private JCheckBox autoBackupsImagesAsFoldersCheck = null;
	private JTextField backupDelayText = null;
	protected JRadioButton hardwareNoneRadio = null;
	protected JRadioButton hardwareOpenGLRadio = null;
	private ButtonGroup hardwareGroup = null;
	protected JCheckBox hardwareFBObjectCheck = null;
	private JTextField rubbingText = null;
	private JTextField elasticityText = null;
	private JTextField initialnumberText = null;
	private JTextField initialenergyText = null;
	private JTextField initialcomplexityText = null;
	private JTextField cladecomplexityText = null;
	private JTextField initialO2Text = null;
	private JTextField initialCO2Text = null;
	private JTextField initialCO1Text = null;
	private JTextField initialCH4Text = null;
	private JTextField initialDetritusText = null;
	private JTextField maxageText = null;
	private JTextField agedivisorText = null;
	private JTextField CO2toCH4divisorText = null;
	private JTextField CH4toCO2divisorText = null;
	private JTextField detritustoCO1divisorText = null;
	private JTextField CO1toCO2divisorText = null;
	private JTextField metamutationrateText = null;
	private JTextField maxmutationrateText = null;
	private JTextField minmutationrateText = null;
	private JTextField maxclonerateText = null;
	private JTextField minclonerateText = null;
	private JTextField reactionvelocityText = null;
	private JTextField spore1velocityText = null;
	private JTextField spore5velocityText = null;
	private JTextField spore10velocityText = null;
	private JTextField redcostText = null;
	private JTextField greencostText = null;
	private JTextField bluecostText = null;
	private JTextField cyancostText = null;
	private JTextField whitecostText = null;
	private JTextField graycostText = null;
	private JTextField yellowcostText = null;
	private JTextField magentacostText = null;
	private JTextField pinkcostText = null;
	private JTextField coralcostText = null;
	private JTextField orangecostText = null;
	private JTextField forestcostText = null;
	private JTextField springcostText = null;
	private JTextField leafcostText = null;
	private JTextField limecostText = null;
	private JTextField summercostText = null;
	private JTextField c4costText = null;
	private JTextField jadecostText = null;
	private JTextField grasscostText = null;
	private JTextField purplecostText = null;
	private JTextField planktoncostText = null;
	private JTextField barkcostText = null;
	private JTextField violetcostText = null;
	private JTextField tealcostText = null;
	private JTextField spincostText = null;
	private JTextField eyecostText = null;
	private JTextField marooncostText = null;
	private JTextField crimsoncostText = null;
	private JTextField olivecostText = null;
	private JTextField mintcostText = null;
	private JTextField creamcostText = null;
	private JTextField rosecostText = null;
	private JTextField darkcostText = null;
	private JTextField ochrecostText = null;
	private JTextField skycostText = null;
	private JTextField lilaccostText = null;
	private JTextField silvercostText = null;
	private JTextField firecostText = null;
	private JTextField darkgraycostText = null;
	private JTextField goldcostText = null;
	private JTextField blondcostText = null;
	private JTextField flowercostText = null;
	private JTextField auburncostText = null;
	private JTextField indigocostText = null;
	private JTextField lavendercostText = null;
	private JTextField fallowcostText = null;
	private JTextField sporecostText = null;
	private JTextField plaguecostText = null;
	private JTextField spikecostText = null;
	private JTextField segmentcostText = null;
	private JTextField drainText = null;
	private JTextField greenenergyText = null;
	private JTextField organicenergyText = null;
	private JTextField organicsubsText = null;
	private JTextField creamorganicsubsText = null;
	private JTextField healingText = null;
	private JTextField immunesystemText = null;
	private JTextField lavendershieldText = null;
	private JTextField indigodivisorText = null;
	private JTextField crowdedlimecostText = null;
	private JTextField crowdedforestcostText = null;
	private JTextField modleafcostText = null;
	private JTextField symbiontcostText = null;
	private JTextField mosquitocostText = null;
	private JTextField experiencecostText = null;
	private JTextField dodgecostText = null;
	private JTextField darkjadedelayText = null;
	private JTextField viruscostText = null;
	private JTextField scourgecostText = null;
	private JTextField redprobText = null;
	private JTextField greenprobText = null;
	private JTextField blueprobText = null;
	private JTextField cyanprobText = null;
	private JTextField whiteprobText = null;
	private JTextField grayprobText = null;
	private JTextField yellowprobText = null;
	private JTextField magentaprobText = null;
	private JTextField pinkprobText = null;
	private JTextField coralprobText = null;
	private JTextField orangeprobText = null;
	private JTextField forestprobText = null;
	private JTextField springprobText = null;
	private JTextField leafprobText = null;
	private JTextField limeprobText = null;
	private JTextField summerprobText = null;
	private JTextField c4probText = null;
	private JTextField jadeprobText = null;
	private JTextField grassprobText = null;
	private JTextField purpleprobText = null;
	private JTextField planktonprobText = null;
	private JTextField barkprobText = null;
	private JTextField violetprobText = null;
	private JTextField tealprobText = null;
	private JTextField spinprobText = null;
	private JTextField eyeprobText = null;
	private JTextField maroonprobText = null;
	private JTextField crimsonprobText = null;
	private JTextField oliveprobText = null;
	private JTextField mintprobText = null;
	private JTextField creamprobText = null;
	private JTextField roseprobText = null;
	private JTextField darkprobText = null;
	private JTextField ochreprobText = null;
	private JTextField skyprobText = null;
	private JTextField lilacprobText = null;
	private JTextField silverprobText = null;
	private JTextField fireprobText = null;
	private JTextField darkgrayprobText = null;
	private JTextField goldprobText = null;
	private JTextField blondprobText = null;
	private JTextField flowerprobText = null;
	private JTextField auburnprobText = null;
	private JTextField indigoprobText = null;
	private JTextField lavenderprobText = null;
	private JTextField fallowprobText = null;
	private JTextField sporeprobText = null;
	private JTextField plagueprobText = null;
	private JTextField spikeprobText = null;
	private JTextField decayenergyText = null;
	private JButton OKButton = null;
	private JButton cancelButton = null;
	private JButton defaultButton = null;

	protected MainWindow mainWindow;

	public ParamDialog(MainWindow parent) {
		// Configurem les caracter�stiques generals
		super(parent,Messages.getString("T_PARAMETERS_CONFIGURATION"),true); //$NON-NLS-1$
		mainWindow = parent;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setComponents();
		pack();
		WindowManager.registerWindow(this, getWidth(), getHeight(), 0, 0);
		setResizable(true);
		// Configurem les accions dels butons
		OKButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                    	checkParams();
                    	Utils.savePreferences();
                    	dispose();
                    }
                    });
		cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                    	dispose();
                    }
                    });
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				defaultPreferences();
			}
		});
		// Ja est� tot apunt
		addWindowListener(new AppFocusWindowAdapter());
		setVisible(true);
	}

	protected void defaultPreferences() {
		widthText.setText(String.valueOf(Utils.DEF_WORLD_WIDTH));
		heightText.setText(String.valueOf(Utils.DEF_WORLD_HEIGHT));
		delayText.setText(String.valueOf(Utils.DEF_DELAY));
		saveDelayText.setText(String.valueOf(Utils.DEF_SAVE_DELAY));
		threadCountText.setText(String.valueOf(Utils.DEF_THREAD_COUNT));
		repaintWorldStrategyRadio1.setSelected(Utils.DEF_repaintWorldStrategy.equals(RepaintWorldStrategy.ALWAYS.toString()));
		repaintWorldStrategyRadio2.setSelected(Utils.DEF_repaintWorldStrategy.equals(RepaintWorldStrategy.ONLY_WHEN_MAIN_WINDOW_IS_IN_FOCUS.toString()));
		repaintWorldStrategyRadio3.setSelected(Utils.DEF_repaintWorldStrategy.equals(RepaintWorldStrategy.WHEN_ANY_APP_WINDOW_IS_IN_FOCUS.toString()));
		autoBackupsCheck.setSelected(Utils.DEF_AUTO_BACKUP);
		autoSaveCheck.setSelected(Utils.DEF_AUTO_SAVE);
		compressBackupsCheck.setSelected(Utils.DEF_COMPRESS_BACKUPS);
		autoBackupsCSVCheck.setSelected(Utils.DEF_AUTO_BACKUP_CSV);
		autoBackupsWorldPngCheck.setSelected(Utils.DEF_AUTO_BACKUP_WORLD_PNG);
		autoBackupsStatisticsPngCheck.setSelected(Utils.DEF_AUTO_BACKUP_STATISTICS_PNG);
		autoBackupsCladesPngCheck.setSelected(Utils.DEF_AUTO_BACKUP_CLADES_PNG);
		autoBackupsImagesAsFoldersCheck.setSelected(Utils.DEF_AUTO_BACKUP_IMAGES_AS_FOLDERS);
		backupDelayText.setText(String.valueOf(Utils.DEF_BACKUP_DELAY));
		rubbingText.setText(String.valueOf(Utils.DEF_RUBBING));
		elasticityText.setText(String.valueOf(Utils.DEF_ELASTICITY));
		initialnumberText.setText(String.valueOf(Utils.DEF_INITIAL_ORGANISMS));
		initialenergyText.setText(String.valueOf(Utils.DEF_INITIAL_ENERGY));
		initialcomplexityText.setText(String.valueOf(Utils.DEF_INITIAL_COMPLEXITY));
		cladecomplexityText.setText(String.valueOf(Utils.DEF_CLADE_COMPLEXITY));
		initialO2Text.setText(String.valueOf(Utils.DEF_INITIAL_O2));
		initialCO2Text.setText(String.valueOf(Utils.DEF_INITIAL_CO2));
		initialCO1Text.setText(String.valueOf(Utils.DEF_INITIAL_CO1));
		initialCH4Text.setText(String.valueOf(Utils.DEF_INITIAL_CH4));
		initialDetritusText.setText(String.valueOf(Utils.DEF_INITIAL_DETRITUS));
		maxageText.setText(String.valueOf(Utils.DEF_MAX_AGE));
		agedivisorText.setText(String.valueOf(Utils.DEF_AGE_DIVISOR));
		CO2toCH4divisorText.setText(String.valueOf(Utils.DEF_CO2_TO_CH4_DIVISOR));
		CH4toCO2divisorText.setText(String.valueOf(Utils.DEF_CH4_TO_CO2_DIVISOR));
		detritustoCO1divisorText.setText(String.valueOf(Utils.DEF_DETRITUS_TO_CO1_DIVISOR));
		CO1toCO2divisorText.setText(String.valueOf(Utils.DEF_CO1_TO_CO2_DIVISOR));
		metamutationrateText.setText(String.valueOf(Utils.DEF_META_MUTATION_RATE));
		maxmutationrateText.setText(String.valueOf(Utils.DEF_MAX_MUTATION_RATE));
		minmutationrateText.setText(String.valueOf(Utils.DEF_MIN_MUTATION_RATE));
		maxclonerateText.setText(String.valueOf(Utils.DEF_MAX_CLONE_RATE));
		minclonerateText.setText(String.valueOf(Utils.DEF_MIN_CLONE_RATE));
		reactionvelocityText.setText(String.valueOf(Utils.DEF_REACTION_VEL));
		spore1velocityText.setText(String.valueOf(Utils.DEF_SPORE1_VEL));
		spore5velocityText.setText(String.valueOf(Utils.DEF_SPORE5_VEL));
		spore10velocityText.setText(String.valueOf(Utils.DEF_SPORE10_VEL));
		segmentcostText.setText(String.valueOf(Utils.DEF_SEGMENT_COST_DIVISOR));
		drainText.setText(String.valueOf(Utils.DEF_DRAIN_SUBS_DIVISOR));
		greenenergyText.setText(String.valueOf(Utils.DEF_GREEN_OBTAINED_ENERGY_DIVISOR));
		organicenergyText.setText(String.valueOf(Utils.DEF_ORGANIC_OBTAINED_ENERGY));
		organicsubsText.setText(String.valueOf(Utils.DEF_ORGANIC_SUBS_PRODUCED));
		creamorganicsubsText.setText(String.valueOf(Utils.DEF_CREAM_ORGANIC_SUBS_PRODUCED));
		healingText.setText(String.valueOf(Utils.DEF_HEALING));
		immunesystemText.setText(String.valueOf(Utils.DEF_IMMUNE_SYSTEM));
		lavendershieldText.setText(String.valueOf(Utils.DEF_LAVENDER_SHIELD));
		indigodivisorText.setText(String.valueOf(Utils.DEF_INDIGO_DIVISOR));
		mosquitocostText.setText(String.valueOf(Utils.DEF_MOSQUITO_ENERGY_CONSUMPTION));
		crowdedlimecostText.setText(String.valueOf(Utils.DEF_CROWDEDLIME_ENERGY_CONSUMPTION));
		crowdedforestcostText.setText(String.valueOf(Utils.DEF_CROWDEDFOREST_ENERGY_CONSUMPTION));
		modleafcostText.setText(String.valueOf(Utils.DEF_MODLEAF_ENERGY_CONSUMPTION));
		symbiontcostText.setText(String.valueOf(Utils.DEF_SYMBIONT_ENERGY_CONSUMPTION));
		experiencecostText.setText(String.valueOf(Utils.DEF_EXPERIENCE_ENERGY_CONSUMPTION));
		dodgecostText.setText(String.valueOf(Utils.DEF_DODGE_ENERGY_CONSUMPTION));
		darkjadedelayText.setText(String.valueOf(Utils.DEF_DARKJADE_DELAY));
		viruscostText.setText(String.valueOf(Utils.DEF_VIRUS_ENERGY_CONSUMPTION));
		scourgecostText.setText(String.valueOf(Utils.DEF_SCOURGE_ENERGY_CONSUMPTION));
		redcostText.setText(String.valueOf(Utils.DEF_RED_ENERGY_CONSUMPTION));
		greencostText.setText(String.valueOf(Utils.DEF_GREEN_ENERGY_CONSUMPTION));
		bluecostText.setText(String.valueOf(Utils.DEF_BLUE_ENERGY_CONSUMPTION));
		cyancostText.setText(String.valueOf(Utils.DEF_CYAN_ENERGY_CONSUMPTION));
		whitecostText.setText(String.valueOf(Utils.DEF_WHITE_ENERGY_CONSUMPTION));
		graycostText.setText(String.valueOf(Utils.DEF_GRAY_ENERGY_CONSUMPTION));
		yellowcostText.setText(String.valueOf(Utils.DEF_YELLOW_ENERGY_CONSUMPTION));
		magentacostText.setText(String.valueOf(Utils.DEF_MAGENTA_ENERGY_CONSUMPTION));
		pinkcostText.setText(String.valueOf(Utils.DEF_PINK_ENERGY_CONSUMPTION));
		coralcostText.setText(String.valueOf(Utils.DEF_CORAL_ENERGY_CONSUMPTION));
		orangecostText.setText(String.valueOf(Utils.DEF_ORANGE_ENERGY_CONSUMPTION));
		forestcostText.setText(String.valueOf(Utils.DEF_FOREST_ENERGY_CONSUMPTION));
		springcostText.setText(String.valueOf(Utils.DEF_SPRING_ENERGY_CONSUMPTION));
		leafcostText.setText(String.valueOf(Utils.DEF_LEAF_ENERGY_CONSUMPTION));
		summercostText.setText(String.valueOf(Utils.DEF_SUMMER_ENERGY_CONSUMPTION));
		limecostText.setText(String.valueOf(Utils.DEF_LIME_ENERGY_CONSUMPTION));
		c4costText.setText(String.valueOf(Utils.DEF_C4_ENERGY_CONSUMPTION));
		jadecostText.setText(String.valueOf(Utils.DEF_JADE_ENERGY_CONSUMPTION));
		grasscostText.setText(String.valueOf(Utils.DEF_GRASS_ENERGY_CONSUMPTION));
		purplecostText.setText(String.valueOf(Utils.DEF_PURPLE_ENERGY_CONSUMPTION));
		planktoncostText.setText(String.valueOf(Utils.DEF_PLANKTON_ENERGY_CONSUMPTION));
		barkcostText.setText(String.valueOf(Utils.DEF_BARK_ENERGY_CONSUMPTION));
		violetcostText.setText(String.valueOf(Utils.DEF_VIOLET_ENERGY_CONSUMPTION));
		tealcostText.setText(String.valueOf(Utils.DEF_TEAL_ENERGY_CONSUMPTION));
		spincostText.setText(String.valueOf(Utils.DEF_SPIN_ENERGY_CONSUMPTION));
		eyecostText.setText(String.valueOf(Utils.DEF_EYE_ENERGY_CONSUMPTION));
		marooncostText.setText(String.valueOf(Utils.DEF_MAROON_ENERGY_CONSUMPTION));
		crimsoncostText.setText(String.valueOf(Utils.DEF_CRIMSON_ENERGY_CONSUMPTION));
		olivecostText.setText(String.valueOf(Utils.DEF_OLIVE_ENERGY_CONSUMPTION));
		mintcostText.setText(String.valueOf(Utils.DEF_MINT_ENERGY_CONSUMPTION));
		creamcostText.setText(String.valueOf(Utils.DEF_CREAM_ENERGY_CONSUMPTION));
		rosecostText.setText(String.valueOf(Utils.DEF_ROSE_ENERGY_CONSUMPTION));
		darkcostText.setText(String.valueOf(Utils.DEF_DARK_ENERGY_CONSUMPTION));
		ochrecostText.setText(String.valueOf(Utils.DEF_OCHRE_ENERGY_CONSUMPTION));
		skycostText.setText(String.valueOf(Utils.DEF_SKY_ENERGY_CONSUMPTION));
		lilaccostText.setText(String.valueOf(Utils.DEF_LILAC_ENERGY_CONSUMPTION));
		silvercostText.setText(String.valueOf(Utils.DEF_SILVER_ENERGY_CONSUMPTION));
		firecostText.setText(String.valueOf(Utils.DEF_FIRE_ENERGY_CONSUMPTION));
		darkgraycostText.setText(String.valueOf(Utils.DEF_DARKGRAY_ENERGY_CONSUMPTION));
		goldcostText.setText(String.valueOf(Utils.DEF_GOLD_DIVISOR));
		blondcostText.setText(String.valueOf(Utils.DEF_BLOND_ENERGY_CONSUMPTION));
		flowercostText.setText(String.valueOf(Utils.DEF_FLOWER_ENERGY_CONSUMPTION));
		auburncostText.setText(String.valueOf(Utils.DEF_AUBURN_ENERGY_CONSUMPTION));
		indigocostText.setText(String.valueOf(Utils.DEF_INDIGO_ENERGY_CONSUMPTION));
		lavendercostText.setText(String.valueOf(Utils.DEF_LAVENDER_ENERGY_CONSUMPTION));
		fallowcostText.setText(String.valueOf(Utils.DEF_FALLOW_ENERGY_CONSUMPTION));
		sporecostText.setText(String.valueOf(Utils.DEF_SPORE_ENERGY_CONSUMPTION));
		plaguecostText.setText(String.valueOf(Utils.DEF_PLAGUE_ENERGY_CONSUMPTION));
		spikecostText.setText(String.valueOf(Utils.DEF_SPIKE_ENERGY_CONSUMPTION));
		redprobText.setText(String.valueOf(Utils.DEF_RED_PROB));
		greenprobText.setText(String.valueOf(Utils.DEF_GREEN_PROB));
		blueprobText.setText(String.valueOf(Utils.DEF_BLUE_PROB));
		cyanprobText.setText(String.valueOf(Utils.DEF_CYAN_PROB));
		whiteprobText.setText(String.valueOf(Utils.DEF_WHITE_PROB));
		grayprobText.setText(String.valueOf(Utils.DEF_GRAY_PROB));
		yellowprobText.setText(String.valueOf(Utils.DEF_YELLOW_PROB));
		magentaprobText.setText(String.valueOf(Utils.DEF_MAGENTA_PROB));
		pinkprobText.setText(String.valueOf(Utils.DEF_PINK_PROB));
		coralprobText.setText(String.valueOf(Utils.DEF_CORAL_PROB));
		orangeprobText.setText(String.valueOf(Utils.DEF_ORANGE_PROB));
		forestprobText.setText(String.valueOf(Utils.DEF_FOREST_PROB));
		springprobText.setText(String.valueOf(Utils.DEF_SPRING_PROB));
		leafprobText.setText(String.valueOf(Utils.DEF_LEAF_PROB));
		summerprobText.setText(String.valueOf(Utils.DEF_SUMMER_PROB));
		limeprobText.setText(String.valueOf(Utils.DEF_LIME_PROB));
		c4probText.setText(String.valueOf(Utils.DEF_C4_PROB));
		jadeprobText.setText(String.valueOf(Utils.DEF_JADE_PROB));
		grassprobText.setText(String.valueOf(Utils.DEF_GRASS_PROB));
		purpleprobText.setText(String.valueOf(Utils.DEF_PURPLE_PROB));
		planktonprobText.setText(String.valueOf(Utils.DEF_PLANKTON_PROB));
		barkprobText.setText(String.valueOf(Utils.DEF_BARK_PROB));
		violetprobText.setText(String.valueOf(Utils.DEF_VIOLET_PROB));
		tealprobText.setText(String.valueOf(Utils.DEF_TEAL_PROB));
		spinprobText.setText(String.valueOf(Utils.DEF_SPIN_PROB));
		eyeprobText.setText(String.valueOf(Utils.DEF_EYE_PROB));
		maroonprobText.setText(String.valueOf(Utils.DEF_MAROON_PROB));
		crimsonprobText.setText(String.valueOf(Utils.DEF_CRIMSON_PROB));
		oliveprobText.setText(String.valueOf(Utils.DEF_OLIVE_PROB));
		mintprobText.setText(String.valueOf(Utils.DEF_MINT_PROB));
		creamprobText.setText(String.valueOf(Utils.DEF_CREAM_PROB));
		roseprobText.setText(String.valueOf(Utils.DEF_ROSE_PROB));
		darkprobText.setText(String.valueOf(Utils.DEF_DARK_PROB));
		ochreprobText.setText(String.valueOf(Utils.DEF_OCHRE_PROB));
		skyprobText.setText(String.valueOf(Utils.DEF_SKY_PROB));
		lilacprobText.setText(String.valueOf(Utils.DEF_LILAC_PROB));
		silverprobText.setText(String.valueOf(Utils.DEF_SILVER_PROB));
		fireprobText.setText(String.valueOf(Utils.DEF_FIRE_PROB));
		darkgrayprobText.setText(String.valueOf(Utils.DEF_DARKGRAY_PROB));
		goldprobText.setText(String.valueOf(Utils.DEF_GOLD_PROB));
		blondprobText.setText(String.valueOf(Utils.DEF_BLOND_PROB));
		flowerprobText.setText(String.valueOf(Utils.DEF_FLOWER_PROB));
		auburnprobText.setText(String.valueOf(Utils.DEF_AUBURN_PROB));
		indigoprobText.setText(String.valueOf(Utils.DEF_INDIGO_PROB));
		lavenderprobText.setText(String.valueOf(Utils.DEF_LAVENDER_PROB));
		fallowprobText.setText(String.valueOf(Utils.DEF_FALLOW_PROB));
		sporeprobText.setText(String.valueOf(Utils.DEF_SPORE_PROB));
		plagueprobText.setText(String.valueOf(Utils.DEF_PLAGUE_PROB));
		spikeprobText.setText(String.valueOf(Utils.DEF_SPIKE_PROB));
		decayenergyText.setText(String.valueOf(Utils.DEF_DECAY_ENERGY));
		switch (Utils.DEF_HARDWARE_ACCELERATION) {
		case 0:
		case 2:
		case 5:
			hardwareNoneRadio.setSelected(true);
			hardwareFBObjectCheck.setSelected(false);
			hardwareFBObjectCheck.setEnabled(false);
			break;
		case 1:
		case 3:
			hardwareOpenGLRadio.setSelected(true);
			hardwareFBObjectCheck.setSelected(false);
			break;
		case 6:
			hardwareOpenGLRadio.setSelected(true);
			hardwareFBObjectCheck.setSelected(true);
			break;
		}
	}

	protected void setComponents() {
		getContentPane().setLayout(new BorderLayout());
		// Set up buttons
		JPanel buttonsPanel = new JPanel();
		OKButton = new JButton(Messages.getString("T_OK")); //$NON-NLS-1$
		cancelButton = new JButton(Messages.getString("T_CANCEL")); //$NON-NLS-1$
		defaultButton = new JButton(Messages.getString("T_DEFAULT_VALUES")); //$NON-NLS-1$
		buttonsPanel.add(OKButton);
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(defaultButton);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		JTabbedPane tabbedPane = new JTabbedPane();
		// Set up general tab
		tabbedPane.addTab(Messages.getString("T_GENERAL"), setGeneralTab()); //$NON-NLS-1$
		// Set up world tab
		tabbedPane.addTab(Messages.getString("T_WORLD"), setWorldTab()); //$NON-NLS-1$
		// Set up organisms tab
		tabbedPane.addTab(Messages.getString("T_ORGANISMS"), setOrganismsTab()); //$NON-NLS-1$
		// Set up metabolism tab
		tabbedPane.addTab(Messages.getString("T_METABOLISM"), setMetabolismTab()); //$NON-NLS-1$
		// Set up genes tab
		tabbedPane.addTab(Messages.getString("T_GENES"), setGenesTab()); //$NON-NLS-1$
		// Set up genes2 tab
		tabbedPane.addTab(Messages.getString("T_GENES2"), setGenes2Tab()); //$NON-NLS-1$
		// Set up genes3 tab
		tabbedPane.addTab(Messages.getString("T_GENES3"), setGenes3Tab()); //$NON-NLS-1$
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
	}

	protected JPanel setGeneralTab() {
		JPanel generalPanel = new JPanel();
		generalPanel.setLayout(new BoxLayout(generalPanel,BoxLayout.Y_AXIS));
		// Language
		JPanel panel = new JPanel();
		JLabel label = new JLabel(Messages.getString("T_LANGUAGE")); //$NON-NLS-1$
		panel.add(label);
		localeCombo = new JComboBox(Messages.getSupportedLocalesNames());
		localeCombo.setSelectedIndex(Messages.getLocaleIndex());
		panel.add(localeCombo);
		generalPanel.add(panel);
		//Frames per second
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_TIME_PER_FRAME")); //$NON-NLS-1$
		panel.add(label);
		delayText = new JTextField(Integer.toString(Utils.DELAY),6);
		panel.add(delayText);
		label = new JLabel(Messages.getString("T_MILLISECONDS")); //$NON-NLS-1$
		panel.add(label);
		label = new JLabel(Messages.getString("T_THREAD_COUNT")); //$NON-NLS-1$
		panel.add(label);
		threadCountText = new JTextField(Integer.toString(Utils.THREAD_COUNT),6);
		panel.add(threadCountText);
		generalPanel.add(panel);
		//World repaint
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(Messages.getString("T_RENDER_WORLD_BORDER_TITLE")));
		panel.setLayout(new GridLayout(3,1));
		repaintWorldStrategyRadio1 = new JRadioButton(Messages.getString("T_ALWAYS"));
		repaintWorldStrategyRadio2 = new JRadioButton(Messages.getString("T_ONLY_WHEN_MAIN_WINDOW_IS_IN_FOCUS"));
		repaintWorldStrategyRadio3 = new JRadioButton(Messages.getString("T_WHEN_ANY_APP_WINDOW_IS_IN_FOCUS"));
		panel.add(repaintWorldStrategyRadio1);
		panel.add(repaintWorldStrategyRadio2);
		panel.add(repaintWorldStrategyRadio3);
		repaintWorldStrategyRadio1.addActionListener(e -> Utils.setRepaintWorldStrategy(RepaintWorldStrategy.ALWAYS));
		repaintWorldStrategyRadio2.addActionListener(e -> Utils.setRepaintWorldStrategy(RepaintWorldStrategy.ONLY_WHEN_MAIN_WINDOW_IS_IN_FOCUS));
		repaintWorldStrategyRadio3.addActionListener(e -> Utils.setRepaintWorldStrategy(RepaintWorldStrategy.WHEN_ANY_APP_WINDOW_IS_IN_FOCUS));
		repaintWorldStrategyRadio1.setSelected(Utils.getRepaintWorldStrategy().equals(RepaintWorldStrategy.ALWAYS));
		repaintWorldStrategyRadio2.setSelected(Utils.getRepaintWorldStrategy().equals(RepaintWorldStrategy.ONLY_WHEN_MAIN_WINDOW_IS_IN_FOCUS));
		repaintWorldStrategyRadio3.setSelected(Utils.getRepaintWorldStrategy().equals(RepaintWorldStrategy.WHEN_ANY_APP_WINDOW_IS_IN_FOCUS));
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(repaintWorldStrategyRadio1);
		buttonGroup.add(repaintWorldStrategyRadio2);
		buttonGroup.add(repaintWorldStrategyRadio3);
		generalPanel.add(panel);
		// Autosaves
		JPanel autosavePanel = new JPanel();
		autosavePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		autoSaveCheck = new JCheckBox(Messages.getString("T_AUTOMATIC_SAVES"));
		autoSaveCheck.setSelected(Utils.AUTO_SAVE);
		autosavePanel.add(autoSaveCheck);
		label = new JLabel(Messages.getString("T_TIME_BETWEEN_SAVES"));
		saveDelayText = new JTextField(Integer.toString(Utils.SAVE_DELAY),6);
		saveDelayText.setEnabled(Utils.AUTO_SAVE);
		autoSaveCheck.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				saveDelayText.setEnabled(autoSaveCheck.isSelected());
			}
		});
		autosavePanel.add(label);
		autosavePanel.add(saveDelayText);
		generalPanel.add(autosavePanel);
		//Backups
		JPanel backupPanel = new JPanel();
		backupPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		autoBackupsCheck = new JCheckBox(Messages.getString("T_AUTOMATIC_BACKUPS"));
		autoBackupsCheck.setSelected(Utils.AUTO_BACKUP);
		backupPanel.add(autoBackupsCheck);
		label = new JLabel(Messages.getString("T_TIME_BETWEEN_BACKUPS")); //$NON-NLS-1$
		backupDelayText = new JTextField(Integer.toString(Utils.BACKUP_DELAY),6);
		backupDelayText.setEnabled(Utils.AUTO_BACKUP);
		autoBackupsCheck.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				backupDelayText.setEnabled(autoBackupsCheck.isSelected());
			}
		});
		backupPanel.add(label);
		backupPanel.add(backupDelayText);
		generalPanel.add(backupPanel);
		// Other automatic backups
		panel = new JPanel();
		panel.setLayout(new GridLayout(6,1));

		compressBackupsCheck = new JCheckBox(Messages.getString("T_COMPRESS_BACKUPS"));
		compressBackupsCheck.setSelected(Utils.COMPRESS_BACKUPS);
		panel.add(compressBackupsCheck);

		autoBackupsCSVCheck = new JCheckBox(Messages.getString("T_AUTOMATIC_BACKUPS_CSV"));
		autoBackupsCSVCheck.setSelected(Utils.AUTO_BACKUP_CSV);
		panel.add(autoBackupsCSVCheck);

		autoBackupsWorldPngCheck = new JCheckBox(Messages.getString("T_AUTOMATIC_BACKUPS_WORLD_PNG"));
		autoBackupsWorldPngCheck.setSelected(Utils.AUTO_BACKUP_WORLD_PNG);
		panel.add(autoBackupsWorldPngCheck);

		autoBackupsStatisticsPngCheck = new JCheckBox(Messages.getString("T_AUTOMATIC_BACKUPS_STATISTICS_PNG"));
		autoBackupsStatisticsPngCheck.setSelected(Utils.AUTO_BACKUP_STATISTICS_PNG);
		panel.add(autoBackupsStatisticsPngCheck);

		autoBackupsCladesPngCheck = new JCheckBox(Messages.getString("T_AUTOMATIC_BACKUPS_CLADES_PNG"));
		autoBackupsCladesPngCheck.setSelected(Utils.AUTO_BACKUP_CLADES_PNG);
		panel.add(autoBackupsCladesPngCheck);

		autoBackupsImagesAsFoldersCheck = new JCheckBox(Messages.getString("T_AUTOMATIC_BACKUPS_IMAGES_AS_FOLDERS"));
		autoBackupsImagesAsFoldersCheck.setSelected(Utils.AUTO_BACKUP_IMAGES_AS_FOLDERS);
		panel.add(autoBackupsImagesAsFoldersCheck);
		generalPanel.add(panel);
		// OpenGL
		panel = new JPanel();
		panel.setLayout(new GridLayout(5,1));
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				Messages.getString("T_HARDWARE_ACCELERATION"))); //$NON-NLS-1$
		hardwareNoneRadio = new JRadioButton(Messages.getString("T_NONE")); //$NON-NLS-1$
		hardwareOpenGLRadio = new JRadioButton(Messages.getString("T_OPENGL")); //$NON-NLS-1$
		hardwareFBObjectCheck = new JCheckBox(Messages.getString("T_DISABLE_FBOBJECT")); //$NON-NLS-1$
		switch (Utils.HARDWARE_ACCELERATION) {
		case 0:
			hardwareNoneRadio.setSelected(true);
			hardwareFBObjectCheck.setEnabled(false);
			break;
		case 1:
		case 2:
		case 3:
			hardwareOpenGLRadio.setSelected(true);
			break;
		case 4:
		case 5:
		case 6:
			hardwareOpenGLRadio.setSelected(true);
			hardwareFBObjectCheck.setSelected(true);
			break;
		}
		hardwareGroup = new ButtonGroup();
		hardwareGroup.add(hardwareNoneRadio);
		hardwareGroup.add(hardwareOpenGLRadio);
		hardwareNoneRadio.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (hardwareNoneRadio.isSelected())
					hardwareFBObjectCheck.setEnabled(false);
				else
					hardwareFBObjectCheck.setEnabled(true);
			}
		});
		panel.add(hardwareNoneRadio);
		panel.add(hardwareOpenGLRadio);
		panel.add(hardwareFBObjectCheck);
		panel.add(new JLabel(Messages.getString("T_DIRECTX_IS_AUTOMATICALLY_DETECTED_AND_INITIALIZED"))); //$NON-NLS-1$
		panel.add(new JLabel(Messages.getString("T_APPLICATION_MUST_BE_RESTARTED_TO_APPLY_CHANGES"))); //$NON-NLS-1$
		generalPanel.add(panel);
		return generalPanel;
	}

	protected JPanel setWorldTab() {
		JPanel worldPanel = new JPanel();
		worldPanel.setPreferredSize(new Dimension(510, 100));
		worldPanel.setLayout(new BoxLayout(worldPanel,BoxLayout.Y_AXIS));
		// World size
		JPanel panel = new JPanel();
		JLabel label = new JLabel(Messages.getString("T_WIDTH")); //$NON-NLS-1$
		panel.add(label);
		widthText = new JTextField(Integer.toString(Utils.WORLD_WIDTH),6);
		panel.add(widthText);
		label = new JLabel(Messages.getString("T_HEIGHT")); //$NON-NLS-1$
		panel.add(label);
		heightText = new JTextField(Integer.toString(Utils.WORLD_HEIGHT),6);
		panel.add(heightText);
		worldPanel.add(panel);
		// Initial O2 - initial CO2
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_INITIAL_OXYGEN")); //$NON-NLS-1$
		panel.add(label);
		initialO2Text = new JTextField(Double.toString(Utils.INITIAL_O2),6);
		panel.add(initialO2Text);
		label = new JLabel(Messages.getString("T_INITIAL_CARBON_DIOXIDE")); //$NON-NLS-1$
		panel.add(label);
		initialCO2Text = new JTextField(Double.toString(Utils.INITIAL_CO2),6);
		panel.add(initialCO2Text);
		worldPanel.add(panel);
		// Initial CH4 -initial CO
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_INITIAL_METHANE")); //$NON-NLS-1$
		panel.add(label);
		initialCH4Text = new JTextField(Double.toString(Utils.INITIAL_CH4),6);
		panel.add(initialCH4Text);
		label = new JLabel(Messages.getString("T_INITIAL_CARBON_MONOXIDE")); //$NON-NLS-1$
		panel.add(label);
		initialCO1Text = new JTextField(Double.toString(Utils.INITIAL_CO1),6);
		panel.add(initialCO1Text);
		worldPanel.add(panel);
		// Initial Detritus
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_INITIAL_DETRITUS")); //$NON-NLS-1$
		panel.add(label);
		initialDetritusText = new JTextField(Double.toString(Utils.INITIAL_DETRITUS),6);
		panel.add(initialDetritusText);
		worldPanel.add(panel);
		// CO2 -> CH4 - CH4 -> CO2
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_CO2_TO_CH4_DIVISOR")); //$NON-NLS-1$
		panel.add(label);
		CO2toCH4divisorText = new JTextField(Integer.toString(Utils.CO2_TO_CH4_DIVISOR),6);
		panel.add(CO2toCH4divisorText);
		label = new JLabel(Messages.getString("T_CH4_TO_CO2_DIVISOR")); //$NON-NLS-1$
		panel.add(label);
		CH4toCO2divisorText = new JTextField(Integer.toString(Utils.CH4_TO_CO2_DIVISOR),6);
		panel.add(CH4toCO2divisorText);
		worldPanel.add(panel);
		// Detritus -> CO - CO -> CO2
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_DETRITUS_TO_CO1_DIVISOR")); //$NON-NLS-1$
		panel.add(label);
		detritustoCO1divisorText = new JTextField(Integer.toString(Utils.DETRITUS_TO_CO1_DIVISOR),6);
		panel.add(detritustoCO1divisorText);
		label = new JLabel(Messages.getString("T_CO1_TO_CO2_DIVISOR")); //$NON-NLS-1$
		panel.add(label);
		CO1toCO2divisorText = new JTextField(Integer.toString(Utils.CO1_TO_CO2_DIVISOR),6);
		panel.add(CO1toCO2divisorText);
		worldPanel.add(panel);
		// Rubbing - Elasticity
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_RUBBING_COEFFICIENT")); //$NON-NLS-1$
		panel.add(label);
		rubbingText = new JTextField(Double.toString(Utils.RUBBING),6);
		panel.add(rubbingText);
		worldPanel.add(panel);
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_ELASTICITY_COEFFICIENT")); //$NON-NLS-1$
		panel.add(label);
		elasticityText = new JTextField(Double.toString(Utils.ELASTICITY),6);
		panel.add(elasticityText);
		worldPanel.add(panel);

		return worldPanel;
	}

	protected JPanel setOrganismsTab() {
		JPanel organismsPanel = new JPanel();
		organismsPanel.setLayout(new BoxLayout(organismsPanel,BoxLayout.Y_AXIS));
		// Initial number - initial energy
		JPanel panel = new JPanel();
		JLabel label = new JLabel(Messages.getString("T_INITIAL_NUMBER")); //$NON-NLS-1$
		panel.add(label);
		initialnumberText = new JTextField(Integer.toString(Utils.INITIAL_ORGANISMS),6);
		panel.add(initialnumberText);
		label = new JLabel(Messages.getString("T_INITIAL_ENERGY")); //$NON-NLS-1$
		panel.add(label);
		initialenergyText = new JTextField(Integer.toString(Utils.INITIAL_ENERGY),6);
		panel.add(initialenergyText);
		organismsPanel.add(panel);
		// Initial complexity - Clade complexity
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_INITIAL_COMPLEXITY")); //$NON-NLS-1$
		panel.add(label);
		initialcomplexityText = new JTextField(Integer.toString(Utils.INITIAL_COMPLEXITY),6);
		panel.add(initialcomplexityText);
		label = new JLabel(Messages.getString("T_CLADE_COMPLEXITY")); //$NON-NLS-1$
		panel.add(label);
		cladecomplexityText = new JTextField(Integer.toString(Utils.CLADE_COMPLEXITY),6);
		panel.add(cladecomplexityText);
		organismsPanel.add(panel);
		// Meta Mutation rate
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_META_MUTATION_PERCENTAGE")); //$NON-NLS-1$
		panel.add(label);
		metamutationrateText = new JTextField(Integer.toString(Utils.META_MUTATION_RATE),6);
		panel.add(metamutationrateText);
		organismsPanel.add(panel);
		// Min Mutation rate - Max Mutation rate
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_MIN_MUTATION_PERCENTAGE")); //$NON-NLS-1$
		panel.add(label);
		minmutationrateText = new JTextField(Integer.toString(Utils.MIN_MUTATION_RATE),6);
		panel.add(minmutationrateText);
		label = new JLabel(Messages.getString("T_MAX_MUTATION_PERCENTAGE")); //$NON-NLS-1$
		panel.add(label);
		maxmutationrateText = new JTextField(Integer.toString(Utils.MAX_MUTATION_RATE),6);
		panel.add(maxmutationrateText);
		organismsPanel.add(panel);
		// Min Clone rate - Max Clone rate
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_MIN_CLONE_PERCENTAGE")); //$NON-NLS-1$
		panel.add(label);
		minclonerateText = new JTextField(Integer.toString(Utils.MIN_CLONE_RATE),6);
		panel.add(minclonerateText);
		label = new JLabel(Messages.getString("T_MAX_CLONE_PERCENTAGE")); //$NON-NLS-1$
		panel.add(label);
		maxclonerateText = new JTextField(Integer.toString(Utils.MAX_CLONE_RATE),6);
		panel.add(maxclonerateText);
		organismsPanel.add(panel);
		// Max age - Age divisor
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_LIFE_EXPECTANCY")); //$NON-NLS-1$
		panel.add(label);
		maxageText = new JTextField(Integer.toString(Utils.MAX_AGE),6);
		panel.add(maxageText);
		label = new JLabel(Messages.getString("T_LIFE_EXPECTANCY_DIVISOR")); //$NON-NLS-1$
		panel.add(label);
		agedivisorText = new JTextField(Integer.toString(Utils.AGE_DIVISOR),6);
		panel.add(agedivisorText);
		organismsPanel.add(panel);
		// Spore-1 velocity factor - Spore-5 velocity factor
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_SPORE1_VELOCITY")); //$NON-NLS-1$
		panel.add(label);
		spore1velocityText = new JTextField(Double.toString(Utils.SPORE1_VEL),6);
		panel.add(spore1velocityText);
		label = new JLabel(Messages.getString("T_SPORE5_VELOCITY")); //$NON-NLS-1$
		panel.add(label);
		spore5velocityText = new JTextField(Double.toString(Utils.SPORE5_VEL),6);
		panel.add(spore5velocityText);
		organismsPanel.add(panel);
		// Spore-10-12 velocity factor -  Reaction velocity factor
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_SPORE10-12_VELOCITY")); //$NON-NLS-1$
		panel.add(label);
		spore10velocityText = new JTextField(Double.toString(Utils.SPORE10_VEL),6);
		panel.add(spore10velocityText);
		label = new JLabel(Messages.getString("T_REACTION_VELOCITY")); //$NON-NLS-1$
		panel.add(label);
		reactionvelocityText = new JTextField(Double.toString(Utils.REACTION_VEL),6);
		panel.add(reactionvelocityText);
		organismsPanel.add(panel);
		// Upkeep cost
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_UPKEEP_COST_DIVISOR")); //$NON-NLS-1$
		panel.add(label);
		segmentcostText = new JTextField(Integer.toString(Utils.SEGMENT_COST_DIVISOR),6);
		panel.add(segmentcostText);
		organismsPanel.add(panel);
		// Drain substance
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_SUBSTANCE_DRAINAGE_DIVISOR")); //$NON-NLS-1$
		panel.add(label);
		drainText = new JTextField(Integer.toString(Utils.DRAIN_SUBS_DIVISOR),6);
		panel.add(drainText);
		organismsPanel.add(panel);
		// Decay energy
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_DECAY_ENERGY")); //$NON-NLS-1$
		panel.add(label);
		decayenergyText = new JTextField(Double.toString(Utils.DECAY_ENERGY),6);
		panel.add(decayenergyText);
		organismsPanel.add(panel);
		return organismsPanel;
	}

	protected JPanel setMetabolismTab() {
		JPanel metabolismPanel = new JPanel();
		metabolismPanel.setLayout(new BoxLayout(metabolismPanel,BoxLayout.Y_AXIS));
		// Photosynthetic metabolism
		JPanel panel = new JPanel();
		JLabel label = new JLabel(Messages.getString("T_PHOTOSYNTHETIC_METABOLISM")); //$NON-NLS-1$
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		metabolismPanel.add(label);
		metabolismPanel.add(Box.createVerticalStrut(10));
		// Obtained energy
		label = new JLabel(Messages.getString("T_OBTAINED_ENERGY_DIVISOR")); //$NON-NLS-1$
		panel.add(label);
		greenenergyText = new JTextField(Integer.toString(Utils.GREEN_OBTAINED_ENERGY_DIVISOR),6);
		panel.add(greenenergyText);
		metabolismPanel.add(panel);
		metabolismPanel.add(Box.createVerticalStrut(30));
		// Chemoorganotrophic metabolism
		label = new JLabel(Messages.getString("T_CHEMOORGANOTROPHIC_METABOLISM")); //$NON-NLS-1$
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		metabolismPanel.add(Box.createVerticalStrut(10));
		metabolismPanel.add(label);
		metabolismPanel.add(Box.createVerticalStrut(10));
		// Obtained energy
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_OBTAINED_ENERGY")); //$NON-NLS-1$
		panel.add(label);
		organicenergyText = new JTextField(Double.toString(Utils.ORGANIC_OBTAINED_ENERGY),6);
		panel.add(organicenergyText);
		metabolismPanel.add(panel);
		// Released energy
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_RELEASED_ENERGY_PROPORTION")); //$NON-NLS-1$
		panel.add(label);
		organicsubsText = new JTextField(Double.toString(Utils.ORGANIC_SUBS_PRODUCED),6);
		panel.add(organicsubsText);
		metabolismPanel.add(panel);
		// Released cream energy
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_CREAM_RELEASED_ENERGY_PROPORTION")); //$NON-NLS-1$
		panel.add(label);
		creamorganicsubsText = new JTextField(Double.toString(Utils.CREAM_ORGANIC_SUBS_PRODUCED),6);
		panel.add(creamorganicsubsText);
		metabolismPanel.add(panel);
		metabolismPanel.add(Box.createVerticalStrut(30));
		// Healing - Immune System
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_HEALING")); //$NON-NLS-1$
		panel.add(label);
		healingText = new JTextField(Integer.toString(Utils.HEALING),6);
		panel.add(healingText);
		label = new JLabel(Messages.getString("T_IMMUNE_SYSTEM")); //$NON-NLS-1$
		panel.add(label);
		immunesystemText = new JTextField(Integer.toString(Utils.IMMUNE_SYSTEM),6);
		panel.add(immunesystemText);
		metabolismPanel.add(panel);
		// Jade regeneration - Lavender shield
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_DARKJADE_DELAY")); //$NON-NLS-1$
		panel.add(label);
		darkjadedelayText = new JTextField(Integer.toString(Utils.DARKJADE_DELAY),6);
		panel.add(darkjadedelayText);
		label = new JLabel(Messages.getString("T_LAVENDER_SHIELD")); //$NON-NLS-1$
		panel.add(label);
		lavendershieldText = new JTextField(Integer.toString(Utils.LAVENDER_SHIELD),6);
		panel.add(lavendershieldText);
		metabolismPanel.add(panel);
		// Indigo Divisor - Dodge costs
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_INDIGO_DIVISOR")); //$NON-NLS-1$
		panel.add(label);
		indigodivisorText = new JTextField(Double.toString(Utils.INDIGO_DIVISOR),6);
		panel.add(indigodivisorText);
		label = new JLabel(Messages.getString("T_DODGE_ENERGY_CONSUMPTION")); //$NON-NLS-1$
		panel.add(label);
		dodgecostText = new JTextField(Double.toString(Utils.DODGE_ENERGY_CONSUMPTION),6);
		panel.add(dodgecostText);
		metabolismPanel.add(panel);
		// Spike consumer costs - Silver consumer costs
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_MOSQUITO_ENERGY_CONSUMPTION")); //$NON-NLS-1$
		panel.add(label);
		mosquitocostText = new JTextField(Double.toString(Utils.MOSQUITO_ENERGY_CONSUMPTION),6);
		panel.add(mosquitocostText);
		label = new JLabel(Messages.getString("T_EXPERIENCE_ENERGY_CONSUMPTION")); //$NON-NLS-1$
		panel.add(label);
		experiencecostText = new JTextField(Double.toString(Utils.EXPERIENCE_ENERGY_CONSUMPTION),6);
		panel.add(experiencecostText);
		metabolismPanel.add(panel);
		// Virus costs - Scourge costs
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_VIRUS_ENERGY_CONSUMPTION")); //$NON-NLS-1$
		panel.add(label);
		viruscostText = new JTextField(Double.toString(Utils.VIRUS_ENERGY_CONSUMPTION),6);
		panel.add(viruscostText);
		label = new JLabel(Messages.getString("T_SCOURGE_ENERGY_CONSUMPTION")); //$NON-NLS-1$
		panel.add(label);
		scourgecostText = new JTextField(Double.toString(Utils.SCOURGE_ENERGY_CONSUMPTION),6);
		panel.add(scourgecostText);
		metabolismPanel.add(panel);
		// Crowded lime costs - Crowded forest costs
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_CROWDEDLIME_ENERGY_CONSUMPTION")); //$NON-NLS-1$
		panel.add(label);
		crowdedlimecostText = new JTextField(Double.toString(Utils.CROWDEDLIME_ENERGY_CONSUMPTION),6);
		panel.add(crowdedlimecostText);
		label = new JLabel(Messages.getString("T_CROWDEDFOREST_ENERGY_CONSUMPTION")); //$NON-NLS-1$
		panel.add(label);
		crowdedforestcostText = new JTextField(Double.toString(Utils.CROWDEDFOREST_ENERGY_CONSUMPTION),6);
		panel.add(crowdedforestcostText);
		metabolismPanel.add(panel);
		// Symbiont costs - Modified leaf costs
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_SYMBIONT_ENERGY_CONSUMPTION")); //$NON-NLS-1$
		panel.add(label);
		symbiontcostText = new JTextField(Double.toString(Utils.SYMBIONT_ENERGY_CONSUMPTION),6);
		panel.add(symbiontcostText);
		label = new JLabel(Messages.getString("T_MODLEAF_ENERGY_CONSUMPTION")); //$NON-NLS-1$
		panel.add(label);
		modleafcostText = new JTextField(Double.toString(Utils.MODLEAF_ENERGY_CONSUMPTION),6);
		panel.add(modleafcostText);
		metabolismPanel.add(panel);

		return metabolismPanel;
	}

	protected JPanel setGenesTab() {
		JPanel genesPanel = new JPanel();
		genesPanel.setLayout(new GridLayout(17,3));
		JLabel label;

		genesPanel.add(new JLabel(Messages.getString("T_COLOR2"),SwingConstants.CENTER)); //$NON-NLS-1$
		genesPanel.add(new JLabel(Messages.getString("T_PROBABILITY"),SwingConstants.CENTER)); //$NON-NLS-1$
		genesPanel.add(new JLabel(Messages.getString("T_EFFECTIVITY"),SwingConstants.CENTER)); //$NON-NLS-1$

		label = new JLabel(Messages.getString("T_GREEN"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		greenprobText = new JTextField(Integer.toString(Utils.GREEN_PROB));
		genesPanel.add(greenprobText);
		greencostText = new JTextField(Double.toString(Utils.GREEN_ENERGY_CONSUMPTION));
		genesPanel.add(greencostText);

		label = new JLabel(Messages.getString("T_FOREST"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		forestprobText = new JTextField(Integer.toString(Utils.FOREST_PROB));
		genesPanel.add(forestprobText);
		forestcostText = new JTextField(Double.toString(Utils.FOREST_ENERGY_CONSUMPTION));
		genesPanel.add(forestcostText);

		label = new JLabel(Messages.getString("T_SPRING"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		springprobText = new JTextField(Integer.toString(Utils.SPRING_PROB));
		genesPanel.add(springprobText);
		springcostText = new JTextField(Double.toString(Utils.SPRING_ENERGY_CONSUMPTION));
		genesPanel.add(springcostText);

		label = new JLabel(Messages.getString("T_SUMMER"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		summerprobText = new JTextField(Integer.toString(Utils.SUMMER_PROB));
		genesPanel.add(summerprobText);
		summercostText = new JTextField(Double.toString(Utils.SUMMER_ENERGY_CONSUMPTION));
		genesPanel.add(summercostText);

		label = new JLabel(Messages.getString("T_LIME"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		limeprobText = new JTextField(Integer.toString(Utils.LIME_PROB));
		genesPanel.add(limeprobText);
		limecostText = new JTextField(Double.toString(Utils.LIME_ENERGY_CONSUMPTION));
		genesPanel.add(limecostText);

		label = new JLabel(Messages.getString("T_LEAF"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		leafprobText = new JTextField(Integer.toString(Utils.LEAF_PROB));
		genesPanel.add(leafprobText);
		leafcostText = new JTextField(Double.toString(Utils.LEAF_ENERGY_CONSUMPTION));
		genesPanel.add(leafcostText);

		label = new JLabel(Messages.getString("T_C4"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		c4probText = new JTextField(Integer.toString(Utils.C4_PROB));
		genesPanel.add(c4probText);
		c4costText = new JTextField(Double.toString(Utils.C4_ENERGY_CONSUMPTION));
		genesPanel.add(c4costText);

		label = new JLabel(Messages.getString("T_JADE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		jadeprobText = new JTextField(Integer.toString(Utils.JADE_PROB));
		genesPanel.add(jadeprobText);
		jadecostText = new JTextField(Double.toString(Utils.JADE_ENERGY_CONSUMPTION));
		genesPanel.add(jadecostText);

		label = new JLabel(Messages.getString("T_GRASS"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		grassprobText = new JTextField(Integer.toString(Utils.GRASS_PROB));
		genesPanel.add(grassprobText);
		grasscostText = new JTextField(Double.toString(Utils.GRASS_ENERGY_CONSUMPTION));
		genesPanel.add(grasscostText);

		label = new JLabel(Messages.getString("T_BARK"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		barkprobText = new JTextField(Integer.toString(Utils.BARK_PROB));
		genesPanel.add(barkprobText);
		barkcostText = new JTextField(Double.toString(Utils.BARK_ENERGY_CONSUMPTION));
		genesPanel.add(barkcostText);

		label = new JLabel(Messages.getString("T_PURPLE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		purpleprobText = new JTextField(Integer.toString(Utils.PURPLE_PROB));
		genesPanel.add(purpleprobText);
		purplecostText = new JTextField(Double.toString(Utils.PURPLE_ENERGY_CONSUMPTION));
		genesPanel.add(purplecostText);

		label = new JLabel(Messages.getString("T_PLANKTON"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		planktonprobText = new JTextField(Integer.toString(Utils.PLANKTON_PROB));
		genesPanel.add(planktonprobText);
		planktoncostText = new JTextField(Double.toString(Utils.PLANKTON_ENERGY_CONSUMPTION));
		genesPanel.add(planktoncostText);

		label = new JLabel(Messages.getString("T_DARKGRAY"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		darkgrayprobText = new JTextField(Integer.toString(Utils.DARKGRAY_PROB));
		genesPanel.add(darkgrayprobText);
		darkgraycostText = new JTextField(Double.toString(Utils.DARKGRAY_ENERGY_CONSUMPTION));
		genesPanel.add(darkgraycostText);

		label = new JLabel(Messages.getString("T_CYAN"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		cyanprobText = new JTextField(Integer.toString(Utils.CYAN_PROB));
		genesPanel.add(cyanprobText);
		cyancostText = new JTextField(Double.toString(Utils.CYAN_ENERGY_CONSUMPTION));
		genesPanel.add(cyancostText);

		label = new JLabel(Messages.getString("T_TEAL"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		tealprobText = new JTextField(Integer.toString(Utils.TEAL_PROB));
		genesPanel.add(tealprobText);
		tealcostText = new JTextField(Double.toString(Utils.TEAL_ENERGY_CONSUMPTION));
		genesPanel.add(tealcostText);

		label = new JLabel(Messages.getString("T_SPIN"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		spinprobText = new JTextField(Integer.toString(Utils.SPIN_PROB));
		genesPanel.add(spinprobText);
		spincostText = new JTextField(Double.toString(Utils.SPIN_ENERGY_CONSUMPTION));
		genesPanel.add(spincostText);

		return genesPanel;
	}

	protected JPanel setGenes2Tab() {
		JPanel genesPanel = new JPanel();
		genesPanel.setLayout(new GridLayout(18,3));
		JLabel label;

		genesPanel.add(new JLabel(Messages.getString("T_COLOR2"),SwingConstants.CENTER)); //$NON-NLS-1$
		genesPanel.add(new JLabel(Messages.getString("T_PROBABILITY"),SwingConstants.CENTER)); //$NON-NLS-1$
		genesPanel.add(new JLabel(Messages.getString("T_COST"),SwingConstants.CENTER)); //$NON-NLS-1$

		label = new JLabel(Messages.getString("T_RED"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		redprobText = new JTextField(Integer.toString(Utils.RED_PROB));
		genesPanel.add(redprobText);
		redcostText = new JTextField(Double.toString(Utils.RED_ENERGY_CONSUMPTION));
		genesPanel.add(redcostText);

		label = new JLabel(Messages.getString("T_FIRE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		fireprobText = new JTextField(Integer.toString(Utils.FIRE_PROB));
		genesPanel.add(fireprobText);
		firecostText = new JTextField(Double.toString(Utils.FIRE_ENERGY_CONSUMPTION));
		genesPanel.add(firecostText);

		label = new JLabel(Messages.getString("T_ORANGE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		orangeprobText = new JTextField(Integer.toString(Utils.ORANGE_PROB));
		genesPanel.add(orangeprobText);
		orangecostText = new JTextField(Double.toString(Utils.ORANGE_ENERGY_CONSUMPTION));
		genesPanel.add(orangecostText);

		label = new JLabel(Messages.getString("T_MAROON"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		maroonprobText = new JTextField(Integer.toString(Utils.MAROON_PROB));
		genesPanel.add(maroonprobText);
		marooncostText = new JTextField(Double.toString(Utils.MAROON_ENERGY_CONSUMPTION));
		genesPanel.add(marooncostText);

		label = new JLabel(Messages.getString("T_CRIMSON"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		crimsonprobText = new JTextField(Integer.toString(Utils.CRIMSON_PROB));
		genesPanel.add(crimsonprobText);
		crimsoncostText = new JTextField(Double.toString(Utils.CRIMSON_ENERGY_CONSUMPTION));
		genesPanel.add(crimsoncostText);

		label = new JLabel(Messages.getString("T_PINK"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		pinkprobText = new JTextField(Integer.toString(Utils.PINK_PROB));
		genesPanel.add(pinkprobText);
		pinkcostText = new JTextField(Double.toString(Utils.PINK_ENERGY_CONSUMPTION));
		genesPanel.add(pinkcostText);

		label = new JLabel(Messages.getString("T_CREAM"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		creamprobText = new JTextField(Integer.toString(Utils.CREAM_PROB));
		genesPanel.add(creamprobText);
		creamcostText = new JTextField(Double.toString(Utils.CREAM_ENERGY_CONSUMPTION));
		genesPanel.add(creamcostText);

		label = new JLabel(Messages.getString("T_SILVER"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		silverprobText = new JTextField(Integer.toString(Utils.SILVER_PROB));
		genesPanel.add(silverprobText);
		silvercostText = new JTextField(Double.toString(Utils.SILVER_ENERGY_CONSUMPTION));
		genesPanel.add(silvercostText);

		label = new JLabel(Messages.getString("T_SPIKE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		spikeprobText = new JTextField(Integer.toString(Utils.SPIKE_PROB));
		genesPanel.add(spikeprobText);
		spikecostText = new JTextField(Double.toString(Utils.SPIKE_ENERGY_CONSUMPTION));
		genesPanel.add(spikecostText);

		label = new JLabel(Messages.getString("T_LILAC"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		lilacprobText = new JTextField(Integer.toString(Utils.LILAC_PROB));
		genesPanel.add(lilacprobText);
		lilaccostText = new JTextField(Double.toString(Utils.LILAC_ENERGY_CONSUMPTION));
		genesPanel.add(lilaccostText);

		label = new JLabel(Messages.getString("T_GRAY"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		grayprobText = new JTextField(Integer.toString(Utils.GRAY_PROB));
		genesPanel.add(grayprobText);
		graycostText = new JTextField(Double.toString(Utils.GRAY_ENERGY_CONSUMPTION));
		genesPanel.add(graycostText);

		label = new JLabel(Messages.getString("T_VIOLET"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		violetprobText = new JTextField(Integer.toString(Utils.VIOLET_PROB));
		genesPanel.add(violetprobText);
		violetcostText = new JTextField(Double.toString(Utils.VIOLET_ENERGY_CONSUMPTION));
		genesPanel.add(violetcostText);

		label = new JLabel(Messages.getString("T_OLIVE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		oliveprobText = new JTextField(Integer.toString(Utils.OLIVE_PROB));
		genesPanel.add(oliveprobText);
		olivecostText = new JTextField(Double.toString(Utils.OLIVE_ENERGY_CONSUMPTION));
		genesPanel.add(olivecostText);

		label = new JLabel(Messages.getString("T_SKY"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		skyprobText = new JTextField(Integer.toString(Utils.SKY_PROB));
		genesPanel.add(skyprobText);
		skycostText = new JTextField(Double.toString(Utils.SKY_ENERGY_CONSUMPTION));
		genesPanel.add(skycostText);

		label = new JLabel(Messages.getString("T_BLUE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		blueprobText = new JTextField(Integer.toString(Utils.BLUE_PROB));
		genesPanel.add(blueprobText);
		bluecostText = new JTextField(Double.toString(Utils.BLUE_ENERGY_CONSUMPTION));
		genesPanel.add(bluecostText);

		label = new JLabel(Messages.getString("T_OCHRE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		ochreprobText = new JTextField(Integer.toString(Utils.OCHRE_PROB));
		genesPanel.add(ochreprobText);
		ochrecostText = new JTextField(Double.toString(Utils.OCHRE_ENERGY_CONSUMPTION));
		genesPanel.add(ochrecostText);

		label = new JLabel(Messages.getString("T_FALLOW"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		fallowprobText = new JTextField(Integer.toString(Utils.FALLOW_PROB));
		genesPanel.add(fallowprobText);
		fallowcostText = new JTextField(Double.toString(Utils.FALLOW_ENERGY_CONSUMPTION));
		genesPanel.add(fallowcostText);

		return genesPanel;
	}

	protected JPanel setGenes3Tab() {
		JPanel genesPanel = new JPanel();
		genesPanel.setLayout(new GridLayout(17,3));
		JLabel label;

		genesPanel.add(new JLabel(Messages.getString("T_COLOR2"),SwingConstants.CENTER)); //$NON-NLS-1$
		genesPanel.add(new JLabel(Messages.getString("T_PROBABILITY"),SwingConstants.CENTER)); //$NON-NLS-1$
		genesPanel.add(new JLabel(Messages.getString("T_COST"),SwingConstants.CENTER)); //$NON-NLS-1$

		label = new JLabel(Messages.getString("T_SPORE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		sporeprobText = new JTextField(Integer.toString(Utils.SPORE_PROB));
		genesPanel.add(sporeprobText);
		sporecostText = new JTextField(Double.toString(Utils.SPORE_ENERGY_CONSUMPTION));
		genesPanel.add(sporecostText);

		label = new JLabel(Messages.getString("T_WHITE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		whiteprobText = new JTextField(Integer.toString(Utils.WHITE_PROB));
		genesPanel.add(whiteprobText);
		whitecostText = new JTextField(Double.toString(Utils.WHITE_ENERGY_CONSUMPTION));
		genesPanel.add(whitecostText);

		label = new JLabel(Messages.getString("T_PLAGUE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		plagueprobText = new JTextField(Integer.toString(Utils.PLAGUE_PROB));
		genesPanel.add(plagueprobText);
		plaguecostText = new JTextField(Double.toString(Utils.PLAGUE_ENERGY_CONSUMPTION));
		genesPanel.add(plaguecostText);

		label = new JLabel(Messages.getString("T_CORAL"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		coralprobText = new JTextField(Integer.toString(Utils.CORAL_PROB));
		genesPanel.add(coralprobText);
		coralcostText = new JTextField(Double.toString(Utils.CORAL_ENERGY_CONSUMPTION));
		genesPanel.add(coralcostText);

		label = new JLabel(Messages.getString("T_MINT"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		mintprobText = new JTextField(Integer.toString(Utils.MINT_PROB));
		genesPanel.add(mintprobText);
		mintcostText = new JTextField(Double.toString(Utils.MINT_ENERGY_CONSUMPTION));
		genesPanel.add(mintcostText);

		label = new JLabel(Messages.getString("T_LAVENDER"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		lavenderprobText = new JTextField(Integer.toString(Utils.LAVENDER_PROB));
		genesPanel.add(lavenderprobText);
		lavendercostText = new JTextField(Double.toString(Utils.LAVENDER_ENERGY_CONSUMPTION));
		genesPanel.add(lavendercostText);

		label = new JLabel(Messages.getString("T_MAGENTA"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		magentaprobText = new JTextField(Integer.toString(Utils.MAGENTA_PROB));
		genesPanel.add(magentaprobText);
		magentacostText = new JTextField(Double.toString(Utils.MAGENTA_ENERGY_CONSUMPTION));
		genesPanel.add(magentacostText);

		label = new JLabel(Messages.getString("T_ROSE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		roseprobText = new JTextField(Integer.toString(Utils.ROSE_PROB));
		genesPanel.add(roseprobText);
		rosecostText = new JTextField(Double.toString(Utils.ROSE_ENERGY_CONSUMPTION));
		genesPanel.add(rosecostText);

		label = new JLabel(Messages.getString("T_YELLOW"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		yellowprobText = new JTextField(Integer.toString(Utils.YELLOW_PROB));
		genesPanel.add(yellowprobText);
		yellowcostText = new JTextField(Double.toString(Utils.YELLOW_ENERGY_CONSUMPTION));
		genesPanel.add(yellowcostText);

		label = new JLabel(Messages.getString("T_AUBURN"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		auburnprobText = new JTextField(Integer.toString(Utils.AUBURN_PROB));
		genesPanel.add(auburnprobText);
		auburncostText = new JTextField(Double.toString(Utils.AUBURN_ENERGY_CONSUMPTION));
		genesPanel.add(auburncostText);

		label = new JLabel(Messages.getString("T_INDIGO"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		indigoprobText = new JTextField(Integer.toString(Utils.INDIGO_PROB));
		genesPanel.add(indigoprobText);
		indigocostText = new JTextField(Double.toString(Utils.INDIGO_ENERGY_CONSUMPTION));
		genesPanel.add(indigocostText);

		label = new JLabel(Messages.getString("T_BLOND"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		blondprobText = new JTextField(Integer.toString(Utils.BLOND_PROB));
		genesPanel.add(blondprobText);
		blondcostText = new JTextField(Double.toString(Utils.BLOND_ENERGY_CONSUMPTION));
		genesPanel.add(blondcostText);

		label = new JLabel(Messages.getString("T_FLOWER"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		flowerprobText = new JTextField(Integer.toString(Utils.FLOWER_PROB));
		genesPanel.add(flowerprobText);
		flowercostText = new JTextField(Double.toString(Utils.FLOWER_ENERGY_CONSUMPTION));
		genesPanel.add(flowercostText);

		label = new JLabel(Messages.getString("T_GOLD"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		goldprobText = new JTextField(Integer.toString(Utils.GOLD_PROB));
		genesPanel.add(goldprobText);
		goldcostText = new JTextField(Double.toString(Utils.GOLD_DIVISOR));
		genesPanel.add(goldcostText);

		label = new JLabel(Messages.getString("T_DARK"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		darkprobText = new JTextField(Integer.toString(Utils.DARK_PROB));
		genesPanel.add(darkprobText);
		darkcostText = new JTextField(Double.toString(Utils.DARK_ENERGY_CONSUMPTION));
		genesPanel.add(darkcostText);

		label = new JLabel(Messages.getString("T_EYE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		eyeprobText = new JTextField(Integer.toString(Utils.EYE_PROB));
		genesPanel.add(eyeprobText);
		eyecostText = new JTextField(Double.toString(Utils.EYE_ENERGY_CONSUMPTION));
		genesPanel.add(eyecostText);

		return genesPanel;
	}

	void checkParams() {
		int i;
		double d;
		if (Messages.getLocaleIndex() != localeCombo.getSelectedIndex()) {
			Messages.setLocale(localeCombo.getSelectedIndex());
			mainWindow.changeLocale();
		}
		try {
			i = Integer.parseInt(widthText.getText());
			if (i > 0) Utils.WORLD_WIDTH = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(heightText.getText());
			if (i > 0) Utils.WORLD_HEIGHT = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(delayText.getText());
			if (i > 0) {
				Utils.DELAY = i;
			}
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(threadCountText.getText());
			if (i > 0) {
				Utils.THREAD_COUNT = i;
			}
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		Utils.AUTO_BACKUP = autoBackupsCheck.isSelected();
		Utils.AUTO_SAVE = autoSaveCheck.isSelected();
		try {
			i = Integer.parseInt(saveDelayText.getText());
			if (i > 0) {
				Utils.SAVE_DELAY = i;
			}
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		Utils.COMPRESS_BACKUPS = compressBackupsCheck.isSelected();
		Utils.AUTO_BACKUP_CSV = autoBackupsCSVCheck.isSelected();
		Utils.AUTO_BACKUP_WORLD_PNG = autoBackupsWorldPngCheck.isSelected();
		Utils.AUTO_BACKUP_STATISTICS_PNG = autoBackupsStatisticsPngCheck.isSelected();
		Utils.AUTO_BACKUP_CLADES_PNG = autoBackupsCladesPngCheck.isSelected();
		Utils.AUTO_BACKUP_IMAGES_AS_FOLDERS = autoBackupsImagesAsFoldersCheck.isSelected();
		try {
			i = Integer.parseInt(backupDelayText.getText());
			if (i > 0) {
				Utils.BACKUP_DELAY = i;
			}
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(initialO2Text.getText());
			if (d >= 0) Utils.INITIAL_O2 = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(initialCO2Text.getText());
			if (d >= 0) Utils.INITIAL_CO2 = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(initialCO1Text.getText());
			if (d >= 0) Utils.INITIAL_CO1 = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(initialCH4Text.getText());
			if (d >= 0) Utils.INITIAL_CH4 = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(initialDetritusText.getText());
			if (d >= 0) Utils.INITIAL_DETRITUS = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(rubbingText.getText());
			if (d >= 0 && d <= 1) Utils.RUBBING = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(elasticityText.getText());
			if (d >= 0 && d <= 1) Utils.ELASTICITY = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(initialnumberText.getText());
			if (i >= 0) Utils.INITIAL_ORGANISMS = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(initialenergyText.getText());
			if (i >= 0) Utils.INITIAL_ENERGY = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(initialcomplexityText.getText());
			if (i > 0) Utils.INITIAL_COMPLEXITY = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(cladecomplexityText.getText());
			if (i >= -1) Utils.CLADE_COMPLEXITY = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(maxageText.getText());
			if (i >= 0) Utils.MAX_AGE = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(agedivisorText.getText());
			if (i > 0) Utils.AGE_DIVISOR = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(CO2toCH4divisorText.getText());
			if (i > 0) Utils.CO2_TO_CH4_DIVISOR = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(CH4toCO2divisorText.getText());
			if (i > 0) Utils.CH4_TO_CO2_DIVISOR = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(detritustoCO1divisorText.getText());
			if (i > 0) Utils.DETRITUS_TO_CO1_DIVISOR = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(CO1toCO2divisorText.getText());
			if (i > 0) Utils.CO1_TO_CO2_DIVISOR = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(metamutationrateText.getText());
			if (i >= 0 && i <= 10000) Utils.META_MUTATION_RATE = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(maxmutationrateText.getText());
			if (i >= 0 && i <= 10000) Utils.MAX_MUTATION_RATE = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(minmutationrateText.getText());
			if (i >= 0 && i <= Utils.MAX_MUTATION_RATE) Utils.MIN_MUTATION_RATE = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(maxclonerateText.getText());
			if (i >= 0 && i <= 100) Utils.MAX_CLONE_RATE = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(minclonerateText.getText());
			if (i >= 0 && i <= Utils.MAX_CLONE_RATE) Utils.MIN_CLONE_RATE = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(reactionvelocityText.getText());
			if (d >= 0) Utils.REACTION_VEL = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(spore1velocityText.getText());
			if (d >= 0) Utils.SPORE1_VEL = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(spore5velocityText.getText());
			if (d >= 0) Utils.SPORE5_VEL = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(spore10velocityText.getText());
			if (d >= 0) Utils.SPORE10_VEL = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(redcostText.getText());
			if (d >= 0) Utils.RED_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(greencostText.getText());
			if (d > 0) Utils.GREEN_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(bluecostText.getText());
			if (d >= 0) Utils.BLUE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(cyancostText.getText());
			if (d >= 0) Utils.CYAN_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(whitecostText.getText());
			if (d >= 0) Utils.WHITE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(graycostText.getText());
			if (d >= 0) Utils.GRAY_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(yellowcostText.getText());
			if (d >= 0) Utils.YELLOW_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(magentacostText.getText());
			if (d >= 0) Utils.MAGENTA_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(pinkcostText.getText());
			if (d >= 0) Utils.PINK_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(coralcostText.getText());
			if (d >= 0) Utils.CORAL_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(orangecostText.getText());
			if (d >= 0) Utils.ORANGE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(forestcostText.getText());
			if (d > 0) Utils.FOREST_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(springcostText.getText());
			if (d > 0) Utils.SPRING_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(leafcostText.getText());
			if (d > 0) Utils.LEAF_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(summercostText.getText());
			if (d > 0) Utils.SUMMER_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(limecostText.getText());
			if (d > 0) Utils.LIME_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(c4costText.getText());
			if (d > 0) Utils.C4_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(jadecostText.getText());
			if (d > 0) Utils.JADE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(grasscostText.getText());
			if (d > 0) Utils.GRASS_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(purplecostText.getText());
			if (d > 0) Utils.PURPLE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(planktoncostText.getText());
			if (d > 0) Utils.PLANKTON_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(barkcostText.getText());
			if (d > 0) Utils.BARK_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(violetcostText.getText());
			if (d >= 0) Utils.VIOLET_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(tealcostText.getText());
			if (d >= 0) Utils.TEAL_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(spincostText.getText());
			if (d >= 0) Utils.SPIN_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(eyecostText.getText());
			if (d >= 0) Utils.EYE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(marooncostText.getText());
			if (d > 0) Utils.MAROON_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(crimsoncostText.getText());
			if (d > 0) Utils.CRIMSON_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(olivecostText.getText());
			if (d >= 0) Utils.OLIVE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(mintcostText.getText());
			if (d >= 0) Utils.MINT_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(creamcostText.getText());
			if (d >= 0) Utils.CREAM_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(rosecostText.getText());
			if (d > 0) Utils.ROSE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(darkcostText.getText());
			if (d >= 0) Utils.DARK_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(ochrecostText.getText());
			if (d >= 0) Utils.OCHRE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(skycostText.getText());
			if (d >= 0) Utils.SKY_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(lilaccostText.getText());
			if (d >= 0) Utils.LILAC_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(silvercostText.getText());
			if (d >= 0) Utils.SILVER_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(firecostText.getText());
			if (d >= 0) Utils.FIRE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(darkgraycostText.getText());
			if (d >= 0) Utils.DARKGRAY_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(goldcostText.getText());
			if (d > 0) Utils.GOLD_DIVISOR = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(blondcostText.getText());
			if (d >= 0) Utils.BLOND_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(flowercostText.getText());
			if (d > 0) Utils.FLOWER_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(auburncostText.getText());
			if (d >= 0) Utils.AUBURN_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(plaguecostText.getText());
			if (d >= 0) Utils.PLAGUE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(spikecostText.getText());
			if (d >= 0) Utils.SPIKE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(indigocostText.getText());
			if (d >= 1) Utils.INDIGO_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(lavendercostText.getText());
			if (d > 0) Utils.LAVENDER_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(fallowcostText.getText());
			if (d >= 0) Utils.FALLOW_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(sporecostText.getText());
			if (d >= 0) Utils.SPORE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(redprobText.getText());
			if (i >= 0) Utils.RED_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(greenprobText.getText());
			if (i >= 0) Utils.GREEN_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(blueprobText.getText());
			if (i >= 0) Utils.BLUE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(cyanprobText.getText());
			if (i >= 0) Utils.CYAN_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(whiteprobText.getText());
			if (i >= 0) Utils.WHITE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(grayprobText.getText());
			if (i >= 0) Utils.GRAY_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(yellowprobText.getText());
			if (i >= 0) Utils.YELLOW_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(magentaprobText.getText());
			if (i >= 0) Utils.MAGENTA_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(pinkprobText.getText());
			if (i >= 0) Utils.PINK_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(coralprobText.getText());
			if (i >= 0) Utils.CORAL_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(orangeprobText.getText());
			if (i >= 0) Utils.ORANGE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(forestprobText.getText());
			if (i >= 0) Utils.FOREST_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(springprobText.getText());
			if (i >= 0) Utils.SPRING_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(leafprobText.getText());
			if (i >= 0) Utils.LEAF_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(summerprobText.getText());
			if (i >= 0) Utils.SUMMER_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(limeprobText.getText());
			if (i >= 0) Utils.LIME_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(c4probText.getText());
			if (i >= 0) Utils.C4_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(jadeprobText.getText());
			if (i >= 0) Utils.JADE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(grassprobText.getText());
			if (i >= 0) Utils.GRASS_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(purpleprobText.getText());
			if (i >= 0) Utils.PURPLE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(planktonprobText.getText());
			if (i >= 0) Utils.PLANKTON_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(barkprobText.getText());
			if (i >= 0) Utils.BARK_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(violetprobText.getText());
			if (i >= 0) Utils.VIOLET_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(tealprobText.getText());
			if (i >= 0) Utils.TEAL_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(spinprobText.getText());
			if (i >= 0) Utils.SPIN_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(eyeprobText.getText());
			if (i >= 0) Utils.EYE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(maroonprobText.getText());
			if (i >= 0) Utils.MAROON_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(crimsonprobText.getText());
			if (i >= 0) Utils.CRIMSON_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(oliveprobText.getText());
			if (i >= 0) Utils.OLIVE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(mintprobText.getText());
			if (i >= 0) Utils.MINT_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(creamprobText.getText());
			if (i >= 0) Utils.CREAM_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(roseprobText.getText());
			if (i >= 0) Utils.ROSE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(darkprobText.getText());
			if (i >= 0) Utils.DARK_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(ochreprobText.getText());
			if (i >= 0) Utils.OCHRE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(skyprobText.getText());
			if (i >= 0) Utils.SKY_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(lilacprobText.getText());
			if (i >= 0) Utils.LILAC_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(silverprobText.getText());
			if (i >= 0) Utils.SILVER_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(fireprobText.getText());
			if (i >= 0) Utils.FIRE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(darkgrayprobText.getText());
			if (i >= 0) Utils.DARKGRAY_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(goldprobText.getText());
			if (i >= 0) Utils.GOLD_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(blondprobText.getText());
			if (i >= 0) Utils.BLOND_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(flowerprobText.getText());
			if (i >= 0) Utils.FLOWER_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(auburnprobText.getText());
			if (i >= 0) Utils.AUBURN_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(plagueprobText.getText());
			if (i >= 0) Utils.PLAGUE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(spikeprobText.getText());
			if (i >= 0) Utils.SPIKE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(indigoprobText.getText());
			if (i >= 0) Utils.INDIGO_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(lavenderprobText.getText());
			if (i >= 0) Utils.LAVENDER_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(fallowprobText.getText());
			if (i >= 0) Utils.FALLOW_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(sporeprobText.getText());
			if (i >= 0) Utils.SPORE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(segmentcostText.getText());
			if (i > 0) Utils.SEGMENT_COST_DIVISOR = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(drainText.getText());
			if (i > 0) Utils.DRAIN_SUBS_DIVISOR = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}

		try {
			i = Integer.parseInt(greenenergyText.getText());
			if (i > 0) Utils.GREEN_OBTAINED_ENERGY_DIVISOR = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(organicenergyText.getText());
			if (d >= 0) Utils.ORGANIC_OBTAINED_ENERGY = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(organicsubsText.getText());
			if (d >= 0 && d <= 1) Utils.ORGANIC_SUBS_PRODUCED = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(creamorganicsubsText.getText());
			if (d >= 0 && d <= 1) Utils.CREAM_ORGANIC_SUBS_PRODUCED = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(healingText.getText());
			if (i >= 1) Utils.HEALING = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(immunesystemText.getText());
			if (i >= 1) Utils.IMMUNE_SYSTEM = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(lavendershieldText.getText());
			if (i >= 0) Utils.LAVENDER_SHIELD = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(indigodivisorText.getText());
			if (d > 0) Utils.INDIGO_DIVISOR = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(mosquitocostText.getText());
			if (d >= 0) Utils.MOSQUITO_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(crowdedlimecostText.getText());
			if (d > 0) Utils.CROWDEDLIME_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(crowdedforestcostText.getText());
			if (d > 0) Utils.CROWDEDFOREST_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(modleafcostText.getText());
			if (d > 0) Utils.MODLEAF_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(symbiontcostText.getText());
			if (d > 0) Utils.SYMBIONT_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(experiencecostText.getText());
			if (d >= 0) Utils.EXPERIENCE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(dodgecostText.getText());
			if (d >= 0) Utils.DODGE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(darkjadedelayText.getText());
			if (i >= 1) Utils.DARKJADE_DELAY = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(viruscostText.getText());
			if (d >= 0) Utils.VIRUS_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(scourgecostText.getText());
			if (d >= 0) Utils.SCOURGE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(decayenergyText.getText());
			if (d >= 0) Utils.DECAY_ENERGY = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		if (hardwareNoneRadio.isSelected()) {
			Utils.setHardwareAcceleration(0);
		}
		if (hardwareOpenGLRadio.isSelected()) {
			if (hardwareFBObjectCheck.isSelected()) {
				if (Utils.HARDWARE_ACCELERATION < 4)
					Utils.setHardwareAcceleration(4);
			} else {
				if (Utils.HARDWARE_ACCELERATION != 0 && Utils.HARDWARE_ACCELERATION > 3)
					Utils.setHardwareAcceleration(1);
			}
		}
	}
}
