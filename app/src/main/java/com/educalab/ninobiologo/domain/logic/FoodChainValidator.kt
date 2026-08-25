package com.educalab.ninobiologo.domain.logic

import com.educalab.ninobiologo.domain.model.TrophicRole

/**
 * Valida una cadena alimentaria construida por el niño arrastrando organismos en orden
 * (ZONA 5 / ecosistemas, mecánica "ordenar"). Una cadena válida:
 *  - empieza en PRODUCTOR;
 *  - cada eslabón siguiente sube de nivel trófico (PRODUCTOR < HERBIVORO < CARNIVORO) o es un
 *    DESCOMPONEDOR que solo puede cerrar la cadena;
 *  - no repite el mismo organismo dos veces (duplicados detectados como caso límite).
 */
object FoodChainValidator {

    data class LinkResult(val index: Int, val organismId: String, val valid: Boolean, val reason: String)

    data class ChainResult(
        val isFullyValid: Boolean,
        val links: List<LinkResult>,
        val summary: String
    )

    private fun levelOf(role: TrophicRole): Int = when (role) {
        TrophicRole.PRODUCTOR -> 0
        TrophicRole.HERBIVORO -> 1
        TrophicRole.CARNIVORO -> 2
        TrophicRole.DESCOMPONEDOR -> 3
    }

    fun validate(chain: List<Pair<String, TrophicRole>>): ChainResult {
        if (chain.isEmpty()) {
            return ChainResult(false, emptyList(), "La cadena está vacía. Arrastra al menos un productor para comenzar.")
        }

        val seenIds = HashSet<String>()
        val links = mutableListOf<LinkResult>()
        var previousLevel = -1
        var valid = true

        chain.forEachIndexed { index, (organismId, role) ->
            val level = levelOf(role)
            val isDuplicate = !seenIds.add(organismId)
            val reasons = mutableListOf<String>()

            if (isDuplicate) {
                reasons += "este organismo ya está en la cadena"
            }
            if (index == 0 && role != TrophicRole.PRODUCTOR) {
                reasons += "toda cadena debe empezar con un productor (planta o alga)"
            }
            if (index > 0 && role == TrophicRole.DESCOMPONEDOR && index != chain.lastIndex) {
                reasons += "el descomponedor debe ir al final de la cadena"
            }
            if (index > 0 && role != TrophicRole.DESCOMPONEDOR && level <= previousLevel && !(previousLevel == 0 && level == 0)) {
                if (level < previousLevel) {
                    reasons += "este organismo se alimenta de un nivel superior: revisa el orden"
                }
            }

            val linkValid = reasons.isEmpty()
            if (!linkValid) valid = false
            links += LinkResult(index, organismId, linkValid, if (linkValid) "correcto" else reasons.joinToString("; "))
            if (role != TrophicRole.DESCOMPONEDOR) previousLevel = level
        }

        val summary = if (valid) {
            "¡Cadena alimentaria completa y correcta! La energía fluye del sol a los productores y de ahí a toda la cadena."
        } else {
            "Hay ${links.count { !it.valid }} eslabón(es) para revisar."
        }

        return ChainResult(valid, links, summary)
    }
}
