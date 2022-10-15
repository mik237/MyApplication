package com.example.myapplication.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.notifications.NotificationManager
import com.example.myapplication.utils.TestRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class SampleWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val testRepo: TestRepo
) :
    CoroutineWorker(context, workerParameters) {

//    @Inject lateinit var testRepo: TestRepo

    private var count = 0
    override suspend fun doWork(): Result {
        return try {
            try {
                testRepo.printTestRepo()
                Log.d("MyWorker", "Running work manager ${System.currentTimeMillis()}")
                count++
                NotificationManager.sendNotification(context, "doWork count: ${count}")
                //Do Your task here
                Result.success()
            } catch (e: Exception) {
                Log.d("MyWorker", "exception in doWork ${e.message}")
                NotificationManager.sendNotification(context, "exception in doWork ${e.message}")

                Result.success()
            }
        } catch (e: Exception) {
            Log.d("MyWorker", "outer catch: exception in doWork ${e.message}")
            NotificationManager.sendNotification(
                context,
                "outer catch: exception in doWork ${e.message}"
            )

            Result.success()
        }
    }
}