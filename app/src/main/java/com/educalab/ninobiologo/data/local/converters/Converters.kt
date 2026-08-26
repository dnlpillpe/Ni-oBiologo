package com.educalab.ninobiologo.data.local.converters

import androidx.room.TypeConverter
import com.educalab.ninobiologo.domain.model.AnalysisTaskType
import com.educalab.ninobiologo.domain.model.BiologistRank
import com.educalab.ninobiologo.domain.model.CreaturePartCategory
import com.educalab.ninobiologo.domain.model.DiscoveryCategory
import com.educalab.ninobiologo.domain.model.DiscoveryRarity
import com.educalab.ninobiologo.domain.model.ExperimentOutcome
import com.educalab.ninobiologo.domain.model.JournalEntryType
import com.educalab.ninobiologo.domain.model.SampleExplorationState
import com.educalab.ninobiologo.domain.model.UnlockCriteriaType

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
    fun fromDiscoveryCategory(value: DiscoveryCategory): String = value.name
    @TypeConverter
    fun toDiscoveryCategory(value: String): DiscoveryCategory = DiscoveryCategory.valueOf(value)

    @TypeConverter
    fun fromDiscoveryRarity(value: DiscoveryRarity): String = value.name
    @TypeConverter
    fun toDiscoveryRarity(value: String): DiscoveryRarity = DiscoveryRarity.valueOf(value)

    @TypeConverter
    fun fromSampleExplorationState(value: SampleExplorationState): String = value.name
    @TypeConverter
    fun toSampleExplorationState(value: String): SampleExplorationState = SampleExplorationState.valueOf(value)

    @TypeConverter
    fun fromBiologistRank(value: BiologistRank): String = value.name
    @TypeConverter
    fun toBiologistRank(value: String): BiologistRank = BiologistRank.valueOf(value)

    @TypeConverter
    fun fromAnalysisTaskType(value: AnalysisTaskType): String = value.name
    @TypeConverter
    fun toAnalysisTaskType(value: String): AnalysisTaskType = AnalysisTaskType.valueOf(value)

    @TypeConverter
    fun fromExperimentOutcome(value: ExperimentOutcome): String = value.name
    @TypeConverter
    fun toExperimentOutcome(value: String): ExperimentOutcome = ExperimentOutcome.valueOf(value)

    @TypeConverter
    fun fromJournalEntryType(value: JournalEntryType): String = value.name
    @TypeConverter
    fun toJournalEntryType(value: String): JournalEntryType = JournalEntryType.valueOf(value)

    @TypeConverter
    fun fromCreaturePartCategory(value: CreaturePartCategory): String = value.name
    @TypeConverter
    fun toCreaturePartCategory(value: String): CreaturePartCategory = CreaturePartCategory.valueOf(value)

    @TypeConverter
    fun fromUnlockCriteriaType(value: UnlockCriteriaType): String = value.name
    @TypeConverter
    fun toUnlockCriteriaType(value: String): UnlockCriteriaType = UnlockCriteriaType.valueOf(value)
}
