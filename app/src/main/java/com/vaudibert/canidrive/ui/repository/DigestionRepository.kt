package com.vaudibert.canidrive.ui.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.data.DrinkDatabase
import com.vaudibert.canidrive.domain.digestion.DigestionService
import com.vaudibert.canidrive.domain.digestion.Drink
import com.vaudibert.canidrive.domain.digestion.PhysicalBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Repository holding the drinker and driveLaw instances.
 *
 * It establishes the link between the drinker absolute state and the drive law limits that depend
 * on the country selection.
 *
 * It is responsible for retrieving all saved values with the given :
 *  - sharedPreferences instance for :
 *      - weight
 *      - sex
 *      - country code (= drive law)
 *      - init flag (= user configuration already validated once)
 *  - drinkDao for past consumed drinks.
 */
class DigestionRepository(context: Context, roomDatabase: DrinkDatabase) {

    // Main instance to link
    val body = PhysicalBody()

    val digestionService = DigestionService(body)

    val toleranceLevels = listOf(
        context.getString(R.string.alcohol_tolerance_low),
        context.getString(R.string.alcohol_tolerance_medium),
        context.getString(R.string.alcohol_tolerance_high)
    )

    private val _liveDrinker = MutableLiveData<PhysicalBody>()
    private val _livePastDrinks = MutableLiveData<MutableList<Drink>>()

    val liveDrinker:LiveData<PhysicalBody>
            get() = _liveDrinker
    val livePastDrinks: LiveData<MutableList<Drink>>
            get() = _livePastDrinks

    init {
        val sharedPref = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE)

        val weight = sharedPref.getFloat(context.getString(R.string.user_weight), 70F).toDouble()
        val sex = sharedPref.getString(context.getString(R.string.user_sex), "NONE") ?: "NONE"
        val tolerance = sharedPref.getFloat(context.getString(R.string.user_tolerance), 0.0F).toDouble()

        body.sex = sex
        body.weight = weight
        body.alcoholTolerance = tolerance

        body.onUpdate = { updatedSex: String, updatedWeight: Double, updatedTolerance:Double -> run {
            sharedPref
                .edit()
                .putString(context.getString(R.string.user_sex), updatedSex)
                .putFloat(context.getString(R.string.user_weight), updatedWeight.toFloat())
                .putFloat("USER_TOLERANCE", updatedTolerance.toFloat())
                .apply()
            _liveDrinker.value = body
        } }

        _liveDrinker.value = body


        val drinkDao = roomDatabase.drinkDao()

        val daoJob = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + daoJob)

        // Add all drinks to current state
        uiScope.launch {
            val drinks = drinkDao.getAll()
            drinks.forEach { digestionService.ingest(it.toDrink())}
            _livePastDrinks.postValue(digestionService.absorbedDrinks)
        }

        // Then set callbacks to keep DB updated
        digestionService.ingestCallback = {
                drink -> run {
            uiScope.launch { drinkDao.insert(drink) }
        }
            _livePastDrinks.postValue(digestionService.absorbedDrinks)
        }

        digestionService.removeCallback = {
                drink -> run {
            uiScope.launch { drinkDao.remove(drink) }
        }
            _livePastDrinks.postValue(digestionService.absorbedDrinks)
        }
    }


}