package com.example.manageproxies.app.presentation.models

data class InputApiTokenState(
    val nameTextField: String = "",
    val tokenTextField: String = "",
    val errors: Map<String, String> = emptyMap(),
    val isServerAdded: Boolean = false,
    val apiTokensList: List<ApiToken> = emptyList()
)


