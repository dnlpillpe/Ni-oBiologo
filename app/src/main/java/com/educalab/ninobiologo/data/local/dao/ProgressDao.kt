package com.educalab.ninobiologo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.educalab.ninobiologo.data.local.entity.BadgeUnlockEntity
import com.educalab.ninobiologo.data.local.entity.BiologistProfileEntity
import com.educalab.ninobiologo.data.local.entity.ChallengeAttemptEntity
import com.educalab.ninobiologo.data.local.entity.EcosystemBuildEntity
import com.educalab.ninobiologo.data.local.entity.ExpeditionProgressEntity
import com.educalab.ninobiologo.data.local.entity.JournalEntryEntity
import com.educalab.ninobiologo.data.local.entity.OrganismDiscoveryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: BiologistProfileEntity)

    @Update
    suspend fun update(profile: BiologistProfileEntity)

    @Query("SELECT * FROM biologist_profile WHERE id = 1")
    fun observe(): Flow<BiologistProfileEntity?>

    @Query("SELECT * FROM biologist_profile WHERE id = 1")
    suspend fun get(): BiologistProfileEntity?

    @Query("UPDATE biologist_profile SET totalXp = totalXp + :delta WHERE id = 1")
    suspend fun addXp(delta: Int)

    @Query("UPDATE biologist_profile SET soundEnabled = :enabled WHERE id = 1")
    suspend fun setSoundEnabled(enabled: Boolean)

    @Query("UPDATE biologist_profile SET hapticsEnabled = :enabled WHERE id = 1")
    suspend fun setHapticsEnabled(enabled: Boolean)

    @Query("UPDATE biologist_profile SET onboardingCompleted = 1 WHERE id = 1")
    suspend fun markOnboardingCompleted()
}

@Dao
interface DiscoveryDao {
    @Query("DELETE FROM organism_discoveries")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(discovery: OrganismDiscoveryEntity): Long

    @Query("SELECT * FROM organism_discoveries")
    fun observeAll(): Flow<List<OrganismDiscoveryEntity>>

    @Query("SELECT organismId FROM organism_discoveries")
    suspend fun getDiscoveredIds(): List<String>

    @Query("SELECT COUNT(*) FROM organism_discoveries")
    fun observeCount(): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM organism_discoveries WHERE organismId = :organismId)")
    suspend fun isDiscovered(organismId: String): Boolean
}

@Dao
interface ExpeditionProgressDao {
    @Query("DELETE FROM expedition_progress")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: ExpeditionProgressEntity)

    @Query("SELECT * FROM expedition_progress")
    fun observeAll(): Flow<List<ExpeditionProgressEntity>>

    @Query("SELECT * FROM expedition_progress WHERE expeditionId = :expeditionId")
    suspend fun getById(expeditionId: String): ExpeditionProgressEntity?

    @Query("SELECT COUNT(*) FROM expedition_progress WHERE state = 'COMPLETADO' OR state = 'DOMINADO'")
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
interface BadgeUnlockDao {
    @Query("DELETE FROM badge_unlocks")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(unlock: BadgeUnlockEntity): Long

    @Query("SELECT * FROM badge_unlocks")
    fun observeAll(): Flow<List<BadgeUnlockEntity>>

    @Query("SELECT badgeId FROM badge_unlocks")
    suspend fun getUnlockedIds(): List<String>

    @Query("SELECT COUNT(*) FROM badge_unlocks")
    suspend fun count(): Int
}

@Dao
interface EcosystemBuildDao {
    @Query("DELETE FROM ecosystem_builds")
    suspend fun clearAll()

    @Insert
    suspend fun insert(build: EcosystemBuildEntity): Long

    @Query("SELECT * FROM ecosystem_builds WHERE templateId = :templateId ORDER BY savedAtEpochMillis DESC")
    fun observeForTemplate(templateId: String): Flow<List<EcosystemBuildEntity>>

    @Query("SELECT COUNT(*) FROM ecosystem_builds WHERE status = 'ESTABLE' OR status = 'FLORECIENTE'")
    suspend fun stableCount(): Int
}

@Dao
interface JournalDao {
    @Insert
    suspend fun insert(entry: JournalEntryEntity): Long

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM journal_entries ORDER BY createdAtEpochMillis DESC")
    fun observeAll(): Flow<List<JournalEntryEntity>>
}
