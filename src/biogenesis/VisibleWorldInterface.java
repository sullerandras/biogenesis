package biogenesis;

import java.awt.Dimension;
import java.awt.Rectangle;

public interface VisibleWorldInterface {
  public MainWindowInterface getMainWindow();
  public void repaint();
  public void repaint(Rectangle r);
  public void setPreferredSize(Dimension d);
  public Organism getSelectedOrganism();
  public void setSelectedOrganism(Organism o);
  public void showDeadToolbar();
}
