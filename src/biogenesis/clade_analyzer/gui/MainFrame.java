package biogenesis.clade_analyzer.gui;

import java.io.File;
import java.sql.SQLException;

import javax.swing.JButton;

import biogenesis.WindowManager;
import biogenesis.clade_analyzer.CladeSummary;
import biogenesis.clade_analyzer.DB;

public class MainFrame extends javax.swing.JFrame {
  private DB db = null;
  private int maxTime;

  public MainFrame() {
    WindowManager.registerWindow(this, 800, 600, 0, 0);

    initComponents();

    openDatabase(new File("test5"));
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
    fileChooser.setCurrentDirectory(new java.io.File("."));
    fileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
    int result = fileChooser.showOpenDialog(this);
    if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
      java.io.File backupDir = fileChooser.getSelectedFile();
      System.out.println("Selected backup directory: " + backupDir.getAbsolutePath());
      openDatabase(backupDir);
    }
  }

  private void openDatabase(File backupDir) {
    try {
      if (db != null) {
        db.close();
      }

      db = new DB(new File(backupDir, "clades.sqlite"));
      maxTime = db.getMaxTime();
      setTitle("DB: " + db.getDbFile().getAbsolutePath() + "  MaxTime: " + maxTime);
    } catch (ClassNotFoundException | SQLException e) {
      System.err.println("Error opening database: " + e);
      e.printStackTrace();
    }
  }
}
