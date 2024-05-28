package com.jmbon.minitools.report.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class FertilityGroupChatListBean(
    val icon: String = "",
    val title: String = "",
    val description: String = "",
    val group_list: List<FertilityGroupChatBean> = listOf()
) : Parcelable

@Keep
@Parcelize
data class FertilityGroupChatBean(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val cover: String = "",
    @SerializedName("item_type")
    val itemType: String = "",
    val identity: String = "",
    val txt: String = ""
) : Parcelable