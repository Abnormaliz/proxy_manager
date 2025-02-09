package com.example.manageproxies.app.presentation.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "apiTokens_list",
    indices = [Index(value = [ApiToken.VALUE_FIELD], unique = true)]
)
data class ApiToken(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val value: String?,
    val id: String = UUID.randomUUID().toString()
) {

    companion object {
        const val VALUE_FIELD = "value"
    }
}