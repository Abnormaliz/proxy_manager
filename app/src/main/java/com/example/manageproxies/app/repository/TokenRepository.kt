package com.example.manageproxies.app.repository

import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.models.DailyStatistic
import com.example.manageproxies.app.presentation.models.ServerInfoUi
import com.example.manageproxies.data.remote.Modem
import com.example.manageproxies.data.remote.ModemIp
import com.example.manageproxies.data.remote.Server

interface TokenRepository {

    fun getServerFromDatabase(serverId: Int): List<ServerInfoUi>

    suspend fun getServerFromApi(token: String): List<Server>

    suspend fun getModemsFromApi(token: String): List<Modem>

    suspend fun getModemIpFromApi(token: String, eid: String?): ModemIp

    suspend fun saveServerToDatabase(server: List<ServerInfoUi>): Boolean

    suspend fun saveDailyStatisticToDatabase(dailyStatistic: List<DailyStatistic>): Boolean

    suspend fun getDailyStatisticFromDatabase(currentDate: String): DailyStatistic?

    suspend fun saveApiTokenToDatabase(apiToken: ApiToken): Boolean

    suspend fun getAllApiTokensFromDatabase(): List<ApiToken>

    suspend fun getApiTokenByName(apiTokenName: String): ApiToken
}