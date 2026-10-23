package biogenesis;

import java.awt.geom.Line2D;

/**
 * Flat-array copy of the organism fields that World.checkHit reads from collision
 * candidates (bounds, center, rotated segments), so the bucket scan doesn't chase
 * scattered heap objects. The Organism objects stay the source of truth.
 *
 * Copied into the build sources by perf/mirror/MirrorTransform, which also inserts
 * the mirrorSync*() calls after every write of a mirrored field (see
 * perf/mirror/README.md). Not part of upstream.
 *
 * Slots are persistent: an organism gets one the first time it is inserted into
 * the buckets and keeps it until the mirror is rebuilt (when slots of dead
 * organisms take up more than half of it).
 *
 * Per slot (stride REC): bounds x, y, width, height; _centerX, _centerY; offset
 * and count of its segments in seg[] (stride SEG: x1, y1, x2, y2 relative to the
 * center, then the integer box of the segment).
 *
 * Run with -Dbiogenesis.mirrorcheck=true to compare every slot with its organism
 * whenever it is read.
 */
final class CollisionMirror {
	static final boolean CHECK = Boolean.getBoolean("biogenesis.mirrorcheck");
	static final int REC = 8;
	static final int SEG = 8;

	Organism[] orgs = new Organism[1024];
	int[] rec = new int[1024 * REC];
	int[] segCap = new int[1024];
	int[] seg = new int[1024 * 8 * SEG];
	int size;
	int segSize;

	/**
	 * Returns the mirror to use this frame: the current one, or a fresh one when
	 * slots of dead organisms take up more than half of it.
	 */
	static CollisionMirror prepare(CollisionMirror m, int population) {
		if (m == null || m.size > 2 * population + 4096) {
			return new CollisionMirror();
		}
		return m;
	}

	/** The slot of o in this mirror, or -1. */
	int slotIfPresent(Organism o) {
		final int s = o._mirrorSlot;
		return o._mirror == this && orgs[s] == o ? s : -1;
	}

	/** Gives o a slot (if it has none in this mirror) and returns it. Not thread safe. */
	int slotOf(Organism o) {
		final int existing = slotIfPresent(o);
		if (existing >= 0) {
			return existing;
		}
		final int s = size++;
		if (s == orgs.length) {
			orgs = java.util.Arrays.copyOf(orgs, s * 2);
			rec = java.util.Arrays.copyOf(rec, s * 2 * REC);
			segCap = java.util.Arrays.copyOf(segCap, s * 2);
		}
		final int n = o._segments;
		if ((segSize + n) * SEG > seg.length) {
			seg = java.util.Arrays.copyOf(seg, Math.max(seg.length * 2, (segSize + n) * SEG));
		}
		orgs[s] = o;
		rec[s * REC + 6] = segSize;
		segCap[s] = n;
		segSize += n;
		o._mirrorSlot = s;
		o._mirror = this;
		syncBounds(o);
		syncCenter(o);
		syncSegments(o);
		return s;
	}

	void syncBounds(Organism o) {
		final int s = slotIfPresent(o);
		if (s < 0) {
			return;
		}
		final int b = s * REC;
		rec[b] = o.x;
		rec[b + 1] = o.y;
		rec[b + 2] = o.width;
		rec[b + 3] = o.height;
	}

	void syncCenter(Organism o) {
		final int s = slotIfPresent(o);
		if (s < 0) {
			return;
		}
		rec[s * REC + 4] = o._centerX;
		rec[s * REC + 5] = o._centerY;
	}

	void syncSegments(Organism o) {
		final int s = slotIfPresent(o);
		if (s < 0) {
			return;
		}
		final int n = o._segments;
		if (n > segCap[s]) {
			// _segments is only written while an organism is created, before it has a slot.
			throw new IllegalStateException("collision mirror: organism " + o._ID + " grew from "
					+ segCap[s] + " to " + n + " segments");
		}
		final int b = s * REC;
		int q = rec[b + 6] * SEG;
		final int[] x1 = o.x1, y1 = o.y1, x2 = o.x2, y2 = o.y2;
		for (int j = 0; j < n; j++, q += SEG) {
			final int a = x1[j], c = y1[j], e = x2[j], f = y2[j];
			seg[q] = a;
			seg[q + 1] = c;
			seg[q + 2] = e;
			seg[q + 3] = f;
			if (a == e && c == f) {
				// segBoxesOverlap() (perf_patch.py) passes point segments through: box covers everything
				seg[q + 4] = Integer.MIN_VALUE;
				seg[q + 5] = Integer.MAX_VALUE;
				seg[q + 6] = Integer.MIN_VALUE;
				seg[q + 7] = Integer.MAX_VALUE;
			} else {
				seg[q + 4] = Math.min(a, e);
				seg[q + 5] = Math.max(a, e);
				seg[q + 6] = Math.min(c, f);
				seg[q + 7] = Math.max(c, f);
			}
		}
		rec[b + 7] = n;
	}

