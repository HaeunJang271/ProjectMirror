package com.projectmirror.data.repository

import com.projectmirror.data.datastore.PlayerPreferences
import com.projectmirror.data.local.dao.ChoiceLogDao
import com.projectmirror.data.local.dao.ForeshadowDao
import com.projectmirror.data.local.dao.SaveSlotDao
import com.projectmirror.data.local.entity.ChoiceLogEntity
import com.projectmirror.data.local.entity.ForeshadowEntity
import com.projectmirror.data.local.entity.SaveSlotEntity
import com.projectmirror.domain.model.ChoiceOption
import com.projectmirror.domain.model.DispositionWeights
import com.projectmirror.domain.model.WorldFlags
import com.projectmirror.domain.model.applyFlagUpdates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameStateRepository @Inject constructor(
    private val saveSlotDao: SaveSlotDao,
    private val choiceLogDao: ChoiceLogDao,
    private val foreshadowDao: ForeshadowDao,
    private val playerPreferences: PlayerPreferences,
) {
    private val json = Json { ignoreUnknownKeys = true }

    private val _worldFlags = MutableStateFlow(WorldFlags())
    val worldFlags: StateFlow<WorldFlags> = _worldFlags.asStateFlow()

    val disposition: Flow<DispositionWeights> = playerPreferences.disposition

    suspend fun resetForNewGame() {
        _worldFlags.value = WorldFlags()
        playerPreferences.updateDisposition(DispositionWeights())
    }

    suspend fun applyChoice(
        choice: ChoiceOption,
        chapterId: String,
        sceneId: String,
    ): WorldFlags {
        recordChoice(choice.id, chapterId)
        if (choice.dispositionDelta.isNotEmpty()) {
            applyDispositionDelta(choice.dispositionDelta)
        }
        choice.foreshadow.forEach { (flagId, state) ->
            setForeshadow(flagId, state)
        }
        val updated = _worldFlags.value.applyFlagUpdates(choice.flagUpdates)
        _worldFlags.value = updated
        autoSave(chapterId, sceneId, updated)
        return updated
    }

    suspend fun recordChoice(choiceId: String, chapterId: String, slot: Int? = null) {
        choiceLogDao.insert(
            ChoiceLogEntity(
                choiceId = choiceId,
                chapterId = chapterId,
                timestamp = System.currentTimeMillis(),
                slot = slot,
            ),
        )
    }

    suspend fun applyDispositionDelta(delta: Map<String, Int>) {
        val current = playerPreferences.disposition.first()
        playerPreferences.updateDisposition(current.applyDelta(delta))
    }

    suspend fun setForeshadow(flagId: String, state: String, metadata: String? = null) {
        foreshadowDao.upsert(
            ForeshadowEntity(flagId = flagId, state = state, metadata = metadata),
        )
    }

    suspend fun autoSave(chapterId: String, sceneId: String, flags: WorldFlags = _worldFlags.value) {
        saveSlotDao.upsert(
            SaveSlotEntity(
                slot = 0,
                chapterId = chapterId,
                sceneId = sceneId,
                snapshotJson = json.encodeToString(flags),
                updatedAt = System.currentTimeMillis(),
            ),
        )
        playerPreferences.setChapter(chapterId, sceneId)
    }
}
