package biogenesis;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Stores a 2D array of buckets, and stores Organisms in one or more buckets that the organism touches.
 *
 * For example if `bucketSize` is 100 then the buckets will be organised like this:
 * <pre>
 *   (0,0)      (100,0)     (200,0)     (300,0)
 *    +-----------+-----------+-----------+---...
 *    |           |           |           |
 *    |bucket0:0  |bucket0:1  |bucket0:2  |
 *    |           |           |           |
 *    |(100,0)    |(100,100)  |(200,100)  |
 *    +-----------+-----------+-----------+---...
 *    |           |           |           |
 *    |bucket1:0  |bucket1:1  |bucket1:2  |
 *    |           |           |           |
 *    ...
 * </pre>
 */
public class OrganismBuckets {
  private final int bucketSize;
  private final int maxWidth;
  private final int maxHeight;
  private final Collection<Organism>[][] buckets;

  public OrganismBuckets(final int mapWidth, final int mapHeight, final int bucketSize) {
    this.bucketSize = bucketSize;

    this.maxWidth = (mapWidth + bucketSize - 1) / bucketSize;
    this.maxHeight = (mapHeight + bucketSize - 1) / bucketSize;
    this.buckets = new Collection[maxHeight + 1][maxWidth + 1];
    for (int y = 0; y <= maxHeight; y++) {
      for (int x = 0; x <= maxWidth; x++) {
        buckets[y][x] = new ArrayList<>();
      }
    }
  }

  /**
   * Inserts the organism into the grid.
   * @param o
   */
  public void insert(Organism o) {
    final int minx = Math.max(0, (int) (o.getMinX() / (double) bucketSize));
    final int miny = Math.max(0, (int) (o.getMinY() / (double) bucketSize));
    final int maxx = Math.min(maxWidth, (int) (o.getMaxX() / (double) bucketSize));
    final int maxy = Math.min(maxHeight, (int) (o.getMaxY() / (double) bucketSize));

    if (minx == maxx && miny == maxy) {
      buckets[miny][minx].add(o);
      return;
    }

    for (int y = miny; y <= maxy; y++) {
      for (int x = minx; x <= maxx; x++) {
        buckets[y][x].add(o);
      }
    }
  }

  /**
   * Matcher that is used in {@link #findFirst(Organism, OrganismMatcher)}.
   */
  public interface OrganismMatcher {
    /**
     * Returns true if the given organism matches the criteria. Usually used for
     * collision detection.
     * @param o The organism to test.
     * @return True if the organism matches the criteria.
     */
    boolean match(Organism o);
  }

  /**
   * Iterates through all organisms that potentially overlap the given organism `o`.
   * The iteration stops when the given <pre>matcher</pre> returns true and the
   * matching organism is returned.
   * @param o The organism to determine which buckets to search. Only using the bounding
   *          box of the organism.
   * @return The first organism that matches the given matcher, or null if no match.
   */
  public Organism findFirst(Organism o, OrganismMatcher matcher) {
    final int minx = Math.max(0, (int) (o.getMinX() / (double) bucketSize));
    final int miny = Math.max(0, (int) (o.getMinY() / (double) bucketSize));
    final int maxx = Math.min(maxWidth, (int) (o.getMaxX() / (double) bucketSize));
    final int maxy = Math.min(maxHeight, (int) (o.getMaxY() / (double) bucketSize));

    if (minx == maxx && miny == maxy) {
      for (Organism match : buckets[miny][minx]) {
        if (matcher.match(match)) {
          return match;
        }
      }
      return null;
    }

    for (int y = miny; y <= maxy; y++) {
      for (int x = minx; x <= maxx; x++) {
        for (Organism match : buckets[y][x]) {
          if (matcher.match(match)) {
            return match;
          }
        }
      }
    }

    return null;
  }

  public Collection<Organism> getBucket(int x, int y) {
    // if (x < 0 || x > maxWidth || y < 0 || y > maxHeight) {
    //   return new ArrayList<>();
    // }
    return buckets[y][x];
  }

  public int getMaxWidth() {
    return maxWidth;
  }

  public int getMaxHeight() {
    return maxHeight;
  }

  public int getBucketSize() {
    return bucketSize;
  }
}
