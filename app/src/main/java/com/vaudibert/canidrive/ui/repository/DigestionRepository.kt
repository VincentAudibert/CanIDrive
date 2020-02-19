package com.vaudibert.canidrive.ui.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.digestion.DigestionService
import com.vaudibert.canidrive.domain.digestion.PhysicalBody
import com.vaudibert.canidrive.domain.drink.DrinkService

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
class DigestionRepository(context: Context, drinkService: DrinkService) {

    // Main instance to link
    val body = PhysicalBody()

    val digestionService = DigestionService(body, drinkService)

    val toleranceLevels = listOf(
        context.getString(R.string.alcohol_tolerance_low),
        context.getString(R.string.alcohol_tolerance_medium),
        context.getString(R.string.alcohol_tolerance_high)
    )

    private val _liveDrinker = MutableLiveData<PhysicalBody>()
    val liveDrinker:LiveData<PhysicalBody>
            get() = _liveDrinker

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
    }


}