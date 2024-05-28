package com.jmbon.middleware.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jmbon.middleware.bury.db.BuryDao
import com.jmbon.middleware.bury.db.BuryPointInfo

@Database(
    entities = [
        BuryPointInfo::class
    ], version = 1, exportSchema = false
)
@TypeConverters(value = [DbTypeConverter::class])
abstract class Database : RoomDatabase() {
    abstract val buryDao: BuryDao
}