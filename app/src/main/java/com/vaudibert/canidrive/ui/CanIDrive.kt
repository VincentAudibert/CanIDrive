package com.vaudibert.canidrive.ui

import android.app.Application
import com.vaudibert.canidrive.ui.repository.MainRepository

class CanIDrive : Application() {

    companion object {
        lateinit var instance: CanIDrive
    }

    val mainRepository = MainRepository()

    override fun onCreate() {
        super.onCreate()
        instance = this

        mainRepository.setContext(this)
    }
}