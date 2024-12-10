package com.example.manageproxies.app.presentation.usecase

import android.icu.util.Calendar
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.manageproxies.data.workmanager.UploadWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduledSavingServerInfoToDatabaseUseCase @Inject constructor(
    private val uploadWorker: WorkManager
) {
    fun scheduleDailyWork() {

        Log.i("TestWorker", "Scheduling work to run every minute")
        val workRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        uploadWorker.enqueueUniqueWork(
            "minuteWork",
            ExistingWorkPolicy.REPLACE,
            workRequest)
    }

    private fun calculateInitialDelay(targetHour: Int, targetMinute: Int): Long {
        val now = Calendar.getInstance()
        Log.i("TestWorker", "$now")
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, targetMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
        }
        return targetTime.timeInMillis - now.timeInMillis
    }
}