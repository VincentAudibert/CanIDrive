package com.vaudibert.canidrive.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DriveLawsTest {

    @Test
    fun `position of other country ("") is 0`() {
        assertEquals(0, DriveLaws.getIndexOf(""))
    }

    @Test
    fun `position of FR country is greater than 0`() {
        assertTrue(DriveLaws.getIndexOf("FR") > 0)
    }
}