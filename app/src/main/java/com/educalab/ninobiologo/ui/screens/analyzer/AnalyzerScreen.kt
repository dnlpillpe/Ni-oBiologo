package com.educalab.ninobiologo.ui.screens.analyzer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.DiscoveryCategory
import com.educalab.ninobiologo.ui.components.AppCard
import com.educalab.ninobiologo.ui.components.BiaExpression
import com.educalab.ninobiologo.ui.components.BiaGuide
import com.educalab.ninobiologo.ui.components.DiscoveryIllustration
import com.educalab.ninobiologo.ui.components.PrimaryButton
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.viewmodel.AnalyzerViewModel

private val CATEGORY_OPTIONS = DiscoveryCategory.entries

/** El Analizador: herramienta de apoyo para comparar/clasificar los descubrimientos de un ambiente. */
@Composable
fun AnalyzerScreen(challengeId: String, viewModel: AnalyzerViewModel, onFinished: () -> Unit) {
    LaunchedEffect(challengeId) { viewModel.load(challengeId) }
    val state by viewModel.uiState.collectAsState()
    var lastFeedback by remember { mutableStateOf<String?>(null) }

    Scaffold { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            SectionHeader(state.challenge?.title ?: "Analizador", "Identifica la categoría de cada descubrimiento.")

            if (state.finished) {
                val attempt = state.attempt
                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    BiaGuide(expression = BiaExpression.CELEBRANDO, sizeDp = 110)
                    Spacer(Modifier.height(12.dp))
                    Text("¡Análisis completado!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("★".repeat(attempt?.stars ?: 0) + "☆".repeat(3 - (attempt?.stars ?: 0)), style = MaterialTheme.typography.displayLarge)
                    Spacer(Modifier.height(16.dp))
                    PrimaryButton(text = "Continuar", onClick = onFinished)
                }
                return@Column
            }

            val remaining = state.discoveries.filter { discovery -> state.attempts.none { it.discoveryId == discovery.id } }
            val current = remaining.firstOrNull()

            if (current == null) {
                Text("Cargando descubrimientos...", style = MaterialTheme.typography.bodyMedium)
            } else {
                Text("${state.attempts.size + 1} de ${state.discoveries.size}", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(12.dp))
                AppCard {
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        DiscoveryIllustration(category = current.category, iconKey = current.iconKey, sizeDp = 96)
                        Spacer(Modifier.height(8.dp))
                        Text(current.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("¿A qué categoría pertenece?", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                CATEGORY_OPTIONS.forEach { category ->
                    PrimaryButton(
                        text = categoryLabel(category),
                        onClick = {
                            viewModel.classify(current.id, category.name)
                            lastFeedback = state.sessionResult?.results?.lastOrNull()?.explanation
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                }
                lastFeedback?.let {
                    Spacer(Modifier.height(12.dp))
                    Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

private fun categoryLabel(category: DiscoveryCategory): String = when (category) {
    DiscoveryCategory.PLANTA -> "Planta 🌿"
    DiscoveryCategory.ANIMAL -> "Animal 🐾"
    DiscoveryCategory.MICROORGANISMO -> "Microorganismo 🔬"
    DiscoveryCategory.HONGO -> "Hongo 🍄"
}
