package com.educalab.ninobiologo.ui.screens.museum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ninobiologo.domain.model.Organism
import com.educalab.ninobiologo.ui.components.OrganismIllustration
import com.educalab.ninobiologo.ui.components.RarityChip

@Composable
fun OrganismDetailScreen(organism: Organism, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") }
                Text(organism.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            OrganismIllustration(category = organism.category, iconKey = organism.iconKey, sizeDp = 140)
            Spacer(Modifier.height(8.dp))
            Text(organism.scientificName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(4.dp))
            RarityChip(label = organism.rarity.displayName, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(16.dp))
            InfoRow("Hábitat", organism.habitat)
            InfoRow("Alimentación", organism.diet)
            Spacer(Modifier.height(12.dp))
            Text("Características", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            organism.characteristics.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
            Spacer(Modifier.height(12.dp))
            Text("Dato curioso", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(organism.funFact, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}
