package com.projectmirror.audio

enum class AudioCue {
    DOOR_WARM,
    DOOR_COLD,
    AMBIENT_HUM,
    AMBIENT_RAIN,
    AMBIENT_PAD,
    BIRD_DISTANT,
}

enum class AmbientType {
    NONE,
    HUM,
    RAIN,
    PAD,
}

data class SceneAudioProfile(
    val ambient: AmbientType = AmbientType.NONE,
    val oneShot: AudioCue? = null,
    val padLayer: Boolean = false,
    val ambientVolumeScale: Float = 1f,
)

object SceneAudioMap {
    fun profile(
        chapterId: String,
        sceneId: String,
        curiosity: Int,
    ): SceneAudioProfile {
        val ambient = when (chapterId) {
            "prologue" -> AmbientType.PAD
            "ch01" -> AmbientType.HUM
            "ch02" -> AmbientType.RAIN
            "ch03", "ch04", "ch05" -> AmbientType.PAD
            else -> AmbientType.NONE
        }
        val oneShot = when (chapterId to sceneId) {
            "ch03" to "c03_intro" -> AudioCue.BIRD_DISTANT
            else -> null
        }
        return SceneAudioProfile(
            ambient = ambient,
            oneShot = oneShot,
            padLayer = curiosity > 10 && chapterId in setOf("ch03", "ch04", "ch05"),
            ambientVolumeScale = if (chapterId == "prologue") 0.35f else 1f,
        )
    }
}
