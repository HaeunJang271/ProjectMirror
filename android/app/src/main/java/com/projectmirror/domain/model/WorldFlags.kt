package com.projectmirror.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorldFlags(
    val f001CrackState: String = "intact",
    val f002DoorHandle: String = "warm",
    val f003SilhouetteDelayMs: Int = 500,
    val f004RinNameFilled: Boolean = false,
    val f005FeatherFound: Boolean = false,
    val rinRecorded: Boolean = false,
    val npcFlags: Map<String, String> = emptyMap(),
    val ambient: AmbientFlags = AmbientFlags(),
)

@Serializable
data class AmbientFlags(
    val colorTempShift: Float = 0f,
)
