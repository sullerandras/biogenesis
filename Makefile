run: build
	SKIP_OPENGL=true java -Dsun.java2d.opengl=True -Dsun.java2d.opengl.fbobject=false -jar biogenesis.jar

run-analyzer: build-analyzer
	SKIP_OPENGL=true java -Dsun.java2d.opengl=True -Dsun.java2d.opengl.fbobject=false -jar biogenesis-analyzer.jar

test: compile-tests
	java -cp classes:lib/gson-2.10.1.jar:lib/xchart-3.8.5.jar:lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar org.junit.runner.JUnitCore biogenesis.test.AllTests

build: compile
	rm -rf build
	rm -rf biogenesis.jar
	mkdir build
	unzip -o lib/gson-2.10.1.jar -d build
	cp -r classes/* build
	cp changes.md build
	jar -cfe biogenesis.jar biogenesis.MainWindow -C build .

build-analyzer: compile-analyzer
	rm -rf build
	rm -rf biogenesis-analyzer.jar
	mkdir build
	unzip -o lib/gson-2.10.1.jar -d build
	unzip -o lib/sqlite-jdbc-3.42.0.0.jar -d build
	unzip -o lib/xchart-3.8.5.jar -d build
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

compile: clean
	mkdir -p classes
	javac -cp lib/gson-2.10.1.jar:lib/xchart-3.8.5.jar -sourcepath src src/biogenesis/*.java -source 8 -target 8 -d classes
	cp -r src/biogenesis/messages classes/biogenesis
	cp -r src/biogenesis/images classes/biogenesis

compile-analyzer: clean
	mkdir -p classes
	javac -cp lib/gson-2.10.1.jar:lib/xchart-3.8.5.jar -sourcepath src src/biogenesis/clade_analyzer/*.java -source 8 -target 8 -d classes
	cp -r src/biogenesis/messages classes/biogenesis
	cp -r src/biogenesis/images classes/biogenesis

compile-tests: clean
	mkdir -p classes
	javac -cp lib/gson-2.10.1.jar:lib/xchart-3.8.5.jar:lib/junit-4.13.2.jar -sourcepath src tests/biogenesis/test/*.java -source 8 -target 8 -d classes
	cp -r src/biogenesis/messages classes/biogenesis
	cp -r src/biogenesis/images classes/biogenesis

clean:
	rm -rf classes
	rm -rf build
	rm -rf biogenesis-src.jar

benchmark: compile
	java -cp lib/gson-2.10.1.jar:classes biogenesis.Benchmark
