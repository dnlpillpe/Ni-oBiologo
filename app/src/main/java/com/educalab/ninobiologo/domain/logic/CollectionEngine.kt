package com.educalab.ninobiologo.domain.logic

import com.educalab.ninobiologo.domain.model.Badge
import com.educalab.ninobiologo.domain.model.BadgeCriteriaType
import com.educalab.ninobiologo.domain.model.Organism

/**
 * Reglas del Museo Biológico Personal: qué porcentaje de una zona está completo y qué insignias
 * corresponde desbloquear dado el estado real acumulado (nunca se "muestran bloqueadas" sin
 * más: se calculan de verdad, sección 43 de la especificación V3).
 */
object CollectionEngine {

    fun biomeCompletionPercent(allBiomeOrganisms: List<Organism>, discoveredIds: Set<String>): Int {
        if (allBiomeOrganisms.isEmpty()) return 0
        val discoveredInBiome = allBiomeOrganisms.count { it.id in discoveredIds }
        return ((discoveredInBiome.toFloat() / allBiomeOrganisms.size.toFloat()) * 100f).toInt().coerceIn(0, 100)
    }

    data class ProgressStats(
        val discoveriesCount: Int,
        val expeditionsCompleted: Int,
        val stableEcosystemsCount: Int,
        val challengesPassed: Int,
        val biomeCompletionPercents: Map<String, Int>,
        val legendaryDiscovered: Boolean
    )

    /** Devuelve las insignias cuya condición ya se cumple pero que aún no estaban desbloqueadas. */
    fun newlyUnlockedBadges(allBadges: List<Badge>, alreadyUnlockedIds: Set<String>, stats: ProgressStats): List<Badge> {
        return allBadges.filter { badge ->
            if (badge.id in alreadyUnlockedIds) return@filter false
            meetsCriteria(badge, stats)
        }
    }

    private fun meetsCriteria(badge: Badge, stats: ProgressStats): Boolean = when (badge.criteriaType) {
        BadgeCriteriaType.DESCUBRIMIENTOS_TOTALES -> stats.discoveriesCount >= badge.criteriaValue
        BadgeCriteriaType.EXPEDICIONES_COMPLETADAS -> stats.expeditionsCompleted >= badge.criteriaValue
        BadgeCriteriaType.ECOSISTEMAS_ESTABLES -> stats.stableEcosystemsCount >= badge.criteriaValue
        BadgeCriteriaType.DESAFIOS_SUPERADOS -> stats.challengesPassed >= badge.criteriaValue
        BadgeCriteriaType.ZONA_COMPLETA -> {
            val pct = badge.biomeId?.let { stats.biomeCompletionPercents[it] } ?: 0
            pct >= badge.criteriaValue
        }
        BadgeCriteriaType.RAREZA_LEGENDARIA -> stats.legendaryDiscovered
    }
}
