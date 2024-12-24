package com.example.manageproxies.app.presentation.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apiTokens_list")
data class ApiToken(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val value: String?
)