package com.vaudibert.canidrive.domain.drink

import java.util.*

class TestIIngestor : IIngestor<PresetDrink> {
    val ingestedList : MutableList<IngestedDrink> = emptyList<IngestedDrink>().toMutableList()

    override fun ingest(preset: PresetDrink, ingestionTime: Date) {
        val ingested = IngestedDrink(preset.name, preset.volume, preset.degree, ingestionTime)
        ingestedList.add(ingested)
    }
}