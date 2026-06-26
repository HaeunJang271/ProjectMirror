package com.projectmirror.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DispositionWeights(
    val empathy: Int = 0,
    val curiosity: Int = 0,
    val order: Int = 0,
    val freedom: Int = 0,
    val sacrifice: Int = 0,
    val selfCentered: Int = 0,
    val doubt: Int = 0,
    val courage: Int = 0,
) {
    fun applyDelta(delta: Map<String, Int>): DispositionWeights = copy(
        empathy = clamp(empathy + (delta["empathy"] ?: 0)),
        curiosity = clamp(curiosity + (delta["curiosity"] ?: 0)),
        order = clamp(order + (delta["order"] ?: 0)),
        freedom = clamp(freedom + (delta["freedom"] ?: 0)),
        sacrifice = clamp(sacrifice + (delta["sacrifice"] ?: 0)),
        selfCentered = clamp(selfCentered + (delta["self_centered"] ?: 0)),
        doubt = clamp(doubt + (delta["doubt"] ?: 0)),
        courage = clamp(courage + (delta["courage"] ?: 0)),
    )

    private fun clamp(value: Int): Int = value.coerceIn(-100, 100)
}
