package com.projectmirror.ui.screens.narrative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectmirror.audio.AudioCue
import com.projectmirror.audio.AudioDirector
import com.projectmirror.audio.SceneAudioMap
import com.projectmirror.data.repository.GameStateRepository
import com.projectmirror.data.repository.NarrativeRepository
import com.projectmirror.domain.model.ChoiceOption
import com.projectmirror.domain.model.ImplicitChoiceOutcome
import com.projectmirror.domain.model.ImplicitChoiceTrigger
import com.projectmirror.domain.model.NarrativeLine
import com.projectmirror.domain.model.NarrativeScene
import com.projectmirror.domain.model.SceneExit
import com.projectmirror.domain.model.filterByFlags
import com.projectmirror.domain.model.totalAmbientShift
import com.projectmirror.domain.usecase.ApplyChoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class NarrativePhase {
    LINES,
    IMPLICIT_WAIT,
    CHOICES,
    HUB_EXITS,
    CHAPTER_CARD,
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
    val ambientShift: Float = 0f,
) {
    val currentLine: NarrativeLine? = displayLines.getOrNull(lineIndex)
    val hasNextLine: Boolean = lineIndex < displayLines.lastIndex
}

@HiltViewModel
class NarrativeViewModel @Inject constructor(
    private val narrativeRepository: NarrativeRepository,
    private val applyChoiceUseCase: ApplyChoiceUseCase,
    private val gameStateRepository: GameStateRepository,
    private val audioDirector: AudioDirector,
) : ViewModel() {

    private var activeChapterId: String = "prologue"
    private var currentSceneId: String = "p01"

    private var scene: NarrativeScene? = null
    private var afterResultSceneId: String? = null
    private var implicitIdleJob: Job? = null
    private var implicitStartedAt: Long = 0L

    private val _uiState = MutableStateFlow(
        NarrativeUiState(chapterId = activeChapterId, sceneId = currentSceneId),
    )
    val uiState: StateFlow<NarrativeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            gameStateRepository.worldFlags.collect { flags ->
                _uiState.update {
                    it.copy(ambientShift = flags.totalAmbientShift(activeChapterId))
                }
            }
        }
        viewModelScope.launch {
            val progress = gameStateRepository.syncSessionFromStorage()
            loadScene(progress.chapterId, progress.sceneId)
        }
    }

    override fun onCleared() {
        audioDirector.stopAll()
        super.onCleared()
    }

    fun setAudioEnabled(enabled: Boolean) {
        audioDirector.setEnabled(enabled)
        if (enabled) {
            refreshSceneAudio()
        }
    }

    private fun loadScene(chapter: String, sceneId: String) {
        viewModelScope.launch {
            implicitIdleJob?.cancel()
            val loaded = narrativeRepository.loadScene(chapter, sceneId)
            if (loaded == null) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Scene not found: $chapter/$sceneId")
                }
                return@launch
            }
            activeChapterId = chapter
            applyScene(loaded)
        }
    }

    private suspend fun applyScene(loaded: NarrativeScene) {
        val flags = gameStateRepository.worldFlags.value
        val filteredLines = loaded.lines.filterByFlags(flags)
        scene = loaded.copy(lines = filteredLines)
        currentSceneId = loaded.id
        afterResultSceneId = null
        implicitIdleJob?.cancel()
        val phase = when {
            loaded.type == "hub" && filteredLines.isEmpty() && loaded.exits.isNotEmpty() ->
                NarrativePhase.HUB_EXITS
            else -> NarrativePhase.LINES
        }
        _uiState.update {
            it.copy(
                chapterId = activeChapterId,
                sceneId = loaded.id,
                speaker = loaded.speaker,
                displayLines = filteredLines,
                lineIndex = 0,
                choices = loaded.choices,
                exits = loaded.exits,
                phase = phase,
                isLoading = false,
                error = null,
                chapterCardTitle = chapterCardTitle(loaded),
                ambientShift = flags.totalAmbientShift(activeChapterId),
            )
        }
        filteredLines.firstOrNull()?.let { logLine(it) }
        gameStateRepository.autoSave(activeChapterId, loaded.id)
        refreshSceneAudio()
    }

    private fun refreshSceneAudio() {
        viewModelScope.launch {
            val curiosity = gameStateRepository.disposition.first().curiosity
            val profile = SceneAudioMap.profile(activeChapterId, currentSceneId, curiosity)
            audioDirector.onSceneEnter(profile)
        }
    }

    fun advance() {
        val state = _uiState.value
        when (state.phase) {
            NarrativePhase.LINES -> {
                if (state.hasNextLine) {
                    val nextIndex = state.lineIndex + 1
                    state.displayLines.getOrNull(nextIndex)?.let { logLine(it) }
                    _uiState.update { it.copy(lineIndex = nextIndex) }
                    maybeStartImplicitWait(nextIndex)
                } else if (afterResultSceneId != null) {
                    goToScene(afterResultSceneId!!)
                    afterResultSceneId = null
                } else {
                    finishLines()
                }
            }
            NarrativePhase.CHAPTER_CARD -> {
                val next = nextChapter(activeChapterId, state.sceneId)
                if (next != null) {
                    loadScene(next.first, next.second)
                }
            }
            else -> Unit
        }
    }

    fun onImplicitTouch() {
        val trigger = scene?.implicitChoice ?: return
        if (_uiState.value.phase != NarrativePhase.IMPLICIT_WAIT) return
        val elapsed = System.currentTimeMillis() - implicitStartedAt
        if (elapsed <= trigger.touchWindowMs) {
            resolveImplicit(trigger.onTouch, trigger)
        }
    }

    private fun maybeStartImplicitWait(lineIndex: Int) {
        val trigger = scene?.implicitChoice ?: return
        if (lineIndex != trigger.triggerLineIndex) return
        implicitStartedAt = System.currentTimeMillis()
        _uiState.update { it.copy(phase = NarrativePhase.IMPLICIT_WAIT) }
        implicitIdleJob?.cancel()
        implicitIdleJob = viewModelScope.launch {
            delay(trigger.idleWindowMs)
            if (_uiState.value.phase == NarrativePhase.IMPLICIT_WAIT) {
                resolveImplicit(trigger.onIdle, trigger)
            }
        }
    }

    private fun resolveImplicit(outcome: ImplicitChoiceOutcome, trigger: ImplicitChoiceTrigger) {
        viewModelScope.launch {
            implicitIdleJob?.cancel()
            gameStateRepository.applyImplicitChoice(outcome, activeChapterId, currentSceneId)
            val flags = gameStateRepository.worldFlags.value
            val cue = when (flags.f002DoorHandle) {
                "cold" -> AudioCue.DOOR_COLD
                else -> AudioCue.DOOR_WARM
            }
            audioDirector.playCue(cue)
            val reactions = trigger.reactionLines.filterByFlags(flags)
            reactions.firstOrNull()?.let { logLine(it) }
            val prefix = _uiState.value.displayLines.take(trigger.triggerLineIndex + 1)
            _uiState.update {
                it.copy(
                    displayLines = prefix + reactions,
                    lineIndex = prefix.size + reactions.size - 1,
                    phase = NarrativePhase.CHOICES,
                    ambientShift = flags.totalAmbientShift(activeChapterId),
                )
            }
        }
    }

    private fun finishLines() {
        val loaded = scene ?: return
        val trigger = loaded.implicitChoice
        if (trigger != null && _uiState.value.lineIndex == trigger.triggerLineIndex) {
            maybeStartImplicitWait(trigger.triggerLineIndex)
            return
        }
        when {
            loaded.choices.isNotEmpty() ->
                _uiState.update { it.copy(phase = NarrativePhase.CHOICES) }
            loaded.type == "hub" && loaded.exits.isNotEmpty() ->
                _uiState.update { it.copy(phase = NarrativePhase.HUB_EXITS) }
            loaded.chapterComplete ->
                _uiState.update { it.copy(phase = NarrativePhase.CHAPTER_CARD) }
            loaded.nextSceneId != null ->
                goToScene(loaded.nextSceneId)
        }
    }

    fun selectChoice(choice: ChoiceOption) {
        viewModelScope.launch {
            gameStateRepository.logChoice(activeChapterId, currentSceneId, choice.label)
            applyChoiceUseCase(choice, activeChapterId, currentSceneId)
            if (choice.resultLines.isNotEmpty()) {
                afterResultSceneId = choice.nextSceneId
                val filtered = choice.resultLines.filterByFlags(gameStateRepository.worldFlags.value)
                filtered.firstOrNull()?.let { logLine(it) }
                _uiState.update {
                    it.copy(
                        displayLines = filtered,
                        lineIndex = 0,
                        phase = NarrativePhase.LINES,
                        choices = emptyList(),
                    )
                }
            } else {
                goToScene(choice.nextSceneId)
            }
        }
    }

    fun selectExit(exit: SceneExit) {
        viewModelScope.launch {
            goToScene(exit.sceneId)
        }
    }

    private fun goToScene(sceneId: String) {
        if (sceneId == currentSceneId) return
        viewModelScope.launch {
            val loaded = narrativeRepository.loadScene(activeChapterId, sceneId) ?: return@launch
            applyScene(loaded)
        }
    }

    private fun logLine(line: NarrativeLine) {
        viewModelScope.launch {
            val speaker = if (line.type == "dialogue") line.speaker ?: scene?.speaker else null
            gameStateRepository.logDialogue(
                chapterId = activeChapterId,
                sceneId = currentSceneId,
                lineType = line.type,
                speaker = speaker,
                text = line.text,
            )
        }
    }

    private fun chapterCardTitle(loaded: NarrativeScene): String? = when (loaded.id) {
        "p06" -> if (loaded.chapterComplete) "Chapter 1\n첫 반사" else null
        "c01_end" -> if (loaded.chapterComplete) "Chapter 2\n비의 자리" else null
        "c02_end" -> if (loaded.chapterComplete) "Chapter 3\n깃털의 방" else null
        "c03_end" -> if (loaded.chapterComplete) "Chapter 4\n거울의 물" else null
        "c04_end" -> if (loaded.chapterComplete) "Chapter 5\n달빛 복도" else null
        "c05_end" -> if (loaded.chapterComplete) "Chapter 6\n(준비 중)" else null
        else -> null
    }

    private fun nextChapter(chapterId: String, sceneId: String): Pair<String, String>? =
        when (chapterId to sceneId) {
            "prologue" to "p06" -> "ch01" to "c01_hub"
            "ch01" to "c01_end" -> "ch02" to "c02_intro"
            "ch02" to "c02_end" -> "ch03" to "c03_intro"
            "ch03" to "c03_end" -> "ch04" to "c04_intro"
            "ch04" to "c04_end" -> "ch05" to "c05_intro"
            else -> null
        }
}
