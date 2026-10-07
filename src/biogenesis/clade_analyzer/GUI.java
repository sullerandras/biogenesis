package biogenesis.clade_analyzer;

import java.net.URL;

import javax.swing.ImageIcon;

import biogenesis.Clade;
import biogenesis.LogCollector;
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
    System.setOut(new java.io.PrintStream(LogCollector.getInstance()));
    System.setErr(new java.io.PrintStream(LogCollector.getInstance()));

    // Set the name of the application menu item on macOS. Also, set the name of the application seen in Activity Monitor.
    System.setProperty("apple.awt.application.name", "Biogenesis Analyzer");

    ImageIcon imageIcon = createIcon("images/menu_track.png");
    try {
      // Set application icon. This only works on macOS.
      // There is no Taskbar in java 8
      // final java.awt.Taskbar taskbar = java.awt.Taskbar.getTaskbar(); // Java 9+
      // So we use reflection to get the taskbar
      if (imageIcon != null) {
        Class<?> taskbarClass = Class.forName("java.awt.Taskbar");
        Object taskbar = taskbarClass.getMethod("getTaskbar").invoke(null); // Java 9+
        taskbar.getClass().getMethod("setIconImage", java.awt.Image.class).invoke(taskbar, imageIcon.getImage());
      }
    } catch (Exception | NoClassDefFoundError e) {
      System.out.println("Unable to set icon: " + e);
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
