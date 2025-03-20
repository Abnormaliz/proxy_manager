package com.example.manageproxies.app.presentation.models

import com.example.manageproxies.data.remote.Server


data class ServerInfoUi(
    val token: String,
    val id: String,
    val domain: String,
    val geo: String,
    val modemList: List<ModemUi> = emptyList(),
    val totalIncome: Int? = 0,
    val dailyIncome: Int? = null,
    val allModems: Int? = null,
    val sellingModems: Int? = null,
    val activatedModems: Int? = null,
    val allOrders: Int? = null,
    val siteOrders: Int? = null,
    val selfOrders: Int? = null,
    val testOrders: Int? = null
)

//fun Server.toServerInfo() = ServerInfoUi(
//    id = server_id,
//    geo = server_geo,
//    domain = server_domain,
//    totalIncome = server_approximate_income.extractDigitsOnly().toInt(),
//)