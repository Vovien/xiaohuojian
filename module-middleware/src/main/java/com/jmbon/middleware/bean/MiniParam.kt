package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 小程序参数
 * @author MilkCoder
 * @date 2023/10/30
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class MiniParam(
    @SerializedName("path") var path: String = "",
    @SerializedName("wechat_mini_program_id") var miniProgramId: String = "",
) : Parcelable
