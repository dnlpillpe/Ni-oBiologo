# Informe de Compilación (BUILD_REPORT) — NiñoBiólogo: Exploradores de la Vida

**Fecha de generación de este informe:** 2026-08-24 19:50 UTC

## 1. Stack utilizado

Kotlin 2.0.21 · Jetpack Compose BOM 2024.06.00 · Material 3 · Navigation Compose 2.7.7 ·
Room 2.6.1 · KSP 2.0.21-1.0.28 · Coroutines 1.8.1 · AGP 8.5.2 · Gradle Wrapper 8.14.3 · JDK 17 ·
compileSdk/targetSdk 34 · minSdk 24. Ver `docs/MANUAL_TECNICO.md` sección 1 para el detalle
completo.

## 2. COMPILACIÓN NO VERIFICADA

Este entorno de generación **no tiene Android SDK instalado** (no existe `ANDROID_HOME`, no hay
`adb`/`sdkmanager`, `/opt` no contiene ningún SDK de Android — solo Gradle, Maven, Node, Ruby y
Chrome) **y el acceso de red a los repositorios necesarios para descargarlo está bloqueado por el
proxy del entorno** (HTTP 403 tanto en `services.gradle.org` como en el Gradle Plugin Portal /
Google Maven). Por lo tanto:

> **La compilación real de la aplicación Android (APK) NO pudo verificarse en este entorno.**

Esto se documenta siguiendo exactamente el mismo criterio de honestidad aplicado en los dos
proyectos hermanos de esta colección (InvestigaWarma, Codea-RobotExplorer), que presentan la misma
limitación de entorno. No se ha simulado, inventado ni asumido ningún resultado de compilación.

## 3. Comando `./gradlew clean`

**Estado: FALLIDO — bloqueado por red.**

```
=== Intento 1: ./gradlew clean (con distributionUrl remoto real, sin red) — 2026-08-24 19:40:21 UTC ===
Downloading https://services.gradle.org/distributions/gradle-8.14.3-bin.zip
Exception in thread "main" java.io.IOException: Unable to tunnel through proxy. Proxy returns "HTTP/1.1 403 Forbidden"
    ... (traza completa en tools/gradle_build_attempt.log)
```

Intento adicional usando la distribución de Gradle 8.14.3 ya instalada localmente en
`/opt/gradle-8.14.3` en modo `--offline`:

```
=== Intento 2: gradle local --offline (sin AGP/Room/Compose en caché local) ===
FAILURE: Build failed with an exception.
* Where: Build file '/home/claude/project/NinoBiologo/build.gradle.kts' line: 2
* What went wrong:
Plugin [id: 'com.android.application', version: '8.5.2', apply: false] was not found ...
BUILD FAILED in 929ms
```

Evidencia completa (salida real, no editada): `tools/gradle_build_attempt.log`.

## 4. Comando `./gradlew testDebugUnitTest`

**Estado: NO EJECUTADO vía Gradle** (depende del mismo `clean`/sincronización de AGP, fallido por
red, ver sección 3).

**Evidencia alternativa real** — dado que la especificación exige no detenerse ante limitaciones
de entorno cuando existe una vía razonable de verificación, se compiló y ejecutó de verdad la capa
de dominio pura (`domain/model` + `domain/logic`, sin dependencias de Android) junto con sus tests
JUnit, usando el compilador de Kotlin y JUnit 4 embebidos en la distribución local de Gradle
8.14.3, sin red:

```
=== Ejecución real de tests de dominio JVM — 2026-08-24 19:41 UTC ===
Compilador: kotlin-compiler-embeddable 2.0.21 (embebido en Gradle 8.14.3 local, sin red)
Runner: JUnit 4.13.2 (local, sin red)
JUnit version 4.13.2
...........................................................
Time: 0.079
OK (59 tests)
```

Evidencia completa: `tools/domain_tests_real_run.log`. Reproducible con:
`GRADLE_HOME=/opt/gradle-8.14.3 ./tools/run_domain_tests.sh`.

- **Cantidad de tests (dominio, ejecutados realmente en este entorno): 59**
- **Tests aprobados: 59**
- **Tests fallidos: 0**

Adicionalmente existen **9 tests de Room** en
`app/src/test/java/com/educalab/ninobiologo/data/AppDatabaseTest.kt` (base de datos en memoria vía
Robolectric), estructuralmente completos y correctos, pero **no ejecutados** en este entorno porque
requieren dependencias de `androidx`/Robolectric que no están disponibles sin red. Se ejecutarán
con `./gradlew testDebugUnitTest` en un entorno con Android SDK y red.

**Total de tests en el repositorio: 68** (59 ejecutados y verificados aquí + 9 escritos y
pendientes de ejecución con Gradle/Android SDK real).

Como evidencia complementaria de la capa de datos, se cargaron `database/schema.sql` y
`database/sample_data.sql` en una base SQLite real mediante el módulo estándar `sqlite3` de
Python (motor equivalente al que usa Room en Android):

