package com.educalab.ninobiologo.ui.screens.microscope

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.ui.components.AmbientParticles
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.components.SimpleTopBar
import com.educalab.ninobiologo.ui.components.XpBar
import com.educalab.ninobiologo.ui.viewmodel.MicroscopeViewModel
import kotlin.math.hypot

/**
 * Microscopio virtual: el niño acerca y mueve la muestra de verdad (pellizcar para hacer zoom,
 * arrastrar para moverse) y toca los puntos brillantes para revelar estructuras reales, en vez de
 * simplemente leer un texto.
 */
@Composable
fun MicroscopeScreen(viewModel: MicroscopeViewModel, onBack: () -> Unit) {
    LaunchedEffect(Unit) { viewModel.load() }
    val state by viewModel.uiState.collectAsState()
    val model = state.cellModels.getOrNull(state.selectedIndex)
    val exploration = state.explorationState

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Scaffold(topBar = { SimpleTopBar(title = "Microscopio Virtual", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            SectionHeader("Microscopio Virtual", "Pellizca para acercar y toca los puntos brillantes para descubrir cada estructura.")
            LazyRow {
                items(state.cellModels) { cell ->
                    FilterChip(
                        selected = cell.id == model?.id,
                        onClick = {
                            viewModel.selectCell(state.cellModels.indexOf(cell))
                            scale = 1f; offset = Offset.Zero
                        },
                        label = { Text(cell.name) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            if (model != null && exploration != null) {
                XpBar(progress = state.completionPercent / 100f)
                Spacer(Modifier.height(4.dp))
                Text("${state.completionPercent}% explorado", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(28.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(1f, 4f)
                                offset += pan
                            }
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(scaleX = scale, scaleY = scale, translationX = offset.x, translationY = offset.y)
                            .pointerInput(model.id) {
                                detectTapOnStructures(model, onTap = { viewModel.revealStructure(it) })
                            }
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            drawCircle(Color.White.copy(alpha = 0.5f), radius = w * 0.42f, center = Offset(w / 2f, h / 2f))
                            model.structures.forEach { structure ->
                                val revealed = structure.id in exploration.revealedStructureIds
                                drawCircle(
                                    color = if (revealed) Color(0xFF2E7D32) else Color(0xFFEF6C00),
                                    radius = if (revealed) w * 0.05f else w * 0.045f,
                                    center = Offset(structure.xPercent * w, structure.yPercent * h)
                                )
                            }
                        }
                        AmbientParticles(modifier = Modifier.fillMaxSize(), color = Color(0xFF2E7D32), count = 8)
                    }
                }
                Spacer(Modifier.height(16.dp))
                val revealedStructures = model.structures.filter { it.id in exploration.revealedStructureIds }
                revealedStructures.forEach { structure ->
                    Column(Modifier.padding(bottom = 8.dp)) {
                        Text(structure.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(structure.function, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                if (state.isComplete) {
                    Text("¡Célula completamente observada!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private suspend fun androidx.compose.ui.input.pointer.PointerInputScope.detectTapOnStructures(
    model: com.educalab.ninobiologo.domain.model.CellModel,
    onTap: (String) -> Unit
) {
    detectTapGestures { offset ->

        val w = size.width
        val h = size.height

        val touchRadius = w * 0.09f

        val tapped = model.structures.minByOrNull { structure ->
            hypot(
                (structure.xPercent * w - offset.x).toDouble(),
                (structure.yPercent * h - offset.y).toDouble()
            )
        }

        if (tapped != null) {
            val dx = tapped.xPercent * w - offset.x
            val dy = tapped.yPercent * h - offset.y

            if (hypot(dx.toDouble(), dy.toDouble()) <= touchRadius) {
                onTap(tapped.id)
            }
        }
    }
}
