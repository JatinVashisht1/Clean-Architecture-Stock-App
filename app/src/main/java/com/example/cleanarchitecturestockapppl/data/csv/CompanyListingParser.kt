package com.example.cleanarchitecturestockapppl.data.csv

import com.example.cleanarchitecturestockapppl.domain.model.CompanyListingModel
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

// by annotating a class by @Inject then we don't have to specify @Provides function in our Module
@Singleton
class CompanyListingParser @Inject constructor() : CSVParser<CompanyListingModel> {
    override suspend fun parse(stream: InputStream): List<CompanyListingModel> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val symbol = line.getOrNull(0)
                    val name = line.getOrNull(1)
                    val exchange = line.getOrNull(2)
                    CompanyListingModel(
                        name = name ?: return@mapNotNull null,
                        symbol = symbol ?: return@mapNotNull null,
                        exchange = exchange ?: return@mapNotNull null
                    )
                }
                .also {
                    csvReader.close()
                }
        }
    }
}