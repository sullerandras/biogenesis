run: build build-src-jar
	java -jar biogenesis.jar

build: compile
	rm -rf build
	rm -rf biogenesis.jar
	mkdir build
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
	javac --source-path src src/biogenesis/*.java --source 8 --target 8 -d classes
	cp -r src/biogenesis/messages classes/biogenesis
	cp -r src/biogenesis/images classes/biogenesis

benchmark: compile
	java -cp classes biogenesis.Benchmark
