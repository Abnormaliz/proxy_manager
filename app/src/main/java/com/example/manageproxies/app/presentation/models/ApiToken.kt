package com.example.manageproxies.app.presentation.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "apiTokens_list",
    indices = [Index(value = [ApiToken.VALUE_FIELD], unique = true)]
)
data class ApiToken(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val value: String?
) {

    companion object {
        const val VALUE_FIELD = "value"
    }
}