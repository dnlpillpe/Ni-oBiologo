# NiñoBiólogo: Exploradores de la Vida

Aplicación educativa Android para niños de 8 a 12 años sobre biología, biodiversidad y
exploración científica. El niño se convierte en un joven biólogo que explora cinco zonas
(Micromundo, Bosque de Vida, Océano Profundo, Cuerpo Humano y Ecosistemas), descubre organismos
reales, construye ecosistemas y arma su propio Museo Biológico Personal — todo 100% offline.

- **Package**: `com.educalab.ninobiologo`
- **Versión**: 1.0.0
- **Público**: niños de 8 a 12 años
- **Offline**: sí, sin `INTERNET`, sin backend, sin analíticas ni publicidad

## Stack técnico

Kotlin 2.0.21 · Jetpack Compose (BOM 2024.06.00) · Material 3 · Navigation Compose 2.7.7 ·
Room 2.6.1 · Coroutines 1.8.1 · MVVM + Repository · AGP 8.5.2 · Gradle 8.14.3 · JDK 17 ·
minSdk 24 / targetSdk 34.

Ver `docs/MANUAL_TECNICO.md` para el detalle completo de arquitectura y dependencias.

## Estructura del repositorio

```
app/                    Código fuente Android (Kotlin + Compose + Room)
database/                schema.sql y sample_data.sql (contenido semilla real)
docs/                     Documentación (memoria, manuales, base de datos, build report) + PDFs
tools/                    generate_seed_data.py, run_domain_tests.sh y logs de verificación real
deliverables/             APK, ZIP de código fuente y PDFs finales
.github/workflows/        Workflow de GitHub Actions para compilar un APK real al hacer push
```

## Compilación

Este proyecto se generó en un entorno sin Android SDK y sin acceso de red a los repositorios de
Google/Maven (evidencia real en `docs/BUILD_REPORT.md` y `tools/gradle_build_attempt.log`), por
lo que la compilación de Android **no pudo verificarse en este entorno**. Sí se compiló y
ejecutó de verdad, con evidencia reproducible, toda la lógica de dominio pura (59 tests JUnit,
ver `tools/domain_tests_real_run.log`) y se verificó `database/schema.sql` +
`database/sample_data.sql` cargando y consultando una base SQLite real (ver
`tools/sqlite_verification.log`).

Para compilar el APK real, en un entorno con Android SDK y red:

```bash
./gradlew clean
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleDebug
```

O simplemente haz `git push` a un repositorio de GitHub: el workflow en
`.github/workflows/android-build.yml` ya está listo para compilar el APK automáticamente.

## Regenerar el contenido semilla

Todo el contenido (40 expediciones, 50 organismos, 20 ecosistemas, 30 desafíos, 15 insignias) se
genera desde una única fuente en Python, revisable y mantenible:

```bash
python3 tools/generate_seed_data.py
```

Esto regenera `app/src/main/java/.../data/local/seed/SeedContent.kt`,
`database/schema.sql` y `database/sample_data.sql` a partir de la misma definición de datos, y
valida su integridad (IDs únicos, referencias válidas, cantidades exactas).

## Documentación

- `docs/MEMORIA_DESCRIPTIVA.md` — memoria descriptiva completa del proyecto (22 secciones)
- `docs/MANUAL_USUARIO.md` — manual para el público real (niños 8-12 y adultos que instalan la app)
- `docs/MANUAL_TECNICO.md` — manual técnico (arquitectura, módulos, dependencias, mantenimiento)
- `docs/BASE_DE_DATOS.md` — modelo de datos completo con DER en Mermaid
- `docs/BUILD_REPORT.md` — resultado real y honesto de cada intento de compilación/prueba
- `docs/pdf/` — versiones PDF de los tres primeros documentos

## Privacidad

No se solicita nombre real, email, teléfono ni ubicación. Cámara y micrófono son opcionales, se
piden solo cuando el niño usa el Diario del Explorador, y la app sigue siendo completamente
funcional si se deniegan. Todos los datos —incluyendo fotos y notas de voz— se guardan
únicamente en el almacenamiento privado del dispositivo.
