package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.logic.CollectionEngine
import com.educalab.ninobiologo.domain.logic.RankEngine
import com.educalab.ninobiologo.domain.model.Biome
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class BiomeCardUiState(val biome: Biome, val completionPercent: Int, val expeditionsCompleted: Int, val expeditionsTotal: Int)

data class ExpeditionMapUiState(
    val loading: Boolean = true,
    val alias: String = "",
    val avatarKey: String = "",
    val totalXp: Int = 0,
    val rank: String = "",
    val progressToNextRank: Float = 0f,
    val biomeCards: List<BiomeCardUiState> = emptyList()
)

class ExpeditionMapViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpeditionMapUiState())
    val uiState: StateFlow<ExpeditionMapUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.observeProfile(),
                repository.observeBiomes(),
                repository.observeDiscoveries(),
                repository.observeExpeditionProgress()
            ) { profile, biomes, discoveredIds, expeditionProgress ->
                Quad(profile, biomes, discoveredIds, expeditionProgress)
            }.collect { (profile, biomes, discoveredIds, expeditionProgress) ->
                val discoveredSet = discoveredIds.toSet()
                val cards = biomes.map { biome ->
                    val biomeOrganisms = repository.observeOrganismsByBiome(biome.id).first()
                    val expeditions = repository.getExpeditionsByBiome(biome.id)
                    val completedInBiome = expeditionProgress.count { p -> expeditions.any { it.id == p.expeditionId } && p.stepsCompleted >= p.totalSteps }
                    BiomeCardUiState(
                        biome = biome,
                        completionPercent = CollectionEngine.biomeCompletionPercent(biomeOrganisms, discoveredSet),
                        expeditionsCompleted = completedInBiome,
                        expeditionsTotal = expeditions.size
                    )
                }
                _uiState.value = ExpeditionMapUiState(
                    loading = false,
                    alias = profile?.alias ?: "Explorador",
                    avatarKey = profile?.avatarKey ?: "avatar_explorador_1",
                    totalXp = profile?.totalXp ?: 0,
                    rank = (profile?.rank ?: RankEngine.rankFor(0)).displayName,
                    progressToNextRank = RankEngine.progressToNextRank(profile?.totalXp ?: 0),
                    biomeCards = cards.sortedBy { it.biome.order }
                )
            }
        }
    }

    private data class Quad<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
}
