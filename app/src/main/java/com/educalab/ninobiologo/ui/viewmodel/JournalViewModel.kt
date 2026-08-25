package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.logic.Validators
import com.educalab.ninobiologo.domain.model.JournalEntry
import com.educalab.ninobiologo.domain.model.JournalEntryType
import com.educalab.ninobiologo.util.AudioRecorderManager
import com.educalab.ninobiologo.util.PhotoStorageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

data class JournalUiState(
    val entries: List<JournalEntry> = emptyList(),
    val isRecording: Boolean = false,
    val playingEntryId: Long? = null
)

class JournalViewModel(
    private val repository: BiologyRepository,
    private val audioRecorderManager: AudioRecorderManager,
    private val photoStorageManager: PhotoStorageManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeJournalEntries().collect { entries ->
                _uiState.value = _uiState.value.copy(entries = entries)
            }
        }
    }

    fun addTextNote(title: String, note: String, biomeId: String?) {
        val sanitized = Validators.sanitizeJournalNote(note)
        viewModelScope.launch {
            repository.addJournalEntry(JournalEntryType.TEXTO, title.ifBlank { "Nota del biólogo" }, sanitized, null, biomeId, System.currentTimeMillis())
        }
    }

    fun addPhotoNote(title: String, filePath: String, biomeId: String?) {
        viewModelScope.launch {
            repository.addJournalEntry(JournalEntryType.FOTO, title.ifBlank { "Foto del explorador" }, "", filePath, biomeId, System.currentTimeMillis())
        }
    }

    fun newPhotoFile(): File = photoStorageManager.createNewPhotoFile()
    fun uriForPhotoFile(file: File): android.net.Uri = photoStorageManager.uriForFile(file)

    fun startRecording() {
        val result = audioRecorderManager.startRecording()
        _uiState.value = _uiState.value.copy(isRecording = result.isSuccess)
    }

    fun stopRecordingAndSave(title: String, biomeId: String?) {
        val result = audioRecorderManager.stopRecording()
        _uiState.value = _uiState.value.copy(isRecording = false)
        result.onSuccess { file ->
            viewModelScope.launch {
                repository.addJournalEntry(JournalEntryType.AUDIO, title.ifBlank { "Nota de voz" }, "", file.absolutePath, biomeId, System.currentTimeMillis())
            }
        }
    }

    fun cancelRecording() {
        audioRecorderManager.cancelRecording()
        _uiState.value = _uiState.value.copy(isRecording = false)
    }

    fun playEntry(entry: JournalEntry) {
        val path = entry.filePath ?: return
        _uiState.value = _uiState.value.copy(playingEntryId = entry.id)
        audioRecorderManager.playFile(path) {
            _uiState.value = _uiState.value.copy(playingEntryId = null)
        }
    }

    fun deleteEntry(entry: JournalEntry) {
        entry.filePath?.let {
            if (entry.type == JournalEntryType.AUDIO) audioRecorderManager.deleteFile(it) else photoStorageManager.deletePhoto(it)
        }
        viewModelScope.launch { repository.deleteJournalEntry(entry.id) }
    }
}
