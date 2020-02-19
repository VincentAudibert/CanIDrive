package com.vaudibert.canidrive.ui.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vaudibert.canidrive.data.DrinkDatabase
import com.vaudibert.canidrive.domain.drink.DrinkService
import com.vaudibert.canidrive.domain.drink.IngestedDrink
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
            val drinks = ingestedDrinkDao.getAll()
            drinks.forEach { drinkService.ingest(it.toDrink())}
            _livePastDrinks.postValue(drinkService.absorbedIngestedDrinks)
        }

        // Then set callbacks to keep DB updated
        drinkService.ingestCallback = {
                ingestedDrink -> run {
            uiScope.launch { ingestedDrinkDao.insert(ingestedDrink) }
        }
            _livePastDrinks.postValue(drinkService.absorbedIngestedDrinks)
        }

        drinkService.removeCallback = {
                deletedDrink -> run {
            uiScope.launch { ingestedDrinkDao.remove(deletedDrink) }
        }
            _livePastDrinks.postValue(drinkService.absorbedIngestedDrinks)
        }
    }
}