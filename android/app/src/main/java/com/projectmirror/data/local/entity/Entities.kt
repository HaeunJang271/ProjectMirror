package com.projectmirror.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "save_slots")
data class SaveSlotEntity(
    @PrimaryKey val slot: Int,
    val chapterId: String,
    val sceneId: String,
    val snapshotJson: String,
    val updatedAt: Long,
)

@Entity(tableName = "choice_log")
data class ChoiceLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val choiceId: String,
    val chapterId: String,
    val timestamp: Long,
    val slot: Int?,
)

@Entity(tableName = "foreshadow_flags")
data class ForeshadowEntity(
    @PrimaryKey val flagId: String,
    val state: String,
    val metadata: String?,
)

@Entity(tableName = "dialogue_log")
data class DialogueLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chapterId: String,
    val sceneId: String,
    val lineType: String,
    val speaker: String?,
    val text: String,
    val timestamp: Long,
)
