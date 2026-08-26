package com.educalab.ninobiologo.data.repository

import androidx.room.withTransaction
import com.educalab.ninobiologo.data.local.AppDatabase
import com.educalab.ninobiologo.data.local.entity.BadgeUnlockEntity
import com.educalab.ninobiologo.data.local.entity.BiologistProfileEntity
import com.educalab.ninobiologo.data.local.entity.ChallengeAttemptEntity
import com.educalab.ninobiologo.data.local.entity.EcosystemBuildEntity
import com.educalab.ninobiologo.data.local.entity.ExpeditionProgressEntity
import com.educalab.ninobiologo.data.local.entity.JournalEntryEntity
import com.educalab.ninobiologo.data.local.entity.OrganismDiscoveryEntity
import com.educalab.ninobiologo.domain.logic.ChallengeScoringEngine
import com.educalab.ninobiologo.domain.logic.CollectionEngine
import com.educalab.ninobiologo.domain.logic.EcosystemBalanceEngine
import com.educalab.ninobiologo.domain.logic.RankEngine
import com.educalab.ninobiologo.domain.model.Badge
import com.educalab.ninobiologo.domain.model.BiologistProfile
import com.educalab.ninobiologo.domain.model.Biome
import com.educalab.ninobiologo.domain.model.BodySystem
import com.educalab.ninobiologo.domain.model.CellModel
import com.educalab.ninobiologo.domain.model.Challenge
import com.educalab.ninobiologo.domain.model.ChallengeAttempt
import com.educalab.ninobiologo.domain.model.CollectionItem
import com.educalab.ninobiologo.domain.model.EcosystemBuild
import com.educalab.ninobiologo.domain.model.EcosystemTemplate
import com.educalab.ninobiologo.domain.model.Expedition
import com.educalab.ninobiologo.domain.model.JournalEntry
import com.educalab.ninobiologo.domain.model.JournalEntryType
import com.educalab.ninobiologo.domain.model.ModuleState
import com.educalab.ninobiologo.domain.model.Organism
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Punto único de acceso a los datos de NiñoBiólogo. Los ViewModel nunca hablan con Room
 * directamente (sección 5 de la especificación maestra): siempre pasan por aquí, y aquí es
 * donde se apoyan en los motores de domain/logic para calcular resultados reales.
 */
class BiologyRepository(private val db: AppDatabase) {

    // ---------- Contenido ----------

    fun observeBiomes(): Flow<List<Biome>> = db.biomeDao().observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun getBiome(id: String): Biome? = db.biomeDao().getById(id)?.toDomain()

    fun observeOrganismsByBiome(biomeId: String): Flow<List<Organism>> =
        db.organismDao().observeByBiome(biomeId).map { list -> list.map { it.toDomain() } }

    fun observeAllOrganisms(): Flow<List<Organism>> =
        db.organismDao().observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun getOrganism(id: String): Organism? = db.organismDao().getById(id)?.toDomain()

    suspend fun getOrganismsByIds(ids: List<String>): List<Organism> = db.organismDao().getByIds(ids).map { it.toDomain() }

    suspend fun getExpeditionsByBiome(biomeId: String): List<Expedition> =
        db.expeditionDao().observeByBiome(biomeId).first().map { entity ->
            entity.toDomain(db.expeditionDao().getSteps(entity.id))
        }.sortedBy { it.order }

    suspend fun getExpedition(id: String): Expedition? {
        val entity = db.expeditionDao().getById(id) ?: return null
        return entity.toDomain(db.expeditionDao().getSteps(id))
    }

    suspend fun getCellModels(): List<CellModel> =
        db.cellModelDao().observeAll().first().map { it.toDomain(db.cellModelDao().getStructures(it.id)) }

    suspend fun getBodySystems(): List<BodySystem> =
        db.bodySystemDao().observeAll().first().map { it.toDomain(db.bodySystemDao().getOrgans(it.id)) }

