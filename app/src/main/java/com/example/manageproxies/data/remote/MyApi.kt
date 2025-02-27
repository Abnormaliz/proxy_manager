package com.example.manageproxies.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface MyApi {

    @GET("api.html?command=load_servers")
    suspend fun getServerInfo(@Header("Authorization") token: String) : List<Server>

    @GET("api.html?command=load_servers")
    suspend fun checkApiToken(@Header("Authorization") token: String) : Response<ResponseBody>

    @GET("api.html?command=load_modems")
    suspend fun getModems(@Header("Authorization") token: String) : List<Modem>

    @GET("api.html?command=modem_ip")
    suspend fun getModemIp(
        @Header("Authorization") token: String,
        @Query("eid") eid: String?
    ) : ModemIp
}