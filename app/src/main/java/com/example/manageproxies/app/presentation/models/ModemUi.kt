package com.example.manageproxies.app.presentation.models

import com.example.manageproxies.data.remote.Modem


data class ModemUi(
    val id: Int,
    val name: String,
    val operator: String,
    val order: String,
    val status: String,
)

fun Modem.toModemUi() = ModemUi(
    id = eid.toInt(),
    name = name,
    operator = operator,
    order = proxy_exp, // if there is no order it has null value
    status = status,
)