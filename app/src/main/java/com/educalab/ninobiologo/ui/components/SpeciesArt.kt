package com.educalab.ninobiologo.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.educalab.ninobiologo.domain.model.DiscoveryCategory
import kotlin.math.cos
import kotlin.math.sin

/**
 * Arte científico por ESPECIE, no por categoría. Antes todos los microorganismos se dibujaban
 * igual (un círculo con bultos) y un niño no podía distinguir una ameba de un paramecio. Aquí
 * cada especie tiene su forma y su rasgo real característico —cilios, flagelo, seudópodos,
 * caparazón de sílice, ocho brazos, disco bicóncavo…— con colores parecidos a los reales, para
 * que la ilustración enseñe de verdad en qué se diferencian.
 *
 * Se usa desde una sola función ([drawSpecies]) para que el museo, el ambiente, el analizador y
 * las criaturas que nadan en el microscopio muestren exactamente el mismo ser vivo.
 */
internal fun DrawScope.drawSpecies(
    iconKey: String,
    category: DiscoveryCategory,
    center: Offset,
    r: Float
) {
    when (iconKey) {
        // ---------- Micromundo ----------
        "disc_paramecio" -> paramecio(center, r)
        "disc_ameba" -> ameba(center, r)
        "disc_euglena" -> euglena(center, r)
        "disc_levadura" -> levadura(center, r)
        "disc_tardigrado" -> tardigrado(center, r)
        "disc_diatomea" -> diatomea(center, r)
        "disc_volvox" -> volvox(center, r)

        // ---------- Bosque de Vida ----------
        "disc_helecho" -> helecho(center, r)
        "disc_seta" -> hongoSombrero(center, r, Color(0xFFD64541), Color.White)
        "disc_conejo" -> mamifero(center, r, Color(0xFFBFA48B), Ears.LARGAS, mane = false, tail = Tail.POMPON)
        "disc_zorro" -> mamifero(center, r, Color(0xFFE07A3E), Ears.PUNTIAGUDAS, mane = false, tail = Tail.ESPESA)
        "disc_lince" -> mamifero(center, r, Color(0xFFA9A296), Ears.MECHONES, mane = false, tail = Tail.CORTA)
        "disc_mariposa" -> mariposa(center, r)
        "disc_buho" -> buho(center, r)

        // ---------- Océano Profundo ----------
        "disc_alga" -> alga(center, r)
        "disc_coral" -> coral(center, r)
        "disc_medusa" -> medusa(center, r)
        "disc_pulpo" -> pulpo(center, r)
        "disc_estrella" -> estrellaMar(center, r)
        "disc_pez_payaso" -> pezPayaso(center, r)
        "disc_ballena" -> ballena(center, r)

        // ---------- Cuerpo Humano ----------
        "disc_globulo_rojo" -> globuloRojo(center, r)
        "disc_globulo_blanco" -> globuloBlanco(center, r)
        "disc_neurona" -> neurona(center, r)
        "disc_celula_muscular" -> fibraMuscular(center, r)
        "disc_plaqueta" -> plaqueta(center, r)

        // ---------- Ecosistemas ----------
        "disc_pasto" -> pasto(center, r)
        "disc_leon" -> mamifero(center, r, Color(0xFFD9A441), Ears.REDONDAS, mane = true, tail = Tail.CORTA)
        "disc_nenufar" -> nenufar(center, r)
        "disc_hongo_reciclador" -> hongoFilamentoso(center, r)

        else -> generico(center, r, category)
    }
}

// ============================ Micromundo ============================

/** Paramecio: forma de zapatilla rodeada de cilios cortos (así se mueve). */
private fun DrawScope.paramecio(c: Offset, r: Float) {
    val body = Color(0xFF8FBF6A)
    val path = Path().apply {
        moveTo(c.x - r, c.y)
        cubicTo(c.x - r, c.y - r * 0.75f, c.x + r * 0.35f, c.y - r * 0.8f, c.x + r, c.y - r * 0.25f)
        cubicTo(c.x + r * 1.05f, c.y + r * 0.25f, c.x - r * 0.2f, c.y + r * 0.8f, c.x - r, c.y)
        close()
    }
    drawPath(path, body)
    repeat(20) { i ->
        val a = Math.toRadians((i * 18).toDouble())
        val sx = c.x + (r * 0.92f * cos(a)).toFloat()
        val sy = c.y + (r * 0.58f * sin(a)).toFloat()
        drawLine(body, Offset(sx, sy), Offset(sx + (r * 0.22f * cos(a)).toFloat(), sy + (r * 0.22f * sin(a)).toFloat()), strokeWidth = r * 0.07f)
    }
    drawOval(Color(0xFF4E7A34), topLeft = Offset(c.x - r * 0.22f, c.y - r * 0.2f), size = Size(r * 0.5f, r * 0.4f))
    ojos(c, r * 0.55f, r * 0.1f)
}

