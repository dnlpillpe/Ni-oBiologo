package com.educalab.ninobiologo.data.repository

import androidx.room.withTransaction
import com.educalab.ninobiologo.data.local.AppDatabase
import com.educalab.ninobiologo.data.local.entity.ChallengeAttemptEntity
import com.educalab.ninobiologo.data.local.entity.CollectibleUnlockEntity
import com.educalab.ninobiologo.data.local.entity.CreatureCollectionEntity
import com.educalab.ninobiologo.data.local.entity.DiscoveryFoundEntity
import com.educalab.ninobiologo.data.local.entity.DiscoveryJournalEntity
import com.educalab.ninobiologo.data.local.entity.ExperimentResultEntity
import com.educalab.ninobiologo.data.local.entity.LabUpgradeUnlockEntity
import com.educalab.ninobiologo.data.local.entity.SampleExplorationEntity
import com.educalab.ninobiologo.domain.logic.ChallengeScoringEngine
import com.educalab.ninobiologo.domain.logic.CollectionEngine
import com.educalab.ninobiologo.domain.logic.CreatureAdaptationEngine
import com.educalab.ninobiologo.domain.logic.ExperimentEngine
import com.educalab.ninobiologo.domain.logic.RankEngine
import com.educalab.ninobiologo.domain.model.BodySystem
import com.educalab.ninobiologo.domain.model.CellModel
import com.educalab.ninobiologo.domain.model.Challenge
import com.educalab.ninobiologo.domain.model.ChallengeAttempt
import com.educalab.ninobiologo.domain.model.CreatureCollection
import com.educalab.ninobiologo.domain.model.CreaturePartOption
import com.educalab.ninobiologo.domain.model.DiscoveryJournalEntry
import com.educalab.ninobiologo.domain.model.Experiment
import com.educalab.ninobiologo.domain.model.ExperimentResult
import com.educalab.ninobiologo.domain.model.ExplorerProfile
import com.educalab.ninobiologo.domain.model.JournalEntryType
import com.educalab.ninobiologo.domain.model.LabCollectible
import com.educalab.ninobiologo.domain.model.LaboratoryUpgrade
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery
import com.educalab.ninobiologo.domain.model.MicroscopicEnvironment
import com.educalab.ninobiologo.domain.model.MuseumItem
import com.educalab.ninobiologo.domain.model.SampleExploration
import com.educalab.ninobiologo.domain.model.SampleExplorationState
import com.educalab.ninobiologo.domain.model.ScientificSample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Punto único de acceso a los datos de NiñoBiólogo. Los ViewModel nunca hablan con Room
 * directamente: siempre pasan por aquí, y aquí es donde se apoyan en los motores de domain/logic
 * para calcular resultados reales.
 */
class BiologyRepository(private val db: AppDatabase) {

    // ---------- Contenido ----------

    fun observeEnvironments(): Flow<List<MicroscopicEnvironment>> =
        db.environmentDao().observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun getEnvironment(id: String): MicroscopicEnvironment? = db.environmentDao().getById(id)?.toDomain()

    fun observeSamplesByEnvironment(environmentId: String): Flow<List<ScientificSample>> =
        db.sampleDao().observeByEnvironment(environmentId).map { list -> list.map { it.toDomain() } }

    suspend fun getSample(id: String): ScientificSample? = db.sampleDao().getById(id)?.toDomain()

    suspend fun getDiscoveriesForSample(sampleId: String): List<MicroscopeDiscovery> =
        db.microscopeDiscoveryDao().getBySample(sampleId).map { it.toDomain() }

    fun observeDiscoveriesByEnvironment(environmentId: String): Flow<List<MicroscopeDiscovery>> =
        db.microscopeDiscoveryDao().observeByEnvironment(environmentId).map { list -> list.map { it.toDomain() } }

    fun observeAllDiscoveries(): Flow<List<MicroscopeDiscovery>> =
        db.microscopeDiscoveryDao().observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun getDiscovery(id: String): MicroscopeDiscovery? = db.microscopeDiscoveryDao().getById(id)?.toDomain()

