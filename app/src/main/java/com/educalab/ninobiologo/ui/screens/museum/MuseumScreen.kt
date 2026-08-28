package com.educalab.ninobiologo.ui.screens.museum

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.DiscoveryRarity
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery
import com.educalab.ninobiologo.ui.components.AppCard
import com.educalab.ninobiologo.ui.components.DiscoveryIllustration
import com.educalab.ninobiologo.ui.components.DiscoverySilhouette
import com.educalab.ninobiologo.ui.components.EmptyState
import com.educalab.ninobiologo.ui.components.RarityChip
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.components.SimpleTopBar
import com.educalab.ninobiologo.ui.theme.RarityCommon
import com.educalab.ninobiologo.ui.theme.RarityLegendary
import com.educalab.ninobiologo.ui.theme.RarityRare
import com.educalab.ninobiologo.ui.theme.RarityUncommon
import com.educalab.ninobiologo.ui.viewmodel.MuseumViewModel

/** "Mi Museo de la Vida": colección real de descubrimientos hechos con el microscopio. */
@Composable
fun MuseumScreen(viewModel: MuseumViewModel, onDiscoveryClick: (MicroscopeDiscovery) -> Unit, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(topBar = { SimpleTopBar(title = "Mi Museo de la Vida", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            SectionHeader("Mi Museo de la Vida", "${state.discoveredCount}/${state.totalCount} descubrimientos")
            if (state.allDiscoveries.isEmpty()) {
                EmptyState("Aún no hay nada aquí", "Explora muestras en el laboratorio para empezar tu colección.")
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(vertical = 12.dp)) {
                    items(state.allDiscoveries) { discovery ->
                        val discovered = discovery.id in state.discoveredIds
                        MuseumSlot(discovery = discovery, discovered = discovered, onClick = { if (discovered) onDiscoveryClick(discovery) })
                    }
                }
            }
        }
    }
}

@Composable
private fun MuseumSlot(discovery: MicroscopeDiscovery, discovered: Boolean, onClick: () -> Unit) {
    AppCard(onClick = if (discovered) onClick else null, modifier = Modifier.padding(6.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            if (discovered) {
                DiscoveryIllustration(category = discovery.category, iconKey = discovery.iconKey, sizeDp = 56)
                Text(discovery.name, style = MaterialTheme.typography.labelMedium, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                RarityChip(label = rarityLabel(discovery.rarity), color = rarityColor(discovery.rarity))
            } else {
                DiscoverySilhouette(category = discovery.category, iconKey = discovery.iconKey, sizeDp = 56)
                Text("???", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            }
        }
    }
}

private fun rarityLabel(rarity: DiscoveryRarity): String = rarity.displayName
private fun rarityColor(rarity: DiscoveryRarity): Color = when (rarity) {
    DiscoveryRarity.COMUN -> RarityCommon
    DiscoveryRarity.POCO_COMUN -> RarityUncommon
    DiscoveryRarity.RARO -> RarityRare
    DiscoveryRarity.LEGENDARIO -> RarityLegendary
}
