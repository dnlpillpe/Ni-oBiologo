package com.educalab.ninobiologo.domain

import com.educalab.ninobiologo.domain.logic.FoodChainValidator
import com.educalab.ninobiologo.domain.model.TrophicRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FoodChainValidatorTest {

    @Test
    fun `cadena vacia no es valida (caso limite)`() {
        val result = FoodChainValidator.validate(emptyList())
        assertFalse(result.isFullyValid)
    }

    @Test
    fun `cadena correcta productor herbivoro carnivoro descomponedor es valida`() {
        val chain = listOf(
            "pasto" to TrophicRole.PRODUCTOR,
            "conejo" to TrophicRole.HERBIVORO,
            "zorro" to TrophicRole.CARNIVORO,
            "hongo" to TrophicRole.DESCOMPONEDOR
        )
        val result = FoodChainValidator.validate(chain)
        assertTrue(result.isFullyValid)
        assertTrue(result.links.all { it.valid })
    }

    @Test
    fun `cadena que no empieza en productor es invalida`() {
        val chain = listOf(
            "zorro" to TrophicRole.CARNIVORO,
            "pasto" to TrophicRole.PRODUCTOR
        )
        val result = FoodChainValidator.validate(chain)
        assertFalse(result.isFullyValid)
        assertFalse(result.links[0].valid)
    }

    @Test
    fun `organismo repetido en la cadena es detectado como caso limite`() {
        val chain = listOf(
            "pasto" to TrophicRole.PRODUCTOR,
            "conejo" to TrophicRole.HERBIVORO,
            "conejo" to TrophicRole.HERBIVORO
        )
        val result = FoodChainValidator.validate(chain)
        assertFalse(result.isFullyValid)
        assertFalse(result.links[2].valid)
    }

    @Test
    fun `descomponedor en medio de la cadena es invalido`() {
        val chain = listOf(
            "pasto" to TrophicRole.PRODUCTOR,
            "hongo" to TrophicRole.DESCOMPONEDOR,
            "conejo" to TrophicRole.HERBIVORO
        )
        val result = FoodChainValidator.validate(chain)
        assertFalse(result.isFullyValid)
    }

    @Test
    fun `orden invertido carnivoro antes que herbivoro es invalido`() {
        val chain = listOf(
            "pasto" to TrophicRole.PRODUCTOR,
            "zorro" to TrophicRole.CARNIVORO,
            "conejo" to TrophicRole.HERBIVORO
        )
        val result = FoodChainValidator.validate(chain)
        assertFalse(result.isFullyValid)
        assertFalse(result.links[2].valid)
    }

    @Test
    fun `cadena de un solo productor es valida (caso limite minimo)`() {
        val chain = listOf("alga" to TrophicRole.PRODUCTOR)
        val result = FoodChainValidator.validate(chain)
        assertTrue(result.isFullyValid)
    }
}
