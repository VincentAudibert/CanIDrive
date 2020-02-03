package com.vaudibert.canidrive.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.KeyboardUtils
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.DriveLaw
import com.vaudibert.canidrive.domain.DriveLaws
import com.vaudibert.canidrive.toFlagEmoji
import kotlinx.android.synthetic.main.constraint_drinker_country.*
import kotlinx.android.synthetic.main.fragment_drinker.*
import kotlinx.android.synthetic.main.linear_drinker_weight_sex.*
import java.util.*
import kotlin.math.roundToInt

/**
 * The drinker fragment to enter its details.
 */
class DrinkerFragment : Fragment() {

    private var country : DriveLaw? = null
    private var limit = 0.0
    private var weight = 0.0
    private var sex = "NONE"
    private lateinit var drinkerRepository : DrinkerRepository

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

        drinkerRepository = mainActivity.drinkerRepository

        val countries: List<String> = DriveLaws.countryLaws.map { law ->
            if (law.countryCode == "")
                mainActivity.getString(R.string.customCountryLabel)
            else
                law.countryCode.toFlagEmoji() + " " + Locale("", law.countryCode).displayCountry
        }

        setupSpinnerCountry(countries, drinkerRepository)

        weight = setupWeightPicker(drinkerRepository)

        sex = setupSexPicker(drinkerRepository)

        setupCheckBoxes(drinkerRepository)

        setupValidationButton(drinkerRepository)

    }

    override fun onResume() {
        super.onResume()
        updateCustomLimit(drinkerRepository.getCustomCountryLimit())
    }

    private fun setupValidationButton(drinkerRepository: DrinkerRepository) {
        buttonValidateDrinker.setOnClickListener {
            drinkerRepository.setSex(sex)
            drinkerRepository.setWeight(weight)

            drinkerRepository.setCustomCountryLimit(editTextCurrentLimit.text.toString().toDouble())

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

    private fun setupCheckBoxes(drinkerRepository: DrinkerRepository) {
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
    }

    private fun setupSexPicker(drinkerRepository: DrinkerRepository): String {
        var sex = drinkerRepository.getSex()

        val sexValues = arrayOf(
            getString(R.string.male),
            getString(R.string.other),
            getString(R.string.female)
        )
        numberPickerSex.minValue = 0
        numberPickerSex.maxValue = sexValues.size - 1
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
        return sex
    }

    private fun setupWeightPicker(drinkerRepository: DrinkerRepository): Double {
        var weight = drinkerRepository.getWeight()

        val weights = intArrayOf(
            30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95,
            100, 110, 120, 130, 140, 150
        )
        val weightLabels = weights.map { i -> i.toString() + "kg" }.toTypedArray()
        numberPickerWeight.minValue = 0
        numberPickerWeight.maxValue = weights.size - 1
        numberPickerWeight.displayedValues = weightLabels

        numberPickerWeight.value = weights
            .indexOf(drinkerRepository.getWeight().toInt())
            .coerceAtLeast(0)

        numberPickerWeight.setOnValueChangedListener { _, _, newVal ->
            weight = weights[newVal].toDouble()
        }
        return weight
    }

    private fun setupSpinnerCountry(
        countries: List<String>,
        drinkerRepository: DrinkerRepository
    ) {
        spinnerCountry.adapter = ArrayAdapter(
            this.context!!,
            R.layout.item_country_spinner,
            countries
        )
        val i = drinkerRepository.getCountryPosition()
        spinnerCountry.setSelection(i)
        val customCountry = i == 0

        updateCustomCountry(customCountry)

        spinnerCountry.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                if (position == 0) {
                    // handle the other country case
                    val customLimit = drinkerRepository.getCustomCountryLimit()
                    // TODO : UI should not handle DriveLaw constructors
                    country = DriveLaw("", customLimit)
                    updateCustomLimit(customLimit)
                } else {
                    country = DriveLaws.countryLaws[position]
                }
                updateCustomCountry(position == 0)
                limit = country?.limit ?: 0.0
                updateCheckBoxYoung()
                updateCheckBoxProfessional()
                drinkerRepository.setDriveLaw(country)
                updateCurrentLimit(drinkerRepository)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                country = null
                limit = 0.0
            }
        }
    }

    private fun updateCustomLimit(customLimit: Double) {
        editTextCurrentLimit.setText(
            (Math.round(customLimit * 100.0) / 100.0).toString()
        )
    }

    private fun updateCustomCountry(customCountry: Boolean) {
        if (customCountry) {
            // country is custom
            textViewCurrentLimit.visibility = TextView.GONE
            editTextCurrentLimit.visibility = TextView.VISIBLE
        } else {
            textViewCurrentLimit.visibility = TextView.VISIBLE
            editTextCurrentLimit.visibility = TextView.GONE
            KeyboardUtils.hideKeyboard(this.activity!!)
        }
    }

    private fun updateCurrentLimit(drinkerRepository: DrinkerRepository) {
        textViewCurrentLimit.text = drinkerRepository.driveLimit().toString()
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
