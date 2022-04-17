package com.example.cleanarchitecturestockapppl.data.mappers

import com.example.cleanarchitecturestockapppl.data.local.CompanyListingEntity
import com.example.cleanarchitecturestockapppl.domain.model.CompanyListingModel

fun CompanyListingEntity.toCompanyListingModel(): CompanyListingModel = CompanyListingModel(
    name = name,
    symbol = symbol,
    exchange = exchange
)

fun CompanyListingModel.toCompanyListingEntity(): CompanyListingEntity = CompanyListingEntity(
    name = name,
    symbol = symbol,
    exchange = exchange
)