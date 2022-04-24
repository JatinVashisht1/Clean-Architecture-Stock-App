package com.example.cleanarchitecturestockapppl.data.mappers

import com.example.cleanarchitecturestockapppl.data.local.CompanyListingEntity
import com.example.cleanarchitecturestockapppl.data.remote.dto.CompanyInfoDto
import com.example.cleanarchitecturestockapppl.domain.model.CompanyInfoModel
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

fun CompanyInfoDto.toCompanyInfoModel(): CompanyInfoModel = CompanyInfoModel(

    // we are making null check here because when our quota exhausts
    // then we won't find these variables and in that case our app will crash
    // to prevent that we are telling our app that these videos will not be always available
    symbol = symbol?: "",
    description = description?: "",
    name = name?: "",
    country = country?: "",
    industry = industry?: "",
)