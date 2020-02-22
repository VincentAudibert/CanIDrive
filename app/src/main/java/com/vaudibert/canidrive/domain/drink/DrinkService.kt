package com.vaudibert.canidrive.domain.drink

import java.util.*
import kotlin.collections.ArrayList

class DrinkService {

    val ingestedDrinks : MutableList<IngestedDrink> = ArrayList()

    var ingestCallback = { _: IngestedDrink -> }
    var removeCallback = { _: IngestedDrink -> }

    var onPresetUpdated = { _: PresetDrink -> }
    var onPresetAdded = { _:PresetDrink -> }

    var presetDrinks = mutableListOf<PresetDrink>()

    fun ingestForInit(initDrinks: List<IngestedDrink>) {
        ingestedDrinks.addAll(initDrinks)
        ingestedDrinks.sortBy {
                absorbedDrink -> absorbedDrink.ingestionTime.time
        }
    }

    fun ingest(presetDrink: PresetDrink, ingestionTime: Date) {
        if (presetDrink !in presetDrinks) addNewPreset(presetDrink)

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
        onPresetUpdated(presetDrink)
        ingestCallback(ingested)
    }

    fun ingestCustom(name:String, volume:Double, degree:Double, ingestionTime: Date) {
        val newPreset = PresetDrink(name, volume, degree)
        addNewPreset(newPreset)
        ingest(newPreset, ingestionTime)
        presetDrinks.sortByDescending { presetDrink -> presetDrink.count }
    }

    private fun addNewPreset(newPreset: PresetDrink) {
        presetDrinks.add(newPreset)
        onPresetAdded(newPreset)
    }

    fun remove(ingestedDrink: IngestedDrink) {
        ingestedDrinks.remove(ingestedDrink)
        removeCallback(ingestedDrink)
    }
}