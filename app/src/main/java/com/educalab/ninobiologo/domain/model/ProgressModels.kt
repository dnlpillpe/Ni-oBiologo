package com.educalab.ninobiologo.domain.model

data class BiologistProfile(
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

data class OrganismDiscovery(
    val organismId: String,
    val discoveredAtEpochMillis: Long,
    val viaExpeditionId: String?
)

data class ExpeditionProgress(
    val expeditionId: String,
    val state: ModuleState,
    val stepsCompleted: Int,
    val totalSteps: Int,
    val bestStars: Int, // 0..3
    val timesCompleted: Int,
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

data class BadgeUnlock(
    val badgeId: String,
    val unlockedAtEpochMillis: Long
)

/** Resultado de una sesión guardada del Constructor de Ecosistemas. */
data class EcosystemBuild(
    val id: Long,
    val templateId: String,
    val producers: Int,
    val herbivores: Int,
    val carnivores: Int,
    val decomposers: Int,
    val balanceScore: Int,
    val status: EcosystemStatus,
    val savedAtEpochMillis: Long
)

data class JournalEntry(
    val id: Long,
    val type: JournalEntryType,
    val title: String,
    val note: String,
    val filePath: String?,
    val relatedBiomeId: String?,
    val createdAtEpochMillis: Long
)

/** Item mostrado en el Museo Biológico Personal: combina Organism + su descubrimiento. */
data class CollectionItem(
    val organism: Organism,
    val discoveredAtEpochMillis: Long,
    val viaExpeditionId: String?
)
