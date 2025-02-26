package com.example.manageproxies.app.presentation.models

import com.example.manageproxies.data.remote.Modem


data class ModemUi(
    var status: Boolean?,
    val eid: Int,
    val name: String,
    val domain: String,
    val operator: String,
    val isSelling: Boolean = false,
    val isActivated: Boolean = false,
    val isOrdered: Boolean = false,
    val isOnSiteOrdered: Boolean = false,
    val isSelfOrdered: Boolean = false,
    val isTestOrdered: Boolean = false
)

fun Modem.toModemUi() = ModemUi(
    status = null,
    eid = eid.toInt(),
    name = name,
    domain = props,
    operator = operator,
    isSelling = canbuy == "1",
    isActivated = status == "1",
    isOrdered = proxy_exp != null,
    isOnSiteOrdered = proxy_exp != null && proxy_self == "0",
    isSelfOrdered = proxy_self == "1",
    isTestOrdered = proxy_testing == "1"
)