package biogenesis.clade_analyzer.gui;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.ProgressMonitor;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import biogenesis.BioFile;
import biogenesis.BioFileFilter;
import biogenesis.JFileChooserWithRemember;
import biogenesis.WindowManager;
import biogenesis.clade_analyzer.Analyzer;
import biogenesis.clade_analyzer.CladeChartManager;
import biogenesis.clade_analyzer.CladeDetails;
import biogenesis.clade_analyzer.DB;

public class MainFrame extends javax.swing.JFrame {
  private DB db = null;
  private int maxTime;

  private CladeListPanel longestSurvivorsPanel;
  private CladeListPanel mostPopulousCladesPanel;

  public MainFrame() {
    WindowManager.registerWindow(this, 800, 600, 0, 0);

    initComponents();
  }

  private void initComponents() {
    if (!java.awt.EventQueue.isDispatchThread()) {
      throw new RuntimeException("Not in dispatch thread");
    }

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

    getContentPane().add(toolbar,
        new java.awt.GridBagConstraints(0, 0, 1, 1, 1, 0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    // tabs
    JTabbedPane tabbedPane = new JTabbedPane();

    longestSurvivorsPanel = new CladeListPanel(this, new CladeChartManager());
    tabbedPane.add("Longest survivors", longestSurvivorsPanel);

    mostPopulousCladesPanel = new CladeListPanel(this, new CladeChartManager());
    tabbedPane.add("Most populous clades", mostPopulousCladesPanel);

    getContentPane().add(tabbedPane,
        new java.awt.GridBagConstraints(0, 1, 1, 1, 1, 1, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    ActionListener clickListener = new ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        CladeDetails cladeSummary = (CladeDetails) evt.getSource();
        System.out.println("Selected clade: " + cladeSummary);
        CladeDetailsDialog cladeDetailsDialog = new CladeDetailsDialog(MainFrame.this, db, cladeSummary.getCladeId(),
            maxTime);
        cladeDetailsDialog.setVisible(true);
      }
    };
    longestSurvivorsPanel.addClickCladeListener(clickListener);
    mostPopulousCladesPanel.addClickCladeListener(clickListener);

    longestSurvivorsPanel.addLimitChangeListener(evt -> refreshTabs());
    mostPopulousCladesPanel.addLimitChangeListener(evt -> refreshTabs());

    invalidate();
  }

  private void refreshTabs() {
    new Thread() {
      public void run() {
        try {
          final java.util.List<CladeDetails> cladeSummaries = db.getLongestSurvivors(longestSurvivorsPanel.getLimit());
          final java.util.List<CladeDetails> mostPopulousClades = db.getMostPopulousClades(maxTime, mostPopulousCladesPanel.getLimit());

          java.awt.EventQueue.invokeLater(() -> {
            longestSurvivorsPanel.setCladeList(cladeSummaries, db, maxTime);
            mostPopulousCladesPanel.setCladeList(mostPopulousClades, db, maxTime);
          });
        } catch (SQLException e) {
          System.err.println("Error getting clade summaries: " + e);
          e.printStackTrace();
        }
      }
    }.start();
  }

  private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {
    javax.swing.JFileChooser fileChooser = new JFileChooserWithRemember();
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
          DB newDB = new DB(bioFile.getSqliteFile());
          ProgressMonitor progressMonitor = new ProgressMonitor(MainFrame.this, "Loading database", "", 0, 100);
          Analyzer.analyze(bioFile, newDB, progressMonitor);
          if (progressMonitor.isCanceled()) {
            newDB.close();
            return;
          }

          progressMonitor.close();

          if (db != null) {
            db.close();
            db = null;
          }

          db = newDB;
          maxTime = db.getMaxTime();
          setTitle("DB: " + relativePath(db.getDbFile()) + "  MaxTime: " + maxTime);

          refreshTabs();
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
