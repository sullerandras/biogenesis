package biogenesis;

import java.awt.Graphics2D;
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

  public int getCladeCount() {
    return allClades.size();
  }

  public void print() {
    for (Clade clade : rootClades.values()) {
      clade.print(0);
    }
  }

  public void draw(Graphics2D graphics) {
    int x = 0;
    int y = 0;
    int width = 100;
    int height = 100;
    for (Clade clade : rootClades.values()) {
      int heightUsed = clade.draw(graphics, x, y, width, height);
      y += heightUsed;
    }
  }
}
