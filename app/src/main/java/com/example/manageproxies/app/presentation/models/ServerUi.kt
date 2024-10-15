package com.example.manageproxies.app.presentation.models

import com.example.manageproxies.data.remote.Server

data class ServerUi(
    val id: Int,
    val geo: String,
    val approximateIncome: String,
)


fun Server.toServerUi() = ServerUi(
    id = server_id.toInt(),
    geo = server_geo,
    approximateIncome = server_approximate_income.extractDigitsOnly(),
)

fun String.extractDigitsOnly(): String {
    return this.filter { it.isDigit() || it.isWhitespace() }
}