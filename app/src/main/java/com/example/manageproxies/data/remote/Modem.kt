package com.example.manageproxies.data.remote

data class Modem(
    val eid: String,
    val name: String,
    val local_ip: String,
    val proxy_exp: String,
    val operator: String,
    val number: String,
    val props: String,
    val status: String,
    val canbuy: String,
    val comment: String,
    val signal: String,
    val modem: String,
    val local_server_ip: String,
    val admin_ip: String,
    val check_err_count: String
)
