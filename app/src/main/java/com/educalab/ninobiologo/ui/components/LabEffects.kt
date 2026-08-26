package com.educalab.ninobiologo.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberInfiniteTransition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.sin
import kotlin.random.Random

/**
 * Fondo ambiental de partículas flotantes (micro-burbujas/motas), usado en el Laboratorio Vivo,
 * el Microscopio y el Viaje al Interior de la Célula para que la exploración se sienta viva sin
 * depender de imágenes externas.
 */
@Composable
fun AmbientParticles(modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.primary, count: Int = 14) {
    val infinite = rememberInfiniteTransition(label = "ambient_particles")
    val t by infinite.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Restart),
        label = "t"
    )
    val particles = remember(count) {
        List(count) {
            val rnd = Random(it * 97)
            Triple(rnd.nextFloat(), rnd.nextFloat(), 0.4f + rnd.nextFloat() * 0.9f)
        }
    }
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        particles.forEachIndexed { index, (px, py, radiusFactor) ->
            val phase = (t + index * 0.07f) % 1f
            val y = ((py + phase) % 1f) * h
            val x = px * w + (sin((phase * 6.283f).toDouble()) * w * 0.03f).toFloat()
            val alpha = (0.15f + 0.25f * sin((phase * 3.1416f).toDouble()).toFloat()).coerceIn(0.05f, 0.4f)
            drawCircle(color.copy(alpha = alpha), radius = radiusFactor * (w * 0.012f), center = Offset(x, y))
        }
    }
}

/** Control +/- reutilizable para variables de Experimentos y piezas del Constructor Biológico. */
@Composable
fun StepperControl(
    label: String,
    value: Int,
    unit: String,
    range: IntRange,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onChange((value - 1).coerceIn(range)) }) {
                Icon(Icons.Filled.Remove, contentDescription = "Disminuir $label")
            }
            Text(
                "$value $unit",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.size(width = 96.dp, height = 32.dp)
            )
            IconButton(onClick = { onChange((value + 1).coerceIn(range)) }) {
                Icon(Icons.Filled.Add, contentDescription = "Aumentar $label")
            }
        }
    }
}
