package com.vaudibert.canidrive.ui

import android.app.Application
import com.vaudibert.canidrive.domain.TimeService
import com.vaudibert.canidrive.ui.repository.MainRepository

class CanIDrive : Application() {

    companion object {
        lateinit var instance: CanIDrive
    }

    val time : TimeService = TimeServiceAndroid()

    lateinit var mainRepository : MainRepository

    override fun onCreate() {
        super.onCreate()
        mainRepository = MainRepository(this)
        instance = this
    }
}