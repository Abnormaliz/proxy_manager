package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.repository.TokenRepository
import javax.inject.Inject

class GetAllApiTokensFromDatabaseUsecase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend fun getAllApiTokens(): List<ApiToken> {
        return tokenRepository.getAllApiTokensFromDatabase()
    }
}