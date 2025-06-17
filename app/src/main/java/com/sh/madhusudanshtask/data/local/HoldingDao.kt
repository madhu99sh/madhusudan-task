package com.sh.madhusudanshtask.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sh.madhusudanshtask.data.local.model.HoldingEntity

@Dao
interface HoldingDao {
    @Query("SELECT * FROM holdings")
    suspend fun getAllHoldings(): List<HoldingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoldings(holdings: List<HoldingEntity>)

}