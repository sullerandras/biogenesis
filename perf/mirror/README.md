# Collision mirror

A build-time optimization of `World.checkHit()`, applied to `build-src/` (the
copy the Makefile compiles) so `src/` stays identical to upstream. The
simulation result is unchanged: single-threaded runs give the same world
checksum with and without it. About 8-16% faster per frame in the headless
benchmark (1 and 4 threads), measured on seven different worlds.

Enabled by `mirror` in `PERF_PATCHES` (on by default). `make
PERF_PATCHES=sym,contact ...` builds without it.

## What it does

Collision detection reads the bounds and segments of each nearby organism,
about 15k candidates per frame, each scattered over several heap objects.
`CollisionMirror` keeps a copy of exactly those fields in flat arrays:

- bounds (`x`, `y`, `width`, `height`, from `java.awt.Rectangle`)
- `_centerX`, `_centerY`
- the rotated segments `x1`, `y1`, `x2`, `y2` (and `_segments`)

`World.checkHit(org1)` becomes `organismBuckets.findFirstHit(org1)`. That runs
the same matcher over the same candidates, in the same order, reading the
candidates from the mirror. It also skips `contact()` when no segment pair can
intersect. `contact()` has no side effects in that case, so the result is
identical (see "Checks").

The Organism objects stay the source of truth (save files, GUI and statistics
use them). Each organism gets a mirror slot the first time it is inserted into
the buckets and keeps it across frames. After every write of a mirrored field,
a `mirrorSyncBounds/Center/Segments()` call copies the new values into the
slot.

## Files

- `MirrorTransform.java`: the source transform (JavaParser with type
  resolution). The Makefile fetches JavaParser into `perf/tools/` on first
  use (`fetch-tools.sh`, pinned SHA-256) and compiles the tool there.
- `CollisionMirror.java`: copied into `build-src/biogenesis/`.
- `Organism.members`, `World.members`, `OrganismBuckets.members`: appended
  to those classes.
- `expected/`: the normalized bodies of `World.checkHit()` and
  `OrganismBuckets.findFirst()` that `findFirstHit()` copies.

After a build, `build-src/mirror-report.txt` lists every sync call and why
it is there.

## What the transform does

1. Finds every write of a mirrored field, in every source file, using
   resolved types: assignments, `++`/`--`, array element writes, Rectangle
   mutator calls on an Organism (`setBounds`, `setLocation`, `translate`,
   ...), and any use of `x1`/`y1`/`x2`/`y2` other than `x1[i]` or
   `x1.length` (an alias could write through it).
2. Inserts the sync call after the statement, on the same line, so line
   numbers in `build-src` match `src`. For writes through `this`, the sync
   moves out of enclosing loops and blocks while they can't read the mirror
   (no call that reaches `checkHit`, found by method name, transitively) and
   can't leave early (`return`, `throw`, `break`/`continue` to an outer
   statement). That is how the loop in `calculateBounds()` gets one sync
   after the loop instead of one per segment.
3. Replaces the body of `World.checkHit()`, attaches the mirror in
   `World.time()` right after the buckets are created, and hooks
   `OrganismBuckets.insert()`.

## Checks (the build stops with a message)

- A write it can't place safely: a receiver other than `this`, a variable or
  a parameter (`a[0]._centerX = ...`), a write inside a loop header or
  condition, a write from a nested or anonymous class, or a subclass of
  Organism.
- `contact()` acts outside a segment-intersection guard. Every statement with
  a side effect must be inside
  `if (... intersectsLine(B) && L.intersectsLine(B))`. Here `B` is set to a
  segment of `org` right before it. That `if` must be inside
  `if (org.intersectsLine(L))`, with `L` set to a segment of `this` right
  before it. `CollisionMirror.mayContact()` tests all segment pairs with these
  predicates, so it never says "no" when `contact()` would act.
- `World.checkHit()` or `OrganismBuckets.findFirst()` changed. Check
  `findFirstHit()` in `OrganismBuckets.members` against the new code, then
  paste the normalized body the error prints into `expected/`.

What it can't see statically is an Organism changed through a variable of
another type (`Rectangle r = organism; r.setLocation(...)`). Run a few worlds
with `-Dbiogenesis.mirrorcheck=true` after each upstream drop. It compares
every slot with its organism on every read and throws on any difference:

    make benchmark FRAMES=1     # builds classes/ including the benchmark
    java -Djava.awt.headless=true -Dbiogenesis.mirrorcheck=true \
      -cp lib/gson-2.10.1.jar:classes biogenesis.Benchmark runs/1.bgw.gz 300 1

Then compare the 1-thread checksum with `PERF_PATCHES=sym,contact`.
