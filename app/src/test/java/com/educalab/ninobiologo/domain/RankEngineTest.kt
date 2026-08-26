package com.educalab.ninobiologo.domain

import com.educalab.ninobiologo.domain.logic.RankEngine
import com.educalab.ninobiologo.domain.model.BiologistRank
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RankEngineTest {

    @Test
    fun `xp cero produce rango inicial Explorador de Vida`() {
        assertEquals(BiologistRank.EXPLORADOR_DE_VIDA, RankEngine.rankFor(0))
    }

    @Test
    fun `xp negativo se trata como cero (caso limite)`() {
        assertEquals(BiologistRank.EXPLORADOR_DE_VIDA, RankEngine.rankFor(-500))
    }

    @Test
    fun `xp justo en el umbral asciende de rango`() {
        assertEquals(BiologistRank.BIOLOGO_JUNIOR, RankEngine.rankFor(250))
        assertEquals(BiologistRank.EXPLORADOR_DE_VIDA, RankEngine.rankFor(249))
    }

    @Test
    fun `xp muy alto llega al rango maximo Guardian del Planeta`() {
        assertEquals(BiologistRank.GUARDIAN_DEL_PLANETA, RankEngine.rankFor(999999))
    }

    @Test
    fun `next devuelve null en el rango maximo`() {
        assertEquals(null, BiologistRank.next(BiologistRank.GUARDIAN_DEL_PLANETA))
    }

    @Test
    fun `progressToNextRank es 1 en el rango maximo`() {
        assertEquals(1f, RankEngine.progressToNextRank(5000), 0.001f)
    }

    @Test
    fun `progressToNextRank a mitad de camino es aproximadamente 0_5`() {
        // Explorador de Vida: 0..250. Punto medio = 125
        val progress = RankEngine.progressToNextRank(125)
        assertEquals(0.5f, progress, 0.01f)
    }

    @Test
    fun `xpToNextRank es 0 en el rango maximo`() {
        assertEquals(0, RankEngine.xpToNextRank(5000))
    }

    @Test
    fun `computeXp suma correctamente todos los componentes`() {
        val breakdown = RankEngine.computeXp(
            discoveryXpSum = 100,
            successfulExperimentsCount = 2,
            analysisXpSum = 40,
            collectiblesUnlockedCount = 3
        )
        // 100 + 2*25 + 40 + 3*20 = 100+50+40+60 = 250
        assertEquals(250, breakdown.total)
    }

    @Test
    fun `computeXp con valores negativos nunca produce total negativo (caso limite)`() {
        val breakdown = RankEngine.computeXp(-50, -3, -40, -2)
        assertTrue(breakdown.total >= 0)
        assertEquals(0, breakdown.total)
    }
}
