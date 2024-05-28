package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class GeneralConfBean(
    @SerializedName("app_introduce")
    var appIntroduce: String = "",
    @SerializedName("dialog_title")
    var dialogTitle: String = "",
    @SerializedName("robot_avatar")
    var robotAvatar: String = "",
    /**
     * 是否开启审核模式：0否   1是
     */
    @SerializedName("examine_switch")
    var isAuditMode: Int = 0
) : Parcelable {
    @Keep
    @Parcelize
    data class Tag(
        @SerializedName("type")
        var type: Int = 0,
        @SerializedName("labelName")
        var labelName: String = "",

        ) : Parcelable
}