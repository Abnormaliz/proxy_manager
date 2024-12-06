package com.example.manageproxies.data.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.manageproxies.app.presentation.models.DemoWorker
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val demoWorker: DemoWorker
) :
    CoroutineWorker(appContext, workerParams) {
    init {
        Log.i("TestWorker", "Worker instance created")
    }

    override suspend fun doWork(): Result {
        Log.i("TestWorker", "Worker is running")
        for (i in 0..demoWorker.c) {
            Log.i("TestWorker", "$i")
        }
        return Result.success()
    }
}