package com.example.hackernews.app

import android.app.Application
import com.example.hackernews.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
open class HackerNewsApplication : Application() {
    @Inject
    lateinit var inspectorInitializer: InspectorInitializer

    override fun onCreate() {
        super.onCreate()

        inspectorInitializer.initialize(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}