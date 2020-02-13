package com.vaudibert.canidrive.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.DriveLaw
import com.vaudibert.canidrive.domain.DriveLawService
import java.util.*

class DriveLawRepository {

    val driveLawService = DriveLawService { code: String -> Locale("", code).displayCountry}

    private val _liveDriveLaw = MutableLiveData<DriveLaw>()
    private val _liveIsYoung = MutableLiveData<Boolean>()
    private val _liveIsProfessional = MutableLiveData<Boolean>()
    private val _liveCustomCountryLimit = MutableLiveData<Double>()


    val liveDriveLaw: LiveData<DriveLaw>
            get() = _liveDriveLaw
    val liveIsYoung: LiveData<Boolean>
            get() = _liveIsYoung
    val liveIsProfessional: LiveData<Boolean>
            get() = _liveIsProfessional
    val liveCustomCountryLimit: LiveData<Double>
            get() = _liveCustomCountryLimit

    // References needed for SharedPreferences operations
    private lateinit var context: Context
    private lateinit var sharedPref: SharedPreferences

    fun setContext(context: Context) {
        this.context = context
        sharedPref = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE)

        // Initiate drive law service
        driveLawService.isYoung = sharedPref.getBoolean(context.getString(R.string.user_young_driver), false)
        driveLawService.isProfessional = sharedPref.getBoolean(context.getString(R.string.user_professional_driver), false)

        driveLawService.select(
            sharedPref.getString(context.getString(R.string.countryCode), "") ?: ""
        )
        driveLawService.customCountryLimit= sharedPref.getFloat(context.getString(R.string.customCountryLimit), 0.0F).toDouble()

        // Set callbacks once drive law service initialized
        driveLawService.onSelectCallback = {
            countryCode:String ->
            sharedPref.edit()
                .putString(context.getString(R.string.countryCode), countryCode)
                .apply()
            _liveDriveLaw.value = driveLawService.driveLaw
        }

        driveLawService.onYoungCallback = {
            isYoung: Boolean ->
            sharedPref.edit()
                .putBoolean(context.getString(R.string.user_young_driver), isYoung)
                .apply()
            _liveIsYoung.value = driveLawService.isYoung
            _liveDriveLaw.value = driveLawService.driveLaw
        }

        driveLawService.onProfessionalCallback = {
            isProfessional: Boolean ->
            sharedPref.edit()
                .putBoolean(context.getString(R.string.user_professional_driver), isProfessional)
                .apply()
            _liveIsProfessional.value = driveLawService.isProfessional
            _liveDriveLaw.value = driveLawService.driveLaw
        }

        driveLawService.onCustomLimitCallback = {
            newLimit:Double ->
            sharedPref.edit()
                .putFloat(context.getString(R.string.customCountryLimit), newLimit.toFloat())
                .apply()
            _liveCustomCountryLimit.value = driveLawService.customCountryLimit
        }

        // Initialize live datas
        _liveDriveLaw.value = driveLawService.driveLaw
        _liveIsYoung.value = driveLawService.isYoung
        _liveIsProfessional.value = driveLawService.isProfessional
        _liveCustomCountryLimit.value = driveLawService.customCountryLimit
    }
}