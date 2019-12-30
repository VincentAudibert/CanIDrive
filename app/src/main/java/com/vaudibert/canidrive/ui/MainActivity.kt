package com.vaudibert.canidrive.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.data.DrinkDatabase

class MainActivity : AppCompatActivity() {

    val drinkerRepository = DrinkerRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drinkerRepository.setContext(this)

        val drinkDB = Room
            .databaseBuilder(this, DrinkDatabase::class.java, "drink-database")
            .build()

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
