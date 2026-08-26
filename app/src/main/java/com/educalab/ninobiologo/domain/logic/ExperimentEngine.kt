package com.educalab.ninobiologo.domain.logic

import com.educalab.ninobiologo.domain.model.Experiment
import com.educalab.ninobiologo.domain.model.ExperimentOutcome

/**
 * Motor de los Experimentos Biológicos (ZONA "Experimentar"). El niño ajusta una variable real
 * (p.ej. horas de luz) y el motor calcula qué tan lejos quedó del rango ideal, sin castigar el
 * intento: siempre hay una explicación real, nunca un simple "correcto/incorrecto".
 */
object ExperimentEngine {

    data class Result(val outcome: ExperimentOutcome, val message: String, val xpAwarded: Int)

    fun evaluate(experiment: Experiment, variableValue: Int): Result {
        val value = variableValue.coerceIn(experiment.variableMin, experiment.variableMax)
        val idealSpan = (experiment.idealMax - experiment.idealMin).coerceAtLeast(1)
        val distance = when {
            value < experiment.idealMin -> experiment.idealMin - value
            value > experiment.idealMax -> value - experiment.idealMax
            else -> 0
        }
        val relativeDistance = (distance.toFloat() / idealSpan.toFloat()).coerceAtLeast(0f)

        val outcome = when {
            distance == 0 -> ExperimentOutcome.SIN_CAMBIOS
            relativeDistance <= 0.5f -> ExperimentOutcome.EFECTO_LEVE
            relativeDistance <= 1.5f -> ExperimentOutcome.EFECTO_NOTABLE
            else -> ExperimentOutcome.EFECTO_DRASTICO
        }

        val message = when (outcome) {
            ExperimentOutcome.SIN_CAMBIOS -> "${experiment.variableName} está en un buen rango: todo funciona con normalidad."
            ExperimentOutcome.EFECTO_LEVE -> "Un cambio leve en ${experiment.variableName.lowercase()}: se notan pequeños efectos."
            ExperimentOutcome.EFECTO_NOTABLE -> "Un cambio notable en ${experiment.variableName.lowercase()}: el efecto ya es claro."
            ExperimentOutcome.EFECTO_DRASTICO -> "Un cambio drástico en ${experiment.variableName.lowercase()}: el efecto es muy fuerte."
        }

        val participationXp = (experiment.rewardXp * 0.3f).toInt()
        val precisionXp = (experiment.rewardXp * 0.7f * (1f - relativeDistance.coerceIn(0f, 1f))).toInt()
        val xpAwarded = (participationXp + precisionXp).coerceAtLeast(0)

        return Result(outcome, message, xpAwarded)
    }
}
