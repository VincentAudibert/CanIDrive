package com.vaudibert.canidrive.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vaudibert.canidrive.domain.drink.IPresetDrink
import com.vaudibert.canidrive.domain.drink.PresetDrink

@Entity
class PresetDrinkEntity(
    @PrimaryKey(autoGenerate = true) var uid : Long,
    name: String,
    volume: Double,
    degree: Double,
    count: Int
): IPresetDrink, PresetDrink(name, volume, degree, count) {

    constructor(uid: Long, presetDrink: PresetDrink) : this(uid, presetDrink.name, presetDrink.volume, presetDrink.degree, presetDrink.count)
}