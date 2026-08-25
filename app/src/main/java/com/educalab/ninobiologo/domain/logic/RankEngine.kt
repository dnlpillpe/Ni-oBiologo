package com.educalab.ninobiologo.domain.logic

import com.educalab.ninobiologo.domain.model.BiologistRank

/**
 * Calcula el rango del biólogo a partir de sus acciones reales. La XP nunca se muestra "escrita a
 * mano": se deriva siempre de eventos persistidos (descubrimientos, expediciones, ecosistemas,
 * desafíos e insignias), tal como exige la especificación maestra V3 (sección 43, "estadística").
 */
object RankEngine {

    const val XP_PER_EXPEDITION_STAR = 15
    const val XP_PER_STABLE_ECOSYSTEM = 25
    const val XP_PER_BADGE = 20

    data class XpBreakdown(
        val discoveryXp: Int,
        val expeditionXp: Int,
        val ecosystemXp: Int,
        val challengeXp: Int,
        val badgeXp: Int
    ) {
        val total: Int get() = (discoveryXp + expeditionXp + ecosystemXp + challengeXp + badgeXp).coerceAtLeast(0)
    }

    /**
     * @param discoveryXpSum suma de [com.educalab.ninobiologo.domain.model.OrganismRarity.xpValue] de organismos descubiertos.
     * @param expeditionStarsSum suma de estrellas (0..3) obtenidas en todas las expediciones completadas.
     * @param stableEcosystemsCount ecosistemas guardados con estado ESTABLE o FLORECIENTE.
     * @param challengeXpSum suma de xp otorgada por ChallengeScoringEngine en cada intento.
     * @param badgesUnlockedCount insignias desbloqueadas.
     */
    fun computeXp(
        discoveryXpSum: Int,
        expeditionStarsSum: Int,
        stableEcosystemsCount: Int,
        challengeXpSum: Int,
        badgesUnlockedCount: Int
    ): XpBreakdown = XpBreakdown(
        discoveryXp = discoveryXpSum.coerceAtLeast(0),
        expeditionXp = (expeditionStarsSum.coerceAtLeast(0)) * XP_PER_EXPEDITION_STAR,
        ecosystemXp = (stableEcosystemsCount.coerceAtLeast(0)) * XP_PER_STABLE_ECOSYSTEM,
        challengeXp = challengeXpSum.coerceAtLeast(0),
        badgeXp = (badgesUnlockedCount.coerceAtLeast(0)) * XP_PER_BADGE
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
