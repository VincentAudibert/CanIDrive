package com.vaudibert.canidrive.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vaudibert.canidrive.domain.drink.IngestedDrink
import java.util.*

@Entity
data class IngestedDrinkEntity(
    @PrimaryKey(autoGenerate = true) val uid : Long,
    val ingestionTime : Date,
    val volume : Double,
    val name : String,
    val degree: Double
    ) {
    fun toDrink(): IngestedDrink =
        IngestedDrink(
            volume,
            degree,
            ingestionTime
        )
}