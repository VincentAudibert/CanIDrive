package com.example.canidrive

import java.util.*

class Drink(val qtyMilliLiter:Int, val degree:Float)

data class AbsorbedDrink(val drink: Drink, val ingestionTime:Date) {
    val qtyMilliLiter: Int
    val degree: Float

    init {
        qtyMilliLiter = drink.qtyMilliLiter
        degree = drink.degree
    }

    override fun toString(): String {
        return "${drink.qtyMilliLiter} cL at ${drink.degree} % at $ingestionTime"
    }
}
