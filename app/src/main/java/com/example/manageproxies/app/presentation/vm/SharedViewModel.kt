package com.example.manageproxies.app.presentation.vm

import androidx.lifecycle.ViewModel
import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.presentation.usecase.GetTokenUseCase
import com.example.manageproxies.app.presentation.usecase.SaveTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val saveTokenUseCase: SaveTokenUseCase,
    private val getTokenUseCase: GetTokenUseCase
) : ViewModel() {

    fun saveToken (token: Token) {
        saveTokenUseCase.execute(token)

    }

    fun getToken (): Token {
        return getTokenUseCase.execute()
    }
}