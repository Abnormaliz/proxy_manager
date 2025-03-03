package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.repository.TokenRepository
import javax.inject.Inject

class SetModemListUsecase @Inject constructor(
    private val tokenRepository: TokenRepository
) {

}