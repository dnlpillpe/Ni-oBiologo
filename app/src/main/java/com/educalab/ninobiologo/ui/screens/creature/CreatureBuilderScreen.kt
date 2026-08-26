package com.educalab.ninobiologo.ui.screens.creature

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.CreaturePartCategory
import com.educalab.ninobiologo.domain.model.CreaturePartOption
import com.educalab.ninobiologo.ui.components.AppCard
import com.educalab.ninobiologo.ui.components.BiaExpression
import com.educalab.ninobiologo.ui.components.BiaGuide
import com.educalab.ninobiologo.ui.components.PrimaryButton
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.components.XpBar
import com.educalab.ninobiologo.ui.viewmodel.CreatureBuilderViewModel

/** Constructor Biológico: combina forma, movimiento, alimentación y adaptación para crear una criatura. */
@Composable
fun CreatureBuilderScreen(environmentId: String, viewModel: CreatureBuilderViewModel, onSaved: () -> Unit) {
    LaunchedEffect(environmentId) { viewModel.load(environmentId) }
    val state by viewModel.uiState.collectAsState()
    val preview = state.preview

    Scaffold { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            item {
                SectionHeader("Constructor Biológico", "Combina características para crear una criatura adaptada a ${state.targetEnvironmentName}.")
                OutlinedTextField(
                    value = state.creatureName,
                    onValueChange = { viewModel.setName(it) },
                    singleLine = true,
                    label = { Text("Nombre de tu criatura") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
            }

            CreaturePartCategory.entries.forEach { category ->
                val options = state.optionsByCategory[category].orEmpty()
                if (options.isNotEmpty()) {
                    item {
                        Text(categoryLabel(category), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        LazyRow {
                            items(options) { option ->
                                PartOptionChip(
                                    option = option,
                                    selected = state.selections[category]?.id == option.id,
                                    onClick = { viewModel.selectOption(option) }
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }

            item {
                if (preview != null) {
                    Text("Adaptación al ambiente: ${preview.fitScore}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    XpBar(progress = preview.fitScore / 100f)
                    Spacer(Modifier.height(8.dp))
                    Text(preview.message, style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(Modifier.height(16.dp))
                if (state.saved) {
                    BiaGuide(expression = BiaExpression.CELEBRANDO, sizeDp = 72)
                    Text("¡Criatura guardada en tu colección!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    if (state.newlyUnlockedCollectibles.isNotEmpty()) {
                        Text("Nuevos coleccionables: ${state.newlyUnlockedCollectibles.joinToString { it.name }}", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(Modifier.height(8.dp))
                    PrimaryButton(text = "Volver al ambiente", onClick = onSaved)
                } else {
                    PrimaryButton(text = "Guardar criatura", onClick = { viewModel.save() }, modifier = Modifier.fillMaxWidth())
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun PartOptionChip(option: CreaturePartOption, selected: Boolean, onClick: () -> Unit) {
    AppCard(
        onClick = onClick,
        modifier = Modifier.width(220.dp).padding(end = 10.dp)
    ) {
        Text(option.name, style = MaterialTheme.typography.titleMedium, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal, color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
        Text(option.description, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun categoryLabel(category: CreaturePartCategory): String = when (category) {
    CreaturePartCategory.FORMA -> "Forma 🔵"
    CreaturePartCategory.MOVIMIENTO -> "Movimiento 🌊"
    CreaturePartCategory.ALIMENTACION -> "Alimentación 🍃"
    CreaturePartCategory.ADAPTACION -> "Adaptación 🛡️"
}
