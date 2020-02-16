package com.vaudibert.canidrive.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.KeyboardUtils
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.drivelaw.DriveLaw
import com.vaudibert.canidrive.domain.drivelaw.DriveLawService
import com.vaudibert.canidrive.ui.CanIDrive
import com.vaudibert.canidrive.ui.MainActivity
import com.vaudibert.canidrive.ui.repository.DrinkerRepository
import com.vaudibert.canidrive.ui.repository.MainRepository
import kotlinx.android.synthetic.main.constraint_content_drinker_country.*
import kotlinx.android.synthetic.main.constraint_content_drinker_pickers.*
import kotlinx.android.synthetic.main.fragment_drinker.*
import kotlin.math.roundToInt

/**
 * The drinker fragment to enter its details.
 */
// TODO : split into 2 fragments : body and drive law ?
class DrinkerFragment : Fragment() {

    private var weight = 0.0
    private var sex = "NONE"

    private lateinit var mainRepository : MainRepository
    private lateinit var drinkerRepository : DrinkerRepository
    private lateinit var driveLawService: DriveLawService

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

        mainRepository = CanIDrive.instance.mainRepository
        drinkerRepository = mainRepository.drinkerRepository

        val driveLawRepository = mainRepository.driveLawRepository
        driveLawService = driveLawRepository.driveLawService

        setupSpinnerCountry(
            driveLawService
                .getListOfCountriesWithFlags(
                    mainActivity.getString(R.string.customCountryLabel)
                )
        )

        setupWeightPicker(drinkerRepository.body.weight)

        setupSexPicker(drinkerRepository.body.sex)


        setupValidationButton(drinkerRepository)

        driveLawRepository.liveDriveLaw.observe(this) { driveLaw: DriveLaw ->
            // update the limit area (custom or not)
            if (driveLaw.isCustom()) {
                textViewCurrentLimit.visibility = TextView.GONE
                editTextCurrentLimit.visibility = TextView.VISIBLE
            } else {
                textViewCurrentLimit.visibility = TextView.VISIBLE
                // TODO : ugly structure for driveLimit call, move in driveLaw ?
                textViewCurrentLimit.text = driveLawService.driveLimit().toString()
                editTextCurrentLimit.visibility = TextView.GONE
                KeyboardUtils.hideKeyboard(this.activity!!)
            }

            // Update for Young driver checkbox visibility (not value)
            if (driveLaw.youngLimit != null) {
                checkboxYoungDriver.visibility = CheckBox.VISIBLE
                checkboxYoungDriver.text = getString(driveLaw.youngLimit.explanationId)
            } else {
                checkboxYoungDriver.visibility = CheckBox.GONE
            }

            // Update for Professional drive checkbox visibility (not value)
            if (driveLaw.professionalLimit != null) {
                checkboxProfessionalDriver.visibility = CheckBox.VISIBLE
            } else {
                checkboxProfessionalDriver.visibility = CheckBox.GONE
            }
        }

        driveLawRepository.liveIsYoung.observe(this) {
            checkboxYoungDriver.isChecked = it
        }
        driveLawRepository.liveIsProfessional.observe(this) {
            checkboxProfessionalDriver.isChecked = it
        }

        setupCheckBoxes()

    }

    override fun onResume() {
        super.onResume()
        updateCustomLimit(driveLawService.customCountryLimit)
    }

    private fun setupValidationButton(drinkerRepository: DrinkerRepository) {
        buttonValidateDrinker.setOnClickListener {
            // country and law option selections were already recorded
            // TODO : country selection and law options should only be recorded when validated (viewmodel?)

            drinkerRepository.body.sex = sex
            drinkerRepository.body.weight = weight

            driveLawService.customCountryLimit = editTextCurrentLimit.text.toString().toDouble()

            var navOptions:NavOptions? = null

            if (!mainRepository.init) {
                mainRepository.init = true

                // Specific option needed for the init of the app, the drinker is the first fragment
                // and needs to be cleared (take over nav_graph definition).
                navOptions = NavOptions.Builder()
                    .setPopUpTo(
                        R.id.drinkerFragment,
                        true
                    ).build()
            }

            findNavController().navigate(
                DrinkerFragmentDirections.actionDrinkerFragmentToDriveFragment(),
                navOptions
            )

        }
    }

    private fun setupCheckBoxes() {
        checkboxYoungDriver.setOnCheckedChangeListener { _, isChecked ->
            driveLawService.isYoung = isChecked
        }
        checkboxProfessionalDriver.setOnCheckedChangeListener { _, isChecked ->
            driveLawService.isProfessional = isChecked
        }
    }

    private fun setupSexPicker(recordedSex: String) {
        sex = recordedSex

        val sexValues = arrayOf(
            getString(R.string.male),
            getString(R.string.other),
            getString(R.string.female)
        )
        numberPickerSex.minValue = 0
        numberPickerSex.maxValue = sexValues.size - 1
        numberPickerSex.value = when (sex) {
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
    }

    private fun setupWeightPicker(recordedWeight: Double) {
        weight = recordedWeight

        val weights = intArrayOf(
            30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95,
            100, 110, 120, 130, 140, 150
        )
        val weightLabels = weights.map { i -> i.toString() + "kg" }.toTypedArray()
        numberPickerWeight.minValue = 0
        numberPickerWeight.maxValue = weights.size - 1
        numberPickerWeight.displayedValues = weightLabels

        numberPickerWeight.value = weights
            .indexOf(weight.toInt())
            .coerceAtLeast(0)

        numberPickerWeight.setOnValueChangedListener { _, _, newVal ->
            weight = weights[newVal].toDouble()
        }
    }

    private fun setupSpinnerCountry(
        countries: List<String>
    ) {
        spinnerCountry.adapter = ArrayAdapter(
            this.context!!,
            R.layout.item_country_spinner,
            countries
        )
        val initialPosition = driveLawService.getIndexOfCurrent()
        spinnerCountry.setSelection(initialPosition)

        spinnerCountry.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                Log.d("DrinkerFragment", "position is $position")
                if (position == 0) {
                    // handle the other country case
                    val customLimit = driveLawService.customCountryLimit
                    updateCustomLimit(customLimit)

                }
                driveLawService.select(position)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun updateCustomLimit(customLimit: Double) {
        editTextCurrentLimit.setText(
            ((customLimit * 100.0).roundToInt() / 100.0).toString()
        )
    }

}