    suspend fun getDiscoveriesByIds(ids: List<String>): List<MicroscopeDiscovery> = db.microscopeDiscoveryDao().getByIds(ids).map { it.toDomain() }

    suspend fun getCellModels(): List<CellModel> =
        db.cellModelDao().observeAll().first().map { it.toDomain(db.cellModelDao().getStructures(it.id)) }

    suspend fun getBodySystems(): List<BodySystem> =
        db.bodySystemDao().observeAll().first().map { it.toDomain(db.bodySystemDao().getOrgans(it.id)) }

    fun observeExperimentsByEnvironment(environmentId: String): Flow<List<Experiment>> =
        db.experimentDao().observeByEnvironment(environmentId).map { list -> list.map { it.toDomain() } }

    suspend fun getExperiment(id: String): Experiment? = db.experimentDao().getById(id)?.toDomain()

    suspend fun getCreaturePartOptions(): List<CreaturePartOption> =
        db.creaturePartOptionDao().observeAll().first().map { it.toDomain() }

    fun observeChallengesByEnvironment(environmentId: String): Flow<List<Challenge>> =
        db.challengeDao().observeByEnvironment(environmentId).map { list -> list.map { it.toDomain() } }

    suspend fun getChallenge(id: String): Challenge? = db.challengeDao().getById(id)?.toDomain()

    fun observeLabCollectibles(): Flow<List<LabCollectible>> = db.labCollectibleDao().observeAll().map { list -> list.map { it.toDomain() } }

    fun observeLaboratoryUpgrades(): Flow<List<LaboratoryUpgrade>> = db.laboratoryUpgradeDao().observeAll().map { list -> list.map { it.toDomain() } }

    // ---------- Perfil ----------

    fun observeProfile(): Flow<ExplorerProfile?> = db.explorerProfileDao().observe().map { it?.toDomain() }

    suspend fun getProfile(): ExplorerProfile? = db.explorerProfileDao().get()?.toDomain()

    suspend fun updateAlias(alias: String) {
        val current = db.explorerProfileDao().get() ?: return
        db.explorerProfileDao().update(current.copy(alias = alias))
    }

    suspend fun updateAvatar(avatarKey: String) {
        val current = db.explorerProfileDao().get() ?: return
        db.explorerProfileDao().update(current.copy(avatarKey = avatarKey))
    }

    suspend fun setSoundEnabled(enabled: Boolean) = db.explorerProfileDao().setSoundEnabled(enabled)
    suspend fun setHapticsEnabled(enabled: Boolean) = db.explorerProfileDao().setHapticsEnabled(enabled)
    suspend fun completeOnboarding() = db.explorerProfileDao().markOnboardingCompleted()

    // ---------- Descubrimientos (Mi Museo de la Vida) ----------

    fun observeDiscoveriesFound(): Flow<List<String>> = db.discoveryFoundDao().observeAll().map { list -> list.map { it.discoveryId } }

    suspend fun isDiscovered(discoveryId: String): Boolean = db.discoveryFoundDao().isDiscovered(discoveryId)

    suspend fun getMuseumItems(): List<MuseumItem> {
        val found = db.discoveryFoundDao().observeAll().first()
        val items = found.mapNotNull { f -> db.microscopeDiscoveryDao().getById(f.discoveryId)?.let { it.toDomain() to f } }
        return items.map { (discovery, found) -> MuseumItem(discovery, found.discoveredAtEpochMillis, found.viaSampleId) }
    }

