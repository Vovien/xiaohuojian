package com.tubewiki.home.bean.hospital.bean

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/**
 * 医院列表
 * @author MilkCoder
 * @date 2023/7/21
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class AllHospitalBean(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("hospitals")
    val hospitals: MutableList<AllHospital> = mutableListOf(),
    @SerializedName("msg")
    val msg: String = "",
    @SerializedName("page_count")
    val pageCount: Int = 0,
    @SerializedName("total")
    val total: Int = 0
) : Parcelable

@Keep
@Parcelize
data class AllHospital(
    @SerializedName("address")
    val address: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("city_id")
    val cityId: Int = 0,
    @SerializedName("distance")
    val distance: String = "",
    @SerializedName("doctor_count")
    val doctorCount: Int = 0,
    @SerializedName("hospital_name")
    val hospitalName: String = "",
    @SerializedName("hospital_short_name")
    val hospitalShortName: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("level_name")
    val levelName: String = "",
    @SerializedName("lng")
    val lng: Double = 0.0,
    @SerializedName("logo")
    val logo: String = "",
    @SerializedName("province")
    val province: String = "",
    @SerializedName("province_id")
    val provinceId: Int = 0
) : Parcelable{

    val doctorCountStr:String
        get() = "${doctorCount}位专家"

    val locationStr:String
        get() = if (province == city) {
            ""
        } else {
            province
        }.run {
            if (address.contains(city)) {
                "${this}${address}"
            } else {
                "${this}${city}${address}"
            }
        }
}