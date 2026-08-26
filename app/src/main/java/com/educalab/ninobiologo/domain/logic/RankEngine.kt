package com.educalab.ninobiologo.domain.logic

import com.educalab.ninobiologo.domain.model.BiologistRank

/**
 * Calcula el rango del biólogo a partir de sus acciones reales. La XP nunca se muestra "escrita a
 * mano": se deriva siempre de eventos persistidos (descubrimientos, experimentos, análisis y
 * coleccionables desbloqueados).
 */
object RankEngine {

    const val XP_PER_SUCCESSFUL_EXPERIMENT = 25
    const val XP_PER_COLLECTIBLE = 20

    data class XpBreakdown(
        val discoveryXp: Int,
        val experimentXp: Int,
        val analysisXp: Int,
        val collectibleXp: Int
    ) {
        val total: Int get() = (discoveryXp + experimentXp + analysisXp + collectibleXp).coerceAtLeast(0)
    }

    /**
     * @param discoveryXpSum suma de [com.educalab.ninobiologo.domain.model.DiscoveryRarity.xpValue] de descubrimientos hechos.
     * @param successfulExperimentsCount experimentos guardados con resultado SIN_CAMBIOS o EFECTO_LEVE (variable bien ajustada).
     * @param analysisXpSum suma de xp otorgada por ChallengeScoringEngine en cada intento del Analizador.
     * @param collectiblesUnlockedCount coleccionables del museo desbloqueados.
     */
    fun computeXp(
        discoveryXpSum: Int,
        successfulExperimentsCount: Int,
        analysisXpSum: Int,
        collectiblesUnlockedCount: Int
    ): XpBreakdown = XpBreakdown(
        discoveryXp = discoveryXpSum.coerceAtLeast(0),
        experimentXp = (successfulExperimentsCount.coerceAtLeast(0)) * XP_PER_SUCCESSFUL_EXPERIMENT,
        analysisXp = analysisXpSum.coerceAtLeast(0),
        collectibleXp = (collectiblesUnlockedCount.coerceAtLeast(0)) * XP_PER_COLLECTIBLE
    )

    fun rankFor(xp: Int): BiologistRank = BiologistRank.fromXp(xp)

    /** Progreso 0f..1f hacia el siguiente rango. 1f si ya se alcanzó el rango máximo. */
    fun progressToNextRank(xp: Int): Float {
        val safeXp = xp.coerceAtLeast(0)
        val current = BiologistRank.fromXp(safeXp)
        val next = BiologistRank.next(current) ?: return 1f
        val span = (next.minXp - current.minXp).coerceAtLeast(1)
        val progressed = (safeXp - current.minXp).coerceIn(0, span)
        return progressed.toFloat() / span.toFloat()
    }

    fun xpToNextRank(xp: Int): Int {
        val safeXp = xp.coerceAtLeast(0)
        val current = BiologistRank.fromXp(safeXp)
        val next = BiologistRank.next(current) ?: return 0
        return (next.minXp - safeXp).coerceAtLeast(0)
    }
}
