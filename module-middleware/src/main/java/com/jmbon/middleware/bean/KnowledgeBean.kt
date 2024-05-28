package com.jmbon.middleware.bean

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/**
 * 知识数据
 * @author MilkCoder
 * @date 2023/10/24
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class KnowledgeBean(
    @SerializedName("recommend_experience")
    val articleList: MutableList<ColumnArticles> = mutableListOf(),
    @SerializedName("card")
    val card: Card = Card()
) : Parcelable

@Keep
@Parcelize
data class Card(
    @SerializedName("bg_img")
    val bgImg: String = "",
    @SerializedName("button_txt")
    val buttonTxt: String = "",
    @SerializedName("person_img")
    val personImg: String = "",
    @SerializedName("rand_user")
    val randUser: List<UserList> = listOf(),
    @SerializedName("text_list")
    val textList: List<String> = listOf(),
    @SerializedName("title_img")
    val titleImg: String = "",
    @SerializedName("item_type")
    var itemType: String = "",
    @SerializedName("identity")
    var identity: String = ""
) : Parcelable {
    val strOne: String
        get() = textList.getOrNull(0) ?: "省邮费"
    val strTwo: String
        get() = textList.getOrNull(1) ?: "中文客服"
    val strThree: String
        get() = textList.getOrNull(2) ?: "省押金"
    val strFour: String
        get() = textList.getOrNull(3) ?: "权限账号"
}