package com.projectmirror.ui.screens.prologue

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectmirror.data.repository.NarrativeRepository
import com.projectmirror.domain.model.NarrativeLine
import com.projectmirror.domain.model.NarrativeScene
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PrologueUiState(
    val sceneId: String = "p01",
    val lines: List<NarrativeLine> = emptyList(),
    val lineIndex: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null,
) {
    val currentLine: NarrativeLine? = lines.getOrNull(lineIndex)
    val hasNextLine: Boolean = lineIndex < lines.lastIndex
    val nextSceneId: String? = null
}

@HiltViewModel
class PrologueViewModel @Inject constructor(
    private val narrativeRepository: NarrativeRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val sceneId: String = savedStateHandle.get<String>("sceneId") ?: "p01"
    private var scene: NarrativeScene? = null

    private val _uiState = MutableStateFlow(PrologueUiState(sceneId = sceneId))
    val uiState: StateFlow<PrologueUiState> = _uiState.asStateFlow()

    init {
        loadScene()
    }

    private fun loadScene() {
        viewModelScope.launch {
            val loaded = narrativeRepository.loadPrologueScene(sceneId)
            if (loaded == null) {
                _uiState.update { it.copy(isLoading = false, error = "Scene not found: $sceneId") }
                return@launch
            }
            scene = loaded
            _uiState.update {
                it.copy(
                    lines = loaded.lines,
                    lineIndex = 0,
                    isLoading = false,
                    error = null,
                )
            }
        }
    }

    fun advance() {
        _uiState.update { state ->
            if (state.hasNextLine) {
                state.copy(lineIndex = state.lineIndex + 1)
            } else {
                state
            }
        }
    }

    fun nextSceneId(): String? = scene?.nextSceneId
}
