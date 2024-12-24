package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.repository.TokenRepository
import javax.inject.Inject

class SaveApiTokenToDatabaseUsecase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend fun saveApiToken(apiToken: ApiToken) {
        tokenRepository.saveApiTokenToDatabase(apiToken)
    }
}