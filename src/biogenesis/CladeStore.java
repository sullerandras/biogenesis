package biogenesis;

import java.util.LinkedHashMap;
import java.util.Map;

public class CladeStore {
  private Map<CladeId, Clade> rootClades = new LinkedHashMap<>();
  private Map<CladeId, Clade> allClades = new LinkedHashMap<>();

  public void add(Clade clade) {
    allClades.put(clade.getId(), clade);

    CladeId parentId = clade.parentId();

    // add clade to first existing parent
    while (parentId != null) {
      Clade parent = allClades.get(parentId);
      if (parent != null) {
        parent.addChild(clade);
        return;
      }
      parentId = parentId.parentId();
    }

    // add to root clades if no living parent exists
    rootClades.put(clade.getId(), clade);
  }

  public void print() {
    for (Clade clade : rootClades.values()) {
      clade.print(0);
    }
  }
}
