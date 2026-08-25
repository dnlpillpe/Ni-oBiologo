package com.educalab.ninobiologo.domain.logic

/**
 * Convierte aciertos/intentos de cualquier desafío interactivo en estrellas (0..3) y XP, con una
 * misma regla justa para todos los tipos de mecánica. Nunca resta puntos: la especificación
 * maestra prohíbe castigos ("evitar mecánicas manipulativas").
 */
object ChallengeScoringEngine {

    data class ScoreResult(val stars: Int, val xpAwarded: Int, val message: String)

    fun score(correctCount: Int, totalCount: Int, baseRewardXp: Int): ScoreResult {
        if (totalCount <= 0) {
            return ScoreResult(0, 0, "Todavía no hay intentos registrados en este desafío.")
        }
        val safeCorrect = correctCount.coerceIn(0, totalCount)
        val accuracy = safeCorrect.toFloat() / totalCount.toFloat()

        val stars = when {
            accuracy >= 0.9f -> 3
            accuracy >= 0.6f -> 2
            accuracy > 0f -> 1
            else -> 0
        }

        // Recompensa proporcional al acierto, con un mínimo de participación para no castigar el
        // intento honesto (la especificación pide "evitar castigos innecesarios").
        val participationXp = (baseRewardXp * 0.2f).toInt()
        val performanceXp = (baseRewardXp * 0.8f * accuracy).toInt()
        val xpAwarded = (participationXp + performanceXp).coerceAtLeast(0)

        val message = when (stars) {
            3 -> "¡Dominaste este desafío como un verdadero biólogo!"
            2 -> "Muy buen trabajo, estás cerca de dominarlo por completo."
            1 -> "Buen intento, sigue practicando para mejorar."
            else -> "Sigue explorando, cada intento suma experiencia real."
        }

        return ScoreResult(stars, xpAwarded, message)
    }
}
