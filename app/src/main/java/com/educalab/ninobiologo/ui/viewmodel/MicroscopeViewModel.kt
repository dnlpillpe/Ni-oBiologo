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

data class MicroscopeUiState(
    val loading: Boolean = true,
    val cellModels: List<CellModel> = emptyList(),
    val selectedIndex: Int = 0,
    val explorationState: MicroscopeEngine.ExplorationState? = null,
    val completionPercent: Int = 0,
    val isComplete: Boolean = false
)

class MicroscopeViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MicroscopeUiState())
    val uiState: StateFlow<MicroscopeUiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            val models = repository.getCellModels()
            if (models.isEmpty()) {
                _uiState.value = MicroscopeUiState(loading = false)
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
        val exploration = MicroscopeEngine.newState(model)
        _uiState.value = MicroscopeUiState(
            loading = false,
            cellModels = models,
            selectedIndex = safeIndex,
            explorationState = exploration,
            completionPercent = MicroscopeEngine.completionPercent(model, exploration),
            isComplete = false
        )
    }

    fun revealStructure(structureId: String) {
        val state = _uiState.value
        val model = state.cellModels.getOrNull(state.selectedIndex) ?: return
        val exploration = (state.explorationState ?: MicroscopeEngine.newState(model)).reveal(structureId)
        _uiState.value = state.copy(
            explorationState = exploration,
            completionPercent = MicroscopeEngine.completionPercent(model, exploration),
            isComplete = MicroscopeEngine.isComplete(model, exploration)
        )
    }
}
