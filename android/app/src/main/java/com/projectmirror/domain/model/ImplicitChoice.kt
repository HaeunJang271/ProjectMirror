package com.projectmirror.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ImplicitChoiceOutcome(
    val id: String,
    val dispositionDelta: Map<String, Int> = emptyMap(),
    val foreshadow: Map<String, String> = emptyMap(),
    val flagUpdates: Map<String, String> = emptyMap(),
)

@Serializable
data class ImplicitChoiceTrigger(
    val triggerLineIndex: Int,
    val touchWindowMs: Long = 5_000,
    val idleWindowMs: Long = 15_000,
    val onTouch: ImplicitChoiceOutcome,
    val onIdle: ImplicitChoiceOutcome,
    val reactionLines: List<NarrativeLine> = emptyList(),
)
