package com.educalab.ninobiologo.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/** Los 8 exploradores disponibles, en el mismo orden que las claves avatar_explorador_1..8. */
private data class ExplorerLook(
    val skin: Color,
    val hair: Color,
    val hairStyle: HairStyle,
    val coat: Color,
    val gear: Gear
)

private enum class HairStyle { CORTO, COLETA, RIZADO, TRENZAS, MOÑO, PUNTAS, LARGO, GORRO }
private enum class Gear { GAFAS, LUPA, LINTERNA_FRONTAL, MASCARILLA, GAFAS_BUCEO, NINGUNO, GAFAS_SOL, AURICULARES }

private val LOOKS = listOf(
    ExplorerLook(Color(0xFFF3C9A0), Color(0xFF3B2B20), HairStyle.CORTO, Color(0xFFFFFFFF), Gear.GAFAS),
    ExplorerLook(Color(0xFF8D5524), Color(0xFF201512), HairStyle.RIZADO, Color(0xFFE7F2E4), Gear.LUPA),
    ExplorerLook(Color(0xFFE0AC69), Color(0xFF6B3410), HairStyle.COLETA, Color(0xFFFFFFFF), Gear.LINTERNA_FRONTAL),
    ExplorerLook(Color(0xFFC68642), Color(0xFF1B1B1B), HairStyle.TRENZAS, Color(0xFFDDEFF7), Gear.MASCARILLA),
    ExplorerLook(Color(0xFFF7D9BE), Color(0xFFB8651B), HairStyle.MOÑO, Color(0xFFFFFFFF), Gear.GAFAS_BUCEO),
    ExplorerLook(Color(0xFF5C3317), Color(0xFF120C09), HairStyle.PUNTAS, Color(0xFFFDF0D5), Gear.NINGUNO),
    ExplorerLook(Color(0xFFFFE0BD), Color(0xFF9B9B9B), HairStyle.LARGO, Color(0xFFEFE4FA), Gear.GAFAS_SOL),
    ExplorerLook(Color(0xFFA1665E), Color(0xFF2E2E2E), HairStyle.GORRO, Color(0xFFFFF3E0), Gear.AURICULARES)
)

/** Índice 0..7 a partir de la clave "avatar_explorador_N". */
private fun lookIndexFor(avatarKey: String): Int =
    ((avatarKey.takeLastWhile { it.isDigit() }.toIntOrNull() ?: 1) - 1).coerceIn(0, LOOKS.lastIndex)

/**
 * Retrato del joven biólogo elegido por el niño. Cada uno de los 8 avatares es una persona
 * distinta —piel, pelo, peinado, bata y equipo científico— para que pueda reconocerse a sí mismo,
 * en vez de la etiqueta "B1"…"B8" que se mostraba antes.
 */
@Composable
fun ExplorerAvatar(avatarKey: String, modifier: Modifier = Modifier, sizeDp: Int = 72) {
    val look = LOOKS[lookIndexFor(avatarKey)]
    Box(modifier = modifier.size(sizeDp.dp)) {
        Canvas(modifier = Modifier.size(sizeDp.dp)) {
            drawExplorer(look, Offset(size.width / 2f, size.height / 2f), size.width * 0.5f)
        }
    }
}

/** Avatar seleccionable: crece y se resalta al elegirlo (respuesta visual a cada toque). */
@Composable
fun SelectableExplorerAvatar(
    avatarKey: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeDp: Int = 72
) {
    val scale by animateFloatAsState(targetValue = if (selected) 1.12f else 1f, animationSpec = tween(220), label = "avatar_scale")
    val ring = if (selected) Color(0xFF2E7D32) else Color(0xFF2E7D32).copy(alpha = 0.18f)
    Box(
        modifier = modifier
            .size((sizeDp + 12).dp)
            .scale(scale)
            .clip(CircleShape)
            .background(if (selected) Color(0xFFDCEFDC) else Color(0xFFF2F5F0))
            .clickable(onClick = onClick)
    ) {
        Canvas(modifier = Modifier.size((sizeDp + 12).dp)) {
            val c = Offset(size.width / 2f, size.height / 2f)
            drawCircle(ring, radius = size.width * 0.47f, center = c, style = Stroke(width = size.width * 0.05f))
            drawExplorer(LOOKS[lookIndexFor(avatarKey)], c, size.width * 0.42f)
        }
    }
}

