package biogenesis;

public class CladeId {
  private final String id;

  public CladeId(String id) {
    this.id = id;
  }

  public CladeId parentId() {
    // find last non hexadecimal character in id
    int i = id.length() - 1;
    while (i >= 0 && Character.digit(id.charAt(i), 16) != -1) {
      i--;
    }

    if (i < 0) {
      return null;
    }

    return new CladeId(id.substring(0, i));
  }

  public String getId() {
    return id;
  }

  public String toString() {
    return id;
  }

  public int hashCode() {
    return id.hashCode();
  }

  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (!(o instanceof CladeId)) {
      return false;
    }
    CladeId other = (CladeId) o;
    return id.equals(other.id);
  }
}
