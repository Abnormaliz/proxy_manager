package com.example.manageproxies.app.presentation.models

import com.example.manageproxies.data.remote.Modem


data class ModemUi(
    var status: Boolean?,
    val id: Int,
    val name: String,
    val operator: String,
    val order: String?,
    val selfOrder: Boolean?,
    val testOrder: Boolean?
)

fun Modem.toModemUi() = ModemUi(
    status = null,
    id = eid.toInt(),
    name = name,
    operator = operator,
    order = if (proxy_exp != null) "Ordered" else null,
    selfOrder = proxy_self == "1",
    testOrder = proxy_testing == "1"
)