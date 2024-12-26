package com.example.manageproxies.data.remote

sealed class ApiResult {
    data class SuccessServer(val servers: List<Server>) : ApiResult()
    data class SuccessApiResponse(val response: ApiResponse): ApiResult()
    data class Error(val error: String): ApiResult()
}