```
biomes: 5 filas · organisms: 50 filas · expeditions: 40 filas · expedition_steps: 120 filas
cell_models: 3 filas · cell_structures: 11 filas · body_systems: 6 filas · body_organs: 18 filas
ecosystem_templates: 20 filas · challenges: 30 filas · badges: 15 filas · biologist_profile: 1 fila
Violaciones de clave foránea: 0
Resultado: OK, base de datos cargada y consistente.
```

Evidencia completa: `tools/sqlite_verification.log`.

## 5. Comando `./gradlew lintDebug`

**Estado: NO EJECUTADO** — requiere AGP/Android SDK, no disponibles en este entorno (misma causa
que la sección 3). No se simula ningún resultado.

## 6. Comando `./gradlew assembleDebug`

**Estado: NO EJECUTADO** — requiere AGP/Android SDK, no disponibles en este entorno (misma causa
que la sección 3).

## 7. APK

**Estado: NO GENERADO EN ESTE ENTORNO.**

No existe `app/build/outputs/apk/debug/app-debug.apk` porque `assembleDebug` no pudo ejecutarse
(sección 6). En consecuencia, `deliverables/` **no contiene** un `.apk` en esta entrega. Se incluye
en su lugar el workflow `.github/workflows/android-build.yml`, listo para generar el APK real
automáticamente en la primera vez que el proyecto se suba a un repositorio de GitHub (entorno con
Android SDK y red completos).

**SHA-256 del APK: no aplica (no generado).**

## 8. PDF

**Estado: GENERADOS Y VALIDADOS — evidencia real.**

Pipeline usado (`tools/add_page_numbers.py` + pandoc + LibreOffice headless, sin servicios
externos): Markdown → `pandoc` (.docx, con tabla de contenidos) → `python-docx` añade numeración de
página real (campos `PAGE`/`NUMPAGES` en el pie) → `soffice --headless --convert-to pdf`
(filtro `writer_pdf_Export`).

| Documento | Cabecera | Tamaño | Páginas | Numeración | Caracteres es. | Tablas | SHA-256 |
|---|---|---|---|---|---|---|---|
| `docs/pdf/MEMORIA_DESCRIPTIVA.pdf` | `%PDF-1.7` | 112 896 B | 6 | Sí (6/6 páginas) | OK | Sí | `f91e68da4baa530d533d2b345a13650a0e1b1a6b9c6eccb9f08765514ff3c754` |
| `docs/pdf/MANUAL_USUARIO.pdf` | `%PDF-1.7` | 92 885 B | 5 | Sí (5/5 páginas) | OK | Sí | `8652c2402f1bf10686d78469b389bb4d8ccd836451099cb418ff58b7c7d72cff` |
| `docs/pdf/MANUAL_TECNICO.pdf` | `%PDF-1.7` | 156 424 B | 6 | Sí (6/6 páginas) | OK | Sí | `88b4ed70bb481f67b5cf98d75302a15daa499f2b2170989f44fcb2bcccc8f590` |

Verificado con `pdfinfo`/`pdftotext` reales (páginas, extracción de texto y presencia de "Página"
en cada una) y `head -c 8` (cabecera real `%PDF-`). Evidencia completa:
`tools/pdf_verification.log`.

## 9. Limitaciones reales de este entorno

- Sin Android SDK instalado (`ANDROID_HOME` no definido, sin `adb`/`sdkmanager`, `/opt` sin ningún
  paquete de Android).
- Sin acceso de red a `services.gradle.org`, Google Maven, Maven Central ni el Gradle Plugin
  Portal (proxy devuelve HTTP 403 Forbidden en todos los intentos).
- Por lo tanto, AGP, el plugin de Compose Compiler, KSP para Room y todas las dependencias de
  `androidx`/Compose no pueden descargarse ni resolverse, y `./gradlew` no puede completar ninguna
  tarea que dependa de ellas.
- La verificación real realizada en este entorno se limita a: (a) la capa de dominio Kotlin puro,
  compilada y probada con JUnit real (59/59 tests), y (b) el esquema y datos SQL, cargados y
  consultados en una base SQLite real sin errores de integridad referencial.
- El código de UI (Compose), Room (entidades/DAO/AppDatabase), ViewModels y manifest fue escrito y
  revisado manualmente con mucho cuidado, pero **no ha sido compilado por un compilador real de
  Kotlin/Compose/Room** en este entorno, por lo que no puede descartarse por completo la existencia
  de algún error de compilación no detectado en esa parte del código. Esta limitación se declara
  explícitamente, sin simular un resultado de compilación exitoso.

## 10. Próximo paso recomendado

En un entorno con Android SDK instalado y acceso de red a Google Maven / Maven Central (por
ejemplo, al hacer `git push` a un repositorio con el workflow de `.github/workflows/` incluido, o
en cualquier máquina de desarrollo Android estándar), ejecutar:

```bash
./gradlew clean
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleDebug
```

y actualizar este informe con los resultados reales, incluyendo la ruta del APK generado y su
SHA-256.
