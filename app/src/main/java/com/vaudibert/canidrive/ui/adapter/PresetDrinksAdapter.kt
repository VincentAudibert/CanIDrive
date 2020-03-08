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
import androidx.lifecycle.Observer
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.data.PresetDrinkEntity
import com.vaudibert.canidrive.ui.repository.DrinkRepository

class PresetDrinksAdapter(
    val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val goToAddPreset: () -> Unit,
    private val drinkRepository: DrinkRepository
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val presetService = drinkRepository.presetService

    private var presetDrinks: List<PresetDrinkEntity> = emptyList()

    init {
        drinkRepository.livePresetDrinks.observe(lifecycleOwner, Observer {
            presetDrinks = it
            notifyDataSetChanged()
        })
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (position == 0) {
            getAddPresetView(parent)
        } else {
            getPresetView(parent, position)
        }
    }

    private fun getPresetView(parent: ViewGroup?, position: Int): View {
        val drinkView = inflater.inflate(R.layout.item_preset_drink, parent, false)
        val presetDrink = getItem(position)

        val propertiesText = drinkView.findViewById(R.id.textViewPresetDrinkProperties) as TextView
        val descriptionText =
            drinkView.findViewById(R.id.textViewPresetDrinkDescription) as TextView
        val glassImage = drinkView.findViewById(R.id.imageViewPresetDrinkIcon) as ImageView
        val deleteButton = drinkView.findViewById(R.id.buttonRemovePresetDrink) as ImageButton

        propertiesText.text = "${presetDrink.volume} ml - ${presetDrink.degree} %"
        descriptionText.text = presetDrink.name
        glassImage.setImageResource(R.drawable.wine_glass)

        drinkRepository.liveSelectedPreset.observe(lifecycleOwner, Observer {
            updatePresetColor(presetDrink, drinkView, deleteButton, it)
        })

        val clickListener = { _: View ->
            presetService.selectedPreset =
                if (presetDrink == drinkRepository.liveSelectedPreset.value)
                    null
                else
                    presetDrink
        }
        val longClickListener = View.OnLongClickListener {
            presetService.selectedPreset = presetDrink
            goToAddPreset()
            true
        }
        propertiesText.setOnClickListener(clickListener)
        propertiesText.setOnLongClickListener(longClickListener)

        descriptionText.setOnClickListener(clickListener)
        descriptionText.setOnLongClickListener(longClickListener)

        glassImage.setOnClickListener(clickListener)
        glassImage.setOnLongClickListener(longClickListener)

        deleteButton.setOnClickListener {
            if (presetDrink != drinkRepository.liveSelectedPreset.value) return@setOnClickListener

            presetService.removePreset(presetDrink)
        }

        return drinkView
    }

    private fun getAddPresetView(parent: ViewGroup?): View {
        val addPresetView = inflater.inflate(
            R.layout.item_add_preset,
            parent,
            false
        )

        val addDescriptionText = addPresetView.findViewById(
            R.id.textViewAddPresetDescription
        ) as TextView

        addDescriptionText.text = context.getString(R.string.add_preset_description)

        addPresetView.setOnClickListener {
            //selectedPreset.postValue(null)
            presetService.selectedPreset = null
            goToAddPreset()
        }

        return addPresetView
    }

    private fun updatePresetColor(
        drink: PresetDrinkEntity,
        drinkView: View,
        deleteButton: ImageButton,
        selected: PresetDrinkEntity?
    ) {
        if (selected != null && drink == selected) {
            drinkView.setBackgroundResource(R.drawable.background_color_primary)
            deleteButton.visibility = ImageButton.VISIBLE
        } else {
            drinkView.setBackgroundResource(R.drawable.background_color_none)
            deleteButton.visibility = ImageButton.GONE
        }
    }

    override fun getItem(position: Int): PresetDrinkEntity = presetDrinks[position-1]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = presetDrinks.size + 1

}