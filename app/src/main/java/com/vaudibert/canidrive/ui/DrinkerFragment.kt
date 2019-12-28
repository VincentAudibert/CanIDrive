package com.vaudibert.canidrive.ui


import KeyboardUtils
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.R
import kotlinx.android.synthetic.main.fragment_drinker.*

/**
 * The drinker fragment to enter its details.
 */
class DrinkerFragment : Fragment() {


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

        // update fields with present data
        editTextWeight.setText(drinkerRepository.getWeight().toString())
        radioGroupSex.check(when (drinkerRepository.getSex()) {
            "MALE" -> R.id.radioButtonMale
            "FEMALE" -> R.id.radioButtonFemale
            else -> R.id.radioButtonOther
        })

        editTextWeight.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (!hasFocus)
                    try {
                        val weight = editTextWeight.text.toString().toDouble()
                        drinkerRepository.setWeight(weight)
                        sharedPref
                            .edit()
                            .putFloat(getString(R.string.user_weight), weight.toFloat())
                            .apply()
                    } catch (e:Exception) {
                        longToast("You did not correctly fill your weight \nPlease try again")
                        return@setOnFocusChangeListener
                    }
            }
        }

        radioGroupSex.setOnCheckedChangeListener {
                _,
            checkedId ->
            run {
                val sex = if (checkedId != -1)
                    view.findViewById<RadioButton>(checkedId).text.toString().toUpperCase()
                else
                    "NONE"
                drinkerRepository.setSex(sex)
                sharedPref
                    .edit()
                    .putString(getString(R.string.user_sex), sex)
                    .apply()
            }

        }

        buttonValidateDrinker.setOnClickListener {
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

    private fun longToast(message: String) =
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()

}
