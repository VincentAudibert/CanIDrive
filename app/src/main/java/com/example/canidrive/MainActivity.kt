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

    val drinks:MutableList<Drink> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        buttonAddDrink.setOnClickListener {
            try {
                var quantity = editTextQuantity.text.toString().toInt()
                var degree = editTextDegree.text.toString().toFloat()
                addDrink(Drink(quantity, degree))

                editTextQuantity.text.clear()
                editTextDegree.text.clear()
                editTextBefore.text.clear()

            } catch (e:Exception) {
                this.longToast("You did not correctly fill in the values \nPlease try again")
                return@setOnClickListener
            }
        }
    }

    private fun addDrink(drink: Drink) {
        val newDrink = TextView(this)
        newDrink.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        newDrink.text = drink.toString()

        linearPastDrinks?.addView(newDrink)
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
