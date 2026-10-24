# Rust translation (proof of concept)

The Java sources stay the only thing anyone edits. `rust/transpile.sh`
translates a list of Java classes to Rust mechanically, and `rust/verify.sh`
runs the Java and the Rust code on the same inputs and requires identical
output. The idea is that this can run in CI on every upstream drop.

    rust/transpile.sh    # src/ -> rust/core/src/generated (Java 25, JavaParser)
    rust/verify.sh       # Java vs Rust, must print only "same" lines
    (cd rust/core && cargo test)

The whole simulation is translated: a headless world runs in Rust, frame by
frame identical to Java, and saves and loads games (see Saved games):

    rust/core/target/release/world_check new 1 2000 2000 1000 5000 every 100 save 5000 my.save
    rust/core/target/release/world_check load my.save 1000 every 100

And it has a window (see The GUI):

    (cd rust/gui && cargo run --release)            # a new world, Java's default parameters
    (cd rust/gui && cargo run --release -- 42)      # the same, with random seed 42
    (cd rust/gui && cargo run --release -- my.save)  # a saved game

## Layout

- `transpiler/J2R.java`: the translator (JavaParser with type resolution).
  `transpiler/classes.txt` lists the classes it translates; static members of
  other classes (`Utils`) come in when used. `source` lines add files that
  are only parsed for their statics (the JDK's `StrictMath` and `FdLibm`,
  which `transpile.sh` takes from the JDK's `src.zip`, and `ParallelExecutor`),
  `static` lines force a static method in. `stub` lines name the GUI and
  networking types (see below), `skip` methods become a body that throws,
  `native` methods get a hand-written body from `core/src/native/` (only
  `World.getDistinctCladeIDCount`, a stream pipeline). Constructs it doesn't support
  are listed in `core/src/generated/unsupported.txt` and become
  `unimplemented!()`, so that file is the to-do list.
- `core/src/generated/`: output. Committed so upstream changes show up as a
  reviewable Rust diff, but never edited by hand.
- `core/src/rt/`: hand-written Java runtime: `JRef` (shared, nullable,
  mutable objects), arrays/lists, statics (`JStatic`), `java.util.HashSet` (ported from the JDK's
  `HashMap`, tree bins included), `Collection`/`Iterator`, lambdas (`JFn`),
  GUI stubs, `java.util.Random` (bit-exact, including
  `nextGaussian` with fdlibm's `log`), `Math` edge cases (`max`/`min` with
  NaN and -0.0, `round`), `Color` as a value, and the dump and its loader.
- `core/src/save.rs`: saved games (the world and the statics).
- `core/src/bin/world_check.rs`: the headless world runner.
- `gui/`: the window (egui), a separate crate so the core stays free of
  GUI dependencies.
- `core/src/samples/`: J2R output for `verify/j2rtest/Samples.java`, the
  translator's own test cases (fall-through, string conversions, wrapping
  arithmetic...). Add a case there when J2R learns a construct.
- `verify/`: the Java side of the comparisons (`WorldCheck.java` runs the
  same worlds, `Load.java` reads saves; `Dump.java` writes the same
  format as `rt/dump.rs`: fields sorted by name, doubles as raw bits, shared
  objects as `@id`, so aliasing is compared too).

## How Java semantics are kept

- Every object is an `Rc<JCell<_>>`. Fields are read with
  `obj.get(|o| o.f)` and written with `obj.setf(|o| &mut o.f, v)`. No borrow
  outlives a field access, so methods can call back into the same object
  freely, as in Java. Because of that, `JCell` is an `UnsafeCell` (a field
  access is a plain load or store); the `checked` cargo feature counts
  borrows like a `RefCell` and panics on an overlap, and `verify.sh` runs a
  world with it.
- An array is one allocation, `Rc<JCell<[T]>>`, like Java's (a `List` is a
  `Vec`). `x1[i]` on a field reads the element inside the field access,
  `this.get(|__o| __o.x1.at(i))`, without cloning the array's `Rc`, when
  the index can't change the object (no calls, assignments, `++`).
- Escape analysis, as HotSpot does it, for primitive arrays: a local
  `new double[2]` (or `{1.0, 1.5}`) that is only indexed, measured or passed
  to a parameter of the same kind is a `JStackArray` on the stack, and such
  parameters of static methods take `&impl JArr<T>`. FdLibm's `sin`, `cos`,
  `pow` allocated arrays on every call before (3/4 of all allocations).
