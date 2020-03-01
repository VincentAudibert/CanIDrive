package com.vaudibert.canidrive.ui.repository

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.data.DrinkDatabase
import com.vaudibert.canidrive.domain.DrinkerStatusService

class MainRepository(private val context: Context) {

    private val migration_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE DrinkEntity ADD COLUMN `name` VARCHAR")
            database.execSQL("ALTER TABLE DrinkEntity RENAME TO IngestedDrinkEntity")
        }
    }


    private val drinkDatabase = Room
        .databaseBuilder(context, DrinkDatabase::class.java, "drink-database")
        //.addMigrations(migration_1_2) // commented as crashing, not priority :-(
        .fallbackToDestructiveMigration()
        .build()

    val drinkRepository = DrinkRepository(drinkDatabase)

    val digestionRepository = DigestionRepository(context, drinkRepository.drinkService)

    val driveLawRepository = DriveLawRepository(context)

    val drinkerStatusService = DrinkerStatusService(
        digestionRepository.digestionService,
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