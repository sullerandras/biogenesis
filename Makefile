run: build
	java -jar biogenesis.jar

build: compile
	rm -rf build
	rm -rf biogenesis.jar
	mkdir build
	unzip lib/*.jar -d build
	cp -r classes/* build
	cp -r src build
	cp changes.md build
	jar -cfe biogenesis.jar biogenesis.MainWindow -C build .

compile:
	javac --class-path lib/jts-core-1.15.0.jar --source-path src src/biogenesis/*.java --source 8 --target 8 -d classes
	cp -r src/biogenesis/messages classes/biogenesis
	cp -r src/biogenesis/images classes/biogenesis