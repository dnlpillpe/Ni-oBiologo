package com.educalab.ninobiologo.ui.screens.environment

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Science
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
import com.educalab.ninobiologo.domain.model.SampleExplorationState
import com.educalab.ninobiologo.ui.components.AppCard
import com.educalab.ninobiologo.ui.components.DiscoveryIllustration
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.viewmodel.EnvironmentViewModel
import com.educalab.ninobiologo.ui.viewmodel.SampleCardUiState

@Composable
fun EnvironmentDetailScreen(
    environmentId: String,
    viewModel: EnvironmentViewModel,
    onBack: () -> Unit,
    onSampleClick: (String) -> Unit,
    onExperimentClick: (String) -> Unit,
    onAnalyzerClick: (String) -> Unit,
    onCreatureBuilderClick: (String) -> Unit
) {
    LaunchedEffect(environmentId) { viewModel.load(environmentId) }
    val state by viewModel.uiState.collectAsState()
    val environment = state.environment

    Scaffold(
        topBar = {
            Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") }
                Column {
                    Text(environment?.name ?: "", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(environment?.tagline ?: "", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    ) { padding ->
        if (environment == null) {
            Column(Modifier.fillMaxSize().padding(padding)) { }
            return@Scaffold
        }
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp)) {
            item {
                SectionHeader(
                    "Muestras por explorar",
                    "${state.sampleCards.count { it.state == SampleExplorationState.DESCUBIERTO }}/${state.sampleCards.size} completadas"
                )
            }
            items(state.sampleCards) { card ->
                SampleRow(card = card, onClick = { if (!card.locked) onSampleClick(card.sample.id) })
                Spacer(Modifier.height(10.dp))
            }

            item {
                Spacer(Modifier.height(12.dp))
                SectionHeader("Descubrimientos de la zona", "${state.discoveries.count { it.id in state.discoveredIds }}/${state.discoveries.size} descubiertos")
            }
            item {
                LazyRow {
                    items(state.discoveries) { discovery ->
                        val discovered = discovery.id in state.discoveredIds
                        Column(
                            modifier = Modifier.padding(end = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (discovered) {
                                DiscoveryIllustration(category = discovery.category, iconKey = discovery.iconKey, sizeDp = 56)
                                Text(discovery.name, style = MaterialTheme.typography.labelMedium)
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

            if (state.experiments.isNotEmpty()) {
                item { Spacer(Modifier.height(12.dp)); SectionHeader("Experimentos biológicos") }
                items(state.experiments) { experiment ->
                    AppCard(onClick = { onExperimentClick(experiment.id) }) {
                        Text(experiment.question, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(experiment.description, style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }

            item {
                Spacer(Modifier.height(12.dp))
                AppCard(onClick = { onCreatureBuilderClick(environmentId) }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Science, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Constructor Biológico", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Text("Crea una criatura microscópica adaptada a este ambiente.", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            if (state.challenges.isNotEmpty()) {
                item { Spacer(Modifier.height(12.dp)); SectionHeader("Analizador") }
                items(state.challenges) { challenge ->
                    AppCard(onClick = { onAnalyzerClick(challenge.id) }) {
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
private fun SampleRow(card: SampleCardUiState, onClick: () -> Unit) {
    AppCard(onClick = if (card.locked) null else onClick) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(1f)) {
                Text(card.sample.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(card.sample.origin, style = MaterialTheme.typography.bodyMedium)
                Text(stateLabel(card.state, card.locked), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            }
            if (card.locked) {
                Icon(Icons.Filled.Lock, contentDescription = "Bloqueada", tint = Color.Gray)
            }
        }
    }
}

private fun stateLabel(state: SampleExplorationState, locked: Boolean): String = when {
    locked -> "Bloqueada"
    state == SampleExplorationState.DESCUBIERTO -> "¡Descubierta!"
    state == SampleExplorationState.NUEVO -> "Lista para explorar"
    else -> "En progreso"
}
