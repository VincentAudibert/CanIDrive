package com.vaudibert.canidrive.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.KeyboardUtils
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.drivelaw.DriveLaw
import com.vaudibert.canidrive.domain.drivelaw.DriveLawService
import com.vaudibert.canidrive.ui.CanIDrive
import com.vaudibert.canidrive.ui.repository.DigestionRepository
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
    private lateinit var digestionRepository : DigestionRepository
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

        mainRepository = CanIDrive.instance.mainRepository
        digestionRepository = mainRepository.digestionRepository

        val driveLawRepository = mainRepository.driveLawRepository
        driveLawService = driveLawRepository.driveLawService

        setupSpinnerCountry(
            driveLawService
                .getListOfCountriesWithFlags()
        )

        setupWeightPicker(digestionRepository.body.weight)

        setupSexPicker(digestionRepository.body.sex)


        setupValidationButton(digestionRepository)

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

        setupAlcoholTolerance(digestionRepository)
    }

    override fun onResume() {
        super.onResume()
        updateCustomLimit(driveLawService.customCountryLimit)
    }

    private fun setupAlcoholTolerance(digestionRepository: DigestionRepository) {
        if (digestionRepository.toleranceLevels.isEmpty()) return

        val levelCount = digestionRepository.toleranceLevels.size - 1
        seekBarAlcoholTolerance.max = levelCount

        seekBarAlcoholTolerance.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textViewAlcoholToleranceTextValue.text = digestionRepository.toleranceLevels[progress]
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        seekBarAlcoholTolerance.progress = (digestionRepository.body.alcoholTolerance * levelCount).roundToInt()
        textViewAlcoholToleranceTextValue.text = digestionRepository.toleranceLevels[seekBarAlcoholTolerance.progress]
    }

    private fun setupValidationButton(digestionRepository: DigestionRepository) {
        buttonValidateDrinker.setOnClickListener {
            // TODO : country selection and law options should only be recorded when validated (viewmodel?)

            digestionRepository.body.sex = when {
                radioMale.isChecked -> "MALE"
                radioFemale.isChecked -> "FEMALE"
                else -> "OTHER"
            }
            digestionRepository.body.weight = weight
            val levelCount = (digestionRepository.toleranceLevels.size - 1).coerceAtLeast(1)
            digestionRepository.body.alcoholTolerance =
                seekBarAlcoholTolerance.progress.toDouble() /
                        levelCount.toDouble()

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

        when (sex) {
            "MALE" -> radioMale.isChecked = true
            "FEMALE" -> radioFemale.isChecked = true
            else -> radioSexOther.isChecked = true
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
                view: View?,
                position: Int,
                id: Long
            ) {
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
