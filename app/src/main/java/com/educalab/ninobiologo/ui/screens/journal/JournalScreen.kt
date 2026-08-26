package com.educalab.ninobiologo.ui.screens.journal

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.educalab.ninobiologo.domain.model.DiscoveryJournalEntry
import com.educalab.ninobiologo.domain.model.JournalEntryType
import com.educalab.ninobiologo.ui.components.AppCard
import com.educalab.ninobiologo.ui.components.EmptyState
import com.educalab.ninobiologo.ui.components.PrimaryButton
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.components.SimpleTopBar
import com.educalab.ninobiologo.ui.viewmodel.JournalViewModel
import com.educalab.ninobiologo.util.DateUtils

/**
 * Diario del Explorador: fotos (cámara real, sin reconocimiento de especies) y notas de voz
 * (MediaRecorder real). Si el permiso se deniega, se ofrece la alternativa de nota escrita en
 * vez de cerrar la función (sección PRIVACIDAD del prompt maestro).
 */
@Composable
fun JournalScreen(viewModel: JournalViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var noteText by remember { mutableStateOf("") }
    var cameraDenied by remember { mutableStateOf(false) }
    var micDenied by remember { mutableStateOf(false) }
    var pendingPhotoPath by remember { mutableStateOf<String?>(null) }

    val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        val path = pendingPhotoPath
        if (success && path != null) {
            viewModel.addPhotoNote("Foto del explorador", path, null)
        }
    }
    fun launchCameraCapture() {
        val file = viewModel.newPhotoFile()
        pendingPhotoPath = file.absolutePath
        val uri = viewModel.uriForPhotoFile(file)
        takePicture.launch(uri)
    }
    val cameraPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            cameraDenied = false
            launchCameraCapture()
        } else {
            cameraDenied = true
        }
    }
    val micPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            micDenied = false
            viewModel.startRecording()
        } else {
            micDenied = true
        }
    }

    Scaffold(topBar = { SimpleTopBar(title = "Diario del Explorador", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            SectionHeader("Diario del Explorador", "Registra lo que descubres al explorar tus muestras.")

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = {
                    val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    if (granted) {
                        launchCameraCapture()
                    } else {
                        cameraPermission.launch(Manifest.permission.CAMERA)
                    }
                }) { Icon(Icons.Filled.CameraAlt, contentDescription = "Tomar foto") }

                IconButton(onClick = {
                    val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                    if (granted) {
                        if (state.isRecording) viewModel.stopRecordingAndSave("Nota de voz", null) else viewModel.startRecording()
                    } else {
                        micPermission.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }) { Icon(Icons.Filled.Mic, contentDescription = if (state.isRecording) "Detener grabación" else "Grabar nota de voz") }
            }

            if (cameraDenied) {
                Text("No hay permiso de cámara. Puedes escribir una nota en su lugar.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
            }
            if (micDenied) {
                Text("No hay permiso de micrófono. Puedes escribir una nota en su lugar.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
            }
            if (state.isRecording) {
                Text("Grabando... toca el micrófono de nuevo para guardar.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = noteText,
                onValueChange = { if (it.length <= 500) noteText = it },
                placeholder = { Text("Escribe una observación...") },
                modifier = Modifier.fillMaxWidth()
            )
            PrimaryButton(
                text = "Guardar nota",
                onClick = { viewModel.addTextNote("Nota del biólogo", noteText, null); noteText = "" },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                enabled = noteText.isNotBlank()
            )

            Spacer(Modifier.height(12.dp))
            if (state.entries.isEmpty()) {
                EmptyState("Tu diario está vacío", "Registra tu primera observación durante una expedición.")
            } else {
                LazyColumn {
                    items(state.entries) { entry ->
                        JournalEntryCard(entry = entry, isPlaying = state.playingEntryId == entry.id, onPlay = { viewModel.playEntry(entry) }, onDelete = { viewModel.deleteEntry(entry) })
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun JournalEntryCard(entry: DiscoveryJournalEntry, isPlaying: Boolean, onPlay: () -> Unit, onDelete: () -> Unit) {
    AppCard {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(1f)) {
                Text(entry.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(DateUtils.formatShort(entry.createdAtEpochMillis), style = MaterialTheme.typography.labelMedium)
                if (entry.type == JournalEntryType.TEXTO && entry.note.isNotBlank()) {
                    Text(entry.note, style = MaterialTheme.typography.bodyMedium)
                }
            }
            if (entry.type == JournalEntryType.AUDIO) {
                IconButton(onClick = onPlay) { Icon(Icons.Filled.PlayArrow, contentDescription = if (isPlaying) "Reproduciendo" else "Reproducir") }
            }
            IconButton(onClick = onDelete) { Icon(Icons.Filled.Delete, contentDescription = "Eliminar") }
        }
    }
}
