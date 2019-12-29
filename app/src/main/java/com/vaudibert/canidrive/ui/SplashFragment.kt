package com.vaudibert.canidrive.ui


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.BuildConfig
import com.vaudibert.canidrive.R
import kotlinx.android.synthetic.main.fragment_splash.*

/**
 * The splash fragment, to display icon, version and do stuff in background.
 */
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewVersionName.text = "v" + BuildConfig.VERSION_NAME
    }

    override fun onResume() {

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.postDelayed( {

            val init = (this.activity as MainActivity).init

            val action = if (init)
                SplashFragmentDirections.actionSplashFragmentToDriveFragment()
            else
                SplashFragmentDirections.actionSplashFragmentToDrinkerFragment()

            findNavController().navigate(action)
        }, 1000)

        super.onResume()
    }



}
