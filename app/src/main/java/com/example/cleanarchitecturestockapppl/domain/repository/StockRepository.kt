package com.example.cleanarchitecturestockapppl.domain.repository

import com.example.cleanarchitecturestockapppl.domain.model.CompanyInfoModel
import com.example.cleanarchitecturestockapppl.domain.model.CompanyListingModel
import com.example.cleanarchitecturestockapppl.domain.model.IntradayInfoModel
import com.example.cleanarchitecturestockapppl.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean, // we will make this boolean true to force the system to get the details from the api
        query: String // what we want to search for in the company listing
    ): Flow<Resource<List<CompanyListingModel>>> // we are returning flow because we also have local cache here

    suspend fun getIntradayInfo(
        symbol: String,
    ): Resource<List<IntradayInfoModel>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfoModel>
}