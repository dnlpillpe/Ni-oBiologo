package com.educalab.ninobiologo.ui.screens.expedition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.educalab.ninobiologo.domain.model.MissionType
import com.educalab.ninobiologo.ui.components.BiaExpression
import com.educalab.ninobiologo.ui.components.BiaGuide
import com.educalab.ninobiologo.ui.components.PrimaryButton
import com.educalab.ninobiologo.ui.components.XpBar
import com.educalab.ninobiologo.ui.viewmodel.ExpeditionViewModel

@Composable
fun ExpeditionScreen(expeditionId: String, viewModel: ExpeditionViewModel, onFinished: () -> Unit, onBack: () -> Unit) {
    LaunchedEffect(expeditionId) { viewModel.load(expeditionId) }
    val state by viewModel.uiState.collectAsState()
    val expedition = state.expedition

    Scaffold { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            if (expedition == null || state.loading) {
                return@Column
            }
            if (state.finished) {
                ExpeditionResultView(stars = state.starsEarned, newBadgesCount = state.newlyUnlockedBadges.size, onContinue = onFinished)
                return@Column
            }

            val step = expedition.steps.getOrNull(state.currentStepIndex)
            Text(expedition.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(expedition.narrative, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            XpBar(progress = (state.currentStepIndex).toFloat() / expedition.steps.size.toFloat())
            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                BiaGuide(expression = expressionFor(step?.type), sizeDp = 84)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(step?.prompt ?: "", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(6.dp))
                    Text(step?.hint ?: "", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                }
            }

            Spacer(Modifier.weight(1f))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PrimaryButton(text = "Necesito otra pista", onClick = { viewModel.advanceStep(correct = false) }, modifier = Modifier.weight(1f))
                PrimaryButton(text = "¡Lo logré!", onClick = { viewModel.advanceStep(correct = true) }, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ExpeditionResultView(stars: Int, newBadgesCount: Int, onContinue: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        BiaGuide(expression = BiaExpression.CELEBRANDO, sizeDp = 130)
        Spacer(Modifier.height(16.dp))
        Text("¡Expedición completada!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("★".repeat(stars) + "☆".repeat(3 - stars), style = MaterialTheme.typography.displayLarge)
        if (newBadgesCount > 0) {
            Spacer(Modifier.height(8.dp))
            Text("¡Desbloqueaste $newBadgesCount insignia(s) nueva(s)!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.height(24.dp))
        PrimaryButton(text = "Continuar explorando", onClick = onContinue)
    }
}

private fun expressionFor(type: MissionType?): BiaExpression = when (type) {
    MissionType.OBSERVAR -> BiaExpression.OBSERVANDO
    MissionType.COMPARAR -> BiaExpression.INVESTIGANDO
    MissionType.INVESTIGAR -> BiaExpression.INVESTIGANDO
    MissionType.CLASIFICAR -> BiaExpression.OBSERVANDO
    MissionType.MICROSCOPIO -> BiaExpression.SORPRENDIDA
    MissionType.CONSTRUIR -> BiaExpression.FELIZ
    null -> BiaExpression.FELIZ
}
