package com.example.manageproxies.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.models.DailyStatistic
import com.example.manageproxies.app.presentation.models.ServerInfoUi

@Dao
interface ServerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addServer(server: List<ServerInfoUi>)

    @Query("SELECT * FROM server_list WHERE id = :serverId")
    fun getServerById(serverId: Int): ServerInfoUi

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveDailyStatistic(dailyStatistic: List<DailyStatistic>)

    @Query("SELECT * FROM daily_statistic_list WHERE date = :currentDate LIMIT 1")
    suspend fun getDailyStatisticByDate(currentDate: String): DailyStatistic?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveApiToken(apiToken: ApiToken)

    @Query("SELECT * FROM apitokens_list")
    fun getAllApiTokens(): List<ApiToken>

    @Query("SELECT * FROM apiTokens_list WHERE name = :apiTokenName LIMIT 1")
    suspend fun getApiTokenByName(apiTokenName: String): ApiToken
}