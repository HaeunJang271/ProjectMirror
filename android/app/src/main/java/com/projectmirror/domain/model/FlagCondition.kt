package com.projectmirror.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FlagCondition(
    val key: String,
    val equals: String,
)

fun WorldFlags.flagValue(key: String): String? = when (key) {
    "f001_crack_state" -> f001CrackState
    "f002_door_handle" -> f002DoorHandle
    "f005_feather_found" -> f005FeatherFound.toString()
    "rin_recorded" -> rinRecorded.toString()
    "f004_rin_noticed" -> npcFlags["f004_rin_noticed"] ?: "false"
    "f006_state" -> npcFlags["f006_state"]
    else -> npcFlags[key]
}

fun WorldFlags.matches(condition: FlagCondition): Boolean =
    flagValue(condition.key) == condition.equals

fun List<NarrativeLine>.filterByFlags(flags: WorldFlags): List<NarrativeLine> =
    filter { line -> line.requireFlag?.let(flags::matches) ?: true }
