package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.model.Biome
import com.educalab.ninobiologo.domain.model.Challenge
import com.educalab.ninobiologo.domain.model.EcosystemTemplate
import com.educalab.ninobiologo.domain.model.Expedition
import com.educalab.ninobiologo.domain.model.ModuleState
import com.educalab.ninobiologo.domain.model.Organism
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class ExpeditionCardUiState(val expedition: Expedition, val state: ModuleState, val bestStars: Int)

data class ZoneUiState(
    val loading: Boolean = true,
    val biome: Biome? = null,
    val expeditionCards: List<ExpeditionCardUiState> = emptyList(),
    val organisms: List<Organism> = emptyList(),
    val discoveredIds: Set<String> = emptySet(),
    val ecosystemTemplates: List<EcosystemTemplate> = emptyList(),
    val challenges: List<Challenge> = emptyList()
)

class ZoneViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ZoneUiState())
    val uiState: StateFlow<ZoneUiState> = _uiState.asStateFlow()

    private var loadedBiomeId: String? = null

    fun load(biomeId: String) {
        if (loadedBiomeId == biomeId) return
        loadedBiomeId = biomeId
        viewModelScope.launch {
            val biome = repository.getBiome(biomeId)
            val expeditions = repository.getExpeditionsByBiome(biomeId)
            val ecosystemTemplates = repository.getEcosystemTemplatesByBiome(biomeId)
            val challenges = repository.getChallengesByBiome(biomeId)

            combine(
                repository.observeOrganismsByBiome(biomeId),
                repository.observeDiscoveries(),
                repository.observeExpeditionProgress()
            ) { organisms, discoveredIds, progressList ->
                val discoveredSet = discoveredIds.toSet()
                val progressByExpedition = progressList.associateBy { it.expeditionId }
                fun isCompleted(expeditionId: String): Boolean {
                    val p = progressByExpedition[expeditionId] ?: return false
                    return p.stepsCompleted >= p.totalSteps
                }
                val cards = expeditions.mapIndexed { index, expedition ->
                    val progress = progressByExpedition[expedition.id]
                    val previousCompleted = index == 0 || isCompleted(expeditions[index - 1].id)
                    val state = when {
                        progress != null && isCompleted(expedition.id) && progress.bestStars >= 3 -> ModuleState.DOMINADO
                        progress != null && isCompleted(expedition.id) -> ModuleState.COMPLETADO
                        progress != null && progress.stepsCompleted > 0 -> ModuleState.INICIADO
                        previousCompleted -> ModuleState.DISPONIBLE
                        else -> ModuleState.BLOQUEADO
                    }
                    ExpeditionCardUiState(expedition, state, progress?.bestStars ?: 0)
                }
                ZoneUiState(
                    loading = false,
                    biome = biome,
                    expeditionCards = cards,
                    organisms = organisms,
                    discoveredIds = discoveredSet,
                    ecosystemTemplates = ecosystemTemplates,
                    challenges = challenges
                )
            }.collect { _uiState.value = it }
        }
    }
}
