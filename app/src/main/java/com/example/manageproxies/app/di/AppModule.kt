package com.example.manageproxies.app.di

import com.example.manageproxies.app.presentation.usecase.GetTokenUsecase
import com.example.manageproxies.app.presentation.usecase.SaveTokenUsecase
import com.example.manageproxies.app.repository.TokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {

    @Provides
    fun provideSaveTokenUseCase(tokenRepository: TokenRepository): SaveTokenUsecase {
        return SaveTokenUsecase(tokenRepository = tokenRepository)
    }

    @Provides
    fun provideGetTokenUseCase(tokenRepository: TokenRepository): GetTokenUsecase {
        return GetTokenUsecase(tokenRepository = tokenRepository)
    }
}