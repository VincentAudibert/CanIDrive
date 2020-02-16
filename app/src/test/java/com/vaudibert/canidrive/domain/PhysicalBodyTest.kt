package com.vaudibert.canidrive.domain

import com.vaudibert.canidrive.domain.digestion.PhysicalBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PhysicalBodyTest {

    @Test
    fun `A 100kg male drinker shall have eff-weight 70, decrease 0,1`() {
        val male = PhysicalBody()
        male.sex = "MALE"
        male.weight = 100.0

        assertEquals(70.0, male.effectiveWeight)
        assertEquals(0.1, male.decreaseFactor)
    }

    @Test
    fun `A 50kg female drinker shall have eff-weight 30, decrease 0,085`() {
        val female = PhysicalBody()
        female.sex = "FEMALE"
        female.weight = 50.0

        assertEquals(30.0, female.effectiveWeight)
        assertEquals(0.085, female.decreaseFactor)
    }

    @Test
    fun `Changing sex impacts effectiveWeight and decreaseFactor`() {
        val changer = PhysicalBody()
        changer.sex = "MALE"
        changer.weight = 100.0

        changer.sex="FEMALE"

        assertEquals(60.0, changer.effectiveWeight)
        assertEquals(0.085, changer.decreaseFactor)

        changer.sex="MALE"

        assertEquals(70.0, changer.effectiveWeight)
        assertEquals(0.1, changer.decreaseFactor)
    }

    @Test
    fun `Changing weight impacts only effectiveWeight`() {
        val shrinker = PhysicalBody()
        shrinker.weight = 200.0
        val decrease = shrinker.decreaseFactor

        shrinker.weight = 50.0

        assertEquals(30.0, shrinker.effectiveWeight)
        assertEquals(decrease, shrinker.decreaseFactor)
    }

    @Test
    fun `Tolerance impacts only decreaseFactor for a given sex`() {
        val female = PhysicalBody()
        female.sex = "FEMALE"
        female.weight = 50.0

        val male = PhysicalBody()
        male.sex = "MALE"
        male.weight = 100.0

        assertEquals(0.085, female.decreaseFactor)
        female.alcoholTolerance = 1.0
        assertEquals(0.1, female.decreaseFactor)

        assertEquals(0.1, male.decreaseFactor)
        male.alcoholTolerance = 1.0
        assertEquals(0.15, male.decreaseFactor)

    }

}