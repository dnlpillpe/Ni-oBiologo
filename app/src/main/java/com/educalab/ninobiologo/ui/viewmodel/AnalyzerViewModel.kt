package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.logic.ClassifierEngine
import com.educalab.ninobiologo.domain.model.Challenge
import com.educalab.ninobiologo.domain.model.ChallengeAttempt
import com.educalab.ninobiologo.domain.model.LabCollectible
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AnalyzerUiState(
    val loading: Boolean = true,
    val challenge: Challenge? = null,
    val discoveries: List<MicroscopeDiscovery> = emptyList(),
    val attempts: List<ClassifierEngine.Attempt> = emptyList(),
    val sessionResult: ClassifierEngine.SessionResult? = null,
    val finished: Boolean = false,
    val attempt: ChallengeAttempt? = null,
    val newlyUnlockedCollectibles: List<LabCollectible> = emptyList()
)

/** El Analizador: herramienta de apoyo para comparar/clasificar los descubrimientos de un ambiente. */
class AnalyzerViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyzerUiState())
    val uiState: StateFlow<AnalyzerUiState> = _uiState.asStateFlow()

    fun load(challengeId: String) {
        viewModelScope.launch {
            val challenge = repository.getChallenge(challengeId) ?: return@launch
            val discoveries = repository.getDiscoveriesByIds(challenge.relatedDiscoveryIds)
            _uiState.value = AnalyzerUiState(loading = false, challenge = challenge, discoveries = discoveries)
        }
    }

    /**
     * Registra una respuesta. Cada pregunta se evalúa con SU propio eje (grupo, hábitat, dieta o
     * rareza), porque un mismo desafío puede mezclarlos: no se puede evaluar toda la ronda con
     * un único criterio.
     */
    fun classify(discoveryId: String, chosenValue: String, axis: ClassifierEngine.ClassifierAxis) {
        val state = _uiState.value
        val attempt = ClassifierEngine.Attempt(discoveryId, chosenValue)
        val single = ClassifierEngine.evaluate(state.discoveries, listOf(attempt), axis)
        val newAttempts = state.attempts + attempt
        val allResults = (state.sessionResult?.results ?: emptyList()) + single.results
        val result = ClassifierEngine.SessionResult(allResults, allResults.count { it.correct }, allResults.size)
        _uiState.value = state.copy(attempts = newAttempts, sessionResult = result)

        if (newAttempts.size >= state.discoveries.size && state.discoveries.isNotEmpty()) {
            val challenge = state.challenge ?: return
            viewModelScope.launch {
                val (recorded, collectibles) = repository.recordChallengeAttempt(
                    challenge, result.correctCount, result.totalCount, System.currentTimeMillis()
                )
                _uiState.value = _uiState.value.copy(finished = true, attempt = recorded, newlyUnlockedCollectibles = collectibles)
            }
        }
    }
}
