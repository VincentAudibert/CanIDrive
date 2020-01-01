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
 *  - it takes 30min to reach max alcohol rate (1h if eating before), see compromise
 *  - rate is : drink_volume * alcohol_degree * alcohol_density / (sex_factor * weight)
 *  - sex_factor is 0.6 for women, 0.7 for men
 *  - decrease is 0.085-0.1g/L/h for women, 0.1-0.15g/L/h for men.
 *
 * Compromise :
 * Model used is for alcohol rate to shoot instantly to a new maximum upon ingestion, then to
 * steadily decrease, reaching the theoretical max rate 30 min after ingestion.
 * This simplifies greatly calculation while staying safe and reliable regarding the drive status.
 */

class Drinker(
    var weight: Double = 80.0,
    var sex: String = "NONE",
    var isYoungDriver: Boolean = false,
    var isProfessionalDriver:Boolean = false
) {

    private val absorbedDrinks : MutableList<Drink> = ArrayList()

    fun ingest (drink: Drink) {
        absorbedDrinks.add(drink)
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
            lastRate = newRate(lastRate, lastIngestion, it.ingestionTime) +
                    (it.alcoholMass() / effectiveWeight() + (decreaseFactor()/2))
            lastIngestion = it.ingestionTime
        }

        return newRate(lastRate, lastIngestion, date)
    }

    /**
     * Computes the alcohol rate since the last ingestion.
     */
    private fun newRate(lastRate: Double, lastIngestion: Date, now: Date) : Double {
        val timeLapse = hoursBetween(lastIngestion, now)

        val newRate = lastRate - (decreaseFactor() * timeLapse)
        return max(0.0, newRate)
    }

    fun getDrinks() = ArrayList(absorbedDrinks)

    fun remove(drink: Drink) {
        absorbedDrinks.remove(drink)
    }

    fun timeToReachLimit(limit: Double) : Date {
        val now = Date()
        val rate = alcoholRateAt(now)
        if (rate < limit) return now

        return Date(
            now.time + ((rate-limit) / decreaseFactor() * 3_600_000).toLong()
        )
    }
}