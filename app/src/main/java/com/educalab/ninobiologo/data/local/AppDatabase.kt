package com.educalab.ninobiologo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.educalab.ninobiologo.data.local.converters.Converters
import com.educalab.ninobiologo.data.local.dao.BadgeDao
import com.educalab.ninobiologo.data.local.dao.BadgeUnlockDao
import com.educalab.ninobiologo.data.local.dao.BiomeDao
import com.educalab.ninobiologo.data.local.dao.BodySystemDao
import com.educalab.ninobiologo.data.local.dao.CellModelDao
import com.educalab.ninobiologo.data.local.dao.ChallengeAttemptDao
import com.educalab.ninobiologo.data.local.dao.ChallengeDao
import com.educalab.ninobiologo.data.local.dao.DiscoveryDao
import com.educalab.ninobiologo.data.local.dao.EcosystemBuildDao
import com.educalab.ninobiologo.data.local.dao.EcosystemTemplateDao
import com.educalab.ninobiologo.data.local.dao.ExpeditionDao
import com.educalab.ninobiologo.data.local.dao.ExpeditionProgressDao
import com.educalab.ninobiologo.data.local.dao.JournalDao
import com.educalab.ninobiologo.data.local.dao.OrganismDao
import com.educalab.ninobiologo.data.local.dao.ProfileDao
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(
    entities = [
        BiomeEntity::class,
        OrganismEntity::class,
        ExpeditionEntity::class,
        ExpeditionStepEntity::class,
        CellModelEntity::class,
        CellStructureEntity::class,
        BodySystemEntity::class,
        BodyOrganEntity::class,
        EcosystemTemplateEntity::class,
        ChallengeEntity::class,
        BadgeEntity::class,
        BiologistProfileEntity::class,
        OrganismDiscoveryEntity::class,
        ExpeditionProgressEntity::class,
        ChallengeAttemptEntity::class,
        BadgeUnlockEntity::class,
        EcosystemBuildEntity::class,
        JournalEntryEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun biomeDao(): BiomeDao
    abstract fun organismDao(): OrganismDao
    abstract fun expeditionDao(): ExpeditionDao
    abstract fun cellModelDao(): CellModelDao
    abstract fun bodySystemDao(): BodySystemDao
    abstract fun ecosystemTemplateDao(): EcosystemTemplateDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun badgeDao(): BadgeDao

    abstract fun profileDao(): ProfileDao
    abstract fun discoveryDao(): DiscoveryDao
    abstract fun expeditionProgressDao(): ExpeditionProgressDao
    abstract fun challengeAttemptDao(): ChallengeAttemptDao
    abstract fun badgeUnlockDao(): BadgeUnlockDao
    abstract fun ecosystemBuildDao(): EcosystemBuildDao
    abstract fun journalDao(): JournalDao

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
