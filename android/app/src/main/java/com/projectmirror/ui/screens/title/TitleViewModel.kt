package com.projectmirror.ui.screens.title

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectmirror.data.repository.GameStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TitleViewModel @Inject constructor(
    private val gameStateRepository: GameStateRepository,
) : ViewModel() {

    fun startNewGame(onReady: () -> Unit) {
        viewModelScope.launch {
            gameStateRepository.resetForNewGame()
            onReady()
        }
    }
}
