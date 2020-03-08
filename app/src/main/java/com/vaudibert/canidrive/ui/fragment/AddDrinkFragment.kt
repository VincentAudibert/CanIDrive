package com.vaudibert.canidrive.ui.fragment


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.KeyboardUtils
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.ui.CanIDrive
import com.vaudibert.canidrive.ui.adapter.PresetDrinksAdapter
import kotlinx.android.synthetic.main.constraint_content_add_drink_delay_button.*
import kotlinx.android.synthetic.main.constraint_content_add_drink_presets.*
import java.util.*

/**
 * Fragment to add a drink.
 */
class AddDrinkFragment : Fragment() {

    private var volume = 0.0
    private var degree = 0.0
    private var delay: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_drink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drinkRepository = CanIDrive.instance.mainRepository.drinkRepository
        val presetService = drinkRepository.presetService

        setDelaySeekBar()

        val presetDrinksAdapter =
            PresetDrinksAdapter(
                this.context!!,
                viewLifecycleOwner,
                {
                    findNavController().navigate(
                        AddDrinkFragmentDirections.actionAddDrinkFragmentToAddPresetFragment()
                    )
                },
                drinkRepository
            )
        listViewPresetDrinks.adapter = presetDrinksAdapter

        drinkRepository.liveSelectedPreset.observe(viewLifecycleOwner, Observer {
            if (it == null)
                buttonValidateNewDrink.visibility = Button.INVISIBLE
            else
                buttonValidateNewDrink.visibility = Button.VISIBLE
        })

        drinkRepository.livePresetDrinks.observe(
            viewLifecycleOwner,
            Observer {
                presetDrinksAdapter.notifyDataSetChanged()
            }
        )

        buttonValidateNewDrink.setOnClickListener {
            val ingestionTime = Date(Date().time - (delay * 60000))
            presetService.ingest(ingestionTime)

            KeyboardUtils.hideKeyboard(this.activity as Activity)
            findNavController().navigate(
                AddDrinkFragmentDirections.actionAddDrinkFragmentToDriveFragment()
            )
        }

    }

    private fun setDelaySeekBar() {
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

        val levelCount = delays.size - 1
        seekBarIngestionDelay.max = levelCount
        textViewWhenText.text = delayLabels[0]

        seekBarIngestionDelay.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textViewWhenText.text = delayLabels[progress]
                delay = delays[progress]
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        seekBarIngestionDelay.progress = 0
        delay = delays[0]
    }

}
