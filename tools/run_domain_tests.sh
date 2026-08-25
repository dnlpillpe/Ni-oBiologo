#!/bin/bash
# Compila y ejecuta de verdad los tests de dominio JVM puro de NiñoBiólogo, sin necesidad de
# Android SDK ni acceso a red: usa el compilador de Kotlin y JUnit 4 que ya vienen embebidos en
# la distribución local de Gradle (/opt/gradle-8.14.3/lib). Reproduce el resultado guardado en
# tools/domain_tests_real_run.log.
#
# Uso: GRADLE_HOME=/opt/gradle-8.14.3 ./tools/run_domain_tests.sh
set -euo pipefail

GRADLE_HOME="${GRADLE_HOME:-/opt/gradle-8.14.3}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

TOOLCP=$(find "$GRADLE_HOME/lib" "$GRADLE_HOME/lib/plugins" -iname "*.jar" | tr '\n' ':')
STDLIB="$GRADLE_HOME/lib/kotlin-stdlib-2.0.21.jar"
JUNIT="$GRADLE_HOME/lib/junit-4.13.2.jar"
HAMCREST="$GRADLE_HOME/lib/hamcrest-core-1.3.jar"

OUT="$(mktemp -d)"
trap 'rm -rf "$OUT"' EXIT

# Solo domain/ (Kotlin puro) y app/src/test/.../domain (JUnit puro). Los tests de Room en
# app/src/test/.../data requieren androidx/Robolectric, que no están disponibles offline en este
# entorno: se ejecutan con ./gradlew testDebugUnitTest cuando haya Android SDK y red (ver
# docs/BUILD_REPORT.md).
find "$PROJECT_ROOT/app/src/main/java/com/educalab/ninobiologo/domain" \
     "$PROJECT_ROOT/app/src/test/java/com/educalab/ninobiologo/domain" -name "*.kt" > "$OUT/files.txt"

echo "Compilando $(wc -l < "$OUT/files.txt") archivos .kt (dominio + tests)..."
java -cp "$TOOLCP" org.jetbrains.kotlin.cli.jvm.K2JVMCompiler \
  -no-reflect -no-stdlib -cp "$STDLIB:$JUNIT:$HAMCREST" -d "$OUT/classes" "@$OUT/files.txt"

TEST_CLASSES=$(cd "$OUT/classes" && find com/educalab/ninobiologo/domain -maxdepth 1 -name "*Test.class" \
  | sed 's/\.class$//' | tr '/' '.' | sort)

echo "Ejecutando tests con JUnitCore..."
java -cp "$OUT/classes:$STDLIB:$JUNIT:$HAMCREST" org.junit.runner.JUnitCore $TEST_CLASSES
