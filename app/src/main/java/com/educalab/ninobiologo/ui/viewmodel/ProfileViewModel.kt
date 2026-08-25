package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.logic.RankEngine
import com.educalab.ninobiologo.domain.logic.Validators
import com.educalab.ninobiologo.domain.model.Badge
import com.educalab.ninobiologo.domain.model.BiologistRank
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class ProfileUiState(
    val alias: String = "",
    val avatarKey: String = "",
    val totalXp: Int = 0,
    val rank: BiologistRank = BiologistRank.EXPLORADOR_DE_VIDA,
    val xpToNextRank: Int = 0,
    val progressToNextRank: Float = 0f,
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val discoveriesCount: Int = 0,
    val badgesUnlocked: List<Badge> = emptyList(),
    val allBadges: List<Badge> = emptyList()
)

class ProfileViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.observeProfile(),
                repository.observeDiscoveries(),
                repository.observeBadges(),
                repository.observeBadgeUnlocks()
            ) { profile, discoveries, allBadges, unlockedIds ->
                val xp = profile?.totalXp ?: 0
                ProfileUiState(
                    alias = profile?.alias ?: "Explorador",
                    avatarKey = profile?.avatarKey ?: "avatar_explorador_1",
                    totalXp = xp,
                    rank = RankEngine.rankFor(xp),
                    xpToNextRank = RankEngine.xpToNextRank(xp),
                    progressToNextRank = RankEngine.progressToNextRank(xp),
                    soundEnabled = profile?.soundEnabled ?: true,
                    hapticsEnabled = profile?.hapticsEnabled ?: true,
                    discoveriesCount = discoveries.size,
                    badgesUnlocked = allBadges.filter { it.id in unlockedIds.toSet() },
                    allBadges = allBadges
                )
            }.collect { _uiState.value = it }
        }
    }

    fun updateAlias(rawAlias: String) {
        val sanitized = Validators.sanitizeAlias(rawAlias)
        if (Validators.isAliasValid(sanitized)) {
            viewModelScope.launch { repository.updateAlias(sanitized) }
        }
    }

    fun updateAvatar(avatarKey: String) = viewModelScope.launch { repository.updateAvatar(avatarKey) }
    fun setSoundEnabled(enabled: Boolean) = viewModelScope.launch { repository.setSoundEnabled(enabled) }
    fun setHapticsEnabled(enabled: Boolean) = viewModelScope.launch { repository.setHapticsEnabled(enabled) }
    fun resetProgress() = viewModelScope.launch { repository.resetProgress() }
}
