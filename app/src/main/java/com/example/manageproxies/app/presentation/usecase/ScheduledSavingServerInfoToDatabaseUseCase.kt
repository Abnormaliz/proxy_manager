package com.example.manageproxies.app.presentation.usecase

import android.icu.util.Calendar
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.manageproxies.data.workmanager.UploadWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduledSavingServerInfoToDatabaseUseCase @Inject constructor(
    private val uploadWorker: WorkManager
) {
    fun scheduleDailyWork() {
        val initialDelay = calculateInitialDelay(21, 58)
        Log.i("TestWorker", "Scheduling work to run every day")
        val workRequest =
            PeriodicWorkRequestBuilder<UploadWorker>(24, TimeUnit.HOURS).setInitialDelay(
                initialDelay, TimeUnit.MILLISECONDS
            ).build()

        uploadWorker.enqueueUniquePeriodicWork(
            "dailyWork", ExistingPeriodicWorkPolicy.UPDATE, workRequest
        )
    }

    private fun calculateInitialDelay(targetHour: Int, targetMinute: Int): Long {
        val now = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, targetMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
        }

        val delayMillis = targetTime.timeInMillis - now.timeInMillis
        val delayHours = TimeUnit.MILLISECONDS.toHours(delayMillis)
        val delayMinutes = TimeUnit.MILLISECONDS.toMinutes(delayMillis) % 60

        Log.i(
            "TestWorker",
            "Time left until $targetHour:$targetMinute - $delayHours hours and $delayMinutes minutes"
        )

        return delayMillis
    }
}
