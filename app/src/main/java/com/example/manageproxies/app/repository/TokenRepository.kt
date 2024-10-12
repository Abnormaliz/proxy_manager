package com.example.manageproxies.app.repository

import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.data.remote.MyApi
import com.example.manageproxies.data.remote.ServerInfo

interface TokenRepository {

    fun saveToken(token: Token) : Boolean

    fun getToken(): Token

    suspend fun getModemsFromApi(): List<ServerInfo>

}