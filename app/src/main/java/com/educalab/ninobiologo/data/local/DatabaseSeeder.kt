package com.educalab.ninobiologo.data.local

import com.educalab.ninobiologo.data.local.entity.ExplorerProfileEntity
import com.educalab.ninobiologo.data.local.seed.SeedContent
import java.util.UUID

/**
 * Puebla la base de datos en la primera apertura real de la app. La cantidad de contenido está
 * generada por tools/generate_seed_data.py siguiendo las cantidades exigidas por el prompt
 * "Vida en Miniatura" (20 muestras, 30 descubrimientos, 10 experimentos, 5 ambientes, 15
 * coleccionables).
 */
class DatabaseSeeder(private val database: AppDatabase) {

    suspend fun seedIfEmpty() {
        if (database.environmentDao().count() > 0) return // ya sembrada: evita duplicar contenido

        database.environmentDao().insertAll(SeedContent.environments)
        database.sampleDao().insertAll(SeedContent.samples)
        database.microscopeDiscoveryDao().insertAll(SeedContent.discoveries)
        database.cellModelDao().insertAll(SeedContent.cellModels)
        database.cellModelDao().insertStructures(SeedContent.cellStructures)
        database.bodySystemDao().insertAll(SeedContent.bodySystems)
        database.bodySystemDao().insertOrgans(SeedContent.bodyOrgans)
        database.experimentDao().insertAll(SeedContent.experiments)
        database.creaturePartOptionDao().insertAll(SeedContent.creaturePartOptions)
        database.challengeDao().insertAll(SeedContent.challenges)
        database.labCollectibleDao().insertAll(SeedContent.labCollectibles)
        database.laboratoryUpgradeDao().insertAll(SeedContent.laboratoryUpgrades)

        if (database.explorerProfileDao().get() == null) {
            database.explorerProfileDao().insert(
                ExplorerProfileEntity(
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
