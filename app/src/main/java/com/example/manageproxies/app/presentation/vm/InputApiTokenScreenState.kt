package com.example.manageproxies.app.presentation.vm

import com.example.manageproxies.app.presentation.models.ApiToken

data class InputApiTokenScreenState(
    val nameTextField: String = "",
    val tokenTextField: String = "",
    val errors: Map<String, String> = emptyMap(),
    val isServerAdded: Boolean = false,
    val apiTokensList: List<ApiToken> = emptyList()
)


