package com.projectmirror.data.repository

import android.content.Context
import com.projectmirror.domain.model.NarrativeScene
import com.projectmirror.domain.model.PrologueScenesFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NarrativeRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun loadPrologueScene(sceneId: String): NarrativeScene? {
        val text = context.assets.open("narrative/prologue/scenes.json")
            .bufferedReader()
            .use { it.readText() }
        val file = json.decodeFromString<PrologueScenesFile>(text)
        return file.scenes.find { it.id == sceneId }
    }
}
