package com.vaudibert.canidrive

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat

class PastDrinksAdapter(
    context: Context,
    private val drinks:ArrayList<AbsorbedDrink>) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    val dateFormat = SimpleDateFormat("HH:mm")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val drinkView = inflater.inflate(R.layout.item_past_drink, parent, false)

        val quantityText = drinkView.findViewById(R.id.pastDrinkTextQuantity) as TextView
        val degreeText = drinkView.findViewById(R.id.pastDrinkTextDegree) as TextView
        val timeText = drinkView.findViewById(R.id.pastDrinkTextTime) as TextView

        val drink = getItem(position)

        quantityText.text = drink.qtyMilliLiter.toString()
        degreeText.text = "${drink.degree}%"
        timeText.text = dateFormat.format(drink.ingestionTime)

        return drinkView
    }

    override fun getItem(position: Int): AbsorbedDrink {
        return drinks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int = drinks.size
}