package com.example.manageproxies.data.repository

import android.content.Context
import com.example.manageproxies.app.presentation.models.DailyStatistic
import com.example.manageproxies.app.presentation.models.ServerInfoUi
import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.database.ServerDatabase
import com.example.manageproxies.data.remote.Modem
import com.example.manageproxies.data.remote.ModemIp
import com.example.manageproxies.data.remote.MyApi
import com.example.manageproxies.data.remote.Server
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: MyApi,
    private val db: ServerDatabase
) : TokenRepository {

    override suspend fun getServerFromApi(token: String): List<Server> {
        return api.getServerInfo(token)
    }

    override suspend fun getModemsFromApi(token: String): List<Modem> {
        return api.getModems(token)
    }

    override suspend fun getModemIpFromApi(token: String, eid: String?): ModemIp {
        return api.getModemIp(token, eid)
    }

    override suspend fun saveServerToDatabase(server: List<ServerInfoUi>): Boolean {
        db.serverDao().addServer(server)
        return true
    }

    override fun getServerFromDatabase(serverId: Int): ServerInfoUi {
        return db.serverDao().getServerById(serverId)
    }

    override suspend fun saveDailyStatisticToDatabase(dailyStatistic: List<DailyStatistic>): Boolean {
        db.serverDao().saveDailyStatistic(dailyStatistic)
        return true
    }

    override suspend fun getDailyStatisticFromDatabase(currentDate: String): DailyStatistic? {
        return db.serverDao().getDailyStatisticByDate(currentDate)
    }

    override suspend fun saveApiTokenToDatabase(apiToken: ApiToken): Boolean {
        db.serverDao().saveApiToken(apiToken)
        return true
    }

    override suspend fun getAllApiTokensFromDatabase(): List<ApiToken> {
        return db.serverDao().getAllApiTokens()
    }

    override suspend fun getApiTokenByName(apiTokenName: String): ApiToken {
        return db.serverDao().getApiTokenByName(apiTokenName)
    }
}