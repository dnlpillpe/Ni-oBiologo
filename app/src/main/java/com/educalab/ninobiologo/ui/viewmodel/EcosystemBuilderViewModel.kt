package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.logic.EcosystemBalanceEngine
import com.educalab.ninobiologo.domain.model.Badge
import com.educalab.ninobiologo.domain.model.EcosystemTemplate
import com.educalab.ninobiologo.domain.model.Organism
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EcosystemBuilderUiState(
    val loading: Boolean = true,
    val template: EcosystemTemplate? = null,
    val availableOrganisms: List<Organism> = emptyList(),
    val producers: Int = 0,
    val herbivores: Int = 0,
    val carnivores: Int = 0,
    val decomposers: Int = 0,
    val balanceResult: EcosystemBalanceEngine.BalanceResult? = null,
    val saved: Boolean = false,
    val newlyUnlockedBadges: List<Badge> = emptyList()
)

class EcosystemBuilderViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(EcosystemBuilderUiState())
    val uiState: StateFlow<EcosystemBuilderUiState> = _uiState.asStateFlow()

    fun load(templateId: String) {
        viewModelScope.launch {
            val template = repository.getEcosystemTemplate(templateId) ?: return@launch
            val organisms = repository.getOrganismsByIds(template.availableOrganismIds)
            _uiState.value = EcosystemBuilderUiState(loading = false, template = template, availableOrganisms = organisms)
            recompute()
        }
    }

    fun setCount(role: String, delta: Int) {
        val state = _uiState.value
        val updated = when (role) {
            "PRODUCTOR" -> state.copy(producers = (state.producers + delta).coerceIn(0, 12))
            "HERBIVORO" -> state.copy(herbivores = (state.herbivores + delta).coerceIn(0, 12))
            "CARNIVORO" -> state.copy(carnivores = (state.carnivores + delta).coerceIn(0, 12))
            "DESCOMPONEDOR" -> state.copy(decomposers = (state.decomposers + delta).coerceIn(0, 12))
            else -> state
        }
        _uiState.value = updated
        recompute()
    }

    private fun recompute() {
        val state = _uiState.value
        val result = EcosystemBalanceEngine.evaluate(state.producers, state.herbivores, state.carnivores, state.decomposers)
        _uiState.value = state.copy(balanceResult = result)
    }

    fun save() {
        val state = _uiState.value
        val template = state.template ?: return
        viewModelScope.launch {
            val (_, badges) = repository.saveEcosystemBuild(
                template.id, state.producers, state.herbivores, state.carnivores, state.decomposers, System.currentTimeMillis()
            )
            _uiState.value = state.copy(saved = true, newlyUnlockedBadges = badges)
        }
    }
}
