package com.sh.madhusudanshtask.di

import android.content.Context
import androidx.room.Room
import com.sh.madhusudanshtask.data.local.AppDatabase
import com.sh.madhusudanshtask.data.local.HoldingDao
import com.sh.madhusudanshtask.utils.NetworkMonitor
import com.sh.madhusudanshtask.utils.RealNetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideUserDao(db: AppDatabase): HoldingDao = db.holdingDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_db").build()

    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor =
        RealNetworkMonitor(context)
}
