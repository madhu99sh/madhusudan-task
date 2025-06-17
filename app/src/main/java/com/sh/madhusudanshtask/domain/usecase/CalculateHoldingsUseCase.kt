package com.sh.madhusudanshtask.domain.usecase

import com.sh.madhusudanshtask.domain.model.Holding
import com.sh.madhusudanshtask.domain.model.HoldingSummary

class CalculateHoldingsUseCase {
    fun execute(holdings: List<Holding>): HoldingSummary {
        val currentValue = holdings.sumOf { it.ltp * it.quantity }
        val investment = holdings.sumOf { it.avgPrice * it.quantity }
        val totalPNL = currentValue - investment
        val todayPNL = holdings.sumOf { (it.close - it.ltp) * it.quantity }
        return HoldingSummary(currentValue, investment, totalPNL, todayPNL)
    }
}