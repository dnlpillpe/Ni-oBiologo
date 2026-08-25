package com.educalab.ninobiologo.data.repository

import com.educalab.ninobiologo.data.local.entity.BadgeEntity
import com.educalab.ninobiologo.data.local.entity.BadgeUnlockEntity
import com.educalab.ninobiologo.data.local.entity.BiologistProfileEntity
import com.educalab.ninobiologo.data.local.entity.BiomeEntity
import com.educalab.ninobiologo.data.local.entity.BodyOrganEntity
import com.educalab.ninobiologo.data.local.entity.BodySystemEntity
import com.educalab.ninobiologo.data.local.entity.CellModelEntity
import com.educalab.ninobiologo.data.local.entity.CellStructureEntity
import com.educalab.ninobiologo.data.local.entity.ChallengeAttemptEntity
import com.educalab.ninobiologo.data.local.entity.ChallengeEntity
import com.educalab.ninobiologo.data.local.entity.EcosystemBuildEntity
import com.educalab.ninobiologo.data.local.entity.EcosystemTemplateEntity
import com.educalab.ninobiologo.data.local.entity.ExpeditionEntity
import com.educalab.ninobiologo.data.local.entity.ExpeditionProgressEntity
import com.educalab.ninobiologo.data.local.entity.ExpeditionStepEntity
import com.educalab.ninobiologo.data.local.entity.JournalEntryEntity
import com.educalab.ninobiologo.data.local.entity.OrganismDiscoveryEntity
import com.educalab.ninobiologo.data.local.entity.OrganismEntity
import com.educalab.ninobiologo.domain.model.Badge
import com.educalab.ninobiologo.domain.model.BadgeUnlock
import com.educalab.ninobiologo.domain.model.Biome
import com.educalab.ninobiologo.domain.model.BiologistProfile
import com.educalab.ninobiologo.domain.model.BodyOrgan
import com.educalab.ninobiologo.domain.model.BodySystem
import com.educalab.ninobiologo.domain.model.CellModel
import com.educalab.ninobiologo.domain.model.CellStructure
import com.educalab.ninobiologo.domain.model.Challenge
import com.educalab.ninobiologo.domain.model.ChallengeAttempt
import com.educalab.ninobiologo.domain.model.EcosystemBuild
import com.educalab.ninobiologo.domain.model.EcosystemTemplate
import com.educalab.ninobiologo.domain.model.Expedition
import com.educalab.ninobiologo.domain.model.ExpeditionProgress
import com.educalab.ninobiologo.domain.model.ExpeditionStep
import com.educalab.ninobiologo.domain.model.JournalEntry
import com.educalab.ninobiologo.domain.model.Organism
import com.educalab.ninobiologo.domain.model.OrganismDiscovery

fun BiomeEntity.toDomain() = Biome(id, orderIndex, name, tagline, description, iconKey, primaryColorHex, secondaryColorHex)

fun OrganismEntity.toDomain() = Organism(id, biomeId, name, scientificName, category, habitat, diet, trophicRole, characteristics, funFact, rarity, iconKey)

fun ExpeditionEntity.toDomain(steps: List<ExpeditionStepEntity>) = Expedition(
    id, biomeId, orderIndex, title, narrative, missionType, difficulty, relatedOrganismIds,
    steps.sortedBy { it.orderIndex }.map { it.toDomain() }, rewardXp, requiredRank
)

fun ExpeditionStepEntity.toDomain() = ExpeditionStep(orderIndex, prompt, type, hint)

fun CellModelEntity.toDomain(structures: List<CellStructureEntity>) =
    CellModel(id, name, cellType, description, structures.map { it.toDomain() })

fun CellStructureEntity.toDomain() = CellStructure(id, name, function, xPercent, yPercent)

fun BodySystemEntity.toDomain(organs: List<BodyOrganEntity>) =
    BodySystem(id, name, description, organs.map { it.toDomain() })

fun BodyOrganEntity.toDomain() = BodyOrgan(id, name, function)

fun EcosystemTemplateEntity.toDomain() = EcosystemTemplate(id, biomeId, name, description, availableOrganismIds, idealProducers, idealHerbivores, idealCarnivores, idealDecomposers)

fun BadgeEntity.toDomain() = Badge(id, name, description, iconKey, criteriaType, criteriaValue, biomeId)

fun ChallengeEntity.toDomain() = Challenge(id, biomeId, type, title, instructions, relatedOrganismIds, rewardXp)

fun BiologistProfileEntity.toDomain() = BiologistProfile(id, alias, avatarKey, totalXp, onboardingCompleted, soundEnabled, hapticsEnabled, createdAtEpochMillis)

fun OrganismDiscoveryEntity.toDomain() = OrganismDiscovery(organismId, discoveredAtEpochMillis, viaExpeditionId)

fun ExpeditionProgressEntity.toDomain() = ExpeditionProgress(expeditionId, state, stepsCompleted, totalSteps, bestStars, timesCompleted, lastAttemptEpochMillis)

fun ChallengeAttemptEntity.toDomain() = ChallengeAttempt(id, challengeId, correctCount, totalCount, stars, xpAwarded, attemptedAtEpochMillis)

fun BadgeUnlockEntity.toDomain() = BadgeUnlock(badgeId, unlockedAtEpochMillis)

fun EcosystemBuildEntity.toDomain() = EcosystemBuild(id, templateId, producers, herbivores, carnivores, decomposers, balanceScore, status, savedAtEpochMillis)

fun JournalEntryEntity.toDomain() = JournalEntry(id, type, title, note, filePath, relatedBiomeId, createdAtEpochMillis)
