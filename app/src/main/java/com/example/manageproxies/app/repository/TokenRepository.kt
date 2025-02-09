package com.example.manageproxies.app.repository

import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.models.DailyStatistic
import com.example.manageproxies.app.presentation.models.ServerInfoUi
import com.example.manageproxies.app.presentation.models.ServerUi
import com.example.manageproxies.data.remote.Modem
import com.example.manageproxies.data.remote.ModemIp
import com.example.manageproxies.data.remote.Server
import okhttp3.ResponseBody
import retrofit2.Response

interface TokenRepository {

    fun getServerFromDatabase(serverId: Int): ServerUi

    suspend fun getServerFromApi(token: String): List<Server>

    suspend fun checkApiToken(token: String): Response<ResponseBody>

    suspend fun getModemsFromApi(token: String): List<Modem>

    suspend fun getModemIpFromApi(token: String, eid: String?): ModemIp

    suspend fun saveServerToDatabase(server: List<ServerUi>): Boolean

    suspend fun saveDailyStatisticToDatabase(dailyStatistic: List<DailyStatistic>): Boolean

    suspend fun getDailyStatisticFromDatabase(currentDate: String): DailyStatistic?

    suspend fun saveApiTokenToDatabase(apiToken: ApiToken): Boolean

    suspend fun getAllApiTokensFromDatabase(): List<ApiToken>

    suspend fun getApiTokenByName(apiTokenName: String): ApiToken

    suspend fun removeApiTokenByName(apiToken: ApiToken)
}