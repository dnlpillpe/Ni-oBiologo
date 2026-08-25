package com.educalab.ninobiologo.data.local

import com.educalab.ninobiologo.data.local.entity.BiologistProfileEntity
import com.educalab.ninobiologo.data.local.seed.SeedContent
import java.util.UUID

/**
 * Puebla la base de datos en la primera apertura real de la app. La cantidad de contenido está
 * generada por tools/generate_seed_data.py siguiendo las cantidades exigidas por el prompt
 * específico V4 (40 expediciones, 50 organismos, 20 ecosistemas, 30 desafíos, 15 recompensas).
 */
class DatabaseSeeder(private val database: AppDatabase) {

    suspend fun seedIfEmpty() {
        if (database.biomeDao().count() > 0) return // ya sembrada: evita duplicar contenido

        database.biomeDao().insertAll(SeedContent.biomes)
        database.organismDao().insertAll(SeedContent.organisms)
        database.expeditionDao().insertAll(SeedContent.expeditions)
        database.expeditionDao().insertSteps(SeedContent.expeditionSteps)
        database.cellModelDao().insertAll(SeedContent.cellModels)
        database.cellModelDao().insertStructures(SeedContent.cellStructures)
        database.bodySystemDao().insertAll(SeedContent.bodySystems)
        database.bodySystemDao().insertOrgans(SeedContent.bodyOrgans)
        database.ecosystemTemplateDao().insertAll(SeedContent.ecosystemTemplates)
        database.challengeDao().insertAll(SeedContent.challenges)
        database.badgeDao().insertAll(SeedContent.badges)

        if (database.profileDao().get() == null) {
            database.profileDao().insert(
                BiologistProfileEntity(
                    id = 1L,
                    alias = "Joven Biólogo",
                    avatarKey = SeedContent.avatarKeys.first(),
                    totalXp = 0,
                    onboardingCompleted = false,
                    soundEnabled = true,
                    hapticsEnabled = true,
                    createdAtEpochMillis = System.currentTimeMillis()
                )
            )
        }
    }

    companion object {
        fun newLocalId(): String = UUID.randomUUID().toString()
    }
}
