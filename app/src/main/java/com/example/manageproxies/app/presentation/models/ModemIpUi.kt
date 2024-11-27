package com.example.manageproxies.app.presentation.models

import com.example.manageproxies.data.remote.ModemIp

data class ModemIpUi(
    val eid: Map<String, String>?,
)

fun ModemIp.toModemIpUi() = ModemIpUi(
    eid = eid
)