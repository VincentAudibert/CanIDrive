package com.vaudibert.canidrive.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.DriveLaw
import com.vaudibert.canidrive.domain.DriveLaws
import com.vaudibert.canidrive.toFlagEmoji
import kotlinx.android.synthetic.main.fragment_drinker.*
import java.util.*

/**
 * The drinker fragment to enter its details.
 */
class DrinkerFragment : Fragment() {

    private var country : DriveLaw? = null
    private var limit = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drinker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = this.activity as MainActivity

        val drinkerRepository = mainActivity.drinkerRepository

        val countries = DriveLaws.countryLaws.map {
                law -> law.countryCode.toFlagEmoji() + " " + Locale("", law.countryCode).displayCountry
        }

        spinnerCountry.adapter = ArrayAdapter(
            this.context!!,
            R.layout.item_country_spinner,
            countries
        )
        spinnerCountry.setSelection(drinkerRepository.getCountryPosition())
        spinnerCountry.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                country = DriveLaws.countryLaws[position]
                limit = country?.limit ?: 0.0
                updateCheckBoxYoung()
                updateCheckBoxProfessional()
                updateCurrentLimit(drinkerRepository)
                drinkerRepository.setDriveLaw(country)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                country = null
                limit = 0.0
            }
        }

        var weight = drinkerRepository.getWeight()
        var sex = drinkerRepository.getSex()


        val weights = intArrayOf(30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95,
            100, 110, 120, 130, 140, 150)
        val weightLabels = weights.map { i -> i.toString() + "kg"}.toTypedArray()
        numberPickerWeight.minValue = 0
        numberPickerWeight.maxValue = weights.size -1
        numberPickerWeight.displayedValues = weightLabels

        numberPickerWeight.value = weights
            .indexOf(drinkerRepository.getWeight().toInt())
            .coerceAtLeast(0)

        numberPickerWeight.setOnValueChangedListener { _, _, newVal ->
            weight = weights[newVal].toDouble()
        }


        val sexValues = arrayOf(
            getString(R.string.male),
            getString(R.string.other),
            getString(R.string.female)
        )
        numberPickerSex.minValue = 0
        numberPickerSex.maxValue = sexValues.size -1
        numberPickerSex.value = when (drinkerRepository.getSex()) {
            "MALE" -> 0
            "FEMALE" -> 2
            else -> 1
        }
        numberPickerSex.displayedValues = sexValues
        numberPickerSex.setOnValueChangedListener { _, _, newVal ->
            sex = when (newVal) {
                0 -> "MALE"
                2 -> "FEMALE"
                else -> "OTHER"
            }
        }

        // Always update checkboxes even if not visible as they hold the state.
        checkboxYoungDriver.isChecked = drinkerRepository.getYoung()
        checkboxProfessionalDriver.isChecked = drinkerRepository.getProfessional()

        updateCheckBoxYoung()

        updateCheckBoxProfessional()

        updateCurrentLimit(drinkerRepository)

        checkboxYoungDriver.setOnCheckedChangeListener { _, isChecked ->
            drinkerRepository.setYoung(isChecked)
            updateCurrentLimit(drinkerRepository)
        }

        checkboxProfessionalDriver.setOnCheckedChangeListener { _, isChecked ->
            drinkerRepository.setProfessional(isChecked)
            updateCurrentLimit(drinkerRepository)
        }

        buttonValidateDrinker.setOnClickListener {
            drinkerRepository.setSex(sex)
            drinkerRepository.setWeight(weight)

            if (!drinkerRepository.init) {
                drinkerRepository.init = true

                // Specific option needed for the init of the app, the drinker is the first fragment
                // and needs to be cleared (take over nav_graph definition).
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(
                        R.id.drinkerFragment,
                        true
                    ).build()
                // TODO : find a DRY way to call navController
                findNavController().navigate(
                    DrinkerFragmentDirections.actionDrinkerFragmentToDriveFragment(),
                    navOptions
                )
            } else {
                findNavController().navigate(
                    DrinkerFragmentDirections.actionDrinkerFragmentToDriveFragment()
                )
            }

        }

    }

    private fun updateCurrentLimit(drinkerRepository: DrinkerRepository) {
        textViewCurrentLimit.text = drinkerRepository.driveLimit().toString() + " g/L"
    }

    private fun updateCheckBoxYoung() {
        if (country?.youngLimit != null) {
            checkboxYoungDriver.visibility = CheckBox.VISIBLE
            checkboxYoungDriver.text = getString(country?.youngLimit?.explanationId ?: 0)
        } else {
            checkboxYoungDriver.visibility = CheckBox.GONE
        }
    }

    private fun updateCheckBoxProfessional() {
        if (country?.professionalLimit != null) {
            checkboxProfessionalDriver.visibility = CheckBox.VISIBLE
        } else {
            checkboxProfessionalDriver.visibility = CheckBox.GONE
        }
    }
}
