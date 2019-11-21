package com.example.canidrive

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max


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

    private var decreaseFactor = 0.1
    private val absorbedDrinks : MutableList<AbsorbedDrink> = ArrayList()

    fun ingest (drink: Drink, ingestionTime: Date) {

        absorbedDrinks.add(AbsorbedDrink(drink, ingestionTime))
        absorbedDrinks.sortBy { absorbedDrink -> absorbedDrink.ingestionTime }
    }

    private fun sexFactor() = if (sex == "MALE") 0.7 else 0.6

    fun alcoholRateAt(date: Date): Double {
        val historicDrinks = absorbedDrinks.filter {
                absorbedDrink -> absorbedDrink.ingestionTime.before(date)
        }
        if (historicDrinks.isEmpty()) return 0.0

        var lastIngestion = historicDrinks[0].ingestionTime
        var lastRate = 0.0
        var newRate = 0.0

        historicDrinks.forEach {
            // update lastRate with time passed since between ingestions
            lastRate -= decreaseFactor * (it.ingestionTime.time - lastIngestion.time) / (3600 * 1000)
            // up to zero if negative, alcohol rate can only be positive
            lastRate = max(lastRate, 0.0)
            // add the current drink to compute the new rate
            lastRate += (it.degree / 100 * it.qtyMilliLiter * ALCOHOL_DENSITY ) / (sexFactor() * weight)
            lastIngestion = it.ingestionTime
        }


        return max(lastRate - decreaseFactor * (date.time - lastIngestion.time) / (3600 * 1000), 0.0)
    }
}