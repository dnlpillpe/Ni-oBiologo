package com.educalab.ninobiologo.domain.model

/** Una de las 5 regiones del Mapa de Expediciones Biológicas. */
data class Biome(
    val id: String,
    val order: Int,
    val name: String,
    val tagline: String,
    val description: String,
    val iconKey: String,
    val primaryColorHex: String,
    val secondaryColorHex: String
)

data class ExpeditionStep(
    val order: Int,
    val prompt: String,
    val type: MissionType,
    val hint: String
)

data class Expedition(
    val id: String,
    val biomeId: String,
    val order: Int,
    val title: String,
    val narrative: String,
    val missionType: MissionType,
    val difficulty: Int, // 1..3
    val relatedOrganismIds: List<String>,
    val steps: List<ExpeditionStep>,
    val rewardXp: Int,
    val requiredRank: BiologistRank
)

data class Organism(
    val id: String,
    val biomeId: String,
    val name: String,
    val scientificName: String,
    val category: OrganismCategory,
    val habitat: String,
    val diet: String,
    val trophicRole: TrophicRole,
    val characteristics: List<String>,
    val funFact: String,
    val rarity: OrganismRarity,
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

/** Plantilla base de un ecosistema para el Constructor de Ecosistemas. */
data class EcosystemTemplate(
    val id: String,
    val biomeId: String,
    val name: String,
    val description: String,
    val availableOrganismIds: List<String>,
    val idealProducers: Int,
    val idealHerbivores: Int,
    val idealCarnivores: Int,
    val idealDecomposers: Int
)

data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val iconKey: String,
    val criteriaType: BadgeCriteriaType,
    val criteriaValue: Int,
    val biomeId: String? = null
)

data class Challenge(
    val id: String,
    val biomeId: String,
    val type: ChallengeType,
    val title: String,
    val instructions: String,
    val relatedOrganismIds: List<String>,
    val rewardXp: Int
)
