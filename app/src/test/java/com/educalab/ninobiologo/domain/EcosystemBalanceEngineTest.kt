package com.educalab.ninobiologo.domain

import com.educalab.ninobiologo.domain.logic.EcosystemBalanceEngine
import com.educalab.ninobiologo.domain.model.EcosystemStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EcosystemBalanceEngineTest {

    @Test
    fun `ecosistema totalmente vacio esta colapsado con puntaje 0 (caso limite)`() {
        val result = EcosystemBalanceEngine.evaluate(0, 0, 0, 0)
        assertEquals(0, result.score)
        assertEquals(EcosystemStatus.COLAPSADO, result.status)
        assertTrue(result.feedback.isNotEmpty())
    }

    @Test
    fun `ecosistema equilibrado y completo es estable o floreciente`() {
        val result = EcosystemBalanceEngine.evaluate(producers = 6, herbivores = 4, carnivores = 2, decomposers = 3)
        assertTrue("score=${result.score}", result.score >= 60)
        assertTrue(result.status == EcosystemStatus.ESTABLE || result.status == EcosystemStatus.FLORECIENTE)
    }

    @Test
    fun `demasiados carnivoros sin herbivoros suficientes genera advertencia`() {
        val result = EcosystemBalanceEngine.evaluate(producers = 5, herbivores = 1, carnivores = 8, decomposers = 1)
        assertTrue(result.feedback.any { it.contains("carnívoro", ignoreCase = true) || it.contains("carnivoro", ignoreCase = true) })
    }

    @Test
    fun `carnivoros sin ningun herbivoro es un caso critico`() {
        val result = EcosystemBalanceEngine.evaluate(producers = 4, herbivores = 0, carnivores = 3, decomposers = 1)
        assertTrue(result.score < 60)
        assertTrue(result.feedback.any { it.contains("herbívoro", ignoreCase = true) || it.contains("herbivoro", ignoreCase = true) })
    }

    @Test
    fun `sin descomponedores se advierte acumulacion de desechos`() {
        val result = EcosystemBalanceEngine.evaluate(producers = 4, herbivores = 2, carnivores = 1, decomposers = 0)
        assertTrue(result.feedback.any { it.contains("descomponedor", ignoreCase = true) })
    }

    @Test
    fun `numeros negativos se recortan a cero y no rompen el calculo (caso limite)`() {
        val result = EcosystemBalanceEngine.evaluate(-5, -3, -1, -2)
        assertEquals(0, result.score)
    }

    @Test
    fun `numeros extremadamente grandes no lanzan excepcion (caso limite)`() {
        val result = EcosystemBalanceEngine.evaluate(500, 500, 500, 500)
        assertTrue(result.score in 0..100)
    }

    @Test
    fun `solo productores sin consumidores no esta completo`() {
        val result = EcosystemBalanceEngine.evaluate(producers = 10, herbivores = 0, carnivores = 0, decomposers = 0)
        assertTrue(result.feedback.any { it.contains("consumidor", ignoreCase = true) })
    }

    @Test
    fun `distanceFromIdeal calcula diferencia absoluta`() {
        assertEquals(3, EcosystemBalanceEngine.distanceFromIdeal(2, 5))
        assertEquals(3, EcosystemBalanceEngine.distanceFromIdeal(5, 2))
        assertEquals(0, EcosystemBalanceEngine.distanceFromIdeal(4, 4))
    }
}
