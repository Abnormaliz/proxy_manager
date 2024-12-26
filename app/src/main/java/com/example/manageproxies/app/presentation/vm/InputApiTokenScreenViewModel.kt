package com.example.manageproxies.app.presentation.vm

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.models.UiState
import com.example.manageproxies.app.presentation.usecase.CheckApiTokenUsecase
import com.example.manageproxies.app.presentation.usecase.SaveApiTokenToDatabaseUsecase
import com.example.manageproxies.data.remote.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputApiTokenScreenViewModel @Inject constructor(
    private val saveApiTokenToDatabaseUsecase: SaveApiTokenToDatabaseUsecase,
    private val checkApiTokenUsecase: CheckApiTokenUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _apiTokenName = MutableStateFlow("")
    val apiTokenName: StateFlow<String> = _apiTokenName.asStateFlow()

    private val _apiTokenValue = MutableStateFlow("")
    val apiTokenValue: StateFlow<String> = _apiTokenValue.asStateFlow()

    private val apiTokenFlow: Flow<ApiToken> = combine(apiTokenName, apiTokenValue) { name, value ->
        ApiToken(name = name, value = value)
    }

    fun saveApiTokenToDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            apiTokenFlow.collectLatest { data -> saveApiTokenToDatabaseUsecase.saveApiToken(data) }
        }
    }

    fun onNameChanged(newValue: String) {
        _apiTokenName.value = newValue
    }

    fun onValueChanged(newValue: String) {
        _apiTokenValue.value = newValue
    }

    fun checkAndSaveApiToken(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val apiTokenName = _apiTokenName.value
            val apiTokenValue = _apiTokenValue.value

            if (apiTokenName.isBlank() || apiTokenValue.isBlank()) {
                onError("Наименование сервера или api-токен не могут быть пустыми")
                return@launch
            }

            try {
                when (val result = checkApiTokenUsecase.checkApiToken(apiTokenValue)) {
                    is ApiResult.SuccessServer -> {
                        try {
                            saveApiTokenToDatabaseUsecase.saveApiToken(
                                ApiToken(
                                    name = apiTokenName,
                                    value = apiTokenValue
                                )
                            )
                            onSuccess("Сервер успешно добавлен")
                        } catch (e: SQLiteConstraintException) {
                            if (e.message?.contains("name") == true) {
                                onError("Наименование сервера уже существует")
                            } else if (e.message?.contains("value") == true) {
                                onError("Api-токен уже существует")
                            }
                        }

                    }

                    is ApiResult.SuccessApiResponse -> {
                        onError("Токен не найден")
                    }

                    is ApiResult.Error -> {
                        onError(result.error)
                    }
                }
            } catch (e: Exception) {
                onError("Unexpected error: ${e.message}")
            }
        }
    }
}