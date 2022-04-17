package com.example.cleanarchitecturestockapppl.presentation.company_listings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitecturestockapppl.domain.repository.StockRepository
import com.example.cleanarchitecturestockapppl.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class CompanyListingsViewModel @Inject constructor(
    val repository: StockRepository
) : ViewModel() {
    var state by mutableStateOf<CompanyListingState>(CompanyListingState())

    init {
        getCompanyListings()
    }

    // we will keep track of coroutine job
    // whenever we type something new we will add a little bit of delay
    // until we actually start the search
    // otherwise we would be searching all the time everytime we type something
    private var searchJob: Job? = null
    fun onEvent(event: CompanyListingsEvent) {
        when (event) {
            is CompanyListingsEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListings()
                }
            }
            CompanyListingsEvent.Refresh -> {
                getCompanyListings(fetchedFromRemote = true)
            }
        }
    }

    private fun getCompanyListings(
        query: String = state.searchQuery.lowercase(),
        fetchedFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository
                .getCompanyListings(fetchFromRemote = fetchedFromRemote, query = query)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            // TODO: Do by yourself
                        }
                        is Resource.Success -> {
                            result.data?.let { listings ->
                                state = state.copy(
                                    companies = listings
                                )
                                Log.d("viewmodeltag", "listings are $listings")
                            }
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }

        }
    }
}