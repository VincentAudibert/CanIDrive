package com.vaudibert.canidrive.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.KeyboardUtils
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.DriveLaw
import com.vaudibert.canidrive.domain.DriveLaws
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

        val sharedPref = mainActivity.getSharedPreferences(
            mainActivity.getString(R.string.user_preferences),
            Context.MODE_PRIVATE
        )

        val drinkerRepository = mainActivity.drinkerRepository

        val countries = DriveLaws.countryLaws.map {
                law -> law.countryCode + " " + Locale("", law.countryCode).displayCountry
        }

        spinnerCountry.adapter = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, countries)
        spinnerCountry.setSelection(DriveLaws.countryLaws.indexOf(drinkerRepository.getLaw()))
        spinnerCountry.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                country = DriveLaws.countryLaws[position]
                limit = country?.limit ?: 0.0
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                country = null
                limit = 0.0
            }
        }

        var weight = drinkerRepository.getWeight()
        var sex = drinkerRepository.getSex()

        val weights = intArrayOf(30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 110, 120, 130, 140, 150)
        val weightLabels = weights.map { i -> i.toString() + "kg"}.toTypedArray()
        numberPickerWeight.minValue = 0
        numberPickerWeight.maxValue = weights.size -1
        numberPickerWeight.displayedValues = weightLabels

        val i = weights.indexOf(drinkerRepository.getWeight().toInt())
        numberPickerWeight.value = i.coerceAtLeast(0)

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

        buttonValidateDrinker.setOnClickListener {
            drinkerRepository.setSex(sex)
            drinkerRepository.setWeight(weight)
            drinkerRepository.driveLaw = this.country
            sharedPref
                .edit()
                .putString(getString(R.string.user_sex), sex)
                .putString(getString(R.string.countryCode), country?.countryCode ?: "")
                .putFloat(getString(R.string.user_weight), weight.toFloat())
                .apply()

            KeyboardUtils.hideKeyboard(mainActivity)

            if (!mainActivity.init) {
                mainActivity.init = true
                sharedPref
                    .edit()
                    .putBoolean(getString(R.string.user_initialized), true)
                    .apply()

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
}
