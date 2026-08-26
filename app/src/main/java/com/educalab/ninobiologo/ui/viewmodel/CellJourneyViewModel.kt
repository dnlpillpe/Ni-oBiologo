package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.logic.MicroscopeEngine
import com.educalab.ninobiologo.domain.model.CellModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CellJourneyUiState(
    val loading: Boolean = true,
    val cellModels: List<CellModel> = emptyList(),
    val selectedIndex: Int = 0,
    val stopIndex: Int = 0,
    val visited: MicroscopeEngine.ExplorationState? = null,
    val completionPercent: Int = 0,
    val isComplete: Boolean = false
)

/** "Viaje al interior de la célula": recorrido guiado, parada por parada, por cada estructura. */
class CellJourneyViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CellJourneyUiState())
    val uiState: StateFlow<CellJourneyUiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            val models = repository.getCellModels()
            if (models.isEmpty()) {
                _uiState.value = CellJourneyUiState(loading = false)
                return@launch
            }
            selectCell(models, 0)
        }
    }

    fun selectCell(index: Int) {
        val models = _uiState.value.cellModels
        if (models.isNotEmpty()) selectCell(models, index)
    }

    private fun selectCell(models: List<CellModel>, index: Int) {
        val safeIndex = index.coerceIn(0, models.lastIndex)
        val model = models[safeIndex]
        val visited = MicroscopeEngine.newState(model)
        _uiState.value = CellJourneyUiState(
            loading = false,
            cellModels = models,
            selectedIndex = safeIndex,
            stopIndex = 0,
            visited = visited.reveal(model.structures.first().id),
            completionPercent = MicroscopeEngine.completionPercent(model, visited),
            isComplete = false
        )
    }

    fun nextStop() {
        val state = _uiState.value
        val model = state.cellModels.getOrNull(state.selectedIndex) ?: return
        val nextIndex = (state.stopIndex + 1).coerceAtMost(model.structures.lastIndex)
        val structure = model.structures[nextIndex]
        val visited = (state.visited ?: MicroscopeEngine.newState(model)).reveal(structure.id)
        _uiState.value = state.copy(
            stopIndex = nextIndex,
            visited = visited,
            completionPercent = MicroscopeEngine.completionPercent(model, visited),
            isComplete = MicroscopeEngine.isComplete(model, visited)
        )
    }

    fun previousStop() {
        val state = _uiState.value
        _uiState.value = state.copy(stopIndex = (state.stopIndex - 1).coerceAtLeast(0))
    }
}
