package com.sh.madhusudanshtask.di

import com.sh.madhusudanshtask.data.local.HoldingDao
import com.sh.madhusudanshtask.data.remote.service.HoldingsApiService
import com.sh.madhusudanshtask.data.repository.HoldingsRepositoryImpl
import com.sh.madhusudanshtask.domain.repository.HoldingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideHoldingsRepository(
        api: HoldingsApiService,
        holdingDao: HoldingDao
    ): HoldingsRepository =
        HoldingsRepositoryImpl(api, holdingDao)
}