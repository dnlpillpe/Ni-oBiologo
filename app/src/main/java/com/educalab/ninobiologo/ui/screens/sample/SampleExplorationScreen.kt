package com.educalab.ninobiologo.ui.screens.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery
import com.educalab.ninobiologo.domain.model.SampleExplorationState
import com.educalab.ninobiologo.ui.components.AppCard
import com.educalab.ninobiologo.ui.components.BiaExpression
import com.educalab.ninobiologo.ui.components.BiaGuide
import com.educalab.ninobiologo.ui.components.DiscoveryIllustration
import com.educalab.ninobiologo.ui.components.PrimaryButton
import com.educalab.ninobiologo.ui.components.XpBar
import com.educalab.ninobiologo.ui.viewmodel.SAMPLE_EXPLORATION_STEPS
import com.educalab.ninobiologo.ui.viewmodel.SampleExplorationViewModel

/** Mecánica principal de la app: Explorar -> Observar -> Experimentar -> Descubrir -> Coleccionar. */
@Composable
fun SampleExplorationScreen(sampleId: String, viewModel: SampleExplorationViewModel, onFinished: () -> Unit) {
    LaunchedEffect(sampleId) { viewModel.load(sampleId) }
    val state by viewModel.uiState.collectAsState()
    val sample = state.sample

    Scaffold { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            if (sample == null || state.loading) return@Column

            if (state.finished) {
                DiscoverResultView(
                    discoveries = state.discoveries,
                    newlyUnlockedCount = state.newlyUnlockedCollectibles.size,
                    onContinue = onFinished
                )
                return@Column
            }

            val step = SAMPLE_EXPLORATION_STEPS.getOrNull(state.stepIndex) ?: SampleExplorationState.NUEVO
            Text(sample.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(sample.origin, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            XpBar(progress = state.stepIndex.toFloat() / (SAMPLE_EXPLORATION_STEPS.size - 1).toFloat())
            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                BiaGuide(expression = expressionFor(step), sizeDp = 84)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(titleFor(step), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text(promptFor(step, sample.name), style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(Modifier.weight(1f))
            PrimaryButton(text = actionLabelFor(step), onClick = { viewModel.advance() }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun DiscoverResultView(discoveries: List<MicroscopeDiscovery>, newlyUnlockedCount: Int, onContinue: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(24.dp))
        BiaGuide(expression = BiaExpression.CELEBRANDO, sizeDp = 120)
        Spacer(Modifier.height(12.dp))
        Text("¡Descubrimiento completo!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Coleccionaste ${discoveries.size} hallazgo(s) nuevo(s) para tu museo.", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))
        LazyRow {
            items(discoveries) { discovery ->
                AppCard(modifier = Modifier.width(120.dp).padding(end = 8.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        DiscoveryIllustration(category = discovery.category, iconKey = discovery.iconKey, sizeDp = 64)
                        Spacer(Modifier.height(6.dp))
                        Text(discovery.name, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
        if (newlyUnlockedCount > 0) {
            Spacer(Modifier.height(12.dp))
            Text("¡Desbloqueaste $newlyUnlockedCount coleccionable(s) nuevo(s)!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.height(24.dp))
        PrimaryButton(text = "Volver al ambiente", onClick = onContinue)
    }
}

private fun titleFor(step: SampleExplorationState): String = when (step) {
    SampleExplorationState.NUEVO -> "Explorar"
    SampleExplorationState.OBSERVANDO -> "Observar"
    SampleExplorationState.ANALIZANDO -> "Experimentar"
    SampleExplorationState.DESCUBIERTO -> "Descubrir"
}

private fun promptFor(step: SampleExplorationState, sampleName: String): String = when (step) {
    SampleExplorationState.NUEVO -> "Recogiste \"$sampleName\". Antes de nada, obsérvala con calma: ¿qué formas o colores distingues a simple vista?"
    SampleExplorationState.OBSERVANDO -> "Acerca el microscopio. Algo se mueve entre las partículas de la muestra... hay más de lo que parece."
    SampleExplorationState.ANALIZANDO -> "Usa tus herramientas de laboratorio para comparar lo que ves con lo que ya conoces. Estás a punto de descubrir algo nuevo."
    SampleExplorationState.DESCUBIERTO -> "¡Descubrimiento listo!"
}

private fun actionLabelFor(step: SampleExplorationState): String = when (step) {
    SampleExplorationState.NUEVO -> "Observar de cerca"
    SampleExplorationState.OBSERVANDO -> "Experimentar"
    SampleExplorationState.ANALIZANDO -> "¡Descubrir!"
    SampleExplorationState.DESCUBIERTO -> "Continuar"
}

private fun expressionFor(step: SampleExplorationState): BiaExpression = when (step) {
    SampleExplorationState.NUEVO -> BiaExpression.OBSERVANDO
    SampleExplorationState.OBSERVANDO -> BiaExpression.SORPRENDIDA
    SampleExplorationState.ANALIZANDO -> BiaExpression.INVESTIGANDO
    SampleExplorationState.DESCUBIERTO -> BiaExpression.CELEBRANDO
}
