package com.vaudibert.canidrive.ui


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.vaudibert.canidrive.BuildConfig
import com.vaudibert.canidrive.R
import kotlinx.android.synthetic.main.fragment_splash.*

/**
 * A simple [Fragment] subclass.
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
            val navController = findNavController()
            val action = SplashFragmentDirections.actionSplashFragmentToDrinkerFragment()
            navController.navigate(action)
        }, 2000)

        super.onResume()
    }



}
