package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.model.CollectionItem
import com.educalab.ninobiologo.domain.model.Organism
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class MuseumUiState(
    val loading: Boolean = true,
    val allOrganisms: List<Organism> = emptyList(),
    val discoveredIds: Set<String> = emptySet(),
    val collectionItems: List<CollectionItem> = emptyList(),
    val totalCount: Int = 0,
    val discoveredCount: Int = 0
)

class MuseumViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MuseumUiState())
    val uiState: StateFlow<MuseumUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(repository.observeAllOrganisms(), repository.observeDiscoveries()) { organisms, discoveredIds ->
                organisms to discoveredIds.toSet()
            }.collect { (organisms, discoveredSet) ->
                val items = repository.getCollectionItems()
                _uiState.value = MuseumUiState(
                    loading = false,
                    allOrganisms = organisms,
                    discoveredIds = discoveredSet,
                    collectionItems = items,
                    totalCount = organisms.size,
                    discoveredCount = discoveredSet.size
                )
            }
        }
    }
}
