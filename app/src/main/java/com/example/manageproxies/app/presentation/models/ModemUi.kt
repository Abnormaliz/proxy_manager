package com.example.manageproxies.app.presentation.models

import com.example.manageproxies.data.remote.Modem


data class ModemUi(
    val id: Int,
    val name: String,
    val operator: String,
    val order: String?
)

fun Modem.toModemUi() = ModemUi(
    id = eid.toInt(),
    name = name,
    operator = operator,
    order = if (proxy_exp != null) "Ordered" else null
)