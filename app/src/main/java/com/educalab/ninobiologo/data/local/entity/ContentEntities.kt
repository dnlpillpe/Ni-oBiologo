package com.educalab.ninobiologo.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.educalab.ninobiologo.domain.model.AnalysisTaskType
import com.educalab.ninobiologo.domain.model.CreaturePartCategory
import com.educalab.ninobiologo.domain.model.DiscoveryCategory
import com.educalab.ninobiologo.domain.model.DiscoveryRarity
import com.educalab.ninobiologo.domain.model.UnlockCriteriaType

@Entity(tableName = "microscopic_environments")
data class MicroscopicEnvironmentEntity(
    @PrimaryKey val id: String,
    val orderIndex: Int,
    val name: String,
    val tagline: String,
    val description: String,
    val iconKey: String,
    val primaryColorHex: String,
    val secondaryColorHex: String
)

@Entity(
    tableName = "scientific_samples",
    foreignKeys = [ForeignKey(entity = MicroscopicEnvironmentEntity::class, parentColumns = ["id"], childColumns = ["environmentId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("environmentId")]
)
data class ScientificSampleEntity(
    @PrimaryKey val id: String,
    val environmentId: String,
    val orderIndex: Int,
    val name: String,
    val origin: String,
    val difficulty: Int,
    val iconKey: String
)

@Entity(
    tableName = "microscope_discoveries",
    foreignKeys = [
        ForeignKey(entity = ScientificSampleEntity::class, parentColumns = ["id"], childColumns = ["sampleId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = MicroscopicEnvironmentEntity::class, parentColumns = ["id"], childColumns = ["environmentId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("sampleId"), Index("environmentId"), Index(value = ["name"], unique = false)]
)
data class MicroscopeDiscoveryEntity(
    @PrimaryKey val id: String,
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

@Entity(tableName = "cell_models")
data class CellModelEntity(
    @PrimaryKey val id: String,
    val name: String,
    val cellType: String,
    val description: String
)

@Entity(
    tableName = "cell_structures",
    foreignKeys = [ForeignKey(entity = CellModelEntity::class, parentColumns = ["id"], childColumns = ["cellModelId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("cellModelId")]
)
data class CellStructureEntity(
    @PrimaryKey val id: String,
    val cellModelId: String,
    val name: String,
    val function: String,
    val xPercent: Float,
    val yPercent: Float
)

@Entity(tableName = "body_systems")
data class BodySystemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String
)

@Entity(
    tableName = "body_organs",
    foreignKeys = [ForeignKey(entity = BodySystemEntity::class, parentColumns = ["id"], childColumns = ["bodySystemId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("bodySystemId")]
)
data class BodyOrganEntity(
    @PrimaryKey val id: String,
    val bodySystemId: String,
    val name: String,
    val function: String
)

@Entity(
    tableName = "experiments",
    foreignKeys = [ForeignKey(entity = MicroscopicEnvironmentEntity::class, parentColumns = ["id"], childColumns = ["environmentId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("environmentId")]
)
data class ExperimentEntity(
    @PrimaryKey val id: String,
    val environmentId: String,
    val orderIndex: Int,
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

@Entity(tableName = "creature_part_options")
data class CreaturePartOptionEntity(
    @PrimaryKey val id: String,
    val category: CreaturePartCategory,
    val name: String,
    val description: String,
    val bestEnvironmentId: String
)

@Entity(
    tableName = "challenges",
    foreignKeys = [ForeignKey(entity = MicroscopicEnvironmentEntity::class, parentColumns = ["id"], childColumns = ["environmentId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("environmentId")]
)
data class ChallengeEntity(
    @PrimaryKey val id: String,
    val environmentId: String,
    val type: AnalysisTaskType,
    val title: String,
    val instructions: String,
    val relatedDiscoveryIds: List<String>,
    val rewardXp: Int
)

@Entity(
    tableName = "lab_collectibles",
    foreignKeys = [ForeignKey(entity = MicroscopicEnvironmentEntity::class, parentColumns = ["id"], childColumns = ["environmentId"], onDelete = ForeignKey.SET_NULL)],
    indices = [Index("environmentId")]
)
data class LabCollectibleEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val iconKey: String,
    val criteriaType: UnlockCriteriaType,
    val criteriaValue: Int,
    val environmentId: String? = null
)

@Entity(
    tableName = "laboratory_upgrades",
    foreignKeys = [ForeignKey(entity = MicroscopicEnvironmentEntity::class, parentColumns = ["id"], childColumns = ["environmentId"], onDelete = ForeignKey.SET_NULL)],
    indices = [Index("environmentId")]
)
data class LaboratoryUpgradeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val iconKey: String,
    val criteriaType: UnlockCriteriaType,
    val criteriaValue: Int,
    val environmentId: String? = null
)
