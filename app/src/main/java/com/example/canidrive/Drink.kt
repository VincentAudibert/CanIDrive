package com.example.canidrive

import java.util.*

class Drink(val quantity:Int, val degree:Float, val ingestionTime:Date) {


    override fun toString(): String {
        return "$quantity cL at $degree % at $ingestionTime"
    }
}
