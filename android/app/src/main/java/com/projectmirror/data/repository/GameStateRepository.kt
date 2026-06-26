package com.projectmirror.data.repository

import com.projectmirror.data.datastore.PlayerPreferences
import com.projectmirror.data.local.dao.ChoiceLogDao
import com.projectmirror.data.local.dao.DialogueLogDao
import com.projectmirror.data.local.dao.ForeshadowDao
import com.projectmirror.data.local.dao.SaveSlotDao
import com.projectmirror.data.local.entity.ChoiceLogEntity
import com.projectmirror.data.local.entity.DialogueLogEntity
import com.projectmirror.data.local.entity.ForeshadowEntity
import com.projectmirror.data.local.entity.SaveSlotEntity
import com.projectmirror.domain.model.ChoiceOption
import com.projectmirror.domain.model.DispositionWeights
import com.projectmirror.domain.model.ImplicitChoiceOutcome
import com.projectmirror.domain.model.SaveProgress
import com.projectmirror.domain.model.SaveSlotInfo
import com.projectmirror.domain.model.SaveSnapshot
import com.projectmirror.domain.model.WorldFlags
import com.projectmirror.domain.model.applyFlagUpdates
import com.projectmirror.domain.model.decodeSaveSnapshot
import com.projectmirror.domain.model.dispositionAmbientDelta
import com.projectmirror.domain.model.encodeSaveSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameStateRepository @Inject constructor(
    private val saveSlotDao: SaveSlotDao,
    private val choiceLogDao: ChoiceLogDao,
    private val dialogueLogDao: DialogueLogDao,
    private val foreshadowDao: ForeshadowDao,
    private val playerPreferences: PlayerPreferences,
) {
    private val _worldFlags = MutableStateFlow(WorldFlags())
    val worldFlags: StateFlow<WorldFlags> = _worldFlags.asStateFlow()

    val disposition: Flow<DispositionWeights> = playerPreferences.disposition
    val dialogueLog = dialogueLogDao.observeAll()
    val hasProgress = playerPreferences.hasProgress

    private var lastChapterId: String? = null
    private var lastSceneId: String? = null

    suspend fun resetForNewGame() {
        _worldFlags.value = WorldFlags()
        playerPreferences.updateDisposition(DispositionWeights())
        dialogueLogDao.clearAll()
        saveSlotDao.deleteSlot(0)
        playerPreferences.clearProgress()
        lastChapterId = null
        lastSceneId = null
    }

    suspend fun syncSessionFromStorage(): SaveProgress {
        val progress = playerPreferences.getProgress()
        val auto = saveSlotDao.getSlot(0)
        if (auto != null) {
            applySnapshot(decodeSaveSnapshot(auto.snapshotJson))
            lastChapterId = auto.chapterId
            lastSceneId = auto.sceneId
            return SaveProgress(auto.chapterId, auto.sceneId)
        }
        return progress ?: SaveProgress("prologue", "p01")
    }

    suspend fun restoreAutoSave(): SaveProgress? {
        val slot = saveSlotDao.getSlot(0) ?: return null
        applySnapshot(decodeSaveSnapshot(slot.snapshotJson))
        val progress = playerPreferences.getProgress() ?: SaveProgress(slot.chapterId, slot.sceneId)
        lastChapterId = progress.chapterId
        lastSceneId = progress.sceneId
        return progress
    }

    suspend fun restoreManualSlot(slot: Int): SaveProgress? {
        require(slot in 1..3)
        val entity = saveSlotDao.getSlot(slot) ?: return null
        applySnapshot(decodeSaveSnapshot(entity.snapshotJson))
        playerPreferences.setChapter(entity.chapterId, entity.sceneId)
        autoSave(entity.chapterId, entity.sceneId)
        return SaveProgress(entity.chapterId, entity.sceneId)
    }

    suspend fun saveToManualSlot(slot: Int) {
        require(slot in 1..3)
        val chapter = lastChapterId ?: playerPreferences.getProgress()?.chapterId ?: return
        val scene = lastSceneId ?: playerPreferences.getProgress()?.sceneId ?: return
        val snapshot = currentSnapshot()
        saveSlotDao.upsert(
            SaveSlotEntity(
                slot = slot,
                chapterId = chapter,
                sceneId = scene,
                snapshotJson = encodeSaveSnapshot(snapshot),
                updatedAt = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun loadManualSlotInfos(): List<SaveSlotInfo> =
        saveSlotDao.getManualSlots().map { entity ->
            SaveSlotInfo(
                slot = entity.slot,
                chapterId = entity.chapterId,
                sceneId = entity.sceneId,
                updatedAt = entity.updatedAt,
            )
        }

    suspend fun applyImplicitChoice(
        outcome: ImplicitChoiceOutcome,
        chapterId: String,
        sceneId: String,
    ): WorldFlags {
        recordChoice(outcome.id, chapterId)
        if (outcome.dispositionDelta.isNotEmpty()) {
            applyDispositionDelta(outcome.dispositionDelta)
        }
        val ambientDelta = dispositionAmbientDelta(outcome.dispositionDelta)
        outcome.foreshadow.forEach { (flagId, state) ->
            setForeshadow(flagId, state)
        }
        val flagUpdated = _worldFlags.value.applyFlagUpdates(outcome.flagUpdates)
        val updated = flagUpdated.copy(
            ambient = flagUpdated.ambient.copy(
                colorTempShift = (flagUpdated.ambient.colorTempShift + ambientDelta)
                    .coerceIn(-1f, 1f),
            ),
        )
        _worldFlags.value = updated
        autoSave(chapterId, sceneId, updated)
        return updated
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
        val ambientDelta = dispositionAmbientDelta(choice.dispositionDelta)
        choice.foreshadow.forEach { (flagId, state) ->
            setForeshadow(flagId, state)
        }
        val flagUpdated = _worldFlags.value.applyFlagUpdates(choice.flagUpdates)
        val updated = flagUpdated.copy(
            ambient = flagUpdated.ambient.copy(
                colorTempShift = (flagUpdated.ambient.colorTempShift + ambientDelta)
                    .coerceIn(-1f, 1f),
            ),
        )
        _worldFlags.value = updated
        autoSave(chapterId, sceneId, updated)
        return updated
    }

    suspend fun logDialogue(
        chapterId: String,
        sceneId: String,
        lineType: String,
        speaker: String?,
        text: String,
    ) {
        val last = dialogueLogDao.getLast()
        if (last?.sceneId == sceneId && last.text == text) return
        dialogueLogDao.insert(
            DialogueLogEntity(
                chapterId = chapterId,
                sceneId = sceneId,
                lineType = lineType,
                speaker = speaker,
                text = text,
                timestamp = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun logChoice(chapterId: String, sceneId: String, label: String) {
        logDialogue(
            chapterId = chapterId,
            sceneId = sceneId,
            lineType = "choice",
            speaker = null,
            text = "— $label —",
        )
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
        lastChapterId = chapterId
        lastSceneId = sceneId
        val snapshot = SaveSnapshot(worldFlags = flags, disposition = playerPreferences.disposition.first())
        saveSlotDao.upsert(
            SaveSlotEntity(
                slot = 0,
                chapterId = chapterId,
                sceneId = sceneId,
                snapshotJson = encodeSaveSnapshot(snapshot),
                updatedAt = System.currentTimeMillis(),
            ),
        )
        playerPreferences.setChapter(chapterId, sceneId)
    }

    private suspend fun currentSnapshot(): SaveSnapshot =
        SaveSnapshot(
            worldFlags = _worldFlags.value,
            disposition = playerPreferences.disposition.first(),
        )

    private suspend fun applySnapshot(snapshot: SaveSnapshot) {
        _worldFlags.value = snapshot.worldFlags
        playerPreferences.updateDisposition(snapshot.disposition)
    }
}
