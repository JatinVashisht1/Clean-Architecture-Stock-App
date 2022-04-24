package com.example.cleanarchitecturestockapppl.di

import com.example.cleanarchitecturestockapppl.data.csv.CSVParser
import com.example.cleanarchitecturestockapppl.data.csv.CompanyListingParser
import com.example.cleanarchitecturestockapppl.data.csv.IntradayInfoParser
import com.example.cleanarchitecturestockapppl.data.repository.StockRepoImpl
import com.example.cleanarchitecturestockapppl.domain.model.CompanyListingModel
import com.example.cleanarchitecturestockapppl.domain.model.IntradayInfoModel
import com.example.cleanarchitecturestockapppl.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // we will declare abstract function here

    @Binds
    // use @Binds when we have to provide abstraction of something
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListingParser: CompanyListingParser
    ): CSVParser<CompanyListingModel>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepoImpl: StockRepoImpl
    ): StockRepository

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ): CSVParser<IntradayInfoModel>
}