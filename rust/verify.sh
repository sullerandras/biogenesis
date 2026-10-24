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
check() {
	name="$1"; java_main="$2"; rust_bin="rust/core/target/release/$3"; shift 3
	java -cp rust/build/java-classes:lib/gson-2.10.1.jar "$java_main" "$@" > "$out/$name.java.txt"
	"$rust_bin" "$@" > "$out/$name.rust.txt"
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
exit $fail
