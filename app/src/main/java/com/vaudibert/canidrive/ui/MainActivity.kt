package com.vaudibert.canidrive.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.room.Room
import com.google.android.material.appbar.AppBarLayout
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.data.DrinkDatabase
import kotlinx.android.synthetic.main.activity_main.*

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

        findNavController(R.id.nav_host_fragment)
            .addOnDestinationChangedListener { _, destination, _ ->
                appBarDrinker.visibility = if (destination.id == R.id.splashFragment)
                    AppBarLayout.GONE
                else
                    AppBarLayout.VISIBLE
                toolbar.title =  when (destination.id) {
                    R.id.driveFragment -> getString(R.string.can_i_drive_question)
                    R.id.drinkerFragment -> getString(R.string.about_you)
                    R.id.addDrinkFragment -> getString(R.string.add_a_drink)
                    else -> ""

                }
            }
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
