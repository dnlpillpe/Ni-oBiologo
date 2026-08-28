package com.educalab.ninobiologo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.educalab.ninobiologo.data.local.converters.Converters
import com.educalab.ninobiologo.data.local.dao.BodySystemDao
import com.educalab.ninobiologo.data.local.dao.CellModelDao
import com.educalab.ninobiologo.data.local.dao.ChallengeAttemptDao
import com.educalab.ninobiologo.data.local.dao.ChallengeDao
import com.educalab.ninobiologo.data.local.dao.CollectibleUnlockDao
import com.educalab.ninobiologo.data.local.dao.CreatureCollectionDao
import com.educalab.ninobiologo.data.local.dao.CreaturePartOptionDao
import com.educalab.ninobiologo.data.local.dao.DiscoveryFoundDao
import com.educalab.ninobiologo.data.local.dao.DiscoveryJournalDao
import com.educalab.ninobiologo.data.local.dao.EnvironmentDao
import com.educalab.ninobiologo.data.local.dao.ExperimentDao
import com.educalab.ninobiologo.data.local.dao.ExperimentResultDao
import com.educalab.ninobiologo.data.local.dao.ExplorerProfileDao
import com.educalab.ninobiologo.data.local.dao.LabCollectibleDao
import com.educalab.ninobiologo.data.local.dao.LabUpgradeUnlockDao
import com.educalab.ninobiologo.data.local.dao.LaboratoryUpgradeDao
import com.educalab.ninobiologo.data.local.dao.MicroscopeDiscoveryDao
import com.educalab.ninobiologo.data.local.dao.SampleDao
import com.educalab.ninobiologo.data.local.dao.SampleExplorationDao
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(
    entities = [
        MicroscopicEnvironmentEntity::class,
        ScientificSampleEntity::class,
        MicroscopeDiscoveryEntity::class,
        CellModelEntity::class,
        CellStructureEntity::class,
        BodySystemEntity::class,
        BodyOrganEntity::class,
        ExperimentEntity::class,
        CreaturePartOptionEntity::class,
        ChallengeEntity::class,
        LabCollectibleEntity::class,
        LaboratoryUpgradeEntity::class,
        ExplorerProfileEntity::class,
        DiscoveryFoundEntity::class,
        SampleExplorationEntity::class,
        ChallengeAttemptEntity::class,
        CollectibleUnlockEntity::class,
        LabUpgradeUnlockEntity::class,
        CreatureCollectionEntity::class,
        ExperimentResultEntity::class,
        DiscoveryJournalEntity::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun environmentDao(): EnvironmentDao
    abstract fun sampleDao(): SampleDao
    abstract fun microscopeDiscoveryDao(): MicroscopeDiscoveryDao
    abstract fun cellModelDao(): CellModelDao
    abstract fun bodySystemDao(): BodySystemDao
    abstract fun experimentDao(): ExperimentDao
    abstract fun creaturePartOptionDao(): CreaturePartOptionDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun labCollectibleDao(): LabCollectibleDao
    abstract fun laboratoryUpgradeDao(): LaboratoryUpgradeDao

    abstract fun explorerProfileDao(): ExplorerProfileDao
    abstract fun discoveryFoundDao(): DiscoveryFoundDao
    abstract fun sampleExplorationDao(): SampleExplorationDao
    abstract fun challengeAttemptDao(): ChallengeAttemptDao
    abstract fun collectibleUnlockDao(): CollectibleUnlockDao
    abstract fun labUpgradeUnlockDao(): LabUpgradeUnlockDao
    abstract fun creatureCollectionDao(): CreatureCollectionDao
    abstract fun experimentResultDao(): ExperimentResultDao
    abstract fun discoveryJournalDao(): DiscoveryJournalDao

    companion object {
        private const val DATABASE_NAME = "ninobiologo.db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : Callback() {
                    override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Una base recién creada nunca debe verse vacía: se siembra en la primera
                        // apertura real (ver DatabaseSeeder). Se lanza en un scope de aplicación
                        // porque onCreate no es suspend.
                        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                            val database = getInstance(context)
                            DatabaseSeeder(database).seedIfEmpty()
                        }
                    }
                })
                .fallbackToDestructiveMigration() // Sin backend: no hay datos remotos que preservar.
                .build()
    }
}
