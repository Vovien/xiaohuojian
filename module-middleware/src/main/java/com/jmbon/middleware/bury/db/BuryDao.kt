package com.jmbon.middleware.bury.db

import androidx.room.*
import com.jmbon.middleware.dao.LDao

@Dao
interface BuryDao : LDao<BuryPointInfo> {

    @Transaction
    @Query("SELECT * FROM BuryPointInfo")
    suspend fun getAllRecords(): List<BuryPointInfo>?

    @Query("SELECT COUNT(*) FROM BuryPointInfo")
    suspend fun getCount(): Int
}