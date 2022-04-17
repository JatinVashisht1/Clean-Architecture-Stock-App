package com.example.cleanarchitecturestockapppl.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query



interface StockApi {
    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apikey") apiKey: String = API_KEY,
    ): ResponseBody // with retrofit we can get access to the byte stream through ResponseBody

    companion object{
        const val API_KEY = "K5RHFDSMWCU2GK8V"
        const val BASE_URL = "https://alphavantage.co"
    }
}