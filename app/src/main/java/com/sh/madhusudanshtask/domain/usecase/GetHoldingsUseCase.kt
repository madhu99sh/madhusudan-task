package com.sh.madhusudanshtask.domain.usecase

import com.sh.madhusudanshtask.ApiResult
import com.sh.madhusudanshtask.domain.model.Holding
import com.sh.madhusudanshtask.domain.repository.HoldingsRepository
import javax.inject.Inject


class GetHoldingsUseCase @Inject constructor(
    private val repository: HoldingsRepository
) {

    suspend operator fun invoke(): ApiResult<List<Holding>> {
        return try {
            val holdings = repository.fetchHoldings()
            ApiResult.Success(holdings)
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}
