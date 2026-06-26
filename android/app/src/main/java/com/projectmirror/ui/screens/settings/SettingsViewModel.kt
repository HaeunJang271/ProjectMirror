package com.projectmirror.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectmirror.data.datastore.PlayerPreferences
import com.projectmirror.domain.model.PlayerSettings
import com.projectmirror.domain.model.SubtitleScale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val playerPreferences: PlayerPreferences,
) : ViewModel() {

    val settings: StateFlow<PlayerSettings> = playerPreferences.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PlayerSettings())

    fun setSubtitleScale(scale: SubtitleScale) {
        viewModelScope.launch {
            val current = settings.value
            playerPreferences.updateSettings(current.copy(subtitleScale = scale))
        }
    }

    fun setHighContrast(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value
            playerPreferences.updateSettings(current.copy(highContrast = enabled))
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value
            playerPreferences.updateSettings(current.copy(soundEnabled = enabled))
        }
    }
}
