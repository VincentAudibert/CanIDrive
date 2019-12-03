package com.vaudibert.canidrive.ui


import KeyboardUtils
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.Drinker
import kotlinx.android.synthetic.main.fragment_drinker.*

/**
 * The drinker fragment to enter its details.
 */
class DrinkerFragment : Fragment() {

    lateinit var drinker: Drinker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drinker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drinker = (this.activity as MainActivity).drinker

        editTextWeight.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (!hasFocus)
                    try {
                        drinker.weight = editTextWeight.text.toString().toDouble()
                    } catch (e:Exception) {
                        longToast("You did not correctly fill your weight \nPlease try again")
                        return@setOnFocusChangeListener
                    }
            }
        }

        radioGroupSex.setOnCheckedChangeListener {
                _,
                checkedId ->
            drinker.sex = if (checkedId != -1)
                view.findViewById<RadioButton>(checkedId).text.toString().toUpperCase()
            else
                "NONE"
        }

        buttonValidateDrinker.setOnClickListener {
            KeyboardUtils.hideKeyboard(this.activity as Activity)
            findNavController().navigate(
                DrinkerFragmentDirections.actionDrinkerFragmentToDriveFragment()
            )
        }

    }

    private fun longToast(message: String) =
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()

}
