package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class FertilityAbilityTestBean(
    val category_list: List<TestFormBean>? = listOf()
) : Parcelable

@Keep
@Parcelize
data class Person(
    val id: Int = 0,
    val title: String = "",
    val logo: String = "",
    val desc: String = "",
    val form: ArrayList<Form>? = arrayListOf()
) : Parcelable

@Keep
@Parcelize
data class TestFormBean(
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("form")
    val form: ArrayList<Form>? = arrayListOf(),
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("logo")
    val logo: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("source")
    val source: String = ""
) : Parcelable

@Keep
@Parcelize
data class Form(
    @SerializedName("content")
    val content: List<ItemAnswer>? = listOf(),
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type_id")
    val typeId: Int = 0
) : Parcelable

@Keep
@Parcelize
data class ItemAnswer(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("val")
    val `val`: Int = 0
) : Parcelable