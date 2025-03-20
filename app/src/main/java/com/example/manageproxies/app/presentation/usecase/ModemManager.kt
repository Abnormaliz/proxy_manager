package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModemManager @Inject constructor(
    private val repository: TokenRepository
) {
    private val _serverDomain = MutableStateFlow("")
    val serverDomain: StateFlow<String> = _serverDomain.asStateFlow()

    fun setServerDomain(serverDomain: String) {
        _serverDomain.value = serverDomain
    }
}