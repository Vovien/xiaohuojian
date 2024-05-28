package com.tubewiki.home.bean.hospital.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class HospitalDoctorKeyWork(
    @SerializedName("keywords") var keywords: ArrayList<KeyWord> = arrayListOf()
) : Parcelable {
    @Keep
    @Parcelize
    data class KeyWord(
        @SerializedName("id") var id: Int = 0,
        @SerializedName("keyword") var keyword: String = "",
    ) : Parcelable
}