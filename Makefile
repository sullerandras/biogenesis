run: compile
	java --class-path lib/jts-core-1.15.0.jar:classes biogenesis.MainWindow

compile:
	javac --class-path lib/jts-core-1.15.0.jar --source-path src src/biogenesis/*.java -d classes
	cp -r src/biogenesis/messages classes/biogenesis
	cp -r src/biogenesis/images classes/biogenesis