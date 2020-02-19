package com.vaudibert.canidrive.ui.repository

import android.content.Context
import androidx.room.Room
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.data.DrinkDatabase
import com.vaudibert.canidrive.domain.DrinkerStatusService

class MainRepository(private val context: Context) {

    private val drinkDatabase = Room
        .databaseBuilder(context, DrinkDatabase::class.java, "drink-database")
        .build()

    val drinkerRepository = DigestionRepository(context, drinkDatabase)

    val driveLawRepository = DriveLawRepository(context)

    val drinkerStatusService = DrinkerStatusService(
        drinkerRepository.digestionService,
        driveLawRepository.driveLawService
    )

    private val sharedPref = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE)


    // Flag for initialization, saved when set.
    var init : Boolean = sharedPref.getBoolean(context.getString(R.string.user_initialized), false)
        set(value) {
            field = value
            sharedPref.edit()
                .putBoolean(context.getString(R.string.user_initialized), value)
                .apply()
        }

}