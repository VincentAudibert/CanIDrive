package com.vaudibert.canidrive.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.AbsorbedDrink
import java.text.SimpleDateFormat

class PastDrinksAdapter(
    context: Context,
    private var drinkList : List<AbsorbedDrink>
    ) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val dateFormat = SimpleDateFormat("HH:mm")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val drinkView = inflater.inflate(R.layout.item_past_drink, parent, false)
        val drink = getItem(position)

        val quantityText = drinkView.findViewById(R.id.pastDrinkTextQuantity) as TextView
        val degreeText = drinkView.findViewById(R.id.pastDrinkTextDegree) as TextView
        val timeText = drinkView.findViewById(R.id.pastDrinkTextTime) as TextView

        quantityText.text = "${drink.qtyMilliLiter} ml"
        degreeText.text = "${drink.degree} %"
        timeText.text = dateFormat.format(drink.ingestionTime)

        val buttonRemovePastDrink =
            drinkView.findViewById(R.id.buttonRemovePastDrink) as ImageButton

        buttonRemovePastDrink.setOnClickListener {
            DrinkerRepository.remove(drink)
            notifyDataSetInvalidated()
        }

        return drinkView
    }

    override fun getItem(position: Int): AbsorbedDrink {
        return drinkList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return drinkList.size
    }

    fun setDrinkList(drinks : List<AbsorbedDrink>) {
        drinkList = drinks
        notifyDataSetChanged()
    }
}