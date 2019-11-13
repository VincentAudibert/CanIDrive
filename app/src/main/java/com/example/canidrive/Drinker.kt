package com.example.canidrive

import java.util.*
import kotlin.collections.ArrayList

class Drinker(weight: Float = 80F, sex: String = "NONE") {

    private val absorbedDrinks : MutableList<Drink> = ArrayList()


    fun drink (drink: Drink) {
        absorbedDrinks.add(drink)
    }

    fun alcoholLevel(date: Date) = absorbedDrinks.size.toFloat()
}