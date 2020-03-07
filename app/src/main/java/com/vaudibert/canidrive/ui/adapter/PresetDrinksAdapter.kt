package com.vaudibert.canidrive.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.domain.drink.DrinkService
import com.vaudibert.canidrive.domain.drink.PresetDrink

class PresetDrinksAdapter(
    val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val presetDrinks: LiveData<List<PresetDrink>>,
    private val goToAddPreset: () -> Unit,
    private val drinkService: DrinkService
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    var selectedPreset = MutableLiveData<PresetDrink?>(null)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (position == 0) {
            val addPresetView = inflater.inflate(R.layout.item_add_preset, parent, false)

            val addDescriptionText = addPresetView.findViewById(R.id.textViewAddPresetDescription) as TextView

            addDescriptionText.text = context.getString(R.string.add_preset_description)

            addPresetView.setOnClickListener {
                selectedPreset.postValue(null)

                goToAddPreset()
            }

            return addPresetView

        } else {

            val drinkView = inflater.inflate(R.layout.item_preset_drink, parent, false)
            val presetDrink = getItem(position)

            val propertiesText = drinkView.findViewById(R.id.textViewPresetDrinkProperties) as TextView
            val descriptionText = drinkView.findViewById(R.id.textViewPresetDrinkDescription) as TextView
            val glassImage = drinkView.findViewById(R.id.imageViewPresetDrinkIcon) as ImageView
            val deleteButton = drinkView.findViewById(R.id.buttonRemovePresetDrink) as ImageButton

            propertiesText.text = "${presetDrink.volume} ml - ${presetDrink.degree} %"
            descriptionText.text = presetDrink.name
            glassImage.setImageResource(R.drawable.wine_glass)

            selectedPreset.observe(lifecycleOwner, Observer {
                updatePresetColor(presetDrink, drinkView, deleteButton)
            })

            val clickListener = {_:View ->
                selectedPreset.postValue(if (presetDrink == selectedPreset.value) null else presetDrink)
            }
            propertiesText.setOnClickListener(clickListener)
            descriptionText.setOnClickListener(clickListener)
            glassImage.setOnClickListener(clickListener)

            deleteButton.setOnClickListener {
                if (presetDrink != selectedPreset.value) return@setOnClickListener

                drinkService.removePreset(presetDrink)
            }

            return drinkView

        }
    }

    private fun updatePresetColor(
        drink: PresetDrink,
        drinkView: View,
        deleteButton: ImageButton
    ) {
        if (drink == selectedPreset.value) {
            drinkView.setBackgroundResource(R.drawable.background_color_primary)
            deleteButton.visibility = ImageButton.VISIBLE
        } else {
            drinkView.setBackgroundResource(R.drawable.background_color_none)
            deleteButton.visibility = ImageButton.GONE
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