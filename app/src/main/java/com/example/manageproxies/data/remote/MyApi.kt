package com.example.manageproxies.data.remote

import retrofit2.http.GET
import retrofit2.http.Headers


interface MyApi {

    @Headers("Authorization: Bearer 4f45205a62f0410e120830278fc60ed6")
    @GET("api.html?command=load_servers")
    suspend fun getModems() : List<ServerInfo>

}