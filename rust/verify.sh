#!/bin/sh
# Runs the Java and the translated Rust code on the same inputs and compares
# their dumps. Needs rust/transpile.sh to have run (it makes rust/build/src).
set -e
cd "$(dirname "$0")/.."
out=rust/build/verify
mkdir -p "$out" rust/build/java-classes
javac -nowarn -encoding UTF-8 -cp lib/gson-2.10.1.jar -sourcepath rust/build/src -d rust/build/java-classes \
	rust/verify/biogenesis/*.java
(cd rust/core && cargo build --release -q --bin genetic_code_check)
rust_bin=rust/core/target/release/genetic_code_check

fail=0
check() {
	name="$1"; shift
	java -cp rust/build/java-classes:lib/gson-2.10.1.jar biogenesis.GeneticCodeCheck "$@" > "$out/$name.java.txt"
	"$rust_bin" "$@" > "$out/$name.rust.txt"
	if cmp -s "$out/$name.java.txt" "$out/$name.rust.txt"; then
		echo "same  $name ($(wc -l < "$out/$name.java.txt" | tr -d ' ') lines)"
	else
		echo "DIFF  $name:"
		diff "$out/$name.java.txt" "$out/$name.rust.txt" | head -20
		fail=1
	fi
}
for seed in 1 2 3 42 1234567; do
	check "genetic-$seed" "$seed" 20 60
	check "genetic-hot-$seed" "$seed" 20 60 hot
done
exit $fail
