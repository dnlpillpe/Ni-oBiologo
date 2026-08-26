package com.educalab.ninobiologo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.educalab.ninobiologo.data.local.entity.BodyOrganEntity
import com.educalab.ninobiologo.data.local.entity.BodySystemEntity
import com.educalab.ninobiologo.data.local.entity.CellModelEntity
import com.educalab.ninobiologo.data.local.entity.CellStructureEntity
import com.educalab.ninobiologo.data.local.entity.ChallengeEntity
import com.educalab.ninobiologo.data.local.entity.CreaturePartOptionEntity
import com.educalab.ninobiologo.data.local.entity.ExperimentEntity
import com.educalab.ninobiologo.data.local.entity.LabCollectibleEntity
import com.educalab.ninobiologo.data.local.entity.LaboratoryUpgradeEntity
import com.educalab.ninobiologo.data.local.entity.MicroscopeDiscoveryEntity
import com.educalab.ninobiologo.data.local.entity.MicroscopicEnvironmentEntity
import com.educalab.ninobiologo.data.local.entity.ScientificSampleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EnvironmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(environments: List<MicroscopicEnvironmentEntity>)

    @Query("SELECT * FROM microscopic_environments ORDER BY orderIndex ASC")
    fun observeAll(): Flow<List<MicroscopicEnvironmentEntity>>

    @Query("SELECT * FROM microscopic_environments WHERE id = :id")
    suspend fun getById(id: String): MicroscopicEnvironmentEntity?

    @Query("SELECT COUNT(*) FROM microscopic_environments")
    suspend fun count(): Int
}

@Dao
interface SampleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(samples: List<ScientificSampleEntity>)

    @Query("SELECT * FROM scientific_samples WHERE environmentId = :environmentId ORDER BY orderIndex ASC")
    fun observeByEnvironment(environmentId: String): Flow<List<ScientificSampleEntity>>

    @Query("SELECT * FROM scientific_samples WHERE id = :id")
    suspend fun getById(id: String): ScientificSampleEntity?

    @Query("SELECT COUNT(*) FROM scientific_samples")
    suspend fun count(): Int
}

@Dao
interface MicroscopeDiscoveryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(discoveries: List<MicroscopeDiscoveryEntity>)

    @Query("SELECT * FROM microscope_discoveries WHERE sampleId = :sampleId ORDER BY name ASC")
    suspend fun getBySample(sampleId: String): List<MicroscopeDiscoveryEntity>

    @Query("SELECT * FROM microscope_discoveries WHERE environmentId = :environmentId ORDER BY name ASC")
    fun observeByEnvironment(environmentId: String): Flow<List<MicroscopeDiscoveryEntity>>

    @Query("SELECT * FROM microscope_discoveries ORDER BY name ASC")
    fun observeAll(): Flow<List<MicroscopeDiscoveryEntity>>

    @Query("SELECT * FROM microscope_discoveries WHERE id = :id")
    suspend fun getById(id: String): MicroscopeDiscoveryEntity?

    @Query("SELECT * FROM microscope_discoveries WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<String>): List<MicroscopeDiscoveryEntity>

    @Query("SELECT COUNT(*) FROM microscope_discoveries")
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
interface ExperimentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(experiments: List<ExperimentEntity>)

    @Query("SELECT * FROM experiments WHERE environmentId = :environmentId ORDER BY orderIndex ASC")
    fun observeByEnvironment(environmentId: String): Flow<List<ExperimentEntity>>

    @Query("SELECT * FROM experiments WHERE id = :id")
    suspend fun getById(id: String): ExperimentEntity?

    @Query("SELECT COUNT(*) FROM experiments")
    suspend fun count(): Int
}

@Dao
interface CreaturePartOptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(options: List<CreaturePartOptionEntity>)

    @Query("SELECT * FROM creature_part_options")
    fun observeAll(): Flow<List<CreaturePartOptionEntity>>

    @Query("SELECT * FROM creature_part_options WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<String>): List<CreaturePartOptionEntity>

    @Query("SELECT COUNT(*) FROM creature_part_options")
    suspend fun count(): Int
}

@Dao
interface ChallengeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(challenges: List<ChallengeEntity>)

    @Query("SELECT * FROM challenges WHERE environmentId = :environmentId")
    fun observeByEnvironment(environmentId: String): Flow<List<ChallengeEntity>>

    @Query("SELECT * FROM challenges WHERE id = :id")
    suspend fun getById(id: String): ChallengeEntity?

    @Query("SELECT COUNT(*) FROM challenges")
    suspend fun count(): Int
}

@Dao
interface LabCollectibleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(collectibles: List<LabCollectibleEntity>)

    @Query("SELECT * FROM lab_collectibles")
    fun observeAll(): Flow<List<LabCollectibleEntity>>

    @Query("SELECT COUNT(*) FROM lab_collectibles")
    suspend fun count(): Int
}

@Dao
interface LaboratoryUpgradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(upgrades: List<LaboratoryUpgradeEntity>)

    @Query("SELECT * FROM laboratory_upgrades")
    fun observeAll(): Flow<List<LaboratoryUpgradeEntity>>

    @Query("SELECT COUNT(*) FROM laboratory_upgrades")
    suspend fun count(): Int
}
