package com.example.cleanarchitecturestockapppl.presentation.company_info

import com.example.cleanarchitecturestockapppl.domain.model.CompanyInfoModel
import com.example.cleanarchitecturestockapppl.domain.model.IntradayInfoModel

data class CompanyInfoState(
    val stockInfo: List<IntradayInfoModel> = emptyList(),
    val company: CompanyInfoModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