- Objects are recycled: a constructor takes its object from a few slots
  per class (`JRecycler`), reusing one that only the slot refers to, reset
  to `default()` before the constructor runs. Nobody can tell it from a new
  object, so this needs no escape analysis. Java can't do it (it can't know
  an object is garbage until it collects). `contact`'s two `ExLine2DDouble`s
  and `getP1()`'s `Point2D`s are the same few objects every time.
- Lambdas that don't escape: a parameter of a functional interface type
  that the method only calls (or passes to another such parameter), and
  that every caller passes a lambda, is a `&dyn Fn`; the lambda is a
  closure on the caller's stack borrowing what it captures (no `Rc`, no
  copies). `checkHit`'s matcher made one for every organism every frame.
- Together these leave about 300 allocations a frame (new organisms'
  arrays) of the 32,000 before; `--features count-allocs` makes
  `world_check` print the count, and `BIOGENESIS_ALLOC_SITES=1` adds sampled
  call stacks (return addresses, for `atos`).
- The binaries use mimalloc (macOS's malloc is slow for Java's allocation
  rate).
- Rust evaluates a call's receiver and arguments left to right, which is
  Java's order for assignments and calls. That's why `setf` and `put` take the
  value as an argument.
- `overflow-checks = false` in all profiles: `+ - *` on i32/i64 wrap like Java.
  `/` and `%` use `wrapping_div`/`wrapping_rem` (Java's `MIN / -1`), and shifts
  use `wrapping_shl`/`wrapping_shr` (Java masks the shift count).
- Casts use `as`, which has Java's semantics (saturating, NaN to 0).
- `Math.sin`, `cos`, `atan2`, `log10`, `pow`... are `StrictMath`'s, translated
  from the JDK's `StrictMath.java`/`FdLibm.java` like any other code. HotSpot's
  `Math.sin`/`cos` don't give fdlibm's bits on arm64 (about 1 call in 16
  differs in the last bit, interpreted or compiled; on x86_64 `log`, `pow`,
  `exp` are intrinsics too), so Java's `Math` is not the same on every
  machine. The reference is `StrictMath`: `verify.sh` compiles the Java side
  with `Math.sin(` etc. replaced by `StrictMath.sin(`, and `make
  STRICT_MATH=1 run` builds the Java app so (with the perf patches too: they
  keep the world bit-identical, `Dump.java` skips the caches they add). From
  the same seed and parameters, that app and `biogenesis-gui` then show the
  same world. Java's `Math.x(` calls go through `rt::jmath`, which is
  `StrictMath`'s unless the `platform-math` cargo feature takes Rust's
  (`cargo run --release --features platform-math`): no faster on Apple
  Silicon (1000 frames of a time-55 world: 29.4 s strict, 29.6 s platform),
  maybe on x86_64; a world then takes its own course. Explicit
  `StrictMath.x(` calls stay strict.
- `java.awt` geometry (`Rectangle`, `Line2D.Double`, `Point`,
  `Point2D.Double`) is hand-ported in `rt/geom.rs`. Organism extends
  Rectangle and ExLine2DDouble extends Line2D.Double, so variables of those
  types are `JRef<dyn RectangleT>`/`JRef<dyn Line2DT>` (`JRef` takes unsized
  types) and the subclass keeps its base part in `__super`. The same
  mechanism can carry other class hierarchies later.
- Strings are `JStr` (`Rc<str>`, nullable). `+` and `+=` become one
  `JStr::cat` of all parts; `char` is a UTF-16 unit (`u16`) as in Java.
- Statics are globals (`JStatic`, initialized on first use, single-threaded)
  with getters/setters (`utils::random()`, `utils::set_MAX_MUTATION_RATE(..)`).
  Compile-time constants become `const`.
- Overloads get the arity appended (`GeneticCode::new1(parent)`,
  `new37(...)`). `clone` becomes `jclone`.

- `World._organisms` is a `HashSet`, and the order organisms move in each
  frame is its iteration order. `Organism.hashCode()` is its `_ID`, so the
  order is reproducible, if the set is Java's: `rt/hashset.rs` is the JDK's
  `HashMap` algorithm (table growth, resize split, bins that become
  red-black trees at 8 entries), checked against `java.util.HashSet` by
  `HashSetCheck`. `equals(Object)` methods take their own class.
- `java.awt.Graphics` is `&mut dyn rt::Graphics` (`setColor`, `drawLine`,
  `drawRect`): `Organism.draw` is translated, and the GUI implements the
  trait. `java.util.Map`/`HashMap` with int or long keys is `rt::JHashMap`,
  lookups only (`put`, `getOrDefault`, `containsKey`, `size`): iterating
  would need the JDK's order, as `HashSet` has, so J2R refuses it.
- The rest of the GUI (`VisibleWorldInterface`, `InfoToolbar`...) and the
  corridors are `stub` types: values are `rt::Stub`, calls do nothing and
  return null (as `benchmark/Benchmark.java`'s stubs do), and a call that
  would need a number throws. Arguments of such calls are still evaluated.
  A headless world has no corridors, so those calls aren't reached.
- Lambdas (`organismBuckets.findFirst(b1, org -> ...)`) are closures owning
  copies of what they capture, behind `JFn<dyn Fn(...)>`.
- Threads: `THREAD_COUNT` is 1 (the Java default), so `ParallelExecutor`
  takes its serial path; the parallel one is skipped.

## The GUI

`gui/` is an [egui](https://github.com/emilk/egui) window (eframe, and
egui_plot for the charts): the world, a status line, menus (new world, open,
save; run, pause, step; zoom) and a statistics window with Java's charts
(population, clades, atmosphere, generations) and numbers.

As in Java, the simulation runs on its own thread (`gui/src/sim.rs`), which
owns everything of the translated code (`Rc` objects and statics are
single-threaded). The window gets copies: the lines of the organisms in
view, recorded from the translated `Organism.draw` through `rt::Graphics`,
and the statistics (`findBestAliveBeings`, as Java's statistics window
calls it). Java's repaint settings are under View: repaint the world
always, only while the main window has the focus, or while any of the
app's windows has it; while repainting, a frame takes at least "ms per
frame" (Utils.DELAY, 20 ms: 50 frames/s, as in Java), otherwise the
simulation runs as fast as it can. The process opts out of App Nap, so a
window in the background doesn't slow the simulation down (a background
priority makes `world_check` 5 times slower).

Keys: drag, scroll or WASD (shift: faster) moves; Q/E or a trackpad pinch
zooms; R is 1:1, F fits the world; space pauses, `.` steps one frame.

For checking a change without a window on the screen:
`BIOGENESIS_SCREENSHOT=<dir> [BIOGENESIS_STATS=1] biogenesis-gui [save]`
keeps the window hidden (no focus, no dock icon, the statistics window drawn
inside the main one) and writes `<dir>/main.ppm` after 8 seconds
(`sips -s format png main.ppm --out main.png`). Quit it with `pkill
biogenesis-gui`.
`BIOGENESIS_PAINT_HIDDEN=1` draws the hidden window anyway,
`BIOGENESIS_LOG_FPS=1` prints the frames per second to stderr (and the ms a
frame spent on the world, on drawing and on the statistics), and
`BIOGENESIS_DELAY_MS` sets the ms per frame (0: no limit). The default world
(7200x7200, 12,500 organisms, about 15,000 after 1,000 frames) runs at
50-55 frames/s without the limit, repainted at 1:1 at 50, and with the
whole world in view at 42 (drawing all organisms each frame).

`biogenesis-gui <seed>` makes a new world with that seed, as `java -jar
biogenesis.jar <seed>` does. It is the world `world_check new <seed> 7200
7200 12500 ...` makes, so a headless run with `every`/`full` can follow it.
The Java app reads its saved parameters (the parameters dialog), this one
uses Java's defaults: a changed simulation parameter (say,
`DETRITUS_TO_CO2_DIVISOR`) makes the Java world another one.

