package com.vaudibert.canidrive.ui


import KeyboardUtils
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.Drink
import kotlinx.android.synthetic.main.fragment_add_drink.*
import java.util.*

/**
 * Fragment to add a drink.
 */
class AddDrinkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_drink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drinkerRepository = (this.activity as MainActivity).drinkerRepository

        buttonValidateNewDrink.setOnClickListener {
            try {
                var quantity = editTextQuantity.text.toString().toDouble()
                var degree = editTextDegree.text.toString().toDouble()
                var ingestionTime = Date(Date().time - (editTextBefore.text.toString().toLong() * 60000))

                drinkerRepository.ingest(
                    Drink(quantity, degree, ingestionTime)
                )

                editTextQuantity.text.clear()
                editTextDegree.text.clear()
                editTextBefore.text.clear()

                KeyboardUtils.hideKeyboard(this.activity as Activity)

                findNavController().navigate(
                    AddDrinkFragmentDirections.actionAddDrinkFragmentToDriveFragment()
                )

            } catch (e:Exception) {
                longToast("You did not correctly fill in the values \nPlease try again")
                return@setOnClickListener
            }
        }

        val volumeLabels = arrayOf("5cL", "8cL", "13cL", "25cL", "33cL", "40cL", "50cL", "1L")
        val volumes = doubleArrayOf(50.0, 80.0, 130.0, 250.0, 330.0, 400.0, 500.0, 1000.0)
        numberPickerVolume.minValue = 0
        numberPickerVolume.maxValue = volumeLabels.size-1
        numberPickerVolume.displayedValues = volumeLabels
        numberPickerVolume.setOnValueChangedListener { picker, oldVal, newVal ->
            editTextQuantity.setText(volumes[newVal].toString())
        }

        val degreeLabels = arrayOf("2.5%", "5%", "7.5%", "10%", "13%", "15%", "20%", "30%", "40%", "60%", "80%")
        val degrees = doubleArrayOf(2.5, 5.0, 7.5, 10.0, 13.0, 15.0, 20.0, 30.0, 40.0, 60.0, 80.0)
        numberPickerDegree.minValue = 0
        numberPickerDegree.maxValue = degreeLabels.size -1
        numberPickerDegree.displayedValues = degreeLabels
        numberPickerDegree.setOnValueChangedListener { picker, oldVal, newVal ->
            editTextDegree.setText(degrees[newVal].toString())
        }

        val delayLabels = arrayOf("now", "20min ago", "1h ago", "2h ago", "3h ago", "5h ago")
        val delays = longArrayOf(0, 20, 60, 120, 180, 300)
        numberPickerWhen.minValue = 0
        numberPickerWhen.maxValue = delays.size -1
        numberPickerWhen.displayedValues = delayLabels
        numberPickerWhen.setOnValueChangedListener { picker, oldVal, newVal ->
            editTextBefore.setText(delays[newVal].toString())
        }

        editTextQuantity.setText(volumes[0].toString())
        editTextDegree.setText(degrees[0].toString())
        editTextBefore.setText(delays[0].toString())

    }

    private fun longToast(message: String) =
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()

}
