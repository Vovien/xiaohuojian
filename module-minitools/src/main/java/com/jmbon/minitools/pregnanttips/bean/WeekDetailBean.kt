package com.jmbon.minitools.pregnanttips.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class WeekDetailBean(
    @SerializedName("adv")
    var adv: Adv = Adv(),
    @SerializedName("days")
    var days: List<Day> = listOf(),
    @SerializedName("user_product_date")
    var userProductDate: String = "",
    @SerializedName("week")
    var week: Week = Week()
) : Parcelable {
    @Keep
    @Parcelize
    data class Adv(
        @SerializedName("covers")
        var covers: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("tool_id")
        var toolId: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class Day(
        @SerializedName("birth_days")
        var birthDays: Int = 0,
        @SerializedName("date")
        var date: String = "",
        @SerializedName("day")
        var day: Int = 0,
        @SerializedName("description")
        var description: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("week_id")
        var weekId: Int = 0,
        @SerializedName("week_str")
        var weekStr: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class Week(
        @SerializedName("advice")
        var advice: String = "",
        @SerializedName("attention")
        var attention: String = "",
        @SerializedName("body_change")
        var bodyChange: String = "",
        @SerializedName("color_doppler_img")
        var colorDopplerImg: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("emotional_change")
        var emotionalChange: String = "",
        @SerializedName("fruits_img")
        var fruitsImg: String = "",
        @SerializedName("height")
        var height: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("weeks")
        var weeks: Int = 0,
        @SerializedName("weight")
        var weight: String = ""
    ) : Parcelable
}