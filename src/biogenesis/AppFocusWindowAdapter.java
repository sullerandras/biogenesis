package biogenesis;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AppFocusWindowAdapter extends WindowAdapter {
  @Override
  public void windowActivated(WindowEvent arg0) {
    Utils.setAppInFocus(true);
  }

  @Override
  public void windowDeactivated(WindowEvent arg0) {
    Utils.setAppInFocus(false);
  }
}
