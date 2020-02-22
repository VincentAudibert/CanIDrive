package com.vaudibert.canidrive.domain.drink

data class PresetDrink(
    val name: String,
    val volume: Double,
    val degree: Double,
    var count: Int = 0
)