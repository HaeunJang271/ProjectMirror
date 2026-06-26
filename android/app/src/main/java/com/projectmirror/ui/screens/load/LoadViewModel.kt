package com.projectmirror.ui.screens.load

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectmirror.data.repository.GameStateRepository
import com.projectmirror.domain.model.SaveProgress
import com.projectmirror.domain.model.SaveSlotInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadViewModel @Inject constructor(
    private val gameStateRepository: GameStateRepository,
) : ViewModel() {

    private val _slots = MutableStateFlow<List<SaveSlotInfo>>(emptyList())
    val slots: StateFlow<List<SaveSlotInfo>> = _slots.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _slots.value = gameStateRepository.loadManualSlotInfos()
        }
    }

    fun loadSlot(slot: Int, onReady: (SaveProgress) -> Unit) {
        viewModelScope.launch {
            val progress = gameStateRepository.restoreManualSlot(slot) ?: return@launch
            onReady(progress)
        }
    }
}
