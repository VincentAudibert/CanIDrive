package com.vaudibert.canidrive.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class DriveLawServiceTest {

    var defaultNamer = { s:String -> s}

    var countryList: List<DriveLaw> = emptyList()

    var defaultDriveLaw = DriveLaw("")

    lateinit var driveLawService: DriveLawService

    @Test
    fun `Default is selected at first time`() {
        countryList = listOf(
            DriveLaw("Z-last"),
            DriveLaw("A-first"),
            defaultDriveLaw
        )

        driveLawService = DriveLawService(defaultNamer, countryList, defaultDriveLaw)
        assertEquals(defaultDriveLaw, driveLawService.driveLaw)
    }


    @Test
    fun `Drive laws are sorted by country name`() {
        countryList = listOf(
            DriveLaw("Z-last"),
            DriveLaw("A-first"),
            defaultDriveLaw
        )

        driveLawService = DriveLawService(defaultNamer, countryList, defaultDriveLaw)

        val countryNames = driveLawService.getListOfCountriesWithFlags("other")

        assertEquals(3, countryNames.size)
        assertTrue(countryNames[0].contains("other"))
        assertTrue(countryNames[1].contains("first"))
        assertTrue(countryNames[2].contains("last"))
    }

}