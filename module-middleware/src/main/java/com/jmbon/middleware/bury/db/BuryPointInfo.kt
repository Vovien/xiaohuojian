package com.jmbon.middleware.bury.db

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
@Entity
data class BuryPointInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    /**
     * 事件类型
     */
    var event_id: String = "",
    /**
     * 事件发生的时间, 时间戳格式
     */
    var timestamp: Long = System.currentTimeMillis(),
    /**
     * 事件对应的参数, Json格式
     */
    var params: Map<String, String?>? = mutableMapOf()
) : Parcelable