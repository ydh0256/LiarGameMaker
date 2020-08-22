package com.duckkite.android.liargamemaker

import android.app.Application
import com.duckkite.android.liargamemaker.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class LiarGameMakerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            androidContext(this@LiarGameMakerApplication)
            modules(appModules)
        }
        // Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}