package com.example.manageproxies.app.di

import android.content.Context
import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.repository.TokenRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideTokenRepository(@ApplicationContext context: Context) : TokenRepository {
        return TokenRepositoryImpl(context = context)
    }
}