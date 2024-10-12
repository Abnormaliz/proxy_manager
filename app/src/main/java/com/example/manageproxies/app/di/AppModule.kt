package com.example.manageproxies.app.di

import com.example.manageproxies.app.presentation.usecase.GetTokenUseCase
import com.example.manageproxies.app.presentation.usecase.SaveTokenUseCase
import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.remote.MyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class, SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMyApi(): MyApi {
        return Retrofit.Builder()
            .baseUrl("https://mobileproxy.space/api.html?command=load_modems")
            .build()
            .create(MyApi::class.java)
    }

    @Provides
    fun provideSaveTokenUseCase(tokenRepository: TokenRepository): SaveTokenUseCase {
        return SaveTokenUseCase(tokenRepository = tokenRepository)
    }

    @Provides
    fun provideGetTokenUseCase(tokenRepository: TokenRepository): GetTokenUseCase {
        return GetTokenUseCase(tokenRepository = tokenRepository)
    }
}