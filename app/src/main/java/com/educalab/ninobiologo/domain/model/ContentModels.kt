package com.educalab.ninobiologo.domain.model

/** Uno de los 5 ambientes microscópicos del Laboratorio Vivo. */
data class MicroscopicEnvironment(
    val id: String,
    val order: Int,
    val name: String,
    val tagline: String,
    val description: String,
    val iconKey: String,
    val primaryColorHex: String,
    val secondaryColorHex: String
)

/** Una muestra encontrada en un ambiente (p.ej. "Muestra de la corteza de un roble"). */
data class ScientificSample(
    val id: String,
    val environmentId: String,
    val order: Int,
    val name: String,
    val origin: String,
    val difficulty: Int, // 1..3
    val iconKey: String
)

/** Lo que el microscopio revela dentro de una muestra: el hallazgo real (antes "Organism"). */
data class MicroscopeDiscovery(
    val id: String,
    val sampleId: String,
    val environmentId: String,
    val name: String,
    val scientificName: String,
    val category: DiscoveryCategory,
    val habitat: String,
    val diet: String,
    val characteristics: List<String>,
    val curiosity: String,
    val rarity: DiscoveryRarity,
    val iconKey: String
)

data class CellStructure(
    val id: String,
    val name: String,
    val function: String,
    val xPercent: Float, // posición del punto interactivo en el microscopio (0..1)
    val yPercent: Float
)

data class CellModel(
    val id: String,
    val name: String,
    val cellType: String, // "Animal", "Vegetal", "Bacteriana"
    val description: String,
    val structures: List<CellStructure>
)

data class BodyOrgan(
    val id: String,
    val name: String,
    val function: String
)

data class BodySystem(
    val id: String,
    val name: String,
    val description: String,
    val organs: List<BodyOrgan>
)

/** Un experimento biológico: variable ajustable y su rango ideal (Experimentos Biológicos). */
data class Experiment(
    val id: String,
    val environmentId: String,
    val order: Int,
    val question: String,
    val description: String,
    val variableName: String,
    val variableUnit: String,
    val variableMin: Int,
    val variableMax: Int,
    val idealMin: Int,
    val idealMax: Int,
    val rewardXp: Int
)

/** Una pieza disponible en el Constructor Biológico (creación de criaturas). */
data class CreaturePartOption(
    val id: String,
    val category: CreaturePartCategory,
    val name: String,
    val description: String,
    val bestEnvironmentId: String
)

/** Tarea del Analizador: comparar/clasificar descubrimientos (antes "Challenge"). */
data class Challenge(
    val id: String,
    val environmentId: String,
    val type: AnalysisTaskType,
    val title: String,
    val instructions: String,
    val relatedDiscoveryIds: List<String>,
    val rewardXp: Int
)

/** Elemento coleccionable de "Mi Museo de la Vida" (antes "Badge"). */
data class LabCollectible(
    val id: String,
    val name: String,
    val description: String,
    val iconKey: String,
    val criteriaType: UnlockCriteriaType,
    val criteriaValue: Int,
    val environmentId: String? = null
)

/** Mejora desbloqueable del laboratorio (nuevas herramientas, zonas, decoración). */
data class LaboratoryUpgrade(
    val id: String,
    val name: String,
    val description: String,
    val iconKey: String,
    val criteriaType: UnlockCriteriaType,
    val criteriaValue: Int,
    val environmentId: String? = null
)
