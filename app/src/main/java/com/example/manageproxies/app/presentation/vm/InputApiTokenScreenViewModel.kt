package com.example.manageproxies.app.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.usecase.GetApiTokenFromDatabaseUsecase
import com.example.manageproxies.app.presentation.usecase.SaveApiTokenToDatabaseUsecase
import com.example.manageproxies.app.presentation.usecase.SetServerInfoUsecase
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
    private val setServerInfoUsecase: SetServerInfoUsecase
) : ViewModel() {

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
}