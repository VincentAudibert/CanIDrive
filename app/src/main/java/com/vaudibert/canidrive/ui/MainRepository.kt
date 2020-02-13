package com.vaudibert.canidrive.ui

import android.content.Context
import android.content.SharedPreferences
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.DrinkerStatusService

class MainRepository {

    val drinkerRepository = DrinkerRepository()
    val driveLawRepository = DriveLawRepository()
    val drinkerStatusService = DrinkerStatusService(
        drinkerRepository.digestionService,
        driveLawRepository.driveLawService
    )

    // Flag for initialization, saved when set.
    var init : Boolean = false
        set(value) {
            field = value
            sharedPref.edit()
                .putBoolean(context.getString(R.string.user_initialized), value)
                .apply()
        }

    // References needed for SharedPreferences operations
    private lateinit var context: Context
    private lateinit var sharedPref: SharedPreferences

    /**
     * Context (main activity) reference set.
     *
     * Triggers the retrieval of SharedPreferences-linked values.
     */
    fun setContext(context: Context) {
        this.context = context
        sharedPref = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE)
        init = sharedPref.getBoolean(context.getString(R.string.user_initialized), false)

        drinkerRepository.setContext(context)
        driveLawRepository.setContext(context)

    }
}