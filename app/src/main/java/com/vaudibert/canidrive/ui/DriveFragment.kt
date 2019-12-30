package com.vaudibert.canidrive.ui


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.R
import kotlinx.android.synthetic.main.fragment_drive.*
import java.text.DateFormat

/**
 * The drive fragment that displays the drive status :
 *  - can the user drive ?
 *  - if not then when ?
 *  - what were the past drinks ?
 */
class DriveFragment : Fragment() {

    private lateinit var drinkerRepository: DrinkerRepository

    lateinit var mainHandler: Handler

    private lateinit var pastDrinksAdapter: PastDrinksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drinkerRepository = (this.activity as MainActivity).drinkerRepository

        pastDrinksAdapter =
            PastDrinksAdapter(
                this.context!!,
                drinkerRepository.getDrinks()
            )
        listViewPastDrinks.adapter = pastDrinksAdapter

        drinkerRepository.liveDrinker.observe(this, Observer {

            pastDrinksAdapter.setDrinkList(drinkerRepository.getDrinks())
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

        if (drinkerRepository.canDrive()) {
            imageCar.setColorFilter(ContextCompat.getColor(this.context!!, R.color.driveGreen))
            imageDriveStatus.setImageResource(R.drawable.ic_check_white_24dp)
            imageDriveStatus.setColorFilter(ContextCompat.getColor(this.context!!, R.color.driveGreen))
            linearWaitToGreen.visibility = LinearLayout.GONE
        } else {
            textViewTimeToWait.text = DateFormat
                .getTimeInstance(DateFormat.SHORT)
                .format(drinkerRepository.timeToDrive())

            imageCar.setColorFilter(ContextCompat.getColor(this.context!!, R.color.driveRed))
            imageDriveStatus.setImageResource(R.drawable.ic_forbidden_white_24dp)
            imageDriveStatus.setColorFilter(ContextCompat.getColor(this.context!!, R.color.driveRed))
            linearWaitToGreen.visibility = LinearLayout.VISIBLE
        }

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
