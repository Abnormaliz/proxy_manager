package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.presentation.models.DailyStatistic
import com.example.manageproxies.app.repository.TokenRepository
import javax.inject.Inject

class GetDailyStatisticFromDatabase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend fun execute(currentDate: String): DailyStatistic? {
        return tokenRepository.getDailyStatisticFromDatabase(currentDate)
    }
}