package com.projectmirror.domain.model

data class SaveProgress(
    val chapterId: String,
    val sceneId: String,
)

enum class SubtitleScale(val multiplier: Float) {
    S(0.9f),
    M(1f),
    L(1.15f),
}

data class PlayerSettings(
    val subtitleScale: SubtitleScale = SubtitleScale.M,
    val highContrast: Boolean = false,
)
