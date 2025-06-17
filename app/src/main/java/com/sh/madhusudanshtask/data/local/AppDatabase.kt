package com.sh.madhusudanshtask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sh.madhusudanshtask.data.local.model.HoldingEntity


@Database(entities = [HoldingEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun holdingDao(): HoldingDao
}
