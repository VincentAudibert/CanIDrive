package com.vaudibert.canidrive.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.data.IngestedDrinkEntity
import com.vaudibert.canidrive.ui.CanIDrive
import java.text.DateFormat
import java.text.DecimalFormat
import java.util.*

class IngestedDrinksAdapter(
    val context: Context
    ) : BaseAdapter() {

    private var ingestedDrinkList : List<IngestedDrinkEntity> = emptyList()

    private val DAY_IN_MILLIS = 3600*1000*24

    private val dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

    private val doubleFormat : DecimalFormat = DecimalFormat("0.#")

    // TODO : inject service ?
    private val ingestionService =
        CanIDrive.instance.mainRepository.drinkRepository.ingestionService

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val drinkView = inflater.inflate(R.layout.item_past_drink, parent, false)
        val drink = getItem(position)

        val propertiesText = drinkView.findViewById(R.id.textViewPresetDrinkProperties) as TextView
        val descriptionText = drinkView.findViewById(R.id.textViewPresetDrinkDescription) as TextView
        val glassImage = drinkView.findViewById(R.id.imageViewPresetDrinkIcon) as ImageView
        val deleteButton = drinkView.findViewById(R.id.buttonRemovePastDrink) as ImageButton
        val timeText = drinkView.findViewById(R.id.textViewPastDrinkTime) as TextView
        val daysText = drinkView.findViewById(R.id.textViewPastDays) as TextView

        propertiesText.text = "${doubleFormat.format(drink.volume)} ml - ${drink.degree} %"
        descriptionText.text = drink.name
        glassImage.setImageResource(R.drawable.wine_glass)
        val days: Long = (drink.ingestionTime.time / DAY_IN_MILLIS) - (Date().time / DAY_IN_MILLIS)
        if (days == 0L)
            daysText.visibility = TextView.GONE
        else {
            daysText.visibility = TextView.VISIBLE
            daysText.text = "$days${context.getString(R.string.day_unit)} "
        }

        timeText.text = dateFormat.format(drink.ingestionTime)

        deleteButton.setOnClickListener {
            ingestionService.remove(drink)
        }

        return drinkView
    }

    override fun getItem(position: Int): IngestedDrinkEntity {
        return ingestedDrinkList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return ingestedDrinkList.size
    }

    fun setDrinkList(ingestedDrinks : List<IngestedDrinkEntity>) {
        ingestedDrinkList = ingestedDrinks
        notifyDataSetChanged()
    }
}