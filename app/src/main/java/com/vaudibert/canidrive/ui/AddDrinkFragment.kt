package com.vaudibert.canidrive.ui


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.KeyboardUtils
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.Drink
import kotlinx.android.synthetic.main.fragment_add_drink.*
import java.util.*

/**
 * Fragment to add a drink.
 */
class AddDrinkFragment : Fragment() {

    private var volume = 0.0
    private var degree = 0.0
    private var delay: Long = 0

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
            val ingestionTime = Date(Date().time - (delay * 60000))

            drinkerRepository.ingest(
                Drink(volume, degree, ingestionTime)
            )

            KeyboardUtils.hideKeyboard(this.activity as Activity)

            findNavController().navigate(
                AddDrinkFragmentDirections.actionAddDrinkFragmentToDriveFragment()
            )
        }

        val volumeLabels = arrayOf("5cL", "8cL", "13cL", "25cL", "33cL", "50cL", "1L")
        val volumes = doubleArrayOf(50.0, 80.0, 130.0, 250.0, 330.0, 500.0, 1000.0)
        numberPickerVolume.minValue = 0
        numberPickerVolume.maxValue = volumeLabels.size-1
        numberPickerVolume.displayedValues = volumeLabels
        volume = volumes[0]
        numberPickerVolume.setOnValueChangedListener { _, _, newVal ->
            volume = volumes[newVal]
        }

        val degreeLabels = arrayOf("2.5%", "5%", "7.5%", "10%", "13%", "15%", "20%", "30%", "40%", "60%", "80%")
        val degrees = doubleArrayOf(2.5, 5.0, 7.5, 10.0, 13.0, 15.0, 20.0, 30.0, 40.0, 60.0, 80.0)
        numberPickerDegree.minValue = 0
        numberPickerDegree.maxValue = degreeLabels.size -1
        numberPickerDegree.displayedValues = degreeLabels
        degree = degrees[0]
        numberPickerDegree.setOnValueChangedListener { _, _, newVal ->
            degree = degrees[newVal]
        }

        val delayLabels = arrayOf(getString(R.string.now), "20min", "1h", "2h", "3h", "5h")
        val delays = longArrayOf(0, 20, 60, 120, 180, 300)
        numberPickerWhen.minValue = 0
        numberPickerWhen.maxValue = delays.size -1
        numberPickerWhen.displayedValues = delayLabels
        delay = delays[0]
        numberPickerWhen.setOnValueChangedListener { _, _, newVal ->
            delay = delays[newVal]
        }
    }
}
