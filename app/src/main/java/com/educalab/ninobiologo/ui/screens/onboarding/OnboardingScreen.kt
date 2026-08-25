package com.educalab.ninobiologo.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.logic.Validators
import com.educalab.ninobiologo.ui.components.BiaExpression
import com.educalab.ninobiologo.ui.components.BiaGuide
import com.educalab.ninobiologo.ui.components.PrimaryButton
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.viewmodel.OnboardingViewModel

private val AVAILABLE_AVATARS = (1..8).map { "avatar_explorador_$it" }

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel, onFinished: () -> Unit) {
    var step by remember { mutableStateOf(0) }
    var alias by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf(AVAILABLE_AVATARS.first()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        AnimatedContent(targetState = step, label = "onboarding_step") { current ->
            when (current) {
                0 -> WelcomeStep()
                1 -> ProfileStep(
                    alias = alias, onAliasChange = { alias = it },
                    selectedAvatar = selectedAvatar, onAvatarSelected = { selectedAvatar = it }
                )
                else -> PrivacyStep()
            }
        }
        Spacer(Modifier.weight(1f))
        Row(Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            if (step > 0) {
                PrimaryButton(text = "Atrás", onClick = { step -= 1 }, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
            }
            PrimaryButton(
                text = if (step < 2) "Continuar" else "¡Comenzar expedición!",
                enabled = step != 1 || Validators.isAliasValid(alias.ifBlank { "x" }),
                onClick = {
                    if (step < 2) {
                        step += 1
                    } else {
                        viewModel.finishOnboarding(alias, selectedAvatar)
                        onFinished()
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun WelcomeStep() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BiaGuide(expression = BiaExpression.FELIZ, sizeDp = 140)
        Spacer(Modifier.height(16.dp))
        Text("¡Hola, futuro biólogo!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "Soy BIA, tu asistente bióloga. Juntos exploraremos el Micromundo, el Bosque de Vida, el Océano Profundo, el Cuerpo Humano y los Ecosistemas para descubrir la vida del planeta.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun ProfileStep(alias: String, onAliasChange: (String) -> Unit, selectedAvatar: String, onAvatarSelected: (String) -> Unit) {
    Column {
        SectionHeader("Elige tu alias de explorador", "No necesitas tu nombre real: usa un apodo científico.")
        OutlinedTextField(
            value = alias,
            onValueChange = { if (it.length <= 18) onAliasChange(it) },
            singleLine = true,
            placeholder = { Text("Ej: Bio Ana") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))
        Text("Elige tu avatar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(4), modifier = Modifier.height(180.dp)) {
            items(AVAILABLE_AVATARS) { avatarKey ->
                AvatarOption(avatarKey = avatarKey, selected = avatarKey == selectedAvatar, onClick = { onAvatarSelected(avatarKey) })
            }
        }
    }
}

@Composable
private fun AvatarOption(avatarKey: String, selected: Boolean, onClick: () -> Unit) {
    val index = avatarKey.last().digitToIntOrNull() ?: 1
    val colors = listOf(
        MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.primaryContainer
    )
    Card(
        modifier = Modifier.padding(6.dp).size(64.dp).clip(CircleShape),
        onClick = onClick,
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = if (selected) colors[index % colors.size] else colors[index % colors.size].copy(alpha = 0.35f)
        )
    ) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("B$index", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PrivacyStep() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BiaGuide(expression = BiaExpression.INVESTIGANDO, sizeDp = 110)
        Spacer(Modifier.height(16.dp))
        Text("Tu privacidad importa", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(
            "NiñoBiólogo funciona sin conexión a Internet. Todo tu progreso se guarda solo en este dispositivo. Si usas la cámara o el micrófono en el Diario del Explorador, te lo pediremos solo cuando lo necesites, y podrás seguir jugando aunque no lo permitas.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
