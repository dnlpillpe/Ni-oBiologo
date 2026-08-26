package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.educalab.ninobiologo.AppContainer

/** Factory manual compartida por todos los ViewModel de la app (sin Hilt). */
class NinoBiologoViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when {
            modelClass.isAssignableFrom(LaboratoryViewModel::class.java) -> LaboratoryViewModel(container.repository) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(container.repository) as T
            modelClass.isAssignableFrom(OnboardingViewModel::class.java) -> OnboardingViewModel(container.repository) as T
            modelClass.isAssignableFrom(EnvironmentViewModel::class.java) -> EnvironmentViewModel(container.repository) as T
            modelClass.isAssignableFrom(SampleExplorationViewModel::class.java) -> SampleExplorationViewModel(container.repository, container.soundManager) as T
            modelClass.isAssignableFrom(MicroscopeViewModel::class.java) -> MicroscopeViewModel(container.repository) as T
            modelClass.isAssignableFrom(CellJourneyViewModel::class.java) -> CellJourneyViewModel(container.repository) as T
            modelClass.isAssignableFrom(MuseumViewModel::class.java) -> MuseumViewModel(container.repository) as T
            modelClass.isAssignableFrom(ExperimentViewModel::class.java) -> ExperimentViewModel(container.repository) as T
            modelClass.isAssignableFrom(CreatureBuilderViewModel::class.java) -> CreatureBuilderViewModel(container.repository) as T
            modelClass.isAssignableFrom(AnalyzerViewModel::class.java) -> AnalyzerViewModel(container.repository) as T
            modelClass.isAssignableFrom(JournalViewModel::class.java) -> JournalViewModel(container.repository, container.audioRecorderManager, container.photoStorageManager) as T
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}
