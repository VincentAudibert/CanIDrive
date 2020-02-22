package com.vaudibert.canidrive.domain.drink

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class DrinkServiceTest {

    private lateinit var drinkService: DrinkService

    private val firstPreset = PresetDrink(
        "first preset",
        100.0,
        10.0,
        2
    )


    @Test
    fun `Adding a preset increments its counter`() {
        val presetList = mutableListOf(
            firstPreset
        )

        drinkService = DrinkService()
        drinkService.presetDrinks = presetList

        assertEquals(2, firstPreset.count)
        assertEquals(0, drinkService.ingestedDrinks.size)

        drinkService.ingest(firstPreset, Date())

        assertEquals(3, firstPreset.count)
        assertEquals(1, drinkService.ingestedDrinks.size)

    }
}