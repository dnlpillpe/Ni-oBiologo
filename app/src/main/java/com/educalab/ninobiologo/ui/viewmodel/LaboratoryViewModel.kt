package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.logic.CollectionEngine
import com.educalab.ninobiologo.domain.logic.RankEngine
import com.educalab.ninobiologo.domain.model.MicroscopicEnvironment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class EnvironmentCardUiState(val environment: MicroscopicEnvironment, val completionPercent: Int, val discoveredCount: Int, val totalCount: Int)

data class LaboratoryUiState(
    val loading: Boolean = true,
    val alias: String = "",
    val avatarKey: String = "",
    val totalXp: Int = 0,
    val rank: String = "",
    val progressToNextRank: Float = 0f,
    val discoveriesCount: Int = 0,
    val environmentCards: List<EnvironmentCardUiState> = emptyList()
)

/** Estado y datos de la pantalla "Laboratorio Vivo" (el nuevo hogar de la app). */
class LaboratoryViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LaboratoryUiState())
    val uiState: StateFlow<LaboratoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.observeProfile(),
                repository.observeEnvironments(),
                repository.observeDiscoveriesFound()
            ) { profile, environments, discoveredIds -> Triple(profile, environments, discoveredIds) }
                .collect { (profile, environments, discoveredIds) ->
                    val discoveredSet = discoveredIds.toSet()
                    val cards = environments.map { environment ->
                        val envDiscoveries = repository.observeDiscoveriesByEnvironment(environment.id).first()
                        EnvironmentCardUiState(
                            environment = environment,
                            completionPercent = CollectionEngine.environmentCompletionPercent(envDiscoveries, discoveredSet),
                            discoveredCount = envDiscoveries.count { it.id in discoveredSet },
                            totalCount = envDiscoveries.size
                        )
                    }
                    _uiState.value = LaboratoryUiState(
                        loading = false,
                        alias = profile?.alias ?: "Explorador",
                        avatarKey = profile?.avatarKey ?: "avatar_explorador_1",
                        totalXp = profile?.totalXp ?: 0,
                        rank = (profile?.rank ?: RankEngine.rankFor(0)).displayName,
                        progressToNextRank = RankEngine.progressToNextRank(profile?.totalXp ?: 0),
                        discoveriesCount = discoveredSet.size,
                        environmentCards = cards.sortedBy { it.environment.order }
                    )
                }
        }
    }
}
