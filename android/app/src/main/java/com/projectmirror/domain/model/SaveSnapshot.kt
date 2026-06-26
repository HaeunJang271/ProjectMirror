package com.projectmirror.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SaveSnapshot(
    val worldFlags: WorldFlags = WorldFlags(),
    val disposition: DispositionWeights = DispositionWeights(),
)

data class SaveSlotInfo(
    val slot: Int,
    val chapterId: String,
    val sceneId: String,
    val updatedAt: Long,
) {
    val label: String get() = chapterLabel(chapterId)

    companion object {
        fun chapterLabel(chapterId: String): String = when (chapterId) {
            "prologue" -> "프롤로그"
            "ch01" -> "Chapter 1 · 첫 반사"
            "ch02" -> "Chapter 2 · 비의 자리"
            "ch03" -> "Chapter 3 · 깃털의 방"
            "ch04" -> "Chapter 4 · 거울의 물"
            else -> chapterId
        }
    }
}
