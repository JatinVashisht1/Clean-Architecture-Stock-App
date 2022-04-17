package com.example.cleanarchitecturestockapppl.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface StockDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertCompanyListings(
        companyListingEntities: List<CompanyListingEntity>,

    )

    @Query("DELETE FROM companylistingentity")
    suspend fun clearCompanyListings()

    @Query("""
        SELECT *
        FROM companylistingentity
        WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
        UPPER(:query) == symbol
    """)
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>
}