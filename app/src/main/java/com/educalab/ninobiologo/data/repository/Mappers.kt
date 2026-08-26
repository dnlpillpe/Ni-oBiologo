package com.educalab.ninobiologo.data.repository

import com.educalab.ninobiologo.data.local.entity.BodyOrganEntity
import com.educalab.ninobiologo.data.local.entity.BodySystemEntity
import com.educalab.ninobiologo.data.local.entity.CellModelEntity
import com.educalab.ninobiologo.data.local.entity.CellStructureEntity
import com.educalab.ninobiologo.data.local.entity.ChallengeAttemptEntity
import com.educalab.ninobiologo.data.local.entity.ChallengeEntity
import com.educalab.ninobiologo.data.local.entity.CollectibleUnlockEntity
import com.educalab.ninobiologo.data.local.entity.CreatureCollectionEntity
import com.educalab.ninobiologo.data.local.entity.CreaturePartOptionEntity
import com.educalab.ninobiologo.data.local.entity.DiscoveryFoundEntity
import com.educalab.ninobiologo.data.local.entity.DiscoveryJournalEntity
import com.educalab.ninobiologo.data.local.entity.ExperimentEntity
import com.educalab.ninobiologo.data.local.entity.ExperimentResultEntity
import com.educalab.ninobiologo.data.local.entity.ExplorerProfileEntity
import com.educalab.ninobiologo.data.local.entity.LabCollectibleEntity
import com.educalab.ninobiologo.data.local.entity.LabUpgradeUnlockEntity
import com.educalab.ninobiologo.data.local.entity.LaboratoryUpgradeEntity
import com.educalab.ninobiologo.data.local.entity.MicroscopeDiscoveryEntity
import com.educalab.ninobiologo.data.local.entity.MicroscopicEnvironmentEntity
import com.educalab.ninobiologo.data.local.entity.SampleExplorationEntity
import com.educalab.ninobiologo.data.local.entity.ScientificSampleEntity
import com.educalab.ninobiologo.domain.model.BodyOrgan
import com.educalab.ninobiologo.domain.model.BodySystem
import com.educalab.ninobiologo.domain.model.CellModel
import com.educalab.ninobiologo.domain.model.CellStructure
import com.educalab.ninobiologo.domain.model.Challenge
import com.educalab.ninobiologo.domain.model.ChallengeAttempt
import com.educalab.ninobiologo.domain.model.CollectibleUnlock
import com.educalab.ninobiologo.domain.model.CreatureCollection
import com.educalab.ninobiologo.domain.model.CreaturePartOption
import com.educalab.ninobiologo.domain.model.DiscoveryFound
import com.educalab.ninobiologo.domain.model.DiscoveryJournalEntry
import com.educalab.ninobiologo.domain.model.Experiment
import com.educalab.ninobiologo.domain.model.ExperimentResult
import com.educalab.ninobiologo.domain.model.ExplorerProfile
import com.educalab.ninobiologo.domain.model.LabCollectible
import com.educalab.ninobiologo.domain.model.LabUpgradeUnlock
import com.educalab.ninobiologo.domain.model.LaboratoryUpgrade
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery
import com.educalab.ninobiologo.domain.model.MicroscopicEnvironment
import com.educalab.ninobiologo.domain.model.SampleExploration
import com.educalab.ninobiologo.domain.model.ScientificSample

fun MicroscopicEnvironmentEntity.toDomain() = MicroscopicEnvironment(id, orderIndex, name, tagline, description, iconKey, primaryColorHex, secondaryColorHex)

fun ScientificSampleEntity.toDomain() = ScientificSample(id, environmentId, orderIndex, name, origin, difficulty, iconKey)

fun MicroscopeDiscoveryEntity.toDomain() = MicroscopeDiscovery(id, sampleId, environmentId, name, scientificName, category, habitat, diet, characteristics, curiosity, rarity, iconKey)

fun CellModelEntity.toDomain(structures: List<CellStructureEntity>) =
    CellModel(id, name, cellType, description, structures.map { it.toDomain() })

fun CellStructureEntity.toDomain() = CellStructure(id, name, function, xPercent, yPercent)

fun BodySystemEntity.toDomain(organs: List<BodyOrganEntity>) =
    BodySystem(id, name, description, organs.map { it.toDomain() })

fun BodyOrganEntity.toDomain() = BodyOrgan(id, name, function)

fun ExperimentEntity.toDomain() = Experiment(id, environmentId, orderIndex, question, description, variableName, variableUnit, variableMin, variableMax, idealMin, idealMax, rewardXp)

fun CreaturePartOptionEntity.toDomain() = CreaturePartOption(id, category, name, description, bestEnvironmentId)

fun LabCollectibleEntity.toDomain() = LabCollectible(id, name, description, iconKey, criteriaType, criteriaValue, environmentId)

fun LaboratoryUpgradeEntity.toDomain() = LaboratoryUpgrade(id, name, description, iconKey, criteriaType, criteriaValue, environmentId)

fun ChallengeEntity.toDomain() = Challenge(id, environmentId, type, title, instructions, relatedDiscoveryIds, rewardXp)

fun ExplorerProfileEntity.toDomain() = ExplorerProfile(id, alias, avatarKey, totalXp, onboardingCompleted, soundEnabled, hapticsEnabled, createdAtEpochMillis)

fun DiscoveryFoundEntity.toDomain() = DiscoveryFound(discoveryId, discoveredAtEpochMillis, viaSampleId)

fun SampleExplorationEntity.toDomain() = SampleExploration(sampleId, state, discoveriesFound, totalDiscoveries, lastAttemptEpochMillis)

fun ChallengeAttemptEntity.toDomain() = ChallengeAttempt(id, challengeId, correctCount, totalCount, stars, xpAwarded, attemptedAtEpochMillis)

fun CollectibleUnlockEntity.toDomain() = CollectibleUnlock(collectibleId, unlockedAtEpochMillis)

fun LabUpgradeUnlockEntity.toDomain() = LabUpgradeUnlock(upgradeId, unlockedAtEpochMillis)

fun CreatureCollectionEntity.toDomain() = CreatureCollection(id, name, formaId, movimientoId, alimentacionId, adaptacionId, targetEnvironmentId, fitScore, createdAtEpochMillis)

fun ExperimentResultEntity.toDomain() = ExperimentResult(id, experimentId, variableValue, outcome, message, xpAwarded, savedAtEpochMillis)

fun DiscoveryJournalEntity.toDomain() = DiscoveryJournalEntry(id, type, title, note, filePath, relatedEnvironmentId, createdAtEpochMillis)
