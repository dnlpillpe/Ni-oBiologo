package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.logic.CreatureAdaptationEngine
import com.educalab.ninobiologo.domain.model.CreaturePartCategory
import com.educalab.ninobiologo.domain.model.CreaturePartOption
import com.educalab.ninobiologo.domain.model.LabCollectible
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreatureBuilderUiState(
    val loading: Boolean = true,
    val targetEnvironmentId: String = "",
    val targetEnvironmentName: String = "",
    val optionsByCategory: Map<CreaturePartCategory, List<CreaturePartOption>> = emptyMap(),
    val selections: Map<CreaturePartCategory, CreaturePartOption> = emptyMap(),
    val creatureName: String = "",
    val preview: CreatureAdaptationEngine.Result? = null,
    val saved: Boolean = false,
    val newlyUnlockedCollectibles: List<LabCollectible> = emptyList()
)

/** Constructor Biológico: el niño combina forma, movimiento, alimentación y adaptación para crear una criatura. */
class CreatureBuilderViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatureBuilderUiState())
    val uiState: StateFlow<CreatureBuilderUiState> = _uiState.asStateFlow()

    fun load(environmentId: String) {
        viewModelScope.launch {
            val environment = repository.getEnvironment(environmentId)
            val options = repository.getCreaturePartOptions().groupBy { it.category }
            val defaultSelections = options.mapNotNull { (category, list) -> list.firstOrNull()?.let { category to it } }.toMap()
            _uiState.value = CreatureBuilderUiState(
                loading = false,
                targetEnvironmentId = environmentId,
                targetEnvironmentName = environment?.name ?: "",
                optionsByCategory = options,
                selections = defaultSelections,
                creatureName = "Mi criatura"
            )
            recompute()
        }
    }

    fun selectOption(option: CreaturePartOption) {
        val state = _uiState.value
        _uiState.value = state.copy(selections = state.selections + (option.category to option))
        recompute()
    }

    fun setName(name: String) {
        _uiState.value = _uiState.value.copy(creatureName = name.take(24))
    }

    private fun recompute() {
        val state = _uiState.value
        val result = CreatureAdaptationEngine.evaluate(state.selections.values.toList(), state.targetEnvironmentId)
        _uiState.value = state.copy(preview = result)
    }

    fun save() {
        val state = _uiState.value
        viewModelScope.launch {
            val (_, collectibles) = repository.saveCreature(
                state.creatureName.ifBlank { "Mi criatura" },
                state.selections.values.toList(),
                state.targetEnvironmentId,
                System.currentTimeMillis()
            )
            _uiState.value = state.copy(saved = true, newlyUnlockedCollectibles = collectibles)
        }
    }
}
