package com.jmbon.minitools.base.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class ToolInfoBean(
    @SerializedName("tool")
    var tool: Tool = Tool()
) : Parcelable {
    @Keep
    @Parcelize
    data class Tool(
        @SerializedName("icon")
        var icon: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("is_collect")
        var isCollect: Boolean = false,
        @SerializedName("name")
        var name: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("tool_id")
        var toolId: String = ""
    ) : Parcelable
}