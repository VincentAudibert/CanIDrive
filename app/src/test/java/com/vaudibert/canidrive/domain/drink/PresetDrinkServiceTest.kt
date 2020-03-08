package com.vaudibert.canidrive.domain.drink

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach

internal class PresetDrinkServiceTest {

    private lateinit var presetService: PresetDrinkService<PresetDrink>
    private lateinit var exportPreset: List<PresetDrink>

    @BeforeEach
    fun setup() {
        presetService = PresetDrinkService {
                name: String, volume: Double, degree: Double ->
            PresetDrink(name, volume, degree)
        }
        presetService.onPresetsChanged = {
            exportPreset = it
        }
    }

    @Test
    fun `preset service is initialized empty`() {
        // FIXME : why the hell is beforeEach not run ???
        setup()
        presetService.populate(emptyList())
        assertEquals(0, exportPreset.size)
        assertNull(presetService.selectedPreset)
    }

}