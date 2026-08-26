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
    private val axis = ClassifierEngine.ClassifierAxis.CATEGORIA

    fun load(challengeId: String) {
        viewModelScope.launch {
            val challenge = repository.getChallenge(challengeId) ?: return@launch
            val discoveries = repository.getDiscoveriesByIds(challenge.relatedDiscoveryIds)
            _uiState.value = AnalyzerUiState(loading = false, challenge = challenge, discoveries = discoveries)
        }
    }

    fun classify(discoveryId: String, chosenCategory: String) {
        val state = _uiState.value
        val newAttempts = state.attempts + ClassifierEngine.Attempt(discoveryId, chosenCategory)
        val result = ClassifierEngine.evaluate(state.discoveries, newAttempts, axis)
        _uiState.value = state.copy(attempts = newAttempts, sessionResult = result)

        if (newAttempts.size >= state.discoveries.size && state.discoveries.isNotEmpty()) {
            val challenge = state.challenge ?: return
            viewModelScope.launch {
                val (attempt, collectibles) = repository.recordChallengeAttempt(
                    challenge, result.correctCount, result.totalCount, System.currentTimeMillis()
                )
                _uiState.value = _uiState.value.copy(finished = true, attempt = attempt, newlyUnlockedCollectibles = collectibles)
            }
        }
    }
}
