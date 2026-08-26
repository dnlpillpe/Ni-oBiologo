package com.educalab.ninobiologo.domain.model

/** Rango de progresión del joven biólogo. Definido en el prompt maestro (no se modifica aquí). */
enum class BiologistRank(val displayName: String, val minXp: Int) {
    EXPLORADOR_DE_VIDA("Explorador de Vida", 0),
    BIOLOGO_JUNIOR("Biólogo Junior", 250),
    INVESTIGADOR_NATURAL("Investigador Natural", 700),
    GUARDIAN_DEL_PLANETA("Guardián del Planeta", 1500);

    companion object {
        /** Devuelve el rango más alto cuyo umbral no supera [xp]. [xp] negativo se trata como 0. */
        fun fromXp(xp: Int): BiologistRank {
            val safeXp = xp.coerceAtLeast(0)
            return entries.lastOrNull { safeXp >= it.minXp } ?: EXPLORADOR_DE_VIDA
        }

        /** Rango siguiente al actual, o null si ya es el máximo. */
        fun next(current: BiologistRank): BiologistRank? {
            val idx = entries.indexOf(current)
            return entries.getOrNull(idx + 1)
        }
    }
}

enum class DiscoveryCategory { PLANTA, ANIMAL, MICROORGANISMO, HONGO }

enum class DiscoveryRarity(val displayName: String, val xpValue: Int) {
    COMUN("Común", 10),
    POCO_COMUN("Poco común", 20),
    RARO("Raro", 35),
    LEGENDARIO("Legendario", 60)
}

/** Estado de exploración de una muestra: Explorar -> Observar -> Experimentar -> Descubrir. */
enum class SampleExplorationState { NUEVO, OBSERVANDO, ANALIZANDO, DESCUBIERTO }

/** Tipo de tarea del Analizador (mecánica de comparar/clasificar, herramienta de apoyo). */
enum class AnalysisTaskType { CLASIFICADOR, MEMORIA_BIOLOGICA }

/** Resultado de un Experimento Biológico según qué tan lejos quedó la variable del rango ideal. */
enum class ExperimentOutcome { SIN_CAMBIOS, EFECTO_LEVE, EFECTO_NOTABLE, EFECTO_DRASTICO }

enum class JournalEntryType { TEXTO, FOTO, AUDIO, DESCUBRIMIENTO }

/** Categoría de pieza del Constructor Biológico (creación de criaturas microscópicas). */
enum class CreaturePartCategory { FORMA, MOVIMIENTO, ALIMENTACION, ADAPTACION }

/** Criterio compartido por coleccionables del museo y mejoras del laboratorio. */
enum class UnlockCriteriaType {
    DESCUBRIMIENTOS_TOTALES,
    EXPERIMENTOS_REALIZADOS,
    CRIATURAS_CREADAS,
    ANALISIS_SUPERADOS,
    AMBIENTE_COMPLETO,
    RAREZA_LEGENDARIA
}
