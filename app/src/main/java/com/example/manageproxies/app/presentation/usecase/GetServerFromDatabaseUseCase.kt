package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.repository.TokenRepository
import javax.inject.Inject

class GetServerFromDatabaseUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {

    fun execute(servedId: Int) {
        tokenRepository.getServerFromDatabase(servedId)
    }
}