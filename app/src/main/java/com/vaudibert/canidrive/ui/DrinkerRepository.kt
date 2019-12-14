package com.vaudibert.canidrive.ui

import androidx.lifecycle.MutableLiveData
import com.vaudibert.canidrive.domain.AbsorbedDrink
import com.vaudibert.canidrive.domain.Drink
import com.vaudibert.canidrive.domain.Drinker
import java.util.*

object DrinkerRepository {
    private var drinker = Drinker()

    val liveDrinker = MutableLiveData<Drinker>(drinker)

    fun setDrinker(drinker: Drinker) {
        this.drinker = drinker
        liveDrinker.value = drinker
    }

    fun remove(drink: AbsorbedDrink) {
        drinker.remove(drink)
        liveDrinker.value = drinker
    }

    fun ingest(drink : Drink, ingestionTime : Date) {
        drinker.ingest(drink, ingestionTime)
        liveDrinker.value = drinker
    }

    fun setWeight(weight: Double) {
        drinker.weight = weight
        liveDrinker.value = drinker
    }

    fun setSex(sex: String) {
        drinker.sex = sex
        liveDrinker.value = drinker
    }

    fun getDrinks() = drinker.getDrinks()

    fun getWeight() = drinker.weight

    fun getSex() = drinker.sex

}