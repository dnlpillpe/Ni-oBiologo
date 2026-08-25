package com.educalab.ninobiologo.domain

import com.educalab.ninobiologo.domain.model.Organism
import com.educalab.ninobiologo.domain.model.OrganismCategory
import com.educalab.ninobiologo.domain.model.OrganismRarity
import com.educalab.ninobiologo.domain.model.TrophicRole

/** Organismos de ejemplo reutilizados por varios tests de dominio. */
object TestFixtures {
    val rana = Organism(
        id = "org_rana", biomeId = "bosque_de_vida", name = "Rana arbórea", scientificName = "Hylidae",
        category = OrganismCategory.ANIMAL, habitat = "Bosque húmedo", diet = "Insectos",
        trophicRole = TrophicRole.CARNIVORO,
        characteristics = listOf("piel húmeda", "salta"), funFact = "Respira por la piel.",
        rarity = OrganismRarity.POCO_COMUN, iconKey = "rana"
    )

    val helecho = Organism(
        id = "org_helecho", biomeId = "bosque_de_vida", name = "Helecho gigante", scientificName = "Polypodiopsida",
        category = OrganismCategory.PLANTA, habitat = "Bosque húmedo", diet = "Luz solar (fotosíntesis)",
        trophicRole = TrophicRole.PRODUCTOR,
        characteristics = listOf("hojas grandes", "esporas"), funFact = "Existen desde antes que los dinosaurios.",
        rarity = OrganismRarity.COMUN, iconKey = "helecho"
    )

    val ballena = Organism(
        id = "org_ballena", biomeId = "oceano_profundo", name = "Ballena azul", scientificName = "Balaenoptera musculus",
        category = OrganismCategory.ANIMAL, habitat = "Océano abierto", diet = "Kril",
        trophicRole = TrophicRole.HERBIVORO,
        characteristics = listOf("el animal más grande del planeta"), funFact = "Su corazón pesa como un auto pequeño.",
        rarity = OrganismRarity.LEGENDARIO, iconKey = "ballena"
    )
}
