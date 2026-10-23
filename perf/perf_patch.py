#!/usr/bin/env python3
"""
Mechanical performance patches for biogenesis. Re-runnable on fresh upstream
sources: every edit is anchored on a text pattern and fails loudly if the
pattern is not found, instead of silently producing a different program.

All patches keep the simulation bit-identical (single-threaded runs give the
same world checksum before and after).

The Makefile applies it to the UTF-8 copy of the sources (build-src/), so
src/ stays identical to upstream.

usage: perf_patch.py <src dir containing biogenesis/>
       PERF_PATCHES=sym,contact (default: all) selects patches;
       PERF_PATCHES=none applies nothing.
"""
import os
import re
import sys
from pathlib import Path

root = Path(sys.argv[1]) / "biogenesis"
ENABLED = set(os.environ.get("PERF_PATCHES", "sym,contact").split(","))


def edit(path, fn):
    p = root / path
    s = p.read_text(encoding="utf-8")
    s2 = fn(s)
    if s2 != s:
        p.write_text(s2, encoding="utf-8")


def sub(s, pattern, repl, count_min=1, flags=0):
    s2, n = re.subn(pattern, repl, s, flags=flags)
    if n < count_min:
        sys.exit(f"pattern not found ({n} < {count_min}): {pattern}")
    return s2


# ---------------------------------------------------------------------------
# 1. Organism.symmetric(): memoize the computed shape.
#
# For organisms without eyes, symmetric() is a pure function of
# (_geneticCode, _symmetry, _segments, _growthRatio). Starving organisms
# shrink every frame, collide, and move() undoes the shrink, so symmetric()
# is called twice per frame alternating between the same two growth ratios.
# Keep the last two results and copy them back instead of recomputing.
# Run with -Dbiogenesis.symcheck=true to verify every cache hit.
# ---------------------------------------------------------------------------
SYM_WRAPPER = r'''
	// ---- perf patch: symmetric() cache ----------------------------------
	private static final boolean SYM_CHECK = Boolean.getBoolean("biogenesis.symcheck");
	private static final class SymmetricShape {
		GeneticCode geneticCode;
		int growthRatio, symmetry, segments;
		int[] startX, startY, endX, endY;
		double[] m;
		double mass, inertia;
		int rx, ry, rw, rh;
	}
	private transient SymmetricShape _symCache0, _symCache1;

	private boolean symmetricShapeMatches(SymmetricShape c) {
		return c != null && c.geneticCode == _geneticCode && c.growthRatio == _growthRatio
			&& c.symmetry == _symmetry && c.segments == _segments
			&& c.startX.length == _startPointX.length && c.m.length == _m.length;
	}

	public void symmetric() {
		if (_haseyes) {
			symmetricCompute();
			return;
		}
		SymmetricShape c = symmetricShapeMatches(_symCache0) ? _symCache0
			: symmetricShapeMatches(_symCache1) ? _symCache1 : null;
		if (c != null) {
			if (_updateEffects == 0) {
				_updateEffects = 1;
			}
			System.arraycopy(c.startX, 0, _startPointX, 0, c.startX.length);
			System.arraycopy(c.startY, 0, _startPointY, 0, c.startY.length);
			System.arraycopy(c.endX, 0, _endPointX, 0, c.endX.length);
			System.arraycopy(c.endY, 0, _endPointY, 0, c.endY.length);
			System.arraycopy(c.m, 0, _m, 0, c.m.length);
			_mass = c.mass;
			_I = c.inertia;
			_sizeRect.setBounds(c.rx, c.ry, c.rw, c.rh);
			if (SYM_CHECK) {
				symmetricCompute();
				if (!java.util.Arrays.equals(c.startX, _startPointX) || !java.util.Arrays.equals(c.startY, _startPointY)
						|| !java.util.Arrays.equals(c.endX, _endPointX) || !java.util.Arrays.equals(c.endY, _endPointY)
						|| !java.util.Arrays.equals(c.m, _m) || java.lang.Double.compare(c.mass, _mass) != 0
						|| java.lang.Double.compare(c.inertia, _I) != 0 || c.rx != _sizeRect.x || c.ry != _sizeRect.y
						|| c.rw != _sizeRect.width || c.rh != _sizeRect.height) {
					throw new IllegalStateException("symmetric() cache mismatch for organism " + _ID);
				}
			}
			return;
		}
		symmetricCompute();
		c = _symCache1 != null ? _symCache1 : new SymmetricShape();
		_symCache1 = _symCache0;
		_symCache0 = c;
		c.geneticCode = _geneticCode;
		c.growthRatio = _growthRatio;
		c.symmetry = _symmetry;
		c.segments = _segments;
		c.startX = _startPointX.clone();
		c.startY = _startPointY.clone();
		c.endX = _endPointX.clone();
		c.endY = _endPointY.clone();
		c.m = _m.clone();
		c.mass = _mass;
		c.inertia = _I;
		c.rx = _sizeRect.x;
		c.ry = _sizeRect.y;
		c.rw = _sizeRect.width;
		c.rh = _sizeRect.height;
	}
'''

SEG_HELPER = r'''
	// ---- perf patch: cheap reject before exact segment intersection -----
	// Line2D.intersectsLine() treats a zero-length segment as intersecting every
	// collinear segment (two separate points always "intersect"); leave those
	// cases to it so results stay identical.
	private static boolean segBoxesOverlap(java.awt.geom.Line2D.Double a, java.awt.geom.Line2D.Double b) {
		if ((a.x1 == a.x2 && a.y1 == a.y2) || (b.x1 == b.x2 && b.y1 == b.y2)) {
			return true;
		}
		return Math.max(a.x1, a.x2) >= Math.min(b.x1, b.x2) && Math.max(b.x1, b.x2) >= Math.min(a.x1, a.x2)
			&& Math.max(a.y1, a.y2) >= Math.min(b.y1, b.y2) && Math.max(b.y1, b.y2) >= Math.min(a.y1, a.y2);
	}
'''


def append_to_class(s, code):
    # Insert before the closing brace of the (single, top-level) class.
    i = s.rstrip().rfind("}")
    return s[:i] + code + s[i:]


def patch_organism(s):
    if "sym" in ENABLED:
        s = sub(s, r"public void symmetric\(\) \{", "private void symmetricCompute() {")
        s = append_to_class(s, SYM_WRAPPER)

    # 2. contact(): segment-vs-segment tests. Line2D.intersectsLine is exact on
    # these integer coordinates, and two proper segments can only intersect if
    # their bounding boxes overlap, so testing the boxes first never changes a
    # result (degenerate point segments are passed through, see helper).
    if "contact" in ENABLED:
        s = append_to_class(s, SEG_HELPER)
        s = sub(s,
                r"if \(((?:org\.)?intersectsLine\((\w+)\)) && (\w+)\.intersectsLine\(\2\)\)",
                r"if (segBoxesOverlap(\3, \2) && \1 && \3.intersectsLine(\2))",
                count_min=6)
    return s


edit("Organism.java", patch_organism)
print("perf_patch.py: applied", ",".join(sorted(ENABLED & {"sym", "contact"})) or "nothing")
