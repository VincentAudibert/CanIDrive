package com.vaudibert.canidrive.domain

import com.vaudibert.canidrive.hoursBetween
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max

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

        absorbedDrinks.add(
            AbsorbedDrink(
                drink,
                ingestionTime
            )
        )
        absorbedDrinks.sortByDescending { absorbedDrink -> absorbedDrink.ingestionTime }
    }

    private fun sexFactor() = if (sex == "MALE") 0.7 else 0.6

    private fun effectiveWeight() = sexFactor() * weight

    private fun decreaseFactor() = if (sex == "MALE") 0.1 else 0.085

    fun alcoholRateAt(date: Date): Double {
        val historicDrinks = absorbedDrinks.filter {
                absorbedDrink -> absorbedDrink.ingestionTime.before(date)
        }
        if (historicDrinks.isEmpty()) return 0.0

        var lastIngestion = historicDrinks[0].ingestionTime
        var lastRate = 0.0

        historicDrinks.forEach {
            lastRate = newRate(lastRate, lastIngestion, it.ingestionTime) + (it.alcoholMass() / effectiveWeight())
            lastIngestion = it.ingestionTime
        }

        return newRate(lastRate, lastIngestion, date)
    }

    /**
     * Computes the alcohol rate since the last ingestion.
     */
    private fun newRate(lastRate: Double, lastIngestion: Date, now: Date) : Double {
        val timeLapse = hoursBetween(lastIngestion, now)

        return if (timeLapse < 0.5) lastRate
        else {
            val newRate = lastRate - (decreaseFactor() * (timeLapse - 0.5))
            max(0.0, newRate)
        }

    }

    fun getDrinks() = ArrayList(absorbedDrinks)

    fun remove(drink: AbsorbedDrink) {
        absorbedDrinks.remove(drink)
    }
}