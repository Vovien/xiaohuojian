package com.tubewiki.home.doctor.bean
import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/**
 * 医生擅长bean
 * @author MilkCoder
 * @date 2023/7/20
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class ColumnBean(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("msg")
    val msg: String = "",
    @SerializedName("territory")
    val territory: List<Territory> = listOf()
) : Parcelable

@Keep
@Parcelize
data class Territory(
    @SerializedName("column_id")
    val columnId: Int = 0,
    @SerializedName("column_name")
    val columnName: String = "",
    var isSelected: Boolean = false,
) : Parcelable