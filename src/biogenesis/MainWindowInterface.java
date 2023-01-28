package biogenesis;

import java.awt.Frame;

public interface MainWindowInterface {
  public Frame getFrame();
  public World getWorld();
  public VisibleWorld getVisibleWorld();
  public InfoToolbar getInfoPanel();
  public BioFile getBioFile();
}
