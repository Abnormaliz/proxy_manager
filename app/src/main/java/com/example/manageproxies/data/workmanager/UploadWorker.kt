package com.example.manageproxies.data.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.manageproxies.app.presentation.models.toServerUi
import com.example.manageproxies.app.repository.TokenRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val tokenRepository: TokenRepository
) :
    CoroutineWorker(appContext, workerParams) {
    init {
        Log.i("TestWorker", "Worker instance created")
    }

    override suspend fun doWork(): Result {
        Log.i("TestWorker", "Worker is running")
        try {
            val token = tokenRepository.getToken().value.toString()
            Log.i("TestWorker", token)
            val servers = tokenRepository.getServerFromApi(token).map { it.toServerUi() }
            tokenRepository.saveServerToDatabase(servers)
            scheduleNextWork()
            Log.i("TestWorker", "Worker finished")
            return Result.success()
        } catch (e: Exception) {
            Log.e("TestWorker", "Error during work")
            return Result.retry()
        }

    }

    private fun scheduleNextWork() {
        val workRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniqueWork(
                "OneTimeWorker",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }
}