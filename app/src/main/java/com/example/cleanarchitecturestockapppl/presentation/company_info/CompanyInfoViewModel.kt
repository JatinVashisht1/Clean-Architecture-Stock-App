package com.example.cleanarchitecturestockapppl.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitecturestockapppl.domain.repository.StockRepository
import com.example.cleanarchitecturestockapppl.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
) : ViewModel() {
    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)

            // we are putting below two variables in async blocks because they can be executed simultaneously
            // and there is not need to execute them simultaneously
            val companyInfoResult = async { repository.getCompanyInfo(symbol = symbol) }
            val intradayInfoResult = async { repository.getIntradayInfo(symbol) }

            // with await we are waiting till we get result
            when (val result = companyInfoResult.await()) {
                is Resource.Error -> {
                    state = state.copy(isLoading = false, error = result.message, company = null)
                }
                is Resource.Success -> {
                    state = state.copy(company = result.data, isLoading = false, error = null)
                }
                else -> Unit // because we don't have any loading values in the called function
            }
            when (val result = intradayInfoResult.await()) {
                is Resource.Error -> {
                    state = state.copy(isLoading = false, error = result.message, company = null)
                }
                is Resource.Success -> {
                    state = state.copy(stockInfo = result.data ?: emptyList(), isLoading = false, error = null)
                }
                else -> Unit // because we don't have any loading values in the called function
            }
        }

    }
}