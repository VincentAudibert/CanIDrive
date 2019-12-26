package com.vaudibert.canidrive.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vaudibert.canidrive.domain.Drink

@Dao
interface DrinkDao {
    @Query("SELECT * from drinkentity")
    suspend fun getAll(): List<DrinkEntity>

    @Insert(entity = DrinkEntity::class)
    suspend fun insert(drink: Drink)

    @Delete(entity = DrinkEntity::class)
    suspend fun remove(drink: Drink)
}