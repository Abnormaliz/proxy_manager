package com.example.manageproxies.app.presentation.vm

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.models.InputApiTokenIntent
import com.example.manageproxies.app.presentation.models.InputApiTokenState
import com.example.manageproxies.app.presentation.usecase.CheckApiTokenUsecase
import com.example.manageproxies.app.presentation.usecase.SaveApiTokenToDatabaseUsecase
import com.example.manageproxies.data.remote.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputApiTokenScreenViewModel @Inject constructor(
    private val saveApiTokenToDatabaseUsecase: SaveApiTokenToDatabaseUsecase,
    private val checkApiTokenUsecase: CheckApiTokenUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow<InputApiTokenState>(InputApiTokenState.Input())
    val uiState: StateFlow<InputApiTokenState> = _uiState.asStateFlow()


    fun handleIntent(intent: InputApiTokenIntent) {
        when (intent) {
            is InputApiTokenIntent.NameChanged -> {
                val currentState = (_uiState.value as? InputApiTokenState.Input) ?: return
                _uiState.value =
                    currentState.copy(name = intent.newValue, errors = currentState.errors - "name")
            }

            is InputApiTokenIntent.TokenChanged -> {
                val currentState = (_uiState.value as? InputApiTokenState.Input) ?: return
                _uiState.value = currentState.copy(
                    token = intent.newValue, errors = currentState.errors - "token"
                )
            }

            is InputApiTokenIntent.SaveApiToken -> {
                saveApiToken()
            }

            is InputApiTokenIntent.MessageShown -> {
                val currentState = (_uiState.value as? InputApiTokenState.Input) ?: return
                _uiState.value = currentState.copy(toastMessage = null)
            }
        }
    }

    fun saveApiToken() {

        val currentState = (_uiState.value as? InputApiTokenState.Input) ?: return
        val name = currentState.name
        val token = currentState.token

        if (name.isBlank() || token.isBlank()) {
            val errors = mutableMapOf<String, String>()
            if (name.isBlank()) errors["name"] = "Имя не может быть пустым"
            if (token.isBlank()) errors["token"] = "Токен не может быть пустым"
            _uiState.value = currentState.copy(errors = errors)
            return
        }

        viewModelScope.launch {
            try {
                when (val result = checkApiTokenUsecase.checkApiToken(token)) {
                    is ApiResult.SuccessServer -> {
                        try {
                            saveApiTokenToDatabaseUsecase.saveApiToken(
                                ApiToken(
                                    name = name, value = token
                                )
                            )
                            _uiState.value = InputApiTokenState.Success("Сервер успешно добавлен")
                        } catch (e: SQLiteConstraintException) {
                            val errors = mutableMapOf<String, String>()
                            if (e.message?.contains("name") == true) {
                                errors["name"] = "Наименование сервера уже существует"
                            }
                            if (e.message?.contains("value") == true) {
                                errors["token"] = "Api-токен уже существует"
                            }
                            _uiState.value = currentState.copy(errors = errors)
                        }
                    }

                    is ApiResult.SuccessApiResponse -> {
                        _uiState.value =
                            currentState.copy(errors = mapOf("token" to "Api-токен не найден"))
                    }

                    is ApiResult.Error -> {
                        _uiState.value = InputApiTokenState.Error(mapOf("general" to result.error))
                    }
                }
            } catch (e: Exception) {
                _uiState.value = InputApiTokenState.Error(
                    mapOf(
                        "general" to "непредвиденная ошибка"
                    )
                )
            }

        }
    }
}