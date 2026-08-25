package com.educalab.ninobiologo.domain.logic

import com.educalab.ninobiologo.domain.model.CellModel

/**
 * Estado del Microscopio Virtual (ZONA 1). El niño va tocando estructuras de la célula; el motor
 * calcula el progreso real de exploración y determina cuándo la célula queda "completamente
 * observada" para desbloquear la ficha en el museo.
 */
object MicroscopeEngine {

    data class ExplorationState(
        val cellModelId: String,
        val revealedStructureIds: Set<String>
    ) {
        fun reveal(structureId: String): ExplorationState =
            copy(revealedStructureIds = revealedStructureIds + structureId)
    }

    fun newState(cellModel: CellModel): ExplorationState = ExplorationState(cellModel.id, emptySet())

    fun completionPercent(cellModel: CellModel, state: ExplorationState): Int {
        if (cellModel.structures.isEmpty()) return 0
        val revealedCount = cellModel.structures.count { it.id in state.revealedStructureIds }
        return ((revealedCount.toFloat() / cellModel.structures.size.toFloat()) * 100f).toInt().coerceIn(0, 100)
    }

    fun isComplete(cellModel: CellModel, state: ExplorationState): Boolean =
        cellModel.structures.isNotEmpty() && cellModel.structures.all { it.id in state.revealedStructureIds }

    fun nextUndiscoveredHint(cellModel: CellModel, state: ExplorationState): String? =
        cellModel.structures.firstOrNull { it.id !in state.revealedStructureIds }?.name
}
