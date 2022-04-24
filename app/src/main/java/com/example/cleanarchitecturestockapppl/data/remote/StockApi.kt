package com.example.cleanarchitecturestockapppl.data.remote

import com.example.cleanarchitecturestockapppl.data.remote.dto.CompanyInfoDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query



interface StockApi {
    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apikey") apiKey: String = API_KEY,
    ): ResponseBody // with retrofit we can get access to the byte stream through ResponseBody because it is sending back csv file


    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): CompanyInfoDto // this will not return ResponseBody because we directly get it as JSON and Moshi will directly parse it to data class

    companion object{
        const val API_KEY = "K5RHFDSMWCU2GK8V"
        const val BASE_URL = "https://alphavantage.co"
    }
}