package com.example.manageproxies.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.manageproxies.app.presentation.models.ServerUi

@Dao
interface ServerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addServer(server: List<ServerUi>)
}