package com.example.canidrive

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.util.*

internal class DrinkerTest {

    @Test
    fun `a new should be sober`() {
        val drinker = Drinker()

        assertEquals(0F, drinker.alcoholLevel(Date(0L)))
    }

    @Test
    fun `a drinker should be drunk just after a strong drink`() {
        val drinker = Drinker()

        val ingestionTime = Date()
        drinker.drink(Drink(100, 40F, ingestionTime))

        assertTrue(0F < drinker.alcoholLevel(ingestionTime))
    }
}