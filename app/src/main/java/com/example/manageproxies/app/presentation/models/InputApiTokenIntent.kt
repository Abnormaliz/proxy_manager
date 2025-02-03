package com.example.manageproxies.app.presentation.models

sealed class InputApiTokenIntent {
    data class NameChanged(val newValue: String?) : InputApiTokenIntent()
    data class TokenChanged(val newValue: String?) : InputApiTokenIntent()
    object SaveApiToken : InputApiTokenIntent()
}