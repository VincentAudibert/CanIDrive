package com.vaudibert.canidrive.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.data.DrinkDatabase
import com.vaudibert.canidrive.domain.Drinker
import com.vaudibert.canidrive.domain.DriveLaws

class MainActivity : AppCompatActivity() {

    val drinkerRepository = DrinkerRepository()

    var init = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO : pass sharedPref to repo and let it fetch its needs.
        val sharedPref = this.getSharedPreferences(getString(R.string.user_preferences), Context.MODE_PRIVATE)

        val weight = sharedPref.getFloat(getString(R.string.user_weight), 70F).toDouble()
        val sex = sharedPref.getString(getString(R.string.user_sex), "NONE") ?: "NONE"
        val countryCode = sharedPref.getString(getString(R.string.countryCode), "")
        init = sharedPref.getBoolean(getString(R.string.user_initialized), false)

        val drinker = Drinker(weight, sex)
        drinkerRepository.driveLaw = DriveLaws.countryLaws.find { law -> law.countryCode == countryCode }

        val drinkDB = Room
            .databaseBuilder(this, DrinkDatabase::class.java, "drink-database")
            .build()

        drinkerRepository.setDrinker(drinker)
        drinkerRepository.setDao(drinkDB.drinkDao())
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
