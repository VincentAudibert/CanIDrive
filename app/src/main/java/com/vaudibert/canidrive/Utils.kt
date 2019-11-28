package com.vaudibert.canidrive

import java.util.*

fun hoursBetween(start: Date, end: Date): Double = ((end.time - start.time).toDouble() / (3600*1000))