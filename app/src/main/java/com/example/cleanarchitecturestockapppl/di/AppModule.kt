package com.example.cleanarchitecturestockapppl.di

import android.app.Application
import androidx.room.Room
import com.example.cleanarchitecturestockapppl.data.local.StockDatabase
import com.example.cleanarchitecturestockapppl.data.remote.StockApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

// Modules are like containers in which we tell dagger-hilt which dependencies we have
// and how it can create those dependencies
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStockApi(): StockApi = Retrofit
        .Builder()
        .addConverterFactory(MoshiConverterFactory.create()) // Moshi is faster than Gson
        .baseUrl(StockApi.BASE_URL)
        .build()
        .create()

    @Provides
    @Singleton
    fun providesStockDatabase(app: Application): StockDatabase = Room.databaseBuilder(
        app,
        StockDatabase::class.java,
        StockDatabase.DATABASE_NAME,
    ).build()
}