package com.example.manageproxies.data.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.manageproxies.app.presentation.models.toDailyStatistic
import com.example.manageproxies.app.repository.TokenRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val tokenRepository: TokenRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val token = tokenRepository.getToken().value.toString()
            val statistics = try {
                tokenRepository.getServerFromApi(token).map { it.toDailyStatistic() }
            } catch (e: Exception) {
                return Result.failure()
            }
            val currentDate = getCurrentDate()
            val statisticsWithDate = statistics.map { it.copy(date = currentDate) }
            tokenRepository.saveDailyStatisticToDatabase(statisticsWithDate)

            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }

    }

    private fun scheduleNextWork() {
        val workRequest =
            OneTimeWorkRequestBuilder<UploadWorker>().setInitialDelay(1, TimeUnit.MINUTES).build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            "OneTimeWorker", ExistingWorkPolicy.REPLACE, workRequest
        )
    }

    private fun getCurrentDate(): String {
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return formatter.format(java.util.Date())
    }
}