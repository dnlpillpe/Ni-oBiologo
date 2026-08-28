package com.educalab.ninobiologo.ui.screens.analyzer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.logic.ClassifierEngine.ClassifierAxis
import com.educalab.ninobiologo.domain.model.AnalysisTaskType
import com.educalab.ninobiologo.domain.model.DiscoveryCategory
import com.educalab.ninobiologo.domain.model.DiscoveryRarity
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery
import com.educalab.ninobiologo.ui.components.AppCard
import com.educalab.ninobiologo.ui.components.BiaExpression
import com.educalab.ninobiologo.ui.components.BiaGuide
import com.educalab.ninobiologo.ui.components.DiscoveryIllustration
import com.educalab.ninobiologo.ui.components.PrimaryButton
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.viewmodel.AnalyzerViewModel

/**
 * El Analizador. Cada desafío pregunta lo que su consigna promete: agrupar por reino, deducir el
 * hábitat, averiguar la alimentación o estimar la rareza. Las opciones salen de los propios
 * descubrimientos de la zona, así que comparar es parte del reto.
 */
@Composable
fun AnalyzerScreen(challengeId: String, viewModel: AnalyzerViewModel, onFinished: () -> Unit) {
    LaunchedEffect(challengeId) { viewModel.load(challengeId) }
    val state by viewModel.uiState.collectAsState()
    val challenge = state.challenge

    Scaffold { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            SectionHeader(challenge?.title ?: "Analizador", challenge?.instructions)

            if (state.finished) {
                val attempt = state.attempt
                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    BiaGuide(expression = BiaExpression.CELEBRANDO, sizeDp = 110)
                    Spacer(Modifier.height(12.dp))
                    Text("¡Análisis completado!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("★".repeat(attempt?.stars ?: 0) + "☆".repeat(3 - (attempt?.stars ?: 0)), style = MaterialTheme.typography.displayLarge)
                    attempt?.let {
                        Text("${it.correctCount} de ${it.totalCount} correctas", style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(Modifier.height(16.dp))
                    PrimaryButton(text = "Continuar", onClick = onFinished)
                }
                return@Column
            }

            val answered = state.attempts.map { it.discoveryId }.toSet()
            val current = state.discoveries.firstOrNull { it.id !in answered }
            if (current == null) {
                Text("Cargando descubrimientos...", style = MaterialTheme.typography.bodyMedium)
                return@Column
            }

            // El eje viene del tipo de tarea; en MIXTO cada ser vivo trae su propia pregunta.
            val taskType = challenge?.type ?: AnalysisTaskType.CATEGORIA
            val axis = axisFor(taskType, state.attempts.size)

            Column(Modifier.verticalScroll(rememberScrollState())) {
                Text("${state.attempts.size + 1} de ${state.discoveries.size}", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(12.dp))
                AppCard {
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        DiscoveryIllustration(category = current.category, iconKey = current.iconKey, sizeDp = 96)
                        Spacer(Modifier.height(8.dp))
                        Text(current.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(current.scientificName, style = MaterialTheme.typography.labelMedium)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(questionFor(axis), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))

                optionsFor(axis, state.discoveries, current).forEach { (label, value) ->
                    PrimaryButton(
                        text = label,
                        onClick = { viewModel.classify(current.id, value, axis) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                }

                state.sessionResult?.results?.lastOrNull()?.let { last ->
                    Spacer(Modifier.height(12.dp))
                    Text(
                        last.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (last.correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

/** MIXTO rota entre los cuatro ejes para que el repaso final no sea siempre la misma pregunta. */
private fun axisFor(type: AnalysisTaskType, questionIndex: Int): ClassifierAxis = when (type) {
    AnalysisTaskType.CATEGORIA -> ClassifierAxis.CATEGORIA
    AnalysisTaskType.HABITAT -> ClassifierAxis.HABITAT
    AnalysisTaskType.DIETA -> ClassifierAxis.DIETA
    AnalysisTaskType.RAREZA -> ClassifierAxis.RAREZA
    AnalysisTaskType.MIXTO -> listOf(
        ClassifierAxis.CATEGORIA, ClassifierAxis.HABITAT, ClassifierAxis.DIETA, ClassifierAxis.RAREZA
    )[questionIndex % 4]
}

private fun questionFor(axis: ClassifierAxis): String = when (axis) {
    ClassifierAxis.CATEGORIA -> "¿A qué grupo de seres vivos pertenece?"
    ClassifierAxis.HABITAT -> "¿Dónde vive?"
    ClassifierAxis.DIETA -> "¿De qué se alimenta?"
    ClassifierAxis.RAREZA -> "¿Qué tan difícil es de encontrar?"
}

/**
 * Opciones (etiqueta visible, valor a comparar). Para hábitat y dieta se toman de los propios
 * hallazgos de la zona: siempre incluyen la respuesta correcta y hasta tres alternativas reales
 * de sus compañeros de muestra.
 */
private fun optionsFor(
    axis: ClassifierAxis,
    all: List<MicroscopeDiscovery>,
    current: MicroscopeDiscovery
): List<Pair<String, String>> = when (axis) {
    ClassifierAxis.CATEGORIA -> DiscoveryCategory.entries.map { categoryLabel(it) to it.name }
    ClassifierAxis.RAREZA -> DiscoveryRarity.entries.map { rarityLabel(it) to it.name }
    ClassifierAxis.HABITAT -> distractors(all.map { it.habitat }, current.habitat)
    ClassifierAxis.DIETA -> distractors(all.map { it.diet }, current.diet)
}

private fun distractors(values: List<String>, correct: String): List<Pair<String, String>> {
    val others = values.filter { !it.equals(correct, ignoreCase = true) }.distinct().take(3)
    // Orden estable (no aleatorio por recomposición) pero distinto según la respuesta correcta.
    return (others + correct).sortedBy { it.length * 31 + it.hashCode() }.map { it to it }
}

private fun categoryLabel(category: DiscoveryCategory): String = when (category) {
    DiscoveryCategory.PLANTA -> "Planta 🌿"
    DiscoveryCategory.ANIMAL -> "Animal 🐾"
    DiscoveryCategory.MICROORGANISMO -> "Microorganismo 🔬"
    DiscoveryCategory.HONGO -> "Hongo 🍄"
}

private fun rarityLabel(rarity: DiscoveryRarity): String = when (rarity) {
    DiscoveryRarity.COMUN -> "Común · se ve a menudo"
    DiscoveryRarity.POCO_COMUN -> "Poco común"
    DiscoveryRarity.RARO -> "Raro · cuesta encontrarlo"
    DiscoveryRarity.LEGENDARIO -> "Legendario · ¡rarísimo!"
}
