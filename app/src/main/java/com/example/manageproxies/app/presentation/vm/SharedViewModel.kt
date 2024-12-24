package com.example.manageproxies.app.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageproxies.app.presentation.models.ModemUi
import com.example.manageproxies.app.presentation.models.ServerInfoUi
import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.models.toModemIpUi
import com.example.manageproxies.app.presentation.usecase.GetModemIpApiUsecase
import com.example.manageproxies.app.presentation.usecase.ScheduledSavingServerInfoToDatabaseUseCase
import com.example.manageproxies.app.presentation.usecase.SetServerInfoUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val saveTokenUseCase: SaveTokenUsecase,
    private val getTokenUseCase: GetTokenUsecase,
    private val getModemIpApiUseCase: GetModemIpApiUsecase,
    private val scheduledSavingServerInfoToDatabaseUseCase: ScheduledSavingServerInfoToDatabaseUseCase,
    private val setServerInfoUseCase: SetServerInfoUsecase
) : ViewModel() {


    private val _serverInfo = MutableLiveData<List<ServerInfoUi>>()
    val serverInfo: LiveData<List<ServerInfoUi>> = _serverInfo

    private val _modems = MutableLiveData<List<ModemUi>>()
    val modems: LiveData<List<ModemUi>> = _modems


    init {
        scheduledSavingServerInfoToDatabaseUseCase.scheduleDailyWork()
        viewModelScope.launch(Dispatchers.IO) {
            getServerApi()

        }

    }

    fun saveToken(apiToken: ApiToken) {
        saveTokenUseCase.execute(apiToken)

    }

    fun getToken(): ApiToken {
        return getTokenUseCase.execute()
    }

    suspend fun getServerApi() {
        try {
            val serverInfo = setServerInfoUseCase.getServerInfo()
            _serverInfo.postValue(serverInfo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setModemStatusNew() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentModems = modems.value
                if (currentModems.isNullOrEmpty()) {
                    return@launch
                }
                val eids = currentModems.map { it.eid }
                val eidsString = eids.joinToString(",")
                val allIps = getModemIpApiUseCase.execute(eidsString).toModemIpUi()
                val ipsMap = allIps.eid ?: emptyMap()
                val updatedModems = currentModems.map { modem ->
                    if (ipsMap.containsKey(modem.eid.toString())) {
                        modem.copy(status = true)
                    } else modem
                }
                _modems.postValue(updatedModems)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
