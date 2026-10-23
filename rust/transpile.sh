#!/bin/sh
# Regenerates rust/core/src/generated from src/ (see rust/README.md).
set -e
cd "$(dirname "$0")/.."
perf/mirror/fetch-tools.sh
mkdir -p rust/build/classes
javac -nowarn -cp "perf/tools/lib/*" -d rust/build/classes rust/transpiler/J2R.java
make -s src-utf8 PERF_PATCHES=none UTF8_SRC=rust/build/src
java -cp "rust/build/classes:perf/tools/lib/*" J2R rust/build/src rust/transpiler/classes.txt rust/core/src/generated lib/gson-2.10.1.jar
