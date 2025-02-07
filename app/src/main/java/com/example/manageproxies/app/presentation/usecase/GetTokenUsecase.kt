package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.repository.TokenRepository

class GetTokenUsecase(private val tokenRepository: TokenRepository) {

    fun execute() : Token {
        return tokenRepository.getToken()
    }


}