    /** Descubre un hallazgo (si no lo estaba ya) y otorga la XP correspondiente en una sola transacción. */
    suspend fun discoverItem(discoveryId: String, viaSampleId: String?, atEpochMillis: Long): List<LabCollectible> {
        var newlyUnlocked: List<LabCollectible> = emptyList()
        db.withTransaction {
            val alreadyDiscovered = db.discoveryFoundDao().isDiscovered(discoveryId)
            if (!alreadyDiscovered) {
                val discovery = db.microscopeDiscoveryDao().getById(discoveryId)
                db.discoveryFoundDao().insert(DiscoveryFoundEntity(discoveryId, atEpochMillis, viaSampleId))
                discovery?.let { db.explorerProfileDao().addXp(it.rarity.xpValue) }
            }
            newlyUnlocked = evaluateAndPersistNewCollectibles()
            evaluateAndPersistNewLabUpgrades()
        }
        return newlyUnlocked
    }

    // ---------- Exploración de muestras ----------

    fun observeSampleExploration(): Flow<List<SampleExploration>> =
        db.sampleExplorationDao().observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun getSampleExploration(sampleId: String): SampleExploration? = db.sampleExplorationDao().getById(sampleId)?.toDomain()

    /** Marca avance dentro de una muestra (Explorar -> Observar -> Experimentar), sin completarla todavía. */
    suspend fun updateSampleExplorationState(sampleId: String, state: SampleExplorationState, totalDiscoveries: Int, nowEpochMillis: Long) {
        val existing = db.sampleExplorationDao().getById(sampleId)
        db.sampleExplorationDao().upsert(
            SampleExplorationEntity(
                sampleId = sampleId,
                state = state,
                discoveriesFound = existing?.discoveriesFound ?: 0,
                totalDiscoveries = totalDiscoveries,
                lastAttemptEpochMillis = nowEpochMillis
            )
        )
    }

    /** Completa una muestra: descubre todos sus hallazgos y marca el estado como DESCUBIERTO. */
    suspend fun completeSample(sample: ScientificSample, discoveries: List<MicroscopeDiscovery>, nowEpochMillis: Long): List<LabCollectible> {
        var newlyUnlocked: List<LabCollectible> = emptyList()
        db.withTransaction {
            discoveries.forEach { discovery ->
                if (!db.discoveryFoundDao().isDiscovered(discovery.id)) {
                    db.discoveryFoundDao().insert(DiscoveryFoundEntity(discovery.id, nowEpochMillis, sample.id))
                    db.explorerProfileDao().addXp(discovery.rarity.xpValue)
                }
            }
            db.sampleExplorationDao().upsert(
                SampleExplorationEntity(
                    sampleId = sample.id,
                    state = SampleExplorationState.DESCUBIERTO,
                    discoveriesFound = discoveries.size,
                    totalDiscoveries = discoveries.size,
                    lastAttemptEpochMillis = nowEpochMillis
                )
            )
            newlyUnlocked = evaluateAndPersistNewCollectibles()
            evaluateAndPersistNewLabUpgrades()
        }
        return newlyUnlocked
    }

    // ---------- Analizador ----------

