package biogenesis;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LogCollector extends OutputStream {
  private static final LogCollector instance = new LogCollector(System.out);

  private final StringBuilder sb = new StringBuilder();
  private final StringBuilder partialLine = new StringBuilder();
  private final OutputStream out;
  private final List<TextAppendListener> listeners = new ArrayList<>();

  public interface TextAppendListener {
    void textAppended(String text);
  }

  private LogCollector(OutputStream out) {
    this.out = out;
  }

  public static LogCollector getInstance() {
    return instance;
  }

  @Override
  public synchronized void write(int b) throws java.io.IOException {
    partialLine.append((char) b);
    out.write(b);
    if (b == '\n') {
      final String line = partialLine.toString();
      partialLine.setLength(0);
      for (TextAppendListener listener : listeners) {
        listener.textAppended(line);
      }
      sb.append(line);
      if (sb.length() > 50000) {
        sb.delete(0, 20000);
      }
    }
  }

  public String getOutput() {
    return sb.toString();
  }

  public void addTextAppendListener(TextAppendListener listener) {
    listeners.add(listener);
  }

  public void removeTextAppendListener(TextAppendListener listener) {
    listeners.remove(listener);
  }
}
