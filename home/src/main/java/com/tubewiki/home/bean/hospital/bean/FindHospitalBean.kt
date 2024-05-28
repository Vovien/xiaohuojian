package com.tubewiki.home.bean.hospital.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class FindHospitalBean(

    @SerializedName("hospitals")
    var hospitals: MutableList<HospitalBean> = mutableListOf(),
    @SerializedName("recommend_hospitals")
    var recommendHospitals: MutableList<HospitalBean> = mutableListOf(),
) : Parcelable