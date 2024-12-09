package com.example.manageproxies.data.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.manageproxies.app.presentation.models.DemoWorker
import com.example.manageproxies.app.presentation.models.toServerUi
import com.example.manageproxies.app.repository.TokenRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

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
        val servers = tokenRepository.getServerFromApi(tokenRepository.getToken().toString()).map { it.toServerUi() }
        tokenRepository.saveServerToDatabase(servers)
        return Result.success()
    }
}