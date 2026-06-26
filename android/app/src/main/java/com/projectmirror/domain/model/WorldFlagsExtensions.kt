package com.projectmirror.domain.model

fun WorldFlags.applyFlagUpdates(updates: Map<String, String>): WorldFlags {
    var result = this
    updates.forEach { (key, value) ->
        result = when (key) {
            "f001_crack_state" -> result.copy(f001CrackState = value)
            "f002_door_handle" -> result.copy(f002DoorHandle = value)
            "f003_silhouette_delay_ms" -> result.copy(f003SilhouetteDelayMs = value.toIntOrNull() ?: result.f003SilhouetteDelayMs)
            "f004_rin_noticed" -> result.copy(npcFlags = result.npcFlags + ("f004_rin_noticed" to value))
            "f005_feather_found" -> result.copy(f005FeatherFound = value.toBoolean())
            "rin_recorded" -> result.copy(rinRecorded = value.toBoolean())
            "ambient_color_temp_shift" -> result.copy(
                ambient = result.ambient.copy(colorTempShift = value.toFloatOrNull() ?: result.ambient.colorTempShift),
            )
            else -> result.copy(npcFlags = result.npcFlags + (key to value))
        }
    }
    return result
}
