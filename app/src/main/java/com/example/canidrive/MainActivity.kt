package com.example.canidrive

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val drinks:MutableList<Drink> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        button_add_drink.setOnClickListener {
            try {
                var quantity = edit_text_quantity.text.toString().toInt()
                var degree = edit_text_degree.text.toString().toFloat()
                var delay = edit_text_before.text.toString().toInt()
                var ingestionTime = Date(Date().time - delay * 60000)
                addDrink(Drink(quantity, degree, ingestionTime))
            } catch (e:Exception) {
                return@setOnClickListener
            }

        }
    }

    private fun addDrink(drink: Drink) {
        val newDrink = TextView(this)
        newDrink.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        newDrink.text = drink.toString()

        linear_past_drinks?.addView(newDrink)
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
