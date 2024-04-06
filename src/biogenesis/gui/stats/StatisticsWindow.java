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
package biogenesis.gui.stats;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import biogenesis.AppFocusWindowAdapter;
import biogenesis.GeneticCode;
import biogenesis.Messages;
import biogenesis.Organism;
import biogenesis.Utils;
import biogenesis.VisibleWorld;
import biogenesis.WindowManager;
import biogenesis.World;
import biogenesis.WorldStatistics;

/**
 * Window that shows the statistics of the world and remarkable organisms.
 */
public class StatisticsWindow extends JDialog {
	private static final long serialVersionUID = Utils.FILE_VERSION;

	private final NumberFormat nf = NumberFormat.getInstance();

	private final World world;
	private final VisibleWorld visibleWorld;
	private final WorldStatistics worldStatistics;
	private final Collection<Organism> organisms;

	private GraphPanel populationGraphPanel;
	private GraphInfo deathsGraph;
	private GraphInfo birthsGraph;
	private GraphInfo populationGraph;
	private GraphInfo cladesGraph;

	private GraphPanel cladesGraphPanel;
	private GraphInfo cladesGraph2;
	private GraphInfo populationGraph2;

	private PopulationStatsPanel populationStatsPanel;

	private GraphPanel atmosphereGraphPanel;
	private GraphInfo oxygenGraph;
	private GraphInfo carbonDioxideGraph;
	private GraphInfo carbonMonoxideGraph;
	private GraphInfo methaneGraph;
	private GraphInfo detritusGraph;

	private AtmosphereStatsPanel atmosphereStatsPanel;

	private ValueAndTimeLabel currentStateTimeLabel;
	private ValueAndTimeLabel currentStateOxygenLabel;
	private ValueAndTimeLabel currentStateCladesLabel;
	private ValueAndTimeLabel currentStateCarbonDioxideLabel;
	private ValueAndTimeLabel currentStateCarbonMonoxideLabel;
	private ValueAndTimeLabel currentStateCh4Label;
	private ValueAndTimeLabel currentStateDetritusLabel;
	private ValueAndTimeLabel currentStatePopulationLabel;
	private ValueAndTimeLabel currentStateRemainsOfBeingsLabel;
	private ValueAndTimeLabel currentStateTotalMassLabel;
	private ValueAndTimeLabel currentStateTotalEnergyLabel;
	private ColorPanel colorPanel;

	private RemarkableOrganismPanel aliveMostChildrenPanel;
	private RemarkableOrganismPanel mostChildrenPanel;
	private RemarkableOrganismPanel aliveMostKillsPanel;
	private RemarkableOrganismPanel mostKillsPanel;
	private RemarkableOrganismPanel aliveMostInfectionsPanel;
	private RemarkableOrganismPanel mostInfectionsPanel;
	private RemarkableOrganismPanel aliveMostMassPanel;
	private RemarkableOrganismPanel aliveOldestPanel;
	private RemarkableOrganismPanel aliveLowestGenerationPanel;
	private RemarkableOrganismPanel aliveHighestGenerationPanel;

	private GraphPanel generationHistogramPanel;

	private GraphInfo generationHistogram;

