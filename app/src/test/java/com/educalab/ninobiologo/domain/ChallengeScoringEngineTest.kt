package com.educalab.ninobiologo.domain

import com.educalab.ninobiologo.domain.logic.ChallengeScoringEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ChallengeScoringEngineTest {

    @Test
    fun `sin intentos no otorga estrellas ni xp (caso limite division por cero)`() {
        val result = ChallengeScoringEngine.score(0, 0, 50)
        assertEquals(0, result.stars)
        assertEquals(0, result.xpAwarded)
    }

    @Test
    fun `acierto perfecto otorga 3 estrellas`() {
        val result = ChallengeScoringEngine.score(10, 10, 50)
        assertEquals(3, result.stars)
        assertEquals(50, result.xpAwarded)
    }

    @Test
    fun `acierto cero pero con intentos otorga xp minimo de participacion sin castigo`() {
        val result = ChallengeScoringEngine.score(0, 5, 50)
        assertEquals(0, result.stars)
        assertTrue("xp de participacion debe ser mayor a 0", result.xpAwarded > 0)
    }

    @Test
    fun `acierto correcto mayor al total se recorta (caso limite entrada invalida)`() {
        val result = ChallengeScoringEngine.score(99, 10, 50)
        assertEquals(3, result.stars)
    }

    @Test
    fun `acierto intermedio otorga 2 estrellas`() {
        val result = ChallengeScoringEngine.score(7, 10, 50)
        assertEquals(2, result.stars)
    }

    @Test
    fun `xp nunca es negativo`() {
        val result = ChallengeScoringEngine.score(1, 10, 50)
        assertTrue(result.xpAwarded >= 0)
    }
}