Not there yet (Java has them): selecting an organism (its info and genetic
code), tracking, feeding/killing, the parameters dialog, remembered
settings.

## Saved games

A save file is the dump of the world followed by the simulation's non-final
statics (the parameters, and `Utils.random`'s state, so a loaded game
continues exactly): text, about 10 KB per organism. Java writes the same
bytes (`WorldCheck ... save N file`) and reads them back (`Load.java`), so a
game saved by either side continues identically in the other; `verify.sh`
checks both directions. It isn't Java serialization: old `.bgw` files can't
be loaded, but Java could convert them (load the `.bgw`, write this format).

A HashSet's capacity is saved (`[#id n HashSet(cap)`), and loading adds the
elements in iteration order, which rebuilds the same table. The exception
is a bin that had become a tree (8+ organisms whose IDs collide in the
table, rare): rebuilt, it may iterate in another order, so a game loaded
from that save would go on differently. `save` warns when that happens.

## Status

Verified identical to Java by `verify.sh`:

- The simulation: `Organism`, `World`, `WorldStatistics`, `OrganismBuckets`,
  `Vector2D`, `ExLine2DDouble`, `ParallelExecutor`'s serial path, `Gene`,
  `GeneticCode` and the parts of `Utils` they use (about 40,000 lines of
  Java). Seeded `genesis()` worlds, the hash of the whole world and the
  statics compared after every frame: 1000x1000 with 200 organisms for 600
  frames, 2000x1500 with 1000 organisms (growing to 4,300) for 1000 frames.
  Saves: byte-identical, and loading either side's save continues exactly.
