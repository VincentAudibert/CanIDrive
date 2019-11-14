package com.example.canidrive

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

internal class DrinkerTest {

    @Test
    fun `a new should be sober`() {
        val drinker = Drinker()

        assertEquals(0.0, drinker.alcoholLevel(Date(0L)), 0.0001)
    }

    @Test
    fun `a 80kg male should reach 0,35g per L with a 50cl 5deg beer`() {
        // 500ml at 5 deg makes 25ml of alcohol
        // density 0.8 gives 20g
        // body mass to consider is 0.7 * 80 = 56kg
        // 20/56 = 0.35

        val drinker = Drinker()

        val ingestionTime = Date()
        drinker.drink(Drink(500, 5F, ingestionTime))

        assertEquals("Calculation of alcohol rate", 0.35, drinker.alcoholLevel(ingestionTime), 0.1)
    }
}