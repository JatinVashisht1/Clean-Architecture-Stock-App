package com.example.cleanarchitecturestockapppl.data.repository

import com.example.cleanarchitecturestockapppl.data.csv.CSVParser
import com.example.cleanarchitecturestockapppl.data.local.StockDatabase
import com.example.cleanarchitecturestockapppl.data.mappers.toCompanyListingEntity
import com.example.cleanarchitecturestockapppl.data.mappers.toCompanyListingModel
import com.example.cleanarchitecturestockapppl.data.remote.StockApi
import com.example.cleanarchitecturestockapppl.domain.model.CompanyListingModel
import com.example.cleanarchitecturestockapppl.domain.repository.StockRepository
import com.example.cleanarchitecturestockapppl.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

// this is the concrete implementation of our repository
// we are telling the app that we are using room for caching
// objective of repository is very specific so it belongs to data layer
@Singleton
class StockRepoImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    // dagger hilt is NOT smart enough to pass concrete implementation of the CSVParser interface we created
    // it is because we can have multiple different implementations of the same interface
    private val companyListingParser: CSVParser<CompanyListingModel>
) : StockRepository {
    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListingModel>>> = flow {

        emit(Resource.Loading<List<CompanyListingModel>>(isLoading = true))
        val localListings = db.dao.searchCompanyListing(query = query)
        emit(Resource.Success<List<CompanyListingModel>>(data = localListings.map { it.toCompanyListingModel() }))

        // if we don't get any request to load data from api and we have valid data in the cache then we don't have to make api call
        val isDbEmpty = localListings.isEmpty() && query.isBlank()
        val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

        // CLEAN CODE TIP
        // we could have just written the condition in shouldJustLoadFromCache condition directly in the if condition
        // but we didn't do that because if someone else will read our code then it would not be clear in the first place what our code is doing
        if (shouldJustLoadFromCache) {
            emit(Resource.Loading<List<CompanyListingModel>>(isLoading = false))
            return@flow
        }

        val remoteListings = try {
            val response = api.getListings()
            val responseByteStream = response.byteStream()
            companyListingParser.parse(responseByteStream)
        } catch (e: IOException) {
            e.printStackTrace()
            emit(
                Resource.Error<List<CompanyListingModel>>(
                    message = e.localizedMessage ?: "Couldn't load data"
                )
            )
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(
                Resource.Error<List<CompanyListingModel>>(
                    message = e.localizedMessage ?: "Couldn't load data"
                )
            )
            null
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                Resource.Error<List<CompanyListingModel>>(
                    message = e.localizedMessage ?: "Couldn't load data"
                )
            )
            null
        }
        remoteListings?.let { listings ->

            db.dao.clearCompanyListings()
            // here we are following Single Source of Truth principle
            // SST principle says that your app should depend on data from one place only (mostly database)
            // So our UI should not directly show data from the API
            // Instead we should load it from API store it in the database and load data from database again
            db.dao.insertCompanyListings(listings.map { it.toCompanyListingEntity() })

            emit(
                Resource.Success<List<CompanyListingModel>>(
                    data = db.dao.searchCompanyListing("")
                        .map { it.toCompanyListingModel() })
            )

            emit(Resource.Loading<List<CompanyListingModel>>(isLoading = false))
        }
    }
}