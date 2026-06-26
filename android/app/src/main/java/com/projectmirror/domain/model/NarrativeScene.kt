package com.projectmirror.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ChoiceOption(
    val id: String,
    val label: String,
    val nextSceneId: String,
    val dispositionDelta: Map<String, Int> = emptyMap(),
    val foreshadow: Map<String, String> = emptyMap(),
    val flagUpdates: Map<String, String> = emptyMap(),
    val resultLines: List<NarrativeLine> = emptyList(),
)

@Serializable
data class SceneExit(
    val label: String,
    val sceneId: String,
)

@Serializable
data class NarrativeScene(
    val id: String,
    val type: String = "narrative",
    val lines: List<NarrativeLine> = emptyList(),
    val speaker: String? = null,
    val nextSceneId: String? = null,
    val choices: List<ChoiceOption> = emptyList(),
    val exits: List<SceneExit> = emptyList(),
    val chapterComplete: Boolean = false,
    val implicitChoice: ImplicitChoiceTrigger? = null,
)

@Serializable
data class NarrativeLine(
    val type: String = "narration",
    val speaker: String? = null,
    val text: String,
    val requireFlag: FlagCondition? = null,
)

@Serializable
data class NarrativeChapterFile(
    val chapterId: String,
    val scenes: List<NarrativeScene>,
)
