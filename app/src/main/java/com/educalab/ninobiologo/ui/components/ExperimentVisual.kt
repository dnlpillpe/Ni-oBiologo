package com.educalab.ninobiologo.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.Experiment
import kotlin.math.cos
import kotlin.math.sin

/**
 * Simulación visual de un experimento. Antes esta pantalla era solo texto y un contador: el niño
 * leía "efecto drástico" sin ver nada. Ahora el resultado se DIBUJA —la planta se marchita, el
 * agua se enturbia, el termómetro sube, el ecosistema se desequilibra— y cambia en vivo mientras
 * mueve la variable, que es de lo que trata un experimento.
 *
 * @param health 0f = muy lejos del rango ideal, 1f = dentro del rango ideal.
 */
@Composable
fun ExperimentVisual(experiment: Experiment, value: Int, health: Float, modifier: Modifier = Modifier) {
    val animatedHealth by animateFloatAsState(targetValue = health.coerceIn(0f, 1f), animationSpec = tween(450), label = "exp_health")
    val ratio = if (experiment.variableMax > experiment.variableMin) {
        (value - experiment.variableMin).toFloat() / (experiment.variableMax - experiment.variableMin).toFloat()
    } else 0.5f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.6f)
            .clip(RoundedCornerShape(24.dp))
            .background(sceneBackground(experiment.id, animatedHealth))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            when (sceneFor(experiment.id)) {
                Scene.LUZ -> escenaLuz(animatedHealth, ratio)
                Scene.LLUVIA -> escenaLluvia(animatedHealth, ratio)
                Scene.TEMPERATURA -> escenaTemperatura(animatedHealth, ratio)
                Scene.CONTAMINACION -> escenaContaminacion(animatedHealth, ratio)
                Scene.HIDRATACION -> escenaHidratacion(animatedHealth, ratio)
                Scene.SUENO -> escenaSueno(animatedHealth, ratio)
                Scene.DESCOMPONEDORES -> escenaDescomponedores(animatedHealth, value)
                Scene.DEPREDADORES -> escenaDepredadores(animatedHealth, value)
            }
        }
    }
}

private enum class Scene { LUZ, LLUVIA, TEMPERATURA, CONTAMINACION, HIDRATACION, SUENO, DESCOMPONEDORES, DEPREDADORES }

/** Cada experimento del contenido semilla tiene su propia escena. */
private fun sceneFor(experimentId: String): Scene = when (experimentId) {
    "exp_micromundo_01" -> Scene.TEMPERATURA
    "exp_micromundo_02" -> Scene.LUZ
    "exp_bosque_de_vida_01" -> Scene.LUZ
    "exp_bosque_de_vida_02" -> Scene.LLUVIA
    "exp_oceano_profundo_01" -> Scene.CONTAMINACION
    "exp_oceano_profundo_02" -> Scene.TEMPERATURA
    "exp_cuerpo_humano_01" -> Scene.HIDRATACION
    "exp_cuerpo_humano_02" -> Scene.SUENO
    "exp_ecosistemas_01" -> Scene.DESCOMPONEDORES
    "exp_ecosistemas_02" -> Scene.DEPREDADORES
    else -> Scene.LUZ
}

private fun sceneBackground(experimentId: String, health: Float): Color = when (sceneFor(experimentId)) {
    Scene.CONTAMINACION, Scene.TEMPERATURA -> lerp(Color(0xFF6E8B7B), Color(0xFFCDE8F5), health)
    Scene.SUENO -> lerp(Color(0xFF2C3550), Color(0xFFBBD7F0), health)
    else -> lerp(Color(0xFFD8CBB2), Color(0xFFDDF0DA), health)
}

// ---------------- Escenas ----------------

