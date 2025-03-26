package com.example.manageproxies.app.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageproxies.app.presentation.usecase.GetAllApiTokensFromDatabaseUsecase
import com.example.manageproxies.app.presentation.usecase.SetServerInfoUsecase
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
class ServersScreenViewModel @Inject constructor(
    private val setServerInfoUsecase: SetServerInfoUsecase,
    private val getAllApiTokenFromDatabaseUsecase: GetAllApiTokensFromDatabaseUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ServersScreenState>(ServersScreenState())
    val uiState: StateFlow<ServersScreenState> = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
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
                    loadAllApiTokensFromDatabase()
                    getServerApi()
                    countTotalIncome()
                    countAmountOfServers()
                    _uiState.update { it.copy(isLoading = false) }
                    Log.d("ServersScreenViewModel", "${_uiState.value.errors}")
                }
            }
        }
    }

    suspend fun getServerApi() {
        try {
            val serverInfo = withContext(Dispatchers.IO) {
                setServerInfoUsecase.getServerInfo(_uiState.value.apiTokenList)
            }
            _uiState.update {
                it.copy(
                    serverList = serverInfo,
                    errors = null
                )
            }
        } catch (e: Exception) {
            val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val errorMessage = "[$timestamp] ${e.message}"
            _uiState.update { it.copy(errors = mapOf("requestError" to errorMessage)) }
        }


    }


    suspend fun loadAllApiTokensFromDatabase() {
        try {
            val apiTokens = withContext(Dispatchers.IO) {
                getAllApiTokenFromDatabaseUsecase.getAllApiTokens()
            }
            _uiState.update {
                it.copy(
                    apiTokenList = apiTokens,
                    errors = null
                )
            }
            Log.d("ServersScreen", "${_uiState.value.apiTokenList}")
        } catch (e: Exception) {
            val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val errorMessage = "[$timestamp] ${e.message}"
            _uiState.update { it.copy(errors = mapOf("requestError" to errorMessage)) }
        }
    }

    private fun countTotalIncome() {
        _uiState.update { it ->
            val totalIncome = it.serverList.sumOf { it.totalIncome ?: 0 }
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