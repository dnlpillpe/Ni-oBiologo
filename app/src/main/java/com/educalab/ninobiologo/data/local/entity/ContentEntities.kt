package com.educalab.ninobiologo.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.educalab.ninobiologo.domain.model.BiologistRank
import com.educalab.ninobiologo.domain.model.MissionType
import com.educalab.ninobiologo.domain.model.OrganismCategory
import com.educalab.ninobiologo.domain.model.OrganismRarity
import com.educalab.ninobiologo.domain.model.TrophicRole

@Entity(tableName = "biomes")
data class BiomeEntity(
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
    tableName = "organisms",
    foreignKeys = [ForeignKey(entity = BiomeEntity::class, parentColumns = ["id"], childColumns = ["biomeId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("biomeId"), Index(value = ["name"], unique = false)]
)
data class OrganismEntity(
    @PrimaryKey val id: String,
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

@Entity(
    tableName = "expeditions",
    foreignKeys = [ForeignKey(entity = BiomeEntity::class, parentColumns = ["id"], childColumns = ["biomeId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("biomeId")]
)
data class ExpeditionEntity(
    @PrimaryKey val id: String,
    val biomeId: String,
    val orderIndex: Int,
    val title: String,
    val narrative: String,
    val missionType: MissionType,
    val difficulty: Int,
    val relatedOrganismIds: List<String>,
    val rewardXp: Int,
    val requiredRank: BiologistRank
)

@Entity(
    tableName = "expedition_steps",
    foreignKeys = [ForeignKey(entity = ExpeditionEntity::class, parentColumns = ["id"], childColumns = ["expeditionId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("expeditionId")]
)
data class ExpeditionStepEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val expeditionId: String,
    val orderIndex: Int,
    val prompt: String,
    val type: MissionType,
    val hint: String
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
    tableName = "ecosystem_templates",
    foreignKeys = [ForeignKey(entity = BiomeEntity::class, parentColumns = ["id"], childColumns = ["biomeId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("biomeId")]
)
data class EcosystemTemplateEntity(
    @PrimaryKey val id: String,
    val biomeId: String,
    val name: String,
    val description: String,
    val availableOrganismIds: List<String>,
    val idealProducers: Int,
    val idealHerbivores: Int,
    val idealCarnivores: Int,
    val idealDecomposers: Int
)

@Entity(
    tableName = "challenges",
    foreignKeys = [ForeignKey(entity = BiomeEntity::class, parentColumns = ["id"], childColumns = ["biomeId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("biomeId")]
)
data class ChallengeEntity(
    @PrimaryKey val id: String,
    val biomeId: String,
    val type: com.educalab.ninobiologo.domain.model.ChallengeType,
    val title: String,
    val instructions: String,
    val relatedOrganismIds: List<String>,
    val rewardXp: Int
)

@Entity(
    tableName = "badges",
    foreignKeys = [ForeignKey(entity = BiomeEntity::class, parentColumns = ["id"], childColumns = ["biomeId"], onDelete = ForeignKey.SET_NULL)],
    indices = [Index("biomeId")]
)
data class BadgeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val iconKey: String,
    val criteriaType: com.educalab.ninobiologo.domain.model.BadgeCriteriaType,
    val criteriaValue: Int,
    val biomeId: String? = null
)
