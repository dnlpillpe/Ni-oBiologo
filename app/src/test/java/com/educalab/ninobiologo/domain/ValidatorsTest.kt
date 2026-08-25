package com.educalab.ninobiologo.domain

import com.educalab.ninobiologo.domain.logic.Validators
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidatorsTest {

    @Test
    fun `alias vacio no es valido`() {
        assertFalse(Validators.isAliasValid("   "))
    }

    @Test
    fun `alias con espacios se recorta correctamente`() {
        assertEquals("Bio Ana", Validators.sanitizeAlias("  Bio   Ana  "))
    }

    @Test
    fun `alias demasiado largo se recorta al maximo permitido (texto demasiado largo)`() {
        val longAlias = "A".repeat(100)
        val sanitized = Validators.sanitizeAlias(longAlias)
        assertTrue(sanitized.length <= 18)
    }

    @Test
    fun `nota de diario dentro del limite es valida`() {
        assertTrue(Validators.isJournalNoteValid("Encontré una rana verde hoy."))
    }

    @Test
    fun `nota de diario demasiado larga no es valida (texto demasiado largo)`() {
        assertFalse(Validators.isJournalNoteValid("x".repeat(1000)))
    }

    @Test
    fun `hasDuplicateIds detecta duplicados`() {
        assertTrue(Validators.hasDuplicateIds(listOf("a", "b", "a")))
        assertFalse(Validators.hasDuplicateIds(listOf("a", "b", "c")))
    }

    @Test
    fun `lista vacia no se considera duplicada (caso limite)`() {
        assertFalse(Validators.hasDuplicateIds(emptyList()))
    }

    @Test
    fun `safeRatio evita division entre cero`() {
        assertEquals(0f, Validators.safeRatio(10, 0), 0.001f)
        assertEquals(0.5f, Validators.safeRatio(5, 10), 0.001f)
    }

    @Test
    fun `clampPercent recorta valores fuera de rango`() {
        assertEquals(0, Validators.clampPercent(-20))
        assertEquals(100, Validators.clampPercent(250))
        assertEquals(42, Validators.clampPercent(42))
    }
}
