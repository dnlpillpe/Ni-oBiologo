package com.educalab.ninobiologo.ui.screens.museum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.Organism
import com.educalab.ninobiologo.domain.model.OrganismRarity
import com.educalab.ninobiologo.ui.components.AppCard
import com.educalab.ninobiologo.ui.components.EmptyState
import com.educalab.ninobiologo.ui.components.OrganismIllustration
import com.educalab.ninobiologo.ui.components.RarityChip
import com.educalab.ninobiologo.ui.components.SectionHeader
import com.educalab.ninobiologo.ui.components.SimpleTopBar
import com.educalab.ninobiologo.ui.theme.RarityCommon
import com.educalab.ninobiologo.ui.theme.RarityLegendary
import com.educalab.ninobiologo.ui.theme.RarityRare
import com.educalab.ninobiologo.ui.theme.RarityUncommon
import com.educalab.ninobiologo.ui.viewmodel.MuseumViewModel

/** Museo Biológico Personal: colección real de descubrimientos (sección MUSEO BIOLÓGICO). */
@Composable
fun MuseumScreen(viewModel: MuseumViewModel, onOrganismClick: (Organism) -> Unit, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(topBar = { SimpleTopBar(title = "Museo Biológico", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            SectionHeader("Museo Biológico", "${state.discoveredCount}/${state.totalCount} organismos descubiertos")
            if (state.allOrganisms.isEmpty()) {
                EmptyState("Aún no hay nada aquí", "Completa expediciones para empezar tu colección.")
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(vertical = 12.dp)) {
                    items(state.allOrganisms) { organism ->
                        val discovered = organism.id in state.discoveredIds
                        MuseumSlot(organism = organism, discovered = discovered, onClick = { if (discovered) onOrganismClick(organism) })
                    }
                }
            }
        }
    }
}

@Composable
private fun MuseumSlot(organism: Organism, discovered: Boolean, onClick: () -> Unit) {
    AppCard(onClick = if (discovered) onClick else null, modifier = Modifier.padding(6.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            if (discovered) {
                OrganismIllustration(category = organism.category, iconKey = organism.iconKey, sizeDp = 56)
                Text(organism.name, style = MaterialTheme.typography.labelMedium, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                RarityChip(label = rarityLabel(organism.rarity), color = rarityColor(organism.rarity))
            } else {
                Icon(Icons.Filled.Lock, contentDescription = "Sin descubrir", tint = Color.Gray, modifier = Modifier)
                Text("???", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            }
        }
    }
}

private fun rarityLabel(rarity: OrganismRarity): String = rarity.displayName
private fun rarityColor(rarity: OrganismRarity): Color = when (rarity) {
    OrganismRarity.COMUN -> RarityCommon
    OrganismRarity.POCO_COMUN -> RarityUncommon
    OrganismRarity.RARO -> RarityRare
    OrganismRarity.LEGENDARIO -> RarityLegendary
}
