package com.educalab.ninobiologo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.OrganismCategory
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Ilustración procedural de un organismo, dibujada con Compose Canvas (sección 4 del prompt
 * específico: prioridad 3, "ilustraciones generadas mediante Canvas" cuando no hay vectores
 * dedicados). La forma varía según la categoría biológica y un pequeño hash de [iconKey] aporta
 * variedad reconocible entre organismos de la misma categoría, evitando que todos se vean iguales.
 */
@Composable
fun OrganismIllustration(
    category: OrganismCategory,
    iconKey: String,
    modifier: Modifier = Modifier,
    sizeDp: Int = 64
) {
    val seed = stableHash(iconKey)
    val baseColor = colorForCategory(category)
    val accent = lerp(baseColor, Color.White, 0.35f)

    Box(modifier = modifier.size(sizeDp.dp)) {
        Canvas(modifier = Modifier.size(sizeDp.dp)) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f

            when (category) {
                OrganismCategory.PLANTA -> {
                    drawLine(baseColor, Offset(cx, cy + h * 0.35f), Offset(cx, cy - h * 0.1f), strokeWidth = w * 0.05f)
                    val leaves = 3 + (seed % 3)
                    for (i in 0 until leaves) {
                        val angle = (i * (360f / leaves)) + (seed % 40)
                        val rad = Math.toRadians(angle.toDouble())
                        val lx = cx + (w * 0.28f * cos(rad)).toFloat()
                        val ly = (cy - h * 0.05f) + (w * 0.28f * sin(rad)).toFloat() * 0.6f
                        drawCircle(if (i % 2 == 0) baseColor else accent, radius = w * 0.16f, center = Offset(lx, ly))
                    }
                }
                OrganismCategory.ANIMAL -> {
                    drawCircle(baseColor, radius = w * 0.3f, center = Offset(cx, cy))
                    val earOffset = w * 0.18f
                    drawCircle(accent, radius = w * 0.1f, center = Offset(cx - earOffset, cy - h * 0.24f))
                    drawCircle(accent, radius = w * 0.1f, center = Offset(cx + earOffset, cy - h * 0.24f))
                    drawCircle(Color.White, radius = w * 0.05f, center = Offset(cx - w * 0.08f, cy - h * 0.02f))
                    drawCircle(Color.White, radius = w * 0.05f, center = Offset(cx + w * 0.08f, cy - h * 0.02f))
                    drawCircle(Color.Black.copy(alpha = 0.7f), radius = w * 0.02f, center = Offset(cx - w * 0.08f, cy - h * 0.02f))
                    drawCircle(Color.Black.copy(alpha = 0.7f), radius = w * 0.02f, center = Offset(cx + w * 0.08f, cy - h * 0.02f))
                }
                OrganismCategory.MICROORGANISMO -> {
                    val bumps = 5 + (seed % 4)
                    for (i in 0 until bumps) {
                        val angle = i * (360f / bumps)
                        val rad = Math.toRadians(angle.toDouble())
                        val px = cx + (w * 0.26f * cos(rad)).toFloat()
                        val py = cy + (w * 0.26f * sin(rad)).toFloat()
                        drawCircle(accent, radius = w * 0.09f, center = Offset(px, py))
                    }
                    drawCircle(baseColor, radius = w * 0.22f, center = Offset(cx, cy))
                }
                OrganismCategory.HONGO -> {
                    drawLine(accent, Offset(cx, cy + h * 0.3f), Offset(cx, cy), strokeWidth = w * 0.08f)
                    drawArc(
                        color = baseColor, startAngle = 180f, sweepAngle = 180f, useCenter = true,
                        topLeft = Offset(cx - w * 0.32f, cy - h * 0.28f),
                        size = androidx.compose.ui.geometry.Size(w * 0.64f, h * 0.4f)
                    )
                    repeat(3) { i ->
                        drawCircle(Color.White.copy(alpha = 0.8f), radius = w * 0.03f, center = Offset(cx - w * 0.15f + i * w * 0.15f, cy - h * 0.14f))
                    }
                }
            }
        }
    }
}

private fun colorForCategory(category: OrganismCategory): Color = when (category) {
    OrganismCategory.PLANTA -> Color(0xFF3E8E41)
    OrganismCategory.ANIMAL -> Color(0xFFC57B3E)
    OrganismCategory.MICROORGANISMO -> Color(0xFF2E8B8B)
    OrganismCategory.HONGO -> Color(0xFFB5473A)
}

private fun stableHash(key: String): Int = abs(key.fold(7) { acc, c -> acc * 31 + c.code })
