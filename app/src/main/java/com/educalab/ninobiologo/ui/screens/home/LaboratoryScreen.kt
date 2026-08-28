package com.educalab.ninobiologo.ui.screens.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.ui.components.AmbientParticles
import com.educalab.ninobiologo.ui.components.BiaExpression
import com.educalab.ninobiologo.ui.components.BiaGuide
import com.educalab.ninobiologo.ui.components.ExplorerAvatar
import com.educalab.ninobiologo.ui.components.XpBar
import com.educalab.ninobiologo.ui.viewmodel.EnvironmentCardUiState
import com.educalab.ninobiologo.ui.viewmodel.LaboratoryViewModel

/**
 * "Laboratorio Vivo": el nuevo hogar de la app. No es un menú de tarjetas, es el espacio personal
 * del joven biólogo — microscopio central, BIA presente, muestras y ambientes por explorar.
 */
@Composable
fun LaboratoryScreen(
    viewModel: LaboratoryViewModel,
    onEnvironmentClick: (String) -> Unit,
    onMicroscopeClick: () -> Unit,
    onCellJourneyClick: () -> Unit,
    onMuseumClick: () -> Unit,
    onJournalClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), MaterialTheme.colorScheme.background)
                    )
                )
        ) {
            AmbientParticles(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary)

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ExplorerAvatar(avatarKey = state.avatarKey, sizeDp = 52)
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Tu laboratorio, ${state.alias}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(state.rank, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                        }
                        Row {
                            IconButton(onClick = onMuseumClick) { Icon(Icons.Filled.Museum, contentDescription = "Mi Museo de la Vida") }
                            IconButton(onClick = onJournalClick) { Icon(Icons.Filled.AutoStories, contentDescription = "Diario del explorador") }
                            IconButton(onClick = onProfileClick) { Icon(Icons.Filled.Person, contentDescription = "Perfil") }
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("${state.totalXp} XP · ${state.discoveriesCount} descubrimientos", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(4.dp))
                    XpBar(progress = state.progressToNextRank)
                    Spacer(Modifier.height(20.dp))
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        BiaGuide(expression = BiaExpression.FELIZ, sizeDp = 64)
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "¿Qué descubriremos hoy? Toca el microscopio para empezar.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                    MicroscopeButton(onClick = onMicroscopeClick)
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        LabToolTile(label = "Viaje a la célula", icon = Icons.Filled.Science, modifier = Modifier.weight(1f), onClick = onCellJourneyClick)
                        LabToolTile(label = "Diario científico", icon = Icons.Filled.AutoStories, modifier = Modifier.weight(1f), onClick = onJournalClick)
                    }
                    Spacer(Modifier.height(24.dp))
                    Text("Ambientes microscópicos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Explora, observa y descubre lo que se esconde en cada muestra.", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(12.dp))
                }

                items(state.environmentCards) { card ->
                    EnvironmentTile(card = card, onClick = { onEnvironmentClick(card.environment.id) })
                    Spacer(Modifier.height(14.dp))
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun MicroscopeButton(onClick: () -> Unit) {
    val infinite = rememberInfiniteTransition(label = "microscope_pulse")
    val pulse by infinite.animateFloat(
        initialValue = 0.97f, targetValue = 1.03f,
        animationSpec = infiniteRepeatable(tween(1600), RepeatMode.Reverse), label = "pulse"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2.4f)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary))
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size((72 * pulse).dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Biotech, contentDescription = "Abrir microscopio", tint = Color.White, modifier = Modifier.size(40.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Microscopio", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Text("Explora tus muestras de cerca", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f))
            }
        }
    }
}

@Composable
private fun LabToolTile(label: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(6.dp))
            Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun EnvironmentTile(card: EnvironmentCardUiState, onClick: () -> Unit) {
    val primary = Color(android.graphics.Color.parseColor(card.environment.primaryColorHex))
    val secondary = Color(android.graphics.Color.parseColor(card.environment.secondaryColorHex))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(secondary)
            .clickable(onClick = onClick)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(primary.copy(alpha = 0.32f), radius = size.height * 0.65f, center = Offset(size.width * 0.9f, size.height * 0.1f))
            drawCircle(primary.copy(alpha = 0.18f), radius = size.height * 0.5f, center = Offset(size.width * 0.05f, size.height))
        }
        Row(modifier = Modifier.fillMaxSize().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(52.dp).clip(CircleShape).background(primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(environmentIcon(card.environment.id), contentDescription = null, tint = Color.White)
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(card.environment.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primary)
                Text(card.environment.tagline, style = MaterialTheme.typography.bodyMedium, color = primary.copy(alpha = 0.85f))
                Spacer(Modifier.height(6.dp))
                Text("${card.discoveredCount}/${card.totalCount} descubrimientos", style = MaterialTheme.typography.labelLarge, color = primary, fontWeight = FontWeight.SemiBold)
            }
            Box(
                modifier = Modifier.clip(CircleShape).background(primary).size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("${card.completionPercent}%", style = MaterialTheme.typography.labelMedium, color = Color.White)
            }
        }
    }
}

private fun environmentIcon(environmentId: String): ImageVector = when (environmentId) {
    "micromundo" -> Icons.Filled.WaterDrop
    "bosque_de_vida" -> Icons.Filled.Forest
    "oceano_profundo" -> Icons.Filled.Waves
    "cuerpo_humano" -> Icons.Filled.MonitorHeart
    "ecosistemas" -> Icons.Filled.Public
    else -> Icons.Filled.Science
}
