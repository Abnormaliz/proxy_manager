package com.example.manageproxies.app.presentation.models

data class InputApiTokenState(
    val nameTextField: String? = null,
    val tokenTextField: String? = null,
    val nameErrorText: Boolean = false,
    val tokenErrorText: Boolean = false
)
