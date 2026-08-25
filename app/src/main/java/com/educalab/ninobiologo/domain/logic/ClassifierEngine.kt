package com.educalab.ninobiologo.domain.logic

import com.educalab.ninobiologo.domain.model.Organism
import com.educalab.ninobiologo.domain.model.OrganismCategory

/**
 * Motor del Clasificador de Vida (mecánica "clasificar/relacionar", no un cuestionario de opción
 * múltiple: el niño arrastra cada organismo hasta la categoría/hábitat que le corresponde).
 */
object ClassifierEngine {

    enum class ClassifierAxis { CATEGORIA, HABITAT, DIETA }

    data class Attempt(val organismId: String, val chosenValue: String)

    data class AttemptResult(
        val organismId: String,
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

    fun expectedValue(organism: Organism, axis: ClassifierAxis): String = when (axis) {
        ClassifierAxis.CATEGORIA -> organism.category.name
        ClassifierAxis.HABITAT -> organism.habitat
        ClassifierAxis.DIETA -> organism.diet
    }

    fun evaluate(organisms: List<Organism>, attempts: List<Attempt>, axis: ClassifierAxis): SessionResult {
        val byId = organisms.associateBy { it.id }
        val results = attempts.map { attempt ->
            val organism = byId[attempt.organismId]
            if (organism == null) {
                AttemptResult(attempt.organismId, false, "desconocido", "Este organismo no está disponible en esta ronda.")
            } else {
                val expected = expectedValue(organism, axis)
                val correct = expected.equals(attempt.chosenValue, ignoreCase = true)
                val explanation = if (correct) {
                    "¡Correcto! ${organism.name} ${explanationFor(organism, axis)}"
                } else {
                    "${organism.name} en realidad ${explanationFor(organism, axis)}"
                }
                AttemptResult(attempt.organismId, correct, expected, explanation)
            }
        }
        return SessionResult(results, results.count { it.correct }, results.size)
    }

    private fun explanationFor(organism: Organism, axis: ClassifierAxis): String = when (axis) {
        ClassifierAxis.CATEGORIA -> "es un/a ${categoryLabel(organism.category)}."
        ClassifierAxis.HABITAT -> "vive en ${organism.habitat}."
        ClassifierAxis.DIETA -> "se alimenta de ${organism.diet}."
    }

    private fun categoryLabel(category: OrganismCategory): String = when (category) {
        OrganismCategory.PLANTA -> "planta"
        OrganismCategory.ANIMAL -> "animal"
        OrganismCategory.MICROORGANISMO -> "microorganismo"
        OrganismCategory.HONGO -> "hongo"
    }
}
