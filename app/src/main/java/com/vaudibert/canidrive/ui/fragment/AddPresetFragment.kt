package com.vaudibert.canidrive.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.KeyboardUtils
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.drink.IngestedDrink
import com.vaudibert.canidrive.ui.CanIDrive
import kotlinx.android.synthetic.main.fragment_add_preset.*
import kotlinx.android.synthetic.main.linear_content_add_drink_custom_pickers.*
import java.text.DecimalFormat

class AddPresetFragment : Fragment() {
    private var volume = 0.0
    private var degree = 0.0

    private val doubleFormat : DecimalFormat = DecimalFormat("0.#")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_preset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drinkRepository = CanIDrive.instance.mainRepository.drinkRepository
        val drinkService = drinkRepository.drinkService

        buttonValidateNewPreset.setOnClickListener {
            if (editTextNewPresetName.text.toString().isBlank()) return@setOnClickListener

            drinkService.addNewPreset(
                editTextNewPresetName.text.toString(),
                volume,
                degree
            )

            KeyboardUtils.hideKeyboard(this.activity as Activity)

            findNavController().navigate(
                AddPresetFragmentDirections.actionAddPresetFragmentToAddDrinkFragment()
            )
        }

        setVolumePicker()

        setDegreePicker()

    }


    private fun setDegreePicker() {
        val degreeLabels = IngestedDrink.degrees.map { deg ->
            "${doubleFormat.format(deg)} %"
        }.toTypedArray()
        numberPickerDegree.minValue = 0
        numberPickerDegree.maxValue = degreeLabels.size - 1
        numberPickerDegree.displayedValues = degreeLabels
        val startDegree = degreeLabels.size / 2
        degree = IngestedDrink.degrees[startDegree]
        numberPickerDegree.value = startDegree
        numberPickerDegree.setOnValueChangedListener { _, _, newVal ->
            degree = IngestedDrink.degrees[newVal]
        }
    }

    private fun setVolumePicker() {
        val volumeLabels = IngestedDrink.volumes.map { vol ->
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
        volume = IngestedDrink.volumes[middleVolume]
        numberPickerVolume.setOnValueChangedListener { _, _, newVal ->
            volume = IngestedDrink.volumes[newVal]
        }
    }
}
