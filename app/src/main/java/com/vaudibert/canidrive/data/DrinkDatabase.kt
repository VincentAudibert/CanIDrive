package com.vaudibert.canidrive.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DrinkEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class DrinkDatabase : RoomDatabase() {
    abstract fun drinkDao(): DrinkDao
}
