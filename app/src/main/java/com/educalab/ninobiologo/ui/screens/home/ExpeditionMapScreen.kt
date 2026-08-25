package com.educalab.ninobiologo.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.ui.components.BiaExpression
import com.educalab.ninobiologo.ui.components.BiaGuide
import com.educalab.ninobiologo.ui.components.XpBar
import com.educalab.ninobiologo.ui.viewmodel.BiomeCardUiState
import com.educalab.ninobiologo.ui.viewmodel.ExpeditionMapViewModel

/**
 * Pantalla principal: "Mapa de Expediciones Biológicas". No es un menú plano: funciona como
 * centro de experiencia mostrando avatar, rango, progreso y las 5 zonas como tarjetas de mundo
 * (sección HOME/DASHBOARD del prompt específico).
 */
@Composable
fun ExpeditionMapScreen(
    viewModel: ExpeditionMapViewModel,
    onZoneClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onJournalClick: () -> Unit,
    onMuseumClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hola, ${state.alias}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(state.rank, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                }
                Row {
                    IconButton(onClick = onMuseumClick) {
                        Icon(Icons.Filled.Museum, contentDescription = "Museo Biológico")
                    }
                    IconButton(onClick = onJournalClick) {
                        Icon(Icons.Filled.AutoStories, contentDescription = "Diario del explorador")
                    }
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Filled.Person, contentDescription = "Perfil")
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            ) {
                BiaGuide(expression = BiaExpression.FELIZ, sizeDp = 56)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("${state.totalXp} XP", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    XpBar(progress = state.progressToNextRank)
                }
            }
            Spacer(Modifier.height(12.dp))
            LazyColumn(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)) {
                items(state.biomeCards) { card ->
                    BiomeMapCard(card = card, onClick = { onZoneClick(card.biome.id) })
                    Spacer(Modifier.height(16.dp))
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun BiomeMapCard(card: BiomeCardUiState, onClick: () -> Unit) {
    val primary = Color(android.graphics.Color.parseColor(card.biome.primaryColorHex))
    val secondary = Color(android.graphics.Color.parseColor(card.biome.secondaryColorHex))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(secondary)
            .clickable(onClick = onClick)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(primary.copy(alpha = 0.35f), radius = size.height * 0.6f, center = Offset(size.width * 0.88f, size.height * 0.15f))
            drawCircle(primary.copy(alpha = 0.2f), radius = size.height * 0.45f, center = Offset(size.width * 0.08f, size.height * 0.95f))
        }
        Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(card.biome.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primary)
                Box(
                    modifier = Modifier.clip(CircleShape).background(primary).size(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${card.completionPercent}%", style = MaterialTheme.typography.labelMedium, color = Color.White)
                }
            }
            Text(card.biome.tagline, style = MaterialTheme.typography.bodyMedium, color = primary.copy(alpha = 0.85f))
            Spacer(Modifier.weight(1f))
            Text(
                "${card.expeditionsCompleted}/${card.expeditionsTotal} expediciones",
                style = MaterialTheme.typography.labelLarge,
                color = primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
