package com.projectmirror.ui.screens.title

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectmirror.data.repository.GameStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TitleUiState(
    val hasProgress: Boolean = false,
    val progressLabel: String? = null,
)

@HiltViewModel
class TitleViewModel @Inject constructor(
    private val gameStateRepository: GameStateRepository,
) : ViewModel() {

    val uiState: StateFlow<TitleUiState> = gameStateRepository.hasProgress
        .map { has ->
            TitleUiState(
                hasProgress = has,
                progressLabel = if (has) "이어하기" else null,
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TitleUiState())

    fun startNewGame(onReady: () -> Unit) {
        viewModelScope.launch {
            gameStateRepository.resetForNewGame()
            onReady()
        }
    }

    fun continueGame(onReady: () -> Unit) {
        viewModelScope.launch {
            gameStateRepository.restoreAutoSave() ?: return@launch
            onReady()
        }
    }
}
