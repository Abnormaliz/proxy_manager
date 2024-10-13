package com.example.manageproxies.data.remote

data class ServerInfo(
    val server_id: String,
    val server_domain: String,
    val server_geo: String,
    val server_static_ip: String,
    val server_local_ip: String,
    val server_comment: String,
    val server_error_count: String,
    val server_approximate_income: String,
)
