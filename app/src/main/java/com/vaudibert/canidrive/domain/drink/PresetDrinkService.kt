package com.vaudibert.canidrive.domain.drink

import java.util.*

class PresetDrinkService<Preset : IPresetDrink>(
    private val presetMaker : (name: String, volume:Double, degree: Double) -> Preset
) : IIngestor<Preset> {
    var onPresetRemoved = { _:Preset -> }
    var onPresetsChanged = { _: List<Preset> -> }
    var onPresetUpdated = { _:Preset -> }

    var onSelectUpdated = { _:Preset? -> }

    var ingestionService : IIngestor<Preset>? = null

    var selectedPreset : Preset? = null
        set(value) {
            field = value
            onSelectUpdated(field)
        }

    private var presetDrinks : MutableList<Preset> = mutableListOf()

    fun populate(presets : List<Preset>) {
        presetDrinks.addAll(presets)
        sortAndCallbackPresets()
    }

    fun addNewPreset(name: String, volume: Double, degree: Double) {
        val newPreset = presetMaker(name, volume, degree)
        presetDrinks.add(newPreset)
        sortAndCallbackPresets()
    }

    private fun sortAndCallbackPresets() {
        presetDrinks.sortByDescending { it.count }
        onPresetsChanged(presetDrinks)
    }

    fun removePreset(presetDrink: Preset) {
        presetDrinks.remove(presetDrink)
        onPresetRemoved(presetDrink)
        sortAndCallbackPresets()
    }

    fun updateSelectedPreset(name: String, volume: Double, degree: Double) {
        val currentSelected = selectedPreset
        if (currentSelected == null)
            addNewPreset(name, volume, degree)
        else {
            currentSelected.name = name
            currentSelected.volume = volume
            currentSelected.degree = degree
            selectedPreset = currentSelected
            onPresetUpdated(currentSelected)
            sortAndCallbackPresets()
        }
    }

    override fun ingest(preset: Preset, ingestionTime: Date) {
        val ingestor = ingestionService
        if (ingestor != null) {
            preset.count++
            onPresetUpdated(preset)
            sortAndCallbackPresets()
            ingestor.ingest(preset, ingestionTime)

        }
    }

}