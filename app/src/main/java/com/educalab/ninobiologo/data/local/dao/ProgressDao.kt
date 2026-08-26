package com.educalab.ninobiologo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.educalab.ninobiologo.data.local.entity.ChallengeAttemptEntity
import com.educalab.ninobiologo.data.local.entity.CollectibleUnlockEntity
import com.educalab.ninobiologo.data.local.entity.CreatureCollectionEntity
import com.educalab.ninobiologo.data.local.entity.DiscoveryFoundEntity
import com.educalab.ninobiologo.data.local.entity.DiscoveryJournalEntity
import com.educalab.ninobiologo.data.local.entity.ExperimentResultEntity
import com.educalab.ninobiologo.data.local.entity.ExplorerProfileEntity
import com.educalab.ninobiologo.data.local.entity.LabUpgradeUnlockEntity
import com.educalab.ninobiologo.data.local.entity.SampleExplorationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExplorerProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: ExplorerProfileEntity)

    @Update
    suspend fun update(profile: ExplorerProfileEntity)

    @Query("SELECT * FROM explorer_profile WHERE id = 1")
    fun observe(): Flow<ExplorerProfileEntity?>

    @Query("SELECT * FROM explorer_profile WHERE id = 1")
    suspend fun get(): ExplorerProfileEntity?

    @Query("UPDATE explorer_profile SET totalXp = totalXp + :delta WHERE id = 1")
    suspend fun addXp(delta: Int)

    @Query("UPDATE explorer_profile SET soundEnabled = :enabled WHERE id = 1")
    suspend fun setSoundEnabled(enabled: Boolean)

    @Query("UPDATE explorer_profile SET hapticsEnabled = :enabled WHERE id = 1")
    suspend fun setHapticsEnabled(enabled: Boolean)

    @Query("UPDATE explorer_profile SET onboardingCompleted = 1 WHERE id = 1")
    suspend fun markOnboardingCompleted()
}

@Dao
interface DiscoveryFoundDao {
    @Query("DELETE FROM discoveries_found")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(discovery: DiscoveryFoundEntity): Long

    @Query("SELECT * FROM discoveries_found")
    fun observeAll(): Flow<List<DiscoveryFoundEntity>>

    @Query("SELECT discoveryId FROM discoveries_found")
    suspend fun getDiscoveredIds(): List<String>

    @Query("SELECT COUNT(*) FROM discoveries_found")
    fun observeCount(): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM discoveries_found WHERE discoveryId = :discoveryId)")
    suspend fun isDiscovered(discoveryId: String): Boolean
}

@Dao
interface SampleExplorationDao {
    @Query("DELETE FROM sample_exploration")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: SampleExplorationEntity)

    @Query("SELECT * FROM sample_exploration")
    fun observeAll(): Flow<List<SampleExplorationEntity>>

    @Query("SELECT * FROM sample_exploration WHERE sampleId = :sampleId")
    suspend fun getById(sampleId: String): SampleExplorationEntity?

    @Query("SELECT COUNT(*) FROM sample_exploration WHERE state = 'DESCUBIERTO'")
    suspend fun completedCount(): Int
}

@Dao
interface ChallengeAttemptDao {
    @Query("DELETE FROM challenge_attempts")
    suspend fun clearAll()

    @Insert
    suspend fun insert(attempt: ChallengeAttemptEntity): Long

    @Query("SELECT * FROM challenge_attempts WHERE challengeId = :challengeId ORDER BY attemptedAtEpochMillis DESC")
    fun observeForChallenge(challengeId: String): Flow<List<ChallengeAttemptEntity>>

    @Query("SELECT COALESCE(MAX(stars), 0) FROM challenge_attempts WHERE challengeId = :challengeId")
    suspend fun bestStars(challengeId: String): Int

    @Query("SELECT COUNT(DISTINCT challengeId) FROM challenge_attempts WHERE stars >= 1")
    suspend fun passedChallengesCount(): Int

    @Query("SELECT COALESCE(SUM(xpAwarded), 0) FROM challenge_attempts")
    suspend fun totalXpAwarded(): Int
}

@Dao
interface CollectibleUnlockDao {
    @Query("DELETE FROM collectible_unlocks")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(unlock: CollectibleUnlockEntity): Long

    @Query("SELECT * FROM collectible_unlocks")
    fun observeAll(): Flow<List<CollectibleUnlockEntity>>

    @Query("SELECT collectibleId FROM collectible_unlocks")
    suspend fun getUnlockedIds(): List<String>

    @Query("SELECT COUNT(*) FROM collectible_unlocks")
    suspend fun count(): Int
}

@Dao
interface LabUpgradeUnlockDao {
    @Query("DELETE FROM lab_upgrade_unlocks")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(unlock: LabUpgradeUnlockEntity): Long

    @Query("SELECT * FROM lab_upgrade_unlocks")
    fun observeAll(): Flow<List<LabUpgradeUnlockEntity>>

    @Query("SELECT upgradeId FROM lab_upgrade_unlocks")
    suspend fun getUnlockedIds(): List<String>

    @Query("SELECT COUNT(*) FROM lab_upgrade_unlocks")
    suspend fun count(): Int
}

@Dao
interface CreatureCollectionDao {
    @Query("DELETE FROM creature_collection")
    suspend fun clearAll()

    @Insert
    suspend fun insert(creature: CreatureCollectionEntity): Long

    @Query("SELECT * FROM creature_collection ORDER BY createdAtEpochMillis DESC")
    fun observeAll(): Flow<List<CreatureCollectionEntity>>

    @Query("SELECT COUNT(*) FROM creature_collection")
    suspend fun count(): Int
}

@Dao
interface ExperimentResultDao {
    @Query("DELETE FROM experiment_results")
    suspend fun clearAll()

    @Insert
    suspend fun insert(result: ExperimentResultEntity): Long

    @Query("SELECT * FROM experiment_results WHERE experimentId = :experimentId ORDER BY savedAtEpochMillis DESC")
    fun observeForExperiment(experimentId: String): Flow<List<ExperimentResultEntity>>

    @Query("SELECT COUNT(*) FROM experiment_results")
    suspend fun count(): Int
}

@Dao
interface DiscoveryJournalDao {
    @Insert
    suspend fun insert(entry: DiscoveryJournalEntity): Long

    @Query("DELETE FROM discovery_journal WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM discovery_journal ORDER BY createdAtEpochMillis DESC")
    fun observeAll(): Flow<List<DiscoveryJournalEntity>>
}
