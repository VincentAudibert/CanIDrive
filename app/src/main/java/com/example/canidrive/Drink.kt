package com.example.canidrive

import java.util.*

class Drink(val qtyMilliLiter:Int, val degree:Float, val ingestionDate:Date) {


    override fun toString(): String {
        return "$qtyMilliLiter cL at $degree % at $ingestionDate"
    }
}
