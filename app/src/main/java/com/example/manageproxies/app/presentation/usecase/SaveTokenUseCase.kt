package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.repository.TokenRepository

class SaveTokenUseCase(private val tokenRepository: TokenRepository) {

    fun execute (value: Token): Boolean {
        val result: Boolean = tokenRepository.saveToken(token = value)
        return result
    }
}