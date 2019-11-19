package com.example.canidrive

import java.text.SimpleDateFormat
import java.util.*

class Drink(val qtyMilliLiter:Int, val degree:Float, val ingestionDate:Date) {


    override fun toString(): String {

        val dateFormat = SimpleDateFormat("HH:mm dd/mm/yyyy")

        return "$qtyMilliLiter cL at $degree % at " + dateFormat.format(ingestionDate)
    }
}
