package com.example.manageproxies.app.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.database.ServerDatabase
import com.example.manageproxies.data.remote.MyApi
import com.example.manageproxies.data.repository.TokenRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideTokenRepository(
        @ApplicationContext context: Context,
        api: MyApi,
        db: ServerDatabase
    ): TokenRepository {
        return TokenRepositoryImpl(context = context, api = api, db = db)
    }

    @Provides
    @Singleton
    fun provideMyApi(): MyApi {
        return Retrofit.Builder()
            .baseUrl("https://mobileproxy.space/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(provideLoggingInterceptor()).build())
            .build().create(MyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app, ServerDatabase::class.java, "server_db"
    ).build()

    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext appContext: Context
    ) : WorkManager {
        return WorkManager.getInstance(appContext)
    }
}