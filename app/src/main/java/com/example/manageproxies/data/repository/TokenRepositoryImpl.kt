package com.example.manageproxies.data.repository

import android.content.Context
import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.remote.MyApi

private const val SHARED_PREFS_TOKEN = "shared_prefs_token"
private const val KEY_NAME = "Token"

class TokenRepositoryImpl(private val context: Context): TokenRepository {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_TOKEN, Context.MODE_PRIVATE)

    override fun saveToken(token: Token) : Boolean{
        sharedPreferences.edit().putString(KEY_NAME, token.value).apply()
        return true
    }

    override fun getToken(): Token {
        val token = sharedPreferences.getString(KEY_NAME, "1")
        return Token(token)
    }

    override fun getModemsFromApi(): MyApi {

    }

}