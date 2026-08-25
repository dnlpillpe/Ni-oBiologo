package com.educalab.ninobiologo.domain

import com.educalab.ninobiologo.domain.logic.CollectionEngine
import com.educalab.ninobiologo.domain.model.Badge
import com.educalab.ninobiologo.domain.model.BadgeCriteriaType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CollectionEngineTest {

    private val organisms = listOf(TestFixtures.rana, TestFixtures.helecho)

    @Test
    fun `porcentaje de completitud con lista vacia es cero (caso limite)`() {
        assertEquals(0, CollectionEngine.biomeCompletionPercent(emptyList(), emptySet()))
    }

    @Test
    fun `porcentaje de completitud se calcula desde descubrimientos reales`() {
        val percent = CollectionEngine.biomeCompletionPercent(organisms, setOf("org_rana"))
        assertEquals(50, percent)
    }

    @Test
    fun `insignia por descubrimientos totales se desbloquea al alcanzar el umbral`() {
        val badge = Badge("b1", "Coleccionista", "desc", "icon", BadgeCriteriaType.DESCUBRIMIENTOS_TOTALES, 5)
        val stats = CollectionEngine.ProgressStats(
            discoveriesCount = 5, expeditionsCompleted = 0, stableEcosystemsCount = 0,
            challengesPassed = 0, biomeCompletionPercents = emptyMap(), legendaryDiscovered = false
        )
        val unlocked = CollectionEngine.newlyUnlockedBadges(listOf(badge), emptySet(), stats)
        assertEquals(1, unlocked.size)
    }

    @Test
    fun `insignia ya desbloqueada no se repite`() {
        val badge = Badge("b1", "Coleccionista", "desc", "icon", BadgeCriteriaType.DESCUBRIMIENTOS_TOTALES, 5)
        val stats = CollectionEngine.ProgressStats(5, 0, 0, 0, emptyMap(), false)
        val unlocked = CollectionEngine.newlyUnlockedBadges(listOf(badge), setOf("b1"), stats)
        assertTrue(unlocked.isEmpty())
    }

    @Test
    fun `insignia de zona completa depende del porcentaje de ese bioma`() {
        val badge = Badge("b2", "Maestro del Bosque", "desc", "icon", BadgeCriteriaType.ZONA_COMPLETA, 100, biomeId = "bosque_de_vida")
        val statsIncompleto = CollectionEngine.ProgressStats(0, 0, 0, 0, mapOf("bosque_de_vida" to 80), false)
        val statsCompleto = CollectionEngine.ProgressStats(0, 0, 0, 0, mapOf("bosque_de_vida" to 100), false)
        assertTrue(CollectionEngine.newlyUnlockedBadges(listOf(badge), emptySet(), statsIncompleto).isEmpty())
        assertEquals(1, CollectionEngine.newlyUnlockedBadges(listOf(badge), emptySet(), statsCompleto).size)
    }

    @Test
    fun `insignia legendaria requiere haber descubierto un organismo legendario`() {
        val badge = Badge("b3", "Cazador Legendario", "desc", "icon", BadgeCriteriaType.RAREZA_LEGENDARIA, 1)
        val statsSinLegendario = CollectionEngine.ProgressStats(1, 0, 0, 0, emptyMap(), false)
        val statsConLegendario = CollectionEngine.ProgressStats(1, 0, 0, 0, emptyMap(), true)
        assertTrue(CollectionEngine.newlyUnlockedBadges(listOf(badge), emptySet(), statsSinLegendario).isEmpty())
        assertEquals(1, CollectionEngine.newlyUnlockedBadges(listOf(badge), emptySet(), statsConLegendario).size)
    }
}
