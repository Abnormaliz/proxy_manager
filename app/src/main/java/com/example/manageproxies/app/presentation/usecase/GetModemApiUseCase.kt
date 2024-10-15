package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.remote.Modem
import jakarta.inject.Inject

class GetModemApiUseCase @Inject constructor(
    private val tokenRepository: TokenRepository, ) {

    suspend fun execute(): List<Modem> {
        return tokenRepository.getModemsFromApi(tokenRepository.getToken().value.toString())
    }
}