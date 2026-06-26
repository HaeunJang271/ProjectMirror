package com.projectmirror.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.projectmirror.domain.model.DispositionWeights
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
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

    private object Keys {
        val CURRENT_CHAPTER = stringPreferencesKey("current_chapter")
        val CURRENT_SCENE = stringPreferencesKey("current_scene")
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
