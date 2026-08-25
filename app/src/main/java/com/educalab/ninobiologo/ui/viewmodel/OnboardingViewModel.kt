package com.educalab.ninobiologo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.domain.logic.Validators
import kotlinx.coroutines.launch

class OnboardingViewModel(private val repository: BiologyRepository) : ViewModel() {

    fun finishOnboarding(alias: String, avatarKey: String) {
        val sanitizedAlias = Validators.sanitizeAlias(alias).ifBlank { "Joven Biólogo" }
        viewModelScope.launch {
            repository.updateAlias(sanitizedAlias)
            repository.updateAvatar(avatarKey)
            repository.completeOnboarding()
        }
    }
}
