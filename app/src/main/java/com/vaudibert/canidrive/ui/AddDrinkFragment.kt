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
import com.vaudibert.canidrive.domain.DrinkData
import kotlinx.android.synthetic.main.fragment_add_drink.*
import java.text.DecimalFormat
import java.util.*

/**
 * Fragment to add a drink.
 */
class AddDrinkFragment : Fragment() {

    private var volume = 0.0
    private var degree = 0.0
    private var delay: Long = 0

    private val doubleFormat : DecimalFormat = DecimalFormat("0.#")

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


        setVolumePicker()

        setDegreePicker()

        setDelayPicker()
    }

    private fun setDelayPicker() {
        val delays = longArrayOf(0, 20, 40, 60, 90, 120, 180, 300, 480, 720, 1080, 1440)
        val delayLabels = arrayOf(
            getString(R.string.now),
            "20min",
            "40min",
            "1h",
            "1h30",
            "2h",
            "3h",
            "5h",
            "8h",
            "12h",
            "18h",
            "24h"
        )
        numberPickerWhen.minValue = 0
        numberPickerWhen.maxValue = delays.size - 1
        numberPickerWhen.displayedValues = delayLabels
        delay = delays[0]
        numberPickerWhen.setOnValueChangedListener { _, _, newVal ->
            delay = delays[newVal]
        }
    }

    private fun setDegreePicker() {
        val degreeLabels = DrinkData.degrees.map { deg ->
            "${doubleFormat.format(deg)} %"
        }.toTypedArray()
        numberPickerDegree.minValue = 0
        numberPickerDegree.maxValue = degreeLabels.size - 1
        numberPickerDegree.displayedValues = degreeLabels
        val startDegree = degreeLabels.size / 2
        degree = DrinkData.degrees[startDegree]
        numberPickerDegree.value = startDegree
        numberPickerDegree.setOnValueChangedListener { _, _, newVal ->
            degree = DrinkData.degrees[newVal]
        }
    }

    private fun setVolumePicker() {
        val volumeLabels = DrinkData.volumes.map { vol ->
            if (vol < 1000.0)
                "${doubleFormat.format(vol / 10.0)} cL"
            else
                "${doubleFormat.format(vol / 1000.0)} L"
        }.toTypedArray()
        numberPickerVolume.minValue = 0
        numberPickerVolume.maxValue = volumeLabels.size - 1
        numberPickerVolume.displayedValues = volumeLabels
        val middleVolume = volumeLabels.size / 2
        numberPickerVolume.value = middleVolume
        volume = DrinkData.volumes[middleVolume]
        numberPickerVolume.setOnValueChangedListener { _, _, newVal ->
            volume = DrinkData.volumes[newVal]
        }
    }
}
