#!/bin/sh
# Runs the Java and the translated Rust code on the same inputs and compares
# their outputs. Needs rust/transpile.sh to have run (it makes rust/build/src).
set -e
cd "$(dirname "$0")/.."
out=rust/build/verify
mkdir -p "$out"

# The Java side is the translated source with Math.sin etc. replaced by
# StrictMath's: J2R translates them from StrictMath, and HotSpot's Math.sin
# gives different bits on some platforms (arm64, x86_64), so StrictMath is
# the reference.
rm -rf rust/build/verify-src rust/build/java-classes
cp -R rust/build/src rust/build/verify-src
find rust/build/verify-src -name '*.java' -exec perl -pi -e \
	's/\bMath\.(sin|cos|tan|asin|acos|atan|atan2|exp|log|log10|log1p|expm1|pow|cbrt|hypot|sinh|cosh|tanh)\(/StrictMath.$1(/g' {} +
mkdir -p rust/build/java-classes
javac -nowarn -encoding UTF-8 -cp lib/gson-2.10.1.jar -sourcepath rust/build/verify-src -d rust/build/java-classes \
	rust/verify/biogenesis/*.java rust/verify/j2rtest/*.java 2>&1 | grep -v '^Note:\|warning: \[removal\]\|^\s\|^[0-9]* warnings$' || true
(cd rust/core && cargo build --release -q --bins)

fail=0
# Arguments for one side: @SIDE@ becomes java or rust, @OTHER@ the other one.
args_for() {
	side="$1"; other="$2"; shift 2
	for a; do printf '%s\n' "$a" | sed "s/@SIDE@/$side/g; s/@OTHER@/$other/g"; done
}
check() {
	name="$1"; java_main="$2"; rust_bin="rust/core/target/release/$3"; shift 3
	java --add-opens java.base/java.util=ALL-UNNAMED -cp rust/build/java-classes:lib/gson-2.10.1.jar "$java_main" $(args_for java rust "$@") > "$out/$name.java.txt"
	"$rust_bin" $(args_for rust java "$@") > "$out/$name.rust.txt"
	if cmp -s "$out/$name.java.txt" "$out/$name.rust.txt"; then
		echo "same  $name ($(wc -l < "$out/$name.java.txt" | tr -d ' ') lines)"
	else
		echo "DIFF  $name:"
		diff "$out/$name.java.txt" "$out/$name.rust.txt" | head -20
		fail=1
	fi
}
check samples j2rtest.SamplesCheck samples_check
for seed in 1 2; do
	check "math-$seed" biogenesis.MathCheck math_check "$seed" 200000
	check "geom-$seed" biogenesis.GeomCheck geom_check "$seed" 300000
done
for seed in 1 2 3 42 1234567; do
	check "genetic-$seed" biogenesis.GeneticCodeCheck genetic_code_check "$seed" 20 60
	check "genetic-hot-$seed" biogenesis.GeneticCodeCheck genetic_code_check "$seed" 20 60 hot
done
for seed in 1 2; do
	check "hashset-$seed" biogenesis.HashSetCheck hashset_check "$seed" 200000
done

# Whole worlds: a hash of the world and the statics (the save file text) after
# every frame. 600 frames pass two statistics updates (every 256 frames).
for seed in 1 2; do
	check "world-$seed" biogenesis.WorldCheck world_check new "$seed" 1000 1000 200 600 save 300 "$out/world-$seed.@SIDE@.save"
	# Java and Rust write the same save, and each continues the other's save
	# exactly as the uninterrupted run did (frames 300 to 600).
	if cmp -s "$out/world-$seed.java.save" "$out/world-$seed.rust.save"; then
		echo "same  world-$seed saves ($(wc -c < "$out/world-$seed.java.save" | tr -d ' ') bytes)"
	else
		echo "DIFF  world-$seed saves"
		fail=1
	fi
	awk 'NR>=301 {sub(/^frame [0-9]+/, "frame " NR-301); print}' "$out/world-$seed.java.txt" > "$out/world-$seed.rest.txt"
	check "world-$seed-loaded" biogenesis.WorldCheck world_check load "$out/world-$seed.@OTHER@.save" 300
	if cmp -s "$out/world-$seed.rest.txt" "$out/world-$seed-loaded.java.txt"; then
		echo "same  world-$seed-loaded continues world-$seed"
	else
		echo "DIFF  world-$seed-loaded doesn't continue world-$seed"
		fail=1
	fi
done
check "world-big" biogenesis.WorldCheck world_check new 3 2000 1500 1000 1000 every 50
exit $fail