private fun DrawScope.drawExplorer(look: ExplorerLook, c: Offset, r: Float) {
    // Hombros con bata de laboratorio
    drawArc(
        look.coat, 180f, 180f, true,
        topLeft = Offset(c.x - r * 0.95f, c.y + r * 0.28f),
        size = Size(r * 1.9f, r * 1.5f)
    )
    drawLine(Color(0xFFB9C4B5), Offset(c.x, c.y + r * 0.35f), Offset(c.x, c.y + r * 1.0f), strokeWidth = r * 0.05f)

    // Cabeza
    drawCircle(look.skin, radius = r * 0.62f, center = c)

    // Pelo según el peinado
    val hair = look.hair
    when (look.hairStyle) {
        HairStyle.CORTO -> drawArc(hair, 180f, 180f, true, topLeft = Offset(c.x - r * 0.63f, c.y - r * 0.72f), size = Size(r * 1.26f, r * 1.1f))
        HairStyle.COLETA -> {
            drawArc(hair, 180f, 180f, true, topLeft = Offset(c.x - r * 0.63f, c.y - r * 0.72f), size = Size(r * 1.26f, r * 1.05f))
            drawCircle(hair, radius = r * 0.26f, center = Offset(c.x + r * 0.72f, c.y - r * 0.28f))
        }
        HairStyle.RIZADO -> repeat(7) { i ->
            val a = Math.toRadians((180.0 + i * 30))
            drawCircle(hair, radius = r * 0.24f, center = Offset(c.x + (r * 0.58f * kotlin.math.cos(a)).toFloat(), c.y + (r * 0.58f * kotlin.math.sin(a)).toFloat()))
        }
        HairStyle.TRENZAS -> {
            drawArc(hair, 180f, 180f, true, topLeft = Offset(c.x - r * 0.63f, c.y - r * 0.72f), size = Size(r * 1.26f, r * 1.0f))
            listOf(-0.7f, 0.7f).forEach { dx ->
                drawRoundRect(hair, topLeft = Offset(c.x + r * dx - r * 0.11f, c.y - r * 0.15f), size = Size(r * 0.22f, r * 0.75f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.11f, r * 0.11f))
            }
        }
        HairStyle.MOÑO -> {
            drawArc(hair, 180f, 180f, true, topLeft = Offset(c.x - r * 0.63f, c.y - r * 0.72f), size = Size(r * 1.26f, r * 1.0f))
            drawCircle(hair, radius = r * 0.28f, center = Offset(c.x, c.y - r * 0.85f))
        }
        HairStyle.PUNTAS -> {
            drawArc(hair, 180f, 180f, true, topLeft = Offset(c.x - r * 0.63f, c.y - r * 0.7f), size = Size(r * 1.26f, r * 1.0f))
            repeat(4) { i ->
                val x = c.x - r * 0.42f + i * r * 0.28f
                val p = Path().apply { moveTo(x - r * 0.12f, c.y - r * 0.5f); lineTo(x, c.y - r * 0.95f); lineTo(x + r * 0.12f, c.y - r * 0.5f); close() }
                drawPath(p, hair)
            }
        }
        HairStyle.LARGO -> {
            drawArc(hair, 180f, 180f, true, topLeft = Offset(c.x - r * 0.68f, c.y - r * 0.75f), size = Size(r * 1.36f, r * 1.1f))
            listOf(-0.62f, 0.62f).forEach { dx ->
                drawOval(hair, topLeft = Offset(c.x + r * dx - r * 0.14f, c.y - r * 0.35f), size = Size(r * 0.28f, r * 0.95f))
            }
        }
        HairStyle.GORRO -> {
            drawArc(Color(0xFF2E7D32), 180f, 180f, true, topLeft = Offset(c.x - r * 0.7f, c.y - r * 0.8f), size = Size(r * 1.4f, r * 1.15f))
            drawRoundRect(Color(0xFF2E7D32), topLeft = Offset(c.x - r * 0.8f, c.y - r * 0.3f), size = Size(r * 1.6f, r * 0.18f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.09f, r * 0.09f))
        }
    }

    // Ojos y sonrisa
    listOf(-0.24f, 0.24f).forEach { dx ->
        drawCircle(Color.White, radius = r * 0.13f, center = Offset(c.x + r * dx, c.y + r * 0.02f))
        drawCircle(Color(0xFF1B1B1B), radius = r * 0.07f, center = Offset(c.x + r * dx, c.y + r * 0.02f))
    }
    drawArc(
        Color(0xFF9E5B4A), 20f, 140f, false,
        topLeft = Offset(c.x - r * 0.2f, c.y + r * 0.1f), size = Size(r * 0.4f, r * 0.3f),
        style = Stroke(width = r * 0.06f)
    )

    // Equipo científico: lo que hace único a cada explorador
    when (look.gear) {
        Gear.GAFAS -> {
            listOf(-0.24f, 0.24f).forEach { dx ->
                drawCircle(Color(0xFF3A3A3A), radius = r * 0.2f, center = Offset(c.x + r * dx, c.y + r * 0.02f), style = Stroke(width = r * 0.05f))
            }
            drawLine(Color(0xFF3A3A3A), Offset(c.x - r * 0.05f, c.y + r * 0.02f), Offset(c.x + r * 0.05f, c.y + r * 0.02f), strokeWidth = r * 0.05f)
        }
        Gear.LUPA -> {
            drawCircle(Color(0xFF0277BD), radius = r * 0.26f, center = Offset(c.x + r * 0.78f, c.y + r * 0.5f), style = Stroke(width = r * 0.08f))
            drawCircle(Color(0xFFBBDEFB).copy(alpha = 0.6f), radius = r * 0.22f, center = Offset(c.x + r * 0.78f, c.y + r * 0.5f))
            drawLine(Color(0xFF6D4C41), Offset(c.x + r * 0.62f, c.y + r * 0.72f), Offset(c.x + r * 0.42f, c.y + r * 1.0f), strokeWidth = r * 0.09f)
        }
        Gear.LINTERNA_FRONTAL -> {
            drawRoundRect(Color(0xFF37474F), topLeft = Offset(c.x - r * 0.66f, c.y - r * 0.42f), size = Size(r * 1.32f, r * 0.17f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.08f, r * 0.08f))
            drawCircle(Color(0xFFFFE082), radius = r * 0.17f, center = Offset(c.x, c.y - r * 0.34f))
        }
        Gear.MASCARILLA -> {
            drawRoundRect(Color(0xFFB3E5FC), topLeft = Offset(c.x - r * 0.42f, c.y + r * 0.12f), size = Size(r * 0.84f, r * 0.5f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.14f, r * 0.14f))
            drawLine(Color(0xFF81D4FA), Offset(c.x - r * 0.42f, c.y + r * 0.2f), Offset(c.x - r * 0.62f, c.y + r * 0.05f), strokeWidth = r * 0.05f)
            drawLine(Color(0xFF81D4FA), Offset(c.x + r * 0.42f, c.y + r * 0.2f), Offset(c.x + r * 0.62f, c.y + r * 0.05f), strokeWidth = r * 0.05f)
        }
        Gear.GAFAS_BUCEO -> {
            drawRoundRect(Color(0xFF00838F).copy(alpha = 0.75f), topLeft = Offset(c.x - r * 0.5f, c.y - r * 0.16f), size = Size(r * 1.0f, r * 0.38f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.16f, r * 0.16f))
            drawLine(Color(0xFF00838F), Offset(c.x - r * 0.5f, c.y + r * 0.02f), Offset(c.x - r * 0.66f, c.y + r * 0.02f), strokeWidth = r * 0.07f)
            drawLine(Color(0xFF00838F), Offset(c.x + r * 0.5f, c.y + r * 0.02f), Offset(c.x + r * 0.66f, c.y + r * 0.02f), strokeWidth = r * 0.07f)
        }
        Gear.GAFAS_SOL -> {
            drawRoundRect(Color(0xFF263238), topLeft = Offset(c.x - r * 0.46f, c.y - r * 0.1f), size = Size(r * 0.92f, r * 0.26f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.1f, r * 0.1f))
        }
        Gear.AURICULARES -> {
            drawArc(Color(0xFF37474F), 200f, 140f, false, topLeft = Offset(c.x - r * 0.7f, c.y - r * 0.7f), size = Size(r * 1.4f, r * 1.4f), style = Stroke(width = r * 0.09f))
            listOf(-0.66f, 0.66f).forEach { dx ->
                drawCircle(Color(0xFF37474F), radius = r * 0.16f, center = Offset(c.x + r * dx, c.y + r * 0.02f))
            }
        }
        Gear.NINGUNO -> {
            // Libreta de campo en la mano
            drawRoundRect(Color(0xFFEF6C00), topLeft = Offset(c.x + r * 0.55f, c.y + r * 0.55f), size = Size(r * 0.42f, r * 0.5f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.06f, r * 0.06f))
            drawLine(Color.White, Offset(c.x + r * 0.62f, c.y + r * 0.72f), Offset(c.x + r * 0.9f, c.y + r * 0.72f), strokeWidth = r * 0.05f)
        }
    }
}
