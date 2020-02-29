package com.vaudibert.canidrive.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        IngestedDrinkEntity::class,
        PresetDrinkEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class DrinkDatabase : RoomDatabase() {
    abstract fun ingestedDrinkDao(): IngestedDrinkDao
    abstract fun presetDrinkDao(): PresetDrinkDao
}
