package com.vaudibert.canidrive.domain.drink

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class DrinkServiceOldTest {

    private lateinit var drinkServiceOld: DrinkServiceOld

    private val firstPreset = PresetDrink(
        "first preset",
        100.0,
        10.0,
        2
    )

    private val presetList = mutableListOf(
        firstPreset
    )

    @BeforeEach
    fun setup() {
        drinkServiceOld = DrinkServiceOld()
        drinkServiceOld.presetDrinks = presetList
    }

    @Test
    fun `A DrinkService is initialized empty`() {
        drinkServiceOld = DrinkServiceOld()
        assertEquals(0, drinkServiceOld.presetDrinks.size)
        assertEquals(0, drinkServiceOld.ingestedDrinks.size)
    }

    @Test
    fun `Ingesting a preset adds it as ingested and increments counter`() {
        val firstPresetCounter =  firstPreset.count
        val ingestedSize = drinkServiceOld.ingestedDrinks.size

        drinkServiceOld.ingest(firstPreset, Date())

        assertEquals(1, firstPreset.count - firstPresetCounter)
        assertEquals(1, drinkServiceOld.ingestedDrinks.size - ingestedSize)
    }

}