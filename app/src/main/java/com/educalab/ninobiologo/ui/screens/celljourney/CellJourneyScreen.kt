package com.educalab.ninobiologo.ui.screens.celljourney

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.ui.components.AmbientParticles
import com.educalab.ninobiologo.ui.components.BiaExpression
import com.educalab.ninobiologo.ui.components.BiaGuide
import com.educalab.ninobiologo.ui.components.PrimaryButton
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.components.SimpleTopBar
import com.educalab.ninobiologo.ui.components.XpBar
import com.educalab.ninobiologo.ui.viewmodel.CellJourneyViewModel

/** "Viaje al Interior de la Célula": recorrido guiado, parada por parada, por cada estructura. */
@Composable
fun CellJourneyScreen(viewModel: CellJourneyViewModel, onBack: () -> Unit) {
    LaunchedEffect(Unit) { viewModel.load() }
    val state by viewModel.uiState.collectAsState()
    val model = state.cellModels.getOrNull(state.selectedIndex)

    Scaffold(topBar = { SimpleTopBar(title = "Viaje al Interior de la Célula", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            SectionHeader("Viaje al Interior de la Célula", "Recorre cada estructura, parada por parada, junto a BIA.")
            LazyRow {
                items(state.cellModels) { cell ->
                    FilterChip(
                        selected = cell.id == model?.id,
                        onClick = { viewModel.selectCell(state.cellModels.indexOf(cell)) },
                        label = { Text(cell.name) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            if (model == null) return@Column
            val structure = model.structures.getOrNull(state.stopIndex) ?: return@Column

            XpBar(progress = state.completionPercent / 100f)
            Spacer(Modifier.height(4.dp))
            Text("Parada ${state.stopIndex + 1} de ${model.structures.size}", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    drawCircle(Color.White.copy(alpha = 0.5f), radius = w * 0.42f, center = Offset(w / 2f, h / 2f))
                    model.structures.forEach { s ->
                        val isCurrent = s.id == structure.id
                        drawCircle(
                            color = if (isCurrent) Color(0xFF2E7D32) else Color(0xFFEF6C00).copy(alpha = 0.35f),
                            radius = if (isCurrent) w * 0.075f else w * 0.035f,
                            center = Offset(s.xPercent * w, s.yPercent * h)
                        )
                    }
                }
                AmbientParticles(modifier = Modifier.fillMaxSize(), color = Color(0xFF2E7D32), count = 10)
            }

            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                BiaGuide(expression = BiaExpression.INVESTIGANDO, sizeDp = 64)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(structure.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(structure.function, style = MaterialTheme.typography.bodyMedium)
                }
            }
            if (state.isComplete) {
                Spacer(Modifier.height(8.dp))
                Text("¡Recorrido completo! Conoces toda la ${model.name.lowercase()}.", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.weight(1f))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PrimaryButton(text = "Atrás", onClick = { viewModel.previousStop() }, modifier = Modifier.weight(1f), enabled = state.stopIndex > 0)
                PrimaryButton(text = "Siguiente parada", onClick = { viewModel.nextStop() }, modifier = Modifier.weight(1f), enabled = state.stopIndex < model.structures.lastIndex)
            }
        }
    }
}
