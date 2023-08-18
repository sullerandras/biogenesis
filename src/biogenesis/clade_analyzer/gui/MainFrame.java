package biogenesis.clade_analyzer.gui;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import biogenesis.clade_analyzer.db.DB;

public class MainFrame extends javax.swing.JFrame {
  private DB db = null;
  private int maxTime;
  private boolean analyzeInProgress = false;

  private CladeListPanel longestSurvivorsPanel;
  private CladeListPanel mostPopulousCladesPanel;
  private HeatMapPanel heatMapPanel;
  private JCheckBox autoAnalyzeCheckBox;

  public MainFrame() {
    WindowManager.registerWindow(this, 800, 600, 0, 0);

    initComponents();

    java.util.Timer timer = new java.util.Timer();
    timer.schedule(new java.util.TimerTask() {
      @Override
      public void run() {
        if (autoAnalyzeCheckBox.isSelected()) {
          reanalyzeDB();
        }
      }
    }, 10000, 10000);
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

    JButton analyzeNewFilesButton = new JButton("Analyze new files");
    analyzeNewFilesButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        reanalyzeDB();
      }
    });
    toolbar.add(analyzeNewFilesButton);

    autoAnalyzeCheckBox = new JCheckBox("Auto analyze");
    autoAnalyzeCheckBox.setSelected(true);
    toolbar.add(autoAnalyzeCheckBox);

    JButton logsButton = new JButton("Logs");
    logsButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        LogsDialog logsDialog = new LogsDialog(MainFrame.this);
        logsDialog.setVisible(true);
      }
    });
    toolbar.add(logsButton);

    getContentPane().add(toolbar,
        new java.awt.GridBagConstraints(0, 0, 1, 1, 1, 0, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    // tabs
    JTabbedPane tabbedPane = new JTabbedPane();

    longestSurvivorsPanel = new CladeListPanel(this, new CladeChartManager());
    tabbedPane.add("Longest survivors", longestSurvivorsPanel);

    mostPopulousCladesPanel = new CladeListPanel(this, new CladeChartManager());
    tabbedPane.add("Most populous clades", mostPopulousCladesPanel);

    heatMapPanel = new HeatMapPanel();
    tabbedPane.add("Heat map", heatMapPanel);

    getContentPane().add(tabbedPane,
        new java.awt.GridBagConstraints(0, 1, 1, 1, 1, 1, java.awt.GridBagConstraints.NORTHWEST,
            java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    // action listeners
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
        db.getLongestSurvivors(longestSurvivorsPanel.getLimit()).then(cladeSummaries -> {
          java.awt.EventQueue.invokeLater(() -> {
            longestSurvivorsPanel.setCladeList(cladeSummaries, db, maxTime);
          });
        }).onError(e -> {
          System.out.println("Error getting clade summaries: " + e);
          e.printStackTrace();
        });

        db.getMostPopulousClades(maxTime, mostPopulousCladesPanel.getLimit()).then(mostPopulousClades -> {
          java.awt.EventQueue.invokeLater(() -> {
            mostPopulousCladesPanel.setCladeList(mostPopulousClades, db, maxTime);
          });
        }).onError(e -> {
          System.out.println("Error getting clade summaries: " + e);
          e.printStackTrace();
        });

        heatMapPanel.setDB(db);
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
          DB newDB = new DB(bioFile);
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
          setTitle("DB: " + relativePath(db.getDbFile().getFile()) + "  MaxTime: " + maxTime);

          refreshTabs();
        } catch (ClassNotFoundException | SQLException | JsonIOException | JsonSyntaxException
            | IOException e) {
          System.out.println("Error opening database: " + e);
          e.printStackTrace();
        }
      }
    }.start();
  }

  private void reanalyzeDB() {
    if (db == null || analyzeInProgress) {
      return;
    }
    analyzeInProgress = true;
    new Thread() {
      public void run() {
        try {
          int oldMaxTime = db.getMaxTime();
          ProgressMonitor progressMonitor = new ProgressMonitor(MainFrame.this, "Reanalyzing database", "", 0, 100);
          Analyzer.analyze(db.getDbFile(), db, progressMonitor);
          progressMonitor.close();

          maxTime = db.getMaxTime();

          if (maxTime != oldMaxTime) {
            setTitle("DB: " + relativePath(db.getDbFile().getFile()) + "  MaxTime: " + maxTime);
            refreshTabs();
          }

        } catch (SQLException | JsonIOException | JsonSyntaxException | IOException e) {
          System.out.println("Error reanalyzing database: " + e);
          e.printStackTrace();
        } finally {
          analyzeInProgress = false;
        }
      }
    }.start();
  }

  private String relativePath(File file) {
    return new File(".").getAbsoluteFile().getParentFile().toPath().relativize(file.getAbsoluteFile().toPath())
        .toString();
  }
}
