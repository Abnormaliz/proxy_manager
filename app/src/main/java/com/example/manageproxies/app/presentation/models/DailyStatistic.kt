package com.example.manageproxies.app.presentation.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.manageproxies.data.remote.Server

@Entity(tableName = "daily_statistic_list")
data class DailyStatistic(
    @PrimaryKey(autoGenerate = true) val primaryKey: Int = 0,
    val id: Int,
    val income: String,
    val date: String
)

fun Server.toDailyStatistic() = DailyStatistic(
    id = server_id.toInt(),
    income = server_approximate_income.extractDigitsOnly(),
    date = ""
)

