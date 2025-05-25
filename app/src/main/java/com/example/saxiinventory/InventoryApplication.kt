package com.example.saxiinventory

import android.app.Application
import com.example.saxiinventory.di.KoinModule.Companion.initKoin
import timber.log.Timber

class InventoryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree()) // Logs only in debug mode
        initKoin(this)
    }
}