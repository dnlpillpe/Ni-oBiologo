package com.educalab.ninobiologo.ui.screens.sample

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery
import com.educalab.ninobiologo.ui.components.AppCard
import com.educalab.ninobiologo.ui.components.BiaExpression
import com.educalab.ninobiologo.ui.components.BiaGuide
import com.educalab.ninobiologo.ui.components.DiscoveryIllustration
import com.educalab.ninobiologo.ui.components.HuntProgress
import com.educalab.ninobiologo.ui.components.MicroHuntGame
import com.educalab.ninobiologo.ui.components.PrimaryButton
import com.educalab.ninobiologo.ui.components.SampleIllustration
import com.educalab.ninobiologo.ui.viewmodel.ExplorationPhase
import com.educalab.ninobiologo.ui.viewmodel.SampleExplorationViewModel

/**
 * Mecánica principal: preparar la muestra -> cazar las criaturas en el microscopio -> coleccionar.
 * El paso central es un mini-juego real, no un botón de "continuar".
 */
@Composable
fun SampleExplorationScreen(sampleId: String, viewModel: SampleExplorationViewModel, onFinished: () -> Unit) {
    LaunchedEffect(sampleId) { viewModel.load(sampleId) }
    val state by viewModel.uiState.collectAsState()
    val sample = state.sample

    Scaffold { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            if (sample == null || state.loading) return@Column

            val envColor = state.environment?.primaryColorHex?.let { Color(android.graphics.Color.parseColor(it)) }
                ?: MaterialTheme.colorScheme.primary

            when (state.phase) {
                ExplorationPhase.PREPARAR -> {
                    Text(sample.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(sample.origin, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(24.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        SampleIllustration(iconKey = sample.iconKey, tint = envColor, sizeDp = 150)
                    }
                    Spacer(Modifier.height(24.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BiaGuide(expression = BiaExpression.OBSERVANDO, sizeDp = 76)
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "Puse tu muestra bajo el microscopio. Hay ${state.discoveries.size} seres vivos moviéndose ahí dentro… ¡atrápalos todos tocándolos!",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    PrimaryButton(text = "¡Mirar por el microscopio!", onClick = { viewModel.startHunt() }, modifier = Modifier.fillMaxWidth())
                }

                ExplorationPhase.CAZAR -> {
                    Text("Atrapa a los seres vivos", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${state.caughtIds.size} de ${state.discoveries.size} atrapados",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    HuntProgress(caught = state.caughtIds.size, total = state.discoveries.size)
                    Spacer(Modifier.height(16.dp))

                    MicroHuntGame(
                        discoveries = state.discoveries,
                        caughtIds = state.caughtIds,
                        onCatch = { viewModel.catchCreature(it) }
                    )

                    Spacer(Modifier.height(16.dp))
                    AnimatedVisibility(visible = state.lastCaught != null) {
                        state.lastCaught?.let { caught ->
                            AppCard {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    DiscoveryIllustration(category = caught.category, iconKey = caught.iconKey, sizeDp = 56)
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text("¡Atrapaste ${caught.name}!", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        Text(caught.curiosity, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }

                ExplorationPhase.RESULTADO -> {
                    DiscoverResultView(
                        discoveries = state.discoveries,
                        newlyUnlockedCount = state.newlyUnlockedCollectibles.size,
                        onContinue = onFinished
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscoverResultView(discoveries: List<MicroscopeDiscovery>, newlyUnlockedCount: Int, onContinue: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(16.dp))
        BiaGuide(expression = BiaExpression.CELEBRANDO, sizeDp = 120)
        Spacer(Modifier.height(12.dp))
        Text("¡Muestra resuelta!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "Atrapaste ${discoveries.size} ser(es) vivo(s) y ya están en tu museo.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        LazyRow {
            items(discoveries) { discovery ->
                AppCard(modifier = Modifier.width(130.dp).padding(end = 8.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        DiscoveryIllustration(category = discovery.category, iconKey = discovery.iconKey, sizeDp = 64)
                        Spacer(Modifier.height(6.dp))
                        Text(discovery.name, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
                    }
                }
            }
        }
        if (newlyUnlockedCount > 0) {
            Spacer(Modifier.height(12.dp))
            Text(
                "¡Desbloqueaste $newlyUnlockedCount coleccionable(s) nuevo(s)!",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.weight(1f))
        PrimaryButton(text = "Volver al ambiente", onClick = onContinue, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
    }
}
