package com.educalab.ninobiologo.ui.screens.ecosystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.EcosystemStatus
import com.educalab.ninobiologo.ui.components.AppCard
import com.educalab.ninobiologo.ui.components.PrimaryButton
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.components.XpBar
import com.educalab.ninobiologo.ui.viewmodel.EcosystemBuilderViewModel

/**
 * Constructor de Ecosistemas: el niño ajusta cantidades reales de productores, herbívoros,
 * carnívoros y descomponedores; el resultado se calcula con EcosystemBalanceEngine (motor real,
 * no texto fijo según botón).
 */
@Composable
fun EcosystemBuilderScreen(templateId: String, viewModel: EcosystemBuilderViewModel, onSaved: () -> Unit) {
    LaunchedEffect(templateId) { viewModel.load(templateId) }
    val state by viewModel.uiState.collectAsState()
    val template = state.template
    val result = state.balanceResult

    Scaffold { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            SectionHeader(template?.name ?: "Constructor de Ecosistemas", template?.description)

            RoleCounter("Productores 🌱", state.producers) { viewModel.setCount("PRODUCTOR", it) }
            RoleCounter("Herbívoros 🐇", state.herbivores) { viewModel.setCount("HERBIVORO", it) }
            RoleCounter("Carnívoros 🦊", state.carnivores) { viewModel.setCount("CARNIVORO", it) }
            RoleCounter("Descomponedores 🍄", state.decomposers) { viewModel.setCount("DESCOMPONEDOR", it) }

            Spacer(Modifier.height(16.dp))
            if (result != null) {
                Text("Equilibrio: ${result.score}% — ${statusLabel(result.status)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                XpBar(progress = result.score / 100f)
                Spacer(Modifier.height(12.dp))
                result.feedback.forEach { line ->
                    Text("• $line", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(Modifier.weight(1f))
            if (state.saved) {
                Text("¡Ecosistema guardado en tu bitácora!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                if (state.newlyUnlockedBadges.isNotEmpty()) {
                    Text("Nuevas insignias: ${state.newlyUnlockedBadges.joinToString { it.name }}", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(Modifier.height(8.dp))
                PrimaryButton(text = "Volver a la zona", onClick = onSaved)
            } else {
                PrimaryButton(text = "Guardar ecosistema", onClick = { viewModel.save() })
            }
        }
    }
}

@Composable
private fun RoleCounter(label: String, value: Int, onChange: (Int) -> Unit) {
    AppCard {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onChange(-1) }) { Icon(Icons.Filled.Remove, contentDescription = "Quitar uno") }
                Text("$value", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.size(32.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                IconButton(onClick = { onChange(1) }) { Icon(Icons.Filled.Add, contentDescription = "Agregar uno") }
            }
        }
    }
    Spacer(Modifier.height(8.dp))
}

private fun statusLabel(status: EcosystemStatus): String = status.displayName
