package biogenesis.gui.stats;

import java.awt.GridBagLayout;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import biogenesis.GeneticCode;
import biogenesis.GeneticCodePanel;
import biogenesis.SimpleGridBagConstraints;
import biogenesis.VisibleWorld;

/**
 * Panel that shows a remarkable organism:
 * <ul>
 * <li>A title describing why this organism is remarkable.</li>
 * <li>Its genetic code.</li>
 * <li>A record value.</li>
 * <li>The time the record was reached. This is optional.</li>
 * </ul>
 */
public class RemarkableOrganismPanel extends JPanel {
  private final GeneticCodePanel geneticCodePanel;
  private final ValueAndTimeLabel valueAndTimeLabel;

  /**
   * Creates a new instance of RemarkableOrganismPanel.
   * @param visibleWorld The visible world, needed to draw the genetic code.
   * @param title The title of the remarkable organism.
   * @param geneticCode The genetic code of the remarkable organism.
   * @param valueLabel The label of the record value.
   * @param timeLabel The label of the time the record was reached. If this is null, the time is not shown.
   * @param nf The number format to use.
   */
  public RemarkableOrganismPanel(VisibleWorld visibleWorld, String title, GeneticCode geneticCode, String valueLabel,
      String timeLabel, NumberFormat nf) {
    geneticCodePanel = new GeneticCodePanel(geneticCode, visibleWorld);
    valueAndTimeLabel = new ValueAndTimeLabel(valueLabel, timeLabel, nf);

    setLayout(new GridBagLayout());
    SimpleGridBagConstraints sgbc = new SimpleGridBagConstraints(0, 0, 1, 1, 1, 0,
        java.awt.GridBagConstraints.NORTHWEST, java.awt.GridBagConstraints.NONE);
    add(new JLabel(title), sgbc.withGridXY(0, 0));
    add(geneticCodePanel, sgbc.withGridXY(0, 1));
    add(valueAndTimeLabel, sgbc.withGridXY(0, 2));
  }

  /**
   * Adds a mouse listener to the genetic code panel. Used for exporting the genetic code with right click.
   */
  public void addMouseListenerToGeneticCodePanel(java.awt.event.MouseListener mouseListener) {
    geneticCodePanel.addMouseListener(mouseListener);
  }

  /**
   * Updates the genetic code and the record value. This method should only be used when the time is not shown.
   */
  public void update(GeneticCode geneticCode, double value) {
    update(geneticCode, value, 0);
  }

  /**
   * Updates the genetic code, the record value and the time. This method should only be used when the time is shown.
   */
  public void update(GeneticCode geneticCode, double value, double time) {
    geneticCodePanel.setGeneticCode(geneticCode);
    valueAndTimeLabel.update(value, time);
  }
}
