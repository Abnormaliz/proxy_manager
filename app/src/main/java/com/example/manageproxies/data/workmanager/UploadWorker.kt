package com.example.manageproxies.data.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.manageproxies.app.repository.TokenRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TokenRepository
) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.i("UploadWorkerTag", "Worker started")
        repository.getServerFromApi(repository.getToken().toString())
        Log.i("UploadWorkerTag", "Worker completed")
        return Result.success()
    }
}