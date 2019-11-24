package com.example.canidrive

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val drinker = Drinker(50.0, "NONE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        buttonAddDrink.setOnClickListener {
            try {
                var quantity = editTextQuantity.text.toString().toInt()
                var degree = editTextDegree.text.toString().toFloat()
                var ingestionTime = Date(Date().time - (editTextBefore.text.toString().toLong() * 60000))

                drinker.ingest(Drink(quantity, degree), ingestionTime)

                updateDriveStatus()

                editTextQuantity.text.clear()
                editTextDegree.text.clear()
                editTextBefore.text.clear()

            } catch (e:Exception) {
                this.longToast("You did not correctly fill in the values \nPlease try again")
                return@setOnClickListener
            }
        }
    }

    private fun updateDriveStatus() {
        textViewDriveStatus.text = if (drinker.alcoholRateAt(Date()) < 0.5)
            "DRIVE : YES"
        else
            "DRIVE : NO"
    }


    private fun Context.longToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

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
