package biogenesis.gui;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import biogenesis.LogCollector;
import biogenesis.WindowManager;

public class LogsDialog extends javax.swing.JDialog implements LogCollector.TextAppendListener {
  private JTextArea textArea;

  public LogsDialog(java.awt.Frame parent) {
    super((JDialog) null, false); // pass in null parent so the logs window can go behind the other windows

    WindowManager.registerWindow(this, 800, 600, 0, 0);

    initComponents();
    LogCollector.getInstance().addTextAppendListener(this);
  }

  private void initComponents() {
    if (!java.awt.EventQueue.isDispatchThread()) {
      throw new RuntimeException("Not in dispatch thread");
    }

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Logs");
    setMinimumSize(new java.awt.Dimension(800, 600));
    getContentPane().setLayout(new java.awt.GridBagLayout());

    textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setFont(new java.awt.Font("Monospaced", 0, 12));
    textArea.setText(LogCollector.getInstance().getOutput());
    getContentPane().add(new JScrollPane(textArea), new java.awt.GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.CENTER, java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 0, 0, 0), 0, 0));

    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        LogsDialog.this.removeWindowListener(this);
        LogCollector.getInstance().removeTextAppendListener(LogsDialog.this);
      }
    });

    invalidate();
  }

  @Override
  public void textAppended(String text) {
    java.awt.EventQueue.invokeLater(() -> {
      textArea.append(text);
      textArea.setCaretPosition(textArea.getDocument().getLength());
    });
  }
}
