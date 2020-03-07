package com.vaudibert.canidrive.ui.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.data.DrinkDatabase
import com.vaudibert.canidrive.data.PresetDrinkEntity
import com.vaudibert.canidrive.domain.drink.DrinkService
import com.vaudibert.canidrive.domain.drink.IngestedDrink
import com.vaudibert.canidrive.domain.drink.PresetDrink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DrinkRepository(context: Context, drinkDatabase: DrinkDatabase) {

    private val _livePastDrinks = MutableLiveData<MutableList<IngestedDrink>>()
    val livePastDrinks: LiveData<MutableList<IngestedDrink>>
        get() = _livePastDrinks

    private val _livePresetDrinks = MutableLiveData<List<PresetDrink>>()
    val livePresetDrinks: LiveData<List<PresetDrink>>
        get() = _livePresetDrinks

    val drinkService = DrinkService()

    // TODO : move this default data in the database init ?
    private val defaultPresetDrink = mutableListOf(
        PresetDrink(
            context.getString(R.string.preset_red_wine),
            130.0,
            13.0
        ),
        PresetDrink(
            context.getString(R.string.preset_light_beer),
            250.0,
            4.5
        ),
        PresetDrink(
            context.getString(R.string.preset_light_beer),
            500.0,
            4.5
        ),
        PresetDrink(
            context.getString(R.string.preset_triple_beer),
            330.0,
            9.0
        ),
        PresetDrink(
            context.getString(R.string.preset_soft_cider),
            250.0,
            2.5
        ),
        PresetDrink(
            context.getString(R.string.preset_martini),
            80.0,
            17.0
        ),
        PresetDrink(
            context.getString(R.string.preset_whisky),
            80.0,
            30.0
        )
    )


    init {
        val ingestedDrinkDao = drinkDatabase.ingestedDrinkDao()
        val presetDrinkDao = drinkDatabase.presetDrinkDao()

        val daoJob = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + daoJob)

        // Add all drinks to current state
        uiScope.launch {
            val ingestedDrinks = ingestedDrinkDao.getAll().map { it.toIngestedDrink() }
            drinkService.ingestForInit(ingestedDrinks)
            _livePastDrinks.postValue(drinkService.ingestedDrinks)
        }

        // Get all preset drinks
        uiScope.launch {
            if (presetDrinkDao.count() == 0) {
                presetDrinkDao.insertAll(defaultPresetDrink)
                Log.d("DrinkRepo", "preset count was 0")
            }
            drinkService.presetDrinks = presetDrinkDao.getAll().toMutableList()
            sortAndPostPresets()
        }

        // Then set callbacks to keep DB updated
        drinkService.ingestCallback = {
            uiScope.launch {
                ingestedDrinkDao.insert(it)
                _livePastDrinks.postValue(drinkService.ingestedDrinks)
            }
        }

        drinkService.removeCallback = {
            uiScope.launch {
                ingestedDrinkDao.remove(it)
                _livePastDrinks.postValue(drinkService.ingestedDrinks)
            }
        }

        drinkService.onPresetAdded = {
            uiScope.launch {
                // Special case as a PresetDrink is inserted and returned as an Entity (with uid)
                // The returned entity needs to be placed instead of the PresetDrink
                val newPresetUid = presetDrinkDao.insert(it)
                val index = drinkService.presetDrinks.indexOf(it)
                drinkService.presetDrinks[index] = PresetDrinkEntity(newPresetUid, it)
                sortAndPostPresets()
            }
        }

        drinkService.onPresetUpdated = {previous: PresetDrink, updated: PresetDrink ->
            val updatedUid = (previous as PresetDrinkEntity).uid
            val updatedEntity = PresetDrinkEntity(updatedUid, updated)
            drinkService.selectedPreset = updatedEntity
            uiScope.launch {
                presetDrinkDao.update(updatedEntity)
                drinkService.presetDrinks[drinkService.presetDrinks.indexOf(updated)] = updatedEntity
                sortAndPostPresets()
            }
        }

        drinkService.onPresetIngested = {
            uiScope.launch {
                presetDrinkDao.update(it as PresetDrinkEntity)
                sortAndPostPresets()
            }
        }

        drinkService.onPresetRemoved = {
            uiScope.launch {
                presetDrinkDao.remove(it)
                sortAndPostPresets()
            }
        }
    }

    private fun sortAndPostPresets() {
        _livePresetDrinks.postValue(drinkService.presetDrinks.sortedByDescending { it.count })
    }
}