package com.example.manageproxies.app.presentation.vm

import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.models.ServerInfoUi

data class ServersScreenState(
    val serverList: List<ServerInfoUi> = emptyList(),
    val apiTokenList: List<ApiToken> = emptyList(),
    val errors: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val totalIncome: Int = 0,
    val amountOfServers: Int = 0
)