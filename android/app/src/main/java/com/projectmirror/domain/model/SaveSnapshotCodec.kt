package com.projectmirror.domain.model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val snapshotJson = Json { ignoreUnknownKeys = true }

fun decodeSaveSnapshot(raw: String): SaveSnapshot = runCatching {
    snapshotJson.decodeFromString<SaveSnapshot>(raw)
}.getOrElse {
    SaveSnapshot(worldFlags = snapshotJson.decodeFromString(raw))
}

fun encodeSaveSnapshot(snapshot: SaveSnapshot): String =
    snapshotJson.encodeToString(snapshot)
