package com.educalab.ninobiologo.domain

import com.educalab.ninobiologo.domain.logic.MicroscopeEngine
import com.educalab.ninobiologo.domain.model.CellModel
import com.educalab.ninobiologo.domain.model.CellStructure
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MicroscopeEngineTest {

    private val cell = CellModel(
        id = "cell_animal", name = "Célula animal", cellType = "Animal", description = "desc",
        structures = listOf(
            CellStructure("nucleo", "Núcleo", "Controla la célula", 0.5f, 0.5f),
            CellStructure("membrana", "Membrana", "Protege la célula", 0.1f, 0.1f),
            CellStructure("citoplasma", "Citoplasma", "Sustancia interna", 0.3f, 0.7f)
        )
    )

    @Test
    fun `estado inicial no tiene estructuras reveladas`() {
        val state = MicroscopeEngine.newState(cell)
        assertEquals(0, MicroscopeEngine.completionPercent(cell, state))
        assertFalse(MicroscopeEngine.isComplete(cell, state))
    }

    @Test
    fun `revelar una estructura aumenta el porcentaje`() {
        val state = MicroscopeEngine.newState(cell).reveal("nucleo")
        assertEquals(33, MicroscopeEngine.completionPercent(cell, state))
    }

    @Test
    fun `revelar todas las estructuras marca la celula como completa`() {
        var state = MicroscopeEngine.newState(cell)
        cell.structures.forEach { state = state.reveal(it.id) }
        assertTrue(MicroscopeEngine.isComplete(cell, state))
        assertEquals(100, MicroscopeEngine.completionPercent(cell, state))
        assertNull(MicroscopeEngine.nextUndiscoveredHint(cell, state))
    }

    @Test
    fun `revelar dos veces la misma estructura no duplica el progreso (doble toque)`() {
        val state = MicroscopeEngine.newState(cell).reveal("nucleo").reveal("nucleo")
        assertEquals(33, MicroscopeEngine.completionPercent(cell, state))
    }

    @Test
    fun `celula sin estructuras no rompe el calculo (caso limite)`() {
        val emptyCell = cell.copy(structures = emptyList())
        val state = MicroscopeEngine.newState(emptyCell)
        assertEquals(0, MicroscopeEngine.completionPercent(emptyCell, state))
        assertFalse(MicroscopeEngine.isComplete(emptyCell, state))
    }

    @Test
    fun `nextUndiscoveredHint sugiere la siguiente estructura pendiente`() {
        val state = MicroscopeEngine.newState(cell).reveal("nucleo")
        assertEquals("Membrana", MicroscopeEngine.nextUndiscoveredHint(cell, state))
    }
}
