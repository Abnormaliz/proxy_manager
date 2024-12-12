package com.example.manageproxies.data.repository

import android.content.Context
import com.example.manageproxies.app.presentation.models.DailyStatistic
import com.example.manageproxies.app.presentation.models.ServerUi
import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.repository.TokenRepository
import com.example.manageproxies.data.database.ServerDatabase
import com.example.manageproxies.data.remote.Modem
import com.example.manageproxies.data.remote.ModemIp
import com.example.manageproxies.data.remote.MyApi
import com.example.manageproxies.data.remote.Server
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val SHARED_PREFS_TOKEN = "shared_prefs_token"
private const val KEY_NAME = "Token"

class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: MyApi,
    private val db: ServerDatabase
) : TokenRepository {

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_TOKEN, Context.MODE_PRIVATE)

    override fun saveToken(token: Token): Boolean {
        sharedPreferences.edit().putString(KEY_NAME, token.value).apply()
        return true
    }

    override fun getToken(): Token {
        val token = sharedPreferences.getString(KEY_NAME, "1")
        return Token(token)
    }

    override suspend fun getServerFromApi(token: String): List<Server> {
        return api.getServerInfo(token)
    }

    override suspend fun getModemsFromApi(token: String): List<Modem> {
        return api.getModems(token)
    }

    override suspend fun getModemIpFromApi(token: String, eid: String?): ModemIp {
        return api.getModemIp(token, eid)
    }

    override suspend fun saveServerToDatabase(server: List<ServerUi>): Boolean {
        db.serverDao().addServer(server)
        return true
    }

    override fun getServerFromDatabase(serverId: Int) {
        db.serverDao().getServerById(serverId)
    }

    override suspend fun saveDailyStatisticToDatabase(dailyStatistic: List<DailyStatistic>): Boolean {
        db.serverDao().saveDailyStatistic(dailyStatistic)
        return true
    }

    override suspend fun getDailyStatisticFromDatabase(currentDate: String): DailyStatistic? {
        return db.serverDao().getDailyStatisticByDate(currentDate)
    }
}