package biogenesis.clade_analyzer.gui;

import java.util.ArrayList;
import java.util.List;

import biogenesis.clade_analyzer.CladeDetails;

/**
 * A list of ClickCladeListener objects.
 */
public class ClickCladeListenerList {
  private final List<ClickCladeListener> listeners = new ArrayList<>();

  /**
   * Adds a listener to the list.
   */
  public void add(ClickCladeListener l) {
    listeners.add(l);
  }

  /**
   * Removes a listener from the list.
   */
  public void remove(ClickCladeListener l) {
    listeners.remove(l);
  }

  /**
   * Returns true if the list is empty.
   */
  public boolean isEmpty() {
    return listeners.isEmpty();
  }

  /**
   * Notifies all listeners that a clade was clicked.
   */
  public void notifyAll(CladeDetails cladeDetails) {
    listeners.forEach(l -> l.clickClade(cladeDetails));
  }
}
