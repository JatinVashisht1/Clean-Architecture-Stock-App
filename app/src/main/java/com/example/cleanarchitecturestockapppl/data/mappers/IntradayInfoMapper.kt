package com.example.cleanarchitecturestockapppl.data.mappers

import com.example.cleanarchitecturestockapppl.data.remote.dto.IntradayInfoDto
import com.example.cleanarchitecturestockapppl.domain.model.IntradayInfoModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun IntradayInfoDto.toIntradayInfoModel(): IntradayInfoModel {
    val dateTimePattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(dateTimePattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timestamp, formatter)
    return IntradayInfoModel(
        date = localDateTime,
        close = close
    )
}
