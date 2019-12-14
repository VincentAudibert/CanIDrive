package com.vaudibert.canidrive.ui


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.Drinker
import kotlinx.android.synthetic.main.fragment_drive.*
import java.util.*

/**
 * The drive fragment that displays the drive status :
 *  - can the user drive ?
 *  - if not then when ?
 *  - what were the past drinks ?
 */
class DriveFragment : Fragment() {

    private lateinit var liveDrinker : LiveData<Drinker>

    lateinit var mainHandler: Handler

    lateinit var pastDrinksAdapter: PastDrinksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        liveDrinker = DrinkerRepository.liveDrinker

        pastDrinksAdapter =
            PastDrinksAdapter(
                this.context!!,
                DrinkerRepository.getDrinks()
            )
        listViewPastDrinks.adapter = pastDrinksAdapter


        liveDrinker.observe(this, Observer {

            pastDrinksAdapter.setDrinkList(DrinkerRepository.getDrinks())
            updateDriveStatus()
        })

        // needed for periodic update of drinker status
        mainHandler = Handler(Looper.getMainLooper())

        buttonToDrinker.setOnClickListener {
            findNavController().navigate(
                DriveFragmentDirections.actionDriveFragmentToDrinkerFragment()
            )
        }

        buttonAddDrink.setOnClickListener {
            findNavController().navigate(
                DriveFragmentDirections.actionDriveFragmentToAddDrinkFragment()
            )
        }

    }
    private fun updateDriveStatus() {
        val drinker = liveDrinker.value ?: return

        textViewDriveStatus.text = if (drinker.alcoholRateAt(Date()) < 0.5)
            "DRIVE : YES"
        else
            "DRIVE : NO"

        pastDrinksAdapter.notifyDataSetChanged()
    }

    /**
     * Helper task to update the drive status while app is running.
     */
    private val updateDriveStatusTask = object : Runnable {
        override fun run() {
            updateDriveStatus()
            mainHandler.postDelayed(this, 1000*60)
        }
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateDriveStatusTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateDriveStatusTask)
    }
}
