package com.example.manageproxies.app.presentation.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageproxies.app.presentation.models.ModemUi
import com.example.manageproxies.app.presentation.models.ServerUi
import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.presentation.models.toModemIpUi
import com.example.manageproxies.app.presentation.models.toModemUi
import com.example.manageproxies.app.presentation.models.toServerUi
import com.example.manageproxies.app.presentation.usecase.GetModemApiUseCase
import com.example.manageproxies.app.presentation.usecase.GetModemIpApiUseCase
import com.example.manageproxies.app.presentation.usecase.GetServerApiUseCase
import com.example.manageproxies.app.presentation.usecase.GetTokenUseCase
import com.example.manageproxies.app.presentation.usecase.SaveTokenUseCase
import com.example.manageproxies.data.remote.ModemIp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val saveTokenUseCase: SaveTokenUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val getServerApiUseCase: GetServerApiUseCase,
    private val getModemApiUseCase: GetModemApiUseCase,
    private val getModemIpApiUseCase: GetModemIpApiUseCase
) : ViewModel() {

    private val _serverInfo = MutableLiveData<List<ServerUi>>()
    val serverInfo: LiveData<List<ServerUi>> = _serverInfo

    private val _modems = MutableLiveData<List<ModemUi>>()
    val modems: LiveData<List<ModemUi>> = _modems

    fun saveToken(token: Token) {
        saveTokenUseCase.execute(token)

    }

    fun getToken(): Token {
        return getTokenUseCase.execute()
    }

    fun getServerApi() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val serverInfo = getServerApiUseCase.execute()
                    .map { it.toServerUi() }
                _serverInfo.postValue(serverInfo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getModemApi() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val modems = getModemApiUseCase.execute()
                    .map { it.toModemUi() }
                    .sortedByDescending { it.order != null }
                _modems.postValue(modems)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAmountOfOrders(): Int {
        return _modems.value?.count { it.order != null } ?: 0
    }

    fun setModemStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updatedModems = _modems.value?.map { modem ->
                    val isActive = !getModemIpApiUseCase.execute(modem.id.toString())
                        .toModemIpUi().eid.isNullOrEmpty()
                    modem.copy(status = isActive)
                }
                updatedModems?.let {
                    _modems.postValue(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Log.i("okhttp", "VIEWMODEL ${_modems.value!!.first().status}")
    }
}