package com.educalab.ninobiologo.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.educalab.ninobiologo.domain.model.ExperimentOutcome
import com.educalab.ninobiologo.domain.model.JournalEntryType
import com.educalab.ninobiologo.domain.model.SampleExplorationState

@Entity(tableName = "explorer_profile")
data class ExplorerProfileEntity(
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
    tableName = "discoveries_found",
    foreignKeys = [ForeignKey(entity = MicroscopeDiscoveryEntity::class, parentColumns = ["id"], childColumns = ["discoveryId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("discoveryId")]
)
data class DiscoveryFoundEntity(
    @PrimaryKey val discoveryId: String,
    val discoveredAtEpochMillis: Long,
    val viaSampleId: String?
)

@Entity(
    tableName = "sample_exploration",
    foreignKeys = [ForeignKey(entity = ScientificSampleEntity::class, parentColumns = ["id"], childColumns = ["sampleId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("sampleId")]
)
data class SampleExplorationEntity(
    @PrimaryKey val sampleId: String,
    val state: SampleExplorationState,
    val discoveriesFound: Int,
    val totalDiscoveries: Int,
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
    tableName = "collectible_unlocks",
    foreignKeys = [ForeignKey(entity = LabCollectibleEntity::class, parentColumns = ["id"], childColumns = ["collectibleId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("collectibleId")]
)
data class CollectibleUnlockEntity(
    @PrimaryKey val collectibleId: String,
    val unlockedAtEpochMillis: Long
)

@Entity(
    tableName = "lab_upgrade_unlocks",
    foreignKeys = [ForeignKey(entity = LaboratoryUpgradeEntity::class, parentColumns = ["id"], childColumns = ["upgradeId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("upgradeId")]
)
data class LabUpgradeUnlockEntity(
    @PrimaryKey val upgradeId: String,
    val unlockedAtEpochMillis: Long
)

@Entity(tableName = "creature_collection")
data class CreatureCollectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val formaId: String,
    val movimientoId: String,
    val alimentacionId: String,
    val adaptacionId: String,
    val targetEnvironmentId: String,
    val fitScore: Int,
    val createdAtEpochMillis: Long
)

@Entity(
    tableName = "experiment_results",
    foreignKeys = [ForeignKey(entity = ExperimentEntity::class, parentColumns = ["id"], childColumns = ["experimentId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("experimentId")]
)
data class ExperimentResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val experimentId: String,
    val variableValue: Int,
    val outcome: ExperimentOutcome,
    val message: String,
    val xpAwarded: Int,
    val savedAtEpochMillis: Long
)

@Entity(
    tableName = "discovery_journal",
    foreignKeys = [ForeignKey(entity = MicroscopicEnvironmentEntity::class, parentColumns = ["id"], childColumns = ["relatedEnvironmentId"], onDelete = ForeignKey.SET_NULL)],
    indices = [Index("relatedEnvironmentId")]
)
data class DiscoveryJournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: JournalEntryType,
    val title: String,
    val note: String,
    val filePath: String?,
    val relatedEnvironmentId: String?,
    val createdAtEpochMillis: Long
)
