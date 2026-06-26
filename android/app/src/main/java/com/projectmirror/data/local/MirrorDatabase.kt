package com.projectmirror.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.projectmirror.data.local.dao.ChoiceLogDao
import com.projectmirror.data.local.dao.DialogueLogDao
import com.projectmirror.data.local.dao.ForeshadowDao
import com.projectmirror.data.local.dao.SaveSlotDao
import com.projectmirror.data.local.entity.ChoiceLogEntity
import com.projectmirror.data.local.entity.DialogueLogEntity
import com.projectmirror.data.local.entity.ForeshadowEntity
import com.projectmirror.data.local.entity.SaveSlotEntity

@Database(
    entities = [
        SaveSlotEntity::class,
        ChoiceLogEntity::class,
        ForeshadowEntity::class,
        DialogueLogEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class MirrorDatabase : RoomDatabase() {
    abstract fun saveSlotDao(): SaveSlotDao
    abstract fun choiceLogDao(): ChoiceLogDao
    abstract fun foreshadowDao(): ForeshadowDao
    abstract fun dialogueLogDao(): DialogueLogDao
}
