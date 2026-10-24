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
  `native` methods get a hand-written body from `core/src/native/`
  (`World.getDistinctCladeIDCount`, a stream pipeline, and
  `ParallelExecutor.progressAllOrganismsInParallel`, Java's thread
  machinery; see Threads). Constructs it doesn't support
  are listed in `core/src/generated/unsupported.txt` and become
  `unimplemented!()`, so that file is the to-do list.
- `core/src/generated/`: output. Committed so upstream changes show up as a
  reviewable Rust diff, but never edited by hand.
- `core/src/rt/`: hand-written Java runtime: `JRef` (shared, nullable,
  mutable objects), arrays/lists, statics (`JStatic`), `java.util.HashSet` (ported from the JDK's
  `HashMap`, tree bins included), `Collection`/`Iterator`, lambdas (`JFn`),
  GUI stubs, `java.util.Random` (bit-exact, including
  `nextGaussian` with fdlibm's `log`), `Math` edge cases (`max`/`min` with
  NaN and -0.0, `round`), `Color` as a value, the dump and its loader, and
  what threads need (`parallel`: monitors, deferred drops; see Threads).
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

- Every object is an `Arc<JCell<_>>` (shared between threads as in Java,
  see Threads). Fields are read with
  `obj.get(|o| o.f)` and written with `obj.setf(|o| &mut o.f, v)`. No borrow
  outlives a field access, so methods can call back into the same object
  freely, as in Java. Because of that, `JCell` is an `UnsafeCell` (a field
  access is a plain load or store); the `checked` cargo feature counts
  borrows like a `RefCell` and panics on an overlap, and `verify.sh` runs a
  world with it.
- An array is one allocation, `Arc<JCell<[T]>>`, like Java's (a `List` is a
  `Vec`). `x1[i]` on a field reads the element inside the field access,
  `this.get(|__o| __o.x1.at(i))`, without cloning the array's `Arc`, when
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
  and `getP1()`'s `Point2D`s are the same few objects every time. The slots
  are per thread (`thread_local!`).
