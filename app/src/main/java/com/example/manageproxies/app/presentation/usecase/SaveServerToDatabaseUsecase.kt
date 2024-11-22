package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.presentation.models.ServerUi
import com.example.manageproxies.app.repository.TokenRepository
import jakarta.inject.Inject

class SaveServerToDatabaseUsecase @Inject constructor
    (private val tokenRepository: TokenRepository) {

    suspend fun execute(server: List<ServerUi>) {
        tokenRepository.saveServerToDatabase(server)
    }
}