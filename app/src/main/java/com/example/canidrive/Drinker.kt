package com.example.canidrive

import java.util.*
import kotlin.collections.ArrayList


const val ALCOHOL_DENSITY = 0.8

/**
 * Represents the person drinking.
 * The parameters such as weight and sex may change as the user adjusts the inputs.
 *
 * Full rules :
 *  - it takes 30min to reach max alcohol rate (1h if eating before)
 *  - rate is : drink_volume * alcohol_degree * alcohol_density / (sex_factor * weight)
 *  - sex_factor is 0.6 for women, 0.7 for men
 *  - decrease is 0.085-0.1g/L/h for women, 0.1-0.15g/L/h for men.
 */

class Drinker(var weight: Double = 80.0, var sex: String = "NONE") {

    private val absorbedDrinks : MutableList<AbsorbedDrink> = ArrayList()

    fun ingest (drink: Drink, ingestionTime: Date) {

        absorbedDrinks.add(AbsorbedDrink(drink, ingestionTime))
    }

    private fun sexFactor() = if (sex == "MALE") 0.7 else 0.6

    fun alcoholRateAt(date: Date): Double {
        val alcoholMass = absorbedDrinks.map {
                drink -> drink.degree / 100 * drink.qtyMilliLiter * ALCOHOL_DENSITY
        } .sum()

        return alcoholMass / (sexFactor() * weight)
    }
}