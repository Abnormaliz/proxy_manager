package com.example.manageproxies.app.presentation.vm

import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.models.ModemUi
import com.example.manageproxies.app.presentation.models.ServerInfoUi

data class ModemsScreenState(
    val server: ServerInfoUi? = null,
    val serverDomain: String? = null,
    val apiTokenList: List<ApiToken> = emptyList(),
    val modemList: List<ModemUi> = emptyList(),
    val isLoading: Boolean = false,
    val errors: Map<String, String>? = null,
    val isLoaded: Boolean = false,
    val modemManager: String? = null
)