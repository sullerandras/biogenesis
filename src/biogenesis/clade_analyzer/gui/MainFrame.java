package biogenesis.clade_analyzer.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import biogenesis.BioFile;
import biogenesis.BioFileFilter;
import biogenesis.WindowManager;
import biogenesis.clade_analyzer.Analyzer;
import biogenesis.clade_analyzer.CladeSummary;
import biogenesis.clade_analyzer.DB;

public class MainFrame extends javax.swing.JFrame {
  private DB db = null;
  private int maxTime;
  private Preferences prefs;

  public MainFrame() {
    WindowManager.registerWindow(this, 800, 600, 0, 0);
    prefs = Preferences.userRoot().node("biogenesis/clade_analyzer");

    initComponents();
  }

  private void initComponents() {
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Biogenesis Clade Analyzer");
    setMinimumSize(new java.awt.Dimension(800, 600));
    getContentPane().setLayout(new java.awt.GridBagLayout());

    // toolbar
    javax.swing.JToolBar toolbar = new javax.swing.JToolBar();
    toolbar.setFloatable(false);
    toolbar.setRollover(true);
    JButton openButton = new JButton("Open");
    openButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openButtonActionPerformed(evt);
      }
    });
    toolbar.add(openButton);

    // button to show 10 clades that survived the longest
    JButton longestSurvivorsButton = new JButton("Longest Survivors");
    toolbar.add(longestSurvivorsButton);

    getContentPane().add(toolbar,
        new java.awt.GridBagConstraints(0, 0, 1, 1, 1, 0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    // main panel
    javax.swing.JPanel mainPanel = new javax.swing.JPanel();
    mainPanel.setLayout(new java.awt.GridBagLayout());
    getContentPane().add(mainPanel,
        new java.awt.GridBagConstraints(0, 1, 1, 1, 1, 1, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    CladeListPanel cladeListPanel = new CladeListPanel();
    mainPanel.add(cladeListPanel,
        new java.awt.GridBagConstraints(0, 0, 1, 1, 1, 1, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    longestSurvivorsButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        try {
          java.util.List<CladeSummary> cladeSummaries = db.getLongestSurvivors();
          System.out.println("Found " + cladeSummaries.size() + " clades");
          cladeListPanel.setCladeList(cladeSummaries, db, maxTime);
        } catch (SQLException e) {
          System.err.println("Error getting clade summaries: " + e);
          e.printStackTrace();
        }
      }
    });

    cladeListPanel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        CladeSummary cladeSummary = (CladeSummary) evt.getSource();
        System.out.println("Selected clade: " + cladeSummary);
        CladeDetailsDialog cladeDetailsDialog = new CladeDetailsDialog(MainFrame.this, db, cladeSummary.getCladeId(),
            maxTime);
        cladeDetailsDialog.setVisible(true);
      }
    });

    invalidate();
  }

  private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {
    javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
    fileChooser.setCurrentDirectory(new File(prefs.get("lastDirectory", ".")));
    fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
    fileChooser.setFileFilter(new BioFileFilter(BioFileFilter.WORLD_EXTENSION, BioFileFilter.WORLD_EXTENSION + ".gz"));
    int result = fileChooser.showOpenDialog(this);
    if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
      java.io.File worldFile;
      try {
        worldFile = fileChooser.getSelectedFile().getCanonicalFile();
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }
      prefs.put("lastDirectory", worldFile.getParent());

      BioFile bioFile = new BioFile(worldFile);
      if (bioFile.fileNameContainsTime()) {
        JOptionPane.showMessageDialog(this, "Please select the world file without @ character in the name.",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      openDatabaseFile(bioFile);
    }
  }

  private void openDatabaseFile(BioFile bioFile) {
    new Thread() {
      public void run() {
        try {
          if (db != null) {
            db.close();
          }

          db = new DB(bioFile.getSqliteFile());
          ProgressMonitor progressMonitor = new ProgressMonitor(MainFrame.this, "Loading database", "", 0, 100);
          Analyzer.analyze(bioFile, db, progressMonitor);
          if (progressMonitor.isCanceled()) {
            db.close();
            db = null;
            return;
          }

          progressMonitor.close();

          maxTime = db.getMaxTime();
          setTitle("DB: " + relativePath(db.getDbFile()) + "  MaxTime: " + maxTime);
        } catch (ClassNotFoundException | SQLException | JsonIOException | JsonSyntaxException
            | FileNotFoundException e) {
          System.err.println("Error opening database: " + e);
          e.printStackTrace();
        }
      }
    }.start();
  }

  private String relativePath(File file) {
    return new File(".").getAbsoluteFile().getParentFile().toPath().relativize(file.getAbsoluteFile().toPath())
        .toString();
  }
}
