package com.vaudibert.canidrive.domain.drink

import java.util.*
import kotlin.collections.ArrayList

class DrinkService {

    val ingestedDrinks : MutableList<IngestedDrink> = ArrayList()
    var presetDrinks = mutableListOf<PresetDrink>()

    var ingestCallback = { _: IngestedDrink -> }
    var removeCallback = { _: IngestedDrink -> }

    var onPresetUpdated = { _: PresetDrink -> }
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
            onPresetUpdated(presetDrink)
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
}