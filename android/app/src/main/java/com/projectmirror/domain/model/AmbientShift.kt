package com.projectmirror.domain.model

fun chapterBaseAmbient(chapterId: String): Float = when (chapterId) {
    "prologue" -> 0.08f
    "ch01" -> 0f
    "ch02" -> -0.18f
    "ch03" -> -0.05f
    "ch04" -> 0.02f
    else -> 0f
}

fun dispositionAmbientDelta(delta: Map<String, Int>): Float {
    if (delta.isEmpty()) return 0f
    var shift = 0f
    delta.forEach { (key, value) ->
        val weight = value / 100f
        shift += when (key) {
            "empathy", "sacrifice", "courage" -> weight * 0.04f
            "doubt", "order", "self_centered" -> weight * -0.04f
            "curiosity", "freedom" -> weight * 0.02f
            else -> 0f
        }
    }
    return shift
}

fun WorldFlags.totalAmbientShift(chapterId: String): Float =
    (chapterBaseAmbient(chapterId) + ambient.colorTempShift).coerceIn(-1f, 1f)
