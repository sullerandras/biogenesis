package biogenesis;

import java.awt.Frame;

public interface MainWindowInterface {
  public Frame getFrame();
  public World getWorld();
  public VisibleWorld getVisibleWorld();
  public InfoToolbarInterface getInfoPanel();
}
