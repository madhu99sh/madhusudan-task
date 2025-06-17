package com.sh.madhusudanshtask.presenter.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh.madhusudanshtask.ApiResult
import com.sh.madhusudanshtask.NoInternetException
import com.sh.madhusudanshtask.domain.model.Holding
import com.sh.madhusudanshtask.domain.model.HoldingSummary
import com.sh.madhusudanshtask.domain.usecase.CalculateHoldingsUseCase
import com.sh.madhusudanshtask.domain.usecase.GetHoldingsUseCase
import com.sh.madhusudanshtask.presenter.ui.HoldingUiState
import com.sh.madhusudanshtask.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoldingViewModel @Inject constructor(
    private val getHoldingsUseCase: GetHoldingsUseCase,
    private val calculateHoldingsUseCase: CalculateHoldingsUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val networkMonitor = NetworkMonitor(context)
    val isConnected = networkMonitor.isConnected

    private val _uiState = MutableStateFlow<HoldingUiState>(HoldingUiState.Loading)
    val uiState get() = _uiState.asStateFlow()

    private val _summary = MutableStateFlow<HoldingSummary?>(null)
    val summary get() = _summary.asStateFlow()


    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing get() = _isRefreshing.asStateFlow()

    init {
        refreshHoldings()
    }

    fun refreshHoldings() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val result = getHoldingsUseCase()
            handleResult(result)
            _isRefreshing.value = false
        }
    }

    private fun handleResult(result: ApiResult<List<Holding>>) {
        when (result) {
            is ApiResult.Success -> {
                if (result.data.isNotEmpty()) {
                    _uiState.value = HoldingUiState.Success(result.data)
                    _summary.value = calculateHoldingsUseCase.execute(result.data)
                } else {
                    _uiState.value = HoldingUiState.Error("No data found!")
                }
            }

            is ApiResult.Error -> {
                val msg =
                    if (result.exception is NoInternetException) "No internet connection. Showing cached data if available."
                    else result.exception.localizedMessage ?: "Something went wrong"
                _uiState.value = HoldingUiState.Error(msg)
            }
        }
    }

}

