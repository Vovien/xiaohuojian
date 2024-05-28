package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 微信小程序跳转数据类型
 * @author MilkCoder
 * @date 2023/11/1
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class WxIdentity(
    @SerializedName("page_type")
    val page_type: String = "",
    @SerializedName("group_name")
    val group_name: String = "",
    @SerializedName("need_pregnancy_type")
    val need_pre_status: Int = 0
) : Parcelable