    suspend fun getEcosystemTemplatesByBiome(biomeId: String): List<EcosystemTemplate> =
        db.ecosystemTemplateDao().observeByBiome(biomeId).first().map { it.toDomain() }

    suspend fun getEcosystemTemplate(id: String): EcosystemTemplate? = db.ecosystemTemplateDao().getById(id)?.toDomain()

    suspend fun getChallengesByBiome(biomeId: String): List<Challenge> =
        db.challengeDao().observeByBiome(biomeId).first().map { it.toDomain() }

    suspend fun getChallenge(id: String): Challenge? = db.challengeDao().getById(id)?.toDomain()

    fun observeBadges(): Flow<List<Badge>> = db.badgeDao().observeAll().map { list -> list.map { it.toDomain() } }

    // ---------- Perfil ----------

    fun observeProfile(): Flow<BiologistProfile?> = db.profileDao().observe().map { it?.toDomain() }

    suspend fun getProfile(): BiologistProfile? = db.profileDao().get()?.toDomain()

    suspend fun updateAlias(alias: String) {
        val current = db.profileDao().get() ?: return
        db.profileDao().update(current.copy(alias = alias))
    }

    suspend fun updateAvatar(avatarKey: String) {
        val current = db.profileDao().get() ?: return
        db.profileDao().update(current.copy(avatarKey = avatarKey))
    }

    suspend fun setSoundEnabled(enabled: Boolean) = db.profileDao().setSoundEnabled(enabled)
    suspend fun setHapticsEnabled(enabled: Boolean) = db.profileDao().setHapticsEnabled(enabled)
    suspend fun completeOnboarding() = db.profileDao().markOnboardingCompleted()

    // ---------- Descubrimientos (Museo Biológico) ----------

    fun observeDiscoveries(): Flow<List<String>> = db.discoveryDao().observeAll().map { list -> list.map { it.organismId } }

    suspend fun isDiscovered(organismId: String): Boolean = db.discoveryDao().isDiscovered(organismId)

    suspend fun getCollectionItems(): List<CollectionItem> {
        val discoveries = db.discoveryDao().observeAll().first()
        val organisms = discoveries.mapNotNull { d -> db.organismDao().getById(d.organismId)?.let { it.toDomain() to d } }
        return organisms.map { (organism, discovery) -> CollectionItem(organism, discovery.discoveredAtEpochMillis, discovery.viaExpeditionId) }
    }

    /** Descubre un organismo (si no lo estaba ya) y otorga la XP correspondiente en una sola transacción. */
    suspend fun discoverOrganism(organismId: String, viaExpeditionId: String?, atEpochMillis: Long): List<Badge> {
        var newlyUnlocked: List<Badge> = emptyList()
        db.withTransaction {
            val alreadyDiscovered = db.discoveryDao().isDiscovered(organismId)
            if (!alreadyDiscovered) {
                val organism = db.organismDao().getById(organismId)
                db.discoveryDao().insert(OrganismDiscoveryEntity(organismId, atEpochMillis, viaExpeditionId))
                organism?.let { db.profileDao().addXp(it.rarity.xpValue) }
            }
            newlyUnlocked = evaluateAndPersistNewBadges()
        }
        return newlyUnlocked
    }

    // ---------- Expediciones ----------

    fun observeExpeditionProgress(): Flow<List<com.educalab.ninobiologo.domain.model.ExpeditionProgress>> =
        db.expeditionProgressDao().observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun getExpeditionProgress(expeditionId: String): com.educalab.ninobiologo.domain.model.ExpeditionProgress? =
        db.expeditionProgressDao().getById(expeditionId)?.toDomain()

