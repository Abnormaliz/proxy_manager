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
        loadAllApiTokensFromDatabase()
    }

     fun getServerApi() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value
            try {
                val serverInfo = setServerInfoUsecase.getServerInfo(currentState.apiTokenList)
                Log.d("ServersScreen", "$serverInfo")
                _uiState.value = currentState.copy(
                    serverList = serverInfo
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("ServersScreen", "${e.message}")
                    _uiState.value = _uiState.value.copy(
                        errors = mapOf("requestError" to "Не удалось загрузить данные сервера")
                    )
                }
            }
        }
    }


    fun loadAllApiTokensFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiTokens = getAllApiTokenFromDatabaseUsecase.getAllApiTokens()
                _uiState.value = uiState.value.copy(
                    apiTokenList = apiTokens
                )
                Log.d("ServersScreen", "${_uiState.value.apiTokenList}")
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        errors = mapOf("requestError" to "Не удалось загрузить данные Api-токенов")
                    )
                }
            }
        }
    }
}