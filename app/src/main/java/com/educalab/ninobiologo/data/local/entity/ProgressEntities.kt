package com.educalab.ninobiologo.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.educalab.ninobiologo.domain.model.EcosystemStatus
import com.educalab.ninobiologo.domain.model.JournalEntryType
import com.educalab.ninobiologo.domain.model.ModuleState

@Entity(tableName = "biologist_profile")
data class BiologistProfileEntity(
    @PrimaryKey val id: Long = 1L,
    val alias: String,
    val avatarKey: String,
    val totalXp: Int,
    val onboardingCompleted: Boolean,
    val soundEnabled: Boolean,
    val hapticsEnabled: Boolean,
    val createdAtEpochMillis: Long
)

@Entity(
    tableName = "organism_discoveries",
    foreignKeys = [ForeignKey(entity = OrganismEntity::class, parentColumns = ["id"], childColumns = ["organismId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("organismId")]
)
data class OrganismDiscoveryEntity(
    @PrimaryKey val organismId: String,
    val discoveredAtEpochMillis: Long,
    val viaExpeditionId: String?
)

@Entity(
    tableName = "expedition_progress",
    foreignKeys = [ForeignKey(entity = ExpeditionEntity::class, parentColumns = ["id"], childColumns = ["expeditionId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("expeditionId")]
)
data class ExpeditionProgressEntity(
    @PrimaryKey val expeditionId: String,
    val state: ModuleState,
    val stepsCompleted: Int,
    val totalSteps: Int,
    val bestStars: Int,
    val timesCompleted: Int,
    val lastAttemptEpochMillis: Long?
)

@Entity(
    tableName = "challenge_attempts",
    foreignKeys = [ForeignKey(entity = ChallengeEntity::class, parentColumns = ["id"], childColumns = ["challengeId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("challengeId")]
)
data class ChallengeAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val challengeId: String,
    val correctCount: Int,
    val totalCount: Int,
    val stars: Int,
    val xpAwarded: Int,
    val attemptedAtEpochMillis: Long
)

@Entity(
    tableName = "badge_unlocks",
    foreignKeys = [ForeignKey(entity = BadgeEntity::class, parentColumns = ["id"], childColumns = ["badgeId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("badgeId")]
)
data class BadgeUnlockEntity(
    @PrimaryKey val badgeId: String,
    val unlockedAtEpochMillis: Long
)

@Entity(
    tableName = "ecosystem_builds",
    foreignKeys = [ForeignKey(entity = EcosystemTemplateEntity::class, parentColumns = ["id"], childColumns = ["templateId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("templateId")]
)
data class EcosystemBuildEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateId: String,
    val producers: Int,
    val herbivores: Int,
    val carnivores: Int,
    val decomposers: Int,
    val balanceScore: Int,
    val status: EcosystemStatus,
    val savedAtEpochMillis: Long
)

@Entity(
    tableName = "journal_entries",
    foreignKeys = [ForeignKey(entity = BiomeEntity::class, parentColumns = ["id"], childColumns = ["relatedBiomeId"], onDelete = ForeignKey.SET_NULL)],
    indices = [Index("relatedBiomeId")]
)
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: JournalEntryType,
    val title: String,
    val note: String,
    val filePath: String?,
    val relatedBiomeId: String?,
    val createdAtEpochMillis: Long
)
