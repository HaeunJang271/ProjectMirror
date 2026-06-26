package com.projectmirror.ui.screens.narrative

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectmirror.data.repository.GameStateRepository
import com.projectmirror.data.repository.NarrativeRepository
import com.projectmirror.domain.model.ChoiceOption
import com.projectmirror.domain.model.NarrativeLine
import com.projectmirror.domain.model.NarrativeScene
import com.projectmirror.domain.model.SceneExit
import com.projectmirror.domain.usecase.ApplyChoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class NarrativePhase {
    LINES,
    CHOICES,
    HUB_EXITS,
    CHAPTER_CARD,
}

sealed class NarrativeNavEvent {
    data class Scene(val chapterId: String, val sceneId: String) : NarrativeNavEvent()
}

data class NarrativeUiState(
    val chapterId: String = "prologue",
    val sceneId: String = "p01",
    val speaker: String? = null,
    val displayLines: List<NarrativeLine> = emptyList(),
    val lineIndex: Int = 0,
    val choices: List<ChoiceOption> = emptyList(),
    val exits: List<SceneExit> = emptyList(),
    val phase: NarrativePhase = NarrativePhase.LINES,
    val isLoading: Boolean = true,
    val error: String? = null,
    val chapterCardTitle: String? = null,
) {
    val currentLine: NarrativeLine? = displayLines.getOrNull(lineIndex)
    val hasNextLine: Boolean = lineIndex < displayLines.lastIndex
}

@HiltViewModel
class NarrativeViewModel @Inject constructor(
    private val narrativeRepository: NarrativeRepository,
    private val applyChoiceUseCase: ApplyChoiceUseCase,
    private val gameStateRepository: GameStateRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val chapterId: String = savedStateHandle.get<String>("chapterId") ?: "prologue"
    private val sceneId: String = savedStateHandle.get<String>("sceneId") ?: "p01"

    private var scene: NarrativeScene? = null
    private var afterResultSceneId: String? = null

    private val _uiState = MutableStateFlow(NarrativeUiState(chapterId = chapterId, sceneId = sceneId))
    val uiState: StateFlow<NarrativeUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableStateFlow<NarrativeNavEvent?>(null)
    val navEvent: StateFlow<NarrativeNavEvent?> = _navEvent.asStateFlow()

    private var isNavigating = false

    init {
        loadScene()
    }

    fun consumeNavEvent() {
        _navEvent.value = null
    }

    private fun loadScene() {
        viewModelScope.launch {
            val loaded = narrativeRepository.loadScene(chapterId, sceneId)
            if (loaded == null) {
                _uiState.update { it.copy(isLoading = false, error = "Scene not found: $chapterId/$sceneId") }
                return@launch
            }
            scene = loaded
            afterResultSceneId = null
            val phase = when {
                loaded.type == "hub" && loaded.lines.isEmpty() && loaded.exits.isNotEmpty() ->
                    NarrativePhase.HUB_EXITS
                else -> NarrativePhase.LINES
            }
            _uiState.update {
                it.copy(
                    sceneId = loaded.id,
                    speaker = loaded.speaker,
                    displayLines = loaded.lines,
                    lineIndex = 0,
                    choices = loaded.choices,
                    exits = loaded.exits,
                    phase = phase,
                    isLoading = false,
                    error = null,
                    chapterCardTitle = chapterCardTitle(loaded),
                )
            }
            gameStateRepository.autoSave(chapterId, loaded.id)
        }
    }

    fun advance() {
        val state = _uiState.value
        when (state.phase) {
            NarrativePhase.LINES -> {
                if (state.hasNextLine) {
                    _uiState.update { it.copy(lineIndex = it.lineIndex + 1) }
                } else if (afterResultSceneId != null) {
                    navigateTo(afterResultSceneId!!)
                    afterResultSceneId = null
                } else {
                    finishLines()
                }
            }
            NarrativePhase.CHAPTER_CARD -> {
                val next = nextChapter(state.sceneId)
                if (next != null) {
                    navigateTo(next.first, next.second)
                }
            }
            else -> Unit
        }
    }

    private fun finishLines() {
        val loaded = scene ?: return
        when {
            loaded.choices.isNotEmpty() ->
                _uiState.update { it.copy(phase = NarrativePhase.CHOICES) }
            loaded.type == "hub" && loaded.exits.isNotEmpty() ->
                _uiState.update { it.copy(phase = NarrativePhase.HUB_EXITS) }
            loaded.chapterComplete ->
                _uiState.update { it.copy(phase = NarrativePhase.CHAPTER_CARD) }
            loaded.nextSceneId != null ->
                navigateTo(loaded.nextSceneId)
        }
    }

    fun selectChoice(choice: ChoiceOption) {
        viewModelScope.launch {
            applyChoiceUseCase(choice, chapterId, sceneId)
            if (choice.resultLines.isNotEmpty()) {
                afterResultSceneId = choice.nextSceneId
                _uiState.update {
                    it.copy(
                        displayLines = choice.resultLines,
                        lineIndex = 0,
                        phase = NarrativePhase.LINES,
                        choices = emptyList(),
                    )
                }
            } else {
                navigateTo(choice.nextSceneId)
            }
        }
    }

    fun selectExit(exit: SceneExit) {
        viewModelScope.launch {
            gameStateRepository.autoSave(chapterId, exit.sceneId)
            navigateTo(exit.sceneId)
        }
    }

    private fun navigateTo(sceneId: String) = navigateTo(chapterId, sceneId)

    private fun navigateTo(chapter: String, scene: String) {
        if (isNavigating) return
        isNavigating = true
        _navEvent.value = NarrativeNavEvent.Scene(chapter, scene)
    }

    private fun chapterCardTitle(loaded: NarrativeScene): String? = when (loaded.id) {
        "p06" -> if (loaded.chapterComplete) "Chapter 1\n첫 반사" else null
        "c01_end" -> if (loaded.chapterComplete) "Chapter 2\n비의 자리" else null
        else -> null
    }

    private fun nextChapter(sceneId: String): Pair<String, String>? = when (chapterId to sceneId) {
        "prologue" to "p06" -> "ch01" to "c01_hub"
        "ch01" to "c01_end" -> "ch02" to "c02_intro"
        else -> null
    }
}