/** Ameba: contorno irregular con seudópodos (no tiene forma fija). */
private fun DrawScope.ameba(c: Offset, r: Float) {
    val body = Color(0xFF9FD6C4)
    val lobes = floatArrayOf(1.15f, 0.7f, 1.25f, 0.65f, 1.05f, 0.8f, 1.3f, 0.72f)
    val path = Path()
    lobes.forEachIndexed { i, f ->
        val a = Math.toRadians((i * 45).toDouble())
        val x = c.x + (r * f * cos(a)).toFloat()
        val y = c.y + (r * f * sin(a)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, body)
    drawCircle(Color(0xFF4C8C79), radius = r * 0.26f, center = Offset(c.x + r * 0.1f, c.y))
    ojos(c, r * 0.5f, r * 0.1f)
}

/** Euglena: gota verde con un flagelo largo y su mancha ocular roja. */
private fun DrawScope.euglena(c: Offset, r: Float) {
    val body = Color(0xFF57A94A)
    drawOval(body, topLeft = Offset(c.x - r * 0.55f, c.y - r), size = Size(r * 1.1f, r * 2f))
    drawCircle(Color(0xFFD8402F), radius = r * 0.16f, center = Offset(c.x + r * 0.2f, c.y - r * 0.62f))
    val flag = Path().apply {
        moveTo(c.x, c.y - r)
        cubicTo(c.x + r * 0.9f, c.y - r * 1.5f, c.x - r * 0.7f, c.y - r * 1.8f, c.x + r * 0.3f, c.y - r * 2.2f)
    }
    drawPath(flag, body, style = Stroke(width = r * 0.13f))
    ojos(Offset(c.x, c.y + r * 0.05f), r * 0.45f, r * 0.1f)
}

/** Levadura: célula redonda con una yema saliendo (se reproduce por gemación). */
private fun DrawScope.levadura(c: Offset, r: Float) {
    val body = Color(0xFFE6C77E)
    drawCircle(body, radius = r * 0.85f, center = c)
    drawCircle(body, radius = r * 0.42f, center = Offset(c.x + r * 0.85f, c.y - r * 0.6f))
    drawCircle(Color(0xFFB79A54), radius = r * 0.26f, center = c)
    ojos(c, r * 0.42f, r * 0.09f)
}

/** Tardígrado: cuerpo segmentado y sus ocho patas con garras ("oso de agua"). */
private fun DrawScope.tardigrado(c: Offset, r: Float) {
    val body = Color(0xFFC9A88B)
    drawOval(body, topLeft = Offset(c.x - r, c.y - r * 0.62f), size = Size(r * 1.9f, r * 1.24f))
    repeat(3) { i ->
        val x = c.x - r * 0.45f + i * r * 0.45f
        drawLine(Color(0xFFA98466), Offset(x, c.y - r * 0.55f), Offset(x, c.y + r * 0.55f), strokeWidth = r * 0.08f)
    }
    repeat(4) { i ->
        val x = c.x - r * 0.7f + i * r * 0.5f
        drawLine(body, Offset(x, c.y + r * 0.5f), Offset(x - r * 0.12f, c.y + r * 0.95f), strokeWidth = r * 0.16f)
        drawLine(body, Offset(x, c.y - r * 0.5f), Offset(x - r * 0.12f, c.y - r * 0.95f), strokeWidth = r * 0.16f)
    }
    drawCircle(body, radius = r * 0.42f, center = Offset(c.x + r * 0.95f, c.y))
    ojos(Offset(c.x + r * 0.95f, c.y), r * 0.3f, r * 0.08f)
}

/** Diatomea: caparazón de sílice geométrico con estrías (parece de cristal). */
private fun DrawScope.diatomea(c: Offset, r: Float) {
    val glass = Color(0xFFD9B44A)
    val hex = Path()
    repeat(6) { i ->
        val a = Math.toRadians((i * 60 - 30).toDouble())
        val x = c.x + (r * cos(a)).toFloat()
        val y = c.y + (r * sin(a)).toFloat()
        if (i == 0) hex.moveTo(x, y) else hex.lineTo(x, y)
    }
    hex.close()
    drawPath(hex, glass.copy(alpha = 0.45f))
    drawPath(hex, glass, style = Stroke(width = r * 0.12f))
    repeat(5) { i ->
        val y = c.y - r * 0.5f + i * r * 0.25f
        drawLine(glass, Offset(c.x - r * 0.6f, y), Offset(c.x + r * 0.6f, y), strokeWidth = r * 0.05f)
    }
}

/** Volvox: esfera hueca formada por muchas células pequeñas trabajando juntas. */
private fun DrawScope.volvox(c: Offset, r: Float) {
    val body = Color(0xFF63B36B)
    drawCircle(body.copy(alpha = 0.28f), radius = r, center = c)
    drawCircle(body, radius = r, center = c, style = Stroke(width = r * 0.08f))
    repeat(12) { i ->
        val a = Math.toRadians((i * 30).toDouble())
        drawCircle(body, radius = r * 0.13f, center = Offset(c.x + (r * 0.72f * cos(a)).toFloat(), c.y + (r * 0.72f * sin(a)).toFloat()))
    }
    repeat(3) { i ->
        val a = Math.toRadians((i * 120 + 40).toDouble())
        drawCircle(body.copy(alpha = 0.7f), radius = r * 0.2f, center = Offset(c.x + (r * 0.3f * cos(a)).toFloat(), c.y + (r * 0.3f * sin(a)).toFloat()))
    }
}

// ============================ Bosque ============================

/** Helecho: fronde curvada con foliolos a los lados (no tiene flores). */
private fun DrawScope.helecho(c: Offset, r: Float) {
    val green = Color(0xFF3B7D3F)
    val stem = Path().apply {
        moveTo(c.x - r * 0.1f, c.y + r)
        cubicTo(c.x - r * 0.2f, c.y, c.x + r * 0.35f, c.y - r * 0.5f, c.x + r * 0.15f, c.y - r)
    }
    drawPath(stem, green, style = Stroke(width = r * 0.12f))
    repeat(6) { i ->
        val t = i / 6f
        val x = c.x - r * 0.12f + r * 0.3f * t
        val y = c.y + r * 0.85f - r * 1.75f * t
        val len = r * (0.55f - 0.35f * t)
        drawOval(green, topLeft = Offset(x - len, y - r * 0.12f), size = Size(len, r * 0.24f))
        drawOval(green, topLeft = Offset(x, y - r * 0.12f), size = Size(len, r * 0.24f))
    }
}

/** Hongo con sombrero (seta): sombrero de color, puntos y pie. */
private fun DrawScope.hongoSombrero(c: Offset, r: Float, cap: Color, dots: Color) {
    drawRoundRect(
        Color(0xFFF0E2C8), topLeft = Offset(c.x - r * 0.22f, c.y - r * 0.1f), size = Size(r * 0.44f, r * 1.05f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.16f, r * 0.16f)
    )
    drawArc(cap, 180f, 180f, true, topLeft = Offset(c.x - r, c.y - r * 0.72f), size = Size(r * 2f, r * 1.3f))
    listOf(-0.5f to -0.28f, 0.12f to -0.42f, 0.55f to -0.18f).forEach { (dx, dy) ->
        drawCircle(dots, radius = r * 0.13f, center = Offset(c.x + r * dx, c.y + r * dy))
    }
    ojos(Offset(c.x, c.y + r * 0.35f), r * 0.2f, r * 0.07f)
}

private enum class Ears { LARGAS, PUNTIAGUDAS, MECHONES, REDONDAS }
private enum class Tail { POMPON, ESPESA, CORTA }

/** Mamífero parametrizado: las orejas, la melena y la cola distinguen conejo/zorro/lince/león. */
private fun DrawScope.mamifero(c: Offset, r: Float, fur: Color, ears: Ears, mane: Boolean, tail: Tail) {
    val dark = Color(0xFF000000).copy(alpha = 0.18f)
    when (tail) {
        Tail.POMPON -> drawCircle(Color.White, radius = r * 0.28f, center = Offset(c.x - r * 0.95f, c.y + r * 0.4f))
        Tail.ESPESA -> {
            drawOval(fur, topLeft = Offset(c.x - r * 1.5f, c.y + r * 0.05f), size = Size(r * 0.95f, r * 0.5f))
            drawCircle(Color.White, radius = r * 0.16f, center = Offset(c.x - r * 1.42f, c.y + r * 0.3f))
        }
        Tail.CORTA -> drawOval(fur, topLeft = Offset(c.x - r * 1.15f, c.y + r * 0.15f), size = Size(r * 0.45f, r * 0.28f))
    }
    if (mane) drawCircle(Color(0xFF9C6B2F), radius = r * 0.98f, center = c)
    drawCircle(fur, radius = r * 0.72f, center = c)

    when (ears) {
        Ears.LARGAS -> listOf(-0.32f, 0.32f).forEach { dx ->
            drawOval(fur, topLeft = Offset(c.x + r * dx - r * 0.13f, c.y - r * 1.5f), size = Size(r * 0.26f, r * 0.9f))
        }
        Ears.PUNTIAGUDAS -> listOf(-0.42f, 0.42f).forEach { dx ->
            val p = Path().apply {
                moveTo(c.x + r * dx - r * 0.24f, c.y - r * 0.5f)
                lineTo(c.x + r * dx, c.y - r * 1.15f)
                lineTo(c.x + r * dx + r * 0.24f, c.y - r * 0.5f); close()
            }
            drawPath(p, fur)
        }
        Ears.MECHONES -> listOf(-0.42f, 0.42f).forEach { dx ->
            val p = Path().apply {
                moveTo(c.x + r * dx - r * 0.2f, c.y - r * 0.5f)
                lineTo(c.x + r * dx, c.y - r * 1.0f)
                lineTo(c.x + r * dx + r * 0.2f, c.y - r * 0.5f); close()
            }
            drawPath(p, fur)
            drawLine(Color(0xFF3A3A3A), Offset(c.x + r * dx, c.y - r * 1.0f), Offset(c.x + r * dx + r * 0.08f, c.y - r * 1.45f), strokeWidth = r * 0.07f)
        }
        Ears.REDONDAS -> listOf(-0.5f, 0.5f).forEach { dx ->
            drawCircle(fur, radius = r * 0.24f, center = Offset(c.x + r * dx, c.y - r * 0.62f))
        }
    }
    drawCircle(dark, radius = r * 0.1f, center = Offset(c.x, c.y + r * 0.28f))
    ojos(c, r * 0.28f, r * 0.1f)
}

/** Mariposa: cuatro alas naranjas con borde negro y antenas. */
private fun DrawScope.mariposa(c: Offset, r: Float) {
    val wing = Color(0xFFE2792B)
    val edge = Color(0xFF2E2A26)
    listOf(-1, 1).forEach { s ->
        drawOval(wing, topLeft = Offset(if (s < 0) c.x - r * 1.1f else c.x + r * 0.12f, c.y - r * 0.9f), size = Size(r * 0.98f, r * 0.8f))
        drawOval(wing, topLeft = Offset(if (s < 0) c.x - r * 0.95f else c.x + r * 0.1f, c.y + r * 0.05f), size = Size(r * 0.85f, r * 0.7f))
        drawOval(edge, topLeft = Offset(if (s < 0) c.x - r * 1.1f else c.x + r * 0.12f, c.y - r * 0.9f), size = Size(r * 0.98f, r * 0.8f), style = Stroke(width = r * 0.1f))
    }
    drawOval(edge, topLeft = Offset(c.x - r * 0.12f, c.y - r * 0.85f), size = Size(r * 0.24f, r * 1.7f))
    listOf(-1, 1).forEach { s ->
        drawLine(edge, Offset(c.x, c.y - r * 0.8f), Offset(c.x + s * r * 0.4f, c.y - r * 1.3f), strokeWidth = r * 0.06f)
    }
    ojos(Offset(c.x, c.y - r * 0.6f), r * 0.12f, r * 0.06f)
}

/** Búho: cara redonda, ojos enormes y penachos de plumas. */
private fun DrawScope.buho(c: Offset, r: Float) {
    val feather = Color(0xFF8A6A4B)
    drawOval(feather, topLeft = Offset(c.x - r * 0.8f, c.y - r * 0.85f), size = Size(r * 1.6f, r * 1.8f))
    listOf(-0.45f, 0.45f).forEach { dx ->
        val p = Path().apply {
            moveTo(c.x + r * dx - r * 0.2f, c.y - r * 0.6f)
            lineTo(c.x + r * dx + r * 0.05f, c.y - r * 1.15f)
            lineTo(c.x + r * dx + r * 0.22f, c.y - r * 0.55f); close()
        }
        drawPath(p, feather)
    }
    listOf(-0.34f, 0.34f).forEach { dx ->
        drawCircle(Color.White, radius = r * 0.31f, center = Offset(c.x + r * dx, c.y - r * 0.22f))
        drawCircle(Color(0xFFE8A33D), radius = r * 0.2f, center = Offset(c.x + r * dx, c.y - r * 0.22f))
        drawCircle(Color(0xFF1B1B1B), radius = r * 0.1f, center = Offset(c.x + r * dx, c.y - r * 0.22f))
    }
    val beak = Path().apply {
        moveTo(c.x - r * 0.11f, c.y + r * 0.12f); lineTo(c.x + r * 0.11f, c.y + r * 0.12f); lineTo(c.x, c.y + r * 0.42f); close()
    }
    drawPath(beak, Color(0xFFE8A33D))
}

// ============================ Océano ============================

/** Alga parda gigante: tallo largo y ondulado con hojas (bosque submarino). */
private fun DrawScope.alga(c: Offset, r: Float) {
    val kelp = Color(0xFF8A6B31)
    val stem = Path().apply {
        moveTo(c.x, c.y + r)
        cubicTo(c.x - r * 0.5f, c.y + r * 0.3f, c.x + r * 0.5f, c.y - r * 0.3f, c.x - r * 0.1f, c.y - r)
    }
    drawPath(stem, kelp, style = Stroke(width = r * 0.14f))
    repeat(5) { i ->
        val t = i / 5f
        val x = c.x + r * 0.35f * sin((t * 6.0)).toFloat() - r * 0.05f
        val y = c.y + r * 0.85f - r * 1.7f * t
        drawOval(kelp, topLeft = Offset(x - r * 0.75f, y - r * 0.1f), size = Size(r * 0.7f, r * 0.2f))
        drawOval(kelp, topLeft = Offset(x + r * 0.05f, y - r * 0.1f), size = Size(r * 0.7f, r * 0.2f))
    }
}

/** Coral cerebro: colonia con surcos sinuosos como un cerebro. */
private fun DrawScope.coral(c: Offset, r: Float) {
    val body = Color(0xFFE08A9B)
    drawCircle(body, radius = r, center = c)
    val groove = Color(0xFFB25E72)
    repeat(4) { i ->
        val y = c.y - r * 0.55f + i * r * 0.38f
        val p = Path().apply {
            moveTo(c.x - r * 0.82f, y)
            cubicTo(c.x - r * 0.3f, y - r * 0.28f, c.x + r * 0.3f, y + r * 0.28f, c.x + r * 0.82f, y)
        }
        drawPath(p, groove, style = Stroke(width = r * 0.13f))
    }
}

/** Medusa: campana translúcida y tentáculos colgando. */
private fun DrawScope.medusa(c: Offset, r: Float) {
    val bell = Color(0xFF9BC7E8)
    drawArc(bell.copy(alpha = 0.85f), 180f, 180f, true, topLeft = Offset(c.x - r, c.y - r * 0.95f), size = Size(r * 2f, r * 1.5f))
    repeat(5) { i ->
        val x = c.x - r * 0.7f + i * r * 0.35f
        val p = Path().apply {
            moveTo(x, c.y - r * 0.2f)
            cubicTo(x - r * 0.2f, c.y + r * 0.25f, x + r * 0.2f, c.y + r * 0.6f, x - r * 0.06f, c.y + r * 1.05f)
        }
        drawPath(p, bell, style = Stroke(width = r * 0.09f))
    }
    repeat(4) { i ->
        val a = Math.toRadians((i * 90 + 45).toDouble())
        drawCircle(Color(0xFF6FA5CC), radius = r * 0.12f, center = Offset(c.x + (r * 0.35f * cos(a)).toFloat(), c.y - r * 0.42f))
    }
    ojos(Offset(c.x, c.y - r * 0.5f), r * 0.28f, r * 0.08f)
}

/** Pulpo: cabeza grande y ocho brazos con ventosas. */
private fun DrawScope.pulpo(c: Offset, r: Float) {
    val body = Color(0xFFC2607A)
    repeat(8) { i ->
        val a = Math.toRadians((180.0 + i * 22.5))
        val ex = c.x + (r * 1.3f * cos(a)).toFloat()
        val ey = c.y + r * 0.55f + (r * 0.75f * -sin(a)).toFloat()
        val p = Path().apply {
            moveTo(c.x + (r * 0.45f * cos(a)).toFloat(), c.y + r * 0.35f)
            cubicTo(c.x + (r * 0.9f * cos(a)).toFloat(), c.y + r * 0.9f, ex, c.y + r * 0.7f, ex, ey + r * 0.5f)
        }
        drawPath(p, body, style = Stroke(width = r * 0.13f))
    }
    drawOval(body, topLeft = Offset(c.x - r * 0.72f, c.y - r * 0.95f), size = Size(r * 1.44f, r * 1.5f))
    ojos(Offset(c.x, c.y - r * 0.35f), r * 0.32f, r * 0.13f)
}

/** Estrella de mar: cinco brazos gruesos. */
private fun DrawScope.estrellaMar(c: Offset, r: Float) {
    val body = Color(0xFFE0873C)
    val p = Path()
    repeat(10) { i ->
        val rad = if (i % 2 == 0) r else r * 0.42f
        val a = Math.toRadians((i * 36 - 90).toDouble())
        val x = c.x + (rad * cos(a)).toFloat()
        val y = c.y + (rad * sin(a)).toFloat()
        if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
    }
    p.close()
    drawPath(p, body)
    repeat(5) { i ->
        val a = Math.toRadians((i * 72 - 90).toDouble())
        drawCircle(Color(0xFFB9682A), radius = r * 0.07f, center = Offset(c.x + (r * 0.5f * cos(a)).toFloat(), c.y + (r * 0.5f * sin(a)).toFloat()))
    }
    ojos(c, r * 0.22f, r * 0.09f)
}

/** Pez payaso: cuerpo naranja con las tres bandas blancas y aleta de cola. */
private fun DrawScope.pezPayaso(c: Offset, r: Float) {
    val body = Color(0xFFEE7B23)
    val tail = Path().apply {
        moveTo(c.x - r * 0.55f, c.y)
        lineTo(c.x - r * 1.25f, c.y - r * 0.5f)
        lineTo(c.x - r * 1.25f, c.y + r * 0.5f); close()
    }
    drawPath(tail, body)
    drawOval(body, topLeft = Offset(c.x - r * 0.75f, c.y - r * 0.6f), size = Size(r * 1.85f, r * 1.2f))
    listOf(-0.2f, 0.35f, 0.85f).forEach { dx ->
        drawOval(Color.White, topLeft = Offset(c.x + r * dx, c.y - r * 0.58f), size = Size(r * 0.2f, r * 1.16f))
    }
    drawCircle(Color.White, radius = r * 0.18f, center = Offset(c.x + r * 0.78f, c.y - r * 0.13f))
    drawCircle(Color(0xFF1B1B1B), radius = r * 0.09f, center = Offset(c.x + r * 0.8f, c.y - r * 0.13f))
}

/** Ballena azul: cuerpo enorme, cola y su chorro de agua. */
private fun DrawScope.ballena(c: Offset, r: Float) {
    val body = Color(0xFF5B8FB9)
    val tail = Path().apply {
        moveTo(c.x - r * 0.75f, c.y)
        lineTo(c.x - r * 1.35f, c.y - r * 0.55f)
        lineTo(c.x - r * 1.05f, c.y)
        lineTo(c.x - r * 1.35f, c.y + r * 0.55f); close()
    }
    drawPath(tail, body)
    drawOval(body, topLeft = Offset(c.x - r * 0.95f, c.y - r * 0.62f), size = Size(r * 2f, r * 1.24f))
    drawOval(Color(0xFFBFD9E8), topLeft = Offset(c.x - r * 0.5f, c.y + r * 0.05f), size = Size(r * 1.3f, r * 0.5f))
    repeat(3) { i ->
        drawCircle(Color(0xFF9BC7E8), radius = r * (0.1f - i * 0.02f), center = Offset(c.x + r * 0.42f, c.y - r * (0.85f + i * 0.28f)))
    }
    drawCircle(Color.White, radius = r * 0.15f, center = Offset(c.x + r * 0.72f, c.y - r * 0.1f))
    drawCircle(Color(0xFF1B1B1B), radius = r * 0.07f, center = Offset(c.x + r * 0.74f, c.y - r * 0.1f))
}

// ============================ Cuerpo Humano ============================

/** Glóbulo rojo: disco bicóncavo (hundido en el centro, sin núcleo). */
private fun DrawScope.globuloRojo(c: Offset, r: Float) {
    drawCircle(Color(0xFFC0392B), radius = r, center = c)
    drawCircle(Color(0xFF8E2A20), radius = r * 0.45f, center = c)
    drawCircle(Color(0xFFD9584A), radius = r * 0.3f, center = c)
}

/** Glóbulo blanco: núcleo lobulado y seudópodos para perseguir microbios. */
private fun DrawScope.globuloBlanco(c: Offset, r: Float) {
    drawCircle(Color(0xFFF2F0F7), radius = r, center = c)
    repeat(5) { i ->
        val a = Math.toRadians((i * 72 + 20).toDouble())
        drawCircle(Color(0xFFF2F0F7), radius = r * 0.28f, center = Offset(c.x + (r * 0.95f * cos(a)).toFloat(), c.y + (r * 0.95f * sin(a)).toFloat()))
    }
    repeat(3) { i ->
        val a = Math.toRadians((i * 120).toDouble())
        drawCircle(Color(0xFF8E6FBF), radius = r * 0.27f, center = Offset(c.x + (r * 0.24f * cos(a)).toFloat(), c.y + (r * 0.24f * sin(a)).toFloat()))
    }
}

/** Neurona: soma con muchas dendritas y un axón largo. */
private fun DrawScope.neurona(c: Offset, r: Float) {
    val body = Color(0xFF9B7EC8)
    repeat(7) { i ->
        val a = Math.toRadians((i * 51 + 120).toDouble())
        val ex = c.x + (r * 1.5f * cos(a)).toFloat()
        val ey = c.y + (r * 1.5f * sin(a)).toFloat()
        drawLine(body, c, Offset(ex, ey), strokeWidth = r * 0.11f)
        drawLine(body, Offset(ex, ey), Offset(ex + r * 0.3f * cos(a + 0.6).toFloat(), ey + r * 0.3f * sin(a + 0.6).toFloat()), strokeWidth = r * 0.07f)
    }
    drawLine(body, c, Offset(c.x + r * 1.9f, c.y + r * 0.25f), strokeWidth = r * 0.16f)
    repeat(3) { i ->
        drawCircle(body, radius = r * 0.12f, center = Offset(c.x + r * (1.9f + i * 0.05f), c.y + r * (0.25f + i * 0.22f)))
    }
    drawCircle(body, radius = r * 0.6f, center = c)
    drawCircle(Color(0xFF6B4F99), radius = r * 0.25f, center = c)
}

/** Célula muscular: fibra muy alargada con estrías transversales. */
private fun DrawScope.fibraMuscular(c: Offset, r: Float) {
    val body = Color(0xFFE07C8B)
    drawRoundRect(
        body, topLeft = Offset(c.x - r * 1.3f, c.y - r * 0.42f), size = Size(r * 2.6f, r * 0.84f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.42f, r * 0.42f)
    )
    repeat(7) { i ->
        val x = c.x - r * 1.05f + i * r * 0.35f
        drawLine(Color(0xFFB35566), Offset(x, c.y - r * 0.36f), Offset(x, c.y + r * 0.36f), strokeWidth = r * 0.07f)
    }
    drawCircle(Color(0xFF8E3F4E), radius = r * 0.14f, center = Offset(c.x - r * 0.5f, c.y - r * 0.18f))
    drawCircle(Color(0xFF8E3F4E), radius = r * 0.14f, center = Offset(c.x + r * 0.55f, c.y + r * 0.18f))
}

/** Plaqueta: fragmento pequeño e irregular con prolongaciones para taponar heridas. */
private fun DrawScope.plaqueta(c: Offset, r: Float) {
    val body = Color(0xFFE8C15C)
    val p = Path()
    val f = floatArrayOf(0.9f, 0.55f, 0.85f, 0.5f, 0.95f, 0.6f)
    f.forEachIndexed { i, v ->
        val a = Math.toRadians((i * 60).toDouble())
        val x = c.x + (r * v * cos(a)).toFloat()
        val y = c.y + (r * v * sin(a)).toFloat()
        if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
    }
    p.close()
    drawPath(p, body)
    repeat(4) { i ->
        val a = Math.toRadians((i * 90 + 30).toDouble())
        drawLine(body, Offset(c.x + (r * 0.55f * cos(a)).toFloat(), c.y + (r * 0.55f * sin(a)).toFloat()),
            Offset(c.x + (r * 1.15f * cos(a)).toFloat(), c.y + (r * 1.15f * sin(a)).toFloat()), strokeWidth = r * 0.1f)
    }
}

// ============================ Ecosistemas ============================

/** Pasto de sabana: matas de hierba que rebrotan tras la lluvia. */
private fun DrawScope.pasto(c: Offset, r: Float) {
    val green = Color(0xFF7FA845)
    repeat(7) { i ->
        val x = c.x - r * 0.9f + i * r * 0.3f
        val lean = (i - 3) * r * 0.13f
        val p = Path().apply {
            moveTo(x, c.y + r * 0.9f)
            cubicTo(x + lean * 0.3f, c.y, x + lean, c.y - r * 0.4f, x + lean * 1.3f, c.y - r * 0.95f)
        }
        drawPath(p, green, style = Stroke(width = r * 0.13f))
    }
}

/** Nenúfar: hoja flotante circular con su muesca y la flor. */
private fun DrawScope.nenufar(c: Offset, r: Float) {
    drawCircle(Color(0xFF4E8C4A), radius = r * 0.95f, center = Offset(c.x, c.y + r * 0.15f))
    val notch = Path().apply {
        moveTo(c.x, c.y + r * 0.15f)
        lineTo(c.x + r * 0.95f, c.y - r * 0.2f)
        lineTo(c.x + r * 0.95f, c.y + r * 0.5f); close()
    }
    drawPath(notch, Color(0xFFDCEFDC))
    repeat(8) { i ->
        val a = Math.toRadians((i * 45).toDouble())
        drawOval(Color(0xFFE9A7C4), topLeft = Offset(c.x - r * 0.14f + (r * 0.3f * cos(a)).toFloat(), c.y - r * 0.62f + (r * 0.3f * sin(a)).toFloat()), size = Size(r * 0.28f, r * 0.18f))
    }
    drawCircle(Color(0xFFF3D45E), radius = r * 0.16f, center = Offset(c.x, c.y - r * 0.55f))
}

/** Hongo filamentoso: red de hifas que descompone la materia. */
private fun DrawScope.hongoFilamentoso(c: Offset, r: Float) {
    val body = Color(0xFFA9A29B)
    repeat(9) { i ->
        val a = Math.toRadians((i * 40).toDouble())
        val ex = c.x + (r * 1.15f * cos(a)).toFloat()
        val ey = c.y + (r * 1.15f * sin(a)).toFloat()
        drawLine(body, c, Offset(ex, ey), strokeWidth = r * 0.09f)
        drawCircle(body, radius = r * 0.13f, center = Offset(ex, ey))
    }
    drawCircle(body, radius = r * 0.3f, center = c)
}

// ============================ Utilidades ============================

/** Ojos amistosos: dan personalidad para el público infantil sin falsear la biología. */
private fun DrawScope.ojos(c: Offset, sep: Float, r: Float) {
    drawCircle(Color.White, radius = r * 1.5f, center = Offset(c.x - sep * 0.5f, c.y))
    drawCircle(Color.White, radius = r * 1.5f, center = Offset(c.x + sep * 0.5f, c.y))
    drawCircle(Color(0xFF1B1B1B), radius = r * 0.8f, center = Offset(c.x - sep * 0.5f, c.y))
    drawCircle(Color(0xFF1B1B1B), radius = r * 0.8f, center = Offset(c.x + sep * 0.5f, c.y))
}

/** Respaldo si apareciera una especie sin arte propio: forma según su categoría. */
private fun DrawScope.generico(c: Offset, r: Float, category: DiscoveryCategory) {
    val color = when (category) {
        DiscoveryCategory.PLANTA -> Color(0xFF3E8E41)
        DiscoveryCategory.ANIMAL -> Color(0xFFC57B3E)
        DiscoveryCategory.MICROORGANISMO -> Color(0xFF2E8B8B)
        DiscoveryCategory.HONGO -> Color(0xFFB5473A)
    }
    drawCircle(color, radius = r * 0.8f, center = c)
    ojos(c, r * 0.5f, r * 0.1f)
}

/** Rota el dibujo de una especie (usado por las criaturas que nadan). */
internal fun DrawScope.drawSpeciesRotated(iconKey: String, category: DiscoveryCategory, center: Offset, r: Float, degrees: Float) {
    rotate(degrees = degrees, pivot = center) { drawSpecies(iconKey, category, center, r) }
}
