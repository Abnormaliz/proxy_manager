package com.example.manageproxies.app.presentation.vm

import com.example.manageproxies.app.presentation.models.ApiToken

sealed class InputApiTokenScreenIntent {
    data class NameChanged(val newValue: String?) : InputApiTokenScreenIntent()
    data class TokenScreenChanged(val newValue: String?) : InputApiTokenScreenIntent()
    object SaveApiTokenScreen : InputApiTokenScreenIntent()
    data class RemoveApiTokenScreen(val apiToken: ApiToken): InputApiTokenScreenIntent()
}