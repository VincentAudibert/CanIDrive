package com.vaudibert.canidrive.domain.drink

import java.util.*

class PresetDrinkService<Preset : IPresetDrink>(
    private val presetMaker : (name: String, volume:Double, degree: Double) -> Preset
) {
    var onPresetRemoved = { _:Preset -> }
    var onPresetsChanged = { _: List<Preset> -> }
    var onPresetUpdated = { _:Preset -> }

    var onSelectUpdated = { _:Preset? -> }

    var ingestionService : IIngestCapable<Preset>? = null

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
        selectedPreset = newPreset
        sortAndCallbackPresets()
    }

    private fun sortAndCallbackPresets() {
        presetDrinks.sortByDescending { it.count }
        onPresetsChanged(presetDrinks)
    }

    fun removePreset(presetDrink: Preset) {
        presetDrinks.remove(presetDrink)
        if (selectedPreset == presetDrink) selectedPreset = null
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

    fun ingest(ingestionTime: Date) {
        val ingester = ingestionService
        val preset = selectedPreset
        if (ingester != null && preset != null) {
            preset.count++
            onPresetUpdated(preset)
            sortAndCallbackPresets()
            ingester.ingest(preset, ingestionTime)
        }
    }

}