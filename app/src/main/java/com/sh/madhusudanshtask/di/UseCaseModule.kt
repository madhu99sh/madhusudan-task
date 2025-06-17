package com.sh.madhusudanshtask.di

import com.sh.madhusudanshtask.domain.repository.HoldingsRepository
import com.sh.madhusudanshtask.domain.usecase.CalculateHoldingsUseCase
import com.sh.madhusudanshtask.domain.usecase.GetHoldingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetHoldingsUseCase(repository: HoldingsRepository): GetHoldingsUseCase =
        GetHoldingsUseCase(repository)

    @Provides
    fun provideCalculateHoldingsUseCase(): CalculateHoldingsUseCase = CalculateHoldingsUseCase()


}