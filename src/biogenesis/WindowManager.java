package biogenesis;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.util.prefs.Preferences;

/**
 * It's a utility class that remembers the last position and size of windows.
 */
public class WindowManager {
  public static void registerWindow(java.awt.Window window, int defaultWidth, int defaultHeight, int defaultX,
      int defaultY) {
    Preferences prefs = Preferences.userRoot().node("biogenesis/window_manager/" + window.getClass().getName());
    int width = prefs.getInt("width", defaultWidth);
    int height = prefs.getInt("height", defaultHeight);
    int x = prefs.getInt("x", defaultX);
    int y = prefs.getInt("y", defaultY);

    window.setSize(width, height);
    window.setLocation(x, y);

    ComponentAdapter componentAdapter = new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        prefs.putInt("width", window.getWidth());
        prefs.putInt("height", window.getHeight());
      }

      public void componentMoved(ComponentEvent e) {
        prefs.putInt("x", window.getX());
        prefs.putInt("y", window.getY());
      }

      public void componentHidden(ComponentEvent e) {
        window.removeComponentListener(this);
      }
    };
    window.addComponentListener(componentAdapter);

    window.addWindowListener(new WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent e) {
        window.removeWindowListener(this);
        window.removeComponentListener(componentAdapter);
      }
    });
  }
}
