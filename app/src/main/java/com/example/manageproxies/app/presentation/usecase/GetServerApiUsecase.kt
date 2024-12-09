package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.remote.Server
import javax.inject.Inject


class GetServerApiUsecase @Inject constructor(
    private val tokenRepository: TokenRepository, ) {

    suspend fun execute(): List<Server> {
        return tokenRepository.getServerFromApi(tokenRepository.getToken().value.toString())
    }
}