package com.vaudibert.canidrive.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DriveLawServiceTest {

    private var defaultNamer = { s:String -> s}

    private var countryList: List<DriveLaw> = emptyList()

    private val defaultDriveLaw = DriveLaw("")
    private val aDriveLaw = DriveLaw("A-first", 0.7,
        YoungLimit(0.15, 654321),
        ProfessionalLimit(0.23)
    )
    private val zDriveLaw = DriveLaw("Z-last")

    private lateinit var driveLawService: DriveLawService

    @BeforeEach
    fun beforeEach() {
        countryList = listOf(zDriveLaw, aDriveLaw, defaultDriveLaw)
        driveLawService = DriveLawService(defaultNamer, countryList, defaultDriveLaw)
    }

    @Test
    fun `Default is selected at first time`() {
        assertEquals(defaultDriveLaw, driveLawService.driveLaw)
    }


    @Test
    fun `Drive laws are sorted by country name (with given namer)`() {
        val countryNames = driveLawService.getListOfCountriesWithFlags("other")

        assertEquals(3, countryNames.size)
        assertTrue(countryNames[0].contains("other"))
        assertTrue(countryNames[1].contains("first"))
        assertTrue(countryNames[2].contains("last"))
    }

    @Test
    fun `Drive law is selectable by country code (with default fallback)`() {
        driveLawService.select("A-first")
        val a = driveLawService.driveLaw

        driveLawService.select("gibberish")
        val default = driveLawService.driveLaw

        assertEquals(aDriveLaw, a)
        assertEquals(defaultDriveLaw, default)
    }

    @Test
    fun `Drive law is selectable by position with default fallback`() {
        driveLawService.select(1)
        val a = driveLawService.driveLaw

        driveLawService.select(-3)
        val default = driveLawService.driveLaw

        assertEquals(aDriveLaw, a)
        assertEquals(defaultDriveLaw, default)
    }
    
    @Test
    fun `Drive limit is regular one by default`() {
        driveLawService.select("A-first")

        assertEquals(0.7, driveLawService.driveLimit())
    }

    @Test
    fun `Drive limit is young one when young, assumed lower than regular`() {
        driveLawService.select("A-first")
        driveLawService.isYoung = true

        assertEquals(0.15, driveLawService.driveLimit())
    }

    @Test
    fun `Drive limit is professional one when professional, assumed lower than regular`() {
        driveLawService.select("A-first")
        driveLawService.isProfessional = true

        assertEquals(0.23, driveLawService.driveLimit())
    }

}