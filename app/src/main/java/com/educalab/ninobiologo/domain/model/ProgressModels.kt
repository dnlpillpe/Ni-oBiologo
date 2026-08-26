package com.educalab.ninobiologo.domain.model

data class ExplorerProfile(
    val id: Long = 1L,
    val alias: String,
    val avatarKey: String,
    val totalXp: Int,
    val onboardingCompleted: Boolean,
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val createdAtEpochMillis: Long
) {
    val rank: BiologistRank get() = BiologistRank.fromXp(totalXp)
}

data class DiscoveryFound(
    val discoveryId: String,
    val discoveredAtEpochMillis: Long,
    val viaSampleId: String?
)

/** Progreso de exploración de una muestra: Explorar -> Observar -> Experimentar -> Descubrir. */
data class SampleExploration(
    val sampleId: String,
    val state: SampleExplorationState,
    val discoveriesFound: Int,
    val totalDiscoveries: Int,
    val lastAttemptEpochMillis: Long?
)

data class ChallengeAttempt(
    val id: Long,
    val challengeId: String,
    val correctCount: Int,
    val totalCount: Int,
    val stars: Int,
    val xpAwarded: Int,
    val attemptedAtEpochMillis: Long
)

data class CollectibleUnlock(
    val collectibleId: String,
    val unlockedAtEpochMillis: Long
)

data class LabUpgradeUnlock(
    val upgradeId: String,
    val unlockedAtEpochMillis: Long
)

/** Una criatura microscópica creada por el niño en el Constructor Biológico. */
data class CreatureCollection(
    val id: Long,
    val name: String,
    val formaId: String,
    val movimientoId: String,
    val alimentacionId: String,
    val adaptacionId: String,
    val targetEnvironmentId: String,
    val fitScore: Int,
    val createdAtEpochMillis: Long
)

/** Resultado guardado de un Experimento Biológico. */
data class ExperimentResult(
    val id: Long,
    val experimentId: String,
    val variableValue: Int,
    val outcome: ExperimentOutcome,
    val message: String,
    val xpAwarded: Int,
    val savedAtEpochMillis: Long
)

data class DiscoveryJournalEntry(
    val id: Long,
    val type: JournalEntryType,
    val title: String,
    val note: String,
    val filePath: String?,
    val relatedEnvironmentId: String?,
    val createdAtEpochMillis: Long
)

/** Item mostrado en "Mi Museo de la Vida": combina MicroscopeDiscovery + su hallazgo. */
data class MuseumItem(
    val discovery: MicroscopeDiscovery,
    val discoveredAtEpochMillis: Long,
    val viaSampleId: String?
)
