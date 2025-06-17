package com.sh.madhusudanshtask.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Holding(
    val symbol: String, val quantity: Int, val ltp: Double, val avgPrice: Double, val close: Double
) {
    fun findTodayPnL(): Double {
        return (close - ltp) * quantity
    }
}