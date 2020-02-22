package com.vaudibert.canidrive.domain

import com.vaudibert.canidrive.domain.digestion.DigestionService
import com.vaudibert.canidrive.domain.digestion.PhysicalBody
import com.vaudibert.canidrive.domain.drink.DrinkService
import com.vaudibert.canidrive.domain.drink.IngestedDrink
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class DigestionServiceTest {

    private val precision = 0.0001

    var body = PhysicalBody()
    var drinkService = DrinkService()
    var digestionService = DigestionService(body, drinkService)

    @BeforeEach
    fun before() {
        body = PhysicalBody()
        body.sex = "MALE"
        body.weight = 100.0
        drinkService = DrinkService()
        digestionService = DigestionService(body, drinkService)
    }

    @Test
    fun `Sober user has zero alcohol`() {
        assertEquals(0.0, digestionService.alcoholRateAt(Date()))
    }

    @Test
    fun `User taking a drink is not sober presently`() {
        val now = Date()
        drinkService.ingestForInit(
            IngestedDrink(
                500.0,
                5.0,
                now
            )
        )

        assertTrue(0.0 < digestionService.alcoholRateAt(Date(now.time + 1000)))
    }

    @Test
    fun `Instant alcohol rate is proportional to drink count`() {
        val now = Date()
        drinkService.ingestForInit(
            IngestedDrink(
                500.0,
                5.0,
                now
            )
        )
        val firstRate = digestionService.alcoholRateAt(Date(now.time + 1))

        drinkService.ingestForInit(
            IngestedDrink(
                500.0,
                5.0,
                Date(now.time + 2)
            )
        )
        val secondRate = digestionService.alcoholRateAt(Date(now.time + 3))

        assertEquals(2.0, secondRate / firstRate, precision)
    }

    @Test
    fun `Alcohol rate decreases as per body's decreaseFactor`() {
        val now = Date()
        drinkService.ingestForInit(
            IngestedDrink(
                1000.0,
                10.0,
                now
            )
        )

        val instantRate = digestionService.alcoholRateAt(Date(now.time + 1))
        val laterRate = digestionService.alcoholRateAt(Date(now.time + 3_600_000))

        assertEquals(body.decreaseFactor, instantRate - laterRate, precision)
    }

}