package com.vaudibert.canidrive.data

import androidx.room.*
import com.vaudibert.canidrive.domain.drink.PresetDrink

@Dao
interface PresetDrinkDao {
    @Insert(entity = PresetDrinkEntity::class)
    suspend fun insert(presetDrink: PresetDrink) : Long

    @Query("SELECT COUNT(uid) from presetdrinkentity")
    suspend fun count(): Int

    @Insert(entity = PresetDrinkEntity::class)
    suspend fun insertAll(presetDrinks: List<PresetDrink>)

    @Delete(entity = PresetDrinkEntity::class)
    suspend fun remove(presetDrink: PresetDrink)

    @Update
    suspend fun update(presetDrinkEntity: PresetDrinkEntity)

    @Query("SELECT * from presetdrinkentity")
    suspend fun getAll(): List<PresetDrinkEntity>
}