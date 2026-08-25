package com.educalab.ninobiologo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.educalab.ninobiologo.data.local.entity.BadgeEntity
import com.educalab.ninobiologo.data.local.entity.BiomeEntity
import com.educalab.ninobiologo.data.local.entity.BodyOrganEntity
import com.educalab.ninobiologo.data.local.entity.BodySystemEntity
import com.educalab.ninobiologo.data.local.entity.CellModelEntity
import com.educalab.ninobiologo.data.local.entity.CellStructureEntity
import com.educalab.ninobiologo.data.local.entity.ChallengeEntity
import com.educalab.ninobiologo.data.local.entity.EcosystemTemplateEntity
import com.educalab.ninobiologo.data.local.entity.ExpeditionEntity
import com.educalab.ninobiologo.data.local.entity.ExpeditionStepEntity
import com.educalab.ninobiologo.data.local.entity.OrganismEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BiomeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(biomes: List<BiomeEntity>)

    @Query("SELECT * FROM biomes ORDER BY orderIndex ASC")
    fun observeAll(): Flow<List<BiomeEntity>>

    @Query("SELECT * FROM biomes WHERE id = :id")
    suspend fun getById(id: String): BiomeEntity?

    @Query("SELECT COUNT(*) FROM biomes")
    suspend fun count(): Int
}

@Dao
interface OrganismDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(organisms: List<OrganismEntity>)

    @Query("SELECT * FROM organisms WHERE biomeId = :biomeId ORDER BY name ASC")
    fun observeByBiome(biomeId: String): Flow<List<OrganismEntity>>

    @Query("SELECT * FROM organisms ORDER BY name ASC")
    fun observeAll(): Flow<List<OrganismEntity>>

    @Query("SELECT * FROM organisms WHERE id = :id")
    suspend fun getById(id: String): OrganismEntity?

    @Query("SELECT * FROM organisms WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<String>): List<OrganismEntity>

    @Query("SELECT COUNT(*) FROM organisms")
    suspend fun count(): Int
}

@Dao
interface ExpeditionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expeditions: List<ExpeditionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<ExpeditionStepEntity>)

    @Query("SELECT * FROM expeditions WHERE biomeId = :biomeId ORDER BY orderIndex ASC")
    fun observeByBiome(biomeId: String): Flow<List<ExpeditionEntity>>

    @Query("SELECT * FROM expeditions WHERE id = :id")
    suspend fun getById(id: String): ExpeditionEntity?

    @Query("SELECT * FROM expedition_steps WHERE expeditionId = :expeditionId ORDER BY orderIndex ASC")
    suspend fun getSteps(expeditionId: String): List<ExpeditionStepEntity>

    @Query("SELECT COUNT(*) FROM expeditions")
    suspend fun count(): Int
}

@Dao
interface CellModelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(models: List<CellModelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStructures(structures: List<CellStructureEntity>)

    @Query("SELECT * FROM cell_models")
    fun observeAll(): Flow<List<CellModelEntity>>

    @Query("SELECT * FROM cell_structures WHERE cellModelId = :cellModelId")
    suspend fun getStructures(cellModelId: String): List<CellStructureEntity>

    @Query("SELECT COUNT(*) FROM cell_models")
    suspend fun count(): Int
}

@Dao
interface BodySystemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(systems: List<BodySystemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrgans(organs: List<BodyOrganEntity>)

    @Query("SELECT * FROM body_systems")
    fun observeAll(): Flow<List<BodySystemEntity>>

    @Query("SELECT * FROM body_organs WHERE bodySystemId = :systemId")
    suspend fun getOrgans(systemId: String): List<BodyOrganEntity>

    @Query("SELECT COUNT(*) FROM body_systems")
    suspend fun count(): Int
}

@Dao
interface EcosystemTemplateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(templates: List<EcosystemTemplateEntity>)

    @Query("SELECT * FROM ecosystem_templates WHERE biomeId = :biomeId")
    fun observeByBiome(biomeId: String): Flow<List<EcosystemTemplateEntity>>

    @Query("SELECT * FROM ecosystem_templates WHERE id = :id")
    suspend fun getById(id: String): EcosystemTemplateEntity?

    @Query("SELECT COUNT(*) FROM ecosystem_templates")
    suspend fun count(): Int
}

@Dao
interface ChallengeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(challenges: List<ChallengeEntity>)

    @Query("SELECT * FROM challenges WHERE biomeId = :biomeId")
    fun observeByBiome(biomeId: String): Flow<List<ChallengeEntity>>

    @Query("SELECT * FROM challenges WHERE id = :id")
    suspend fun getById(id: String): ChallengeEntity?

    @Query("SELECT COUNT(*) FROM challenges")
    suspend fun count(): Int
}

@Dao
interface BadgeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(badges: List<BadgeEntity>)

    @Query("SELECT * FROM badges")
    fun observeAll(): Flow<List<BadgeEntity>>

    @Query("SELECT COUNT(*) FROM badges")
    suspend fun count(): Int
}
