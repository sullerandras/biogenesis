package biogenesis.gui.stats;

import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import biogenesis.Messages;
import biogenesis.WorldStatistics;

/**
 * Panel that shows the statistics of the population.
 */
public class PopulationStatsPanel extends JPanel {
  private final WorldStatistics worldStatistics;
  private final NumberFormat nf;

  private ValueAndTimeLabel averagePopulationLabel;
  private ValueAndTimeLabel averageBirthsLabel;
  private ValueAndTimeLabel averageDeathsLabel;
  private ValueAndTimeLabel averageInfectionsLabel;
  private ValueAndTimeLabel createdOrganismsLabel;
  private ValueAndTimeLabel maxPopulationLabel;
  private ValueAndTimeLabel minPopulationLabel;
  private ValueAndTimeLabel massExtintionsLabel;

  /**
   * Creates a new instance of PopulationStatsPanel.
   * @param worldStatistics The statistics of the world, used to update the values.
   * @param nf The number format to use.
   */
  public PopulationStatsPanel(WorldStatistics worldStatistics, NumberFormat nf) {
    this.worldStatistics = worldStatistics;
    this.nf = nf;

    initComponents();
  }

  private void initComponents() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    String timeLabel = Messages.getString("T_AT_TIME"); //$NON-NLS-1$
    averagePopulationLabel = new ValueAndTimeLabel(Messages.getString("T_AVERAGE_POPULATION"), null, nf); //$NON-NLS-1$
    averageBirthsLabel = new ValueAndTimeLabel(Messages.getString("T_AVERAGE_BIRTH_RATE"), null, nf); //$NON-NLS-1$
    averageDeathsLabel = new ValueAndTimeLabel(Messages.getString("T_AVERAGE_MORTALITY_RATE"), null, nf); //$NON-NLS-1$
    averageInfectionsLabel = new ValueAndTimeLabel(Messages.getString("T_AVERAGE_INFECTIONS_RATE"), null, nf); //$NON-NLS-1$
    createdOrganismsLabel = new ValueAndTimeLabel(Messages.getString("T_GENERATED_ORGANISMS"), null, nf); //$NON-NLS-1$
    maxPopulationLabel = new ValueAndTimeLabel(Messages.getString("T_MAXIMUM_POPULATION"), timeLabel, nf); //$NON-NLS-1$
    minPopulationLabel = new ValueAndTimeLabel(Messages.getString("T_MINIMUM_POPULATION"), timeLabel, nf); //$NON-NLS-1$
    massExtintionsLabel = new ValueAndTimeLabel(Messages.getString("T_MASS_EXTINTIONS"), null, nf); //$NON-NLS-1$

    add(averagePopulationLabel);
    add(averageBirthsLabel);
    add(averageDeathsLabel);
    add(averageInfectionsLabel);
    add(createdOrganismsLabel);
    add(maxPopulationLabel);
    add(minPopulationLabel);
    add(massExtintionsLabel);
  }

  /**
   * Updates the statistics shown in the panel using worldStatistics.
   */
  public void update() {
    averagePopulationLabel.update(worldStatistics.getAveragePopulation());
    averageBirthsLabel.update(worldStatistics.getAverageBirths());
    averageDeathsLabel.update(worldStatistics.getAverageDeaths());
    averageInfectionsLabel.update(worldStatistics.getAverageInfections());
    createdOrganismsLabel.update(worldStatistics.getCreatedOrganisms());
    maxPopulationLabel.update(worldStatistics.getMaxPopulation(), worldStatistics.getMaxPopulationTime());
    minPopulationLabel.update(worldStatistics.getMinPopulation(), worldStatistics.getMinPopulationTime());
    massExtintionsLabel.update(worldStatistics.getMassExtintions());
  }
}
