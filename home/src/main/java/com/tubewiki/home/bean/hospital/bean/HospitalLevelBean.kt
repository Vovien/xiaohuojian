package com.tubewiki.home.bean.hospital.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class HospitalLevelBean(
    @SerializedName("levels", alternate = ["level_list"])
    val level_list: List<HospitalLevelItemBean> = listOf()
) : Parcelable

@Keep
@Parcelize
data class HospitalLevelItemBean(
    val id: Int = 0,
    val level_name: String = "",
    var isSelected: Boolean = false,
) : Parcelable