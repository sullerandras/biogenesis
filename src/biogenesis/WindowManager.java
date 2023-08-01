package biogenesis;

import java.util.prefs.Preferences;

/**
 * It's a utility class that remembers the last position and size of windows.
 */
public class WindowManager {
  public static void registerWindow(java.awt.Window window, int defaultWidth, int defaultHeight, int defaultX, int defaultY) {
    Preferences prefs = Preferences.userRoot().node("biogenesis/window_manager/" + window.getClass().getName());
    int width = prefs.getInt("width", defaultWidth);
    int height = prefs.getInt("height", defaultHeight);
    int x = prefs.getInt("x", defaultX);
    int y = prefs.getInt("y", defaultY);

    window.setSize(width, height);
    window.setLocation(x, y);

    window.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent evt) {
        prefs.putInt("width", window.getWidth());
        prefs.putInt("height", window.getHeight());
      }

      public void componentMoved(java.awt.event.ComponentEvent evt) {
        prefs.putInt("x", window.getX());
        prefs.putInt("y", window.getY());
      }
    });
  }
}
