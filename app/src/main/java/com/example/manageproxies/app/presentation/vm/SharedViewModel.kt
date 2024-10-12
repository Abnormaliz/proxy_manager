package com.example.manageproxies.app.presentation.vm

import androidx.lifecycle.ViewModel
import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.presentation.usecase.GetTokenUseCase
import com.example.manageproxies.app.presentation.usecase.SaveTokenUseCase
import com.example.manageproxies.app.presentation.usecase.getServerInfoApiUseCase
import com.example.manageproxies.data.remote.ServerInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val saveTokenUseCase: SaveTokenUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val getServerInfoApiUseCase: getServerInfoApiUseCase
) : ViewModel() {

    fun saveToken(token: Token) {
        saveTokenUseCase.execute(token)

    }

    fun getToken(): Token {
        return getTokenUseCase.execute()
    }

    suspend fun getServerInfoApi(): ServerInfo {
        return getServerInfoApiUseCase.execute()
    }
}