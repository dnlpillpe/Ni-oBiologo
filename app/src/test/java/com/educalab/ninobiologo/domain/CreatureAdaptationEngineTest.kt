package com.educalab.ninobiologo.domain

import com.educalab.ninobiologo.domain.logic.CreatureAdaptationEngine
import com.educalab.ninobiologo.domain.model.CreaturePartCategory
import com.educalab.ninobiologo.domain.model.CreaturePartOption
import org.junit.Assert.assertEquals
import org.junit.Test

class CreatureAdaptationEngineTest {

    private val forma = CreaturePartOption("forma_redonda", CreaturePartCategory.FORMA, "Redonda", "desc", "micromundo")
    private val movimiento = CreaturePartOption("mov_cilios", CreaturePartCategory.MOVIMIENTO, "Cilios", "desc", "micromundo")
    private val alimentacion = CreaturePartOption("alim_fotosintesis", CreaturePartCategory.ALIMENTACION, "Fotosíntesis", "desc", "bosque_de_vida")

    @Test
    fun `todas las piezas coinciden con el ambiente produce el puntaje mas alto`() {
        val result = CreatureAdaptationEngine.evaluate(listOf(forma, movimiento), "micromundo")
        assertEquals(100, result.fitScore)
    }

    @Test
    fun `ninguna pieza coincide produce el puntaje mas bajo`() {
        val result = CreatureAdaptationEngine.evaluate(listOf(forma, movimiento), "oceano_profundo")
        assertEquals(0, result.fitScore)
    }

    @Test
    fun `mezcla de piezas produce un puntaje intermedio`() {
        val result = CreatureAdaptationEngine.evaluate(listOf(forma, movimiento, alimentacion), "micromundo")
        assertEquals(66, result.fitScore)
    }

    @Test
    fun `sin selecciones no rompe el calculo (caso limite)`() {
        val result = CreatureAdaptationEngine.evaluate(emptyList(), "micromundo")
        assertEquals(0, result.fitScore)
    }
}
