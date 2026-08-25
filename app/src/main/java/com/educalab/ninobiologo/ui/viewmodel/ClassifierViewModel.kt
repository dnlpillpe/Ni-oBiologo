package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.logic.ClassifierEngine
import com.educalab.ninobiologo.domain.model.Badge
import com.educalab.ninobiologo.domain.model.Challenge
import com.educalab.ninobiologo.domain.model.ChallengeAttempt
import com.educalab.ninobiologo.domain.model.Organism
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ClassifierUiState(
    val loading: Boolean = true,
    val challenge: Challenge? = null,
    val organisms: List<Organism> = emptyList(),
    val attempts: List<ClassifierEngine.Attempt> = emptyList(),
    val sessionResult: ClassifierEngine.SessionResult? = null,
    val finished: Boolean = false,
    val attempt: ChallengeAttempt? = null,
    val newlyUnlockedBadges: List<Badge> = emptyList()
)

class ClassifierViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ClassifierUiState())
    val uiState: StateFlow<ClassifierUiState> = _uiState.asStateFlow()
    private val axis = ClassifierEngine.ClassifierAxis.CATEGORIA

    fun load(challengeId: String) {
        viewModelScope.launch {
            val challenge = repository.getChallenge(challengeId) ?: return@launch
            val organisms = repository.getOrganismsByIds(challenge.relatedOrganismIds)
            _uiState.value = ClassifierUiState(loading = false, challenge = challenge, organisms = organisms)
        }
    }

    fun classify(organismId: String, chosenCategory: String) {
        val state = _uiState.value
        val newAttempts = state.attempts + ClassifierEngine.Attempt(organismId, chosenCategory)
        val result = ClassifierEngine.evaluate(state.organisms, newAttempts, axis)
        _uiState.value = state.copy(attempts = newAttempts, sessionResult = result)

        if (newAttempts.size >= state.organisms.size && state.organisms.isNotEmpty()) {
            val challenge = state.challenge ?: return
            viewModelScope.launch {
                val (attempt, badges) = repository.recordChallengeAttempt(
                    challenge, result.correctCount, result.totalCount, System.currentTimeMillis()
                )
                _uiState.value = _uiState.value.copy(finished = true, attempt = attempt, newlyUnlockedBadges = badges)
            }
        }
    }
}
