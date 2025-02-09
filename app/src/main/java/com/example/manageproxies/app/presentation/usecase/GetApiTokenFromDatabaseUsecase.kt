package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.repository.TokenRepository
import javax.inject.Inject

class GetApiTokenFromDatabaseUsecase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend fun getApiToken(apiTokenName: String) {
        tokenRepository.getApiTokenByName(apiTokenName)
    }
}