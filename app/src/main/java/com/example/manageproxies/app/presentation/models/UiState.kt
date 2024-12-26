package com.example.manageproxies.app.presentation.models

import com.example.manageproxies.data.remote.ApiResponse
import com.example.manageproxies.data.remote.Server

sealed class UiState {
    object Loading: UiState()
    data class SuccessServer(val servers: List<Server>): UiState()
    data class SuccessApiResponse(val apiResponse: ApiResponse): UiState()
    data class Error(val message: String): UiState()
}