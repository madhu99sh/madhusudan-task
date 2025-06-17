package com.sh.madhusudanshtask

import com.sh.madhusudanshtask.domain.model.Holding
import com.sh.madhusudanshtask.domain.usecase.CalculateHoldingsUseCase
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CalculateHoldingsUseCaseTest {

    private val useCase = CalculateHoldingsUseCase()

    @Test
    fun `execute calculates correct summary`() {
        val holdings = listOf(
            Holding(symbol = "TCS", quantity = 2, ltp = 100.0, avgPrice = 90.0, close = 98.0),
            Holding(symbol = "INFY", quantity = 1, ltp = 200.0, avgPrice = 180.0, close = 190.0)
        )

        val result = useCase.execute(holdings)

        assertEquals(400.0, result.currentValue, 0.1)
        assertEquals(360.0, result.totalInvestment, 0.1)
        assertEquals(40.0, result.totalPNL, 0.1)
        assertEquals(-14.0, result.todayPNL, 0.1)
    }
}