    /** Marca avance dentro de una expedición (sin completarla todavía). */
    suspend fun updateExpeditionStep(expeditionId: String, stepsCompleted: Int, totalSteps: Int, nowEpochMillis: Long) {
        val existing = db.expeditionProgressDao().getById(expeditionId)
        db.expeditionProgressDao().upsert(
            ExpeditionProgressEntity(
                expeditionId = expeditionId,
                state = if (stepsCompleted >= totalSteps) ModuleState.COMPLETADO else ModuleState.INICIADO,
                stepsCompleted = stepsCompleted,
                totalSteps = totalSteps,
                bestStars = existing?.bestStars ?: 0,
                timesCompleted = existing?.timesCompleted ?: 0,
                lastAttemptEpochMillis = nowEpochMillis
            )
        )
    }

    /** Completa una expedición: registra estrellas, otorga XP y desbloquea el organismo relacionado. */
    suspend fun completeExpedition(expedition: Expedition, stars: Int, nowEpochMillis: Long): List<Badge> {
        var newlyUnlocked: List<Badge> = emptyList()
        db.withTransaction {
            val existing = db.expeditionProgressDao().getById(expedition.id)
            val bestStars = maxOf(existing?.bestStars ?: 0, stars)
            db.expeditionProgressDao().upsert(
                ExpeditionProgressEntity(
                    expeditionId = expedition.id,
                    state = if (bestStars >= 3) ModuleState.DOMINADO else ModuleState.COMPLETADO,
                    stepsCompleted = expedition.steps.size,
                    totalSteps = expedition.steps.size,
                    bestStars = bestStars,
                    timesCompleted = (existing?.timesCompleted ?: 0) + 1,
                    lastAttemptEpochMillis = nowEpochMillis
                )
            )
            db.profileDao().addXp(expedition.rewardXp)
            expedition.relatedOrganismIds.forEach { organismId ->
                if (!db.discoveryDao().isDiscovered(organismId)) {
                    val organism = db.organismDao().getById(organismId)
                    db.discoveryDao().insert(OrganismDiscoveryEntity(organismId, nowEpochMillis, expedition.id))
                    organism?.let { db.profileDao().addXp(it.rarity.xpValue) }
                }
            }
            newlyUnlocked = evaluateAndPersistNewBadges()
        }
        return newlyUnlocked
    }

    // ---------- Desafíos ----------

    suspend fun recordChallengeAttempt(challenge: Challenge, correctCount: Int, totalCount: Int, nowEpochMillis: Long): Pair<ChallengeAttempt, List<Badge>> {
        val scoreResult = ChallengeScoringEngine.score(correctCount, totalCount, challenge.rewardXp)
        var attempt: ChallengeAttempt? = null
        var newlyUnlocked: List<Badge> = emptyList()
        db.withTransaction {
            val id = db.challengeAttemptDao().insert(
                ChallengeAttemptEntity(
                    challengeId = challenge.id,
                    correctCount = correctCount,
                    totalCount = totalCount,
                    stars = scoreResult.stars,
                    xpAwarded = scoreResult.xpAwarded,
                    attemptedAtEpochMillis = nowEpochMillis
                )
            )
            db.profileDao().addXp(scoreResult.xpAwarded)
            attempt = ChallengeAttempt(id, challenge.id, correctCount, totalCount, scoreResult.stars, scoreResult.xpAwarded, nowEpochMillis)
            newlyUnlocked = evaluateAndPersistNewBadges()
        }
        return attempt!! to newlyUnlocked
    }

    // ---------- Constructor de Ecosistemas ----------

    suspend fun saveEcosystemBuild(templateId: String, producers: Int, herbivores: Int, carnivores: Int, decomposers: Int, nowEpochMillis: Long): Pair<EcosystemBuild, List<Badge>> {
        val result = EcosystemBalanceEngine.evaluate(producers, herbivores, carnivores, decomposers)
        var build: EcosystemBuild? = null 
        var newlyUnlocked: List<Badge> = emptyList()
        db.withTransaction {
            val id = db.ecosystemBuildDao().insert(
                EcosystemBuildEntity(
                    templateId = templateId, producers = producers, herbivores = herbivores,
                    carnivores = carnivores, decomposers = decomposers, balanceScore = result.score,
                    status = result.status, savedAtEpochMillis = nowEpochMillis
                )
            )
            if (result.score >= 60) {
                db.profileDao().addXp(RankEngine.XP_PER_STABLE_ECOSYSTEM)
            }
            build = EcosystemBuild(id, templateId, producers, herbivores, carnivores, decomposers, result.score, result.status, nowEpochMillis)
            newlyUnlocked = evaluateAndPersistNewBadges()
        }
        return build!! to newlyUnlocked
    }

