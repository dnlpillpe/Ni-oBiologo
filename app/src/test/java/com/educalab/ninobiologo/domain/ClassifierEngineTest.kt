package com.educalab.ninobiologo.domain

import com.educalab.ninobiologo.domain.logic.ClassifierEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ClassifierEngineTest {

    private val discoveries = listOf(TestFixtures.rana, TestFixtures.helecho, TestFixtures.ballena)

    @Test
    fun `clasificacion correcta por categoria`() {
        val attempts = listOf(ClassifierEngine.Attempt("disc_rana", "ANIMAL"))
        val session = ClassifierEngine.evaluate(discoveries, attempts, ClassifierEngine.ClassifierAxis.CATEGORIA)
        assertEquals(1, session.correctCount)
        assertTrue(session.results[0].correct)
    }

    @Test
    fun `clasificacion incorrecta explica la respuesta correcta`() {
        val attempts = listOf(ClassifierEngine.Attempt("disc_helecho", "ANIMAL"))
        val session = ClassifierEngine.evaluate(discoveries, attempts, ClassifierEngine.ClassifierAxis.CATEGORIA)
        assertFalse(session.results[0].correct)
        assertEquals("PLANTA", session.results[0].expectedValue)
        assertTrue(session.results[0].explanation.isNotBlank())
    }

    @Test
    fun `descubrimiento inexistente no rompe la sesion (caso limite)`() {
        val attempts = listOf(ClassifierEngine.Attempt("disc_no_existe", "ANIMAL"))
        val session = ClassifierEngine.evaluate(discoveries, attempts, ClassifierEngine.ClassifierAxis.CATEGORIA)
        assertEquals(0, session.correctCount)
        assertFalse(session.results[0].correct)
    }

    @Test
    fun `lista de intentos vacia produce accuracy cero sin division por cero (caso limite)`() {
        val session = ClassifierEngine.evaluate(discoveries, emptyList(), ClassifierEngine.ClassifierAxis.HABITAT)
        assertEquals(0f, session.accuracy, 0.001f)
    }

    @Test
    fun `accuracy se calcula correctamente sobre varios intentos`() {
        val attempts = listOf(
            ClassifierEngine.Attempt("disc_rana", "ANIMAL"),
            ClassifierEngine.Attempt("disc_helecho", "ANIMAL"),
            ClassifierEngine.Attempt("disc_ballena", "ANIMAL")
        )
        val session = ClassifierEngine.evaluate(discoveries, attempts, ClassifierEngine.ClassifierAxis.CATEGORIA)
        assertEquals(2, session.correctCount)
        assertEquals(3, session.totalCount)
        assertEquals(2f / 3f, session.accuracy, 0.01f)
    }

    @Test
    fun `clasificacion por rareza usa la rareza real del descubrimiento`() {
        val attempts = listOf(ClassifierEngine.Attempt("disc_ballena", "LEGENDARIO"))
        val session = ClassifierEngine.evaluate(discoveries, attempts, ClassifierEngine.ClassifierAxis.RAREZA)
        assertTrue(session.results[0].correct)
    }

    @Test
    fun `rareza incorrecta explica cual era la correcta`() {
        val attempts = listOf(ClassifierEngine.Attempt("disc_helecho", "LEGENDARIO"))
        val session = ClassifierEngine.evaluate(discoveries, attempts, ClassifierEngine.ClassifierAxis.RAREZA)
        assertFalse(session.results[0].correct)
        assertEquals("COMUN", session.results[0].expectedValue)
    }

    @Test
    fun `clasificacion por dieta es sensible al descubrimiento`() {
        val attempts = listOf(ClassifierEngine.Attempt("disc_ballena", "Kril"))
        val session = ClassifierEngine.evaluate(discoveries, attempts, ClassifierEngine.ClassifierAxis.DIETA)
        assertTrue(session.results[0].correct)
    }
}
