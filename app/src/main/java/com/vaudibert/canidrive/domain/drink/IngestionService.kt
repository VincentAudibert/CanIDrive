package com.vaudibert.canidrive.domain.drink

import java.util.*

class IngestionService<Preset : IPresetDrink, Ingested : IIngestedDrink>(
    private val ingestor : ( preset: Preset, ingestionTime: Date) -> Ingested
) : IIngestor<Preset>, IIngestedDrinkProvider {

    val ingestedDrinks : MutableList<Ingested> = mutableListOf()

    var onRemoved = { _ : Ingested -> }
    var onIngestedChanged = { _: List<Ingested> -> }

    override fun ingest(preset : Preset, ingestionTime : Date) {
        val ingested =  ingestor(preset, ingestionTime)
        ingestedDrinks.add(ingested)
        sortAndListCallBack()
    }

    override fun getDrinks(): List<IIngestedDrink> = ingestedDrinks

    fun remove(ingested : Ingested) {
        ingestedDrinks.remove(ingested)
        onRemoved(ingested)
        sortAndListCallBack()
    }

    fun populate(ingests : List<Ingested>) {
        ingestedDrinks.addAll(ingests)
        sortAndListCallBack()
    }

    private fun sortAndListCallBack() {
        ingestedDrinks.sortBy { it.ingestionTime.time }
        onIngestedChanged(ingestedDrinks)
    }
}