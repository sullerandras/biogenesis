package biogenesis.clade_analyzer;

import java.awt.Taskbar;
import java.net.URL;

import javax.swing.ImageIcon;

import biogenesis.Clade;
import biogenesis.clade_analyzer.gui.MainFrame;

public class GUI {
  public static ImageIcon createIcon(String imageName) {
    ImageIcon icon = null;
    if (imageName != null) {
      URL url = Clade.class.getResource(imageName);
      if (url != null) {
        icon = new ImageIcon(url);
      }
    }
    return icon;
  }

  public static void main(String args[]) {
    // Set the name of the application menu item on macOS. Also, set the name of the application seen in Activity Monitor.
    System.setProperty("apple.awt.application.name", "Biogenesis Analyzer");

    ImageIcon imageIcon = createIcon("images/menu_track.png");
    try {
      // Set application icon. This only works on macOS.
      final Taskbar taskbar = Taskbar.getTaskbar(); // Java 9+
      if (imageIcon != null) {
        taskbar.setIconImage(imageIcon.getImage());
      }
    } catch (Exception | NoClassDefFoundError e) {
      Logger.println("Unable to set icon: " + e);
    }

    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        MainFrame mainFrame = new MainFrame();
        if (imageIcon != null) {
          // Set application icon. This only works on Windows and Linux.
          mainFrame.setIconImage(imageIcon.getImage());
        }
        mainFrame.setVisible(true);
      }
    });
  }
}
