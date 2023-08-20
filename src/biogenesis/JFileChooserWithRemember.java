package biogenesis;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;

/**
 * JFileChooser that remembers the last directory used.
 */
public class JFileChooserWithRemember extends JFileChooser {
  private static final long serialVersionUID = 1L;
  private final Preferences prefs;

  public JFileChooserWithRemember() {
    super();
    prefs = Preferences.userRoot().node("biogenesis/file_chooser");
  }

  public JFileChooserWithRemember(String currentDirectoryPath) {
    super(currentDirectoryPath);
    prefs = Preferences.userRoot().node("biogenesis/file_chooser");
  }

  public JFileChooserWithRemember(java.io.File currentDirectory) {
    super(currentDirectory);
    prefs = Preferences.userRoot().node("biogenesis/file_chooser");
  }

  @Override
  public int showOpenDialog(Component parent) throws HeadlessException {
    setCurrentDirectory(new File(prefs.get("lastDirectory", ".")));

    return super.showOpenDialog(parent);
  }

  @Override
  public int showSaveDialog(Component parent) throws HeadlessException {
    setCurrentDirectory(new File(prefs.get("lastDirectory", ".")));

    return super.showSaveDialog(parent);
  }

  @Override
  public void approveSelection() {
    super.approveSelection();

    if (getSelectedFile() != null) {
      if (getSelectedFile().isDirectory()) {
        prefs.put("lastDirectory", getSelectedFile().getAbsolutePath());
      } else {
        prefs.put("lastDirectory", getSelectedFile().getParentFile().getAbsolutePath());
      }
    }
  }
}
