package com.example.manageproxies.app

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.manageproxies.app.repository.TokenRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class App : Application() {

//    @Inject
//    lateinit var workerFactory: CustomWorkerFactory

//    override val workManagerConfiguration: Configuration
//        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

    override fun onCreate() {
        super.onCreate()
    }

}

//class CustomWorkerFactory @Inject constructor(private val tokenRepository: TokenRepository): WorkerFactory() {
//    override fun createWorker(
//        appContext: Context,
//        workerClassName: String,
//        workerParameters: WorkerParameters
//    ): ListenableWorker = UploadWorker(tokenRepository = tokenRepository, appContext = appContext, workerParams = workerParameters)
//}