	/** java.awt.Rectangle.intersects(Rectangle), with this = t and r = slot s. */
	boolean boxIntersects(Organism t, int s) {
		final int b = s * REC;
		int tw = t.width;
		int th = t.height;
		int rw = rec[b + 2];
		int rh = rec[b + 3];
		if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
			return false;
		}
		int tx = t.x;
		int ty = t.y;
		int rx = rec[b];
		int ry = rec[b + 1];
		rw += rx;
		rh += ry;
		tw += tx;
		th += ty;
		return ((rw < rx || rw > tx) &&
				(rh < ry || rh > ty) &&
				(tw < tx || tw > rx) &&
				(th < ty || th > ry));
	}

	/**
	 * False only if a.contact(orgs[s]) can't find an intersecting segment pair, in
	 * which case contact() returns false without side effects (MirrorTransform
	 * checks that contact() only acts under such a guard). Tests every segment pair,
	 * a superset of the pairs contact() tests, with contact()'s predicates:
	 * org.intersectsLine(line), segBoxesOverlap, intersectsLine(bline),
	 * line.intersectsLine(bline).
	 */
	boolean mayContact(Organism a, int s) {
		final int b = s * REC;
		final int cnt = rec[b + 7];
		if (cnt == 0) {
			return false;
		}
		final int rx = rec[b], ry = rec[b + 1], rw = rec[b + 2], rh = rec[b + 3];
		final int cx = rec[b + 4], cy = rec[b + 5];
		final int q0 = rec[b + 6] * SEG, q1 = q0 + cnt * SEG;
		final int[] seg = this.seg;
		final int[] ax1 = a.x1, ay1 = a.y1, ax2 = a.x2, ay2 = a.y2;
		final int acx = a._centerX, acy = a._centerY;
		final int ax = a.x, ay = a.y, aw = a.width, ah = a.height;
		for (int i = a._segments - 1; i >= 0; i--) {
			final int ix1 = ax1[i] + acx, iy1 = ay1[i] + acy, ix2 = ax2[i] + acx, iy2 = ay2[i] + acy;
			final double lx1 = ix1, ly1 = iy1, lx2 = ix2, ly2 = iy2;
			if (!rectIntersectsLine(rx, ry, rw, rh, lx1, ly1, lx2, ly2)) {
				continue;
			}
			// line box, in the candidate's center-relative coordinates
			final boolean point = ix1 == ix2 && iy1 == iy2;
			final int minX = Math.min(ix1, ix2) - cx, maxX = Math.max(ix1, ix2) - cx;
			final int minY = Math.min(iy1, iy2) - cy, maxY = Math.max(iy1, iy2) - cy;
			for (int q = q0; q < q1; q += SEG) {
				if (!point && (seg[q + 4] > maxX || seg[q + 5] < minX || seg[q + 6] > maxY || seg[q + 7] < minY)) {
					continue;
				}
				final double bx1 = seg[q] + cx, by1 = seg[q + 1] + cy, bx2 = seg[q + 2] + cx, by2 = seg[q + 3] + cy;
				if (rectIntersectsLine(ax, ay, aw, ah, bx1, by1, bx2, by2)
						&& Line2D.linesIntersect(bx1, by1, bx2, by2, lx1, ly1, lx2, ly2)) {
					return true;
				}
			}
		}
		return false;
	}

	private static final int OUT_LEFT = java.awt.geom.Rectangle2D.OUT_LEFT;
	private static final int OUT_TOP = java.awt.geom.Rectangle2D.OUT_TOP;
	private static final int OUT_RIGHT = java.awt.geom.Rectangle2D.OUT_RIGHT;
	private static final int OUT_BOTTOM = java.awt.geom.Rectangle2D.OUT_BOTTOM;

	/** java.awt.Rectangle.outcode(double, double). */
	private static int outcode(int rx, int ry, int rw, int rh, double x, double y) {
		int out = 0;
		if (rw <= 0) {
			out |= OUT_LEFT | OUT_RIGHT;
		} else if (x < rx) {
			out |= OUT_LEFT;
		} else if (x > rx + (double) rw) {
			out |= OUT_RIGHT;
		}
		if (rh <= 0) {
			out |= OUT_TOP | OUT_BOTTOM;
		} else if (y < ry) {
			out |= OUT_TOP;
		} else if (y > ry + (double) rh) {
			out |= OUT_BOTTOM;
		}
		return out;
	}

	/** java.awt.geom.Rectangle2D.intersectsLine(double, double, double, double) on a Rectangle. */
	private static boolean rectIntersectsLine(int rx, int ry, int rw, int rh,
			double x1, double y1, double x2, double y2) {
		int out1, out2;
		if ((out2 = outcode(rx, ry, rw, rh, x2, y2)) == 0) {
			return true;
		}
		while ((out1 = outcode(rx, ry, rw, rh, x1, y1)) != 0) {
			if ((out1 & out2) != 0) {
				return false;
			}
			if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
				double x = rx;
				if ((out1 & OUT_RIGHT) != 0) {
					x += (double) rw;
				}
				y1 = y1 + (x - x1) * (y2 - y1) / (x2 - x1);
				x1 = x;
			} else {
				double y = ry;
				if ((out1 & OUT_BOTTOM) != 0) {
					y += (double) rh;
				}
				x1 = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
				y1 = y;
			}
		}
		return true;
	}

	/** -Dbiogenesis.mirrorcheck=true: throws if slot s differs from its organism. */
	void check(int s) {
		final Organism o = orgs[s];
		final int b = s * REC;
		final int n = o._segments;
		boolean ok = rec[b] == o.x && rec[b + 1] == o.y && rec[b + 2] == o.width && rec[b + 3] == o.height
				&& rec[b + 4] == o._centerX && rec[b + 5] == o._centerY && rec[b + 7] == n;
		for (int j = 0, q = rec[b + 6] * SEG; ok && j < n; j++, q += SEG) {
			ok = seg[q] == o.x1[j] && seg[q + 1] == o.y1[j] && seg[q + 2] == o.x2[j] && seg[q + 3] == o.y2[j];
		}
		if (!ok) {
			throw new IllegalStateException("collision mirror out of date for organism " + o._ID
					+ " (a write of a mirrored field without a mirrorSync*() call?)");
		}
	}
}
