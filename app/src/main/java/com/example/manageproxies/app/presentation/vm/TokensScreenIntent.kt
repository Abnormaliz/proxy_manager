package com.example.manageproxies.app.presentation.vm

import com.example.manageproxies.app.presentation.models.ApiToken

sealed class TokensScreenIntent {
    data class NameChanged(val newValue: String?) : TokensScreenIntent()
    data class TokensScreenChanged(val newValue: String?) : TokensScreenIntent()
    object SaveApiTokensScreen : TokensScreenIntent()
    data class RemoveApiTokensScreen(val apiToken: ApiToken): TokensScreenIntent()
}