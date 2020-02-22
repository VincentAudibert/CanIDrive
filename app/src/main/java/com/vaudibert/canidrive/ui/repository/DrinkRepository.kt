package com.vaudibert.canidrive.ui.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vaudibert.canidrive.data.DrinkDatabase
import com.vaudibert.canidrive.domain.drink.DrinkService
import com.vaudibert.canidrive.domain.drink.IngestedDrink
import com.vaudibert.canidrive.domain.drink.PresetDrink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DrinkRepository(roomDatabase: DrinkDatabase) {

    private val _livePastDrinks = MutableLiveData<MutableList<IngestedDrink>>()
    val livePastDrinks: LiveData<MutableList<IngestedDrink>>
        get() = _livePastDrinks

    val drinkService = DrinkService()

    init {
        val ingestedDrinkDao = roomDatabase.ingestedDrinkDao()

        val daoJob = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + daoJob)

        // Add all drinks to current state
        uiScope.launch {
            val ingestedDrinks = ingestedDrinkDao.getAll().map { it.toIngestedDrink() }
            drinkService.ingestForInit(ingestedDrinks)
            _livePastDrinks.postValue(drinkService.ingestedDrinks)
        }

        drinkService.presetDrinks = mutableListOf(
            PresetDrink(
                "wine glass",
                130.0,
                13.0
            ),
            PresetDrink(
                "Half-a-pint light beer",
                250.0,
                4.5
            ),
            PresetDrink(
                "Pint light beer",
                500.0,
                4.5
            ),
            PresetDrink(
                "33cl triple",
                33.0,
                9.0
            ),
            PresetDrink(
                "Half-a-pint cider",
                250.0,
                2.5
            ),
            PresetDrink(
                "Apetizer (Martini)",
                8.0,
                17.0
            ),
            PresetDrink(
                "Shot",
                5.0,
                40.0
            )
        )

        // Then set callbacks to keep DB updated
        drinkService.ingestCallback = {
                ingestedDrink -> run {
            uiScope.launch { ingestedDrinkDao.insert(ingestedDrink) }
        }
            _livePastDrinks.postValue(drinkService.ingestedDrinks)
        }

        drinkService.removeCallback = {
                deletedDrink -> run {
            uiScope.launch { ingestedDrinkDao.remove(deletedDrink) }
        }
            _livePastDrinks.postValue(drinkService.ingestedDrinks)
        }
    }
}