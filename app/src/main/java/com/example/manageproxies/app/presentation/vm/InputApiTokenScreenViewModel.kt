package com.example.manageproxies.app.presentation.vm

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.usecase.CheckApiTokenUsecase
import com.example.manageproxies.app.presentation.usecase.GetAllApiTokensFromDatabaseUsecase
import com.example.manageproxies.app.presentation.usecase.RemoveApiTokenFromDatabaseUsecase
import com.example.manageproxies.app.presentation.usecase.SaveApiTokenToDatabaseUsecase
import com.example.manageproxies.data.remote.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InputApiTokenScreenViewModel @Inject constructor(
    private val saveApiTokenToDatabaseUsecase: SaveApiTokenToDatabaseUsecase,
    private val checkApiTokenUsecase: CheckApiTokenUsecase,
    private val getAllApiTokensFromDatabaseUsecase: GetAllApiTokensFromDatabaseUsecase,
    private val removeApiTokenFromDatabaseUsecase: RemoveApiTokenFromDatabaseUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow<InputApiTokenScreenState>(InputApiTokenScreenState())
    val uiState: StateFlow<InputApiTokenScreenState> = _uiState.asStateFlow()

    init {
        loadAllApiTokensFromDatabase()
    }

    fun handleIntent(intent: InputApiTokenScreenIntent) {
        when (intent) {
            is InputApiTokenScreenIntent.NameChanged -> {
                val currentState = _uiState.value
                _uiState.value = currentState.copy(nameTextField = intent.newValue ?: "")
            }

            is InputApiTokenScreenIntent.TokenScreenChanged -> {
                val currentState = _uiState.value
                _uiState.value = currentState.copy(tokenTextField = intent.newValue ?: "")
            }

            is InputApiTokenScreenIntent.SaveApiTokenScreen -> {
                saveApiToken()
            }

            is InputApiTokenScreenIntent.RemoveApiTokenScreen -> {
                removeApiTokenFromDatabase(intent.apiToken)
            }
        }
    }

    fun saveApiToken() {

        val currentState = _uiState.value
        val name = currentState.nameTextField.trim()
        val token = currentState.tokenTextField.trim()

        val nameError = name.isBlank()
        val tokenError = token.isBlank()

        if (nameError || tokenError) {
            _uiState.value = currentState.copy(
                errors = mapOf("fieldIsBlank" to "Наименование сервера или Api-токен не могут быть пустыми")
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = checkApiTokenUsecase.checkApiToken(token)) {
                    is ApiResult.SuccessServer -> {
                        try {
                            saveApiTokenToDatabaseUsecase.saveApiToken(
                                ApiToken(
                                    name = name, value = token
                                )
                            )
                            val apiTokens = getAllApiTokensFromDatabaseUsecase.getAllApiTokens()
                            withContext(Dispatchers.Main) {
                                _uiState.value = currentState.copy(
                                    nameTextField = "",
                                    tokenTextField = "",
                                    isServerAdded = true,
                                    apiTokensList = apiTokens
                                )
                            }
                        } catch (e: SQLiteConstraintException) {
                            val errors = mutableMapOf<String, String>().apply {
                                if (e.message?.contains("name") == true) {
                                    this["name"] = "Наименование сервера уже существует"
                                }
                                if (e.message?.contains("value") == true) {
                                    this["token"] = "Api-токен уже существует"
                                }
                            }
                            withContext(Dispatchers.Main) {
                                _uiState.value = currentState.copy(
                                    errors = errors
                                )
                            }

                        }
                    }

                    is ApiResult.SuccessApiResponse -> {
                        withContext(Dispatchers.Main) {
                            _uiState.value =
                                currentState.copy(errors = mapOf("token" to "Api-токен не найден"))
                        }

                    }

                    is ApiResult.Error -> {
                        _uiState.value = currentState.copy(
                            errors = mapOf("requestError" to result.error)
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _uiState.value = currentState.copy(
                        errors = mapOf(
                            "requestError" to "непредвиденная ошибка: ${e.localizedMessage}"
                        )
                    )
                }

            }

        }
    }

    fun resetServerAddedFlag() {
        _uiState.value = _uiState.value.copy(isServerAdded = false)
    }

    private fun loadAllApiTokensFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiTokens = getAllApiTokensFromDatabaseUsecase.getAllApiTokens()
                _uiState.value = uiState.value.copy(
                    apiTokensList = apiTokens
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        errors = mapOf("requestError" to "Не удалось загрузить данные Api-токенов")
                    )
                }

            }
        }

    }

    private fun removeApiTokenFromDatabase(apiToken: ApiToken) {
        viewModelScope.launch(Dispatchers.IO) {
            removeApiTokenFromDatabaseUsecase.removeApiTokenFromDatabase(apiToken)
            val updatedList = getAllApiTokensFromDatabaseUsecase.getAllApiTokens()
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(apiTokensList = updatedList)
                Log.d("123", "${_uiState.value.apiTokensList}")
            }
        }

    }
}