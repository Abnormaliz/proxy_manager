package com.example.manageproxies.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.models.DailyStatistic
import com.example.manageproxies.app.presentation.models.ServerUi


@Database(
    entities = [
        ServerUi::class,
        DailyStatistic::class,
        ApiToken::class],
    version = 1
)

abstract class ServerDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerDao
}