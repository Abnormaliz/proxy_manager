package com.example.manageproxies.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.manageproxies.app.presentation.models.ServerUi


@Database(
    entities = [ServerUi::class],
    version = 1
)

abstract class ServerDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerDao
}