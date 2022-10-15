package com.example.myapplication.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

object WorkManagerScheduler {

    fun refreshPeriodicWork(context: Context) {

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        // Set Execution around 08:00:00 PM
        dueDate.set(Calendar.HOUR_OF_DAY, currentDate[Calendar.HOUR_OF_DAY])
        dueDate.set(Calendar.MINUTE, currentDate[Calendar.MINUTE]+1)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff)

        Log.d("MyWorker", "time difference $minutes")

        //define constraints
        val myConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        /*val refreshCpnWork =
            PeriodicWorkRequest.Builder(SampleWorker::class.java, 15, TimeUnit.MINUTES)
                .setInitialDelay(minutes, TimeUnit.MINUTES)
                .setConstraints(myConstraints)
                .addTag("myWorkManager")
                .build()*/

        val refreshCpnWork = PeriodicWorkRequestBuilder<SampleWorker>(
            repeatInterval = 15, TimeUnit.MINUTES,
            flexTimeInterval = 1, TimeUnit.MINUTES
        ).setInitialDelay(1, TimeUnit.MINUTES)
            .setConstraints(myConstraints)
            .addTag("myWorkManager")
            .build()


        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "myWorkManager",
            ExistingPeriodicWorkPolicy.REPLACE,
            refreshCpnWork
        )

    }
}