package com.example.manageproxies.app.presentation.vm

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
import com.example.manageproxies.app.presentation.usecase.GetModemApiUsecase
import com.example.manageproxies.app.presentation.usecase.GetModemIpApiUsecase
import com.example.manageproxies.app.presentation.usecase.GetServerApiUsecase
import com.example.manageproxies.app.presentation.usecase.GetTokenUsecase
import com.example.manageproxies.app.presentation.usecase.SaveServerToDatabaseUsecase
import com.example.manageproxies.app.presentation.usecase.SaveTokenUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val saveTokenUseCase: SaveTokenUsecase,
    private val getTokenUseCase: GetTokenUsecase,
    private val getServerApiUseCase: GetServerApiUsecase,
    private val getModemApiUseCase: GetModemApiUsecase,
    private val getModemIpApiUseCase: GetModemIpApiUsecase,
    private val saveServerToDatabaseUsecase: SaveServerToDatabaseUsecase
) : ViewModel() {

    @Volatile
    private var isProcessing = false

    private val _serverInfo = MutableLiveData<List<ServerUi>>()
    val serverInfo: LiveData<List<ServerUi>> = _serverInfo

    private val _modems = MutableLiveData<List<ModemUi>>()
    val modems: LiveData<List<ModemUi>> = _modems

    init {
        getServerApi()
        getModemApi()
    }

    fun saveToken(token: Token) {
        saveTokenUseCase.execute(token)

    }

    fun getToken(): Token {
        return getTokenUseCase.execute()
    }

    fun saveServer(server: List<ServerUi>) {
        viewModelScope.launch(Dispatchers.IO) {
            saveServerToDatabaseUsecase.execute(server)
        }

    }

    fun getServerApi() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val serverInfo = getServerApiUseCase.execute()
                    .map { it.toServerUi() }
                _serverInfo.postValue(serverInfo)
                saveServer(serverInfo)
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
        if (isProcessing) return
        viewModelScope.launch(Dispatchers.IO) {
            isProcessing = true
            try {
                val currentModems = _modems.value?.toMutableList() ?: return@launch

                for ((index, modem) in currentModems.withIndex()) {
                    try {
                        val isActive = !getModemIpApiUseCase.execute(modem.id.toString())
                            .toModemIpUi().eid.isNullOrEmpty()

                        currentModems[index] = modem.copy(status = isActive)

                        _modems.postValue(currentModems.toList())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace() //
            }
        }
    }
}