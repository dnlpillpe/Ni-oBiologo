package com.educalab.ninobiologo.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.sin

enum class BiaExpression { OBSERVANDO, SORPRENDIDA, FELIZ, INVESTIGANDO, CELEBRANDO }

/**
 * BIA, la asistente bióloga inteligente. Se dibuja íntegramente con Compose Canvas (sin assets
 * externos, 100% offline) para que tenga una identidad visual propia y reconocible en toda la
 * app. Sus expresiones cambian según el contexto de la expedición.
 */
@Composable
fun BiaGuide(expression: BiaExpression, modifier: Modifier = Modifier, sizeDp: Int = 96) {
    val infiniteTransition = rememberInfiniteTransition(label = "bia_bob")
    val bob by infiniteTransition.animateFloat(
        initialValue = -3f, targetValue = 3f,
        animationSpec = infiniteRepeatable(tween(1400), RepeatMode.Reverse), label = "bob"
    )
    val bodyColor = MaterialTheme.colorScheme.primary
    val accentColor = MaterialTheme.colorScheme.secondary

    Box(modifier = modifier.size(sizeDp.dp)) {
        Canvas(modifier = Modifier.size(sizeDp.dp)) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f + bob

            // Cuerpo (cápsula de exploración)
            drawCircle(color = bodyColor, radius = w * 0.38f, center = Offset(cx, cy))
            drawCircle(color = accentColor, radius = w * 0.38f, center = Offset(cx, cy), style = Stroke(width = w * 0.03f))

            // Visor / cara
            val visorRadius = w * 0.26f
            drawCircle(color = Color.White, radius = visorRadius, center = Offset(cx, cy - h * 0.02f))

            val eyeOffsetX = w * 0.09f
            val eyeY = cy - h * 0.02f
            when (expression) {
                BiaExpression.OBSERVANDO -> {
                    drawCircle(bodyColor, radius = w * 0.03f, center = Offset(cx - eyeOffsetX, eyeY))
                    drawCircle(bodyColor, radius = w * 0.03f, center = Offset(cx + eyeOffsetX, eyeY))
                }
                BiaExpression.SORPRENDIDA -> {
                    drawCircle(bodyColor, radius = w * 0.045f, center = Offset(cx - eyeOffsetX, eyeY))
                    drawCircle(bodyColor, radius = w * 0.045f, center = Offset(cx + eyeOffsetX, eyeY))
                }
                BiaExpression.FELIZ, BiaExpression.CELEBRANDO -> {
                    val sweep = 20f
                    drawArc(bodyColor, 200f, sweep, false, topLeft = Offset(cx - eyeOffsetX - visorRadius * 0.18f, eyeY - visorRadius * 0.1f), size = androidx.compose.ui.geometry.Size(visorRadius * 0.36f, visorRadius * 0.36f), style = Stroke(width = w * 0.02f))
                    drawArc(bodyColor, 200f, sweep, false, topLeft = Offset(cx + eyeOffsetX - visorRadius * 0.18f, eyeY - visorRadius * 0.1f), size = androidx.compose.ui.geometry.Size(visorRadius * 0.36f, visorRadius * 0.36f), style = Stroke(width = w * 0.02f))
                }
                BiaExpression.INVESTIGANDO -> {
                    drawCircle(bodyColor, radius = w * 0.03f, center = Offset(cx - eyeOffsetX, eyeY - h * 0.015f))
                    drawCircle(bodyColor, radius = w * 0.03f, center = Offset(cx + eyeOffsetX, eyeY - h * 0.015f))
                    // lupa
                    drawCircle(accentColor, radius = w * 0.08f, center = Offset(cx + w * 0.22f, cy + h * 0.16f), style = Stroke(width = w * 0.02f))
                }
            }

            // Boca según expresión
            val mouthY = cy + h * 0.08f
            when (expression) {
                BiaExpression.FELIZ, BiaExpression.CELEBRANDO -> drawArc(
                    color = bodyColor, startAngle = 10f, sweepAngle = 160f, useCenter = false,
                    topLeft = Offset(cx - w * 0.09f, mouthY - w * 0.03f),
                    size = androidx.compose.ui.geometry.Size(w * 0.18f, w * 0.12f),
                    style = Stroke(width = w * 0.02f)
                )
                BiaExpression.SORPRENDIDA -> drawCircle(bodyColor, radius = w * 0.025f, center = Offset(cx, mouthY))
                else -> drawLine(bodyColor, Offset(cx - w * 0.06f, mouthY), Offset(cx + w * 0.06f, mouthY), strokeWidth = w * 0.02f)
            }

            // Antena
            drawLine(accentColor, Offset(cx, cy - w * 0.38f), Offset(cx, cy - w * 0.5f), strokeWidth = w * 0.025f)
            drawCircle(accentColor, radius = w * 0.035f, center = Offset(cx, cy - w * 0.52f))

            if (expression == BiaExpression.CELEBRANDO) {
                val sparkleColor = accentColor
                repeat(4) { i ->
                    val angle = (i * 90f) + (bob * 6f)
                    val rad = Math.toRadians(angle.toDouble())
                    val sx = cx + (w * 0.55f * sin(rad)).toFloat()
                    val sy = cy - (w * 0.5f) - (w * 0.15f * i)
                    drawCircle(sparkleColor, radius = w * 0.02f, center = Offset(sx, sy))
                }
            }
        }
    }
}
