package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.model.Badge
import com.educalab.ninobiologo.domain.model.Expedition
import com.educalab.ninobiologo.util.SoundManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExpeditionUiState(
    val loading: Boolean = true,
    val expedition: Expedition? = null,
    val currentStepIndex: Int = 0,
    val finished: Boolean = false,
    val starsEarned: Int = 0,
    val newlyUnlockedBadges: List<Badge> = emptyList()
)

class ExpeditionViewModel(
    private val repository: BiologyRepository,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpeditionUiState())
    val uiState: StateFlow<ExpeditionUiState> = _uiState.asStateFlow()

    fun load(expeditionId: String) {
        viewModelScope.launch {
            val expedition = repository.getExpedition(expeditionId)
            val progress = repository.getExpeditionProgress(expeditionId)
            _uiState.value = ExpeditionUiState(
                loading = false,
                expedition = expedition,
                currentStepIndex = (progress?.stepsCompleted ?: 0).coerceAtMost((expedition?.steps?.size ?: 1) - 1).coerceAtLeast(0)
            )
        }
    }

    /** Avanza un paso. Al llegar al último paso, calcula estrellas y otorga la recompensa real. */
    fun advanceStep(correct: Boolean) {
        val state = _uiState.value
        val expedition = state.expedition ?: return
        soundManager.let { if (correct) it.playCorrect() else it.playIncorrect() }

        val nextIndex = state.currentStepIndex + 1
        viewModelScope.launch {
            repository.updateExpeditionStep(expedition.id, nextIndex, expedition.steps.size, System.currentTimeMillis())
            if (nextIndex >= expedition.steps.size) {
                val stars = if (correct) 3 else 2 // participar siempre otorga al menos 2 estrellas: sin castigos
                val newBadges = repository.completeExpedition(expedition, stars, System.currentTimeMillis())
                if (newBadges.isNotEmpty()) soundManager.playUnlock()
                _uiState.value = state.copy(finished = true, starsEarned = stars, newlyUnlockedBadges = newBadges)
            } else {
                _uiState.value = state.copy(currentStepIndex = nextIndex)
            }
        }
    }
}
