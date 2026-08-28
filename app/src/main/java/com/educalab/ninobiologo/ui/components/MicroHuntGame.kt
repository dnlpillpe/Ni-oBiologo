package com.educalab.ninobiologo.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.educalab.ninobiologo.domain.model.DiscoveryCategory
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Mini-juego de caza microscópica: las criaturas de la muestra nadan de verdad por el campo del
 * microscopio y el niño tiene que tocarlas para atraparlas. Sustituye al antiguo "pulsa continuar"
 * por una acción real — el aprendizaje ocurre haciendo, no leyendo.
 *
 * @param discoveries criaturas presentes en la muestra.
 * @param caughtIds ids ya atrapados (se dibujan resaltados y dejan de moverse).
 * @param onCatch se invoca al tocar una criatura todavía libre.
 */
@Composable
fun MicroHuntGame(
    discoveries: List<MicroscopeDiscovery>,
    caughtIds: Set<String>,
    onCatch: (MicroscopeDiscovery) -> Unit,
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition(label = "hunt")
    val t by infinite.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(11000, easing = LinearEasing), RepeatMode.Restart),
        label = "swim"
    )
    // El gesto necesita el tiempo actual, no el capturado en la composición inicial.
    val currentT by rememberUpdatedState(t)
    val currentCaught by rememberUpdatedState(caughtIds)
    val currentList by rememberUpdatedState(discoveries)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(Color(0xFFDCEFDC))
            .pointerInput(Unit) {
                detectTapGestures { tap ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()
                    val hit = currentList.withIndex().firstOrNull { (index, discovery) ->
                        if (discovery.id in currentCaught) return@firstOrNull false
                        val p = swimPosition(index, currentList.size, currentT, w, h)
                        hypot((p.x - tap.x).toDouble(), (p.y - tap.y).toDouble()) <= w * 0.13f
                    }
                    hit?.let { onCatch(it.value) }
                }
            }
    ) {
        // Halo de lente + partículas de fondo
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawCircle(Color.White.copy(alpha = 0.55f), radius = w * 0.47f, center = Offset(w / 2f, h / 2f))
            drawCircle(Color(0xFF2E7D32).copy(alpha = 0.25f), radius = w * 0.47f, center = Offset(w / 2f, h / 2f), style = Stroke(width = w * 0.02f))

            currentList.forEachIndexed { index, discovery ->
                val caught = discovery.id in caughtIds
                val p = if (caught) restPosition(index, currentList.size, w, h) else swimPosition(index, currentList.size, t, w, h)
                drawCreature(p, w * 0.085f, discovery.category, caught)
            }
        }
        AmbientParticles(modifier = Modifier.fillMaxSize(), color = Color(0xFF2E7D32), count = 10)
    }
}

/** Movimiento orgánico: cada criatura recorre su propia órbita irregular dentro de la lente. */
private fun swimPosition(index: Int, total: Int, t: Float, w: Float, h: Float): Offset {
    val phase = t * 6.2831853f + index * 1.7f
    val ringBase = 0.14f + (index % 3) * 0.09f
    val wobble = 0.035f * sin((phase * 2.3f).toDouble()).toFloat()
    val radius = (ringBase + wobble) * w
    val angle = phase * (if (index % 2 == 0) 1f else -1f) * (0.6f + (index % 4) * 0.12f) + index * (6.2831853f / total.coerceAtLeast(1))
    return Offset(
        w / 2f + (radius * cos(angle.toDouble())).toFloat(),
        h / 2f + (radius * sin(angle.toDouble())).toFloat() * 0.95f
    )
}

/** Posición ordenada donde descansan las criaturas ya atrapadas (fila inferior de la lente). */
private fun restPosition(index: Int, total: Int, w: Float, h: Float): Offset {
    val slots = total.coerceAtLeast(1)
    val step = w * 0.62f / slots
    return Offset(w * 0.19f + step * (index + 0.5f), h * 0.83f)
}

private fun DrawScope.drawCreature(center: Offset, radius: Float, category: DiscoveryCategory, caught: Boolean) {
    val base = when (category) {
        DiscoveryCategory.PLANTA -> Color(0xFF3E8E41)
        DiscoveryCategory.ANIMAL -> Color(0xFFC57B3E)
        DiscoveryCategory.MICROORGANISMO -> Color(0xFF2E8B8B)
        DiscoveryCategory.HONGO -> Color(0xFFB5473A)
    }
    if (caught) {
        drawCircle(Color(0xFF2E7D32).copy(alpha = 0.28f), radius = radius * 1.7f, center = center)
    } else {
        // Estela que insinúa movimiento
        drawCircle(base.copy(alpha = 0.18f), radius = radius * 1.45f, center = center)
    }
    drawCircle(base, radius = radius, center = center)

    // Cilios / flagelo alrededor: señal visual de "está vivo"
    repeat(7) { i ->
        val a = Math.toRadians((i * 51.4).toDouble())
        drawLine(
            base.copy(alpha = 0.85f),
            start = Offset(center.x + (radius * cos(a)).toFloat(), center.y + (radius * sin(a)).toFloat()),
            end = Offset(center.x + (radius * 1.45f * cos(a)).toFloat(), center.y + (radius * 1.45f * sin(a)).toFloat()),
            strokeWidth = radius * 0.16f
        )
    }
    // Núcleo y ojitos (personalidad para el público infantil)
    drawCircle(Color.White.copy(alpha = 0.9f), radius = radius * 0.42f, center = center)
    drawCircle(Color(0xFF15201A), radius = radius * 0.13f, center = Offset(center.x - radius * 0.15f, center.y - radius * 0.05f))
    drawCircle(Color(0xFF15201A), radius = radius * 0.13f, center = Offset(center.x + radius * 0.15f, center.y - radius * 0.05f))

    if (caught) {
        drawCircle(Color(0xFF2E7D32), radius = radius * 1.35f, center = center, style = Stroke(width = radius * 0.22f))
    }
}

/** Marcador de progreso de la caza, con animación al subir. */
@Composable
fun HuntProgress(caught: Int, total: Int, modifier: Modifier = Modifier) {
    val progress by animateFloatAsState(
        targetValue = if (total == 0) 0f else caught.toFloat() / total.toFloat(),
        animationSpec = tween(420),
        label = "hunt_progress"
    )
    XpBar(progress = progress, modifier = modifier, barHeight = 14)
}
