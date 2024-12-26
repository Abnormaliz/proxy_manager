package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.remote.ModemIp
import javax.inject.Inject

class GetModemIpApiUsecase @Inject constructor(
    private val tokenRepository: TokenRepository
) {

    suspend fun execute(eid: String?): ModemIp {
        return tokenRepository.getModemIpFromApi(tokenRepository.getApiTokenByName("1").value.toString(), eid)
    }
}