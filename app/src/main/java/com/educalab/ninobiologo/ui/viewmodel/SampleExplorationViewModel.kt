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

/** Fases visibles de la exploración de una muestra. */
enum class ExplorationPhase { PREPARAR, CAZAR, RESULTADO }

data class SampleExplorationUiState(
    val loading: Boolean = true,
    val sample: ScientificSample? = null,
    val environment: MicroscopicEnvironment? = null,
    val discoveries: List<MicroscopeDiscovery> = emptyList(),
    val phase: ExplorationPhase = ExplorationPhase.PREPARAR,
    val caughtIds: Set<String> = emptySet(),
    /** Última criatura atrapada, para mostrar su ficha al vuelo. */
    val lastCaught: MicroscopeDiscovery? = null,
    val newlyUnlockedCollectibles: List<LabCollectible> = emptyList()
)

/**
 * Conduce una muestra por el ciclo Explorar -> Observar -> Descubrir -> Coleccionar. El paso
 * central no es un botón: el niño caza de verdad cada criatura en el microscopio (MicroHuntGame),
 * y solo cuando las atrapa todas se registra el descubrimiento.
 */
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
            _uiState.value = SampleExplorationUiState(
                loading = false,
                sample = sample,
                environment = environment,
                discoveries = discoveries,
                phase = ExplorationPhase.PREPARAR
            )
        }
    }

    /** Empieza la caza: pasa del "preparar la muestra" al microscopio interactivo. */
    fun startHunt() {
        val state = _uiState.value
        val sample = state.sample ?: return
        soundManager.playCorrect()
        _uiState.value = state.copy(phase = ExplorationPhase.CAZAR)
        viewModelScope.launch {
            repository.updateSampleExplorationState(
                sample.id, SampleExplorationState.OBSERVANDO, state.discoveries.size, System.currentTimeMillis()
            )
        }
    }

    /** El niño tocó una criatura en el microscopio: la atrapa y, si eran todas, cierra la muestra. */
    fun catchCreature(discovery: MicroscopeDiscovery) {
        val state = _uiState.value
        if (discovery.id in state.caughtIds) return
        val sample = state.sample ?: return

        val caught = state.caughtIds + discovery.id
        soundManager.playCorrect()
        _uiState.value = state.copy(caughtIds = caught, lastCaught = discovery)

        viewModelScope.launch {
            if (caught.size >= state.discoveries.size && state.discoveries.isNotEmpty()) {
                val newlyUnlocked = repository.completeSample(sample, state.discoveries, System.currentTimeMillis())
                if (newlyUnlocked.isNotEmpty()) soundManager.playUnlock()
                _uiState.value = _uiState.value.copy(
                    phase = ExplorationPhase.RESULTADO,
                    newlyUnlockedCollectibles = newlyUnlocked
                )
            } else {
                repository.updateSampleExplorationState(
                    sample.id, SampleExplorationState.ANALIZANDO, state.discoveries.size, System.currentTimeMillis()
                )
            }
        }
    }
}
