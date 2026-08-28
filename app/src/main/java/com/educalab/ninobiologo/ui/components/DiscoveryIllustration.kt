package com.educalab.ninobiologo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.DiscoveryCategory

/**
 * Ilustración de un descubrimiento concreto. Delega en [drawSpecies], de modo que cada especie
 * tiene su propia forma real (un paramecio no se parece a una ameba, ni un búho a un zorro) y esa
 * misma imagen aparece en el museo, en el ambiente, en el analizador y en el microscopio.
 */
@Composable
fun DiscoveryIllustration(
    category: DiscoveryCategory,
    iconKey: String,
    modifier: Modifier = Modifier,
    sizeDp: Int = 64
) {
    Box(modifier = modifier.size(sizeDp.dp)) {
        Canvas(modifier = Modifier.size(sizeDp.dp)) {
            drawSpecies(iconKey, category, Offset(size.width / 2f, size.height / 2f), size.width * 0.3f)
        }
    }
}
