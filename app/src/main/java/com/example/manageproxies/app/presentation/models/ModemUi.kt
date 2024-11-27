package com.example.manageproxies.app.presentation.models

import com.example.manageproxies.data.remote.Modem


data class ModemUi(
    var status: Boolean?,
    val eid: Int,
    val name: String,
    val operator: String,
    val order: String?,
    val selfOrder: Boolean?,
    val testOrder: Boolean?
)

fun Modem.toModemUi() = ModemUi(
    status = null,
    eid = eid.toInt(),
    name = name,
    operator = operator,
    order = if (proxy_exp != null && proxy_self != "1") "Ordered" else null,
    selfOrder = proxy_self == "1",
    testOrder = proxy_testing == "1"
)