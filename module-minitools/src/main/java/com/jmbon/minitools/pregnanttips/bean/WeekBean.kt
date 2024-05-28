package com.jmbon.minitools.pregnanttips.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class WeekBean(
    @SerializedName("user_product_date")
    var userProductDate: String = "",
    @SerializedName("weeks")
    var weeks: MutableList<String> = mutableListOf()
) : Parcelable