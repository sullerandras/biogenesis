package biogenesis;

/**
 * A custom listener that allows interested parties to listen to events when the `drawWorld` flag changes.
 * This listener could be used e.g. to redraw the whole visible part of the world when the flag changed.
 */
public interface RepaintWorldChangeListener {
  /** Indicates that the `drawWorld` flag has been changed. The new value is passed in as a parameter. */
  public void drawWorldChanged(boolean value);
}
