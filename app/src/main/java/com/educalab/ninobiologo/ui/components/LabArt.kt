package com.educalab.ninobiologo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.DiscoveryCategory
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Ilustración de una muestra científica: un recipiente de laboratorio (placa de Petri, tubo o
 * portaobjetos) con su contenido dentro. Se dibuja con Canvas para que cada muestra tenga imagen
 * propia sin depender de assets externos (la app es 100% offline).
 */
@Composable
fun SampleIllustration(
    iconKey: String,
    tint: Color,
    modifier: Modifier = Modifier,
    sizeDp: Int = 72
) {
    val seed = stableSeed(iconKey)
    val shell = lerp(tint, Color.White, 0.55f)

    Box(modifier = modifier.size(sizeDp.dp)) {
        Canvas(modifier = Modifier.size(sizeDp.dp)) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f

            when (seed % 3) {
                // Placa de Petri vista desde arriba
                0 -> {
                    drawCircle(shell, radius = w * 0.42f, center = Offset(cx, cy))
                    drawCircle(tint.copy(alpha = 0.28f), radius = w * 0.34f, center = Offset(cx, cy))
                    repeat(4 + seed % 3) { i ->
                        val a = Math.toRadians((i * 74 + seed % 60).toDouble())
                        drawCircle(
                            tint,
                            radius = w * (0.045f + (i % 3) * 0.018f),
                            center = Offset(cx + (w * 0.19f * cos(a)).toFloat(), cy + (w * 0.19f * sin(a)).toFloat())
                        )
                    }
                    drawCircle(tint.copy(alpha = 0.75f), radius = w * 0.42f, center = Offset(cx, cy), style = Stroke(width = w * 0.045f))
                }
                // Tubo de ensayo
                1 -> {
                    val tubeW = w * 0.34f
                    val left = cx - tubeW / 2f
                    drawRoundRect(
                        color = tint.copy(alpha = 0.30f),
                        topLeft = Offset(left, h * 0.30f),
                        size = Size(tubeW, h * 0.55f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(tubeW / 2f, tubeW / 2f)
                    )
                    drawRoundRect(
                        color = tint,
                        topLeft = Offset(left, h * 0.52f),
                        size = Size(tubeW, h * 0.33f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(tubeW / 2f, tubeW / 2f)
                    )
                    repeat(3) { i ->
                        drawCircle(
                            Color.White.copy(alpha = 0.7f),
                            radius = w * 0.028f,
                            center = Offset(cx + (if (i % 2 == 0) -1 else 1) * w * 0.05f, h * (0.60f + i * 0.08f))
                        )
                    }
                    drawRoundRect(
                        color = shell,
                        topLeft = Offset(left - w * 0.03f, h * 0.24f),
                        size = Size(tubeW + w * 0.06f, h * 0.09f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.03f, w * 0.03f)
                    )
                }
                // Portaobjetos con muestra
                else -> {
                    drawRoundRect(
                        color = shell,
                        topLeft = Offset(w * 0.14f, h * 0.24f),
                        size = Size(w * 0.72f, h * 0.52f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.05f, w * 0.05f)
                    )
                    drawCircle(tint.copy(alpha = 0.35f), radius = w * 0.19f, center = Offset(cx, cy))
                    repeat(3 + seed % 3) { i ->
                        val a = Math.toRadians((i * 96 + seed % 45).toDouble())
                        drawCircle(
                            tint,
                            radius = w * 0.035f,
                            center = Offset(cx + (w * 0.11f * cos(a)).toFloat(), cy + (w * 0.11f * sin(a)).toFloat())
                        )
                    }
                    drawRoundRect(
                        color = tint.copy(alpha = 0.8f),
                        topLeft = Offset(w * 0.14f, h * 0.24f),
                        size = Size(w * 0.72f, h * 0.52f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.05f, w * 0.05f),
                        style = Stroke(width = w * 0.035f)
                    )
                }
            }
        }
    }
}

/**
 * Silueta de un descubrimiento aún no encontrado. En vez de un candado gris, muestra la forma
 * real en sombra: el niño ve que "hay algo ahí" y siente curiosidad por descubrirlo.
 */
@Composable
fun DiscoverySilhouette(
    category: DiscoveryCategory,
    iconKey: String,
    modifier: Modifier = Modifier,
    sizeDp: Int = 56
) {
    Box(modifier = modifier.size(sizeDp.dp)) {
        Canvas(modifier = Modifier.size(sizeDp.dp)) {
            val w = size.width
            val cx = w / 2f
            val cy = size.height / 2f
            val shadow = Color(0xFF6B6B6B).copy(alpha = 0.35f)
            val seed = stableSeed(iconKey)
            when (category) {
                DiscoveryCategory.PLANTA -> {
                    drawLine(shadow, Offset(cx, cy + w * 0.32f), Offset(cx, cy - w * 0.08f), strokeWidth = w * 0.06f)
                    repeat(3) { i ->
                        val a = Math.toRadians((i * 120 + seed % 40).toDouble())
                        drawCircle(shadow, radius = w * 0.15f, center = Offset(cx + (w * 0.24f * cos(a)).toFloat(), cy - w * 0.04f + (w * 0.15f * sin(a)).toFloat()))
                    }
                }
                DiscoveryCategory.ANIMAL -> {
                    drawCircle(shadow, radius = w * 0.28f, center = Offset(cx, cy))
                    drawCircle(shadow, radius = w * 0.1f, center = Offset(cx - w * 0.18f, cy - w * 0.23f))
                    drawCircle(shadow, radius = w * 0.1f, center = Offset(cx + w * 0.18f, cy - w * 0.23f))
                }
                DiscoveryCategory.MICROORGANISMO -> {
                    repeat(6) { i ->
                        val a = Math.toRadians((i * 60).toDouble())
                        drawCircle(shadow, radius = w * 0.08f, center = Offset(cx + (w * 0.25f * cos(a)).toFloat(), cy + (w * 0.25f * sin(a)).toFloat()))
                    }
                    drawCircle(shadow, radius = w * 0.2f, center = Offset(cx, cy))
                }
                DiscoveryCategory.HONGO -> {
                    drawLine(shadow, Offset(cx, cy + w * 0.28f), Offset(cx, cy), strokeWidth = w * 0.09f)
                    drawArc(
                        color = shadow, startAngle = 180f, sweepAngle = 180f, useCenter = true,
                        topLeft = Offset(cx - w * 0.3f, cy - w * 0.26f),
                        size = Size(w * 0.6f, w * 0.38f)
                    )
                }
            }
            // Signo de interrogación insinuado
            drawCircle(Color.White.copy(alpha = 0.55f), radius = w * 0.06f, center = Offset(cx + w * 0.3f, cy - w * 0.28f))
        }
    }
}

internal fun stableSeed(key: String): Int = abs(key.fold(7) { acc, c -> acc * 31 + c.code })
