package com.projectmirror.data.repository

import android.content.Context
import com.projectmirror.domain.model.NarrativeChapterFile
import com.projectmirror.domain.model.NarrativeScene
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NarrativeRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun loadScene(chapterId: String, sceneId: String): NarrativeScene? {
        val assetPath = when (chapterId) {
            "prologue" -> "narrative/prologue/scenes.json"
            else -> "narrative/$chapterId/dialogue_trees.json"
        }
        val text = context.assets.open(assetPath).bufferedReader().use { it.readText() }
        val file = json.decodeFromString<NarrativeChapterFile>(text)
        return file.scenes.find { it.id == sceneId }
    }

    fun loadChapter(chapterId: String): NarrativeChapterFile {
        val assetPath = when (chapterId) {
            "prologue" -> "narrative/prologue/scenes.json"
            else -> "narrative/$chapterId/dialogue_trees.json"
        }
        val text = context.assets.open(assetPath).bufferedReader().use { it.readText() }
        return json.decodeFromString(text)
    }
}
