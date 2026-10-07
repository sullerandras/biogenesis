# UTF-8 copy of src/ that javac compiles from (see the src-utf8 target)
UTF8_SRC = build-src

run: build
	SKIP_OPENGL=true java -Dsun.java2d.opengl=True -Dsun.java2d.opengl.fbobject=false -jar biogenesis.jar

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
	java -cp lib/gson-2.10.1.jar:classes biogenesis.Benchmark

.PHONY: run run-analyzer test build build-analyzer build-src-jar src-utf8 compile compile-analyzer compile-tests clean benchmark
