package com.vaudibert.canidrive.ui.fragment


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
import com.vaudibert.canidrive.domain.DrinkerStatusService
import com.vaudibert.canidrive.ui.CanIDrive
import com.vaudibert.canidrive.ui.PastDrinksAdapter
import com.vaudibert.canidrive.ui.repository.DrinkerRepository
import kotlinx.android.synthetic.main.constraint_content_drive_history.*
import kotlinx.android.synthetic.main.fragment_drive_status.*
import java.text.DateFormat

/**
 * The drive fragment that displays the drive status :
 *  - can the user drive ?
 *  - if not then when ?
 *  - what were the past drinks ?
 */
class DriveFragment : Fragment() {

    private lateinit var drinkerRepository: DrinkerRepository
    private lateinit var drinkerStatusService: DrinkerStatusService

    lateinit var mainHandler: Handler

    private lateinit var pastDrinksAdapter: PastDrinksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drive_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainRepository = CanIDrive.instance.mainRepository
        drinkerStatusService = mainRepository.drinkerStatusService

        drinkerRepository = mainRepository.drinkerRepository

        pastDrinksAdapter =
            PastDrinksAdapter(
                this.context!!,
                emptyList()
            )
        listViewPastDrinks.adapter = pastDrinksAdapter

        drinkerRepository.livePastDrinks.observe(this, Observer {

            pastDrinksAdapter.setDrinkList(it.asReversed())
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
        val drinkerStatus = drinkerStatusService.status()

        if (drinkerStatus.alcoholRate < 0.01) {
            linearAlcoholRate.visibility = LinearLayout.GONE
            linearWaitToSober.visibility = LinearLayout.GONE

            imageCar.setColorFilter(ContextCompat.getColor(this.context!!, R.color.driveGreen))
            imageDriveStatus.setImageResource(R.drawable.ic_check_white_24dp)
            imageDriveStatus.setColorFilter(ContextCompat.getColor(this.context!!, R.color.driveGreen))
            linearWaitToDrive.visibility = LinearLayout.GONE

        } else {
            linearAlcoholRate.visibility = LinearLayout.VISIBLE
            linearWaitToSober.visibility = LinearLayout.VISIBLE
            textViewAlcoholRate.text =
                "${drinkerStatus.alcoholRate.toString().substring(0, 4)} g/L"

            textViewTimeToSober.text = DateFormat
                .getTimeInstance(DateFormat.SHORT)
                .format(drinkerStatus.soberDate)

            if (drinkerStatus.canDrive) {
                // Set status icons to drive-able
                imageCar.setColorFilter(ContextCompat.getColor(this.context!!, R.color.driveGreen))
                imageDriveStatus.setImageResource(R.drawable.ic_warning_white_24dp)
                imageDriveStatus.setColorFilter(ContextCompat.getColor(this.context!!, R.color.driveAmber))
                linearWaitToDrive.visibility = LinearLayout.GONE
                textViewAlcoholRate.setTextColor(ContextCompat.getColor(this.context!!,R.color.driveAmber))
            } else {
                textViewTimeToDrive.text = DateFormat
                    .getTimeInstance(DateFormat.SHORT)
                    .format(drinkerStatus.canDriveDate)

                // Set status icons to NOT drive-able
                imageCar.setColorFilter(ContextCompat.getColor(this.context!!, R.color.driveRed))
                imageDriveStatus.setImageResource(R.drawable.ic_forbidden_white_24dp)
                imageDriveStatus.setColorFilter(ContextCompat.getColor(this.context!!, R.color.driveRed))
                linearWaitToDrive.visibility = LinearLayout.VISIBLE
                textViewAlcoholRate.setTextColor(ContextCompat.getColor(this.context!!,R.color.driveRed))
            }

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
        updateDriveStatus()
        mainHandler.post(updateDriveStatusTask)
    }
}
