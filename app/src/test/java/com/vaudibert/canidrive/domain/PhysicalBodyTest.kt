package com.vaudibert.canidrive.domain

import com.vaudibert.canidrive.domain.drinker.PhysicalBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PhysicalBodyTest {

    @Test
    fun `A 100kg male drinker shall have eff-weight 70, decrease 0,1`() {
        val male =
            PhysicalBody(100.0, "MALE")

        assertEquals(70.0, male.effectiveWeight)
        assertEquals(0.1, male.decreaseFactor)
    }

    @Test
    fun `A 50kg female drinker shall have eff-weight 30, decrease 0,085`() {
        val female =
            PhysicalBody(50.0, "FEMALE")

        assertEquals(30.0, female.effectiveWeight)
        assertEquals(0.085, female.decreaseFactor)
    }

    @Test
    fun `Changing sex impacts effectiveWeight and decreaseFactor`() {
        val changer =
            PhysicalBody(100.0, "MALE")

        changer.changeSex("FEMALE")

        assertEquals(60.0, changer.effectiveWeight)
        assertEquals(0.085, changer.decreaseFactor)

        changer.changeSex("MALE")

        assertEquals(70.0, changer.effectiveWeight)
        assertEquals(0.1, changer.decreaseFactor)
    }

    @Test
    fun `Changing weight impacts only effectiveWeight`() {
        val shrinker = PhysicalBody(200.0)
        val decrease = shrinker.decreaseFactor

        shrinker.changeWeight(50.0)

        assertEquals(30.0, shrinker.effectiveWeight)
        assertEquals(decrease, shrinker.decreaseFactor)
    }

}