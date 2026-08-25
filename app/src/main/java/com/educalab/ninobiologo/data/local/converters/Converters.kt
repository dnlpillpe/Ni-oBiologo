package com.educalab.ninobiologo.data.local.converters

import androidx.room.TypeConverter
import com.educalab.ninobiologo.domain.model.BadgeCriteriaType
import com.educalab.ninobiologo.domain.model.BiologistRank
import com.educalab.ninobiologo.domain.model.ChallengeType
import com.educalab.ninobiologo.domain.model.EcosystemStatus
import com.educalab.ninobiologo.domain.model.JournalEntryType
import com.educalab.ninobiologo.domain.model.MissionType
import com.educalab.ninobiologo.domain.model.ModuleState
import com.educalab.ninobiologo.domain.model.OrganismCategory
import com.educalab.ninobiologo.domain.model.OrganismRarity
import com.educalab.ninobiologo.domain.model.TrophicRole

/**
 * Convertidores de Room. Las listas de String se serializan con un delimitador que no aparece en
 * los ids del proyecto ("|"), evitando depender de una librería externa de JSON.
 */
class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>?): String = value?.joinToString("|") ?: ""

    @TypeConverter
    fun toStringList(value: String?): List<String> =
        if (value.isNullOrBlank()) emptyList() else value.split("|").filter { it.isNotBlank() }

    @TypeConverter
    fun fromOrganismCategory(value: OrganismCategory): String = value.name
    @TypeConverter
    fun toOrganismCategory(value: String): OrganismCategory = OrganismCategory.valueOf(value)

    @TypeConverter
    fun fromOrganismRarity(value: OrganismRarity): String = value.name
    @TypeConverter
    fun toOrganismRarity(value: String): OrganismRarity = OrganismRarity.valueOf(value)

    @TypeConverter
    fun fromTrophicRole(value: TrophicRole): String = value.name
    @TypeConverter
    fun toTrophicRole(value: String): TrophicRole = TrophicRole.valueOf(value)

    @TypeConverter
    fun fromMissionType(value: MissionType): String = value.name
    @TypeConverter
    fun toMissionType(value: String): MissionType = MissionType.valueOf(value)

    @TypeConverter
    fun fromModuleState(value: ModuleState): String = value.name
    @TypeConverter
    fun toModuleState(value: String): ModuleState = ModuleState.valueOf(value)

    @TypeConverter
    fun fromBiologistRank(value: BiologistRank): String = value.name
    @TypeConverter
    fun toBiologistRank(value: String): BiologistRank = BiologistRank.valueOf(value)

    @TypeConverter
    fun fromChallengeType(value: ChallengeType): String = value.name
    @TypeConverter
    fun toChallengeType(value: String): ChallengeType = ChallengeType.valueOf(value)

    @TypeConverter
    fun fromEcosystemStatus(value: EcosystemStatus): String = value.name
    @TypeConverter
    fun toEcosystemStatus(value: String): EcosystemStatus = EcosystemStatus.valueOf(value)

    @TypeConverter
    fun fromJournalEntryType(value: JournalEntryType): String = value.name
    @TypeConverter
    fun toJournalEntryType(value: String): JournalEntryType = JournalEntryType.valueOf(value)

    @TypeConverter
    fun fromBadgeCriteriaType(value: BadgeCriteriaType): String = value.name
    @TypeConverter
    fun toBadgeCriteriaType(value: String): BadgeCriteriaType = BadgeCriteriaType.valueOf(value)
}
