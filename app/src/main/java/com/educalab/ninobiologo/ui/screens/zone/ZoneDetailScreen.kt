package com.educalab.ninobiologo.ui.screens.zone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.ModuleState
import com.educalab.ninobiologo.ui.components.AppCard
import com.educalab.ninobiologo.ui.components.OrganismIllustration
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.viewmodel.ExpeditionCardUiState
import com.educalab.ninobiologo.ui.viewmodel.ZoneViewModel

@Composable
fun ZoneDetailScreen(
    biomeId: String,
    viewModel: ZoneViewModel,
    onBack: () -> Unit,
    onExpeditionClick: (String) -> Unit,
    onEcosystemClick: (String) -> Unit,
    onChallengeClick: (String) -> Unit,
    onMicroscopeClick: () -> Unit
) {
    LaunchedEffect(biomeId) { viewModel.load(biomeId) }
    val state by viewModel.uiState.collectAsState()
    val biome = state.biome

    Scaffold(
        topBar = {
            Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") }
                Column {
                    Text(biome?.name ?: "", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(biome?.tagline ?: "", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    ) { padding ->
        if (biome == null) {
            Column(Modifier.fillMaxSize().padding(padding)) { }
            return@Scaffold
        }
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp)) {
            if (biomeId == "micromundo") {
                item {
                    AppCard(onClick = onMicroscopeClick) {
                        Text("Microscopio Virtual", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("Explora células y descubre sus estructuras en detalle.", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
            item {
                SectionHeader("Expediciones", "${state.expeditionCards.count { it.state == ModuleState.COMPLETADO || it.state == ModuleState.DOMINADO }}/${state.expeditionCards.size} completadas")
            }
            items(state.expeditionCards) { card ->
                ExpeditionRow(card = card, onClick = { if (card.state != ModuleState.BLOQUEADO) onExpeditionClick(card.expedition.id) })
                Spacer(Modifier.height(10.dp))
            }

            item { Spacer(Modifier.height(12.dp)); SectionHeader("Organismos de la zona", "${state.discoveredIds.count { id -> state.organisms.any { it.id == id } }}/${state.organisms.size} descubiertos") }
            item {
                LazyRow {
                    items(state.organisms) { organism ->
                        val discovered = organism.id in state.discoveredIds
                        Column(
                            modifier = Modifier.padding(end = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (discovered) {
                                OrganismIllustration(category = organism.category, iconKey = organism.iconKey, sizeDp = 56)
                                Text(organism.name, style = MaterialTheme.typography.labelMedium)
                            } else {
                                Column(
                                    modifier = Modifier.size(56.dp).clip(CircleShape),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(Icons.Filled.Lock, contentDescription = "Sin descubrir", tint = Color.Gray)
                                }
                                Text("???", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                            }
                        }
                    }
                }
            }

            if (state.ecosystemTemplates.isNotEmpty()) {
                item { Spacer(Modifier.height(12.dp)); SectionHeader("Constructor de Ecosistemas") }
                items(state.ecosystemTemplates) { template ->
                    AppCard(onClick = { onEcosystemClick(template.id) }) {
                        Text(template.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(template.description, style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }

            if (state.challenges.isNotEmpty()) {
                item { Spacer(Modifier.height(12.dp)); SectionHeader("Desafíos") }
                items(state.challenges) { challenge ->
                    AppCard(onClick = { onChallengeClick(challenge.id) }) {
                        Text(challenge.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(challenge.instructions, style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun ExpeditionRow(card: ExpeditionCardUiState, onClick: () -> Unit) {
    val locked = card.state == ModuleState.BLOQUEADO
    AppCard(onClick = if (locked) null else onClick) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(1f)) {
                Text(card.expedition.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(stateLabel(card.state), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            }
            if (locked) {
                Icon(Icons.Filled.Lock, contentDescription = "Bloqueada", tint = Color.Gray)
            } else {
                Text("★".repeat(card.bestStars).ifEmpty { "☆☆☆" }, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

private fun stateLabel(state: ModuleState): String = when (state) {
    ModuleState.BLOQUEADO -> "Bloqueada"
    ModuleState.DISPONIBLE -> "Disponible"
    ModuleState.INICIADO -> "En progreso"
    ModuleState.COMPLETADO -> "Completada"
    ModuleState.DOMINADO -> "¡Dominada!"
}
