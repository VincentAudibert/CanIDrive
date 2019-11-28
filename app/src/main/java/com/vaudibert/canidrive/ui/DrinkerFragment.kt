package com.vaudibert.canidrive.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.Drinker
import kotlinx.android.synthetic.main.fragment_drinker.*

/**
 * A simple [Fragment] subclass.
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
                        this.longToast("You did not correctly fill your weight \nPlease try again")
                        return@setOnFocusChangeListener
                    }
            }
        }

        radioGroupSex.setOnCheckedChangeListener {
                _,
                checkedId ->
            drinker.sex = if (checkedId != -1) view.findViewById<RadioButton>(checkedId).text.toString().toUpperCase() else "NONE"
        }

    }


}
