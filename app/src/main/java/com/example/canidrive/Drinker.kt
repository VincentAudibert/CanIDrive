package com.example.canidrive

import java.util.*
import kotlin.collections.ArrayList

class Drinker(val weight: Double = 80.0, val sex: String = "NONE") {

    private val absorbedDrinks : MutableList<Drink> = ArrayList()


    fun drink (drink: Drink) {
        absorbedDrinks.add(drink)
    }

    fun alcoholLevel(date: Date): Double {
        var alcoholMass = absorbedDrinks.map {
                drink -> drink.degree * drink.qtyMilliLiter * 0.8 / 100
        } .sum()

        return alcoholMass / (0.7 * weight)
    }
}