package com.example.manageproxies.data.repository

import android.content.Context
import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.remote.MyApi
import com.example.manageproxies.data.remote.ServerInfo

private const val SHARED_PREFS_TOKEN = "shared_prefs_token"
private const val KEY_NAME = "Token"

class TokenRepositoryImpl(private val context: Context, private val api: MyApi): TokenRepository {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_TOKEN, Context.MODE_PRIVATE)

    override fun saveToken(token: Token) : Boolean{
        sharedPreferences.edit().putString(KEY_NAME, token.value).apply()
        return true
    }

    override fun getToken(): Token {
        val token = sharedPreferences.getString(KEY_NAME, "1")
        return Token(token)
    }

    override suspend fun getModemsFromApi(): List<ServerInfo> {
        return api.getModems()
    }

}