package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery
import com.educalab.ninobiologo.domain.model.MuseumItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class MuseumUiState(
    val loading: Boolean = true,
    val allDiscoveries: List<MicroscopeDiscovery> = emptyList(),
    val discoveredIds: Set<String> = emptySet(),
    val museumItems: List<MuseumItem> = emptyList(),
    val totalCount: Int = 0,
    val discoveredCount: Int = 0
)

/** "Mi Museo de la Vida": colección real de descubrimientos hechos con el microscopio. */
class MuseumViewModel(private val repository: BiologyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MuseumUiState())
    val uiState: StateFlow<MuseumUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(repository.observeAllDiscoveries(), repository.observeDiscoveriesFound()) { discoveries, discoveredIds ->
                discoveries to discoveredIds.toSet()
            }.collect { (discoveries, discoveredSet) ->
                val items = repository.getMuseumItems()
                _uiState.value = MuseumUiState(
                    loading = false,
                    allDiscoveries = discoveries,
                    discoveredIds = discoveredSet,
                    museumItems = items,
                    totalCount = discoveries.size,
                    discoveredCount = discoveredSet.size
                )
            }
        }
    }
}
