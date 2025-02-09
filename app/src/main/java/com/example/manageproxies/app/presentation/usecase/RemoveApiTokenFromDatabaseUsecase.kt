package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.repository.TokenRepository
import javax.inject.Inject

class RemoveApiTokenFromDatabaseUsecase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend fun removeApiTokenFromDatabase(apiToken: ApiToken) {
        tokenRepository.removeApiTokenByName(apiToken)
    }
}