package com.example.canidrive

import java.util.*
import kotlin.collections.ArrayList

class Drinker(var weight: Double = 80.0, val sex: String = "NONE") {

    private val absorbedDrinks : MutableList<Drink> = ArrayList()
    private val sexFactor : Double = if (sex == "MALE") 0.7 else 0.6

    fun drink (drink: Drink) {
        absorbedDrinks.add(drink)
    }

    fun alcoholLevel(date: Date): Double {
        var alcoholMass = absorbedDrinks.map {
                drink -> drink.degree * drink.qtyMilliLiter * 0.8 / 100
        } .sum()

        return alcoholMass / (sexFactor * weight)
    }
}