package com.example.myapplication

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ExampleApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
    }

}