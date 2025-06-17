package com.sh.madhusudanshtask.domain.model

data class HoldingSummary(
    val currentValue: Double,
    val totalInvestment: Double,
    val totalPNL: Double,
    val todayPNL: Double
) {

}