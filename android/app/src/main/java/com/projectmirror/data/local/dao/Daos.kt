package com.projectmirror.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projectmirror.data.local.entity.ChoiceLogEntity
import com.projectmirror.data.local.entity.ForeshadowEntity
import com.projectmirror.data.local.entity.SaveSlotEntity

@Dao
interface SaveSlotDao {
    @Query("SELECT * FROM save_slots WHERE slot = :slot LIMIT 1")
    suspend fun getSlot(slot: Int): SaveSlotEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(slot: SaveSlotEntity)
}

@Dao
interface ChoiceLogDao {
    @Insert
    suspend fun insert(entry: ChoiceLogEntity)

    @Query("SELECT * FROM choice_log ORDER BY timestamp DESC")
    suspend fun getAll(): List<ChoiceLogEntity>
}

@Dao
interface ForeshadowDao {
    @Query("SELECT * FROM foreshadow_flags WHERE flagId = :flagId LIMIT 1")
    suspend fun get(flagId: String): ForeshadowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(flag: ForeshadowEntity)
}
