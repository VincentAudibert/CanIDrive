package com.vaudibert.canidrive.domain.drink

import java.util.*

interface IIngestor<Preset : IPresetDrink> {
    fun ingest(preset : Preset, ingestionTime : Date)
}