package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.remote.ServerInfo
import jakarta.inject.Inject


class GetServerInfoApiUseCase @Inject constructor(private val tokenRepository: TokenRepository) {

    suspend fun execute(): List<ServerInfo> {
        return tokenRepository.getModemsFromApi()
    }
}