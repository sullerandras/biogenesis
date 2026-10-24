#!/bin/sh
# Regenerates rust/core/src/generated from src/ (see rust/README.md).
set -e
cd "$(dirname "$0")/.."
perf/mirror/fetch-tools.sh
mkdir -p rust/build/classes
javac -nowarn -cp "perf/tools/lib/*" -d rust/build/classes rust/transpiler/J2R.java
make -s src-utf8 PERF_PATCHES=none UTF8_SRC=rust/build/src
# StrictMath and FdLibm from the JDK's own sources: Math.sin etc. are translated from them.
java_home=$(java -XshowSettings:properties -version 2>&1 | sed -n 's/^ *java.home = //p')
rm -rf rust/build/jdk-src
unzip -q "$java_home/lib/src.zip" java.base/java/lang/StrictMath.java java.base/java/lang/FdLibm.java -d rust/build/jdk-src
java -cp "rust/build/classes:perf/tools/lib/*" J2R rust/build/src:rust/build/jdk-src/java.base rust/transpiler/classes.txt rust/core/src/generated lib/gson-2.10.1.jar
# Translator tests (rust/verify/j2rtest)
java -cp "rust/build/classes:perf/tools/lib/*" J2R rust/verify rust/verify/samples.txt rust/core/src/samples
