CLASSPATH = lib/jts-core-1.15.0.jar:lib/gson-2.10.1.jar
# UTF-8 copy of src/ that javac compiles from (see the src-utf8 target)
UTF8_SRC = build-src

run: compile
	java --class-path $(CLASSPATH):classes biogenesis.MainWindow

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

compile: src-utf8
	javac --class-path $(CLASSPATH) --source-path $(UTF8_SRC) $(UTF8_SRC)/biogenesis/*.java -d classes
	cp -r src/biogenesis/messages classes/biogenesis
	cp -r src/biogenesis/images classes/biogenesis

.PHONY: run compile src-utf8
