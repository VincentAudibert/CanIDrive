package com.example.canidrive

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class DrinkerTest {

    @Test
    fun `a new should be sober`() {
        val drinker = Drinker()

        assertEquals(0.0, drinker.alcoholLevel(Date(0L)), 0.0001)
    }

    @Test
    fun `a 100kg male should reach 0,28g per L with a 500ml 5 deg beer`() {
        // 500ml at 5 deg makes 25ml of alcohol
        // density 0.8 gives 20g
        // body mass to consider is 0.7 * 100 = 70
        // 20/70 = 0.28g per L of blood.

        val drinker = Drinker(100.0, "MALE")

        val ingestionTime = Date()
        drinker.drink(Drink(500, 5F, ingestionTime))

        assertEquals(0.28, drinker.alcoholLevel(ingestionTime), 0.02)
    }

    @Test
    fun `a 50kg female should reach 0,67g per L with a 500ml 5 deg beer`() {
        // 500ml 5deg gives 20g
        // 20 / (0.6 * 50) = 0.67

        val drinker = Drinker(50.0, "FEMALE")

        val ingestionTime = Date()
        drinker.drink(Drink(500, 5F, ingestionTime))

        assertEquals(0.67, drinker.alcoholLevel(ingestionTime), 0.02)
    }
    
    @Test
    fun `a weight change shall change the alcohol rate`() {
        val drinker = Drinker(100.0, "MALE")

        val ingestionTime = Date()
        drinker.drink(Drink(500, 5F, ingestionTime))

        assertEquals(0.28, drinker.alcoholLevel(ingestionTime), 0.02)
        drinker.weight = 50.0
        assertEquals(0.56, drinker.alcoholLevel(ingestionTime), 0.02)
    }

    @Test
    fun `a sex change shall change the alcohol rate`() {
        val drinker = Drinker(100.0, "MALE")

        val ingestionTime = Date()
        drinker.drink(Drink(500, 5F, ingestionTime))

        assertEquals(0.28, drinker.alcoholLevel(ingestionTime), 0.02)
        drinker.sex = "FEMALE"
        assertEquals(0.33, drinker.alcoholLevel(ingestionTime), 0.02)

    }
}