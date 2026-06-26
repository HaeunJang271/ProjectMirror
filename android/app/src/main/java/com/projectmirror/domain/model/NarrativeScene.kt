package com.projectmirror.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NarrativeScene(
    val id: String,
    val lines: List<NarrativeLine>,
    val nextSceneId: String? = null,
)

@Serializable
data class NarrativeLine(
    val type: String = "narration",
    val speaker: String? = null,
    val text: String,
)

@Serializable
data class PrologueScenesFile(
    val scenes: List<NarrativeScene>,
)
