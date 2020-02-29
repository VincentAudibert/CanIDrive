package com.vaudibert.canidrive.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.drink.IngestedDrink
import java.text.SimpleDateFormat
import java.util.*

class IngestedDrinksAdapter(
    val context: Context,
    private var ingestedDrinkList : List<IngestedDrink>
    ) : BaseAdapter() {

    private val DAY_IN_MILLIS = 3600*1000*24

    // TODO : inject service ?
    private val drinkService =
        CanIDrive.instance.mainRepository.drinkRepository.drinkService

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val dateFormat = SimpleDateFormat("HH:mm")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val drinkView = inflater.inflate(R.layout.item_past_drink, parent, false)
        val drink = getItem(position)

        val quantityText = drinkView.findViewById(R.id.pastDrinkTextQuantity) as TextView
        val degreeText = drinkView.findViewById(R.id.pastDrinkTextDegree) as TextView
        val timeText = drinkView.findViewById(R.id.pastDrinkTextTime) as TextView
        val daysText = drinkView.findViewById(R.id.textViewPastDays) as TextView

        quantityText.text = "${drink.volume} ml"
        degreeText.text = "${drink.degree} %"
        val days: Long = (drink.ingestionTime.time / DAY_IN_MILLIS) - (Date().time / DAY_IN_MILLIS)
        if (days == 0L)
            daysText.visibility = TextView.GONE
        else {
            daysText.visibility = TextView.VISIBLE
            daysText.text = "$days${context.getString(R.string.day_unit)} "
        }

        timeText.text = dateFormat.format(drink.ingestionTime)

        val buttonRemovePastDrink =
            drinkView.findViewById(R.id.buttonRemovePastDrink) as ImageButton

        buttonRemovePastDrink.setOnClickListener {
            drinkService.remove(drink)
        }

        return drinkView
    }

    override fun getItem(position: Int): IngestedDrink {
        return ingestedDrinkList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return ingestedDrinkList.size
    }

    fun setDrinkList(ingestedDrinks : List<IngestedDrink>) {
        ingestedDrinkList = ingestedDrinks
        notifyDataSetChanged()
    }
}