package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.model.Challenge
import com.educalab.ninobiologo.domain.model.Experiment
import com.educalab.ninobiologo.domain.model.SampleExplorationState
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery
import com.educalab.ninobiologo.domain.model.MicroscopicEnvironment
import com.educalab.ninobiologo.domain.model.ScientificSample
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class SampleCardUiState(val sample: ScientificSample, val state: SampleExplorationState, val locked: Boolean)

data class EnvironmentUiState(
    val loading: Boolean = true,
    val environment: MicroscopicEnvironment? = null,
    val sampleCards: List<SampleCardUiState> = emptyList(),
    val discoveries: List<MicroscopeDiscovery> = emptyList(),
    val discoveredIds: Set<String> = emptySet(),
    val experiments: List<Experiment> = emptyList(),
    val challenges: List<Challenge> = emptyList()
)

/** Pantalla de un ambiente microscópico: lista sus muestras, descubrimientos, experimentos y tareas del Analizador. */
class EnvironmentViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(EnvironmentUiState())
    val uiState: StateFlow<EnvironmentUiState> = _uiState.asStateFlow()

    private var loadedEnvironmentId: String? = null

    fun load(environmentId: String) {
        if (loadedEnvironmentId == environmentId) return
        loadedEnvironmentId = environmentId
        viewModelScope.launch {
            val environment = repository.getEnvironment(environmentId)
            val experiments = repository.observeExperimentsByEnvironment(environmentId).first()
            val challenges = repository.observeChallengesByEnvironment(environmentId).first()

            combine(
                repository.observeSamplesByEnvironment(environmentId),
                repository.observeDiscoveriesByEnvironment(environmentId),
                repository.observeDiscoveriesFound(),
                repository.observeSampleExploration()
            ) { samples, discoveries, discoveredIdsList, explorationList ->
                val discoveredIds = discoveredIdsList.toSet()
                val explorationBySample = explorationList.associateBy { it.sampleId }
                val orderedSamples = samples.sortedBy { it.order }
                val cards = orderedSamples.mapIndexed { index, sample ->
                    val state = explorationBySample[sample.id]?.state ?: SampleExplorationState.NUEVO
                    val previousDiscovered = index == 0 || explorationBySample[orderedSamples[index - 1].id]?.state == SampleExplorationState.DESCUBIERTO
                    SampleCardUiState(sample = sample, state = state, locked = !previousDiscovered)
                }

                EnvironmentUiState(
                    loading = false,
                    environment = environment,
                    sampleCards = cards,
                    discoveries = discoveries,
                    discoveredIds = discoveredIds,
                    experiments = experiments,
                    challenges = challenges
                )
            }.collect { _uiState.value = it }
        }
    }
}
