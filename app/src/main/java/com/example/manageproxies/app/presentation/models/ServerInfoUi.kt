package com.example.manageproxies.app.presentation.models

import com.example.manageproxies.data.remote.Server

data class ServerInfoUi(
    val id: String,
    val geo: String,
    val totalIncome: Int,
    val dailyIncome: Int? = null,
    val allModems: Int? = null,
    val sellingModems: Int? = null,
    val activatedModems: Int? = null,
    val allOrders: Int? = null,
    val siteOrders: Int? = null,
    val selfOrders: Int? = null,
    val testOrders: Int? = null
)

fun Server.toServerInfo() = ServerInfoUi(
    id = server_id,
    geo = server_geo,
    totalIncome = server_approximate_income.extractDigitsOnly().toInt(),
)