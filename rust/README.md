# Rust translation (proof of concept)

The Java sources stay the only thing anyone edits. `rust/transpile.sh`
translates a list of Java classes to Rust mechanically, and `rust/verify.sh`
runs the Java and the Rust code on the same inputs and requires identical
output. The idea is that this can run in CI on every upstream drop.

    rust/transpile.sh    # src/ -> rust/core/src/generated (Java 25, JavaParser)
    rust/verify.sh       # Java vs Rust, must print only "same" lines
    (cd rust/core && cargo test)

## Layout

- `transpiler/J2R.java`: the translator (JavaParser with type resolution).
  `transpiler/classes.txt` lists the classes it translates; static members of
  other classes (`Utils`) come in when used. `source` lines add files that
  are only parsed for their statics (the JDK's `StrictMath` and `FdLibm`,
  which `transpile.sh` takes from the JDK's `src.zip`), `static` lines force
  a static method in. Constructs it doesn't support
  are listed in `core/src/generated/unsupported.txt` and become
  `unimplemented!()`, so that file is the to-do list.
- `core/src/generated/`: output. Committed so upstream changes show up as a
  reviewable Rust diff, but never edited by hand.
- `core/src/rt/`: hand-written Java runtime: `JRef` (shared, nullable,
  mutable objects), arrays/lists, `java.util.Random` (bit-exact, including
  `nextGaussian` with fdlibm's `log`), `Math` edge cases (`max`/`min` with
  NaN and -0.0, `round`), `Color` as a value, and the dump.
- `core/src/samples/`: J2R output for `verify/j2rtest/Samples.java`, the
  translator's own test cases (fall-through, string conversions, wrapping
  arithmetic...). Add a case there when J2R learns a construct.
- `verify/`: the Java side of the comparisons (`Dump.java` writes the same
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

## Status

Verified identical to Java by `verify.sh`:

- `Gene`, `GeneticCode` (except `draw`) and the parts of `Utils` they use,
  about 4,000 lines: 10 genetic code runs (5 seeds, normal and high mutation
  rates, 20 lineages × 60 generations, about 1.8M dump lines), including which
  genes a child shares with its parent.
- `StrictMath`'s 18 fdlibm functions (`sin` ... `tanh`, 3,500 lines of JDK
  code), on 400,000 random arguments (NaN, infinities, huge values).
- The `java.awt` geometry port, on 600,000 random cases.
- The translator tests (`Samples`).

Changing one constant in the generated code makes the comparison fail.

A trial run on `Organism` + `World` + `Vector2D` + `ExLine2DDouble` (34k lines)
compiles, and reports 56 unsupported constructs (down from 180: strings,
math, geometry and switch fall-through are done). What is left is mostly a
matter of deciding what the headless simulation includes:

| What | Count | Where | Plan |
|------|------:|------|------|
| `VisibleWorldInterface`, `InfoToolbar`, `Graphics`, `Dimension`, `BufferedImage` | ~20 | GUI callbacks in `World`/`Organism`, `draw`, `getImage` | No-ops, as the stubs in `benchmark/Benchmark.java`; `skip` for `draw`/`getImage` |
| `WorldStatistics` | ~12 | `addOrganism`, `organismHasDied`, `time`... | Translate the class |
| `Iterator`/`Collection`, `Stream.count` | ~12 | `World._organisms` loops | Collections in rt |
| `OrganismBuckets`, `ParallelExecutor` | ~7 | `checkHit` family, `time` | Translate; one thread |
| `InCorridor`/`OutCorridor` | ~8 | Networking (corridors) | Opaque stub that panics if reached (`_corridorexists` is false headless) |
| `readObject`, `hashCode`/`equals(Object)` | 3 | | `skip` / translator |

## Not done / next steps

1. The items in the table above, then a headless world runner in Rust and
   Java-vs-Rust world checksums per frame (the Java side already has `make
   benchmark` and the 1-thread checksum).
2. Loading `.bgw` saves (Java serialization) or a shared seeded world
   generator, so both programs can start from the same world.
3. Performance: `RefCell` checks and `Rc` clones on every access are the price
   of correctness here. Once it is exact, swap `JRef` internals (`UnsafeCell`,
   fewer clones of `this` fields) and re-verify.
4. CI: a workflow that runs `transpile.sh`, `cargo build`, `verify.sh` on every
   upstream drop and opens a PR with the regenerated `generated/`.
