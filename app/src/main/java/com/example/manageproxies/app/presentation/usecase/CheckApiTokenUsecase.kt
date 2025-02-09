package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.remote.ApiResponse
import com.example.manageproxies.data.remote.ApiResult
import com.example.manageproxies.data.remote.Server
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class CheckApiTokenUsecase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend fun checkApiToken(apiTokenValue: String): ApiResult {
        return try {
            val response = tokenRepository.checkApiToken(apiTokenValue)

            if (response.isSuccessful) {
                val rawBody =
                    response.body()?.string() ?: ApiResult.Error("Empty response body").toString()

                try {
                    val servers = parseAsListOfServers(rawBody)
                    return ApiResult.SuccessServer(servers)
                } catch (e: Exception) {
                    try {
                        val apiResponse = parseAsApiResponse(rawBody)
                        return ApiResult.SuccessApiResponse(apiResponse)
                    } catch (e: Exception) {
                        return ApiResult.Error("Unexpected response format")
                    }
                }
            } else {
                when (response.code()) {
                    429 -> ApiResult.Error("Слишком много запросов, попробуйте через 3 секунды.")
                    else -> ApiResult.Error("Error while requesting: HTTP ${response.code()}")
                }
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown exception")
        }
    }

    private fun parseAsApiResponse(json: String): ApiResponse {
        val gson = Gson()
        return gson.fromJson(json, ApiResponse::class.java)
    }

    private fun parseAsListOfServers(json: String): List<Server> {
        val gson = Gson()
        val type = object : TypeToken<List<Server>>() {}.type
        return gson.fromJson(json, type)
    }
}
