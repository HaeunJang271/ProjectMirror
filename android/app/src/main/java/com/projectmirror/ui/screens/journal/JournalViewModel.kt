package com.projectmirror.ui.screens.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectmirror.data.local.entity.DialogueLogEntity
import com.projectmirror.data.repository.GameStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class JournalEntry(
    val id: Long,
    val speaker: String?,
    val text: String,
    val isChoice: Boolean,
)

@HiltViewModel
class JournalViewModel @Inject constructor(
    gameStateRepository: GameStateRepository,
) : ViewModel() {

    val entries: StateFlow<List<JournalEntry>> = gameStateRepository.dialogueLog
        .map { logs -> logs.map { it.toJournalEntry() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

private fun DialogueLogEntity.toJournalEntry(): JournalEntry = JournalEntry(
    id = id,
    speaker = speaker,
    text = text,
    isChoice = lineType == "choice",
)