/** Planta con sol: al bajar la luz la planta se encoge, amarillea y el sol se apaga. */
private fun DrawScope.escenaLuz(health: Float, ratio: Float) {
    val w = size.width; val h = size.height
    drawCircle(lerp(Color(0xFF8D7B4A), Color(0xFFFFD54F), ratio), radius = w * 0.09f, center = Offset(w * 0.83f, h * 0.22f))
    repeat(8) { i ->
        val a = Math.toRadians((i * 45).toDouble())
        val s = w * (0.11f + 0.05f * ratio)
        drawLine(
            lerp(Color(0xFF8D7B4A), Color(0xFFFFD54F), ratio),
            Offset(w * 0.83f + (s * cos(a)).toFloat(), h * 0.22f + (s * sin(a)).toFloat()),
            Offset(w * 0.83f + (s * 1.5f * cos(a)).toFloat(), h * 0.22f + (s * 1.5f * sin(a)).toFloat()),
            strokeWidth = w * 0.012f
        )
    }
    maceta(w * 0.35f, h)
    val green = lerp(Color(0xFFB59B4A), Color(0xFF3E8E41), health)
    val altura = h * (0.16f + 0.34f * health)
    drawLine(green, Offset(w * 0.35f, h * 0.74f), Offset(w * 0.35f, h * 0.74f - altura), strokeWidth = w * 0.018f)
    repeat(3) { i ->
        val y = h * 0.74f - altura * (0.42f + i * 0.26f)
        val droop = (1f - health) * h * 0.06f
        drawOval(green, topLeft = Offset(w * 0.35f - w * 0.14f, y - h * 0.03f + droop), size = Size(w * 0.13f, h * 0.075f))
        drawOval(green, topLeft = Offset(w * 0.35f + w * 0.01f, y - h * 0.03f + droop), size = Size(w * 0.13f, h * 0.075f))
    }
    if (health < 0.4f) hojasCaidas(w * 0.35f, h)
}

/** Planta con lluvia: gotas y suelo que se seca cuando falta agua. */
private fun DrawScope.escenaLluvia(health: Float, ratio: Float) {
    val w = size.width; val h = size.height
    val nube = lerp(Color(0xFFBFC6C9), Color(0xFF7E93A6), ratio)
    listOf(0.58f to 0.16f, 0.66f to 0.13f, 0.74f to 0.17f).forEach { (x, y) ->
        drawCircle(nube, radius = w * 0.07f, center = Offset(w * x, h * y))
    }
    val gotas = (ratio * 12).toInt()
    repeat(gotas) { i ->
        val x = w * (0.55f + (i % 5) * 0.05f)
        val y = h * (0.28f + ((i * 7) % 5) * 0.11f)
        drawLine(Color(0xFF4FA3D1), Offset(x, y), Offset(x - w * 0.008f, y + h * 0.07f), strokeWidth = w * 0.009f)
    }
    val suelo = lerp(Color(0xFFC7A06A), Color(0xFF6B4E2E), health)
    drawRect(suelo, topLeft = Offset(0f, h * 0.8f), size = Size(w, h * 0.2f))
    if (health < 0.45f) repeat(4) { i ->
        drawLine(Color(0xFF8C6A3F), Offset(w * (0.1f + i * 0.22f), h * 0.82f), Offset(w * (0.13f + i * 0.22f), h * 0.98f), strokeWidth = w * 0.008f)
    }
    val green = lerp(Color(0xFFB59B4A), Color(0xFF3E8E41), health)
    drawLine(green, Offset(w * 0.28f, h * 0.8f), Offset(w * 0.28f, h * (0.8f - 0.3f * (0.4f + health))), strokeWidth = w * 0.016f)
    drawOval(green, topLeft = Offset(w * 0.19f, h * (0.5f - 0.1f * health)), size = Size(w * 0.09f, h * 0.09f))
    drawOval(green, topLeft = Offset(w * 0.29f, h * (0.48f - 0.1f * health)), size = Size(w * 0.09f, h * 0.09f))
}

