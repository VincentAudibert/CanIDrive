package com.vaudibert.canidrive.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vaudibert.canidrive.domain.digestion.Drink
import java.util.*

@Entity
data class DrinkEntity(
    @PrimaryKey(autoGenerate = true) val uid : Long,
    val ingestionTime : Date,
    val volume : Double,
    val degree: Double
    ) {
    fun toDrink(): Drink =
        Drink(
            volume,
            degree,
            ingestionTime
        )
}