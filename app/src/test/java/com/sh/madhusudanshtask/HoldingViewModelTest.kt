package com.sh.madhusudanshtask

import com.sh.madhusudanshtask.domain.model.Holding
import com.sh.madhusudanshtask.domain.model.HoldingSummary
import com.sh.madhusudanshtask.domain.usecase.CalculateHoldingsUseCase
import com.sh.madhusudanshtask.domain.usecase.GetHoldingsUseCase
import com.sh.madhusudanshtask.presenter.ui.HoldingUiState
import com.sh.madhusudanshtask.presenter.viewmodel.HoldingViewModel
import com.sh.madhusudanshtask.utils.FakeNetworkMonitor
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
class HoldingViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: HoldingViewModel

    private val mockUseCase = mock<GetHoldingsUseCase>()
    private val mockCalcUseCase = mock<CalculateHoldingsUseCase>()
    private val fakeNetworkMonitor = FakeNetworkMonitor(false)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HoldingViewModel(mockUseCase, mockCalcUseCase, fakeNetworkMonitor)
    }

    @Test
    fun `refreshHoldings emits success state`() = runTest {
        val mockHoldings = listOf(
            Holding(symbol = "TCS", quantity = 2, ltp = 100.0, avgPrice = 90.0, close = 98.0),
            Holding(symbol = "INFY", quantity = 1, ltp = 200.0, avgPrice = 180.0, close = 190.0)
        )
        whenever(mockUseCase()).thenReturn(ApiResult.Success(mockHoldings))
        whenever(mockCalcUseCase.execute(mockHoldings)).thenReturn(
            HoldingSummary(
                currentValue = (3600.0 * 10) + (1500.0 * 5),
                totalInvestment = (3500.0 * 10) + (1400.0 * 5),
                totalPNL = 43500.0 - 42000.0,
                todayPNL = ((3550.0 - 3600.0) * 10) + ((1480.0 - 1500.0) * 5)
            )
        )

        viewModel.refreshHoldings()

        assert(viewModel.uiState.value is HoldingUiState.Success)
        assertNotNull(viewModel.summary.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
