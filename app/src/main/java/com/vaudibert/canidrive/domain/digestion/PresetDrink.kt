package com.vaudibert.canidrive.domain.digestion

data class PresetDrink(val volume: Double, val degree: Double, val name: String) {

    companion object {
        val list = listOf(
            PresetDrink(130.0, 13.0, "wine glass"),
            PresetDrink(250.0, 4.5, "Half-a-pint light beer"),
            PresetDrink(500.0, 4.5, "Pint light beer"),
            PresetDrink(33.0, 9.0, "33cl triple"),
            PresetDrink(250.0, 2.5, "Half-a-pint cider"),
            PresetDrink(8.0, 17.0, "Apetizer (Martini)"),
            PresetDrink(5.0, 40.0, "Shot")
        )
    }
}