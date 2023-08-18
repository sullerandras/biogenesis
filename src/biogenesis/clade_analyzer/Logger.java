package biogenesis.clade_analyzer;

import java.util.ArrayList;
import java.util.List;

public class Logger {
  private static final StringBuilder sb = new StringBuilder();
  private static final List<TextAppendListener> listeners = new ArrayList<>();

  public interface TextAppendListener {
    public void textAppended(String text);
  }

  public static void println(String s) {
    append(s);
    append("\n");
  }

  public static void print(String s) {
    append(s);
  }

  public static void printStackTrace(Exception e) {
    println(e.toString() + ":");
    for (StackTraceElement ste : e.getStackTrace()) {
      println("\tat " + ste.toString());
    }
  }

  private static void append(String s) {
    synchronized (sb) {
      sb.append(s);
      System.out.print(s);
      for (TextAppendListener listener : listeners) {
        listener.textAppended(s);
      }
      if (sb.length() > 50000) {
        sb.delete(0, 20000);
      }
    }
  }

  public static String getOutput() {
    return sb.toString();
  }

  public static void addTextAppendListener(TextAppendListener listener) {
    listeners.add(listener);
  }

  public static void removeTextAppendListener(TextAppendListener listener) {
    listeners.remove(listener);
  }
}
