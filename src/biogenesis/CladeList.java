package biogenesis;

import java.util.ArrayList;

public class CladeList extends ArrayList<Clade> {
  public void print(int indentation) {
    for (Clade clade : this) {
      clade.print(indentation);
    }
  }
}
