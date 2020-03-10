package com.vaudibert.canidrive.ui.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vaudibert.canidrive.R
import com.vaudibert.canidrive.data.DrinkDatabase
import com.vaudibert.canidrive.data.IngestedDrinkEntity
import com.vaudibert.canidrive.data.PresetDrinkEntity
import com.vaudibert.canidrive.domain.drink.IngestedDrink
import com.vaudibert.canidrive.domain.drink.IngestionService
import com.vaudibert.canidrive.domain.drink.PresetDrink
import com.vaudibert.canidrive.domain.drink.PresetDrinkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class DrinkRepository(context: Context, drinkDatabase: DrinkDatabase) {

    private val _livePastDrinks = MutableLiveData<List<IngestedDrinkEntity>>()
    val livePastDrinks: LiveData<List<IngestedDrinkEntity>>
        get() = _livePastDrinks

    private val _livePresetDrinks = MutableLiveData<List<PresetDrinkEntity>>()
    val livePresetDrinks: LiveData<List<PresetDrinkEntity>>
        get() = _livePresetDrinks

    private val _liveSelectedPreset = MutableLiveData<PresetDrinkEntity?>()
    val liveSelectedPreset: LiveData<PresetDrinkEntity?>
        get() = _liveSelectedPreset

    private val ingestedDrinkDao = drinkDatabase.ingestedDrinkDao()
    private val presetDrinkDao = drinkDatabase.presetDrinkDao()

    private val daoJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + daoJob)


    private val presetMaker: (String, Double, Double) -> PresetDrinkEntity = {
            name:String, volume:Double, degree:Double ->
                val newPreset = PresetDrink(name, volume, degree)
                val newPresetEntity = PresetDrinkEntity(-1, newPreset)
                uiScope.launch {
                    newPresetEntity.uid = presetDrinkDao.insert(newPreset)
                }
                newPresetEntity
    }

    val presetService = PresetDrinkService(presetMaker)

    private val ingestor: (PresetDrinkEntity, Date) -> IngestedDrinkEntity = {
        preset : PresetDrinkEntity, ingestionTime : Date ->
            val newIngested = IngestedDrink(preset.name, preset.volume, preset.degree, ingestionTime)
            val newIngestedEntity = IngestedDrinkEntity(-1, newIngested)
            uiScope.launch { newIngestedEntity.uid = ingestedDrinkDao.insert(newIngested) }
            newIngestedEntity
    }

    val ingestionService = IngestionService(ingestor)

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

        ingestionService.onIngestedChanged = {
            _livePastDrinks.postValue(it)
        }

        // Add all drinks to current state
        uiScope.launch {
            ingestionService.populate(ingestedDrinkDao.getAll())
        }

        ingestionService.onRemoved = {
            uiScope.launch {
                ingestedDrinkDao.remove(it)
            }
        }

        presetService.onPresetsChanged = {
            _livePresetDrinks.postValue(it)
        }

        presetService.onSelectUpdated = {
            _liveSelectedPreset.postValue(it)
        }

        // Get all preset drinks
        uiScope.launch {
            if (presetDrinkDao.count() == 0) {
                presetDrinkDao.insertAll(defaultPresetDrink)
                Log.d("DrinkRepo", "preset count was 0")
            }
            presetService.populate(presetDrinkDao.getAll())
        }

        presetService.onPresetRemoved = {
            uiScope.launch {
                presetDrinkDao.remove(it)
            }
        }

        presetService.onPresetUpdated = {
            uiScope.launch {
                presetDrinkDao.update(it)
            }
        }

        // Connect the presetService to the ingestionService
        presetService.ingestionService = ingestionService
    }

}