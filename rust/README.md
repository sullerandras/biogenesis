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
  mutable objects), arrays/lists, `java.util.HashSet` (ported from the JDK's
  `HashMap`, tree bins included), `Collection`/`Iterator`, lambdas (`JFn`),
  GUI stubs, `java.util.Random` (bit-exact, including
  `nextGaussian` with fdlibm's `log`), `Math` edge cases (`max`/`min` with
  NaN and -0.0, `round`), `Color` as a value, and the dump and its loader.
- `core/src/save.rs`: saved games (the world and the statics).
- `core/src/bin/world_check.rs`: the headless world runner.
- `core/src/samples/`: J2R output for `verify/j2rtest/Samples.java`, the
  translator's own test cases (fall-through, string conversions, wrapping
  arithmetic...). Add a case there when J2R learns a construct.
- `verify/`: the Java side of the comparisons (`WorldCheck.java` runs the
  same worlds, `Load.java` reads saves; `Dump.java` writes the same
  format as `rt/dump.rs`: fields sorted by name, doubles as raw bits, shared
  objects as `@id`, so aliasing is compared too).

## How Java semantics are kept

- Every object is an `Rc<RefCell<_>>`. Fields are read with
  `obj.get(|o| o.f)` and written with `obj.setf(|o| &mut o.f, v)`. No borrow
  outlives a field access, so methods can call back into the same object
  freely, as in Java.
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
  with `Math.sin(` etc. replaced by `StrictMath.sin(`. Shipping the same
  replacement in the Java build would make Java runs reproducible across
  machines too.
- `java.awt` geometry (`Rectangle`, `Line2D.Double`, `Point`,
  `Point2D.Double`) is hand-ported in `rt/geom.rs`. Organism extends
  Rectangle and ExLine2DDouble extends Line2D.Double, so variables of those
  types are `JRef<dyn RectangleT>`/`JRef<dyn Line2DT>` (`JRef` takes unsized
  types) and the subclass keeps its base part in `__super`. The same
  mechanism can carry other class hierarchies later.
- Strings are `JStr` (`Rc<str>`, nullable). `+` and `+=` become one
  `JStr::cat` of all parts; `char` is a UTF-16 unit (`u16`) as in Java.
- Statics are thread-locals with getters/setters (`utils::random()`,
  `utils::set_MAX_MUTATION_RATE(..)`). Compile-time constants become `const`.
- Overloads get the arity appended (`GeneticCode::new1(parent)`,
  `new37(...)`). `clone` becomes `jclone`.

- `World._organisms` is a `HashSet`, and the order organisms move in each
  frame is its iteration order. `Organism.hashCode()` is its `_ID`, so the
  order is reproducible, if the set is Java's: `rt/hashset.rs` is the JDK's
  `HashMap` algorithm (table growth, resize split, bins that become
  red-black trees at 8 entries), checked against `java.util.HashSet` by
  `HashSetCheck`. `equals(Object)` methods take their own class.
- The GUI (`VisibleWorldInterface`, `InfoToolbar`, `Graphics`...) and the
  corridors are `stub` types: values are `rt::Stub`, calls do nothing and
  return null (as `benchmark/Benchmark.java`'s stubs do), and a call that
  would need a number throws. Arguments of such calls are still evaluated.
  A headless world has no corridors, so those calls aren't reached.
- Lambdas (`organismBuckets.findFirst(b1, org -> ...)`) are closures owning
  copies of what they capture, behind `JFn<dyn Fn(...)>`.
- Threads: `THREAD_COUNT` is 1 (the Java default), so `ParallelExecutor`
  takes its serial path; the parallel one is skipped.

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
organisms in 2000 frames (same final state): Java 43 s, Rust 74 s. The
translation is exact but not yet fast; see below.

## Not done / next steps

Picking up: `rust/transpile.sh && rust/verify.sh` must print only "same"
lines (about 3 minutes). Speed baseline, to compare against after changes:

    java --add-opens java.base/java.util=ALL-UNNAMED -cp rust/build/java-classes:lib/gson-2.10.1.jar \
        biogenesis.WorldCheck new 3 3000 3000 3000 2000 every 0      # 43 s
    rust/core/target/release/world_check new 3 3000 3000 3000 2000 every 0   # 74 s
    # both print: frame 2000 population 13213 organisms 13760 hash 82c26c3df8ccda5a

When a world run differs, `full N` prints the whole state of frame N on
both sides (as a save file); diff the two to find the first wrong field.


1. Performance: `RefCell` checks and `Rc` clones on every access are the
   price of correctness so far. Now that it is exact, swap `JRef` internals
   (`UnsafeCell`, fewer clones of `this` fields), keep the collision checks'
   hot paths allocation-free, and re-verify. The multi-threaded executor
   is the other big win (Java's isn't deterministic either).
2. A front end: the Rust world has no GUI. Drawing (`Organism.draw` etc.)
   is skipped now; a renderer would read the world's state.
3. Compressed saves (gzip): the text format compresses well.
4. CI: a workflow that runs `transpile.sh`, `cargo build`, `verify.sh` on every
   upstream drop and opens a PR with the regenerated `generated/`.
