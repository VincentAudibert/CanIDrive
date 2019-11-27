package com.vaudibert.canidrive

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val drinker = Drinker(50.0, "NONE")

    lateinit var mainHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        textViewVersionName.text = "v" + BuildConfig.VERSION_NAME

        mainHandler = Handler(Looper.getMainLooper())

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

        editTextWeight.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (!hasFocus)
                    try {
                        drinker.weight = editTextWeight.text.toString().toDouble()
                        updateDriveStatus()
                    } catch (e:Exception) {
                        this.longToast("You did not correctly fill your weight \nPlease try again")
                        return@setOnFocusChangeListener
                    }
            }
        }

        radioGroupSex.setOnCheckedChangeListener {
                _,
            checkedId ->
            drinker.sex = if (checkedId != -1) findViewById<RadioButton>(checkedId).text.toString().toUpperCase() else "NONE"
            updateDriveStatus()
        }

    }

    private fun updateDriveStatus() {
        textViewDriveStatus.text = if (drinker.alcoholRateAt(Date()) < 0.5)
            "DRIVE : YES"
        else
            "DRIVE : NO"
    }

    /**
     * Helper task to update the drive status while app is running.
     */
    private val updateDriveStatusTask = object : Runnable {
        override fun run() {
            updateDriveStatus()
            mainHandler.postDelayed(this, 1000*60)
        }
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateDriveStatusTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateDriveStatusTask)
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
