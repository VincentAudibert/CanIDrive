package com.vaudibert.canidrive.domain.drink

import java.util.*
import kotlin.collections.ArrayList

class DrinkService {

    val absorbedIngestedDrinks : MutableList<IngestedDrink> = ArrayList()

    var ingestCallback = { _: IngestedDrink -> }
    var removeCallback = { _: IngestedDrink -> }
    var onPresetUpdated = { _: PresetDrink -> }

    val presetDrinks : MutableList<PresetDrink> = mutableListOf(
        PresetDrink(
            130.0,
            13.0,
            "wine glass"
        ),
        PresetDrink(
            250.0,
            4.5,
            "Half-a-pint light beer"
        ),
        PresetDrink(
            500.0,
            4.5,
            "Pint light beer"
        ),
        PresetDrink(
            33.0,
            9.0,
            "33cl triple"
        ),
        PresetDrink(
            250.0,
            2.5,
            "Half-a-pint cider"
        ),
        PresetDrink(
            8.0,
            17.0,
            "Apetizer (Martini)"
        ),
        PresetDrink(5.0, 40.0, "Shot")
    )

    fun ingestForInit(ingestedDrink: IngestedDrink) {
        absorbedIngestedDrinks.add(ingestedDrink)
        absorbedIngestedDrinks.sortBy {
                absorbedDrink -> absorbedDrink.ingestionTime.time
        }
    }

    fun ingest(presetDrink: PresetDrink, ingestionTime: Date) {
        val ingested = IngestedDrink(presetDrink.volume, presetDrink.degree, ingestionTime)
        absorbedIngestedDrinks.add(ingested)
        absorbedIngestedDrinks.sortBy {
                absorbedDrink -> absorbedDrink.ingestionTime.time
        }
        //presetDrink.count++
        onPresetUpdated(presetDrink)
        ingestCallback(ingested)
    }

    fun ingestCustom(name:String, volume:Double, degree:Double, ingestionTime: Date) {
        val newPreset = PresetDrink(volume, degree, name)
        presetDrinks.add(newPreset)
        ingest(newPreset, ingestionTime)
        //presetDrinks.sortByDescending { presetDrink -> presetDrink.count }
    }

    fun remove(ingestedDrink: IngestedDrink) {
        absorbedIngestedDrinks.remove(ingestedDrink)
        removeCallback(ingestedDrink)
    }
}