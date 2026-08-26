package com.educalab.ninobiologo.domain.logic

import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery
import com.educalab.ninobiologo.domain.model.UnlockCriteriaType

/**
 * Reglas de "Mi Museo de la Vida": qué porcentaje de un ambiente está completo y qué
 * coleccionables/mejoras de laboratorio corresponde desbloquear dado el estado real acumulado
 * (nunca se "muestran bloqueados" sin más: se calculan de verdad).
 */
object CollectionEngine {

    fun environmentCompletionPercent(allEnvironmentDiscoveries: List<MicroscopeDiscovery>, discoveredIds: Set<String>): Int {
        if (allEnvironmentDiscoveries.isEmpty()) return 0
        val discoveredInEnvironment = allEnvironmentDiscoveries.count { it.id in discoveredIds }
        return ((discoveredInEnvironment.toFloat() / allEnvironmentDiscoveries.size.toFloat()) * 100f).toInt().coerceIn(0, 100)
    }

    data class ProgressStats(
        val discoveriesCount: Int,
        val experimentsRun: Int,
        val creaturesCreated: Int,
        val analysisPassed: Int,
        val environmentCompletionPercents: Map<String, Int>,
        val legendaryDiscovered: Boolean
    )

    /** Forma mínima común entre LabCollectible y LaboratoryUpgrade para evaluar su desbloqueo. */
    data class UnlockableCriteria(val criteriaType: UnlockCriteriaType, val criteriaValue: Int, val environmentId: String?)

    /** Devuelve los elementos cuya condición ya se cumple pero que aún no estaban desbloqueados. */
    fun <T> newlyUnlocked(
        items: List<T>,
        idOf: (T) -> String,
        criteriaOf: (T) -> UnlockableCriteria,
        alreadyUnlockedIds: Set<String>,
        stats: ProgressStats
    ): List<T> = items.filter { item ->
        val id = idOf(item)
        if (id in alreadyUnlockedIds) return@filter false
        meetsCriteria(criteriaOf(item), stats)
    }

    private fun meetsCriteria(criteria: UnlockableCriteria, stats: ProgressStats): Boolean = when (criteria.criteriaType) {
        UnlockCriteriaType.DESCUBRIMIENTOS_TOTALES -> stats.discoveriesCount >= criteria.criteriaValue
        UnlockCriteriaType.EXPERIMENTOS_REALIZADOS -> stats.experimentsRun >= criteria.criteriaValue
        UnlockCriteriaType.CRIATURAS_CREADAS -> stats.creaturesCreated >= criteria.criteriaValue
        UnlockCriteriaType.ANALISIS_SUPERADOS -> stats.analysisPassed >= criteria.criteriaValue
        UnlockCriteriaType.AMBIENTE_COMPLETO -> {
            val pct = criteria.environmentId?.let { stats.environmentCompletionPercents[it] } ?: 0
            pct >= criteria.criteriaValue
        }
        UnlockCriteriaType.RAREZA_LEGENDARIA -> stats.legendaryDiscovered
    }
}
