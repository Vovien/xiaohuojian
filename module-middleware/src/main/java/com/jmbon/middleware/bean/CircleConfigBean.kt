package com.jmbon.middleware.bean

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
@Parcelize
data class CircleConfigBean(
    @SerializedName("recommend_group_list")
    val circleList: MutableList<CircleGroup>? = mutableListOf(),
    @SerializedName("two_group_category")
    val group_type: List<CircleItemConfigBean>? = listOf()
) : Parcelable

@Keep
@Parcelize
data class CircleGroup(
    @SerializedName("assistant")
    val assistant: String = "",
    @SerializedName("cover")
    val cover: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("identity")
    val identity: String = "",
    @SerializedName("item_type")
    val itemType: String = "",
    @SerializedName("member_avatar")
    val memberAvatar: List<String> = listOf(),
    @SerializedName("member_count")
    val memberCount: Int = 0,
    @SerializedName("name")
    val name: String = ""
) : Parcelable

@Keep
@Parcelize
data class CircleItemConfigBean(
    @SerializedName("cate_id")
    val group_type: Int = 0,
    @SerializedName("name")
    val title: String = "",
    val sub_title: String = "",
    val icon: String = ""
) : Parcelable