package com.vaudibert.canidrive.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.drink.PresetDrink

class PresetDrinksAdapter(
    val context: Context,
    private val presetDrinks: LiveData<List<PresetDrink>>
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (position == 0) {
            val addPresetView = inflater.inflate(R.layout.item_add_preset, parent, false)

            val addDescriptionText = addPresetView.findViewById(R.id.textViewAddPresetDescription) as TextView

            addDescriptionText.text = context.getString(R.string.add_preset_description)

            return addPresetView

        } else {

            val drinkView = inflater.inflate(R.layout.item_preset_drink, parent, false)
            val drink = getItem(position)

            val propertiesText = drinkView.findViewById(R.id.textViewPresetDrinkProperties) as TextView
            val descriptionText = drinkView.findViewById(R.id.textViewPresetDrinkDescription) as TextView
            val glassImage = drinkView.findViewById(R.id.imageViewPresetDrinkIcon) as ImageView

            propertiesText.text = "${drink.volume} ml - ${drink.degree} %"
            descriptionText.text = drink.name
            glassImage.setImageResource(R.drawable.wine_glass)

            return drinkView

        }
    }

    override fun getItem(position: Int): PresetDrink {
        return presetDrinks.value?.get(position-1) ?: PresetDrink("No preset", 0.0, 0.0)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return (presetDrinks.value?.size ?: 0) + 1
    }

}