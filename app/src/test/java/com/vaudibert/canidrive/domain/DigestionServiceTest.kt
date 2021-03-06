package com.vaudibert.canidrive.domain

import com.vaudibert.canidrive.domain.digestion.DigestionService
import com.vaudibert.canidrive.domain.digestion.PhysicalBody
import com.vaudibert.canidrive.domain.drink.IngestedDrink
import com.vaudibert.canidrive.domain.drink.IngestionService
import com.vaudibert.canidrive.domain.drink.PresetDrink
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class DigestionServiceTest {

    private val precision = 0.0001

    private lateinit var body: PhysicalBody
    private lateinit var ingestionService: IngestionService<PresetDrink, IngestedDrink>
    private lateinit var digestionService: DigestionService

    private fun ingestBeer(now: Date) {
        ingestionService.ingest(
            PresetDrink(
                "beer",
                500.0,
                5.0
            ),
            now
        )
    }

    @BeforeEach
    fun before() {
        body = PhysicalBody()
        body.sex = "MALE"
        body.weight = 100.0
        ingestionService = IngestionService {
                preset: PresetDrink, ingestionTime: Date -> IngestedDrink(preset.name, preset.volume, preset.degree, ingestionTime)
        }
        digestionService = DigestionService(body, ingestionService)
    }

    @Test
    fun `Sober user has zero alcohol`() {
        assertEquals(0.0, digestionService.alcoholRateAt(Date()))
    }

    @Test
    fun `User taking a drink is not sober presently`() {
        val now = Date()
        ingestBeer(now)

        assertTrue(0.0 < digestionService.alcoholRateAt(Date(now.time + 1000)))
    }

    @Test
    fun `Instant alcohol rate is proportional to drink count`() {
        val now = Date()
        ingestBeer(now)
        val firstRate = digestionService.alcoholRateAt(Date(now.time + 1))

        ingestBeer(Date(now.time + 2))

        val secondRate = digestionService.alcoholRateAt(Date(now.time + 3))

        assertEquals(2.0, secondRate / firstRate, precision)
    }

    @Test
    fun `Alcohol rate decreases as per body's decreaseFactor`() {
        val now = Date()
        ingestBeer(now)

        val instantRate = digestionService.alcoholRateAt(Date(now.time + 1))
        val laterRate = digestionService.alcoholRateAt(Date(now.time + 3_600_000))

        assertEquals(body.decreaseFactor, instantRate - laterRate, precision)
    }

}