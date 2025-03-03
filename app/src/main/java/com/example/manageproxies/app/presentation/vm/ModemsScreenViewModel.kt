package com.example.manageproxies.app.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageproxies.app.presentation.models.toModemIpUi
import com.example.manageproxies.app.presentation.usecase.GetAllApiTokensFromDatabaseUsecase
import com.example.manageproxies.app.presentation.usecase.GetModemIpApiUsecase
import com.example.manageproxies.app.presentation.usecase.SetOneServerInfoUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ModemsScreenViewModel @Inject constructor(
    private val getModemIpApiUseCase: GetModemIpApiUsecase,
    private val setOneServerInfoUsecase: SetOneServerInfoUsecase,
    private val getAllApiTokenFromDatabaseUsecase: GetAllApiTokensFromDatabaseUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ModemsScreenState>(ModemsScreenState())
    val uiState: StateFlow<ModemsScreenState> = _uiState.asStateFlow()

    fun handleIntent(intent: ModemsScreenIntent) {
        when (intent) {
            is ModemsScreenIntent.UpdateModemsScreen -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    loadAllApiTokensFromDatabase()
                    launch { setServerInfo() }.join()
                    setModemStatusNew()
                    _uiState.update { it.copy(isLoading = false) }
                }


            }

            is ModemsScreenIntent.UpdateServerDomain -> _uiState.update {
                it.copy(serverDomain = intent.serverDomain)
            }
        }
    }

    private suspend fun loadAllApiTokensFromDatabase() {
        try {
            val apiTokens = withContext(Dispatchers.IO) {
                getAllApiTokenFromDatabaseUsecase.getAllApiTokens()
            }
            _uiState.update { it.copy(apiTokenList = apiTokens) }
            Log.d("ServersScreen", "${_uiState.value.apiTokenList}")
        } catch (e: Exception) {
            _uiState.update { it.copy(errors = mapOf("requestError" to "Не удалось загрузить данные Api-токенов")) }
        }
    }

    private suspend fun setServerInfo() {
        try {
            val serverInfo = setOneServerInfoUsecase.getServerInfo(
                _uiState.value.apiTokenList,
                _uiState.value.serverDomain.toString()
            )
            _uiState.update { it.copy(server = serverInfo) }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(errors = mapOf("requestError" to "Не удалось загрузить данные сервера"))
            }
        }
    }

    fun setModemStatusNew() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val server = _uiState.value.server
                if (server == null || server.modemList.isEmpty()) {
                    return@launch
                }

                val eids = server.modemList.map { it.eid }

                val eidsString = eids.joinToString(",")

                val allIps = getModemIpApiUseCase.execute(server.token, eidsString).toModemIpUi()

                val ipsMap = allIps.eid ?: emptyMap()

                val updatedModems = server.modemList.map { modem ->
                    if (ipsMap.containsKey(modem.eid.toString())) {
                        modem.copy(status = true)
                    } else modem
                }

                _uiState.update { it.copy(server = server.copy(modemList = updatedModems)) }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
