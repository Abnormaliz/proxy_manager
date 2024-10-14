package com.example.manageproxies.app.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageproxies.app.presentation.models.ModemUi
import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.presentation.models.toModemUi
import com.example.manageproxies.app.presentation.usecase.GetModemApiUseCase
import com.example.manageproxies.app.presentation.usecase.GetTokenUseCase
import com.example.manageproxies.app.presentation.usecase.SaveTokenUseCase
import com.example.manageproxies.app.presentation.usecase.GetServerInfoApiUseCase
import com.example.manageproxies.data.remote.Modem
import com.example.manageproxies.data.remote.ServerInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val saveTokenUseCase: SaveTokenUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val getServerInfoApiUseCase: GetServerInfoApiUseCase,
    private val getModemApiUseCase: GetModemApiUseCase
) : ViewModel() {

    private val _serverInfo = MutableLiveData<List<ServerInfo>>()
    val serverInfo: LiveData<List<ServerInfo>> = _serverInfo

    private val _modems = MutableLiveData<List<ModemUi>>()
    val modems: LiveData<List<ModemUi>> = _modems
    
    fun saveToken(token: Token) {
        saveTokenUseCase.execute(token)

    }

    fun getToken(): Token {
        return getTokenUseCase.execute()
    }

    fun getServerInfoApi() {
        viewModelScope.launch {
            try {
                val serverInfo = getServerInfoApiUseCase.execute()
                _serverInfo.postValue(serverInfo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getModemApi() {
        viewModelScope.launch {
            try {
                val modems = getModemApiUseCase.execute().map {
                    it.toModemUi()
                }
                _modems.postValue(modems)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}