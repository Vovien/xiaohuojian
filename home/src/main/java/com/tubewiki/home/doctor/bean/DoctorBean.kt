package com.tubewiki.home.doctor.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 医生
 * @author MilkCoder
 * @date 2023/7/19
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class DoctorBean(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("msg")
    val msg: String = "",
    val total: Int = 0,
    val doctors: MutableList<LocalDoctor> = mutableListOf(),
    @SerializedName("page_count")
    val page_count: Int = 0,
    val doctor_names: String = "",
    val city_name: String = ""
) : Parcelable

@Keep
@Parcelize
data class LocalDoctor(
    @SerializedName("avatar_file")
    val avatarFile: String = "",
    @SerializedName("column_ids")
    val columnIds: String = "",
    @SerializedName("column_names")
    val columnNames: String = "",
    @SerializedName("columns")
    val columns: List<Column> = listOf(),
    @SerializedName("consult_count")
    val consultCount: Int = 0,
    @SerializedName("department")
    val department: String = "",
    @SerializedName("department_id")
    val departmentId: Int = 0,
    @SerializedName("doctor_descript")
    val doctorDescript: String = "",
    @SerializedName("doctor_id")
    val doctorId: Int = 0,
    @SerializedName("honor")
    val honor: String = "",
    @SerializedName("hospital_id")
    val hospitalId: Int = 0,
    @SerializedName("hospital_name")
    val hospitalName: String = "",
    @SerializedName("is_auth")
    val isAuth: Boolean = false,
    @SerializedName("label_ids")
    val labelIds: String = "",
    @SerializedName("labels")
    val labels: List<Label> = listOf(),
    @SerializedName("name")
    val name: String = "",
    @SerializedName("position")
    val position: String = "",
    @SerializedName("price")
    val price: Int = 0,
    @SerializedName("star")
    val star: Int = 0,
    @SerializedName("uid")
    val uid: Int = 0
) : Parcelable{
    val columnStr:String
        get() = "擅长：$columnNames"

    val starStr:String
        get() = "${star.toDouble()}"

    val consultStr:String
        get() = "咨询量${consultCount}"
}

@Keep
@Parcelize
data class Label(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("label")
    val label: String = ""
) : Parcelable


@Keep
@Parcelize
data class Column(
    @SerializedName("column_id")
    val columnId: Int = 0,
    @SerializedName("column_name")
    val columnName: String = ""
) : Parcelable
