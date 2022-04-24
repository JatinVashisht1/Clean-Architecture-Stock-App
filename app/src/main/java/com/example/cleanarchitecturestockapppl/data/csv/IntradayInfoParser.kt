package com.example.cleanarchitecturestockapppl.data.csv

import com.example.cleanarchitecturestockapppl.data.mappers.toIntradayInfoModel
import com.example.cleanarchitecturestockapppl.data.remote.dto.IntradayInfoDto
import com.example.cleanarchitecturestockapppl.domain.model.CompanyListingModel
import com.example.cleanarchitecturestockapppl.domain.model.IntradayInfoModel
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

// by annotating a class by @Inject then we don't have to specify @Provides function in our Module
@Singleton
class IntradayInfoParser @Inject constructor() : CSVParser<IntradayInfoModel> {
    override suspend fun parse(stream: InputStream): List<IntradayInfoModel> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                    val close = line.getOrNull(4) ?: return@mapNotNull null
                    val dto = IntradayInfoDto(timestamp = timestamp, close.toDouble())
                    dto.toIntradayInfoModel()
                }
                .filter {
                    // only these entries where the date of intraday info is equal to yesterday basically
                    // we take current day and subtract one day to get yesterday and kept day of month
                    it.date.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth
                }
                .sortedBy {
                    it.date.hour
                }
                .also {
                    csvReader.close()
                }
        }
    }
}