	public StatisticsWindow(JFrame owner, World world, VisibleWorld visibleWorld, WorldStatistics ws,
			Collection<Organism> os) {
		super((JDialog) null, false);
		this.world = world;
		this.visibleWorld = visibleWorld;
		worldStatistics = ws;
		organisms = os;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(Messages.getString("T_STATISTICS")); //$NON-NLS-1$

		nf.setMaximumFractionDigits(1);

		initComponents();
		setResizable(true);
		Dimension minSize = getSize();
		setPreferredSize(new Dimension(minSize.width + 50, minSize.height));
		setSize(new Dimension(minSize.width + 50, minSize.height));
		setMinimumSize(new Dimension(minSize.width, 600));

		Timer timer = new Timer(1000 / Utils.STATISTICS_REFRESH_FPS, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Utils.isAppInFocus()) {
					repaintStats();
				}
			}
		});
		timer.start();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				timer.stop();
			}
		});
		addWindowListener(new AppFocusWindowAdapter());

		setVisible(true);
		WindowManager.registerWindow(this, getWidth(), getHeight(), 0, 0);
	}

	public void repaintStats() {
		updatePerformed(new ActionEvent(this, 0, ""));
	}

	private void initComponents() {
		// Population graphic
		deathsGraph = new GraphInfo(0, 0, 100, 104, Color.RED, Messages.getString("T_DEATHS")); //$NON-NLS-1$
		birthsGraph = new GraphInfo(0, 0, 100, 104, Color.GREEN, Messages.getString("T_BIRTHS")); //$NON-NLS-1$
		populationGraph = new GraphInfo(0, 0, 100, 104, Color.WHITE, Messages.getString("T_POPULATION")); //$NON-NLS-1$
		cladesGraph = new GraphInfo(0, 0, 100, 104, Color.ORANGE, Messages.getString("T_CLADES")); //$NON-NLS-1$
		populationGraphPanel = new GraphPanel(100, 104, nf);
		populationGraphPanel.addGraph(deathsGraph);
		populationGraphPanel.addGraph(birthsGraph);
		populationGraphPanel.addGraph(populationGraph);
		populationGraphPanel.addGraph(cladesGraph);

		// Clades graphic
		// Population graph is added here too to make both clade graphs align
		populationGraph2 = new GraphInfo(0, 0, 100, 104, Color.BLACK, Messages.getString("T_POPULATION")); //$NON-NLS-1$
		cladesGraph2 = new GraphInfo(0, 0, 100, 104, Color.ORANGE, Messages.getString("T_CLADES")); //$NON-NLS-1$
		cladesGraphPanel = new GraphPanel(100, 52, nf);
		cladesGraphPanel.addGraph(populationGraph2);
		cladesGraphPanel.addGraph(cladesGraph2);

		// Population statistics
		populationStatsPanel = new PopulationStatsPanel(worldStatistics, nf);
		populationStatsPanel.update();

		// Population = population graph + population stats
		JPanel populationPanel = new JPanel();
		GroupLayout populationPanelLayout = new GroupLayout(populationPanel);
		populationPanel.setLayout(populationPanelLayout);
		populationPanelLayout.setAutoCreateGaps(true);
		populationPanelLayout.setAutoCreateContainerGaps(true);
		populationPanelLayout.setHorizontalGroup(
				populationPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(cladesGraphPanel)
						.addComponent(populationGraphPanel)
						.addComponent(populationStatsPanel));
		populationPanelLayout.setVerticalGroup(
				populationPanelLayout.createSequentialGroup()
						.addComponent(cladesGraphPanel)
						.addComponent(populationGraphPanel)
						.addComponent(populationStatsPanel));
		Border title = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
				Messages.getString("T_POPULATION"), TitledBorder.LEFT, TitledBorder.TOP); //$NON-NLS-1$
		populationPanel.setBorder(title);

		// Atmosphere graphic
		oxygenGraph = new GraphInfo(0, 0, 100, 104, Color.BLUE, Messages.getString("T_OXYGEN")); //$NON-NLS-1$
		carbonDioxideGraph = new GraphInfo(0, 0, 100, 104, Color.WHITE, Messages.getString("T_CARBON_DIOXIDE")); //$NON-NLS-1$
		carbonMonoxideGraph = new GraphInfo(0, 0, 100, 104, Color.GRAY, Messages.getString("T_CARBON_MONOXIDE")); //$NON-NLS-1$
		methaneGraph = new GraphInfo(0, 0, 100, 104, Color.MAGENTA, Messages.getString("T_METHANE")); //$NON-NLS-1$
		detritusGraph = new GraphInfo(0, 0, 100, 104, Color.YELLOW, Messages.getString("T_DETRITUS")); //$NON-NLS-1$
		atmosphereGraphPanel = new GraphPanel(100, 105, nf);
		atmosphereGraphPanel.addGraph(oxygenGraph);
		atmosphereGraphPanel.addGraph(carbonDioxideGraph);
		atmosphereGraphPanel.addGraph(carbonMonoxideGraph);
		atmosphereGraphPanel.addGraph(methaneGraph);
		atmosphereGraphPanel.addGraph(detritusGraph);

		// Atmosphere statistics
		atmosphereStatsPanel = new AtmosphereStatsPanel(worldStatistics, nf);
		atmosphereStatsPanel.update();

		// Population = population graph + population stats
		JPanel atmospherePanel = new JPanel();
		GroupLayout atmospherePanelLayout = new GroupLayout(atmospherePanel);
		atmospherePanel.setLayout(atmospherePanelLayout);
		atmospherePanelLayout.setHorizontalGroup(
				atmospherePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(atmosphereGraphPanel)
						.addComponent(atmosphereStatsPanel));
		atmospherePanelLayout.setVerticalGroup(
				atmospherePanelLayout.createSequentialGroup()
						.addComponent(atmosphereGraphPanel)
						.addComponent(atmosphereStatsPanel));
		title = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
				Messages.getString("T_ATMOSPHERE"), TitledBorder.LEFT, TitledBorder.TOP); //$NON-NLS-1$
		atmospherePanel.setBorder(title);

		// World history: population + atmosphere + other graps
		JPanel worldHistoryPanel = new JPanel();
		GroupLayout worldHistoryPanelLayout = new GroupLayout(worldHistoryPanel);
		worldHistoryPanel.setLayout(worldHistoryPanelLayout);
		worldHistoryPanelLayout.setHorizontalGroup(
				worldHistoryPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(populationPanel)
						.addComponent(atmospherePanel));
		worldHistoryPanelLayout.setVerticalGroup(
				worldHistoryPanelLayout.createSequentialGroup()
						.addComponent(populationPanel)
						.addComponent(atmospherePanel)
						.addGap(0, 0, Short.MAX_VALUE));
		title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				Messages.getString("T_WORLD_HISTORY"), TitledBorder.LEFT, TitledBorder.TOP); //$NON-NLS-1$
		worldHistoryPanel.setBorder(title);

		// Current state
		currentStateTimeLabel = new ValueAndTimeLabel(Messages.getString("T_TIME"), null, nf); //$NON-NLS-1$
		currentStateOxygenLabel = new ValueAndTimeLabel(Messages.getString("T_OXYGEN2"), null, nf); //$NON-NLS-1$
		currentStateCladesLabel = new ValueAndTimeLabel(Messages.getString("T_CLADES2"), null, nf); //$NON-NLS-1$
		currentStateCarbonDioxideLabel = new ValueAndTimeLabel(Messages.getString("T_CARBON_DIOXIDE2"), null, nf); //$NON-NLS-1$
		currentStateCarbonMonoxideLabel = new ValueAndTimeLabel(Messages.getString("T_CARBON_MONOXIDE2"), null, nf); //$NON-NLS-1$
		currentStatePopulationLabel = new ValueAndTimeLabel(Messages.getString("T_POPULATION2"), null, nf); //$NON-NLS-1$
		currentStateCh4Label = new ValueAndTimeLabel(Messages.getString("T_METHANE2"), null, nf); //$NON-NLS-1$
		currentStateRemainsOfBeingsLabel = new ValueAndTimeLabel(Messages.getString("T_REMAINS_OF_BEINGS"), null, nf); //$NON-NLS-1$
		currentStateDetritusLabel = new ValueAndTimeLabel(Messages.getString("T_DETRITUS2"), null, nf); //$NON-NLS-1$
		currentStateTotalMassLabel = new ValueAndTimeLabel(Messages.getString("T_TOTAL_MASS"), null, nf); //$NON-NLS-1$
		currentStateTotalEnergyLabel = new ValueAndTimeLabel(Messages.getString("T_TOTAL_ENERGY"), null, nf); //$NON-NLS-1$

		colorPanel = new ColorPanel();
		updateColorPanel(colorPanel);
		colorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel colorPanelWrapper = new JPanel();
		JLabel colorProportionLabel = new JLabel(Messages.getString("T_COLOR_PROPORTION")); //$NON-NLS-1$
		GroupLayout colorPanelWrapperLayout = new GroupLayout(colorPanelWrapper);
		colorPanelWrapper.setLayout(colorPanelWrapperLayout);
		colorPanelWrapperLayout.setHorizontalGroup(
				colorPanelWrapperLayout.createSequentialGroup()
						.addComponent(colorProportionLabel)
						.addComponent(colorPanel));
		colorPanelWrapperLayout.setVerticalGroup(
				colorPanelWrapperLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(colorProportionLabel)
						.addComponent(colorPanel));

		JPanel currentStatePanel = new JPanel();
		GroupLayout currentStatePanelLayout = new GroupLayout(currentStatePanel);
		currentStatePanel.setLayout(currentStatePanelLayout);
		currentStatePanelLayout.setHorizontalGroup(
				currentStatePanelLayout.createParallelGroup()
						.addGroup(
								currentStatePanelLayout.createSequentialGroup()
										.addGroup(currentStatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(currentStateTimeLabel)
												.addComponent(currentStateCladesLabel)
												.addComponent(currentStatePopulationLabel)
												.addComponent(currentStateRemainsOfBeingsLabel)
												.addComponent(currentStateTotalMassLabel)
												.addComponent(currentStateTotalEnergyLabel))
										.addGroup(currentStatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(currentStateOxygenLabel)
												.addComponent(currentStateCarbonDioxideLabel)
												.addComponent(currentStateCarbonMonoxideLabel)
												.addComponent(currentStateCh4Label)
												.addComponent(currentStateDetritusLabel)))
						.addComponent(colorPanelWrapper));
		currentStatePanelLayout.setVerticalGroup(
				currentStatePanelLayout.createSequentialGroup()
						.addGroup(currentStatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(currentStateTimeLabel)
								.addComponent(currentStateOxygenLabel))
						.addGroup(currentStatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(currentStateCladesLabel)
								.addComponent(currentStateCarbonDioxideLabel))
						.addGroup(currentStatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(currentStatePopulationLabel)
								.addComponent(currentStateCarbonMonoxideLabel))
						.addGroup(currentStatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(currentStateRemainsOfBeingsLabel)
								.addComponent(currentStateCh4Label))
						.addGroup(currentStatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(currentStateTotalMassLabel)
								.addComponent(currentStateDetritusLabel))
		                .addGroup(currentStatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                		.addComponent(currentStateTotalEnergyLabel))
						.addComponent(colorPanelWrapper));

		title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				Messages.getString("T_CURRENT_STATE"), TitledBorder.LEFT, TitledBorder.TOP); //$NON-NLS-1$
		currentStatePanel.setBorder(title);

		// Notable beings \\

		// Being having the most children
		aliveMostChildrenPanel = new RemarkableOrganismPanel(
				visibleWorld,
				Messages.getString("T_ALIVE_BEING_HAVING_THE_MOST_CHILDREN"), //$NON-NLS-1$
				worldStatistics.getAliveOrganismMostChildren(),
				Messages.getString("T_NUMBER_OF_CHILDREN"), null, nf); //$NON-NLS-1$
		aliveMostChildrenPanel.addMouseListenerToGeneticCodePanel(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showOrganism(worldStatistics.getAliveOrganismMostChildren());
			}
		});

		mostChildrenPanel = new RemarkableOrganismPanel(
				visibleWorld,
				Messages.getString("T_BEING_HAVING_THE_MOST_CHILDREN"), //$NON-NLS-1$
				worldStatistics.getBeingMostChildren(),
				Messages.getString("T_NUMBER_OF_CHILDREN"), Messages.getString("T_AT_TIME"), nf); //$NON-NLS-1$ //$NON-NLS-2$

		// Being having the most victims
		aliveMostKillsPanel = new RemarkableOrganismPanel(
				visibleWorld,
				Messages.getString("T_ALIVE_BEING_HAVING_THE_MOST_VICTIMS"), //$NON-NLS-1$
				worldStatistics.getAliveOrganismMostKills(),
				Messages.getString("T_NUMBER_OF_VICTIMS"), null, nf); //$NON-NLS-1$
		aliveMostKillsPanel.addMouseListenerToGeneticCodePanel(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showOrganism(worldStatistics.getAliveOrganismMostKills());
			}
		});

		mostKillsPanel = new RemarkableOrganismPanel(
				visibleWorld,
				Messages.getString("T_BEING_HAVING_THE_MOST_VICTIMS"), //$NON-NLS-1$
				worldStatistics.getBeingMostKills(),
				Messages.getString("T_NUMBER_OF_VICTIMS"), Messages.getString("T_AT_TIME"), nf); //$NON-NLS-1$ //$NON-NLS-2$

		// Being having the most infections
		aliveMostInfectionsPanel = new RemarkableOrganismPanel(
				visibleWorld,
				Messages.getString("T_ALIVE_BEING_HAVING_THE_MOST_INFECTED"), //$NON-NLS-1$
				worldStatistics.getAliveOrganismMostInfections(),
				Messages.getString("T_NUMBER_OF_INFECTED"), null, nf); //$NON-NLS-1$
		aliveMostInfectionsPanel.addMouseListenerToGeneticCodePanel(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showOrganism(worldStatistics.getAliveOrganismMostInfections());
			}
		});

		mostInfectionsPanel = new RemarkableOrganismPanel(
				visibleWorld,
				Messages.getString("T_BEING_HAVING_THE_MOST_INFECTED"), //$NON-NLS-1$
				worldStatistics.getBeingMostInfections(),
				Messages.getString("T_NUMBER_OF_INFECTED"), Messages.getString("T_AT_TIME"), nf); //$NON-NLS-1$ //$NON-NLS-2$

		// Being having the most mass
		aliveMostMassPanel = new RemarkableOrganismPanel(
				visibleWorld,
				Messages.getString("T_ALIVE_BEING_HAVING_THE_MOST_MASS"), //$NON-NLS-1$
				worldStatistics.getAliveOrganismMostMass(),
				Messages.getString("T_MASS"), null, nf); //$NON-NLS-1$
		aliveMostMassPanel.addMouseListenerToGeneticCodePanel(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showOrganism(worldStatistics.getAliveOrganismMostMass());
			}
		});

		// The oldest being
		aliveOldestPanel = new RemarkableOrganismPanel(
				visibleWorld,
				Messages.getString("T_ALIVE_BEING_THE_OLDEST"), //$NON-NLS-1$
				worldStatistics.getAliveOrganismOldest(),
				Messages.getString("T_AGE"), null, nf); //$NON-NLS-1$
		aliveOldestPanel.addMouseListenerToGeneticCodePanel(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showOrganism(worldStatistics.getAliveOrganismOldest());
			}
		});

		// Being having the lowest generation
		aliveLowestGenerationPanel = new RemarkableOrganismPanel(
				visibleWorld,
				Messages.getString("T_ALIVE_BEING_HAVING_THE_LOWEST_GENERATION"), //$NON-NLS-1$
				worldStatistics.getAliveOrganismLowestGeneration(),
				Messages.getString("T_GENERATION"), null, nf); //$NON-NLS-1$
		aliveLowestGenerationPanel.addMouseListenerToGeneticCodePanel(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showOrganism(worldStatistics.getAliveOrganismLowestGeneration());
			}
		});

		// Being having the highest generation
		aliveHighestGenerationPanel = new RemarkableOrganismPanel(
				visibleWorld,
				Messages.getString("T_ALIVE_BEING_HAVING_THE_HIGHEST_GENERATION"), //$NON-NLS-1$
				worldStatistics.getAliveOrganismHighestGeneration(),
				Messages.getString("T_GENERATION"), null, nf); //$NON-NLS-1$
		aliveHighestGenerationPanel.addMouseListenerToGeneticCodePanel(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showOrganism(worldStatistics.getAliveOrganismHighestGeneration());
			}
		});

		// Notable beings panel
		JPanel notableBeingsPanel = new JPanel();
		GroupLayout notableBeingsPanelLayout = new GroupLayout(notableBeingsPanel);
		notableBeingsPanel.setLayout(notableBeingsPanelLayout);
		notableBeingsPanelLayout.setHorizontalGroup(
				notableBeingsPanelLayout.createSequentialGroup()
						.addGroup(notableBeingsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(aliveMostChildrenPanel)
								.addComponent(aliveMostKillsPanel)
								.addComponent(aliveMostInfectionsPanel)
								.addComponent(aliveMostMassPanel)
								.addComponent(aliveLowestGenerationPanel))
						.addGroup(notableBeingsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(mostChildrenPanel)
								.addComponent(mostKillsPanel)
								.addComponent(mostInfectionsPanel)
								.addComponent(aliveOldestPanel)
								.addComponent(aliveHighestGenerationPanel)));
		notableBeingsPanelLayout.setVerticalGroup(
				notableBeingsPanelLayout.createSequentialGroup()
						.addGroup(notableBeingsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(aliveMostChildrenPanel)
								.addComponent(mostChildrenPanel))
						.addGroup(notableBeingsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(aliveMostKillsPanel)
								.addComponent(mostKillsPanel))
						.addGroup(notableBeingsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(aliveMostInfectionsPanel)
								.addComponent(mostInfectionsPanel))
						.addGroup(notableBeingsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(aliveMostMassPanel)
								.addComponent(aliveOldestPanel))
						.addGroup(notableBeingsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(aliveLowestGenerationPanel)
								.addComponent(aliveHighestGenerationPanel)));

		// Generation histogram
		generationHistogram = new GraphInfo(0, 0, 0, 104, Color.WHITE, Messages.getString("T_POPULATION")); //$NON-NLS-1$
		generationHistogramPanel = new GraphPanel(100, 104, nf);
		generationHistogramPanel.addGraph(generationHistogram);
		JPanel generationHistogramWrapper = new JPanel(new GridLayout(1, 1));
		generationHistogramWrapper.add(generationHistogramPanel);
		generationHistogramWrapper
				.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
						Messages.getString("T_GENERATION_HISTOGRAM"), TitledBorder.LEFT, TitledBorder.TOP)); //$NON-NLS-1$

		JPanel leftInsidePanel = new JPanel();
		GroupLayout leftInsidePanelLayout = new GroupLayout(leftInsidePanel);
		leftInsidePanel.setLayout(leftInsidePanelLayout);
		leftInsidePanelLayout.setHorizontalGroup(
				leftInsidePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(notableBeingsPanel, 0, 0, Short.MAX_VALUE)
						.addComponent(generationHistogramWrapper, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		leftInsidePanelLayout.setVerticalGroup(
				leftInsidePanelLayout.createSequentialGroup()
						.addComponent(notableBeingsPanel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(generationHistogramWrapper));

		JScrollPane leftInsidePanelScrollPane = new BetterJScrollPane(leftInsidePanel);
		// Add all components to the content pane
		JPanel leftPanel = new JPanel();
		GroupLayout leftPanelLayout = new GroupLayout(leftPanel);
		leftPanel.setLayout(leftPanelLayout);
		leftPanelLayout.setHorizontalGroup(
				leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(currentStatePanel)
						.addComponent(leftInsidePanelScrollPane, 0, 0, Short.MAX_VALUE));
		leftPanelLayout.setVerticalGroup(
				leftPanelLayout.createSequentialGroup()
						.addComponent(currentStatePanel)
						.addComponent(leftInsidePanelScrollPane, 0, Short.MAX_VALUE, Short.MAX_VALUE));

		JScrollPane rightPanelWrapper = new BetterJScrollPane(worldHistoryPanel);
		rightPanelWrapper.setBorder(null);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanelWrapper);
		splitPane.setResizeWeight(0.5);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(splitPane, BorderLayout.CENTER);
	}

	private void updateColorPanel(ColorPanel colorPanel) {
		InfoAndColor[] colorCounter = new InfoAndColor[51];
		colorCounter[0] = new InfoAndColor(0, Color.GREEN);
		colorCounter[1] = new InfoAndColor(0, Utils.ColorFOREST);
		colorCounter[2] = new InfoAndColor(0, Utils.ColorIVY);
		colorCounter[3] = new InfoAndColor(0, Utils.ColorSPRING);
		colorCounter[4] = new InfoAndColor(0, Utils.ColorSUMMER);
		colorCounter[5] = new InfoAndColor(0, Utils.ColorLIME);
		colorCounter[6] = new InfoAndColor(0, Utils.ColorLEAF);
		colorCounter[7] = new InfoAndColor(0, Utils.ColorC4);
		colorCounter[8] = new InfoAndColor(0, Utils.ColorJADE);
		colorCounter[9] = new InfoAndColor(0, Utils.ColorGRASS);
		colorCounter[10] = new InfoAndColor(0, Utils.ColorBARK);
		colorCounter[11] = new InfoAndColor(0, Utils.ColorPURPLE);
		colorCounter[12] = new InfoAndColor(0, Utils.ColorPLANKTON);
		colorCounter[13] = new InfoAndColor(0, Color.RED);
		colorCounter[14] = new InfoAndColor(0, Utils.ColorFIRE);
		colorCounter[15] = new InfoAndColor(0, Color.ORANGE);
		colorCounter[16] = new InfoAndColor(0, Utils.ColorMAROON);
		colorCounter[17] = new InfoAndColor(0, Utils.ColorCRIMSON);
		colorCounter[18] = new InfoAndColor(0, Color.PINK);
		colorCounter[19] = new InfoAndColor(0, Utils.ColorCREAM);
		colorCounter[20] = new InfoAndColor(0, Color.LIGHT_GRAY);
		colorCounter[21] = new InfoAndColor(0, Utils.ColorSPIKE);
		colorCounter[22] = new InfoAndColor(0, Utils.ColorLILAC);
		colorCounter[23] = new InfoAndColor(0, Color.GRAY);
		colorCounter[24] = new InfoAndColor(0, Utils.ColorVIOLET);
		colorCounter[25] = new InfoAndColor(0, Utils.ColorOLIVE);
		colorCounter[26] = new InfoAndColor(0, Utils.ColorSKY);
		colorCounter[27] = new InfoAndColor(0, Color.BLUE);
		colorCounter[28] = new InfoAndColor(0, Utils.ColorOCHRE);
		colorCounter[29] = new InfoAndColor(0, Utils.ColorFALLOW);
		colorCounter[30] = new InfoAndColor(0, Utils.ColorSPORE);
		colorCounter[31] = new InfoAndColor(0, Color.WHITE);
		colorCounter[32] = new InfoAndColor(0, Utils.ColorPLAGUE);
		colorCounter[33] = new InfoAndColor(0, Utils.ColorCORAL);
		colorCounter[34] = new InfoAndColor(0, Utils.ColorMINT);
		colorCounter[35] = new InfoAndColor(0, Utils.ColorLAVENDER);
		colorCounter[36] = new InfoAndColor(0, Color.MAGENTA);
		colorCounter[37] = new InfoAndColor(0, Utils.ColorROSE);
		colorCounter[38] = new InfoAndColor(0, Color.CYAN);
		colorCounter[39] = new InfoAndColor(0, Utils.ColorTEAL);
		colorCounter[40] = new InfoAndColor(0, Utils.ColorDRIFT);		
		colorCounter[41] = new InfoAndColor(0, Utils.ColorSPIN);
		colorCounter[42] = new InfoAndColor(0, Color.YELLOW);
		colorCounter[43] = new InfoAndColor(0, Utils.ColorAUBURN);
		colorCounter[44] = new InfoAndColor(0, Utils.ColorINDIGO);
		colorCounter[45] = new InfoAndColor(0, Utils.ColorBLOND);
		colorCounter[46] = new InfoAndColor(0, Utils.ColorFLOWER);
		colorCounter[47] = new InfoAndColor(0, Color.DARK_GRAY);
		colorCounter[48] = new InfoAndColor(0, Utils.ColorGOLD);
		colorCounter[49] = new InfoAndColor(0, Utils.ColorDARK);
		colorCounter[50] = new InfoAndColor(0, Utils.ColorEYE);

		synchronized (organisms) {
			for (Iterator<Organism> it = organisms.iterator(); it.hasNext();) {
				GeneticCode gc = it.next().getGeneticCode();
				for (int i = 0; i < gc.getNGenes(); i++) {
					Color c = gc.getGene(i).getColor();
					for (int j = 0; j < colorCounter.length; j++) {
						if (c.equals(colorCounter[j].color)) {
							colorCounter[j].info++;
						}
					}
				}
			}
		}
		Arrays.sort(colorCounter);
		colorPanel.setColors(Arrays.asList(colorCounter));
	}

	private void updatePerformed(ActionEvent e) {
		// Update world statistics
		worldStatistics.findBestAliveBeings(organisms);

		long minTime = Math.max(world.getTime() - 100, 0);
		long maxTime = world.getTime();

		// Update graphs
		populationGraphPanel.setMinTime(minTime);
		populationGraphPanel.setMaxTime(maxTime);
		double max = Utils.max(worldStatistics.getMaxDeathsFromList(), worldStatistics.getMaxBirthFromList(),
				worldStatistics.getMaxPopulationFromList());
		deathsGraph.setMaxAndPoints(max, worldStatistics.getDeathList());
		birthsGraph.setMaxAndPoints(max, worldStatistics.getBirthList());
		populationGraph.setMaxAndPoints(max, worldStatistics.getPopulationList());
		cladesGraph.setMaxAndPoints(max, worldStatistics.getDistinctCladesList());

		cladesGraphPanel.setMinTime(minTime);
		cladesGraphPanel.setMaxTime(maxTime);
		cladesGraph2.setMaxAndPoints(worldStatistics.getMaxDistinctClades(), worldStatistics.getDistinctCladesList());
		populationGraph2.setMaxAndPoints(worldStatistics.getMaxDistinctClades(), worldStatistics.getPopulationList());

		populationStatsPanel.update();

		atmosphereGraphPanel.setMinTime(minTime);
		atmosphereGraphPanel.setMaxTime(maxTime);
		max = Math.sqrt(Math.sqrt(world.getO2() + world.getCO2() + world.getCO1() + world.getCH4() + world.getDetritus()));
		oxygenGraph.setMaxAndPoints(max, worldStatistics.getOxygenList());
		carbonDioxideGraph.setMaxAndPoints(max, worldStatistics.getCarbonDioxideList());
		carbonMonoxideGraph.setMaxAndPoints(max, worldStatistics.getCarbonMonoxideList());
		methaneGraph.setMaxAndPoints(max, worldStatistics.getMethaneList());
		detritusGraph.setMaxAndPoints(max, worldStatistics.getDetritusList());

		atmosphereStatsPanel.update();

		generationHistogramPanel.setMinTime(worldStatistics.getMinGeneration());
		generationHistogramPanel.setMaxTime(worldStatistics.getMaxGeneration());
		max = worldStatistics.getGenerationHistogramList().stream().max(Double::compare).orElse(1.0);
		generationHistogram.setMaxAndPoints(
				max,
				worldStatistics.getGenerationHistogramList());

		// Update current state panel
		currentStateTimeLabel.update(world.getTime());
		currentStateOxygenLabel.update(world.getO2());
		currentStateCladesLabel.update(world.getDistinctCladeIDCount());
		currentStateCarbonDioxideLabel.update(world.getCO2());
		currentStateCarbonMonoxideLabel.update(world.getCO1());
		currentStatePopulationLabel.update(world.getPopulation());
		currentStateCh4Label.update(world.getCH4());
		currentStateRemainsOfBeingsLabel.update(world.getNCorpses());
		currentStateDetritusLabel.update(world.getDetritus());
		currentStateTotalMassLabel.update(worldStatistics.getTotalMass());
		currentStateTotalEnergyLabel.update(worldStatistics.getTotalEnergy());
		updateColorPanel(colorPanel);

		// Update notable beings panel
		aliveMostChildrenPanel.update(worldStatistics.getAliveOrganismMostChildren(),
				worldStatistics.getAliveOrganismMostChildrenNumber());
		mostChildrenPanel.update(worldStatistics.getBeingMostChildren(), worldStatistics.getBeingMostChildrenNumber(),
				worldStatistics.getBeingMostChildrenTime());
		aliveMostKillsPanel.update(worldStatistics.getAliveOrganismMostKills(),
				worldStatistics.getAliveOrganismMostKillsNumber());
		mostKillsPanel.update(worldStatistics.getBeingMostKills(), worldStatistics.getBeingMostKillsNumber(),
				worldStatistics.getBeingMostKillsTime());
		aliveMostInfectionsPanel.update(worldStatistics.getAliveOrganismMostInfections(),
				worldStatistics.getAliveOrganismMostInfectionsNumber());
		mostInfectionsPanel.update(worldStatistics.getBeingMostInfections(),
				worldStatistics.getBeingMostInfectionsNumber(), worldStatistics.getBeingMostInfectionsTime());
		aliveMostMassPanel.update(worldStatistics.getAliveOrganismMostMass(),
				worldStatistics.getAliveOrganismMostMassNumber());
		aliveOldestPanel.update(worldStatistics.getAliveOrganismOldest(),
				worldStatistics.getAliveOrganismOldestNumber());
		aliveLowestGenerationPanel.update(worldStatistics.getAliveOrganismLowestGeneration(),
				worldStatistics.getAliveOrganismLowestGenerationNumber());
		aliveHighestGenerationPanel.update(worldStatistics.getAliveOrganismHighestGeneration(),
				worldStatistics.getAliveOrganismHighestGenerationNumber());
		
		repaint();
	}

	/**
	 * Sets the given organism as the selected organism in the world,
	 * then scrolls the world view so this organism is at the center,
	 * then brings the main window to front and focus.
	 */
	private void showOrganism(Organism o) {
		if (o != null) {
			visibleWorld.setSelectedOrganism(o);
			visibleWorld.getMainWindow().scrollOrganismToCenter(o);
			visibleWorld.getMainWindow().toFront();
		}
	}
}

