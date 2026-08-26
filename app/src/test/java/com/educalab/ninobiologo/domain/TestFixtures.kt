package com.educalab.ninobiologo.domain

import com.educalab.ninobiologo.domain.model.DiscoveryCategory
import com.educalab.ninobiologo.domain.model.DiscoveryRarity
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery

/** Descubrimientos de ejemplo reutilizados por varios tests de dominio. */
object TestFixtures {
    val rana = MicroscopeDiscovery(
        id = "disc_rana", sampleId = "sample_bosque_01", environmentId = "bosque_de_vida",
        name = "Rana arbórea", scientificName = "Hylidae",
        category = DiscoveryCategory.ANIMAL, habitat = "Bosque húmedo", diet = "Insectos",
        characteristics = listOf("piel húmeda", "salta"), curiosity = "Respira por la piel.",
        rarity = DiscoveryRarity.POCO_COMUN, iconKey = "rana"
    )

    val helecho = MicroscopeDiscovery(
        id = "disc_helecho", sampleId = "sample_bosque_01", environmentId = "bosque_de_vida",
        name = "Helecho gigante", scientificName = "Polypodiopsida",
        category = DiscoveryCategory.PLANTA, habitat = "Bosque húmedo", diet = "Luz solar (fotosíntesis)",
        characteristics = listOf("hojas grandes", "esporas"), curiosity = "Existen desde antes que los dinosaurios.",
        rarity = DiscoveryRarity.COMUN, iconKey = "helecho"
    )

    val ballena = MicroscopeDiscovery(
        id = "disc_ballena", sampleId = "sample_oceano_04", environmentId = "oceano_profundo",
        name = "Ballena azul", scientificName = "Balaenoptera musculus",
        category = DiscoveryCategory.ANIMAL, habitat = "Océano abierto", diet = "Kril",
        characteristics = listOf("el animal más grande del planeta"), curiosity = "Su corazón pesa como un auto pequeño.",
        rarity = DiscoveryRarity.LEGENDARIO, iconKey = "ballena"
    )
}