- A call's receiver isn't cloned when it is a field of `this` that nothing
  assigns outside its class's constructors (`_world`, J2R checks every
  assignment in the parsed sources), or a static that nothing assigns
  after its initializer (`Utils.random`): `this.get(|o| unsafe {
  o._world.alias() })` borrows it (`JAlias`, a copy of the reference that
  is never dropped), as `this` lives through the method. Other fields of
  `this` (`_geneticCode`, the world's `organismBuckets`) are borrowed only
  while threads run (`JShared`: overwritten references are kept until the
  threads are done then), cloned otherwise. With threads, every organism
  updating its world's count would make that count's cache line the
  bottleneck.
- Lambdas that don't escape: a parameter of a functional interface type
  that the method only calls (or passes to another such parameter), and
  that every caller passes a lambda, is a `&dyn Fn`; the lambda is a
  closure on the caller's stack borrowing what it captures (no `Arc`, no
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
  the same seed and parameters, that app and `biogenesis-rust` then show the
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
- Strings are `JStr` (`Arc<str>`, nullable). `+` and `+=` become one
  `JStr::cat` of all parts; `char` is a UTF-16 unit (`u16`) as in Java.
- Statics are globals (`JStatic`, initialized on first use, under a lock as
  that may happen on a worker thread) with getters/setters (`utils::random()`, `utils::set_MAX_MUTATION_RATE(..)`).
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

## Threads

`Utils.THREAD_COUNT` (Java's default 1; `world_check ... threads N`, the
GUI's Simulation menu) > 1 moves organisms on that many threads, with
Java's `ParallelExecutor` scheme: the world's bucket columns are split in
ranges, one per thread, and each thread holds locks on the columns near
the one it is in, so organisms that touch never move at the same time
(`core/src/native/parallel_executor.rs`, hand-written: it's Java's
thread machinery, `WorkerThread`, `LinesLocker`; `ThreadStartIndexes`,
which balances the ranges, is translated). As in Java, a world then takes
its own course on each run: only THREAD_COUNT 1 is reproducible and
compared with Java (`verify.sh`). A save made with threads loads anywhere
(it keeps THREAD_COUNT; `threads 1` continues it identically in Java and
Rust).

What Java gives that code, `rt/parallel.rs` provides, while the threads
run (`parallel::active()`; otherwise all of it is a branch on a flag, so
the serial simulation keeps its speed and its bits):

- `synchronized`: `static final Object x_monitor = new Object()` becomes a
  `JMonitor` (reentrant, spinning: the atmosphere's are held for two
  additions), `synchronized (x)` a guard on it; other objects' monitors
  (`synchronized (_organisms)`, synchronized methods) come from a table
  keyed by address.
- `Collections.synchronizedSet`: every operation on a collection that has a
  monitor takes it.
- `java.util.Random` is thread-safe: the seed is an atomic, updated with
  Java's compare-and-set loop (a plain store when serial).
- The garbage collector: a thread may read a reference field (statistics'
  `lastBornBeing`) while another overwrites it. An overwritten reference
  isn't dropped while the threads run, but kept until they're done, so a
  racing read always finds the object alive; the overwrite itself is under
  a striped lock, so two writers can't both drop the old value.

Plain fields (ints, doubles) race as in Java: a reader sees the old or the
new value (Rust calls a data race undefined; the generated code does plain
loads and stores, as the JVM does). Collections other than synchronized
ones aren't protected, as in Java: the simulation doesn't change shared
lists while organisms move. A panic in a worker ends the process (the
other threads may wait for its columns).

The atmosphere: every organism breathes every frame (`World.respiration`,
about 13,000 calls a frame in the default world, `photosynthesis` 4,300),
and each call used to take `_O2_monitor` and a second monitor, so threads
spent most of their time passing those between cores. Now a worker thread
collects its changes to the atmosphere in an `AtmosphereChanges` (a
`ThreadLocal` in `World`, translated to `rt::JThreadLocal`) and adds them to
the world after every line of buckets (`World.flushAtmosphereChanges`, a few
hundred monitor operations a frame). The transfers that don't depend on the
atmosphere's amounts (respiration, decomposition, `add*`...) are exact but
for rounding order; the draws that do (`photosynthesis` and the like, capped
by the amount of their gas) still take that gas's monitor, but not
`_O2_monitor`, and see the other threads' changes up to a line late (a line
is about 1 CO2 of the ~900 in the air, of ~100 a frame moved). On the
simulation thread (THREAD_COUNT 1) nothing is collected: the same code as
before, bit for bit.

Speed (M1 Pro: 8 performance cores; the GUI's default world, 7200x7200,
12,500 organisms, 600 frames, back to back, including the 2 s
single-threaded `genesis()`):

    threads              1      4      6      8
    Rust                 8.4 s  4.7 s  4.2 s  4.0 s
    Rust, before         8.2 s  6.2 s  6.2 s  6.3 s

From a saved world (no `genesis()`, no loading), 400 frames take 5.1 s
on one thread, 1.9 s on 4, 1.45 s on 8 (2.8 s and 3.0 s before the
atmosphere change); Java (perf patches, warmed up) takes 4.8 s, about
2.3 s, about 2 s. Switching monitors off altogether would gain only 3-4%
more. What's left at more
threads: the machine (with every core busy, threads land on slower or
shared cores), the ranges' balance (the slowest thread takes ~25% longer
than the average in a frame) and ~1 ms a frame of serial work (filling the
buckets). Tried and dropped, all slower here than the column ranges: 2D
tiles in 4 phases with work stealing, ranges recomputed every frame from
organism counts or times, filling the buckets on all threads, spinning
instead of sleeping while waiting. Found on the way: monitors side by side
in memory shared a cache line (now each has its own, as do
`parallel::active()`'s flag and `Random`), and on macOS `yield_now` can
take a waiting thread off its core for milliseconds. The machine's speed
drifts by 25% within minutes while other apps run: compare alternating
runs only.

## The GUI

`gui/` is an [egui](https://github.com/emilk/egui) window (eframe, and
egui_plot for the charts): the world, a status line, menus (new world, open,
save; run, pause, step; zoom) and a statistics window with Java's charts
(population, clades, atmosphere, generations) and numbers.

As in Java, the simulation runs on its own thread (`gui/src/sim.rs`), and
with THREAD_COUNT > 1 the organisms move on the executor's threads (the
Simulation menu's "threads", 1 by default as in Java, or
`BIOGENESIS_THREADS`). The window gets copies: the lines of the organisms in
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
`BIOGENESIS_SCREENSHOT=<dir> [BIOGENESIS_STATS=1] biogenesis-rust [save]`
keeps the window hidden (no focus, no dock icon, the statistics window drawn
inside the main one) and writes `<dir>/main.ppm` after 8 seconds
(`sips -s format png main.ppm --out main.png`). Quit it with `pkill
biogenesis-rust`.
`BIOGENESIS_PAINT_HIDDEN=1` draws the hidden window anyway,
`BIOGENESIS_LOG_FPS=1` prints the frames per second to stderr (and the ms a
frame spent on the world, on drawing and on the statistics), and
`BIOGENESIS_DELAY_MS` sets the ms per frame (0: no limit). The default world
(7200x7200, 12,500 organisms, about 15,000 after 1,000 frames) runs at
50-55 frames/s without the limit, repainted at 1:1 at 50, and with the
whole world in view at 42 (drawing all organisms each frame).

`biogenesis-rust <seed>` makes a new world with that seed, as `java -jar
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
almost nothing, the GUI shows it (with charts), and organisms move on
several threads as in Java (see Threads). Next candidates: more of the
GUI (2), then gzip saves (3) and CI (4).

Picking up: `rust/transpile.sh && rust/verify.sh` must print only "same"
lines (about 4 minutes). Speed baseline, to compare against after changes:

    java --add-opens java.base/java.util=ALL-UNNAMED -cp rust/build/java-classes:lib/gson-2.10.1.jar \
        biogenesis.WorldCheck new 3 3000 3000 3000 2000 every 0
    rust/core/target/release/world_check new 3 3000 3000 3000 2000 every 0   # 33 s (Java 41 s)
    # both print: frame 2000 population 13213 organisms 13760 hash 610eabb4552699a7
    # (82c26c3d... before the saves included ThreadStartIndexes' statics)

When a world run differs, `full N` prints the whole state of frame N on
both sides (as a save file); diff the two to find the first wrong field.


1. Performance: single-threaded, Rust is about 20% faster than Java, and
   the time goes where it goes in Java (see Status). Left of the
   translation's own overhead: `Arc` count updates when references are
   passed by value. A sin/cos table would save at most the 5% fdlibm
   takes, and change the simulation (Java would need the same table).
   Threads stop gaining at 4 because of the atmosphere's monitors (see
   Threads): more needs a change of the Java code (per-thread deltas, or
   fewer atmosphere calls), which is Marco's model.
2. The GUI: see the list at the end of The GUI. Selecting an organism
   needs `World.findOrganismFromPosition` (translated) and
   `GeneticCode.draw` (skipped now; with `rt::Graphics` it would translate
   like `Organism.draw`).
3. Compressed saves (gzip): the text format compresses well.
4. CI: a workflow that runs `transpile.sh`, `cargo build`, `verify.sh` on every
   upstream drop and opens a PR with the regenerated `generated/`.
