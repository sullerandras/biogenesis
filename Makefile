# UTF-8 copy of src/ that javac compiles from (see the src-utf8 target)
UTF8_SRC = build-src

# Performance patches applied to $(UTF8_SRC): sym and contact are
# perf/perf_patch.py, mirror is the collision mirror (perf/mirror/README.md).
# "make PERF_PATCHES=none ..." builds the unpatched upstream code.
PERF_PATCHES ?= sym,contact,mirror

# STRICT_MATH=1: Math.sin etc. become StrictMath's, as in rust/verify.sh. HotSpot's
# Math.sin can differ in the last bit (arm64, x86_64), so only then does a world
# take the same course as the Rust translation's (same seed or save).
STRICT_MATH ?=

# Collision mirror source transform (needs JavaParser, fetched on first use)
MIRROR_TOOL = perf/tools/classes/MirrorTransform.class

# Benchmark parameters: make benchmark WORLD=runs/1.bgw.gz FRAMES=500 THREADS=1
WORLD ?= runs/1.bgw.gz
FRAMES ?= 500
THREADS ?= 1

# Arguments of the Rust GUI: make run-rust ARGS=42 (a seed) or ARGS=my.save
ARGS ?=

run: build
	SKIP_OPENGL=true java -Dsun.java2d.opengl=True -Dsun.java2d.opengl.fbobject=false -jar biogenesis.jar

# The Rust translation's GUI (rust/gui). It uses StrictMath, so its worlds are
# those of Java built with STRICT_MATH=1; run-rust-platform-math takes Math.sin
# etc. from the platform instead, so its worlds differ from Java's.
run-rust:
	cargo run --release --manifest-path rust/gui/Cargo.toml -- $(ARGS)

run-rust-platform-math:
	cargo run --release --manifest-path rust/gui/Cargo.toml --features platform-math -- $(ARGS)

run-analyzer: build-analyzer
	SKIP_OPENGL=true java -Dsun.java2d.opengl=True -Dsun.java2d.opengl.fbobject=false -jar biogenesis-analyzer.jar

test: compile-tests
	java -cp classes:lib/gson-2.10.1.jar:lib/xchart-3.8.5.jar:lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar org.junit.runner.JUnitCore biogenesis.test.AllTests biogenesis.parallel_executor.test.AllTests

build: compile
	rm -rf build
	rm -rf biogenesis.jar
	mkdir build
	unzip -q -o lib/gson-2.10.1.jar -d build
	cp -r classes/* build
	cp changes.md build
	jar -cfe biogenesis.jar biogenesis.MainWindow -C build .

build-analyzer: compile-analyzer
	rm -rf build
	rm -rf biogenesis-analyzer.jar
	mkdir build
	unzip -q -o lib/gson-2.10.1.jar -d build
	unzip -q -o lib/sqlite-jdbc-3.42.0.0.jar -d build
	unzip -q -o lib/xchart-3.8.5.jar -d build
	cp -r classes/* build
	cp -r src/biogenesis/clade_analyzer/db/migrations build/biogenesis/clade_analyzer/db
	cp changes.md build
	jar -cfe biogenesis-analyzer.jar biogenesis.clade_analyzer.GUI -C build .

build-src-jar:
	rm -rf build
	rm -rf biogenesis-src.jar
	mkdir build
	cp -r src build
	cp -r .git build
	jar -cfe biogenesis-src.jar biogenesis.MainWindow -C build .

# Upstream sources are sometimes saved as Windows-1252 (Eclipse default on
# Windows), which javac (UTF-8 by default since JDK 18) rejects. Copy every
# .java file into $(UTF8_SRC), converting the ones that aren't valid UTF-8,
# so src/ stays identical to upstream.
src-utf8:
	@rm -rf $(UTF8_SRC) && mkdir -p $(UTF8_SRC)
	@cd src && find . -name '*.java' | while read -r f; do \
		mkdir -p "../$(UTF8_SRC)/$$(dirname "$$f")"; \
		if iconv -f UTF-8 -t UTF-8 "$$f" > /dev/null 2>&1; then \
			cp "$$f" "../$(UTF8_SRC)/$$f"; \
		else \
			echo "Converting $$f from Windows-1252 to UTF-8"; \
			iconv -f WINDOWS-1252 -t UTF-8 "$$f" > "../$(UTF8_SRC)/$$f" || exit 1; \
		fi; \
	done
	@PERF_PATCHES=$(PERF_PATCHES) python3 perf/perf_patch.py $(UTF8_SRC)
	@case ",$(PERF_PATCHES)," in *,mirror,*) \
		$(MAKE) -s $(MIRROR_TOOL) && \
		java -cp "perf/tools/classes:perf/tools/lib/*" MirrorTransform $(UTF8_SRC) perf/mirror \
			lib/gson-2.10.1.jar lib/xchart-3.8.5.jar;; \
	esac
	@if [ -n "$(STRICT_MATH)" ]; then \
		find $(UTF8_SRC) -name '*.java' -exec perl -pi -e \
			's/\bMath\.(sin|cos|tan|asin|acos|atan|atan2|exp|log|log10|log1p|expm1|pow|cbrt|hypot|sinh|cosh|tanh)\(/StrictMath.$$1(/g' {} +; \
	fi

$(MIRROR_TOOL): perf/mirror/MirrorTransform.java perf/mirror/fetch-tools.sh
	@perf/mirror/fetch-tools.sh
	@mkdir -p perf/tools/classes
	javac -nowarn -cp "perf/tools/lib/*" -d perf/tools/classes perf/mirror/MirrorTransform.java

compile: clean src-utf8
	mkdir -p classes
	javac -cp lib/gson-2.10.1.jar:lib/xchart-3.8.5.jar -sourcepath $(UTF8_SRC) $(UTF8_SRC)/biogenesis/*.java -source 8 -target 8 -d classes
	cp -r src/biogenesis/messages classes/biogenesis
	cp -r src/biogenesis/images classes/biogenesis

compile-analyzer: clean src-utf8
	mkdir -p classes
	javac -cp lib/gson-2.10.1.jar:lib/xchart-3.8.5.jar -sourcepath $(UTF8_SRC) $(UTF8_SRC)/biogenesis/clade_analyzer/*.java -source 8 -target 8 -d classes
	cp -r src/biogenesis/messages classes/biogenesis
	cp -r src/biogenesis/images classes/biogenesis

compile-tests: clean src-utf8
	mkdir -p classes
	javac -cp lib/gson-2.10.1.jar:lib/xchart-3.8.5.jar:lib/junit-4.13.2.jar -sourcepath $(UTF8_SRC) tests/biogenesis/test/*.java tests/biogenesis/parallel_executor/test/*.java -source 8 -target 8 -d classes
	cp -r src/biogenesis/messages classes/biogenesis
	cp -r src/biogenesis/images classes/biogenesis

clean:
	rm -rf classes
	rm -rf build
	rm -rf $(UTF8_SRC)
	rm -rf biogenesis-src.jar

benchmark: compile
	javac -nowarn -cp classes:lib/gson-2.10.1.jar benchmark/biogenesis/Benchmark.java -source 8 -target 8 -d classes
	java -Djava.awt.headless=true -cp lib/gson-2.10.1.jar:classes biogenesis.Benchmark $(WORLD) $(FRAMES) $(THREADS)

.PHONY: run run-rust run-rust-platform-math run-analyzer test build build-analyzer build-src-jar src-utf8 compile compile-analyzer compile-tests clean benchmark
