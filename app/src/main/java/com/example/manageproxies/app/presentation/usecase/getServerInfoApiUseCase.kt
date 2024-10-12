package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.remote.ServerInfo


class getServerInfoApiUseCase(private val tokenRepository: TokenRepository) {

    suspend fun execute(): ServerInfo {
        return tokenRepository.getModemsFromApi()

    }
}