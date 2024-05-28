package com.jmbon.minitools.tubetest.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.android.exoplayer2.C

@Keep
@Parcelize
data class BaseInfoBean(
    @SerializedName("ages")
    var ages: MutableList<SelectBean> = mutableListOf(),
    @SerializedName("birth_numbers")
    var birthNumbers: MutableList<SelectBean> = mutableListOf(),
    @SerializedName("citys")
    var citys: Citys = Citys(),
    @SerializedName("demands")
    var demands: MutableList<SelectBean> = mutableListOf(),
    @SerializedName("fallopians")
    var fallopians: MutableList<SelectBean> = mutableListOf(),
    @SerializedName("heights")
    var heights: MutableList<SelectBean> = mutableListOf(),
    @SerializedName("ovarys")
    var ovarys: MutableList<SelectBean> = mutableListOf(),
    @SerializedName("pregnancy_numbers")
    var pregnancyNumbers: MutableList<SelectBean> = mutableListOf(),
    @SerializedName("problems")
    var problems: MutableList<SelectBean> = mutableListOf(),
    @SerializedName("programmes")
    var programmes: MutableList<SelectBean> = mutableListOf(),
    @SerializedName("sperms")
    var sperms: MutableList<SelectBean> = mutableListOf(),
    @SerializedName("tubetest_numbers")
    var tubetestNumbers: MutableList<SelectBean> = mutableListOf(),
    @SerializedName("tubetest_types")
    var tubetestTypes: MutableList<SelectBean> = mutableListOf(),
    @SerializedName("uteruss")
    var uteruss: MutableList<SelectBean> = mutableListOf(),
    @SerializedName("weights")
    var weights: MutableList<SelectBean> = mutableListOf()
) : Parcelable {
    @Keep
    @Parcelize
    data class SelectBean(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("select_value")
        var selectValue: Int = 0,
        @SerializedName("title")
        var title: String = "",
        @SerializedName("type")
        var type: Int = 0
    ) : Parcelable


    @Keep
    @Parcelize
    data class Citys(
        @SerializedName("1")
        var china: City = City(),
        @SerializedName("2")
        var us: City = City(),
        @SerializedName("3")
        var jpz: City = City(),
        @SerializedName("4")
        var tg: City = City(),
        @SerializedName("448")
        var wkl: City = City(),
        @SerializedName("5")
        var japan: City = City(),
        @SerializedName("6")
        var mlxy: City = City(),
        @SerializedName("7")
        var gljy: City = City(),
        @SerializedName("8")
        var russion: City = City(),
    ) : Parcelable
    @Keep
    @Parcelize
    data class City(
        @SerializedName("children")
        var children: MutableList<City> = mutableListOf(),
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("name")
        var name: String = "",
        @SerializedName("pid")
        var pid: Int = 0,
        @SerializedName("title")
        var title: String = ""
    ) : Parcelable

}