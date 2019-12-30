package com.vaudibert.canidrive.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.data.DrinkDao
import com.vaudibert.canidrive.domain.Drink
import com.vaudibert.canidrive.domain.Drinker
import com.vaudibert.canidrive.domain.DriveLaw
import com.vaudibert.canidrive.domain.DriveLaws
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class DrinkerRepository {

    private var drinker = Drinker()

    private var driveLaw : DriveLaw? = null

    var init : Boolean = false
        set(value) {
            field = value
            sharedPref.edit()
                .putBoolean(context.getString(R.string.user_initialized), value)
                .apply()
        }

    private var daoJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + daoJob)

    private lateinit var drinkDao : DrinkDao

    private lateinit var context: Context

    private lateinit var sharedPref: SharedPreferences

    val liveDrinker = MutableLiveData<Drinker>(drinker)

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
        sharedPref
            .edit()
            .putString(context.getString(R.string.countryCode), driveLaw?.countryCode ?: "")
            .putFloat(context.getString(R.string.user_weight), weight.toFloat())
            .apply()

        liveDrinker.value = drinker
    }

    fun setSex(sex: String) {
        drinker.sex = sex
        sharedPref
            .edit()
            .putString(context.getString(R.string.user_sex), sex)
            .apply()
        liveDrinker.value = drinker
    }

    fun getDrinks() = drinker.getDrinks()

    fun getWeight() = drinker.weight

    fun getSex() = drinker.sex

    fun canDrive(): Boolean {
        return drinker.alcoholRateAt(Date()) <= driveLaw?.limit ?:0.01
    }

    fun timeToDrive(): Date {
        return drinker.timeToReachLimit(driveLaw?.limit ?: 0.01)
    }

    fun setDriveLaw(driveLaw: DriveLaw?) {
        this.driveLaw = driveLaw
        sharedPref.edit()
            .putString(context.getString(R.string.countryCode), driveLaw?.countryCode ?: "")
            .apply()
    }

    fun setContext(context: Context) {
        this.context = context
        sharedPref = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE)

        val weight = sharedPref.getFloat(context.getString(R.string.user_weight), 70F).toDouble()
        val sex = sharedPref.getString(context.getString(R.string.user_sex), "NONE") ?: "NONE"
        val countryCode = sharedPref.getString(context.getString(R.string.countryCode), "")
        init = sharedPref.getBoolean(context.getString(R.string.user_initialized), false)

        drinker = Drinker(weight, sex)
        driveLaw = DriveLaws.countryLaws.find { law -> law.countryCode == countryCode }
        liveDrinker.value = drinker

    }

    fun getCountryPosition() : Int {
        return DriveLaws.countryLaws.indexOf(driveLaw).coerceAtLeast(0)
    }
}