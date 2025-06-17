package com.sh.madhusudanshtask.presenter.ui

import com.sh.madhusudanshtask.domain.model.Holding

sealed class HoldingUiState {
//    data object Idle : HoldingUiState()
    data object Loading : HoldingUiState()
    data class Success(val holdings: List<Holding>) : HoldingUiState()
    data class Error(val message: String) : HoldingUiState()
}