package com.example.canidrive

import java.text.SimpleDateFormat
import java.util.*

class Drink(val qtyMilliLiter:Int, val degree:Float, val ingestionDate:Date) {


    override fun toString(): String {

        val dateFormat = SimpleDateFormat("HH:mm")

        return "You drank $qtyMilliLiter mL at $degree% at " + dateFormat.format(ingestionDate)
    }
}
