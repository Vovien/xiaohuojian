package com.jmbon.middleware.utils

import androidx.room.Room
import com.apkdv.mvvmfast.utils.context
import com.jmbon.middleware.extend.ioThreadExecutor

/**
 * @author MilkCoder
 * @date 2023/9/21
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
val database by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    Room.databaseBuilder(context, Database::class.java, "tubewiki.db")
        .fallbackToDestructiveMigration()
        .setQueryExecutor(ioThreadExecutor)
        .fallbackToDestructiveMigration()
        .build()
}