/** Termómetro y microorganismos que se agitan o se paralizan según la temperatura. */
private fun DrawScope.escenaTemperatura(health: Float, ratio: Float) {
    val w = size.width; val h = size.height
    val tubo = w * 0.05f
    drawRoundRect(Color.White, topLeft = Offset(w * 0.12f, h * 0.14f), size = Size(tubo, h * 0.6f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(tubo / 2f, tubo / 2f))
    val mercurio = lerp(Color(0xFF4FA3D1), Color(0xFFD64541), ratio)
    val alto = h * 0.58f * ratio
    drawRoundRect(mercurio, topLeft = Offset(w * 0.12f, h * 0.72f - alto), size = Size(tubo, alto + h * 0.02f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(tubo / 2f, tubo / 2f))
    drawCircle(mercurio, radius = w * 0.05f, center = Offset(w * 0.145f, h * 0.78f))

    val vivos = (2 + health * 5).toInt()
    repeat(vivos) { i ->
        val a = Math.toRadians((i * 61).toDouble())
        val c = Offset(w * (0.6f + 0.16f * cos(a)).toFloat(), h * (0.5f + 0.3f * sin(a)).toFloat())
        drawCircle(lerp(Color(0xFF9E9E9E), Color(0xFF2E8B8B), health), radius = w * 0.035f, center = c)
        repeat(6) { j ->
            val b = Math.toRadians((j * 60).toDouble())
            drawLine(lerp(Color(0xFF9E9E9E), Color(0xFF2E8B8B), health), c,
                Offset(c.x + (w * 0.055f * cos(b)).toFloat(), c.y + (w * 0.055f * sin(b)).toFloat()), strokeWidth = w * 0.007f)
        }
    }
}

/** Arrecife que pierde color y agua que se enturbia con la contaminación. */
private fun DrawScope.escenaContaminacion(health: Float, ratio: Float) {
    val w = size.width; val h = size.height
    drawRect(lerp(Color(0xFF6E7A5E), Color(0xFF7FC4E8), health), size = Size(w, h))
    repeat((ratio * 9).toInt()) { i ->
        drawCircle(Color(0xFF4E4B3E).copy(alpha = 0.35f), radius = w * (0.02f + (i % 3) * 0.015f),
            center = Offset(w * (0.1f + (i * 0.11f) % 0.85f), h * (0.15f + ((i * 5) % 6) * 0.12f)))
    }
    drawRect(Color(0xFFD9C9A3), topLeft = Offset(0f, h * 0.82f), size = Size(w, h * 0.18f))
    val coralColor = lerp(Color(0xFFE8E4DC), Color(0xFFE08A9B), health)
    listOf(0.25f, 0.5f, 0.72f).forEachIndexed { i, x ->
        repeat(3) { j ->
            val a = Math.toRadians((240.0 + j * 30))
            drawLine(coralColor, Offset(w * x, h * 0.84f),
                Offset(w * x + (w * 0.09f * cos(a)).toFloat(), h * 0.84f + (h * 0.22f * sin(a)).toFloat()),
                strokeWidth = w * 0.022f)
        }
        drawCircle(coralColor, radius = w * 0.035f, center = Offset(w * x, h * (0.62f - i * 0.01f)))
    }
}

/** Células del cuerpo hidratadas o arrugadas según los vasos de agua. */
private fun DrawScope.escenaHidratacion(health: Float, ratio: Float) {
    val w = size.width; val h = size.height
    val vaso = Path().apply {
        moveTo(w * 0.14f, h * 0.28f); lineTo(w * 0.30f, h * 0.28f)
        lineTo(w * 0.27f, h * 0.78f); lineTo(w * 0.17f, h * 0.78f); close()
    }
    drawPath(vaso, Color.White.copy(alpha = 0.8f))
    val nivel = h * 0.46f * ratio
    drawRect(Color(0xFF4FA3D1), topLeft = Offset(w * 0.175f, h * 0.77f - nivel), size = Size(w * 0.095f, nivel))
    drawPath(vaso, Color(0xFF90A4AE), style = Stroke(width = w * 0.012f))

    repeat(5) { i ->
        val a = Math.toRadians((i * 72).toDouble())
        val c = Offset(w * (0.65f + 0.16f * cos(a)).toFloat(), h * (0.5f + 0.28f * sin(a)).toFloat())
        val r = w * (0.03f + 0.022f * health)
        drawCircle(lerp(Color(0xFFB07F76), Color(0xFFC0392B), health), radius = r, center = c)
        drawCircle(lerp(Color(0xFF8E6560), Color(0xFF8E2A20), health), radius = r * 0.45f, center = c)
    }
}

/** Cerebro descansado o agotado: energía que se apaga si falta sueño. */
private fun DrawScope.escenaSueno(health: Float, ratio: Float) {
    val w = size.width; val h = size.height
    if (ratio < 0.55f) {
        drawCircle(Color(0xFFFFE082), radius = w * 0.07f, center = Offset(w * 0.82f, h * 0.24f))
        drawCircle(sceneBackgroundNight(health), radius = w * 0.06f, center = Offset(w * 0.79f, h * 0.21f))
    } else {
        drawCircle(Color(0xFFFFF3C4), radius = w * 0.075f, center = Offset(w * 0.82f, h * 0.24f))
    }
    val brain = lerp(Color(0xFF9E8E9B), Color(0xFFE59BB0), health)
    drawCircle(brain, radius = w * 0.15f, center = Offset(w * 0.4f, h * 0.5f))
    repeat(4) { i ->
        val y = h * (0.4f + i * 0.06f)
        val p = Path().apply {
            moveTo(w * 0.28f, y)
            cubicTo(w * 0.34f, y - h * 0.05f, w * 0.46f, y + h * 0.05f, w * 0.52f, y)
        }
        drawPath(p, brain.copy(alpha = 0.75f), style = Stroke(width = w * 0.012f))
    }
    repeat((health * 5).toInt() + 1) { i ->
        val p = Path().apply {
            val x = w * (0.58f + i * 0.07f)
            moveTo(x, h * 0.42f); lineTo(x + w * 0.03f, h * 0.5f)
            lineTo(x - w * 0.005f, h * 0.5f); lineTo(x + w * 0.025f, h * 0.6f)
        }
        drawPath(p, Color(0xFFFFD54F), style = Stroke(width = w * 0.012f))
    }
}

private fun sceneBackgroundNight(health: Float) = lerp(Color(0xFF2C3550), Color(0xFFBBD7F0), health)

/** Hojarasca que se recicla o se acumula según cuántos descomponedores haya. */
private fun DrawScope.escenaDescomponedores(health: Float, value: Int) {
    val w = size.width; val h = size.height
    drawRect(Color(0xFF6B4E2E), topLeft = Offset(0f, h * 0.78f), size = Size(w, h * 0.22f))
    val acumulacion = (1f - health)
    repeat((3 + acumulacion * 9).toInt()) { i ->
        val x = w * (0.08f + (i * 0.13f) % 0.86f)
        val y = h * (0.74f - (i / 7) * 0.1f)
        drawOval(lerp(Color(0xFF9E7B3D), Color(0xFFB98D46), (i % 3) / 3f),
            topLeft = Offset(x, y), size = Size(w * 0.1f, h * 0.055f))
    }
    repeat(value.coerceIn(0, 10)) { i ->
        val c = Offset(w * (0.12f + i * 0.085f), h * 0.88f)
        drawCircle(Color(0xFFA9A29B), radius = w * 0.022f, center = c)
        repeat(5) { j ->
            val a = Math.toRadians((j * 72).toDouble())
            drawLine(Color(0xFFA9A29B), c, Offset(c.x + (w * 0.038f * cos(a)).toFloat(), c.y + (w * 0.038f * sin(a)).toFloat()), strokeWidth = w * 0.006f)
        }
    }
}

/** Presas y depredadores: si hay demasiados cazadores, las presas desaparecen. */
private fun DrawScope.escenaDepredadores(health: Float, value: Int) {
    val w = size.width; val h = size.height
    drawRect(Color(0xFFCBB77A), topLeft = Offset(0f, h * 0.72f), size = Size(w, h * 0.28f))
    val presas = (7 - value * 0.6f).toInt().coerceIn(0, 7)
    repeat(presas) { i ->
        val c = Offset(w * (0.1f + i * 0.11f), h * 0.62f)
        drawCircle(Color(0xFFE0C68A), radius = w * 0.028f, center = c)
        drawCircle(Color(0xFFE0C68A), radius = w * 0.016f, center = Offset(c.x + w * 0.025f, c.y - w * 0.02f))
    }
    repeat(value.coerceIn(0, 10)) { i ->
        val c = Offset(w * (0.12f + i * 0.085f), h * 0.86f)
        drawCircle(Color(0xFFD9A441), radius = w * 0.03f, center = c)
        drawCircle(Color(0xFF9C6B2F), radius = w * 0.042f, center = c, style = Stroke(width = w * 0.012f))
    }
    if (presas == 0) {
        drawLine(Color(0xFFB33A3A), Offset(w * 0.1f, h * 0.58f), Offset(w * 0.25f, h * 0.66f), strokeWidth = w * 0.012f)
        drawLine(Color(0xFFB33A3A), Offset(w * 0.25f, h * 0.58f), Offset(w * 0.1f, h * 0.66f), strokeWidth = w * 0.012f)
    }
    if (health > 0.7f) drawCircle(Color(0xFF6FA85C).copy(alpha = 0.5f), radius = w * 0.05f, center = Offset(w * 0.88f, h * 0.2f))
}

// ---------------- Utilidades ----------------

private fun DrawScope.maceta(cx: Float, h: Float) {
    val w = size.width
    val p = Path().apply {
        moveTo(cx - w * 0.09f, h * 0.74f); lineTo(cx + w * 0.09f, h * 0.74f)
        lineTo(cx + w * 0.07f, h * 0.94f); lineTo(cx - w * 0.07f, h * 0.94f); close()
    }
    drawPath(p, Color(0xFFB4653A))
    drawRect(Color(0xFF9C5330), topLeft = Offset(cx - w * 0.1f, h * 0.71f), size = Size(w * 0.2f, h * 0.045f))
}

private fun DrawScope.hojasCaidas(cx: Float, h: Float) {
    val w = size.width
    listOf(-0.13f, 0.12f, 0.2f).forEach { dx ->
        drawOval(Color(0xFFB59B4A), topLeft = Offset(cx + w * dx, h * 0.9f), size = Size(w * 0.07f, h * 0.035f))
    }
}
