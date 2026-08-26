package com.educalab.ninobiologo.ui.screens.experiment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.ExperimentOutcome
import com.educalab.ninobiologo.ui.components.PrimaryButton
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.components.StepperControl
import com.educalab.ninobiologo.ui.viewmodel.ExperimentViewModel

/**
 * Experimentos Biológicos: el niño ajusta una variable real y observa un resultado calculado por
 * ExperimentEngine (no un texto fijo según botón).
 */
@Composable
fun ExperimentScreen(experimentId: String, viewModel: ExperimentViewModel, onSaved: () -> Unit) {
    LaunchedEffect(experimentId) { viewModel.load(experimentId) }
    val state by viewModel.uiState.collectAsState()
    val experiment = state.experiment
    val preview = state.preview

    Scaffold { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            SectionHeader(experiment?.question ?: "Experimento biológico", experiment?.description)

            if (experiment != null) {
                Spacer(Modifier.height(8.dp))
                StepperControl(
                    label = experiment.variableName,
                    value = state.variableValue,
                    unit = experiment.variableUnit,
                    range = experiment.variableMin..experiment.variableMax,
                    onChange = { viewModel.setVariable(it) }
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Rango ideal: ${experiment.idealMin}–${experiment.idealMax} ${experiment.variableUnit}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(16.dp))
            if (preview != null) {
                Text(outcomeLabel(preview.outcome), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(preview.message, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.weight(1f))
            if (state.saved) {
                Text("¡Resultado guardado en tu diario científico!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                if (state.newlyUnlockedCollectibles.isNotEmpty()) {
                    Text("Nuevos coleccionables: ${state.newlyUnlockedCollectibles.joinToString { it.name }}", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(Modifier.height(8.dp))
                PrimaryButton(text = "Volver al ambiente", onClick = onSaved)
            } else {
                PrimaryButton(text = "Registrar experimento", onClick = { viewModel.save() })
            }
        }
    }
}

private fun outcomeLabel(outcome: ExperimentOutcome): String = when (outcome) {
    ExperimentOutcome.SIN_CAMBIOS -> "Sin cambios notables"
    ExperimentOutcome.EFECTO_LEVE -> "Efecto leve"
    ExperimentOutcome.EFECTO_NOTABLE -> "Efecto notable"
    ExperimentOutcome.EFECTO_DRASTICO -> "Efecto drástico"
}
