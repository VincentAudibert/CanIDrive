package com.vaudibert.canidrive.domain.drink

import java.util.*
import kotlin.collections.ArrayList

class DrinkService {

    val ingestedDrinks : MutableList<IngestedDrink> = ArrayList()
    var presetDrinks = mutableListOf<PresetDrink>()
    var selectedPreset: PresetDrink? = null

    var ingestCallback = { _: IngestedDrink -> }
    var removeCallback = { _: IngestedDrink -> }

    var onPresetUpdated = { previous: PresetDrink, updated: PresetDrink -> }
    var onPresetIngested = {_:PresetDrink -> }
    var onPresetAdded = { _:PresetDrink -> }
    var onPresetRemoved = { _:PresetDrink -> }

    fun ingestForInit(initDrinks: List<IngestedDrink>) {
        ingestedDrinks.addAll(initDrinks)
        ingestedDrinks.sortBy {
                absorbedDrink -> absorbedDrink.ingestionTime.time
        }
    }

    fun ingest(presetDrink: PresetDrink, ingestionTime: Date) {

        val ingested = IngestedDrink(
            presetDrink.name,
            presetDrink.volume,
            presetDrink.degree,
            ingestionTime
        )
        ingestedDrinks.add(ingested)
        ingestedDrinks.sortBy {
                absorbedDrink -> absorbedDrink.ingestionTime.time
        }
        presetDrink.count++
        if (presetDrink !in presetDrinks) {
            presetDrinks.add(presetDrink)
            onPresetAdded(presetDrink)
        }
        else
            onPresetIngested(presetDrink)
        ingestCallback(ingested)
    }

    fun remove(ingestedDrink: IngestedDrink) {
        ingestedDrinks.remove(ingestedDrink)
        removeCallback(ingestedDrink)
    }

    fun removePreset(presetDrink: PresetDrink) {
        presetDrinks.remove(presetDrink)
        onPresetRemoved(presetDrink)
    }

    fun addNewPreset(name: String, volume: Double, degree: Double) {
        val newPreset = PresetDrink(name, volume, degree)
        if (newPreset in presetDrinks) return

        presetDrinks.add(newPreset)
        onPresetAdded(newPreset)
    }

    fun updatePreset(name: String, volume: Double, degree: Double, count:Int) {
        val currentSelected = selectedPreset
        if (currentSelected == null)
            addNewPreset(name, volume, degree)
        else {
            val updatedPreset = PresetDrink(name, volume, degree, count)
            val selectedIndex = presetDrinks.indexOf(currentSelected)
            presetDrinks[selectedIndex] = updatedPreset
            selectedPreset = updatedPreset
            onPresetUpdated(currentSelected, updatedPreset)
        }
    }
}