package com.example.canidrive

import java.util.*

const val ALCOHOL_DENSITY = 0.8

class Drink(val qtyMilliLiter:Int, val degree:Float)

data class AbsorbedDrink(val drink: Drink, val ingestionTime:Date) {
    val qtyMilliLiter: Int
    val degree: Float

    init {
        qtyMilliLiter = drink.qtyMilliLiter
        degree = drink.degree
    }

    fun alcoholMass(): Double = degree/100 * qtyMilliLiter * ALCOHOL_DENSITY

    override fun toString(): String {
        return "${drink.qtyMilliLiter} cL at ${drink.degree} % at $ingestionTime"
    }
}
