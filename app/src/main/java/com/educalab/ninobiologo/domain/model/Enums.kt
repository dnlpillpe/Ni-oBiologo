package com.educalab.ninobiologo.domain.model

/** Rango de progresión del joven biólogo. Definido en el prompt específico (sección PROGRESIÓN). */
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

enum class OrganismCategory { PLANTA, ANIMAL, MICROORGANISMO, HONGO }

enum class OrganismRarity(val displayName: String, val xpValue: Int) {
    COMUN("Común", 10),
    POCO_COMUN("Poco común", 20),
    RARO("Raro", 35),
    LEGENDARIO("Legendario", 60)
}

/** Rol trófico usado por el Constructor de Ecosistemas y el validador de cadenas alimentarias. */
enum class TrophicRole { PRODUCTOR, HERBIVORO, CARNIVORO, DESCOMPONEDOR }

enum class MissionType { OBSERVAR, COMPARAR, INVESTIGAR, CONSTRUIR, CLASIFICAR, MICROSCOPIO }

enum class ChallengeType { CLASIFICADOR, CONSTRUCTOR_ECOSISTEMA, CADENA_ALIMENTARIA, CONSTRUCTOR_CELULA, MEMORIA_BIOLOGICA }

enum class EcosystemStatus(val displayName: String) {
    COLAPSADO("Colapsado"),
    INESTABLE("Inestable"),
    ESTABLE("Estable"),
    FLORECIENTE("Floreciente")
}

enum class ModuleState { BLOQUEADO, DISPONIBLE, INICIADO, COMPLETADO, DOMINADO }

enum class JournalEntryType { TEXTO, FOTO, AUDIO }

enum class BadgeCriteriaType {
    DESCUBRIMIENTOS_TOTALES,
    EXPEDICIONES_COMPLETADAS,
    ECOSISTEMAS_ESTABLES,
    DESAFIOS_SUPERADOS,
    ZONA_COMPLETA,
    RAREZA_LEGENDARIA
}
