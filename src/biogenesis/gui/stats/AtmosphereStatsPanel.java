package biogenesis.gui.stats;

import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import biogenesis.Messages;
import biogenesis.WorldStatistics;

/**
 * Panel that shows the statistics of the atmosphere:
 * <ul>
 * <li>Maximum and minimum carbon dioxide.</li>
 * <li>Maximum and minimum methane.</li>
 * <li>Maximum and minimum detritus.</li>
 * <li>Maximum and minimum oxygen.</li>
 * </ul>
 */
public class AtmosphereStatsPanel extends JPanel {
  private final WorldStatistics worldStatistics;
  private final NumberFormat nf;

  private ValueAndTimeLabel maxCarbonDioxideLabel;
  private ValueAndTimeLabel minCarbonDioxideLabel;
  private ValueAndTimeLabel maxMethaneLabel;
  private ValueAndTimeLabel minMethaneLabel;
  private ValueAndTimeLabel maxDetritusLabel;
  private ValueAndTimeLabel minDetritusLabel;
  private ValueAndTimeLabel maxOxygenLabel;
  private ValueAndTimeLabel minOxygenLabel;

  /**
   * Creates a new instance of AtmosphereStatsPanel.
   * @param worldStatistics it is used to get the statistics of the world.
   * @param nf The number format to use.
   */
  public AtmosphereStatsPanel(WorldStatistics worldStatistics, NumberFormat nf) {
    this.worldStatistics = worldStatistics;
    this.nf = nf;

    initComponents();
  }

  private void initComponents() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    String timeLabel = Messages.getString("T_AT_TIME"); //$NON-NLS-1$

    maxCarbonDioxideLabel = new ValueAndTimeLabel(Messages.getString("T_MAXIMUM_CARBON_DIOXIDE"), timeLabel, nf); //$NON-NLS-1$
    minCarbonDioxideLabel = new ValueAndTimeLabel(Messages.getString("T_MINIMUM_CARBON_DIOXIDE"), timeLabel, nf); //$NON-NLS-1$
    maxMethaneLabel = new ValueAndTimeLabel(Messages.getString("T_MAXIMUM_METHANE"), timeLabel, nf); //$NON-NLS-1$
    minMethaneLabel = new ValueAndTimeLabel(Messages.getString("T_MINIMUM_METHANE"), timeLabel, nf); //$NON-NLS-1$
    maxDetritusLabel = new ValueAndTimeLabel(Messages.getString("T_MAXIMUM_DETRITUS"), timeLabel, nf); //$NON-NLS-1$
    minDetritusLabel = new ValueAndTimeLabel(Messages.getString("T_MINIMUM_DETRITUS"), timeLabel, nf); //$NON-NLS-1$
    maxOxygenLabel = new ValueAndTimeLabel(Messages.getString("T_MAXIMUM_OXYGEN"), timeLabel, nf); //$NON-NLS-1$
    minOxygenLabel = new ValueAndTimeLabel(Messages.getString("T_MINIMUM_OXYGEN"), timeLabel, nf); //$NON-NLS-1$

    add(maxCarbonDioxideLabel);
    add(minCarbonDioxideLabel);
    add(maxMethaneLabel);
    add(minMethaneLabel);
    add(maxDetritusLabel);
    add(minDetritusLabel);
    add(maxOxygenLabel);
    add(minOxygenLabel);
  }

  /**
   * Updates the statistics to the latest from worldStatistics.
   */
  public void update() {
    maxCarbonDioxideLabel.update(worldStatistics.getMaxCarbonDioxide(), worldStatistics.getMaxCarbonDioxideTime());
    minCarbonDioxideLabel.update(worldStatistics.getMinCarbonDioxide(), worldStatistics.getMinCarbonDioxideTime());
    maxMethaneLabel.update(worldStatistics.getMaxMethane(), worldStatistics.getMaxMethaneTime());
    minMethaneLabel.update(worldStatistics.getMinMethane(), worldStatistics.getMinMethaneTime());
    maxDetritusLabel.update(worldStatistics.getMaxDetritus(), worldStatistics.getMaxDetritusTime());
    minDetritusLabel.update(worldStatistics.getMinDetritus(), worldStatistics.getMinDetritusTime());
    maxOxygenLabel.update(worldStatistics.getMaxOxygen(), worldStatistics.getMaxOxygenTime());
    minOxygenLabel.update(worldStatistics.getMinOxygen(), worldStatistics.getMinOxygenTime());
  }
}
