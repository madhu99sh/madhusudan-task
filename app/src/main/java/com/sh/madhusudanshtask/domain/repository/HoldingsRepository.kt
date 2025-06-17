package com.sh.madhusudanshtask.domain.repository

import com.sh.madhusudanshtask.domain.model.Holding

interface HoldingsRepository {
    suspend fun fetchHoldings(): List<Holding>
}