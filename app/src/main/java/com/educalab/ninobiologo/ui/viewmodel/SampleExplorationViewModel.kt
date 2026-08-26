package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.model.LabCollectible
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery
import com.educalab.ninobiologo.domain.model.MicroscopicEnvironment
import com.educalab.ninobiologo.domain.model.SampleExplorationState
import com.educalab.ninobiologo.domain.model.ScientificSample
import com.educalab.ninobiologo.util.SoundManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Los 4 pasos reales de la mecánica: Explorar -> Observar -> Experimentar -> Descubrir. */
val SAMPLE_EXPLORATION_STEPS = listOf(
    SampleExplorationState.NUEVO,
    SampleExplorationState.OBSERVANDO,
    SampleExplorationState.ANALIZANDO,
    SampleExplorationState.DESCUBIERTO
)

data class SampleExplorationUiState(
    val loading: Boolean = true,
    val sample: ScientificSample? = null,
    val environment: MicroscopicEnvironment? = null,
    val discoveries: List<MicroscopeDiscovery> = emptyList(),
    val stepIndex: Int = 0,
    val finished: Boolean = false,
    val newlyUnlockedCollectibles: List<LabCollectible> = emptyList()
)

/** Conduce una muestra a través de la mecánica Explorar -> Observar -> Experimentar -> Descubrir -> Coleccionar. */
class SampleExplorationViewModel(
    private val repository: BiologyRepository,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SampleExplorationUiState())
    val uiState: StateFlow<SampleExplorationUiState> = _uiState.asStateFlow()

    fun load(sampleId: String) {
        viewModelScope.launch {
            val sample = repository.getSample(sampleId) ?: return@launch
            val environment = repository.getEnvironment(sample.environmentId)
            val discoveries = repository.getDiscoveriesForSample(sampleId)
            val progress = repository.getSampleExploration(sampleId)
            val startIndex = progress?.let { p -> SAMPLE_EXPLORATION_STEPS.indexOf(p.state).coerceAtLeast(0) } ?: 0
            _uiState.value = SampleExplorationUiState(
                loading = false,
                sample = sample,
                environment = environment,
                discoveries = discoveries,
                stepIndex = startIndex.coerceAtMost(SAMPLE_EXPLORATION_STEPS.lastIndex - 1),
                finished = progress?.state == SampleExplorationState.DESCUBIERTO
            )
        }
    }

    /** Avanza al siguiente paso de la exploración; al llegar a "Descubrir" revela todos los hallazgos reales. */
    fun advance() {
        val state = _uiState.value
        val sample = state.sample ?: return
        soundManager.playCorrect()
        val nextIndex = state.stepIndex + 1
        viewModelScope.launch {
            if (nextIndex >= SAMPLE_EXPLORATION_STEPS.lastIndex) {
                val newlyUnlocked = repository.completeSample(sample, state.discoveries, System.currentTimeMillis())
                if (newlyUnlocked.isNotEmpty()) soundManager.playUnlock()
                _uiState.value = state.copy(stepIndex = SAMPLE_EXPLORATION_STEPS.lastIndex, finished = true, newlyUnlockedCollectibles = newlyUnlocked)
            } else {
                repository.updateSampleExplorationState(sample.id, SAMPLE_EXPLORATION_STEPS[nextIndex], state.discoveries.size, System.currentTimeMillis())
                _uiState.value = state.copy(stepIndex = nextIndex)
            }
        }
    }
}
