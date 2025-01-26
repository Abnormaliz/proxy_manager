package com.example.manageproxies.app.presentation.models

//sealed class InputApiTokenState {
//    object Loading : InputApiTokenState()
//    data class Success(val message: String) : InputApiTokenState()
//    data class Error(val errors: Map<String, String>) : InputApiTokenState()
//    data class Input(
//        val name: String = "",
//        val token: String = "",
//        val errors: Map<String, String> = emptyMap(),
//        val toastMessage: String? = null
//    ) : InputApiTokenState()
//}

sealed class InputApiTokenIntent {
    data class SaveToken(val name: String, val value: String) : InputApiTokenIntent()
    data class TokenChanged(val newValue: String) : InputApiTokenIntent()
    object MessageShown: InputApiTokenIntent()
    object SaveApiToken : InputApiTokenIntent()
}