package com.vaudibert.canidrive.domain.drink

open class PresetDrink(
    volume: Double,
    degree: Double,
    val name: String
): Drink(volume, degree) {

    fun alcoholMass(): Double = degree/100 * volume * ALCOHOL_DENSITY

}