    suspend fun recordChallengeAttempt(challenge: Challenge, correctCount: Int, totalCount: Int, nowEpochMillis: Long): Pair<ChallengeAttempt, List<LabCollectible>> {
        val scoreResult = ChallengeScoringEngine.score(correctCount, totalCount, challenge.rewardXp)
        var attempt: ChallengeAttempt? = null
        var newlyUnlocked: List<LabCollectible> = emptyList()
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
            db.explorerProfileDao().addXp(scoreResult.xpAwarded)
            attempt = ChallengeAttempt(id, challenge.id, correctCount, totalCount, scoreResult.stars, scoreResult.xpAwarded, nowEpochMillis)
            newlyUnlocked = evaluateAndPersistNewCollectibles()
            evaluateAndPersistNewLabUpgrades()
        }
        return attempt!! to newlyUnlocked
    }

    // ---------- Experimentos Biológicos ----------

    suspend fun runExperiment(experiment: Experiment, variableValue: Int, nowEpochMillis: Long): Pair<ExperimentResult, List<LabCollectible>> {
        val evaluation = ExperimentEngine.evaluate(experiment, variableValue)
        var result: ExperimentResult? = null
        var newlyUnlocked: List<LabCollectible> = emptyList()
        db.withTransaction {
            val id = db.experimentResultDao().insert(
                ExperimentResultEntity(
                    experimentId = experiment.id,
                    variableValue = variableValue,
                    outcome = evaluation.outcome,
                    message = evaluation.message,
                    xpAwarded = evaluation.xpAwarded,
                    savedAtEpochMillis = nowEpochMillis
                )
            )
            db.explorerProfileDao().addXp(evaluation.xpAwarded)
            result = ExperimentResult(id, experiment.id, variableValue, evaluation.outcome, evaluation.message, evaluation.xpAwarded, nowEpochMillis)
            newlyUnlocked = evaluateAndPersistNewCollectibles()
            evaluateAndPersistNewLabUpgrades()
        }
        return result!! to newlyUnlocked
    }

    // ---------- Constructor Biológico (creación de criaturas) ----------

    fun observeCreatureCollection(): Flow<List<CreatureCollection>> = db.creatureCollectionDao().observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun saveCreature(
        name: String,
        selections: List<CreaturePartOption>,
        targetEnvironmentId: String,
        nowEpochMillis: Long
    ): Pair<CreatureCollection, List<LabCollectible>> {
        val evaluation = CreatureAdaptationEngine.evaluate(selections, targetEnvironmentId)
        val byCategory = selections.associateBy { it.category }
        var creature: CreatureCollection? = null
        var newlyUnlocked: List<LabCollectible> = emptyList()
        db.withTransaction {
            val id = db.creatureCollectionDao().insert(
                CreatureCollectionEntity(
                    name = name,
                    formaId = byCategory[com.educalab.ninobiologo.domain.model.CreaturePartCategory.FORMA]?.id ?: "",
                    movimientoId = byCategory[com.educalab.ninobiologo.domain.model.CreaturePartCategory.MOVIMIENTO]?.id ?: "",
                    alimentacionId = byCategory[com.educalab.ninobiologo.domain.model.CreaturePartCategory.ALIMENTACION]?.id ?: "",
                    adaptacionId = byCategory[com.educalab.ninobiologo.domain.model.CreaturePartCategory.ADAPTACION]?.id ?: "",
                    targetEnvironmentId = targetEnvironmentId,
                    fitScore = evaluation.fitScore,
                    createdAtEpochMillis = nowEpochMillis
                )
            )
            creature = CreatureCollection(id, name, byCategory[com.educalab.ninobiologo.domain.model.CreaturePartCategory.FORMA]?.id ?: "", byCategory[com.educalab.ninobiologo.domain.model.CreaturePartCategory.MOVIMIENTO]?.id ?: "", byCategory[com.educalab.ninobiologo.domain.model.CreaturePartCategory.ALIMENTACION]?.id ?: "", byCategory[com.educalab.ninobiologo.domain.model.CreaturePartCategory.ADAPTACION]?.id ?: "", targetEnvironmentId, evaluation.fitScore, nowEpochMillis)
            newlyUnlocked = evaluateAndPersistNewCollectibles()
            evaluateAndPersistNewLabUpgrades()
        }
        return creature!! to newlyUnlocked
    }

    // ---------- Coleccionables y mejoras del laboratorio ----------

    fun observeCollectibleUnlocks(): Flow<List<String>> = db.collectibleUnlockDao().observeAll().map { list -> list.map { it.collectibleId } }

    fun observeLabUpgradeUnlocks(): Flow<List<String>> = db.labUpgradeUnlockDao().observeAll().map { list -> list.map { it.upgradeId } }

    private suspend fun currentProgressStats(): CollectionEngine.ProgressStats {
        val discoveredIds = db.discoveryFoundDao().getDiscoveredIds().toSet()
        val allDiscoveries = db.microscopeDiscoveryDao().observeAll().first()
        val legendaryDiscovered = allDiscoveries.any { it.id in discoveredIds && it.rarity == com.educalab.ninobiologo.domain.model.DiscoveryRarity.LEGENDARIO }

        val environments = db.environmentDao().observeAll().first()
        val completionByEnvironment = environments.associate { environment ->
            val envDiscoveries = allDiscoveries.filter { it.environmentId == environment.id }.map { it.toDomain() }
            environment.id to CollectionEngine.environmentCompletionPercent(envDiscoveries, discoveredIds)
        }

        return CollectionEngine.ProgressStats(
            discoveriesCount = discoveredIds.size,
            experimentsRun = db.experimentResultDao().count(),
            creaturesCreated = db.creatureCollectionDao().count(),
            analysisPassed = db.challengeAttemptDao().passedChallengesCount(),
            environmentCompletionPercents = completionByEnvironment,
            legendaryDiscovered = legendaryDiscovered
        )
    }

    private suspend fun evaluateAndPersistNewCollectibles(): List<LabCollectible> {
        val all = db.labCollectibleDao().observeAll().first()
        val unlockedIds = db.collectibleUnlockDao().getUnlockedIds().toSet()
        val stats = currentProgressStats()
        val newly = CollectionEngine.newlyUnlocked(
            all.map { it.toDomain() },
            idOf = { it.id },
            criteriaOf = { CollectionEngine.UnlockableCriteria(it.criteriaType, it.criteriaValue, it.environmentId) },
            alreadyUnlockedIds = unlockedIds,
            stats = stats
        )
        val now = System.currentTimeMillis()
        newly.forEach { collectible ->
            db.collectibleUnlockDao().insert(CollectibleUnlockEntity(collectible.id, now))
            db.explorerProfileDao().addXp(RankEngine.XP_PER_COLLECTIBLE)
        }
        return newly
    }

    private suspend fun evaluateAndPersistNewLabUpgrades(): List<LaboratoryUpgrade> {
        val all = db.laboratoryUpgradeDao().observeAll().first()
        val unlockedIds = db.labUpgradeUnlockDao().getUnlockedIds().toSet()
        val stats = currentProgressStats()
        val newly = CollectionEngine.newlyUnlocked(
            all.map { it.toDomain() },
            idOf = { it.id },
            criteriaOf = { CollectionEngine.UnlockableCriteria(it.criteriaType, it.criteriaValue, it.environmentId) },
            alreadyUnlockedIds = unlockedIds,
            stats = stats
        )
        val now = System.currentTimeMillis()
        newly.forEach { upgrade -> db.labUpgradeUnlockDao().insert(LabUpgradeUnlockEntity(upgrade.id, now)) }
        return newly
    }

    // ---------- Diario del Explorador ----------

    fun observeJournalEntries(): Flow<List<DiscoveryJournalEntry>> = db.discoveryJournalDao().observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun addJournalEntry(type: JournalEntryType, title: String, note: String, filePath: String?, relatedEnvironmentId: String?, nowEpochMillis: Long): Long =
        db.discoveryJournalDao().insert(DiscoveryJournalEntity(type = type, title = title, note = note, filePath = filePath, relatedEnvironmentId = relatedEnvironmentId, createdAtEpochMillis = nowEpochMillis))

    suspend fun deleteJournalEntry(id: Long) = db.discoveryJournalDao().delete(id)

    // ---------- Reinicio de progreso ----------

    /**
     * Borra todo el progreso (descubrimientos, exploración de muestras, análisis, coleccionables,
     * mejoras, experimentos y criaturas) pero conserva el contenido semilla, el diario y el
     * alias/avatar del perfil. Pensado para que un adulto pueda reiniciar la experiencia sin
     * reinstalar la app.
     */
    suspend fun resetProgress() {
        db.withTransaction {
            db.discoveryFoundDao().clearAll()
            db.sampleExplorationDao().clearAll()
            db.challengeAttemptDao().clearAll()
            db.collectibleUnlockDao().clearAll()
            db.labUpgradeUnlockDao().clearAll()
            db.experimentResultDao().clearAll()
            db.creatureCollectionDao().clearAll()
            val current = db.explorerProfileDao().get()
            if (current != null) {
                db.explorerProfileDao().update(current.copy(totalXp = 0))
            }
        }
    }
}
