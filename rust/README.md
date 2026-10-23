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
  other classes (`Utils`) come in when used. Constructs it doesn't support
  are listed in `core/src/generated/unsupported.txt` and become
  `unimplemented!()`, so that file is the to-do list.
- `core/src/generated/`: output. Committed so upstream changes show up as a
  reviewable Rust diff, but never edited by hand.
- `core/src/rt/`: hand-written Java runtime: `JRef` (shared, nullable,
  mutable objects), arrays/lists, `java.util.Random` (bit-exact, including
  `nextGaussian` with fdlibm's `log`), `Math` edge cases (`max`/`min` with
  NaN and -0.0, `round`), `Color` as a value, and the dump.
- `verify/`: the Java side of the comparison (`Dump.java` writes the same
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
- Statics are thread-locals with getters/setters (`utils::random()`,
  `utils::set_MAX_MUTATION_RATE(..)`). Compile-time constants become `const`.
- Overloads get the arity appended (`GeneticCode::new1(parent)`,
  `new37(...)`). `clone` becomes `jclone`.

## Status

Translated and verified: `Gene`, `GeneticCode` (except `draw`), and the
parts of `Utils` they use, about 4,000 lines of Java. `verify.sh` compares 10
runs (5 seeds, normal and high mutation rates, 20 lineages × 60 generations,
about 1.8M dump lines). They are identical, including which genes a child
shares with its parent. Changing one constant in the generated code makes the
comparison fail.

A trial run on `Organism` + `World` + `Vector2D` + `ExLine2DDouble` (34k lines)
reports 180 unsupported constructs. What they are:

| What | Count | Work |
|------|------:|------|
| `Integer.toHexString`, string `+`/`+=`, `toCharArray` (clade IDs) | ~55 | String support in rt (Java's int/double formatting) |
| `extends Rectangle` / `Line2D.Double`, `intersectsLine`, `setLine`, `setBounds` | ~25 | Base classes in rt, ported from the JDK's geometry code |
| `Math.sin/cos/atan2/log10/pow` | ~25 | fdlibm ports. **Caveat:** HotSpot uses intrinsics for some of these (Intel libm on x86_64), so Java itself may not give the same bits on every machine. Check on each platform, or decide which one is the reference. |
| `VisibleWorldInterface`, `InfoToolbar`, `WorldStatistics`, `Graphics`, `BufferedImage` | ~25 | GUI/statistics links: skip or stub (config `skip` lines) |
| Corridors, `OrganismBuckets`, `ParallelExecutor`, `Iterator`/`Collection` | ~20 | More classes in the list, plus collections in rt |
| `switch` fall-through | 4 | Translator: lower to a chain of labeled blocks |

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
