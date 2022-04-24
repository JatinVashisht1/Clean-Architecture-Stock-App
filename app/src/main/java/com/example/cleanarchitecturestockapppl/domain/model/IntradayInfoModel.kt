package com.example.cleanarchitecturestockapppl.domain.model

import java.time.LocalDateTime

data class IntradayInfoModel(
    val date: LocalDateTime,
    val close: Double,
)
