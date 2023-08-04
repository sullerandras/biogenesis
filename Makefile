run: build build-src-jar
	SKIP_OPENGL=true java -Dsun.java2d.opengl=True -Dsun.java2d.opengl.fbobject=false -jar biogenesis.jar

run-analyzer: build build-src-jar
	SKIP_OPENGL=true java -Dsun.java2d.opengl=True -Dsun.java2d.opengl.fbobject=false -cp biogenesis.jar biogenesis.clade_analyzer.Analyzer ${BACKUP_DIR}

run-analyzer-gui: build build-src-jar
	SKIP_OPENGL=true java -Dsun.java2d.opengl=True -Dsun.java2d.opengl.fbobject=false -cp biogenesis.jar biogenesis.clade_analyzer.GUI

build: compile
	rm -rf build
	rm -rf biogenesis.jar
	mkdir build
	unzip -o lib/gson-2.10.1.jar -d build
	unzip -o lib/sqlite-jdbc-3.42.0.0.jar -d build
	unzip -o lib/xchart-3.8.5.jar -d build
	cp -r classes/* build
	cp changes.md build
	jar -cfe biogenesis.jar biogenesis.MainWindow -C build .

build-src-jar:
	rm -rf build
	rm -rf biogenesis-src.jar
	mkdir build
	cp -r src build
	cp -r .git build
	jar -cfe biogenesis-src.jar biogenesis.MainWindow -C build .

compile:
	javac --class-path lib/gson-2.10.1.jar:lib/xchart-3.8.5.jar --source-path src src/biogenesis/*.java src/biogenesis/clade_analyzer/*.java --source 8 --target 8 -d classes
	cp -r src/biogenesis/messages classes/biogenesis
	cp -r src/biogenesis/images classes/biogenesis

benchmark: compile
	java -cp lib/gson-2.10.1.jar:classes biogenesis.Benchmark