    // ---------- Insignias ----------

    fun observeBadgeUnlocks(): Flow<List<String>> = db.badgeUnlockDao().observeAll().map { list -> list.map { it.badgeId } }

    private suspend fun evaluateAndPersistNewBadges(): List<Badge> {
        val allBadges = db.badgeDao().observeAll().first()
        val unlockedIds = db.badgeUnlockDao().getUnlockedIds().toSet()
        val discoveredIds = db.discoveryDao().getDiscoveredIds().toSet()
        val allOrganisms = db.organismDao().observeAll().first()
        val legendaryDiscovered = allOrganisms.any { it.id in discoveredIds && it.rarity == com.educalab.ninobiologo.domain.model.OrganismRarity.LEGENDARIO }

        val biomes = db.biomeDao().observeAll().first()
        val completionByBiome = biomes.associate { biome ->
            val biomeOrganisms = allOrganisms.filter { it.biomeId == biome.id }.map { it.toDomain() }
            biome.id to CollectionEngine.biomeCompletionPercent(biomeOrganisms, discoveredIds)
        }

        val stats = CollectionEngine.ProgressStats(
            discoveriesCount = discoveredIds.size,
            expeditionsCompleted = db.expeditionProgressDao().completedCount(),
            stableEcosystemsCount = db.ecosystemBuildDao().stableCount(),
            challengesPassed = db.challengeAttemptDao().passedChallengesCount(),
            biomeCompletionPercents = completionByBiome,
            legendaryDiscovered = legendaryDiscovered
        )

        val newly = CollectionEngine.newlyUnlockedBadges(allBadges.map { it.toDomain() }, unlockedIds, stats)
        val now = System.currentTimeMillis()
        newly.forEach { badge ->
            db.badgeUnlockDao().insert(BadgeUnlockEntity(badge.id, now))
            db.profileDao().addXp(RankEngine.XP_PER_BADGE)
        }
        return newly
    }

    // ---------- Diario del Explorador ----------

    fun observeJournalEntries(): Flow<List<JournalEntry>> = db.journalDao().observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun addJournalEntry(type: JournalEntryType, title: String, note: String, filePath: String?, relatedBiomeId: String?, nowEpochMillis: Long): Long =
        db.journalDao().insert(JournalEntryEntity(type = type, title = title, note = note, filePath = filePath, relatedBiomeId = relatedBiomeId, createdAtEpochMillis = nowEpochMillis))

    suspend fun deleteJournalEntry(id: Long) = db.journalDao().delete(id)

    // ---------- Reinicio de progreso ----------

    /**
     * Borra todo el progreso (descubrimientos, expediciones, desafíos, insignias, ecosistemas y
     * XP) pero conserva el contenido semilla y el alias/avatar del perfil. Pensado para el caso
     * límite "reinicio de información" (sección 18 de la especificación maestra) y para que un
     * adulto pueda reiniciar la experiencia sin reinstalar la app.
     */
    suspend fun resetProgress() {
        db.withTransaction {
            db.discoveryDao().clearAll()
            db.expeditionProgressDao().clearAll()
            db.challengeAttemptDao().clearAll()
            db.badgeUnlockDao().clearAll()
            db.ecosystemBuildDao().clearAll()
            val current = db.profileDao().get()
            if (current != null) {
                db.profileDao().update(current.copy(totalXp = 0))
            }
        }
    }
}
