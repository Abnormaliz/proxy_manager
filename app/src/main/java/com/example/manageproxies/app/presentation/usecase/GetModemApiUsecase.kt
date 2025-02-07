package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.remote.Modem
import javax.inject.Inject

class GetModemApiUsecase @Inject constructor(
    private val tokenRepository: TokenRepository, ) {

    suspend fun execute(): List<Modem> {
        return tokenRepository.getModemsFromApi(tokenRepository.getToken().value.toString())
    }
}