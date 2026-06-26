package com.projectmirror.ui.screens.save

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectmirror.data.repository.GameStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveViewModel @Inject constructor(
    private val gameStateRepository: GameStateRepository,
) : ViewModel() {

    private val _savedSlot = MutableStateFlow<Int?>(null)
    val savedSlot: StateFlow<Int?> = _savedSlot.asStateFlow()

    fun saveToSlot(slot: Int) {
        viewModelScope.launch {
            gameStateRepository.saveToManualSlot(slot)
            _savedSlot.value = slot
        }
    }

    fun clearSavedMessage() {
        _savedSlot.value = null
    }
}
