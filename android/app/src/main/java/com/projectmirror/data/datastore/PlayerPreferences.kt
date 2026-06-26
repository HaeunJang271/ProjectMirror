package com.projectmirror.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.projectmirror.domain.model.DispositionWeights
import com.projectmirror.domain.model.PlayerSettings
import com.projectmirror.domain.model.SaveProgress
import com.projectmirror.domain.model.SubtitleScale
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "player_meta")

@Singleton
class PlayerPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val currentChapter: Flow<String> = context.dataStore.data.map {
        it[Keys.CURRENT_CHAPTER] ?: "prologue"
    }

    val currentScene: Flow<String> = context.dataStore.data.map {
        it[Keys.CURRENT_SCENE] ?: "p01"
    }

    val settings: Flow<PlayerSettings> = context.dataStore.data.map { prefs ->
        PlayerSettings(
            subtitleScale = SubtitleScale.entries.getOrElse(prefs[Keys.SUBTITLE_SCALE] ?: 1) { SubtitleScale.M },
            highContrast = prefs[Keys.HIGH_CONTRAST] ?: false,
            soundEnabled = prefs[Keys.SOUND_ENABLED] ?: true,
        )
    }

    val hasProgress: Flow<Boolean> = context.dataStore.data.map {
        it[Keys.CURRENT_CHAPTER] != null && it[Keys.CURRENT_SCENE] != null
    }

    val disposition: Flow<DispositionWeights> = context.dataStore.data.map { prefs ->
        DispositionWeights(
            empathy = prefs[Keys.EMPATHY] ?: 0,
            curiosity = prefs[Keys.CURIOSITY] ?: 0,
            order = prefs[Keys.ORDER] ?: 0,
            freedom = prefs[Keys.FREEDOM] ?: 0,
            sacrifice = prefs[Keys.SACRIFICE] ?: 0,
            selfCentered = prefs[Keys.SELF_CENTERED] ?: 0,
            doubt = prefs[Keys.DOUBT] ?: 0,
            courage = prefs[Keys.COURAGE] ?: 0,
        )
    }

    suspend fun getProgress(): SaveProgress? {
        var chapter: String? = null
        var scene: String? = null
        context.dataStore.data.first().let { prefs ->
            chapter = prefs[Keys.CURRENT_CHAPTER]
            scene = prefs[Keys.CURRENT_SCENE]
        }
        return if (chapter != null && scene != null) SaveProgress(chapter!!, scene!!) else null
    }

    suspend fun setChapter(chapterId: String, sceneId: String) {
        context.dataStore.edit {
            it[Keys.CURRENT_CHAPTER] = chapterId
            it[Keys.CURRENT_SCENE] = sceneId
        }
    }

    suspend fun updateDisposition(weights: DispositionWeights) {
        context.dataStore.edit {
            it[Keys.EMPATHY] = weights.empathy
            it[Keys.CURIOSITY] = weights.curiosity
            it[Keys.ORDER] = weights.order
            it[Keys.FREEDOM] = weights.freedom
            it[Keys.SACRIFICE] = weights.sacrifice
            it[Keys.SELF_CENTERED] = weights.selfCentered
            it[Keys.DOUBT] = weights.doubt
            it[Keys.COURAGE] = weights.courage
        }
    }

    suspend fun clearProgress() {
        context.dataStore.edit {
            it.remove(Keys.CURRENT_CHAPTER)
            it.remove(Keys.CURRENT_SCENE)
        }
    }

    suspend fun updateSettings(settings: PlayerSettings) {
        context.dataStore.edit {
            it[Keys.SUBTITLE_SCALE] = settings.subtitleScale.ordinal
            it[Keys.HIGH_CONTRAST] = settings.highContrast
            it[Keys.SOUND_ENABLED] = settings.soundEnabled
        }
    }

    private object Keys {
        val CURRENT_CHAPTER = stringPreferencesKey("current_chapter")
        val CURRENT_SCENE = stringPreferencesKey("current_scene")
        val SUBTITLE_SCALE = intPreferencesKey("subtitle_scale")
        val HIGH_CONTRAST = booleanPreferencesKey("high_contrast")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val EMPATHY = intPreferencesKey("empathy")
        val CURIOSITY = intPreferencesKey("curiosity")
        val ORDER = intPreferencesKey("order")
        val FREEDOM = intPreferencesKey("freedom")
        val SACRIFICE = intPreferencesKey("sacrifice")
        val SELF_CENTERED = intPreferencesKey("self_centered")
        val DOUBT = intPreferencesKey("doubt")
        val COURAGE = intPreferencesKey("courage")
    }
}
