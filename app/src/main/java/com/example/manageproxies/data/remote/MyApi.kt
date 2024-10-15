package com.example.manageproxies.data.remote

import retrofit2.http.GET
import retrofit2.http.Header


interface MyApi {

    @GET("api.html?command=load_servers")
    suspend fun getServerInfo(@Header("Authorization") token: String) : List<Server>

    @GET("api.html?command=load_modems")
    suspend fun getModems(@Header("Authorization") token: String) : List<Modem>
}