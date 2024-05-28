package com.jmbon.middleware.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ToolsCollectBean(
    @SerializedName("is_collected")
    var is_collected: Boolean = false
):Parcelable