- `java.util.HashSet`'s iteration order, on 200,000 random operations with
  colliding hash codes (tree bins).
- `GeneticCode` mutation, 10 runs of 20 lineages x 60 generations.
- `StrictMath`'s 18 fdlibm functions (`sin` ... `tanh`, 3,500 lines of JDK
  code), on 400,000 random arguments (NaN, infinities, huge values).
- The `java.awt` geometry port, on 600,000 random cases.
- The translator tests (`Samples`, `Item`, `Bag`).

Speed, single-threaded, a 3000x3000 world growing from 3,000 to 13,000
organisms in 2000 frames (same final state): Java 41 s, Rust 33 s, run
back to back (it was 74 s with `RefCell`, thread-local statics, two
allocations per array and FdLibm's arrays on the heap; 36 s before
recycling objects and borrowing lambdas). The machine's speed drifts by
10-20% over a session, so only compare runs made back to back.

Profiling: `samply record --save-only --unstable-presymbolicate -o prof.json.gz
world_check ...` works (outside a sandbox) and `samply load` shows it. Where
the time goes (self time): `Rectangle.intersectsLine` 25%, `checkHit`'s
bounding box test 19% (cache misses on the other organism), `contact` 11%,
`calculateBounds` 11%, fdlibm `sin`/`cos` 5%. That's the same work Java
does. Reading `this`'s fields once before `calculateBounds`' loop (they
can't change in it) was tried and gained under 1%.

## Not done / next steps

Where it stands (2026-10-24): the translation is complete and verified,
single-threaded Rust is ~20% faster than Java, and a frame allocates
almost nothing, and the GUI shows it (with charts). Next candidates:
multi-threading (item 1 below), more of the GUI (2), then gzip saves (3)
and CI (4).

Picking up: `rust/transpile.sh && rust/verify.sh` must print only "same"
lines (about 4 minutes). Speed baseline, to compare against after changes:

    java --add-opens java.base/java.util=ALL-UNNAMED -cp rust/build/java-classes:lib/gson-2.10.1.jar \
        biogenesis.WorldCheck new 3 3000 3000 3000 2000 every 0
    rust/core/target/release/world_check new 3 3000 3000 3000 2000 every 0   # 33 s (Java 41 s)
    # both print: frame 2000 population 13213 organisms 13760 hash 82c26c3df8ccda5a

When a world run differs, `full N` prints the whole state of frame N on
both sides (as a save file); diff the two to find the first wrong field.


1. Performance: single-threaded, Rust is about 20% faster than Java, and
   the time goes where it goes in Java (see Status). Left of the
   translation's own overhead: `Rc` count updates when references are
   passed by value. A sin/cos table would save at most the 5% fdlibm
   takes, and change the simulation (Java would need the same table). The
   multi-threaded executor is the big win left (Java's isn't deterministic
   either); it needs `Rc` to become something `Send`.
2. The GUI: see the list at the end of The GUI. Selecting an organism
   needs `World.findOrganismFromPosition` (translated) and
   `GeneticCode.draw` (skipped now; with `rt::Graphics` it would translate
   like `Organism.draw`).
3. Compressed saves (gzip): the text format compresses well.
4. CI: a workflow that runs `transpile.sh`, `cargo build`, `verify.sh` on every
   upstream drop and opens a PR with the regenerated `generated/`.
