package com.example.manageproxies.data.remote

data class ServerInfo(
    val serverId: String,
    val serverDomain: String,
    val serverGeo: String,
    val serverStaticIp: String,
    val serverLocalIp: String,
    val serverComment: String,
    val serverErrorCount: String,
    val serverApproximateIncome: String,
)
