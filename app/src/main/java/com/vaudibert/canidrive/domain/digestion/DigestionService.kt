package com.vaudibert.canidrive.domain.digestion

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max

/**
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
class DigestionService(private var body: PhysicalBody) {

    val absorbedDrinks : MutableList<Drink> = ArrayList()

    var ingestCallback = { _: Drink -> }
    var removeCallback = { _: Drink -> }

    fun ingest (drink: Drink) {
        absorbedDrinks.add(drink)
        absorbedDrinks.sortBy {
                absorbedDrink -> absorbedDrink.ingestionTime.time
        }
        ingestCallback(drink)
    }

    fun remove(drink: Drink) {
        absorbedDrinks.remove(drink)
        removeCallback(drink)
    }

    // TODO : extract a time service to avoid java.util dependency ?
    fun alcoholRateAt(date: Date): Double {
        if (absorbedDrinks.isEmpty()) return 0.0

        var lastIngestion = absorbedDrinks[0].ingestionTime
        var lastRate = 0.0

        absorbedDrinks.forEach {
            lastRate = newRate(lastRate, lastIngestion, it.ingestionTime) +
                    (it.alcoholMass() / body.effectiveWeight + (body.decreaseFactor/2))
            lastIngestion = it.ingestionTime
        }

        return newRate(lastRate, lastIngestion, date)
    }

    fun timeToReachLimit(limit: Double) : Date {
        val now = Date()
        val rate = alcoholRateAt(now)
        if (rate < limit) return now

        return Date(
            now.time + ((rate-limit) / body.decreaseFactor* 3_600_000).toLong()
        )
    }

    /**
     * Computes the alcohol rate since the last ingestion.
     */
    private fun newRate(lastRate: Double, lastIngestion: Date, now: Date) : Double {
        val timeLapse = ((now.time - lastIngestion.time).toDouble() / (3600*1000))

        val newRate = lastRate - (body.decreaseFactor* timeLapse)
        return max(0.0, newRate)
    }
}