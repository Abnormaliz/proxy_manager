package com.example.manageproxies.app.presentation.vm

import androidx.lifecycle.ViewModel
import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.presentation.usecase.GetModemApiUseCase
import com.example.manageproxies.app.presentation.usecase.GetTokenUseCase
import com.example.manageproxies.app.presentation.usecase.SaveTokenUseCase
import com.example.manageproxies.app.presentation.usecase.GetServerInfoApiUseCase
import com.example.manageproxies.data.remote.Modem
import com.example.manageproxies.data.remote.ServerInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val saveTokenUseCase: SaveTokenUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val getServerInfoApiUseCase: GetServerInfoApiUseCase,
    private val getModemApiUseCase: GetModemApiUseCase
) : ViewModel() {

    fun saveToken(token: Token) {
        saveTokenUseCase.execute(token)

    }

    fun getToken(): Token {
        return getTokenUseCase.execute()
    }

    suspend fun getServerInfoApi(): List<ServerInfo> {
        return getServerInfoApiUseCase.execute()
    }

    suspend fun getModemApi(): List<Modem> {

        return getModemApiUseCase.execute()
    }
}