class ColorPanel extends JPanel {
	private static final long serialVersionUID = Utils.FILE_VERSION;

	private List<InfoAndColor> infoList = new ArrayList<InfoAndColor>();
	private int total = 0;

	public ColorPanel() {
		setPreferredSize(new Dimension(300, 25));
		setMinimumSize(new Dimension(100, 25));
	}

	public void setColors(List<InfoAndColor> infoList) {
		this.infoList = infoList;
		total = infoList.stream().mapToInt(i -> i.info).sum();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = getSize().width;
		int height = getSize().height;
		double x, lastX = 0;
		InfoAndColor infoAndColor;
		if (total > 0) {
			for (Iterator<InfoAndColor> it = infoList.iterator(); it.hasNext();) {
				infoAndColor = it.next();
				x = width * infoAndColor.info / (double) total;
				g.setColor(infoAndColor.color);
				g.fillRect((int) lastX, 0, (int) (lastX + x) - (int) lastX, height);
				lastX += x;
			}
		}
	}
}

class InfoAndColor implements Comparable<InfoAndColor> {
	public int info;
	public Color color;

	public InfoAndColor(int i, Color c) {
		info = i;
		color = c;
	}

	public int compareTo(InfoAndColor o) {
		if (info < o.info) {
			return 1; // reverse order
		}
		if (info > o.info) {
			return -1; // reverse order
		}
		return 0;
	}
}
