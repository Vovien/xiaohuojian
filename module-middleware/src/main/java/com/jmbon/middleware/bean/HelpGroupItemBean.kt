package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/******************************************************************************
 * Description: 好孕互助群数据结构
 *
 * Author: jhg
 *
 * Date: 2023/9/8
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class HelpGroupBean(
    @SerializedName("group_list")
    val circle_list: List<HelpGroupItemBean>? = listOf(),
    @SerializedName("group_category_name")
    val group_type_title: String = ""
) : Parcelable

@Keep
@Parcelize
data class HelpGroupItemBean(
    @SerializedName("name")
    val title: String = "",
    val cover: String = "",
    val txt: String = "",
    val color: String = "",
    val item_type: String = "",
    val identity: String = "",
    @SerializedName("member_avatar")
    val avatars: List<String>? = listOf()
) : Parcelable