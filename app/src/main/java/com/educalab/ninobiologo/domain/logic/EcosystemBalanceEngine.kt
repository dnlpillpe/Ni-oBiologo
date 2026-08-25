package com.educalab.ninobiologo.domain.logic

import com.educalab.ninobiologo.domain.model.EcosystemStatus
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Motor real del Constructor de Ecosistemas (ZONA 5). No es un simple "mostrar texto según
 * botón": calcula el equilibrio a partir de las cantidades de organismos que el niño coloca,
 * usando una cadena energética simplificada pero consistente:
 *
 *   productores -> generan energía disponible
 *   herbívoros  -> consumen energía de los productores
 *   carnívoros  -> consumen "presión" de caza sobre los herbívoros
 *   descomponedores -> reciclan nutrientes y aumentan la capacidad de los productores
 */
object EcosystemBalanceEngine {

    private const val PRODUCER_ENERGY = 12
    private const val HERBIVORE_DEMAND = 10
    private const val CARNIVORE_DEMAND = 9
    private const val DECOMPOSER_BONUS_PER_UNIT = 4
    private const val MAX_DECOMPOSER_BONUS_UNITS = 6

    data class BalanceResult(
        val score: Int, // 0..100
        val status: EcosystemStatus,
        val feedback: List<String>
    )

    fun evaluate(producers: Int, herbivores: Int, carnivores: Int, decomposers: Int): BalanceResult {
        val p = producers.coerceIn(0, 999)
        val h = herbivores.coerceIn(0, 999)
        val c = carnivores.coerceIn(0, 999)
        val d = decomposers.coerceIn(0, 999)

        val feedback = mutableListOf<String>()

        if (p == 0 && h == 0 && c == 0 && d == 0) {
            return BalanceResult(0, EcosystemStatus.COLAPSADO, listOf("El ecosistema está vacío. Agrega productores para empezar la cadena de energía."))
        }

        if (p == 0) {
            feedback += "Sin productores no hay energía para nadie: agrega plantas o algas."
        }

        val decomposerBonusUnits = d.coerceAtMost(MAX_DECOMPOSER_BONUS_UNITS)
        val availableEnergy = p * PRODUCER_ENERGY + decomposerBonusUnits * DECOMPOSER_BONUS_PER_UNIT
        val herbivoreDemand = h * HERBIVORE_DEMAND
        val carnivoreDemand = c * CARNIVORE_DEMAND
        val herbivoreCapacityForCarnivores = h * 6

        // Ratio de energía productores -> herbívoros
        val energyRatio = if (herbivoreDemand == 0) 1f else (availableEnergy.toFloat() / herbivoreDemand.toFloat()).coerceAtMost(2f)
        // Ratio de presión de caza carnívoros -> herbívoros
        val predationRatio = if (carnivoreDemand == 0) 1f else (herbivoreCapacityForCarnivores.toFloat() / carnivoreDemand.toFloat()).coerceAtMost(2f)

        if (h > 0 && availableEnergy < herbivoreDemand) {
            feedback += "Hay demasiados herbívoros para la energía que producen las plantas actuales."
        }
        if (c > 0 && h == 0) {
            feedback += "Los carnívoros no tienen de qué alimentarse: faltan herbívoros."
        } else if (c > 0 && herbivoreCapacityForCarnivores < carnivoreDemand) {
            feedback += "Demasiados carnívoros para los herbívoros disponibles: la caza es insostenible."
        }
        if (d == 0) {
            feedback += "Sin descomponedores, los restos se acumulan y el suelo pierde nutrientes."
        }
        if (p > 0 && h == 0 && c == 0) {
            feedback += "Hay productores pero ningún consumidor: el ecosistema está incompleto."
        }

        val energyScore = (energyRatio.coerceAtMost(1.3f) / 1.3f) * 45f
        val predationScore = (predationRatio.coerceAtMost(1.3f) / 1.3f) * 35f
        val decomposerScore = (decomposerBonusUnits.toFloat() / MAX_DECOMPOSER_BONUS_UNITS.toFloat()) * 20f

        var rawScore = energyScore + predationScore + decomposerScore
        if (p == 0) rawScore *= 0.15f
        if (h == 0 && c > 0) rawScore *= 0.3f

        val score = rawScore.roundToInt().coerceIn(0, 100)

        val status = when {
            score >= 85 -> EcosystemStatus.FLORECIENTE
            score >= 60 -> EcosystemStatus.ESTABLE
            score >= 30 -> EcosystemStatus.INESTABLE
            else -> EcosystemStatus.COLAPSADO
        }

        if (feedback.isEmpty()) {
            feedback += when (status) {
                EcosystemStatus.FLORECIENTE -> "¡Excelente equilibrio! Cada nivel de la cadena tiene lo que necesita."
                EcosystemStatus.ESTABLE -> "El ecosistema funciona de forma estable."
                EcosystemStatus.INESTABLE -> "El ecosistema sobrevive, pero está en riesgo de desequilibrio."
                EcosystemStatus.COLAPSADO -> "El ecosistema no puede sostenerse así."
            }
        }

        return BalanceResult(score, status, feedback)
    }

    /** Diferencia absoluta respecto a una plantilla ideal, útil para pistas de BIA. */
    fun distanceFromIdeal(actual: Int, ideal: Int): Int = abs(actual - ideal)
}
