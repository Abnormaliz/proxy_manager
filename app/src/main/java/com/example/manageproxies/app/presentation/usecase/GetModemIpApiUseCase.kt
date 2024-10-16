package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.remote.ModemIp
import jakarta.inject.Inject

class GetModemIpApiUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {

    suspend fun execute(eid: String?): ModemIp {
        return tokenRepository.getModemIpFromApi(tokenRepository.getToken().value.toString(), eid)
    }
}