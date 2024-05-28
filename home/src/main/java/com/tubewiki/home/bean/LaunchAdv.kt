package com.tubewiki.home.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 启动广告
 * @author MilkCoder
 * @date 2023/6/25
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class LaunchAdv(
    @SerializedName("msg")
    var msg: String = "",
    @SerializedName("code")
    var code: Int = 0,
    val adv: Adv? = Adv()
) : Parcelable {

    @Keep
    @Parcelize
    data class Adv(
        val cover: String = "",
        @SerializedName("item_type")
        val itemType: String = "",
        val identity: String = ""
    ) : Parcelable

}