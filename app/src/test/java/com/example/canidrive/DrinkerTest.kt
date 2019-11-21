package com.example.canidrive

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

const val RATE_PRECISION = 0.02
val BEER = Drink(500, 5F)

internal class DrinkerTest {

    private fun getMaleDrinkerWithBeer() : Drinker {
        val drinker = Drinker(100.0, "MALE")
        val ingestionTime = Date(Date().time - 10)

        drinker.ingest(BEER, ingestionTime)

        return drinker
    }

    private fun getFemaleDrinkerWithBeer() : Drinker {
        val drinker = getMaleDrinkerWithBeer()
        drinker.weight = 50.0
        drinker.sex = "FEMALE"
        return drinker
    }

    @Test
    fun `A fresh new drinker should be sober`() {
        val drinker = Drinker()

        assertEquals(0.0, drinker.alcoholRateAt(Date(0L)), 0.0001)
    }

    @Test
    fun `A 100kg male should reach instantly 0,28g per L with a 500ml 5 deg beer`() {
        // 500ml at 5 deg makes 25ml of alcohol
        // density 0.8 gives 20g
        // body mass to consider is 0.7 * 100 = 70
        // 20/70 = 0.28g per L of blood.

        val drinker = getMaleDrinkerWithBeer()

        val measureTime = Date()
        assertEquals(0.28, drinker.alcoholRateAt(measureTime), RATE_PRECISION)
    }

    @Test
    fun `A 50kg female should reach instantly 0,67g per L with a 500ml 5 deg beer`() {
        // 500ml 5deg gives 20g
        // 20 / (0.6 * 50) = 0.67

        val drinker = getFemaleDrinkerWithBeer()

        val measureTime = Date()
        assertEquals(0.67, drinker.alcoholRateAt(measureTime), RATE_PRECISION)
    }
    
    @Test
    fun `Alcohol rate is inverse proportional to drinker's weight`() {

        val ratio = 2.0
        val drinker = getMaleDrinkerWithBeer()
        val measureTime = Date()
        val firstRate = drinker.alcoholRateAt(measureTime)

        drinker.weight /= ratio

        val secondRate = drinker.alcoholRateAt(measureTime)
        assertEquals(ratio, secondRate / firstRate, 0.01)
    }

    @Test
    fun `Alcohol rate depends on sex selection (weight sex factor)`() {

        val drinker = getMaleDrinkerWithBeer()

        val measureTime = Date()
        val maleRate = drinker.alcoholRateAt(measureTime)

        drinker.sex = "FEMALE"

        val femaleRate = drinker.alcoholRateAt(measureTime)
        assertEquals(0.7/0.6, femaleRate / maleRate, 0.01)
    }

    @Test
    fun `Alcohol rate is double if drink is doubled` () {

        val drinker = getMaleDrinkerWithBeer()
        val measureTime = Date()
        val simpleRate = drinker.alcoholRateAt(measureTime)

        drinker.ingest(BEER, Date(measureTime.time -10))

        val doubleRate = drinker.alcoholRateAt(measureTime)

        assertEquals(2.0, doubleRate / simpleRate, 0.01)
    }

    @Test
    fun `Male rate decrease is 0,1 per hour`() {

        val drinker = getMaleDrinkerWithBeer()

        val ingestionTime = Date()
        val oneHourAfter = Date(ingestionTime.time + 3600*1000)
        val twoHoursAfter = Date(ingestionTime.time + 3600*1000*2)


        val rateDecrease = drinker.alcoholRateAt(oneHourAfter) - drinker.alcoholRateAt(twoHoursAfter)
        assertEquals(0.1, rateDecrease, 0.001)
    }
}