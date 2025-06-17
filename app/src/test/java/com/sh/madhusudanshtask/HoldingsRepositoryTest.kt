package com.sh.madhusudanshtask

import com.sh.madhusudanshtask.data.local.HoldingDao
import com.sh.madhusudanshtask.data.local.model.HoldingEntity
import com.sh.madhusudanshtask.data.local.toEntity
import com.sh.madhusudanshtask.data.local.toModel
import com.sh.madhusudanshtask.data.remote.model.ApiResponse
import com.sh.madhusudanshtask.data.remote.model.HoldingDto
import com.sh.madhusudanshtask.data.remote.model.UserHoldingResponse
import com.sh.madhusudanshtask.data.remote.service.HoldingsApiService
import com.sh.madhusudanshtask.data.repository.HoldingsRepositoryImpl
import com.sh.madhusudanshtask.domain.repository.HoldingsRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class HoldingsRepositoryTest {

    private val mockApi = mock<HoldingsApiService>()
    private val mockDao = mock<HoldingDao>()
    private lateinit var repository: HoldingsRepository

    @Before
    fun setup() {
        repository = HoldingsRepositoryImpl(mockApi, mockDao)
    }

    @Test
    fun `fetchHoldings returns API data and saves to DB`() = runTest {
        val holdingsResponse = listOf(
            HoldingDto("TCS", 10, 3600.0, 3500.0, 3550.0),
            HoldingDto("INFY", 5, 1500.0, 1400.0, 1480.0)
        )
        val holdingEntities = holdingsResponse.map { it.toEntity() }
        val holdingModels = holdingsResponse.map { it.toModel() }

        whenever(mockApi.fetchHoldings()).thenReturn(
            ApiResponse(
                UserHoldingResponse(
                    holdingsResponse
                )
            )
        )
        whenever(mockDao.insertHoldings(any())).thenReturn(Unit)

        val result = repository.fetchHoldings()

        assertEquals(holdingModels, result)
        verify(mockDao).insertHoldings(eq(holdingEntities))
    }

    @Test
    fun `fetchHoldings falls back to cache on error`() = runTest {
        whenever(mockApi.fetchHoldings()).thenThrow(RuntimeException("No internet"))
        whenever(mockDao.getAllHoldings()).thenReturn(
            listOf(
                HoldingEntity("TCS", 10, 3600.0, 3500.0, 3550.0),
                HoldingEntity("INFY", 5, 1500.0, 1400.0, 1480.0)
            )
        )

        val result = repository.fetchHoldings()

        assert(result.isNotEmpty())
        verify(mockDao).getAllHoldings()
    }
}
