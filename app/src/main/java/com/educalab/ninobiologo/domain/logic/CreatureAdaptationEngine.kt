package com.educalab.ninobiologo.domain.logic

import com.educalab.ninobiologo.domain.model.CreaturePartOption

/**
 * Motor del Constructor Biológico (creación de criaturas microscópicas). Calcula qué tan bien
 * encajan las características elegidas por el niño con el ambiente objetivo: no hay una única
 * combinación "correcta", pero sí una real (cada pieza tiene un ambiente donde funciona mejor).
 */
object CreatureAdaptationEngine {

    data class Result(val fitScore: Int, val message: String)

    fun evaluate(selections: List<CreaturePartOption>, targetEnvironmentId: String): Result {
        if (selections.isEmpty()) {
            return Result(0, "Elige al menos una característica para tu criatura.")
        }
        val matches = selections.count { it.bestEnvironmentId == targetEnvironmentId }
        val fitScore = ((matches.toFloat() / selections.size.toFloat()) * 100f).toInt().coerceIn(0, 100)
        val message = when {
            fitScore >= 75 -> "¡Excelente adaptación! Tu criatura está lista para vivir en este ambiente."
            fitScore >= 40 -> "Tu criatura podría sobrevivir aquí, pero algunas características no encajan del todo."
            else -> "Esta combinación no encaja bien con este ambiente: prueba otras características."
        }
        return Result(fitScore, message)
    }
}
