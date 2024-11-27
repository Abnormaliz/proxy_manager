package com.example.manageproxies.app.repository

import com.example.manageproxies.app.presentation.models.ServerUi
import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.data.remote.Modem
import com.example.manageproxies.data.remote.ModemIp
import com.example.manageproxies.data.remote.Server

interface TokenRepository {

    fun saveToken(token: Token) : Boolean

    fun getToken(): Token

    suspend fun getServerFromApi(token: String): List<Server>

    suspend fun getModemsFromApi(token: String): List<Modem>

    suspend fun getModemIpFromApi(token: String, eid: String?): ModemIp

    suspend fun saveServerToDatabase(server: List<ServerUi>): Boolean
}