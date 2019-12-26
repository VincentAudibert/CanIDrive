package com.vaudibert.canidrive.domain

import java.text.SimpleDateFormat
import java.util.*

const val ALCOHOL_DENSITY = 0.8

data class Drink(val volume:Double, val degree:Double, val ingestionTime: Date) {
    fun alcoholMass(): Double = degree/100 * volume * ALCOHOL_DENSITY

    override fun toString(): String {
        val dateFormat = SimpleDateFormat("HH:mm")
        return "You drank $volume mL at $degree% at " + dateFormat.format(ingestionTime)
    }
}

