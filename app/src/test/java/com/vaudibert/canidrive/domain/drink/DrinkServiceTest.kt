package com.vaudibert.canidrive.domain.drink

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
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

    private val presetList = mutableListOf(
        firstPreset
    )

    @BeforeEach
    fun setup() {
        drinkService = DrinkService()
        drinkService.presetDrinks = presetList
    }

    @Test
    fun `A DrinkService is initialized empty`() {
        drinkService = DrinkService()
        assertEquals(0, drinkService.presetDrinks.size)
        assertEquals(0, drinkService.ingestedDrinks.size)
    }

    @Test
    fun `Ingesting a preset adds it as ingested and increments counter`() {
        val firstPresetCounter =  firstPreset.count
        val ingestedSize = drinkService.ingestedDrinks.size

        drinkService.ingest(firstPreset, Date())

        assertEquals(1, firstPreset.count - firstPresetCounter)
        assertEquals(1, drinkService.ingestedDrinks.size - ingestedSize)
    }

    @Test
    fun `Ingesting a custom add it as ingested and adds a new preset `() {
        drinkService = DrinkService()
        val now = Date()
        val customPreset = PresetDrink(
            "custom drink",
            200.0,
            5.0,
            1
        )
        val customIngested = IngestedDrink(
            customPreset.name,
            customPreset.volume,
            customPreset.degree,
            now
        )

        drinkService.ingestCustom(
            customPreset.name,
            customPreset.volume,
            customPreset.degree,
            now
        )

        assertEquals(customPreset, drinkService.presetDrinks.first())
        assertEquals(customIngested, drinkService.ingestedDrinks.first())
    }
}