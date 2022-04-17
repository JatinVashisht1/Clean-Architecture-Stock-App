package com.example.cleanarchitecturestockapppl.presentation.company_listings

import com.example.cleanarchitecturestockapppl.domain.model.CompanyListingModel

// this class will contain everything relevant for the UI State
data class CompanyListingState(
    val companies: List<CompanyListingModel> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",

)
