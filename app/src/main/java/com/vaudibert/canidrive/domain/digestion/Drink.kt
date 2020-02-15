package com.vaudibert.canidrive.domain.digestion

import java.util.*

const val ALCOHOL_DENSITY = 0.8

data class Drink(val volume:Double, val degree:Double, val ingestionTime: Date) {
    fun alcoholMass(): Double = degree/100 * volume * ALCOHOL_DENSITY

    companion object Data {

        // degrees in % : 2.5 = 2.5%
        val degrees = doubleArrayOf(
            0.5,
            2.5,
            3.0,
            3.5,
            4.0,
            4.5,
            5.0,
            5.5,
            6.0,
            6.5,
            7.0,
            7.5,
            8.0,
            9.0,
            10.0,
            11.0,
            12.0,
            13.0,
            14.0,
            15.0,
            17.0,
            20.0,
            25.0,
            30.0,
            40.0,
            60.0,
            80.0)

        // Volumes in mL
        val volumes = doubleArrayOf(
            50.0,
            80.0,
            130.0,
            200.0,
            250.0,
            330.0,
            500.0,
            750.0,
            1000.0)

    }
}



