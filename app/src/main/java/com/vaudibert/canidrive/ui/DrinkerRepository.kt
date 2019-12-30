package com.vaudibert.canidrive.ui

import androidx.lifecycle.MutableLiveData
import com.vaudibert.canidrive.data.DrinkDao
import com.vaudibert.canidrive.domain.Drink
import com.vaudibert.canidrive.domain.Drinker
import com.vaudibert.canidrive.domain.DriveLaw
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class DrinkerRepository {
    private var drinker = Drinker()

    var driveLaw : DriveLaw? = null

    private var daoJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + daoJob)

    private lateinit var drinkDao : DrinkDao

    var liveDrinker = MutableLiveData<Drinker>(drinker)

    fun setDrinker(drinker: Drinker) {
        this.drinker = drinker
        liveDrinker.value = drinker
    }

    fun setDao(drinkDao: DrinkDao) {
        this.drinkDao = drinkDao
        uiScope.launch {
            val drinks = drinkDao.getAll()
            drinks.forEach { drinker.ingest(it.toDrink())}
            liveDrinker.postValue(drinker)
        }
    }

    fun ingest(drink : Drink) {
        drinker.ingest(drink)
        uiScope.launch { drinkDao.insert(drink) }
        liveDrinker.value = drinker
    }

    fun remove(drink: Drink) {
        drinker.remove(drink)
        uiScope.launch { drinkDao.remove(drink) }
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

    fun getLaw(): DriveLaw? = driveLaw

    fun canDrive(): Boolean {
        return drinker.alcoholRateAt(Date()) <= driveLaw?.limit ?:0.01
    }

    fun timeToDrive(): Date {
        return drinker.timeToReachLimit(driveLaw?.limit ?: 0.01)
    }
}