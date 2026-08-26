package com.educalab.ninobiologo.domain

import com.educalab.ninobiologo.domain.logic.CollectionEngine
import com.educalab.ninobiologo.domain.model.LabCollectible
import com.educalab.ninobiologo.domain.model.UnlockCriteriaType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CollectionEngineTest {

    private val discoveries = listOf(TestFixtures.rana, TestFixtures.helecho)

    private fun criteriaOf(c: LabCollectible) =
        CollectionEngine.UnlockableCriteria(c.criteriaType, c.criteriaValue, c.environmentId)

    @Test
    fun `porcentaje de completitud con lista vacia es cero (caso limite)`() {
        assertEquals(0, CollectionEngine.environmentCompletionPercent(emptyList(), emptySet()))
    }

    @Test
    fun `porcentaje de completitud se calcula desde descubrimientos reales`() {
        val percent = CollectionEngine.environmentCompletionPercent(discoveries, setOf("disc_rana"))
        assertEquals(50, percent)
    }

    @Test
    fun `coleccionable por descubrimientos totales se desbloquea al alcanzar el umbral`() {
        val collectible = LabCollectible("c1", "Coleccionista", "desc", "icon", UnlockCriteriaType.DESCUBRIMIENTOS_TOTALES, 5)
        val stats = CollectionEngine.ProgressStats(
            discoveriesCount = 5, experimentsRun = 0, creaturesCreated = 0,
            analysisPassed = 0, environmentCompletionPercents = emptyMap(), legendaryDiscovered = false
        )
        val unlocked = CollectionEngine.newlyUnlocked(listOf(collectible), { it.id }, ::criteriaOf, emptySet(), stats)
        assertEquals(1, unlocked.size)
    }

    @Test
    fun `coleccionable ya desbloqueado no se repite`() {
        val collectible = LabCollectible("c1", "Coleccionista", "desc", "icon", UnlockCriteriaType.DESCUBRIMIENTOS_TOTALES, 5)
        val stats = CollectionEngine.ProgressStats(5, 0, 0, 0, emptyMap(), false)
        val unlocked = CollectionEngine.newlyUnlocked(listOf(collectible), { it.id }, ::criteriaOf, setOf("c1"), stats)
        assertTrue(unlocked.isEmpty())
    }

    @Test
    fun `coleccionable de ambiente completo depende del porcentaje de ese ambiente`() {
        val collectible = LabCollectible("c2", "Guardián del Bosque", "desc", "icon", UnlockCriteriaType.AMBIENTE_COMPLETO, 100, environmentId = "bosque_de_vida")
        val statsIncompleto = CollectionEngine.ProgressStats(0, 0, 0, 0, mapOf("bosque_de_vida" to 80), false)
        val statsCompleto = CollectionEngine.ProgressStats(0, 0, 0, 0, mapOf("bosque_de_vida" to 100), false)
        assertTrue(CollectionEngine.newlyUnlocked(listOf(collectible), { it.id }, ::criteriaOf, emptySet(), statsIncompleto).isEmpty())
        assertEquals(1, CollectionEngine.newlyUnlocked(listOf(collectible), { it.id }, ::criteriaOf, emptySet(), statsCompleto).size)
    }

    @Test
    fun `coleccionable legendario requiere haber descubierto una rareza legendaria`() {
        val collectible = LabCollectible("c3", "Cazador Legendario", "desc", "icon", UnlockCriteriaType.RAREZA_LEGENDARIA, 1)
        val statsSinLegendario = CollectionEngine.ProgressStats(1, 0, 0, 0, emptyMap(), false)
        val statsConLegendario = CollectionEngine.ProgressStats(1, 0, 0, 0, emptyMap(), true)
        assertTrue(CollectionEngine.newlyUnlocked(listOf(collectible), { it.id }, ::criteriaOf, emptySet(), statsSinLegendario).isEmpty())
        assertEquals(1, CollectionEngine.newlyUnlocked(listOf(collectible), { it.id }, ::criteriaOf, emptySet(), statsConLegendario).size)
    }
}
