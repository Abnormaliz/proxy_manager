package com.example.manageproxies.app.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageproxies.app.presentation.models.toModemIpUi
import com.example.manageproxies.app.presentation.usecase.GetAllApiTokensFromDatabaseUsecase
import com.example.manageproxies.app.presentation.usecase.GetModemIpApiUsecase
import com.example.manageproxies.app.presentation.usecase.ModemManager
import com.example.manageproxies.app.presentation.usecase.SetOneServerInfoUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ModemsScreenViewModel @Inject constructor(
    private val getModemIpApiUseCase: GetModemIpApiUsecase,
    private val setOneServerInfoUsecase: SetOneServerInfoUsecase,
    private val getAllApiTokenFromDatabaseUsecase: GetAllApiTokensFromDatabaseUsecase,
    private val modemManager: ModemManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ModemsScreenState>(ModemsScreenState())
    val uiState: StateFlow<ModemsScreenState> = _uiState.asStateFlow()

    fun handleIntent(intent: ModemsScreenIntent) {
        Log.d("duplicate", "Intent received: $intent")
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

            is ModemsScreenIntent.UpdateServerDomain -> {
                val currentDomain = _uiState.value.serverDomain
                if (currentDomain != intent.serverDomain) {
                    viewModelScope.launch {
                        _uiState.update { it.copy(isLoading = true, serverDomain = intent.serverDomain) }
                        loadAllApiTokensFromDatabase()
                        setServerInfo()
                        setModemStatusNew()
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    Log.d("123", "modemManager: ${modemManager.serverDomain.value}")
                }
            }
        }
    }

    private suspend fun loadAllApiTokensFromDatabase() {
        try {
            val apiTokens = withContext(Dispatchers.IO) {
                getAllApiTokenFromDatabaseUsecase.getAllApiTokens()
            }
            _uiState.update { it.copy(apiTokenList = apiTokens, errors = null) }
        } catch (e: Exception) {
            val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val errorMessage = "[$timestamp] ${e.message}"
            _uiState.update { it.copy(errors = mapOf("requestError" to errorMessage)) }
        }
    }

    private suspend fun setServerInfo() {
        Log.d("duplicate", "Updating server domain:")
        try {
            val serverInfo = setOneServerInfoUsecase.getServerInfo(
                _uiState.value.apiTokenList,
                _uiState.value.serverDomain.toString()
            )
            _uiState.update { it.copy(server = serverInfo, errors = null) }
        } catch (e: Exception) {
            val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val errorMessage = "[$timestamp] ${e.message}"
            _uiState.update { it.copy(errors = mapOf("requestError" to errorMessage)) }
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
                val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                val errorMessage = "[$timestamp] ${e.message}"
                _uiState.update { it.copy(errors = mapOf("requestError" to errorMessage)) }
            }
        }
    }
}
