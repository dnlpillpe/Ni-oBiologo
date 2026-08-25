package com.educalab.ninobiologo.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.ui.components.AppCard
import com.educalab.ninobiologo.ui.components.BadgeChip
import com.educalab.ninobiologo.ui.components.BiaExpression
import com.educalab.ninobiologo.ui.components.BiaGuide
import com.educalab.ninobiologo.ui.components.ConfirmationDialog
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.components.SimpleTopBar
import com.educalab.ninobiologo.ui.components.XpBar
import com.educalab.ninobiologo.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(topBar = { SimpleTopBar(title = "Tu perfil", onBack = onBack) }) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BiaGuide(expression = BiaExpression.FELIZ, sizeDp = 72)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(state.alias, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text(state.rank.displayName, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("${state.totalXp} XP · faltan ${state.xpToNextRank} XP para el siguiente rango", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                XpBar(progress = state.progressToNextRank)
                Spacer(Modifier.height(20.dp))
                SectionHeader("Insignias", "${state.badgesUnlocked.size}/${state.allBadges.size} desbloqueadas")
            }
            item {
                LazyVerticalGrid(columns = GridCells.Fixed(4), modifier = Modifier.height(((state.allBadges.size / 4 + 1) * 88).dp)) {
                    items(state.allBadges) { badge ->
                        BadgeChip(name = badge.name, unlocked = state.badgesUnlocked.any { it.id == badge.id })
                    }
                }
            }
            item {
                Spacer(Modifier.height(20.dp))
                SectionHeader("Configuración")
                AppCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Sonido", style = MaterialTheme.typography.titleMedium)
                        Switch(checked = state.soundEnabled, onCheckedChange = { viewModel.setSoundEnabled(it) })
                    }
                }
                Spacer(Modifier.height(8.dp))
                AppCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Vibración", style = MaterialTheme.typography.titleMedium)
                        Switch(checked = state.hapticsEnabled, onCheckedChange = { viewModel.setHapticsEnabled(it) })
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    "Reiniciar progreso",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                AppCard(onClick = { showResetDialog = true }) {
                    Text("Borra descubrimientos, expediciones, insignias y XP. Esta acción no se puede deshacer.", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (showResetDialog) {
        ConfirmationDialog(
            title = "¿Reiniciar todo tu progreso?",
            message = "Perderás tus descubrimientos, expediciones completadas, insignias y XP. Tu alias y avatar se mantendrán.",
            confirmLabel = "Sí, reiniciar",
            onConfirm = { viewModel.resetProgress(); showResetDialog = false },
            onDismiss = { showResetDialog = false }
        )
    }
}
