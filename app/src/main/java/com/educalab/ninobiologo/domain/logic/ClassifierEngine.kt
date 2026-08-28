package com.educalab.ninobiologo.domain.logic

import com.educalab.ninobiologo.domain.model.DiscoveryCategory
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery

/**
 * Motor del Analizador (herramienta de apoyo para comparar/clasificar descubrimientos; no es un
 * cuestionario de opción múltiple aislado, se usa dentro de la exploración de una muestra).
 */
object ClassifierEngine {

    enum class ClassifierAxis { CATEGORIA, HABITAT, DIETA, RAREZA }

    data class Attempt(val discoveryId: String, val chosenValue: String)

    data class AttemptResult(
        val discoveryId: String,
        val correct: Boolean,
        val expectedValue: String,
        val explanation: String
    )

    data class SessionResult(
        val results: List<AttemptResult>,
        val correctCount: Int,
        val totalCount: Int
    ) {
        val accuracy: Float get() = if (totalCount == 0) 0f else correctCount.toFloat() / totalCount.toFloat()
    }

    fun expectedValue(discovery: MicroscopeDiscovery, axis: ClassifierAxis): String = when (axis) {
        ClassifierAxis.CATEGORIA -> discovery.category.name
        ClassifierAxis.HABITAT -> discovery.habitat
        ClassifierAxis.DIETA -> discovery.diet
        ClassifierAxis.RAREZA -> discovery.rarity.name
    }

    fun evaluate(discoveries: List<MicroscopeDiscovery>, attempts: List<Attempt>, axis: ClassifierAxis): SessionResult {
        val byId = discoveries.associateBy { it.id }
        val results = attempts.map { attempt ->
            val discovery = byId[attempt.discoveryId]
            if (discovery == null) {
                AttemptResult(attempt.discoveryId, false, "desconocido", "Este descubrimiento no está disponible en esta ronda.")
            } else {
                val expected = expectedValue(discovery, axis)
                val correct = expected.equals(attempt.chosenValue, ignoreCase = true)
                val explanation = if (correct) {
                    "¡Correcto! ${discovery.name} ${explanationFor(discovery, axis)}"
                } else {
                    "${discovery.name} en realidad ${explanationFor(discovery, axis)}"
                }
                AttemptResult(attempt.discoveryId, correct, expected, explanation)
            }
        }
        return SessionResult(results, results.count { it.correct }, results.size)
    }

    private fun explanationFor(discovery: MicroscopeDiscovery, axis: ClassifierAxis): String = when (axis) {
        ClassifierAxis.CATEGORIA -> "es un/a ${categoryLabel(discovery.category)}."
        ClassifierAxis.HABITAT -> "vive en ${discovery.habitat}."
        ClassifierAxis.DIETA -> "se alimenta de ${discovery.diet}."
        ClassifierAxis.RAREZA -> "es de rareza ${discovery.rarity.displayName.lowercase()}."
    }

    private fun categoryLabel(category: DiscoveryCategory): String = when (category) {
        DiscoveryCategory.PLANTA -> "planta"
        DiscoveryCategory.ANIMAL -> "animal"
        DiscoveryCategory.MICROORGANISMO -> "microorganismo"
        DiscoveryCategory.HONGO -> "hongo"
    }
}
