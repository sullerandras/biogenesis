#!/bin/sh
# Downloads the jars MirrorTransform needs (JavaParser and its dependencies)
# from Maven Central into perf/tools/lib, checking their SHA-256.
set -e
dir=perf/tools/lib
mkdir -p "$dir"
while read -r sha path; do
  case "$sha" in ''|'#'*) continue;; esac
  jar="$dir/$(basename "$path")"
  if [ ! -f "$jar" ] || ! echo "$sha  $jar" | shasum -a 256 -c - >/dev/null 2>&1; then
    echo "fetching $(basename "$path")"
    curl -sSfL -o "$jar.part" "https://repo1.maven.org/maven2/$path"
    echo "$sha  $jar.part" | shasum -a 256 -c - >/dev/null || { echo "checksum mismatch: $path" >&2; rm -f "$jar.part"; exit 1; }
    mv "$jar.part" "$jar"
  fi
done <<'LIST'
b5499a3b1c40b16c0671fabe478c9aafeab38160c6fde74a6c13f42d86716ecd com/github/javaparser/javaparser-core/3.28.2/javaparser-core-3.28.2.jar
4cb097c0834427939c01f4169dd0bab20068673913341c5b6db27be1a7445098 com/github/javaparser/javaparser-symbol-solver-core/3.28.2/javaparser-symbol-solver-core-3.28.2.jar
44b6b900ca352f4a0048e9428efab16582a014e10f8f65b4e58f5136c704624e org/javassist/javassist/3.31.0-GA/javassist-3.31.0-GA.jar
dc573e1fca4fd5454f4a5fd3d7da2df03002876a4175bafc14a95980dd7713b3 com/google/guava/guava/33.6.0-jre/guava-33.6.0-jre.jar
cbfc3906b19b8f55dd7cfd6dfe0aa4532e834250d7f080bd8d211a3e246b59cb com/google/guava/failureaccess/1.0.3/failureaccess-1.0.3.jar
LIST
