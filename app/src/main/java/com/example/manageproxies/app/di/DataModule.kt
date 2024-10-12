package com.example.manageproxies.app.di

import android.content.Context
import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.remote.MyApi
import com.example.manageproxies.data.repository.TokenRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideTokenRepository(@ApplicationContext context: Context, api: MyApi) : TokenRepository {
        return TokenRepositoryImpl(context = context, api = api)
    }

    @Provides
    @Singleton
    fun provideMyApi(): MyApi {
        return Retrofit.Builder()
            .baseUrl("https://mobileproxy.space/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)
    }
}