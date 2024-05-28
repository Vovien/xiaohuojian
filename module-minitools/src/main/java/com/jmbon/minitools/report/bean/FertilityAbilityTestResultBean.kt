package com.jmbon.minitools.report.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.jmbon.minitools.tubetest.bean.UserList
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName


/******************************************************************************
 * Description: 生育力自测结果
 *
 * Author: jhg
 *
 * Date: 2023/4/21
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class FertilityAbilityTestResultBean(
    val id: Int = 0,
    val result: String = "",
    val score: Double = 0.0,
    val image: String = "",
    val user_list: List<UserList> = listOf(),
    val button: Button? = null
) : Parcelable

@Keep
@Parcelize
data class Button(
    @SerializedName("identity")
    val identity: String = "",
    @SerializedName("item_type")
    val itemType: String = "",
    @SerializedName("title")
    val title: String = ""
) : Parcelable