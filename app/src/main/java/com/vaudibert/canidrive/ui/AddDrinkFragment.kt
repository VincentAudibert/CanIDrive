package com.vaudibert.canidrive.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.Drink
import com.vaudibert.canidrive.domain.Drinker
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddDrinkFragment : Fragment() {

    lateinit var drinker: Drinker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_drink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drinker = (this.activity as MainActivity).drinker

        buttonValidateNewDrink.setOnClickListener {
            try {
                var quantity = editTextQuantity.text.toString().toInt()
                var degree = editTextDegree.text.toString().toFloat()
                var ingestionTime = Date(Date().time - (editTextBefore.text.toString().toLong() * 60000))

                drinker.ingest(
                    Drink(
                        quantity,
                        degree
                    ), ingestionTime)

                editTextQuantity.text.clear()
                editTextDegree.text.clear()
                editTextBefore.text.clear()


            } catch (e:Exception) {
                longToast("You did not correctly fill in the values \nPlease try again")
                return@setOnClickListener
            }
        }
    }

    private fun longToast(message: String) =
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()


}
