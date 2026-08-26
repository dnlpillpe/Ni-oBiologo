package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.logic.ExperimentEngine
import com.educalab.ninobiologo.domain.model.Experiment
import com.educalab.ninobiologo.domain.model.LabCollectible
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExperimentUiState(
    val loading: Boolean = true,
    val experiment: Experiment? = null,
    val variableValue: Int = 0,
    val preview: ExperimentEngine.Result? = null,
    val saved: Boolean = false,
    val newlyUnlockedCollectibles: List<LabCollectible> = emptyList()
)

/** Experimentos Biológicos: el niño ajusta una variable real y ve un resultado calculado, no un texto fijo. */
class ExperimentViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ExperimentUiState())
    val uiState: StateFlow<ExperimentUiState> = _uiState.asStateFlow()

    fun load(experimentId: String) {
        viewModelScope.launch {
            val experiment = repository.getExperiment(experimentId) ?: return@launch
            val startValue = (experiment.variableMin + experiment.variableMax) / 2
            _uiState.value = ExperimentUiState(loading = false, experiment = experiment, variableValue = startValue)
            recompute()
        }
    }

    fun setVariable(value: Int) {
        val state = _uiState.value
        val experiment = state.experiment ?: return
        _uiState.value = state.copy(variableValue = value.coerceIn(experiment.variableMin, experiment.variableMax))
        recompute()
    }

    private fun recompute() {
        val state = _uiState.value
        val experiment = state.experiment ?: return
        _uiState.value = state.copy(preview = ExperimentEngine.evaluate(experiment, state.variableValue))
    }

    fun save() {
        val state = _uiState.value
        val experiment = state.experiment ?: return
        viewModelScope.launch {
            val (_, collectibles) = repository.runExperiment(experiment, state.variableValue, System.currentTimeMillis())
            _uiState.value = state.copy(saved = true, newlyUnlockedCollectibles = collectibles)
        }
    }
}
