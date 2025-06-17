package com.sh.madhusudanshtask.data.repository

import com.sh.madhusudanshtask.NoInternetException
import com.sh.madhusudanshtask.data.local.HoldingDao
import com.sh.madhusudanshtask.data.local.toEntity
import com.sh.madhusudanshtask.data.local.toModel
import com.sh.madhusudanshtask.data.remote.service.HoldingsApiService
import com.sh.madhusudanshtask.domain.model.Holding
import com.sh.madhusudanshtask.domain.repository.HoldingsRepository
import kotlinx.io.IOException
import javax.inject.Inject

class HoldingsRepositoryImpl @Inject constructor(
    private val api: HoldingsApiService,
    private val holdingDao: HoldingDao,
) : HoldingsRepository {
    override suspend fun fetchHoldings(): List<Holding> {
        return try {
            val response = api.fetchHoldings()
            holdingDao.insertHoldings(response.data.userHolding.map { it.toEntity() })
            response.data.userHolding.map { it.toModel() }
        } catch (e: IOException) {
            if (holdingDao.getAllHoldings().isNotEmpty()) {
                holdingDao.getAllHoldings().map { it.toModel() }
            } else {
                throw NoInternetException("No internet connection and no cached data")
            }
        } catch (e: Exception) {
            holdingDao.getAllHoldings().map { it.toModel() }
        }
    }
}
