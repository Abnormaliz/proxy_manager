package com.example.manageproxies.app.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageproxies.app.presentation.usecase.GetAllApiTokensFromDatabaseUsecase
import com.example.manageproxies.app.presentation.usecase.SetServerInfoUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ServersScreenViewModel @Inject constructor(
    private val setServerInfoUsecase: SetServerInfoUsecase,
    private val getAllApiTokenFromDatabaseUsecase: GetAllApiTokensFromDatabaseUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ServersScreenState>(ServersScreenState())
    val uiState: StateFlow<ServersScreenState> = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(3000L)
            loadAllApiTokensFromDatabase()
            getServerApi()
            countTotalIncome()
            countAmountOfServers()
            _uiState.update { it.copy(isLoading = false) }
        }

    }

    fun handleIntent(intent: ServersScreenIntent) {
        when (intent) {
            is ServersScreenIntent.UpdateServersScreen -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    delay(3000L)
                    loadAllApiTokensFromDatabase()
                    getServerApi()
                    countTotalIncome()
                    countAmountOfServers()
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    suspend fun getServerApi() {
        try {
            val serverInfo = withContext(Dispatchers.IO) {
                setServerInfoUsecase.getServerInfo(_uiState.value.apiTokenList)
            }
            Log.d("ServersScreen", "$serverInfo")
            _uiState.update {
                it.copy(
                    serverList = serverInfo
                )
            }
        } catch (e: Exception) {
            Log.d("ServersScreen", "${e.message}")
            _uiState.update {
                it.copy(
                    errors = mapOf("requestError" to "Не удалось загрузить данные сервера")
                )
            }
        }


    }


    suspend fun loadAllApiTokensFromDatabase() {
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

    private fun countTotalIncome() {
        _uiState.update { it ->
            val totalIncome = it.serverList.sumOf { it.totalIncome }
            it.copy(totalIncome = totalIncome)
        }
    }

    private fun countAmountOfServers() {
        _uiState.update { it ->
            val amountOfServers = it.serverList.count()
            it.copy(amountOfServers = amountOfServers)
        }
    }
}